package org.gsm.oneapi.server.location;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mife.sandbox.model.LocationImpl;
import mife.sandbox.model.entities.Locationparam;
import mife.sandbox.model.entities.ManageNumber;
import mife.sandbox.model.entities.User;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.dialog.mife.responsebean.location.TerminalLocation;
import org.dialog.mife.responsebean.location.TerminalLocationList;
import org.gsm.oneapi.responsebean.RequestError;
import org.gsm.oneapi.server.OneAPIServlet;
import static org.gsm.oneapi.server.OneAPIServlet.BAD_REQUEST;
import static org.gsm.oneapi.server.OneAPIServlet.OK;
import static org.gsm.oneapi.server.OneAPIServlet.getProfileIdFromRequest;
import static org.gsm.oneapi.server.OneAPIServlet.sendError;
import static org.gsm.oneapi.server.OneAPIServlet.sendJSONResponse;
import org.gsm.oneapi.server.ValidationRule;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementing the OneAPI function for locating one or more mobile
 * terminals
 */
public class LocationServlet extends OneAPIServlet {

    static Logger logger = Logger.getLogger(LocationServlet.class);

    private static final long serialVersionUID = 68103504439958479L;

    public LocationImpl locationImpl;

    public void init() throws ServletException {
        logger.debug("LocationServlet initialised");
    }

    private final String[] validationRules = {"location","1","queries", "location"};

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        dumpRequestDetails(request, logger);

        locationImpl = new LocationImpl();

        String sandboxusr = request.getHeader("sandbox");
        logger.debug("Sandbox user : " + sandboxusr);
        User user = null;
        if (sandboxusr == null) {
            sandboxusr = getProfileIdFromRequest(request);
        }

        user = locationImpl.getUser(sandboxusr);

        String[] requestParts = getRequestParts(request);

        if (validateRequest(request, response, requestParts, validationRules)) {

            /*
             * Decode the service parameters - in this case it is an HTTP GET request 
             */
            String address = nullOrTrimmed(request.getParameter("address").toString().replace(" ", "")); //.replace("tel:", "tel:+"));
            Double requestedAccuracy = 0.0;
            logger.debug("requestedAccuracy in get parameter = " + request.getParameter("requestedAccuracy"));
            if (request.getParameter("requestedAccuracy") != null && request.getParameter("requestedAccuracy") != "") {
                try {
                    requestedAccuracy = Double.parseDouble(request.getParameter("requestedAccuracy"));

                    logger.debug("address = " + address);
                    logger.debug("requestedAccuracy = " + requestedAccuracy);

                    ValidationRule[] rules = {
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_LOC_ACCURACY, "requestedAccuracy", requestedAccuracy),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL, "address", address)
                    };

                    if (checkRequestParameters(response, rules)) {

                        /*
                         * Generate response
                         */
                        String userNum = getLastMobileNumber(address);
                        ManageNumber wlnumber = locationImpl.getWhitelisted(user.getId(), userNum);

                        if (wlnumber != null) {

                            //Temp status variable
                            Locationparam locparam = locationImpl.queryLocationParam(user.getId());

                            if (locparam != null) {

                                if (locparam.getLocationRetrieveStatus().equals("Retrieved")) {
                                    logger.debug("Location retrieve status : Retrieved");
                                    locationImpl.saveTransaction(address, requestedAccuracy, "Retrieved", user);

                                    TerminalLocationList objTerminalLocationList = new TerminalLocationList();

                                    TerminalLocation objTerminalLocation = new TerminalLocation();
                                    objTerminalLocation.setAddress(address);

                                    TerminalLocation.CurrentLocation objCurrentLocation = new TerminalLocation.CurrentLocation();
                                    objCurrentLocation.setAccuracy(requestedAccuracy);
                                    objCurrentLocation.setAltitude(Double.parseDouble(locparam.getAltitude()));
                                    objCurrentLocation.setLatitude(Double.parseDouble(locparam.getLatitude()));
                                    objCurrentLocation.setLongitude(Double.parseDouble(locparam.getLongitude()));

                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date currentDate = new Date();
                                    objCurrentLocation.setTimestamp(dateFormat.format(currentDate));

                                    objTerminalLocation.setCurrentLocation(objCurrentLocation);
                                    objTerminalLocation.setLocationRetrievalStatus("Retrieved");

                                    objTerminalLocationList.setTerminalLocation(objTerminalLocation);

                                    ObjectMapper mapper = new ObjectMapper();
                                    String strResponse = "{\"terminalLocationList\":" + mapper.writeValueAsString(objTerminalLocationList) + "}";
                                    String jsonResponse = removeNullPartsInResponse(strResponse);
                                    logger.debug("Response JSON: " + jsonResponse);

                                    sendJSONResponse(response, jsonResponse, OK, null);
                                } else if (locparam.getLocationRetrieveStatus().equals("NotRetrieved")) {
                                    logger.debug("Location retrieve status : NotRetrieved");
                                    locationImpl.saveTransaction(address, requestedAccuracy, "NotRetrieved", user);

                                    TerminalLocationList objTerminalLocationList = new TerminalLocationList();

                                    TerminalLocation objTerminalLocation = new TerminalLocation();
                                    objTerminalLocation.setAddress(address);
                                    objTerminalLocation.setLocationRetrievalStatus("NotRetrieved");

                                    objTerminalLocationList.setTerminalLocation(objTerminalLocation);

                                    ObjectMapper mapper = new ObjectMapper();
                                    String strResponse = "{\"terminalLocationList\":" + mapper.writeValueAsString(objTerminalLocationList) + "}";
                                    String jsonResponse = removeNullPartsInResponse(strResponse);
                                    logger.debug("Response JSON: " + jsonResponse);

                                    sendJSONResponse(response, jsonResponse, OK, null);
                                } else if (locparam.getLocationRetrieveStatus().equals("Error")) {

                                    /*locationImpl.saveTransaction(address, requestedAccuracy, "Error", user);

                                     TerminalLocationList objTerminalLocationList = new TerminalLocationList();

                                     TerminalLocation objTerminalLocation = new TerminalLocation();
                                     objTerminalLocation.setAddress(address);

                                     TerminalLocation.RequestError objRequestError = new TerminalLocation.RequestError();
                                     objRequestError.setMessageId("SVC0001");
                                     objRequestError.setText("A service error occurred. %1 %2");
                                     objRequestError.setVariables("Location information is not available for " + address);

                                     objTerminalLocation.setErrorInformation(objRequestError);
                                     objTerminalLocation.setLocationRetrievalStatus("Error");

                                     objTerminalLocationList.setTerminalLocation(objTerminalLocation);

                                     ObjectMapper mapper = new ObjectMapper();
                                     String strResponse = "{\"terminalLocationList\":" + mapper.writeValueAsString(objTerminalLocationList) + "}";
                                     String jsonResponse = removeNullPartsInResponse(strResponse);
                                     logger.debug("Response JSON: " + jsonResponse);

                                     sendJSONResponse(response, jsonResponse, OK, null);*/
                                    logger.debug("Location retrieve status : Error");
                                    sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", address);
                                }
                            } else {

                                /*locationImpl.saveTransaction(address, requestedAccuracy, "Error", user);

                                 TerminalLocationList objTerminalLocationList = new TerminalLocationList();

                                 TerminalLocation objTerminalLocation = new TerminalLocation();
                                 objTerminalLocation.setAddress(address);

                                 TerminalLocation.RequestError objRequestError = new TerminalLocation.RequestError();
                                 objRequestError.setMessageId("SVC0001");
                                 objRequestError.setText("A service error occurred. %1 %2");
                                 objRequestError.setVariables("Location information is not available for " + address);

                                 objTerminalLocation.setErrorInformation(objRequestError);
                                 objTerminalLocation.setLocationRetrievalStatus("Error");

                                 objTerminalLocationList.setTerminalLocation(objTerminalLocation);

                                 ObjectMapper mapper = new ObjectMapper();
                                 String strResponse = "{\"terminalLocationList\":" + mapper.writeValueAsString(objTerminalLocationList) + "}";
                                 String jsonResponse = removeNullPartsInResponse(strResponse);
                                 logger.debug("Response JSON: " + jsonResponse);

                                 sendJSONResponse(response, jsonResponse, OK, null);*/
                                logger.debug("Location parameters are empty");
                                sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", address);
                            }
                        } else {
                            //Throw error for not available in whitelist!                    
                            sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0001", "A service error occurred. Error code is %1", address + " Not Whitelisted");
                        }

                    }
                } catch (Exception e) {
                    logger.error("Location exception " + e);
                    sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "requestedAccuracy");
                }
            } else {
                logger.debug("Requested accuracy is empty, requestedAccuracy : " + requestedAccuracy);
                sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "requestedAccuracy");
            }
        }
    }

    private static String removeNullPartsInResponse(String strResponse) {

        String errorObjectString = "";
        try {
            errorObjectString = strResponse;
            JSONObject j = new JSONObject(errorObjectString);
            JSONObject objTerminalLocationList = (JSONObject) j.get("terminalLocationList");
            JSONObject objTerminalLocation = (JSONObject) objTerminalLocationList.get("terminalLocation");

            if (objTerminalLocation.isNull("errorInformation") && objTerminalLocation.isNull("currentLocation") ) {
                //.out.println("errorInformation is null");
                objTerminalLocation.remove("errorInformation");
                objTerminalLocation.remove("currentLocation");
            } else if (objTerminalLocation.isNull("errorInformation")) {
                objTerminalLocation.remove("errorInformation");
            } else if (objTerminalLocation.isNull("currentLocation")) {
                objTerminalLocation.remove("currentLocation");
            }
            objTerminalLocationList.put("terminalLocation", objTerminalLocation);
            j.put("terminalLocationList", objTerminalLocationList);
            
            errorObjectString = j.toString();
        } catch (Exception ex) {
            //System.out.println("LocationServlet>removeElement: " + ex);
            logger.error("removeNullPartsInResponse " + ex);
        }
        return errorObjectString;
    }
}

package org.gsm.oneapi.server.mobileidapi;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mife.sandbox.model.MobileIdApiImpl;
import mife.sandbox.model.entities.MobileApiEncodeRequest;
import mife.sandbox.model.entities.User;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.gsm.oneapi.responsebean.RequestError;
import org.gsm.oneapi.server.OneAPIServlet;
import org.gsm.oneapi.server.ValidationRule;
import org.json.JSONObject;

/**
 * Servlet implementing the OneAPI function for authorization 
 */
public class MobileIdApiAuthorizationServlet extends OneAPIServlet{
    
    static Logger logger = Logger.getLogger(MobileIdApiAuthorizationServlet.class);
    private final String[] validationRules = {"mobileidapi","1","queries", "mobileidapi"};
    
    //public LocationImpl locationImpl;
    public MobileIdApiImpl mobileIdApiImpl;

    @Override
    public void init() throws ServletException {
        logger.debug("MobileIdApiServlet initialised");
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        dumpRequestDetails(request, logger);

        mobileIdApiImpl = new MobileIdApiImpl();

        String sandboxusr = request.getHeader("sandbox");
        User user = null;
        if (sandboxusr == null) {
            sandboxusr = getProfileIdFromRequest(request);
        }

        user = mobileIdApiImpl.getUser(sandboxusr);

        String[] requestParts = getRequestParts(request);

        if (validateRequest(request, response, requestParts, validationRules)) {

            /*
             * Decode the service parameters - in this case it is an HTTP GET request 
             */

            if ((request.getParameter("key") != null && !request.getParameter("key").equals(""))&&
                (request.getParameter("secret") != null && !request.getParameter("secret").equals(""))) {
                try {
                    
                    String consumerKey = nullOrTrimmed(request.getParameter("key").replace(" ", "")); 
                    String consumerSecret = nullOrTrimmed(request.getParameter("secret").replace(" ", "")); 
                   
                    ValidationRule[] rules = {
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "key", consumerKey),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "secret", consumerSecret),
                        
                    };

                    if (checkRequestParameters(response, rules)) {
                            MobileApiEncodeRequest mobileApiEncodeRequest = mobileIdApiImpl.saveClientAuthCode(consumerKey, consumerSecret);
                            String strResponse = mobileApiEncodeRequest.getAuthCode();
                            String jsonResponse = removeNullPartsInResponse(strResponse);
                            logger.debug("Response JSON: " + jsonResponse);

                            sendJSONResponse(response, jsonResponse, OK, null);
                     
                    } else {                   
                            sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0001", "A service error occurred. Error code is %1", " ");
                        }
                } catch (Exception e) {
                    sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "");
                }
            } else {
                sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0003", "Invalid input value for message part %1", "");
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

            if (objTerminalLocation.get("errorInformation") == null && objTerminalLocation.get("currentLocation") == null) {
                //.out.println("errorInformation is null");
                objTerminalLocation.remove("errorInformation");
                objTerminalLocation.remove("currentLocation");
            } else if (objTerminalLocation.get("errorInformation") == null) {
                objTerminalLocation.remove("errorInformation");
            } else if (objTerminalLocation.get("currentLocation") == null) {
                objTerminalLocation.remove("currentLocation");
            }
            objTerminalLocationList.put("terminalLocation", objTerminalLocation);
            j.put("terminalLocationList", objTerminalLocationList);
            
            errorObjectString = j.toString();
        } catch (Exception ex) {
            System.out.println("MobileIdApiServlet>removeElement: " + ex);
        }
        return errorObjectString;
    }
}

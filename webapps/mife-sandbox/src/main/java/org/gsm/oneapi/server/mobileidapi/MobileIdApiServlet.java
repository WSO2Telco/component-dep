/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.gsm.oneapi.server.mobileidapi;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mife.sandbox.model.LocationImpl;
import mife.sandbox.model.MobileIdApiImpl;
import mife.sandbox.model.entities.Locationparam;
import mife.sandbox.model.entities.ManageNumber;
import mife.sandbox.model.entities.MobileIdApiRequest;
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
public class MobileIdApiServlet extends OneAPIServlet {

    static Logger logger = Logger.getLogger(MobileIdApiServlet.class);

    private static final long serialVersionUID = 68103504439958479L;

    //public LocationImpl locationImpl;
    public MobileIdApiImpl mobileIdApiImpl;

    public void init() throws ServletException {
        logger.debug("MobileIdApiServlet initialised");
    }

    private final String[] validationRules = {"mobileidapi","1","queries", "mobileidapi"};

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


            //logger.debug("sub in get parameter = " + request.getParameter("sub"));
            if ((request.getParameter("sub") != null && request.getParameter("sub") != "")&&
                (request.getParameter("email") != null && request.getParameter("email") != "")&&
                (request.getParameter("name") != null && request.getParameter("name") != "")&&
                (request.getParameter("family_name") != null && request.getParameter("family_name") != "")&&
                (request.getParameter("preferred_username") != null && request.getParameter("preferred_username") != "")&&
                (request.getParameter("given_name") != null && request.getParameter("given_name") != "")) {
                try {
                    
                    String sub = nullOrTrimmed(request.getParameter("sub").toString().replace(" ", "")); 
                    String email = nullOrTrimmed(request.getParameter("email").toString().replace(" ", "")); 
                    String name = nullOrTrimmed(request.getParameter("name").toString().replace(" ", "")); 
                    String family_name = nullOrTrimmed(request.getParameter("family_name").toString().replace(" ", "")); 
                    String preferred_username = nullOrTrimmed(request.getParameter("preferred_username").toString().replace(" ", "")); 
                    String given_name = nullOrTrimmed(request.getParameter("given_name").toString().replace(" ", "")); //.replace("tel:", "tel:+"));                    
                    
                    //requestedAccuracy = Double.parseDouble(request.getParameter("requestedAccuracy"));
                    //logger.debug("address = " + address);

                    ValidationRule[] rules = {
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "sub", sub),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "email", email),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "name", name),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "family_name", family_name),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "preferred_username", preferred_username),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "given_name", given_name)
                    };

                    if (checkRequestParameters(response, rules)) {

                            MobileIdApiRequest apiRequest =mobileIdApiImpl.saveTransaction(sub,email,name,family_name,preferred_username,given_name, user);

                            ObjectMapper mapper = new ObjectMapper();
                            String strResponse = "{\"MobileIdApiRequest\":" + mapper.writeValueAsString(apiRequest) + "}";
                            String jsonResponse = removeNullPartsInResponse(strResponse);
                            logger.debug("Response JSON: " + jsonResponse);

                            sendJSONResponse(response, jsonResponse, OK, null);
                     

                    } else {
                            //Throw error for not available in whitelist!                    
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.gsm.oneapi.server.mobileidapi;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mife.sandbox.model.MobileIdApiImpl;
import mife.sandbox.model.entities.MobileApiEncodeRequest;
import mife.sandbox.model.entities.MobileIdApiRequest;
import mife.sandbox.model.entities.TokenReponse;
import mife.sandbox.model.entities.User;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.gsm.oneapi.responsebean.RequestError;
import org.gsm.oneapi.server.OneAPIServlet;
import org.gsm.oneapi.server.ValidationRule;
import org.json.JSONObject;

/**
 * Servlet implementing the OneAPI function for locating one or more mobile
 * terminals
 */
public class GetRefreshToken extends OneAPIServlet {

    static Logger logger = Logger.getLogger(GetRefreshToken.class);

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

        String sandboxusr = request.getHeader("user");
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

        	String granttype="",username="",password="",scope="";
        	
        	String pContent=request.getParameter("url").split("\\?")[1];
    		String[] pArray= pContent.split("&");                  
			granttype=pArray[0].split("=")[1];
        	username=request.getParameter("username");
			password=request.getParameter("password");
			scope=request.getParameter("scope");
        	
            //logger.debug("sub in get parameter = " + request.getParameter("sub"));
            if ((request.getParameter("url") != null && request.getParameter("url") != "")){
                try {
                    String url = nullOrTrimmed(request.getParameter("url").toString().replace(" ", ""));                     
                    ValidationRule[] rules = {
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "granttype", url)
                    };

                    if (checkRequestParameters(response, rules)) {                 		

                    	
                    		MobileApiEncodeRequest apiRequest =mobileIdApiImpl.getTokenData(granttype,username,password,scope);

                    		TokenReponse reponseObj=new TokenReponse();
                    		reponseObj.setScope("PRODUCT");
                    		reponseObj.setTokenType("bearer");
                    		reponseObj.setExpiresIn("3600");
                    		reponseObj.setRefreshToken(apiRequest.getRefreshToken());
                    		reponseObj.setAccessToken(apiRequest.getAccessToken());
                    		
                            ObjectMapper mapper = new ObjectMapper();
                            String strResponse = "{\"MobileIdApiRequest\":" + mapper.writeValueAsString(reponseObj) + "}";
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

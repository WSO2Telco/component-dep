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
import mife.sandbox.model.entities.AccessTokenResponse;
import mife.sandbox.model.entities.MobileApiEncodeRequest;
import mife.sandbox.model.entities.User;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.gsm.oneapi.responsebean.RequestError;
import org.gsm.oneapi.server.OneAPIServlet;

import static org.gsm.oneapi.server.OneAPIServlet.BAD_REQUEST;
import static org.gsm.oneapi.server.OneAPIServlet.OK;
import static org.gsm.oneapi.server.OneAPIServlet.checkRequestParameters;
import static org.gsm.oneapi.server.OneAPIServlet.getProfileIdFromRequest;
import static org.gsm.oneapi.server.OneAPIServlet.getRequestParts;
import static org.gsm.oneapi.server.OneAPIServlet.nullOrTrimmed;
import static org.gsm.oneapi.server.OneAPIServlet.sendError;
import static org.gsm.oneapi.server.OneAPIServlet.sendJSONResponse;
import static org.gsm.oneapi.server.OneAPIServlet.validateRequest;

import org.gsm.oneapi.server.ValidationRule;
import org.json.JSONObject;


public class MobileIdApiGenerateAccessTokenServlet extends OneAPIServlet {

    static Logger logger = Logger.getLogger(MobileIdApiAuthorizationServlet.class);
    private final String[] validationRules = {"mobileidapi", "1", "queries", "mobileidapi"};

    //public LocationImpl locationImpl;
    public MobileIdApiImpl mobileIdApiImpl;

    public void init() throws ServletException {
        logger.debug("MobileIdApiServlet initialised");
    }

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
            if ((request.getParameter("grant_type") != null && !request.getParameter("grant_type").equals(""))
                    && (request.getParameter("refresh_token") != null && !request.getParameter("refresh_token").equals(""))
                    && (request.getParameter("scope") != null && !request.getParameter("scope").equals(""))
                    && (request.getParameter("authCode") != null && !request.getParameter("authCode").equals(""))) {
                try {

                    String grantType = nullOrTrimmed(request.getParameter("grant_type").replace(" ", ""));
                    String refresh_token = nullOrTrimmed(request.getParameter("refresh_token").replace(" ", ""));
                    String scope = nullOrTrimmed(request.getParameter("scope").replace(" ", ""));
                    String authCode = nullOrTrimmed(request.getParameter("authCode").replace(" ", ""));

                    ValidationRule[] rules = {
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "grant_type", grantType),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "refresh_token", refresh_token),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "scope", scope),
                        new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "authCode", authCode),};

                    if (checkRequestParameters(response, rules)) {
                            MobileApiEncodeRequest mobileApiEncodeRequest = mobileIdApiImpl.getAcccessToken(grantType, refresh_token, scope, authCode);
                            AccessTokenResponse accessTokenResponse = new AccessTokenResponse();
                            accessTokenResponse.setAccess_token(mobileApiEncodeRequest.getAccessToken());
                            accessTokenResponse.setExpires_in(3600);
                            accessTokenResponse.setRefresh_token(mobileApiEncodeRequest.getRefreshToken());
                            accessTokenResponse.setScope("PRODUCTION");
                            accessTokenResponse.setToken_type("bearer");
                            ObjectMapper mapper = new ObjectMapper();
                            String strResponse = "{\"MobileIdApiRequest\":" + mapper.writeValueAsString(accessTokenResponse) +"}";
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

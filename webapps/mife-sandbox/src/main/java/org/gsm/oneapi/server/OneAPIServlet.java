package org.gsm.oneapi.server;

//import com.sun.jersey.core.util.Base64;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.gsm.oneapi.responsebean.RequestError;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Provides constants and utilities to the individual OneAPI servlets
 */
public class OneAPIServlet extends HttpServlet {

    static Logger logger = Logger.getLogger(OneAPIServlet.class);
    public static final long serialVersionUID = -8195763247832284073L;
    public static final int BAD_REQUEST = 400;
    public static final int AUTHENTICATION_FAILURE = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int METHOD_NOT_SUPPORTED = 405;
    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int ACCEPTED = 202;
    public static final int NONAUTHORITATIVE = 203;
    public static final int NOCONTENT = 204;
    public static boolean dumpRequestAndResponse = false;

    /**
     * Ensure the input value is either a null value or a trimmed string
     */
    public static String nullOrTrimmed(String s) {
        String rv = null;
        if (s != null && s.trim().length() > 0) {
            rv = s.trim();
        }
        return rv;
    }

    /**
     * Convert to an integer value
     */
    public static int parseInt(String s) {
        int rv = 0;
        if (s != null && s.trim().length() > 0) {
            if (s.indexOf(",") == -1) {
                try {
                    rv = Integer.parseInt(s.trim());
                } catch (NumberFormatException nfe) {
                }
            } else {
                String[] p = s.trim().split("\\,");
                try {
                    rv = Integer.parseInt(p[0].trim());
                } catch (NumberFormatException nfe) {
                }
            }
        }
        return rv;
    }

    /**
     * Convert to a long value
     */
    public static long parseLong(String s) {
        long rv = 0;
        if (s != null && s.trim().length() > 0) {
            if (s.indexOf(",") == -1) {
                try {
                    rv = Long.parseLong(s.trim());
                } catch (NumberFormatException nfe) {
                }
            } else {
                String[] p = s.trim().split("\\,");
                try {
                    rv = Long.parseLong(p[0].trim());
                } catch (NumberFormatException nfe) {
                }
            }
        }
        return rv;
    }

    public static String getLastMobileNumber(String str) {
        return str.substring(Math.max(0, str.length() - 11));
    }

    public static String getProfileIdFromRequest(HttpServletRequest request) {

        final String JWT_TOKEN = "x-jwt-assertion";
        String authtoken = request.getHeader(JWT_TOKEN);
        String jwtsubs = null;
        System.out.println("AuthToken: " + authtoken);
        if (authtoken != null) {
            String[] jwttoken = authtoken.split("\\.");

            Base64 base64encoder = new Base64();
            byte[] decoded = base64encoder.decode(jwttoken[1].getBytes());
            String jwtbody = new String(decoded);

            JSONObject jwtobj = null;
            try {
                jwtobj = new org.json.JSONObject(jwtbody);
                jwtsubs = jwtobj.get("http://wso2.org/claims/subscriber").toString();
            } catch (JSONException ex) {
                java.util.logging.Logger.getLogger(OneAPIServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("requestedUser: " + jwtsubs);

            //replyData = Getters.getProfileIdByUsername(jwtsubs);
        }
        //Method Body has to implement!
        return jwtsubs;
    }

    public static String getBearerKeyFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("authorization");
        String[] parts = authHeader.split(" ", 2);
        return parts[1];
    }

    /**
     * Convert to a boolean value
     */
    public static boolean isTrue(String s) {
        boolean rv = false;
        if (s != null && (s.trim().equalsIgnoreCase("true") || s.trim().equals("1") || s.trim().equalsIgnoreCase("yes"))) {
            rv = true;
        }
        return rv;
    }

    /**
     * Alternate function name to convert to a boolean value
     */
    public static boolean parseBoolean(String s) {
        return isTrue(s);
    }
    private static Pattern nf1 = Pattern.compile("[\\-\\+]?[0-9]*\\.?[0-9]+");
    private static Pattern nf2 = Pattern.compile("[\\-\\+]?\\.?[0-9]+");

    /**
     * Convert to a double value
     */
    public static double parseDouble(String s) {
        double rv = 0;
        if (s != null && s.trim().length() > 0) {
            try {
                rv = Double.parseDouble(s.trim());
            } catch (NumberFormatException nfe) {
                boolean parsed = false;
                String trimmed = s.trim();
                Matcher m1 = nf1.matcher(trimmed);
                if (m1.find()) {
                    try {
                        String pv = m1.group();
                        rv = Double.parseDouble(pv);
                        parsed = true;
                    } catch (NumberFormatException nfe1) {
                    }
                }
                if (!parsed) {
                    Matcher m2 = nf2.matcher(trimmed);
                    if (m2.find()) {
                        try {
                            String pv = m1.group();
                            rv = Double.parseDouble(pv);
                            parsed = true;
                        } catch (NumberFormatException nfe2) {
                        }
                    }
                }
            }
        }
        return rv;
    }

    /**
     * Do basic URL encoding based on UTF-8
     */
    public static String urlEncode(String s) {
        String rv = s;
        if (s != null) {
            try {
                rv = URLEncoder.encode(s, "UTF-8");
            } catch (UnsupportedEncodingException uee) {
            }
        } else {
            rv = "";
        }
        return rv;
    }

    /**
     * Default servlet initialisation method - should be overridden by the
     * individual servlet
     */
    public void init() throws ServletException {
        logger.info("OneAPIServlet initialised");
    }

    /**
     * By default make HTTP get requests return a not supported error. The
     * individual servlet can replace this if it should be supported.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.info("invoked default doGet method - not supported");
        sendError(response, METHOD_NOT_SUPPORTED, RequestError.SERVICEEXCEPTION, "SVC0001", "HTTP Get Method Is Not Supported", null);
    }

    /**
     * By default make HTTP post requests return a not supported error. The
     * individual servlet can replace this if it should be supported.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.info("invoked default doPost method - not supported");
        sendError(response, METHOD_NOT_SUPPORTED, RequestError.SERVICEEXCEPTION, "SVC0001", "HTTP Post Method Is Not Supported", null);
    }

    /**
     * By default make HTTP delete requests return a not supported error. The
     * individual servlet can replace this if it should be supported.
     */
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.info("invoked default doDelete method - not supported");
        sendError(response, METHOD_NOT_SUPPORTED, RequestError.SERVICEEXCEPTION, "SVC0001", "HTTP Delete Method Is Not Supported", null);
    }

    /**
     * By default make HTTP put requests return a not supported error. The
     * individual servlet can replace this if it should be supported.
     */
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.info("invoked default doPut method - not supported");
        sendError(response, METHOD_NOT_SUPPORTED, RequestError.SERVICEEXCEPTION, "SVC0001", "HTTP Put Method Is Not Supported", null);
    }

    /**
     * By default make HTTP head requests return a not supported error. The
     * individual servlet can replace this if it should be supported.
     */
    public void doHead(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.info("invoked default doHead method - not supported");
        sendError(response, METHOD_NOT_SUPPORTED, RequestError.SERVICEEXCEPTION, "SVC0001", "HTTP Head Method Is Not Supported", null);
    }

    /**
     * By default make HTTP trace requests return a not supported error. The
     * individual servlet can replace this if it should be supported.
     */
    public void doTrace(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.info("invoked default doTrace method - not supported");
        sendError(response, METHOD_NOT_SUPPORTED, RequestError.SERVICEEXCEPTION, "SVC0001", "HTTP Trace Method Is Not Supported", null);
    }

    /**
     * By default make HTTP options requests return a not supported error. The
     * individual servlet can replace this if it should be supported.
     */
    public void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.info("invoked default doOptions method - not supported");
        sendError(response, METHOD_NOT_SUPPORTED, RequestError.SERVICEEXCEPTION, "SVC0001", "HTTP Options Method Is Not Supported", null);
    }

    /**
     * This is a helper to send the formed JSON response
     *
     * @param response
     * @param jsonResponse
     * @param status
     * @param location
     * @throws IOException
     */
    public static void sendJSONResponse(HttpServletResponse response, String jsonResponse, int status, String location) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        if (location != null) {
            response.setHeader("Location", location);
        }

        ServletOutputStream output = response.getOutputStream();
        output.print(jsonResponse);
        output.flush();

        if (dumpRequestAndResponse) {
            System.out.println("JSON response=" + jsonResponse);
        }
    }

    /**
     * This is a helper to send an error response
     *
     * @param response
     * @param errorCode
     * @param errorType
     * @param messageId
     * @param errorText
     * @param errorInformation
     */
    public static void sendError(HttpServletResponse response, int errorCode, int errorType, String messageId, String errorText, String errorInformation) {
        response.setContentType("application/json");

        RequestError error = new RequestError(errorType, messageId, errorText, errorInformation);

        String jsonResponse = "{\"requestError\":" + getErrorJSONString(error) + "}";
        try {
            sendJSONResponse(response, jsonResponse, errorCode, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String getErrorJSONString(RequestError error) {

        ObjectMapper mapper = new ObjectMapper();
        String errorObjectString = "";
        try {
            errorObjectString = mapper.defaultPrettyPrintingWriter().writeValueAsString(error);
            //JSONObject j = new JSONObject(errorObjectString);
            JSONObject innerObj = new JSONObject(error);
            if (innerObj.isNull("serviceException")) {
                //.out.println("serviceException is null");
                innerObj.remove("serviceException");
            } else if (innerObj.isNull("policyException")) {
                //System.out.println("serviceException is null");
                innerObj.remove("policyException");
            }
            //System.out.println(j.toJSONString());
            errorObjectString = innerObj.toString();

        } catch (Exception ex) {
            System.out.println("OneAPIServlet>removeElement: " + ex);
        }
        return errorObjectString;
    }

    /**
     * Get the base URL based on the incoming request
     *
     * @param request
     */
    public static String getRequestHostnameAndContext(HttpServletRequest request) {
        return (request.isSecure() ? "https://" : "http://") + (request.getHeader("x-forwarded-host") != null ? request.getHeader("x-forwarded-host") : request.getHeader("host")) + (request.getContextPath() != null ? request.getContextPath() : "");
    }

    /**
     * Get the slash separated path components from the incoming request
     *
     * @param request
     */
    public static String[] getRequestParts(HttpServletRequest request) {
        String[] requestParts = null;
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            if (pathInfo.startsWith("/")) {
                pathInfo = pathInfo.substring(1);
            }
            requestParts = pathInfo.split("/");
        }
        return requestParts;
    }

    /**
     * Output the request details to the log4j output
     *
     * @param request
     * @param callerLogger
     */
    @SuppressWarnings("unchecked")
    protected void dumpRequestDetails(HttpServletRequest request, Logger callerLogger) {
        String contentType = request.getContentType();
        callerLogger.debug("Requested hostname/application=" + getRequestHostnameAndContext(request));
        callerLogger.debug("Servlet Path=" + request.getServletPath());
        callerLogger.debug("Received contentType=" + contentType);
        callerLogger.debug("Method=" + request.getMethod());
        callerLogger.debug("Query String=" + request.getQueryString());
        callerLogger.debug("Path info=" + request.getPathInfo());

        Enumeration<String> headers = (Enumeration<String>) request.getHeaderNames();
        if (headers != null) {
            while (headers.hasMoreElements()) {
                String header = headers.nextElement();
                callerLogger.debug("Header " + header + " = " + request.getHeader(header));
            }
        }

        Enumeration<String> paramNames = (Enumeration<String>) request.getParameterNames();
        if (paramNames != null) {
            while (paramNames.hasMoreElements()) {
                String param = paramNames.nextElement();
                callerLogger.debug("Parameter " + param + " = " + request.getParameter(param));
            }
        }

    }

    /**
     * Is a starter function to check the request Basic authorization
     *
     * @param request
     * @param response
     */
    public static boolean isAuthorizationFailed(HttpServletRequest request, HttpServletResponse response) {
        boolean failedAuthentication = false;

        String authorizationHeader = request.getHeader("authorization");
        String isApplication = request.getHeader("source");

        logger.debug("Authorization header = " + authorizationHeader);
        logger.debug("Request Source - isApplication = " + isApplication);

        if (authorizationHeader == null) {
            if (isApplication == null) {
                failedAuthentication = false;
            } else {
                failedAuthentication = true;
                sendError(response, AUTHENTICATION_FAILURE, RequestError.SERVICEEXCEPTION, "SVC0003", "Authorization required for use of this service", null);
            }
        } else {

            if (isApplication != null && isApplication.equals("true")) {

                String[] parts = authorizationHeader.split(" ", 2);
                if (parts.length < 2) {

                    failedAuthentication = true;
                    //sendError(response, RequestError.SERVICEEXCEPTION, AUTHENTICATION_FAILURE, "SVC0003", "Authorization incorrectly specified", null);
                    sendError(response, AUTHENTICATION_FAILURE, RequestError.SERVICEEXCEPTION, "SVC0003", "Authorization incorrectly specified", null);
                } else if (parts[0].equalsIgnoreCase("Bearer")) {

                    logger.debug("Using bearer authorization. Token = " + parts[1]);
                    try {
                        /*byte[] decoded = Base64.decode(parts[1]);
                         String usernamePassword = new String(decoded);
                         logger.debug("Username/Password = " + usernamePassword);*/
                        /*
                         * At this point the username and password string is known and could be checked against some form of service access database
                         */
                        if (parts[1].equals("123456")) {

                            failedAuthentication = false;
                            logger.debug("Valid Bearer Token = " + parts[1]);
                        } else {

                            failedAuthentication = true;
                            logger.debug("Invalid Bearer Token = " + parts[1]);
                            //sendError(response, RequestError.SERVICEEXCEPTION, AUTHENTICATION_FAILURE, "SVC0001", "A service error occurred. Error code is %1", "Access failure for API: Invalid Credentials");
                            sendError(response, AUTHENTICATION_FAILURE, RequestError.SERVICEEXCEPTION, "SVC0001", "A service error occurred. Error code is %1", "Access failure for API: Invalid Credentials");
                        }
                        //} catch (Base64DecodingException e) {
                    } catch (Exception e) {

                        failedAuthentication = true;
                        //sendError(response, RequestError.SERVICEEXCEPTION, AUTHENTICATION_FAILURE, "SVC0003", "Base64 Decoding Failure ", null);
                        //sendError(response, RequestError.SERVICEEXCEPTION, AUTHENTICATION_FAILURE, "SVC0003", e.getMessage(), null);
                        sendError(response, AUTHENTICATION_FAILURE, RequestError.SERVICEEXCEPTION, "SVC0003", e.getMessage(), null);
                    }
                } else {

                    failedAuthentication = true;
                    //sendError(response, RequestError.SERVICEEXCEPTION, AUTHENTICATION_FAILURE, "SVC0003", "Authorization scheme not supported ", parts[0]);
                    sendError(response, AUTHENTICATION_FAILURE, RequestError.SERVICEEXCEPTION, "SVC0003", "Authorization scheme not supported ", parts[0]);
                }
            } else {

                String[] parts = authorizationHeader.split(" ", 2);
                if (parts.length < 2) {

                    failedAuthentication = true;
                    //sendError(response, RequestError.SERVICEEXCEPTION, AUTHENTICATION_FAILURE, "SVC0003", "Authorization incorrectly specified", null);     
                    sendError(response, AUTHENTICATION_FAILURE, RequestError.SERVICEEXCEPTION, "SVC0003", "Authorization incorrectly specified", null);
                } else if (parts[0].equalsIgnoreCase("Bearer")) {

                    logger.debug("Using bearer authorization. Token = " + parts[1]);
                    try {

                        failedAuthentication = false;
                        logger.debug("Valid Bearer Token = " + parts[1]);
                    } catch (Exception e) {

                        failedAuthentication = true;
                        //sendError(response, RequestError.SERVICEEXCEPTION, AUTHENTICATION_FAILURE, "SVC0003", e.getMessage(), null);
                        sendError(response, AUTHENTICATION_FAILURE, RequestError.SERVICEEXCEPTION, "SVC0003", e.getMessage(), null);
                    }
                } else {

                    failedAuthentication = true;
                    sendError(response, RequestError.SERVICEEXCEPTION, AUTHENTICATION_FAILURE, "SVC0003", "Authorization scheme not supported ", parts[0]);
                }
            }
        }
        return failedAuthentication;
    }

    /**
     * This function implements initial checks on the format of the requested
     * URL
     *
     * @param request
     * @param response
     * @param requestParts
     * @param validationRules
     * @return true is the request is considered valid
     */
    public static boolean validateRequest(HttpServletRequest request, HttpServletResponse response, String[] requestParts, String[] validationRules) {

        boolean valid = true;

        boolean failedAuthentication = isAuthorizationFailed(request, response);

        if (!failedAuthentication) {
            if (requestParts == null || requestParts.length < validationRules.length) {
                valid = false;
                logger.debug("Request parts length" + requestParts.length);
                logger.debug("Validation rules length" + validationRules.length);
                sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Request is missing required URI components", null);
            } else {
                String errorMessage = null;
                for (int i = 0; i < validationRules.length; i++) {
                    logger.debug("Validation of " + requestParts[i] + " against " + validationRules[i]);
                    if (validationRules[i].equals("*")) {
                        // Still valid
                    } else if (!requestParts[i].equals(validationRules[i])) {
                        if (valid) {
                            errorMessage = validationRules[i] + " at component [" + i + "] ";
                            logger.debug(validationRules[i] + " at component [" + requestParts[i] + "] ");
                            valid = false;
                        } else {
                            errorMessage += ", " + validationRules[i] + " at component [" + i + "] ";
                            logger.debug(validationRules[i] + " at component [" + requestParts[i] + "] ");
                        }
                    }
                }
                if (!valid) {
                    sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Request URI missing required component(s): ", errorMessage);
                }
            }
        } else {
            //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0003", "Failed Authentication: ", request.getHeader("authorization"));
            valid = false;
        }

        return valid;
    }

    /**
     * This function implements the specific parameter validation rules applying
     * to each OneAPI servlet (specifically request parameters).
     *
     * @param response
     * @param rules
     * @return true if all parameter validation rules have been passed
     * @see ValidationRule
     */
    public static boolean checkRequestParameters(HttpServletResponse response, ValidationRule[] rules) {
        boolean valid = true;

        if (rules != null && rules.length > 0) {

            // Pass 1 - check mandatory parameters
            String missingList = null;
            for (int i = 0; i < rules.length; i++) {
                ValidationRule current = rules[i];
                if (ValidationRule.isMandatory(current.validationType)) {
                    boolean missing = false;
                    if (current.parameterValue == null) {
                        missing = true;
                        logger.debug("Parameter " + current.parameterName + " is missing");
                    } else {
                        Object parameterValue = current.parameterValue;
                        if (parameterValue instanceof String) {
                            if (((String) current.parameterValue).trim().length() == 0) {
                                missing = true;
                                logger.debug("Parameter " + current.parameterName + " is missing");
                            }
                        } else if (parameterValue instanceof Double) {
                            // This is ok for the moment
                        } else if (parameterValue instanceof String[]) {
                            String[] sa = (String[]) parameterValue;
                            boolean empty = true;
                            if (sa != null && sa.length > 0) {
                                // See if there is at least one non null string present
                                for (int j = 0; j < sa.length && empty; j++) {
                                    if (sa[j] != null && sa[j].trim().length() > 0) {
                                        empty = false;
                                    }
                                }
                            }
                            if (empty) {
                                missing = true;
                                logger.debug("Parameter " + current.parameterName + " is missing");
                            }
                        } else {
                            logger.warn("Not sure how to validate parameter " + current.parameterName + " type=" + current.parameterValue.getClass().getName());
                        }
                    }
                    if (missing) {
                        if (missingList == null) {
                            missingList = current.parameterName;
                        } else {
                            missingList += "," + current.parameterName;
                        }
                        valid = false;
                    }
                }
            }

            if (!valid) {
                sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Missing mandatory parameter: " + missingList);
            } else {
                logger.debug("Starting second pass");
                // Pass 2 - other validations - stop on the first error 
                for (int i = 0; i < rules.length && valid; i++) {
                    ValidationRule current = rules[i];
                    Object parameterValue = current.parameterValue;
                    switch (current.validationType) {
                        case ValidationRule.VALIDATION_TYPE_MANDATORY_MESSAGE:
                            if (current.specificValue != null && parameterValue instanceof String) {
                                String pv = (String) parameterValue;
                                if (!(pv.equalsIgnoreCase(current.specificValue))) {
                                    sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter " + current.parameterName + " expected " + current.specificValue + " received " + pv);
                                    logger.debug("Parameter " + current.parameterName + " does not match expected value " + current.specificValue);
                                }
                            } else if (parameterValue != null) {

                                String parameterValueString = parameterValue.toString();
                                int msgChaCount = parameterValueString.length();
                                if (msgChaCount > 150) {

                                    valid = false;
                                    sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0280", "Message too long. Maximum length is %1 characters", "150");
                                    logger.debug("Message too long. " + msgChaCount);
                                }
                            }
                            break;

                        case ValidationRule.VALIDATION_TYPE_MANDATORY_CURRENCY:
                            if (current.specificValue != null && parameterValue instanceof String) {
                                String pv = (String) parameterValue;
                                if (!(pv.equalsIgnoreCase(current.specificValue))) {
                                    sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter " + current.parameterName + " expected " + current.specificValue + " received " + pv);
                                    logger.debug("Parameter " + current.parameterName + " does not match expected value " + current.specificValue);
                                }
                            } else if (parameterValue != null) {

                                String paymentCurrency = parameterValue.toString();
                                if (!paymentCurrency.equalsIgnoreCase("LKR") && !paymentCurrency.equalsIgnoreCase("USD")) {

                                    valid = false;
                                    sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0007", "Invalid charging information", "");
                                    logger.debug("Invalid Currency Type. " + paymentCurrency);
                                }
                            }
                            break;
                            
                            case ValidationRule.VALIDATION_TYPE_OPTIONAL_CURRENCY:
                            if (current.specificValue != null && parameterValue instanceof String) {
                                String pv = (String) parameterValue;
                                if (!(pv.equalsIgnoreCase(current.specificValue))) {
                                    sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter " + current.parameterName + " expected " + current.specificValue + " received " + pv);
                                    logger.debug("Parameter " + current.parameterName + " does not match expected value " + current.specificValue);
                                }
                            } else if (parameterValue != null) {

                                String paymentCurrency = parameterValue.toString();
                                if (!paymentCurrency.equalsIgnoreCase("LKR") && !paymentCurrency.equalsIgnoreCase("USD")) {

                                    valid = false;
                                    sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0007", "Invalid charging information", "");
                                    logger.debug("Invalid Currency Type. " + paymentCurrency);
                                }
                            }
                            break;

                        case ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID:
                            // Mandatory will already have been enforced - can just do the validation
                            if (parameterValue != null) {
                                if (parameterValue instanceof String) {
                                    if (!isCorrectlyFormattedNumber((String) parameterValue)) {
                                        //logger.debug("Rejecting phone number " + current.parameterName + " : " + (String) parameterValue);
                                        //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0004", "No valid addresses provided in message part %1", ((String) current.parameterValue));
                                        logger.debug("Rejecting phone number " + current.parameterName + " : " + (String) parameterValue);
                                        sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0271", "endUserId format invalid. Expected format is %1", "tel:+94771211212");
                                        valid = false;
                                    }
                                } else if (parameterValue instanceof String[]) {
                                    String[] sa = (String[]) parameterValue;
                                    if (sa != null && sa.length > 0) {
                                        // See if there is at least one non null string present
                                        for (int j = 0; j < sa.length && valid; j++) {
                                            if (sa[j] != null && sa[j].trim().length() > 0) {
                                                if (!isCorrectlyFormattedNumber(sa[j])) {
                                                    //logger.debug("Rejecting phone number " + current.parameterName + " : " + sa[j]);
                                                    //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0004", "No valid addresses provided in message part %1", sa[j]);
                                                    logger.debug("Rejecting phone number " + current.parameterName + " : " + sa[j]);
                                                    sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0271", "endUserId format invalid. Expected format is %1", "tel:+94771211212");
                                                    valid = false;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            break;

                        case ValidationRule.VALIDATION_TYPE_OPTIONAL_TEL_END_USER_ID:
                            // Mandatory will already have been enforced - can just do the validation
                            if (parameterValue != null) {
                                if (parameterValue instanceof String) {
                                    if (!isCorrectlyFormattedNumber((String) parameterValue)) {
                                        logger.debug("Rejecting phone number " + current.parameterName + " : " + (String) parameterValue);
                                        sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0271", "endUserId format invalid. Expected format is %1", "tel:+94771211212");
                                        valid = false;
                                    }
                                } else if (parameterValue instanceof String[]) {
                                    String[] sa = (String[]) parameterValue;
                                    if (sa != null && sa.length > 0) {
                                        // See if there is at least one non null string present
                                        for (int j = 0; j < sa.length && valid; j++) {
                                            if (sa[j] != null && sa[j].trim().length() > 0) {
                                                if (!isCorrectlyFormattedNumber(sa[j])) {
                                                    logger.debug("Rejecting phone number " + current.parameterName + " : " + sa[j]);
                                                    sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0271", "endUserId format invalid. Expected format is %1", "tel:+94771211212");
                                                    valid = false;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            break;

                        case ValidationRule.VALIDATION_TYPE_MANDATORY:
                            if (current.specificValue != null && parameterValue instanceof String) {
                                String pv = (String) parameterValue;
                                if (!(pv.equalsIgnoreCase(current.specificValue))) {
                                    sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter " + current.parameterName + " expected " + current.specificValue + " received " + pv);
                                    logger.debug("Parameter " + current.parameterName + " does not match expected value " + current.specificValue);
                                    valid = false;
                                }
                            }
                            break;

                        case ValidationRule.VALIDATION_TYPE_MANDATORY_DOUBLE_GT_ZERO:
                        case ValidationRule.VALIDATION_TYPE_OPTIONAL_DOUBLE_GT_ZERO:
                            if (current.parameterValue instanceof Double) {
                                if (((Double) current.parameterValue) <= 0.0) {
                                    valid = false;
                                    //logger.debug("Rejecting double value " + current.parameterName + " : " + ((Double) parameterValue) + " should be > 0");
                                    /*
                                     ***** Comment this and use the new exception message because charge amount need exception type SVC0007
                                     */
                                    //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter " + current.parameterName + " value " + ((Double) current.parameterValue));
                                    sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0007", "Invalid charging information", "");
                                } /*else if (((Double) current.parameterValue) > 2500) {

                                    valid = false;
                                    logger.debug("Invalid charge amount " + current.parameterName + " : " + ((Double) parameterValue) + " should be <= 2500");
                                    sendError(response, FORBIDDEN, RequestError.POLICYEXCEPTION, "POL0254", "The amount exceeds the operator limit for a single charge", "");
                                }*/
                            }
                            break;
                        case ValidationRule.VALIDATION_TYPE_MANDATORY_DOUBLE_GE_ZERO:
                        case ValidationRule.VALIDATION_TYPE_OPTIONAL_DOUBLE_GE_ZERO:
                            if (current.parameterValue instanceof Double) {
                                if (((Double) current.parameterValue) < 0.0) {
                                    valid = false;
                                    logger.debug("Rejecting double value " + current.parameterName + " : " + ((Double) parameterValue) + " should be >= 0");
                                    sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter " + current.parameterName + " value " + ((Double) current.parameterValue));
                                }
                            }
                            break;
                        case ValidationRule.VALIDATION_TYPE_MANDATORY_TEL:
                        case ValidationRule.VALIDATION_TYPE_OPTIONAL_TEL:
                            // Mandatory will already have been enforced - can just do the validation
                            if (parameterValue != null) {
                                if (parameterValue instanceof String) {
                                    if (!isCorrectlyFormattedNumber((String) parameterValue)) {
                                        logger.debug("Rejecting phone number " + current.parameterName + " : " + (String) parameterValue);
                                        sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0004", "No valid addresses provided in message part %1", ((String) current.parameterValue));
                                        valid = false;
                                    }
                                } else if (parameterValue instanceof String[]) {
                                    String[] sa = (String[]) parameterValue;
                                    if (sa != null && sa.length > 0) {
                                        String duplicatedAddress = checkDuplicatedAddress(sa);
                                        if (sa.length > 10) {

                                            logger.debug("No of addresses in array" + sa.length);
                                            sendError(response, FORBIDDEN, RequestError.POLICYEXCEPTION, "POL0003", "Too many addresses specified in message part %1", current.parameterName);
                                            valid = false;
                                        } else if (duplicatedAddress != "false") {

                                            logger.debug("Duplicated addresses in array" + duplicatedAddress);
                                            sendError(response, FORBIDDEN, RequestError.POLICYEXCEPTION, "POL0013", "Duplicated addresses", duplicatedAddress);
                                            valid = false;
                                        } else {
                                            // See if there is at least one non null string present
                                            for (int j = 0; j < sa.length && valid; j++) {
                                                if (sa[j] != null && sa[j].trim().length() > 0) {
                                                    if (!isCorrectlyFormattedNumber(sa[j])) {
                                                        logger.debug("Rejecting phone number " + current.parameterName + " : " + sa[j]);
                                                        sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0004", "No valid addresses provided in message part %1", sa[j]);
                                                        valid = false;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        case ValidationRule.VALIDATION_TYPE_MANDATORY_URL:
                        case ValidationRule.VALIDATION_TYPE_OPTIONAL_URL:
                            // Mandatory will already have been enforced - can just do the validation
                            if (parameterValue != null) {
                                if (parameterValue instanceof String) {
                                    if (!isCorrectlyFormattedURL((String) parameterValue)) {
                                        logger.debug("Bad URL " + current.parameterName + " : " + (String) parameterValue);
                                        sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter " + current.parameterName + " expected URL " + ((String) parameterValue));
                                        valid = false;
                                    }
                                }
                            }
                            break;
                        case ValidationRule.VALIDATION_TYPE_MANDATORY_INT_GE_ZERO:
                        case ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO:
                            if (parameterValue != null) {
                                if (parameterValue instanceof Integer) {
                                    if (((Integer) parameterValue).intValue() < 0) {
                                        logger.debug("Rejecting int value " + current.parameterName + " : " + ((Integer) parameterValue) + " should be >= 0");
                                        sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter " + current.parameterName + " less than zero: " + ((String) parameterValue));
                                        valid = false;
                                    }
                                }
                            }
                            break;
                        case ValidationRule.VALIDATION_TYPE_MANDATORY_INT_GT_ONE:
                        case ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GT_ONE:
                            if (parameterValue != null) {
                                if (parameterValue instanceof Integer) {
                                    if (((Integer) parameterValue).intValue() <= 1) {
                                        logger.debug("Rejecting int value " + current.parameterName + " : " + ((Integer) parameterValue) + " should be > 1");
                                        sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter " + current.parameterName + " must be greater than 1: " + ((String) parameterValue));
                                        valid = false;
                                    }
                                }
                            }
                            break;
                        case ValidationRule.VALIDATION_TYPE_MANDATORY_JSON:
                        case ValidationRule.VALIDATION_TYPE_OPTIONAL_JSON:
                            if (parameterValue != null) {
                                if (parameterValue instanceof String) {
                                    if (!((String) parameterValue).equalsIgnoreCase("JSON")) {
                                        logger.debug("Rejecting parameter " + current.parameterName + " : " + ((String) parameterValue) + " should be 'JSON'");
                                        sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter " + current.parameterName + " expected 'JSON': " + ((String) parameterValue));
                                        valid = false;
                                    }
                                }
                            }
                            break;
                        case ValidationRule.VALIDATION_TYPE_MANDATORY_PAYMENT_CHANNEL:
                        case ValidationRule.VALIDATION_TYPE_OPTIONAL_PAYMENT_CHANNEL:
                            if (parameterValue != null) {
                                if (parameterValue instanceof String) {
                                    if (!(((String) parameterValue).equalsIgnoreCase("WAP") || ((String) parameterValue).equalsIgnoreCase("WEB") || ((String) parameterValue).equalsIgnoreCase("SMS"))) {
                                        logger.debug("Rejecting parameter " + current.parameterName + " : " + ((String) parameterValue) + " should be 'Web', 'Wap' or 'SMS'");
                                        sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter " + current.parameterName + " expected 'Wap', 'Web' or 'SMS': " + ((String) parameterValue));
                                        valid = false;
                                    }
                                }
                            }
                            break;
                        /*case ValidationRule.VALIDATION_TYPE_MANDATORY_LOC_ACCURACY:
                         if (current.parameterValue instanceof Double) {
                         if (((Double) current.parameterValue) <= 0.0) {

                         valid = false;
                         sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "LOC0007", "Invalid charging information", "");
                         } else if (((Double) current.parameterValue) > 2500) {

                         valid = false;
                         logger.debug("Invalid charge amount " + current.parameterName + " : " + ((Double) parameterValue) + " should be <= 2500");
                         sendError(response, FORBIDDEN, RequestError.POLICYEXCEPTION, "LOC0254", "The amount exceeds the operator limit for a single charge", "");
                         }
                         } else {

                         valid = false;
                         logger.debug("Invalid location accuracy " + current.parameterName + " : " + parameterValue);
                         sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", parameterValue.toString());
                         }
                         break;*/
                    }

                }
            }

        }

        logger.debug("Parameters are valid?" + valid);
        return valid;
    }

    /**
     * Utility function to create a java.util.Date object from constituent date/
     * time fields (UTC clock)
     *
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param second
     */
    public java.util.Date makeUTCDateTime(int year, int month, int day, int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    //This validation allows numbers to pass this with or without +
    /*private static final String[] telFormats = {
     "tel\\:\\+[0-9]+", "tel\\:[0-9]+"
     };*/
    //This validation allows numbers to pass this with only +
    private static final String[] telFormats = {
        "tel\\:\\+[0-9]+", "tel\\:[0-9]+"
    };

    /**
     * Check on valid telephone number formats. Extend the regular expression
     * rules in telFormats if needed.
     */
    public static boolean isCorrectlyFormattedNumber(String tel) {
        boolean matched = false;
        if (tel != null) {
            for (int i = 0; i < telFormats.length && !matched; i++) {
                if (tel.matches(telFormats[i])) {
                    matched = true;
                }
                logger.debug("Number=" + tel + " matches regex=" + telFormats[i] + " = " + matched);
            }
        }
        return matched;
    }
    private static final String[] urlFormats = {
        "http\\:\\/\\/.+", "https\\:\\/\\/.+"
    };

    /*
     ****** This method is use to get the country code from address
     
    public static String getCountryCode(String address) throws Exception {

        String[] noParts = address.split("\\+", 2);
        String countryCode = noParts[1].substring(0, 2);
        logger.debug("Country Code = " + countryCode);
        return countryCode;
    }*/

    /*
     ****** This method is used to identify duplicate values in address array
     */
    private static String checkDuplicatedAddress(String[] addressTemp) {
        Set<String> addressValueSet = new HashSet<String>();
        for (String tempValueSet : addressTemp) {
            if (addressValueSet.contains(tempValueSet)) {
                return tempValueSet;
            } else if (!tempValueSet.equals("")) {
                addressValueSet.add(tempValueSet);
            }
        }
        return "false";
    }

    /**
     * Check on valid URL formats. Extend the regular expression rules in
     * urlFormats if needed.
     */
    public static boolean isCorrectlyFormattedURL(String url) {
        boolean matched = false;
        if (url != null) {
            for (int i = 0; i < urlFormats.length && !matched; i++) {
                if (url.matches(urlFormats[i])) {
                    matched = true;
                }
                logger.debug("URL=" + url + " matches regex=" + urlFormats[i] + " = " + matched);
            }
        }
        return matched;
    }
}

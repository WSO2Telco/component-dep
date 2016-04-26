/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gsm.oneapi.server.ussd;

import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mife.sandbox.model.UssdImpl;
import mife.sandbox.model.entities.User;
import mife.sandbox.model.entities.UssdTransaction;
import mife.sandbox.model.functions.UssdFunctions;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.dialog.mife.server.SessionManager;
import org.dialog.mife.util.RequestHandler;
import org.gsm.oneapi.responseobject.ussd.UssdResponse;
import org.gsm.oneapi.server.OneAPIServlet;
import org.gsm.oneapi.server.ValidationRule;
import org.json.JSONObject;

/**
 *
 * @author User
 */
public class UssdReceiveServlet extends OneAPIServlet{
    static Logger logger = Logger.getLogger(UssdSendServlet.class);
    private static final long serialVersionUID = -6237772242372106922L;
    private final String[] validationRules = {"ussd", "*", "inbound"};
    SessionManager sessionMgr = new SessionManager();
    public UssdImpl ussdImpl = new UssdImpl();

    @Override
    public void init() throws ServletException {
        logger.debug("UssdSendServlet initialised");
        sessionMgr.start();
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        dumpRequestDetails(request, logger);

        String[] requestParts = getRequestParts(request);

        String sandboxusr = request.getHeader("sandbox");
        
        User user = null;
        if (sandboxusr == null) {
            sandboxusr = getProfileIdFromRequest(request);
        }
        user = ussdImpl.getUser(sandboxusr);

        if (validateRequest(request, response, requestParts, validationRules)) {

            String address = null;
            String shortCode = null;
            String keyword = null;
            String message = null;
            String clientCorrelator = null;
            String notifyUrl = null;
            String callbackData = null;
            String ussdAction = null;

            String json = RequestHandler.getRequestJSON(request);

            try {
                JSONObject objJSONObject = new JSONObject(json);
                JSONObject requestData = objJSONObject.getJSONObject("inboundUSSDMessageRequest");

                if (requestData.has("address")) {
                    address = nullOrTrimmed(requestData.getString("address"));
                }
                if (requestData.has("shortCode")) {
                    shortCode = nullOrTrimmed(requestData.getString("shortCode"));
                }

                if (requestData.has("keyword")) {
                    keyword = nullOrTrimmed(requestData.getString("keyword"));
                }
                if (requestData.has("inboundUSSDMessage")) {
                    message = nullOrTrimmed(requestData.getString("inboundUSSDMessage"));
                }
                if (requestData.has("clientCorrelator")) {
                    clientCorrelator = nullOrTrimmed(requestData.getString("clientCorrelator"));
                }
                if (requestData.has("ussdAction")) {
                    ussdAction = nullOrTrimmed(requestData.getString("ussdAction"));
                }

                JSONObject responseRequest = requestData.getJSONObject("responseRequest");
                if (responseRequest.has("notifyURL")) {
                    notifyUrl = nullOrTrimmed(responseRequest.getString("notifyURL"));
                }
                if (responseRequest.has("callbackData")) {
                    callbackData = nullOrTrimmed(responseRequest.getString("callbackData"));
                }

            } catch (Exception e) {
                logger.error("Manipulating recived JSON Object: " + e);
                //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Request JSON format is invalid");
            }

            ValidationRule[] rules = null;

            rules = new ValidationRule[]{
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, "address", address),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "shortCode", shortCode),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "keyword", keyword),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "inboundUSSDMessage", message),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "clientCorrelator", clientCorrelator),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "ussdAction", ussdAction),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "notifyURL", notifyUrl),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "callbackData", callbackData),};

            if (checkRequestParameters(response, rules)) {

                UssdTransaction snd = new UssdTransaction();
                snd.setAddress(address);
                snd.setShortCode(shortCode);
                snd.setKeyword(keyword);
                snd.setMessage(message);
                snd.setClientCorrelator(clientCorrelator);
                snd.setUssdAction(ussdAction);
                snd.setNotifyUrl(notifyUrl);
                snd.setCallbackData(callbackData);

                snd.setActionStatus(2); //ActionStatus of Receive USSD should be 2

                snd.setDate(new Date());
                snd.setUser(user);
                
                String resourceURL = getRequestHostnameAndContext(request) + request.getServletPath() + "/ussd/v1/inbound";

                if (UssdFunctions.saveUssdTransaction(snd) == 201) {
                    /*
                    //Sending response
                    UssdResponse ussdRes = new UssdResponse();
                    ussdRes.setAddress(address);
                    ussdRes.setShortCode(shortCode);
                    ussdRes.setKeyword(keyword);
                    ussdRes.setOutboundUSSDMessage(message);
                    ussdRes.setClientCorrelator(clientCorrelator);
                    
                    ussdRes.setDeliveryStatus("SENT");
                    ussdRes.setUssdAction(ussdAction);
                    
                    UssdResponse.ResponseRequest ussdResInnerData = new UssdResponse.ResponseRequest();
                    ussdResInnerData.setCallbackData(callbackData);
                    ussdResInnerData.setNotifyURL(notifyUrl);
                    
                    ussdRes.setResponseRequest(ussdResInnerData);
                    
                    ObjectMapper mapper = new ObjectMapper();
                    String ussdResponseString = "{\"outboundUSSDMessageRequest\":" + mapper.writeValueAsString(ussdRes) + "}";
                    */
                    sendJSONResponse(response, "", CREATED, resourceURL);
                }
            }
        }
    }
    
}

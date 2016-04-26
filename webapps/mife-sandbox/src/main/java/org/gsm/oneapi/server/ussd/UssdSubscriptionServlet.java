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
import mife.sandbox.model.entities.UssdSubscriptions;
import mife.sandbox.model.functions.UssdFunctions;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.dialog.mife.server.SessionManager;
import org.dialog.mife.util.RequestHandler;
import org.gsm.oneapi.responsebean.RequestError;
import org.gsm.oneapi.responseobject.ussd.UssdSubscriptionResponse;
import org.gsm.oneapi.server.OneAPIServlet;
import org.gsm.oneapi.server.ValidationRule;
import org.json.JSONObject;

/**
 *
 * @author User
 */
public class UssdSubscriptionServlet extends OneAPIServlet{
    
    static Logger logger = Logger.getLogger(UssdSubscriptionServlet.class);
    private static final long serialVersionUID = -6237772242372106922L;
    private final String[] validationRules = {"ussd","*", "inbound", "subscriptions"};
    SessionManager sessionMgr = new SessionManager();
    public UssdImpl ussdImpl = new UssdImpl();

    @Override
    public void init() throws ServletException {
        logger.debug("UssdSubscriptionServlet initialised");
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
            
            String callbackData = null;
            String notifyUrl = null;
            String destinationAddress = null;
            String clientCorrelator = null;
            String resourceUrl = null;
            
            String json = RequestHandler.getRequestJSON(request);
                
            try {
                JSONObject objJSONObject = new JSONObject(json);
                JSONObject requestData = objJSONObject.getJSONObject("subscription");

                if (requestData.has("destinationAddress")) {
                    destinationAddress = nullOrTrimmed(requestData.getString("destinationAddress"));
                }
                if (requestData.has("clientCorrelator")) {
                    clientCorrelator = nullOrTrimmed(requestData.getString("clientCorrelator"));
                }
                if (requestData.has("resourceURL")) {
                    resourceUrl = nullOrTrimmed(requestData.getString("resourceURL"));
                }
                
                if (requestData.has("callbackReference")) {
                    JSONObject callbackReference = requestData.getJSONObject("callbackReference");
                    
                    if (callbackReference.has("callbackData")) {
                        callbackData = nullOrTrimmed(callbackReference.getString("callbackData"));
                    }
                    if (callbackReference.has("notifyURL")) {
                        notifyUrl = nullOrTrimmed(callbackReference.getString("notifyURL"));
                    }
                }
                
            } catch (Exception e) {
                logger.error("Manipulating recived JSON Object: " + e);
                //sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Request JSON format is invalid");
            }
            
            ValidationRule[] rules = null;

            rules = new ValidationRule[]{
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "destinationAddress", destinationAddress),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "resourceURL", resourceUrl),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "clientCorrelator", clientCorrelator),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "notifyURL", notifyUrl),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "callbackData", callbackData),};
            
            if (checkRequestParameters(response, rules)) {
                
                UssdSubscriptions sub = new UssdSubscriptions();
                sub.setDestinationAddress(destinationAddress);
                sub.setResourceUrl(resourceUrl);
                sub.setClientCorrelator(clientCorrelator);
                sub.setNotifyUrl(notifyUrl);
                sub.setCallbackData(callbackData);
                
                sub.setCreatedDate(new Date());
                sub.setEffectedDate(new Date());
                
                sub.setUser(user);
                
                sub.setSubStatus(1);
                
                String createdSubscriptionId = UssdFunctions.saveUssdSubscription(sub);
                if(!createdSubscriptionId.isEmpty()){
                    
                    String newResourceURL = getRequestHostnameAndContext(request) + request.getServletPath() + "/ussd/v1/inbound/subscriptions/" + urlEncode(createdSubscriptionId);
                    
                    UssdSubscriptionResponse res = new UssdSubscriptionResponse();
                    res.setDestinationAddress(destinationAddress);
                    res.setClientCorrelator(clientCorrelator);
                    res.setResourceUrl(newResourceURL);
                    
                    UssdSubscriptionResponse.CallbackReference callbackReference = new UssdSubscriptionResponse.CallbackReference();
                    callbackReference.setCallbackData(callbackData);
                    callbackReference.setNotifyUrl(notifyUrl);
                    
                    res.setCallbackReference(callbackReference);
                    
                    
                    ObjectMapper mapper = new ObjectMapper();
                    String ussdResponseString = "{\"subscription\":" + mapper.writeValueAsString(res) + "}";
                    
                    sendJSONResponse(response, ussdResponseString, CREATED, newResourceURL);
                    
                } else {
                    //Save error
                    sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0001", "A service error occurred. Error code is %1", "");
                }
            }
            
        }
    }
}

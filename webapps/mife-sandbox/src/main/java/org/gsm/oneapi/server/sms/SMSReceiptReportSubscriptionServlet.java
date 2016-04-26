package org.gsm.oneapi.server.sms;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mife.sandbox.model.SmsImpl;
import mife.sandbox.model.entities.Smsparam;
import mife.sandbox.model.entities.User;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.dialog.mife.responsebean.sms.SubscribeNotification;
import org.dialog.mife.util.RequestHandler;
import org.gsm.oneapi.responsebean.RequestError;
import org.gsm.oneapi.responsebean.ResourceReference;
import org.gsm.oneapi.server.OneAPIServlet;
import org.gsm.oneapi.server.ValidationRule;
import org.json.JSONObject;

/**
 * Servlet implementing the OneAPI function for creating an SMS message receipt
 * subscription
 */
public class SMSReceiptReportSubscriptionServlet extends OneAPIServlet {

    private static final long serialVersionUID = -7359556423074788912L;

    static Logger logger = Logger.getLogger(SMSReceiptReportSubscriptionServlet.class);

    public SmsImpl smsimpl;

    public void init() throws ServletException {
        logger.debug("SMSReceiptReportSubscriptionServlet initialised");
    }

    private final String[] validationRules = {"smsmessaging", "1", "inbound", "subscriptions"};

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        dumpRequestDetails(request, logger);

        String[] requestParts = getRequestParts(request);

        smsimpl = new SmsImpl();

        //Get user profile 
        //Implementation part of local DB :START
        String sandboxusr = request.getHeader("sandbox");
        User user = null;
        if (sandboxusr == null) {
            sandboxusr = getProfileIdFromRequest(request);
        }

        user = smsimpl.getUser(sandboxusr);
        Smsparam smsparam = smsimpl.querySmsParam(sandboxusr);

        if (validateRequest(request, response, requestParts, validationRules)) {
            //if (true) {

            logger.debug("SMS Receive Report Subscriptions - url appears correctly formatted");

            /*
             * Decode the service parameters - in this case it is an HTTP POST request 
             */
            /*String destinationAddress=nullOrTrimmed(request.getParameter("destinationAddress"));
             String notifyURL=nullOrTrimmed(request.getParameter("notifyURL"));
             String criteria=nullOrTrimmed(request.getParameter("criteria"));
             String notificationFormat=nullOrTrimmed(request.getParameter("notificationFormat"));
             String clientCorrelator=nullOrTrimmed(request.getParameter("clientCorrelator"));
             String callbackData=nullOrTrimmed(request.getParameter("callbackData"));

             logger.debug("destinationAddress = "+destinationAddress);
             logger.debug("notifyURL = "+notifyURL);
             logger.debug("criteria = "+criteria);
             logger.debug("notificationFormat = "+notificationFormat);
             logger.debug("clientCorrelator = "+clientCorrelator);
             logger.debug("callbackData = "+callbackData);*/
            String destinationAddress = null;
            String notifyURL = null;
            String criteria = null;
            String notificationFormat = null;
            String clientCorrelator = null;
            String callbackData = null;

            String requestString = RequestHandler.getRequestJSON(request);

            try {

                JSONObject objJSONObject = new JSONObject(requestString);
                JSONObject objSubscription = objJSONObject.getJSONObject("subscription");

                JSONObject objCallbackReference = objSubscription.getJSONObject("callbackReference");
                if (objCallbackReference.get("callbackData") != null) {
                    callbackData = nullOrTrimmed(objCallbackReference.get("callbackData").toString());
                }
                if (objCallbackReference.get("notifyURL") != null) {
                    notifyURL = nullOrTrimmed(objCallbackReference.get("notifyURL").toString());
                }

                if (objSubscription.get("criteria") != null) {
                    criteria = nullOrTrimmed(objSubscription.get("criteria").toString());
                }
                if (objSubscription.get("destinationAddress") != null) {
                    destinationAddress = nullOrTrimmed(objSubscription.get("destinationAddress").toString());
                }
                if (objSubscription.get("notificationFormat") != null) {
                    notificationFormat = nullOrTrimmed(objSubscription.get("notificationFormat").toString());
                }
                if (objSubscription.get("clientCorrelator") != null) {
                    clientCorrelator = nullOrTrimmed(objSubscription.get("clientCorrelator").toString());
                }
            } catch (Exception e) {
                System.out.println("Manipulating recived JSON Object: " + e);
            }

            ValidationRule[] rules = {
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "destinationAddress", destinationAddress),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_URL, "notifyURL", notifyURL),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "criteria", criteria),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_JSON, "notificationFormat", notificationFormat),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "callbackData", callbackData),};

            if (checkRequestParameters(response, rules)) {
                //int user_profile_id = Integer.parseInt(getProfileIdFromRequest(request));

                if (!smsimpl.isSenderAddress(user.getId(), destinationAddress)) {
                    sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0001", "A service error occurred. Error code is %1", destinationAddress + " Not Provisioned");
                } else {

                    try {
                        criteria = (criteria == null) ? "" : criteria.trim();
                        //updateShortCodeForSMSSubscription(user_profile_id, criteria);
                        //check overlap                        
                        if (smsimpl.getSubsNotification(destinationAddress, criteria)) {
                            sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0008", "Overlapped criteria %1", criteria);
                        } else {

                            Integer subsId = smsimpl.saveSubsNotification(sandboxusr, callbackData, clientCorrelator, criteria, destinationAddress, notifyURL);
                            smsimpl.saveTransaction(destinationAddress, null, null, null, null, notifyURL, callbackData, 0, "success", 3, criteria, notificationFormat, user, null);

                            String resourceURL = getRequestHostnameAndContext(request) + request.getServletPath() + "/smsmessaging/1/inbound/subscriptions/" + subsId;

                            /*ResourceReference resourceReference=new ResourceReference();
                             resourceReference.setResourceURL(resourceURL);*/
                            SubscribeNotification objSubscribeNotification = new SubscribeNotification();

                            SubscribeNotification.CallbackReference objCallbackReference = new SubscribeNotification.CallbackReference();

                            objCallbackReference.setCallbackData(callbackData);
                            objCallbackReference.setNotifyURL(notifyURL);

                            objSubscribeNotification.setCallbackReference(objCallbackReference);
                            objSubscribeNotification.setCriteria(criteria);
                            objSubscribeNotification.setDestinationAddress(destinationAddress);
                            objSubscribeNotification.setNotificationFormat(notificationFormat);
                            objSubscribeNotification.setClientCorrelator(clientCorrelator);
                            objSubscribeNotification.setResourceURL(resourceURL);

                            ObjectMapper mapper = new ObjectMapper();

                            String jsonResponse = "{\"subscription\":" + mapper.writeValueAsString(objSubscribeNotification) + "}";

                            logger.debug("Sending response. ResourceURL=" + resourceURL);

                            sendJSONResponse(response, jsonResponse, CREATED, resourceURL);
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }
        }
    }
}

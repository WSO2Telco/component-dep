package org.gsm.oneapi.server.sms;

import java.io.IOException;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mife.sandbox.controller.DeliverySubscriptionCtrlFunctions;
import mife.sandbox.model.SmsImpl;
import mife.sandbox.model.entities.Smsparam;
import mife.sandbox.model.entities.User;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.dialog.mife.util.RequestHandler;
import org.gsm.oneapi.responsebean.sms.DeliveryReceiptSubscription;
import org.gsm.oneapi.server.OneAPIServlet;
import org.gsm.oneapi.server.ValidationRule;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementing the OneAPI function for creating an SMS delivery report
 * subscription
 */
public class SMSDeliveryReportSubscriptionServlet extends OneAPIServlet {

    private static final long serialVersionUID = -7359556423074788912L;
    static Logger logger = Logger.getLogger(SMSDeliveryReportSubscriptionServlet.class);

    public void init() throws ServletException {
        logger.debug("SMSDeliveryReportSubscriptionServlet initialised");
    }
    private final String[] validationRules = {"smsmessaging", "*", "outbound", "*", "subscriptions"};
    public SmsImpl smsimpl;

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        dumpRequestDetails(request, logger);

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

        String[] requestParts = getRequestParts(request);

        if (validateRequest(request, response, requestParts, validationRules)) {

            logger.debug("SMS Delivery Report Subscriptions - url appears correctly formatted");

            String senderAddress = requestParts[3];
            String requestJsonString = RequestHandler.getRequestJSON(request);

            String clientCorrelator = null;
            String notifyURL = null;
            String callbackData = null;
            String filterCriteria = null;
            try {
                JSONObject mainJson = new JSONObject(requestJsonString);
                JSONObject deliveryReceiptSubscription = mainJson.getJSONObject("deliveryReceiptSubscription");

                if (deliveryReceiptSubscription.get("filterCriteria") != null) {
                    filterCriteria = deliveryReceiptSubscription.getString("filterCriteria");
                }
                if (deliveryReceiptSubscription.get("clientCorrelator") != null) {
                    clientCorrelator = deliveryReceiptSubscription.getString("clientCorrelator");
                }

                JSONObject callbackReference = deliveryReceiptSubscription.getJSONObject("callbackReference");
                if (callbackReference.get("callbackData") != null) {
                    callbackData = callbackReference.getString("callbackData");
                }
                if (callbackReference.get("notifyURL") != null) {
                    notifyURL = callbackReference.getString("notifyURL");
                }

            } catch (JSONException ex) {
                java.util.logging.Logger.getLogger(SMSDeliveryReportSubscriptionServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

            logger.debug("senderAddress = " + senderAddress);
            logger.debug("clientCorrelator = " + clientCorrelator);
            logger.debug("notifyURL = " + notifyURL);
            logger.debug("callbackData = " + callbackData);

            ValidationRule[] rules = {
                //new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL, "senderAddress", senderAddress),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_URL, "notifyURL", notifyURL),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "callbackData", callbackData),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "filterCriteria", filterCriteria),};

            if (checkRequestParameters(response, rules)) {
                String resourceURL = getRequestHostnameAndContext(request) + request.getServletPath() + "/" + requestParts[1] + "/smsmessaging/outbound/subscriptions/sub789";

                DeliveryReceiptSubscription receiptSubscription = new DeliveryReceiptSubscription();

                DeliveryReceiptSubscription.CallbackReference callbackReference = new DeliveryReceiptSubscription.CallbackReference();
                callbackReference.setNotifyURL(notifyURL);
                callbackReference.setCallbackData(callbackData);
                callbackReference.setClientCorrelator(clientCorrelator);
                
                receiptSubscription.setCallbackReference(callbackReference);
                receiptSubscription.setResourceURL(resourceURL);
                receiptSubscription.setFileterCriteria(filterCriteria);

                ObjectMapper mapper = new ObjectMapper();
                String jsonResponse = "{\"deliveryReceiptSubscription\":" + mapper.writeValueAsString(receiptSubscription) + "}";

                logger.debug("Sending response. ResourceURL=" + resourceURL);
                if (DeliverySubscriptionCtrlFunctions.isSubscriptionExists(filterCriteria, notifyURL, callbackData, clientCorrelator)) {
                    sendJSONResponse(response, jsonResponse, CREATED, resourceURL);
                } else {
                    smsimpl.saveDeliverySub(sandboxusr, requestJsonString, notifyURL, clientCorrelator, callbackData, filterCriteria);
                    sendJSONResponse(response, jsonResponse, CREATED, resourceURL);
                }
            }
        }

    }
}

package org.gsm.oneapi.server.sms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mife.sandbox.model.SmsImpl;
import mife.sandbox.model.entities.Smsparam;
import mife.sandbox.model.entities.User;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.dialog.mife.responsebean.sms.DeliveryInfo;
import org.dialog.mife.responsebean.sms.DeliveryInfoList;
import org.dialog.mife.responsebean.sms.OutboundSMSMessageRequest;
import org.dialog.mife.util.RequestHandler;
import org.gsm.oneapi.responsebean.RequestError;
import org.gsm.oneapi.server.OneAPIServlet;
import org.gsm.oneapi.server.ValidationRule;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Servlet implementing the OneAPI function for sending an SMS message
 */
public class SendSMSServlet extends OneAPIServlet {

    private static final long serialVersionUID = 6553586905656923326L;
    static Logger logger = Logger.getLogger(SendSMSServlet.class);
    public SmsImpl smsimpl;

    public void init() throws ServletException {
        logger.debug("SendSMSServlet initialised");
    }
    private final String[] validationRules = {"smsmessaging", "*", "outbound", "*", "requests"};

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        dumpRequestDetails(request, logger);
        String[] requestParts = getRequestParts(request);
        logger.debug("Response JSON: " + "before validate");

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

            List<String> addresses = new ArrayList<String>(); // Note there can be multiple addresses specified
            String senderAddress = null;
            String message = null;
            String clientCorrelator = null;
            String notifyURL = null;
            String callbackData = null;
            String senderName = null;

            String resourceURL = null;

            String requestString = RequestHandler.getRequestJSON(request);

            try {
                JSONObject objJSONObject = new JSONObject(requestString);
                JSONObject objOtboundSMSMessageRequest = (JSONObject) objJSONObject.get("outboundSMSMessageRequest");

                if (objOtboundSMSMessageRequest.get("address") != null) {
                    JSONArray addressArray = objOtboundSMSMessageRequest.getJSONArray("address");
                    for (int a = 0; a < addressArray.length(); a++) {
                        addresses.add(nullOrTrimmed(addressArray.get(a).toString()));
                    }
                }

                if (objOtboundSMSMessageRequest.get("senderAddress") != null) {
                    senderAddress = nullOrTrimmed(objOtboundSMSMessageRequest.get("senderAddress").toString());
                }

                JSONObject objOutboundSMSTextMessage = (JSONObject) objOtboundSMSMessageRequest.get("outboundSMSTextMessage");
                if (objOutboundSMSTextMessage.get("message") != null) {
                    message = nullOrTrimmed(objOutboundSMSTextMessage.get("message").toString());
                }

                if (objOtboundSMSMessageRequest.get("clientCorrelator") != null) {
                    clientCorrelator = nullOrTrimmed(objOtboundSMSMessageRequest.get("clientCorrelator").toString());
                }

                JSONObject objReceiptRequest = (JSONObject) objOtboundSMSMessageRequest.get("receiptRequest");
                if (objReceiptRequest.get("callbackData") != null) {
                    callbackData = nullOrTrimmed(objReceiptRequest.get("callbackData").toString());
                }

                if (objReceiptRequest.get("notifyURL") != null) {
                    notifyURL = nullOrTrimmed(objReceiptRequest.get("notifyURL").toString());
                }

                if (objOtboundSMSMessageRequest.get("senderName") != null) {
                    senderName = nullOrTrimmed(objOtboundSMSMessageRequest.get("senderName").toString());
                }
            } catch (Exception e) {
                //logger.debug("Response JSON: " + "test");
                System.out.println("Manipulating recived JSON Object: " + e);
            }

            ValidationRule[] rules = {
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL, "senderAddress", senderAddress),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL, "address", addresses.toArray(new String[addresses.size()])),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_MESSAGE, "message", message),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "senderName", senderName),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_URL, "notifyURL", notifyURL),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "callbackData", callbackData),};

            if (checkRequestParameters(response, rules)) {
                String[] addressArray = addresses.toArray(new String[addresses.size()]);
                String num = smsimpl.checkWhitelisted(user.getId(), addressArray);

                String sender_address = senderAddress;
                if (senderAddress.contains("tel:+")) {
                    sender_address = senderAddress.replace("tel:+", "").trim();
                } else if (senderAddress.contains("tel:")) {
                    sender_address = senderAddress.replace("tel:", "").trim();
                }

                if (num != null) {
                    sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0001", "A service error occurred. Error code is %1", num + " Not Whitelisted");
                } else if (!smsimpl.isSenderAddress(user.getId(), sender_address)) {
                    sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0001", "A service error occurred. Error code is %1", senderAddress + " Not Provisioned");
                } else {
                    String smsTransactionId = smsimpl.saveSendSMSTransaction(senderAddress, addresses.toString(), message, clientCorrelator, senderName, notifyURL, callbackData, 0, "success", 1, null, null, user, smsparam.getDeliveryStatus());

                    if (smsTransactionId == null) {
                        sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0001", "A service error occurred. Error code is %1", "Access failure for API");
                    } else {
                        resourceURL = getRequestHostnameAndContext(request) + request.getServletPath() + "/smsmessaging/" + requestParts[1] + "/outbound/" + urlEncode(senderAddress) + "/requests/" + urlEncode(smsTransactionId);

                        OutboundSMSMessageRequest objOutboundSMSMessageRequest = new OutboundSMSMessageRequest();
                        objOutboundSMSMessageRequest.setAddress(addresses);

                        DeliveryInfoList objDeliveryInfoList = new DeliveryInfoList();

                        List<DeliveryInfo> deliveryInforArrayList = new ArrayList<DeliveryInfo>();

                        Iterator<String> addressReader = addresses.iterator();
                        while (addressReader.hasNext()) {

                            DeliveryInfo objDeliveryInfo = new DeliveryInfo();
                            objDeliveryInfo.setAddress(addressReader.next());
                            objDeliveryInfo.setDeliveryStatus(smsparam.getDeliveryStatus());

                            deliveryInforArrayList.add(objDeliveryInfo);
                        }
                        objDeliveryInfoList.setDeliveryInfo(deliveryInforArrayList);
                        objDeliveryInfoList.setResourceURL(resourceURL);

                        objOutboundSMSMessageRequest.setDeliveryInfoList(objDeliveryInfoList);
                        objOutboundSMSMessageRequest.setSenderAddress(senderAddress);

                        OutboundSMSMessageRequest.OutboundSMSTextMessage objOutboundSMSTextMessage = new OutboundSMSMessageRequest.OutboundSMSTextMessage();
                        objOutboundSMSTextMessage.setMessage(message);

                        objOutboundSMSMessageRequest.setOutboundSMSTextMessage(objOutboundSMSTextMessage);
                        objOutboundSMSMessageRequest.setClientCorrelator(clientCorrelator);

                        OutboundSMSMessageRequest.ReceiptRequest objReceiptRequest = new OutboundSMSMessageRequest.ReceiptRequest();
                        objReceiptRequest.setNotifyURL(notifyURL);
                        objReceiptRequest.setCallbackData(callbackData);

                        objOutboundSMSMessageRequest.setReceiptRequest(objReceiptRequest);
                        objOutboundSMSMessageRequest.setSenderName(senderName);
                        objOutboundSMSMessageRequest.setResourceURL(resourceURL);

                        ObjectMapper mapper = new ObjectMapper();

                        String jsonResponse = "{\"outboundSMSMessageRequest\":" + mapper.writeValueAsString(objOutboundSMSMessageRequest) + "}";

                        sendJSONResponse(response, jsonResponse, CREATED, resourceURL);
                    }
                }
            }
        }
    }
}

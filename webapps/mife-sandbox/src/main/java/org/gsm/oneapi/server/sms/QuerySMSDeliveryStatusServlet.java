package org.gsm.oneapi.server.sms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mife.sandbox.model.SmsImpl;
import mife.sandbox.model.entities.SMSDeliveryStatus;
import mife.sandbox.model.entities.SMSRequestLog;
import mife.sandbox.model.entities.User;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.dialog.mife.responsebean.sms.DeliveryInfo;
import org.dialog.mife.responsebean.sms.DeliveryInfoList;
import org.gsm.oneapi.responsebean.RequestError;
import org.gsm.oneapi.server.OneAPIServlet;
import static org.gsm.oneapi.server.OneAPIServlet.BAD_REQUEST;
import static org.gsm.oneapi.server.OneAPIServlet.getProfileIdFromRequest;
import static org.gsm.oneapi.server.OneAPIServlet.sendError;
import org.gsm.oneapi.server.ValidationRule;

/**
 * Servlet implementing the OneAPI function for querying (sent) SMS delivery
 * status
 */
public class QuerySMSDeliveryStatusServlet extends OneAPIServlet {

    private static final long serialVersionUID = 2849235677506318772L;

    static Logger logger = Logger.getLogger(QuerySMSDeliveryStatusServlet.class);

    public SmsImpl smsimpl;

    public void init() throws ServletException {
        logger.debug("QueryDeliveryStausServlet initialised");
    }

    private final String[] validationRules = {"smsmessaging", "*", "outbound", "*", "requests", "*", "deliveryInfos"};

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        dumpRequestDetails(request, logger);

        String[] requestParts = getRequestParts(request);
        smsimpl = new SmsImpl();

        String sandboxusr = request.getHeader("sandbox");
        User user = null;
        if (sandboxusr == null) {
            sandboxusr = getProfileIdFromRequest(request);
        }

        user = smsimpl.getUser(sandboxusr);

        if (validateRequest(request, response, requestParts, validationRules)) {
            /*
             * Decode the service parameters - in this case it is an HTTP GET request 
             */
            String senderAddress = requestParts[3];
            String requestId = requestParts[5];

            logger.debug("senderAddress = " + senderAddress);
            logger.debug("requestId = " + requestId);

            ValidationRule[] rules = {
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL, "senderAddress", senderAddress),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "requestId", requestId),};

            if (checkRequestParameters(response, rules)) {
                String requestIdParts[] = requestId.split("-");

                String previousSenderAddress = null;
                String previousDeliveryStatus = null;

                String sender_address = senderAddress;
                if (senderAddress.contains("tel:+")) {
                    sender_address = senderAddress.replace("tel:+", "").trim();
                } else if (senderAddress.contains("tel:")) {
                    sender_address = senderAddress.replace("tel:", "").trim();
                }

                SMSDeliveryStatus smsDeliveryDetails = smsimpl.getPreviousSMSDeliveryDetailsByTransactionId(requestId);
                if (smsDeliveryDetails != null) {
                    previousSenderAddress = smsDeliveryDetails.getSenderAddress();
                    previousDeliveryStatus = smsDeliveryDetails.getDeliveryStatus();
                }

                if (requestIdParts.length != 2) {
                    sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", requestId);
                } else {
                    SMSRequestLog previousSMSRequestLog = smsimpl.getPreviousChargeAmountRequestByTransactionId(Integer.parseInt(requestIdParts[1]));

                    if (previousSMSRequestLog == null) {
                        sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", requestId);
                    } else if (!smsimpl.isSenderAddress(user.getId(), sender_address)) {
                        sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0001", "A service error occurred. Error code is %1", senderAddress + " Not Provisioned");
                    } else if (smsDeliveryDetails == null) {
                        sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", requestId);
                    } else if (!senderAddress.equals(previousSenderAddress)) {
                        sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0004", "No valid addresses provided in message part %1", senderAddress);
                    } else {
                        boolean tranStatus = smsimpl.saveTransaction(senderAddress, null, null, null, null, null, null, 0, "success", 5, null, null, user, requestId);
                        if (!tranStatus) {
                            sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0001", "A service error occurred. Error code is %1", "Access failure for API");
                        } else {
                            String addresses[] = previousSMSRequestLog.getAddresses().replace("[", "").replace("]", "").split(",");
                            String resourceURL = getRequestHostnameAndContext(request) + request.getServletPath() + "/smsmessaging/" + requestParts[1] + "/outbound/" + urlEncode(senderAddress) + "/requests/" + urlEncode(requestId) + "/deliveryInfos";

                            DeliveryInfoList objDeliveryInfoList = new DeliveryInfoList();
                            List<DeliveryInfo> deliveryInforArrayList = new ArrayList<DeliveryInfo>();

                            for (int i = 0; i < addresses.length; i++) {
                                DeliveryInfo objDeliveryInfo = new DeliveryInfo();
                                objDeliveryInfo.setAddress(addresses[i]);
                                objDeliveryInfo.setDeliveryStatus(previousDeliveryStatus);

                                deliveryInforArrayList.add(objDeliveryInfo);
                            }

                            objDeliveryInfoList.setDeliveryInfo(deliveryInforArrayList);
                            objDeliveryInfoList.setResourceURL(resourceURL);

                            ObjectMapper mapper = new ObjectMapper();
                            String jsonResponse = "{\"deliveryInfoList\":" + mapper.writeValueAsString(objDeliveryInfoList) + "}";

                            sendJSONResponse(response, jsonResponse, OK, null);
                        }
                    }
                }
            }
        }
    }
}

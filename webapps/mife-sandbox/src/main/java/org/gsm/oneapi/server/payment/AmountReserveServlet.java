package org.gsm.oneapi.server.payment;

import java.io.IOException;
import java.text.DecimalFormat;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mife.sandbox.model.PaymentImpl;
import mife.sandbox.model.entities.ChargeAmountRequest;
import mife.sandbox.model.entities.ManageNumber;
import mife.sandbox.model.entities.Paymentparam;
import mife.sandbox.model.entities.User;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.dialog.mife.responsebean.payment.AmountReservationTransaction;
import org.dialog.mife.server.IHttpSession;
import org.dialog.mife.server.SessionManager;
import org.dialog.mife.util.RequestHandler;
import org.gsm.oneapi.responsebean.RequestError;
import static org.gsm.oneapi.server.OneAPIServlet.BAD_REQUEST;
import static org.gsm.oneapi.server.OneAPIServlet.getProfileIdFromRequest;
import static org.gsm.oneapi.server.OneAPIServlet.sendError;
import org.gsm.oneapi.server.ValidationRule;
import org.json.JSONObject;

/**
 * Servlet implementing the OneAPI function for the payment API - reserving an
 * initial amount on the end user's account
 */
public class AmountReserveServlet extends PaymentServlet {

    static Logger logger = Logger.getLogger(AmountReserveServlet.class);
    private static final long serialVersionUID = -6237772242372106922L;
    private final String[] validationRules = {"payment", "*", "*", "transactions", "amountReservation"};
    SessionManager sessionMgr = new SessionManager();

    public void init() throws ServletException {
        logger.debug("AmountReserveServlet initialised");
    }
    private PaymentImpl payimpl;

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        dumpRequestDetails(request, logger);

        String[] requestParts = getRequestParts(request);

        payimpl = new PaymentImpl();

        String sandboxusr = request.getHeader("sandbox");
        User user = null;
        if (sandboxusr == null) {
            sandboxusr = getProfileIdFromRequest(request);
        }

        user = payimpl.getUser(sandboxusr);

        if (validateRequest(request, response, requestParts, validationRules)) {

            String clientCorrelator = null;
            String endUserId = null;
            Double amount = 0.0;
            String currency = null;
            String description = null;
            Double totalAmountCharged = 0.0;
            Double amountReserved = 0.0;
            String onBehalfOf = null;
            String purchaseCategoryCode = null;
            String channel = null;
            Double taxAmount = 0.0;
            String mandateId = null;
            String productId = null;
            String serviceId = null;
            String callbackData = null;
            String notifyURL = null;
            String notificationFormat = null;
            String referenceCode = null;
            int referenceSequence = 0;
            String transactionOperationStatus = null;

            //RequestHandler objRequestHandler = new RequestHandler();
            String requestString = RequestHandler.getRequestJSON(request);

            try {
                JSONObject objJSONObject = new JSONObject(requestString);
                JSONObject objAmountReservationTransaction = objJSONObject.getJSONObject("amountReservationTransaction");

                if (!objAmountReservationTransaction.isNull("clientCorrelator")) {
                    clientCorrelator = nullOrTrimmed(objAmountReservationTransaction.getString("clientCorrelator"));
                }
                if (!objAmountReservationTransaction.isNull("endUserId")) {
                    endUserId = nullOrTrimmed(objAmountReservationTransaction.getString("endUserId"));
                }
                if (!objAmountReservationTransaction.isNull("callbackData")) {
                    callbackData = nullOrTrimmed(objAmountReservationTransaction.getString("callbackData"));
                }
                if (!objAmountReservationTransaction.isNull("notifyURL")) {
                    notifyURL = nullOrTrimmed(objAmountReservationTransaction.getString("notifyURL"));
                }
                if (!objAmountReservationTransaction.isNull("notificationFormat")) {
                    notificationFormat = nullOrTrimmed(objAmountReservationTransaction.getString("notificationFormat"));
                }
                if (!objAmountReservationTransaction.isNull("referenceCode")) {
                    referenceCode = nullOrTrimmed(objAmountReservationTransaction.getString("referenceCode"));
                }
                if (!objAmountReservationTransaction.isNull("transactionOperationStatus")) {
                    transactionOperationStatus = nullOrTrimmed(objAmountReservationTransaction.getString("transactionOperationStatus"));
                }
                if (!objAmountReservationTransaction.isNull("referenceSequence")) {
                    if (!objAmountReservationTransaction.getString("referenceSequence").equals("")) {
                        referenceSequence = objAmountReservationTransaction.getInt("referenceSequence");
                    }
                }
                JSONObject objPaymentAmount = objAmountReservationTransaction.getJSONObject("paymentAmount");
                if (!objPaymentAmount.isNull("totalAmountCharged")) {
                    if (!objPaymentAmount.getString("totalAmountCharged").equals("")) {
                        totalAmountCharged = objPaymentAmount.getDouble("totalAmountCharged");
                    }
                }
                if (!objPaymentAmount.isNull("amountReserved")) {
                    if (!objPaymentAmount.getString("amountReserved").equals("")) {
                        amountReserved = objPaymentAmount.getDouble("amountReserved");
                    }
                }

                JSONObject objChargingInformation = objPaymentAmount.getJSONObject("chargingInformation");
                if (!objChargingInformation.isNull("amount")) {
                    if (!objChargingInformation.getString("amount").equals("")) {
                        amount = objChargingInformation.getDouble("amount");
                    }
                }
                if (!objChargingInformation.isNull("currency")) {
                    currency = nullOrTrimmed(objChargingInformation.get("currency").toString());
                }
                if (!objChargingInformation.isNull("description")) {
                    description = nullOrTrimmed(objChargingInformation.get("description").toString());
                }

                JSONObject objChargingMetaData = objPaymentAmount.getJSONObject("chargingMetaData");
                if (!objChargingMetaData.isNull("onBehalfOf")) {
                    onBehalfOf = nullOrTrimmed(objChargingMetaData.get("onBehalfOf").toString());
                }
                if (!objChargingMetaData.isNull("purchaseCategoryCode")) {
                    purchaseCategoryCode = nullOrTrimmed(objChargingMetaData.get("purchaseCategoryCode").toString());
                }
                if (!objChargingMetaData.isNull("channel")) {
                    channel = nullOrTrimmed(objChargingMetaData.get("channel").toString());
                }
                if (!objChargingMetaData.isNull("taxAmount")) {
                    taxAmount = objChargingMetaData.getDouble("taxAmount");
                }
                if (!objChargingMetaData.isNull("mandateId")) {
                    mandateId = nullOrTrimmed(objChargingMetaData.get("mandateId").toString());
                }
                if (!objChargingMetaData.isNull("productId")) {
                    productId = nullOrTrimmed(objChargingMetaData.get("productId").toString());
                }
                if (!objChargingMetaData.isNull("serviceId")) {
                    serviceId = nullOrTrimmed(objChargingMetaData.get("serviceId").toString());
                }
            } catch (Exception e) {
                logger.error("Manipulating recived JSON Object: " + e);
            }

            ValidationRule[] rules = null;
            rules = new ValidationRule[]{
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, "endUserId", endUserId),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "referenceCode", referenceCode),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "transactionOperationStatus", transactionOperationStatus, "reserved"),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "description", description),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_CURRENCY, "currency", currency),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_DOUBLE_GT_ZERO, "amount", amount),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "callbackData", callbackData),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_URL, "notifyURL", notifyURL),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "notificationFormat", notificationFormat),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO, "referenceSequence", referenceSequence),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "onBehalfOf", onBehalfOf),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "purchaseCategoryCode", purchaseCategoryCode),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_PAYMENT_CHANNEL, "channel", channel),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_DOUBLE_GE_ZERO, "taxAmount", taxAmount),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_DOUBLE_GE_ZERO, "totalAmountCharged", totalAmountCharged),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_DOUBLE_GE_ZERO, "amountReserved", amountReserved),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "serviceId", serviceId),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "mandateId", mandateId),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "productId", productId)};

            if (checkRequestParameters(response, rules)) {

                ChargeAmountRequest chargeAmountRequest = null;
                String userNum = getLastMobileNumber(endUserId);
                ManageNumber wlnumber = payimpl.getWhitelisted(user.getId(), userNum);
                Paymentparam payparam = payimpl.queryPaymentParam(user.getId());

                chargeAmountRequest = payimpl.checkClientCorrelator(user.getId(), clientCorrelator);
                int maxTranCount = Integer.parseInt(payparam.getMaxtrn());

                if (Double.parseDouble(payparam.getMaxamt()) < amount) {
                    sendError(response, BAD_REQUEST, RequestError.POLICYEXCEPTION, "POL1001", "The %1 operator charging limit for this user has been exceeded", "" + amount);
                } else if (maxTranCount <= payimpl.getTransactionCount(user.getId())) {
                    sendError(response, BAD_REQUEST, RequestError.POLICYEXCEPTION, "POL0001", "A policy error occurred. Error code is %1", "%1- Total transactions exceeds the operator limit");
                } else if (wlnumber != null) {
                    if (chargeAmountRequest == null) {
                        double openBalance = 0;
                        double openReservedAmount = 0;

                        if (wlnumber.getBalance() != 0) {
                            openBalance = wlnumber.getBalance();
                            openReservedAmount = wlnumber.getReserved_amount();
                        }

                        double sum = 0;
                        double tmpTotal = 0;
                        if (totalAmountCharged != 0) {
                            tmpTotal = amount + totalAmountCharged;
                            sum = openBalance - tmpTotal;
                        } else {
                            tmpTotal = amount;
                            sum = openBalance - tmpTotal;
                        }

                        if (sum < 0) {
                            //Throw no succicient funds
                            sendError(response, BAD_REQUEST, RequestError.POLICYEXCEPTION, "POL1000", "User has insufficient credit for transaction", null);
                        } else {
                            //Updating balance
                            double currentReservedAmount = openReservedAmount + amount;

                            //Saving transaction data in DB
                            String paymentTransactionId = payimpl.saveReservationTransaction(user, clientCorrelator, endUserId, amount, currency, description, onBehalfOf, purchaseCategoryCode, channel, taxAmount, referenceCode, transactionOperationStatus, notifyURL, callbackData, mandateId, notificationFormat, productId, referenceSequence, serviceId, null, totalAmountCharged, amountReserved, 2);
                            if (paymentTransactionId == null) {
                                sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0270", "Charging operation failed, the charge was not applied", null);
                            } else {
                                payimpl.updateWhitelisted(wlnumber.getId(), Double.valueOf(new DecimalFormat("#.##").format(sum)), Double.valueOf(new DecimalFormat("#.##").format(currentReservedAmount)));

                                //Cresting JSON and send response
                                AmountReservationTransaction objAmountReservationTransaction = new AmountReservationTransaction();
                                objAmountReservationTransaction.setClientCorrelator(clientCorrelator);
                                objAmountReservationTransaction.setEndUserId(endUserId);

                                AmountReservationTransaction.PaymentAmount objPaymentAmount = new AmountReservationTransaction.PaymentAmount();

                                AmountReservationTransaction.PaymentAmount.ChargingInformation objChargingInformation = new AmountReservationTransaction.PaymentAmount.ChargingInformation();
                                objChargingInformation.setAmount(amount);
                                objChargingInformation.setCurrency(currency);
                                objChargingInformation.setDescription(description);

                                objPaymentAmount.setChargingInformation(objChargingInformation);

                                objPaymentAmount.setTotalAmountCharged(totalAmountCharged);
                                objPaymentAmount.setAmountReserved(amountReserved);

                                AmountReservationTransaction.PaymentAmount.ChargingMetaData objChargingMetaData = new AmountReservationTransaction.PaymentAmount.ChargingMetaData();
                                objChargingMetaData.setOnBehalfOf(onBehalfOf);
                                objChargingMetaData.setPurchaseCategoryCode(purchaseCategoryCode);
                                objChargingMetaData.setChannel(channel);
                                objChargingMetaData.setTaxAmount(taxAmount);
                                objChargingMetaData.setMandateId(mandateId);
                                objChargingMetaData.setProductId(productId);
                                objChargingMetaData.setServiceId(serviceId);

                                objPaymentAmount.setChargingMetaData(objChargingMetaData);

                                objAmountReservationTransaction.setPaymentAmount(objPaymentAmount);
                                objAmountReservationTransaction.setCallbackData(callbackData);
                                objAmountReservationTransaction.setNotifyURL(notifyURL);
                                objAmountReservationTransaction.setNotificationFormat(notificationFormat);
                                objAmountReservationTransaction.setReferenceCode(referenceCode);
                                objAmountReservationTransaction.setReferenceSequence(referenceSequence);
                                objAmountReservationTransaction.setTransactionOperationStatus(transactionOperationStatus);

                                String resourceURL = getRequestHostnameAndContext(request) + request.getServletPath() + "/payment/1/" + urlEncode(endUserId) + "/transactions/amountReservation" + (paymentTransactionId != null ? ("/" + urlEncode(paymentTransactionId)) : "");

                                ObjectMapper mapper = new ObjectMapper();
                                String jsonResponse = "{\"amountReservationTransaction\":" + mapper.writeValueAsString(objAmountReservationTransaction) + "}";

                                if (request.getHeader("authorization") != null) {
                                    response.setHeader("account", String.valueOf(updateSessionBalance(tmpTotal, request.getHeader("authorization"))));
                                }
                                sendJSONResponse(response, jsonResponse, CREATED, resourceURL);
                            }
                        }
                    } else {
                        if (!endUserId.equals(chargeAmountRequest.getEndUserId())) {
                            sendError(response, FORBIDDEN, RequestError.POLICYEXCEPTION, "POL1006", "originalServerReferenceCode not valid", null);
                        } else if (chargeAmountRequest.getPaymentTranscationType() != 2) {
                            sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter clientCorrelator ");
                        } else {
                            AmountReservationTransaction objAmountReservationTransaction = new AmountReservationTransaction();
                            objAmountReservationTransaction.setClientCorrelator(chargeAmountRequest.getClientCorrelator());
                            objAmountReservationTransaction.setEndUserId(chargeAmountRequest.getEndUserId());

                            AmountReservationTransaction.PaymentAmount objPaymentAmount = new AmountReservationTransaction.PaymentAmount();

                            AmountReservationTransaction.PaymentAmount.ChargingInformation objChargingInformation = new AmountReservationTransaction.PaymentAmount.ChargingInformation();
                            objChargingInformation.setAmount(chargeAmountRequest.getAmount());
                            objChargingInformation.setCurrency(chargeAmountRequest.getCurrency());
                            objChargingInformation.setDescription(chargeAmountRequest.getDescription());

                            objPaymentAmount.setChargingInformation(objChargingInformation);

                            objPaymentAmount.setTotalAmountCharged(chargeAmountRequest.getTotalAmountCharged());
                            objPaymentAmount.setAmountReserved(chargeAmountRequest.getAmountReserved());

                            AmountReservationTransaction.PaymentAmount.ChargingMetaData objChargingMetaData = new AmountReservationTransaction.PaymentAmount.ChargingMetaData();
                            objChargingMetaData.setOnBehalfOf(chargeAmountRequest.getOnBehalfOf());
                            objChargingMetaData.setPurchaseCategoryCode(chargeAmountRequest.getPurchaseCategoryCode());
                            objChargingMetaData.setChannel(chargeAmountRequest.getChannel());
                            objChargingMetaData.setTaxAmount(chargeAmountRequest.getTaxAmount());
                            objChargingMetaData.setMandateId(chargeAmountRequest.getMandateId());
                            objChargingMetaData.setProductId(chargeAmountRequest.getProductId());
                            objChargingMetaData.setServiceId(chargeAmountRequest.getServiceId());

                            objPaymentAmount.setChargingMetaData(objChargingMetaData);

                            objAmountReservationTransaction.setPaymentAmount(objPaymentAmount);
                            objAmountReservationTransaction.setCallbackData(chargeAmountRequest.getCallbackData());
                            objAmountReservationTransaction.setNotifyURL(chargeAmountRequest.getNotifyURL());
                            objAmountReservationTransaction.setNotificationFormat(chargeAmountRequest.getNotificationFormat());
                            objAmountReservationTransaction.setReferenceCode(chargeAmountRequest.getReferenceCode());
                            objAmountReservationTransaction.setReferenceSequence(chargeAmountRequest.getReferenceSequence());
                            objAmountReservationTransaction.setTransactionOperationStatus(chargeAmountRequest.getTransactionOperationStatus());

                            String resourceURL = getRequestHostnameAndContext(request) + request.getServletPath() + "/payment/1/" + urlEncode(endUserId) + "/transactions/amountReservation" + (chargeAmountRequest.getTransactionId() != null ? ("/" + urlEncode(chargeAmountRequest.getTransactionId())) : "");

                            ObjectMapper mapper = new ObjectMapper();
                            String jsonResponse = "{\"amountReservationTransaction\":" + mapper.writeValueAsString(objAmountReservationTransaction) + "}";
                            sendJSONResponse(response, jsonResponse, CREATED, resourceURL);
                        }
                    }
                } else {
                    //Throw error for not available in whitelist!
                    sendError(response, BAD_REQUEST, RequestError.POLICYEXCEPTION, "POL1009", "User has not been provisioned for Charge Amount", null);
                }
            }
        }
    }

    private double updateSessionBalance(Double tmpTotal, String authHeader) {
        String[] authParts = authHeader.split(" ", 2);
        String sessionKey = authParts[1];

        IHttpSession hSession = sessionMgr.getSession(sessionKey, "ussd", true);

        double availableBalance;
        if (hSession.getAttribute("account") != null) {
            availableBalance = Double.parseDouble(hSession.getAttribute("account").toString());
        } else {
            availableBalance = 0.0;
        }
        double newBalance = availableBalance - tmpTotal;
        logger.debug("Updated credit balance:: " + newBalance);

        hSession.setAttribute("account", newBalance);

        return newBalance;
    }
}

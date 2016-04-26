package org.gsm.oneapi.server.payment;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mife.sandbox.model.PaymentImpl;
import mife.sandbox.model.entities.ChargeAmountRequest;
import mife.sandbox.model.entities.ManageNumber;
import mife.sandbox.model.entities.PaymentTransaction;
import mife.sandbox.model.entities.Paymentparam;
import mife.sandbox.model.entities.User;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.dialog.mife.responsebean.payment.AdditionalAmountReservationTransaction;
import org.dialog.mife.server.IHttpSession;
import org.dialog.mife.server.SessionManager;
import org.dialog.mife.util.RequestHandler;
import org.gsm.oneapi.responsebean.RequestError;
import org.gsm.oneapi.responsebean.payment.AmountReservationTransaction;
import static org.gsm.oneapi.server.OneAPIServlet.getProfileIdFromRequest;
import org.gsm.oneapi.server.ValidationRule;
import static org.gsm.oneapi.server.payment.AmountReserveServlet.logger;
import org.json.JSONObject;

/**
 * Servlet implementing the OneAPI function for the payment API - reserving an
 * additional amount on the end user's account
 */
public class AmountReserveAdditionalServlet extends PaymentServlet {

    static Logger logger = Logger.getLogger(AmountReserveAdditionalServlet.class);

    private static final long serialVersionUID = -6237772242372106922L;

    private final String[] validationRules = {"payment", "*", "*", "transactions", "amountReservation", "*"};
    SessionManager sessionMgr = new SessionManager();

    public void init() throws ServletException {
        logger.debug("AmountReserveAdditionalServlet initialised");
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

            String transactionId = requestParts[5];
            String endUserId = null;
            String callbackData = null;
            String notifyURL = null;
            String notificationFormat = null;
            Double amount = null;
            String currency = null;
            String description = null;
            String onBehalfOf = null;
            String purchaseCategoryCode = null;
            String channel = null;
            Double taxAmount = null;
            String mandateId = null;
            String productId = null;
            String serviceId = null;
            String referenceCode = null;
            int referenceSequence = 0;
            String transactionOperationStatus = null;

            String requestString = RequestHandler.getRequestJSON(request);

            try {

                JSONObject objJSONObject = new JSONObject(requestString);
                JSONObject objAmountReservationTransaction = (JSONObject) objJSONObject.get("amountReservationTransaction");

                if (objAmountReservationTransaction.get("endUserId") != null) {

                    endUserId = nullOrTrimmed(objAmountReservationTransaction.get("endUserId").toString());
                }
                if (objAmountReservationTransaction.get("callbackData") != null) {

                    callbackData = nullOrTrimmed(objAmountReservationTransaction.get("callbackData").toString());
                }
                if (objAmountReservationTransaction.get("notifyURL") != null) {

                    notifyURL = nullOrTrimmed(objAmountReservationTransaction.get("notifyURL").toString());
                }
                if (objAmountReservationTransaction.get("notificationFormat") != null) {

                    notificationFormat = nullOrTrimmed(objAmountReservationTransaction.get("notificationFormat").toString());
                }
                if (objAmountReservationTransaction.get("referenceCode") != null) {

                    referenceCode = nullOrTrimmed(objAmountReservationTransaction.get("referenceCode").toString());
                }
                if (objAmountReservationTransaction.get("transactionOperationStatus") != null) {

                    transactionOperationStatus = nullOrTrimmed(objAmountReservationTransaction.get("transactionOperationStatus").toString());
                }
                if (objAmountReservationTransaction.get("referenceSequence") != null) {

                    referenceSequence = Integer.parseInt(nullOrTrimmed(objAmountReservationTransaction.get("referenceSequence").toString()));
                }

                JSONObject objPaymentAmount = (JSONObject) objAmountReservationTransaction.get("paymentAmount");

                JSONObject objChargingInformation = (JSONObject) objPaymentAmount.get("chargingInformation");

                if (objChargingInformation.get("amount") != null) {

                    amount = Double.parseDouble(nullOrTrimmed(objChargingInformation.get("amount").toString()));
                }
                if (objChargingInformation.get("currency") != null) {

                    currency = nullOrTrimmed(objChargingInformation.get("currency").toString());
                }
                if (objChargingInformation.get("description") != null) {

                    description = nullOrTrimmed(objChargingInformation.get("description").toString());
                }

                JSONObject objChargingMetaData = (JSONObject) objPaymentAmount.get("chargingMetaData");

                if (objChargingMetaData.get("onBehalfOf") != null) {

                    onBehalfOf = nullOrTrimmed(objChargingMetaData.get("onBehalfOf").toString());
                }
                if (objChargingMetaData.get("purchaseCategoryCode") != null) {

                    purchaseCategoryCode = nullOrTrimmed(objChargingMetaData.get("purchaseCategoryCode").toString());
                }
                if (objChargingMetaData.get("channel") != null) {

                    channel = nullOrTrimmed(objChargingMetaData.get("channel").toString());
                }
                if (objChargingMetaData.get("taxAmount") != null) {

                    taxAmount = Double.parseDouble(nullOrTrimmed(objChargingMetaData.get("taxAmount").toString()));
                }
                if (objChargingMetaData.get("mandateId") != null) {

                    mandateId = nullOrTrimmed(objChargingMetaData.get("mandateId").toString());
                }
                if (objChargingMetaData.get("productId") != null) {

                    productId = nullOrTrimmed(objChargingMetaData.get("productId").toString());
                }
                if (objChargingMetaData.get("serviceId") != null) {

                    serviceId = nullOrTrimmed(objChargingMetaData.get("serviceId").toString());
                }

            } catch (Exception e) {
                logger.error("Manipulating recived JSON Object: " + e);
            }

            ValidationRule[] rules = null;

            rules = new ValidationRule[]{
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, "endUserId", endUserId),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_DOUBLE_GT_ZERO, "amount", amount),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_CURRENCY, "currency", currency),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "description", description),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "transactionId", transactionId),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "onBehalfOf", onBehalfOf),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "notificationFormat", notificationFormat),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "purchaseCategoryCode", purchaseCategoryCode),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_PAYMENT_CHANNEL, "channel", channel),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_DOUBLE_GE_ZERO, "taxAmount", taxAmount),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "serviceId", serviceId),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "mandateId", mandateId),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "productId", productId),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_URL, "notifyURL", notifyURL),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "callbackData", callbackData),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "referenceCode", referenceCode),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "referenceSequence", Integer.toString(referenceSequence)),
              //  new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "transactionOperationStatus", transactionOperationStatus, "reserved")};
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "transactionOperationStatus", transactionOperationStatus)};

            if (checkRequestParameters(response, rules)) {

                Double previousReservedAmount = 0.0;
                String previousEndUserId = null;
                String userNum = getLastMobileNumber(endUserId);
                ManageNumber wlnumber = payimpl.getWhitelisted(user.getId(), userNum);
                Paymentparam payparam = payimpl.queryPaymentParam(user.getId());

                PaymentTransaction transactionDetails = payimpl.getPreviousTransactionDetailsByTransactionId(transactionId);
                if (transactionDetails != null) {
                    previousReservedAmount = transactionDetails.getAmount();
                    previousEndUserId = transactionDetails.getEndUserId();
                }
                
                int maxTranCount = Integer.parseInt(payparam.getMaxtrn());
                
                if (Double.parseDouble(payparam.getMaxamt()) < amount) {
                    sendError(response, BAD_REQUEST, RequestError.POLICYEXCEPTION, "POL1001", "The %1 operator charging limit for this user has been exceeded", "" + amount);
                } else if (maxTranCount <= payimpl.getTransactionCount(user.getId())) {
                    sendError(response, BAD_REQUEST, RequestError.POLICYEXCEPTION, "POL0001", "A policy error occurred. Error code is %1", "%1- Total transactions exceeds the operator limit");
                }else if (!transactionOperationStatus.equalsIgnoreCase(payparam.getPaystatus())) {
                    sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter transactionOperationStatus expected " + payparam.getPaystatus() + " received " + transactionOperationStatus);}
                 else if (wlnumber != null) {
                    double openBalance = 0;
                    double openReservedAmount = 0;

                    if (wlnumber.getBalance() != 0) {
                        openBalance = wlnumber.getBalance();
                        openReservedAmount = wlnumber.getReserved_amount();
                    }

                    double sum = openBalance - amount;
                    if (sum < 0) {
                        //Throw no succicient funds
                        sendError(response, BAD_REQUEST, RequestError.POLICYEXCEPTION, "POL1000", "User has insufficient credit for transaction", null);
                    } else if (transactionDetails == null) {
                        sendError(response, FORBIDDEN, RequestError.POLICYEXCEPTION, "POL1006", "originalServerReferenceCode not valid", null);
                    } else if (!endUserId.equals(previousEndUserId)) {
                        sendError(response, FORBIDDEN, RequestError.POLICYEXCEPTION, "POL1006", "originalServerReferenceCode not valid", null);
                    } else {
                        
                        double currentReservedAmount = openReservedAmount + amount;
                        
                        //Total amount reserved
                        double amountReserved = previousReservedAmount + amount;

                        //Saving transaction data in DB
                        String paymentTransactionId = payimpl.saveAdditionalReservationTransaction(user, null, endUserId, amount, currency, description, onBehalfOf, purchaseCategoryCode, channel, taxAmount, referenceCode, transactionOperationStatus, callbackData, notifyURL, mandateId, notificationFormat, productId, referenceSequence, serviceId, null, 0.0, 0.0, 3, transactionId);
                        if (paymentTransactionId == null) {
                            sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0270", "Charging operation failed, the charge was not applied", null);
                        } else {
                            //Updating balance
                            payimpl.updateWhitelisted(wlnumber.getId(), Double.valueOf(new DecimalFormat("#.##").format(sum)), Double.valueOf(new DecimalFormat("#.##").format(currentReservedAmount)));
                            
                            String resourceURL = getRequestHostnameAndContext(request) + request.getServletPath() + "/payment/1/" + urlEncode(endUserId) + "/transactions/amountReservation" + (transactionId != null ? ("/" + urlEncode(transactionId)) : "");

                            AdditionalAmountReservationTransaction objAdditionalAmountReservationTransaction = new AdditionalAmountReservationTransaction();
                            objAdditionalAmountReservationTransaction.setEndUserId(endUserId);

                            AdditionalAmountReservationTransaction.PaymentAmount objPaymentAmount = new AdditionalAmountReservationTransaction.PaymentAmount();

                            AdditionalAmountReservationTransaction.PaymentAmount.ChargingInformation objChargingInformation = new AdditionalAmountReservationTransaction.PaymentAmount.ChargingInformation();
                            objChargingInformation.setAmount(amount);
                            objChargingInformation.setCurrency(currency);
                            objChargingInformation.setDescription(description);

                            objPaymentAmount.setChargingInformation(objChargingInformation);

                            objPaymentAmount.setAmountReserved(amountReserved);
                            objPaymentAmount.setTotalAmountCharged(0.0);
                            AdditionalAmountReservationTransaction.PaymentAmount.ChargingMetaData objChargingMetaData = new AdditionalAmountReservationTransaction.PaymentAmount.ChargingMetaData();
                            objChargingMetaData.setOnBehalfOf(onBehalfOf);
                            objChargingMetaData.setPurchaseCategoryCode(purchaseCategoryCode);
                            objChargingMetaData.setChannel(channel);
                            objChargingMetaData.setTaxAmount(taxAmount);
                            objChargingMetaData.setMandateId(mandateId);
                            objChargingMetaData.setProductId(productId);
                            objChargingMetaData.setServiceId(serviceId);

                            objPaymentAmount.setChargingMetaData(objChargingMetaData);

                            objAdditionalAmountReservationTransaction.setPaymentAmount(objPaymentAmount);
                            objAdditionalAmountReservationTransaction.setCallbackData(callbackData);
                            objAdditionalAmountReservationTransaction.setNotifyURL(notifyURL);
                            objAdditionalAmountReservationTransaction.setNotificationFormat(notificationFormat);
                            objAdditionalAmountReservationTransaction.setReferenceCode(referenceCode);
                            objAdditionalAmountReservationTransaction.setReferenceSequence(referenceSequence);
                            objAdditionalAmountReservationTransaction.setResourceURL(resourceURL);
                            objAdditionalAmountReservationTransaction.setTransactionOperationStatus(transactionOperationStatus);

                            ObjectMapper mapper = new ObjectMapper();
                            String jsonResponse = "{\"amountReservationTransaction\":" + mapper.writeValueAsString(objAdditionalAmountReservationTransaction) + "}";

                            if (request.getHeader("authorization") != null) {
                                response.setHeader("account", String.valueOf(updateSessionBalance(amount, request.getHeader("authorization"))));
                            }
                            sendJSONResponse(response, jsonResponse, OK, null);
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

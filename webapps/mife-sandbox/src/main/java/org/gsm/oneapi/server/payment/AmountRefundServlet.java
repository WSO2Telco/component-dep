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
import org.dialog.mife.responsebean.payment.RefundAmountCharge;
import org.dialog.mife.server.SessionManager;
import org.dialog.mife.util.RequestHandler;
import org.gsm.oneapi.responsebean.RequestError;
import static org.gsm.oneapi.server.OneAPIServlet.getLastMobileNumber;
import static org.gsm.oneapi.server.OneAPIServlet.getProfileIdFromRequest;
import org.gsm.oneapi.server.ValidationRule;
import org.json.JSONObject;

/**
 * Servlet implementing the OneAPI function for the payment API - refunding an
 * end user
 */
public class AmountRefundServlet extends PaymentServlet {

    static Logger logger = Logger.getLogger(AmountRefundServlet.class);

    private static final long serialVersionUID = -6237772242372106922L;

    private final String[] validationRules = {"payment", "1", "*", "transactions", "amount"};
    SessionManager sessionMgr = new SessionManager();

    public void init() throws ServletException {
        logger.debug("AmountRefundServlet initialised");
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
            Double amount = null;
            String currency = null;
            String description = null;
            String code = null;
            String onBehalfOf = null;
            String purchaseCategoryCode = null;
            String channel = null;
            Double taxAmount = null;
            String mandateId = null;
            String productId = null;
            String serviceId = null;
            String callbackData = null;
            String notifyURL = null;
            String referenceCode = null;
            String notificationFormat = null;
            String originalServerReferenceCode = null;
            String transactionOperationStatus = null;

            String requestString = RequestHandler.getRequestJSON(request);

            try {
                JSONObject objJSONObject = new JSONObject(requestString);
                JSONObject objAmountTransaction = (JSONObject) objJSONObject.get("amountTransaction");

                if (objAmountTransaction.get("clientCorrelator") != null) {
                    clientCorrelator = nullOrTrimmed(objAmountTransaction.get("clientCorrelator").toString());
                }
                if (objAmountTransaction.get("endUserId") != null) {
                    endUserId = nullOrTrimmed(objAmountTransaction.get("endUserId").toString());
                }
                if (objAmountTransaction.get("callbackData") != null) {
                    callbackData = nullOrTrimmed(objAmountTransaction.get("callbackData").toString());
                }
                if (objAmountTransaction.get("notifyURL") != null) {
                    notifyURL = nullOrTrimmed(objAmountTransaction.get("notifyURL").toString());
                }
                if (objAmountTransaction.get("notificationFormat") != null) {
                    notificationFormat = nullOrTrimmed(objAmountTransaction.get("notificationFormat").toString());
                }
                if (objAmountTransaction.get("referenceCode") != null) {
                    referenceCode = nullOrTrimmed(objAmountTransaction.get("referenceCode").toString());
                }
                if (objAmountTransaction.get("originalServerReferenceCode") != null) {
                    originalServerReferenceCode = nullOrTrimmed(objAmountTransaction.get("originalServerReferenceCode").toString());
                }
                if (objAmountTransaction.get("transactionOperationStatus") != null) {
                    transactionOperationStatus = nullOrTrimmed(objAmountTransaction.get("transactionOperationStatus").toString());
                }

                JSONObject objPaymentAmount = (JSONObject) objAmountTransaction.get("paymentAmount");

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
                if (objChargingInformation.get("code") != null) {
                    code = nullOrTrimmed(objChargingInformation.get("code").toString());
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
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, "endUserId", endUserId),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_DOUBLE_GT_ZERO, "amount", amount),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_CURRENCY, "currency", currency),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "description", description),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "onBehalfOf", onBehalfOf),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "notificationFormat", notificationFormat),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "code", code),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "purchaseCategoryCode", purchaseCategoryCode),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_PAYMENT_CHANNEL, "channel", channel),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_DOUBLE_GE_ZERO, "taxAmount", taxAmount),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "serviceId", serviceId),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "mandateId", mandateId),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "productId", productId),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "callbackData", callbackData),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_URL, "notifyURL", notifyURL),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "referenceCode", referenceCode),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "originalServerReferenceCode", originalServerReferenceCode),
                // new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "transactionOperationStatus", transactionOperationStatus, "Refunded")};
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "transactionOperationStatus", transactionOperationStatus)};

            if (checkRequestParameters(response, rules)) {
                String originalServerReferenceCodeParts[] = originalServerReferenceCode.split("-");
                Double previousChargedAmount = 0.0;
                Double previousTaxAmount = 0.0;
                ChargeAmountRequest chargeAmountRequest = null;
                int previousChargedRefundStatus = -1;
                String previousEndUserId = null;
                String userNum = getLastMobileNumber(endUserId);
                ManageNumber wlnumber = payimpl.getWhitelisted(user.getId(), userNum);
                chargeAmountRequest = payimpl.checkClientCorrelator(user.getId(), clientCorrelator);
                Paymentparam payparam = payimpl.queryPaymentParam(user.getId());

                if (originalServerReferenceCodeParts.length != 2) {
                    sendError(response, FORBIDDEN, RequestError.POLICYEXCEPTION, "POL1006", "originalServerReferenceCode not valid", null);
                } else {
                    ChargeAmountRequest previousChargeAmountRequest = payimpl.getPreviousChargeAmountRequestByTransactionId(Integer.parseInt(originalServerReferenceCodeParts[1]));
                    if (previousChargeAmountRequest != null) {
                        previousChargedAmount = previousChargeAmountRequest.getAmount();
                        previousTaxAmount = previousChargeAmountRequest.getTaxAmount();
                        previousChargedRefundStatus = previousChargeAmountRequest.getRefundStatus();
                        previousEndUserId = previousChargeAmountRequest.getEndUserId();
                    }

                    if (currency == null) {
                        sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0007", "Invalid charging information", null);
                    } else if (code == null && amount == null) {
                        sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0007", "Invalid charging information", null);
                    } else if (previousChargeAmountRequest == null) {
                        sendError(response, FORBIDDEN, RequestError.POLICYEXCEPTION, "POL1005", "A refund request requires the originalServerReferenceCode for the charge that is being refunded", null);
                    } else if (previousChargedRefundStatus != 0) {
                        sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0007", "Invalid charging information", null);
                    } else if (!endUserId.equals(previousEndUserId)) {
                        sendError(response, FORBIDDEN, RequestError.POLICYEXCEPTION, "POL1006", "originalServerReferenceCode not valid", null);
                    } else if (!transactionOperationStatus.equalsIgnoreCase(payparam.getPaystatus())) {
                        sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter transactionOperationStatus expected " + payparam.getPaystatus() + " received " + transactionOperationStatus);
                    } else if (amount != null && amount > previousChargedAmount) {
                        sendError(response, FORBIDDEN, RequestError.POLICYEXCEPTION, "POL1003", "The refund exceeds the original amount charged %1", "" + previousChargedAmount);
                    } else if (wlnumber != null) {
                        if (chargeAmountRequest == null) {
                            double openBalance = 0;

                            if (wlnumber.getBalance() != 0) {
                                openBalance = wlnumber.getBalance();
                            }

                            double sum = openBalance + previousChargedAmount + previousTaxAmount;

                            //Saving transaction data in DB
                            String paymentTransactionId = payimpl.saveRefundTransaction(user, clientCorrelator, endUserId, amount, currency, description, onBehalfOf, purchaseCategoryCode, channel, taxAmount, referenceCode, originalServerReferenceCode, transactionOperationStatus, callbackData, notifyURL, mandateId, notificationFormat, productId, 0, serviceId, code, 0.0, 0.0, 6);

                            if (paymentTransactionId == null) {
                                sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0270", "Charging operation failed, the charge was not applied", null);
                            } else {
                                
                                payimpl.updateWhitelisted(wlnumber.getId(), Double.valueOf(new DecimalFormat("#.##").format(sum)));

                                String serverReferenceCode = "ABC-123";

                                String resourceURL = getRequestHostnameAndContext(request) + request.getServletPath() + "/payment/1/" + urlEncode(endUserId) + "/transactions/amount" + (paymentTransactionId != null ? ("/" + urlEncode(paymentTransactionId)) : "");

                                RefundAmountCharge objRefundAmountCharge = new RefundAmountCharge();
                                objRefundAmountCharge.setClientCorrelator(clientCorrelator);
                                objRefundAmountCharge.setEndUserId(endUserId);

                                RefundAmountCharge.PaymentAmount objPaymentAmount = new RefundAmountCharge.PaymentAmount();

                                RefundAmountCharge.PaymentAmount.ChargingInformation objChargingInformation = new RefundAmountCharge.PaymentAmount.ChargingInformation();
                                objChargingInformation.setAmount(amount);
                                objChargingInformation.setCurrency(currency);
                                objChargingInformation.setDescription(description);
                                objChargingInformation.setCode(code);

                                RefundAmountCharge.PaymentAmount.ChargingMetaData objChargingMetaData = new RefundAmountCharge.PaymentAmount.ChargingMetaData();
                                objChargingMetaData.setOnBehalfOf(onBehalfOf);
                                objChargingMetaData.setPurchaseCategoryCode(purchaseCategoryCode);
                                objChargingMetaData.setChannel(channel);
                                objChargingMetaData.setTaxAmount(taxAmount);
                                objChargingMetaData.setMandateId(mandateId);
                                objChargingMetaData.setProductId(productId);
                                objChargingMetaData.setServiceId(serviceId);

                                objPaymentAmount.setChargingInformation(objChargingInformation);
                                objPaymentAmount.setChargingMetaData(objChargingMetaData);
                                objPaymentAmount.setTotalAmountRefunded(previousChargedAmount);

                                objRefundAmountCharge.setPaymentAmount(objPaymentAmount);
                                objRefundAmountCharge.setCallbackData(callbackData);
                                objRefundAmountCharge.setNotifyURL(notifyURL);
                                objRefundAmountCharge.setNotificationFormat(notificationFormat);
                                objRefundAmountCharge.setReferenceCode(referenceCode);
                                objRefundAmountCharge.setOriginalServerReferenceCode(originalServerReferenceCode);
                                objRefundAmountCharge.setResourceURL(resourceURL);
                                objRefundAmountCharge.setTransactionOperationStatus(transactionOperationStatus);

                                ObjectMapper mapper = new ObjectMapper();
                                String jsonResponse = "{\"amountTransaction\":" + mapper.writeValueAsString(objRefundAmountCharge) + "}";
                                sendJSONResponse(response, jsonResponse, CREATED, resourceURL);
                            }
                        } else {
                            if (!endUserId.equals(chargeAmountRequest.getEndUserId())) {
                                sendError(response, FORBIDDEN, RequestError.POLICYEXCEPTION, "POL1006", "originalServerReferenceCode not valid", null);
                            } else if (chargeAmountRequest.getPaymentTranscationType() != 6) {
                                sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter clientCorrelator ");
                            } else {
                                String resourceURL = getRequestHostnameAndContext(request) + request.getServletPath() + "/payment/1/" + urlEncode(endUserId) + "/transactions/amount" + (chargeAmountRequest.getTransactionId() != null ? ("/" + urlEncode(chargeAmountRequest.getTransactionId())) : "");

                                RefundAmountCharge objRefundAmountCharge = new RefundAmountCharge();
                                objRefundAmountCharge.setClientCorrelator(chargeAmountRequest.getClientCorrelator());
                                objRefundAmountCharge.setEndUserId(chargeAmountRequest.getEndUserId());

                                RefundAmountCharge.PaymentAmount objPaymentAmount = new RefundAmountCharge.PaymentAmount();

                                RefundAmountCharge.PaymentAmount.ChargingInformation objChargingInformation = new RefundAmountCharge.PaymentAmount.ChargingInformation();
                                objChargingInformation.setAmount(chargeAmountRequest.getAmount());
                                objChargingInformation.setCurrency(chargeAmountRequest.getCurrency());
                                objChargingInformation.setDescription(chargeAmountRequest.getDescription());
                                objChargingInformation.setCode(chargeAmountRequest.getCode());

                                RefundAmountCharge.PaymentAmount.ChargingMetaData objChargingMetaData = new RefundAmountCharge.PaymentAmount.ChargingMetaData();
                                objChargingMetaData.setOnBehalfOf(chargeAmountRequest.getOnBehalfOf());
                                objChargingMetaData.setPurchaseCategoryCode(chargeAmountRequest.getPurchaseCategoryCode());
                                objChargingMetaData.setChannel(chargeAmountRequest.getChannel());
                                objChargingMetaData.setTaxAmount(chargeAmountRequest.getTaxAmount());
                                objChargingMetaData.setMandateId(chargeAmountRequest.getMandateId());
                                objChargingMetaData.setProductId(chargeAmountRequest.getProductId());
                                objChargingMetaData.setServiceId(chargeAmountRequest.getServiceId());

                                objPaymentAmount.setChargingInformation(objChargingInformation);
                                objPaymentAmount.setChargingMetaData(objChargingMetaData);
                                objPaymentAmount.setTotalAmountRefunded(chargeAmountRequest.getTotalAmountCharged());

                                objRefundAmountCharge.setPaymentAmount(objPaymentAmount);
                                objRefundAmountCharge.setCallbackData(chargeAmountRequest.getCallbackData());
                                objRefundAmountCharge.setNotifyURL(chargeAmountRequest.getNotifyURL());
                                objRefundAmountCharge.setNotificationFormat(chargeAmountRequest.getNotificationFormat());
                                objRefundAmountCharge.setReferenceCode(chargeAmountRequest.getReferenceCode());
                                objRefundAmountCharge.setOriginalServerReferenceCode(chargeAmountRequest.getOriginalServerReferenceCode());
                                objRefundAmountCharge.setResourceURL(resourceURL);
                                objRefundAmountCharge.setTransactionOperationStatus(chargeAmountRequest.getTransactionOperationStatus());

                                ObjectMapper mapper = new ObjectMapper();
                                String jsonResponse = "{\"amountTransaction\":" + mapper.writeValueAsString(objRefundAmountCharge) + "}";
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
    }
}

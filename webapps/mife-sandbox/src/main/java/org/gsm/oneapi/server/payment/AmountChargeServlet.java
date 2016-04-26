package org.gsm.oneapi.server.payment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.dialog.mife.server.IHttpSession;
import org.dialog.mife.server.SessionManager;
import org.dialog.mife.util.RequestHandler;
import org.gsm.oneapi.responsebean.RequestError;
import org.gsm.oneapi.responseobject.payment.ChargeResponse;
import org.gsm.oneapi.server.ValidationRule;
import org.json.JSONObject;

/**
 * Servlet implementing the OneAPI function for the payment API - charging an
 * end user
 */
public class AmountChargeServlet extends PaymentServlet {

    static Logger logger = Logger.getLogger(AmountChargeServlet.class);
    private static final long serialVersionUID = -6237772242372106922L;
    private final String[] validationRules = {"payment", "*", "*", "transactions", "amount"};
    SessionManager sessionMgr = new SessionManager();

    public void init() throws ServletException {
        logger.debug("AmountChargeServlet initialised");
        sessionMgr.start();
    }
    //private AmountTransaction callbackData = null;
    private ChargeResponse callbackData = null;
    private String callbackURL = null;
    private PaymentImpl payimpl;

    public AmountChargeServlet() {
    }

    public AmountChargeServlet(ChargeResponse callbackData, String callbackURL) {
        this.callbackData = callbackData;
        this.callbackURL = callbackURL;
        //payimpl = new PaymentImpl();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        dumpRequestDetails(request, logger);

        String[] requestParts = getRequestParts(request);

        payimpl = new PaymentImpl();
        //Get user profile 
        //Implementation part of local DB :START
        String sandboxusr = request.getHeader("sandbox");
        User user = null;
        if (sandboxusr == null) {
            sandboxusr = getProfileIdFromRequest(request);
        }

        user = payimpl.getUser(sandboxusr);

        if (validateRequest(request, response, requestParts, validationRules)) {

            String endUserId = null;
            String transactionOperationStatus = null;
            String referenceCode = null;
            String description = null;
            String currency = null;
            Double amount = null;

            String clientCorrelator = null;
            String notifyURL = null;
            String onBehalfOf = null;
            String purchaseCategoryCode = null;
            String channel = null;
            Double taxAmount =0.0;

            String json = RequestHandler.getRequestJSON(request);  //getRequestJSON(request);
            logger.debug("Received request json string : " + json);
            try {
                JSONObject mainJSONObject = new JSONObject(json);
                JSONObject jsonObj = mainJSONObject.getJSONObject("amountTransaction");

                if (!jsonObj.isNull("clientCorrelator")) {
                    clientCorrelator = nullOrTrimmed(jsonObj.get("clientCorrelator").toString());
                }
                if (!jsonObj.isNull("endUserId")) {
                    endUserId = nullOrTrimmed(jsonObj.get("endUserId").toString());
                }

                if (!jsonObj.isNull("referenceCode")) {
                    referenceCode = nullOrTrimmed(jsonObj.get("referenceCode").toString());
                }
                if (!jsonObj.isNull("transactionOperationStatus")) {
                    transactionOperationStatus = nullOrTrimmed(jsonObj.get("transactionOperationStatus").toString());
                }

                JSONObject payAmount = jsonObj.getJSONObject("paymentAmount");


                JSONObject chargingInfo = (JSONObject) payAmount.get("chargingInformation");
                if (!chargingInfo.isNull("amount")) {
                    amount = Double.parseDouble(nullOrTrimmed(chargingInfo.get("amount").toString()));
                }
                if (!chargingInfo.isNull("currency")) {
                    currency = nullOrTrimmed(chargingInfo.get("currency").toString());
                }
                if (!chargingInfo.isNull("description")) {
                    description = nullOrTrimmed(chargingInfo.get("description").toString());
                }

                JSONObject chargingMetaData = payAmount.getJSONObject("chargingMetaData");
                if (!chargingMetaData.isNull("onBehalfOf")) {
                    onBehalfOf = nullOrTrimmed(chargingMetaData.get("onBehalfOf").toString());
                }
                if (!chargingMetaData.isNull("purchaseCategoryCode")) {
                    purchaseCategoryCode = nullOrTrimmed(chargingMetaData.get("purchaseCategoryCode").toString());
                }
                if (!chargingMetaData.isNull("channel")) {
                    channel = nullOrTrimmed(chargingMetaData.get("channel").toString());
                }
                if (!chargingMetaData.isNull("taxAmount")) {
                    if (!chargingMetaData.getString("taxAmount").equals("")) {
                        taxAmount = Double.parseDouble(nullOrTrimmed(chargingMetaData.get("taxAmount").toString()));
                    }
                }
                System.out.println("Manipulated recived JSON Object: " + json);

            } catch (Exception e) {
                System.out.println("Manipulating recived JSON Object: " + e);
            }

            ValidationRule[] rules = null;

            rules = new ValidationRule[]{
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, "endUserId", endUserId),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "transactionOperationStatus", transactionOperationStatus),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "referenceCode", referenceCode),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "description", description),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_CURRENCY, "currency", currency),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_DOUBLE_GT_ZERO, "amount", amount),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "clientCorrelator", clientCorrelator),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "onBehalfOf", onBehalfOf),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "purchaseCategoryCode", purchaseCategoryCode),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_PAYMENT_CHANNEL, "channel", channel),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL_DOUBLE_GE_ZERO, "taxAmount", taxAmount),};
                

            if (checkRequestParameters(response, rules)) {
                /*
                 * Generate response
                 */

                //MIFE sandbox Necessities
                String serverReferenceCode = null;
                ChargeAmountRequest chargeAmountRequest = null;

                chargeAmountRequest = payimpl.checkClientCorrelator(user.getId(), clientCorrelator);
                String userNum = getLastMobileNumber(endUserId);
                ManageNumber wlnumber = payimpl.getWhitelisted(user.getId(), userNum);
                Paymentparam payparam = payimpl.queryPaymentParam(user.getId());

                double totalAmount = amount + taxAmount;
                int maxTranCount = Integer.parseInt(payparam.getMaxtrn());

                if (Double.parseDouble(payparam.getMaxamt()) < totalAmount) {
                    sendError(response, BAD_REQUEST, RequestError.POLICYEXCEPTION, "POL1001", "The %1 operator charging limit for this user has been exceeded", "" + amount);
                } else if (maxTranCount <= payimpl.getTransactionCount(user.getId())) {
                    sendError(response, BAD_REQUEST, RequestError.POLICYEXCEPTION, "POL0001", "A policy error occurred. Error code is %1", "%1- Total transactions exceeds the operator limit");
                } else if (!transactionOperationStatus.equalsIgnoreCase(payparam.getPaystatus())) {
                    sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter transactionOperationStatus expected " + payparam.getPaystatus() + " received " + transactionOperationStatus);
                } else if (wlnumber != null) {
                    if (chargeAmountRequest == null) {
                        double openBalance = 0;
                        if (wlnumber.getBalance() != 0) {
                            openBalance = wlnumber.getBalance();
                        }

                        double sum = openBalance - totalAmount;
                        if (sum < 0) {
                            //Throw no succicient funds
                            sendError(response, BAD_REQUEST, RequestError.POLICYEXCEPTION, "POL1000", "User has insufficient credit for transaction", null);
                        } else {
                            //Saving transaction data in DB
                            serverReferenceCode = payimpl.saveChargeTransaction(user, clientCorrelator, endUserId, amount, currency, description, onBehalfOf, purchaseCategoryCode, channel, taxAmount, referenceCode, transactionOperationStatus, null, null, null, null, null, 0, null, null, totalAmount, 0.0, 1);
                            if (serverReferenceCode == null) {
                                sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0270", "Charging operation failed, the charge was not applied", null);
                            } else {
                                //Updating balance
                                payimpl.updateWhitelisted(wlnumber.getId(), Double.valueOf(new DecimalFormat("#.##").format(sum)));

                                String resourceURL = getRequestHostnameAndContext(request) + request.getServletPath() + "/payment/1/" + urlEncode(endUserId) + "/transactions/amount" + (serverReferenceCode != null ? ("/" + urlEncode(serverReferenceCode)) : "");

                                //Cresting JSON and send response
                                ChargeResponse cr = new ChargeResponse();
                                cr.setClientCorrelator(clientCorrelator);
                                cr.setEndUserId(endUserId);

                                ChargeResponse.PaymentAmount.ChargingInformation chargeInfo = new ChargeResponse.PaymentAmount.ChargingInformation();
                                chargeInfo.setAmount(amount);
                                chargeInfo.setCurrency(currency);
                                chargeInfo.setDescription(description);

                                ChargeResponse.PaymentAmount.ChargingMetaData chargeMetaData = new ChargeResponse.PaymentAmount.ChargingMetaData();
                                chargeMetaData.setChannel(channel);
                                chargeMetaData.setOnBehalfOf(onBehalfOf);
                                chargeMetaData.setPurchaseCategoryCode(purchaseCategoryCode);
                                chargeMetaData.setTaxAmount(taxAmount);

                                ChargeResponse.PaymentAmount payAmount = new ChargeResponse.PaymentAmount();
                                payAmount.setTotalAmountCharged(totalAmount);
                                payAmount.setChargingInformation(chargeInfo);
                                payAmount.setChargingMetaData(chargeMetaData);

                                cr.setPaymentAmount(payAmount);

                                cr.setReferenceCode(referenceCode);
                                //cr.setServerReferenceCode(SandboxRefID.getUniqueID());
                                cr.setServerReferenceCode(serverReferenceCode);
                                cr.setTransactionOperationStatus(payparam.getPaystatus());

                                ObjectMapper mapper = new ObjectMapper();
                                String jsonResponse = "{\"amountTransaction\":" + mapper.writeValueAsString(cr) + "}";

                                if (request.getHeader("authorization") != null) {
                                    response.setHeader("account", String.valueOf(updateSessionBalance(amount, request.getHeader("authorization"))));
                                }
                                logger.debug("Response json string : " + jsonResponse);
                                sendJSONResponse(response, jsonResponse, CREATED, resourceURL);
                            }
                        }
                    } else {
                        if (!endUserId.equals(chargeAmountRequest.getEndUserId())) {
                            sendError(response, FORBIDDEN, RequestError.POLICYEXCEPTION, "POL1006", "originalServerReferenceCode not valid", null);
                        } else if (chargeAmountRequest.getPaymentTranscationType() != 1) {
                            sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter clientCorrelator ");
                        } else {
                            serverReferenceCode = "src-" + chargeAmountRequest.getChargeId();
                            String resourceURL = getRequestHostnameAndContext(request) + request.getServletPath() + "/payment/1/" + urlEncode(endUserId) + "/transactions/amount" + (serverReferenceCode != null ? ("/" + urlEncode(serverReferenceCode)) : "");

                            //Cresting JSON and send response
                            ChargeResponse cr = new ChargeResponse();
                            cr.setClientCorrelator(chargeAmountRequest.getClientCorrelator());
                            cr.setEndUserId(chargeAmountRequest.getEndUserId());

                            ChargeResponse.PaymentAmount.ChargingInformation chargeInfo = new ChargeResponse.PaymentAmount.ChargingInformation();
                            chargeInfo.setAmount(chargeAmountRequest.getAmount());
                            chargeInfo.setCurrency(chargeAmountRequest.getCurrency());
                            chargeInfo.setDescription(chargeAmountRequest.getDescription());

                            ChargeResponse.PaymentAmount.ChargingMetaData chargeMetaData = new ChargeResponse.PaymentAmount.ChargingMetaData();
                            chargeMetaData.setChannel(chargeAmountRequest.getChannel());
                            chargeMetaData.setOnBehalfOf(chargeAmountRequest.getOnBehalfOf());
                            chargeMetaData.setPurchaseCategoryCode(chargeAmountRequest.getPurchaseCategoryCode());
                            chargeMetaData.setTaxAmount(chargeAmountRequest.getTaxAmount());

                            ChargeResponse.PaymentAmount payAmount = new ChargeResponse.PaymentAmount();
                            payAmount.setTotalAmountCharged(chargeAmountRequest.getTotalAmountCharged());
                            payAmount.setChargingInformation(chargeInfo);
                            payAmount.setChargingMetaData(chargeMetaData);

                            cr.setPaymentAmount(payAmount);

                            cr.setReferenceCode(chargeAmountRequest.getReferenceCode());
                            cr.setServerReferenceCode(serverReferenceCode);
                            cr.setTransactionOperationStatus(chargeAmountRequest.getTransactionOperationStatus());

                            ObjectMapper mapper = new ObjectMapper();
                            String jsonResponse = "{\"amountTransaction\":" + mapper.writeValueAsString(cr) + "}";

                            if (request.getHeader("authorization") != null) {
                                response.setHeader("account", String.valueOf(updateSessionBalance(amount, request.getHeader("authorization"))));
                            }
                            logger.debug("Response json string : " + jsonResponse);
                            sendJSONResponse(response, jsonResponse, CREATED, resourceURL);
                        }
                    }
                } else {
                    //Throw error for not available in whitelist!
                    sendError(response, BAD_REQUEST, RequestError.POLICYEXCEPTION, "POL1009", "User has not been provisioned for Charge Amount", null);
                }
                //Implementation part of local DB :END
            }
        }
    }

    private double updateSessionBalance(Double amount, String authHeader) {
        String[] authParts = authHeader.split(" ", 2);
        String sessionKey = authParts[1];

        IHttpSession hSession = sessionMgr.getSession(sessionKey, "ussd", true);

        double availableBalance;
        if (hSession.getAttribute("account") != null) {
            availableBalance = Double.parseDouble(hSession.getAttribute("account").toString());
        } else {
            availableBalance = 0.0;
        }
        double newBalance = availableBalance - amount;
        logger.debug("Updated credit balance:: " + newBalance);

        hSession.setAttribute("account", newBalance);

        return newBalance;
    }

    private String getRequestJSON(HttpServletRequest request) {
        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            System.out.println("Error in reading RequestJSON: " + ex);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    System.out.println("Error in BufferReader: " + ex);
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }
}

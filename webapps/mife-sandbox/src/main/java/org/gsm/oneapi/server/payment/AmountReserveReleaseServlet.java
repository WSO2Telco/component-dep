package org.gsm.oneapi.server.payment;

import java.io.IOException;
import java.text.DecimalFormat;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mife.sandbox.model.PaymentImpl;
import mife.sandbox.model.entities.ManageNumber;
import mife.sandbox.model.entities.PaymentTransaction;
import mife.sandbox.model.entities.Paymentparam;
import mife.sandbox.model.entities.User;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.dialog.mife.responsebean.payment.ReleaseAmountReservationTransaction;
import org.dialog.mife.server.SessionManager;
import org.dialog.mife.util.RequestHandler;
import org.gsm.oneapi.responsebean.RequestError;
import static org.gsm.oneapi.server.OneAPIServlet.getLastMobileNumber;
import static org.gsm.oneapi.server.OneAPIServlet.getProfileIdFromRequest;
import static org.gsm.oneapi.server.OneAPIServlet.sendError;
import org.gsm.oneapi.server.ValidationRule;
import static org.gsm.oneapi.server.payment.AmountReserveChargeServlet.logger;
import org.json.JSONObject;

/**
 * Servlet implementing the OneAPI function for the payment API - releasing a
 * reservation amount on the end user's account
 */
public class AmountReserveReleaseServlet extends PaymentServlet {

    static Logger logger = Logger.getLogger(AmountReserveReleaseServlet.class);

    private static final long serialVersionUID = -6237772242372106922L;

    private final String[] validationRules = {"payment", "1", "*", "transactions", "amountReservation", "*"};
    SessionManager sessionMgr = new SessionManager();

    public void init() throws ServletException {
        logger.debug("AmountReserveReleaseServlet initialised");
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
            int referenceSequence = 0;
            String transactionOperationStatus = null;

            String requestString = RequestHandler.getRequestJSON(request);

            try {
                JSONObject objJSONObject = new JSONObject(requestString);
                JSONObject objAmountReservationTransaction = (JSONObject) objJSONObject.get("amountReservationTransaction");

                if (objAmountReservationTransaction.get("endUserId") != null) {

                    endUserId = nullOrTrimmed(objAmountReservationTransaction.get("endUserId").toString());
                }
                if (objAmountReservationTransaction.get("referenceSequence") != null) {

                    referenceSequence = Integer.parseInt(nullOrTrimmed(objAmountReservationTransaction.get("referenceSequence").toString()));
                }
                if (objAmountReservationTransaction.get("transactionOperationStatus") != null) {

                    transactionOperationStatus = nullOrTrimmed(objAmountReservationTransaction.get("transactionOperationStatus").toString());
                }
            } catch (Exception e) {
                logger.error("Manipulating recived JSON Object: " + e);
            }

            ValidationRule[] rules = {
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID, "endUserId", endUserId),
             // new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "transactionOperationStatus", transactionOperationStatus, "released"),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY, "transactionOperationStatus",transactionOperationStatus),
                new ValidationRule(ValidationRule.VALIDATION_TYPE_OPTIONAL, "referenceSequence", Integer.valueOf(referenceSequence)),};

            if (checkRequestParameters(response, rules)) {
                String referenceCode = "REF-12346";

                Double reservedAmount = 0.0;
                String previousEndUserId = null;
                String transactionCurrency = null;
                String userNum = getLastMobileNumber(endUserId);
                ManageNumber wlnumber = payimpl.getWhitelisted(user.getId(), userNum);
                Paymentparam payparam = payimpl.queryPaymentParam(user.getId());

                PaymentTransaction transactionDetails = payimpl.getPreviousTransactionDetailsByTransactionId(transactionId);
                if (transactionDetails != null) {
                    reservedAmount = transactionDetails.getAmount();
                    transactionCurrency = transactionDetails.getCurrency();
                    previousEndUserId = transactionDetails.getEndUserId();
                }

                if (wlnumber != null) {
                    double openBalance = 0;
                    double openReservedAmount = 0;

                    if (wlnumber.getBalance() != 0) {
                        openBalance = wlnumber.getBalance();
                        openReservedAmount = wlnumber.getReserved_amount();
                    }

                    if (transactionDetails == null) {
                        sendError(response, FORBIDDEN, RequestError.POLICYEXCEPTION, "POL1006", "originalServerReferenceCode not valid", null);
                    } else if (!endUserId.equals(previousEndUserId)) {
                        sendError(response, FORBIDDEN, RequestError.POLICYEXCEPTION, "POL1006", "originalServerReferenceCode not valid", null);
                    }else if (!transactionOperationStatus.equalsIgnoreCase(payparam.getPaystatus())) {
                        sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0002", "Invalid input value for message part %1", "Parameter transactionOperationStatus expected " + payparam.getPaystatus() + " received " + transactionOperationStatus);}
                     else {
                        double currentReservedAmount = openReservedAmount - reservedAmount;
                        openBalance = openBalance + reservedAmount;                        

                        //Saving transaction data in DB
                        String paymentTransactionId = payimpl.saveReleasedReservationTransaction(user, null, endUserId, 0.0, null, null, null, null, null, 0.0, null, transactionOperationStatus, null, null, null, null, null, referenceSequence, null, null, 0.0, 0.0, 5, transactionId);

                        if (paymentTransactionId == null) {
                            sendError(response, BAD_REQUEST, RequestError.SERVICEEXCEPTION, "SVC0270", "Charging operation failed, the charge was not applied", null);
                        } else {
                            payimpl.updateWhitelisted(wlnumber.getId(), Double.valueOf(new DecimalFormat("#.##").format(openBalance)), Double.valueOf(new DecimalFormat("#.##").format(currentReservedAmount)));
                            
                            String resourceURL = getRequestHostnameAndContext(request) + request.getServletPath() + "/payment/1/" + urlEncode(endUserId) + "/transactions/amountReservation" + (transactionId != null ? ("/" + urlEncode(transactionId)) : "");

                            ReleaseAmountReservationTransaction objReleaseAmountReservationTransaction = new ReleaseAmountReservationTransaction();
                            objReleaseAmountReservationTransaction.setEndUserId(endUserId);

                            ReleaseAmountReservationTransaction.PaymentAmount objPaymentAmount = new ReleaseAmountReservationTransaction.PaymentAmount();
                            objPaymentAmount.setAmountReserved(0.0);

                            ReleaseAmountReservationTransaction.PaymentAmount.ChargingInformation objChargingInformation = new ReleaseAmountReservationTransaction.PaymentAmount.ChargingInformation();
                            objChargingInformation.setAmount(reservedAmount);
                            objChargingInformation.setCurrency(transactionCurrency);

                            objPaymentAmount.setChargingInformation(objChargingInformation);
                            objPaymentAmount.setTotalAmountCharged(0.0);

                            objReleaseAmountReservationTransaction.setPaymentAmount(objPaymentAmount);
                            objReleaseAmountReservationTransaction.setReferenceCode(referenceCode);
                            objReleaseAmountReservationTransaction.setReferenceSequence(referenceSequence);
                            objReleaseAmountReservationTransaction.setResourceURL(resourceURL);
                            objReleaseAmountReservationTransaction.setTransactionOperationStatus(transactionOperationStatus);

                            ObjectMapper mapper = new ObjectMapper();
                            String jsonResponse = "{\"amountReservationTransaction\":" + mapper.writeValueAsString(objReleaseAmountReservationTransaction) + "}";
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

}

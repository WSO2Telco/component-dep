/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mife.sandbox.view;

import java.io.IOException;
import java.text.ParseException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mife.sandbox.model.SmsImpl;
import org.apache.log4j.Logger;
import org.dialog.mife.util.RequestHandler;
import static org.gsm.oneapi.server.OneAPIServlet.nullOrTrimmed;
import org.json.JSONObject;

/**
 *
 * @author User
 */
public class Sandboxcontroller extends HttpServlet {

    static Logger logger = Logger.getLogger(Sandboxcontroller.class);
    SmsImpl smsimpl;

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        APIRequestWrapper objAPIRequestWrapper = new APIRequestWrapper(request);

        String requestURL = request.getRequestURL().toString();
        logger.debug("requestURL : " + requestURL);

        String apitype = findAPIType(requestURL);
        logger.debug("apitype : " + apitype);

        String forwardto = null;

        try {

            if (apitype.equalsIgnoreCase("send_sms")) {
                forwardto = "/SendSMSService";
            } else if (apitype.equalsIgnoreCase("retrive_sms_subscriptions")) {
                if (request.getMethod().equalsIgnoreCase("DELETE")) {
                    forwardto = "/CancelSMSReceiptService";
                } else {
                    forwardto = "/SMSReceiptService";
                }
            } else if (apitype.equalsIgnoreCase("retrive_sms")) {
                forwardto = "/RetrieveSMSService";
            } else if (apitype.equalsIgnoreCase("query_sms_delivery")) {
                forwardto = "/QuerySMSService";
            } else if (apitype.equalsIgnoreCase("start_delivery_subscription")) {
                forwardto = "/SMSDeliveryService";
            } else if (apitype.equalsIgnoreCase("stop_delivery_subscription")) {
                forwardto = "/CancelSMSDeliveryService";
            } else if (apitype.equalsIgnoreCase("payment")) {
                forwardto = findPaymentAPIType(requestURL, objAPIRequestWrapper);
            } else if (apitype.equalsIgnoreCase("location")) {
                forwardto = "/LocationService";
            } else if (apitype.equalsIgnoreCase("ussd_send")) {
                forwardto = "/UssdSendService";
            } else if (apitype.equalsIgnoreCase("ussd_receive")) {
                forwardto = "/UssdReceiveService";
            } else if (apitype.equalsIgnoreCase("ussd_subscription")) {
                forwardto = "/UssdSubscriptionService";
            } else if (apitype.equalsIgnoreCase("stop_ussd_subscription")) {
                forwardto = "/CancelUssdSubscriptionService";
            } else {
                throw new Exception("API Type Not found");
            }
            request.getRequestDispatcher(forwardto + "/" + request.getPathInfo()).forward(objAPIRequestWrapper, response);

        } catch (Exception e) {
            logger.error("Request error: " + e);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private String findAPIType(String ResourceURL) {

        String apiType = null;

        String paymentKeyString = "transactions";
        String sendSMSkeyString = "outbound";
        String sendSMSkeyStringRequest = "requests";
        String retriveSMSString = "inbound";
        String subscriptionKeyString = "subscriptions";
        String regKeyString = "registrations";
        String delivaryInfoKeyString = "deliveryInfos";
        String delivaryNotifyString = "DeliveryInfoNotification";
        String locationString = "location";
        String ussdKeyString = "ussd";


        String lastWord = ResourceURL.substring(ResourceURL.lastIndexOf("/") + 1);

        if (ResourceURL.toLowerCase().contains(sendSMSkeyString.toLowerCase())
                && ResourceURL.toLowerCase().contains(sendSMSkeyStringRequest.toLowerCase())
                && (!lastWord.equals(delivaryInfoKeyString))) {
            apiType = "send_sms";
        } else if (ResourceURL.toLowerCase().contains(sendSMSkeyString.toLowerCase())
                && lastWord.equals(subscriptionKeyString)) {
            apiType = "start_delivery_subscription";
        } else if (ResourceURL.toLowerCase().contains(sendSMSkeyString.toLowerCase())
                && ResourceURL.toLowerCase().contains(subscriptionKeyString.toLowerCase())
                && (!lastWord.equals(subscriptionKeyString))) {
            apiType = "stop_delivery_subscription";
        } else if (lastWord.equals(delivaryInfoKeyString)) {
            apiType = "query_sms_delivery";
        } else if (ResourceURL.toLowerCase().contains(retriveSMSString.toLowerCase())
                && ResourceURL.toLowerCase().contains(regKeyString.toLowerCase())) {
            apiType = "retrive_sms";
        } else if (ResourceURL.toLowerCase().contains(retriveSMSString.toLowerCase())
                && ResourceURL.toLowerCase().contains(subscriptionKeyString.toLowerCase())) {
            apiType = "retrive_sms_subscriptions";
        } else if (ResourceURL.toLowerCase().contains(paymentKeyString.toLowerCase())) {
            apiType = "payment";
        } else if (ResourceURL.toLowerCase().contains(delivaryNotifyString.toLowerCase())) {
            apiType = "sms_inbound_notifications";
        } else if (ResourceURL.toLowerCase().contains(locationString.toLowerCase())) {
            apiType = "location";
        } else if (ResourceURL.toLowerCase().contains(ussdKeyString.toLowerCase())) {
            //apiType = "location";
            if(ResourceURL.toLowerCase().contains("outbound")){
                apiType = "ussd_send";
            } else if (!ResourceURL.toLowerCase().contains(subscriptionKeyString.toLowerCase())){
                apiType = "ussd_receive";
            } else {
                if (lastWord.equals(subscriptionKeyString.toLowerCase())){
                    apiType = "ussd_subscription";
                } else {
                    apiType = "stop_ussd_subscription";
                }
            }
        } else {
            return null;
        }

        return apiType;
    }

    private String findPaymentAPIType(String requestURL, HttpServletRequest servletRequest) throws Exception {
        String transactionOperationStatus = null;
        String requestString = null;
        String forwardServlet = null;

        if (requestURL.contains("amountReservation")) {
            logger.debug("Amount reservation transaction");
            String parts[] = requestURL.split("/transactions/");
            String urlParts[] = parts[1].split("/");
            logger.debug("Payment url parts : " + urlParts.length);

            if (urlParts.length == 1) {
                logger.debug("Reserve an amount to charge");
                forwardServlet = "/AmountReserveService";
            } else if (urlParts.length == 2) {
                requestString = RequestHandler.getRequestJSON(servletRequest);
                logger.debug("Json string : " + requestString);
                JSONObject objJSONObject;
                try {
                    objJSONObject = new JSONObject(requestString);
                    JSONObject objAmountReservationTransaction = (JSONObject) objJSONObject.get("amountReservationTransaction");

                    if (objAmountReservationTransaction.get("transactionOperationStatus") != null) {
                        transactionOperationStatus = nullOrTrimmed(objAmountReservationTransaction.get("transactionOperationStatus").toString());
                        logger.debug("Transaction operation status" + transactionOperationStatus);
                        if (transactionOperationStatus.equalsIgnoreCase("Reserved")) {
                            logger.debug("Reserve an additional amount");
                            forwardServlet = "/AmountReserveAdditionalService";
                        } else if (transactionOperationStatus.equalsIgnoreCase("Charged")) {
                            logger.debug("Charge against the reservation");
                            forwardServlet = "/AmountReserveChargeService";
                        } else if (transactionOperationStatus.equalsIgnoreCase("Released")) {
                            logger.debug("Release the reservation");
                            forwardServlet = "/AmountReserveReleaseService";
                        } else {
                            logger.debug("API Type Not found");
                            throw new Exception("API Type Not found");
                        }
                    } else {
                        logger.debug("API Type Not found");
                        throw new Exception("API Type Not found");
                    }
                } catch (Exception ex) {
                    logger.error("Sandbox controller - Manipulating recived JSON Object: " + ex);
                    throw new Exception("API Type Not found");
                }
            } else {
                logger.debug("API Type Not found");
                throw new Exception("API Type Not found");
            }
        } else if (requestURL.contains("amount")) {
            requestString = RequestHandler.getRequestJSON(servletRequest);
            logger.debug("Json string : " + requestString);
            JSONObject objJSONObject = new JSONObject(requestString);
            try {
                JSONObject objAmountTransaction = (JSONObject) objJSONObject.get("amountTransaction");
                if (objAmountTransaction.get("transactionOperationStatus") != null) {
                    transactionOperationStatus = nullOrTrimmed(objAmountTransaction.get("transactionOperationStatus").toString());
                    logger.debug("Transaction operation status" + transactionOperationStatus);
                    if (transactionOperationStatus.equalsIgnoreCase("Charged")) {
                        logger.debug("Charge a user");
                        forwardServlet = "/AmountChargeService";
                    } else if (transactionOperationStatus.equalsIgnoreCase("Refunded")) {
                        logger.debug("Refund a user");
                        forwardServlet = "/AmountRefundService";
                    } else {
                        logger.debug("API Type Not found");
                        throw new Exception("API Type Not found");
                    }
                } else {
                    logger.debug("API Type Not found");
                    throw new Exception("API Type Not found");
                }
            } catch (Exception e) {
                logger.error("Sandbox controller - Manipulating recived JSON Object: " + e);
                throw new Exception("API Type Not found");
            }
        } else if (requestURL.contains("transactions")) {
            logger.debug("List all transactions");
            forwardServlet = "/ListTransactionsService";
        } else {
            logger.debug("API Type Not found");
            throw new Exception("API Type Not found");
        }
        return forwardServlet;
    }
}

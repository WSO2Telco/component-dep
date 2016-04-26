/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mife.sandbox.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mife.sandbox.controller.TransactionFunctions;
import mife.sandbox.model.SmsImpl;
import mife.sandbox.model.entities.ChargeAmountRequest;
import mife.sandbox.model.entities.LocationRequestLog;
import mife.sandbox.model.entities.SMSRequestLog;
import mife.sandbox.model.entities.User;
import mife.sandbox.model.entities.UssdSubscriptions;
import mife.sandbox.model.entities.UssdTransaction;

/**
 *
 * @author User
 */
public class TransactionsServlet extends HttpServlet {

    SmsImpl smsimpl;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet TransactionsController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet TransactionsController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String type = request.getParameter("type").toString().trim();
        smsimpl = new SmsImpl();
        String userid = request.getHeader("userid").trim();
        int iuser = ((User) smsimpl.getUser(userid)).getId();

        try {
            if (type.equals("1")) {
                generateSendSMSTransactionGrid(request, response, iuser);
            } else if (type.equals("2")) {
                generateRetrieveSMSMessagesTransactionGrid(request, response, iuser);
            } else if (type.equals("3")) {
                generateSMSSubscriptionTransactionGrid(request, response, iuser);
            } else if (type.equals("4")) {
                generateCancelSubscriptionTransactionGrid(request, response, iuser);
            } else if (type.equals("5")) {
                generateChargeAmountTransactionGrid(request, response, iuser);
            } else if (type.equals("6")) {
                generateReserveAmountTransactionGrid(request, response, iuser);
            } else if (type.equals("7")) {
                generateReserveAdditionalAmountTransactionGrid(request, response, iuser);
            } else if (type.equals("8")) {
                generateReserveChargedAmountTransactionGrid(request, response, iuser);
            } else if (type.equals("9")) {
                generateReserveAmountReleaseTransactionGrid(request, response, iuser);
            } else if (type.equals("10")) {
                generateRefundTransactionGrid(request, response, iuser);
            } else if (type.equals("11")) {
                generateLocationRequestTransactionGrid(request, response, iuser);
            } else if(type.equals("12")){
                generateUssdSendGrid(request, response, iuser);
            } else if(type.equals("13")){
                generateUssdReceiveGrid(request, response, iuser);
            } else if(type.equals("14")){
                generateUssdSubscriptionGrid(request, response, iuser);
            }
        } catch (ParseException e) {
            System.out.println("ParseException: " + e);
        }
    }

    private void generateSendSMSTransactionGrid(HttpServletRequest request, HttpServletResponse response, int iuser) throws IOException, ParseException {
        /*System.out.println("from Date in request as string: " + request.getParameter("from_date").toString().trim());
         System.out.println("to Date in request as string: " + request.getParameter("to_date").toString().trim());*/
        //int userId = Integer.parseInt(request.getHeader("User-Id").replaceAll("\\s",""));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = dateFormat.parse(request.getParameter("from_date").toString().trim());
        //System.out.println("from Date in request as date: " + fromDate);
        Date toDate = dateFormat.parse(request.getParameter("to_date").toString().trim());
        //System.out.println("to Date in request as date: " + toDate);
        int txType = 1;
        List<SMSRequestLog> smsLogArray = TransactionFunctions.getSMSLogTransactions(iuser, fromDate, toDate, txType);

        if (smsLogArray.size() > 0) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table id=\"transaction_table\" class=\"table table-bordered table-striped\">");
            response.getWriter().write("<thead>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<th>Transaction Id</th>");
            response.getWriter().write("<th>Sender Address</th>");
            response.getWriter().write("<th>Addresses</th>");
            response.getWriter().write("<th>Message</th>");
            response.getWriter().write("<th>Client Correlator</th>");
            response.getWriter().write("<th>Sender Name</th>");
            response.getWriter().write("<th>Notify URL</th>");
            response.getWriter().write("<th>Callback Data</th>");
            response.getWriter().write("<th>Created Date</th>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</thead>");

            response.getWriter().write("<tbody>");
            for (int i = 0; i < smsLogArray.size(); i++) {
                SMSRequestLog sendSMSLog = smsLogArray.get(i);
                response.getWriter().write("<tr>");
                response.getWriter().write("<td>" + (i + 1) + "</td>");
                if (sendSMSLog.getSenderAddress() != null) {
                    response.getWriter().write("<td>" + sendSMSLog.getSenderAddress() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (sendSMSLog.getAddresses() != null) {
                    response.getWriter().write("<td>" + sendSMSLog.getAddresses() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (sendSMSLog.getMessage() != null) {
                    response.getWriter().write("<td>" + sendSMSLog.getMessage() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (sendSMSLog.getClientCorrelator() != null) {
                    response.getWriter().write("<td>" + sendSMSLog.getClientCorrelator() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (sendSMSLog.getSenderName() != null) {
                    response.getWriter().write("<td>" + sendSMSLog.getSenderName() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (sendSMSLog.getNotifyURL() != null) {
                    response.getWriter().write("<td>" + sendSMSLog.getNotifyURL() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (sendSMSLog.getCallbackData() != null) {
                    response.getWriter().write("<td>" + sendSMSLog.getCallbackData() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (sendSMSLog.getDate() != null) {
                    response.getWriter().write("<td>" + sendSMSLog.getDate() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                response.getWriter().write("</tr>");
            }
            response.getWriter().write("</tbody>");

            response.getWriter().write("</table>");
        } else {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table id=\"transaction_table\" class=\"table table-bordered table-striped\">");
            response.getWriter().write("<thead>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<th>Transaction Id</th>");
            response.getWriter().write("<th>Sender Address</th>");
            response.getWriter().write("<th>Addresses</th>");
            response.getWriter().write("<th>Message</th>");
            response.getWriter().write("<th>Client Correlator</th>");
            response.getWriter().write("<th>Sender Name</th>");
            response.getWriter().write("<th>Notify URL</th>");
            response.getWriter().write("<th>Callback Data</th>");
            response.getWriter().write("<th>Created Date</th>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</thead>");

            response.getWriter().write("<tbody>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<td colspan = '9' style='text-align:center;'><h2>No data found for query</h2></td>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</tbody>");
            response.getWriter().write("</table>");
        }
    }
    
    private void generateUssdSendGrid(HttpServletRequest request, HttpServletResponse response, int iuser) throws IOException, ParseException {
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = dateFormat.parse(request.getParameter("from_date").toString().trim());
        Date toDate = dateFormat.parse(request.getParameter("to_date").toString().trim());
        int txType = 1;
        List<UssdTransaction> ussdTxArray = TransactionFunctions.getUssdTransactions(iuser, txType, fromDate, toDate);

        if (ussdTxArray.size() > 0) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table id=\"transaction_table\" class=\"table table-bordered table-striped\">");
            response.getWriter().write("<thead>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<th>Id</th>");
            response.getWriter().write("<th>Address</th>");
            response.getWriter().write("<th>Short Code</th>");
            response.getWriter().write("<th>Keyword</th>");
            response.getWriter().write("<th>Outbound USSD Message</th>");
            response.getWriter().write("<th>Client Correlator</th>");
            response.getWriter().write("<th>Notify URL</th>");
            response.getWriter().write("<th>Callback Data</th>");
            response.getWriter().write("<th>USSD Action</th>");
            response.getWriter().write("<th>Created Date</th>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</thead>");

            response.getWriter().write("<tbody>");
            for (int i = 0; i < ussdTxArray.size(); i++) {
                UssdTransaction ussdTx = ussdTxArray.get(i);
                response.getWriter().write("<tr>");
                response.getWriter().write("<td>" + (i + 1) + "</td>");
                if (ussdTx.getAddress() != null) {
                    response.getWriter().write("<td>" + ussdTx.getAddress() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (ussdTx.getShortCode() != null) {
                    response.getWriter().write("<td>" + ussdTx.getShortCode() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (ussdTx.getKeyword() != null) {
                    response.getWriter().write("<td>" + ussdTx.getKeyword() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (ussdTx.getMessage() != null) {
                    response.getWriter().write("<td>" + ussdTx.getMessage() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (ussdTx.getClientCorrelator() != null) {
                    response.getWriter().write("<td>" + ussdTx.getClientCorrelator() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (ussdTx.getNotifyUrl() != null) {
                    response.getWriter().write("<td>" + ussdTx.getNotifyUrl() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (ussdTx.getCallbackData() != null) {
                    response.getWriter().write("<td>" + ussdTx.getCallbackData() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (ussdTx.getUssdAction() != null) {
                    response.getWriter().write("<td>" + ussdTx.getUssdAction() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (ussdTx.getDate() != null) {
                    response.getWriter().write("<td>" + ussdTx.getDate() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                response.getWriter().write("</tr>");
            }
            response.getWriter().write("</tbody>");
            response.getWriter().write("</table>");
        } else {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table id=\"transaction_table\" class=\"table table-bordered table-striped\">");
            response.getWriter().write("<thead>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<th>Id</th>");
            response.getWriter().write("<th>Address</th>");
            response.getWriter().write("<th>Short Code</th>");
            response.getWriter().write("<th>Keyword</th>");
            response.getWriter().write("<th>Inbound USSD Message</th>");
            response.getWriter().write("<th>Client Correlator</th>");
            response.getWriter().write("<th>Notify URL</th>");
            response.getWriter().write("<th>Callback Data</th>");
            response.getWriter().write("<th>USSD Action</th>");
            response.getWriter().write("<th>Created Date</th>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</thead>");
            response.getWriter().write("<tbody>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<td colspan = '13' style='text-align:center;'><h2>No data found for query</h2></td>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</tbody>");
            response.getWriter().write("</table>");
        }
    }
    
    
    private void generateUssdReceiveGrid(HttpServletRequest request, HttpServletResponse response, int iuser) throws IOException, ParseException {
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = dateFormat.parse(request.getParameter("from_date").toString().trim());
        Date toDate = dateFormat.parse(request.getParameter("to_date").toString().trim());
        int txType = 2;
        List<UssdTransaction> ussdTxArray = TransactionFunctions.getUssdTransactions(iuser, txType, fromDate, toDate);

        if (ussdTxArray.size() > 0) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table id=\"transaction_table\" class=\"table table-bordered table-striped\">");
            response.getWriter().write("<thead>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<th>Id</th>");
            response.getWriter().write("<th>Address</th>");
            response.getWriter().write("<th>Short Code</th>");
            response.getWriter().write("<th>Keyword</th>");
            response.getWriter().write("<th>Inbound USSD Message</th>");
            response.getWriter().write("<th>Client Correlator</th>");
            response.getWriter().write("<th>Notify URL</th>");
            response.getWriter().write("<th>Callback Data</th>");
            response.getWriter().write("<th>USSD Action</th>");
            response.getWriter().write("<th>Created Date</th>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</thead>");

            response.getWriter().write("<tbody>");
            for (int i = 0; i < ussdTxArray.size(); i++) {
                UssdTransaction ussdTx = ussdTxArray.get(i);
                response.getWriter().write("<tr>");
                response.getWriter().write("<td>" + (i + 1) + "</td>");
                if (ussdTx.getAddress() != null) {
                    response.getWriter().write("<td>" + ussdTx.getAddress() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (ussdTx.getShortCode() != null) {
                    response.getWriter().write("<td>" + ussdTx.getShortCode() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (ussdTx.getKeyword() != null) {
                    response.getWriter().write("<td>" + ussdTx.getKeyword() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (ussdTx.getMessage() != null) {
                    response.getWriter().write("<td>" + ussdTx.getMessage() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (ussdTx.getClientCorrelator() != null) {
                    response.getWriter().write("<td>" + ussdTx.getClientCorrelator() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (ussdTx.getNotifyUrl() != null) {
                    response.getWriter().write("<td>" + ussdTx.getNotifyUrl() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (ussdTx.getCallbackData() != null) {
                    response.getWriter().write("<td>" + ussdTx.getCallbackData() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (ussdTx.getUssdAction() != null) {
                    response.getWriter().write("<td>" + ussdTx.getUssdAction() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (ussdTx.getDate() != null) {
                    response.getWriter().write("<td>" + ussdTx.getDate() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                response.getWriter().write("</tr>");
            }
            response.getWriter().write("</tbody>");
            response.getWriter().write("</table>");
        } else {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table id=\"transaction_table\" class=\"table table-bordered table-striped\">");
            response.getWriter().write("<thead>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<th>Id</th>");
            response.getWriter().write("<th>Address</th>");
            response.getWriter().write("<th>Short Code</th>");
            response.getWriter().write("<th>Keyword</th>");
            response.getWriter().write("<th>Inbound USSD Message</th>");
            response.getWriter().write("<th>Client Correlator</th>");
            response.getWriter().write("<th>Notify URL</th>");
            response.getWriter().write("<th>Callback Data</th>");
            response.getWriter().write("<th>USSD Action</th>");
            response.getWriter().write("<th>Created Date</th>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</thead>");
            response.getWriter().write("<tbody>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<td colspan = '13' style='text-align:center;'><h2>No data found for query</h2></td>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</tbody>");
            response.getWriter().write("</table>");
        }
    }
    
    private void generateUssdSubscriptionGrid(HttpServletRequest request, HttpServletResponse response, int iuser) throws IOException, ParseException {
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = dateFormat.parse(request.getParameter("from_date").toString().trim());
        Date toDate = dateFormat.parse(request.getParameter("to_date").toString().trim());
        List<UssdSubscriptions> ussdSubArray = TransactionFunctions.getUssdSubscriptions(iuser, fromDate, toDate);

        if (ussdSubArray.size() > 0) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table id=\"transaction_table\" class=\"table table-bordered table-striped\">");
            response.getWriter().write("<thead>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<th>Id</th>");
            response.getWriter().write("<th>Client Correlator</th>");
            response.getWriter().write("<th>Destination Address</th>");
            response.getWriter().write("<th>Resource URL</th>");
            response.getWriter().write("<th>Callback Data</th>");
            response.getWriter().write("<th>Notify URL</th>");
            response.getWriter().write("<th>Created Date</th>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</thead>");

            response.getWriter().write("<tbody>");
            for (int i = 0; i < ussdSubArray.size(); i++) {
                UssdSubscriptions ussdSub = ussdSubArray.get(i);
                response.getWriter().write("<tr>");
                response.getWriter().write("<td>" + (i + 1) + "</td>");
                if (ussdSub.getClientCorrelator() != null) {
                    response.getWriter().write("<td>" + ussdSub.getClientCorrelator() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (ussdSub.getDestinationAddress() != null) {
                    response.getWriter().write("<td>" + ussdSub.getDestinationAddress() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (ussdSub.getResourceUrl() != null) {
                    response.getWriter().write("<td>" + ussdSub.getResourceUrl() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (ussdSub.getCallbackData() != null) {
                    response.getWriter().write("<td>" + ussdSub.getCallbackData() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (ussdSub.getNotifyUrl() != null) {
                    response.getWriter().write("<td>" + ussdSub.getNotifyUrl() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (ussdSub.getCreatedDate() != null) {
                    response.getWriter().write("<td>" + ussdSub.getCreatedDate() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                response.getWriter().write("</tr>");
            }
            response.getWriter().write("</tbody>");
            response.getWriter().write("</table>");
        } else {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table id=\"transaction_table\" class=\"table table-bordered table-striped\">");
            response.getWriter().write("<thead>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<th>Id</th>");
            response.getWriter().write("<th>Client Correlator</th>");
            response.getWriter().write("<th>Destination Address</th>");
            response.getWriter().write("<th>Resource URL</th>");
            response.getWriter().write("<th>Callback Data</th>");
            response.getWriter().write("<th>Notify URL</th>");
            response.getWriter().write("<th>Created Date</th>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</thead>");
            response.getWriter().write("<tbody>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<td colspan = '13' style='text-align:center;'><h2>No data found for query</h2></td>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</tbody>");
            response.getWriter().write("</table>");
        }
    }

    private void generateChargeAmountTransactionGrid(HttpServletRequest request, HttpServletResponse response, int iuser) throws IOException, ParseException {
        //int userId = Integer.parseInt(request.getHeader("User-Id").replaceAll("\\s",""));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = dateFormat.parse(request.getParameter("from_date").toString().trim());
        Date toDate = dateFormat.parse(request.getParameter("to_date").toString().trim());
        int txType = 1;
        List<ChargeAmountRequest> chargeAmountRequestArray = TransactionFunctions.getPaymentTransactions(iuser, txType, fromDate, toDate);

        if (chargeAmountRequestArray.size() > 0) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table id=\"transaction_table\" class=\"table table-bordered table-striped\">");
            response.getWriter().write("<thead>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<th>Id</th>");
            response.getWriter().write("<th>End User Id</th>");
            response.getWriter().write("<th>Status</th>");
            response.getWriter().write("<th>Reference Code</th>");
            response.getWriter().write("<th>Description</th>");
            response.getWriter().write("<th>Currency</th>");
            response.getWriter().write("<th>Amount</th>");
            response.getWriter().write("<th>Client Correlator</th>");
            response.getWriter().write("<th>On Behalf of</th>");
            response.getWriter().write("<th>Purchase Category Code</th>");
            response.getWriter().write("<th>Channel</th>");
            response.getWriter().write("<th>Tax Amount</th>");
            response.getWriter().write("<th>Created Date</th>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</thead>");

            response.getWriter().write("<tbody>");
            for (int i = 0; i < chargeAmountRequestArray.size(); i++) {
                ChargeAmountRequest chargeAmountRequest = chargeAmountRequestArray.get(i);
                response.getWriter().write("<tr>");
                response.getWriter().write("<td>" + (i + 1) + "</td>");
                if (chargeAmountRequest.getEndUserId() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getEndUserId() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getTransactionOperationStatus() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getTransactionOperationStatus() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getReferenceCode() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getReferenceCode() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getDescription() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getDescription() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getCurrency() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getCurrency() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getAmount() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getAmount() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getClientCorrelator() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getClientCorrelator() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getOnBehalfOf() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getOnBehalfOf() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getPurchaseCategoryCode() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getPurchaseCategoryCode() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getChannel() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getChannel() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getTaxAmount() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getTaxAmount() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getDate() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getDate() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                response.getWriter().write("</tr>");
            }
            response.getWriter().write("</tbody>");
            response.getWriter().write("</table>");
        } else {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table id=\"transaction_table\" class=\"table table-bordered table-striped\">");
            response.getWriter().write("<thead>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<th>Id</th>");
            response.getWriter().write("<th>End User Id</th>");
            response.getWriter().write("<th>Status</th>");
            response.getWriter().write("<th>Reference Code</th>");
            response.getWriter().write("<th>Description</th>");
            response.getWriter().write("<th>Currency</th>");
            response.getWriter().write("<th>Amount</th>");
            response.getWriter().write("<th>Client Correlator</th>");
//                        response.getWriter().write("<th>Notify URL</th>");
            response.getWriter().write("<th>On Behalf of</th>");
            response.getWriter().write("<th>Purchase Category Code</th>");
            response.getWriter().write("<th>Channel</th>");
            response.getWriter().write("<th>Tax Amount</th>");
            response.getWriter().write("<th>Created Date</th>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</thead>");
            response.getWriter().write("<tbody>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<td colspan = '13' style='text-align:center;'><h2>No data found for query</h2></td>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</tbody>");
            response.getWriter().write("</table>");
        }
    }

    private void generateRetrieveSMSMessagesTransactionGrid(HttpServletRequest request, HttpServletResponse response, int iuser) throws IOException, ParseException {

        //int userId = Integer.parseInt(request.getHeader("User-Id").replaceAll("\\s",""));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = dateFormat.parse(request.getParameter("from_date").toString().trim());
        Date toDate = dateFormat.parse(request.getParameter("to_date").toString().trim());
        int txType = 2;
        List<SMSRequestLog> smsLogArray = TransactionFunctions.getSMSLogTransactions(iuser, fromDate, toDate, txType);

        if (smsLogArray.size() > 0) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table id=\"transaction_table\" class=\"table table-bordered table-striped\">");
            response.getWriter().write("<thead>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<th>Transaction Id</th>");
            response.getWriter().write("<th>Registration Id</th>");
            response.getWriter().write("<th>Max Batch Size</th>");
            response.getWriter().write("<th>Created Date</th>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</thead>");

            response.getWriter().write("<tbody>");
            for (int i = 0; i < smsLogArray.size(); i++) {
                SMSRequestLog smsRetrieveRequestLog = smsLogArray.get(i);
                response.getWriter().write("<tr>");
                response.getWriter().write("<td>" + (i + 1) + "</td>");
                if (smsRetrieveRequestLog.getSenderAddress() != null) {
                    response.getWriter().write("<td>" + smsRetrieveRequestLog.getSenderAddress() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (smsRetrieveRequestLog.getBatchsize() != null) {
                    response.getWriter().write("<td>" + smsRetrieveRequestLog.getBatchsize() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (smsRetrieveRequestLog.getDate() != null) {
                    response.getWriter().write("<td>" + smsRetrieveRequestLog.getDate() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                response.getWriter().write("</tr>");
            }
            response.getWriter().write("</tbody>");
            response.getWriter().write("</table>");
        } else {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table id=\"transaction_table\" class=\"table table-bordered table-striped\">");
            response.getWriter().write("<thead>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<th>Transaction Id</th>");
            response.getWriter().write("<th>Registration Id</th>");
            response.getWriter().write("<th>Max Batch Size</th>");
            response.getWriter().write("<th>Created Date</th>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</thead>");
            response.getWriter().write("<tbody>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<td colspan = '4' style='text-align:center;'><h2>No data found for query</h2></td>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</tbody>");
            response.getWriter().write("</table>");
        }
    }

    private void generateSMSSubscriptionTransactionGrid(HttpServletRequest request, HttpServletResponse response, int iuser) throws IOException, ParseException {

        //int userId = Integer.parseInt(request.getHeader("User-Id").replaceAll("\\s",""));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = dateFormat.parse(request.getParameter("from_date").toString().trim());
        Date toDate = dateFormat.parse(request.getParameter("to_date").toString().trim());
        int txType = 3;
        List<SMSRequestLog> smsLogArray = TransactionFunctions.getSMSLogTransactions(iuser, fromDate, toDate, txType);

        if (smsLogArray.size() > 0) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table id=\"transaction_table\" class=\"table table-bordered table-striped\">");
            response.getWriter().write("<thead>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<th>Transaction Id</th>");
            response.getWriter().write("<th>Destination Address</th>");
            response.getWriter().write("<th>Notify URL</th>");
            response.getWriter().write("<th>Callback Data</th>");
            response.getWriter().write("<th>Criteria</th>");
            response.getWriter().write("<th>Notification Format</th>");
            response.getWriter().write("<th>Created Date</th>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</thead>");

            response.getWriter().write("<tbody>");
            for (int i = 0; i < smsLogArray.size(); i++) {
                SMSRequestLog smsRetrieveRequestLog = smsLogArray.get(i);
                response.getWriter().write("<tr>");
                response.getWriter().write("<td>" + (i + 1) + "</td>");
                if (smsRetrieveRequestLog.getSenderAddress() != null) {
                    response.getWriter().write("<td>" + smsRetrieveRequestLog.getSenderAddress() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (smsRetrieveRequestLog.getNotifyURL() != null) {
                    response.getWriter().write("<td>" + smsRetrieveRequestLog.getNotifyURL() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (smsRetrieveRequestLog.getCallbackData() != null) {
                    response.getWriter().write("<td>" + smsRetrieveRequestLog.getCallbackData() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (smsRetrieveRequestLog.getCriteria() != null) {
                    response.getWriter().write("<td>" + smsRetrieveRequestLog.getCriteria() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (smsRetrieveRequestLog.getNotificationFormat() != null) {
                    response.getWriter().write("<td>" + smsRetrieveRequestLog.getNotificationFormat() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (smsRetrieveRequestLog.getDate() != null) {
                    response.getWriter().write("<td>" + smsRetrieveRequestLog.getDate() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                response.getWriter().write("</tr>");
            }
            response.getWriter().write("</tbody>");
            response.getWriter().write("</table>");
        } else {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table id=\"transaction_table\" class=\"table table-bordered table-striped\">");
            response.getWriter().write("<thead>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<th>Transaction Id</th>");
            response.getWriter().write("<th>Notify URL</th>");
            response.getWriter().write("<th>Callback Data</th>");
            response.getWriter().write("<th>Criteria</th>");
            response.getWriter().write("<th>Notification Format</th>");
            response.getWriter().write("<th>Created Date</th>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</thead>");
            response.getWriter().write("<tbody>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<td colspan = '6' style='text-align:center;'><h2>No data found for query</h2></td>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</tbody>");
            response.getWriter().write("</table>");
        }
    }

    private void generateCancelSubscriptionTransactionGrid(HttpServletRequest request, HttpServletResponse response, int iuser) throws IOException, ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = dateFormat.parse(request.getParameter("from_date").toString().trim());
        Date toDate = dateFormat.parse(request.getParameter("to_date").toString().trim());
        int txType = 4;
        List<SMSRequestLog> smsLogArray = TransactionFunctions.getSMSLogTransactions(iuser, fromDate, toDate, txType);

        if (smsLogArray.size() > 0) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table id=\"transaction_table\" class=\"table table-bordered table-striped\">");
            response.getWriter().write("<thead>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<th>Transaction Id</th>");
            response.getWriter().write("<th>Subscription Id</th>");
            response.getWriter().write("<th>Status</th>");
            response.getWriter().write("<th>Created Date</th>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</thead>");

            response.getWriter().write("<tbody>");
            for (int i = 0; i < smsLogArray.size(); i++) {
                SMSRequestLog smsRetrieveRequestLog = smsLogArray.get(i);
                response.getWriter().write("<tr>");
                response.getWriter().write("<td>" + (i + 1) + "</td>");
                if (smsRetrieveRequestLog.getSenderAddress() != null) {
                    response.getWriter().write("<td>" + smsRetrieveRequestLog.getSenderAddress() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (smsRetrieveRequestLog.getTransactionstatus() != null) {
                    response.getWriter().write("<td>" + smsRetrieveRequestLog.getTransactionstatus() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (smsRetrieveRequestLog.getDate() != null) {
                    response.getWriter().write("<td>" + smsRetrieveRequestLog.getDate() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                response.getWriter().write("</tr>");
            }
            response.getWriter().write("</tbody>");
            response.getWriter().write("</table>");
        } else {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table id=\"transaction_table\" class=\"table table-bordered table-striped\">");
            response.getWriter().write("<thead>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<th>Transaction Id</th>");
            response.getWriter().write("<th>Subscription Id</th>");
            response.getWriter().write("<th>Status</th>");
            response.getWriter().write("<th>Created Date</th>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</thead>");
            response.getWriter().write("<tbody>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<td colspan = '4' style='text-align:center;'><h2>No data found for query</h2></td>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</tbody>");
            response.getWriter().write("</table>");
        }
    }

    private void generateLocationRequestTransactionGrid(HttpServletRequest request, HttpServletResponse response, int iuser) throws IOException, ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = dateFormat.parse(request.getParameter("from_date").toString().trim());
        Date toDate = dateFormat.parse(request.getParameter("to_date").toString().trim());
        List<LocationRequestLog> locationRequestLogArray = TransactionFunctions.getLocationTransactions(iuser, fromDate, toDate);

        if (locationRequestLogArray.size() > 0) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table id=\"transaction_table\" class=\"table table-bordered table-striped\">");
            response.getWriter().write("<thead>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<th>Transaction Id</th>");
            response.getWriter().write("<th>Address</th>");
            response.getWriter().write("<th>Requested Accuracy</th>");
            response.getWriter().write("<th>Status</th>");
            response.getWriter().write("<th>Created Date</th>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</thead>");

            response.getWriter().write("<tbody>");
            for (int i = 0; i < locationRequestLogArray.size(); i++) {
                LocationRequestLog locationRequestLog = locationRequestLogArray.get(i);
                response.getWriter().write("<tr>");
                response.getWriter().write("<td>" + (i + 1) + "</td>");
                if (locationRequestLog.getAddress() != null) {
                    response.getWriter().write("<td>" + locationRequestLog.getAddress() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (locationRequestLog.getRequestedAccuracy() != null) {
                    response.getWriter().write("<td>" + locationRequestLog.getRequestedAccuracy() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (locationRequestLog.getTransactionstatus() != null) {
                    response.getWriter().write("<td>" + locationRequestLog.getTransactionstatus() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (locationRequestLog.getDate() != null) {
                    response.getWriter().write("<td>" + locationRequestLog.getDate() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                response.getWriter().write("</tr>");
            }
            response.getWriter().write("</tbody>");
            response.getWriter().write("</table>");
        } else {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table id=\"transaction_table\" class=\"table table-bordered table-striped\">");
            response.getWriter().write("<thead>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<th>Transaction Id</th>");
            response.getWriter().write("<th>Address</th>");
            response.getWriter().write("<th>Requested Accuracy</th>");
            response.getWriter().write("<th>Status</th>");
            response.getWriter().write("<th>Created Date</th>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</thead>");
            response.getWriter().write("<tbody>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<td colspan = '5' style='text-align:center;'><h2>No data found for query</h2></td>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</tbody>");
            response.getWriter().write("</table>");
        }
    }

    private void generateReserveAmountTransactionGrid(HttpServletRequest request, HttpServletResponse response, int iuser) throws IOException, ParseException {
        //int userId = Integer.parseInt(request.getHeader("User-Id").replaceAll("\\s",""));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = dateFormat.parse(request.getParameter("from_date").toString().trim());
        Date toDate = dateFormat.parse(request.getParameter("to_date").toString().trim());
        int txType = 2;
        List<ChargeAmountRequest> chargeAmountRequestArray = TransactionFunctions.getPaymentTransactions(iuser, txType, fromDate, toDate);

        if (chargeAmountRequestArray.size() > 0) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table id=\"transaction_table\" class=\"table table-bordered table-striped\">");
            response.getWriter().write("<thead>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<th>Id</th>");
            response.getWriter().write("<th>End User Id</th>");
            response.getWriter().write("<th>Status</th>");
            response.getWriter().write("<th>Reference Code</th>");
            response.getWriter().write("<th>Description</th>");
            response.getWriter().write("<th>Currency</th>");
            response.getWriter().write("<th>Amount</th>");
            response.getWriter().write("<th>Client Correlator</th>");
            response.getWriter().write("<th>On Behalf of</th>");
            response.getWriter().write("<th>Purchase Category Code</th>");
            response.getWriter().write("<th>Channel</th>");
            response.getWriter().write("<th>Tax Amount</th>");
            response.getWriter().write("<th>Callback Data</th>");
            response.getWriter().write("<th>Notify URL</th>");
            response.getWriter().write("<th>Notification Format</th>");
            response.getWriter().write("<th>Reference Sequence</th>");
            response.getWriter().write("<th>Mandate Id</th>");
            response.getWriter().write("<th>Product Id</th>");
            response.getWriter().write("<th>Service Id</th>");
            response.getWriter().write("<th>Total Amount Charged</th>");
            response.getWriter().write("<th>Amount Reserved</th>");
            response.getWriter().write("<th>Transaction Id</th>");
            response.getWriter().write("<th>Created Date</th>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</thead>");

            response.getWriter().write("<tbody>");
            for (int i = 0; i < chargeAmountRequestArray.size(); i++) {
                ChargeAmountRequest chargeAmountRequest = chargeAmountRequestArray.get(i);
                response.getWriter().write("<tr>");
                response.getWriter().write("<td>" + (i + 1) + "</td>");
                if (chargeAmountRequest.getEndUserId() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getEndUserId() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getTransactionOperationStatus() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getTransactionOperationStatus() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getReferenceCode() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getReferenceCode() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getDescription() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getDescription() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getCurrency() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getCurrency() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getAmount() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getAmount() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getClientCorrelator() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getClientCorrelator() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getOnBehalfOf() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getOnBehalfOf() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getPurchaseCategoryCode() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getPurchaseCategoryCode() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getChannel() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getChannel() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getTaxAmount() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getTaxAmount() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getCallbackData() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getCallbackData() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getNotifyURL() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getNotifyURL() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getNotificationFormat() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getNotificationFormat() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getReferenceSequence() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getReferenceSequence() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getMandateId() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getMandateId() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getProductId() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getProductId() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getServiceId() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getServiceId() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getTotalAmountCharged() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getTotalAmountCharged() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getAmountReserved() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getAmountReserved() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getTransactionId() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getTransactionId() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getDate() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getDate() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                response.getWriter().write("</tr>");
            }
            response.getWriter().write("</tbody>");
            response.getWriter().write("</table>");
        } else {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table id=\"transaction_table\" class=\"table table-bordered table-striped\">");
            response.getWriter().write("<thead>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<th>Id</th>");
            response.getWriter().write("<th>End User Id</th>");
            response.getWriter().write("<th>Status</th>");
            response.getWriter().write("<th>Reference Code</th>");
            response.getWriter().write("<th>Description</th>");
            response.getWriter().write("<th>Currency</th>");
            response.getWriter().write("<th>Amount</th>");
            response.getWriter().write("<th>Client Correlator</th>");
            response.getWriter().write("<th>On Behalf of</th>");
            response.getWriter().write("<th>Purchase Category Code</th>");
            response.getWriter().write("<th>Channel</th>");
            response.getWriter().write("<th>Tax Amount</th>");
            response.getWriter().write("<th>Callback Data</th>");
            response.getWriter().write("<th>Notify URL</th>");
            response.getWriter().write("<th>Notification Format</th>");
            response.getWriter().write("<th>Reference Sequence</th>");
            response.getWriter().write("<th>Mandate Id</th>");
            response.getWriter().write("<th>Product Id</th>");
            response.getWriter().write("<th>Service Id</th>");
            response.getWriter().write("<th>Total Amount Charged</th>");
            response.getWriter().write("<th>Amount Reserved</th>");
            response.getWriter().write("<th>Transaction Id</th>");
            response.getWriter().write("<th>Created Date</th>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</thead>");
            response.getWriter().write("<tbody>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<td colspan = '23' style='text-align:center;'><h2>No data found for query</h2></td>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</tbody>");
            response.getWriter().write("</table>");
        }
    }

    private void generateReserveAdditionalAmountTransactionGrid(HttpServletRequest request, HttpServletResponse response, int iuser) throws IOException, ParseException {
        //int userId = Integer.parseInt(request.getHeader("User-Id").replaceAll("\\s",""));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = dateFormat.parse(request.getParameter("from_date").toString().trim());
        Date toDate = dateFormat.parse(request.getParameter("to_date").toString().trim());
        int txType = 3;
        List<ChargeAmountRequest> chargeAmountRequestArray = TransactionFunctions.getPaymentTransactions(iuser, txType, fromDate, toDate);

        if (chargeAmountRequestArray.size() > 0) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table id=\"transaction_table\" class=\"table table-bordered table-striped\">");
            response.getWriter().write("<thead>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<th>Id</th>");
            response.getWriter().write("<th>End User Id</th>");
            response.getWriter().write("<th>Status</th>");
            response.getWriter().write("<th>Reference Code</th>");
            response.getWriter().write("<th>Description</th>");
            response.getWriter().write("<th>Currency</th>");
            response.getWriter().write("<th>Amount</th>");
            response.getWriter().write("<th>On Behalf of</th>");
            response.getWriter().write("<th>Purchase Category Code</th>");
            response.getWriter().write("<th>Channel</th>");
            response.getWriter().write("<th>Tax Amount</th>");
            response.getWriter().write("<th>Callback Data</th>");
            response.getWriter().write("<th>Notify URL</th>");
            response.getWriter().write("<th>Notification Format</th>");
            response.getWriter().write("<th>Reference Sequence</th>");
            response.getWriter().write("<th>Mandate Id</th>");
            response.getWriter().write("<th>Product Id</th>");
            response.getWriter().write("<th>Service Id</th>");
            response.getWriter().write("<th>Transaction Id</th>");
            response.getWriter().write("<th>Created Date</th>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</thead>");

            response.getWriter().write("<tbody>");
            for (int i = 0; i < chargeAmountRequestArray.size(); i++) {
                ChargeAmountRequest chargeAmountRequest = chargeAmountRequestArray.get(i);
                response.getWriter().write("<tr>");
                response.getWriter().write("<td>" + (i + 1) + "</td>");
                if (chargeAmountRequest.getEndUserId() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getEndUserId() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getTransactionOperationStatus() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getTransactionOperationStatus() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getReferenceCode() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getReferenceCode() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getDescription() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getDescription() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getCurrency() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getCurrency() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getAmount() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getAmount() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getOnBehalfOf() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getOnBehalfOf() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getPurchaseCategoryCode() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getPurchaseCategoryCode() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getChannel() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getChannel() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getTaxAmount() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getTaxAmount() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getCallbackData() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getCallbackData() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getNotifyURL() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getNotifyURL() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getNotificationFormat() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getNotificationFormat() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getReferenceSequence() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getReferenceSequence() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getMandateId() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getMandateId() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getProductId() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getProductId() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getServiceId() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getServiceId() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getTransactionId() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getTransactionId() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getDate() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getDate() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                response.getWriter().write("</tr>");
            }
            response.getWriter().write("</tbody>");
            response.getWriter().write("</table>");
        } else {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table id=\"transaction_table\" class=\"table table-bordered table-striped\">");
            response.getWriter().write("<thead>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<th>Id</th>");
            response.getWriter().write("<th>End User Id</th>");
            response.getWriter().write("<th>Status</th>");
            response.getWriter().write("<th>Reference Code</th>");
            response.getWriter().write("<th>Description</th>");
            response.getWriter().write("<th>Currency</th>");
            response.getWriter().write("<th>Amount</th>");
            response.getWriter().write("<th>On Behalf of</th>");
            response.getWriter().write("<th>Purchase Category Code</th>");
            response.getWriter().write("<th>Channel</th>");
            response.getWriter().write("<th>Tax Amount</th>");
            response.getWriter().write("<th>Callback Data</th>");
            response.getWriter().write("<th>Notify URL</th>");
            response.getWriter().write("<th>Notification Format</th>");
            response.getWriter().write("<th>Reference Sequence</th>");
            response.getWriter().write("<th>Mandate Id</th>");
            response.getWriter().write("<th>Product Id</th>");
            response.getWriter().write("<th>Service Id</th>");
            response.getWriter().write("<th>Transaction Id</th>");
            response.getWriter().write("<th>Created Date</th>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</thead>");
            response.getWriter().write("<tbody>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<td colspan = '20' style='text-align:center;'><h2>No data found for query</h2></td>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</tbody>");
            response.getWriter().write("</table>");
        }
    }

    private void generateReserveChargedAmountTransactionGrid(HttpServletRequest request, HttpServletResponse response, int iuser) throws IOException, ParseException {
        //int userId = Integer.parseInt(request.getHeader("User-Id").replaceAll("\\s",""));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = dateFormat.parse(request.getParameter("from_date").toString().trim());
        Date toDate = dateFormat.parse(request.getParameter("to_date").toString().trim());
        int txType = 4;
        List<ChargeAmountRequest> chargeAmountRequestArray = TransactionFunctions.getPaymentTransactions(iuser, txType, fromDate, toDate);

        if (chargeAmountRequestArray.size() > 0) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table id=\"transaction_table\" class=\"table table-bordered table-striped\">");
            response.getWriter().write("<thead>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<th>Id</th>");
            response.getWriter().write("<th>End User Id</th>");
            response.getWriter().write("<th>Status</th>");
            response.getWriter().write("<th>Reference Code</th>");
            response.getWriter().write("<th>Description</th>");
            response.getWriter().write("<th>Currency</th>");
            response.getWriter().write("<th>Amount</th>");
            response.getWriter().write("<th>On Behalf of</th>");
            response.getWriter().write("<th>Purchase Category Code</th>");
            response.getWriter().write("<th>Channel</th>");
            response.getWriter().write("<th>Tax Amount</th>");
            response.getWriter().write("<th>Callback Data</th>");
            response.getWriter().write("<th>Notify URL</th>");
            response.getWriter().write("<th>Notification Format</th>");
            response.getWriter().write("<th>Reference Sequence</th>");
            response.getWriter().write("<th>Mandate Id</th>");
            response.getWriter().write("<th>Product Id</th>");
            response.getWriter().write("<th>Service Id</th>");
            response.getWriter().write("<th>Transaction Id</th>");
            response.getWriter().write("<th>Created Date</th>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</thead>");

            response.getWriter().write("<tbody>");
            for (int i = 0; i < chargeAmountRequestArray.size(); i++) {
                ChargeAmountRequest chargeAmountRequest = chargeAmountRequestArray.get(i);
                response.getWriter().write("<tr>");
                response.getWriter().write("<td>" + (i + 1) + "</td>");
                if (chargeAmountRequest.getEndUserId() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getEndUserId() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getTransactionOperationStatus() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getTransactionOperationStatus() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getReferenceCode() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getReferenceCode() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getDescription() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getDescription() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getCurrency() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getCurrency() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getAmount() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getAmount() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getOnBehalfOf() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getOnBehalfOf() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getPurchaseCategoryCode() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getPurchaseCategoryCode() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getChannel() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getChannel() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getTaxAmount() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getTaxAmount() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getCallbackData() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getCallbackData() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getNotifyURL() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getNotifyURL() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getNotificationFormat() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getNotificationFormat() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getReferenceSequence() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getReferenceSequence() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getMandateId() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getMandateId() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getProductId() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getProductId() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getServiceId() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getServiceId() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getTransactionId() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getTransactionId() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getDate() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getDate() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                response.getWriter().write("</tr>");
            }
            response.getWriter().write("</tbody>");
            response.getWriter().write("</table>");
        } else {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table id=\"transaction_table\" class=\"table table-bordered table-striped\">");
            response.getWriter().write("<thead>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<th>Id</th>");
            response.getWriter().write("<th>End User Id</th>");
            response.getWriter().write("<th>Status</th>");
            response.getWriter().write("<th>Reference Code</th>");
            response.getWriter().write("<th>Description</th>");
            response.getWriter().write("<th>Currency</th>");
            response.getWriter().write("<th>Amount</th>");
            response.getWriter().write("<th>On Behalf of</th>");
            response.getWriter().write("<th>Purchase Category Code</th>");
            response.getWriter().write("<th>Channel</th>");
            response.getWriter().write("<th>Tax Amount</th>");
            response.getWriter().write("<th>Callback Data</th>");
            response.getWriter().write("<th>Notify URL</th>");
            response.getWriter().write("<th>Notification Format</th>");
            response.getWriter().write("<th>Reference Sequence</th>");
            response.getWriter().write("<th>Mandate Id</th>");
            response.getWriter().write("<th>Product Id</th>");
            response.getWriter().write("<th>Service Id</th>");
            response.getWriter().write("<th>Transaction Id</th>");
            response.getWriter().write("<th>Created Date</th>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</thead>");
            response.getWriter().write("<tbody>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<td colspan = '20' style='text-align:center;'><h2>No data found for query</h2></td>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</tbody>");
            response.getWriter().write("</table>");
        }
    }

    private void generateReserveAmountReleaseTransactionGrid(HttpServletRequest request, HttpServletResponse response, int iuser) throws IOException, ParseException {
        //int userId = Integer.parseInt(request.getHeader("User-Id").replaceAll("\\s",""));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = dateFormat.parse(request.getParameter("from_date").toString().trim());
        Date toDate = dateFormat.parse(request.getParameter("to_date").toString().trim());
        int txType = 5;
        List<ChargeAmountRequest> chargeAmountRequestArray = TransactionFunctions.getPaymentTransactions(iuser, txType, fromDate, toDate);

        if (chargeAmountRequestArray.size() > 0) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table id=\"transaction_table\" class=\"table table-bordered table-striped\">");
            response.getWriter().write("<thead>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<th>Id</th>");
            response.getWriter().write("<th>End User Id</th>");
            response.getWriter().write("<th>Status</th>");
            response.getWriter().write("<th>Reference Sequence</th>");
            response.getWriter().write("<th>Transaction Id</th>");
            response.getWriter().write("<th>Created Date</th>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</thead>");

            response.getWriter().write("<tbody>");
            for (int i = 0; i < chargeAmountRequestArray.size(); i++) {
                ChargeAmountRequest chargeAmountRequest = chargeAmountRequestArray.get(i);
                response.getWriter().write("<tr>");
                response.getWriter().write("<td>" + (i + 1) + "</td>");
                if (chargeAmountRequest.getEndUserId() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getEndUserId() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getTransactionOperationStatus() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getTransactionOperationStatus() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getReferenceSequence() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getReferenceSequence() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getTransactionId() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getTransactionId() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getDate() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getDate() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                response.getWriter().write("</tr>");
            }
            response.getWriter().write("</tbody>");
            response.getWriter().write("</table>");
        } else {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table id=\"transaction_table\" class=\"table table-bordered table-striped\">");
            response.getWriter().write("<thead>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<th>Id</th>");
            response.getWriter().write("<th>End User Id</th>");
            response.getWriter().write("<th>Status</th>");
            response.getWriter().write("<th>Reference Sequence</th>");
            response.getWriter().write("<th>Transaction Id</th>");
            response.getWriter().write("<th>Created Date</th>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</thead>");
            response.getWriter().write("<tbody>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<td colspan = '6' style='text-align:center;'><h2>No data found for query</h2></td>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</tbody>");
            response.getWriter().write("</table>");
        }
    }

    private void generateRefundTransactionGrid(HttpServletRequest request, HttpServletResponse response, int iuser) throws IOException, ParseException {
        //int userId = Integer.parseInt(request.getHeader("User-Id").replaceAll("\\s",""));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = dateFormat.parse(request.getParameter("from_date").toString().trim());
        Date toDate = dateFormat.parse(request.getParameter("to_date").toString().trim());
        int txType = 6;
        List<ChargeAmountRequest> chargeAmountRequestArray = TransactionFunctions.getPaymentTransactions(iuser, txType, fromDate, toDate);

        if (chargeAmountRequestArray.size() > 0) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table id=\"transaction_table\" class=\"table table-bordered table-striped\">");
            response.getWriter().write("<thead>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<th>Id</th>");
            response.getWriter().write("<th>End User Id</th>");
            response.getWriter().write("<th>Status</th>");
            response.getWriter().write("<th>Reference Code</th>");
            response.getWriter().write("<th>Description</th>");
            response.getWriter().write("<th>Currency</th>");
            response.getWriter().write("<th>Amount</th>");
            response.getWriter().write("<th>Client Correlator</th>");
            response.getWriter().write("<th>On Behalf of</th>");
            response.getWriter().write("<th>Purchase Category Code</th>");
            response.getWriter().write("<th>Channel</th>");
            response.getWriter().write("<th>Tax Amount</th>");
            response.getWriter().write("<th>Callback Data</th>");
            response.getWriter().write("<th>Notify URL</th>");
            response.getWriter().write("<th>Notification Format</th>");
            response.getWriter().write("<th>Original Server Reference Code</th>");
            response.getWriter().write("<th>Mandate Id</th>");
            response.getWriter().write("<th>Product Id</th>");
            response.getWriter().write("<th>Service Id</th>");
            response.getWriter().write("<th>Transaction Id</th>");
            response.getWriter().write("<th>Created Date</th>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</thead>");

            response.getWriter().write("<tbody>");
            for (int i = 0; i < chargeAmountRequestArray.size(); i++) {
                ChargeAmountRequest chargeAmountRequest = chargeAmountRequestArray.get(i);
                response.getWriter().write("<tr>");
                response.getWriter().write("<td>" + (i + 1) + "</td>");
                if (chargeAmountRequest.getEndUserId() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getEndUserId() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getTransactionOperationStatus() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getTransactionOperationStatus() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getReferenceCode() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getReferenceCode() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getDescription() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getDescription() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getCurrency() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getCurrency() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getAmount() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getAmount() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getClientCorrelator() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getClientCorrelator() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getOnBehalfOf() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getOnBehalfOf() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getPurchaseCategoryCode() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getPurchaseCategoryCode() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getChannel() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getChannel() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getTaxAmount() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getTaxAmount() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getCallbackData() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getCallbackData() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getNotifyURL() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getNotifyURL() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getNotificationFormat() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getNotificationFormat() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getOriginalServerReferenceCode() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getOriginalServerReferenceCode() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getMandateId() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getMandateId() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getProductId() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getProductId() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getServiceId() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getServiceId() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getTransactionId() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getTransactionId() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                if (chargeAmountRequest.getDate() != null) {
                    response.getWriter().write("<td>" + chargeAmountRequest.getDate() + "</td>");
                } else {
                    response.getWriter().write("<td></td>");
                }
                response.getWriter().write("</tr>");
            }
            response.getWriter().write("</tbody>");
            response.getWriter().write("</table>");
        } else {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table id=\"transaction_table\" class=\"table table-bordered table-striped\">");
            response.getWriter().write("<thead>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<th>Id</th>");
            response.getWriter().write("<th>End User Id</th>");
            response.getWriter().write("<th>Status</th>");
            response.getWriter().write("<th>Reference Code</th>");
            response.getWriter().write("<th>Description</th>");
            response.getWriter().write("<th>Currency</th>");
            response.getWriter().write("<th>Amount</th>");
            response.getWriter().write("<th>Client Correlator</th>");
            response.getWriter().write("<th>On Behalf of</th>");
            response.getWriter().write("<th>Purchase Category Code</th>");
            response.getWriter().write("<th>Channel</th>");
            response.getWriter().write("<th>Tax Amount</th>");
            response.getWriter().write("<th>Callback Data</th>");
            response.getWriter().write("<th>Notify URL</th>");
            response.getWriter().write("<th>Notification Format</th>");
            response.getWriter().write("<th>Original Server Reference Code</th>");
            response.getWriter().write("<th>Mandate Id</th>");
            response.getWriter().write("<th>Product Id</th>");
            response.getWriter().write("<th>Service Id</th>");
            response.getWriter().write("<th>Transaction Id</th>");
            response.getWriter().write("<th>Created Date</th>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</thead>");
            response.getWriter().write("<tbody>");
            response.getWriter().write("<tr>");
            response.getWriter().write("<td colspan = '21' style='text-align:center;'><h2>No data found for query</h2></td>");
            response.getWriter().write("</tr>");
            response.getWriter().write("</tbody>");
            response.getWriter().write("</table>");
        }
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

}

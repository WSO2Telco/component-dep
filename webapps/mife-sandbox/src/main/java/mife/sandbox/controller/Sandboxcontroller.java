/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mife.sandbox.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mife.sandbox.model.SmsImpl;
import mife.sandbox.model.entities.SMSRequestLog;
import mife.sandbox.model.entities.User;

/**
 *
 * @author User
 */
public class Sandboxcontroller extends HttpServlet {

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
            throws ServletException, IOException  {
        
        String type = request.getParameter("type").toString().trim();
        smsimpl = new SmsImpl();
        String userid = request.getHeader("userid").trim();
        int iuser = ((User)smsimpl.getUser(userid)).getId();
                        
        try {
            if (type.equals("1")) {
                generateSendSMSTransactionGrid(request, response,iuser);
            } else if(type.equals("2")){
                //generateRetrieveSMSMessagesTransactionGrid(request, response,iuser);
            }else if(type.equals("3")){
                //generateSMSSubscriptionTransactionGrid(request, response,iuser);
            }else if(type.equals("4")){
                System.out.println("Transaction Type 4");
            } else if (type.equals("5")) {
                //generateChargeAmountTransactionGrid(request, response,iuser);
            }
        } catch (ParseException e) {
            System.out.println("ParseException: " + e);
        }
    }
    
    private void generateSendSMSTransactionGrid(HttpServletRequest request, HttpServletResponse response, int iuser) throws IOException, ParseException{
        /*System.out.println("from Date in request as string: " + request.getParameter("from_date").toString().trim());
        System.out.println("to Date in request as string: " + request.getParameter("to_date").toString().trim());*/
        //int userId = Integer.parseInt(request.getHeader("User-Id").replaceAll("\\s",""));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = dateFormat.parse(request.getParameter("from_date").toString().trim());
        //System.out.println("from Date in request as date: " + fromDate);
        Date toDate = dateFormat.parse(request.getParameter("to_date").toString().trim());
        //System.out.println("to Date in request as date: " + toDate);
        int txType = 1;
        List<SMSRequestLog> smsLogArray = TransactionFunctions.getSMSLogTransactions(iuser,fromDate,toDate,txType);
        
        if (smsLogArray.size() > 0) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table id=\"transaction_table\" class=\"table table-bordered table-striped\">");
                response.getWriter().write("<thead>");
                    response.getWriter().write("<tr>");
                        response.getWriter().write("<th>Sender Address</th>");
                        response.getWriter().write("<th>Addresses</th>");
                        response.getWriter().write("<th>Message</th>");
                        response.getWriter().write("<th>Client Correlator</th>");
                        response.getWriter().write("<th>Sender Name</th>");
                        response.getWriter().write("<th>Notify URL</th>");
                        response.getWriter().write("<th>Callback Data</th>");
                        response.getWriter().write("<th>Date</th>");
                    response.getWriter().write("</tr>");
                response.getWriter().write("</thead>");

                response.getWriter().write("<tbody>");
                for (int i = 0; i < smsLogArray.size(); i++) {
                    SMSRequestLog sendSMSLog = smsLogArray.get(i);
                    response.getWriter().write("<tr>");
                        response.getWriter().write("<td>"+sendSMSLog.getSenderAddress()+"</td>");
                        response.getWriter().write("<td>"+sendSMSLog.getAddresses()+"</td>");
                        response.getWriter().write("<td>"+sendSMSLog.getMessage()+"</td>");
                        response.getWriter().write("<td>"+sendSMSLog.getClientCorrelator()+"</td>");
                        response.getWriter().write("<td>"+sendSMSLog.getSenderName()+"</td>");
                        response.getWriter().write("<td>"+sendSMSLog.getNotifyURL()+"</td>");
                        response.getWriter().write("<td>"+sendSMSLog.getCallbackData()+"</td>");
                        response.getWriter().write("<td>"+sendSMSLog.getDate()+"</td>");
                    response.getWriter().write("</tr>");
                }                
                response.getWriter().write("</tbody>");

            response.getWriter().write("</table>");
        }else{
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table id=\"transaction_table\" class=\"table table-bordered table-striped\">");
                response.getWriter().write("<thead>");
                    response.getWriter().write("<tr>");
                        response.getWriter().write("<th>Sender Address</th>");
                        response.getWriter().write("<th>Addresses</th>");
                        response.getWriter().write("<th>Message</th>");
                        response.getWriter().write("<th>Client Correlator</th>");
                        response.getWriter().write("<th>Sender Name</th>");
                        response.getWriter().write("<th>Notify URL</th>");
                        response.getWriter().write("<th>Callback Data</th>");
                        response.getWriter().write("<th>Date</th>");
                    response.getWriter().write("</tr>");
                response.getWriter().write("</thead>");
                
                response.getWriter().write("<tbody>");
                    response.getWriter().write("<tr>");
                        response.getWriter().write("<td>Empty</td>");
                        response.getWriter().write("<td>Empty</td>");
                        response.getWriter().write("<td>Empty</td>");
                        response.getWriter().write("<td>Empty</td>");
                        response.getWriter().write("<td>Empty</td>");
                        response.getWriter().write("<td>Empty</td>");
                        response.getWriter().write("<td>Empty</td>");
                        response.getWriter().write("<td>Empty</td>");
                    response.getWriter().write("</tr>");
                response.getWriter().write("</tbody>");
            response.getWriter().write("</table>");
        }        
    }
    
    /*private void generateChargeAmountTransactionGrid(HttpServletRequest request, HttpServletResponse response,int iuser) throws IOException, ParseException{
        //int userId = Integer.parseInt(request.getHeader("User-Id").replaceAll("\\s",""));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = dateFormat.parse(request.getParameter("from_date").toString().trim());
        Date toDate = dateFormat.parse(request.getParameter("to_date").toString().trim());
        List<ChargeAmountRequest> chargeAmountRequestArray = TransactionFunctions.getPaymentTransactions(iuser,fromDate,toDate);
        
        if (chargeAmountRequestArray.size() > 0) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table class=\"table table-bordered table-striped\">");
                response.getWriter().write("<thead>");
                    response.getWriter().write("<tr>");
                        response.getWriter().write("<th>Enduser Id</th>");
                        response.getWriter().write("<th>Status</th>");
                        response.getWriter().write("<th>Reference Code</th>");
                        response.getWriter().write("<th>Description</th>");
                        response.getWriter().write("<th>Currency</th>");
                        response.getWriter().write("<th>Amount</th>");
                        response.getWriter().write("<th>Client Correlator</th>");
                        response.getWriter().write("<th>Notify URL</th>");
                        response.getWriter().write("<th>On Behalf of</th>");                    
                        response.getWriter().write("<th>Purchase Category Code</th>");
                        response.getWriter().write("<th>Channel</th>"); 
                        response.getWriter().write("<th>TaxAmount</th>"); 
                        response.getWriter().write("<th>Date</th>"); 
                    response.getWriter().write("</tr>");
                response.getWriter().write("</thead>");

                response.getWriter().write("<tbody>");
                for (int i = 0; i < chargeAmountRequestArray.size(); i++) {
                    ChargeAmountRequest chargeAmountRequest = chargeAmountRequestArray.get(i);
                    response.getWriter().write("<tr>");
                        response.getWriter().write("<td>"+chargeAmountRequest.getEndUserId()+"</td>");
                        response.getWriter().write("<td>"+chargeAmountRequest.getTransactionOperationStatus()+"</td>");
                        response.getWriter().write("<td>"+chargeAmountRequest.getReferenceCode()+"</td>");
                        response.getWriter().write("<td>"+chargeAmountRequest.getDescription()+"</td>");
                        response.getWriter().write("<td>"+chargeAmountRequest.getCurrency()+"</td>");
                        response.getWriter().write("<td>"+chargeAmountRequest.getAmount()+"</td>");
                        response.getWriter().write("<td>"+chargeAmountRequest.getClientCorrelator()+"</td>");
                        response.getWriter().write("<td>"+chargeAmountRequest.getNotifyURL()+"</td>");
                        response.getWriter().write("<td>"+chargeAmountRequest.getOnBehalfOf()+"</td>");
                        response.getWriter().write("<td>"+chargeAmountRequest.getPurchaseCategoryCode()+"</td>");
                        response.getWriter().write("<td>"+chargeAmountRequest.getChannel()+"</td>");
                        response.getWriter().write("<td>"+chargeAmountRequest.getTaxAmount()+"</td>");
                        response.getWriter().write("<td>"+chargeAmountRequest.getDate()+"</td>");
                    response.getWriter().write("</tr>");
                }                
                response.getWriter().write("</tbody>");

            response.getWriter().write("</table>");
        }else{
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table class=\"table table-bordered table-striped\">");
                response.getWriter().write("<thead>");
                    response.getWriter().write("<tr>");
                        response.getWriter().write("<th>Enduser Id</th>");
                        response.getWriter().write("<th>Status</th>");
                        response.getWriter().write("<th>Reference Code</th>");
                        response.getWriter().write("<th>Description</th>");
                        response.getWriter().write("<th>Currency</th>");
                        response.getWriter().write("<th>Amount</th>");
                        response.getWriter().write("<th>Client Correlator</th>");
                        response.getWriter().write("<th>Notify URL</th>");
                        response.getWriter().write("<th>On Behalf of</th>");                    
                        response.getWriter().write("<th>Purchase Category Code</th>");
                        response.getWriter().write("<th>Channel</th>"); 
                        response.getWriter().write("<th>TaxAmount</th>"); 
                        response.getWriter().write("<th>Date</th>"); 
                    response.getWriter().write("</tr>");
                response.getWriter().write("</thead>");
                    response.getWriter().write("<tr>");
                        response.getWriter().write("<td>Empty</td>");
                        response.getWriter().write("<td>Empty</td>");
                        response.getWriter().write("<td>Empty</td>");
                        response.getWriter().write("<td>Empty</td>");
                        response.getWriter().write("<td>Empty</td>");
                        response.getWriter().write("<td>Empty</td>");
                        response.getWriter().write("<td>Empty</td>");
                        response.getWriter().write("<td>Empty</td>");
                        response.getWriter().write("<td>Empty</td>");
                        response.getWriter().write("<td>Empty</td>");
                        response.getWriter().write("<td>Empty</td>");
                        response.getWriter().write("<td>Empty</td>");
                        response.getWriter().write("<td>Empty</td>");
                    response.getWriter().write("</tr>");
                response.getWriter().write("<tbody>");
                    
                response.getWriter().write("</tbody>");
            response.getWriter().write("</table>");
        }        
    }
    
    private void generateRetrieveSMSMessagesTransactionGrid(HttpServletRequest request, HttpServletResponse response, int iuser) throws IOException, ParseException{
        
        //int userId = Integer.parseInt(request.getHeader("User-Id").replaceAll("\\s",""));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = dateFormat.parse(request.getParameter("from_date").toString().trim());
        Date toDate = dateFormat.parse(request.getParameter("to_date").toString().trim());
        int txType = 2;
        List<SMSRequestLog> smsLogArray = TransactionFunctions.getSMSLogTransactions(iuser,fromDate,toDate,txType);
        
        if (smsLogArray.size() > 0) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table class=\"table table-bordered table-striped\">");
                response.getWriter().write("<thead>");
                    response.getWriter().write("<tr>");
                        response.getWriter().write("<th>registrationId</th>");
                        response.getWriter().write("<th>maxBatchSize</th>");
                        response.getWriter().write("<th>Date</th>"); 
                    response.getWriter().write("</tr>");
                response.getWriter().write("</thead>");

                response.getWriter().write("<tbody>");
                for (int i = 0; i < smsLogArray.size(); i++) {
                    SMSRequestLog smsRetrieveRequestLog = smsLogArray.get(i);
                    response.getWriter().write("<tr>");
                        response.getWriter().write("<td>"+smsRetrieveRequestLog.getSenderAddress()+"</td>");
                        response.getWriter().write("<td>"+smsRetrieveRequestLog.getBatchsize()+"</td>");
                        response.getWriter().write("<td>"+smsRetrieveRequestLog.getDate()+"</td>");
                    response.getWriter().write("</tr>");
                }                
                response.getWriter().write("</tbody>");

            response.getWriter().write("</table>");
        }else{
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table class=\"table table-bordered table-striped\">");
                response.getWriter().write("<thead>");
                    response.getWriter().write("<tr>");
                        response.getWriter().write("<th>registrationId</th>");
                        response.getWriter().write("<th>maxBatchSize</th>");
                        response.getWriter().write("<th>Date</th>"); 
                    response.getWriter().write("</tr>");
                response.getWriter().write("</thead>");
                    response.getWriter().write("<tr>");
                        response.getWriter().write("<td>Empty</td>");
                        response.getWriter().write("<td>Empty</td>");
                        response.getWriter().write("<td>Empty</td>");
                    response.getWriter().write("</tr>");
                response.getWriter().write("<tbody>");
                    
                response.getWriter().write("</tbody>");
            response.getWriter().write("</table>");
        }
    }
    
    private void generateSMSSubscriptionTransactionGrid(HttpServletRequest request, HttpServletResponse response, int iuser) throws IOException, ParseException{
        
        //int userId = Integer.parseInt(request.getHeader("User-Id").replaceAll("\\s",""));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = dateFormat.parse(request.getParameter("from_date").toString().trim());
        Date toDate = dateFormat.parse(request.getParameter("to_date").toString().trim());
        int txType = 3;
        List<SMSRequestLog> smsLogArray = TransactionFunctions.getSMSLogTransactions(iuser,fromDate,toDate,txType);
        
        if (smsLogArray.size() > 0) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table class=\"table table-bordered table-striped\">");
                response.getWriter().write("<thead>");
                    response.getWriter().write("<tr>");
                        response.getWriter().write("<th>notifyURL</th>");
                        response.getWriter().write("<th>callbackData</th>");
                        response.getWriter().write("<th>criteria</th>");
                        response.getWriter().write("<th>notificationFormat</th>");
                        response.getWriter().write("<th>Date</th>"); 
                    response.getWriter().write("</tr>");
                response.getWriter().write("</thead>");

                response.getWriter().write("<tbody>");
                for (int i = 0; i < smsLogArray.size(); i++) {
                    SMSRequestLog smsRetrieveRequestLog = smsLogArray.get(i);
                    response.getWriter().write("<tr>");
                        response.getWriter().write("<td>"+smsRetrieveRequestLog.getNotifyURL()+"</td>");
                        response.getWriter().write("<td>"+smsRetrieveRequestLog.getCallbackData()+"</td>");
                        response.getWriter().write("<td>"+smsRetrieveRequestLog.getCriteria()+"</td>");
                        response.getWriter().write("<td>"+smsRetrieveRequestLog.getNotificationFormat()+"</td>");
                        response.getWriter().write("<td>"+smsRetrieveRequestLog.getDate()+"</td>");
                    response.getWriter().write("</tr>");
                }                
                response.getWriter().write("</tbody>");

            response.getWriter().write("</table>");
        }else{
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<table class=\"table table-bordered table-striped\">");
                response.getWriter().write("<thead>");
                    response.getWriter().write("<tr>");
                        response.getWriter().write("<th>notifyURL</th>");
                        response.getWriter().write("<th>callbackData</th>");
                        response.getWriter().write("<th>criteria</th>");
                        response.getWriter().write("<th>notificationFormat</th>");
                        response.getWriter().write("<th>Date</th>");
                    response.getWriter().write("</tr>");
                response.getWriter().write("</thead>");
                    response.getWriter().write("<tr>");
                        response.getWriter().write("<td>Empty</td>");
                        response.getWriter().write("<td>Empty</td>");
                        response.getWriter().write("<td>Empty</td>");
                        response.getWriter().write("<td>Empty</td>");
                        response.getWriter().write("<td>Empty</td>");
                    response.getWriter().write("</tr>");
                response.getWriter().write("<tbody>");
                    
                response.getWriter().write("</tbody>");
            response.getWriter().write("</table>");
        }
    }*/

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

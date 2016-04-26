/*
 * Smssandbox.java
 * May 20, 2014  4:55:30 PM
 * Roshan.Saputhanthri
 *
 * Copyright (C) Dialog Axiata PLC. All Rights Reserved.
 */
package org.gsm.oneapi.server.sms;

import org.gsm.oneapi.server.payment.*;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mife.sandbox.model.SmsImpl;

import mife.sandbox.model.entities.SandboxError;
import mife.sandbox.model.entities.Smsparam;
import mife.sandbox.model.entities.SubscribeSMSRequest;
//import mife.sandbox.responseobject.SubscribeSMSResponse;
import org.dialog.mife.responsebean.sms.SubscribeSMSResponse;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * <TO-DO>
 * <code>Smssandbox</code>
 *
 * @version $Id: Smssandbox.java,v 1.00.000
 */
public class Smssandbox extends PaymentServlet {

    static Logger logger = Logger.getLogger(AmountRefundServlet.class);
    private static final long serialVersionUID = -6237772242372106922L;
    
    public static final int BAD_REQUEST = 400;
    public static final int AUTHENTICATION_FAILURE = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int METHOD_NOT_SUPPORTED = 405;
    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int ACCEPTED = 202;
    public static final int NONAUTHORITATIVE = 203;
    public static final int NOCONTENT = 204;
    
            
    public void init() throws ServletException {
        logger.debug("Paymentsandbox initialised");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        SmsImpl smsimpl = new SmsImpl();
        
        String action = request.getHeader("payact");

        if (action.equalsIgnoreCase("addparam")) {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();

            String ret = "error";
            //String msg = URLDecoder.decode(msg.toString(), "UTF-8");
            String user = (String) request.getParameter("userid");
            String delstatus = (String) request.getParameter("delstatus");
            String notifydelay = (String) request.getParameter("notifydelay");
            String maxallowed = (String) request.getParameter("maxallowed");

            if ((smsimpl.saveParam(user, delstatus, notifydelay, maxallowed))) {
                ret = "success";
            }

            try {
                out.println(ret);
            } finally {
                out.close();
            }
        } else if (action.equalsIgnoreCase("queryparam")) {

            String ret = "error";
            String jsonreturn = null;
            //String msg = URLDecoder.decode(msg.toString(), "UTF-8");
            String user = (String) request.getParameter("userid");
            Smsparam sms = smsimpl.querySmsParam(user);
                                   
            if (sms == null) {
                jsonreturn = new Gson().toJson(new SandboxError(ret));
            } else {
                ObjectMapper mapper = new ObjectMapper();
                jsonreturn = mapper.writeValueAsString(sms);
            }
            sendJSONResponse(response,jsonreturn,OK,null);
            
        } else if (action.equalsIgnoreCase("getTableData")) {
            String ret = "error";
            String jsonreturn = null;
            String user = (String) request.getParameter("userid").replaceAll("\\s", "");
            //int useridInt = Integer.parseInt(user);
            List<SubscribeSMSRequest> data = smsimpl.getSubscriberSMSTableData(user);
            List<SubscribeSMSResponse> responseObj = new ArrayList<SubscribeSMSResponse>();
                                   
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (data == null) {
                jsonreturn = new Gson().toJson(new SandboxError(ret));
            } else {
                for (SubscribeSMSRequest temp: data){
                    SubscribeSMSResponse res = new SubscribeSMSResponse();
                    res.setSubscribeId(Integer.toString(temp.getSubscribeId()));
                    res.setCallbackData(temp.getCallbackData());
                    res.setClientCorrelator(temp.getClientCorrelator());
                    res.setCriteria(temp.getCriteria());
                    res.setDestinationAddress(temp.getDestinationAddress());
                    res.setNotificationFormat(temp.getNotificationFormat());
                    res.setNotifyURL(temp.getNotifyURL());
                    if(temp.getDate().toString() != null){
                        res.setDate(dateFormat.format(temp.getDate()));
                    }
//                    Date cretedDate = temp.getDate();
//                    res.setDate(dateFormat.format(cretedDate));
                    
                    responseObj.add(res);
                }
                ObjectMapper mapper = new ObjectMapper();
                jsonreturn = mapper.writeValueAsString(responseObj);
            }
            sendJSONResponse(response,jsonreturn,OK,null);
            
        } else if (action.equalsIgnoreCase("editSubscriberData")) {
            String userid = request.getParameter("userid");
            
        } else if (action.equalsIgnoreCase("login")) {
            String userid = request.getParameter("userid");
            String ret = null;
            
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            
            if (smsimpl.saveUser(userid)) {
                ret = "success";
            } else {
                ret = "error";
            }
            try {
                out.println(ret);
            } finally {
                out.close();
            }            
        }

    }
    
      public static void sendJSONResponse(HttpServletResponse response, String jsonResponse, int status, String location) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        if (location != null) {
            response.setHeader("Location", location);
        }

        ServletOutputStream output = response.getOutputStream();
        output.print(jsonResponse);
        output.flush();

    }
}

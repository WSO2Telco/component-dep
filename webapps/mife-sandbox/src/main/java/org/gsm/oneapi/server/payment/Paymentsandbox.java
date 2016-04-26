/*
 * Paymentsandbox.java
 * May 20, 2014  4:55:30 PM
 * Roshan.Saputhanthri
 *
 * Copyright (C) Dialog Axiata PLC. All Rights Reserved.
 */
package org.gsm.oneapi.server.payment;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mife.sandbox.model.PaymentImpl;
import mife.sandbox.model.entities.Paymentparam;
import mife.sandbox.model.entities.SandboxError;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * <TO-DO>
 * <code>Paymentsandbox</code>
 *
 * @version $Id: Paymentsandbox.java,v 1.00.000
 */
public class Paymentsandbox extends PaymentServlet {

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

        String action = request.getHeader("payact");

        if (action.equalsIgnoreCase("addparam")) {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();

            String ret = "error";
            //String msg = URLDecoder.decode(msg.toString(), "UTF-8");
            String user = (String) request.getParameter("userid");
            String paystatus = (String) request.getParameter("paystatus");
            String maxtrn = (String) request.getParameter("maxtrn");
            String maxamt = (String) request.getParameter("maxamt");

            if ((new PaymentImpl().saveParam(user, paystatus, maxtrn, maxamt))) {
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
            Paymentparam payparam = new PaymentImpl().queryPaymentParam(user);
                                   
            if (payparam == null) {
                jsonreturn = new Gson().toJson(new SandboxError(ret));
            } else {
                ObjectMapper mapper = new ObjectMapper();
                jsonreturn = mapper.writeValueAsString(payparam);
            }
            sendJSONResponse(response,jsonreturn,OK,null);
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

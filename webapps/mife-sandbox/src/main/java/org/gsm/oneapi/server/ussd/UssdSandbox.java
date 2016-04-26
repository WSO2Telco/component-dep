/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gsm.oneapi.server.ussd;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mife.sandbox.controller.UssdCtrlFunctions;
import org.apache.log4j.Logger;
import org.gsm.oneapi.server.OneAPIServlet;
import org.gsm.oneapi.server.payment.AmountRefundServlet;

/**
 *
 * @author User
 */
public class UssdSandbox extends OneAPIServlet{
    
    static Logger logger = Logger.getLogger(UssdSandbox.class);
    private static final long serialVersionUID = -6237772242372106922L;
    
    
    public void init() throws ServletException {
        logger.debug("UssdSandbox initialised");
    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        
        String action = request.getHeader("payact");
        
        if(action.equals("getSubTableData")){
            
            String user = request.getParameter("user");
            
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            
            ArrayList l = (ArrayList) UssdCtrlFunctions.getUssdSubscriptions(Integer.valueOf(user));
            
            
            try {
                out.println("");
            } finally {
                out.close();
            }
            
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mife.sandbox.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mife.sandbox.controller.UssdCtrlFunctions;
import mife.sandbox.model.UssdImpl;
import mife.sandbox.model.entities.User;
import mife.sandbox.model.entities.UssdSubscriptions;
import mife.sandbox.model.functions.UssdFunctions;

/**
 *
 * @author User
 */
public class UssdServlet extends HttpServlet{
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String[] requestParts = getRequestParts(request);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        UssdImpl ussdImpl = new UssdImpl();
        
        if (requestParts[0].equals("getSubTableData")) {
            String receivedUserId = request.getParameter("userid").replaceAll("\\s", "");
            
            if (request.getParameter("userid") != null) {
                int userId = ((User)ussdImpl.getUser(receivedUserId)).getId();
                List<UssdSubscriptions> ussdSubs = UssdCtrlFunctions.getUssdSubscriptions(userId);
                for (UssdSubscriptions temp : ussdSubs) {
                    out.println("<tr>");
                    out.println("<td>" + temp.getSubscriptionId() + "</td>");
                    out.println("<td>" + temp.getCallbackData() + "</td>");
                    out.println("<td>" + temp.getNotifyUrl() + "</td>");
                    out.println("<td>" + temp.getClientCorrelator() + "</td>");
                    out.println("<td>" + temp.getResourceUrl() + "</td>");
                    out.println("<td>" + temp.getCreatedDate() + "</td>");
                    out.println("<td>");
                    out.println("<input type=\"hidden\" class=\"row_data_key\" value=\" " + temp.getSubscriptionId() + " \" />");
                    out.println("<a title=\"Unsubscribe\" class=\"delete_tbl_icon\"><img src=\"/sandbox/site/themes/default/images/icons/color/cross.png\" /></a>");
                    out.println("</td>");
                    out.println("</tr>");
                }
            }
        } else if (requestParts[0].equals("CancelUssdSub")){
            String return_val;

            try {
                String id = request.getParameter("subId").replaceAll("\\s", "");
                return_val = UssdFunctions.unsubscribeUssdSub(id) + "";
            } catch (Exception e) {
                return_val = e.getMessage();
            }
            try {
                out.println(return_val);
            } finally {
                out.close();
            }
        }
        
        
    }
    
    
    public static String[] getRequestParts(HttpServletRequest request) {
        String[] requestParts = null;
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            if (pathInfo.startsWith("/")) {
                pathInfo = pathInfo.substring(1);
            }
            requestParts = pathInfo.split("/");
        }
        return requestParts;
    }
}

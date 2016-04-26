package mife.sandbox.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mife.sandbox.controller.DeliverySubscriptionCtrlFunctions;
import mife.sandbox.model.SmsImpl;
import mife.sandbox.model.entities.DeliverySubscription;
import mife.sandbox.model.entities.User;
import mife.sandbox.model.functions.DeliverySubscriptionFunctions;

/**
 *
 * @author User
 */
public class DeliverySubscriptionServlet extends HttpServlet{
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] requestParts = getRequestParts(request);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        //String return_val = "";

        SmsImpl smsimpl = new SmsImpl();
        
        if (requestParts[0].equals("GetActiveSubscriptions")) {
            String receivedUserId = request.getParameter("userid").replaceAll("\\s", "");
            
            if (request.getParameter("userid") != null) {
                int userId = ((User)smsimpl.getUser(receivedUserId)).getId();
                List<DeliverySubscription> deliverySubs = DeliverySubscriptionCtrlFunctions.getActiveSubsctiptions(userId);
                for (DeliverySubscription temp : deliverySubs) {
                    out.println("<tr>");
                    out.println("<td class=\"editable_td\">" + temp.getFilterCriteria() + "</td>");
                    out.println("<td class=\"editable_td\">" + temp.getNotifyUrl() + "</td>");
                    out.println("<td class=\"editable_td\">" + temp.getCallbackData() + "</td>");
                    out.println("<td class=\"editable_td\">" + temp.getClientCorrelator() + "</td>");
                    out.println("<td class=\"da-icon-column\">");
                    out.println("<input type=\"hidden\" class=\"row_data_key\" value=\" " + temp.getId() + " \" />");
                    out.println("<a title=\"Unsubscribe\" class=\"delete_tbl_icon\"><img src=\"/sandbox/site/themes/default/images/icons/color/cross.png\" /></a>");
                    out.println("</td>");
                    out.println("</tr>");
                }
            }
        } else if (requestParts[0].equals("RemoveDeliverySub")) {
            String rowId = request.getParameter("rowid").replaceAll("\\s", "");
            if(DeliverySubscriptionFunctions.deleteDeliverySubscription(Integer.parseInt(rowId))){
                out.println("true");
            }
            else {
                out.println("false");
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

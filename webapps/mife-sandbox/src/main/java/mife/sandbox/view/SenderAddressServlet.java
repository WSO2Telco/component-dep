/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mife.sandbox.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mife.sandbox.controller.SenderAddressCtrlFunctions;
import mife.sandbox.controller.UserControlFunctions;
import mife.sandbox.model.SmsImpl;
import mife.sandbox.model.entities.SenderAddress;
import mife.sandbox.model.entities.User;
import mife.sandbox.model.functions.SenderAddressFunctions;
import org.gsm.oneapi.server.OneAPIServlet;

/**
 *
 * @author User
 */
public class SenderAddressServlet extends OneAPIServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] requestParts = getRequestParts(request);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        SmsImpl smsimpl = new SmsImpl();
        
        if (requestParts[0].equals("CreateSenderAddress")) {

            String return_val;

            String userid = request.getParameter("userid").replaceAll("\\s", "");
            String shortcode = request.getParameter("shortcode");
            String description = request.getParameter("description");

            int useridInt = ((User)smsimpl.getUser(userid)).getId();

            SenderAddress sa = new SenderAddress();
            sa.setShortCode(shortcode);
            sa.setDescription(description);
            sa.setUser(UserControlFunctions.getUserObj(useridInt));

            int[] receivedData = SenderAddressFunctions.saveSenderAddress(sa);
            if (receivedData[0] == 201) {
                return_val = "true," + receivedData[1];
            } else {
                return_val = "false";
            }
            System.out.println(return_val);

            try {
                out.println(return_val);
            } finally {
                out.close();
            }

        } else if (requestParts[0].equals("GetShortCodesData")) {
            String userid = request.getParameter("userid").replaceAll("\\s", "");

            if (request.getParameter("userid") != null) {
                int useridInt = ((User)smsimpl.getUser(userid)).getId();
                List<SenderAddress> senderAddressRes = SenderAddressCtrlFunctions.getActiveSenderAddressList(useridInt);
                for (SenderAddress temp : senderAddressRes) {
                    out.println("<tr>");
                    out.println("<td class=\"editable_td\">" + temp.getShortCode() + "</td>");
                    out.println("<td class=\"editable_td\">" + temp.getDescription() + "</td>");
                    out.println("<td class=\"da-icon-column\">");
                    out.println("<input type=\"hidden\" class=\"row_data_key\" value=\" " + temp.getId() + " \" />");
                    out.println("<a title=\"Save\" class=\"save_edit_icon\" style=\"display: none;\"><img src=\"/sandbox/site/themes/default/images/icons/color/disk.png\"/></a>");
                    out.println("<a title=\"Reset\" class=\"reset_edit_icon\" style=\"display: none;\"><img src=\"/sandbox/site/themes/default/images/icons/color/arrow_redo.png\"/></a>");
                    out.println("<a title=\"Edit\" class=\"edit_tbl_icon\"><img src=\"/sandbox/site/themes/default/images/icons/color/pencil.png\"/></a>");
                    out.println("<a title=\"Delete\" class=\"delete_tbl_icon\"><img src=\"/sandbox/site/themes/default/images/icons/color/cross.png\" /></a>");
                    out.println("</td>");
                    out.println("</tr>");
                }
            }
        } else if (requestParts[0].equals("DeleteShortCode")) {
            String return_val;

            try {
                String id = request.getParameter("id").replaceAll("\\s", "");
                int idX = Integer.parseInt(id);

                return_val = SenderAddressFunctions.deleteSenderAddress(idX) + "";
            } catch (Exception e) {
                return_val = e.getMessage();
            }
            try {
                out.println(return_val);
            } finally {
                out.close();
            }
        } else if (requestParts[0].equals("EditShortCode")) {
            String return_val;

            try {
                String id = request.getParameter("id").replaceAll("\\s", "");
                String code = request.getParameter("shortcode");
                String description = request.getParameter("description");

                int idX = Integer.parseInt(id);

                return_val = SenderAddressFunctions.editSenderAddress(idX, code, description) + "";
            } catch (Exception e) {
                return_val = e.getMessage();
            }
            try {
                out.println(return_val);
            } finally {
                out.close();
            }
        } else if (requestParts[0].equals("SearchShortCode")) {
            String receivedSAId = request.getParameter("id").replaceAll("\\s", "");
            if (receivedSAId != null) {
                int saId = Integer.valueOf(receivedSAId);
                SenderAddress sa = SenderAddressCtrlFunctions.getSenderAddressData(saId);
                out.println(sa.getId() + "," + sa.getShortCode() + "," + sa.getDescription());
            }
        } else if (requestParts[0].equals("GetShortCodesArray")) {
            String receivedUserId = request.getParameter("userid").replaceAll("\\s", "");
            
            if (request.getParameter("userid") != null) {
                int userId = ((User)smsimpl.getUser(receivedUserId)).getId();  //Integer.parseInt(receivedUserId);

                List<SenderAddress> activeNumbers = SenderAddressCtrlFunctions.getActiveSenderAddressList(userId);
                out.print("[");
                for (SenderAddress temp : activeNumbers) {
                    out.print("\"" + temp.getShortCode().replaceAll("\\s", "") + "\",");
                }
                out.print("]");
            }
        } else if (requestParts[0].equals("GetDescriptionsArray")) {
            String receivedUserId = request.getParameter("userid").replaceAll("\\s", "");
            if (request.getParameter("userid") != null) {
                int userId = ((User)smsimpl.getUser(receivedUserId)).getId();//Integer.parseInt(receivedUserId);

                List<SenderAddress> activeNumbers = SenderAddressCtrlFunctions.getActiveSenderAddressList(userId);
                out.print("[");
                for (SenderAddress temp : activeNumbers) {
                    out.print("\"" + temp.getDescription() + "\",");
                }
                out.print("]");
            }
        } else if (requestParts[0].equals("FileterShortCodeData")) {
            String receivedUserId = request.getParameter("userid").replaceAll("\\s", "");
            String shortcode = request.getParameter("shortcode");
            String descript = request.getParameter("description");

            if (request.getParameter("userid") != null) {
                int userId = ((User)smsimpl.getUser(receivedUserId)).getId();

                List<SenderAddress> filteredCodes = SenderAddressFunctions.getFilterdAddressData(userId, shortcode, descript);
                if (filteredCodes.size() > 0) {
                    for (SenderAddress temp : filteredCodes) {
                        out.println("<tr>");
                        out.println("<td class=\"editable_td\">"+temp.getShortCode()+"</td>");
                        out.println("<td class=\"editable_td\">"+temp.getDescription()+"</td>");
                        out.println("<td class=\"da-icon-column\">");
                        out.println("<input type=\"hidden\" class=\"row_data_key\" value=\""+temp.getId()+"\" />");
                        out.println("<a title=\"Save\" class=\"save_edit_icon\" style=\"display: none;\"><img src=\"/sandbox/site/themes/default/images/icons/color/disk.png\"/></a>");
                        out.println("<a title=\"Reset\" class=\"reset_edit_icon\" style=\"display: none;\"><img src=\"/sandbox/site/themes/default/images/icons/color/arrow_redo.png\"/></a>");
                        out.println("<a title=\"Edit\" class=\"edit_tbl_icon\"><img src=\"/sandbox/site/themes/default/images/icons/color/pencil.png\"/></a>");
                        out.println("<a title=\"Delete\" class=\"delete_tbl_icon\"><img src=\"/sandbox/site/themes/default/images/icons/color/cross.png\" /></a>");
                        out.println("</td>");
                        out.println("</tr>");
                    }
                } else {
                    out.println("<tr>");
                    out.println("<td colspan = '4' style='text-align:center;'><h2>No data found for query</h2></td>");
                    out.println("</tr>");
                }
            }
        }
    }
}
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
import mife.sandbox.controller.ManageNumberFunctions;
import mife.sandbox.controller.UserControlFunctions;
import mife.sandbox.model.SmsImpl;
import mife.sandbox.model.entities.ManageNumber;
import mife.sandbox.model.entities.Sms;
import mife.sandbox.model.entities.User;
import mife.sandbox.model.functions.NumberFunctions;

/**
 *
 * @author User
 */
public class ManageNumberServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String[] requestParts = getRequestParts(request);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        //String return_val = "";

        SmsImpl smsimpl = new SmsImpl();

        if (requestParts[0].equals("EditNumber")) {
            String return_val;

            try {
                String id = request.getParameter("id").replaceAll("\\s", "");
                String phoneNumber = request.getParameter("phonenumber");
                String description = request.getParameter("description");
                String balance = request.getParameter("balance");

                int idX = Integer.parseInt(id);
                double bal = Double.parseDouble(balance);
                
                return_val = NumberFunctions.editNumber(idX, phoneNumber, description, bal) + "";
            } catch (Exception e) {
                return_val = e.getMessage();
            }
            try {
                out.println(return_val);
            } finally {
                out.close();
            }

        } else if (requestParts[0].equals("SaveNumber")) {
            String phoneNumber = request.getParameter("phonenumber");
            String description = request.getParameter("description");
            String balance = request.getParameter("balance");

            String userid = request.getParameter("userid").replaceAll("\\s", "");
            int user_id = ((User) smsimpl.getUser(userid)).getId();//Integer.parseInt(userid);

            String return_val = "";

            double bal = Double.parseDouble(balance);

            if (ManageNumberFunctions.isNumberExistforUser(user_id, phoneNumber)) {
                return_val = "false_d"; 
                System.out.println("Number already exists");
            } else {
                ManageNumber num = new ManageNumber();
                num.setNumber(phoneNumber);
                num.setDescription(description);
                num.setBalance(bal);
                num.setStatus(1);
                num.setUser(UserControlFunctions.getUserObj(user_id));

                int[] receivedData = NumberFunctions.saveNumber(num);
                if (receivedData[0] == 201) {
                    return_val = "true," + receivedData[1];
                } else {
                    return_val = "false";
                }
                //System.out.println(return_val);
            }
            try {
                out.println(return_val);
            } finally {
                out.close();
            }
        } else if (requestParts[0].equals("DeleteNumber")) {
            String return_val;

            try {
                String id = request.getParameter("id").replaceAll("\\s", "");
                int idX = Integer.parseInt(id);

                return_val = NumberFunctions.deleteNumber(idX) + "";
            } catch (Exception e) {
                return_val = e.getMessage();
            }
            try {
                out.println(return_val);
            } finally {
                out.close();
            }
        } else if (requestParts[0].equals("GetNumberData")) {

            String receivedUserId = request.getParameter("userid").replaceAll("\\s", "");
            if (request.getParameter("userid") != null) {
                int userId = ((User) smsimpl.getUser(receivedUserId)).getId();//Integer.parseInt(receivedUserId);

                List<ManageNumber> activeNumbers = ManageNumberFunctions.getActiveNumbers(userId); //UserId should ve replaced with logId
                for (ManageNumber temp : activeNumbers) {
                    out.println("<tr>");
                    out.println("<td class=\"editable_td\">" + temp.getNumber() + "</td>");
                    out.println("<td class=\"editable_td\">" + temp.getDescription() + "</td>");
                    out.println("<td class=\"editable_td\">" + temp.getBalance() + "</td>");
                    out.println("<td class=\"da-icon-column\">");
                    out.println("<input type=\"hidden\" class=\"row_data_key\" value=\"" + temp.getId() + "\"/>");
                    out.println("<a title=\"Save\" class=\"save_edit_icon\" style=\"display: none;\"><img src=\"/sandbox/site/themes/default/images/icons/color/disk.png\"/></a>");
                    out.println("<a title=\"Reset\" class=\"reset_edit_icon\" style=\"display: none;\"><img src=\"/sandbox/site/themes/default/images/icons/color/arrow_redo.png\"/></a>");
                    out.println("<a title=\"Edit\" class=\"edit_tbl_icon\"><img src=\"/sandbox/site/themes/default/images/icons/color/pencil.png\"/></a>");
                    out.println("<a title=\"Delete\" class=\"delete_tbl_icon\"><img src=\"/sandbox/site/themes/default/images/icons/color/cross.png\" /></a>");
                    out.println("</td>");
                    out.println("</tr>");
                }
            }
        } else if (requestParts[0].equals("FilterNumbers")) {
            String receivedUserId = request.getParameter("userid").replaceAll("\\s", "");
            String receivedNumber = request.getParameter("phonenumber");
            String receivedDesc = request.getParameter("description");

            if (request.getParameter("userid") != null) {
                int userId = ((User) smsimpl.getUser(receivedUserId)).getId(); //Integer.valueOf(receivedUserId);

                List<ManageNumber> filteredNumbers = ManageNumberFunctions.getFilteredNumbers(userId, receivedNumber, receivedDesc);
                if (filteredNumbers.size() > 0) {
                    for (ManageNumber temp : filteredNumbers) {
                        out.println("<tr>");
                        out.println("<td class=\"editable_td\">" + temp.getNumber() + "</td>");
                        out.println("<td class=\"editable_td\">" + temp.getDescription() + "</td>");
                        out.println("<td class=\"editable_td\">" + temp.getBalance() + "</td>");
                        out.println("<td class=\"da-icon-column\">");
                        out.println("<input type=\"hidden\" class=\"row_data_key\" value=\" " + temp.getId() + " \" />");
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
            } else {

                System.out.println("User Not Found");
            }
        } else if (requestParts[0].equals("SearchNumber")) {
            String receivedNumberId = request.getParameter("numberid").replaceAll("\\s", "");
            if (receivedNumberId != null) {
                int numId = Integer.valueOf(receivedNumberId);
                ManageNumber res = ManageNumberFunctions.searchNumber(numId);
                out.println(res.getId() + "," + res.getNumber() + "," + res.getDescription() + "," + res.getBalance());
            }
        } else if (requestParts[0].equals("GetNumberList")) {
            String receivedUserId = request.getParameter("userid");
            if (request.getParameter("userid") != null) {
                int userId = ((User) smsimpl.getUser(receivedUserId)).getId();   //Integer.parseInt(receivedUserId);

                List<ManageNumber> activeNumbers = ManageNumberFunctions.getActiveNumbers(userId);
                out.print("[");
                for (ManageNumber temp : activeNumbers) {
                    out.print("\"" + temp.getNumber().replaceAll("\\s", "") + "\",");
                }
                out.print("]");
            }
        } else if (requestParts[0].equals("GetDescriptionsList")) {
            String receivedUserId = request.getParameter("userid");
            if (request.getParameter("userid") != null) {
                int userId = ((User) smsimpl.getUser(receivedUserId)).getId();  //Integer.parseInt(receivedUserId);

                List<ManageNumber> descriptions = ManageNumberFunctions.getActiveNumbers(userId);
                out.print("[");
                for (ManageNumber temp : descriptions) {
                    out.print("\"" + temp.getDescription().replaceAll("\"", "") + "\",");
                }
                out.print("]");
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

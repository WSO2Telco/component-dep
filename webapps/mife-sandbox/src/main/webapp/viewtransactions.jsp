<%@page import="java.sql.ResultSet"%>
<%@page import="org.dialog.mifesandbox.payment.PaymentFunctions"%>
<%
    String userid = "";
    String selectValue = "";
    if (request.getSession().getAttribute("user_profile_id") == null) {
        response.sendRedirect("index.jsp");
    } else {
        userid = request.getSession().getAttribute("user_profile_id").toString();

        if (request.getParameter("filterval") != null) {
            selectValue = request.getParameter("filterval").toString();
        }
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
    <head>
        <title>Payment API | MIFE Sandbox Portal</title>
        <%@include file="commons/common_head.jsp" %>
        <link rel="stylesheet" href="/mifesandbox/template/table_styles.css"/>
    </head>
    <body>
        <div id="fullContainer">
            <%@include file="commons/header.jsp" %>
            <div class="container_12">
                <div class="grid_12">
                    <!-- Comment needed for IE7 -->
                </div>
            </div>

            <div id="body_container" class="container_12 clearfix">
                <div class="grid_12">
                    <h2>View Transactions</h2>

                    <div class="grid_4 suffix_1 omega">

                        <%@include file="commons/response_header_display.jsp" %>
                        <form action="viewtransactions.jsp" method="POST">
                            <p style="">
                                <label for="req_balance">End User</label><span class="requiredField ">*</span> 
                                <br/>
                                <select name="filterval" style="width: 200px;">
                                    <option value="">All</option>
                                    <%
                                        ResultSet s = PaymentFunctions.getTransactionEndUserList(userid);
                                        if (!s.isFirst()) {
                                            while (s.next()) {
                                                String num = s.getString(1).substring(Math.max(0, s.getString(1).length() - 11));
                                    %>
                                    <option value="<%= num%>"> <%= num%> </option>
                                    <%
                                        }
                                    } else {%>
                                    <option value="" disabled> No subscribers found</option>
                                    <%} s.close(); %>
                                </select>
                                <input type="submit" class="primary" style="width: 90px; height: 30px; float: right;" value="Filter"/>
                            </p>
                        </form>
                        <hr/>
                    </div>

                    <table>
                        <tr>
                            <th>Transaction ID</th>
                            <th>End User</th>
                            <th>Amount</th>
                            <th style="width: 40%;">Description</th>
                            <th>Status</th>
                        </tr>
                        <%
                            try {
                                ResultSet t = PaymentFunctions.getTransactionData(userid, selectValue);
                                if (!t.isFirst()) {
                                    while (t.next()) {
                        %>
                        <tr>
                            <td><%= t.getString("id")%></td>
                            <td><%= t.getString("endUserId")%></td>
                            <td><%= t.getString("amount") + " " + t.getString("currency")%></td>
                            <td><%= t.getString("description")%></td>
                            <td><%= t.getString("transaction_op_status")%></td>

                        </tr>
                        <%                            }
                        } else {
                        %>
                        <tr>
                            <td colspan="2">No Transactions added yet!</td>
                        </tr>
                        <%                                        }
                                t.close();
                        } catch (Exception e) {
                        %>
                        <tr>
                            <td colspan="2" style="color: red;">ERROR! <%= e.getMessage()%></td>
                        </tr>
                        <%
                            }
                        %>
                    </table>

                </div>
            </div>
            <div id="clearfooter"></div>
        </div>

        <%@include file="commons/footer.jsp" %>

    </body>
</html>

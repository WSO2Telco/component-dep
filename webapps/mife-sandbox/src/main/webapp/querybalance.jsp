<%@page import="java.sql.ResultSet"%>
<%@page import="org.dialog.mifesandbox.payment.PaymentFunctions"%>
<%
    String userProfileId = "";
    String req_number = "";
    String balance = "";
    if (request.getSession().getAttribute("user_profile_id") == null) {
        response.sendRedirect("index.jsp");
    } else {
        userProfileId = request.getSession().getAttribute("user_profile_id").toString();

        if (request.getParameter("req_number") != null) {
            req_number = request.getParameter("req_number");
            balance = request.getParameter("req_balance");
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
                    <h2>Query Balance</h2>

                    <div id="inputArea">
                        <form action="QueryBalanceCharge" method="POST" id="sms">
                            <div class="grid_10 prefix_1 suffix_1 alpha omega">
                                <div class="grid_4 suffix_1 omega">

                                    <%@include file="commons/response_header_display.jsp" %>

                                    <p style="">
                                        <label for="req_number">Whitelist Number</label><span class="requiredField ">*</span> 
                                        <br/>
                                        <input class="text_" name="req_number" id="req_number" type="text" value="<%= req_number%>" required/>
                                    </p>
                                    <p style="">
                                        <label for="req_balance">Balance to Set</label><span class="requiredField ">*</span> 
                                        <br/>
                                        <input class="text_" name="req_balance" id="req_balance" type="text" value="<%= balance%>" required/>
                                        <input type="submit" class="primary" style="width: 90px; float: right;" value="Set Balance"/>
                                    </p>
                                </div>
                                <div class="grid_4 suffix_1 alpha omega" style="width: 750px;">
                                    <hr/>
                                    <table>
                                        <tr>
                                            <th>Number</th>
                                            <th>Balance</th>
                                        </tr>
                                        <%
                                            ResultSet r = PaymentFunctions.getBalanceData(userProfileId);
                                            if (r.isBeforeFirst()) {
                                                while (r.next()) {
                                        %>
                                        <tr>
                                            <td><%= r.getString("subscriber_num")%></td>
                                            <td><%= r.getString("balance")%></td>
                                        </tr>
                                        <% }
                                        } else {
                                        %>
                                        <tr>
                                            <td colspan="2">No Subscriber(s) added yet!</td>
                                        </tr>
                                        <%        }
                                            r.close();%>
                                    </table>
                                    <div class="grid_4 prefix_1 suffix_1 alpha"  style="text-align: right; width: 670px;;">
                                        <p style="text-align: right; margin-right: 5px;"> </p>
                                    </div>


                                </div>
                            </div>
                        </form>
                    </div>

                </div>
            </div>
            <div id="clearfooter"></div>
        </div>

        <%@include file="commons/footer.jsp" %>

    </body>
</html>

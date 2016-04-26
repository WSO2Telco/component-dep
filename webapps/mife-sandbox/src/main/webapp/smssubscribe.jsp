<%@page import="org.dialog.mifesandbox.sms.SMSMethods"%>
<%@page import="java.sql.ResultSet"%>
<%
    String userProfileId = "";
    if (request.getSession().getAttribute("user_profile_id") == null) {
        response.sendRedirect("index.jsp");
    } else {
        userProfileId = request.getSession().getAttribute("user_profile_id").toString();
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
                    <h2>SMS Subscribers List</h2>

                    <div id="inputArea">
                        <form action="SandboxSMSSubscribe" method="POST" id="sms">
                            <div class="grid_10 prefix_1 suffix_1 alpha omega">
                                <div class="grid_4 suffix_1 omega">
                                    <%@include file="commons/response_header_display.jsp" %>
                                    <p style="">
                                        <label for="max_notifications">Add new Subscriber</label><span class="requiredField ">*</span> 
                                        <br/>
                                        <input class="text_" name="subscriber" id="max_notifications" type="text" value=""/>
                                        <input type="submit" class="primary" style="width: 90px; float: right;" value="Add"/>
                                    </p>
                                </div>
                                <div class="grid_4 suffix_1 alpha omega" style="width: 750px;">
                                    <hr/>
                                    <table>
                                        <tr>
                                            <th>Subscriber(s)</th>
                                        </tr>
                                        <%
                                            ResultSet r = SMSMethods.getSubscriberList(userProfileId);
                                            if (r.isBeforeFirst()) {
                                                while (r.next()) {
                                        %>
                                        <tr>
                                            <td><%= r.getString("subscriber_num")%></td>
                                        </tr>
                                        <% }
                                        } else {
                                        %>
                                        <tr>
                                            <td>No Subscriber(s) added yet!</td>
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

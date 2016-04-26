<%@page import="java.sql.ResultSet"%>
<%@page import="org.dialog.mifesandbox.Support.UserData"%>
<%
    String maxNotifications = "";
    String notificationDelay = "";
    String msgText = "";
    String senderWhiteList = "";
    String msgCriteria = "";

    String deliveryStatus = "";
    String deliveryStatusDelay = "";
    String shortCodes = "";

    if (request.getSession().getAttribute("user_profile_id") == null) {
        response.sendRedirect("index.jsp");
    } else {
        try {
            ResultSet sms = UserData.getSMSData(request.getSession().getAttribute("user_profile_id").toString());
            while (sms.next()) {
                maxNotifications = sms.getString("max_notifications");
                notificationDelay = sms.getString("notification_delay");
                msgText = sms.getString("message_text");
                senderWhiteList = sms.getString("sender_white_list");
                msgCriteria = sms.getString("message_criterias");

                deliveryStatus = sms.getString("delivery_status");
                deliveryStatusDelay = sms.getString("delivery_status_delay");
                shortCodes = sms.getString("short_codes");
            }
        } catch (Exception e) {
            System.out.println("Reading SMS data in jap: " + e);
        }
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
    <head>
        <title>SMS API | MIFE Sandbox Portal</title>
        <%@include file="commons/common_head.jsp" %>
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
                    <%@include file="commons/response_header_display.jsp" %>
                    <h2>SMS</h2>

                    <div id="inputArea">
                        <form action="SMSSandBoxService" method="POST" id="sms">
                            <div class="grid_10 prefix_1 suffix_1 alpha omega">
                                <div class="grid_4 suffix_1 alpha omega" style="width: 750px;">

                                    <p>
                                        <br/>
                                        <label>Mobile Originated Traffic</label>
                                    </p>
                                    <p>
                                        <label for="max_notifications">Max Notifications</label><span class="requiredField ">*</span>
                                        <br/>
                                        <input class="text_" name="max_notifications" id="max_notifications" type="text" value="<%= maxNotifications %>" required/>
                                    </p>

                                    <p>
                                        <label for="notification_delay">Notification Delay</label><span class="requiredField ">*</span>
                                        <br/>
                                        <input class="text_" name="notification_delay" id="notification_delay" type="text" value="<%= notificationDelay %>" required/>
                                    </p>

                                    <p>
                                        <label for="msg_txt">Message Text</label><span class="requiredField ">*</span>
                                        <br/>
                                        <textarea class="text_" name="message_text" id="message_text" style="height: 90px;" required><%= msgText %></textarea>
                                    </p>

                                    <p>
                                        <label for="sender_list">Sender Whitelist</label><span class="requiredField " required>*</span>
                                        <br/>
                                        <textarea class="text_" name="sender_white_list" id="sender_white_list" style="height: 90px;"><%= senderWhiteList %></textarea>
                                    </p>

                                    <p>
                                        <label for="msg_criteria">Message Criteria</label><span class="requiredField " required>*</span>
                                        <br/>
                                        <textarea class="text_" name="message_criterias" id="message_criterias" style="height: 90px;"><%= msgCriteria %></textarea>
                                    </p>


                                    <p>
                                        <br/>
                                        <label>Mobile Terminated Traffic</label>
                                    </p>
                                    <p>
                                        <label for="delivery_state">Delivery Status</label><span class="requiredField ">*</span>
                                        <br/>
                                        <select name="delivery_status" id="delivery_status" style="width: 300px;">
                                            <option value="<%= deliveryStatus %>" selected><%= deliveryStatus %></option>
                                            <option value="" disabled> -- </option>
                                            <option value="DeliveredToNetwork">DeliveredToNetwork</option>
                                            <option value="DeliveredToTerminal">DeliveredToTerminal</option>
                                            <option value="DeliveryUncertain">DeliveredToTerminal</option>
                                            <option value="DeliveryImpossible">DeliveryImpossible</option>
                                            <option value="MessageWaiting">MessageWaiting</option>
                                            <option value="DeliveryNotificationNotSupported">DeliveryNotificationNotSupported</option>
                                        </select>
                                    </p>

                                    <p>
                                        <label for="delivery_state_delay">Delivery Status Delay</label><span class="requiredField ">*</span>
                                        <br/>
                                        <input class="text_" name="delivery_status_delay" id="delivery_status_delay" type="text" value="<%= deliveryStatusDelay %>"/>
                                    </p>
                                    <p>
                                        <label for="short_codes">Short Codes</label><span class="requiredField ">*</span>
                                        <br/>
                                        <textarea class="text_" name="short_codes" id="short_codes" style="height: 90px;"><%= shortCodes %></textarea>
                                    </p>
                                </div>
                            </div>

                            <div class="grid_4 prefix_1 suffix_1 alpha"  style="width: 750px;">
                                <p style="text-align: right; margin-right: 5px;"><button type="submit" class="primary"><strong>Save</strong></button> or <a href="index.jsp">Cancel</a></p>
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

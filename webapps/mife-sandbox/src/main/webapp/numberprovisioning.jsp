<%@page import="org.dialog.mifesandbox.payment.PaymentFunctions"%>
<%
    String userWhitelist = "";
    if (request.getSession().getAttribute("user_profile_id") == null) {
        response.sendRedirect("index.jsp");
    } else {
        userWhitelist = PaymentFunctions.getUserWhitelist(request.getSession().getAttribute("user_profile_id").toString());
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
    <head>
        <title>Payment API | MIFE Sandbox Portal</title>
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
                    <h2>Charging whitelist number provisioning</h2>

                    <div id="inputArea">
                        <form action="ChargingWhitelistServlet" method="POST" id="loginForm">
                            <div class="grid_10 prefix_1 suffix_1 alpha omega">
                                <div class="grid_4 suffix_1 alpha omega" style="width: 98%;">
                                    <p>
                                        <label for="numbers">Whitelisted Number(s)</label><span class="requiredField ">*</span>
                                        <br/>
                                        <textarea class="text_" name="numbers" id="numbers" style="height: 150px;" required><%= userWhitelist %></textarea>
                                    </p>
                                </div>
                            </div>

                            <div class="grid_4 prefix_1 suffix_1 alpha" style="text-align: right; width: 750px;">
                                <p><button type="submit" class="primary"><strong>Update</strong></button> or <a href="index.jsp">Cancel</a></p>
                            </div>
                        </form>

                    </div>

                    <script language="javascript" type="text/javascript">
                        $(function() {
                            $('#numbers').focus();
                        });
                    </script>
                </div>
            </div>
            <div id="clearfooter"></div>
        </div>

        <%@include file="commons/footer.jsp" %>

    </body>
</html>

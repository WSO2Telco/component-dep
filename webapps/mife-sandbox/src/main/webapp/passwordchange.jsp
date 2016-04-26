<%
    if(request.getSession().getAttribute("user_profile_id") == null){
        response.sendRedirect("index.jsp");
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
    <head>
        <title>Change Your Password | MIFE Sandbox Portal</title>
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
                    <h2>Change Your Password</h2>

                    <div id="inputArea">
                        <form action="ChangeUserPassword" method="POST" id="change_user_password" name="change_user_password">
                            <div class="grid_10 prefix_1 suffix_1 alpha omega">
                                <div class="grid_4 suffix_1 alpha omega">
                                    <p>
                                        <label for="currentpassword">Current Password</label><span class="requiredField ">*</span>
                                        <br/>
                                        <input class="text_" name="current_password" id="currentpassword" type="password" required/>
                                    </p>

                                    <p>
                                        <label for="newpassword">New Password</label><span class="requiredField ">*</span>
                                        <br/>
                                        <input class="text_" name="new_password" id="newpassword" type="password" required/>
                                    </p>

                                    <p>
                                        <label for="re_newpassword">Confirm New Password</label><span class="requiredField ">*</span>
                                        <br/>
                                        <input class="text_" name="confirm_new_password" id="re_newpassword" type="password" required/>
                                    </p>
                                </div>

                                <div class="grid_4 prefix_1 alpha omega">

                                </div>

                            </div>

                            <div class="grid_4 prefix_1 suffix_1 alpha">
                                <p><button type="submit" class="primary"><strong>Change Password</strong></button> or <a href="index.jsp">Cancel</a></p>
                            </div>

                        </form>

                    </div>

                    <script language="javascript" type="text/javascript">
                        $(function() {
                            $('#currentpassword').focus();
                        });
                    </script>
                </div>


            </div>
            <div id="clearfooter"></div>
        </div>

        <%@include file="commons/footer.jsp" %>

    </body>
</html>

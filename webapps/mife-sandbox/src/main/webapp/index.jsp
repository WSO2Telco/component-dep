<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
    if(request.getSession().getAttribute("user_profile_id") != null){
        response.sendRedirect("profile.jsp");
    }
%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
    <head>
        <title>Please Login | MIFE Sandbox Portal</title>
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
                    <h2>Please Login</h2>

                    <div id="inputArea">
                        <form action="LoginService" method="POST" id="loginForm">

                            <div class="grid_10 prefix_1 suffix_1 alpha omega">

                                <div class="grid_4 suffix_1 alpha omega right-border">
                                    <p>
                                        <%@include file="commons/response_header_display.jsp" %>
                                    </p>
                                    <p>
                                        <label for="username">Username</label><span class="requiredField ">*</span>
                                        <br/>
                                        <input name="j_username" id="username" type="text" required="requred"/>
                                    </p>

                                    <p>
                                        <label for="password">Password</label><span class="requiredField ">*</span>
                                        <br/>
                                        <input class="text_" name="j_password" id="password" type="password" required="required"/>
                                    </p>

                                    <p>
                                        <!-- a id="forgottenPassword" href="/mifesandbox/g/user/forgotPassword">Forgot your password?</a-->
                                    </p>
                                </div>

                                <div class="grid_4 prefix_1 alpha omega">

                                </div>

                            </div>

                            <div class="grid_4 prefix_1 suffix_1 alpha">
                                <p><button type="submit" class="primary"><strong>Login</strong></button></p>
                            </div>

                            <div class="grid_5 suffix_1 omega">
                                <p>
                                    <!-- a href="/mifesandbox/g/user/register" id="sign-up_btn">Sign up now</a --> 
                                </p>
                            </div>
                        </form>

                    </div>

                    <script language="javascript" type="text/javascript">
                        $(function() {
                            $('#username').focus();
                        });
                    </script>
                </div>


            </div>
            <div id="clearfooter"></div>
        </div>

        <%@include file="commons/footer.jsp" %>

    </body>
</html>

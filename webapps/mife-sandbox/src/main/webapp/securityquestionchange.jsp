<%
    if(request.getSession().getAttribute("user_profile_id") == null){
        response.sendRedirect("index.jsp");
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
    <head>
        <title>Change Security | MIFE Sandbox Portal</title>
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
                    <h2>Change Security Settings</h2>

                    <div id="inputArea">
                        <form action="ChangeSecurityQuestion" method="POST" id="change_security_question">
                            <div class="grid_10 prefix_1 suffix_1 alpha omega">
                                <div class="grid_4 suffix_1 alpha omega">
                                    <p>
                                        <label for="question">Security Question</label><span class="requiredField ">*</span>
                                        <br/>
                                        <select class="text_" name="security_question" id="question" style="width: 100%" required >
                                            <option value=""> -- </option>
                                            <option value="What was your town/city of birth?">What was your town/city of birth?</option>
                                        </select>
                                    </p>

                                    <p>
                                        <label for="answer">Security Response</label><span class="requiredField ">*</span>
                                        <br/>
                                        <input class="text_" name="security_response" id="response" type="password" required/>
                                    </p>

                                    <p>
                                        <label for="re_answer">Confirm Security Response</label><span class="requiredField ">*</span>
                                        <br/>
                                        <input class="text_" name="confirm_security_response" id="re_response" type="password" required/>
                                    </p>
                                </div>

                                <div class="grid_4 prefix_1 alpha omega">

                                </div>

                            </div>

                            <div class="grid_4 prefix_1 suffix_1 alpha">
                                <p><button type="submit" class="primary"><strong>Submit</strong></button> or <a href="index.jsp">Cancel</a></p>
                            </div>

                        </form>

                    </div>

                    <script language="javascript" type="text/javascript">
                        $(function() {
                            $('#question').focus();
                        });
                    </script>
                </div>


            </div>
            <div id="clearfooter"></div>
        </div>

        <%@include file="commons/footer.jsp" %>

    </body>
</html>

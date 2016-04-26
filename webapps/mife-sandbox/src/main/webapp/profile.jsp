<%
    if(request.getSession().getAttribute("user_profile_id") == null){
        response.sendRedirect("index.jsp");
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
    <head>
        <title>Home | MIFE Sandbox Portal</title>
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
                    <h2>Profile</h2>

                    <table style="padding: 20px; width: 180px;">
                        <tr style="height: 180px; background-color: silver;"><td><img style="height: 180px; width: 180px;" src="profilepics/FB-profile-avatar.jpg" alt="Profile Pic" /></td></tr>
                        <tr><td><br/><a href="editprofile.jsp">Edit Profile</a></td></tr>
                        <!--
                        <tr><td><a href="passwordchange.jsp">Change Password</a></td></tr>
                        <tr><td><a href="securityquestionchange.jsp">Change Security Question</a></td></tr>
                        -->
                        <tr><td><br/></td></tr>
                        <tr><td><form method="POST" action="LogoutService"><input type="submit" value="Logout" /></form></td></tr>
                    </table>
                </div>


            </div>
            <div id="clearfooter"></div>
        </div>

        <%@include file="commons/footer.jsp" %>

    </body>
</html>

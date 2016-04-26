<div id="header" class="clearfix">
    <div id="header-main" class="container_12 container_12clearfix">
        <div class="grid_12">
            <div id="user-controls" class="clearfix">
                <ul>
                    <% if (request.getSession().getAttribute("user_profile_id") != null) {%>
                    <li style="color: silver;">Welcome <a href="profile.jsp" style="color: white;"><%= request.getSession().getAttribute("user_first_name")%></a>!</li>
                    <li>
                        <form method="POST" id="logoutForm" action="LogoutService"><a href="#" onclick="document.getElementById('logoutForm').submit()">Logout</a></form>
                    </li>
                    <% } else {%>
                    <li><a href="registration.jsp"><span></span></a></li>
                    <li><a href="index.jsp" id="login_btn"><span>Log In</span></a></li>	
                    <% }%>	
                </ul>
            </div>
            <div id="left-logo-content">
                <div id="devportal-logo">
                    <h1><a href="#">MIFE Sandbox Portal</a></h1>
                </div>
            </div>
            <div id="right-logo">
            </div>
        </div>
        <div class="grid_12">
            <div class="nav-header">
                <ul>
                    <li><a href="/mifesandbox/">Home</a></li>
                    <% if (request.getSession().getAttribute("user_profile_id") != null) {%>
                    <li onmouseover="mouseover('01')" onmouseout="mouseout('01')">
                        <a href="#">APIs</a>
                        <ul id="subnavi01" class="subnavi">
                            <li style="line-height: 1; color: black; border-top-color: #999; border-top-style: solid; border-top-width: 1px;">Payment</li>
                            <li>&nbsp;&nbsp;&nbsp;<a href="/mifesandbox/numberprovisioning.jsp">Number Provision</a></li>
                            <li>&nbsp;&nbsp;&nbsp;<a href="/mifesandbox/querybalance.jsp">Query Balance</a></li>
                            <li>&nbsp;&nbsp;&nbsp;<a href="/mifesandbox/viewtransactions.jsp">View Transactions</a></li>
                            <li style="line-height: 1; color: black; border-top-color: #999; border-top-style: solid; border-top-width: 1px;">SMS</li>
                            <li>
                                &nbsp;&nbsp;&nbsp;<a href="/mifesandbox/sms.jsp">SMS Settings</a>
                            </li>
                            <li>&nbsp;&nbsp;&nbsp;<a href="/mifesandbox/smssubscribe.jsp">Subscriber Provision</a></li>
                        </ul>
                    </li>
                    <% }%>
                    <li><a href="/mifesandbox/resources.jsp">Resources</a></li>
                    <li><a href="/mifesandbox/news.jsp">News</a></li>
                    <li><a href="/mifesandbox/info.jsp">Info</a></li>
                    <% if (request.getSession().getAttribute("user_profile_id") != null) {%>
                    <li><a href="/mifesandbox/forum/index.jsp">Forum</a></li>
                    <% }%>
                </ul>
            </div>
        </div>
    </div> 
</div>
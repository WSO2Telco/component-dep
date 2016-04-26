<%
    if (request.getAttribute("repValue") != null) {

        int resId = Integer.parseInt(request.getAttribute("repValue").toString());
        String resMsg = request.getAttribute("repMsg").toString();
%>
<% if (resId == 0) {%> 
<div style="border-radius: 3px; padding: 10px; color: red; text-align: center;">
    <label>ERROR: <%= resMsg%></label>
</div>
<% } else if (resId == 1) {%> 
<div style="border-radius: 3px; padding: 10px; color: green; text-align: center;">
    <label><%= resMsg%></label>
</div>
<% } else {%> 
<div style="border-radius: 3px; padding: 10px; color: red; text-align: center; font-weight: normal;">
    <label><%= resMsg%></label>
</div>
<% }
    }%> 
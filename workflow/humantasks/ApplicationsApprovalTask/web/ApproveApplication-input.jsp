<%@ page import="org.apache.axiom.om.OMElement" %>
<%@ page import="javax.xml.namespace.QName" %>
<p>
        <%
        String customerId = "";
        String customerFirstName = "";
        String customerLastName = "";
        String amount = "";
        String region = "";

        OMElement requestElement = (OMElement) request.getAttribute("taskInput");
        String ns = "http://workflow.application.apimgt.carbon.wso2.org";

        if (requestElement != null) {

                OMElement id = requestElement.getFirstChildWithName(new QName(ns, "applicationName"));
                if (id != null) {
                    customerId = id.getText();
                }

                OMElement fName = requestElement.getFirstChildWithName(new QName(ns, "applicationId"));
                if(fName !=null){
                    customerFirstName = fName.getText();
                }

                OMElement lName = requestElement.getFirstChildWithName(new QName(ns, "applicationTier"));
                if(lName !=null){
                    customerLastName = lName.getText();
                }

            OMElement regionElement = requestElement.getFirstChildWithName(new QName(ns, "applicationName"));

            if(regionElement !=null){
                region = regionElement.getText();
            }

            OMElement amountElement = requestElement.getFirstChildWithName(new QName(ns, "tenantDomain"));

            if(amountElement !=null){
                amount = amountElement.getText();
            }
        }
    %>

<table border="0">
    <tr>
        <td>Application Name</td>
        <td><%=customerId%>
        </td>
    </tr>
    <tr>
        <td>Application Id</td>
        <td><%=customerFirstName%>
        </td>
    </tr>
    <tr>
        <td>Application Tier</td>
        <td><%=customerLastName%>
        </td>
    </tr>
    <tr>
        <td>Tier Name</td>
        <td><%=amount%>
        </td>
    </tr>
    <tr>
        <td>Application Name</td>
        <td><%=region%>
        </td>
    </tr>

</table>

</p>

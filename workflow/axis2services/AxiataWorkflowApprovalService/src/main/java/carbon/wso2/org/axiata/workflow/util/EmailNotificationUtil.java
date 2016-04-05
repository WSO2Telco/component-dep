package carbon.wso2.org.axiata.workflow.util;

public class EmailNotificationUtil {

	public static String getAppPluginApproverEmailContent(String applicationName, String applicationTier, String applicationDescription, String userName) {
		String content = "";
		
		content = "<div style='margin: 0 0 3px 0;padding: 5px;border-style: solid;border-width: 1px 1px 1px 11px;border-color:#e46c0a;color: #525863;line-height: normal;font-weight: bold;'>" +
						"<h2> Application Approval Request</h2>" +
					"</div>" +
					"<div style='border-style:solid;border-width:1px 1px 1px 1px;border-color:#e46c0a;color:#525863;'>" +
						"<table border='0'>" +
							"<tr>" +	
								"<td><b> Application User : </b></td>" +
								"<td>" + userName + "</td>" +
							"</tr>" +
							"<tr>" +
								"<td><b> Application Name : </b></td>" +
								"<td>" + applicationName + "</td>" +
							"</tr>" +
							"<tr>" +
								"<td><b> Application Tier : </b></td>" +
								"<td>" + applicationTier + "</td>" +
							"</tr>" +
							"<tr>" +
								"<td><b> Application Description : </b></td>" +
								"<td>" + applicationDescription + "</td>" +
							"</tr>" +
						"</table>" +
					"</div>";
		
		return content;
	}
	
	public static String getAppApprovalStatusSPEmailNotificationContent(
			String applicationName, String applicationTier, String applicationDescription, String userName, String approvalStatus) {
		
		String content = "";
		
		content = "<div style='margin: 0 0 3px 0;padding: 5px;border-style: solid;border-width: 1px 1px 1px 11px;border-color:#e46c0a;color: #525863;line-height: normal;font-weight: bold;'>" +
						"<h2> Application Approval Status</h2>" +
					"</div>" +
					"<div style='border-style:solid;border-width:1px 1px 1px 1px;border-color:#e46c0a;color:#525863;'>" + 
						"<table border='0'>" +
							"<tr>" +	
								"<td><b> Application User : </b></td>" +
								"<td>" + userName + "</td>" +
							"</tr>" +
							"<tr>" +
								"<td><b> Application Name : </b></td>" +
								"<td>" + applicationName + "</td>" +
							"</tr>" +
							"<tr>" +
								"<td><b> Application Tier : </b></td>" +
								"<td>" + applicationTier + "</td>" +
							"</tr>" +
							"<tr>" +
								"<td><b> Application Description : </b></td>" +
								"<td>" + applicationDescription + "</td>" +
							"</tr>" +
							"<tr>" +
								"<td><b> Approval Status : </b></td>" +
								"<td>" + approvalStatus + "</td>" +
							"</tr>" +
						"</table>" +
					"</div>";
		
		return content;
	}
	
	public static String getSubPluginApproverEmailContent(
			String apiName, String apiVersion, String apiContext, String apiProvider, 
			String subscriber, String tierName, String applicationName, String applicationDescription) {
		
		String content = "";
		
		content = "<div style='margin: 0 0 3px 0;padding: 5px;border-style: solid;border-width: 1px 1px 1px 11px;border-color:#e46c0a;color: #525863;line-height: normal;font-weight: bold;'>" +
						"<h2> Subscription Approval Request</h2>" +
					"</div>" +
					"<div style='border-style:solid;border-width:1px 1px 1px 1px;border-color:#e46c0a;color:#525863;'>" +  
						"<table border='0'>" +
							"<tr>" +	
								"<td><b> API Name : </b></td>" +
								"<td>" + apiName + "</td>" +
							"</tr>" +
							"<tr>" +
								"<td><b> API Version : </b></td>" +
								"<td>" + apiVersion + "</td>" +
							"</tr>" +
							"<tr>" +
								"<td><b> API Context : </b></td>" +
								"<td>" + apiContext + "</td>" +
							"</tr>" +
							"<tr>" +
								"<td><b> API Provider : </b></td>" +
								"<td>" + apiProvider + "</td>" +
							"</tr>" +
							"<tr>" +
								"<td><b> Subscriber : </b></td>" +
								"<td>" + subscriber + "</td>" +
							"</tr>" +
							"<tr>" +
								"<td><b> Subscription Tier : </b></td>" +
								"<td>" + tierName + "</td>" +
							"</tr>" +
							"<tr>" +
								"<td><b> Application Name : </b></td>" +
								"<td>" + applicationName + "</td>" +
							"</tr>" +
							"<tr>" +
								"<td><b> Application Description : </b></td>" +
								"<td>" + applicationDescription + "</td>" +
							"</tr>" +
						"</table>" +
					"</div>";
		
		return content;
	}
	
	public static String getSubApprovalStatusSPEmailNotificationContent(
			String apiName, String apiVersion, String apiContext, String apiProvider, 
			String subscriber, String tierName, String applicationName, String applicationDescription, String approvalStatus) {
		
		String content = "";
		
		content = "<div style='margin: 0 0 3px 0;padding: 5px;border-style: solid;border-width: 1px 1px 1px 11px;border-color:#e46c0a;color: #525863;line-height: normal;font-weight: bold;'>" +
						"<h2> Subscription Approval Status</h2>" +
					"</div>" +
					"<div style='border-style:solid;border-width:1px 1px 1px 1px;border-color:#e46c0a;color:#525863;'>" +  
						"<table border='0'>" +
							"<tr>" +	
								"<td><b> API Name : </b></td>" +
								"<td>" + apiName + "</td>" +
							"</tr>" +
							"<tr>" +
								"<td><b> API Version : </b></td>" +
								"<td>" + apiVersion + "</td>" +
							"</tr>" +
							"<tr>" +
								"<td><b> API Context : </b></td>" +
								"<td>" + apiContext + "</td>" +
							"</tr>" +
							"<tr>" +
								"<td><b> API Provider : </b></td>" +
								"<td>" + apiProvider + "</td>" +
							"</tr>" +
							"<tr>" +
								"<td><b> Subscriber : </b></td>" +
								"<td>" + subscriber + "</td>" +
							"</tr>" +
							"<tr>" +
								"<td><b> Subscription Tier : </b></td>" +
								"<td>" + tierName + "</td>" +
							"</tr>" +
							"<tr>" +
								"<td><b> Application Name : </b></td>" +
								"<td>" + applicationName + "</td>" +
							"</tr>" +
							"<tr>" +
								"<td><b> Application Description : </b></td>" +
								"<td>" + applicationDescription + "</td>" +
							"</tr>" +
							"<tr>" +
								"<td><b> Approval Status : </b></td>" +
								"<td>" + approvalStatus + "</td>" +
							"</tr>" +
						"</table>" +
					"</div>";
		
		return content;
	}
}

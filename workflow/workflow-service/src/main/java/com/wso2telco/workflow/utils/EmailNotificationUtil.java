/**
 * Copyright (c) 2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wso2telco.workflow.utils;

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

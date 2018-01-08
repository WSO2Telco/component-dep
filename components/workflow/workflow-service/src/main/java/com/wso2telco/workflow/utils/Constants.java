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

public class Constants {

	public static final String CLAIM_EMAIL = "email";
	
	// Email subjects.
	public static final String SUBJECT_HUB_ADMIN_APP_APPROVAL_NOTIFICATION = "HUB Level Application Approval Request";
	public static final String SUBJECT_HUB_ADMIN_SUB_APPROVAL_NOTIFICATION = "HUB Level Subscription Approval Request";
	public static final String SUBJECT_PLUGIN_ADMIN_APP_APPROVAL_NOTIFICATION = "PLUGIN Level Application Approval Request";
	public static final String SUBJECT_PLUGIN_ADMIN_SUB_APPROVAL_NOTIFICATION = "PLUGIN Level Subscription Approval Request";
	public static final String SUBJECT_APP_APPROVAL_STATUS_SP_NOTIFICATION = "Application Approval Status Notification";
	public static final String SUBJECT_SUB_APPROVAL_STATUS_SP_NOTIFICATION = "Subscription Approval Status Notification";
    public static final String SUBJECT_PUBLISHER_SUB_APPROVAL_NOTIFICATION= "Publisher Subscription Approval Request";
    public static final String SUBJECT_OPE_ADMIN_SUB_APPROVAL_NOTIFICATION = "Subscription Approval Request";
	
	// workflow.properties file entries: Email sending configs.
	public static final String WORKFLOW_PROPERTIES_FILE = "workflow.properties";
	public static final String WORKFLOW_PROPERTIES_XML_FILE = "workflow-configuration.xml";
	public static final String KEY_WORKFLOW_EMAIL_NOTIFICATION_HOST = "workflow.email.notification.host";
	public static final String KEY_WORKFLOW_EMAIL_NOTIFICATION_FROM_ADDRESS = "workflow.email.notification.from.address";
	public static final String KEY_WORKFLOW_EMAIL_NOTIFICATION_FROM_PASSWORD = "workflow.email.notification.from.password";
	public static final String SERVICE_HOST = "service.host";
	public static final String PUBLISHER_ROLE_START_WITH ="workflow.Publisher.role.start.with";
	public static final String PUBLISHER_ROLE_END_WITH ="workflow.Publisher.role.end.with";
	public static final String MANDATE_SERVICE_HOST = "mandate.service.host";
	
	// workflow.properties file entries: Remote userstore manager service configs.
	public static final String REMOTE_USERSTORE_MANAGER_HOST = "userstoremanager.service.host";
	public static final String REMOTE_USERSTORE_MANAGER_USERNAME = "userstoremanager.service.username";

	
}

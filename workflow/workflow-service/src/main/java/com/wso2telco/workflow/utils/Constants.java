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
    public static final String SUBJECT_OPE_ADMIN_SUB_APPROVAL_NOTIFICATION = "Operator Subscription Approval Request";
	
	// workflow.properties file entries: Email sending configs.
	public static final String WORKFLOW_PROPERTIES_FILE = "workflow.properties";
	public static final String KEY_WORKFLOW_EMAIL_NOTIFICATION_HOST = "workflow.email.notification.host";
	public static final String KEY_WORKFLOW_EMAIL_NOTIFICATION_FROM_ADDRESS = "workflow.email.notification.from.address";
	public static final String KEY_WORKFLOW_EMAIL_NOTIFICATION_FROM_PASSWORD = "workflow.email.notification.from.password";
	
	// workflow.properties file entries: Remote userstore manager service configs.
	public static final String REMOTE_USERSTORE_MANAGER_HOST = "userstoremanager.service.host";
	public static final String REMOTE_USERSTORE_MANAGER_USERNAME = "userstoremanager.service.username";

	
}

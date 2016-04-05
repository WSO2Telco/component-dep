package carbon.wso2.org.axiata.workflow.util;

public class AxiataConstants {

	public static final String CLAIM_EMAIL = "email";
	
	// Email subjects.
	public static final String SUBJECT_HUB_ADMIN_APP_APPROVAL_NOTIFICATION = "HUB Level Application Approval Request";
	public static final String SUBJECT_HUB_ADMIN_SUB_APPROVAL_NOTIFICATION = "HUB Level Subscription Approval Request";
	public static final String SUBJECT_PLUGIN_ADMIN_APP_APPROVAL_NOTIFICATION = "PLUGIN Level Application Approval Request";
	public static final String SUBJECT_PLUGIN_ADMIN_SUB_APPROVAL_NOTIFICATION = "PLUGIN Level Subscription Approval Request";
	public static final String SUBJECT_APP_APPROVAL_STATUS_SP_NOTIFICATION = "Application Approval Status Notification";
	public static final String SUBJECT_SUB_APPROVAL_STATUS_SP_NOTIFICATION = "Subscription Approval Status Notification";
	
	// axiata-workflow.properties file entries: Email sending configs.
	public static final String AXIATA_WORKFLOW_PROPERTIES_FILE = "axiata-workflow.properties";
	public static final String KEY_AXIATA_WORKFLOW_EMAIL_NOTIFICATION_HOST = "axiata.workflow.email.notification.host";
	public static final String KEY_AXIATA_WORKFLOW_EMAIL_NOTIFICATION_FROM_ADDRESS = "axiata.workflow.email.notification.from.address";
	public static final String KEY_AXIATA_WORKFLOW_EMAIL_NOTIFICATION_FROM_PASSWORD = "axiata.workflow.email.notification.from.password";
	
	// axiata-workflow.properties file entries: Remote userstore manager service configs.
	public static final String REMOTE_USERSTORE_MANAGER_HOST = "axiata.userstoremanager.service.host";
	public static final String REMOTE_USERSTORE_MANAGER_USERNAME = "axiata.userstoremanager.service.username";
	public static final String REMOTE_USERSTORE_MANAGER_PASSWORD = "axiata.userstoremanager.service.password";
	
}

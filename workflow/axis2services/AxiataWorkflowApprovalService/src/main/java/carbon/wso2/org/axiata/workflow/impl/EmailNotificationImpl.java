package carbon.wso2.org.axiata.workflow.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import carbon.wso2.org.axiata.workflow.in.EmailNotification;
import carbon.wso2.org.axiata.workflow.in.RemoteUserStoreManager;
import carbon.wso2.org.axiata.workflow.notify.AppApprovalStatusSPEmailNotificationRequestType;
import carbon.wso2.org.axiata.workflow.notify.HUBAdminAppApprovalEmailNotificationRequestType;
import carbon.wso2.org.axiata.workflow.notify.HUBAdminSubApprovalEmailNotificationRequestType;
import carbon.wso2.org.axiata.workflow.notify.PLUGINAdminAppApprovalEmailNotificationRequestType;
import carbon.wso2.org.axiata.workflow.notify.PLUGINAdminSubApprovalEmailNotificationRequestType;
import carbon.wso2.org.axiata.workflow.notify.SubApprovalStatusSPEmailNotificationRequestType;
import carbon.wso2.org.axiata.workflow.util.AxiataConstants;
import carbon.wso2.org.axiata.workflow.util.CommonUtil;
import carbon.wso2.org.axiata.workflow.util.EmailNotificationUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EmailNotificationImpl implements EmailNotification {

	private static Log log = LogFactory.getLog(EmailNotificationImpl.class);

	@Override
	public void sendHUBAdminAppApprovalEmailNotification(HUBAdminAppApprovalEmailNotificationRequestType request) {
		
		String role = request.getReceiverRole();
		String applicationName = request.getApplicationName();
		String applicationTier = request.getApplicationTier();
		String applicationDescription = request.getApplicationDescription();
		String userName = request.getUserName();
		
		log.debug("role : " + role);
		log.debug("applicationName : " + applicationName);
		log.debug("applicationTier : " + applicationTier);
		log.debug("applicationDescription : " + applicationDescription);
		log.debug("userName : " + userName);
		
		if(role != null && role.length() > 0) {
			
			RemoteUserStoreManager userStoreManager = new RemoteUserStoreManagerImpl();
			String[] userListOfRole = userStoreManager.getUserListOfRole(role);
			
			if (userListOfRole != null && userListOfRole.length > 0) {
				
				String subject = AxiataConstants.SUBJECT_HUB_ADMIN_APP_APPROVAL_NOTIFICATION;
				String content = EmailNotificationUtil.getAppPluginApproverEmailContent(applicationName, applicationTier, applicationDescription, userName);
				
				if(content != null && content.length() > 0) {
					for (int i = 0; i < userListOfRole.length; i++) {
						String email = userStoreManager.getUserClaimValue(userListOfRole[i], AxiataConstants.CLAIM_EMAIL);
						sendEmail(email, subject, content);
					}
					
				} else {
					log.error("Email content is either null or empty. [ content : " + content + " ]");
				}
			} else {
				log.info("User list is either null or empty. [ userListOfRole : " + userListOfRole + " ]");
			}
		} else {
			log.error("The specified role is either null or empty. [ role : " + role + " ]");
		}
	}
	
	public void sendAppApprovalStatusSPEmailNotification(AppApprovalStatusSPEmailNotificationRequestType request) {
		
		String applicationName = request.getApplicationName();
		String applicationTier = request.getApplicationTier();
		String applicationDescription = request.getApplicationDescription();
		String userName = request.getUserName();
		String approvalStatus = request.getApprovalStatus();
		
		if(userName != null && userName.length() > 0) {
			RemoteUserStoreManager userStoreManager = new RemoteUserStoreManagerImpl();
			String email = userStoreManager.getUserClaimValue(userName, AxiataConstants.CLAIM_EMAIL);
			
			if(email != null && email.length() > 0) {
				
				String subject = AxiataConstants.SUBJECT_APP_APPROVAL_STATUS_SP_NOTIFICATION;
				String content = EmailNotificationUtil.getAppApprovalStatusSPEmailNotificationContent(
						applicationName, applicationTier, applicationDescription, userName, approvalStatus);
				
				if(content != null && content.length() > 0) {
					sendEmail(email, subject, content);
					
				} else {
					log.error("The content of the SP email is invalid. [ content : " + content + " ]");
				}
			} else {
				log.error("The email address of the SP is invalid. [ email : " + email + " ]");
			}
		} else {
			log.error("The specified SP username is invalid. [ userName : " + userName + " ]");
		}
	}

	@Override
	public void sendHUBAdminSubrovalEmailNotification(HUBAdminSubApprovalEmailNotificationRequestType request) {
		
		String role = request.getReceiverRole();
		String apiName = request.getApiName();
		String apiVersion = request.getApiVersion();
		String apiContext = request.getApiContext();
		String apiProvider = request.getApiProvider();
		String subscriber = request.getSubscriber();
		String tierName = request.getSubscriptionTier();
		String applicationName = request.getApplicationName();
		String applicationDescription = request.getApplicationDescription();
		
		log.debug("role : " + role);
		log.debug("apiName : " + apiName);
		log.debug("apiVersion : " + apiVersion);
		log.debug("apiContext : " + apiContext);
		log.debug("apiProvider : " + apiProvider);
		log.debug("subscriber : " + subscriber);
		log.debug("tierName : " + tierName);
		log.debug("applicationName : " + applicationName);
		log.debug("applicationDescription : " + applicationDescription);
		
		if(role != null && role.length() > 0) {
			
			RemoteUserStoreManager userStoreManager = new RemoteUserStoreManagerImpl();
			String[] userListOfRole = userStoreManager.getUserListOfRole(role);
			
			if (userListOfRole != null && userListOfRole.length > 0) {
				
				String subject = AxiataConstants.SUBJECT_HUB_ADMIN_SUB_APPROVAL_NOTIFICATION;
				String content = EmailNotificationUtil.getSubPluginApproverEmailContent(
						apiName, apiVersion, apiContext, apiProvider, subscriber, tierName, applicationName, applicationDescription);
				
				if(content != null && content.length() > 0) {
					for (int i = 0; i < userListOfRole.length; i++) {
						String email = userStoreManager.getUserClaimValue(userListOfRole[i], AxiataConstants.CLAIM_EMAIL);
						sendEmail(email, subject, content);
					}
					
				} else {
					log.error("Email content is either null or empty. [ content : " + content + " ]");
				}
			} else {
				log.info("User list is either null or empty. [ userListOfRole : " + userListOfRole + " ]");
			}
		} else {
			log.error("The specified role is either null or empty. [ role : " + role + " ]");
		}
		
	}

	@Override
	public void sendSubApprovalStatusSPEmailNotification(SubApprovalStatusSPEmailNotificationRequestType request) {
		
		String apiName = request.getApiName();
		String apiVersion = request.getApiVersion();
		String apiContext = request.getApiContext();
		String apiProvider = request.getApiProvider();
		String subscriber = request.getSubscriber();
		String tierName = request.getSubscriptionTier();
		String applicationName = request.getApplicationName();
		String applicationDescription = request.getApplicationDescription();
		String approvalStatus = request.getApprovalStatus();
		
		if(subscriber != null && subscriber.length() > 0) {
			RemoteUserStoreManager userStoreManager = new RemoteUserStoreManagerImpl();
			String email = userStoreManager.getUserClaimValue(subscriber, AxiataConstants.CLAIM_EMAIL);
			
			if(email != null && email.length() > 0) {
				
				String subject = AxiataConstants.SUBJECT_SUB_APPROVAL_STATUS_SP_NOTIFICATION;
				String content = EmailNotificationUtil.getSubApprovalStatusSPEmailNotificationContent(
						apiName, apiVersion, apiContext, apiProvider, subscriber, tierName, applicationName, applicationDescription, approvalStatus);
				
				if(content != null && content.length() > 0) {
					sendEmail(email, subject, content);
					
				} else {
					log.error("The content of the SP email is invalid. [ content : " + content + " ]");
				}
			} else {
				log.error("The email address of the SP is invalid. [ email : " + email + " ]");
			}
		} else {
			log.error("The specified SP username is invalid. [ userName : " + subscriber + " ]");
		}
		
	}
	
	@Override
	public void sendPLUGINAdminAppApprovalEmailNotification(PLUGINAdminAppApprovalEmailNotificationRequestType request) {
		
		String role = request.getReceiverRole();
		String applicationName = request.getApplicationName();
		String applicationTier = request.getApplicationTier();
		String applicationDescription = request.getApplicationDescription();
		String userName = request.getUserName();
		
		log.debug("role : " + role);
		log.debug("applicationName : " + applicationName);
		log.debug("applicationTier : " + applicationTier);
		log.debug("applicationDescription : " + applicationDescription);
		log.debug("userName : " + userName);
		
		if(role != null && role.length() > 0) {
			
			RemoteUserStoreManager userStoreManager = new RemoteUserStoreManagerImpl();
			String[] userListOfRole = userStoreManager.getUserListOfRole(role);
			
			if (userListOfRole != null && userListOfRole.length > 0) {
				
				String subject = AxiataConstants.SUBJECT_PLUGIN_ADMIN_APP_APPROVAL_NOTIFICATION;
				String content = EmailNotificationUtil.getAppPluginApproverEmailContent(applicationName, applicationTier, applicationDescription, userName);
				
				if(content != null && content.length() > 0) {
					for (int i = 0; i < userListOfRole.length; i++) {
						String email = userStoreManager.getUserClaimValue(userListOfRole[i], AxiataConstants.CLAIM_EMAIL);
						sendEmail(email, subject, content);
					}
					
				} else {
					log.error("Email content is either null or empty. [ content : " + content + " ]");
				}
			} else {
				log.info("User list is either null or empty. [ userListOfRole : " + userListOfRole + " ]");
			}
		} else {
			log.error("The specified role is either null or empty. [ role : " + role + " ]");
		}
	}

	@Override
	public void sendPLUGINAdminSubApprovalEmailNotification(PLUGINAdminSubApprovalEmailNotificationRequestType request) {
		
		String role = request.getReceiverRole();
		String apiName = request.getApiName();
		String apiVersion = request.getApiVersion();
		String apiContext = request.getApiContext();
		String apiProvider = request.getApiProvider();
		String subscriber = request.getSubscriber();
		String tierName = request.getSubscriptionTier();
		String applicationName = request.getApplicationName();
		String applicationDescription = request.getApplicationDescription();
		
		log.debug("role : " + role);
		log.debug("apiName : " + apiName);
		log.debug("apiVersion : " + apiVersion);
		log.debug("apiContext : " + apiContext);
		log.debug("apiProvider : " + apiProvider);
		log.debug("subscriber : " + subscriber);
		log.debug("tierName : " + tierName);
		log.debug("applicationName : " + applicationName);
		log.debug("applicationDescription : " + applicationDescription);
		
		if(role != null && role.length() > 0) {
			
			RemoteUserStoreManager userStoreManager = new RemoteUserStoreManagerImpl();
			String[] userListOfRole = userStoreManager.getUserListOfRole(role);
			
			if (userListOfRole != null && userListOfRole.length > 0) {
				
				String subject = AxiataConstants.SUBJECT_PLUGIN_ADMIN_SUB_APPROVAL_NOTIFICATION;
				String content = EmailNotificationUtil.getSubPluginApproverEmailContent(
						apiName, apiVersion, apiContext, apiProvider, subscriber, tierName, applicationName, applicationDescription);
				
				if(content != null && content.length() > 0) {
					for (int i = 0; i < userListOfRole.length; i++) {
						String email = userStoreManager.getUserClaimValue(userListOfRole[i], AxiataConstants.CLAIM_EMAIL);
						sendEmail(email, subject, content);
					}
					
				} else {
					log.error("Email content is either null or empty. [ content : " + content + " ]");
				}
			} else {
				log.info("User list is either null or empty. [ userListOfRole : " + userListOfRole + " ]");
			}
		} else {
			log.error("The specified role is either null or empty. [ role : " + role + " ]");
		}
		
	}
	
	@Override
	public void sendEmail(String emailAddress, String subject, String content) {

		Properties workflowProperties = CommonUtil.loadAxiataWorkflowProperties();
		String emailHost = workflowProperties.getProperty(AxiataConstants.KEY_AXIATA_WORKFLOW_EMAIL_NOTIFICATION_HOST);
		String fromEmailAddress = workflowProperties.getProperty(AxiataConstants.KEY_AXIATA_WORKFLOW_EMAIL_NOTIFICATION_FROM_ADDRESS);
		String fromEmailPassword = workflowProperties.getProperty(AxiataConstants.KEY_AXIATA_WORKFLOW_EMAIL_NOTIFICATION_FROM_PASSWORD);

		Properties props = System.getProperties();
		props.put("mail.smtp.host", emailHost);
		props.put("mail.smtp.user", fromEmailAddress);
		props.put("mail.smtp.password", "");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");

		try {
			Session session = Session.getDefaultInstance(props, null);
			InternetAddress to_address = new InternetAddress(emailAddress);

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromEmailAddress));
			message.addRecipient(Message.RecipientType.TO, to_address);
			message.setSubject(subject);
			message.setContent(content, "text/html; charset=UTF-8");

			Transport transport = session.getTransport("smtp");
			transport.connect(emailHost, fromEmailAddress, fromEmailPassword);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();

		} catch (Exception e) {
			log.error("Email sending failed. ", e);
		}

	}

}

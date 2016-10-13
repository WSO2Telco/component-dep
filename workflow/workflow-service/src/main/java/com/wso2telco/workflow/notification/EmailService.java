package com.wso2telco.workflow.notification;

import com.wso2telco.workflow.utils.WorkflowProperties;
import com.wso2telco.workflow.utils.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class EmailService {

    private static Log log = LogFactory.getLog(EmailService.class);

    public void sendEmail(final String emailAddress,final String subject,final String content) {

        new Thread() {
            public void run() {
                Properties workflowProperties = WorkflowProperties.loadWorkflowProperties();
                String emailHost = workflowProperties.getProperty(Constants.KEY_WORKFLOW_EMAIL_NOTIFICATION_HOST);
                String fromEmailAddress = workflowProperties.getProperty(Constants.KEY_WORKFLOW_EMAIL_NOTIFICATION_FROM_ADDRESS);
                String fromEmailPassword = workflowProperties.getProperty(Constants.KEY_WORKFLOW_EMAIL_NOTIFICATION_FROM_PASSWORD);

                Properties props = System.getProperties();
                props.put("mail.smtp.host", emailHost);
                props.put("mail.smtp.user", fromEmailAddress);
                props.put("mail.smtp.password", fromEmailPassword);
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
        }.start();
    }
}

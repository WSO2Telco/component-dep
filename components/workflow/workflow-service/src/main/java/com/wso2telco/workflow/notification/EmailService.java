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

import java.util.Map;
import java.util.Properties;


public class EmailService {

    private static Log log = LogFactory.getLog(EmailService.class);

    public void sendEmail(final String emailAddress,final String subject,final String content) {

        new Thread() {
            @Override
            public void run() {
                Map<String, String> workflowProperties = WorkflowProperties.loadWorkflowPropertiesFromXML();
                String emailHost = workflowProperties.get(Constants.KEY_WORKFLOW_EMAIL_NOTIFICATION_HOST);
                String fromEmailAddress = workflowProperties.get(Constants.KEY_WORKFLOW_EMAIL_NOTIFICATION_FROM_ADDRESS);
                String fromEmailPassword = workflowProperties.get(Constants.KEY_WORKFLOW_EMAIL_NOTIFICATION_FROM_PASSWORD);

                Properties props = System.getProperties();
                props.put("mail.smtp.host", emailHost);
                props.put("mail.smtp.user", fromEmailAddress);
                props.put("mail.smtp.password", fromEmailPassword);
                props.put("mail.smtp.port", "587");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.auth", "true");

                try {
                    Session session = Session.getDefaultInstance(props, null);
                    InternetAddress toAddress = new InternetAddress(emailAddress);

                    MimeMessage message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(fromEmailAddress));
                    message.addRecipient(Message.RecipientType.TO, toAddress);
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

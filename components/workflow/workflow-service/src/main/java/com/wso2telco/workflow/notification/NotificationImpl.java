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

import com.wso2telco.workflow.model.*;
import com.wso2telco.workflow.userstore.RemoteUserStoreManager;
import com.wso2telco.workflow.userstore.RemoteUserStoreManagerImpl;
import com.wso2telco.workflow.utils.Constants;
import com.wso2telco.workflow.utils.EmailNotificationUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collection;
import java.util.Iterator;

public class NotificationImpl implements Notification {

    private static Log log = LogFactory.getLog(NotificationImpl.class);
    private EmailService emailService = new EmailService();

    @Override
    public void sendHUBAdminAppApprovalNotification(NotificationRequest request) {

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

        if (role != null && role.length() > 0) {

            RemoteUserStoreManager userStoreManager = new RemoteUserStoreManagerImpl();
            String[] userListOfRole = userStoreManager.getUserListOfRole(role);

            if (userListOfRole != null && userListOfRole.length > 0) {

                String subject = Constants.SUBJECT_HUB_ADMIN_APP_APPROVAL_NOTIFICATION;
                String content = EmailNotificationUtil.getAppPluginApproverEmailContent(applicationName, applicationTier, applicationDescription, userName);

                if (content != null && content.length() > 0) {
                    for (int i = 0; i < userListOfRole.length; i++) {
                        String email = userStoreManager.getUserClaimValue(userListOfRole[i], Constants.CLAIM_EMAIL);
                        emailService.sendEmail(email, subject, content);
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

    public void sendAppApprovalStatusSPNotification(NotificationRequest request) {

        String applicationName = request.getApplicationName();
        String applicationTier = request.getApplicationTier();
        String applicationDescription = request.getApplicationDescription();
        String userName = request.getUserName();
        String approvalStatus = request.getApprovalStatus();

        if (userName != null && userName.length() > 0) {
            RemoteUserStoreManager userStoreManager = new RemoteUserStoreManagerImpl();
            String email = userStoreManager.getUserClaimValue(userName, Constants.CLAIM_EMAIL);

            if (email != null && email.length() > 0) {

                String subject = Constants.SUBJECT_APP_APPROVAL_STATUS_SP_NOTIFICATION;
                String content = EmailNotificationUtil.getAppApprovalStatusSPEmailNotificationContent(
                        applicationName, applicationTier, applicationDescription, userName, approvalStatus);

                if (content != null && content.length() > 0) {
                    emailService.sendEmail(email, subject, content);

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
    public void sendHUBAdminSubrovalNotification(NotificationRequest request) {

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

        if (role != null && role.length() > 0) {

            RemoteUserStoreManager userStoreManager = new RemoteUserStoreManagerImpl();
            String[] userListOfRole = userStoreManager.getUserListOfRole(role);

            if (userListOfRole != null && userListOfRole.length > 0) {

                String subject = Constants.SUBJECT_HUB_ADMIN_SUB_APPROVAL_NOTIFICATION;
                String content = EmailNotificationUtil.getSubPluginApproverEmailContent(
                        apiName, apiVersion, apiContext, apiProvider, subscriber, tierName, applicationName, applicationDescription);

                if (content != null && content.length() > 0) {
                    for (int i = 0; i < userListOfRole.length; i++) {
                        String email = userStoreManager.getUserClaimValue(userListOfRole[i], Constants.CLAIM_EMAIL);
                        emailService.sendEmail(email, subject, content);
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
    public void sendSubApprovalStatusSPNotification(NotificationRequest request) {

        String apiName = request.getApiName();
        String apiVersion = request.getApiVersion();
        String apiContext = request.getApiContext();
        String apiProvider = request.getApiProvider();
        String subscriber = request.getSubscriber();
        String tierName = request.getSubscriptionTier();
        String applicationName = request.getApplicationName();
        String applicationDescription = request.getApplicationDescription();
        String approvalStatus = request.getApprovalStatus();

        if (subscriber != null && subscriber.length() > 0) {
            RemoteUserStoreManager userStoreManager = new RemoteUserStoreManagerImpl();
            String email = userStoreManager.getUserClaimValue(subscriber, Constants.CLAIM_EMAIL);

            if (email != null && email.length() > 0) {

                String subject = Constants.SUBJECT_SUB_APPROVAL_STATUS_SP_NOTIFICATION;
                String content = EmailNotificationUtil.getSubApprovalStatusSPEmailNotificationContent(
                        apiName, apiVersion, apiContext, apiProvider, subscriber, tierName, applicationName, applicationDescription, approvalStatus);

                if (content != null && content.length() > 0) {
                    emailService.sendEmail(email, subject, content);

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
    public void sendPLUGINAdminAppApprovalNotification(NotificationRequest request) {

        String role = null;
        Collection<String> operatorsRoles = request.getOperatorsRoles();
        String applicationName = request.getApplicationName();
        String applicationTier = request.getApplicationTier();
        String applicationDescription = request.getApplicationDescription();
        String userName = request.getUserName();

        log.debug("applicationName : " + applicationName);
        log.debug("applicationTier : " + applicationTier);
        log.debug("applicationDescription : " + applicationDescription);
        log.debug("userName : " + userName);

            for (Iterator<String> iterator = operatorsRoles.iterator(); iterator.hasNext(); ) {
                role = iterator.next();
                log.info("role : " + role);
                RemoteUserStoreManager userStoreManager = new RemoteUserStoreManagerImpl();
                String[] userListOfRole = userStoreManager.getUserListOfRole(role);

                if (userListOfRole != null && userListOfRole.length > 0) {

                    String subject = Constants.SUBJECT_PLUGIN_ADMIN_APP_APPROVAL_NOTIFICATION;
                    String content = EmailNotificationUtil.getAppPluginApproverEmailContent(applicationName, applicationTier, applicationDescription, userName);

                    if (content != null && content.length() > 0) {
                        for (int i = 0; i < userListOfRole.length; i++) {
                            String email = userStoreManager.getUserClaimValue(userListOfRole[i], Constants.CLAIM_EMAIL);
                            emailService.sendEmail(email, subject, content);
                        }

                    } else {
                        log.error("Email content is either null or empty. [ content : " + content + " ]");
                    }
                } else {
                    log.info("User list is either null or empty. [ userListOfRole : " + userListOfRole + " ]");
                }
            }

    }

    @Override
    public void sendPLUGINAdminSubApprovalNotification(NotificationRequest request) {

        String role = request.getReceiverRole();
        String apiName = request.getApiName();
        String apiVersion = request.getApiVersion();
        String apiContext = request.getApiContext();
        String apiProvider = request.getApiProvider();
        String subscriber = request.getSubscriber();
        String tierName = request.getSubscriptionTier();
        String applicationName = request.getApplicationName();
        String applicationDescription = request.getApplicationDescription();
        Collection<String> operatorsRoles = request.getOperatorsRoles();

        log.debug("role : " + role);
        log.debug("apiName : " + apiName);
        log.debug("apiVersion : " + apiVersion);
        log.debug("apiContext : " + apiContext);
        log.debug("apiProvider : " + apiProvider);
        log.debug("subscriber : " + subscriber);
        log.debug("tierName : " + tierName);
        log.debug("applicationName : " + applicationName);
        log.debug("applicationDescription : " + applicationDescription);

        for (Iterator<String> iterator = operatorsRoles.iterator(); iterator.hasNext(); ) {
            role = iterator.next();
            RemoteUserStoreManager userStoreManager = new RemoteUserStoreManagerImpl();
            String[] userListOfRole = userStoreManager.getUserListOfRole(role);
            if (userListOfRole != null && userListOfRole.length > 0) {
                String subject = Constants.SUBJECT_PLUGIN_ADMIN_SUB_APPROVAL_NOTIFICATION;
                String content = EmailNotificationUtil.getSubPluginApproverEmailContent(
                        apiName, apiVersion, apiContext, apiProvider, subscriber, tierName, applicationName, applicationDescription);
                if (content != null && content.length() > 0) {
                    for (int i = 0; i < userListOfRole.length; i++) {
                        String email = userStoreManager.getUserClaimValue(userListOfRole[i], Constants.CLAIM_EMAIL);
                        emailService.sendEmail(email, subject, content);
                    }
                } else {
                    log.error("Email content is either null or empty. [ content : " + content + " ]");
                }
            } else {
                log.info("User list is either null or empty. [ userListOfRole : " + userListOfRole + " ]");
            }
        }
    }


    @Override
    public void sendInternalAdminSubrovalNotification(NotificationRequest request) {
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

        if (role != null && role.length() > 0) {

            RemoteUserStoreManager userStoreManager = new RemoteUserStoreManagerImpl();
            String[] userListOfRole = userStoreManager.getUserListOfRole(role);

            if (userListOfRole != null && userListOfRole.length > 0) {

                String subject = Constants.SUBJECT_OPE_ADMIN_SUB_APPROVAL_NOTIFICATION;
                String content = EmailNotificationUtil.getSubPluginApproverEmailContent(
                        apiName, apiVersion, apiContext, apiProvider, subscriber, tierName, applicationName, applicationDescription);

                if (content != null && content.length() > 0) {
                    for (int i = 0; i < userListOfRole.length; i++) {
                        String email = userStoreManager.getUserClaimValue(userListOfRole[i], Constants.CLAIM_EMAIL);
                        emailService.sendEmail(email, subject, content);
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
    public void sendPublisherSubApprovalNotification(NotificationRequest request) {

        String apiName = request.getApiName();
        String apiVersion = request.getApiVersion();
        String apiContext = request.getApiContext();
        String apiProvider = request.getApiProvider();
        String subscriber = request.getSubscriber();
        String tierName = request.getSubscriptionTier();
        String applicationName = request.getApplicationName();
        String applicationDescription = request.getApplicationDescription();
        String apiPublisher = request.getApiPublisher();

        log.debug("apiName : " + apiName);
        log.debug("apiVersion : " + apiVersion);
        log.debug("apiContext : " + apiContext);
        log.debug("apiProvider : " + apiProvider);
        log.debug("subscriber : " + subscriber);
        log.debug("tierName : " + tierName);
        log.debug("applicationName : " + applicationName);
        log.debug("applicationDescription : " + applicationDescription);

        RemoteUserStoreManager userStoreManager = new RemoteUserStoreManagerImpl();

        if (apiPublisher != null) {

            String subject = Constants.SUBJECT_PUBLISHER_SUB_APPROVAL_NOTIFICATION;
            String content = EmailNotificationUtil.getSubPluginApproverEmailContent(
                    apiName, apiVersion, apiContext, apiProvider, subscriber, tierName, applicationName, applicationDescription);
            String email = userStoreManager.getUserClaimValue(apiPublisher, Constants.CLAIM_EMAIL);
            emailService.sendEmail(email, subject, content);

        } else {
            log.info("apiPublisher is either null or empty. [ apiPublisher : " + apiPublisher + " ]");
        }

    }
}

/**
 * Copyright (c) 2020-2021, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
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

package com.wso2telco.dep.lifecycleextension.notification.impl;

import com.wso2telco.dep.lifecycleextension.notification.NotificationService;
import com.wso2telco.dep.lifecycleextension.userstore.RemoteUserStoreManager;
import com.wso2telco.dep.lifecycleextension.userstore.impl.RemoteUserStoreManagerImpl;
import com.wso2telco.dep.lifecycleextension.util.Constants;
import com.wso2telco.dep.lifecycleextension.util.EmailNotificationUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.api.model.API;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.utils.APIUtil;
import org.wso2.carbon.governance.api.generic.GenericArtifactManager;
import org.wso2.carbon.governance.api.generic.dataobjects.GenericArtifact;
import org.wso2.carbon.registry.core.jdbc.handlers.RequestContext;

import java.util.Arrays;
import java.util.List;


public class NotificationServiceImpl implements NotificationService{

    private static final Log log = LogFactory.getLog(NotificationServiceImpl.class);

    private final RemoteUserStoreManager remoteUserStoreManager;

    private final EmailService emailService;

    public NotificationServiceImpl(){
        remoteUserStoreManager = new RemoteUserStoreManagerImpl();
        emailService = new EmailService();
    }

    @Override
    public void sendApiProviderEmail(RequestContext context) {


        try {

            GenericArtifactManager artifactManager = APIUtil.getArtifactManager(context.getSystemRegistry(),
                    APIConstants.API_KEY);
            log.info("Starting email trigger functionality in API creation");
            String artifactId = context.getResource().getUUID();
            GenericArtifact apiArtifact = artifactManager.getGenericArtifact(artifactId);
            API api = APIUtil.getAPI(apiArtifact);

            List<String> userList = Arrays.asList(remoteUserStoreManager.getUserListOfRole(Constants.ADMIN_ROLE));

            String subject = Constants.SUBJECT_API_PROVIDER_EMAIL;

            if (!userList.isEmpty()) {
                log.info("Sending API creation email to all users in " + Constants.ADMIN_ROLE +" role");
                for (String user : userList) {

                    String content = EmailNotificationUtil.getApiProviderEmailContent(api.getId().getApiName(),
                            remoteUserStoreManager.getUserClaimValue(user,
                                    Constants.CLAIM_GIVEN_NAME));

                    String email = remoteUserStoreManager.getUserClaimValue(user,
                            Constants.CLAIM_EMAIL);

                    if (email != null) {
                        emailService.sendEmail(email, subject, content);
                    } else {
                        log.error("Failed to send email user " + user + " email is null");
                    }
                }
            } else {
                log.info("User list is either empty. [ userList : " + userList + " ]");
            }

        } catch (Exception e) {
            log.error("Failed to validate user details for send email ", e);
        }
    }
}

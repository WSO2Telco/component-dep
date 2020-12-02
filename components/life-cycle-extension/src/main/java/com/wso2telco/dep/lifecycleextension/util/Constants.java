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

package com.wso2telco.dep.lifecycleextension.util;

public class Constants {

    private Constants(){
    }

    public static final String STATE_CREATED="Created";
    public static final String STATE_PUBLISHED="Published";
    public static final String ADMIN_ROLE="admin";

    public static final String CLAIM_EMAIL = "email";
    public static final String CLAIM_GIVEN_NAME = "given_name";

    public static final String WORKFLOW_PROPERTIES_XML_FILE = "workflow-configuration.xml";
    public static final String KEY_WORKFLOW_EMAIL_NOTIFICATION_HOST = "workflow.email.notification.host";
    public static final String KEY_WORKFLOW_EMAIL_NOTIFICATION_FROM_ADDRESS = "workflow.email.notification.from.address";
    public static final String KEY_WORKFLOW_EMAIL_NOTIFICATION_FROM_PASSWORD = "workflow.email.notification.from.password";


    public static final String SUBJECT_API_PROVIDER_EMAIL = "New API creation - Notification ";

}

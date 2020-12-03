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

public class EmailNotificationUtil {

    private EmailNotificationUtil(){
    }

    public static String getApiProviderEmailContent(String apiName, String receiverName) {

        return "<p>Dear "+receiverName+",</p>" +
                "    <br>" +
                "    <p style='margin-left: pxs'>This is to notify you that we have published an API called "+apiName+".</p>" +
                "    <br>" +
                "    <p>Thanks</p>";

    }
}

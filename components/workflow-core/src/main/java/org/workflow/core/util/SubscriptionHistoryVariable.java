package org.workflow.core.util;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) 2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * <p>
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public enum SubscriptionHistoryVariable {

    ID("subscriptionId"),
    APPNAME("applicationName"),
    APPID("applicationId"),
    OPERATOR("operator"),
    APINAME("apiName"),
    APIID("apiId"),
    STATUS("status"),
    TIER("tier"),
    CREATED_BY("createdBy");


    private String varName;
    SubscriptionHistoryVariable(String paramName){
        varName = paramName;
    }
    public String key() {
        return this.varName;
    }

    private static final Map<String, SubscriptionHistoryVariable> keyMap = new HashMap<String, SubscriptionHistoryVariable>();

    static {

        for (SubscriptionHistoryVariable iterable_element : EnumSet.allOf( SubscriptionHistoryVariable.class)) {
            keyMap.put(iterable_element.key(), iterable_element);
        }
    }
    public static SubscriptionHistoryVariable getByKey(String key) {
        return keyMap.get(key);
    }
}

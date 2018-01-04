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
public enum HistoryVariable {

    NAME("applicationName"),
    OPARATOR("operator"),
    ID("applicationId"),
    SP("serviceProvider");


    private String varName;
    HistoryVariable(String paramName){
        varName = paramName;
    }
    public String key() {
        return this.varName;
    }

    private static final Map<String,HistoryVariable> keyMap = new HashMap<String,HistoryVariable>();

    static {

        for (HistoryVariable iterable_element : EnumSet.allOf( HistoryVariable.class)) {
            keyMap.put(iterable_element.key(), iterable_element);
        }
    }
    public static HistoryVariable getByKey(String key) {
        return keyMap.get(key);
    }
}

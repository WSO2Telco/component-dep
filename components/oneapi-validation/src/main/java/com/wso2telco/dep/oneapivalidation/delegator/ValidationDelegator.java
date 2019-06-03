/**
 * Copyright (c) 2019, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
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
package com.wso2telco.dep.oneapivalidation.delegator;

import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;

public class ValidationDelegator {

    private static final ValidationDelegator INSTANCE = new ValidationDelegator();

    private ValidationDelegator() {
    }

    public static ValidationDelegator getInstance(){
        return INSTANCE;
    }

    public boolean checkRequestParams(ValidationRule[] rules){
        return Validation.checkRequestParams(rules);
    }

}

/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licences this file to you under  the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.dep.oneapivalidation.service.impl.ussd;

import java.util.Arrays;

import com.wso2telco.dep.oneapivalidation.exceptions.CustomException;
import com.wso2telco.dep.oneapivalidation.service.IServiceValidate;
import com.wso2telco.dep.oneapivalidation.util.Validation;
import com.wso2telco.dep.oneapivalidation.util.ValidationRule;

// TODO: Auto-generated Javadoc
/**
 * The Class ValidateUssdCancelSubscription.
 */
public class ValidateUssdCancelSubscription implements IServiceValidate {

    /* (non-Javadoc)
     * @see com.wso2telco.oneapivalidation.service.IServiceValidate#validate(java.lang.String)
     */
    public void validate(String json) throws CustomException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * in ValidateUssdCancelSubscription flow url pattern is like, http://<host>:8280/ussd/v1/inbound/subscriptions/{id}
     * url contains 3 components.
     *
     * {@inheritDoc}
     */
    public void validateUrl(String pathInfo) throws CustomException {
        String[] requestParts = new String[3];
        if (pathInfo != null) {
            if (pathInfo.startsWith("/")) {
                pathInfo = pathInfo.substring(1);
            }
            requestParts = Arrays.copyOf(pathInfo.split("/"), 3);
        }

        ValidationRule validationRuleInbound = new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_URL,
            "inbound", requestParts[0]);
        ValidationRule validationRuleSubscriptions = new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_URL,
            "subscriptions", requestParts[1]);
        ValidationRule validationRuleSubscriptionId = new ValidationRule(ValidationRule.VALIDATION_TYPE_MANDATORY_URL,
            "subscriptionID", requestParts[2]);
        Validation.checkRequestParams(
            new ValidationRule[] { validationRuleInbound, validationRuleSubscriptions, validationRuleSubscriptionId });
    }

    /* (non-Javadoc)
     * @see com.wso2telco.oneapivalidation.service.IServiceValidate#validate(java.lang.String[])
     */
    public void validate(String[] params) throws CustomException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

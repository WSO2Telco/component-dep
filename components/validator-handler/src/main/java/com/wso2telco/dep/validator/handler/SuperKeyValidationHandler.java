/*
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

package com.wso2telco.dep.validator.handler;

import com.wso2telco.dep.validator.handler.exceptions.ValidatorException;
import com.wso2telco.dep.validator.handler.utils.PropertyUtil;
import com.wso2telco.dep.validator.handler.utils.ValidatorDBUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.dto.APIKeyValidationInfoDTO;
import org.wso2.carbon.apimgt.keymgt.APIKeyMgtException;
import org.wso2.carbon.apimgt.keymgt.handlers.DefaultKeyValidationHandler;
import org.wso2.carbon.apimgt.keymgt.service.TokenValidationContext;

public class SuperKeyValidationHandler extends DefaultKeyValidationHandler {

    private static final Log log = LogFactory.getLog(SuperKeyValidationHandler.class);

    @Override
    public boolean validateSubscription(TokenValidationContext validationContext) throws APIKeyMgtException {
        boolean state = super.validateSubscription(validationContext);
        APIKeyValidationInfoDTO infoDTO = validationContext.getValidationInfoDTO();
        if (!state && infoDTO.getValidationStatus() == APIConstants.KeyValidationStatus.API_AUTH_RESOURCE_FORBIDDEN &&
            infoDTO.getConsumerKey().equals(PropertyUtil.superTokenProperties().getProperty("super.token.consumer.key"))
        ) {
            try {
                state = ValidatorDBUtils.skipSubscriptionValidation(
                    validationContext.getContext(), validationContext.getVersion(), infoDTO.getConsumerKey(), infoDTO
                );
            } catch (ValidatorException e) {
                log.error("Error Occurred while validating subscription.", e);
            }
        }
        return state;
    }
}

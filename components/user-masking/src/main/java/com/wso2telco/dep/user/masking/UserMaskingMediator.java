/*******************************************************************************
 * Copyright  (c) 2019, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
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
package com.wso2telco.dep.user.masking;

import com.wso2telco.dep.user.masking.utils.APIType;
import com.wso2telco.dep.user.masking.utils.MaskingUtils;
import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

public class UserMaskingMediator extends AbstractMediator {

    @Override
    public boolean mediate(MessageContext messageContext) {
        messageContext.setProperty("ENABLE_WSO2AM_EXT_IN_PATH", "true");
        if (MaskingUtils.isUserAnonymizationEnabled() && APIType.PAYMENT.equals(MaskingUtils.getAPIType(messageContext))) {
            messageContext.setProperty("ENABLE_WSO2AM_EXT_IN_PATH", "false");
        }
        return true;
    }
}

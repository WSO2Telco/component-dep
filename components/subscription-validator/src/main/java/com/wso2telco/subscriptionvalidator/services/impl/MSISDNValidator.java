/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *  
 *  WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.subscriptionvalidator.services.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.user.api.UserRealm;
import org.wso2.carbon.user.api.UserStoreException;

import com.wso2telco.subscriptionvalidator.services.MifeValidator;


// TODO: Auto-generated Javadoc
/**
 * The Class MSISDNValidator.
 */
public class MSISDNValidator implements MifeValidator {
    
    /** The Constant log. */
    private static final Log log = LogFactory.getLog(MSISDNValidator.class);

    /**
     * Checks if is existing user.
     *
     * @param userName the user name
     * @return true, if is existing user
     */
    private boolean isExistingUser(String userName) {
        boolean isUser = false;
        try {

            CarbonContext carbonContext = CarbonContext.getThreadLocalCarbonContext();
            UserRealm userRealm = carbonContext.getUserRealm();
            isUser = userRealm.getUserStoreManager().isExistingUser(userName);

        } catch (UserStoreException e) {
            String errorMsg = "Error while getting MSISDN of the user, " + e.getMessage();
            log.error(errorMsg, e);
        }
        return isUser;

    }

	/* (non-Javadoc)
	 * @see com.wso2telco.subscriptionvalidator.services.MifeValidator#validate(org.apache.synapse.MessageContext)
	 */
	public boolean validate(MessageContext messageContext) {
		String userMSISDN = (String) messageContext.getProperty("UserMSISDN");
        return isExistingUser(userMSISDN);
	}
}

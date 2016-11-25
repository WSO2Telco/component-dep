/**
 * Copyright (c) 2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
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


package com.wso2telco.workflow.userstore;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.user.api.UserStoreManager;
import org.wso2.carbon.utils.multitenancy.MultitenantConstants;

public class RemoteUserStoreManagerImpl implements RemoteUserStoreManager {
	
	private static Log log = LogFactory.getLog(RemoteUserStoreManagerImpl.class);

	@Override
	public String[] getUserListOfRole(String role) {

        String[] userList = null;

		try {
            PrivilegedCarbonContext.startTenantFlow();
            PrivilegedCarbonContext privilegedCarbonContext = PrivilegedCarbonContext.getThreadLocalCarbonContext();
            privilegedCarbonContext.setTenantId(MultitenantConstants.SUPER_TENANT_ID);
            privilegedCarbonContext.setTenantDomain(MultitenantConstants.SUPER_TENANT_DOMAIN_NAME);
            log.info("Getting users for the role : " + role);
            UserStoreManager userstoremanager = privilegedCarbonContext.getUserRealm().getUserStoreManager();
            userList = userstoremanager.getUserListOfRole(role);

        } catch (Exception ex){
            log.error(ex);
            throw new RuntimeException(ex);

        } finally {
            PrivilegedCarbonContext.endTenantFlow();
        }

        return userList;
	}

	@Override
	public String getUserClaimValue(String userName, String claim) {

        String claimValue = "";
        log.info("Getting claim value for the userName : " + userName + " and claim : " + claim);

        try {
            PrivilegedCarbonContext.startTenantFlow();
            PrivilegedCarbonContext privilegedCarbonContext = PrivilegedCarbonContext.getThreadLocalCarbonContext();
            privilegedCarbonContext.setTenantId(MultitenantConstants.SUPER_TENANT_ID);
            privilegedCarbonContext.setTenantDomain(MultitenantConstants.SUPER_TENANT_DOMAIN_NAME);
            UserStoreManager userstoremanager = privilegedCarbonContext.getUserRealm().getUserStoreManager();
            claimValue = userstoremanager.getUserClaimValue(userName, claim, null);
            log.info("Claim value : " + claimValue);

        } catch (Exception e) {
            throw new RuntimeException(e);

        } finally {
            PrivilegedCarbonContext.endTenantFlow();
        }

		return claimValue;
	}
	


}

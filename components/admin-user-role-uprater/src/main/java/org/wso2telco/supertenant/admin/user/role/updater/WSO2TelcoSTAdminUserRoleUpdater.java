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

package org.wso2telco.supertenant.admin.user.role.updater;


import java.util.Map;

public class WSO2TelcoSTAdminUserRoleUpdater {

//    private static Log log = LogFactory.getLog(WSO2TelcoSTAdminUserRoleUpdater.class);
//
//    @Override
//    public int getExecutionOrderId() {
//
//        int orderId = getOrderId();
//        if (orderId != IdentityCoreConstants.EVENT_LISTENER_ORDER_ID) {
//            return orderId;
//        }
//        return 20;
//    }
//
//    public boolean doPostAuthenticate(String userName, boolean authenticated, UserStoreManager userStoreManager)
//            throws UserStoreException {
//
//        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%55 username: " + userName);
//        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%55 authenticated: " + authenticated);
//        return true;
//    }
//
//    public boolean doPostAddUser(String userName, Object credential, String[] roleList, Map<String, String> claims,
//                                 String profile, UserStoreManager userStoreManager) throws UserStoreException {
//
//        if (isEnable()) {
//            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%55 username: " + userName);
//            System.out.println("credential: " + credential.toString());
//            for (String role : roleList) {
//                System.out.println("Role: " + role);
//            }
//            for (Map.Entry<String, String> entry : claims.entrySet()) {
//                System.out.println("Claim: " + entry.getKey() + ", " + entry.getValue());
//            }
//            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%55 profile: " + profile);
//        }
//        return true;
//    }
//
//    public boolean isEnable() {
//        IdentityEventListenerConfig identityEventListenerConfig = IdentityUtil.readEventListenerProperty(UserOperationEventListener.class.getName(), this.getClass().getName());
//        return identityEventListenerConfig == null?true:(StringUtils.isNotBlank(identityEventListenerConfig.getEnable())?Boolean.parseBoolean(identityEventListenerConfig.getEnable()):true);
//    }

}

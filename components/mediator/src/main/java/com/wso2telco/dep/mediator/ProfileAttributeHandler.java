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
package com.wso2telco.dep.mediator;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.user.api.UserRealm;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.api.UserStoreManager;

import com.wso2telco.dep.mediator.internal.Util;

import org.wso2.carbon.user.api.Claim;



// TODO: Auto-generated Javadoc
/**
 * The Class ProfileAttributeHandler.
 */
public class ProfileAttributeHandler {
	
	 /** The Constant CURRENT_LOCATION_CLAIM. */
 	private static final String CURRENT_LOCATION_CLAIM = "http://wso2.org/claims/currentlocation";
	 
 	/** The Constant MSISDN_CLAIM. */
 	private static final String MSISDN_CLAIM = "http://wso2.org/claims/msisdn";
	 
	 /** The log. */
 	private static Log log = LogFactory.getLog(ProfileAttributeHandler.class);
	 
	  
	 /**
 	 * Gets the user current location.
 	 *
 	 * @param userName the user name
 	 * @return the user current location
 	 */
 	public String getUserCurrentLocation(String userName) {
	        UserRealm realm;
	        try {
	           realm = Util.getRealmService().getTenantUserRealm(-1234);
	           UserStoreManager um = realm.getUserStoreManager();
	           String currentLocation = um.getUserClaimValue(userName, CURRENT_LOCATION_CLAIM, null);
	        	
	            return currentLocation;
	        } catch (UserStoreException e) {
	            String errorMsg = "Error while getting Current Location of the user, " + e.getMessage();
	            log.error(errorMsg, e);
	        }
	        return null;

	    }
	 
	  
	 /**
 	 * Gets the user msisdn.
 	 *
 	 * @param userName the user name
 	 * @return the user msisdn
 	 */
 	public String getUserMSISDN(String userName) {
	        UserRealm realm;
	        try {
	           realm = Util.getRealmService().getTenantUserRealm(-1234);
	           UserStoreManager um = realm.getUserStoreManager();
	           String msisdn = um.getUserClaimValue(userName, MSISDN_CLAIM, null);
	        	
	            return msisdn;
	        } catch (UserStoreException e) {
	            String errorMsg = "Error while getting MSISDN of the user, " + e.getMessage();
	            log.error(errorMsg, e);
	        }
	        return null;

	    }
	 
	  
	 /**
 	 * Checks if is existing user.
 	 *
 	 * @param userName the user name
 	 * @return true, if is existing user
 	 */
 	public boolean isExistingUser(String userName) {
		 UserRealm realm;
		 boolean isUser = false; 
		 try {
	           realm = Util.getRealmService().getTenantUserRealm(-1234);
	           UserStoreManager um = realm.getUserStoreManager();
	           isUser = um.isExistingUser(userName);
	        	
	            return isUser;
	        } catch (UserStoreException e) {
	            String errorMsg = "Error while getting MSISDN of the user, " + e.getMessage();
	            log.error(errorMsg, e);
	        }
		 return false;
	 
	 }
	 
	  
	 /**
 	 * Gets the all propertity values.
 	 *
 	 * @param userName the user name
 	 * @return the all propertity values
 	 */
 	public Claim[] getAllPropertityValues(String userName) {
		 UserRealm realm;
		 Claim [] allAttributes = {};
		 
		 try {
	           realm = Util.getRealmService().getTenantUserRealm(-1234);
	           UserStoreManager um = realm.getUserStoreManager();
	           allAttributes = um.getUserClaimValues(userName, null);
	           return allAttributes;	
	           
	        } catch (UserStoreException e) {
	            String errorMsg = "Error while getting Attributes of the user, " + e.getMessage();
	            log.error(errorMsg, e);
	        }
		 return null;
	 
	 }
	 
	 
	 
}

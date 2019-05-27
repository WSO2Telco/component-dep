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
package com.wso2telco.dep.user.masking.utils;

import com.wso2telco.core.dbutils.fileutils.PropertyFileReader;
import com.wso2telco.dep.user.masking.configuration.UserMaskingConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;

import java.util.Map;
import java.util.Properties;

public class MaskingUtils {

	private static final Log log = LogFactory.getLog(MaskingUtils.class);
	public static final String USER_MASKING_PROPERTIES_FILE = "user-masking.properties";
	public static final String USER_MASKING_ENABLED = "user.masking.feature.enable";
	public static final String USER_MASKING_SECRET_KEY = "user.masking.feature.masking.secret.key";
	public static final String USER_MASKING_DEFAULT_MSISDN_REGEX = "default.msisdn.regex";
	public static final String USER_MASKING_USER_ID_FILTER_REGEX = "user.masking.feature.user.Id.filter.regex";
	private static Properties props = null;

	/**
	 *
	 * @param messageContext Message Conext
	 * @return APIType See {@link APIType} for return values
	 */
	public static APIType getAPIType(MessageContext messageContext) {
		APIType apiType = null;
		Object headers = ((Axis2MessageContext) messageContext).getAxis2MessageContext().getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
		Map headersMap = (Map) headers;
		String resource = (String)headersMap.get("RESOURCE");
		if (resource != null && resource.contains("/transactions/amount")) {
			apiType = APIType.PAYMENT;
		} else if (resource != null && resource.contains("/outbound/") && resource.contains("/requests")) {
			apiType = APIType.SMS;
		}
		return  apiType;
	}

	/**
	 * Get config URL
	 */
	public static String getUrlProperty(String propertyName) {
		String esbUrl = getUserMaskingConfiguration(propertyName);
		if(esbUrl != null && esbUrl.endsWith("/")) {
			esbUrl = esbUrl.substring(0, esbUrl.lastIndexOf("/"));
		}
		return esbUrl;
	}

	/**
	 * Get config UserAnonymization
	 */
	public static boolean isUserAnonymizationEnabled() {
		boolean isUserAnonymizationEnabled = false;
		if(UserMaskingConfiguration.getInstance().getUserMaskingEnabled()) {
			try {
				isUserAnonymizationEnabled = UserMaskingConfiguration.getInstance().getUserMaskingEnabled();
			} catch (Exception e) {
				String errorMessage = "Invalid for configuration in user-masking.properties :  user.masking.feature.enable";
				log.error(errorMessage, e);
			}
		}
		return isUserAnonymizationEnabled;
	}

	/**
	 * Get configuration status for give config
	 * @return String
	 */
	public static String getUserMaskingConfiguration(String property) {
		loadUserMaskingProperties();
		return props.getProperty(property);
	}

	/**
	 * Load user-masking.properties
	 * @return Properties
	 */
	public static Properties loadUserMaskingProperties(){
		if (props == null) {
			props = PropertyFileReader.getFileReader().getProperties(USER_MASKING_PROPERTIES_FILE);
		}
		return props;
	}

	/**
	 * This method is to filter the user ID bases on country or prefix in order to
	 * decide whether this number is eligible for user masking
	 * @param userId User id to be checked
	 * @return true if masking allowed user ID
	 */
	public static boolean isUnmaskedUserId(String userId) {
		boolean isMaskingAllowedUserId = false;
		String regex = UserMaskingConfiguration.getInstance().getUserIdFilterRegex();

		if (userId != null && regex != null) {
			isMaskingAllowedUserId = userId.matches(regex);
		}
		return isMaskingAllowedUserId;
	}
}

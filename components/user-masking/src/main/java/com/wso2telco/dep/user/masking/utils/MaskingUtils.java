package com.wso2telco.dep.user.masking.utils;

import com.wso2telco.core.dbutils.fileutils.PropertyFileReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;

import java.util.Map;
import java.util.Properties;

public class MaskingUtils {

	private static final Log log = LogFactory.getLog(MaskingUtils.class);
	private static final String USER_MASKING_PROPERTIES_FILE = "user-masking.properties";
	private static Properties props = null;


	/**
	 *
	 * @param messageContext
	 * @return APIType
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
	 * This method is to filter the user ID bases on country or prefix in order to
	 * decide whether this number is eligible for user masking
	 * @param userId
	 * @return true if masking allowed user ID
	 */
	public static boolean isMaskingAllowedUserId(String userId) {
		boolean isMaskingAllowedUserId = false;
		String regex = MaskingUtils.getUserMaskingConfiguration("user.masking.feature.user.Id.filter.regex");
		if (userId != null && regex != null) {
			isMaskingAllowedUserId = userId.matches(regex);
		}
		return isMaskingAllowedUserId;
	}

	/**
	 * Get config UserAnonymization
	 */
	public static boolean isUserAnonymizationEnabled() {
		boolean isUserAnonymizationEnabled = false;
		if(getUserMaskingConfiguration("user.masking.feature.enable") != null) {
			try {
				isUserAnonymizationEnabled = Boolean.valueOf(getUserMaskingConfiguration("user.masking.feature.enable"));
			} catch (Exception e) {
				String errorMessage = "Invalid for configration in user-masking.properties :  user.masking.feature.enable";
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
	 * @return Propertiess
	 */
	public static Properties loadUserMaskingProperties(){
		if (props == null) {
			props = PropertyFileReader.getFileReader().getProperties(USER_MASKING_PROPERTIES_FILE);
		}
		return props;
	}
}

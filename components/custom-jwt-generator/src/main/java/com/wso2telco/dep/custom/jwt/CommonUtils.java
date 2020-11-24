package com.wso2telco.dep.custom.jwt;

import java.util.Map;

import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.impl.token.DefaultClaimsRetriever;

public class CommonUtils {

	public static String[] formatStringsToAlphaNum(String[] values) {
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				values[i] = values[i].replaceAll("[^a-zA-Z\\d]", "");
			}
		}
		return values;
	}

	public static String getClaimValueFromDialect(String endUserName, String claimUrl) throws APIManagementException {
		String claimValue = null;
		DefaultClaimsRetriever defaultClaimsRetriever = new DefaultClaimsRetriever();
		Map<String, String> claims = defaultClaimsRetriever.getClaims(endUserName);
		if (claims != null) {
			claimValue = claims.get(claimUrl);
		}
		return claimValue;
	}

}

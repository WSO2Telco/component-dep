package com.wso2telco.dep.ipvalidate.handler.validation.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityConstants;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityException;

import com.wso2telco.dep.ipvalidate.handler.validation.CustomValidator;
import com.wso2telco.dep.ipvalidate.handler.validation.configuration.IPValidationProperties;
import com.wso2telco.dep.ipvalidate.handler.validation.dto.ClientKeyIPData;
import com.wso2telco.dep.ipvalidate.handler.validation.dto.RequestData;
import com.wso2telco.dep.ipvalidate.handler.validation.service.ValidationCacheService;

public class ClientKeyValidation extends CustomValidator {

	private static final Log log = LogFactory.getLog(ClientKeyValidation.class);

	@Override
	public boolean doValidation(RequestData requestData) throws APISecurityException {
		log.debug("Client key validation : " + requestData);
		boolean status = false;

		List<ClientKeyIPData> clientIpSummaryList = ValidationCacheService.getCache().get(requestData.getClientkey());

		if (clientIpSummaryList != null) {
			status = true;
		}

		if (status && nextValidator != null) {
			nextValidator.doValidation(requestData);
		}

		return status;
	}

}

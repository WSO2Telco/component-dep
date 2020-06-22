package com.wso2telco.dep.ipvalidate.handler.validation.impl;

import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityException;

import com.wso2telco.dep.ipvalidate.handler.validation.CustomValidator;
import com.wso2telco.dep.ipvalidate.handler.validation.dto.RequestData;

public class ThirdValidation extends CustomValidator {

	@Override
	public boolean doValidation(RequestData requestData) throws APISecurityException {
		System.out.println("------------Third Validation Implementation--------------------");

		if (nextValidator != null) {
			nextValidator.doValidation(requestData);
		}

		return true;
	}

}

package com.wso2telco.dep.ipvalidate.handler.validation;

import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityException;

import com.wso2telco.dep.ipvalidate.handler.validation.dto.RequestData;

public abstract class CustomValidator {

	protected CustomValidator nextValidator;

	public void setNextValidator(CustomValidator nextValidator) {
		this.nextValidator = nextValidator;
	}

	public abstract boolean doValidation(RequestData requestData) throws APISecurityException;

}

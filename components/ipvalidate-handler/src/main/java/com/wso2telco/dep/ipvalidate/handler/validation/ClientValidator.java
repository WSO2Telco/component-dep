package com.wso2telco.dep.ipvalidate.handler.validation;

import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityException;

import com.wso2telco.dep.ipvalidate.handler.validation.dto.RequestData;

public interface ClientValidator {
	
	public boolean validateRequest(RequestData requestdata) throws APISecurityException ;

}

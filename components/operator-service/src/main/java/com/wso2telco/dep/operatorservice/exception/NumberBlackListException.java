package com.wso2telco.dep.operatorservice.exception;

import com.wso2telco.utils.exception.BusinessException;
import com.wso2telco.utils.exception.ThrowableError;

public class NumberBlackListException extends BusinessException {

	public NumberBlackListException(ThrowableError error) {
		super(error);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8847404632470687579L;

}

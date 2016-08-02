package com.wso2telco.dep.operatorservice.util;

import com.wso2telco.utils.exception.ThrowableError;

public enum OparatorError implements ThrowableError  {
	
	UNDEFINED("POE0001", "Undefined Error"), 
	INVALID_OPARATOR_NAME("POE0002","Oparator Name not found"),
	INVALID_OPARATOR_ID("POE0003","Invalid operator id"),
	INVALID_OPERATOR_SUBSCRIPTION_LIST("POE0004","Invalid operator subscription list");

	private String code;
	private String desc;

	OparatorError(final String code, final String desc) {
		this.desc = desc;
		this.code = code;
	}

	@Override
	public String getMessage() {
		return this.desc;
	}

	@Override
	public String getCode() {
		return this.code;
	}
}

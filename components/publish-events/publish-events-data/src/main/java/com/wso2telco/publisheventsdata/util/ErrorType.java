package com.wso2telco.publisheventsdata.util;

import com.wso2telco.utils.exception.ThrowableError;

public enum ErrorType implements ThrowableError {
	INVALID_GROUP_NAME("PED0001", "Invalid group name"),
	INVALID_OPERATOR("PED0002", "Invalid operator"),
	INVALID_MSISDN("PED0003", "Invalid msisdn");
	
	private String code;
	private String desc;

	ErrorType(final String code, final String desc) {
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

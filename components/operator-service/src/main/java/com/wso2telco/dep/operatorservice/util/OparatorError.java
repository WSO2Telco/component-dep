package com.wso2telco.dep.operatorservice.util;

import com.wso2telco.utils.exception.ThrowableError;

public enum OparatorError implements ThrowableError  {
	UNDEFINED("POE0001", "Undefined Error"), 
	INVALID_OPARATOR_NAME("POE0002","Oparator Name not found"),
	INVALID_OPARATOR_ID("POE0002","Invalid operator id");

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

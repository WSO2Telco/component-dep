package com.wso2telco.dep.reportingservice.exception;

import com.wso2telco.utils.exception.ThrowableError;

public enum ReportingServiceError implements ThrowableError  {
	UNDEFINED("CORE0001", "Undefined Error"),
	INTERNAL_SERVER_ERROR("CORE0299", "Internal Server Error"),
	INPUT_ERROR("CORE0300", "Input mismatch Error");

	private String code;
	private String desc;

	ReportingServiceError(final String code, final String desc) {
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

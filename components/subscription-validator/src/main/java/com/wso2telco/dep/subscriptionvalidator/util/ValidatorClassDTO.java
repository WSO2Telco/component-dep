package com.wso2telco.dep.subscriptionvalidator.util;

import com.wso2telco.dep.subscriptionvalidator.services.MifeValidator;

public class ValidatorClassDTO {
	private String className;
	private Integer app;
	private Integer api;
	private  MifeValidator validator;


	public MifeValidator getValidator() {
		return validator;
	}

	public void setValidator(MifeValidator validator) {
		this.validator = validator;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Integer getApp() {
		return app;
	}

	public void setApp(Integer app) {
		this.app = app;
	}

	public Integer getApi() {
		return api;
	}

	public void setApi(Integer api) {
		this.api = api;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((api == null) ? 0 : api.hashCode());
		result = prime * result + ((app == null) ? 0 : app.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ValidatorClassDTO other = (ValidatorClassDTO) obj;
		if (api == null) {
			if (other.api != null)
				return false;
		} else if (!api.equals(other.api))
			return false;
		if (app == null) {
			if (other.app != null)
				return false;
		} else if (!app.equals(other.app))
			return false;
		return true;
	}



}

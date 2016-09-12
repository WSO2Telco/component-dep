package com.wso2telco.dep.operatorservice.model;

import java.io.Serializable;

public class OperatorSearchDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4819771080570943508L;
	private String name;
	private boolean hasName_;

	public boolean hasName() {
		return hasName_;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name != null && name.trim().length() > 0) {
			this.name = name;
			this.hasName_ = true;
		}
	}

}

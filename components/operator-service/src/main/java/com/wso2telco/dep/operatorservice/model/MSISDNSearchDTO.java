package com.wso2telco.dep.operatorservice.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.wso2telco.core.msisdnvalidator.MSISDN;

public class MSISDNSearchDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7476682043789702438L;
	/**
	 * 
	 */
	private String apiID;
	private List<MSISDN> msisdntoSearch;

	{
		msisdntoSearch = new ArrayList<MSISDN>();
	}

	public String getApiID() {
		return apiID;
	}

	public void setApiID(String apiID) {
		this.apiID = apiID;
	}

	public void addMSISDN2Search(MSISDN msisdn) {
		this.msisdntoSearch.add(msisdn);

	}

	private String apiName_;
	private boolean validApiName_;

	public boolean isValidApiName() {
		return validApiName_;
	}

	public void setApiName(final String apiName) {
		if (apiName != null && apiName.trim().length() > 0) {
			this.apiName_ = apiName.trim();
			this.validApiName_ = true;
		}

	}
	public String getApiName(){
		return this.apiName_;
	}
}

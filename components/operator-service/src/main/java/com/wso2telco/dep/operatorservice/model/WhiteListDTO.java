package com.wso2telco.dep.operatorservice.model;

import java.io.Serializable;
import java.util.Arrays;

public class WhiteListDTO extends AbstractWBDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -629976919811789083L;

	private String subscriptionID;
	private String applicationID;

	public String getSubscriptionID() {
		return subscriptionID;
	}

	private boolean isValidSubscriptionID_;

	public void setSubscriptionID(final String subscriptionID) {
		if (subscriptionID != null && subscriptionID.trim().length() > 0) {
			this.subscriptionID = subscriptionID.trim();
			isValidSubscriptionID_ = true;
		}

	}

	public boolean hasValidSubscriptionID() {
		return isValidSubscriptionID_;
	}

	private boolean hasValidAppid_;

	public String getApplicationID() {
		return applicationID;
	}

	public void setApplicationID(String applicationID) {
		if (applicationID != null && applicationID.trim().length() > 0) {
			this.applicationID = applicationID;
			this.hasValidAppid_=true;
		}
	}
	public boolean hasValidApplicationID(){
		return this.hasValidAppid_;
	}

	@Override
	public String toString() {
		return "WhiteListDTO [subscriptionID=" + subscriptionID + ", applicationID=" + applicationID
				+ ", getUserMSISDN()=" + Arrays.toString(getUserMSISDN()) + ", getApiID()=" + getApiID() + "]";
	}

}

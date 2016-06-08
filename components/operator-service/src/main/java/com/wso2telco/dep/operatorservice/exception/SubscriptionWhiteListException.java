package com.wso2telco.dep.operatorservice.exception;

import com.wso2telco.utils.exception.BusinessException;
import com.wso2telco.utils.exception.ThrowableError;

public class SubscriptionWhiteListException extends BusinessException {

	public SubscriptionWhiteListException(ThrowableError error) {
		super(error);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -2910795693397794853L;

	public enum SubscriptionWhiteListErrorType implements ThrowableError  {
		
		NULL_SUBSCRIPTION("SWE0001", "No valid subscription id found "),
		SUBSCRIPTION_ALREADY_WHITELISTED("SWE0002", "Subscription allready whitelisted");
		
		//all the msisdns already white listed for the given subscription
		SubscriptionWhiteListErrorType(final String code, final String msg) {
			
			this.code = code;
			this.msg = msg;
		}

		final String code;
		final String msg;

		public String getMessage() {
			
			return this.msg;
		}
		
		public String getCode(){
			
			return this.code;
		}
	}

	@Override
	public String toString() {
		return "SubscriptionWhiteListException [getErrorType()=" + getErrorType() + ", getMessage()=" + getMessage()
				+ "]";
	}
	


}

package com.wso2telco.dep.verificationhandler.model.ussd;

public class InboundUSSDMessageRequest {

	private String address;
	private String sessionID;
	private String shortCode;
	private String keyword;
	private String inboundUSSDMessage;
	private String clientCorrelator;
	private ResponseRequest responseRequest;
	private USSDAction ussdAction;

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * 
	 * @return
	 */
	public String getSessionID() {
		return sessionID;
	}

	/**
	 * @return the senderAddress
	 */
	public String getShortCode() {
		return shortCode;
	}

	/**
	 * @return the outboundUSSDMessage
	 */
	public String getInboundUSSDMessage() {
		return inboundUSSDMessage;
	}

	/**
	 * @return the clientCorrelator
	 */
	public String getClientCorrelator() {
		return clientCorrelator;
	}

	/**
	 * @return the responseRequest
	 */
	public ResponseRequest getResponseRequest() {
		return responseRequest;
	}

	/**
	 * @return the ussdAction
	 */
	public USSDAction getUssdAction() {
		return ussdAction;
	}

	/**
	 * @return the keyword
	 */
	public String getKeyword() {
		return keyword;
	}

	/**
	 * 
	 * @param sessionID
	 */
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	/**
	 * @param shortCode
	 *            the shortCode to set
	 */
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	/**
	 * @param keyword
	 *            the keyword to set
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	/**
	 * @param ussdAction
	 *            the ussdAction to set
	 */
	public void setUssdAction(USSDAction ussdAction) {
		this.ussdAction = ussdAction;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @param outboundUSSDMessage
	 *            the outboundUSSDMessage to set
	 */
	public void setInboundUSSDMessage(String inboundUSSDMessage) {
		this.inboundUSSDMessage = inboundUSSDMessage;
	}

	/**
	 * @param clientCorrelator
	 *            the clientCorrelator to set
	 */
	public void setClientCorrelator(String clientCorrelator) {
		this.clientCorrelator = clientCorrelator;
	}

	/**
	 * @param responseRequest
	 *            the responseRequest to set
	 */
	public void setResponseRequest(ResponseRequest responseRequest) {
		this.responseRequest = responseRequest;
	}
}

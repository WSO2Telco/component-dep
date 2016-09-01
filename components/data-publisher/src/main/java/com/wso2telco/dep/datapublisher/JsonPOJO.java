package com.wso2telco.dep.datapublisher;

class JsonPOJO {
	private InboundSMSMessageList inboundSMSMessageList;

	public InboundSMSMessageList getInboundSMSMessageList() {
		return inboundSMSMessageList;
	}

	public void setInboundSMSMessageList(
			InboundSMSMessageList inboundSMSMessageList) {
		this.inboundSMSMessageList = inboundSMSMessageList;
	}

	@Override
	public String toString() {
		return "ClassPojo [inboundSMSMessageList = " + inboundSMSMessageList
				+ "]";
	}
}

class InboundSMSMessage {
	private String message;

	private String senderAddress;

	private String resourceURL;

	private String dateTime;

	private String destinationAddress;

	private String messageId;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSenderAddress() {
		return senderAddress;
	}

	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}

	public String getResourceURL() {
		return resourceURL;
	}

	public void setResourceURL(String resourceURL) {
		this.resourceURL = resourceURL;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getDestinationAddress() {
		return destinationAddress;
	}

	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

}

class InboundSMSMessageList {
	private InboundSMSMessage[] inboundSMSMessage;

	private String resourceURL;

	private String totalNumberOfPendingMessages;

	private String numberOfMessagesInThisBatch;

	public InboundSMSMessage[] getInboundSMSMessage() {
		return inboundSMSMessage;
	}

	public void setInboundSMSMessage(InboundSMSMessage[] inboundSMSMessage) {
		this.inboundSMSMessage = inboundSMSMessage;
	}

	public String getResourceURL() {
		return resourceURL;
	}

	public void setResourceURL(String resourceURL) {
		this.resourceURL = resourceURL;
	}

	public String getTotalNumberOfPendingMessages() {
		return totalNumberOfPendingMessages;
	}

	public void setTotalNumberOfPendingMessages(
			String totalNumberOfPendingMessages) {
		this.totalNumberOfPendingMessages = totalNumberOfPendingMessages;
	}

	public String getNumberOfMessagesInThisBatch() {
		return numberOfMessagesInThisBatch;
	}

	public void setNumberOfMessagesInThisBatch(
			String numberOfMessagesInThisBatch) {
		this.numberOfMessagesInThisBatch = numberOfMessagesInThisBatch;
	}
}

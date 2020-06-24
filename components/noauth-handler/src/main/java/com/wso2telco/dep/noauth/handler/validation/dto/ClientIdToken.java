package com.wso2telco.dep.noauth.handler.validation.dto;

public class ClientIdToken {

	private int clientId = 0;
	private String clientKey = null;
	private String token = null;

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public String getClientKey() {
		return clientKey;
	}

	public void setClientKey(String clientKey) {
		this.clientKey = clientKey;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "ClientIdToken [clientId=" + clientId + ", clientKey=" + clientKey + ", token=" + token + "]";
	}

}

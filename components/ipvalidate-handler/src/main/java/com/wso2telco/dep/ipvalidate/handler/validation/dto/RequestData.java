package com.wso2telco.dep.ipvalidate.handler.validation.dto;

public class RequestData {

	private String hostip = null;
	private String clientkey = null;
	private String clienttoken = null;

	public String getHostip() {
		return hostip;
	}

	public void setHostip(String hostip) {
		this.hostip = hostip;
	}

	public String getClientkey() {
		return clientkey;
	}

	public void setClientkey(String clientkey) {
		this.clientkey = clientkey;
	}

	public String getClienttoken() {
		return clienttoken;
	}

	public void setClienttoken(String clienttoken) {
		this.clienttoken = clienttoken;
	}

	@Override
	public String toString() {
		return "RequestData [hostip=" + hostip + ", clientkey=" + clientkey + ", clienttoken=" + clienttoken + "]";
	}
	
	

}

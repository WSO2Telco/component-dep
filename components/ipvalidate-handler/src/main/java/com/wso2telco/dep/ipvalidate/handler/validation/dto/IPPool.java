package com.wso2telco.dep.ipvalidate.handler.validation.dto;

public class IPPool {

	private int poolId = 0;
	private String ip = null;

	public int getPoolId() {
		return poolId;
	}

	public void setPoolId(int poolId) {
		this.poolId = poolId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Override
	public String toString() {
		return "IPPool [poolId=" + poolId + ", ip=" + ip + "]";
	}

}

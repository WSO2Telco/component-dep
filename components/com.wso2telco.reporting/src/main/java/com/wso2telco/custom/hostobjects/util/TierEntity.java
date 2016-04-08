package com.wso2telco.custom.hostobjects.util;

import java.util.HashMap;

public class TierEntity {

	private String tierRate;
	private HashMap<String, Integer> tiers;

	public String getTierRate() {
		return tierRate;
	}

	public void setTierRate(String tierRate) {
		this.tierRate = tierRate;
	}

	public HashMap<String, Integer> getTiers() {
		return tiers;
	}

	public void setTiers(HashMap<String, Integer> tiers) {
		this.tiers = tiers;
	}
}
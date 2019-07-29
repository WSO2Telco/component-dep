package com.wso2telco.dep.operatorservice.model;

public class BlacklistWhitelistRequestDTO {

    private String msisdn;
    private String action;
    private String sp;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getSp() {
        return sp;
    }

    public void setSp(String sp) {
        this.sp = sp;
    }
}

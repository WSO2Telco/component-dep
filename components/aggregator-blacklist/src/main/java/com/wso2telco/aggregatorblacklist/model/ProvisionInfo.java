package com.wso2telco.aggregatorblacklist.model;


import java.util.List;

public class ProvisionInfo {

    private String applicationid;
    private List<String> merchantcode;
    private String subscriber;
    private String operatorcode;
    
    public String getApplicationid() {
        return applicationid;
    }

    public void setApplicationid(String applicationid) {
        this.applicationid = applicationid;
    }
    

    public List<String> getMerchantcode() {
        return merchantcode;
    }

    public void setMerchantcode(List<String> merchantcode) {
        this.merchantcode = merchantcode;
    }
    

    public String getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(String subscriber) {
        this.subscriber = subscriber;
    }

    public String getOperatorcode() {
        return operatorcode;
    }

    public void setOperatorcode(String operatorcode) {
        this.operatorcode = operatorcode;
    }    
    
}
package com.wso2telco.workflow.approval.model;


public class Subscription {

    private int endpointID;
    private int applicationID;
    private String status;
    private String ApiName;
    private String OpID;
    private String operatorName;

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOpID() {
        return OpID;
    }

    public void setOpID(String opID) {
        OpID = opID;
    }

    public String getApiName() {
        return ApiName;
    }

    public void setApiName(String apiName) {
        ApiName = apiName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(int applicationID) {
        this.applicationID = applicationID;
    }

    public int getEndpointID() {
        return endpointID;
    }

    public void setEndpointID(int endpointID) {
        this.endpointID = endpointID;
    }



}

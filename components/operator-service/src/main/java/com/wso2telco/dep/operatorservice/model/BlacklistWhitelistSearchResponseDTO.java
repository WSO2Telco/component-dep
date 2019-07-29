package com.wso2telco.dep.operatorservice.model;

public class BlacklistWhitelistSearchResponseDTO {

    private boolean exists;
    private String error;

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

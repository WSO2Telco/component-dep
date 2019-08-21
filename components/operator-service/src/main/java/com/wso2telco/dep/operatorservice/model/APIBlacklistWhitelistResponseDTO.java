package com.wso2telco.dep.operatorservice.model;

/**
 * DTO class for blacklist whitelist service response
 */
public class APIBlacklistWhitelistResponseDTO {
    boolean validationError;
    int processed;
    int failed;

    public APIBlacklistWhitelistResponseDTO() {
        this.processed = 0;
        this.failed = 0;
        validationError = false;
    }

    public boolean isValidationError() {
        return validationError;
    }

    public int getProcessed() {
        return processed;
    }

    public void setProcessed(int processed) {
        this.processed = processed;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
        validationError = failed > 0;
    }
}

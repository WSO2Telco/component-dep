package com.wso2telco.dep.operatorservice.model;

public class APIBlacklistWhitelistResponseDTO {
    boolean validationError;
    int processed;
    int failed;

    public APIBlacklistWhitelistResponseDTO(int processed, int failed) {
        this.processed = processed;
        this.failed = failed;
        validationError = failed > 0;
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

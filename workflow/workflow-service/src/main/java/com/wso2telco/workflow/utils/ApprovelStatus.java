package com.wso2telco.workflow.utils;


public enum ApprovelStatus {

    PENDING(0),APPROVED(1),REJECTED(2);

    private final int value;
    private ApprovelStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

package com.wso2telco.dep.mediator.internal.messageenum;

/**
 * Created by sasikala on 11/24/16.
 */
public enum ClientReference {

    PAYMENT_REQUEST_REFCODE("referenceCode"),
    PAYMENT_RESPONSE_REFCODE("serverReferenceCode");

    private String refCode;

    private ClientReference(String code){
        this.refCode = code;
    }

    public String getRefCode() {
        return refCode;

    }
}
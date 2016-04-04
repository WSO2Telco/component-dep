package com.wso2telco.oneapivalidation;

public class ResponseError {

    private RequestError requestError;

    public RequestError getRequestError() {
        return requestError;
    }

    public void setRequestError(RequestError requestError) {
        this.requestError = requestError;
    }
}

package com.wso2telco.dep.operatorservice;

public class AppObject {
    /**
     * DTO to store the following application related information:
     * application_id, name, SP_name, access_token, consumer_key and consumer_secret
     */

    private int app_id;
    private String app_name;
    private String sp_name;
    private String access_token;
    private String consumer_key;
    private String consmer_secret;


    public int getApp_id() {
        return app_id;
    }

    public void setApp_id(int app_id) {
        this.app_id = app_id;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getSp_name() {
        return sp_name;
    }

    public void setSp_name(String sp_name) {
        this.sp_name = sp_name;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getConsumer_key() {
        return consumer_key;
    }

    public void setConsumer_key(String consumer_key) {
        this.consumer_key = consumer_key;
    }

    public String getConsmer_secret() {
        return consmer_secret;
    }

    public void setConsmer_secret(String consmer_secret) {
        this.consmer_secret = consmer_secret;
    }
}

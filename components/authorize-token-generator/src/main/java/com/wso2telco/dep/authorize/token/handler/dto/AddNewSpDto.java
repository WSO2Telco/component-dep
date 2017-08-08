package com.wso2telco.dep.authorize.token.handler.dto;

import java.util.ArrayList;

/**
 * Created by sheshan on 8/4/17.
 */
public class AddNewSpDto {

    private String ownerId;
    private final String tokenUrl = "https://localhost:8243/token";
    private final long defaultconnectionresettime = 4000;
    private final int retryAttmpt =3;
    private final int retrymax=10;
    private final int retrydelay=20000;
    private ArrayList<TokenDTO> spTokenList ;

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getTokenUrl() { return tokenUrl; }

    public long getDefaultconnectionresettime() {
        return defaultconnectionresettime;
    }

    public int getRetryAttmpt() {
        return retryAttmpt;
    }

    public int getRetrymax() {
        return retrymax;
    }

    public int getRetrydelay() {
        return retrydelay;
    }

    public ArrayList<TokenDTO> getSpTokenList() {
        return spTokenList;
    }

    public void setSpTokenList(ArrayList<TokenDTO> spTokenList) {
        this.spTokenList = spTokenList;
    }
}

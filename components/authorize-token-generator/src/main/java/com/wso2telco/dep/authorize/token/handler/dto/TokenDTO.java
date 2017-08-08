package com.wso2telco.dep.authorize.token.handler.dto;

/**
 * Created by sheshan on 8/4/17.
 */
public class TokenDTO {

    private int id;
    private int whoId;
    private String tokenAuth;
    private long tokenValidity;
    private boolean valid;
    private String accessToken;
    private String refreshToken;
    private int uc;
    private long createdTime;
    private int parentTokenId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWhoId() {
        return whoId;
    }

    public void setWhoId(int whoId) {
        this.whoId = whoId;
    }

    public String getTokenAuth() {
        return tokenAuth;
    }

    public void setTokenAuth(String tokenAuth) {
        this.tokenAuth = tokenAuth;
    }

    public long getTokenValidity() {
        return tokenValidity;
    }

    public void setTokenValidity(long tokenValidity) {
        this.tokenValidity = tokenValidity;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public int getUc() {
        return uc;
    }

    public void setUc(int uc) {
        this.uc = uc;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public int getParentTokenId() {
        return parentTokenId;
    }

    public void setParentTokenId(int parentTokenId) {
        this.parentTokenId = parentTokenId;
    }
}

package com.wso2telco.dep.usermaskservice.service;


import com.wso2telco.dep.user.masking.UserMaskHandler;

public class UserMaskService {

    public String getUserMask(String userId) {
        return UserMaskHandler.getUserMask(userId);
    }

    public String getUserId(String mask) {
        return UserMaskHandler.getUserId(mask);
    }
}

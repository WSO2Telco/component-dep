package com.wso2telco.workflow.model;


import javax.xml.bind.annotation.XmlRootElement;

/**
 * PLUGINAdminAppApprovalEmailNotificationRequestType bean class
 */

@XmlRootElement
public class PLUGINAdminAppApprovalNotificationRequest {


    protected String receiverRole;
    protected String applicationName;
    protected String applicationTier;
    protected String applicationDescription;
    protected String userName;

    public String getReceiverRole() {
        return receiverRole;
    }

    public void setReceiverRole(String receiverRole) {
        this.receiverRole = receiverRole;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationTier() {
        return applicationTier;
    }

    public void setApplicationTier(String applicationTier) {
        this.applicationTier = applicationTier;
    }

    public String getApplicationDescription() {
        return applicationDescription;
    }

    public void setApplicationDescription(String applicationDescription) {
        this.applicationDescription = applicationDescription;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }





}
           
    
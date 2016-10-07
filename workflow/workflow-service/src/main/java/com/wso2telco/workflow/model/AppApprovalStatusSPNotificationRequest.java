package com.wso2telco.workflow.model;


import javax.xml.bind.annotation.XmlRootElement;

/**
 * AppApprovalStatusSPEmailNotificationRequestType bean class
 */


@XmlRootElement
public class AppApprovalStatusSPNotificationRequest {

    /**
     * field for ApplicationName
     */
    protected String applicationName;
    /**
     * field for ApplicationTier
     */
    protected String applicationTier;
    /**
     * field for ApplicationDescription
     */
    protected String applicationDescription;
    /**
     * field for UserName
     */
    protected String userName;
    /**
     * field for ApprovalStatus
     */
    protected String approvalStatus;

    /**
     * @return java.lang.String
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * @param param ApplicationName
     */
    public void setApplicationName(String param) {
        this.applicationName = param;
    }

    /**
     * @return java.lang.String
     */
    public String getApplicationTier() {
        return applicationTier;
    }

    /**
     * @param param ApplicationTier
     */
    public void setApplicationTier(String param) {
        this.applicationTier = param;
    }

    /**
     * @return java.lang.String
     */
    public String getApplicationDescription() {
        return applicationDescription;
    }

    /**
     * @param param ApplicationDescription
     */
    public void setApplicationDescription(String param) {
        this.applicationDescription = param;
    }

    /**
     * @return java.lang.String
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param param UserName
     */
    public void setUserName(String param) {

        this.userName = param;


    }

    /**
     * @return java.lang.String
     */
    public String getApprovalStatus() {
        return approvalStatus;
    }

    /**
     * @param param ApprovalStatus
     */
    public void setApprovalStatus(String param) {
        this.approvalStatus = param;
    }


}
           
    
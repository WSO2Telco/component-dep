package com.wso2telco.workflow.audit;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * AppApprovalAuditRequestType bean class
 */


@XmlRootElement(name = "AppApprovalAuditRequest")
public class AppApprovalAuditRequestType {
        /* This type was generated from the piece of schema that had
                name = AppApprovalAuditRequestType
                Namespace URI = http://org.wso2.carbon/axiata/workflow/audit
                Namespace Prefix = ns4
                */


    /**
     * field for RequestType
     */


    protected java.lang.String localRequestType;


    /**
     * Auto generated getter method
     *
     * @return java.lang.String
     */
    public java.lang.String getRequestType() {
        return localRequestType;
    }


    /**
     * Auto generated setter method
     *
     * @param param RequestType
     */
    public void setRequestType(java.lang.String param) {

        this.localRequestType = param;


    }


    /**
     * field for AppName
     */

    protected java.lang.String localAppName;


    /**
     * Auto generated getter method
     *
     * @return java.lang.String
     */
    public java.lang.String getAppName() {
        return localAppName;
    }


    /**
     * Auto generated setter method
     *
     * @param param AppName
     */
    public void setAppName(java.lang.String param) {

        this.localAppName = param;


    }


    /**
     * field for AppCreator
     */


    protected java.lang.String localAppCreator;


    /**
     * Auto generated getter method
     *
     * @return java.lang.String
     */
    public java.lang.String getAppCreator() {
        return localAppCreator;
    }


    /**
     * Auto generated setter method
     *
     * @param param AppCreator
     */
    public void setAppCreator(java.lang.String param) {

        this.localAppCreator = param;


    }


    /**
     * field for AppStatus
     */


    protected java.lang.String localAppStatus;


    /**
     * Auto generated getter method
     *
     * @return java.lang.String
     */
    public java.lang.String getAppStatus() {
        return localAppStatus;
    }


    /**
     * Auto generated setter method
     *
     * @param param AppStatus
     */
    public void setAppStatus(java.lang.String param) {

        this.localAppStatus = param;


    }


    /**
     * field for AppApprovalType
     */

    protected java.lang.String localAppApprovalType;


    /**
     * Auto generated getter method
     *
     * @return java.lang.String
     */
    public java.lang.String getAppApprovalType() {
        return localAppApprovalType;
    }


    /**
     * Auto generated setter method
     *
     * @param param AppApprovalType
     */
    public void setAppApprovalType(java.lang.String param) {

        this.localAppApprovalType = param;


    }


    /**
     * field for CompletedByRole
     */


    protected java.lang.String localCompletedByRole;


    /**
     * Auto generated getter method
     *
     * @return java.lang.String
     */
    public java.lang.String getCompletedByRole() {
        return localCompletedByRole;
    }


    /**
     * Auto generated setter method
     *
     * @param param CompletedByRole
     */
    public void setCompletedByRole(java.lang.String param) {

        this.localCompletedByRole = param;


    }


    /**
     * field for CompletedByUser
     */


    protected java.lang.String localCompletedByUser;


    /**
     * Auto generated getter method
     *
     * @return java.lang.String
     */
    public java.lang.String getCompletedByUser() {
        return localCompletedByUser;
    }


    /**
     * Auto generated setter method
     *
     * @param param CompletedByUser
     */
    public void setCompletedByUser(java.lang.String param) {

        this.localCompletedByUser = param;


    }


    /**
     * field for CompletedOn
     */

    
    protected java.lang.String localCompletedOn;


    /**
     * Auto generated getter method
     *
     * @return java.lang.String
     */
    public java.lang.String getCompletedOn() {
        return localCompletedOn;
    }


    /**
     * Auto generated setter method
     *
     * @param param CompletedOn
     */
    public void setCompletedOn(java.lang.String param) {

        this.localCompletedOn = param;


    }


}
           
    
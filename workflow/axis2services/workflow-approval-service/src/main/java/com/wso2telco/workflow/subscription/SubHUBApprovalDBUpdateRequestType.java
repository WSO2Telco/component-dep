
/**
 * SubHUBApprovalDBUpdateRequestType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.1-wso2v10  Built on : Sep 04, 2013 (02:03:08 UTC)
 */


package com.wso2telco.workflow.subscription;


import javax.xml.bind.annotation.XmlRootElement;

/**
 * SubHUBApprovalDBUpdateRequestType bean class
 */


@XmlRootElement(name = "SubHUBApprovalDBUpdateRequest")
public class SubHUBApprovalDBUpdateRequestType {
        /* This type was generated from the piece of schema that had
                name = SubHUBApprovalDBUpdateRequestType
                Namespace URI = http://org.wso2.carbon/axiata/workflow/subscription
                Namespace Prefix = ns3
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
     * field for AppID
     */


    protected java.lang.String localAppID;


    /**
     * Auto generated getter method
     *
     * @return java.lang.String
     */
    public java.lang.String getAppID() {
        return localAppID;
    }


    /**
     * Auto generated setter method
     *
     * @param param AppID
     */
    public void setAppID(java.lang.String param) {

        this.localAppID = param;


    }


    /**
     * field for ApiName
     */


    protected java.lang.String localApiName;


    /**
     * Auto generated getter method
     *
     * @return java.lang.String
     */
    public java.lang.String getApiName() {
        return localApiName;
    }


    /**
     * Auto generated setter method
     *
     * @param param ApiName
     */
    public void setApiName(java.lang.String param) {

        this.localApiName = param;


    }


}
           
    
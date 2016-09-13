
/**
 * HUBApprovalSubValidatorRequestType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.1-wso2v10  Built on : Sep 04, 2013 (02:03:08 UTC)
 */


package com.wso2telco.workflow.subscription;


import javax.xml.bind.annotation.XmlRootElement;

/**
 * HUBApprovalSubValidatorRequestType bean class
 */


@XmlRootElement(name = "HUBApprovalSubValidatorRequest")
public class HUBApprovalSubValidatorRequestType {
        /* This type was generated from the piece of schema that had
                name = HUBApprovalSubValidatorRequestType
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
     * field for ApiID
     */


    protected java.lang.String localApiID;


    /**
     * Auto generated getter method
     *
     * @return java.lang.String
     */
    public java.lang.String getApiID() {
        return localApiID;
    }


    /**
     * Auto generated setter method
     *
     * @param param ApiID
     */
    public void setApiID(java.lang.String param) {

        this.localApiID = param;


    }


}
           
    
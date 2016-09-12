/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licences this file to you under  the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.dep.operatorservice.model;


import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class ProvisionInfo.
 */
public class ProvisionInfo {

    /** The applicationid. */
    private String applicationid;
    
    /** The merchantcode. */
    private List<String> merchantcode;
    
    /** The subscriber. */
    private String subscriber;
    
    /** The operatorcode. */
    private String operatorcode;
    
    /**
     * Gets the applicationid.
     *
     * @return the applicationid
     */
    public String getApplicationid() {
        return applicationid;
    }

    /**
     * Sets the applicationid.
     *
     * @param applicationid the new applicationid
     */
    public void setApplicationid(String applicationid) {
        this.applicationid = applicationid;
    }
    

    /**
     * Gets the merchantcode.
     *
     * @return the merchantcode
     */
    public List<String> getMerchantcode() {
        return merchantcode;
    }

    /**
     * Sets the merchantcode.
     *
     * @param merchantcode the new merchantcode
     */
    public void setMerchantcode(List<String> merchantcode) {
        this.merchantcode = merchantcode;
    }
    

    /**
     * Gets the subscriber.
     *
     * @return the subscriber
     */
    public String getSubscriber() {
        return subscriber;
    }

    /**
     * Sets the subscriber.
     *
     * @param subscriber the new subscriber
     */
    public void setSubscriber(String subscriber) {
        this.subscriber = subscriber;
    }

    /**
     * Gets the operatorcode.
     *
     * @return the operatorcode
     */
    public String getOperatorcode() {
        return operatorcode;
    }

    /**
     * Sets the operatorcode.
     *
     * @param operatorcode the new operatorcode
     */
    public void setOperatorcode(String operatorcode) {
        this.operatorcode = operatorcode;
    }    
    
}

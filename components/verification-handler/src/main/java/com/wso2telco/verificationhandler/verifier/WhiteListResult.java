/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.verificationhandler.verifier;

 
// TODO: Auto-generated Javadoc
/**
 * The Class WhiteListResult.
 */
public class WhiteListResult {
    
    /** The subscription id. */
    String subscriptionID;
    
    /** The msisdn. */
    String msisdn;
    
    /** The api_id. */
    String api_id;
    
    /** The application_id. */
    String application_id;

    /**
     * Gets the subscription id.
     *
     * @return the subscription id
     */
    public String getSubscriptionID() {
        return subscriptionID;
    }

    /**
     * Sets the subscription id.
     *
     * @param subscriptionID the new subscription id
     */
    public void setSubscriptionID(String subscriptionID) {
        this.subscriptionID = subscriptionID;
    }

    /**
     * Gets the msisdn.
     *
     * @return the msisdn
     */
    public String getMsisdn() {
        return msisdn;
    }

    /**
     * Sets the msisdn.
     *
     * @param msisdn the new msisdn
     */
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    /**
     * Gets the api_id.
     *
     * @return the api_id
     */
    public String getApi_id() {
        return api_id;
    }

    /**
     * Sets the api_id.
     *
     * @param api_id the new api_id
     */
    public void setApi_id(String api_id) {
        this.api_id = api_id;
    }

    /**
     * Gets the application_id.
     *
     * @return the application_id
     */
    public String getApplication_id() {
        return application_id;
    }

    /**
     * Sets the application_id.
     *
     * @param application_id the new application_id
     */
    public void setApplication_id(String application_id) {
        this.application_id = application_id;
    }
}


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
package com.wso2telco.dep.verificationhandler.model.USSD;

 
// TODO: Auto-generated Javadoc
/**
 * The Class ResponseRequest.
 */
public class ResponseRequest {
    
    /** The notify url. */
    String notifyURL;
    
    /** The callback data. */
    String callbackData;

    /**
     * Gets the notify url.
     *
     * @return the notify url
     */
    public String getNotifyURL() {
        return notifyURL;
    }

    /**
     * Sets the notify url.
     *
     * @param notifyURL the new notify url
     */
    public void setNotifyURL(String notifyURL) {
        this.notifyURL = notifyURL;
    }

    /**
     * Gets the callback data.
     *
     * @return the callback data
     */
    public String getCallbackData() {
        return callbackData;
    }

    /**
     * Sets the callback data.
     *
     * @param callbackData the new callback data
     */
    public void setCallbackData(String callbackData) {
        this.callbackData = callbackData;
    }

    /**
     * Instantiates a new response request.
     */
    public ResponseRequest() {
    }

    /**
     * Instantiates a new response request.
     *
     * @param notifyURL the notify url
     * @param callbackData the callback data
     */
    public ResponseRequest(String notifyURL, String callbackData) {
        this.notifyURL = notifyURL;
        this.callbackData = callbackData;
    }
}


/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *  
 *  WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.dep.custom.hostobjects;


import java.sql.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class APIRequestDTO.
 */
public class APIRequestDTO {

    /** The api version. */
    private String apiVersion;
    
    /** The request count. */
    private int requestCount;
    
    /** The date. */
    private Date date;

    /**
     * Gets the api version.
     *
     * @return the api version
     */
    public String getApiVersion() {
        return apiVersion;
    }

    /**
     * Sets the api version.
     *
     * @param apiVersion the new api version
     */
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    /**
     * Gets the request count.
     *
     * @return the request count
     */
    public int getRequestCount() {
        return requestCount;
    }

    /**
     * Sets the request count.
     *
     * @param requestCount the new request count
     */
    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }

    /**
     * Gets the date.
     *
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date.
     *
     * @param date the new date
     */
    public void setDate(Date date) {
        this.date = date;
    }
}

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
package com.wso2telco.aggregatorblacklist.model;

// TODO: Auto-generated Javadoc
/**
 * The Class RequestError.
 */
public class RequestError{

    /** The errorreturn. */
    private ErrorReturn errorreturn;

    /**
     * Instantiates a new request error.
     *
     * @param errorreturn the errorreturn
     */
    public RequestError(ErrorReturn errorreturn) {
        this.errorreturn = errorreturn;
    }
        
    /**
     * Gets the errorreturn.
     *
     * @return the errorreturn
     */
    public ErrorReturn getErrorreturn() {
        return errorreturn;
    }

    /**
     * Sets the errorreturn.
     *
     * @param errorreturn the new errorreturn
     */
    public void setErrorreturn(ErrorReturn errorreturn) {
        this.errorreturn = errorreturn;
    }

    
}

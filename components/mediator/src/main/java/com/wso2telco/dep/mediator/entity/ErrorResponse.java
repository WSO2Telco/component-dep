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
package com.wso2telco.dep.mediator.entity;

import javax.xml.bind.annotation.XmlRootElement;

 

// TODO: Auto-generated Javadoc
/**
 * The Class ErrorResponse.
 */
@XmlRootElement(name = "errorreturn")
@Deprecated
public class ErrorResponse {
    
    /** The errorreturn. */
    private ErrorReturn errorreturn;
    
    /**
     * Instantiates a new error response.
     */
    public ErrorResponse() {
        
    }
    
    /**
     * Gets the error response.
     *
     * @return the error response
     */
    public ErrorReturn getErrorResponse() {
            return errorreturn;
    }


    /**
     * Sets the error response.
     *
     * @param errorreturn the new error response
     */
    public void setErrorResponse(ErrorReturn errorreturn) {
            this.errorreturn = errorreturn;
    }
    
}

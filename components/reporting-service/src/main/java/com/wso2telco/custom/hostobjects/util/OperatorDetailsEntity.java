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
package com.wso2telco.custom.hostobjects.util;

// TODO: Auto-generated Javadoc
/**
 * The Class OperatorDetailsEntity.
 */
public class OperatorDetailsEntity {
    
    /** The operator name. */
    private String operatorName;
    
    /** The rate name. */
    private String rateName;
    
    /** The operation id. */
    private int operationId;

    /**
     * Gets the operator name.
     *
     * @return the operator name
     */
    public String getOperatorName() {
        return operatorName;
    }

    /**
     * Sets the operator name.
     *
     * @param operatorName the new operator name
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    /**
     * Gets the rate name.
     *
     * @return the rate name
     */
    public String getRateName() {
        return rateName;
    }

    /**
     * Sets the rate name.
     *
     * @param rateName the new rate name
     */
    public void setRateName(String rateName) {
        this.rateName = rateName;
    }

    /**
     * Gets the operation id.
     *
     * @return the operation id
     */
    public int getOperationId() {
        return operationId;
    }

    /**
     * Sets the operation id.
     *
     * @param operationId the new operation id
     */
    public void setOperationId(int operationId) {
        this.operationId = operationId;
    }
}

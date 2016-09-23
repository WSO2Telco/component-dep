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
package com.wso2telco.dep.oneapivalidation.exceptions;

import com.wso2telco.core.dbutils.exception.ThrowableError;

// TODO: Auto-generated Javadoc
/**
 * The Class PolicyException.
 */
public class PolicyException {

    /**
     * Instantiates a new policy exception.
     *
     * @param messageId the message id
     * @param text the text
     * @param variables the variables
     */
    public PolicyException(String messageId, String text, String variables) {
        this.messageId = messageId;
        this.text = text;
        this.variables = variables;
    }
    
    /**
     * Instantiates a new policy exception.
     *
     * @param ThrowableError the throwable error
     * @param variables the variables
     */
    public PolicyException(ThrowableError error, String variables) {
    	this.messageId = error.getCode();
    	this.text = error.getMessage();
    	this.variables = variables;
    }
    
    /** The message id. */
    private String messageId;
    
    /**
     * Gets the message id.
     *
     * @return the message id
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Sets the message id.
     *
     * @param messageId the new message id
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    
    /** The text. */
    private String text;

    /**
     * Gets the text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text.
     *
     * @param text the new text
     */
    public void setText(String text) {
        this.text = text;
    }
    
    /** The variables. */
    private String variables;

    /**
     * Gets the variables.
     *
     * @return the variables
     */
    public String getVariables() {
        return variables;
    }

    /**
     * Sets the variables.
     *
     * @param variables the new variables
     */
    public void setVariables(String variables) {
        this.variables = variables;
    }
}

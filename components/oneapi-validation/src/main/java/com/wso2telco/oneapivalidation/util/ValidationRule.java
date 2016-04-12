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
package com.wso2telco.oneapivalidation.util;

// TODO: Auto-generated Javadoc
/**
 * The Class ValidationRule.
 */
public class ValidationRule {

     
    /** The Constant VALIDATION_TYPE_OPTIONAL. */
    public static final int VALIDATION_TYPE_OPTIONAL = 0;
     
    /** The Constant VALIDATION_TYPE_MANDATORY. */
    public static final int VALIDATION_TYPE_MANDATORY = 1;
     
    /** The Constant VALIDATION_TYPE_MANDATORY_DOUBLE_GT_ZERO. */
    public static final int VALIDATION_TYPE_MANDATORY_DOUBLE_GT_ZERO = 2;
     
    /** The Constant VALIDATION_TYPE_MANDATORY_DOUBLE_GE_ZERO. */
    public static final int VALIDATION_TYPE_MANDATORY_DOUBLE_GE_ZERO = 3;
     
    /** The Constant VALIDATION_TYPE_MANDATORY_TEL. */
    public static final int VALIDATION_TYPE_MANDATORY_TEL = 5;
     
    /** The Constant VALIDATION_TYPE_MANDATORY_URL. */
    public static final int VALIDATION_TYPE_MANDATORY_URL = 10;
     
    /** The Constant VALIDATION_TYPE_MANDATORY_INT_GE_ZERO. */
    public static final int VALIDATION_TYPE_MANDATORY_INT_GE_ZERO = 15;
     
    /** The Constant VALIDATION_TYPE_MANDATORY_INT_GT_ONE. */
    public static final int VALIDATION_TYPE_MANDATORY_INT_GT_ONE = 16;
     
    /** The Constant VALIDATION_TYPE_MANDATORY_JSON. */
    public static final int VALIDATION_TYPE_MANDATORY_JSON = 20;
     
    /** The Constant VALIDATION_TYPE_MANDATORY_PAYMENT_CHANNEL. */
    public static final int VALIDATION_TYPE_MANDATORY_PAYMENT_CHANNEL = 30;
     
    /** The Constant VALIDATION_TYPE_OPTIONAL_DOUBLE_GT_ZERO. */
    public static final int VALIDATION_TYPE_OPTIONAL_DOUBLE_GT_ZERO = 102;
     
    /** The Constant VALIDATION_TYPE_OPTIONAL_DOUBLE_GE_ZERO. */
    public static final int VALIDATION_TYPE_OPTIONAL_DOUBLE_GE_ZERO = 103;
     
    /** The Constant VALIDATION_TYPE_OPTIONAL_TEL. */
    public static final int VALIDATION_TYPE_OPTIONAL_TEL = 105;
     
    /** The Constant VALIDATION_TYPE_OPTIONAL_URL. */
    public static final int VALIDATION_TYPE_OPTIONAL_URL = 110;
     
    /** The Constant VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO. */
    public static final int VALIDATION_TYPE_OPTIONAL_INT_GE_ZERO = 115;
     
    /** The Constant VALIDATION_TYPE_OPTIONAL_INT_GT_ONE. */
    public static final int VALIDATION_TYPE_OPTIONAL_INT_GT_ONE = 116;
     
    /** The Constant VALIDATION_TYPE_OPTIONAL_JSON. */
    public static final int VALIDATION_TYPE_OPTIONAL_JSON = 120;
     
    /** The Constant VALIDATION_TYPE_OPTIONAL_PAYMENT_CHANNEL. */
    public static final int VALIDATION_TYPE_OPTIONAL_PAYMENT_CHANNEL = 130;
    
    /** The Constant VALIDATION_TYPE_MANDATORY_MESSAGE. */
    public static final int VALIDATION_TYPE_MANDATORY_MESSAGE = 135;
    
    /** The Constant VALIDATION_TYPE_MANDATORY_CURRENCY. */
    public static final int VALIDATION_TYPE_MANDATORY_CURRENCY = 136;
    
    /** The Constant VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID. */
    public static final int VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID = 137;

    /** The Constant VALIDATION_TYPE_MANDATORY_NUMBER. */
    public static final int VALIDATION_TYPE_MANDATORY_NUMBER = 138;
    
    /** The Constant VALIDATION_TYPE_MANDATORY_LOC_ACCURACY. */
    public static final int VALIDATION_TYPE_MANDATORY_LOC_ACCURACY = 140;
     
    /** The validation type. */
    int validationType = VALIDATION_TYPE_OPTIONAL;
     
    /** The parameter name. */
    String parameterName = null;
     
    /** The parameter value. */
    Object parameterValue = null;
     
    /** The specific value. */
    String specificValue = null;

     
    /**
     * Instantiates a new validation rule.
     *
     * @param validationType the validation type
     * @param parameterName the parameter name
     * @param parameterValue the parameter value
     */
    public ValidationRule(int validationType, String parameterName, Object parameterValue) {
        this.validationType = validationType;
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
    }

     
    /**
     * Instantiates a new validation rule.
     *
     * @param validationType the validation type
     * @param parameterName the parameter name
     * @param parameterValue the parameter value
     * @param specificValue the specific value
     */
    public ValidationRule(int validationType, String parameterName, Object parameterValue, String specificValue) {
        this.validationType = validationType;
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
        this.specificValue = specificValue;
    }

     
    /**
     * Checks if is mandatory.
     *
     * @param type the type
     * @return true, if is mandatory
     */
    public static boolean isMandatory(int type) {
        boolean mandatory = false;
        switch (type) {
            case VALIDATION_TYPE_MANDATORY:
                mandatory = true;
                break;
            case VALIDATION_TYPE_MANDATORY_DOUBLE_GT_ZERO:
                mandatory = true;
                break;
            case VALIDATION_TYPE_MANDATORY_DOUBLE_GE_ZERO:
                mandatory = true;
                break;
            case VALIDATION_TYPE_MANDATORY_TEL:
                mandatory = true;
                break;
            case VALIDATION_TYPE_MANDATORY_URL:
                mandatory = true;
                break;
            case VALIDATION_TYPE_MANDATORY_INT_GE_ZERO:
                mandatory = true;
                break;
            case VALIDATION_TYPE_MANDATORY_INT_GT_ONE:
                mandatory = true;
                break;
            case VALIDATION_TYPE_MANDATORY_JSON:
                mandatory = true;
                break;
            case VALIDATION_TYPE_MANDATORY_PAYMENT_CHANNEL:
                mandatory = true;
                break;
            case VALIDATION_TYPE_MANDATORY_MESSAGE:
                mandatory = true;
                break;
            case VALIDATION_TYPE_MANDATORY_CURRENCY:
                mandatory = true;
                break;
            case VALIDATION_TYPE_MANDATORY_TEL_END_USER_ID:
                mandatory = true;
                break;
            case VALIDATION_TYPE_MANDATORY_NUMBER:
                mandatory = true;
                break;
        }

        return mandatory;
    }
}

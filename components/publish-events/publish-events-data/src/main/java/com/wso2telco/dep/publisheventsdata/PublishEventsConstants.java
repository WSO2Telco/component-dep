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
package com.wso2telco.dep.publisheventsdata;


// TODO: Auto-generated Javadoc
/**
 * The Class PublishEventsConstants.
 */
public class PublishEventsConstants {

    /** The Constant SPEND_LIMIT_DATA_STREAM_NAME. */
    public static final String SPEND_LIMIT_DATA_STREAM_NAME = "publish.events.spend.limit.data";
    
    /** The Constant SPEND_LIMIT_DATA_STREAM_VERSION. */
    public static final String SPEND_LIMIT_DATA_STREAM_VERSION = "1.0.0";
    
    /** The Constant CEP_SPEND_LIMIT_HANDLER_ENABLED_PROPERTY. */
    public static final String CEP_SPEND_LIMIT_HANDLER_ENABLED_PROPERTY = "com.wso2telco.events.spend.limit.handler.enabled";
    
    /** The Constant CEP_URL_PROPERTY. */
    public static final String CEP_URL_PROPERTY = "com.wso2telco.events.cep.url";
    
    /** The Constant CEP_USERNAME_PROPERTY. */
    public static final String CEP_USERNAME_PROPERTY = "com.wso2telco.events.cep.username";
    
    /** The Constant CEP_PASSWORD_PROPERTY. */
    public static final String CEP_PASSWORD_PROPERTY = "com.wso2telco.events.cep.password";

    /** The Constant MSISDN_HAZELCAST_MAP_NAME. */
    public static final String MSISDN_HAZELCAST_MAP_NAME = "msisdnSpendLimitMap";
    
    /** The Constant APPLICATION_HAZELCAST_MAP_NAME. */
    public static final String APPLICATION_HAZELCAST_MAP_NAME = "appSpendLimitMap";
    
    /** The Constant OPERATOR_HAZELCAST_MAP_NAME. */
    public static final String OPERATOR_HAZELCAST_MAP_NAME = "operatorSpendLimitMap";

    /** The Constant OPERATOR_ID. */
    public static final String OPERATOR_ID = "com.wso2telco.prop.operatorId";
    
    /** The Constant MSISDN. */
    public static final String MSISDN = "com.wso2telco.prop.msisdn";
    
    /** The Constant CHARGE_AMOUNT. */
    public static final String CHARGE_AMOUNT = "com.wso2telco.prop.chargeAmount";
    
    /** The Constant RESPONSE_CODE. */
    public static final String RESPONSE_CODE = "com.wso2telco.prop.responseCode";
    
    public static final String PAYMENT_TYPE = "com.wso2telco.prop.paymentType";
    
    public static final String GROUP_NAME ="com.wso2telco.prop.groupName";
    
    public static final String CURRENT_DATE_TIME="com.wso2telco.prop.curretntime";
    
}

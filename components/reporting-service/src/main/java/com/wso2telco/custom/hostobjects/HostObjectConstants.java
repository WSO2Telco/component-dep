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
package com.wso2telco.custom.hostobjects;


import org.wso2.carbon.apimgt.impl.APIConstants;

import javax.xml.namespace.QName;

// TODO: Auto-generated Javadoc
/**
 * The Class HostObjectConstants.
 */
public class HostObjectConstants {

    /** The Constant SB_RATE_CARD_LOCATION. */
    public static final String SB_RATE_CARD_LOCATION = APIConstants.API_APPLICATION_DATA_LOCATION + "/sb-rate-card.xml";
    
    /** The Constant NB_RATE_CARD_LOCATION. */
    public static final String NB_RATE_CARD_LOCATION = APIConstants.API_APPLICATION_DATA_LOCATION + "/nb-rate-card.xml";
    
    /** The Constant OPERATORS_ELEMENT. */
    public static final QName OPERATORS_ELEMENT = new QName("Operators");
    
    /** The Constant OPERATOR_ELEMENT. */
    public static final QName OPERATOR_ELEMENT = new QName("Operator");
    
    /** The Constant OPERATION_ELEMENT. */
    public static final QName OPERATION_ELEMENT = new QName("Operation");
    
    /** The Constant API_ELEMENT. */
    public static final QName API_ELEMENT = new QName("API");
    
    /** The Constant NAME_ATTRIBUTE. */
    public static final QName NAME_ATTRIBUTE = new QName("name");
    
    /** The Constant RATE_ELEMENT. */
    public static final QName RATE_ELEMENT = new QName("Rate");
    
    /** The Constant TIER_RATE_ELEMENT. */
    public static final QName TIER_RATE_ELEMENT = new QName("rate");
    
    /** The Constant TIER_NAME_ELEMENT. */
    public static final QName TIER_NAME_ELEMENT = new QName("name");
    
    /** The Constant RATE_NAME_ELEMENT. */
    public static final QName RATE_NAME_ELEMENT = new QName("Name");
    
    /** The Constant RATE_CURRENCY_ELEMENT. */
    public static final QName RATE_CURRENCY_ELEMENT = new QName("Currency");
    
    /** The Constant RATE_VALUE_ELEMENT. */
    public static final QName RATE_VALUE_ELEMENT = new QName("Value");
    
    /** The Constant TIER_VALUE_ELEMENT. */
    public static final QName TIER_VALUE_ELEMENT = new QName("value");
    
    /** The Constant RATE_TYPE_ELEMENT. */
    public static final QName RATE_TYPE_ELEMENT = new QName("Type");
    
    /** The Constant RATE_DEFAULT_ATTRIBUTE. */
    public static final QName RATE_DEFAULT_ATTRIBUTE = new QName("default");
    
    /** The Constant RATE_ATTRIBUTES_ELEMENT. */
    public static final QName RATE_ATTRIBUTES_ELEMENT = new QName("Attributes");
    
    /** The Constant RATE_USAGE_TIERS_ELEMENT. */
    public static final QName RATE_USAGE_TIERS_ELEMENT = new QName("UsageTiers");
    
    /** The Constant RATE_USAGE_TIER_ELEMENT. */
    public static final QName RATE_USAGE_TIER_ELEMENT = new QName("Tier");
    
    /** The Constant RATE_RANGE_MIN. */
    public static final QName RATE_RANGE_MIN = new QName("Min");
    
    /** The Constant RATE_RANGE_MAX. */
    public static final QName RATE_RANGE_MAX = new QName("Max");    
    
    /** The Constant RATE_SURCHARGE_ELEMENT. */
    public static final QName RATE_SURCHARGE_ELEMENT=new QName("Surcharge");
    
    /** The Constant RATE_ADS_ELEMENT. */
    public static final QName RATE_ADS_ELEMENT=new QName("Ads");
    
    /** The Constant RATE_RANGES_ELEMENT. */
    public static final QName RATE_RANGES_ELEMENT = new QName("Ranges");
    
    /** The Constant RATE_RANGE_ELEMENT. */
    public static final QName RATE_RANGE_ELEMENT = new QName("Range");
    
    /** The Constant RATE_CATEGORY_ELEMENT. */
    public static final QName RATE_CATEGORY_ELEMENT = new QName("Category");
    
    /** The Constant RATE_CATEGORY_BASE_ELEMENT. */
    public static final QName RATE_CATEGORY_BASE_ELEMENT = new QName("CategoryBase");
    
    /** The Constant RATE_SUB_CATEGORY_ELEMENT. */
    public static final QName RATE_SUB_CATEGORY_ELEMENT = new QName("SubCategory");
    
    /** The Constant RATE_TAXES_ELEMENT. */
    public static final QName RATE_TAXES_ELEMENT = new QName("Taxes");
    
    /** The Constant RATE_TAX_ELEMENT. */
    public static final QName RATE_TAX_ELEMENT = new QName("Tax");
    
    /** The Constant RATE_REFUND_ELEMENT. */
    public static final QName RATE_REFUND_ELEMENT= new QName("Refund");
    
    /** The Constant SUBSCRIPTION_OPCO_RATES_TABLE. */
    public static final String SUBSCRIPTION_OPCO_RATES_TABLE = "subscription_rates";
    
    /** The Constant SUBSCRIPTION_RATES_TABLE. */
    public static final String SUBSCRIPTION_RATES_TABLE = "subscription_rates_nb";
    
    /** The Constant SB_RESPONSE_SUMMARY_TABLE. */
    public static final String SB_RESPONSE_SUMMARY_TABLE = "SB_API_RESPONSE_SUMMARY";
    
    /** The Constant NB_RESPONSE_SUMMARY_TABLE. */
    public static final String NB_RESPONSE_SUMMARY_TABLE = "NB_API_RESPONSE_SUMMARY";
    
    /** The Constant RATE_COMMISSION. */
    public static final QName RATE_COMMISSION = new QName("Commission");
    
    /** The Constant RATE_SP_COMMISSION. */
    public static final QName RATE_SP_COMMISSION = new QName("Sp");
    
    /** The Constant RATE_ADS_COMMISSION. */
    public static final QName RATE_ADS_COMMISSION = new QName("Ads");
    
    /** The Constant RATE_OPCO_COMMISSION. */
    public static final QName RATE_OPCO_COMMISSION = new QName("Opco");
    
    /** The Constant API_OPERATION_TYPES_TABLE. */
    public static final String API_OPERATION_TYPES_TABLE = "api_operation_types";
    
    /** The Constant RATE_ATTRIBUTE_MAX_COUNT. */
    public static final QName RATE_ATTRIBUTE_MAX_COUNT = new QName("MaxCount");
    
    /** The Constant RATE_ATTRIBUTE_EXCESS_RATE. */
    public static final QName RATE_ATTRIBUTE_EXCESS_RATE = new QName("ExcessRate");
    
    /** The Constant RATE_ATTRIBUTE_DEFAULT_RATE. */
    public static final QName RATE_ATTRIBUTE_DEFAULT_RATE = new QName("DefaultRate");

    /** The Constant RATE_RANGE_FROM. */
    public static final QName RATE_RANGE_FROM = new QName("From");
    
    /** The Constant RATE_RANGE_TO. */
    public static final QName RATE_RANGE_TO = new QName("To");
    
    /** The Constant DATE_LAST_MONTH. */
    public static final String DATE_LAST_MONTH = "last month";
    
    /** The Constant DATE_LAST_YEAR. */
    public static final String DATE_LAST_YEAR = "last year";
    
    /** The Constant DATE_LAST_WEEK. */
    public static final String DATE_LAST_WEEK = "last week";
    
    /** The Constant DATE_LAST_DAY. */
    public static final String DATE_LAST_DAY = "last day";
    
    /** The Constant ALL_SUBSCRIBERS. */
    public static final String ALL_SUBSCRIBERS = "__ALL__";
    
    /** The Constant ALL_APIS. */
    public static final String ALL_APIS = "__ALL__";
    
    /** The Constant ALL_OPERATORS. */
    public static final String ALL_OPERATORS = "__ALL__";
    
    /** The Constant ALL_APPLICATIONS. */
    public static final int ALL_APPLICATIONS = 0;
	
}

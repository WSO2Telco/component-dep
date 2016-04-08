package com.wso2telco.custom.hostobjects;


import org.wso2.carbon.apimgt.impl.APIConstants;

import javax.xml.namespace.QName;

public class HostObjectConstants {

    public static final String SB_RATE_CARD_LOCATION = APIConstants.API_APPLICATION_DATA_LOCATION + "/sb-rate-card.xml";
    public static final String NB_RATE_CARD_LOCATION = APIConstants.API_APPLICATION_DATA_LOCATION + "/nb-rate-card.xml";
    public static final QName OPERATORS_ELEMENT = new QName("Operators");
    public static final QName OPERATOR_ELEMENT = new QName("Operator");
    public static final QName OPERATION_ELEMENT = new QName("Operation");
    public static final QName API_ELEMENT = new QName("API");
    public static final QName NAME_ATTRIBUTE = new QName("name");
    public static final QName RATE_ELEMENT = new QName("Rate");
    public static final QName TIER_RATE_ELEMENT = new QName("rate");
    public static final QName TIER_NAME_ELEMENT = new QName("name");
    public static final QName RATE_NAME_ELEMENT = new QName("Name");
    public static final QName RATE_CURRENCY_ELEMENT = new QName("Currency");
    public static final QName RATE_VALUE_ELEMENT = new QName("Value");
    public static final QName TIER_VALUE_ELEMENT = new QName("value");
    public static final QName RATE_TYPE_ELEMENT = new QName("Type");
    public static final QName RATE_DEFAULT_ATTRIBUTE = new QName("default");
    public static final QName RATE_ATTRIBUTES_ELEMENT = new QName("Attributes");
    public static final QName RATE_USAGE_TIERS_ELEMENT = new QName("UsageTiers");
    public static final QName RATE_USAGE_TIER_ELEMENT = new QName("Tier");
    public static final QName RATE_RANGE_MIN = new QName("Min");
    public static final QName RATE_RANGE_MAX = new QName("Max");    
    public static final QName RATE_SURCHARGE_ELEMENT=new QName("Surcharge");
    public static final QName RATE_ADS_ELEMENT=new QName("Ads");
    public static final QName RATE_RANGES_ELEMENT = new QName("Ranges");
    public static final QName RATE_RANGE_ELEMENT = new QName("Range");
    public static final QName RATE_CATEGORY_ELEMENT = new QName("Category");
    public static final QName RATE_CATEGORY_BASE_ELEMENT = new QName("CategoryBase");
    public static final QName RATE_SUB_CATEGORY_ELEMENT = new QName("SubCategory");
    public static final QName RATE_TAXES_ELEMENT = new QName("Taxes");
    public static final QName RATE_TAX_ELEMENT = new QName("Tax");
    public static final QName RATE_REFUND_ELEMENT= new QName("Refund");
    public static final String SUBSCRIPTION_OPCO_RATES_TABLE = "subscription_rates";
    public static final String SUBSCRIPTION_RATES_TABLE = "subscription_rates_nb";
    public static final String SB_RESPONSE_SUMMARY_TABLE = "SB_API_RESPONSE_SUMMARY";
    public static final String NB_RESPONSE_SUMMARY_TABLE = "NB_API_RESPONSE_SUMMARY";
    public static final QName RATE_COMMISSION = new QName("Commission");
    public static final QName RATE_SP_COMMISSION = new QName("Sp");
    public static final QName RATE_ADS_COMMISSION = new QName("Ads");
    public static final QName RATE_OPCO_COMMISSION = new QName("Opco");
    
    public static final String API_OPERATION_TYPES_TABLE = "api_operation_types";
    
    public static final QName RATE_ATTRIBUTE_MAX_COUNT = new QName("MaxCount");
    public static final QName RATE_ATTRIBUTE_EXCESS_RATE = new QName("ExcessRate");
    public static final QName RATE_ATTRIBUTE_DEFAULT_RATE = new QName("DefaultRate");

    public static final QName RATE_RANGE_FROM = new QName("From");
    public static final QName RATE_RANGE_TO = new QName("To");
    
    public static final String DATE_LAST_MONTH = "last month";
    public static final String DATE_LAST_YEAR = "last year";
    public static final String DATE_LAST_WEEK = "last week";
    public static final String DATE_LAST_DAY = "last day";
    public static final String ALL_SUBSCRIBERS = "__ALL__";
    public static final String ALL_APIS = "__ALL__";
    public static final String ALL_OPERATORS = "__ALL__";
    public static final int ALL_APPLICATIONS = 0;
	
}

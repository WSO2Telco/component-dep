package com.wso2telco.dep.reportingservice.util;

// TODO: Auto-generated Javadoc
/**
 * The Enum ReportingTable.
 */
public enum ReportingTable {

	/** The sb api response summary. */
	SB_API_RESPONSE_SUMMARY("SB_API_RESPONSE_SUMMARY"),
	
	/** The nb api response summary. */
	NB_API_RESPONSE_SUMMARY("NB_API_RESPONSE_SUMMARY"),
	
	/** The sb api request summary. */
	SB_API_REQUEST_SUMMARY("SB_API_REQUEST_SUMMARY"),
	
	/** The nb api request summary. */
	NB_API_REQUEST_SUMMARY("NB_API_REQUEST_SUMMARY"),	
	
	/** The am subscription. */
	AM_SUBSCRIPTION("AM_SUBSCRIPTION"),
	
	/** The am workflows. */
	AM_WORKFLOWS("AM_WORKFLOWS"),
	
	/** The am application key mapping. */
	AM_APPLICATION_KEY_MAPPING("AM_APPLICATION_KEY_MAPPING"),
	
	/** The am application. */
	AM_APPLICATION("AM_APPLICATION"),
	
	/** The api operation types. */
	API_OPERATION_TYPES("api_operation_types"),
	
	/** The am api. */
	AM_API("AM_API"),
	
	/** The operators. */
	OPERATORS("operators"),
	
	/** The operatorapps. */
	OPERATORAPPS("operatorapps"),
	
	/** The operatorendpoints. */
	OPERATORENDPOINTS("operatorendpoints"),
	
	/** The endpointapps. */
	ENDPOINTAPPS("endpointapps"),
	
	/** The sub approval operators. */
	SUB_APPROVAL_OPERATORS("sub_approval_operators"),
	
	/** The tax. */ 
	TAX("tax"),
	
	/** The subscription tax. */
	SUBSCRIPTION_TAX("subscription_tax"), 
	
	/** The api request summary. */
	API_REQUEST_SUMMARY("API_REQUEST_SUMMARY"), 
	
	/** The subscription rates. */
	SUBSCRIPTION_RATES("subscription_rates"),
	
	/** The subscription rates nb. */
	SUBSCRIPTION_RATES_NB("subscription_rates_nb"),
	
	/** The test db. */
	TEST_DB("TEST_DB"),
	
	/** The am subscriber. */
	AM_SUBSCRIBER("AM_SUBSCRIBER"),
	
	/** The subscriptioncount. */
	SUBSCRIPTIONCOUNT("SUBSCRIPTIONCOUNT");
	
	/**
	 * Instantiates a new reporting table.
	 *
	 * @param tObject the t object
	 */
	ReportingTable(String tObject) {
		this.tObject = tObject;
	}
	
	/**
	 * Gets the t object.
	 *
	 * @return the t object
	 */
	public String getTObject(){
		return this.tObject;
	}

	/** The t object. */
	String tObject;
}

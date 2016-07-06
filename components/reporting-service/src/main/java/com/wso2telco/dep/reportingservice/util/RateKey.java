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
package com.wso2telco.dep.reportingservice.util;


import java.util.Locale;

 
// TODO: Auto-generated Javadoc
/**
 * The Class RateKey.
 */
public class RateKey {

    /** The operator. */
    private final String operator;
    
    /** The api name. */
    private final String apiName;
    
    /** The rate name. */
    private final String rateName;
    
    /** The operation name. */
    private final String operationName;

    /**
     * Instantiates a new rate key.
     *
     * @param operator the operator
     * @param apiName the api name
     * @param rateName the rate name
     * @param operationName the operation name
     */
    public RateKey(String operator, String apiName, String rateName,String operationName) {
    	this.operator = operator;
        this.apiName = apiName;
        this.rateName = rateName;
        this.operationName=operationName;
	}

    
        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
    public int hashCode() {
        int result = operator != null ? operator.toLowerCase(Locale.ENGLISH).hashCode() : 0;
        result = 31 * result + (apiName != null ? apiName.toLowerCase(Locale.ENGLISH).hashCode() : 0);
        result = 31 * result + (rateName != null ? rateName.toLowerCase(Locale.ENGLISH).hashCode() : 0);
        result = 31 * result + (operationName != null ? operationName.toLowerCase(Locale.ENGLISH).hashCode() : 0);
        return result;
    }
    
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RateKey rateKey = (RateKey) o;

        if (apiName != null ? !apiName.equalsIgnoreCase(rateKey.apiName) : rateKey.apiName != null) return false;
        if (operator != null ? !operator.equalsIgnoreCase(rateKey.operator) : rateKey.operator != null) return false;
        if (rateName != null ? !rateName.equalsIgnoreCase(rateKey.rateName) : rateKey.rateName != null) return false;
        if (operationName != null ? !operationName.equalsIgnoreCase(rateKey.operationName) : rateKey.operationName != null) return false;
        return true;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "RateKey{" +
                "operator='" + operator + '\'' +
                ", apiName='" + apiName + '\'' +
                ", rateName='" + rateName + '\'' +
                ", operationName='" + operationName + '\'' +
                '}';
    }
}

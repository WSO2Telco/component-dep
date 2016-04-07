package com.wso2telco.custom.hostobjects.util;


import java.util.Locale;

/**
 * Class used as the key for RateCard map. Used to combine multiple keys such as operatorName, apiName,
 * rateName and set as the single key.
 * Uses case-insensitive equals() and hashCode() implementations to allow for case-insensitive lookup
 * through the rateCard map.
 */
public class RateKey {

    private final String operator;
    private final String apiName;
    private final String rateName;
    private final String operationName;

    public RateKey(String operator, String apiName, String rateName,String operationName) {
    	this.operator = operator;
        this.apiName = apiName;
        this.rateName = rateName;
        this.operationName=operationName;
	}

    
        @Override
    public int hashCode() {
        int result = operator != null ? operator.toLowerCase(Locale.ENGLISH).hashCode() : 0;
        result = 31 * result + (apiName != null ? apiName.toLowerCase(Locale.ENGLISH).hashCode() : 0);
        result = 31 * result + (rateName != null ? rateName.toLowerCase(Locale.ENGLISH).hashCode() : 0);
        result = 31 * result + (operationName != null ? operationName.toLowerCase(Locale.ENGLISH).hashCode() : 0);
        return result;
    }
    
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

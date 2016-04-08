package com.wso2telco.custom.hostobjects.util;

/**
 * The type of rate.
 */
public enum RateType {
    /**
     * A constant charge for the charging period.
     */
    CONSTANT("CONSTANT"),
    /**
     * A percentage of the amount applied as the charge. Used for Payment API.
     */
    PERCENTAGE("PERCENTAGE"),
    /**
     * Rate applied per request.
     */
    PER_REQUEST("PER_REQUEST"),
    /**
     * A constant charge upto the quota value and a different rate for the excess count.
     */
    QUOTA("QUOTA"),
    /**
     * Charged based on the no. of unique subscribers for the API. eg: LBS
     */
    SUBSCRIPTION("SUBSCRIPTION"),
    /**
     * Charged based on the range. eg: 5000-600, 6001-7000
     */
    RANGE("RANGE"),
    /**
     * Charged based on the min-max. eg: 5000-600, 6001-7000
     */
    MULTITIER("MULTITIER");

    private String name;

    RateType(String name) { this.name = name; }

    public String getName() { return name; }

    public static RateType getEnum(String name) {
        for (RateType r : RateType.values()) {
            if ((r.name).equalsIgnoreCase(name)) {
                return r;
            }
        }
        return null;
    }
}

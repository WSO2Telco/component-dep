package com.wso2telco.dep.oneapivalidation.util;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * MsisdnDTO class to store the prefix and digits of the end user id separately.
 * A custom .equals and .hasCode method has been implemented so that we can ignore the prefix when checking for duplicates.
 */

public class MsisdnDTO {
    private String prefix = null;
    private String digits = null;

    public MsisdnDTO(String prefix, String digits) {
        this.prefix = prefix;
        this.digits = digits;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getDigits() {
        return digits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        MsisdnDTO msisdn = (MsisdnDTO) o;

        return new EqualsBuilder()
                .append(digits, msisdn.digits)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(digits)
                .toHashCode();
    }

    public String toString() {
        return prefix + digits;
    }
}

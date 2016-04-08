package com.wso2telco.custom.hostobjects;

import java.math.BigDecimal;
import java.sql.Date;

public class Tax {
    private String type;
    private Date effective_from;
    private Date effective_to;

    /**
     * tax percentage in decimal form. eg: 0.25 for 25% tax
     */
    private BigDecimal value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getEffective_from() {
        return effective_from;
    }

    public void setEffective_from(Date effective_from) {
        this.effective_from = effective_from;
    }

    public Date getEffective_to() {
        return effective_to;
    }

    public void setEffective_to(Date effective_to) {
        this.effective_to = effective_to;
    }

    /**
     * Get tax value which is in decimal form.
     * @return value
     */
    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}

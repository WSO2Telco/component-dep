/*
 * BilledCharge.java
 * May 18, 2015  12:10:17 PM
 * Roshan.Saputhanthri
 *
 * Copyright (C) Dialog Axiata PLC. All Rights Reserved.
 */

package com.wso2telco.custom.hostobjects.southbound;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class BilledCharge {

    private int count;
    
    private BigDecimal price = BigDecimal.ZERO;
    private BigDecimal spcom = BigDecimal.ZERO;
    private BigDecimal adscom = BigDecimal.ZERO;
    private BigDecimal opcom = BigDecimal.ZERO;
    private BigDecimal tax = BigDecimal.ZERO;

    Map<String,BilledCharge> tierCharges = new HashMap<String,BilledCharge>();

    public BilledCharge(int count) {
        this.count = count;
    }   
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSpcom() {
        return spcom;
    }

    public void setSpcom(BigDecimal spcom) {
        this.spcom = spcom;
    }

    public BigDecimal getAdscom() {
        return adscom;
    }

    public void setAdscom(BigDecimal adscom) {
        this.adscom = adscom;
    }

    public BigDecimal getOpcom() {
        return opcom;
    }

    public void setOpcom(BigDecimal opcom) {
        this.opcom = opcom;
    }  

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }
    
    public void addAdscom(BigDecimal adscom) {
        this.adscom = this.adscom.add(adscom);
    }
    
    public void addOpcom(BigDecimal opcom) {
        this.opcom = this.opcom.add(opcom);
    }
    
    public void addSpcom(BigDecimal spcom) {
        this.spcom = this.spcom.add(spcom);
    }
    
    public void addPrice(BigDecimal price) {
        this.price = this.price.add(price);
    }
    
    public void addTax(BigDecimal tax) {
        this.tax = this.tax.add(tax);
    }
    
    public void addCount(int count) {
        this.count = this.count + count;
    }
    
    public Map<String, BilledCharge> getTierCharges() {
            return tierCharges;
        }

    public void setTierCharges(Map<String, BilledCharge> tierCharges) {
            this.tierCharges = tierCharges;
    }
    
}

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
package com.wso2telco.dep.reportingservice.southbound;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class BilledCharge.
 */
public class BilledCharge {

    /** The count. */
    private int count;
    
    /** The price. */
    private BigDecimal price = BigDecimal.ZERO;
    
    /** The spcom. */
    private BigDecimal spcom = BigDecimal.ZERO;
    
    /** The adscom. */
    private BigDecimal adscom = BigDecimal.ZERO;
    
    /** The opcom. */
    private BigDecimal opcom = BigDecimal.ZERO;
    
    /** The tax. */
    private BigDecimal tax = BigDecimal.ZERO;

    /** The tier charges. */
    Map<String,BilledCharge> tierCharges = new HashMap<String,BilledCharge>();

    /**
     * Instantiates a new billed charge.
     *
     * @param count the count
     */
    public BilledCharge(int count) {
        this.count = count;
    }   
    
    /**
     * Gets the count.
     *
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets the count.
     *
     * @param count the new count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Gets the price.
     *
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Sets the price.
     *
     * @param price the new price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * Gets the spcom.
     *
     * @return the spcom
     */
    public BigDecimal getSpcom() {
        return spcom;
    }

    /**
     * Sets the spcom.
     *
     * @param spcom the new spcom
     */
    public void setSpcom(BigDecimal spcom) {
        this.spcom = spcom;
    }

    /**
     * Gets the adscom.
     *
     * @return the adscom
     */
    public BigDecimal getAdscom() {
        return adscom;
    }

    /**
     * Sets the adscom.
     *
     * @param adscom the new adscom
     */
    public void setAdscom(BigDecimal adscom) {
        this.adscom = adscom;
    }

    /**
     * Gets the opcom.
     *
     * @return the opcom
     */
    public BigDecimal getOpcom() {
        return opcom;
    }

    /**
     * Sets the opcom.
     *
     * @param opcom the new opcom
     */
    public void setOpcom(BigDecimal opcom) {
        this.opcom = opcom;
    }  

    /**
     * Gets the tax.
     *
     * @return the tax
     */
    public BigDecimal getTax() {
        return tax;
    }

    /**
     * Sets the tax.
     *
     * @param tax the new tax
     */
    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }
    
    /**
     * Adds the adscom.
     *
     * @param adscom the adscom
     */
    public void addAdscom(BigDecimal adscom) {
        this.adscom = this.adscom.add(adscom);
    }
    
    /**
     * Adds the opcom.
     *
     * @param opcom the opcom
     */
    public void addOpcom(BigDecimal opcom) {
        this.opcom = this.opcom.add(opcom);
    }
    
    /**
     * Adds the spcom.
     *
     * @param spcom the spcom
     */
    public void addSpcom(BigDecimal spcom) {
        this.spcom = this.spcom.add(spcom);
    }
    
    /**
     * Adds the price.
     *
     * @param price the price
     */
    public void addPrice(BigDecimal price) {
        this.price = this.price.add(price);
    }
    
    /**
     * Adds the tax.
     *
     * @param tax the tax
     */
    public void addTax(BigDecimal tax) {
        this.tax = this.tax.add(tax);
    }
    
    /**
     * Adds the count.
     *
     * @param count the count
     */
    public void addCount(int count) {
        this.count = this.count + count;
    }
    
    /**
     * Gets the tier charges.
     *
     * @return the tier charges
     */
    public Map<String, BilledCharge> getTierCharges() {
            return tierCharges;
        }

    /**
     * Sets the tier charges.
     *
     * @param tierCharges the tier charges
     */
    public void setTierCharges(Map<String, BilledCharge> tierCharges) {
            this.tierCharges = tierCharges;
    }
    
}

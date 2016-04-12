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

import org.wso2.carbon.apimgt.api.model.APIIdentifier;
import org.wso2.carbon.apimgt.api.model.SubscribedAPI;
import org.wso2.carbon.apimgt.api.model.Subscriber;
import com.wso2telco.custom.hostobjects.southbound.BilledCharge;
import com.wso2telco.custom.hostobjects.southbound.CategoryCharge;
import com.wso2telco.custom.hostobjects.util.ChargeRate;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

// TODO: Auto-generated Javadoc
/**
 * The Class BillingSubscription.
 */
public class BillingSubscription extends SubscribedAPI {

    /** The context. */
    private String context;
    
    /** The month. */
    private String month;
    
    /** The year. */
    private String year;
    
    /** The operator subscription list. */
    private List<OperatorSubscription> operatorSubscriptionList;
    
    /** The api id int. */
    private int apiIdInt;

    /**
     * Gets the month.
     *
     * @return the month
     */
    public String getMonth() {
        return month;
    }

    /**
     * Sets the month.
     *
     * @param month the new month
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * Gets the year.
     *
     * @return the year
     */
    public String getYear() {
        return year;
    }

    /**
     * Sets the year.
     *
     * @param year the new year
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * Instantiates a new billing subscription.
     *
     * @param subscribedAPI the subscribed api
     */
    public BillingSubscription(SubscribedAPI subscribedAPI) {
        super(subscribedAPI.getSubscriber(), subscribedAPI.getApiId());
        super.setApplication(subscribedAPI.getApplication());
        super.setBlocked(subscribedAPI.isBlocked());
        super.setTier(subscribedAPI.getTier());
        super.setSubStatus(subscribedAPI.getSubStatus());
    }

    /**
     * Instantiates a new billing subscription.
     *
     * @param subscriber the subscriber
     * @param apiId the api id
     */
    public BillingSubscription(Subscriber subscriber, APIIdentifier apiId) {
        super(subscriber, apiId);
    }

    /**
     * Gets the context.
     *
     * @return the context
     */
    public String getContext() {
        return context;
    }

    /**
     * Sets the context.
     *
     * @param context the new context
     */
    public void setContext(String context) {
        this.context = context;
    }

    /**
     * Gets the operator subscription list.
     *
     * @return the operator subscription list
     */
    public List<OperatorSubscription> getOperatorSubscriptionList() {
        return operatorSubscriptionList;
    }

    /**
     * Sets the operator subscription list.
     *
     * @param operatorSubscriptionList the new operator subscription list
     */
    public void setOperatorSubscriptionList(List<OperatorSubscription> operatorSubscriptionList) {
        this.operatorSubscriptionList = operatorSubscriptionList;
    }

    /**
     * Gets the api id int.
     *
     * @return the api id int
     */
    public int getApiIdInt() { return apiIdInt; }

    /**
     * Sets the api id int.
     *
     * @param apiIdInt the new api id int
     */
    public void setApiIdInt(int apiIdInt) { this.apiIdInt = apiIdInt; }

    /* (non-Javadoc)
     * @see org.wso2.carbon.apimgt.api.model.SubscribedAPI#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    /* (non-Javadoc)
     * @see org.wso2.carbon.apimgt.api.model.SubscribedAPI#hashCode()
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * The Class OperatorSubscription.
     */
    public static class OperatorSubscription {

        /** The operation id. */
        private int operationId;

        /**
         * Gets the operation id.
         *
         * @return the operation id
         */
        public int getOperationId() {
            return operationId;
        }

        /**
         * Sets the operation id.
         *
         * @param operationId the new operation id
         */
        public void setOperationId(int operationId) {
            this.operationId = operationId;
        }

        /** The category charges. */
        Map<CategoryCharge, BilledCharge> categoryCharges;
        
        /** The merchant charges. */
        Map<String,BilledCharge> merchantCharges = new HashMap<String,BilledCharge>();
        
        /** The operator. */
        private String operator;
        
        /** The rate. */
        private ChargeRate rate;
        
        /** The count. */
        private int count;
         
        /** The price. */
        private BigDecimal price = BigDecimal.ZERO;
        
        /** The tax value. */
        private BigDecimal taxValue = BigDecimal.ZERO;
        
        /** The spcom. */
        private BigDecimal spcom = BigDecimal.ZERO;
        
        /** The adscom. */
        private BigDecimal adscom = BigDecimal.ZERO;
        
        /** The opcom. */
        private BigDecimal opcom = BigDecimal.ZERO;
        
        /**
         * Instantiates a new operator subscription.
         *
         * @param operator the operator
         */
        public OperatorSubscription(String operator) {
            this.operator = operator;
        }

        /**
         * Gets the operator.
         *
         * @return the operator
         */
        public String getOperator() { return operator; }

        /**
         * Gets the rate.
         *
         * @return the rate
         */
        public ChargeRate getRate() { return rate; }

        /**
         * Sets the rate.
         *
         * @param rate the new rate
         */
        public void setRate(ChargeRate rate) { this.rate = rate; }

        /**
         * Gets the count.
         *
         * @return the count
         */
        public int getCount() { return count; }

        /**
         * Sets the count.
         *
         * @param count the new count
         */
        public void setCount(int count) { this.count = count; }        

        /**
         * Gets the price.
         *
         * @return the price
         */
        public BigDecimal getPrice() { return price; }

        /**
         * Sets the price.
         *
         * @param price the new price
         */
        public void setPrice(BigDecimal price) { this.price = price; }
        
        /**
         * Adds the price.
         *
         * @param price the price
         */
        public void addPrice(BigDecimal price) { this.price = this.price.add(price); }
        
        /**
         * Adds the count.
         *
         * @param count the count
         */
        public void addCount(int count) { this.count = this.count + count; }
        
        /**
         * Gets the tax value.
         *
         * @return the tax value
         */
        public BigDecimal getTaxValue() { return taxValue; }

        /**
         * Sets the tax value.
         *
         * @param taxValue the new tax value
         */
        public void setTaxValue(BigDecimal taxValue) { this.taxValue = taxValue; }

        /**
         * Gets the merchant charges.
         *
         * @return the merchant charges
         */
        public Map<String, BilledCharge> getMerchantCharges() {
            return merchantCharges;
        }

        /**
         * Sets the merchant charges.
         *
         * @param merchantCharges the merchant charges
         */
        public void setMerchantCharges(Map<String, BilledCharge> merchantCharges) {
            this.merchantCharges = merchantCharges;
        }

        /**
         * Gets the category charges.
         *
         * @return the category charges
         */
        public Map<CategoryCharge, BilledCharge> getCategoryCharges() {
            return categoryCharges;
        }

        /**
         * Sets the category charges.
         *
         * @param categoryCharges the category charges
         */
        public void setCategoryCharges(Map<CategoryCharge, BilledCharge> categoryCharges) {
            this.categoryCharges = categoryCharges;
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
         * Adds the adscom.
         *
         * @param price the price
         */
        public void addAdscom(BigDecimal price) { this.adscom = this.adscom.add(price); }
        
        /**
         * Adds the opcom.
         *
         * @param price the price
         */
        public void addOpcom(BigDecimal price) { this.opcom = this.opcom.add(price); }
        
        /**
         * Adds the spcom.
         *
         * @param price the price
         */
        public void addSpcom(BigDecimal price) { this.spcom = this.spcom.add(price); }
        
        /**
         * Adds the tax value.
         *
         * @param price the price
         */
        public void addTaxValue(BigDecimal price) { this.taxValue = this.taxValue.add(price); }
        
    }
}

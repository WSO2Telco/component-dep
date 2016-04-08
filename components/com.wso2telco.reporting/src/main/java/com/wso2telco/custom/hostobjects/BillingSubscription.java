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

public class BillingSubscription extends SubscribedAPI {

    private String context;
    private String month;
    private String year;
    private List<OperatorSubscription> operatorSubscriptionList;
    private int apiIdInt;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public BillingSubscription(SubscribedAPI subscribedAPI) {
        super(subscribedAPI.getSubscriber(), subscribedAPI.getApiId());
        super.setApplication(subscribedAPI.getApplication());
        super.setBlocked(subscribedAPI.isBlocked());
        super.setTier(subscribedAPI.getTier());
        super.setSubStatus(subscribedAPI.getSubStatus());
    }

    public BillingSubscription(Subscriber subscriber, APIIdentifier apiId) {
        super(subscriber, apiId);
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public List<OperatorSubscription> getOperatorSubscriptionList() {
        return operatorSubscriptionList;
    }

    public void setOperatorSubscriptionList(List<OperatorSubscription> operatorSubscriptionList) {
        this.operatorSubscriptionList = operatorSubscriptionList;
    }

    public int getApiIdInt() { return apiIdInt; }

    public void setApiIdInt(int apiIdInt) { this.apiIdInt = apiIdInt; }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public static class OperatorSubscription {

        private int operationId;

        public int getOperationId() {
            return operationId;
        }

        public void setOperationId(int operationId) {
            this.operationId = operationId;
        }

        Map<CategoryCharge, BilledCharge> categoryCharges;
        Map<String,BilledCharge> merchantCharges = new HashMap<String,BilledCharge>();
        
        private String operator;
        private ChargeRate rate;
        private int count;
        /**
        * The charge before tax
        */
        private BigDecimal price = BigDecimal.ZERO;
        private BigDecimal taxValue = BigDecimal.ZERO;
        
        private BigDecimal spcom = BigDecimal.ZERO;
        private BigDecimal adscom = BigDecimal.ZERO;
        private BigDecimal opcom = BigDecimal.ZERO;
        
        public OperatorSubscription(String operator) {
            this.operator = operator;
        }

        public String getOperator() { return operator; }

        public ChargeRate getRate() { return rate; }

        public void setRate(ChargeRate rate) { this.rate = rate; }

        public int getCount() { return count; }

        public void setCount(int count) { this.count = count; }        

        public BigDecimal getPrice() { return price; }

        public void setPrice(BigDecimal price) { this.price = price; }
        public void addPrice(BigDecimal price) { this.price = this.price.add(price); }
        
        public void addCount(int count) { this.count = this.count + count; }
        
        public BigDecimal getTaxValue() { return taxValue; }

        public void setTaxValue(BigDecimal taxValue) { this.taxValue = taxValue; }

        public Map<String, BilledCharge> getMerchantCharges() {
            return merchantCharges;
        }

        public void setMerchantCharges(Map<String, BilledCharge> merchantCharges) {
            this.merchantCharges = merchantCharges;
        }

        public Map<CategoryCharge, BilledCharge> getCategoryCharges() {
            return categoryCharges;
        }

        public void setCategoryCharges(Map<CategoryCharge, BilledCharge> categoryCharges) {
            this.categoryCharges = categoryCharges;
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
        
        public void addAdscom(BigDecimal price) { this.adscom = this.adscom.add(price); }
        public void addOpcom(BigDecimal price) { this.opcom = this.opcom.add(price); }
        public void addSpcom(BigDecimal price) { this.spcom = this.spcom.add(price); }
        public void addTaxValue(BigDecimal price) { this.taxValue = this.taxValue.add(price); }
        
    }
}

package com.wso2telco.workflow.model;

import javax.xml.bind.annotation.*;

public class SubTierUpdtReq extends BaseTierUpdtReq{

    private String apiId;
    private String subscriptionId;
    private SubscriptionStatusEnum status;
    private String requestedThrottlingPolicy;

    @XmlType(name="SubscriptionStatusEnum")
    @XmlEnum(String.class)
    public enum SubscriptionStatusEnum {

        @XmlEnumValue("BLOCKED") BLOCKED(String.valueOf("BLOCKED")), @XmlEnumValue("PROD_ONLY_BLOCKED") PROD_ONLY_BLOCKED(String.valueOf("PROD_ONLY_BLOCKED")), @XmlEnumValue("UNBLOCKED") UNBLOCKED(String.valueOf("UNBLOCKED")), @XmlEnumValue("ON_HOLD") ON_HOLD(String.valueOf("ON_HOLD")), @XmlEnumValue("REJECTED") REJECTED(String.valueOf("REJECTED"));


        private String value;

        SubscriptionStatusEnum (String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static SubscriptionStatusEnum fromValue(String v) {
            for (SubscriptionStatusEnum b : SubscriptionStatusEnum.values()) {
                if (String.valueOf(b.value).equals(v)) {
                    return b;
                }
            }
            return null;
        }
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public SubscriptionStatusEnum getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatusEnum status) {
        this.status = status;
    }

    public String getRequestedThrottlingPolicy() {
        return requestedThrottlingPolicy;
    }

    public void setRequestedThrottlingPolicy(String requestedThrottlingPolicy) {
        this.requestedThrottlingPolicy = requestedThrottlingPolicy;
    }
}

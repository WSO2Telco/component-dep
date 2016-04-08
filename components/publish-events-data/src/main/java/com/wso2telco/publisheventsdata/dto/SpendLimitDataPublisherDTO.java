package com.wso2telco.publisheventsdata.dto;


import org.wso2.carbon.apimgt.usage.publisher.dto.ResponsePublisherDTO;

import com.wso2telco.publisheventsdata.MifeEventsConstants;

public class SpendLimitDataPublisherDTO extends ResponsePublisherDTO {

    private String operatorId;
    private String responseCode;
    private String msisdn;
    private double chargeAmount;

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }
    
    public double getChargeAmount() { return chargeAmount; }

    public void setChargeAmount(double chargeAmount) { this.chargeAmount = chargeAmount; }

    public static String getStreamDefinition() {
        String streamDefinition = "{" +
                "  'name':'" + MifeEventsConstants .MIFE_SPEND_LIMIT_DATA_STREAM_NAME + "'," +
                "  'version':'" + MifeEventsConstants.MIFE_SPEND_LIMIT_DATA_STREAM_VERSION + "'," +
                "  'nickName': 'MIFE Spend Limit Data'," +
                "  'description': 'MIFE Spend Limit Data'," +
                "  'metaData':[" +
                "          {'name':'clientType','type':'STRING'}" +
                "  ]," +
                "  'payloadData':[" +
                "          {'name':'consumerKey','type':'STRING'}," +
                "          {'name':'context','type':'STRING'}," +
                "          {'name':'api','type':'STRING'}," +
                "          {'name':'version','type':'STRING'}," +
                "          {'name':'responseTime','type':'LONG'}," +
                "          {'name':'userId','type':'STRING'}," +
                "          {'name':'tenantDomain','type':'STRING'}," +
                "          {'name':'applicationName','type':'STRING'}," +
                "          {'name':'applicationId','type':'STRING'}," +

                "          {'name':'operatorId','type':'STRING'}," +
                "          {'name':'responseCode','type':'STRING'}," +
                "          {'name':'msisdn','type':'STRING'}," +
                "          {'name':'chargeAmount','type':'DOUBLE'}" +
                "  ]" +
                "}";

        return streamDefinition;
    }

    public Object createPayload(){
        return new Object[]{getConsumerKey(),getContext(),getApi(),getVersion(),getResponseTime(),getUsername(),
                            getTenantDomain(),getApplicationName(), getApplicationId(),

                            getOperatorId(), getResponseCode(), getMsisdn(), getChargeAmount()};
    }
}

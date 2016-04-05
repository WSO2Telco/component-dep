package com.wso2telco.datapublisher.dto;

import org.wso2.carbon.apimgt.usage.publisher.dto.RequestPublisherDTO;

import com.wso2telco.datapublisher.SouthboundPublisherConstants;

public class SouthboundRequestPublisherDTO extends RequestPublisherDTO {

    private String requestId;
    private String operatorId;
    private String sbEndpoint;
    private String chargeAmount;
    private String jsonBody;
    private String purchaseCategoryCode;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getSbEndpoint() {
        return sbEndpoint;
    }

    public void setSbEndpoint(String sbEndpoint) {
        this.sbEndpoint = sbEndpoint;
    }

    public String getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(String chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public String getJsonBody() {
        return jsonBody;
    }

    public void setJsonBody(String jsonBody) {
        this.jsonBody = jsonBody;
    }

    public String getPurchaseCategoryCode() {
        return purchaseCategoryCode;
    }

    public void setPurchaseCategoryCode(String purchaseCategoryCode) {
        this.purchaseCategoryCode = purchaseCategoryCode;
    }

    

    public static String getStreamDefinition() {
        String streamDefinition = "{" +
                "  'name':'" + SouthboundPublisherConstants.SOUTHBOUND_REQUEST_STREAM_NAME + "'," +
                "  'version':'" + SouthboundPublisherConstants.SOUTHBOUND_REQUEST_STREAM_VERSION + "'," +
                "  'nickName': 'Southbound Request Data'," +
                "  'description': 'Southbound Request Data'," +
                "  'metaData':[" +
                "          {'name':'clientType','type':'STRING'}" +
                "  ]," +
                "  'payloadData':[" +
                "          {'name':'consumerKey','type':'STRING'}," +
                "          {'name':'context','type':'STRING'}," +
                "          {'name':'api_version','type':'STRING'}," +
                "          {'name':'api','type':'STRING'}," +
                "          {'name':'resourcePath','type':'STRING'}," +
                "          {'name':'method','type':'STRING'}," +
                "          {'name':'version','type':'STRING'}," +
                "          {'name':'request','type':'INT'}," +
                "          {'name':'requestTime','type':'LONG'}," +
                "          {'name':'userId','type':'STRING'}," +
                "          {'name':'tenantDomain','type':'STRING'}," +
                "          {'name':'hostName','type':'STRING'}," +
                "          {'name':'apiPublisher','type':'STRING'}," +
                "          {'name':'applicationName','type':'STRING'}," +
                "          {'name':'applicationId','type':'STRING'}," +

                "          {'name':'requestId','type':'STRING'}," +
                "          {'name':'operatorId','type':'STRING'}," +
                "          {'name':'sbEndpoint','type':'STRING'}," +
                "          {'name':'chargeAmount','type':'STRING'}," +
                "          {'name':'purchaseCategoryCode','type':'STRING'}," +
                "          {'name':'jsonBody','type':'STRING'}" +
                "  ]" +
                "}";

        return streamDefinition;
    }

    public Object createPayload(){
        return new Object[]{getConsumerKey(),getContext(),getApi_version(),getApi(), getResourcePath(),getMethod(),
                            getVersion(), getRequestCount(),getRequestTime(),getUsername(),getTenantDomain(),getHostName(),
                            getApiPublisher(), getApplicationName(), getApplicationId(),

                            getRequestId(), getOperatorId(), getSbEndpoint(), getChargeAmount(), getPurchaseCategoryCode(), getJsonBody()};

    }
}

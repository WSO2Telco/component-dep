package com.wso2telco.datapublisher.dto;

import org.wso2.carbon.apimgt.usage.publisher.dto.ResponsePublisherDTO;

import com.wso2telco.datapublisher.SouthboundPublisherConstants;

public class SouthboundResponsePublisherDTO extends ResponsePublisherDTO {

    private String requestId;
    private String operatorId;
    private String responseCode;
    private String msisdn;
    private String chargeAmount;
    private String operatorRef;
    private String exceptionId;
    private String exceptionMessage;
    private String purchaseCategoryCode;
    private String jsonBody;
    private Integer operationType;
    private String merchantId;
    private String category;
    private String subCategory;
    private int response;

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

    public String getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(String chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public String getOperatorRef() {
        return operatorRef;
    }

    public void setOperatorRef(String operatorRef) {
        this.operatorRef = operatorRef;
    }

    public String getExceptionId() {
        return exceptionId;
    }

    public void setExceptionId(String exceptionId) {
        this.exceptionId = exceptionId;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
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

    public Integer getOperationType() {
        return operationType;
    }

    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    @Override
    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public static String getStreamDefinition() {
        String streamDefinition = "{"
                + "  'name':'" + SouthboundPublisherConstants.SOUTHBOUND_RESPONSE_STREAM_NAME + "',"
                + "  'version':'" + SouthboundPublisherConstants.SOUTHBOUND_RESPONSE_STREAM_VERSION + "',"
                + "  'nickName': 'Southbound Response Data',"
                + "  'description': 'Southbound Response Data',"
                + "  'metaData':["
                + "          {'name':'clientType','type':'STRING'}"
                + "  ],"
                + "  'payloadData':["
                + "          {'name':'consumerKey','type':'STRING'},"
                + "          {'name':'context','type':'STRING'},"
                + "          {'name':'api_version','type':'STRING'},"
                + "          {'name':'api','type':'STRING'},"
                + "          {'name':'resourcePath','type':'STRING'},"
                + "          {'name':'method','type':'STRING'},"
                + "          {'name':'version','type':'STRING'},"
                + "          {'name':'response','type':'INT'},"
                + "          {'name':'responseTime','type':'LONG'},"
                + "          {'name':'serviceTime','type':'LONG'},"
                + "          {'name':'userId','type':'STRING'},"
                + "          {'name':'tenantDomain','type':'STRING'},"
                + "          {'name':'hostName','type':'STRING'},"
                + "          {'name':'apiPublisher','type':'STRING'},"
                + "          {'name':'applicationName','type':'STRING'},"
                + "          {'name':'applicationId','type':'STRING'},"
                + "          {'name':'requestId','type':'STRING'},"
                + "          {'name':'operatorId','type':'STRING'},"
                + "          {'name':'responseCode','type':'STRING'},"
                + "          {'name':'msisdn','type':'STRING'},"
                + "          {'name':'chargeAmount','type':'STRING'},"
                + "          {'name':'purchaseCategoryCode','type':'STRING'},"
                + "          {'name':'operatorRef','type':'STRING'},"
                + "          {'name':'exceptionId','type':'STRING'},"
                + "          {'name':'exceptionMessage','type':'STRING'},"
                + "          {'name':'jsonBody','type':'STRING'},"
                + "          {'name':'operationType','type':'INT'},"
                + "          {'name':'merchantId','type':'STRING'},"
                + "          {'name':'category','type':'STRING'},"
                + "          {'name':'subCategory','type':'STRING'}"
                + "  ]"
                + "}";

        return streamDefinition;
    }

    public Object createPayload() {
        return new Object[]{getConsumerKey(), getContext(), getApi_version(), getApi(), getResourcePath(), getMethod(),
            getVersion(), this.getResponse(), getResponseTime(), getServiceTime(), getUsername(), getTenantDomain(), getHostName(),
            getApiPublisher(), getApplicationName(), getApplicationId(),
            getRequestId(), getOperatorId(), getResponseCode(), getMsisdn(), getChargeAmount(), getPurchaseCategoryCode(),
            getOperatorRef(), getExceptionId(), getExceptionMessage(), getJsonBody(), getOperationType(), getMerchantId(), getCategory(), getSubCategory()};
    }
}

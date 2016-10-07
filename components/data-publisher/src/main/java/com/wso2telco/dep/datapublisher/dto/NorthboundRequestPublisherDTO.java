/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *  
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.dep.datapublisher.dto;

import org.wso2.carbon.apimgt.usage.publisher.dto.RequestPublisherDTO;

import com.wso2telco.dep.datapublisher.DataPublisherConstants;

// TODO: Auto-generated Javadoc
/**
 * The Class NorthboundRequestPublisherDTO.
 */
public class NorthboundRequestPublisherDTO extends RequestPublisherDTO {

    /** The request id. */
    private String requestId;
    
    /** The operator id. */
    private String operatorId;
    
    /** The sb endpoint. */
    private String sbEndpoint;
    
    /** The charge amount. */
    private String chargeAmount;
    
    /** The json body. */
    private String jsonBody;
    
    /** The purchase category code. */
    private String purchaseCategoryCode;

    /**
     * Gets the request id.
     *
     * @return the request id
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Sets the request id.
     *
     * @param requestId the new request id
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    /**
     * Gets the operator id.
     *
     * @return the operator id
     */
    public String getOperatorId() {
        return operatorId;
    }

    /**
     * Sets the operator id.
     *
     * @param operatorId the new operator id
     */
    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    /**
     * Gets the sb endpoint.
     *
     * @return the sb endpoint
     */
    public String getSbEndpoint() {
        return sbEndpoint;
    }

    /**
     * Sets the sb endpoint.
     *
     * @param sbEndpoint the new sb endpoint
     */
    public void setSbEndpoint(String sbEndpoint) {
        this.sbEndpoint = sbEndpoint;
    }

    /**
     * Gets the charge amount.
     *
     * @return the charge amount
     */
    public String getChargeAmount() {
        return chargeAmount;
    }

    /**
     * Sets the charge amount.
     *
     * @param chargeAmount the new charge amount
     */
    public void setChargeAmount(String chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    /**
     * Gets the json body.
     *
     * @return the json body
     */
    public String getJsonBody() {
        return jsonBody;
    }

    /**
     * Sets the json body.
     *
     * @param jsonBody the new json body
     */
    public void setJsonBody(String jsonBody) {
        this.jsonBody = jsonBody;
    }

    /**
     * Gets the purchase category code.
     *
     * @return the purchase category code
     */
    public String getPurchaseCategoryCode() {
        return purchaseCategoryCode;
    }

    /**
     * Sets the purchase category code.
     *
     * @param purchaseCategoryCode the new purchase category code
     */
    public void setPurchaseCategoryCode(String purchaseCategoryCode) {
        this.purchaseCategoryCode = purchaseCategoryCode;
    }
    

    /**
     * Gets the stream definition.
     *
     * @return the stream definition
     */
    public static String getStreamDefinition() {
        String streamDefinition = "{" +
                "  'name':'" + DataPublisherConstants.NORTHBOUND_REQUEST_STREAM_NAME + "'," +
                "  'version':'" + DataPublisherConstants.NORTHBOUND_REQUEST_STREAM_VERSION + "'," +
                "  'nickName': 'Northbound Request Data'," +
                "  'description': 'Northbound Request Data'," +
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
                "          {'name':'sbEndpoint','type':'STRING'}," +
                "          {'name':'chargeAmount','type':'STRING'}," +
                "          {'name':'purchaseCategoryCode','type':'STRING'}," +
                "          {'name':'jsonBody','type':'STRING'}" +
                "  ]" +
                "}";

        return streamDefinition;
    }

    /**
     * Creates the payload.
     *
     * @return the object
     */
    public Object createPayload(){
        return new Object[]{getConsumerKey(),getContext(),getApiVersion(),getApi(), getResourcePath(),getMethod(),
                getVersion(), getRequestCount(),getRequestTime(),getUsername(),getTenantDomain(),getHostName(),
                getApiPublisher(), getApplicationName(), getApplicationId(),

                getRequestId(), getOperatorId(), getSbEndpoint(), getChargeAmount(), getPurchaseCategoryCode(), getJsonBody()};

    }
}

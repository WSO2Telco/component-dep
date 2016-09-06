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
package com.wso2telco.dep.publisheventsdata.dto;


import org.wso2.carbon.apimgt.usage.publisher.dto.ResponsePublisherDTO;
import com.wso2telco.dep.publisheventsdata.EventsConstants;

// TODO: Auto-generated Javadoc
/**
 * The Class SpendLimitDataPublisherDTO.
 */
public class SpendLimitDataPublisherDTO extends ResponsePublisherDTO {

    /** The operator id. */
    private String operatorId;
    
    /** The response code. */
    private String responseCode;
    
    /** The msisdn. */
    private String msisdn;
    
    /** The charge amount. */
    private double chargeAmount;

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
     * Gets the response code.
     *
     * @return the response code
     */
    public String getResponseCode() {
        return responseCode;
    }

    /**
     * Sets the response code.
     *
     * @param responseCode the new response code
     */
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * Gets the msisdn.
     *
     * @return the msisdn
     */
    public String getMsisdn() {
        return msisdn;
    }

    /**
     * Sets the msisdn.
     *
     * @param msisdn the new msisdn
     */
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }
    
    /**
     * Gets the charge amount.
     *
     * @return the charge amount
     */
    public double getChargeAmount() { return chargeAmount; }

    /**
     * Sets the charge amount.
     *
     * @param chargeAmount the new charge amount
     */
    public void setChargeAmount(double chargeAmount) { this.chargeAmount = chargeAmount; }

    /**
     * Gets the stream definition.
     *
     * @return the stream definition
     */
    public static String getStreamDefinition() {
    	
        String streamDefinition = "{" +
                "  'name':'" + EventsConstants.SPEND_LIMIT_DATA_STREAM_NAME + "'," +
                "  'version':'" + EventsConstants.SPEND_LIMIT_DATA_STREAM_VERSION + "'," +
                "  'nickName': 'Spend Limit Data'," +
                "  'description': 'Spend Limit Data'," +
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

    /**
     * Creates the payload.
     *
     * @return the object
     */
    public Object createPayload(){
        return new Object[]{getConsumerKey(),getContext(),getApi(),getVersion(),getResponseTime(),getUsername(),
                            getTenantDomain(),getApplicationName(), getApplicationId(),

                            getOperatorId(), getResponseCode(), getMsisdn(), getChargeAmount()};
    }
}

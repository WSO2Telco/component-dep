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
package com.wso2telco.datapublisher.dto;

import org.wso2.carbon.apimgt.usage.publisher.dto.ResponsePublisherDTO;

import com.wso2telco.datapublisher.DataPublisherConstants;

// TODO: Auto-generated Javadoc
/**
 * The Class SouthboundResponsePublisherDTO.
 */
public class SouthboundResponsePublisherDTO extends ResponsePublisherDTO {

    /** The request id. */
    private String requestId;
    
    /** The operator id. */
    private String operatorId;
    
    /** The response code. */
    private String responseCode;
    
    /** The msisdn. */
    private String msisdn;
    
    /** The charge amount. */
    private String chargeAmount;
    
    /** The operator ref. */
    private String operatorRef;
    
    /** The exception id. */
    private String exceptionId;
    
    /** The exception message. */
    private String exceptionMessage;
    
    /** The purchase category code. */
    private String purchaseCategoryCode;
    
    /** The json body. */
    private String jsonBody;
    
    /** The operation type. */
    private Integer operationType;
    
    /** The merchant id. */
    private String merchantId;
    
    /** The category. */
    private String category;
    
    /** The sub category. */
    private String subCategory;
    
    /** The response. */
    private int response;
    
    private String taxAmount;
    private String channel;
    private String onBehalfOf;
    private String description;
    private String ussdAction;
	private String ussdSessionId;
	private String transactionOperationStatus;
	private String referenceCode;
	private String destinationAddress;
    private String senderAddress;
    private String message;
    private String dateTime;
    private String resourceURL;
    private String messageId;
	    
	    
	
	    public String getDestinationAddress() {
			return destinationAddress;
		}
	
		public void setDestinationAddress(String destinationAddress) {
			this.destinationAddress = destinationAddress;
		}
	
		public String getSenderAddress() {
			return senderAddress;
		}
	
		public void setSenderAddress(String senderAddress) {
			this.senderAddress = senderAddress;
		}
	
		public String getMessage() {
			return message;
		}
	
		public void setMessage(String message) {
		this.message = message;
		}
	
		public String getDateTime() {
			return dateTime;
		}
	
		public void setDateTime(String dateTime) {
			this.dateTime = dateTime;
		}
	
		public String getResourceURL() {
			return resourceURL;
		}
	
		public void setResourceURL(String resourceURL) {
			this.resourceURL = resourceURL;
		}
	
		public String getMessageId() {
			return messageId;
		}
	
		public void setMessageId(String messageId) {
			this.messageId = messageId;
		}
	
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
     * Gets the operator ref.
     *
     * @return the operator ref
     */
    public String getOperatorRef() {
        return operatorRef;
    }

    /**
     * Sets the operator ref.
     *
     * @param operatorRef the new operator ref
     */
    public void setOperatorRef(String operatorRef) {
        this.operatorRef = operatorRef;
    }

    /**
     * Gets the exception id.
     *
     * @return the exception id
     */
    public String getExceptionId() {
        return exceptionId;
    }

    /**
     * Sets the exception id.
     *
     * @param exceptionId the new exception id
     */
    public void setExceptionId(String exceptionId) {
        this.exceptionId = exceptionId;
    }

    /**
     * Gets the exception message.
     *
     * @return the exception message
     */
    public String getExceptionMessage() {
        return exceptionMessage;
    }

    /**
     * Sets the exception message.
     *
     * @param exceptionMessage the new exception message
     */
    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
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
     * Gets the operation type.
     *
     * @return the operation type
     */
    public Integer getOperationType() {
        return operationType;
    }

    /**
     * Sets the operation type.
     *
     * @param operationType the new operation type
     */
    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }

    /**
     * Gets the category.
     *
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category.
     *
     * @param category the new category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets the sub category.
     *
     * @return the sub category
     */
    public String getSubCategory() {
        return subCategory;
    }

    /**
     * Sets the sub category.
     *
     * @param subCategory the new sub category
     */
    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    /**
     * Gets the merchant id.
     *
     * @return the merchant id
     */
    public String getMerchantId() {
        return merchantId;
    }

    /**
     * Sets the merchant id.
     *
     * @param merchantId the new merchant id
     */
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    /* (non-Javadoc)
     * @see org.wso2.carbon.apimgt.usage.publisher.dto.ResponsePublisherDTO#getResponse()
     */
    @Override
    public int getResponse() {
        return response;
    }

    /**
     * Sets the response.
     *
     * @param response the new response
     */
    public void setResponse(int response) {
        this.response = response;
    }
    
    public String getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(String taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getOnBehalfOf() {
		return onBehalfOf;
	}

	public void setOnBehalfOf(String onBehalfOf) {
		this.onBehalfOf = onBehalfOf;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public String getUssdAction() {
        return ussdAction;
    }

    public void setUssdAction(String ussdAction) {
        this.ussdAction = ussdAction;
    }

    public String getUssdSessionId() {
        return ussdSessionId;
    }

    public void setUssdSessionId(String ussdSessionId) {
        this.ussdSessionId = ussdSessionId;
    }

    public String getTransactionOperationStatus() {
        return transactionOperationStatus;
    }

    public void setTransactionOperationStatus(String transactionOperationStatus) {
        this.transactionOperationStatus = transactionOperationStatus;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }
		

    /**
     * Gets the stream definition.
     *
     * @return the stream definition
     */
    public static String getStreamDefinition() {
        String streamDefinition = "{"
                + "  'name':'" + DataPublisherConstants.SOUTHBOUND_RESPONSE_STREAM_NAME + "',"
                + "  'version':'" + DataPublisherConstants.SOUTHBOUND_RESPONSE_STREAM_VERSION + "',"
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
                + "          {'name':'jsonBody','type':'STRING'}"//look comma
                + "          {'name':'jsonBody','type':'STRING'}," 
                + "          {'name':'taxAmount','type':'STRING'}," 
                + "          {'name':'channel','type':'STRING'}," 
                + "          {'name':'onBehalfOf','type':'STRING'}," 
                + "          {'name':'description','type':'STRING'},"
                + "          {'name':'ussdAction','type':'STRING'},"
                + "          {'name':'ussdSessionId','type':'STRING'},"
                + "          {'name':'transactionOperationStatus','type':'STRING'},"
                + "          {'name':'referenceCode','type':'STRING'}"
                
          	/*  + "          {'name':'destinationAddress','type':'STRING'},"
                + "          {'name':'senderAddress','type':'STRING'},"
                + "          {'name':'message','type':'STRING'},"
                + "          {'name':'date_Time','type':'STRING'},"
                + "          {'name':'resourceURL','type':'STRING'},"
               	+ "          {'name':'messageId','type':'STRING'}"*/
                + "  ]"
                + "}";

        return streamDefinition;
    }

    /**
     * Creates the payload.
     *
     * @return the object
     */
    public Object createPayload() {
        return new Object[]{getConsumerKey(), getContext(), getApi_version(), getApi(), getResourcePath(), getMethod(),
            getVersion(), this.getResponse(), getResponseTime(), getServiceTime(), getUsername(), getTenantDomain(), getHostName(),
            getApiPublisher(), getApplicationName(), getApplicationId(),
            getRequestId(), getOperatorId(), getResponseCode(), getMsisdn(), getChargeAmount(), getPurchaseCategoryCode(),
            getOperatorRef(), getExceptionId(), getExceptionMessage(), getJsonBody(), getTaxAmount() ,getChannel(),
            getOnBehalfOf(), getDescription(),getUssdAction(), getUssdSessionId(),getTransactionOperationStatus(),getReferenceCode(),
            getDestinationAddress(),getSenderAddress(),getMessage(),getDateTime(),getResourceURL(),getMessageId()};
        }
}

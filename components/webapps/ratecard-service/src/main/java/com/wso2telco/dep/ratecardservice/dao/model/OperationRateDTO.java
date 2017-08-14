package com.wso2telco.dep.ratecardservice.dao.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class OperationRateDTO {

	private Integer operationRateId;
	private OperatorDTO operator;
	private APIOperationDTO apiOperation;
	private RateDefinitionDTO rateDefinition;
	private String createdBy;
	private String createdDate;
	private String updatedBy;
	private String updatedDate;
	
	public Integer getOperationRateId() {
		return operationRateId;
	}
	public void setOperationRateId(Integer operationRateId) {
		this.operationRateId = operationRateId;
	}
	public OperatorDTO getOperator() {
		return operator;
	}
	public void setOperator(OperatorDTO operator) {
		this.operator = operator;
	}
	public APIOperationDTO getApiOperation() {
		return apiOperation;
	}
	public void setApiOperation(APIOperationDTO apiOperation) {
		this.apiOperation = apiOperation;
	}
	public RateDefinitionDTO getRateDefinition() {
		return rateDefinition;
	}
	public void setRateDefinition(RateDefinitionDTO rateDefinition) {
		this.rateDefinition = rateDefinition;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	} 
}

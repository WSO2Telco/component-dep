package com.wso2telco.dep.ratecardservice.dao.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class OperationRateDTO {

	private Integer operationRateId;
	private OperatorDTO operator;
	private APIOperationDTO apiOperation;
	private RateDefinitionDTO rateDefinition;
	private String createdBy;
	
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
}

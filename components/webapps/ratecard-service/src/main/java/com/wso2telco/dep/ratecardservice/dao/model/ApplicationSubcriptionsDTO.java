package com.wso2telco.dep.ratecardservice.dao.model;

import java.util.Date;

public class ApplicationSubcriptionsDTO {
	
	private String apiOperation;
	private String rateDefname;
	private String apiVersion;
	
	public String getApiVersion() {
		return apiVersion;
	}
	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}
	private Integer operatorId;
	private Integer apiOperationId;
	private Integer  applicationId;
	private Integer rateDefId;
	
	private Date subRateSBActDate;
	private Date subRateSBDisDate;
	private String createBy;

	private String updateBy;
	private String comment;
	

	
	
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public Integer getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}
	
	public Integer getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}
	public Integer getApiOperationId() {
		return apiOperationId;
	}
	public void setApiOperationId(Integer apiOperationId) {
		this.apiOperationId = apiOperationId;
	}
	public Integer getRateDefId() {
		return rateDefId;
	}
	public void setRateDefId(Integer rateDefId) {
		this.rateDefId = rateDefId;
	}
	public Date getSubRateSBActDate() {
		return subRateSBActDate;
	}
	public void setSubRateSBActDate(Date subRateSBActDate) {
		this.subRateSBActDate = subRateSBActDate;
	}
	public Date getSubRateSBDisDate() {
		return subRateSBDisDate;
	}
	public void setSubRateSBDisDate(Date subRateSBDisDate) {
		this.subRateSBDisDate = subRateSBDisDate;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public String getApiOperation() {
		return apiOperation;
	}
	public void setApiOperation(String apiOperation) {
		this.apiOperation = apiOperation;
	}
	public String getRateDefname() {
		return rateDefname;
	}
	public void setRateDefname(String rateDefname) {
		this.rateDefname = rateDefname;
	}

}

package com.wso2telco.workflow.impl;

import javax.xml.namespace.QName;

import com.wso2telco.workflow.audit.*;
import com.wso2telco.workflow.dao.WorkflowDAO;
import com.wso2telco.workflow.in.WorkflowAudit;
import com.wso2telco.workflow.model.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WorkflowAuditImpl implements WorkflowAudit {
    private static Log log = LogFactory.getLog(WorkflowAuditImpl.class);
	@Override
	public void insertAppApprovalAuditRecord(
			AppApprovalAuditRequestType appApprovalAuditRequest) {
		
		String appName = appApprovalAuditRequest.getAppName();
		String appCreator = appApprovalAuditRequest.getAppCreator();
		String appStatus = appApprovalAuditRequest.getAppStatus();
		String appApprovalType = appApprovalAuditRequest.getAppApprovalType();
		String completedByRole = appApprovalAuditRequest.getCompletedByRole();
		String completedByUser = appApprovalAuditRequest.getCompletedByUser();
		String completedOn = appApprovalAuditRequest.getCompletedOn();
		
		ApplicationApprovalAuditRecord record = new ApplicationApprovalAuditRecord();
		record.setAppName(appName);
		record.setAppCreator(appCreator);
		record.setAppStatus(appStatus);
		record.setAppApprovalType(appApprovalType);
		record.setCompletedByRole(completedByRole);
		record.setCompletedByUser(completedByUser);
		record.setCompletedOn(completedOn);
		
		try {
			WorkflowDAO.insertAppApprovalAuditRecord(record);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void insertSubApprovalAuditRecord(
			SubApprovalAuditRequestType subApprovalAuditRequest) {
		
		String apiProvider = subApprovalAuditRequest.getApiProvider();
        System.out.println("apiProvider"+apiProvider);

		String apiName = subApprovalAuditRequest.getApiName();
        System.out.println("apiName"+apiName);
		String apiVersion = subApprovalAuditRequest.getApiVersion();
        System.out.println("apiVersion"+apiVersion);
		String appId = subApprovalAuditRequest.getAppId();
        System.out.println("appId"+appId);
		String subStatus = subApprovalAuditRequest.getSubStatus();
        System.out.println("subStatus"+subStatus);
		String subApprovalType = subApprovalAuditRequest.getSubApprovalType();
        System.out.println("subApprovalType"+subApprovalType);
		String completedByRole = subApprovalAuditRequest.getCompletedByRole();
        System.out.println("completedByRole"+completedByRole);
		String completedByUser = subApprovalAuditRequest.getCompletedByUser();
        System.out.println("completedByUser"+completedByUser);
		String completedOn = subApprovalAuditRequest.getCompletedOn();

		
		SubscriptionApprovalAuditRecord record = new SubscriptionApprovalAuditRecord();
		record.setApiProvider(apiProvider);
		record.setApiName(apiName);
		record.setApiVersion(apiVersion);
		record.setAppId(new Integer(appId).intValue());
		record.setSubStatus(subStatus);
		record.setSubApprovalType(subApprovalType);
		record.setCompletedByRole(completedByRole);
		record.setCompletedByUser(completedByUser);
		record.setCompletedOn(completedOn);
		
		try {
			WorkflowDAO.insertSubApprovalAuditRecord(record);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}

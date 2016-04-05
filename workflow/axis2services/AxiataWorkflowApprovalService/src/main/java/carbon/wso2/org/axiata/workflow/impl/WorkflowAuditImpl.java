package carbon.wso2.org.axiata.workflow.impl;

import javax.xml.namespace.QName;

import carbon.wso2.org.axiata.workflow.audit.*;
import carbon.wso2.org.axiata.workflow.dao.WorkflowDAO;
import carbon.wso2.org.axiata.workflow.in.WorkflowAudit;
import carbon.wso2.org.axiata.workflow.model.*;

public class WorkflowAuditImpl implements WorkflowAudit {

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
		String apiName = subApprovalAuditRequest.getApiName();
		String apiVersion = subApprovalAuditRequest.getApiVersion();
		String appId = subApprovalAuditRequest.getAppId();
		String subStatus = subApprovalAuditRequest.getSubStatus();
		String subApprovalType = subApprovalAuditRequest.getSubApprovalType();
		String completedByRole = subApprovalAuditRequest.getCompletedByRole();
		String completedByUser = subApprovalAuditRequest.getCompletedByUser();
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

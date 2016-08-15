package com.wso2telco.workflow.in;

import com.wso2telco.workflow.audit.*;

public interface WorkflowAudit {

	void insertAppApprovalAuditRecord(AppApprovalAuditRequestType appApprovalAuditRequest);
	
	void insertSubApprovalAuditRecord(SubApprovalAuditRequestType subApprovalAuditRequest);
}

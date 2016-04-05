package carbon.wso2.org.axiata.workflow.in;

import carbon.wso2.org.axiata.workflow.audit.*;

public interface WorkflowAudit {

	void insertAppApprovalAuditRecord(AppApprovalAuditRequestType appApprovalAuditRequest);
	
	void insertSubApprovalAuditRecord(SubApprovalAuditRequestType subApprovalAuditRequest);
}

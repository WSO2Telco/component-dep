package com.wso2telco.subscription.approval.workflow.serviceTask;

import com.wso2telco.subscription.approval.workflow.utils.WorkflowConstants;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OperatorAdminDbUpdater implements JavaDelegate {

	private static final Log log = LogFactory.getLog(OperatorAdminDbUpdater.class);

	public void execute(DelegateExecution arg0) throws Exception {

		Object operatorName = arg0.getVariable(WorkflowConstants.OPERATOR).toString();
		String operatorAdminApprovalStatus = arg0.getVariable(WorkflowConstants.OPERATOR_ADMIN_APPROVAL).toString();
		log.info("In OperatorDataUpdater, Operator admin approval status: " + operatorAdminApprovalStatus +
		         " Operator: " + operatorName);
	}

}


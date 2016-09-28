package com.wso2telco.subscription.approval.workflow.serviceTask;

import com.wso2telco.subscription.approval.workflow.utils.WorkflowConstants;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HubAdminDbUpdater implements JavaDelegate {

	private static final Log log = LogFactory.getLog(HubAdminDbUpdater.class);

	public void execute(DelegateExecution arg0) throws Exception {

		String status = arg0.getVariable(WorkflowConstants.HUB_ADMIN_APPROVAL).toString();
		log.info("In HubDataUpdater, Hub admin approval status: " + status);
	}

}

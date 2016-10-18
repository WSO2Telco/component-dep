package com.wso2telco.workflow.approval.subscription.servicetask;


import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.workflow.approval.util.Constants;

public class HubAdminDbUpdater implements JavaDelegate {

	private static final Log log = LogFactory.getLog(HubAdminDbUpdater.class);

	public void execute(DelegateExecution arg0) throws Exception {

		String status = arg0.getVariable(Constants.HUB_ADMIN_APPROVAL).toString();
		log.info("In HubDataUpdater, Hub admin approval status: " + status);
	}

}

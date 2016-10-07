package com.wso2telco.subscription.approval.workflow.serviceTask;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Collection;

public class OperatorListConverter implements JavaDelegate {

	private static final Log log = LogFactory.getLog(OperatorListConverter.class);

	public void execute(DelegateExecution arg0) throws Exception {

		String[] operatorList = arg0.getVariable("operators").toString().split(",");
		Collection<String> operatorNames = new ArrayList<String>();
		for(String operator : operatorList) {
			operatorNames.add(operator.trim());
			// TODO: make debug
			log.info("Operator '" + operator.trim() + "' added to operatorList");
		}
		arg0.setVariable("operatorList", operatorNames);
	}
}
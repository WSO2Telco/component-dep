package com.wso2telco.dep.workflow.activator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


public class Activator implements BundleActivator {
	
	private static final Log log = LogFactory.getLog(Activator.class);

	@Override
	public void start(BundleContext arg0) throws Exception {
		// TODO Auto-generated method stub
		log.debug("Starting Workflow Manager OSGI Activator");
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		// TODO Auto-generated method stub
		log.debug("Workflow Manager OSGI Activator Stopped");
	}

}

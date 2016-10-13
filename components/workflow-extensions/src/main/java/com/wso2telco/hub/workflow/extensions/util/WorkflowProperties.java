package com.wso2telco.hub.workflow.extensions.util;

import com.wso2telco.core.dbutils.fileutils.PropertyFileReader;

import java.util.Properties;

public class WorkflowProperties {

    private static final String WORKFLOW_PROPERTIES_FILE = "workflow.properties";
	
    public static Properties loadWorkflowProperties(){
        Properties props=null;
        props =PropertyFileReader.getFileReader().getProperties(WORKFLOW_PROPERTIES_FILE);
        return props;
    }

}

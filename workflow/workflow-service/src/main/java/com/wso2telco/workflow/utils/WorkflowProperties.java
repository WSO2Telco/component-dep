package com.wso2telco.workflow.utils;

import com.wso2telco.core.dbutils.fileutils.PropertyFileReader;
import java.util.Properties;

public class WorkflowProperties {
	
    public static Properties loadWorkflowProperties(){
        Properties props;
        props =PropertyFileReader.getFileReader().getProperties(Constants.WORKFLOW_PROPERTIES_FILE);
        return props;
    }

}

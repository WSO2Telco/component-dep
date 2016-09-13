package com.wso2telco.workflow.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.wso2.carbon.utils.CarbonUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CommonUtil {
	
	private static Log log = LogFactory.getLog(CommonUtil.class);
	
	public static Properties loadAxiataWorkflowProperties(){
        String configPath = CarbonUtils.getCarbonConfigDirPath() + File.separator + Constants.AXIATA_WORKFLOW_PROPERTIES_FILE;
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(configPath));
        } catch (IOException e) {
            log.error("Error while loading axiata-workflow.properties file", e);
        }
        return props;
    }

}

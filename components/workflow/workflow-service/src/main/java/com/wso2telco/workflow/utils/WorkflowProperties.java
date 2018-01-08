/**
 * Copyright (c) 2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wso2telco.workflow.utils;

import com.wso2telco.core.dbutils.fileutils.PropertyFileReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WorkflowProperties {

    private static HashMap<String, String> propertiesMap = null;
    private static final Log log = LogFactory.getLog(WorkflowProperties.class);

    public static Properties loadWorkflowProperties(){
        Properties props;
        props =PropertyFileReader.getFileReader().getProperties(Constants.WORKFLOW_PROPERTIES_FILE);
        return props;
    }
    
    public static Map<String, String> loadWorkflowPropertiesFromXML() {
    	if (propertiesMap == null) {
    		try {
            	propertiesMap = new HashMap<String, String>();
            	
            	DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        		
        		String carbonHome = System.getProperty("carbon.home");
        		String workflowPropertiesFile = carbonHome + "/repository/conf/" + Constants.WORKFLOW_PROPERTIES_XML_FILE;

        		Document document = builder.parse(new File(workflowPropertiesFile));
        		Element rootElement = document.getDocumentElement();
        		
        		NodeList nodeList = rootElement.getElementsByTagName("Property");
        		if (nodeList != null && nodeList.getLength() > 0) {
        			for (int i = 0; i < nodeList.getLength(); i++) {
        				Node node = nodeList.item(i);
        				String nodeName = node.getAttributes().getNamedItem("name").getNodeValue();
        				if (nodeName.equalsIgnoreCase(Constants.SERVICE_HOST)
        						|| nodeName.equalsIgnoreCase(Constants.KEY_WORKFLOW_EMAIL_NOTIFICATION_HOST)
        						|| nodeName.equalsIgnoreCase(Constants.KEY_WORKFLOW_EMAIL_NOTIFICATION_FROM_ADDRESS)
        						|| nodeName.equalsIgnoreCase(Constants.KEY_WORKFLOW_EMAIL_NOTIFICATION_FROM_PASSWORD)
        						|| nodeName.equalsIgnoreCase(Constants.PUBLISHER_ROLE_START_WITH)
        						|| nodeName.equalsIgnoreCase(Constants.PUBLISHER_ROLE_END_WITH)
        						|| nodeName.equalsIgnoreCase(Constants.MANDATE_SERVICE_HOST)) {
        					String value = ((Element)node).getTextContent();
        					propertiesMap.put(nodeName, value);
        				} else {
        					//Not a matching property
        				}
        			}
                }
    		} catch (Exception e) {
    			String errorMessage = "Error in WorkflowProperties.loadWorkflowPropertiesFromXML";
    			log.error(errorMessage, e);
    		}
    	} else {
    		//Return already loaded propertiesMap
    	}
    	return propertiesMap;
    }

}

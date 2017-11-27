package org.workflow.core.util;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WorkflowProperties {

	public static final String WORKFLOW_PROPERTIES_XML_FILE = "workflow-extensions.xml";
	public static final String ACTIVITI_SERVICE_HOST = "serviceEndpoint";
    private static ActivitiBean activitiBean = null;
	private static final Log log = LogFactory.getLog(WorkflowProperties.class);

    public static ActivitiBean loadWorkflowPropertiesFromXML() {
    	if (activitiBean == null) {
    		try {
    			activitiBean = new ActivitiBean();
            	DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        		String carbonHome = System.getProperty("carbon.home");
        		String workflowPropertiesFile = carbonHome + "/repository/resources/" + WORKFLOW_PROPERTIES_XML_FILE;
        		Document document = builder.parse(new File(workflowPropertiesFile));
        		Element rootElement = document.getDocumentElement();

        		NodeList nodeList = rootElement.getElementsByTagName("Property");
        		if (nodeList != null && nodeList.getLength() > 0) {
        			for (int i = 0; i < nodeList.getLength(); i++) {
        				Node node = nodeList.item(i);
        				String nodeName = node.getAttributes().getNamedItem("name").getNodeValue();
        				if (nodeName.equalsIgnoreCase(ACTIVITI_SERVICE_HOST)) {
        					String value = ((Element)node).getTextContent();
        					activitiBean.setTag(nodeName);
        					activitiBean.setValue(value);
        					//propertiesMap.put(nodeName, value);
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
    	return activitiBean;
    }
}
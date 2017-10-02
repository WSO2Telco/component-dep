package com.wso2telco.workflow.approval.approvaltask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.wso2telco.workflow.approval.util.Constants;

public class WorkflowApprovalTaskListReader {
	
	private static ArrayList<String> taskList = null;
	
    private WorkflowApprovalTaskListReader() {
    }
    
	private static ArrayList<String> getWorkflowClassList()
			throws ParserConfigurationException, SAXException, IOException {
		if (taskList == null) {
			taskList = new ArrayList<String>();
			
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
					if (nodeName.equalsIgnoreCase("WorkflowApprovalTasksList")) {
						NodeList list = node.getChildNodes();
						if (list != null && list.getLength() > 0) {
							for (int j = 0; j < list.getLength(); j++) {
								Node taskNode = list.item(j);
								if (taskNode.getNodeType() == Node.ELEMENT_NODE) {
									String taskClass = taskNode.getAttributes().getNamedItem("executor").getNodeValue();
									taskList.add(taskClass);
								}
							}
				        }
					} else {
						//Do nothing
					}
				}
	        }
		} else {
			//Return already loaded taskList
		}
		return taskList;
	}

	public static ArrayList<WorkflowApprovalTask> getWorkflowClassObjList() throws Exception {
		ArrayList<WorkflowApprovalTask> taskObjList = new ArrayList<WorkflowApprovalTask>();
		ArrayList<String> taskClassesList = getWorkflowClassList();
		for (String taskClassString: taskClassesList) {
			Class taskClass = Class.forName(taskClassString);
			WorkflowApprovalTask taskObj = (WorkflowApprovalTask)taskClass.newInstance();
			taskObjList.add(taskObj);
		}
		return taskObjList;
	}
}

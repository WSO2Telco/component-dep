/**
 * Copyright (c) 2020-2021, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
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

package com.wso2telco.dep.lifecycleextension.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class WorkflowProperties {

    private static HashMap<String, String> propertiesMap = null;
    private static final Log log = LogFactory.getLog(WorkflowProperties.class);

    private WorkflowProperties(){

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
                        if (nodeName.equalsIgnoreCase(Constants.KEY_WORKFLOW_EMAIL_NOTIFICATION_HOST)
                                || nodeName.equalsIgnoreCase(Constants.KEY_WORKFLOW_EMAIL_NOTIFICATION_FROM_ADDRESS)
                                || nodeName.equalsIgnoreCase(Constants.KEY_WORKFLOW_EMAIL_NOTIFICATION_FROM_PASSWORD)) {
                            String value = (node).getTextContent();
                            propertiesMap.put(nodeName, value);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("Error in WorkflowProperties.loadWorkflowPropertiesFromXML", e);
            }
        }
        return propertiesMap;
    }
}

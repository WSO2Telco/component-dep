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

package com.wso2telco.dep.server.startup.observer.user.role.updater;


import com.wso2telco.dep.operatorservice.model.Operator;
import com.wso2telco.dep.operatorservice.model.OperatorApplicationDTO;
import com.wso2telco.dep.operatorservice.service.OparatorService;
import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.wso2.carbon.utils.CarbonUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * The Class ReadMobileOperator.
 */
public class ReadMobileOperator {

    private List<OperatorApplicationDTO> updateOperator;
    private List<OperatorApplicationDTO> addOperator;
    private Map<String, String> existingOperator;

    /**
     * Query.
     *
     * @param XpathExpression the xpath expression
     * @return NodeList
     * @throws javax.xml.parsers.ParserConfigurationException the parser configuration exception
     * @throws org.xml.sax.SAXException                 the SAX exception
     * @throws java.io.IOException                  Signals that an I/O exception has occurred.
     * @throws javax.xml.xpath.XPathExpressionException     the x path expression exception
     */
    public NodeList query(String XpathExpression) throws ParserConfigurationException, SAXException,
            IOException, XPathExpressionException {
        // standard for reading an XML file
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;
        XPathExpression expr = null;
        builder = factory.newDocumentBuilder();
        doc = builder.parse(CarbonUtils.getCarbonConfigDirPath() + File.separator + "operators.xml");
        // create an XPathFactory
        XPathFactory xFactory = XPathFactory.newInstance();
        // create an XPath object
        XPath xpath = xFactory.newXPath();
        NodeList nodeList = (NodeList) xpath.compile(XpathExpression).evaluate(doc, XPathConstants.NODESET);
        return nodeList;
    }

    public void setOperators() throws Exception {

        NodeList nodes = null;
        UserRole userRole = new UserRole();
        OparatorService oparatorService = new OparatorService();
        updateOperator = new ArrayList<OperatorApplicationDTO>();
        addOperator = new ArrayList<OperatorApplicationDTO>();
        setExistingOperator();

        nodes = this.query("/operatorsConfig/operators/operator");
        for (int i = 0; i < nodes.getLength(); i++) {

            String operatorName = nodes.item(i).getChildNodes().item(1).getFirstChild().getNodeValue();
            String role = operatorName + "-admin-role";
            String description = nodes.item(i).getChildNodes().item(3).getChildNodes().item(1).getFirstChild().getNodeValue();
            String tokenUrl = nodes.item(i).getChildNodes().item(3).getChildNodes().item(1).getFirstChild().getNodeValue();
            String userName = nodes.item(i).getChildNodes().item(3).getChildNodes().item(3).getFirstChild().getNodeValue();
            String passWord = nodes.item(i).getChildNodes().item(3).getChildNodes().item(5).getFirstChild().getNodeValue();

            byte[] encodedBytes = Base64.encodeBase64((userName + ":" + passWord).getBytes());
            String tokenAuth = "Basic " + new String(encodedBytes);

            OperatorApplicationDTO operatorApplicationDTO = new OperatorApplicationDTO();
            operatorApplicationDTO.setTokenurl(tokenUrl);
            operatorApplicationDTO.setOperatorname(operatorName);
            operatorApplicationDTO.setTokenauth(tokenAuth);

            userRole.addRoles(role);

            if (existingOperator.containsKey(operatorName)) {
                updateOperator.add(operatorApplicationDTO);
            } else {
                addOperator.add(operatorApplicationDTO);
            }

        }

    }


    public List<OperatorApplicationDTO> getUpdateOperators() {
        return updateOperator;
    }

    public List<OperatorApplicationDTO> getAddOperators() {
        return addOperator;
    }

    public void setExistingOperator() throws Exception {
        existingOperator = new HashMap<String, String>();
        OparatorService oparatorService = new OparatorService();
        List<Operator> operatorList = oparatorService.retrieveOperatorList();
        for (Operator operator : operatorList) {
            existingOperator.put(operator.getOperatorName(), operator.getOperatorDescription());
        }

    }


}

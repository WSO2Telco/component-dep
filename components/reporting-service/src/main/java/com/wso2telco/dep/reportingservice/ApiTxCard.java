/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *  
 *  WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.dep.reportingservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;

import com.wso2telco.dep.reportingservice.dao.TxCardDAO;
import com.wso2telco.dep.reportingservice.internal.HostObjectComponent;


// TODO: Auto-generated Javadoc
/**
 * The Class ApiTxCard.
 */
public class ApiTxCard {
	
	/** The tx card temp. */
	public static HashMap<Integer, Object> txCardTemp = new HashMap<Integer, Object>();
	
	/**
	 * Instantiates a new api tx card.
	 */
	public ApiTxCard(){
		readTxCard();
	}
	
	/**
	 * Read tx card.
	 */
	@SuppressWarnings("rawtypes")
    private void readTxCard() {
		
        try {
            Registry registry = HostObjectComponent.getRegistryService().getGovernanceSystemRegistry();
            Resource resource = registry.get(TxCardConstrainsts.TX_CARD_LOCATION);
            String content = new String((byte[]) resource.getContent());
            OMElement element = AXIOMUtil.stringToOM(content);
            
            OMElement txLogElement = element.getFirstChildWithName(TxCardConstrainsts.ROOT_ELEMENT);
            Iterator apiElements = txLogElement.getChildrenWithName(TxCardConstrainsts.API_ELEMENT);
            
            while(apiElements.hasNext()){
            	
            	List<String> headers = new ArrayList<String>();
        		List<TxCardDAO.DataList> dataList = new ArrayList<TxCardDAO.DataList>();
            	
                OMElement currentAPI = (OMElement) apiElements.next();
                String operationType = currentAPI.getAttributeValue(TxCardConstrainsts.API_OPERATION_TYPE_ATT);
                
                OMElement headerParamList = currentAPI.getFirstChildWithName(TxCardConstrainsts.HEADER_PARAMS_ELEMENT);
                Iterator paramList = headerParamList.getChildrenWithName(TxCardConstrainsts.PARAM_ELEMENT);
                while(paramList.hasNext()){
                    OMElement currentParam = (OMElement) paramList.next();
                    String paramValue = currentParam.getText();
                    headers.add(paramValue);                    
                }
                
                OMElement dataParamList = currentAPI.getFirstChildWithName(TxCardConstrainsts.DATA_PARAMS_ELEMENT);
                Iterator rowList = dataParamList.getChildrenWithName(TxCardConstrainsts.DATA_ROW_ELEMENT);
                
                while(rowList.hasNext()){
                    OMElement currentRow = (OMElement) rowList.next();
                    OMElement manipulation = currentRow.getFirstChildWithName(TxCardConstrainsts.DATA_ROW_MANIPULATION_ELEMENT);
                    String manipulatioType = manipulation.getAttributeValue(TxCardConstrainsts.MANIPULATION_TYPE_ATT);
                    String manipulationField = manipulation.getAttributeValue(TxCardConstrainsts.MANIPULATION_FIELD_ATT);
                    String manipulationRegEx = manipulation.getText();
                    
                    TxCardDAO.DataList temp = new TxCardDAO.DataList();
                    temp.setManipulationField(manipulationField);
                    temp.setManipulationType(manipulatioType);
                    temp.setManipulationRegEx(manipulationRegEx);
                    
                    dataList.add(temp);
                }
                
                TxCardDAO txCardDao = new TxCardDAO();
                txCardDao.setDataList(dataList);
                txCardDao.setHeaderList(headers);
                txCardDao.setTxOperationType(Integer.valueOf(operationType));
                
                txCardTemp.put(Integer.valueOf(operationType), txCardDao);
                
            }
            
        } catch (Exception ex) {
            System.out.println("Error in GetApiTxCard :: " + ex);
        }
    }

	/**
	 * Gets the tx card temp.
	 *
	 * @return the tx card temp
	 */
	public static HashMap<Integer, Object> getTxCardTemp() {
		return txCardTemp;
	}

	/**
	 * Sets the tx card temp.
	 *
	 * @param txCardTemp the tx card temp
	 */
	public static void setTxCardTemp(HashMap<Integer, Object> txCardTemp) {
		ApiTxCard.txCardTemp = txCardTemp;
	}
	
}
class TxCardConstrainsts {
    public static final String TX_CARD_LOCATION = APIConstants.API_APPLICATION_DATA_LOCATION + "/TxCard.xml";
    public static final QName ROOT_ELEMENT = new QName("transactionlog");
    public static final QName API_ELEMENT = new QName("api");
    public static final QName API_OPERATION_TYPE_ATT = new QName("type");
    public static final QName HEADER_PARAMS_ELEMENT = new QName("headerparams");
    public static final QName PARAM_ELEMENT = new QName("param");
    public static final QName DATA_PARAMS_ELEMENT = new QName("dataparams");
    public static final QName DATA_ROW_ELEMENT = new QName("row");
    public static final QName DATA_ROW_MANIPULATION_ELEMENT = new QName("manipulation");
    public static final QName MANIPULATION_TYPE_ATT = new QName("type");
    public static final QName MANIPULATION_FIELD_ATT = new QName("field");
    
}

/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * <p>
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.dep.operatorservice;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.core.dbutils.fileutils.PropertyFileReader;
import com.wso2telco.dep.operatorservice.dao.WorkflowDAO;
import com.wso2telco.dep.operatorservice.exception.StoreHostObjectException;
import com.wso2telco.dep.operatorservice.model.WorkflowReferenceDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import com.wso2telco.dep.operatorservice.dao.OperatorDAO;
import com.wso2telco.dep.operatorservice.model.Operator;
import com.wso2telco.dep.operatorservice.model.OperatorSearchDTO;
import com.wso2telco.dep.operatorservice.service.OparatorService;



public class StoreHostObject extends ScriptableObject {
	
	private static final String DEPLOYMENT_TYPE_SYSTEM_PARAM = "DEPLOYMENT_TYPE";

    /**
     *
     */
    private static final long serialVersionUID = 3642128192613608256L;

    /** The Constant log. */
    private static final Log log = LogFactory.getLog(StoreHostObject.class);

    /** The hostobject name. */
    private String hostobjectName = "StoreHostObject";

    /*
     * (non-Javadoc)
     *
     * @see org.mozilla.javascript.ScriptableObject#getClassName()
     */
    @Override
    public String getClassName() {
        return hostobjectName;
    }

    /**
     * Instantiates a new api store host object.
     */
    public StoreHostObject() {
        if (log.isDebugEnabled()) {
            log.debug("Initialized HostObject StoreHostObject");
        }
    }

    /**
     *
     * @param cx
     * @param thisObj
     * @param args
     * @param funObj
     * @return
     * @throws StoreHostObjectException
     */
    public static List<Operator> jsFunction_retrieveOperatorList(Context cx,
                                                                 Scriptable thisObj,
                                                                 Object[] args,
                                                                 Function funObj) throws StoreHostObjectException {

        List<Operator> operatorList = null;

        try {
            OperatorSearchDTO searchDTO = new OperatorSearchDTO();
            operatorList = new OparatorService().loadOperators(searchDTO);

        } catch (Exception e) {
            handleException("Error occured while retrieving operator list. ", e);
        }

        return operatorList;
    }

    /**
     *
     * @param cx
     * @param thisObj
     * @param args
     * @param funObj
     * @return
     * @throws StoreHostObjectException
     */
    public static boolean jsFunction_persistSubOperatorList(Context cx,
                                                            Scriptable thisObj,
                                                            Object[] args,
                                                            Function funObj) throws StoreHostObjectException {

        boolean status = false;

        String apiName = (String) args[0];
        String apiVersion = (String) args[1];
        String apiProvider = (String) args[2];
        int appId = ((Double) args[3]).intValue();
        String operatorList = (String) args[4];

        try {
            new OperatorDAO().persistOperators(apiName, apiVersion, apiProvider, appId, operatorList);

        } catch (Exception e) {
            handleException("Error occured while retrieving operator list. ", e);
        }

        return status;
    }
    
    public static String jsFunction_getDeploymentType(){
    	
    	return System.getProperty(DEPLOYMENT_TYPE_SYSTEM_PARAM, "hub");
    			
    }

    public static void jsFunction_removeAPISubscription(Context cx,
                                                        Scriptable thisObj,
                                                        Object[] args,
                                                        Function funObj) {
        OperatorDAO operatorDAO = new OperatorDAO();
        String applicationId = (String) args[0];
        String apiName= (String) args[1];
        try {
            operatorDAO.removeAPISubscription(applicationId,apiName);
        } catch (SQLException e) {
            log.error("database operation error in remove API Subscription : ", e);
        }
    }

    public static void jsFunction_removeAPISubscriptionFromStatDB(Context cx,
                                                                  Scriptable thisObj,
                                                                  Object[] args,
                                                                  Function funObj) {
        OperatorDAO operatorDAO = new OperatorDAO();
        String applicationId = (String) args[0];
        String apiName= (String) args[1];
        String version = (String) args[2];
        try {
            operatorDAO.removeAPISubscriptionFromStatDB(applicationId,apiName,version);
        } catch (SQLException e) {
            log.error("database operation error in remove API Subscription : ", e);
        }
    }

    public static void jsFunction_removeApplication(Context cx,
                                                    Scriptable thisObj,
                                                    Object[] args,
                                                    Function funObj) {
        OperatorDAO operatorDAO = new OperatorDAO();
        String applicationId = (String) args[0];
        try {
            operatorDAO.removeApplication(applicationId);
        } catch (SQLException e) {
            log.error("database operation error in remove application : ", e);
        }
    }

    public static void jsFunction_removeSubApprovalOperators(Context cx,
                                                    Scriptable thisObj,
                                                    Object[] args,
                                                    Function funObj) {
        OperatorDAO operatorDAO = new OperatorDAO();
        String applicationId = (String) args[0];
        try {
            operatorDAO.removeSubApprovalOperators(applicationId);
        } catch (SQLException e) {
            log.error("database operation error in remove application : ", e);
        }
    }

    public static WorkflowReferenceDTO jsFunction_getWorkflowRef(Context cx,
                                                      Scriptable thisObj,
                                                      Object[] args,
                                                      Function funObj){
        WorkflowReferenceDTO workflow=null;
        WorkflowDAO workflowDAO=new WorkflowDAO();
        String apiName = (String) args[1];
        String applicationId = (String) args[0];
        String apiVersion = (String) args[2];
        try {
            workflow=workflowDAO.findWorkflow(apiName, applicationId, apiVersion);
        } catch (Exception e) {
            log.error("database operation error in get workflow ref : ", e);
        }
        return workflow;

    }

    public static boolean jsFunction_getAppStatus(Context cx,Scriptable thisObj,Object[] args,Function funObj){

        String appId = (String) args[0];
        String operator = (String) args[1];
        boolean status=false;
        WorkflowDAO workflowDAO=new WorkflowDAO();

        try {
            status=workflowDAO.operatorAppsIsActive(Integer.parseInt(appId),operator);
        } catch (Exception e) {
            log.error("database operation error in get workflow ref : ", e);
        }
        return status;

    }
    
    public static NativeObject jsFunction_getOperatorApprovedSubscriptionsByApplicationId(Context cx, Scriptable thisObj, Object[] args,
			Function funObj) throws StoreHostObjectException {

		NativeObject resultObject = new NativeObject();
		NativeArray historyArray = new NativeArray(0);

		String appId = args[0].toString();

		OparatorService oparatorService = new OparatorService();

        if(jsFunction_getDeploymentType().equals("hub") || jsFunction_getDeploymentType().equals("external_gateway")){
            try {

                Map<Integer, Map<String, Map<String,String>>> subDetails = oparatorService.getOperatorApprovedSubscriptionsByApplicationId(Integer.parseInt(appId));
                log.debug("getOperatorApprovedSubscriptionsByApplicationId : " + subDetails);

                if (!subDetails.isEmpty()) {

                    int j = 0;
                    for (Map.Entry<Integer, Map<String, Map<String,String>>> sub : subDetails.entrySet()) {

                        Map<String, Map<String,String>> subInfo = sub.getValue();

                        NativeArray historyDataArray = new NativeArray(0);
                        int z = 0;

                        for (Map.Entry<String, Map<String,String>> sb : subInfo.entrySet()) {

                            String apiName = sb.getKey();
                            Map<String,String> s = sb.getValue();

                            NativeObject subData = new NativeObject();
                            subData.put("apiName", subData, apiName);
                            subData.put("substatus", subData, s.get("substatus"));
                            subData.put("operatorname", subData, s.get("operatorname"));

                            historyDataArray.put(z, historyDataArray, subData);
                            z++;
                        }

                        historyArray.put(j, historyArray, historyDataArray);
                        j++;
                    }
                } else {

                    log.debug("subscription details unavalible for application id : " + appId);
                }
            } catch (Exception e) {

                log.error("error occurred in getOperatorApprovedSubscriptionsByApplicationId : ", e);
                handleException(e.getMessage(), e);
            }

        }

		resultObject.put("operatorSubsApprovedHistory", resultObject, historyArray);

		return resultObject;
	}

    /**
     * Handle exception.
     *
     * @param msg
     *            the msg
     * @throws StoreHostObjectException
     *             the API management exception
     */
    private static void handleException(String msg) throws StoreHostObjectException {
        log.error(msg);
        throw new StoreHostObjectException(msg);
    }

    /**
     * Handle exception.
     *
     * @param msg
     *            the msg
     * @param t
     *            the t
     * @throws StoreHostObjectException
     *             the API management exception
     */
    private static void handleException(String msg, Throwable t) throws StoreHostObjectException {
        log.error(msg, t);
        throw new StoreHostObjectException(msg, t);
    }
}

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

package com.wso2telco.workflow.subscription;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.GenaralError;
import com.wso2telco.dep.operatorservice.model.OperatorEndPointDTO;
import com.wso2telco.workflow.dao.WorkflowDbService;
import com.wso2telco.workflow.model.Subscription;
import com.wso2telco.workflow.model.SubscriptionValidation;
import com.wso2telco.workflow.utils.ApprovelStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import com.wso2telco.dep.operatorservice.model.Operator;
import org.wso2.carbon.apimgt.impl.dao.ApiMgtDAO;
import org.wso2.carbon.apimgt.impl.dto.WorkflowDTO;
import com.wso2telco.dep.reportingservice.dao.WorkflowDAO;

public class SubscriptionApprovalImpl implements SubscriptionApproval {

    private static Log log = LogFactory.getLog(SubscriptionApprovalImpl.class);
    private WorkflowDbService dbservice = null;

    public void updateDBSubHubApproval(
            Subscription subHUBApprovalDBUpdateRequest) throws Exception {
        try {
        	String deploymentType = System.getProperty("DEPLOYMENT_TYPE", "hub");
            int appID = subHUBApprovalDBUpdateRequest.getApplicationID();
            String apiName = subHUBApprovalDBUpdateRequest.getApiName();
            int[] idList = null;
            int counter = 0;
            boolean isAdd = false;
            log.info("appID : " + appID + " | apiName : " + apiName);
            dbservice = new WorkflowDbService();
            
            List<OperatorEndPointDTO> operatorEndpoints = dbservice.getOperatorEndpoints();
            
            if(deploymentType == "hub"){
            	
            	String selectedOperators=dbservice.getSubApprovalOperators(apiName, subHUBApprovalDBUpdateRequest.getApiVersion(), subHUBApprovalDBUpdateRequest.getApiProvider(), appID);
                List<String> operatorList =  Arrays.asList(selectedOperators.split("\\s*,\\s*"));
                log.info("operatorList.size() : " + operatorList.size());
                idList = new int[operatorList.size()];
                for (Iterator iterator = operatorList.iterator(); iterator.hasNext(); ) {
                    String operatorName = (String) iterator.next();
                    int operatorId=dbservice.getOperatorIdByName(operatorName);
                    log.info("operator name : " + operatorName + "| operator id : " + operatorId);
                    for (Iterator iterator2 = operatorEndpoints.iterator(); iterator2.hasNext(); ) {
                        OperatorEndPointDTO operatorendpoint = (OperatorEndPointDTO) iterator2.next();
                        log.debug("operatorendpoint.getOperatorid : " + operatorendpoint.getOperatorid());
                        if (operatorId == operatorendpoint.getOperatorid() && operatorendpoint.getApi().equalsIgnoreCase(apiName)) {
                            log.info("operatorendpoint.getId : " + operatorendpoint.getId());
                            idList[counter] = operatorendpoint.getId();
                            isAdd = true;
                            break;
                        }
                    }
                    counter++;
                }
            } else {
            	
            	List<Operator> operatorList = dbservice.getOperators();
            	log.info("operatorList.size() : " + operatorList.size());
                idList = new int[operatorList.size()];
                for (Iterator iterator = operatorList.iterator(); iterator.hasNext(); ) {
                    Operator operator = (Operator) iterator.next();
                    log.info("operator name : " + operator.getOperatorName() + "| operator id : " + operator.getOperatorId());
                    for (Iterator iterator2 = operatorEndpoints.iterator(); iterator2.hasNext(); ) {
                        OperatorEndPointDTO operatorendpoint = (OperatorEndPointDTO) iterator2.next();
                        log.debug("operatorendpoint.getOperatorid : " + operatorendpoint.getOperatorid());
                        if (operator.getOperatorId() == operatorendpoint.getOperatorid() && operatorendpoint.getApi().equalsIgnoreCase(apiName)) {
                            log.info("operatorendpoint.getId : " + operatorendpoint.getId());
                            idList[counter] = operatorendpoint.getId();
                            isAdd = true;
                            break;
                        }
                    }
                    counter++;
                }
            }         
     
            log.info("idList : " + idList);
            if (isAdd) {
                dbservice.insertOperatorAppEndpoints(appID, idList);
                
                //Update tier of the subscription
                String workflowRefId = subHUBApprovalDBUpdateRequest.getWorkflowRefId();
                String selectedTier = subHUBApprovalDBUpdateRequest.getSelectedTier();

                ApiMgtDAO apiMgtDAO = ApiMgtDAO.getInstance();
                WorkflowDTO workflowDTO = apiMgtDAO.retrieveWorkflow(workflowRefId);
                String subscriptionId = workflowDTO.getWorkflowReference();

                WorkflowDAO workflowDAO = new WorkflowDAO();
                workflowDAO.updateSubscriptionTier(subscriptionId, selectedTier);
            }
        } catch (Exception e) {
            log.error("ERROR: Error occurred while updating  hub dep db for subscription HUB approval. " + e);
            throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
        }
    }


    public void updateDBSubOpApproval(
            Subscription subOpApprovalDBUpdateRequest) throws Exception {
        int appID = subOpApprovalDBUpdateRequest.getApplicationID();
        int opID;
        String statusStr = subOpApprovalDBUpdateRequest.getStatus();
        int operatorEndpointID = -1;
        String apiName = subOpApprovalDBUpdateRequest.getApiName();
        try {
            dbservice = new WorkflowDbService();
            opID = dbservice.getOperatorIdByName(subOpApprovalDBUpdateRequest.getOperatorName());
            List<OperatorEndPointDTO> operatorEndpoints = dbservice.getOperatorEndpoints();
            for (Iterator iterator = operatorEndpoints.iterator(); iterator.hasNext(); ) {
                OperatorEndPointDTO operatorendpoint = (OperatorEndPointDTO) iterator.next();
                if (operatorendpoint.getOperatorid() == opID && operatorendpoint.getApi().equalsIgnoreCase(apiName)) {
                    operatorEndpointID = operatorendpoint.getId();
                    break;
                }
            }
            if (operatorEndpointID > 0 && statusStr != null && statusStr.length() > 0) {
            	
            	dbservice.updateOperatorAppEndpointStatus(appID, operatorEndpointID, ApprovelStatus.valueOf(statusStr).getValue());
            }
        } catch (NumberFormatException e) {
            log.error("ERROR: NumberFormatException. " + e);
            throw new NumberFormatException();

        } catch (Exception e) {
            log.error("ERROR: Exception. " + e);
            throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
        }
    }


    public void insertValidatorForSubscription(
            SubscriptionValidation hUBApprovalSubValidatorRequest) throws Exception {
        int appID = hUBApprovalSubValidatorRequest.getApplicationID();
        int apiID = hUBApprovalSubValidatorRequest.getApiID();
        try {
        	
            dbservice = new WorkflowDbService();
            dbservice.insertValidatorForSubscription(appID, apiID, 1);
        } catch (Exception e) {
            log.error("ERROR: Exception. " + e);
            throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
        }
    }
}

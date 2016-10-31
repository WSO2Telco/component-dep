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
import com.wso2telco.dep.operatorservice.model.Operator;
import com.wso2telco.dep.operatorservice.model.OperatorEndPointDTO;
import com.wso2telco.workflow.dao.WorkflowDbService;
import com.wso2telco.workflow.model.Subscription;
import com.wso2telco.workflow.model.SubscriptionValidation;
import com.wso2telco.workflow.utils.ApprovelStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class SubscriptionApprovalImpl implements SubscriptionApproval {

    private static Log log = LogFactory.getLog(SubscriptionApprovalImpl.class);
    private WorkflowDbService dbservice = null;


    public void updateDBSubHubApproval(
            Subscription subHUBApprovalDBUpdateRequest) throws Exception {

        try {

            int appID = subHUBApprovalDBUpdateRequest.getApplicationID();
            String apiName = subHUBApprovalDBUpdateRequest.getApiName();
            int[] idList = null;
            int counter = 0;

            log.info("appID : " + appID + " | apiName : " + apiName);

            dbservice = new WorkflowDbService();
            List<Operator> operatorList = dbservice.getOperators();
            List<OperatorEndPointDTO> operatorEndpoints = dbservice.getOperatorEndpoints();

            log.info("operatorList.size() : " + operatorList.size());

            idList = new int[operatorList.size()];

            for (Iterator iterator = operatorList.iterator(); iterator.hasNext(); ) {
                Operator operator = (Operator) iterator.next();
                log.info("operator name : " + operator.getOperatorName() + "| operator id : " + operator.getOperatorId());

                for (Iterator iterator2 = operatorEndpoints.iterator(); iterator2.hasNext(); ) {
                    OperatorEndPointDTO operatorendpoint = (OperatorEndPointDTO) iterator2.next();
                    log.debug("operatorendpoint.getOperatorid : " + operatorendpoint.getOperatorid());

                    if (operator.getOperatorId() == operatorendpoint.getOperatorid()) {
                        log.info("operatorendpoint.getId : " + operatorendpoint.getId());
                        idList[counter] = operatorendpoint.getId();
                        break;
                    }
                }
                counter++;
            }

            log.info("idList : " + idList);

            dbservice.insertOperatorAppEndpoints(new Integer(appID).intValue(), idList);

        } catch (Exception e) {
            log.error("ERROR: Error occurred while updating axiatadb for subscription HUB approval. " + e.getStackTrace());
            throw new Exception();
        }
    }


    public void updateDBSubOpApproval(
            Subscription subOpApprovalDBUpdateRequest) throws Exception {

        int appID = subOpApprovalDBUpdateRequest.getApplicationID();
        int opID;
        String statusStr = subOpApprovalDBUpdateRequest.getStatus();
        int operatorEndpointID = -1;

        try {
            dbservice = new WorkflowDbService();
            opID = dbservice.getOperatorIdByName(subOpApprovalDBUpdateRequest.getOperatorName());
            List<OperatorEndPointDTO> operatorEndpoints = dbservice.getOperatorEndpoints();

            for (Iterator iterator = operatorEndpoints.iterator(); iterator.hasNext(); ) {
                OperatorEndPointDTO operatorendpoint = (OperatorEndPointDTO) iterator.next();
                if (operatorendpoint.getOperatorid() == new Integer(opID).intValue()) {
                    operatorEndpointID = operatorendpoint.getId();
                    break;
                }
            }

            if (operatorEndpointID > 0) {
                if (statusStr != null && statusStr.length() > 0) {
                    dbservice.updateOperatorAppEndpointStatus(new Integer(appID).intValue(), operatorEndpointID, ApprovelStatus.valueOf(statusStr).getValue());
                } else {
                    throw new BusinessException(GenaralError.UNDEFINED);
                }
            }

        } catch (NumberFormatException e) {
            log.error("ERROR: NumberFormatException. " + e.getStackTrace());
            throw new NumberFormatException();

        } catch (Exception e) {
            log.error("ERROR: Exception. " + e.getStackTrace());
            throw new Exception();
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
            log.error("ERROR: Exception. " + e.getStackTrace());
            throw new Exception();
        }


    }

}

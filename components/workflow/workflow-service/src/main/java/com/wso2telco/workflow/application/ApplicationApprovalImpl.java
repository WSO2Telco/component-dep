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

package com.wso2telco.workflow.application;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.GenaralError;
import com.wso2telco.dep.operatorservice.model.Operator;
import com.wso2telco.dep.reportingservice.dao.WorkflowDAO;
import com.wso2telco.workflow.dao.WorkflowDbService;
import com.wso2telco.workflow.model.Application;
import com.wso2telco.workflow.utils.ApprovelStatus;

import java.util.Iterator;
import java.util.List;

public class ApplicationApprovalImpl implements ApplicationApproval{

	private WorkflowDbService dbservice = null;

	public void updateDBAppHubApproval (
            Application appHUBApprovalDBUpdateRequest) throws Exception {
		
		int appID = appHUBApprovalDBUpdateRequest.getApplicationID();
        Integer[] opIDs;
        int counter = 0;
           dbservice = new WorkflowDbService();

        	List<Operator> operatorList = dbservice.getOperators();
        	opIDs = new Integer[operatorList.size()];
        	for (Iterator<Operator> iterator = operatorList.iterator(); iterator.hasNext();) {
        		Operator operator = (Operator) iterator.next();
				opIDs[counter] = operator.getOperatorId();
				counter++;
			}

          dbservice.applicationEntry(appID, opIDs);

          //Update tier of the application
          String selectedTier = appHUBApprovalDBUpdateRequest.getSelectedTier();
          WorkflowDAO workflowDAO = new WorkflowDAO();
          workflowDAO.updateApplicationTier(String.valueOf(appID), selectedTier);
	}


	public void updateDBAppOpApproval(
            Application appOpApprovalDBUpdateRequest) throws Exception{
		
		int appID = appOpApprovalDBUpdateRequest.getApplicationID();
        int opID;
        String statusStr = appOpApprovalDBUpdateRequest.getStatus();

        if (statusStr != null && statusStr.length() > 0) {

        		dbservice = new WorkflowDbService();
                opID=dbservice.getOperatorIdByName(appOpApprovalDBUpdateRequest.getOperatorName());
				dbservice.updateAppApprovalStatusOp(appID,opID,ApprovelStatus.valueOf(statusStr).getValue());

        } else {
            throw new BusinessException(GenaralError.UNDEFINED);
        }
		
	}

}

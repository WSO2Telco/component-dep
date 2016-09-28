package com.wso2telco.workflow.application;


import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.GenaralError;
import com.wso2telco.dep.operatorservice.model.Operator;
import com.wso2telco.workflow.dao.WorkflowDbService;
import com.wso2telco.workflow.model.Application;
import com.wso2telco.workflow.utils.ApprovelStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import java.util.Iterator;
import java.util.List;

public class ApplicationApprovalImpl implements ApplicationApproval{

	private static Log log = LogFactory.getLog(ApplicationApprovalImpl.class);
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

          dbservice.applicationEntry(new Integer(appID).intValue(), opIDs);

	}


	public void updateDBAppOpApproval(
            Application appOpApprovalDBUpdateRequest) throws Exception{
		
		int appID = appOpApprovalDBUpdateRequest.getApplicationID();
        int opID;
        String statusStr = appOpApprovalDBUpdateRequest.getStatus();

        if (statusStr != null && statusStr.length() > 0) {

        		dbservice = new WorkflowDbService();
                opID=dbservice.getOperatorIdByName(appOpApprovalDBUpdateRequest.getOperatorName());
				dbservice.updateAppApprovalStatusOp(new Integer(appID).intValue(), new Integer(opID).intValue(),ApprovelStatus.valueOf(statusStr).getValue());

        } else {
            throw new BusinessException(GenaralError.UNDEFINED);
        }
		
	}

}

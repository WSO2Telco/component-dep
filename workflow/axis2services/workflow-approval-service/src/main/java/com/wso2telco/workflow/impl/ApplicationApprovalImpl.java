package com.wso2telco.workflow.impl;

import java.util.Iterator;
import java.util.List;

import com.wso2telco.dbutils.AxiataDbService;
import com.wso2telco.dbutils.Operator;
import com.wso2telco.workflow.application.*;
import com.wso2telco.workflow.in.ApplicationApproval;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ApplicationApprovalImpl implements ApplicationApproval {
	
	private static final String CONST_APPROVED = "APPROVED";
	private static final String CONST_REJECTED = "REJECTED";
	
	private static Log log = LogFactory.getLog(ApplicationApprovalImpl.class);
    private AxiataDbService dbservice = null;

	@Override
	public void updateDBAppHubApproval(
			AppHUBApprovalDBUpdateRequestType appHUBApprovalDBUpdateRequest) {
		
		String appID = appHUBApprovalDBUpdateRequest.getAppID();
        Integer[] opIDs = null;
        int counter = 0;
        
        try {
        	dbservice = new AxiataDbService();
        	
        	List<Operator> operatorList = dbservice.getOperators();
        	opIDs = new Integer[operatorList.size()];
        	for (Iterator<Operator> iterator = operatorList.iterator(); iterator.hasNext();) {
        		Operator operator = (Operator) iterator.next();
				opIDs[counter] = operator.getOperatorid();
				counter++;
			}
        	
			dbservice.applicationEntry(new Integer(appID).intValue(), opIDs);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void updateDBAppOpApproval(
			AppOpApprovalDBUpdateRequestType appOpApprovalDBUpdateRequest) {
		
		String appID = appOpApprovalDBUpdateRequest.getAppID();
        String opID = appOpApprovalDBUpdateRequest.getOpID();
        String statusStr = appOpApprovalDBUpdateRequest.getStatus();
        int status = -1;
        
        if (statusStr != null && statusStr.length() > 0) {
        	if(statusStr.equalsIgnoreCase(CONST_APPROVED)) {
        		status = 1;
        		
        	} else if(statusStr.equalsIgnoreCase(CONST_REJECTED)) {
        		status = 2;
        		
        	} else {
        		status = 0;
        	}
        	
        	try {
        		dbservice = new AxiataDbService();
				dbservice.updateAppApprovalStatusOp(new Integer(appID).intValue(), new Integer(opID).intValue(), status);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
        } else {
        	log.error("ERROR: 'status' is either null or empty.");
        }
		
	}

}

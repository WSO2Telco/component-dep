package com.wso2telco.workflow.subscription;



import com.wso2telco.core.dbutils.Operator;
import com.wso2telco.core.dbutils.Operatorendpoint;
import com.wso2telco.workflow.dao.WorkflowDbService;
import com.wso2telco.workflow.model.Subscription;
import com.wso2telco.workflow.model.SubscriptionValidation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import java.util.Iterator;
import java.util.List;
import java.util.Map;

//import org.wso2.carbon.apimgt.api.APIManagementException;


public class SubscriptionApprovalImpl implements SubscriptionApproval{
	
	private static final String CONST_APPROVED = "APPROVED";
	private static final String CONST_REJECTED = "REJECTED";
	
    private static Log log = LogFactory.getLog(SubscriptionApprovalImpl.class);
    private WorkflowDbService dbservice = null;


	public void updateDBSubHubApproval(
            Subscription subHUBApprovalDBUpdateRequest) throws Exception{
		
		Map<String , String> apiKeyMapping = null;
		try {
            WorkflowDbService workflowDbService =new WorkflowDbService();
			apiKeyMapping = workflowDbService.getWorkflowAPIKeyMappings();
			
			int appID = subHUBApprovalDBUpdateRequest.getApplicationID();
			String apiName = subHUBApprovalDBUpdateRequest.getApiName();
			int[] idList = null;
			int counter = 0;
			
			log.info("appID : " + appID + " | apiName : " + apiName);
			

				
				String apiKey = apiKeyMapping.get(apiName);
				//log.info("apiKey : " + apiKey);
				
				if (apiKey != null && !apiKey.isEmpty()) {
					dbservice = new WorkflowDbService();
					List<Operator> operatorList = dbservice.getOperators();
					List<Operatorendpoint> operatorEndpoints = dbservice.getOperatorEndpoints();
				
					log.info("operatorList.size() : " + operatorList.size());
					
					idList = new int[operatorList.size()];
				
					for (Iterator iterator = operatorList.iterator(); iterator.hasNext();) {
						Operator operator = (Operator) iterator.next();
						log.info("operator name : " + operator.getOperatorname() + "| operator id : " + operator.getOperatorid());
					
						for (Iterator iterator2 = operatorEndpoints.iterator(); iterator2.hasNext();) {
							Operatorendpoint operatorendpoint = (Operatorendpoint) iterator2.next();
							log.debug("operatorendpoint.getOperatorid : " + operatorendpoint.getOperatorid());
						
							if(operator.getOperatorid() == operatorendpoint.getOperatorid()
									&& apiKey.equals(operatorendpoint.getApi())) {
								
								log.info("operatorendpoint.getId : " + operatorendpoint.getId());
								idList[counter] = operatorendpoint.getId(); 
								break;
							}
						}
						counter++;
					}
				
					log.info("idList : " + idList);
					
					dbservice.insertOperatorAppEndpoints(new Integer(appID).intValue(), idList);
				}else {
					log.error("Please insert API Name into workflow_api_key_mappings table in axiatadb. ");
				}
				

			
		} catch (Exception e) {
			log.error("ERROR: Error occurred while updating axiatadb for subscription HUB approval. " + e.getStackTrace());
            throw new Exception();
		}
	}


	public void updateDBSubOpApproval(
            Subscription subOpApprovalDBUpdateRequest) throws Exception{
		
		int appID = subOpApprovalDBUpdateRequest.getApplicationID();
		String opID = subOpApprovalDBUpdateRequest.getOpID();
		String apiName = subOpApprovalDBUpdateRequest.getApiName();
		String statusStr = subOpApprovalDBUpdateRequest.getStatus();
		int status = -1;
					
		int operatorEndpointID = -1;
		
		try {
            WorkflowDbService workflowDbService =new WorkflowDbService();
			Map<String , String> apiKeyMapping = workflowDbService.getWorkflowAPIKeyMappings();
			String apiKey = apiKeyMapping.get(apiName);
			
			dbservice = new WorkflowDbService();
			List<Operatorendpoint> operatorEndpoints = dbservice.getOperatorEndpoints();
			
			for (Iterator iterator = operatorEndpoints.iterator(); iterator
					.hasNext();) {
				Operatorendpoint operatorendpoint = (Operatorendpoint) iterator
						.next();
				
				if(operatorendpoint.getOperatorid() == new Integer(opID).intValue() && apiKey.equals(operatorendpoint.getApi())) {
					operatorEndpointID = operatorendpoint.getId();
					break;
				}
			}
			
			if (operatorEndpointID > 0) {
				if (statusStr != null && statusStr.length() > 0) {
	            	if(statusStr.equalsIgnoreCase(CONST_APPROVED)) {
	            		status = 1;
	            		
	            	} else if(statusStr.equalsIgnoreCase(CONST_REJECTED)) {
	            		status = 2;
	            		
	            	} else {
	            		status = 0;
	            	}
	            	
	            	try {
	            		dbservice.updateOperatorAppEndpointStatus(new Integer(appID).intValue(), operatorEndpointID, status);
	    				
	    			} catch (Exception e) {
	    				e.printStackTrace();
	    			}
	            } else {
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
            SubscriptionValidation hUBApprovalSubValidatorRequest) throws Exception{
		
		int appID = hUBApprovalSubValidatorRequest.getApplicationID();
		int apiID = hUBApprovalSubValidatorRequest.getApiID();

			try {
				dbservice = new WorkflowDbService();
				dbservice.insertValidatorForSubscription(appID,apiID, 1);
				
			} catch (Exception e) {
                log.error("ERROR: Exception. " + e.getStackTrace());
                throw new Exception();
			}

		
	}

}

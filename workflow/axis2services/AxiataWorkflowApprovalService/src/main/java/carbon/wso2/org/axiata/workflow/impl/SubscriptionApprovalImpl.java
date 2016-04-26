package carbon.wso2.org.axiata.workflow.impl;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.xml.namespace.QName;
import carbon.wso2.org.axiata.workflow.dao.WorkflowDAO;
import carbon.wso2.org.axiata.workflow.in.SubscriptionApproval;
import carbon.wso2.org.axiata.workflow.subscription.*;
import com.wso2telco.dbutils.AxiataDbService;
import com.wso2telco.dbutils.Operator;
import com.wso2telco.dbutils.Operatorendpoint;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//import org.wso2.carbon.apimgt.api.APIManagementException;


public class SubscriptionApprovalImpl implements SubscriptionApproval {
	
	private static final String CONST_APPROVED = "APPROVED";
	private static final String CONST_REJECTED = "REJECTED";
	
    private static Log log = LogFactory.getLog(SubscriptionApprovalImpl.class);
    private AxiataDbService dbservice = null;

	@Override
	public void updateDBSubHubApproval(
			SubHUBApprovalDBUpdateRequestType subHUBApprovalDBUpdateRequest) {
		
		Map<String , String> apiKeyMapping = null;
		try {
			apiKeyMapping = WorkflowDAO.getWorkflowAPIKeyMappings();
			
			String appID = subHUBApprovalDBUpdateRequest.getAppID();
			String apiName = subHUBApprovalDBUpdateRequest.getApiName();
			int[] idList = null;
			int counter = 0;
			
			log.debug("appID : " + appID + " | apiName : " + apiName); 
			
			if(appID != null && !appID.isEmpty() && apiName != null && !apiName.isEmpty()) {
				
				String apiKey = apiKeyMapping.get(apiName);
				log.debug("apiKey : " + apiKey);
				
				if (apiKey != null && !apiKey.isEmpty()) {
					dbservice = new AxiataDbService();
					List<Operator> operatorList = dbservice.getOperators();
					List<Operatorendpoint> operatorEndpoints = dbservice.getOperatorEndpoints();
				
					log.debug("operatorList.size() : " + operatorList.size());
					
					idList = new int[operatorList.size()];
				
					for (Iterator iterator = operatorList.iterator(); iterator.hasNext();) {
						Operator operator = (Operator) iterator.next();
						log.debug("operator name : " + operator.getOperatorname() + "| operator id : " + operator.getOperatorid());
					
						for (Iterator iterator2 = operatorEndpoints.iterator(); iterator2.hasNext();) {
							Operatorendpoint operatorendpoint = (Operatorendpoint) iterator2.next();
							log.debug("operatorendpoint.getOperatorid : " + operatorendpoint.getOperatorid());
						
							if(operator.getOperatorid() == operatorendpoint.getOperatorid()
									&& apiKey.equals(operatorendpoint.getApi())) {
								
								log.debug("operatorendpoint.getId : " + operatorendpoint.getId());
								idList[counter] = operatorendpoint.getId(); 
								break;
							}
						}
						counter++;
					}
				
					log.debug("idList : " + idList);
					
					dbservice.insertOperatorAppEndpoints(new Integer(appID).intValue(), idList);
				}else {
					log.error("Please insert API Name into workflow_api_key_mappings table in axiatadb. ");
				}
				
			} else {
				log.error("ERROR: 'apiName' is either null or empty.");
			}
			
		} catch (Exception e) {
			log.error("ERROR: Error occurred while updating axiatadb for subscription HUB approval. " + e.getStackTrace());
			e.printStackTrace();
		}
	}

	@Override
	public void updateDBSubOpApproval(
			SubOpApprovalDBUpdateRequestType subOpApprovalDBUpdateRequest) {
		
		String appID = subOpApprovalDBUpdateRequest.getAppID();
		String opID = subOpApprovalDBUpdateRequest.getOpID();
		String apiName = subOpApprovalDBUpdateRequest.getApiName();
		String statusStr = subOpApprovalDBUpdateRequest.getStatus();
		int status = -1;
					
		int operatorEndpointID = -1;
		
		try {
			
			Map<String , String> apiKeyMapping = WorkflowDAO.getWorkflowAPIKeyMappings();
			String apiKey = apiKeyMapping.get(apiName);
			
			dbservice = new AxiataDbService();
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
			e.printStackTrace();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public SubOperatorRetrievalResponse retrieveSubOperatorList(
			SubOperatorRetrievalRequestType subOperatorRetrievalRequest) {
		
		SubOperatorRetrievalResponse response = null;
		
		String apiName = subOperatorRetrievalRequest.getApiName();
		String apiVersion = subOperatorRetrievalRequest.getApiVersion();
		String apiProvider = subOperatorRetrievalRequest.getApiProvider();
		int appId = new Integer(subOperatorRetrievalRequest.getAppId()).intValue();
		
		log.debug("apiName : " + apiName);
		log.debug("apiVersion : " + apiVersion);
		log.debug("apiProvider : " + apiProvider);
		log.debug("appId : " + appId);
		
		try {
			String operators = WorkflowDAO.getSubApprovalOperators(apiName, apiVersion, apiProvider, appId);
			log.debug("operators : " + operators);
			
			response = new SubOperatorRetrievalResponse();
			SubOperatorRetrievalResponseType responseType = new SubOperatorRetrievalResponseType();
			responseType.setSubOperatorList(operators);
			response.setSubOperatorRetrievalResponse(responseType);
			
		} catch (Exception e) {
			log.error("Error occured in retrieving subscription operator list. " + e.getMessage());
			e.printStackTrace();
		}
		
		return response;
	}

	@Override
	public void insertValidatorForSubscription(
			HUBApprovalSubValidatorRequestType hUBApprovalSubValidatorRequest) {
		
		String appID = hUBApprovalSubValidatorRequest.getAppID();
		String apiID = hUBApprovalSubValidatorRequest.getApiID();
		
		if(appID != null && appID.length() > 0 && apiID != null && apiID.length() > 0) {
			try {
				dbservice = new AxiataDbService();
				dbservice.insertValidatorForSubscription(new Integer(appID).intValue(), new Integer(apiID).intValue(), 1);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} else {
			log.error("ERROR: Invalid 'appId' or 'apiID'.");
		}
		
	}

}

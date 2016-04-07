package com.wso2telco.custom.hostobjects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.*;
import org.wso2.carbon.apimgt.api.APIConsumer;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.model.Tier;
import org.wso2.carbon.apimgt.impl.APIManagerFactory;
import org.wso2.carbon.apimgt.impl.dao.ApiMgtDAO;
import org.wso2.carbon.apimgt.impl.dto.WorkflowDTO;

import com.wso2telco.custom.dao.AxiataWorkflowDAO;
import com.wso2telco.custom.hostobjects.southbound.SbHostObjectUtils;
import com.wso2telco.custom.hostobjects.util.ChargeRate;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Host object to hanled Axiata workflow related tasks. 
 */
public class WorkflowHostObject extends ScriptableObject {

	private static final Log log = LogFactory.getLog(WorkflowHostObject.class);
	private String hostobjectName = "AxiataWorkflow";
	private static String CONST_FAILURE = "FAILURE";
	private static String CONST_SUCCESS = "SUCCESS";

	@Override
	public String getClassName() {
		return hostobjectName;
	}

	public WorkflowHostObject() {
		log.info("::: Initialized HostObject ");
	}

	/**
	 * This method sets the specified tier for a given subscription.
	 * @param cx
	 * @param thisObj
	 * @param args
	 * @param funObj
	 * @return
	 * @throws APIManagementException
	 */
	public static String jsFunction_setSubscriptionTier(Context cx,
													Scriptable thisObj, Object[] args, Function funObj)
													throws APIManagementException {
		
		String status = "";
		
		if (args == null || args.length == 0) {
			handleException("Invalid number of parameters.");
		}
		
		String workflowReference = (String) args[0];
		String tierId = (String) args[1];
		
		if(workflowReference != null) {
			ApiMgtDAO apiMgtDAO = new ApiMgtDAO();
			WorkflowDTO workflowDTO = apiMgtDAO.retrieveWorkflow(workflowReference);
			
			if(workflowDTO != null) {
				
				String subscriptionId = workflowDTO.getWorkflowReference();
				AxiataWorkflowDAO axiataWorkflowDAO = new AxiataWorkflowDAO();
				axiataWorkflowDAO.updateSubscriptionTier(subscriptionId, tierId);
				
				status = CONST_SUCCESS;
				
			} else {
				status = CONST_FAILURE;
			}
		} else {
			status = CONST_FAILURE;
		}
		return status;
	}
        
        
        public static String jsFunction_setSubscriptionChargeRate(Context cx, Scriptable thisObj, Object[] args, Function funObj) throws APIManagementException {
            if (args == null || args.length == 0) {
                handleException("Invalid number of parameters.");
            }
            
            String appId = (String) args[0];
            String apiId = (String) args[1];
            String opName = (String) args[2];
            
            String status = "";
            try {
                AxiataWorkflowDAO axiataWorkflowDAO = new AxiataWorkflowDAO();
                axiataWorkflowDAO.saveSubscriptionChargeRate(appId, apiId, opName);
                status = CONST_SUCCESS;
            } catch (Exception e) {
                status = CONST_FAILURE;
            }
            return status;
	}

    public static String jsFunction_setSubscriptionChargeRateNB(Context cx, Scriptable thisObj, Object[] args, Function funObj) throws APIManagementException {
        if (args == null || args.length == 0) {
            handleException("Invalid number of parameters.");
        }

        String appId = (String) args[0];
        String apiId = (String) args[1];

        String status = "";
        try {
            AxiataWorkflowDAO axiataWorkflowDAO = new AxiataWorkflowDAO();
            axiataWorkflowDAO.saveSubscriptionChargeRateNB(appId, apiId);
            status = CONST_SUCCESS;
        } catch (Exception e) {
            status = CONST_FAILURE;
        }
        return status;
    }
        
    public static NativeArray jsFunction_getSubscriptionRatesForOperator(Context cx,
            Scriptable thisObj, Object[] args, Function funObj)
            throws APIManagementException {    
        
        String apiName = (String) args[0];
        String opName = (String) args[1];

        List<ChargeRate> rateList = SbHostObjectUtils.getRatesForOperatorApi(opName, apiName);
        
        NativeArray nativeArray = new NativeArray(0);
        try {
            int i = 0;
            for (ChargeRate rate : rateList) {
                NativeObject row = new NativeObject();
                row.put("rateName", row, rate.getName());
                row.put("isDefault", row, rate.isDefault());
                nativeArray.put(i, nativeArray, row);
                i++;
            }
        } catch (Exception e){
            log.error("Error occurred getSubscriptionRatesForOperator");
            log.error(e.getStackTrace());
            handleException("Error occurred while getting getSubscriptionRatesForOperator", e);
        }
        return nativeArray;
    }
        
    public static NativeArray jsFunction_getTierDetails(Context cx,
            Scriptable thisObj, Object[] args, Function funObj)
            throws APIManagementException {    
        NativeArray nativeArray = new NativeArray(0);
        Set<Tier> tiers;
        try {
            APIConsumer apiConsumer = APIManagerFactory.getInstance().getAPIConsumer();
            tiers = apiConsumer.getTiers();
            
            int i = 0;
            for (Tier tier : tiers) {
                NativeObject row = new NativeObject();
                row.put("tierName", row, tier.getName());
                if(tier.getTierAttributes() != null){
                    row.put("tierAttributes", row, getTierAttributesString(tier.getTierAttributes()));
                }else{
                    NativeArray tempArray = new NativeArray(0);
                    row.put("tierAttributes", row, tempArray);
                }
                nativeArray.put(i, nativeArray, row);
                i++;
            }
        } catch (APIManagementException apiManagementException) {
            log.error("Error occurred getTierDetails");
            log.error(apiManagementException.getStackTrace());
            handleException("Error occurred while getting tier details", apiManagementException);
        } catch (Exception e){
            log.error("Error occurred getTierDetails");
            log.error(e.getStackTrace());
            handleException("Error occurred while getting tier details", e);
        }
        return nativeArray;
    }
    
    private static NativeArray getTierAttributesString(Map<String, Object> tierAttributesMap) {
        NativeArray tierAttributesList = new NativeArray(0);
        Iterator tierAttributesIterator = tierAttributesMap.entrySet().iterator();
        int i = 0;
        while (tierAttributesIterator.hasNext()) {
            Map.Entry tierAttribute = (Map.Entry) tierAttributesIterator.next();
            NativeObject row = new NativeObject();
            row.put("tierAttributeName", row, tierAttribute.getKey().toString());
            row.put("tierAttributevalue", row, tierAttribute.getValue().toString());
            tierAttributesList.put(i, tierAttributesList, row);
            i++;
        }
        return tierAttributesList;
    }
	/**
	 * This method sets the specified tier for a given application.
	 * @param cx
	 * @param thisObj
	 * @param args
	 * @param funObj
	 * @return
	 * @throws APIManagementException
	 */
	public static String jsFunction_setApplicationTier(Context cx,
													Scriptable thisObj, Object[] args, Function funObj)
													throws APIManagementException {
		
		String status = "";
		
		if (args == null || args.length == 0) {
			handleException("Invalid number of parameters.");
		}
		
		String workflowReference = (String) args[0];
		String tierId = (String) args[1];
		
		if(workflowReference != null) {
			ApiMgtDAO apiMgtDAO = new ApiMgtDAO();
			WorkflowDTO workflowDTO = apiMgtDAO.retrieveWorkflow(workflowReference);
			
			if(workflowDTO != null) {
				
				String applicationId = workflowDTO.getWorkflowReference();
				AxiataWorkflowDAO axiataWorkflowDAO = new AxiataWorkflowDAO();
				axiataWorkflowDAO.updateApplicationTier(applicationId, tierId);
				
				status = CONST_SUCCESS;
				
			} else {
				status = CONST_FAILURE;
			}
		} else {
			status = CONST_FAILURE;
		}
		return status;
	}
	
	/**
	 * Handle expection.
	 * @param msg
	 * @throws APIManagementException
	 */
	private static void handleException(String msg) throws APIManagementException {
		log.error(msg);
		throw new APIManagementException(msg);
	}

	/**
	 * Handle expection.
	 * @param msg
	 * @param t
	 * @throws APIManagementException
	 */
	private static void handleException(String msg, Throwable t) throws APIManagementException {
		log.error(msg, t);
		throw new APIManagementException(msg, t);
	}
}

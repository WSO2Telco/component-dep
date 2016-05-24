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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.*;
import org.wso2.carbon.apimgt.api.APIConsumer;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.model.Tier;
import org.wso2.carbon.apimgt.impl.APIManagerFactory;
import org.wso2.carbon.apimgt.impl.dao.ApiMgtDAO;
import org.wso2.carbon.apimgt.impl.dto.WorkflowDTO;

import com.wso2telco.dep.reportingservice.dao.WorkflowDAO;
import com.wso2telco.dep.reportingservice.southbound.SbHostObjectUtils;
import com.wso2telco.dep.reportingservice.util.ChargeRate;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

 
// TODO: Auto-generated Javadoc
/**
 * The Class WorkflowHostObject.
 */
public class WorkflowHostObject extends ScriptableObject {

	/** The Constant log. */
	private static final Log log = LogFactory.getLog(WorkflowHostObject.class);
	
	/** The hostobject name. */
	private String hostobjectName = "WorkflowHostObject";
	
	/** The const failure. */
	private static String CONST_FAILURE = "FAILURE";
	
	/** The const success. */
	private static String CONST_SUCCESS = "SUCCESS";

	/* (non-Javadoc)
	 * @see org.mozilla.javascript.ScriptableObject#getClassName()
	 */
	@Override
	public String getClassName() {
		return hostobjectName;
	}

	/**
	 * Instantiates a new workflow host object.
	 */
	public WorkflowHostObject() {
		log.info("::: Initialized HostObject ");
	}

	 
	/**
	 * Js function_set subscription tier.
	 *
	 * @param cx the cx
	 * @param thisObj the this obj
	 * @param args the args
	 * @param funObj the fun obj
	 * @return the string
	 * @throws APIManagementException the API management exception
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
				WorkflowDAO WorkflowDAO = new WorkflowDAO();
				WorkflowDAO.updateSubscriptionTier(subscriptionId, tierId);
				
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
         * Js function_set subscription charge rate.
         *
         * @param cx the cx
         * @param thisObj the this obj
         * @param args the args
         * @param funObj the fun obj
         * @return the string
         * @throws APIManagementException the API management exception
         */
        public static String jsFunction_setSubscriptionChargeRate(Context cx, Scriptable thisObj, Object[] args, Function funObj) throws APIManagementException {
            if (args == null || args.length == 0) {
                handleException("Invalid number of parameters.");
            }
            
            String appId = (String) args[0];
            String apiId = (String) args[1];
            String opName = (String) args[2];
            
            String status = "";
            try {
                WorkflowDAO WorkflowDAO = new WorkflowDAO();
                WorkflowDAO.saveSubscriptionChargeRate(appId, apiId, opName);
                status = CONST_SUCCESS;
            } catch (Exception e) {
                status = CONST_FAILURE;
            }
            return status;
	}

    /**
     * Js function_set subscription charge rate nb.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return the string
     * @throws APIManagementException the API management exception
     */
    public static String jsFunction_setSubscriptionChargeRateNB(Context cx, Scriptable thisObj, Object[] args, Function funObj) throws APIManagementException {
        if (args == null || args.length == 0) {
            handleException("Invalid number of parameters.");
        }

        String appId = (String) args[0];
        String apiId = (String) args[1];

        String status = "";
        try {
            WorkflowDAO WorkflowDAO = new WorkflowDAO();
            WorkflowDAO.saveSubscriptionChargeRateNB(appId, apiId);
            status = CONST_SUCCESS;
        } catch (Exception e) {
            status = CONST_FAILURE;
        }
        return status;
    }
        
    /**
     * Js function_get subscription rates for operator.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return the native array
     * @throws APIManagementException the API management exception
     */
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
        
    /**
     * Js function_get tier details.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return the native array
     * @throws APIManagementException the API management exception
     */
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
    
    /**
     * Gets the tier attributes string.
     *
     * @param tierAttributesMap the tier attributes map
     * @return the tier attributes string
     */
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
	 * Js function_set application tier.
	 *
	 * @param cx the cx
	 * @param thisObj the this obj
	 * @param args the args
	 * @param funObj the fun obj
	 * @return the string
	 * @throws APIManagementException the API management exception
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
				WorkflowDAO WorkflowDAO = new WorkflowDAO();
				WorkflowDAO.updateApplicationTier(applicationId, tierId);
				
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
	 * Handle exception.
	 *
	 * @param msg the msg
	 * @throws APIManagementException the API management exception
	 */
	private static void handleException(String msg) throws APIManagementException {
		log.error(msg);
		throw new APIManagementException(msg);
	}

	 
	/**
	 * Handle exception.
	 *
	 * @param msg the msg
	 * @param t the t
	 * @throws APIManagementException the API management exception
	 */
	private static void handleException(String msg, Throwable t) throws APIManagementException {
		log.error(msg, t);
		throw new APIManagementException(msg, t);
	}
}

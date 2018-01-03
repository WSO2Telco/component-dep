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

package com.wso2telco.dep.operatorservice.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.GenaralError;
import com.wso2telco.core.dbutils.exception.ThrowableError;
import com.wso2telco.dep.operatorservice.dao.OperatorDAO;
import com.wso2telco.dep.operatorservice.dao.WorkflowDAO;
import com.wso2telco.dep.operatorservice.exception.APIException;
import com.wso2telco.dep.operatorservice.exception.APIException.APIErrorType;
import com.wso2telco.dep.operatorservice.exception.ApplicationException;
import com.wso2telco.dep.operatorservice.exception.ApplicationException.ApplicationErrorType;
import com.wso2telco.dep.operatorservice.exception.TokenException;
import com.wso2telco.dep.operatorservice.exception.TokenException.TokenErrorType;
import com.wso2telco.dep.operatorservice.model.Operator;
import com.wso2telco.dep.operatorservice.model.OperatorApplicationDTO;
import com.wso2telco.dep.operatorservice.model.OperatorEndPointDTO;
import com.wso2telco.dep.operatorservice.model.OperatorSearchDTO;
import com.wso2telco.dep.operatorservice.model.ProvisionReq;
import com.wso2telco.dep.operatorservice.util.OparatorError;

public class OparatorService {

	/** The Constant log. */
	Log LOG = LogFactory.getLog(OparatorService.class);

	OperatorDAO dao;

	{
		dao = new OperatorDAO();
	}

	/**
	 * load all operators according to OperatorSearchDTO filters
	 * 
	 * @param searchDTO
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Operator> loadOperators(OperatorSearchDTO searchDTO) throws BusinessException {

		LOG.debug(" Got request to loadOperators  searchDTO : " + searchDTO);
		List<Operator> operatorList = null;

		try {

			operatorList = dao.seachOparators(searchDTO);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		if (operatorList != null) {

			return operatorList;
		} else {

			return Collections.emptyList();
		}
	}

	public void blacklistAggregator(ProvisionReq provisionreq) throws BusinessException {

		Integer applicationid = (provisionreq.getProvisioninfo().getApplicationid() != null)
				? Integer.parseInt(provisionreq.getProvisioninfo().getApplicationid()) : null;

		String subscriber = provisionreq.getProvisioninfo().getSubscriber();
		String operator = provisionreq.getProvisioninfo().getOperatorcode();
		String[] merchants = (String[]) (provisionreq.getProvisioninfo().getMerchantcode())
				.toArray(new String[(provisionreq.getProvisioninfo().getMerchantcode()).size()]);

		LOG.debug(" applicationid :" + applicationid + " subscriber:" + subscriber + " operator:" + operator
				+ " merchants :" + StringUtils.join(merchants));
		/**
		 * response created
		 */
		try {
			/**
			 * check for macing operator for given operator name
			 */
			OperatorSearchDTO searchDTO = new OperatorSearchDTO();
			searchDTO.setName(operator);
			List<Operator> oparators = dao.seachOparators(searchDTO);

			/**
			 * No operators found :return
			 */
			if (oparators == null || oparators.isEmpty()) {
				throw new BusinessException(OparatorError.INVALID_OPARATOR_NAME);
			}

			/**
			 * if operators available first one will taken out
			 */
			Operator oparator = oparators.get(0);

			dao.insertBlacklistAggregatoRows(applicationid, subscriber, oparator.getOperatorId(), merchants);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings("unchecked")
	public List<OperatorEndPointDTO> getOperatorEndpoints() throws BusinessException {

		List<OperatorEndPointDTO> endPoints = null;

		try {

			endPoints = dao.getOperatorEndpoints();
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		if (endPoints != null) {

			return endPoints;
		} else {

			return Collections.emptyList();
		}
	}

	@SuppressWarnings("unchecked")
	public List<OperatorApplicationDTO> getApplicationOperators(Integer applicationId)
			throws ApplicationException, BusinessException {

		if (applicationId == null || applicationId <= 0) {

			throw new ApplicationException(ApplicationErrorType.INVALID_APPLICATION_ID);
		}

		List<OperatorApplicationDTO> operators = null;

		try {

			operators = dao.getApplicationOperators(applicationId);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		if (operators != null) {

			return operators;
		} else {

			return Collections.emptyList();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Integer> getActiveApplicationOperators(Integer appId, String apiType)
			throws ApplicationException, APIException, BusinessException {

		if (appId == null || appId <= 0) {

			throw new ApplicationException(ApplicationErrorType.INVALID_APPLICATION_ID);
		}

		if (apiType == null || apiType.trim().length() <= 0) {

			throw new APIException(APIErrorType.INVALID_API_NAME);
		}

		List<Integer> operators = null;

		try {

			operators = dao.getActiveApplicationOperators(appId, apiType);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		if (operators != null) {

			return operators;
		} else {

			return Collections.emptyList();
		}
	}

	public void updateOperatorToken(int operatorId, String refreshToken, long tokenValidity, long tokenTime,
			String token) throws TokenException, BusinessException {

		if (operatorId <= 0) {

			throw new BusinessException(OparatorError.INVALID_OPARATOR_ID); 
		}

		if (refreshToken == null || refreshToken.trim().length() <= 0) {

			throw new TokenException(TokenErrorType.INVALID_REFRESH_TOKEN);
		}

		if (tokenValidity <= 0) {

			throw new TokenException(TokenErrorType.INVALID_TOKEN_VALIDITY);
		}

		if (tokenTime <= 0) {

			throw new TokenException(TokenErrorType.INVALID_TOKEN_TIME);
		}

		if (token == null || token.trim().length() <= 0) {

			throw new TokenException(TokenErrorType.INVALID_TOKEN);
		}

		try {

			dao.updateOperatorToken(operatorId, refreshToken, tokenValidity, tokenTime, token);
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}
	}

    public List<Operator> retrieveOperatorList() throws Exception {
          return dao.retrieveOperatorList();
    }

    public Map<Integer, Map<String, Map<String,String>>> getOperatorApprovedSubscriptionsByApplicationId(int appId) throws Exception {

		Map<Integer, Map<String, Map<String,String>>> historyDetails = null;

		try {

			historyDetails = dao.getOperatorApprovedSubscriptionsByApplicationId(appId);
		} catch (Exception e) {

			throw e;
		}

		if (historyDetails != null) {

			return historyDetails;
		} else {

			return Collections.emptyMap();
		}
	}
    
    public List<String> getOparatorApprovedApp(String appIds,final String oparatorName) throws BusinessException {
    	LOG.debug("calling getOparatorApprovedApp ,appIds:"+appIds);
    	
    	List<String> returnList= Collections.emptyList();
    	
    	ThrowableError emptyAppId = new ThrowableError() {
			
			@Override
			public String getMessage() {
				return "API ids can not be empty";
			}
			
			@Override
			public String getCode() {
				return "SUB009";
			}
		};
		
		appIds= appIds.replace("'","");
    	if(appIds==null || appIds.trim().length()==0 ) {
    		LOG.debug("parameter null appIds");
    	throw	new BusinessException(emptyAppId);
    	}
    	
    	List<String> appIdList = new ArrayList<String>();
    	
    	WorkflowDAO dao = new WorkflowDAO();
    	String []  apps = appIds.split(",") ;
     	
    	if(apps==null || apps.length==0 ) {
    		LOG.debug("parameter null appIds");
    		throw new BusinessException(emptyAppId);
    	}
    	
    	for (String string : apps) {
    		try {
				Integer.parseInt(string);
				appIdList.add(string);
			} catch (Exception e) {
				//ignore and continue
			}
		}
    	
    	if(appIdList.size()==0) {

    		LOG.debug("parameter null appIds");
    		throw new BusinessException(emptyAppId);
    	
    	}
    	
    	try {
			returnList = dao.getOparatorApprovedApp(apps,oparatorName);
		} catch (Exception e) {
			LOG.error("getOparatorApprovedApp appIds:"+appIds,e);
			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}
//    	returnList= returnList==null? Collections.emptyList():	returnList;
    	return returnList;
    }
}

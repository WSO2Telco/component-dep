package com.wso2telco.dep.ratecardservice.service;

import com.wso2telco.dep.ratecardservice.dao.APIOperationDAO;
import com.wso2telco.dep.ratecardservice.dao.model.APIDTO;
import com.wso2telco.dep.ratecardservice.dao.model.APIOperationDTO;

public class APIOperationService {

	APIOperationDAO apiOperationDAO;

	{
		apiOperationDAO = new APIOperationDAO();
	}

	public APIOperationDTO getAPIOperation(int apiOperationId) throws Exception {

		APIService apiService = new APIService();
		
		APIOperationDTO apiOperation = null;

		try {

			apiOperation = apiOperationDAO.getAPIOperation(apiOperationId);
			
			APIDTO api = apiService.getAPI(apiOperation.getApi().getApiId());
			apiOperation.setApi(api);
		} catch (Exception e) {

			throw e;
		}

		return apiOperation;
	}
}

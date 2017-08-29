package com.wso2telco.dep.ratecardservice.service;

import java.util.Collections;
import java.util.List;
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

	public List<APIOperationDTO> getAPIOperations(String apiName) throws Exception {

		APIService apiService = new APIService();
		List<APIOperationDTO> apiOperations = null;

		try {

			apiOperations = apiOperationDAO.getAPIOperations(apiName);
		} catch (Exception e) {

			throw e;
		}

		if (apiOperations != null) {

			for (int i = 0; i < apiOperations.size(); i++) {

				APIOperationDTO apiOperation = apiOperations.get(i);

				APIDTO api = apiService.getAPI(apiOperation.getApi().getApiId());
				apiOperation.setApi(api);

				apiOperations.set(i, apiOperation);
			}

			return apiOperations;
		} else {

			return Collections.emptyList();
		}
	}
}

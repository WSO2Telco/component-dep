/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * <p>
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.dep.ratecardservice.service;

import java.util.Collections;
import java.util.List;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.dep.ratecardservice.dao.APIOperationDAO;
import com.wso2telco.dep.ratecardservice.dao.model.APIDTO;
import com.wso2telco.dep.ratecardservice.dao.model.APIOperationDTO;

public class APIOperationService {

	APIOperationDAO apiOperationDAO;

	public APIOperationService() {

		apiOperationDAO = new APIOperationDAO();
	}

	public APIOperationDTO getAPIOperation(int apiOperationId, String schema) throws BusinessException {

		APIService apiService = new APIService();

		APIOperationDTO apiOperation = null;

		apiOperation = apiOperationDAO.getAPIOperation(apiOperationId);

		if ((schema != null && schema.trim().length() > 0) && schema.equalsIgnoreCase("full")) {

			APIDTO api = apiService.getAPI(apiOperation.getApi().getApiId());
			apiOperation.setApi(api);
		}

		return apiOperation;
	}

	public List<APIOperationDTO> getAPIOperations(String apiName, String schema) throws BusinessException {

		APIService apiService = new APIService();
		List<APIOperationDTO> apiOperations = null;

		apiOperations = apiOperationDAO.getAPIOperations(apiName);

		if (apiOperations != null && !apiOperations.isEmpty()) {

			if ((schema != null && schema.trim().length() > 0) && schema.equalsIgnoreCase("full")) {

				for (int i = 0; i < apiOperations.size(); i++) {

					APIOperationDTO apiOperation = apiOperations.get(i);

					APIDTO api = apiService.getAPI(apiOperation.getApi().getApiId());
					apiOperation.setApi(api);

					apiOperations.set(i, apiOperation);
				}
			}

			return apiOperations;
		} else {

			return Collections.emptyList();
		}
	}
}

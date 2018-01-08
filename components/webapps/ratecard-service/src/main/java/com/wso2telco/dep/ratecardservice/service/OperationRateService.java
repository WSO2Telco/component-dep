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
import com.wso2telco.dep.ratecardservice.dao.OperationRateDAO;
import com.wso2telco.dep.ratecardservice.dao.model.APIOperationDTO;
import com.wso2telco.dep.ratecardservice.dao.model.OperationRateDTO;
import com.wso2telco.dep.ratecardservice.dao.model.OperatorDTO;
import com.wso2telco.dep.ratecardservice.dao.model.RateDefinitionDTO;

public class OperationRateService {

	OperationRateDAO operationRateDAO;

	public OperationRateService() {

		operationRateDAO = new OperationRateDAO();
	}

	public List<OperationRateDTO> getOperationRates(String apiName, String schema) throws BusinessException {

		OperatorService operatorService = new OperatorService();
		APIOperationService apiOperationService = new APIOperationService();
		RateDefinitionService rateDefinitionService = new RateDefinitionService();

		List<OperationRateDTO> operationRates = null;

		operationRates = operationRateDAO.getOperationRates(apiName);

		if (operationRates != null && !operationRates.isEmpty()) {

			for (int i = 0; i < operationRates.size(); i++) {

				OperationRateDTO operationRate = operationRates.get(i);

				OperatorDTO operator = operatorService.getOperator(operationRate.getOperator().getOperatorId());
				operationRate.setOperator(operator);

				APIOperationDTO apiOperation = apiOperationService
						.getAPIOperation(operationRate.getApiOperation().getApiOperationId(), schema);
				operationRate.setApiOperation(apiOperation);

				RateDefinitionDTO rateDefinition = rateDefinitionService
						.getRateDefinition(operationRate.getRateDefinition().getRateDefId(), schema);
				operationRate.setRateDefinition(rateDefinition);

				operationRates.set(i, operationRate);
			}

			return operationRates;
		} else {

			return Collections.emptyList();
		}
	}

	public List<OperationRateDTO> getOperationRates(String apiName, String operatorName, String schema)
			throws BusinessException {

		OperatorService operatorService = new OperatorService();
		APIOperationService apiOperationService = new APIOperationService();
		RateDefinitionService rateDefinitionService = new RateDefinitionService();

		List<OperationRateDTO> operationRates = null;

		operationRates = operationRateDAO.getOperationRates(apiName, operatorName);

		if (operationRates != null && !operationRates.isEmpty()) {

			for (int i = 0; i < operationRates.size(); i++) {

				OperationRateDTO operationRate = operationRates.get(i);

				OperatorDTO operator = operatorService.getOperator(operationRate.getOperator().getOperatorId());
				operationRate.setOperator(operator);

				APIOperationDTO apiOperation = apiOperationService
						.getAPIOperation(operationRate.getApiOperation().getApiOperationId(), schema);
				operationRate.setApiOperation(apiOperation);

				RateDefinitionDTO rateDefinition = rateDefinitionService
						.getRateDefinition(operationRate.getRateDefinition().getRateDefId(), schema);
				operationRate.setRateDefinition(rateDefinition);

				operationRates.set(i, operationRate);
			}

			return operationRates;
		} else {

			return Collections.emptyList();
		}
	}

	public OperationRateDTO addOperationRate(OperationRateDTO operationRate) throws BusinessException {

		OperationRateDTO newOperationRate = null;

		newOperationRate = operationRateDAO.addOperationRate(operationRate);
		newOperationRate = getOperationRate(newOperationRate.getOperationRateId(), null);

		return newOperationRate;
	}

	public OperationRateDTO getOperationRate(int operationRateId, String schema) throws BusinessException {

		OperatorService operatorService = new OperatorService();
		APIOperationService apiOperationService = new APIOperationService();
		RateDefinitionService rateDefinitionService = new RateDefinitionService();

		OperationRateDTO operationRate = null;

		operationRate = operationRateDAO.getOperationRate(operationRateId);

		if ((schema != null && schema.trim().length() > 0) && schema.equalsIgnoreCase("full")) {

			OperatorDTO operator = operatorService.getOperator(operationRate.getOperator().getOperatorId());
			operationRate.setOperator(operator);

			APIOperationDTO apiOperation = apiOperationService
					.getAPIOperation(operationRate.getApiOperation().getApiOperationId(), schema);
			operationRate.setApiOperation(apiOperation);

			RateDefinitionDTO rateDefinition = rateDefinitionService
					.getRateDefinition(operationRate.getRateDefinition().getRateDefId(), schema);
			operationRate.setRateDefinition(rateDefinition);
		}

		return operationRate;
	}
}

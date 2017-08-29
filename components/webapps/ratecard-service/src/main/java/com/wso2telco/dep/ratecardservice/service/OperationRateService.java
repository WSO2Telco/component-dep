package com.wso2telco.dep.ratecardservice.service;

import java.util.Collections;
import java.util.List;
import com.wso2telco.dep.ratecardservice.dao.OperationRateDAO;
import com.wso2telco.dep.ratecardservice.dao.model.APIOperationDTO;
import com.wso2telco.dep.ratecardservice.dao.model.OperationRateDTO;
import com.wso2telco.dep.ratecardservice.dao.model.OperatorDTO;
import com.wso2telco.dep.ratecardservice.dao.model.RateDefinitionDTO;

public class OperationRateService {

	OperationRateDAO operationRateDAO;

	{
		operationRateDAO = new OperationRateDAO();
	}

	public List<OperationRateDTO> getOperationRates(String apiName) throws Exception {

		OperatorService operatorService = new OperatorService();
		APIOperationService apiOperationService = new APIOperationService();
		RateDefinitionService rateDefinitionService = new RateDefinitionService();

		List<OperationRateDTO> operationRates = null;

		try {

			operationRates = operationRateDAO.getOperationRates(apiName);
		} catch (Exception e) {

			throw e;
		}

		if (operationRates != null) {

			for (int i = 0; i < operationRates.size(); i++) {

				OperationRateDTO operationRate = operationRates.get(i);

				OperatorDTO operator = operatorService.getOperator(operationRate.getOperator().getOperatorId());
				operationRate.setOperator(operator);

				APIOperationDTO apiOperation = apiOperationService
						.getAPIOperation(operationRate.getApiOperation().getApiOperationId());
				operationRate.setApiOperation(apiOperation);

				RateDefinitionDTO rateDefinition = rateDefinitionService
						.getRateDefinition(operationRate.getRateDefinition().getRateDefId());
				operationRate.setRateDefinition(rateDefinition);

				operationRates.set(i, operationRate);
			}

			return operationRates;
		} else {

			return Collections.emptyList();
		}
	}

	public List<OperationRateDTO> getOperationRates(String apiName, String operatorName) throws Exception {

		OperatorService operatorService = new OperatorService();
		APIOperationService apiOperationService = new APIOperationService();
		RateDefinitionService rateDefinitionService = new RateDefinitionService();

		List<OperationRateDTO> operationRates = null;

		try {

			operationRates = operationRateDAO.getOperationRates(apiName, operatorName);
		} catch (Exception e) {

			throw e;
		}

		if (operationRates != null) {

			for (int i = 0; i < operationRates.size(); i++) {

				OperationRateDTO operationRate = operationRates.get(i);

				OperatorDTO operator = operatorService.getOperator(operationRate.getOperator().getOperatorId());
				operationRate.setOperator(operator);

				APIOperationDTO apiOperation = apiOperationService
						.getAPIOperation(operationRate.getApiOperation().getApiOperationId());
				operationRate.setApiOperation(apiOperation);

				RateDefinitionDTO rateDefinition = rateDefinitionService
						.getRateDefinition(operationRate.getRateDefinition().getRateDefId());
				operationRate.setRateDefinition(rateDefinition);

				operationRates.set(i, operationRate);
			}

			return operationRates;
		} else {

			return Collections.emptyList();
		}
	}

	public OperationRateDTO addOperationRate(OperationRateDTO operationRate) throws Exception {

		OperationRateDTO newOperationRate = null;

		try {

			newOperationRate = operationRateDAO.addOperationRate(operationRate);
			newOperationRate = getOperationRate(newOperationRate.getOperationRateId());
		} catch (Exception e) {

			throw e;
		}

		return newOperationRate;
	}

	public OperationRateDTO getOperationRate(int operationRateId) throws Exception {

		OperatorService operatorService = new OperatorService();
		APIOperationService apiOperationService = new APIOperationService();
		RateDefinitionService rateDefinitionService = new RateDefinitionService();

		OperationRateDTO operationRate = null;

		try {

			operationRate = operationRateDAO.getOperationRate(operationRateId);

			OperatorDTO operator = operatorService.getOperator(operationRate.getOperator().getOperatorId());
			operationRate.setOperator(operator);

			APIOperationDTO apiOperation = apiOperationService
					.getAPIOperation(operationRate.getApiOperation().getApiOperationId());
			operationRate.setApiOperation(apiOperation);

			RateDefinitionDTO rateDefinition = rateDefinitionService
					.getRateDefinition(operationRate.getRateDefinition().getRateDefId());
			operationRate.setRateDefinition(rateDefinition);
		} catch (Exception e) {

			throw e;
		}

		return operationRate;
	}
}

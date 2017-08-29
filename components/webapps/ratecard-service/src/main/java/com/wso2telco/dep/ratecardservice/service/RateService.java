package com.wso2telco.dep.ratecardservice.service;

import java.util.ArrayList;
import java.util.List;
import com.wso2telco.dep.ratecardservice.dao.model.OperationRateDTO;
import com.wso2telco.dep.ratecardservice.dao.model.RateDTO;

public class RateService {

	public RateDTO getOperationRates(String apiName) throws Exception {

		OperationRateService operationRateService = new OperationRateService();

		RateDTO rate = null;
		List<OperationRateDTO> operationRates = null;

		try {

			operationRates = operationRateService.getOperationRates(apiName);
		} catch (Exception e) {

			throw e;
		}

		if (operationRates != null) {

			rate = new RateDTO();

			OperationRateDTO operationRate = operationRates.get(0);

			RateDTO.API api = new RateDTO.API();

			api.setApiId(operationRate.getApiOperation().getApi().getApiId());
			api.setApiName(operationRate.getApiOperation().getApi().getApiName());
			api.setApiDescription(operationRate.getApiOperation().getApi().getApiDescription());
			api.setCreatedBy(operationRate.getApiOperation().getApi().getCreatedBy());

			List<RateDTO.API.APIOperation> operationList = new ArrayList<RateDTO.API.APIOperation>();

			List<Integer> operationRateIds = new ArrayList<Integer>();

			for (int i = 0; i < operationRates.size(); i++) {

				operationRate = operationRates.get(i);
				int operationId = operationRate.getApiOperation().getApiOperationId();

				RateDTO.API.APIOperation operation = new RateDTO.API.APIOperation();

				operation.setApiOperationId(operationRate.getApiOperation().getApiOperationId());

				operation.setApiOperationName(operationRate.getApiOperation().getApiOperation());
				operation.setApiOperationCode(operationRate.getApiOperation().getApiOperationCode());
				operation.setCreatedBy(operationRate.getApiOperation().getCreatedBy());

				List<RateDTO.API.APIOperation.RateDefinition> rateDefinitionList = new ArrayList<RateDTO.API.APIOperation.RateDefinition>();

				for (int j = 0; j < operationRates.size(); j++) {

					OperationRateDTO innerOperationRate = operationRates.get(j);

					if (operationId == innerOperationRate.getApiOperation().getApiOperationId()
							&& !operationRateIds.contains(innerOperationRate.getOperationRateId())) {

						operationRateIds.add(innerOperationRate.getOperationRateId());

						RateDTO.API.APIOperation.RateDefinition rateDefinition = new RateDTO.API.APIOperation.RateDefinition();

						rateDefinition.setOperationRateId(innerOperationRate.getOperationRateId());
						rateDefinition.setRateDefId(innerOperationRate.getRateDefinition().getRateDefId());
						rateDefinition.setRateDefName(innerOperationRate.getRateDefinition().getRateDefName());
						rateDefinition
								.setRateDefDescription(innerOperationRate.getRateDefinition().getRateDefDescription());
						rateDefinition.setRateDefDefault(innerOperationRate.getRateDefinition().getRateDefDefault());
						rateDefinition.setRateDefCategoryBase(
								innerOperationRate.getRateDefinition().getRateDefCategoryBase());
						rateDefinition.setCreatedBy(innerOperationRate.getRateDefinition().getCreatedBy());

						rateDefinition.setCurrency(innerOperationRate.getRateDefinition().getCurrency());

						rateDefinition.setRateType(innerOperationRate.getRateDefinition().getRateType());

						rateDefinition.setTariff(innerOperationRate.getRateDefinition().getTariff());

						rateDefinitionList.add(rateDefinition);
					}
				}

				RateDTO.API.APIOperation.RateDefinition rateDef[] = new RateDTO.API.APIOperation.RateDefinition[rateDefinitionList
						.size()];

				rateDef = rateDefinitionList.toArray(rateDef);

				operation.setRates(rateDef);

				operationList.add(operation);
			}

			RateDTO.API.APIOperation operationindicator = new RateDTO.API.APIOperation();

			operationindicator = operationList.get(operationList.size() - 1);

			if (operationindicator.getRates().length == 0) {

				operationList.remove(operationList.size() - 1);
			}

			RateDTO.API.APIOperation operations[] = new RateDTO.API.APIOperation[operationList.size()];
			operations = operationList.toArray(operations);

			api.setOperations(operations);

			rate.setApi(api);
			rate.setOperator(operationRate.getOperator());
		}

		return rate;
	}

	public RateDTO getOperationRates(String apiName, String operatorName) throws Exception {

		OperationRateService operationRateService = new OperationRateService();

		RateDTO rate = null;
		List<OperationRateDTO> operationRates = null;

		try {

			operationRates = operationRateService.getOperationRates(apiName, operatorName);
		} catch (Exception e) {

			throw e;
		}

		if (operationRates != null) {

			rate = new RateDTO();

			OperationRateDTO operationRate = operationRates.get(0);

			RateDTO.API api = new RateDTO.API();

			api.setApiId(operationRate.getApiOperation().getApi().getApiId());
			api.setApiName(operationRate.getApiOperation().getApi().getApiName());
			api.setApiDescription(operationRate.getApiOperation().getApi().getApiDescription());
			api.setCreatedBy(operationRate.getApiOperation().getApi().getCreatedBy());

			List<RateDTO.API.APIOperation> operationList = new ArrayList<RateDTO.API.APIOperation>();

			List<Integer> operationRateIds = new ArrayList<Integer>();

			for (int i = 0; i < operationRates.size(); i++) {

				operationRate = operationRates.get(i);
				int operationId = operationRate.getApiOperation().getApiOperationId();

				RateDTO.API.APIOperation operation = new RateDTO.API.APIOperation();

				operation.setApiOperationId(operationRate.getApiOperation().getApiOperationId());

				operation.setApiOperationName(operationRate.getApiOperation().getApiOperation());
				operation.setApiOperationCode(operationRate.getApiOperation().getApiOperationCode());
				operation.setCreatedBy(operationRate.getApiOperation().getCreatedBy());

				List<RateDTO.API.APIOperation.RateDefinition> rateDefinitionList = new ArrayList<RateDTO.API.APIOperation.RateDefinition>();

				for (int j = 0; j < operationRates.size(); j++) {

					OperationRateDTO innerOperationRate = operationRates.get(j);

					if (operationId == innerOperationRate.getApiOperation().getApiOperationId()
							&& !operationRateIds.contains(innerOperationRate.getOperationRateId())) {

						operationRateIds.add(innerOperationRate.getOperationRateId());

						RateDTO.API.APIOperation.RateDefinition rateDefinition = new RateDTO.API.APIOperation.RateDefinition();

						rateDefinition.setOperationRateId(innerOperationRate.getOperationRateId());
						rateDefinition.setRateDefId(innerOperationRate.getRateDefinition().getRateDefId());
						rateDefinition.setRateDefName(innerOperationRate.getRateDefinition().getRateDefName());
						rateDefinition
								.setRateDefDescription(innerOperationRate.getRateDefinition().getRateDefDescription());
						rateDefinition.setRateDefDefault(innerOperationRate.getRateDefinition().getRateDefDefault());
						rateDefinition.setRateDefCategoryBase(
								innerOperationRate.getRateDefinition().getRateDefCategoryBase());
						rateDefinition.setCreatedBy(innerOperationRate.getRateDefinition().getCreatedBy());

						rateDefinition.setCurrency(innerOperationRate.getRateDefinition().getCurrency());

						rateDefinition.setRateType(innerOperationRate.getRateDefinition().getRateType());

						rateDefinition.setTariff(innerOperationRate.getRateDefinition().getTariff());

						rateDefinitionList.add(rateDefinition);
					}
				}

				RateDTO.API.APIOperation.RateDefinition rateDef[] = new RateDTO.API.APIOperation.RateDefinition[rateDefinitionList
						.size()];

				rateDef = rateDefinitionList.toArray(rateDef);

				operation.setRates(rateDef);

				operationList.add(operation);
			}

			RateDTO.API.APIOperation operationindicator = operationList.get(operationList.size() - 1);

			if (operationindicator.getRates().length == 0) {

				operationList.remove(operationList.size() - 1);
			}

			RateDTO.API.APIOperation operations[] = new RateDTO.API.APIOperation[operationList.size()];
			operations = operationList.toArray(operations);

			api.setOperations(operations);

			rate.setApi(api);
			rate.setOperator(operationRate.getOperator());
		}

		return rate;
	}
}

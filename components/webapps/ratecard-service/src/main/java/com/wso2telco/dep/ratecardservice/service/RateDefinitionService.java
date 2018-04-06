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
import com.wso2telco.dep.ratecardservice.dao.RateDefinitionDAO;
import com.wso2telco.dep.ratecardservice.dao.model.CurrencyDTO;
import com.wso2telco.dep.ratecardservice.dao.model.RateDefinitionDTO;
import com.wso2telco.dep.ratecardservice.dao.model.RateTypeDTO;
import com.wso2telco.dep.ratecardservice.dao.model.TariffDTO;

public class RateDefinitionService {

	RateDefinitionDAO rateDefinitionDAO;

	public RateDefinitionService(){
		
		rateDefinitionDAO = new RateDefinitionDAO();
	}

	public List<RateDefinitionDTO> getRateDefinitions(String schema) throws BusinessException {

		CurrencyService currencyService = new CurrencyService();
		RateTypeService rateTypeService = new RateTypeService();
		TariffService tariffService = new TariffService();

		List<RateDefinitionDTO> rateDefinitions = null;

		rateDefinitions = rateDefinitionDAO.getRateDefinitions();

		if (rateDefinitions != null && !rateDefinitions.isEmpty()) {

			if ((schema != null && schema.trim().length() > 0) && schema.equalsIgnoreCase("full")) {

				for (int i = 0; i < rateDefinitions.size(); i++) {

					RateDefinitionDTO rateDefinition = rateDefinitions.get(i);

					CurrencyDTO currency = currencyService.getCurrency(rateDefinition.getCurrency().getCurrencyId());
					rateDefinition.setCurrency(currency);

					RateTypeDTO rateType = rateTypeService.getRateType(rateDefinition.getRateType().getRateTypeId());
					rateDefinition.setRateType(rateType);

					TariffDTO tariff = tariffService.getTariff(rateDefinition.getTariff().getTariffId());
					rateDefinition.setTariff(tariff);

					rateDefinitions.set(i, rateDefinition);
				}
			}

			return rateDefinitions;
		} else {

			return Collections.emptyList();
		}
	}

	public RateDefinitionDTO addRateDefinition(RateDefinitionDTO rateDefinition) throws BusinessException {

		RateDefinitionDTO newRateDefinition = null;

		newRateDefinition = rateDefinitionDAO.addRateDefinition(rateDefinition);
		newRateDefinition = getRateDefinition(newRateDefinition.getRateDefId(), null);

		return newRateDefinition;
	}

	public RateDefinitionDTO getRateDefinition(int rateDefId, String schema) throws BusinessException {

		CurrencyService currencyService = new CurrencyService();
		RateTypeService rateTypeService = new RateTypeService();
		TariffService tariffService = new TariffService();

		RateDefinitionDTO rateDefinition = null;

		rateDefinition = rateDefinitionDAO.getRateDefinition(rateDefId);

		if ((schema != null && schema.trim().length() > 0) && schema.equalsIgnoreCase("full")) {

			CurrencyDTO currency = currencyService.getCurrency(rateDefinition.getCurrency().getCurrencyId());
			rateDefinition.setCurrency(currency);

			RateTypeDTO rateType = rateTypeService.getRateType(rateDefinition.getRateType().getRateTypeId());
			rateDefinition.setRateType(rateType);

			TariffDTO tariff = tariffService.getTariff(rateDefinition.getTariff().getTariffId());
			rateDefinition.setTariff(tariff);
		}

		return rateDefinition;
	}

	public boolean deleteRateDefinition(int rateDefId) throws BusinessException {

		boolean status = false;

		status = rateDefinitionDAO.deleteRateDefinition(rateDefId);

		return status;
	}

	public List<RateDefinitionDTO> getRateDefinitions(int apiOperationId, String schema) throws BusinessException {

		CurrencyService currencyService = new CurrencyService();
		RateTypeService rateTypeService = new RateTypeService();
		TariffService tariffService = new TariffService();

		List<RateDefinitionDTO> rateDefinitions = null;

		rateDefinitions = rateDefinitionDAO.getRateDefinitions(apiOperationId);

		if (rateDefinitions != null && !rateDefinitions.isEmpty()) {

			if ((schema != null && schema.trim().length() > 0) && schema.equalsIgnoreCase("full")) {

				for (int i = 0; i < rateDefinitions.size(); i++) {

					RateDefinitionDTO rateDefinition = rateDefinitions.get(i);

					CurrencyDTO currency = currencyService.getCurrency(rateDefinition.getCurrency().getCurrencyId());
					rateDefinition.setCurrency(currency);

					RateTypeDTO rateType = rateTypeService.getRateType(rateDefinition.getRateType().getRateTypeId());
					rateDefinition.setRateType(rateType);

					TariffDTO tariff = tariffService.getTariff(rateDefinition.getTariff().getTariffId());
					rateDefinition.setTariff(tariff);

					rateDefinitions.set(i, rateDefinition);
				}
			}

			return rateDefinitions;
		} else {

			return Collections.emptyList();
		}
	}

	public List<RateDefinitionDTO> getRateDefinitions(int apiOperationId, int operatorId, String schema)
			throws BusinessException {

		CurrencyService currencyService = new CurrencyService();
		RateTypeService rateTypeService = new RateTypeService();
		TariffService tariffService = new TariffService();

		List<RateDefinitionDTO> rateDefinitions = null;

		rateDefinitions = rateDefinitionDAO.getRateDefinitions(apiOperationId, operatorId);

		if (rateDefinitions != null && !rateDefinitions.isEmpty()) {

			if ((schema != null && schema.trim().length() > 0) && schema.equalsIgnoreCase("full")) {

				for (int i = 0; i < rateDefinitions.size(); i++) {

					RateDefinitionDTO rateDefinition = rateDefinitions.get(i);

					CurrencyDTO currency = currencyService.getCurrency(rateDefinition.getCurrency().getCurrencyId());
					rateDefinition.setCurrency(currency);

					RateTypeDTO rateType = rateTypeService.getRateType(rateDefinition.getRateType().getRateTypeId());
					rateDefinition.setRateType(rateType);

					TariffDTO tariff = tariffService.getTariff(rateDefinition.getTariff().getTariffId());
					rateDefinition.setTariff(tariff);

					rateDefinitions.set(i, rateDefinition);
				}
			}

			return rateDefinitions;
		} else {

			return Collections.emptyList();
		}
	}

	public List<RateDefinitionDTO> getAssignedRateDefinitions(int apiOperationId, String schema)
			throws BusinessException {

		CurrencyService currencyService = new CurrencyService();
		RateTypeService rateTypeService = new RateTypeService();
		TariffService tariffService = new TariffService();

		List<RateDefinitionDTO> rateDefinitions = null;

		rateDefinitions = rateDefinitionDAO.getAssignedRateDefinitions(apiOperationId);

		if (rateDefinitions != null && !rateDefinitions.isEmpty()) {

			if ((schema != null && schema.trim().length() > 0) && schema.equalsIgnoreCase("full")) {

				for (int i = 0; i < rateDefinitions.size(); i++) {

					RateDefinitionDTO rateDefinition = rateDefinitions.get(i);

					CurrencyDTO currency = currencyService.getCurrency(rateDefinition.getCurrency().getCurrencyId());
					rateDefinition.setCurrency(currency);

					RateTypeDTO rateType = rateTypeService.getRateType(rateDefinition.getRateType().getRateTypeId());
					rateDefinition.setRateType(rateType);

					TariffDTO tariff = tariffService.getTariff(rateDefinition.getTariff().getTariffId());
					rateDefinition.setTariff(tariff);

					rateDefinitions.set(i, rateDefinition);
				}
			}

			return rateDefinitions;
		} else {

			return Collections.emptyList();
		}
	}

	public List<RateDefinitionDTO> getAssignedRateDefinitions(int apiOperationId, int operatorId, String schema)
			throws BusinessException {

		CurrencyService currencyService = new CurrencyService();
		RateTypeService rateTypeService = new RateTypeService();
		TariffService tariffService = new TariffService();

		List<RateDefinitionDTO> rateDefinitions = null;

		rateDefinitions = rateDefinitionDAO.getAssignedRateDefinitions(apiOperationId, operatorId);

		if (rateDefinitions != null && !rateDefinitions.isEmpty()) {

			if ((schema != null && schema.trim().length() > 0) && schema.equalsIgnoreCase("full")) {

				for (int i = 0; i < rateDefinitions.size(); i++) {

					RateDefinitionDTO rateDefinition = rateDefinitions.get(i);

					CurrencyDTO currency = currencyService.getCurrency(rateDefinition.getCurrency().getCurrencyId());
					rateDefinition.setCurrency(currency);

					RateTypeDTO rateType = rateTypeService.getRateType(rateDefinition.getRateType().getRateTypeId());
					rateDefinition.setRateType(rateType);

					TariffDTO tariff = tariffService.getTariff(rateDefinition.getTariff().getTariffId());
					rateDefinition.setTariff(tariff);

					rateDefinitions.set(i, rateDefinition);
				}
			}

			return rateDefinitions;
		} else {

			return Collections.emptyList();
		}
	}
	
	
	
	public List<RateDefinitionDTO> getAssignedRateDefinitionsForOperator(int operatorId, String schema)
			throws BusinessException {

		CurrencyService currencyService = new CurrencyService();
		RateTypeService rateTypeService = new RateTypeService();
		TariffService tariffService = new TariffService();

		List<RateDefinitionDTO> rateDefinitions = null;

		rateDefinitions = rateDefinitionDAO.getAssignedRateDefinitionsForOperator(operatorId);

		if (rateDefinitions != null && !rateDefinitions.isEmpty()) {

			if ((schema != null && schema.trim().length() > 0) && schema.equalsIgnoreCase("full")) {

				for (int i = 0; i < rateDefinitions.size(); i++) {

					RateDefinitionDTO rateDefinition = rateDefinitions.get(i);

					CurrencyDTO currency = currencyService.getCurrency(rateDefinition.getCurrency().getCurrencyId());
					rateDefinition.setCurrency(currency);

					RateTypeDTO rateType = rateTypeService.getRateType(rateDefinition.getRateType().getRateTypeId());
					rateDefinition.setRateType(rateType);

					TariffDTO tariff = tariffService.getTariff(rateDefinition.getTariff().getTariffId());
					rateDefinition.setTariff(tariff);

					rateDefinitions.set(i, rateDefinition);
				}
			}

			return rateDefinitions;
		} else {

			return Collections.emptyList();
		}
	}
}

package com.wso2telco.dep.ratecardservice.service;

import java.util.Collections;
import java.util.List;
import com.wso2telco.dep.ratecardservice.dao.RateDefinitionDAO;
import com.wso2telco.dep.ratecardservice.dao.model.CurrencyDTO;
import com.wso2telco.dep.ratecardservice.dao.model.RateDefinitionDTO;
import com.wso2telco.dep.ratecardservice.dao.model.RateTypeDTO;
import com.wso2telco.dep.ratecardservice.dao.model.TariffDTO;

public class RateDefinitionService {

	RateDefinitionDAO rateDefinitionDAO;

	{
		rateDefinitionDAO = new RateDefinitionDAO();
	}

	public List<RateDefinitionDTO> getRateDefinitions() throws Exception {

		CurrencyService currencyService = new CurrencyService();
		RateTypeService rateTypeService = new RateTypeService();
		TariffService tariffService = new TariffService();

		List<RateDefinitionDTO> rateDefinitions = null;

		try {

			rateDefinitions = rateDefinitionDAO.getRateDefinitions();
		} catch (Exception e) {

			throw e;
		}

		if (rateDefinitions != null) {

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

			return rateDefinitions;
		} else {

			return Collections.emptyList();
		}
	}

	public RateDefinitionDTO addRateDefinition(RateDefinitionDTO rateDefinition) throws Exception {

		RateDefinitionDTO newRateDefinition = null;

		try {

			newRateDefinition = rateDefinitionDAO.addRateDefinition(rateDefinition);
			newRateDefinition = getRateDefinition(newRateDefinition.getRateDefId());
		} catch (Exception e) {

			throw e;
		}

		return newRateDefinition;
	}

	public RateDefinitionDTO getRateDefinition(int rateDefId) throws Exception {

		CurrencyService currencyService = new CurrencyService();
		RateTypeService rateTypeService = new RateTypeService();
		TariffService tariffService = new TariffService();

		RateDefinitionDTO rateDefinition = null;

		try {

			rateDefinition = rateDefinitionDAO.getRateDefinition(rateDefId);

			CurrencyDTO currency = currencyService.getCurrency(rateDefinition.getCurrency().getCurrencyId());
			rateDefinition.setCurrency(currency);

			RateTypeDTO rateType = rateTypeService.getRateType(rateDefinition.getRateType().getRateTypeId());
			rateDefinition.setRateType(rateType);

			TariffDTO tariff = tariffService.getTariff(rateDefinition.getTariff().getTariffId());
			rateDefinition.setTariff(tariff);
		} catch (Exception e) {

			throw e;
		}

		return rateDefinition;
	}

	public boolean deleteRateDefinition(int rateDefId) throws Exception {

		boolean status = false;

		try {

			status = rateDefinitionDAO.deleteRateDefinition(rateDefId);
		} catch (Exception e) {

			throw e;
		}

		return status;
	}

	public List<RateDefinitionDTO> getRateDefinitions(int apiOperationId) throws Exception {

		CurrencyService currencyService = new CurrencyService();
		RateTypeService rateTypeService = new RateTypeService();
		TariffService tariffService = new TariffService();

		List<RateDefinitionDTO> rateDefinitions = null;

		try {

			rateDefinitions = rateDefinitionDAO.getRateDefinitions(apiOperationId);
		} catch (Exception e) {

			throw e;
		}

		if (rateDefinitions != null) {

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

			return rateDefinitions;
		} else {

			return Collections.emptyList();
		}
	}

	public List<RateDefinitionDTO> getRateDefinitions(int apiOperationId, int operatorId) throws Exception {

		CurrencyService currencyService = new CurrencyService();
		RateTypeService rateTypeService = new RateTypeService();
		TariffService tariffService = new TariffService();

		List<RateDefinitionDTO> rateDefinitions = null;

		try {

			rateDefinitions = rateDefinitionDAO.getRateDefinitions(apiOperationId, operatorId);
		} catch (Exception e) {

			throw e;
		}

		if (rateDefinitions != null) {

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

			return rateDefinitions;
		} else {

			return Collections.emptyList();
		}
	}

	public List<RateDefinitionDTO> getAssignedRateDefinitions(int apiOperationId) throws Exception {

		CurrencyService currencyService = new CurrencyService();
		RateTypeService rateTypeService = new RateTypeService();
		TariffService tariffService = new TariffService();

		List<RateDefinitionDTO> rateDefinitions = null;

		try {

			rateDefinitions = rateDefinitionDAO.getAssignedRateDefinitions(apiOperationId);
		} catch (Exception e) {

			throw e;
		}

		if (rateDefinitions != null) {

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

			return rateDefinitions;
		} else {

			return Collections.emptyList();
		}
	}

	public List<RateDefinitionDTO> getAssignedRateDefinitions(int apiOperationId, int operatorId) throws Exception {

		CurrencyService currencyService = new CurrencyService();
		RateTypeService rateTypeService = new RateTypeService();
		TariffService tariffService = new TariffService();

		List<RateDefinitionDTO> rateDefinitions = null;

		try {

			rateDefinitions = rateDefinitionDAO.getAssignedRateDefinitions(apiOperationId, operatorId);
		} catch (Exception e) {

			throw e;
		}

		if (rateDefinitions != null) {

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

			return rateDefinitions;
		} else {

			return Collections.emptyList();
		}
	}
}

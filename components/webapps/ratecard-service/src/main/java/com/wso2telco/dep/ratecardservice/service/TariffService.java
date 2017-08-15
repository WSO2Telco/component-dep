package com.wso2telco.dep.ratecardservice.service;

import java.util.Collections;
import java.util.List;
import com.wso2telco.dep.ratecardservice.dao.TariffDAO;
import com.wso2telco.dep.ratecardservice.dao.model.TariffDTO;

public class TariffService {

	TariffDAO tariffDAO;

	{
		tariffDAO = new TariffDAO();
	}

	public List<TariffDTO> getTariffs() throws Exception {

		List<TariffDTO> tariffs = null;

		try {

			tariffs = tariffDAO.getTariffs();
		} catch (Exception e) {

			throw e;
		}

		if (tariffs != null) {

			return tariffs;
		} else {

			return Collections.emptyList();
		}
	}
	
	public TariffDTO addTariff(TariffDTO tariff) throws Exception {

		TariffDTO newTariff = null;

		try {

			newTariff = tariffDAO.addTariff(tariff);
			newTariff = getTariff(newTariff.getTariffId());
		} catch (Exception e) {

			throw e;
		}
		
		return newTariff;
	}
	
	public TariffDTO getTariff(int tariffId) throws Exception {

		TariffDTO tariff = null;

		try {

			tariff = tariffDAO.getTariff(tariffId);
		} catch (Exception e) {

			throw e;
		}

		return tariff;
	}
}

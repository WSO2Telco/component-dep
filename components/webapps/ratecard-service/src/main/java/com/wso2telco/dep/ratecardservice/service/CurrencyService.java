package com.wso2telco.dep.ratecardservice.service;

import java.util.Collections;
import java.util.List;
import com.wso2telco.dep.ratecardservice.dao.CurrencyDAO;
import com.wso2telco.dep.ratecardservice.dao.model.CurrencyDTO;

public class CurrencyService {

	CurrencyDAO currencyDAO;

	{
		currencyDAO = new CurrencyDAO();
	}

	public List<CurrencyDTO> getCurrencies() throws Exception {

		List<CurrencyDTO> currencies = null;

		try {

			currencies = currencyDAO.getCurrencies();
		} catch (Exception e) {

			throw e;
		}

		if (currencies != null) {

			return currencies;
		} else {

			return Collections.emptyList();
		}
	}

	public CurrencyDTO addCurrency(CurrencyDTO currency) throws Exception {

		CurrencyDTO newCurrency = null;

		try {

			newCurrency = currencyDAO.addCurrency(currency);
			newCurrency = getCurrency(newCurrency.getCurrencyId());
		} catch (Exception e) {

			throw e;
		}

		return newCurrency;
	}

	public CurrencyDTO getCurrency(int currencyId) throws Exception {

		CurrencyDTO currency = null;

		try {

			currency = currencyDAO.getCurrency(currencyId);
		} catch (Exception e) {

			throw e;
		}

		return currency;
	}
}

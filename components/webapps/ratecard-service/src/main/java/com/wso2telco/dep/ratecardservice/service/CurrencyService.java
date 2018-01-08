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
import com.wso2telco.dep.ratecardservice.dao.CurrencyDAO;
import com.wso2telco.dep.ratecardservice.dao.model.CurrencyDTO;

public class CurrencyService {

	CurrencyDAO currencyDAO;
	
	public CurrencyService(){
		
		currencyDAO = new CurrencyDAO();
	}

	public List<CurrencyDTO> getCurrencies() throws BusinessException {

		List<CurrencyDTO> currencies = null;

		currencies = currencyDAO.getCurrencies();

		if (currencies != null && !currencies.isEmpty()) {

			return currencies;
		} else {

			return Collections.emptyList();
		}
	}

	public CurrencyDTO addCurrency(CurrencyDTO currency) throws BusinessException {

		CurrencyDTO newCurrency = null;

		newCurrency = currencyDAO.addCurrency(currency);
		newCurrency = getCurrency(newCurrency.getCurrencyId());

		return newCurrency;
	}

	public CurrencyDTO getCurrency(int currencyId) throws BusinessException {

		CurrencyDTO currency = null;

		currency = currencyDAO.getCurrency(currencyId);

		return currency;
	}
}

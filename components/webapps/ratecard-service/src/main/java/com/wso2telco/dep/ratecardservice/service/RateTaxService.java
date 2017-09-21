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
import com.wso2telco.dep.ratecardservice.dao.RateTaxDAO;
import com.wso2telco.dep.ratecardservice.dao.model.RateDefinitionDTO;
import com.wso2telco.dep.ratecardservice.dao.model.RateTaxDTO;
import com.wso2telco.dep.ratecardservice.dao.model.TaxDTO;

public class RateTaxService {

	RateTaxDAO rateTaxDAO;
	
	public RateTaxService(){
		
		rateTaxDAO = new RateTaxDAO();
	}

	public RateTaxDTO addRateTax(RateTaxDTO rateTax) throws BusinessException {

		RateTaxDTO newRateTax = null;

		newRateTax = rateTaxDAO.addRateTax(rateTax);
		newRateTax = getRateTax(newRateTax.getRateTaxId(), null);

		return newRateTax;
	}

	public RateTaxDTO getRateTax(int rateTaxId, String schema) throws BusinessException {

		RateDefinitionService rateDefinitionService = new RateDefinitionService();
		TaxService taxService = new TaxService();

		RateTaxDTO rateTax = null;

		rateTax = rateTaxDAO.getRateTax(rateTaxId);

		if ((schema != null && schema.trim().length() > 0) && schema.equalsIgnoreCase("full")) {

			RateDefinitionDTO rateDefinition = rateDefinitionService
					.getRateDefinition(rateTax.getRateDefinition().getRateDefId(), schema);
			rateTax.setRateDefinition(rateDefinition);

			TaxDTO tax = taxService.getTax(rateTax.getTax().getTaxId());
			rateTax.setTax(tax);
		}

		return rateTax;
	}

	public List<RateTaxDTO> getRateTaxes(int taxId, String schema) throws BusinessException {

		RateDefinitionService rateDefinitionService = new RateDefinitionService();
		TaxService taxService = new TaxService();

		List<RateTaxDTO> rateTaxes = null;

		rateTaxes = rateTaxDAO.getRateTaxes(taxId);

		if (rateTaxes != null && !rateTaxes.isEmpty()) {

			if ((schema != null && schema.trim().length() > 0) && schema.equalsIgnoreCase("full")) {

				for (int i = 0; i < rateTaxes.size(); i++) {

					RateTaxDTO rateTax = rateTaxes.get(i);

					RateDefinitionDTO rateDefinition = rateDefinitionService
							.getRateDefinition(rateTax.getRateDefinition().getRateDefId(), schema);
					rateTax.setRateDefinition(rateDefinition);

					TaxDTO tax = taxService.getTax(rateTax.getTax().getTaxId());
					rateTax.setTax(tax);

					rateTaxes.set(i, rateTax);
				}
			}

			return rateTaxes;
		} else {

			return Collections.emptyList();
		}
	}

	public boolean deleteRateTax(int rateTaxId) throws BusinessException {

		boolean status = false;

		status = rateTaxDAO.deleteRateTax(rateTaxId);

		return status;
	}
	
	public List<RateTaxDTO> getRateTaxesByRateDefinition(int rateDefId, String schema) throws BusinessException {

		RateDefinitionService rateDefinitionService = new RateDefinitionService();
		TaxService taxService = new TaxService();

		List<RateTaxDTO> rateTaxes = null;

		rateTaxes = rateTaxDAO.getRateTaxesByRateDefinition(rateDefId);

		if (rateTaxes != null && !rateTaxes.isEmpty()) {

			if ((schema != null && schema.trim().length() > 0) && schema.equalsIgnoreCase("full")) {

				for (int i = 0; i < rateTaxes.size(); i++) {

					RateTaxDTO rateTax = rateTaxes.get(i);

					RateDefinitionDTO rateDefinition = rateDefinitionService
							.getRateDefinition(rateTax.getRateDefinition().getRateDefId(), schema);
					rateTax.setRateDefinition(rateDefinition);

					TaxDTO tax = taxService.getTax(rateTax.getTax().getTaxId());
					rateTax.setTax(tax);

					rateTaxes.set(i, rateTax);
				}
			}

			return rateTaxes;
		} else {

			return Collections.emptyList();
		}
	}
}

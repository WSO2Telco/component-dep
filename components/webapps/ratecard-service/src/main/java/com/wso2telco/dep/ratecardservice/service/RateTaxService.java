package com.wso2telco.dep.ratecardservice.service;

import java.util.Collections;
import java.util.List;
import com.wso2telco.dep.ratecardservice.dao.RateTaxDAO;
import com.wso2telco.dep.ratecardservice.dao.model.RateDefinitionDTO;
import com.wso2telco.dep.ratecardservice.dao.model.RateTaxDTO;
import com.wso2telco.dep.ratecardservice.dao.model.TaxDTO;

public class RateTaxService {

	RateTaxDAO rateTaxDAO;

	{
		rateTaxDAO = new RateTaxDAO();
	}

	public RateTaxDTO addRateTax(RateTaxDTO rateTax) throws Exception {

		RateTaxDTO newRateTax = null;

		try {

			newRateTax = rateTaxDAO.addRateTax(rateTax);
			newRateTax = getRateTax(newRateTax.getRateTaxId(), null);
		} catch (Exception e) {

			throw e;
		}

		return newRateTax;
	}

	public RateTaxDTO getRateTax(int rateTaxId, String schema) throws Exception {

		RateDefinitionService rateDefinitionService = new RateDefinitionService();
		TaxService taxService = new TaxService();

		RateTaxDTO rateTax = null;

		try {

			rateTax = rateTaxDAO.getRateTax(rateTaxId);

			if ((schema != null && schema.trim().length() > 0) && schema.equalsIgnoreCase("full")) {
				
				RateDefinitionDTO rateDefinition = rateDefinitionService
						.getRateDefinition(rateTax.getRateDefinition().getRateDefId(), schema);
				rateTax.setRateDefinition(rateDefinition);

				TaxDTO tax = taxService.getTax(rateTax.getTax().getTaxId());
				rateTax.setTax(tax);
			}			
		} catch (Exception e) {

			throw e;
		}

		return rateTax;
	}

	public List<RateTaxDTO> getRateTaxes(int taxId, String schema) throws Exception {

		RateDefinitionService rateDefinitionService = new RateDefinitionService();
		TaxService taxService = new TaxService();

		List<RateTaxDTO> rateTaxes = null;

		try {

			rateTaxes = rateTaxDAO.getRateTaxes(taxId);
		} catch (Exception e) {

			throw e;
		}

		if (rateTaxes != null) {

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

	public boolean deleteRateTax(int rateTaxId) throws Exception {

		boolean status = false;

		try {

			status = rateTaxDAO.deleteRateTax(rateTaxId);
		} catch (Exception e) {

			throw e;
		}

		return status;
	}
}

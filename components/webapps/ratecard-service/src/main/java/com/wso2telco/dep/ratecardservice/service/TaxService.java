package com.wso2telco.dep.ratecardservice.service;

import java.util.Collections;
import java.util.List;
import com.wso2telco.dep.ratecardservice.dao.TaxDAO;
import com.wso2telco.dep.ratecardservice.dao.model.TaxDTO;

public class TaxService {

	TaxDAO taxDAO;

	{
		taxDAO = new TaxDAO();
	}

	public List<TaxDTO> getTaxes() throws Exception {

		List<TaxDTO> taxes = null;

		try {

			taxes = taxDAO.getTaxes();
		} catch (Exception e) {

			throw e;
		}

		if (taxes != null) {

			return taxes;
		} else {

			return Collections.emptyList();
		}
	}

	public TaxDTO addTax(TaxDTO tax) throws Exception {

		TaxDTO newTax = null;

		try {

			newTax = taxDAO.addTax(tax);
			newTax = getTax(newTax.getTaxId());
		} catch (Exception e) {

			throw e;
		}

		return newTax;
	}

	public TaxDTO getTax(int taxId) throws Exception {

		TaxDTO tax = null;

		try {

			tax = taxDAO.getTax(taxId);
		} catch (Exception e) {

			throw e;
		}

		return tax;
	}
}

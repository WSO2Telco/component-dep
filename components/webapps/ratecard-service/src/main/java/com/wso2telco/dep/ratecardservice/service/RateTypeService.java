package com.wso2telco.dep.ratecardservice.service;

import java.util.Collections;
import java.util.List;
import com.wso2telco.dep.ratecardservice.dao.RateTypeDAO;
import com.wso2telco.dep.ratecardservice.dao.model.RateTypeDTO;

public class RateTypeService {

	RateTypeDAO rateTypeDAO;

	{
		rateTypeDAO = new RateTypeDAO();
	}

	public List<RateTypeDTO> getRateTypes() throws Exception {

		List<RateTypeDTO> rateTypes = null;

		try {

			rateTypes = rateTypeDAO.getRateTypes();
		} catch (Exception e) {

			throw e;
		}

		if (rateTypes != null) {

			return rateTypes;
		} else {

			return Collections.emptyList();
		}
	}
}

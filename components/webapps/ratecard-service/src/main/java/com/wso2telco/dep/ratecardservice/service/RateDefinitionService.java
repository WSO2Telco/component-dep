package com.wso2telco.dep.ratecardservice.service;

import java.util.Collections;
import java.util.List;
import com.wso2telco.dep.ratecardservice.dao.RateDefinitionDAO;
import com.wso2telco.dep.ratecardservice.dao.model.RateDefinitionDTO;

public class RateDefinitionService {

	RateDefinitionDAO rateDefinitionDAO;

	{
		rateDefinitionDAO = new RateDefinitionDAO();
	}

	public List<RateDefinitionDTO> getRateDefinitions() throws Exception {

		List<RateDefinitionDTO> rateDefinitions = null;

		try {

			rateDefinitions = rateDefinitionDAO.getRateDefinitions();
		} catch (Exception e) {

			throw e;
		}

		if (rateDefinitions != null) {

			return rateDefinitions;
		} else {

			return Collections.emptyList();
		}
	}

	public RateDefinitionDTO addRateDefinition(RateDefinitionDTO rateDefinition) throws Exception {

		RateDefinitionDTO newRateDefinition = null;

		try {

			newRateDefinition = rateDefinitionDAO.addRateDefinition(rateDefinition);
			newRateDefinition = rateDefinitionDAO.getRateDefinition(newRateDefinition.getRateDefId());
		} catch (Exception e) {

			throw e;
		}

		return newRateDefinition;
	}

	public RateDefinitionDTO getRateDefinition(int rateDefId) throws Exception {

		RateDefinitionDTO rateDefinition = null;

		try {

			rateDefinition = rateDefinitionDAO.getRateDefinition(rateDefId);
		} catch (Exception e) {

			throw e;
		}

		return rateDefinition;
	}
}

package com.wso2telco.dep.ratecardservice.service;

import java.util.Collections;
import java.util.List;
import com.wso2telco.dep.ratecardservice.dao.APIDAO;
import com.wso2telco.dep.ratecardservice.dao.model.APIDTO;

public class APIService {

	APIDAO apiDAO;

	{
		apiDAO = new APIDAO();
	}

	public List<APIDTO> getAPIs() throws Exception {

		List<APIDTO> apis = null;

		try {

			apis = apiDAO.getAPIs();
		} catch (Exception e) {

			throw e;
		}

		if (apis != null) {

			return apis;
		} else {

			return Collections.emptyList();
		}
	}
	
	public APIDTO getAPI(int apiId) throws Exception {

		APIDTO api = null;

		try {

			api = apiDAO.getAPI(apiId);
		} catch (Exception e) {

			throw e;
		}

		return api;
	}
}

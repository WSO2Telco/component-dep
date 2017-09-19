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
import com.wso2telco.dep.ratecardservice.dao.APIDAO;
import com.wso2telco.dep.ratecardservice.dao.model.APIDTO;

public class APIService {

	APIDAO apiDAO;
	
	public APIService(){
		
		apiDAO = new APIDAO();
	}

	public List<APIDTO> getAPIs() throws BusinessException {

		List<APIDTO> apis = null;

		apis = apiDAO.getAPIs();

		if (apis != null) {

			return apis;
		} else {

			return Collections.emptyList();
		}
	}

	public APIDTO getAPI(int apiId) throws BusinessException {

		APIDTO api = null;

		api = apiDAO.getAPI(apiId);

		return api;
	}
}

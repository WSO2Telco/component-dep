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
import com.wso2telco.dep.ratecardservice.dao.RateCategoryDAO;
import com.wso2telco.dep.ratecardservice.dao.model.CategoryDTO;
import com.wso2telco.dep.ratecardservice.dao.model.RateCategoryDTO;
import com.wso2telco.dep.ratecardservice.dao.model.RateDefinitionDTO;
import com.wso2telco.dep.ratecardservice.dao.model.TariffDTO;

public class RateCategoryService {

	RateCategoryDAO rateCategoryDAO;

	public RateCategoryService(){
		
		rateCategoryDAO = new RateCategoryDAO();
	}
	
	public RateCategoryDTO addRateCategory(RateCategoryDTO rateCategory) throws BusinessException {

		RateCategoryDTO newRateCategory = null;

		newRateCategory = rateCategoryDAO.addRateCategory(rateCategory);
		newRateCategory = getRateCategory(newRateCategory.getRateCategoryId(), null);

		return newRateCategory;
	}

	public List<RateCategoryDTO> getRateCategories(int rateDefId, String schema) throws BusinessException {

		RateDefinitionService rateDefinitionService = new RateDefinitionService();
		CategoryService categoryService = new CategoryService();
		TariffService tariffService = new TariffService();

		List<RateCategoryDTO> rateCategories = null;

		rateCategories = rateCategoryDAO.getRateCategories(rateDefId);

		if (rateCategories != null && !rateCategories.isEmpty()) {

			if ((schema != null && schema.trim().length() > 0) && schema.equalsIgnoreCase("full")) {

				for (int i = 0; i < rateCategories.size(); i++) {

					RateCategoryDTO rateCategory = rateCategories.get(i);

					RateDefinitionDTO rateDefinition = rateDefinitionService
							.getRateDefinition(rateCategory.getRateDefinition().getRateDefId(), schema);
					rateCategory.setRateDefinition(rateDefinition);

					CategoryDTO category = categoryService.getCategory(rateCategory.getCategory().getCategoryId());
					rateCategory.setCategory(category);

					CategoryDTO subCategory = categoryService
							.getCategory(rateCategory.getSubCategory().getCategoryId());
					rateCategory.setSubCategory(subCategory);

					TariffDTO tariff = tariffService.getTariff(rateCategory.getTariff().getTariffId());
					rateCategory.setTariff(tariff);

					rateCategories.set(i, rateCategory);
				}
			}

			return rateCategories;
		} else {

			return Collections.emptyList();
		}
	}

	public RateCategoryDTO getRateCategory(int rateCategoryId, String schema) throws BusinessException {

		RateDefinitionService rateDefinitionService = new RateDefinitionService();
		CategoryService categoryService = new CategoryService();
		TariffService tariffService = new TariffService();

		RateCategoryDTO rateCategory = null;

		rateCategory = rateCategoryDAO.getRateCategory(rateCategoryId);

		if ((schema != null && schema.trim().length() > 0) && schema.equalsIgnoreCase("full")) {

			RateDefinitionDTO rateDefinition = rateDefinitionService
					.getRateDefinition(rateCategory.getRateDefinition().getRateDefId(), schema);
			rateCategory.setRateDefinition(rateDefinition);

			CategoryDTO category = categoryService.getCategory(rateCategory.getCategory().getCategoryId());
			rateCategory.setCategory(category);

			CategoryDTO subCategory = categoryService.getCategory(rateCategory.getSubCategory().getCategoryId());
			rateCategory.setSubCategory(subCategory);

			TariffDTO tariff = tariffService.getTariff(rateCategory.getTariff().getTariffId());
			rateCategory.setTariff(tariff);
		}

		return rateCategory;
	}
}

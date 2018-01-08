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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.ratecardservice.dao.model.RateCard;
import com.wso2telco.dep.ratecardservice.dao.model.RateCardDTO;
import com.wso2telco.dep.ratecardservice.dao.model.RateCategoryDTO;
import com.wso2telco.dep.ratecardservice.dao.model.RateDefinitionDTO;
import com.wso2telco.dep.ratecardservice.dao.model.RateTaxDTO;

public class RateCardService {

	private final Log log = LogFactory.getLog(RateCardService.class);

	public RateCardDTO addRateCard(RateCardDTO rateCard) throws BusinessException {

		RateDefinitionService rateDefinitionService = new RateDefinitionService();
		RateCategoryService rateCategoryService = new RateCategoryService();
		RateTaxService rateTaxService = new RateTaxService();

		RateCardDTO newRateCard = null;
		RateDefinitionDTO newRateDefinition = null;

		RateDefinitionDTO rateDefinition = rateCard.getRateDefinition();
		RateCategoryDTO[] rateCategoryArray = rateCard.getRateCategories();
		RateTaxDTO[] rateTaxArray = rateCard.getRateTaxes();

		rateDefinition.setCreatedBy(rateCard.getCreatedBy());
		newRateDefinition = rateDefinitionService.addRateDefinition(rateDefinition);

		try {

			for (int i = 0; i < rateCategoryArray.length; i++) {

				RateCategoryDTO rateCategory = rateCategoryArray[i];
				rateCategory.setRateDefinition(newRateDefinition);
				rateCategory.setCreatedBy(rateCard.getCreatedBy());
				RateCategoryDTO newRateCategory = rateCategoryService.addRateCategory(rateCategory);

				rateCategoryArray[i] = newRateCategory;
			}

			for (int i = 0; i < rateTaxArray.length; i++) {

				RateTaxDTO rateTax = rateTaxArray[i];
				rateTax.setRateDefinition(newRateDefinition);
				rateTax.setCreatedBy(rateCard.getCreatedBy());
				RateTaxDTO newRateTax = rateTaxService.addRateTax(rateTax);

				rateTaxArray[i] = newRateTax;
			}
		} catch (Exception e) {

			rateDefinitionService.deleteRateDefinition(newRateDefinition.getRateDefId());
			log.error("error while saving rate categories and taxes : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		}

		newRateCard = new RateCardDTO();

		newRateCard.setRateDefinition(newRateDefinition);
		newRateCard.setRateCategories(rateCategoryArray);
		newRateCard.setRateTaxes(rateTaxArray);

		return newRateCard;
	}

	public List<RateCardDTO> getRateCards(String schema) throws BusinessException {

		RateDefinitionService rateDefinitionService = new RateDefinitionService();
		RateCategoryService rateCategoryService = new RateCategoryService();
		RateTaxService rateTaxService = new RateTaxService();

		List<RateCardDTO> rateCards = null;

		List<RateDefinitionDTO> rateDefinitions = rateDefinitionService.getRateDefinitions(schema);

		if (rateDefinitions != null && !rateDefinitions.isEmpty()) {

			rateCards = new ArrayList<RateCardDTO>();

			for (int i = 0; i < rateDefinitions.size(); i++) {

				RateCardDTO rateCard = new RateCardDTO();

				RateDefinitionDTO rateDefinition = rateDefinitions.get(i);

				List<RateCategoryDTO> rateCategories = rateCategoryService
						.getRateCategories(rateDefinition.getRateDefId(), schema);
				RateCategoryDTO[] rateCategoryArray = new RateCategoryDTO[rateCategories.size()];

				List<RateTaxDTO> rateTaxes = rateTaxService.getRateTaxesByRateDefinition(rateDefinition.getRateDefId(),
						schema);
				RateTaxDTO[] rateTaxArray = new RateTaxDTO[rateTaxes.size()];

				rateCard.setRateDefinition(rateDefinition);
				rateCard.setRateCategories(rateCategories.toArray(rateCategoryArray));
				rateCard.setRateTaxes(rateTaxes.toArray(rateTaxArray));

				rateCards.add(rateCard);
			}

			return rateCards;
		} else {

			return Collections.emptyList();
		}
	}

	public List<RateCard> getRateCards(String schema, String test) throws BusinessException {

		RateDefinitionService rateDefinitionService = new RateDefinitionService();
		RateCategoryService rateCategoryService = new RateCategoryService();
		RateTaxService rateTaxService = new RateTaxService();

		List<RateCard> rateCards = null;

		List<RateDefinitionDTO> rateDefinitions = rateDefinitionService.getRateDefinitions(schema);

		if (rateDefinitions != null && !rateDefinitions.isEmpty()) {

			rateCards = new ArrayList<RateCard>();

			for (int i = 0; i < rateDefinitions.size(); i++) {

				RateCard rateCard = new RateCard();

				RateDefinitionDTO rateDefinition = rateDefinitions.get(i);

				rateCard.setRateDefId(rateDefinition.getRateDefId());
				rateCard.setRateDefName(rateDefinition.getRateDefName());
				rateCard.setRateDefDescription(rateDefinition.getRateDefDescription());
				rateCard.setRateDefDefault(rateDefinition.getRateDefDefault());
				rateCard.setCurrency(rateDefinition.getCurrency());
				rateCard.setRateType(rateDefinition.getRateType());
				rateCard.setRateDefCategoryBase(rateDefinition.getRateDefCategoryBase());
				rateCard.setTariff(rateDefinition.getTariff());

				List<RateCategoryDTO> rateCategories = rateCategoryService
						.getRateCategories(rateDefinition.getRateDefId(), schema);
				RateCard.RateCategory[] rateCategoryArray = new RateCard.RateCategory[rateCategories.size()];
				for (int j = 0; j < rateCategories.size(); j++) {

					RateCategoryDTO rateCategoryDTO = rateCategories.get(j);
					RateCard.RateCategory rateCategory = new RateCard.RateCategory();

					rateCategory.setRateCategoryId(rateCategoryDTO.getRateCategoryId());
					rateCategory.setCategory(rateCategoryDTO.getCategory());
					rateCategory.setSubCategory(rateCategoryDTO.getSubCategory());
					rateCategory.setTariff(rateCategoryDTO.getTariff());
					rateCategory.setCreatedBy(rateCategoryDTO.getCreatedBy());

					rateCategoryArray[j] = rateCategory;
				}

				List<RateTaxDTO> rateTaxes = rateTaxService.getRateTaxesByRateDefinition(rateDefinition.getRateDefId(),
						schema);
				RateCard.RateTax[] rateTaxArray = new RateCard.RateTax[rateTaxes.size()];
				for (int k = 0; k < rateTaxes.size(); k++) {

					RateTaxDTO rateTaxDTO = rateTaxes.get(k);
					RateCard.RateTax rateTax = new RateCard.RateTax();

					rateTax.setRateTaxId(rateTaxDTO.getRateTaxId());
					rateTax.setTax(rateTaxDTO.getTax());
					rateTax.setCreatedBy(rateTaxDTO.getCreatedBy());

					rateTaxArray[k] = rateTax;
				}

				rateCard.setRateCategories(rateCategoryArray);
				rateCard.setRateTaxes(rateTaxArray);

				rateCard.setCreatedBy(rateDefinition.getCreatedBy());

				rateCards.add(rateCard);
			}

			return rateCards;
		} else {

			return Collections.emptyList();
		}
	}
}

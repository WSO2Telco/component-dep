package com.wso2telco.dep.ratecardservice.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.ratecardservice.dao.model.RateCardDTO;
import com.wso2telco.dep.ratecardservice.dao.model.RateCategoryDTO;
import com.wso2telco.dep.ratecardservice.dao.model.RateDefinitionDTO;
import com.wso2telco.dep.ratecardservice.dao.model.RateTaxDTO;

public class RateCardService {

	private final Log log = LogFactory.getLog(RateCardService.class);

	public RateCardDTO addRateCard(RateCardDTO rateCard) throws Exception {

		RateDefinitionService rateDefinitionService = new RateDefinitionService();
		RateCategoryService rateCategoryService = new RateCategoryService();
		RateTaxService rateTaxService = new RateTaxService();

		RateCardDTO newRateCard = null;
		RateDefinitionDTO newRateDefinition = null;

		try {

			RateDefinitionDTO rateDefinition = rateCard.getRateDefinition();
			RateCategoryDTO rateCategoryArray[] = rateCard.getRateCategories();
			RateTaxDTO rateTaxArray[] = rateCard.getRateTaxes();

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
		} catch (Exception e) {

			throw e;
		}

		return newRateCard;
	}
}

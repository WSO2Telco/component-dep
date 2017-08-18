package com.wso2telco.dep.ratecardservice.service;

import java.util.Collections;
import java.util.List;
import com.wso2telco.dep.ratecardservice.dao.RateCategoryDAO;
import com.wso2telco.dep.ratecardservice.dao.model.CategoryDTO;
import com.wso2telco.dep.ratecardservice.dao.model.RateCategoryDTO;
import com.wso2telco.dep.ratecardservice.dao.model.RateDefinitionDTO;
import com.wso2telco.dep.ratecardservice.dao.model.TariffDTO;

public class RateCategoryService {

	RateCategoryDAO rateCategoryDAO;

	{
		rateCategoryDAO = new RateCategoryDAO();
	}

	public RateCategoryDTO addRateCategory(RateCategoryDTO rateCategory) throws Exception {

		RateCategoryDTO newRateCategory = null;

		try {

			newRateCategory = rateCategoryDAO.addRateCategory(rateCategory);
			newRateCategory = getRateCategory(newRateCategory.getRateCategoryId());
		} catch (Exception e) {

			throw e;
		}

		return newRateCategory;
	}

	public List<RateCategoryDTO> getRateCategories(int rateDefId) throws Exception {

		RateDefinitionService rateDefinitionService = new RateDefinitionService();
		CategoryService categoryService = new CategoryService();
		TariffService tariffService = new TariffService();

		List<RateCategoryDTO> rateCategories = null;

		try {

			rateCategories = rateCategoryDAO.getRateCategories(rateDefId);
		} catch (Exception e) {

			throw e;
		}

		if (rateCategories != null) {

			for (int i = 0; i < rateCategories.size(); i++) {

				RateCategoryDTO rateCategory = rateCategories.get(i);

				RateDefinitionDTO rateDefinition = rateDefinitionService
						.getRateDefinition(rateCategory.getRateDefinition().getRateDefId());
				rateCategory.setRateDefinition(rateDefinition);

				CategoryDTO category = categoryService.getCategory(rateCategory.getCategory().getCategoryId());
				rateCategory.setCategory(category);

				CategoryDTO subCategory = categoryService.getCategory(rateCategory.getSubCategory().getCategoryId());
				rateCategory.setSubCategory(subCategory);

				TariffDTO tariff = tariffService.getTariff(rateCategory.getTariff().getTariffId());
				rateCategory.setTariff(tariff);

				rateCategories.set(i, rateCategory);
			}

			return rateCategories;
		} else {

			return Collections.emptyList();
		}
	}

	public RateCategoryDTO getRateCategory(int rateCategoryId) throws Exception {

		RateDefinitionService rateDefinitionService = new RateDefinitionService();
		CategoryService categoryService = new CategoryService();
		TariffService tariffService = new TariffService();

		RateCategoryDTO rateCategory = null;

		try {

			rateCategory = rateCategoryDAO.getRateCategory(rateCategoryId);

			RateDefinitionDTO rateDefinition = rateDefinitionService
					.getRateDefinition(rateCategory.getRateDefinition().getRateDefId());
			rateCategory.setRateDefinition(rateDefinition);

			CategoryDTO category = categoryService.getCategory(rateCategory.getCategory().getCategoryId());
			rateCategory.setCategory(category);

			CategoryDTO subCategory = categoryService.getCategory(rateCategory.getSubCategory().getCategoryId());
			rateCategory.setSubCategory(subCategory);

			TariffDTO tariff = tariffService.getTariff(rateCategory.getTariff().getTariffId());
			rateCategory.setTariff(tariff);
		} catch (Exception e) {

			throw e;
		}

		return rateCategory;
	}
}

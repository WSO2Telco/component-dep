package com.wso2telco.dep.ratecardservice.service;

import java.util.Collections;
import java.util.List;
import com.wso2telco.dep.ratecardservice.dao.CategoryDAO;
import com.wso2telco.dep.ratecardservice.dao.model.CategoryDTO;

public class CategoryService {

	CategoryDAO categoryDAO;

	{
		categoryDAO = new CategoryDAO();
	}

	public List<CategoryDTO> getCategories() throws Exception {

		List<CategoryDTO> categories = null;

		try {

			categories = categoryDAO.getCategories();
		} catch (Exception e) {

			throw e;
		}

		if (categories != null) {

			return categories;
		} else {

			return Collections.emptyList();
		}
	}

	public CategoryDTO addCategory(CategoryDTO category) throws Exception {

		CategoryDTO newCategory = null;

		try {

			newCategory = categoryDAO.addCategory(category);
		} catch (Exception e) {

			throw e;
		}

		return newCategory;
	}
}

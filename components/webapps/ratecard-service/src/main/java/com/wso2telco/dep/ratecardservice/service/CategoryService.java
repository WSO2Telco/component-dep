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
import com.wso2telco.dep.ratecardservice.dao.CategoryDAO;
import com.wso2telco.dep.ratecardservice.dao.model.CategoryDTO;

public class CategoryService {

	CategoryDAO categoryDAO;

	public CategoryService() {

		categoryDAO = new CategoryDAO();
	}

	public List<CategoryDTO> getCategories() throws BusinessException {

		List<CategoryDTO> categories = null;

		categories = categoryDAO.getCategories();

		if (categories != null && !categories.isEmpty()) {

			return categories;
		} else {

			return Collections.emptyList();
		}
	}

	public CategoryDTO addCategory(CategoryDTO category) throws BusinessException {

		CategoryDTO newCategory = null;

		newCategory = categoryDAO.addCategory(category);
		newCategory = getCategory(newCategory.getCategoryId());

		return newCategory;
	}

	public CategoryDTO getCategory(int categoryId) throws BusinessException {

		CategoryDTO category = null;

		category = categoryDAO.getCategory(categoryId);

		return category;
	}
}

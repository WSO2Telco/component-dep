/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *  
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.dep.mediator.service;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.wso2telco.dep.mediator.dao.PaymentDAO;
import com.wso2telco.utils.exception.BusinessException;
import com.wso2telco.utils.exception.GenaralError;

public class PaymentService {

	/** The Constant log. */
	private final Log log = LogFactory.getLog(PaymentService.class);

	PaymentDAO paymentDAO;

	{
		paymentDAO = new PaymentDAO();
	}

	public List<String> getValidPayCategories() throws BusinessException {

		List<String> categories = null;

		try {

			categories = paymentDAO.getValidPayCategories();
		} catch (Exception e) {

			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		return categories;
	}
}

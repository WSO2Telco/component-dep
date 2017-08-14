package com.wso2telco.dep.ratecardservice.service;

import com.wso2telco.dep.ratecardservice.dao.OperatorDAO;
import com.wso2telco.dep.ratecardservice.dao.model.OperatorDTO;

public class OperatorService {

	OperatorDAO operatorDAO;

	{
		operatorDAO = new OperatorDAO();
	}

	public OperatorDTO getOperator(int operatorId) throws Exception {

		OperatorDTO operator = null;

		try {

			operator = operatorDAO.getOperator(operatorId);
		} catch (Exception e) {

			throw e;
		}

		return operator;
	}
}

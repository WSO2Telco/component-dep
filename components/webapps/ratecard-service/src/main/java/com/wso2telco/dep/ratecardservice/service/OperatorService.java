package com.wso2telco.dep.ratecardservice.service;

import java.util.Collections;
import java.util.List;
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
	
	public List<OperatorDTO> getOperators() throws Exception {

		List<OperatorDTO> operators = null;

		try {

			operators = operatorDAO.getOperators();
		} catch (Exception e) {

			throw e;
		}

		if (operators != null) {

			return operators;
		} else {

			return Collections.emptyList();
		}
	}
}

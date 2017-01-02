package com.wso2telco.dep.operator.onboarding.service;

import java.util.ArrayList;
import java.util.List;


import com.wso2telco.dep.operator.onboarding.dao.OperatorDAO;
import com.wso2telco.dep.operator.onboarding.dao.hibernate.HibernateOperatorDAO;
import com.wso2telco.dep.operator.onboarding.dao.model.domain.Operator;

public class App {
	public static void main( String[] args ) throws Exception{
        
		OperatorDAO dao = new HibernateOperatorDAO();
		
		Operator operator = new Operator();
		operator.setId(6);
		operator.setOperatorname("test6");
	
		List<Operator> operators = new ArrayList<Operator>();
		operators.add(operator);
		
		dao.saveOperators(operators);
		
	}

}

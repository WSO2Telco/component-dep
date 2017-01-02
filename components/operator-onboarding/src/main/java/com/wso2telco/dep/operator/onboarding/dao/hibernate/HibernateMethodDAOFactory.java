package com.wso2telco.dep.operator.onboarding.dao.hibernate;

import com.wso2telco.dep.operator.onboarding.dao.GenericMethodDAO;
import com.wso2telco.dep.operator.onboarding.util.MethodType;

public class HibernateMethodDAOFactory {
	
	public static GenericMethodDAO getInstance(final MethodType methodType) {

		GenericMethodDAO genericMethodDAO = null;

		switch (methodType) {
		case PREFIX:
			genericMethodDAO = new HibernatePrefixDAO();
			break;
		case RANGE:
			genericMethodDAO = new HibernateRangeDAO();
			break;
		case PATHFINDER:
			genericMethodDAO = new HibernatePathfinderDAO();
			break;
		default:
			break;
		}
		return genericMethodDAO;
	}
}

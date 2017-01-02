package com.wso2telco.dep.operator.onboarding.dao.hibernate;

import java.io.Serializable;


import com.wso2telco.core.dbutils.hibernateutil.HibernateAbstractDAO;
import com.wso2telco.dep.operator.onboarding.dao.GenericMethodDAO;

public abstract class HibernateGenericMethodDAO<T extends Serializable> extends HibernateAbstractDAO implements GenericMethodDAO<T>{

	public void saveMethod(T obj) throws Exception {
		try {
		    save(obj);
		} catch (Exception e) {
		    //LOG.error("saveMethod", e);
		    throw e;
		}
	}
	
	
}

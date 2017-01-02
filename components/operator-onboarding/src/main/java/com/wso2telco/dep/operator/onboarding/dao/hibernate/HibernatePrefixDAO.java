package com.wso2telco.dep.operator.onboarding.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.wso2telco.dep.operator.onboarding.dao.model.domain.Prefix;

public class HibernatePrefixDAO extends HibernateGenericMethodDAO<Prefix>{

	@Override
	public List<Prefix> getMethod(int routingdid) throws Exception {
		Session sess = getSession();
		List<Prefix> prefixList = new ArrayList<Prefix>();

		try {
			prefixList = (List<Prefix>) sess.createQuery("from Prefix where routing.id = :routingdid").setParameter("routingdid", routingdid).list();
		} catch (Exception e) {
		    //LOG.error("getMethod", e);
		    throw e;
		}
		return prefixList;
	}


}

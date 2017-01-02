package com.wso2telco.dep.operator.onboarding.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.wso2telco.dep.operator.onboarding.dao.model.domain.Range;

public class HibernateRangeDAO extends HibernateGenericMethodDAO<Range>{

	@Override
	public List<Range> getMethod(int routingdid) throws Exception {
		Session sess = getSession();
		List<Range> rangeList = new ArrayList<Range>();

		try {
			rangeList = (List<Range>) sess.createQuery("from Range where routing.id = :routingdid").setParameter("routingdid", routingdid).list();
		} catch (Exception e) {
		    //LOG.error("getMethod", e);
		    throw e;
		}
		return rangeList;
	}

}

package com.wso2telco.dep.operator.onboarding.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.wso2telco.dep.operator.onboarding.dao.model.domain.Pathfinder;

public class HibernatePathfinderDAO extends HibernateGenericMethodDAO<Pathfinder>{

	@Override
	public List<Pathfinder> getMethod(int routingdid) throws Exception {
		Session sess = getSession();
		List<Pathfinder> pathfinderList = new ArrayList<Pathfinder>();

		try {
			pathfinderList = (List<Pathfinder>) sess.createQuery("from Pathfinder where routing.id = :routingdid").setParameter("routingdid", routingdid).list();
		} catch (Exception e) {
		    //LOG.error("getMethod", e);
		    throw e;
		}
		return pathfinderList;
	}


}

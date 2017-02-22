package com.wso2telco.dep.operator.onboarding.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;

import com.wso2telco.dep.operator.onboarding.dao.OperatorEndpointDAO;
import com.wso2telco.dep.operator.onboarding.dao.model.domain.Operator;
import com.wso2telco.dep.operator.onboarding.dao.model.domain.OperatorEndpoint;
import com.wso2telco.dep.operator.onboarding.hibernateutil.HibernateAbstractDAO;

public class HibernateOperatorEndpointDAO extends HibernateAbstractDAO implements OperatorEndpointDAO{

	@Override
	public void saveOperatorEndpoints(List<OperatorEndpoint> operatorEndpoints) throws Exception {
		List<Object> objectList = new ArrayList<Object>(operatorEndpoints);
		try {
			saveList(objectList);
		} catch (Exception e) {
			throw e;
		}
		
	}

	@Override
	public void updateOperatorEndpoint(OperatorEndpoint operatorEndpoint) throws Exception {
		try {
			saveOrUpdate(operatorEndpoint);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<OperatorEndpoint> getOperatorEndpoints(int operatorid) throws Exception {
		Session sess = getSession();

		List<OperatorEndpoint> operatorEndpoints = null;
		try {
			operatorEndpoints = sess.createQuery("from OperatorEndpoint where operator.id = :operatorid").setParameter("operatorid", operatorid).list();
		} catch (Exception e) {
			//LOG.error("getOperators", e);
			throw e;
		} finally {
			sess.close();
		}
		return operatorEndpoints;
	}

	@Override
	public OperatorEndpoint getOperatorEndpoint(int operatorid, String endpoint) throws Exception {
		Session sess = getSession();

		OperatorEndpoint operatorEndpoint = null;
		try {
			operatorEndpoint = (OperatorEndpoint) sess.createQuery("from OperatorEndpoint where operator.id = :operatorid and endpoint = :endpoint").setParameter("operatorid", operatorid).setParameter("endpoint", endpoint).uniqueResult();
		} catch (NoResultException e) {
		    return null;
		} catch (Exception e) {
		    throw e;
		} finally {
			sess.close();
		}
		return operatorEndpoint;
	}

	

}

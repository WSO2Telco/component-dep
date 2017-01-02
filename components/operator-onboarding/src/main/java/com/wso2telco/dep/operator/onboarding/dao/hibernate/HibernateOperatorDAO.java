package com.wso2telco.dep.operator.onboarding.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import org.hibernate.query.Query;
import org.hibernate.Session;

import com.wso2telco.dep.operator.onboarding.dao.OperatorDAO;
import com.wso2telco.dep.operator.onboarding.dao.model.domain.Operator;
import com.wso2telco.dep.operator.onboarding.dao.model.domain.Routing;
import com.wso2telco.dep.operator.onboarding.dao.model.domain.RoutingMethod;
import com.wso2telco.core.dbutils.hibernateutil.*;
//import com.wso2telco.dep.operator.onboarding.hibernateutil.*;

public class HibernateOperatorDAO extends HibernateAbstractDAO implements OperatorDAO{

	@Override
	public void saveOperators(List<Operator> operators) throws Exception {
		List<Object> objectList = new ArrayList<Object>(operators);
		try {
			saveList(objectList);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<Operator> getOperators() throws Exception {
		Session sess = getSession();

		List<Operator> operators = null;
		try {
			operators = sess.createQuery("from Operator ").list();
		} catch (Exception e) {
			//LOG.error("getOperators", e);
			throw e;
		} finally {
			sess.close();
		}
		return operators;
	}

	@Override
	public void updateOperator(Operator operator) throws Exception {
		try {
			saveOrUpdate(operator);
		} catch (Exception e) {
			throw e;
		}
		
	}

	@Override
	public Operator getOperator(String operatorname) throws Exception {
		Session sess = getSession();

		Operator operator = null;
		try {
			operator = (Operator) sess.createQuery("from Operator where operatorname = :operatorname").setParameter("operatorname", operatorname).uniqueResult();
		} catch (NoResultException e) {
		    return null;
		} catch (Exception e) {
		    throw e;
		} finally {
			sess.close();
		}
		return operator;
	}

	@Override
	public RoutingMethod getRoutingMethod(String methodname) throws Exception {
		Session sess = getSession();
		RoutingMethod routingMethod = null;
		try {
			routingMethod = (RoutingMethod) sess
			    .createQuery("from RoutingMethod WHERE lower(methodname) = :methodname")
			    .setParameter("methodname", methodname).uniqueResult();

		} catch (Exception e) {
		    //LOG.error("getRoutingMethod", e);
		    throw e;
		}

		return routingMethod;
	}

	@Override
	public Routing getRouting(int operatordid, int routingmethodid) throws Exception {
		Session sess = getSession();
		Routing routing = null;
		try {
			routing = (Routing) sess
			    .createQuery(
				    "from Routing WHERE operator.id = ? AND routingMethod.id = ?")
			    .setParameter(0, operatordid).setParameter(1, routingmethodid)
			    .uniqueResult();

		} catch (Exception e) {
		    //LOG.error("getRouting", e);
		    throw e;
		}

		return routing;
	}

	@Override
	public List<RoutingMethod> getAllRoutingMethod() throws Exception {
		Session sess = getSession();
		List<RoutingMethod> methodList = new ArrayList<RoutingMethod>();

		try {
			methodList = (List<RoutingMethod>) sess.createQuery("from RoutingMethod ").list();
		} catch (Exception e) {
		    //LOG.error("getAllRoutingMethod", e);
		    throw e;
		}
		return methodList;
	}

	@Override
	public List<Routing> getAllRouting(int methodid) throws Exception {
		Session sess = getSession();
		List<Routing> routingList = new ArrayList<Routing>();

		try {
			routingList = (List<Routing>) sess
			    .createQuery(
				    "from Routing where RoutingMethod.id = :methodid")
			    .setParameter("methodid", methodid).list();
		} catch (Exception e) {
		    //LOG.error("getAllRouting", e);
		    throw e;
		}
		return routingList;
	}

	@Override
	public void saveRouting(List<Routing> routings) throws Exception {
		List<Object> objectList = new ArrayList<Object>(routings);
		try {
			saveList(objectList);
		} catch (Exception e) {
			throw e;
		}
		
	}
	
}

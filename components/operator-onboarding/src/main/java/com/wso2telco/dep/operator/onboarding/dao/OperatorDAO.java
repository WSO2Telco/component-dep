package com.wso2telco.dep.operator.onboarding.dao;

import java.util.List;

import com.wso2telco.dep.operator.onboarding.dao.model.domain.Operator;
import com.wso2telco.dep.operator.onboarding.dao.model.domain.Routing;
import com.wso2telco.dep.operator.onboarding.dao.model.domain.RoutingMethod;

public interface OperatorDAO {
	
	public void saveOperators(List<Operator> operators) throws Exception;
	
	public List<Operator> getOperators() throws Exception;
	
	public Operator getOperator(String operatorname) throws Exception;
	
	public void updateOperator(Operator operator) throws Exception;
	
	public RoutingMethod getRoutingMethod(String methodname) throws Exception;
	
	public Routing getRouting(int operatordid, int routingmethodid) throws Exception;
	
	public List<RoutingMethod> getAllRoutingMethod() throws Exception;
	
	public List<Routing> getAllRouting(int methodid) throws Exception;
	
	public void saveRouting(List<Routing> routings) throws Exception;

}

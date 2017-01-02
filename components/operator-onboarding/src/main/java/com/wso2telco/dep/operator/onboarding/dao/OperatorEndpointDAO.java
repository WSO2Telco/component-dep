package com.wso2telco.dep.operator.onboarding.dao;

import java.util.List;

import com.wso2telco.dep.operator.onboarding.dao.model.domain.OperatorEndpoint;

public interface OperatorEndpointDAO {
	
	public void saveOperatorEndpoints(List<OperatorEndpoint> operatorEndpoints) throws Exception;
	
	public void updateOperatorEndpoint(OperatorEndpoint operatorEndpoint) throws Exception;

	public List<OperatorEndpoint> getOperatorEndpoints(int operatorid) throws Exception;
	
	public OperatorEndpoint getOperatorEndpoint(int operatorid, String endpoint) throws Exception;

}

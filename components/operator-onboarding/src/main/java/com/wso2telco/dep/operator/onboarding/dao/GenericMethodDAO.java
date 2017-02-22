package com.wso2telco.dep.operator.onboarding.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericMethodDAO<T extends Serializable> {
	
	public void saveMethod(T obj) throws Exception;
	
	public List<T> getMethod(int routingdid) throws Exception;

}

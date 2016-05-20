package com.wso2telco.dep.operatorservice.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.dep.operatorservice.dao.DAO;
import com.wso2telco.dep.operatorservice.model.Operator;
import com.wso2telco.dep.operatorservice.model.OperatorSearchDTO;

public class OparatorService {
	Log LOG = LogFactory.getLog(OparatorService.class);
	DAO dao;
	
	{
		dao = new DAO();
	}
	public List<Operator> loadOperators(OperatorSearchDTO searchDTO) throws Exception {
		LOG.debug(" Got request to loadOperators  searchDTO :"+searchDTO);
		dao.seachOparators(searchDTO);
		return dao.seachOparators(searchDTO);
	}

}

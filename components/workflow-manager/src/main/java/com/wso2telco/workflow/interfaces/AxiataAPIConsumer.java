package com.wso2telco.workflow.interfaces;

import java.sql.Connection;

import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.model.APIIdentifier;

public interface AxiataAPIConsumer {
	
	int getAPIID(APIIdentifier apiId) throws APIManagementException;

}

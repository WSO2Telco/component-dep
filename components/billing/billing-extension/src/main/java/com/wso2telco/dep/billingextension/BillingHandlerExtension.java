package com.wso2telco.dep.billingextension;

import com.wso2telco.core.dbutils.exception.BusinessException;

public interface BillingHandlerExtension {

	public void publishHubAPIRate(int servicesRateDid, int applicationDid ,String apiVersion) throws BusinessException;

	public void publishOperatorAPIRate(int operatorRateDid, int applicationDid , String apiVersion) throws BusinessException;

}

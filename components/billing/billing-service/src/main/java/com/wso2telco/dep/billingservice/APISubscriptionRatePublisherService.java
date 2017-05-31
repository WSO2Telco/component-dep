package com.wso2telco.dep.billingservice;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.billingextension.BillingHandlerExtension;
import com.wso2telco.dep.billingservice.service.RateCardService;

public class APISubscriptionRatePublisherService implements BillingHandlerExtension {
	@Override
	public void publishHubAPIRate(int servicesRateDid, int applicationDid) throws BusinessException {
		try {

			RateCardService rateCardService = new RateCardService();
			rateCardService.setHubSubscriptionRateData(servicesRateDid, applicationDid);
		} catch (Exception e) {

			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		}
	}

	@Override
	public void publishOperatorAPIRate(int operatorRateDid, int applicationDid) throws BusinessException {
		try {

			RateCardService rateCardService = new RateCardService();
			rateCardService.setOperatorSubscriptionRateData(operatorRateDid, applicationDid);
		} catch (Exception e) {

			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		}
	}

}

package com.wso2telco.dep.billingservice.service;

import java.util.Collections;
import java.util.Map;
import com.wso2telco.dep.billingservice.dao.RateCardDAO;

public class RateCardService {

	RateCardDAO rateCardDAO;

	{
		rateCardDAO = new RateCardDAO();
	}

	//TODO:hit method
	public Map<Integer, String> getServiceDetailsByAPICode(String apiCode) throws Exception {

		Map<Integer, String> serviceDetails = null;

		try {

			serviceDetails = rateCardDAO.getServiceDetailsByAPICode(apiCode);
		} catch (Exception e) {

			throw e;
		}

		if (serviceDetails != null) {

			return serviceDetails;
		} else {

			return Collections.emptyMap();
		}
	}

	public Map<Integer, String> getHubRateDetailsByServicesDid(int servicesDid) throws Exception {

		Map<Integer, String> rateDetails = null;

		try {

			rateDetails = rateCardDAO.getHubRateDetailsByServicesDid(servicesDid);
		} catch (Exception e) {

			throw e;
		}

		if (rateDetails != null) {

			return rateDetails;
		} else {

			return Collections.emptyMap();
		}
	}

	public Map<Integer, String> getOperatorRateDetailsByServicesDidAndOperatorCode(int servicesDid, String operatorCode)
			throws Exception {

		Map<Integer, String> rateDetails = null;

		try {

			rateDetails = rateCardDAO.getOperatorRateDetailsByServicesDidAndOperatorCode(servicesDid, operatorCode);
		} catch (Exception e) {

			throw e;
		}

		if (rateDetails != null) {

			return rateDetails;
		} else {

			return Collections.emptyMap();
		}
	}


	//setters
	//public void setHubSubscriptionRateData(int servicesRateDid, int applicationDid, String apiCode) throws Exception {
	public void setHubSubscriptionRateData(int servicesRateDid, int applicationDid) throws Exception {

		try {

			rateCardDAO.setHubSubscriptionRateData(servicesRateDid, applicationDid);
		} catch (Exception e) {

			throw e;
		}
	}
//int operatorRateDid, int applicationDid, String operatorId, String operationId
	//public void setOperatorSubscriptionRateData(int operatorRateDid, int applicationDid) throws Exception {
	public void setOperatorSubscriptionRateData(int operatorRateDid, int applicationDid, String operatorId, String operationId) throws Exception {

		try {

			rateCardDAO.setOperatorSubscriptionRateData(operatorRateDid, applicationDid, operatorId, operationId);
		} catch (Exception e) {

			throw e;
		}
	}
}

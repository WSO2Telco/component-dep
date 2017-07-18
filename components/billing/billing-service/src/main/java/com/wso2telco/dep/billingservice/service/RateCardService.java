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

	public Map<Integer, Map<String,String>> getHubRateDetailsByServicesDid(int servicesDid) throws Exception {

		Map<Integer, Map<String,String>> rateDetails = null;

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

	public Map<Integer, Map<String,String>> getOperatorRateDetailsByServicesDidAndOperatorCode(int servicesDid, String operatorCode)
			throws Exception {

		Map<Integer, Map<String,String>> rateDetails = null;

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
			boolean recordsExists = rateCardDAO.checkHubSubscriptionRateDataExists(servicesRateDid, applicationDid);
			if (recordsExists) {
				rateCardDAO.deleteHubSubscriptionRateData(servicesRateDid, applicationDid);
			}
			rateCardDAO.setHubSubscriptionRateData(servicesRateDid, applicationDid);
		} catch (Exception e) {

			throw e;
		}
	}
//int operatorRateDid, int applicationDid, String operatorId, String operationId
	//public void setOperatorSubscriptionRateData(int operatorRateDid, int applicationDid) throws Exception {
	public void setOperatorSubscriptionRateData(int operatorRateDid, int applicationDid) throws Exception {

		try {
			boolean recordsExists = rateCardDAO.checkOperatorSubscriptionRateData(operatorRateDid, applicationDid);
			if (recordsExists) {
				rateCardDAO.deleteOperatorSubscriptionRateData(operatorRateDid, applicationDid);
			}
			rateCardDAO.setOperatorSubscriptionRateData(operatorRateDid, applicationDid);
		} catch (Exception e) {

			throw e;
		}
	}
}

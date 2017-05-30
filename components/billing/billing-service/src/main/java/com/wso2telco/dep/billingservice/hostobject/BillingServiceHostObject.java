package com.wso2telco.dep.billingservice.hostobject;

import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.billingservice.service.RateCardService;

public class BillingServiceHostObject extends ScriptableObject {

	private static final long serialVersionUID = -5856906717551002932L;

	/** The Constant log. */
	private static final Log log = LogFactory.getLog(BillingServiceHostObject.class);

	/** The hostobject name. */
	private String hostobjectName = "BillingServiceHostObject";

	@Override
	public String getClassName() {

		return hostobjectName;
	}

	/**
	 * Instantiates a new billing service host object.
	 */
	public BillingServiceHostObject() {

		log.debug("--------------------------Initialized HostObject Billing Sservice Host Object--------------------");
	}
	//TODO: hit method 1
	public static NativeObject jsFunction_getHubRatesByAPICode(Context cx, Scriptable thisObj, Object[] args,
			Function funObj) throws BusinessException {

		NativeObject resultObject = new NativeObject();
		NativeObject apiData = new NativeObject();

		String apiCode = args[0].toString();

		RateCardService rateCardService = new RateCardService();

		try {

			Map<Integer, String> serviceDetails = rateCardService.getServiceDetailsByAPICode(apiCode);

			NativeArray serviceDataArray = new NativeArray(0);

			apiData.put("apiCode", apiData, apiCode);

			if (!serviceDetails.isEmpty()) {

				int y = 0;

				for (Map.Entry<Integer, String> service : serviceDetails.entrySet()) {

					int servicesDid = service.getKey();
					String serviceCode = service.getValue();

					Map<Integer, String> rateDetails = rateCardService.getHubRateDetailsByServicesDid(servicesDid);

					NativeArray rateDataArray = new NativeArray(0);

					if (!rateDetails.isEmpty()) {

						int z = 0;

						for (Map.Entry<Integer, String> rate : rateDetails.entrySet()) {

							int servicesRateDid = rate.getKey();
							String rateCode = rate.getValue();

							NativeObject rateData = new NativeObject();
							rateData.put("servicesRateDid", rateData, servicesRateDid);
							rateData.put("rateCode", rateData, rateCode);

							rateDataArray.put(z, rateDataArray, rateData);
							z++;
						}
					} else {

						log.error("rate details unavalible for service did : " + servicesDid + " - service code : "
								+ serviceCode);
					}

					NativeObject serviceData = new NativeObject();
					serviceData.put("servicesDid", serviceData, servicesDid);
					serviceData.put("serviceCode", serviceData, serviceCode);
					serviceData.put("rates", serviceData, rateDataArray);

					serviceDataArray.put(y, serviceDataArray, serviceData);
					y++;
				}
			} else {

				log.error("service details unavalible for api code : " + apiCode);
			}

			apiData.put("apiRates", apiData, serviceDataArray);
		} catch (Exception e) {

			log.error("error occurred in getHubRatesByAPICode : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		}

		resultObject.put("apiServiceRates", resultObject, apiData);

		return resultObject;
	}


	//TODO: hit method 2
	public static NativeObject jsFunction_getOperatorRatesByAPICodeAndOperatorCode(Context cx, Scriptable thisObj,
			Object[] args, Function funObj) throws BusinessException {

		NativeObject resultObject = new NativeObject();
		NativeObject apiData = new NativeObject();

		String apiCode = args[0].toString(); // api name - payment or smsmessaging
		String operatorCode = args[1].toString();

		RateCardService rateCardService = new RateCardService();

		try {

			Map<Integer, String> serviceDetails = rateCardService.getServiceDetailsByAPICode(apiCode);

			NativeArray serviceDataArray = new NativeArray(0);

			apiData.put("apiCode", apiData, apiCode);
			apiData.put("operatorCode", apiData, operatorCode);
			if (!serviceDetails.isEmpty()) {

				int y = 0;

				for (Map.Entry<Integer, String> service : serviceDetails.entrySet()) {

					int servicesDid = service.getKey();
					String serviceCode = service.getValue();

					Map<Integer, String> rateDetails = rateCardService
							.getOperatorRateDetailsByServicesDidAndOperatorCode(servicesDid, operatorCode);

					NativeArray rateDataArray = new NativeArray(0);

					if (!rateDetails.isEmpty()) {

						int z = 0;

						for (Map.Entry<Integer, String> rate : rateDetails.entrySet()) {

							int operatorRateDid = rate.getKey();
							String rateCode = rate.getValue();

							NativeObject rateData = new NativeObject();
							rateData.put("operatorRateDid", rateData, operatorRateDid);
							rateData.put("rateCode", rateData, rateCode);

							rateDataArray.put(z, rateDataArray, rateData);
							z++;
						}
					} else {

						log.error("rate details unavalible for service did : " + servicesDid + " - service code : "
								+ serviceCode + " and operator : " + operatorCode);
					}

					NativeObject serviceData = new NativeObject();
					serviceData.put("servicesDid", serviceData, servicesDid);
					serviceData.put("serviceCode", serviceData, serviceCode);
					serviceData.put("rates", serviceData, rateDataArray);

					serviceDataArray.put(y, serviceDataArray, serviceData);
					y++;
				}
			} else {

				log.error("service details unavalible for api code : " + apiCode);
			}

			apiData.put("apiRates", apiData, serviceDataArray);
		} catch (Exception e) {

			log.error("error occurred in getOperatorRatesByAPICodeAndOperatorCode : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		}

		resultObject.put("operatorAPIServiceRates", resultObject, apiData);

		return resultObject;
	}
}

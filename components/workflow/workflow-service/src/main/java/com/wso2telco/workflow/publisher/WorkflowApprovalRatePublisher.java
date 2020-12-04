package com.wso2telco.workflow.publisher;

import java.util.ServiceLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.core.dbutils.exception.BusinessException;
//import com.wso2telco.dep.billingextension.BillingHandlerExtension;

public class WorkflowApprovalRatePublisher {

	private final Log log = LogFactory.getLog(WorkflowApprovalRatePublisher.class);

	public void publishHubAPIRate(int servicesRateDid, int applicationDid,String apiVersion) throws BusinessException {

		/*try {

			ServiceLoader<BillingHandlerExtension> loader = ServiceLoader.load(BillingHandlerExtension.class);

			for (BillingHandlerExtension extn : loader) {

				extn.publishHubAPIRate(servicesRateDid, applicationDid, apiVersion);
			}
		} catch (BusinessException e) {

			log.error("error occurred in publishHubAPIRate : ", e);
			throw e;
		}*/
	}

	//TODO:change in here
	//rateId, appID, operatorId, operationId
	public void publishOperatorAPIRate(int operatorRateDid, int applicationDid, String apiVersion) throws BusinessException {

/*		try {

			ServiceLoader<BillingHandlerExtension> loader = ServiceLoader.load(BillingHandlerExtension.class);

			for (BillingHandlerExtension extn : loader) {

				extn.publishOperatorAPIRate(operatorRateDid, applicationDid, apiVersion);
			}
		} catch (BusinessException e) {

			log.error("error occurred in publishOperatorAPIRate : ", e);
			throw e;
		}*/
	}
}

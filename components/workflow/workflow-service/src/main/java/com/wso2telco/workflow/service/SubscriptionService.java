package com.wso2telco.workflow.service;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.workflow.dao.SubscriptionDAO;
import com.wso2telco.workflow.http.template.AbstractTemplate;
import com.wso2telco.workflow.http.template.HttpRequestTemplate;
import com.wso2telco.workflow.model.SubTierUpdtReq;
import com.wso2telco.workflow.model.SubscriptionEditDTO;
import com.wso2telco.workflow.model.TierUpdtConnDTO;
import com.wso2telco.workflow.service.admin.build.TierRequst;
import com.wso2telco.workflow.utils.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.api.APIConsumer;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.model.Application;
import org.wso2.carbon.apimgt.api.model.Subscriber;
import org.wso2.carbon.apimgt.api.model.SubscriptionResponse;
import org.wso2.carbon.apimgt.impl.APIManagerFactory;
import org.wso2.carbon.utils.multitenancy.MultitenantConstants;
import org.wso2.carbon.apimgt.api.model.ApiTypeWrapper;

import javax.ws.rs.core.Response;
import java.sql.SQLException;

public class SubscriptionService {

	private static Log LOG = LogFactory.getLog(SubscriptionService.class);
	private static final Log AUDIT_LOG = LogFactory.getLog("AUDIT_LOG");

	SubscriptionDAO subscriptionDAO;
	TierRequst tierRequst;

	{
		subscriptionDAO = new SubscriptionDAO();
		tierRequst = new TierRequst();
	}
	
	public Response editSubscriptionTier(SubscriptionEditDTO subscription) throws SQLException, BusinessException, APIManagementException {

		TierUpdtConnDTO tierUpdtConnDTO = tierRequst.constructTierUpdtRequsst(subscription);
		Response response = null;

		try {

			String username = subscription.getServiceProvider();
			SubTierUpdtReq tierUpdtReq = (SubTierUpdtReq) tierUpdtConnDTO.getTierUpdtReq();
			APIConsumer apiConsumer = APIManagerFactory.getInstance().getAPIConsumer(username);
			String UUID = tierUpdtReq.getApiId();

			Subscriber subscriber = new Subscriber(username);
			ApiTypeWrapper apiTypeWrapper = apiConsumer.getAPIorAPIProductByUUID(UUID, Constants.SUPER_TENENT);
			Application application = apiConsumer.getApplicationByUUID(tierUpdtReq.getApplicationId());
			SubscriptionResponse subscriptionResponse = apiConsumer
					.updateSubscription(apiTypeWrapper, username, application.getId(), tierUpdtReq.getSubscriptionId(),
							tierUpdtReq.getThrottlingPolicy(), tierUpdtReq.getRequestedThrottlingPolicy());

			editSubscriptionLog(subscription);
			editSubscriptionAuditLog(subscription);
			response = Response.status(Response.Status.OK).entity(subscription).build();

		}catch (Exception e){
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(subscription).build();
		}
		return response;
	}

	private void editSubscriptionLog(SubscriptionEditDTO subscription) {
		String logMessage = "Subscription tier edited :"
				+ " Completed by - " + subscription.getUser()
				+ ", Application name - " + subscription.getApplicationName()
				+ ", API name - " + subscription.getApiName()
				+ ", Previous tier - "+ subscription.getExistingTier()
				+ ", New tier - " + subscription.getSubscriptionTier();
		LOG.info(logMessage);
	}


	private void editSubscriptionAuditLog(SubscriptionEditDTO subscription) {
		String logMessage = "Subscription Updated." +
				" | Application: " + subscription.getApplicationId() + ":" + subscription.getApplicationName() +
				" | API: " + subscription.getApiID() + ":" + subscription.getApiName() + ":" + subscription.getApiVersion() +
				" | Previous Tier: " + subscription.getExistingTier() +
				" | Updated Tier: " + subscription.getSubscriptionTier() +
				" | User: " + subscription.getUser();
		AUDIT_LOG.info(logMessage);
	}
}
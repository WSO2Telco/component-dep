package com.wso2telco.workflow.service;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.workflow.model.SubTierUpdtReq;
import com.wso2telco.workflow.model.SubscriptionEditDTO;
import com.wso2telco.workflow.model.TierUpdtConnDTO;
import com.wso2telco.workflow.service.admin.build.TierRequst;
import com.wso2telco.workflow.utils.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.wso2.carbon.apimgt.api.APIConsumer;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.model.Application;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.APIManagerFactory;
import org.wso2.carbon.apimgt.impl.utils.APIUtil;
import org.wso2.carbon.apimgt.api.model.ApiTypeWrapper;

import javax.ws.rs.core.Response;
import java.sql.SQLException;

public class SubscriptionService {

	private static final Log LOG = LogFactory.getLog(SubscriptionService.class);
	private final TierRequst<TierUpdtConnDTO> tierRequst;

	{
		tierRequst = new TierRequst<>();
	}
	
	public Response editSubscriptionTier(SubscriptionEditDTO subscription) throws SQLException, BusinessException, APIManagementException {

		TierUpdtConnDTO tierUpdtConnDTO = tierRequst.constructTierUpdtRequsst(subscription);
		Response response;

		try {

			String username = subscription.getServiceProvider();
			SubTierUpdtReq tierUpdtReq = (SubTierUpdtReq) tierUpdtConnDTO.getTierUpdtReq();
			APIConsumer apiConsumer = APIManagerFactory.getInstance().getAPIConsumer(username);
			String uuid = tierUpdtReq.getApiId();

			ApiTypeWrapper apiTypeWrapper = apiConsumer.getAPIorAPIProductByUUID(uuid, Constants.SUPER_TENENT);
			Application application = apiConsumer.getApplicationByUUID(tierUpdtReq.getApplicationId());
			apiConsumer.updateSubscription(
				apiTypeWrapper, username, application.getId(), tierUpdtReq.getSubscriptionId(),
				tierUpdtReq.getThrottlingPolicy(), tierUpdtReq.getRequestedThrottlingPolicy()
			);

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
		JSONObject subTierEditLog = new JSONObject();
		subTierEditLog.put(APIConstants.AuditLogConstants.APPLICATION_NAME, subscription.getApplicationName());
		subTierEditLog.put(APIConstants.AuditLogConstants.APPLICATION_ID, subscription.getApplicationId());
		subTierEditLog.put(APIConstants.AuditLogConstants.API_NAME, subscription.getApiName());
		subTierEditLog.put(APIConstants.USERNAME, subscription.getUser());
		subTierEditLog.put("prev_tier", subscription.getExistingTier());
		subTierEditLog.put("updated_tier", subscription.getExistingTier());

		APIUtil.logAuditMessage(
			APIConstants.AuditLogConstants.SUBSCRIPTION,
			subTierEditLog.toString(),
			APIConstants.AuditLogConstants.UPDATED,
			subscription.getUser()
		);
	}
}
package com.wso2telco.workflow.service;

import java.sql.SQLException;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.workflow.dao.SubscriptionDAO;
import com.wso2telco.workflow.http.template.AbstractTemplate;
import com.wso2telco.workflow.http.template.HttpRequestTemplate;
import com.wso2telco.workflow.model.SubscriptionEditDTO;
import com.wso2telco.workflow.model.TierUpdtConnDTO;
import com.wso2telco.workflow.service.admin.build.TierRequst;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.api.APIManagementException;

import javax.ws.rs.core.Response;

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

		AbstractTemplate httpRequestTemplate = new HttpRequestTemplate();
		Response response = httpRequestTemplate.HTTP_PUT(tierUpdtConnDTO);

		if (response.getStatus() == Response.Status.OK.getStatusCode()) {
			editSubscriptionLog(subscription);
			editSubscriptionAuditLog(subscription);
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

package com.wso2telco.workflow.service;

import java.sql.SQLException;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.workflow.model.AppTierUpdtReq;
import com.wso2telco.workflow.model.ApplicationEditDTO;
import com.wso2telco.workflow.model.TierUpdtConnDTO;
import com.wso2telco.workflow.service.admin.build.TierRequst;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.wso2.carbon.apimgt.api.APIConsumer;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.model.Application;
import org.wso2.carbon.apimgt.api.model.Subscriber;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.APIManagerFactory;
import org.wso2.carbon.apimgt.impl.utils.APIUtil;


import javax.ws.rs.core.Response;

public class ApplicationService {

	private static final Log LOG = LogFactory.getLog(ApplicationService.class);
	private final TierRequst<TierUpdtConnDTO> tierRequst;

	{
		tierRequst = new TierRequst<>();
	}

	public Response editApplicationTier(ApplicationEditDTO application) throws SQLException, BusinessException, APIManagementException {

		Response response;
		TierUpdtConnDTO tierUpdtConnDTO = tierRequst.constructTierUpdtRequsst(application);

		try{

			String username = application.getServiceProvider();

			AppTierUpdtReq tierUpdtReq = (AppTierUpdtReq)tierUpdtConnDTO.getTierUpdtReq();

			APIConsumer apiConsumer = APIManagerFactory.getInstance().getAPIConsumer(username);
			Subscriber subscriber = new Subscriber(username);
			Application updtApplication = new Application(application.getApplicationName(), subscriber);
			updtApplication.setTier(tierUpdtReq.getThrottlingPolicy());
			updtApplication.setUUID(tierUpdtReq.getApplicationId());
			updtApplication.setTokenType(tierUpdtReq.getTokenType());
			apiConsumer.updateApplication(updtApplication);

			editApplicationLog(application);
			editApplicationAuditLog(application);

			response = Response.status(Response.Status.OK).entity(application).build();

		}catch (Exception e){
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(application).build();
		}

		return response;
	}

	private void editApplicationLog(ApplicationEditDTO application) {
		String logMessage = "Application tier edited :"
				+ " Completed by - " + application.getUser()
				+ ", Application name - " + application.getApplicationName()
				+ ", Previous tier - " + application.getExistingTier()
				+ ", New tier - " + application.getApplicationTier();
		LOG.info(logMessage);
	}

	private void editApplicationAuditLog(ApplicationEditDTO application) {
		JSONObject appTierEditLog = new JSONObject();
		appTierEditLog.put(APIConstants.AuditLogConstants.APPLICATION_NAME, application.getApplicationName());
		appTierEditLog.put(APIConstants.AuditLogConstants.APPLICATION_ID, application.getApplicationId());
		appTierEditLog.put(APIConstants.USERNAME, application.getUser());
		appTierEditLog.put("prev_tier", application.getExistingTier());
		appTierEditLog.put("updated_tier", application.getExistingTier());
		appTierEditLog.put("sp", application.getServiceProvider());

		APIUtil.logAuditMessage(
			APIConstants.AuditLogConstants.APPLICATION,
			appTierEditLog.toString(),
			APIConstants.AuditLogConstants.UPDATED,
			application.getUser()
		);
	}
}
package org.workflow.core.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.user.api.UserStoreException;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.GenaralError;
import com.wso2telco.core.dbutils.exception.ThrowableError;

public class WorkFlowHealper {
	private static final String DEPLOYMENT_TYPE_SYSTEM_PARAM = "DEPLOYMENT_TYPE";
	Log log = LogFactory.getLog(WorkFlowHealper.class);

	private static WorkFlowHealper instance;
	private String wrorkflowServiceEndPoint;

	public static WorkFlowHealper getInstance() {
		if (instance == null) {
			instance = new WorkFlowHealper();

		}
		return instance;
	}

	private WorkFlowHealper() {
	}

	public static String getDeploymentType() {

		return System.getProperty(DEPLOYMENT_TYPE_SYSTEM_PARAM, "hub");
	}

	public String getWorkflowServiceEndPoint() {
		return wrorkflowServiceEndPoint;
	}

	public void setAppCreationServiceEndPoint(String appCreationServiceEndPoint) {
		this.wrorkflowServiceEndPoint = appCreationServiceEndPoint;
	}

	public String getAdmin() throws BusinessException {
		String adminUser = null;
		try {
			adminUser = CarbonContext.getThreadLocalCarbonContext().getUserRealm().getRealmConfiguration()
					.getAdminUserName();
		} catch (UserStoreException e) {
			log.warn("Unable to get the admin user from carbon platform");
		}
		if (adminUser == null) {
			log.debug("lookup for admin user from enviroment variable user.admin");
			adminUser = System.getProperty("user.admin");
			log.debug(" adminUser :" + adminUser);
		}
		if (adminUser == null) {
			throw new BusinessException(new ThrowableError() {

				@Override
				public String getMessage() {
					return "Unable to find the admin user";
				}

				@Override
				public String getCode() {
					return "WF001";
				}
			});
		} else {
			return adminUser;
		}
	}

}

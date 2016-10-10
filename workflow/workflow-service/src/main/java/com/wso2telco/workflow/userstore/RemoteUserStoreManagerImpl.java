package com.wso2telco.workflow.userstore;


import com.wso2telco.workflow.utils.Constants;
import com.wso2telco.workflow.utils.WorkflowProperties;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.um.ws.service.RemoteUserStoreManagerServiceStub;
import org.wso2.carbon.um.ws.service.RemoteUserStoreManagerServiceStub.GetUserClaimValue;
import org.wso2.carbon.um.ws.service.RemoteUserStoreManagerServiceStub.GetUserClaimValueResponse;
import org.wso2.carbon.um.ws.service.RemoteUserStoreManagerServiceStub.GetUserListOfRole;
import org.wso2.carbon.um.ws.service.RemoteUserStoreManagerServiceStub.GetUserListOfRoleResponse;
import org.wso2.carbon.user.api.UserStoreException;

import java.util.Properties;

public class RemoteUserStoreManagerImpl implements RemoteUserStoreManager {
	
	private static Log log = LogFactory.getLog(RemoteUserStoreManagerImpl.class);

	@Override
	public String[] getUserListOfRole(String role) {
		
		String[] userList = null;
		
		log.info("Getting users for the role : " + role);
		GetUserListOfRole roleObj = new GetUserListOfRole();
		roleObj.setRoleName(role);
		
		try {
			
			RemoteUserStoreManagerServiceStub stub = getRemoteUserStoreManagerServiceStub();
			GetUserListOfRoleResponse response = stub.getUserListOfRole(roleObj);
			userList = response.get_return();
			log.info("User list : " + userList.toString());
			
		} catch (Exception e) {
			log.error("Error while getting user list of the the role : " + role);
			log.error(e.getMessage());
		}
		return userList;
	}

	@Override
	public String getUserClaimValue(String userName, String claim) {
		
		String claimValue = "";
		
		log.info("Getting claim value for the userName : " + userName + " and claim : " + claim);
		GetUserClaimValue requestObj = new GetUserClaimValue();
		requestObj.setUserName(userName);
		requestObj.setClaim(claim);
		
		try {
			
			RemoteUserStoreManagerServiceStub stub = getRemoteUserStoreManagerServiceStub();
			GetUserClaimValueResponse response = stub.getUserClaimValue(requestObj);
			
			claimValue = response.get_return();
			log.info("Claim value : " + claimValue);
			
		} catch (Exception e) {
			log.error("Error while getting claim value for the userName : " + userName + " and claim : " + claim);
			log.error(e.getMessage());
		}
		
		return claimValue;
	}
	
	private RemoteUserStoreManagerServiceStub getRemoteUserStoreManagerServiceStub() throws org.apache.axis2.AxisFault, UserStoreException {
		
		Properties workflowProperties = WorkflowProperties.loadWorkflowProperties();
        String adminPassword=CarbonContext.getThreadLocalCarbonContext().getUserRealm().getRealmConfiguration().getAdminPassword();

		String host = workflowProperties.getProperty(Constants.REMOTE_USERSTORE_MANAGER_HOST);
		String username = workflowProperties.getProperty(Constants.REMOTE_USERSTORE_MANAGER_USERNAME);
		String password = workflowProperties.getProperty(adminPassword);
		
		RemoteUserStoreManagerServiceStub stub = new RemoteUserStoreManagerServiceStub(host + "services/RemoteUserStoreManagerService");
		ServiceClient client = stub._getServiceClient();
		Options options = client.getOptions();
        HttpTransportProperties.Authenticator authenticator = new HttpTransportProperties.Authenticator();
        authenticator.setUsername(username);
        authenticator.setPassword(password);
        authenticator.setPreemptiveAuthentication(true);
        options.setProperty(HTTPConstants.AUTHENTICATE, authenticator);
        client.setOptions(options);
        
        return stub;
	}
}

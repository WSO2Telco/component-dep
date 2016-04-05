package carbon.wso2.org.axiata.workflow.impl;

import java.util.Properties;

import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.wso2.carbon.um.ws.service.RemoteUserStoreManagerServiceStub;
import org.wso2.carbon.um.ws.service.RemoteUserStoreManagerServiceStub.GetUserListOfRole;
import org.wso2.carbon.um.ws.service.RemoteUserStoreManagerServiceStub.GetUserListOfRoleResponse;
import org.wso2.carbon.um.ws.service.RemoteUserStoreManagerServiceStub.GetUserClaimValueResponse;
import org.wso2.carbon.um.ws.service.RemoteUserStoreManagerServiceStub.GetUserClaimValue;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;

import carbon.wso2.org.axiata.workflow.in.RemoteUserStoreManager;
import carbon.wso2.org.axiata.workflow.util.AxiataConstants;
import carbon.wso2.org.axiata.workflow.util.CommonUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
	
	private RemoteUserStoreManagerServiceStub getRemoteUserStoreManagerServiceStub() throws org.apache.axis2.AxisFault {
		
		Properties workflowProperties = CommonUtil.loadAxiataWorkflowProperties();
		String host = workflowProperties.getProperty(AxiataConstants.REMOTE_USERSTORE_MANAGER_HOST);
		String username = workflowProperties.getProperty(AxiataConstants.REMOTE_USERSTORE_MANAGER_USERNAME);
		String password = workflowProperties.getProperty(AxiataConstants.REMOTE_USERSTORE_MANAGER_PASSWORD);
		
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

package com.wso2telco.dep.operatorservice.service.adminService;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.wso2.carbon.apimgt.hostobjects.internal.HostObjectComponent;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.APIManagerConfiguration;
import org.wso2.carbon.authenticator.stub.AuthenticationAdminStub;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.carbon.authenticator.stub.LogoutAuthenticationExceptionException;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

public class AdminServiceClient {
    /**
     * The admin service client is used to generate a session cookie in order to invoke an Admin Service.
     * See <a href="https://docs.wso2.com/display/AM160/WSO2+Admin+Services">Invoking an admin service</a>
     */

    private AuthenticationAdminStub authenticationAdminStub;
    private String servicepath = "services/AuthenticationAdmin";
    private String serviceEndpoint;
    private String adminUsername;
    private String adminPassword;

    public AdminServiceClient() throws AxisFault {
        loadCredentials();
        authenticationAdminStub = new AuthenticationAdminStub(serviceEndpoint);
    }

    public String getSessionCookie() throws RemoteException, LoginAuthenticationExceptionException, MalformedURLException {
        String sessionCookie = null;

        if (authenticationAdminStub.login(adminUsername, adminPassword, new URL(serviceEndpoint).getHost())) {
            ServiceContext serviceContext = authenticationAdminStub.
                    _getServiceClient().getLastOperationContext().getServiceContext();
            sessionCookie = (String) serviceContext.getProperty(HTTPConstants.COOKIE_STRING);
        }
        return sessionCookie;
    }

    public void logOut() throws RemoteException, LogoutAuthenticationExceptionException {
        authenticationAdminStub.logout();
    }

    private void loadCredentials() {
        APIManagerConfiguration config = HostObjectComponent.getAPIManagerConfiguration();
        serviceEndpoint = config.getFirstProperty(APIConstants.AUTH_MANAGER_URL) + servicepath;
        adminUsername = config.getFirstProperty(APIConstants.AUTH_MANAGER_USERNAME);
        adminPassword = config.getFirstProperty(APIConstants.AUTH_MANAGER_PASSWORD);
    }
}

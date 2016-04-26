/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dialog.mifesandbox.user;

import java.rmi.RemoteException;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.wso2.carbon.authenticator.stub.AuthenticationAdminStub;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.carbon.authenticator.stub.LogoutAuthenticationExceptionException;

/**
 *
 * @author User
 */
public class Login {

    public static boolean wso2Login(String username, String password) throws RemoteException, LoginAuthenticationExceptionException,
            LogoutAuthenticationExceptionException {

        boolean success = false;

      /*  System.setProperty("javax.net.ssl.trustStore", "D:/wso2/kottamalli/wso2am-1.6.0/repository/resources/security/wso2carbon.jks");                                                        
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        String backEndUrl = "https://localhost:9443";

        LoginAdminServiceClient login = new LoginAdminServiceClient(backEndUrl);
        String session = login.authenticate(username, password);
//        ServiceAdminClient serviceAdminClient = new ServiceAdminClient(backEndUrl, session);
//        ServiceMetaDataWrapper serviceList = serviceAdminClient.listServices();
//        System.out.println("Service Names:");
//        for (ServiceMetaData serviceData : serviceList.getServices()) {
//            System.out.println(serviceData.getName());
//        }
        if (login.isLoginSuccess()) {
            success = true;
            System.out.println("Login success assigned!");
        }
        login.logOut();
     */
        return true;
    }
}
//
class LoginAdminServiceClient {

    private final String serviceName = "AuthenticationAdmin";
    private AuthenticationAdminStub authenticationAdminStub;
    private String endPoint;
    private boolean loginSuccess = false;

    public LoginAdminServiceClient(String backEndUrl) throws AxisFault {
        this.endPoint = backEndUrl + "/services/" + serviceName;
        authenticationAdminStub = new AuthenticationAdminStub(endPoint);
    }

    public String authenticate(String userName, String password) throws RemoteException,
            LoginAuthenticationExceptionException {

        String sessionCookie = null;

        if (authenticationAdminStub.login(userName, password, "localhost")) {
            System.out.println("Login Successful");
            setLoginSuccess(true);

            ServiceContext serviceContext = authenticationAdminStub.
                    _getServiceClient().getLastOperationContext().getServiceContext();
            sessionCookie = (String) serviceContext.getProperty(HTTPConstants.COOKIE_STRING);
            System.out.println(sessionCookie);
        }

        return sessionCookie;
    }

    public void logOut() throws RemoteException, LogoutAuthenticationExceptionException {
        authenticationAdminStub.logout();
    }

    /**
     * @return the loginSuccess
     */
    public boolean isLoginSuccess() {
        return loginSuccess;
    }

    /**
     * @param loginSuccess the loginSuccess to set
     */
    public void setLoginSuccess(boolean loginSuccess) {
        this.loginSuccess = loginSuccess;
    }
}

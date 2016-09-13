
/**
 * RemoteUserStoreManagerServiceUserStoreExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.1-wso2v10  Built on : Sep 04, 2013 (02:02:54 UTC)
 */

package org.wso2.carbon.um.ws.service;

public class RemoteUserStoreManagerServiceUserStoreExceptionException extends java.lang.Exception{

    private static final long serialVersionUID = 1429765666105L;
    
    private org.wso2.carbon.um.ws.service.RemoteUserStoreManagerServiceStub.RemoteUserStoreManagerServiceUserStoreException faultMessage;

    
        public RemoteUserStoreManagerServiceUserStoreExceptionException() {
            super("RemoteUserStoreManagerServiceUserStoreExceptionException");
        }

        public RemoteUserStoreManagerServiceUserStoreExceptionException(java.lang.String s) {
           super(s);
        }

        public RemoteUserStoreManagerServiceUserStoreExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public RemoteUserStoreManagerServiceUserStoreExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(org.wso2.carbon.um.ws.service.RemoteUserStoreManagerServiceStub.RemoteUserStoreManagerServiceUserStoreException msg){
       faultMessage = msg;
    }
    
    public org.wso2.carbon.um.ws.service.RemoteUserStoreManagerServiceStub.RemoteUserStoreManagerServiceUserStoreException getFaultMessage(){
       return faultMessage;
    }
}
    

    package com.wso2telco.workflow.notify;

import com.wso2telco.workflow.impl.EmailNotificationImpl;
import com.wso2telco.workflow.in.EmailNotification;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.WebParam;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

    @WebService
    @SOAPBinding(style = SOAPBinding.Style.RPC)
    public class WorkflowNotificationServiceSkeleton{
        
         
        /**
         * Auto generated method signature
         * 
         * @param subApprovalStatusSPEmailNotificationRequest
         * @return
         */
        @WebMethod(operationName="sendSubApprovalStatusSPEmailNotification")
                 public void sendSubApprovalStatusSPEmailNotification
                  (
                          @WebParam com.wso2telco.workflow.notify.SubApprovalStatusSPEmailNotificationRequest subApprovalStatusSPEmailNotificationRequest
                  )
            {
                
                	 EmailNotification emailNotification = new EmailNotificationImpl();
                	 emailNotification.sendSubApprovalStatusSPEmailNotification(subApprovalStatusSPEmailNotificationRequest.getSubApprovalStatusSPEmailNotificationRequest());
                
        }
     
         
        /**
         * Auto generated method signature
         * 
         * @param pLUGINAdminAppApprovalEmailNotificationRequest
         * @return
         */
        @WebMethod(operationName="sendPLUGINAdminAppApprovalEmailNotification")
                 public void sendPLUGINAdminAppApprovalEmailNotification
                  (
                          @WebParam  com.wso2telco.workflow.notify.PLUGINAdminAppApprovalEmailNotificationRequest pLUGINAdminAppApprovalEmailNotificationRequest
                  )
            {
                
                	 EmailNotification emailNotification = new EmailNotificationImpl();
                	 emailNotification.sendPLUGINAdminAppApprovalEmailNotification(pLUGINAdminAppApprovalEmailNotificationRequest.getPLUGINAdminAppApprovalEmailNotificationRequest());
                
        }
     
         
        /**
         * Auto generated method signature
         * 
         * @param pLUGINAdminSubApprovalEmailNotificationRequest
         * @return
         */
        @WebMethod(operationName="sendPLUGINAdminSubApprovalEmailNotification")
                 public void sendPLUGINAdminSubApprovalEmailNotification
                  (
                          @WebParam com.wso2telco.workflow.notify.PLUGINAdminSubApprovalEmailNotificationRequest pLUGINAdminSubApprovalEmailNotificationRequest
                  )
            {
                
                	 EmailNotification emailNotification = new EmailNotificationImpl();
                	 emailNotification.sendPLUGINAdminSubApprovalEmailNotification(pLUGINAdminSubApprovalEmailNotificationRequest.getPLUGINAdminSubApprovalEmailNotificationRequest());
                
        }
     
         
        /**
         * Auto generated method signature
         * 
         * @param hUBAdminAppApprovalEmailNotificationRequest
         * @return
         */
        @WebMethod(operationName="sendHUBAdminAppApprovalEmailNotification")
                 public void sendHUBAdminAppApprovalEmailNotification
                  (
                          @WebParam com.wso2telco.workflow.notify.HUBAdminAppApprovalEmailNotificationRequest hUBAdminAppApprovalEmailNotificationRequest
                  )
            {
                
                	 EmailNotification emailNotification = new EmailNotificationImpl();
                	 emailNotification.sendHUBAdminAppApprovalEmailNotification(hUBAdminAppApprovalEmailNotificationRequest.getHUBAdminAppApprovalEmailNotificationRequest());
                
        }
     
         
        /**
         * Auto generated method signature
         * 
         * @param appApprovalStatusSPEmailNotificationRequest
         * @return
         */
        @WebMethod(operationName="sendAppApprovalStatusSPEmailNotification")
                 public void sendAppApprovalStatusSPEmailNotification
                  (
                          @WebParam com.wso2telco.workflow.notify.AppApprovalStatusSPEmailNotificationRequest appApprovalStatusSPEmailNotificationRequest
                  )
            {
                
                	 EmailNotification emailNotification = new EmailNotificationImpl();
                	 emailNotification.sendAppApprovalStatusSPEmailNotification(appApprovalStatusSPEmailNotificationRequest.getAppApprovalStatusSPEmailNotificationRequest());
                
        }
     
         
        /**
         * Auto generated method signature
         * 
         * @param hUBAdminSubApprovalEmailNotificationRequest
         * @return
         */
        @WebMethod(operationName="sendHUBAdminSubApprovalEmailNotification")
                 public void sendHUBAdminSubApprovalEmailNotification
                  (
                          @WebParam com.wso2telco.workflow.notify.HUBAdminSubApprovalEmailNotificationRequest hUBAdminSubApprovalEmailNotificationRequest
                  )
            {
                
                	 EmailNotification emailNotification = new EmailNotificationImpl();
                	 emailNotification.sendHUBAdminSubrovalEmailNotification(hUBAdminSubApprovalEmailNotificationRequest.getHUBAdminSubApprovalEmailNotificationRequest());
                
        }
     
    }
    
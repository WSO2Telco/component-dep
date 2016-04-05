
/**
 * AxiataWorkflowNotificationServiceSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.1-wso2v10  Built on : Sep 04, 2013 (02:02:54 UTC)
 */
    package carbon.wso2.org.axiata.workflow.notify;

import carbon.wso2.org.axiata.workflow.impl.EmailNotificationImpl;
import carbon.wso2.org.axiata.workflow.in.EmailNotification;
    /**
     *  AxiataWorkflowNotificationServiceSkeleton java skeleton for the axisService
     */
    public class AxiataWorkflowNotificationServiceSkeleton{
        
         
        /**
         * Auto generated method signature
         * 
                                     * @param subApprovalStatusSPEmailNotificationRequest 
             * @return  
         */
        
                 public void sendSubApprovalStatusSPEmailNotification
                  (
                  carbon.wso2.org.axiata.workflow.notify.SubApprovalStatusSPEmailNotificationRequest subApprovalStatusSPEmailNotificationRequest
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
        
                 public void sendPLUGINAdminAppApprovalEmailNotification
                  (
                  carbon.wso2.org.axiata.workflow.notify.PLUGINAdminAppApprovalEmailNotificationRequest pLUGINAdminAppApprovalEmailNotificationRequest
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
        
                 public void sendPLUGINAdminSubApprovalEmailNotification
                  (
                  carbon.wso2.org.axiata.workflow.notify.PLUGINAdminSubApprovalEmailNotificationRequest pLUGINAdminSubApprovalEmailNotificationRequest
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
        
                 public void sendHUBAdminAppApprovalEmailNotification
                  (
                  carbon.wso2.org.axiata.workflow.notify.HUBAdminAppApprovalEmailNotificationRequest hUBAdminAppApprovalEmailNotificationRequest
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
        
                 public void sendAppApprovalStatusSPEmailNotification
                  (
                  carbon.wso2.org.axiata.workflow.notify.AppApprovalStatusSPEmailNotificationRequest appApprovalStatusSPEmailNotificationRequest
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
        
                 public void sendHUBAdminSubApprovalEmailNotification
                  (
                  carbon.wso2.org.axiata.workflow.notify.HUBAdminSubApprovalEmailNotificationRequest hUBAdminSubApprovalEmailNotificationRequest
                  )
            {
                
                	 EmailNotification emailNotification = new EmailNotificationImpl();
                	 emailNotification.sendHUBAdminSubrovalEmailNotification(hUBAdminSubApprovalEmailNotificationRequest.getHUBAdminSubApprovalEmailNotificationRequest());
                
        }
     
    }
    
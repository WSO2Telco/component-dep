
/**
 * AxiataWorkflowApprovalServiceSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.1-wso2v10  Built on : Sep 04, 2013 (02:02:54 UTC)
 */
    package carbon.wso2.org.axiata.workflow;

import carbon.wso2.org.axiata.workflow.impl.*;
import carbon.wso2.org.axiata.workflow.in.*;
    /**
     *  AxiataWorkflowApprovalServiceSkeleton java skeleton for the axisService
     */
    public class AxiataWorkflowApprovalServiceSkeleton{
        
         
        /**
         * Auto generated method signature
         * 
                                     * @param appApprovalAuditRequest 
             * @return  
         */
        
                 public void insertAppApprovalAuditRecord
                  (
                  carbon.wso2.org.axiata.workflow.audit.AppApprovalAuditRequest appApprovalAuditRequest
                  )
            {
                
                	 WorkflowAudit workflowAudit = new WorkflowAuditImpl();
                	 workflowAudit.insertAppApprovalAuditRecord(appApprovalAuditRequest.getAppApprovalAuditRequest());
                
        }
     
         
        /**
         * Auto generated method signature
         * 
                                     * @param appHUBApprovalDBUpdateRequest 
             * @return  
         */
        
                 public void updateDBAppHubApproval
                  (
                  carbon.wso2.org.axiata.workflow.application.AppHUBApprovalDBUpdateRequest appHUBApprovalDBUpdateRequest
                  )
            {
                
                	 ApplicationApproval appApproval = new ApplicationApprovalImpl();
                	 appApproval.updateDBAppHubApproval(appHUBApprovalDBUpdateRequest.getAppHUBApprovalDBUpdateRequest());
                
        }
     
         
        /**
         * Auto generated method signature
         * 
                                     * @param subOpApprovalDBUpdateRequest 
             * @return  
         */
        
                 public void updateDBSubOpApproval
                  (
                  carbon.wso2.org.axiata.workflow.subscription.SubOpApprovalDBUpdateRequest subOpApprovalDBUpdateRequest
                  )
            {
                
                	 SubscriptionApproval subApproval = new SubscriptionApprovalImpl();
                	 subApproval.updateDBSubOpApproval(subOpApprovalDBUpdateRequest.getSubOpApprovalDBUpdateRequest());
                
        }
     
         
        /**
         * Auto generated method signature
         * 
                                     * @param subOperatorRetrievalRequest 
             * @return subOperatorRetrievalResponse 
         */
        
                 public carbon.wso2.org.axiata.workflow.subscription.SubOperatorRetrievalResponse retrieveSubOperatorList
                  (
                  carbon.wso2.org.axiata.workflow.subscription.SubOperatorRetrievalRequest subOperatorRetrievalRequest
                  )
            {
                
                	 SubscriptionApproval subApproval = new SubscriptionApprovalImpl();
                	 return subApproval.retrieveSubOperatorList(subOperatorRetrievalRequest.getSubOperatorRetrievalRequest());
        }
     
         
        /**
         * Auto generated method signature
         * 
                                     * @param hUBApprovalSubValidatorRequest 
             * @return  
         */
        
                 public void insertValidatorForSubscription
                  (
                  carbon.wso2.org.axiata.workflow.subscription.HUBApprovalSubValidatorRequest hUBApprovalSubValidatorRequest
                  )
            {
                
                	 SubscriptionApproval subApproval = new SubscriptionApprovalImpl();
                	 subApproval.insertValidatorForSubscription(hUBApprovalSubValidatorRequest.getHUBApprovalSubValidatorRequest());
                
        }
     
         
        /**
         * Auto generated method signature
         * 
                                     * @param appOpApprovalDBUpdateRequest 
             * @return  
         */
        
                 public void updateDBAppOpApproval
                  (
                  carbon.wso2.org.axiata.workflow.application.AppOpApprovalDBUpdateRequest appOpApprovalDBUpdateRequest
                  )
            {
                
                	 ApplicationApproval appApproval = new ApplicationApprovalImpl();
                	 appApproval.updateDBAppOpApproval(appOpApprovalDBUpdateRequest.getAppOpApprovalDBUpdateRequest());
                
        }
     
         
        /**
         * Auto generated method signature
         * 
                                     * @param subHUBApprovalDBUpdateRequest 
             * @return  
         */
        
                 public void updateDBSubHubApproval
                  (
                  carbon.wso2.org.axiata.workflow.subscription.SubHUBApprovalDBUpdateRequest subHUBApprovalDBUpdateRequest
                  )
            {
                
                	 SubscriptionApproval subApproval = new SubscriptionApprovalImpl();
                	 subApproval.updateDBSubHubApproval(subHUBApprovalDBUpdateRequest.getSubHUBApprovalDBUpdateRequest());
                
        }
     
         
        /**
         * Auto generated method signature
         * 
                                     * @param subApprovalAuditRequest 
             * @return  
         */
        
                 public void insertSubApprovalAuditRecord
                  (
                  carbon.wso2.org.axiata.workflow.audit.SubApprovalAuditRequest subApprovalAuditRequest
                  )
            {
                
                	 WorkflowAudit workflowAudit = new WorkflowAuditImpl();
                	 workflowAudit.insertSubApprovalAuditRecord(subApprovalAuditRequest.getSubApprovalAuditRequest());
                
        }
     
         
        /**
         * Auto generated method signature
         * 
                                     * @param operatorRetrievalRequest 
             * @return operatorRetrievalResponse 
         */
        
                 public carbon.wso2.org.axiata.workflow.common.OperatorRetrievalResponse retrieveOperatorList
                  (
                  carbon.wso2.org.axiata.workflow.common.OperatorRetrievalRequest operatorRetrievalRequest
                  )
            {
                //TODO : fill this with the necessary business logic
                throw new  java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName() + "#retrieveOperatorList");
        }
     
    }
    
package com.wso2telco.workflow;

import com.wso2telco.workflow.application.AppHUBApprovalDBUpdateRequestType;
import com.wso2telco.workflow.application.AppOpApprovalDBUpdateRequestType;
import com.wso2telco.workflow.audit.AppApprovalAuditRequestType;
import com.wso2telco.workflow.audit.SubApprovalAuditRequestType;
import com.wso2telco.workflow.common.OperatorRetrievalRequestType;
import com.wso2telco.workflow.common.OperatorRetrievalResponseType;
import com.wso2telco.workflow.impl.*;
import com.wso2telco.workflow.in.*;
import com.wso2telco.workflow.subscription.HUBApprovalSubValidatorRequestType;
import com.wso2telco.workflow.subscription.SubHUBApprovalDBUpdateRequestType;
import com.wso2telco.workflow.subscription.SubOpApprovalDBUpdateRequestType;
import com.wso2telco.workflow.subscription.SubOperatorRetrievalRequestType;

import javax.jws.*;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.ws.Action;
import java.lang.System;


@WebService(name = "WorkflowApprovalServiceSkeleton", targetNamespace = "http://org.wso2.carbon/axiata/workflow/application", endpointInterface = "com.wso2telco.workflow.WorkflowApprovalServiceSkeleton")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.BARE)
public class WorkflowApprovalServiceSkeleton {


    /**
     * @param appApprovalAuditRequest
     * @return
     */
    @WebMethod(operationName = "insertAppApprovalAuditRecord", action = "aud:insertAppApprovalAuditRecord")
    @Action(input = "urn:insertAppApprovalAuditRecord", output = "aud:insertAppApprovalAuditRecord")
    @Oneway()
    public void insertAppApprovalAuditRecord(@WebParam(name = "AppApprovalAuditRequest", targetNamespace = "http://org.wso2.carbon/axiata/workflow/audit") AppApprovalAuditRequestType appApprovalAuditRequest) {
        WorkflowAudit workflowAudit = new WorkflowAuditImpl();
        System.out.println("AppApprovalAuditRequest"+appApprovalAuditRequest.getAppApprovalType()+""+appApprovalAuditRequest.getAppCreator());
        workflowAudit.insertAppApprovalAuditRecord(appApprovalAuditRequest);

    }


    /**
     * @param appHUBApprovalDBUpdateRequestType
     * @return
     */

    @WebMethod(operationName = "updateDBAppHubApproval", action = "urn:updateDBAppHubApproval")
    @Action(input = "urn:updateDBAppHubApproval", output = "urn:updateDBAppHubApproval")
    @Oneway()
    public void updateDBAppHubApproval
    (

            @WebParam(name = "AppHUBApprovalDBUpdateRequest", targetNamespace = "http://org.wso2.carbon/axiata/workflow/audit") AppHUBApprovalDBUpdateRequestType appHUBApprovalDBUpdateRequestType
    ) {
        System.out.println("AppHUBApprovalDBUpdateRequest"+appHUBApprovalDBUpdateRequestType.getAppID());
        ApplicationApproval appApproval = new ApplicationApprovalImpl();
        appApproval.updateDBAppHubApproval(appHUBApprovalDBUpdateRequestType);

    }


    /**
     * @param subOpApprovalDBUpdateRequest
     * @return
     */

    @WebMethod(operationName = "updateDBSubOpApproval", action = "urn:updateDBSubOpApproval")
    @Action(input = "urn:updateDBSubOpApproval", output = "urn:updateDBSubOpApproval")
    @Oneway()
    public void updateDBSubOpApproval
    (
            @WebParam(name = "SubOpApprovalDBUpdateRequest", targetNamespace = "http://org.wso2.carbon/axiata/workflow/audit") SubOpApprovalDBUpdateRequestType subOpApprovalDBUpdateRequest
    ) {
        System.out.println("SubOpApprovalDBUpdateRequest"+subOpApprovalDBUpdateRequest.getAppID());
        SubscriptionApproval subApproval = new SubscriptionApprovalImpl();
        subApproval.updateDBSubOpApproval(subOpApprovalDBUpdateRequest);

    }


    /**
     * @param subOperatorRetrievalRequest
     * @return subOperatorRetrievalResponse
     */

    @WebMethod(operationName = "retrieveSubOperatorList", action = "urn:retrieveSubOperatorList")
    @Action(input = "urn:retrieveSubOperatorList", output = "urn:retrieveSubOperatorList")
    public
    @WebResult(name = "SubOperatorRetrievalResponse")
    com.wso2telco.workflow.subscription.SubOperatorRetrievalResponse retrieveSubOperatorList
    (
            @WebParam(name = "SubOperatorRetrievalRequest", targetNamespace = "http://org.wso2.carbon/axiata/workflow/audit")  SubOperatorRetrievalRequestType subOperatorRetrievalRequest
    ) {
        System.out.println("retrieveSubOperatorList"+subOperatorRetrievalRequest.getAppId());
        SubscriptionApproval subApproval = new SubscriptionApprovalImpl();
        return subApproval.retrieveSubOperatorList(subOperatorRetrievalRequest);
    }


    /**
     * @param hUBApprovalSubValidatorRequest
     * @return
     */
    @WebMethod(operationName = "insertValidatorForSubscription", action = "urn:insertValidatorForSubscription")
    @Action(input = "urn:insertValidatorForSubscription", output = "urn:insertValidatorForSubscription")
    @Oneway()
    public void insertValidatorForSubscription
    (
            @WebParam(name = "HUBApprovalSubValidatorRequest",targetNamespace = "http://org.wso2.carbon/axiata/workflow/audit") HUBApprovalSubValidatorRequestType hUBApprovalSubValidatorRequest
    ) {
        System.out.println("insertValidatorForSubscription"+hUBApprovalSubValidatorRequest.getAppID());
        SubscriptionApproval subApproval = new SubscriptionApprovalImpl();
        subApproval.insertValidatorForSubscription(hUBApprovalSubValidatorRequest);

    }


    /**
     * @param appOpApprovalDBUpdateRequest
     * @return
     */
    @WebMethod(operationName = "updateDBAppOpApproval", action = "urn:updateDBAppOpApproval")
    @Action(input = "urn:updateDBAppOpApproval", output = "urn:updateDBAppOpApproval")
    @Oneway()
    public void updateDBAppOpApproval
    (
            @WebParam(name = "AppOpApprovalDBUpdateRequest",targetNamespace = "http://org.wso2.carbon/axiata/workflow/audit") AppOpApprovalDBUpdateRequestType appOpApprovalDBUpdateRequest
    ) {
        System.out.println("updateDBAppOpApproval"+appOpApprovalDBUpdateRequest.getAppID());
        ApplicationApproval appApproval = new ApplicationApprovalImpl();
        appApproval.updateDBAppOpApproval(appOpApprovalDBUpdateRequest);

    }


    /**
     * @param subHUBApprovalDBUpdateRequest
     * @return
     */
    @WebMethod(operationName = "updateDBSubHubApproval", action = "urn:updateDBSubHubApproval")
    @Action(input = "urn:updateDBSubHubApproval", output = "urn:updateDBSubHubApproval")
    @Oneway()
    public void updateDBSubHubApproval
    (
            @WebParam(name = "SubHUBApprovalDBUpdateRequest",targetNamespace = "http://org.wso2.carbon/axiata/workflow/audit") SubHUBApprovalDBUpdateRequestType subHUBApprovalDBUpdateRequest
    ) {
        System.out.println("updateDBSubHubApproval"+subHUBApprovalDBUpdateRequest.getAppID());
        SubscriptionApproval subApproval = new SubscriptionApprovalImpl();
        subApproval.updateDBSubHubApproval(subHUBApprovalDBUpdateRequest);

    }


    /**
     * @param subApprovalAuditRequest
     * @return
     */
    @WebMethod(operationName = "insertSubApprovalAuditRecord", action = "urn:insertSubApprovalAuditRecord")
    @Action(input = "urn:insertSubApprovalAuditRecord", output = "urn:insertSubApprovalAuditRecord")
    @Oneway()
    public void insertSubApprovalAuditRecord
    (
            @WebParam(name = "SubApprovalAuditRequest",targetNamespace = "http://org.wso2.carbon/axiata/workflow/audit") SubApprovalAuditRequestType subApprovalAuditRequest
    ) {
        System.out.println("insertSubApprovalAuditRecord"+subApprovalAuditRequest.getAppId());
        WorkflowAudit workflowAudit = new WorkflowAuditImpl();
        workflowAudit.insertSubApprovalAuditRecord(subApprovalAuditRequest);

    }


    /**
     * @param operatorRetrievalRequest
     * @return operatorRetrievalResponse
     */
    @WebMethod(operationName = "retrieveOperatorList", action = "urn:retrieveOperatorList")
    @Action(input = "urn:retrieveOperatorList", output = "urn:retrieveOperatorList")
    public OperatorRetrievalResponseType retrieveOperatorList
    (
            @WebParam OperatorRetrievalRequestType operatorRetrievalRequest
    ) {
        //TODO : fill this with the necessary business logic
        throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName() + "#retrieveOperatorList");
    }

}
    
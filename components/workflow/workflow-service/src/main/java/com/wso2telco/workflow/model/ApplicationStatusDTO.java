package com.wso2telco.workflow.model;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for application approval status
 */
public class ApplicationStatusDTO

{

    private String name = null;
    private String status = null;
    private ArrayList<OperatorApproval> operatorApprovals = null;
    private ArrayList<APISubscriptionStatusDTO> subscriptions = null;


    public void addSubscription(APISubscriptionStatusDTO subscription) {
        if (subscriptions == null) {
            subscriptions = new ArrayList();
        }
        subscriptions.add(subscription);
    }

    public List<APISubscriptionStatusDTO> getSubscriptionList() {
        return subscriptions;
    }

    /**
     * @param name   Operator's name
     * @param tempStatus Operator approval status
     */
    public void addOperator(String name, String tempStatus) {
        String operatorStatus = "";
        if (operatorApprovals == null) {
            operatorApprovals = new ArrayList<>();
        }
        switch (tempStatus) {
            case "0":
                operatorStatus = "Pending";
                break;
            case "1":
                operatorStatus = "Approved";
                break;
            case "2":
                operatorStatus = "Rejected";
                break;
            default: operatorStatus = tempStatus;
        }
        operatorApprovals.add(new OperatorApproval(name, operatorStatus));
    }

    public List<OperatorApproval> getOperatorList() {
        return operatorApprovals;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase();
    }


}

class OperatorApproval {
    String operatorName;
    String approvalStatus;

    public OperatorApproval(String operatorName, String approvalStatus) {
        this.operatorName = operatorName;
        this.approvalStatus = approvalStatus;
    }
}



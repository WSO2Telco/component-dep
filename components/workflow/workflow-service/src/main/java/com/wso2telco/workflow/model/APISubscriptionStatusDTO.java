package com.wso2telco.workflow.model;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for  API with list of operator approvals
 */
public class APISubscriptionStatusDTO {

    private String name;
    private String version;
    private String tier;
    private String adminApprovalStatus;
    private ArrayList<OperatorApproval> operatorApprovals;
    private String lastUpdated;

    public APISubscriptionStatusDTO() {
        operatorApprovals = new ArrayList();
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }


    public void addOperator(String name, String tempStatus) {
        String operatorStatus = "";

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
            default:
                operatorStatus = tempStatus;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getAdminApprovalStatus() {
        return adminApprovalStatus;
    }

    public void setAdminApprovalStatus(String adminApprovalStatus) {
        String status = "";

        switch (adminApprovalStatus) {
            case "BLOCKED":
                status = "Not Approved";
                break;
            case "UNBLOCKED":
                status = "Approved";
                break;
            default:
                status = adminApprovalStatus;
        }

        this.adminApprovalStatus = status;

    }
}

package com.wso2telco.workflow.model;



public class APISubscriptionDTO {


    private String name;
    private String id;
	private String version;
    private String tier;
    private String approvalStatus;
    private String operatorName;
    private String lastUpdated;


	public String getId() {
		return id;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public void setId(String id) {
		this.id = id;
	}
	


    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }


    public void setOperatoApprovalStatus(String name, String tempStatus) {
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

        operatorName=name;
        approvalStatus=operatorStatus;
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

        this.approvalStatus = status;

    }
	
}

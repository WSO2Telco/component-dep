package com.wso2telco.workflow.notification;

import com.wso2telco.workflow.model.*;

public interface Notification {

	public void sendHUBAdminAppApprovalNotification(HUBAdminAppApprovalNotificationRequest request);
    public void sendHUBAdminSubrovalNotification(HUBAdminSubApprovalNotificationRequest request);
    public void sendPLUGINAdminAppApprovalNotification(PLUGINAdminAppApprovalNotificationRequest request);
    public void sendPLUGINAdminSubApprovalNotification(PLUGINAdminSubApprovalNotificationRequest request);
    public void sendAppApprovalStatusSPNotification(AppApprovalStatusSPNotificationRequest request);
    public void sendSubApprovalStatusSPNotification(SubApprovalStatusSPNotificationRequest request);
    public void sendInternalAdminSubrovalNotification(HUBAdminSubApprovalNotificationRequest request);
    public void sendPublisherSubApprovalNotification(PLUGINAdminSubApprovalNotificationRequest request);

}

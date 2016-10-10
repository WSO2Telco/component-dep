package com.wso2telco.workflow.service;


import com.wso2telco.workflow.model.*;
import com.wso2telco.workflow.notification.Notification;
import com.wso2telco.workflow.notification.NotificationImpl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/notification")
public class NotificationService {

    Notification notification=new NotificationImpl();

    @POST
    @Path("application/hub")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendHUBAdminAppApprovalNotification(HUBAdminAppApprovalNotificationRequest request){
        try {
            notification.sendHUBAdminAppApprovalNotification(request);
            return Response.status(Response.Status.OK).build();
        }catch(Exception ex){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("subscription/hub")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendHUBAdminSubrovalNotification(HUBAdminSubApprovalNotificationRequest request){
        try {
            notification.sendHUBAdminSubrovalNotification(request);
            return Response.status(Response.Status.OK).build();
        }catch(Exception ex){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("application/plugin")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendPLUGINAdminAppApprovalNotification(PLUGINAdminAppApprovalNotificationRequest request){
        try {
            notification.sendPLUGINAdminAppApprovalNotification(request);
            return Response.status(Response.Status.OK).build();
        }catch(Exception ex){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("subscription/plugin")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendPLUGINAdminSubApprovalNotification(PLUGINAdminSubApprovalNotificationRequest request){
        try {
            notification.sendPLUGINAdminSubApprovalNotification(request);
            return Response.status(Response.Status.OK).build();
        }catch(Exception ex){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("application/sp")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendAppApprovalStatusSPNotification(AppApprovalStatusSPNotificationRequest request){
        try {
            notification.sendAppApprovalStatusSPNotification(request);
            return Response.status(Response.Status.OK).build();
        }catch(Exception ex){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("subscription/sp")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendSubApprovalStatusSPNotification(SubApprovalStatusSPNotificationRequest request){
        try {
            notification.sendSubApprovalStatusSPNotification(request);
        return Response.status(Response.Status.OK).build();
    }catch(Exception ex){
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }}


    @POST
    @Path("subscription/gateway")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendSubApprovalStatusGatewayNotification(HUBAdminSubApprovalNotificationRequest request){
        try {
            notification.sendInternalAdminSubrovalNotification(request);
            return Response.status(Response.Status.OK).build();
        }catch(Exception ex){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }}

    @POST
    @Path("subscription/publisher")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendSubApprovalStatusApiPublisherNotification(PLUGINAdminSubApprovalNotificationRequest request){
        try {
            notification.sendPublisherSubApprovalNotification(request);
            return Response.status(Response.Status.OK).build();
        }catch(Exception ex){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }}


}

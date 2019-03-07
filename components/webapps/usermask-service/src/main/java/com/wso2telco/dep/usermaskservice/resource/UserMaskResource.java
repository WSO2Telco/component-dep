package com.wso2telco.dep.usermaskservice.resource;


import com.wso2telco.dep.usermaskservice.dto.ErrorDTO;
import com.wso2telco.dep.usermaskservice.dto.UserMaskDTO;
import com.wso2telco.dep.usermaskservice.service.UserMaskService;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user-mask")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserMaskResource {

    private UserMaskService userMaskService = new UserMaskService();

    @POST
    @Path("/msisdn")
    public Response getUserMaskByMSISDN(UserMaskDTO userMaskDTO) {
        String mask = userMaskService.getUserMask(userMaskDTO.getUserId());

        if (userMaskDTO.getUserId().equalsIgnoreCase(mask) || StringUtils.isEmpty(mask)) {
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setMessage("Cannot get user mask due to server error.");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorDTO).build();
        }
        userMaskDTO.setMask(mask);
        return Response.status(Response.Status.OK).entity(userMaskDTO).build();
    }


    @POST
    @Path("/mask")
    public Response getUserIdByMask(UserMaskDTO userMaskDTO) {
        String msisdn = userMaskService.getUserId(userMaskDTO.getMask());

        if (msisdn.equalsIgnoreCase(userMaskDTO.getMask()) || StringUtils.isEmpty(msisdn)) {
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setMessage("Cannot get user id due to server error.");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorDTO).build();
        }
        userMaskDTO.setUserId(msisdn);
        return Response.status(Response.Status.OK).entity(userMaskDTO).build();
    }
}

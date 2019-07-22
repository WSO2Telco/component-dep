/*******************************************************************************
 * Copyright  (c) 2019, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *
 * WSO2.Telco Inc. licences this file to you under  the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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

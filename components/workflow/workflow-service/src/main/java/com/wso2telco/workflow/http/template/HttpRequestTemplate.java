package com.wso2telco.workflow.http.template;

import com.wso2telco.workflow.model.TierUpdtConnDTO;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

public class HttpRequestTemplate extends AbstractTemplate{

    public Response HTTP_PUT(TierUpdtConnDTO tierUpdtConnDTO){
        return getInvoker(tierUpdtConnDTO.getServerEP(),tierUpdtConnDTO.getPath(),tierUpdtConnDTO.getMediaType()
                ,tierUpdtConnDTO.getHeaders()).put(Entity.entity(tierUpdtConnDTO.getTierUpdtReq(), tierUpdtConnDTO.getMediaType()));
    }

}

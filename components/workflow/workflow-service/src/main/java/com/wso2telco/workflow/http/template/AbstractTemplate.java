package com.wso2telco.workflow.http.template;

import com.wso2telco.workflow.model.TierUpdtConnDTO;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

public abstract class AbstractTemplate {

    public abstract Response HTTP_PUT(TierUpdtConnDTO tierUpdtConnDTO);

    protected Invocation.Builder getInvoker(String serverEP, String path, MediaType mediaType, MultivaluedMap headers){
        return getWebTarget(serverEP,getClient(),path).request(mediaType).headers(headers);
    }

    private Client getClient(){
        return ClientBuilder.newClient();
    }

    private WebTarget getWebTarget(String sertverEP,Client client,String path){
        return client.target(sertverEP).path(path);
    }

}

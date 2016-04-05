/*
 * Queries.java
 * Apr 2, 2013  11:20:38 AM
 * Roshan.Saputhanthri
 *
 * Copyright (C) Dialog Axiata PLC. All Rights Reserved.
 */
package com.wso2telco.aggregatorblacklist;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.log4j.Logger;
import com.wso2telco.aggregatorblacklist.model.ErrorReturn;
import com.wso2telco.aggregatorblacklist.model.ProvisionReq;
import com.wso2telco.aggregatorblacklist.model.RequestError;
import com.wso2telco.dbutils.AxataDBUtilException;
import com.google.gson.Gson;

/**
 * REST Web Service
 *
 * @version $Id: Queries.java,v 1.00.000
 */
@Path("/")
public class Queries {

    private static final Logger LOG = Logger.getLogger(Queries.class.getName());
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of QueriesResource
     */
    public Queries() {
    }

    @POST
    @Path("/merchant/blacklist")
    @Consumes("application/json")
    @Produces("application/json")
    public Response merchantinsert(String jsonData) {

        try {

            System.out.println("jsondata: "+jsonData);
            ProvisionReq provisionreq = new Gson().fromJson(jsonData, ProvisionReq.class);
                       
            new ProvisionService().provisionapp(provisionreq);
            
        } catch (AxataDBUtilException e) {
            jsonData = new Gson().toJson(new RequestError(new ErrorReturn("POL0299",new String[]{e.getMessage()})));
            return Response.status(400).entity(jsonData).build();
        } catch (Exception e) {
            jsonData = new Gson().toJson(new RequestError(new ErrorReturn("POL0299",new String[]{"Internal Server Error"})));
            return Response.status(400).entity(jsonData).build();
        }

        return Response.status(201).build();
    }
    
}

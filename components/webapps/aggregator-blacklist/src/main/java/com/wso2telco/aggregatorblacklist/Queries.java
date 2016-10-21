/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
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
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.google.gson.Gson;


// TODO: Auto-generated Javadoc
/**
 * The Class Queries.
 */
@Path("/")
public class Queries {

    /** The Constant LOG. */
    private static final Logger LOG = Logger.getLogger(Queries.class.getName());
    
    /** The context. */
    @Context
    private UriInfo context;

   
    /**
     * Instantiates a new queries.
     */
    public Queries() {
    }

    /**
     * Merchantinsert.
     *
     * @param jsonData the json data
     * @return the response
     */
    @POST
    @Path("/merchant/blacklist")
    @Consumes("application/json")
    @Produces("application/json")
    public Response merchantinsert(String jsonData) {

        try {

            System.out.println("jsondata: "+jsonData);
            ProvisionReq provisionreq = new Gson().fromJson(jsonData, ProvisionReq.class);
                       
            new ProvisionService().provisionapp(provisionreq);
            
        } catch (BusinessException e) {
            jsonData = new Gson().toJson(new RequestError(new ErrorReturn("POL0299",new String[]{e.getMessage()})));
            return Response.status(400).entity(jsonData).build();
        } catch (Exception e) {
            jsonData = new Gson().toJson(new RequestError(new ErrorReturn("POL0299",new String[]{"Internal Server Error"})));
            return Response.status(400).entity(jsonData).build();
        }

        return Response.status(201).build();
    }
    
}

/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 *  WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.refund.api;

import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.wso2telco.refund.service.CreateService;
import com.wso2telco.refund.utils.DBUtilException;

@Path("/telco")
public class RefundService{
	
	/** The Constant logger. */
	final static Logger logger = Logger.getLogger(RefundService.class);
	
	@GET
	@Path("/payment")
	public String get(){
		logger.info("GET method called");
		return "Resource not available";
	}
	
	/**
	 * Refund.
	 *
	 * @param jsonBody the json body
	 * @return the response
	 */
	@POST
	@Path("/payment/refund")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response refund(String jsonBody){
		
		Response response = null;
		
		CreateService createService = new CreateService();
		try {
			createService.createEntry(jsonBody);
			response = Response.status(Response.Status.OK).build();
			logger.info("POST method called success");
		} catch (SQLException e) {
			logger.error(e.toString());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).type(MediaType.TEXT_PLAIN).build();
			e.printStackTrace();
		} catch (DBUtilException e) {
			logger.error(e.toString());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).type(MediaType.TEXT_PLAIN).build();
			e.printStackTrace();
		}
		return response;		
		
	}
}

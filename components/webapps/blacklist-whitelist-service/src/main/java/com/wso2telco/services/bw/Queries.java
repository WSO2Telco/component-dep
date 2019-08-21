/**
 * Copyright (c) 2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wso2telco.services.bw;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.dep.bw.model.RequestError;
import com.wso2telco.dep.operatorservice.model.*;
import com.wso2telco.dep.operatorservice.service.BlackListWhiteListService;
import com.wso2telco.dep.operatorservice.service.OparatorService;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Path("/queries")
public class Queries {

	private static final Logger LOG = Logger.getLogger(Queries.class.getName());
	@Context
	private UriInfo context;

	BlackListWhiteListService blackListWhiteListService;
	OparatorService oparatorService = null;

	{
		oparatorService = new OparatorService();
		blackListWhiteListService = new BlackListWhiteListService();
	}

	@GET
	@Path("/search/{app}/{api}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response search(@PathParam("app") String app, @PathParam("api") String api,
						   @QueryParam("msisdn") String msisdn, @QueryParam("action") String action, @QueryParam("sp") String sp) {

		Gson gson = new Gson();
		BlacklistWhitelistDTO dto = new BlacklistWhitelistDTO();
		BlacklistWhitelistSearchResponseDTO responseDTO;

		app = replaceAllWithWildcard(app);
		api = replaceAllWithWildcard(api);

		dto.setAppId(app);
		dto.setApiID(api);
		dto.addMsisdn(msisdn);
		dto.setAction(action);
		dto.setServiceProvider(sp);

		try {
			responseDTO = blackListWhiteListService.search(dto);

		} catch (Exception e) {
			LOG.error("Error searching for MSISDN", e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}

		return Response.status(Response.Status.OK).entity(gson.toJson(responseDTO)).build();
	}

	/**
	 * API used for blacklisting/whitelisting single a msisdn
	 * @param appId Application ID for the particular record
	 * @param apiId API ID for the particular entry
	 * @param jsonBody JSON body containing msisdn, action (i.e: blacklist/whitelist) and userId (login user i.e: admin's user name)
	 * @return
	 * @throws BusinessException
	 */
	@POST
	@Path("/add/{app}/{api}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response location(@PathParam("app") String appId, @PathParam("api") String apiId,
							 String jsonBody) throws BusinessException {

		LOG.debug("location Triggerd  jsonBody :" + jsonBody);

		Gson gson = new GsonBuilder().serializeNulls().create();

		BlacklistWhitelistRequestDTO requestDTO = gson.fromJson(jsonBody, BlacklistWhitelistRequestDTO.class);

		appId = replaceAllWithWildcard(appId);
		apiId = replaceAllWithWildcard(apiId);

		BlacklistWhitelistDTO blacklistWhitelistDTO = new BlacklistWhitelistDTO();

		blacklistWhitelistDTO.setApiID(apiId);
		blacklistWhitelistDTO.setAppId(appId);
		blacklistWhitelistDTO.addMsisdn(requestDTO.getMsisdn());
		blacklistWhitelistDTO.setAction(requestDTO.getAction());
		blacklistWhitelistDTO.setServiceProvider(requestDTO.getSp());
		blacklistWhitelistDTO.setUser(requestDTO.getUser());

		try {
			Response.Status status = Response.Status.CREATED;
			APIBlacklistWhitelistResponseDTO responseDTO = blackListWhiteListService.blacklist(blacklistWhitelistDTO);

			if(responseDTO.getFailed() > 0){
				status = Response.Status.BAD_REQUEST;
			}

			return Response.status(status).entity(gson.toJson(responseDTO)).build();

		} catch (BusinessException ex) {
			StringBuilder errorMessage = new StringBuilder("{ \"error\": \"" + ex.getMessage() + "\"}");
			return Response.status(Response.Status.BAD_REQUEST).entity(errorMessage.toString()).build();
		}
	}

	/**
	 * Returns the count per app per api
	 *
	 * {empty request body}
	 */
	@GET
	@Path("/count/{app}/{api}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response getBlacklistPerApi(@PathParam("app") String app, @PathParam("api") String api,
									   @QueryParam("action") String action, @QueryParam("sp") String sp) {

		Gson gson = new Gson();
		StringBuilder errorMSG = new StringBuilder();
		errorMSG.append("{" + "\"Failed\":").append("{").append("\"messageId\":\"").append("Blacklist result")
				.append("\",").append("\"text\":\"");
		errorMSG.append("Blacklist numbers could not be retrieved").append("\"" + "}}");

		app = replaceAllWithWildcard(app);
		api = replaceAllWithWildcard(api);
		sp = replaceAllWithWildcard(sp);

		BlacklistWhitelistDTO dto = new BlacklistWhitelistDTO();
		dto.setAppId(app);
		dto.setApiID(api);
		dto.setAction(action);
		dto.setServiceProvider(sp);

			try {

				BlacklistWhitelistCountResponseDTO responseDTO = blackListWhiteListService.count(dto);
				StringBuilder successMSG = new StringBuilder();

				return Response.status(Response.Status.OK).entity(gson.toJson(responseDTO)).build();
			} catch (BusinessException e) {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getErrorType()).build();
			}

	}

	@GET
	@Path("/downloadAsZip/{app}/{api}")
	@Produces("application/zip")
	public Response getBlacklistAsZip(@PathParam("app") String app, @PathParam("api") String api, @QueryParam("action") String action, @QueryParam("sp") String sp) {

		app = replaceAllWithWildcard(app);
		api = replaceAllWithWildcard(api);
		sp = replaceAllWithWildcard(sp);

		final BlacklistWhitelistDTO dto = new BlacklistWhitelistDTO();
		dto.setAppId(app);
		dto.setApiID(api);
		dto.setAction(action);
		dto.setServiceProvider(sp);

		try {

				StreamingOutput stream = new StreamingOutput() {
					@Override
					public void write(OutputStream outputStream) throws IOException, WebApplicationException {

						try {
							blackListWhiteListService.getBlacklistAsZip(dto, outputStream);
						} catch (Exception e) {
							throw new WebApplicationException(e);
						}


					}
				};

				return Response.status(Response.Status.OK).entity(stream).build();
			} catch (Exception e) {
				LOG.error("Error building zip file",e);
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
			}
	}

	/**
	 * API used for blacklisting/whitelisting a list of MSISDNs
	 * @param uploadedInputStream text file with records separated by a new line
	 * @param api API ID for this list of records
	 * @param app APP ID for this list of records
	 * @param action Action (i.e: blacklist/whitelist)
	 * @param user userId (login user i.e: admin's user name)
	 * @return
	 */
	@POST
	@Path("/bulkAdd/{app}/{api}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response bulkBlacklist(@PathParam("app") String app, @PathParam("api") String api, @Multipart("file") InputStream uploadedInputStream, @Multipart("action") String action, @Multipart("sp") String sp, @Multipart("user") String user) {

		APIBlacklistWhitelistResponseDTO response;
		BlacklistWhitelistDTO blacklistWhitelistDTO = new BlacklistWhitelistDTO();
		Gson gson = new Gson();
		List<String> msisdnList = new ArrayList<>();
		BufferedReader br;
		int limit = 5000; //max number of records to process

		app = replaceAllWithWildcard(app);
		api = replaceAllWithWildcard(api);
		sp = replaceAllWithWildcard(sp);

		blacklistWhitelistDTO.setAppId(app);
		blacklistWhitelistDTO.setApiID(api);
		blacklistWhitelistDTO.setAction(action);
		blacklistWhitelistDTO.setServiceProvider(sp);
		blacklistWhitelistDTO.setUser(user);


		br = new BufferedReader(new InputStreamReader(uploadedInputStream, StandardCharsets.UTF_8));

		try {
			while (br.ready() && limit > 0) {
				msisdnList.add(br.readLine());
				limit--;
			}
			Response.Status status = Response.Status.CREATED;
			blacklistWhitelistDTO.setMsisdnList(msisdnList);
			response = blackListWhiteListService.blacklist(blacklistWhitelistDTO);

			if(response.getFailed() > 0 && response.getProcessed() == 0){
				status = Response.Status.BAD_REQUEST;
			}

			return Response.status(status).entity(gson.toJson(response)).build();
		} catch (IOException | BusinessException e) {
			LOG.error("Unable to process upload", e);
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

	}

	/**
	 * Removes from the blacklist
	 *
	 * { "apiName":"USSD" }
	 */
	@DELETE
	@Path("/remove/{app}/{api}")
	public Response removeFromBlacklist(@PathParam("app") String app, @PathParam("api") String api,
										@QueryParam("msisdn") String msisdn, @QueryParam("action") String action, @QueryParam("sp") String sp) {
		Gson gson = new GsonBuilder().serializeNulls().create();
		BlacklistWhitelistDTO dto = new BlacklistWhitelistDTO();

		app = replaceAllWithWildcard(app);
		api = replaceAllWithWildcard(api);
		sp = replaceAllWithWildcard(sp);

		dto.setAppId(app);
		dto.setApiID(api);
		dto.addMsisdn(msisdn);
		dto.setAction(action);
		dto.setServiceProvider(sp);

		try {
			blackListWhiteListService.remove(dto);

			return Response.status(Response.Status.NO_CONTENT).build();
		} catch (BusinessException e) {
			LOG.error("Error removing number", e);

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getErrorType()).build();
		}
	}

	@GET
	@Path("/subscribers")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllSubscribers() throws BusinessException {

		String jsonString = blackListWhiteListService.getAllSubscribedUsers();
		return Response.status(Response.Status.OK).entity(jsonString).build();
	}

	@GET
	@Path("/apps/{userId}/{operatorId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllApplicationsByUser(@PathParam("userId") String userId,@PathParam("operatorId") String operatorId) throws BusinessException {

		//Gson gson = new GsonBuilder().serializeNulls().create();
		//Id userID = gson.fromJson(jsonBody, Id.class);
		String jsonString;
		userId = replaceAllWithWildcard(userId);
		if (operatorId.equals("_ALL_")) {
			jsonString = blackListWhiteListService.getAllApplicationsByUser(userId);
		} else{
			jsonString = blackListWhiteListService.getAllApplicationsByUserAndOperator(userId, operatorId);
		}

		return Response.status(Response.Status.OK).entity(jsonString).build();
	}


	@GET
	@Path("/apis/{userId}/{appId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllApisByUserAndApp(@PathParam("userId") String userId, @PathParam("appId") String appId) {
		String jsonString = null;
		try {
			userId = replaceAllWithWildcard(userId);
			appId = replaceAllWithWildcard(appId);

			jsonString = blackListWhiteListService.getAllApisByUserAndApp(userId, appId);
			return Response.status(Response.Status.OK).entity(jsonString).build();
		} catch (BusinessException e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}


	}

	@GET
	@Path("/apis")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllApi() throws BusinessException {
		String jsonString = blackListWhiteListService.getAllAPIs();
		return Response.status(Response.Status.OK).entity(jsonString).build();
	}

	/**
	 * Merchantinsert.
	 *
	 * @param jsonData
	 *            the json data
	 * @return the response
	 */
	@POST
	@Path("/merchant/blacklist")
	@Consumes("application/json")
	@Produces("application/json")
	public Response blacklistAggregator(String jsonData) {

		try {

			LOG.debug("jsondata: " + jsonData);

			ProvisionReq provisionreq = new Gson().fromJson(jsonData, ProvisionReq.class);

			oparatorService.blacklistAggregator(provisionreq);

		} catch (BusinessException e) {
			jsonData = new Gson().toJson(new RequestError(e.getErrorType()));
			LOG.error("", e);
			return Response.status(Response.Status.BAD_REQUEST).entity(jsonData).build();
		}
		LOG.debug("Aggregators blacklist  success jsonData :" + jsonData);
		return Response.status(Response.Status.CREATED).build();
	}

	private String replaceAllWithWildcard(String value){
		if(value.equals("_ALL_")){
			value = "%";
		}

		return value;
	}
}

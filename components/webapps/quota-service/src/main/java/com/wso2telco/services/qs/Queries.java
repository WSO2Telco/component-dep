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

package com.wso2telco.services.qs;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.services.qs.entity.QuotaBean;
import com.wso2telco.services.qs.entity.QuotaReqBean;
import com.wso2telco.services.qs.entity.QuotaReqBeanWithDates;
import com.wso2telco.services.qs.service.QuotaLimitService;

@Path("/services")
public class Queries {

	private static final Logger LOG = Logger.getLogger(Queries.class.getName());
	QuotaLimitService quotaService;

	{
		quotaService = new QuotaLimitService();
	}

	@POST
	@Path("/getQuotaLimitInfo")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getQuotaLimitInfo(String jsonBody) throws SQLException {
		LOG.debug("getQuotaLimitInfo request jsonBody :" + jsonBody);
		try {
			Gson gson = new Gson();
			QuotaReqBean quotaReqBean = gson.fromJson(jsonBody, QuotaReqBean.class);
			List<QuotaBean> quotaBeanList = quotaService.getQuotaLimitInfo(quotaReqBean.getByFlag(),quotaReqBean.getInfo());
			StringBuilder succMSG = new StringBuilder();
			succMSG.append("{ \"Success\": { \"service\": \"QuotaLimit\", \"text\": ");
			succMSG.append(new Gson().toJson(quotaBeanList));
			succMSG.append("}}");
			return Response.status(Response.Status.OK).entity(succMSG.toString()).build();

		} catch (BusinessException e) {
			StringBuilder errorMSG = new StringBuilder();
			errorMSG.append("{\"Failed\":{\"service\":\"getQuotaLimitInfo\",\"text\":\"QuotaLimit could not be retrieved. \"}}");
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getErrorType()).build();
		}

	}

	@POST
	@Path("/checkIfDatesOverlap")
	@Consumes("application/json")
	@Produces("application/json")
	public Response checkIfDatesOverlap(String jsonBody) throws SQLException {
		LOG.debug("checkIfDatesOverlap request jsonBody :" + jsonBody);
		try {
			Gson gson = new Gson();
			QuotaReqBeanWithDates quotaReqBeanWD = gson.fromJson(jsonBody, QuotaReqBeanWithDates.class);
			Boolean checkIfDatesOverlap = quotaService.checkIfDatesOverlap(quotaReqBeanWD.getByFlag(),quotaReqBeanWD.getInfo(),quotaReqBeanWD.getFromDate(),quotaReqBeanWD.getToDate());
			StringBuilder succMSG = new StringBuilder();
			succMSG.append("{ \"Success\": { \"service\": \"QuotaLimit\", \"text\": ");
			succMSG.append(new Gson().toJson(checkIfDatesOverlap.toString()));
			succMSG.append("}}");
			return Response.status(Response.Status.OK).entity(succMSG.toString()).build();

		} catch (BusinessException e) {
			StringBuilder errorMSG = new StringBuilder();
			errorMSG.append("{\"Failed\":{\"service\":\"getQuotaLimitInfo\",\"text\":\"QuotaLimit could not be retrieved. \"}}");
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getErrorType()).build();
		}

	}

	@POST
	@Path("/applyQuotaLimit")
	@Consumes("application/json")
	@Produces("application/json")
	public Response applyQuotaLimit(String jsonBody) throws SQLException {
		LOG.debug("applyQuotaLimit request jsonBody :" + jsonBody);

		Gson gson = new GsonBuilder().serializeNulls().create();
		QuotaBean quotaBean = gson.fromJson(jsonBody, QuotaBean.class);

		try {
			quotaService.addQuotaLimit(quotaBean);

			StringBuilder succMSG = new StringBuilder();
			succMSG.append("{\"Success\":{\"service\":\"applyQuotaLimit\",\"text\":\" Quota limit Successfully Added to the system. \"}}");
			return Response.status(Response.Status.OK).entity(succMSG.toString()).build();

		}catch (BusinessException exception) {
			StringBuilder errorMSG = new StringBuilder();
			errorMSG.append("{\"Failed\":{\"service\":\"applyQuotaLimit\",\"text\":\"QuotaLimit could not be added to the system. \"}}");
			return Response.status(Response.Status.BAD_REQUEST).entity(errorMSG.toString()).build();
		}
	}
}

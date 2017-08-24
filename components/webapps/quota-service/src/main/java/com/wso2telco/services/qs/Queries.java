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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.wso2.carbon.apimgt.api.model.Application;
import org.wso2.carbon.apimgt.api.model.Subscriber;
import org.wso2.carbon.apimgt.impl.dao.ApiMgtDAO;

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
	private static QuotaLimitService quotaService;

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
			List<QuotaBean> quotaBeanList = quotaService.getQuotaLimitInfo(quotaReqBean.getByFlag(),quotaReqBean.getInfo(),quotaReqBean.getOperator());
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
			Boolean checkIfDatesOverlap = quotaService.checkIfDatesOverlap(quotaReqBeanWD.getByFlag(),quotaReqBeanWD.getInfo(),quotaReqBeanWD.getFromDate(),quotaReqBeanWD.getToDate(),quotaReqBeanWD.getOperator());
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

		if (quotaBean.getOperator().equalsIgnoreCase("_ALL_")) {
			quotaBean.setOperator(null);
		}

		/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String fromDate = sdf.format(quotaBean.getFromDate());*/

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

	@GET
	@Path("/getOperatorsBySubscriber")
	@Produces("application/json")
	public static Response getOperatorsBySubscriber(@QueryParam("subscriberName") String subscriberName)throws SQLException,Exception {
		String operatorListJsonStr="{\"result\": \"empty\"}";
		List<String> operatorList = new ArrayList<String>();
		List<String> tempOperatorList = new ArrayList<String>();
		ApiMgtDAO apiMgtDAO = ApiMgtDAO.getInstance();
		Application[] applications = apiMgtDAO.getApplications(new Subscriber(subscriberName), null);
		if (applications != null && applications.length > 0) {
			for (Application application : applications) {
				int tempApplicationId = application.getId();
				List<String> tempOperatorNames = new ArrayList<String>();
				tempOperatorNames = QuotaLimitService.getOperatorNamesByApplication(tempApplicationId);
				for (String operator : tempOperatorNames) {
					String tempOperator = operator;
					tempOperatorList.add(tempOperator);
				}
			}
		} else {
			LOG.info("Application list for the provided subscriber is null or empty.");
		}
		operatorList.addAll(removeDuplicateWithOrder(tempOperatorList));
		if (!(operatorList.size()<1||operatorList.isEmpty())) {
			Gson gson=new Gson();
			operatorListJsonStr= gson.toJson(operatorList);
		}
		return Response.status(Response.Status.OK).entity(operatorListJsonStr).type(MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Path("/getSubscribersByOperator")
	@Produces("application/json")
	public static Response getSubscribersByOperator(@QueryParam("operatorName") String operatorName)throws SQLException, Exception{
        String subscribersJsonStr="{\"result\": \"empty\"}";
        List<String> subscribers = null;
        try {
        	if (operatorName.equalsIgnoreCase("_ALL_")) {
        		subscribers = QuotaLimitService.getAllSubscribers();
			}else{
				subscribers = getSubscribersListByOperator(operatorName);
			}

            if (subscribers != null) {
            	Gson gson=new Gson();
        		subscribersJsonStr= gson.toJson(subscribers);
            }
        } catch (Exception e) {
            LOG.error("Error occurred getSubscribersByOperator",e);
        }
		return Response.status(Response.Status.OK).entity(subscribersJsonStr).type(MediaType.APPLICATION_JSON).build();
    }


	public static List<String> getSubscribersListByOperator(String operatorName) throws SQLException, Exception {
		List<Integer> applicationIds = QuotaLimitService.getApplicationsByOperator(operatorName);
		List<String> subscribers = new ArrayList<String>();
		List<String> tempSubscribers = new ArrayList<String>();
		for (Integer applicationId : applicationIds) {
			ApiMgtDAO apiMgtDAO = ApiMgtDAO.getInstance();
			try {
				Application application = apiMgtDAO.getApplicationById(applicationId);
				int tempSubscriberId = application.getSubscriber().getId();
				String tempSubscriberName = apiMgtDAO.getSubscriber(tempSubscriberId).getName();
				tempSubscribers.add(tempSubscriberName);
			} catch (NullPointerException ne) {
				continue;
			}
		}
		subscribers.addAll(removeDuplicateWithOrder(tempSubscribers));
		return subscribers;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<String> removeDuplicateWithOrder(List<String> arlList) {
		Set set = new HashSet();
		List newList = new ArrayList();
		for (Iterator iter = arlList.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if (set.add(element)) {
				newList.add(element);
			}
		}
		arlList.clear();
		arlList.addAll(newList);
		return arlList;
	}

}

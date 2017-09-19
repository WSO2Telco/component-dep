/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * <p>
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.dep.ratecardservice.resource;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.ratecardservice.dao.model.ErrorDTO;
import com.wso2telco.dep.ratecardservice.dao.model.OperationRateDTO;
import com.wso2telco.dep.ratecardservice.dao.model.RateDefinitionDTO;
import com.wso2telco.dep.ratecardservice.service.OperationRateService;
import com.wso2telco.dep.ratecardservice.service.RateDefinitionService;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OperationRateResource {

	private final Log log = LogFactory.getLog(OperationRateResource.class);
	private OperationRateService operationRateService = new OperationRateService();

	@GET
	public Response getAssignedRateDefinitions(@PathParam("apiName") String apiName,
			@PathParam("apiOperationId") int apiOperationId, @QueryParam("schema") String schema) {

		List<RateDefinitionDTO> rateDefinitions = null;
		Status responseCode = null;
		Object responseString = null;

		RateDefinitionService rateDefinitionService = new RateDefinitionService();

		try {

			rateDefinitions = rateDefinitionService.getAssignedRateDefinitions(apiOperationId, schema);

			if (!rateDefinitions.isEmpty()) {

				responseString = rateDefinitions;
				responseCode = Response.Status.OK;
			} else {

				log.error(
						"Error in OperationRateResource getAssignedRateDefinitions : assigned api operation rates are not found in database ");
				throw new BusinessException(ServiceError.NO_RESOURCES);
			}
		} catch (BusinessException e) {

			ErrorDTO error = new ErrorDTO();
			ErrorDTO.RequestError requestError = new ErrorDTO.RequestError();
			ErrorDTO.RequestError.ServiceException serviceException = new ErrorDTO.RequestError.ServiceException();

			serviceException.setMessageId(e.getErrorType().getCode());
			serviceException.setText(e.getErrorType().getMessage());
			requestError.setServiceException(serviceException);
			error.setRequestError(requestError);

			if (e.getErrorType().getCode() == ServiceError.NO_RESOURCES.getCode()) {

				responseCode = Response.Status.NOT_FOUND;
			} else {

				responseCode = Response.Status.BAD_REQUEST;
			}

			responseString = error;
		}

		log.debug("OperationRateResource getAssignedRateDefinitions -> response code : " + responseCode);
		log.debug("OperationRateResource getAssignedRateDefinitions -> response body : " + responseString);

		return Response.status(responseCode).entity(responseString).build();
	}

	@POST
	public Response addOperationRate(List<OperationRateDTO> operationRateList) {

		List<OperationRateDTO> newOperationRateList = new ArrayList<OperationRateDTO>();
		Status responseCode = null;
		Object responseString = null;

		try {

			for (int i = 0; i < operationRateList.size(); i++) {

				OperationRateDTO operationRate = operationRateList.get(i);

				OperationRateDTO newOperationRate = operationRateService.addOperationRate(operationRate);

				newOperationRateList.add(newOperationRate);
			}

			if (!newOperationRateList.isEmpty()) {

				responseString = newOperationRateList;
				responseCode = Response.Status.CREATED;
			} else {

				log.error(
						"Error in OperationRateResource addOperationRate : operation rate can not insert to database ");
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
			}
		} catch (BusinessException e) {

			ErrorDTO error = new ErrorDTO();
			ErrorDTO.RequestError requestError = new ErrorDTO.RequestError();
			ErrorDTO.RequestError.ServiceException serviceException = new ErrorDTO.RequestError.ServiceException();

			serviceException.setMessageId(e.getErrorType().getCode());
			serviceException.setText(e.getErrorType().getMessage());
			requestError.setServiceException(serviceException);
			error.setRequestError(requestError);

			if (e.getErrorType().getCode() == ServiceError.NO_RESOURCES.getCode()) {

				responseCode = Response.Status.NOT_FOUND;
			} else {

				responseCode = Response.Status.BAD_REQUEST;
			}

			responseString = error;
		}

		log.debug("OperationRateResource addOperationRate -> response code : " + responseCode);
		log.debug("OperationRateResource addOperationRate -> response body : " + responseString);

		return Response.status(responseCode).entity(responseString).build();
	}
}

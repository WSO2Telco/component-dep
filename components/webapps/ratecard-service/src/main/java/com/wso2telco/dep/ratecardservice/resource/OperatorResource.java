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

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
import com.wso2telco.dep.ratecardservice.dao.model.OperatorDTO;
import com.wso2telco.dep.ratecardservice.dao.model.RateDTO;
import com.wso2telco.dep.ratecardservice.dao.model.RateDefinitionDTO;
import com.wso2telco.dep.ratecardservice.dao.model.ErrorDTO;
import com.wso2telco.dep.ratecardservice.service.OperatorService;
import com.wso2telco.dep.ratecardservice.service.RateDefinitionService;
import com.wso2telco.dep.ratecardservice.service.RateService;

@Path("/operators")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OperatorResource {

	private final Log log = LogFactory.getLog(CurrencyResource.class);
	private OperatorService operatorService = new OperatorService();

	@GET
	public Response getOperators() {

		List<OperatorDTO> operators = null;
		Status responseCode = null;
		Object responseString = null;

		try {

			operators = operatorService.getOperators();

			if (!operators.isEmpty()) {

				responseString = operators;
				responseCode = Response.Status.OK;
			} else {

				log.error("Error in OperatorResource getOperators : operators are not found in database ");
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

		log.debug("OperatorResource getOperators -> response code : " + responseCode);
		log.debug("OperatorResource getOperators -> response body : " + responseString);

		return Response.status(responseCode).entity(responseString).build();
	}

	@GET
	@Path("/{operatorName}/apis/{apiName}/operatorrates")
	public Response getOperationRates(@PathParam("operatorName") String operatorName,
			@PathParam("apiName") String apiName, @QueryParam("schema") String schema) {

		RateDTO rate = null;
		Status responseCode = null;
		Object responseString = null;

		RateService rateService = new RateService();

		try {

			rate = rateService.getOperationRates(apiName, operatorName.toUpperCase(), schema);

			if (rate != null) {

				responseString = rate;
				responseCode = Response.Status.OK;
			} else {

				log.error("Error in OperatorResource getOperationRates : operation rates are not found in database ");
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

		log.debug("OperatorResource getOperationRates -> response code : " + responseCode);
		log.debug("OperatorResource getOperationRates -> response body : " + responseString);

		return Response.status(responseCode).entity(responseString).build();
	}

	@GET
	@Path("/{operatorId}/apis/{apiName}/operations/{apiOperationId}/ratedefinitions")
	public Response getAPIOperationRates(@PathParam("operatorId") int operatorId, @PathParam("apiName") String apiName,
			@PathParam("apiOperationId") int apiOperationId, @QueryParam("schema") String schema) {

		List<RateDefinitionDTO> rateDefinitions = null;
		Status responseCode = null;
		Object responseString = null;

		RateDefinitionService rateDefinitionService = new RateDefinitionService();

		try {

			rateDefinitions = rateDefinitionService.getRateDefinitions(apiOperationId, operatorId, schema);

			if (!rateDefinitions.isEmpty()) {

				responseString = rateDefinitions;
				responseCode = Response.Status.OK;
			} else {

				log.error(
						"Error in OperatorResource getAPIOperationRates : api operation rates are not found in database ");
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

		log.debug("OperatorResource getAPIOperationRates -> response code : " + responseCode);
		log.debug("OperatorResource getAPIOperationRates -> response body : " + responseString);

		return Response.status(responseCode).entity(responseString).build();
	}

	@Path("/{operatorId}/apis/{apiName}/operations/{apiOperationId}/operatorrates")
	public OperatorRateResource getOperatorRateResource() {

		return new OperatorRateResource();
	}
}

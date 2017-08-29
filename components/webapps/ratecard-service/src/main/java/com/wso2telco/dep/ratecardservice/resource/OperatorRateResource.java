package com.wso2telco.dep.ratecardservice.resource;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
public class OperatorRateResource {

	private final Log log = LogFactory.getLog(OperatorRateResource.class);
	private OperationRateService operationRateService = new OperationRateService();

	@GET
	public Response getAssignedRateDefinitions(@PathParam("operatorId") int operatorId,
			@PathParam("apiName") String apiName, @PathParam("apiOperationId") int apiOperationId) {

		List<RateDefinitionDTO> rateDefinitions = null;
		Status responseCode = null;
		Object responseString = null;

		RateDefinitionService rateDefinitionService = new RateDefinitionService();

		try {

			rateDefinitions = rateDefinitionService.getAssignedRateDefinitions(apiOperationId, operatorId);

			if (!rateDefinitions.isEmpty()) {

				responseString = rateDefinitions;
				responseCode = Response.Status.OK;
			} else {

				log.error(
						"Error in OperatorRateResource getAssignedRateDefinitions : assigned api operation rates are not found in database ");
				throw new BusinessException(ServiceError.NO_RESOURCES);
			}
		} catch (BusinessException e) {

			ErrorDTO errorDTO = new ErrorDTO();
			ErrorDTO.ServiceException serviceException = new ErrorDTO.ServiceException();

			serviceException.setMessageId(e.getErrorType().getCode());
			serviceException.setText(e.getErrorType().getMessage());
			errorDTO.setServiceException(serviceException);

			responseCode = Response.Status.NOT_FOUND;
			responseString = errorDTO;
		} catch (Exception e) {

			ErrorDTO errorDTO = new ErrorDTO();
			ErrorDTO.ServiceException serviceException = new ErrorDTO.ServiceException();

			if (e instanceof BusinessException) {

				BusinessException be = (BusinessException) e;
				serviceException.setMessageId(be.getErrorType().getCode());
				serviceException.setText(be.getErrorType().getMessage());
				errorDTO.setServiceException(serviceException);
			}

			responseCode = Response.Status.BAD_REQUEST;
			responseString = errorDTO;
		}

		log.debug("OperatorRateResource getAssignedRateDefinitions -> response code : " + responseCode);
		log.debug("OperatorRateResource getAssignedRateDefinitions -> response body : " + responseString);

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

				log.error("Error in OperatorRateResource addOperationRate : operator rate can not insert to database ");
				throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
			}
		} catch (Exception e) {

			ErrorDTO errorDTO = new ErrorDTO();
			ErrorDTO.ServiceException serviceException = new ErrorDTO.ServiceException();

			if (e instanceof BusinessException) {

				BusinessException be = (BusinessException) e;
				serviceException.setMessageId(be.getErrorType().getCode());
				serviceException.setText(be.getErrorType().getMessage());
				errorDTO.setServiceException(serviceException);
			}

			responseCode = Response.Status.BAD_REQUEST;
			responseString = errorDTO;
		}

		log.debug("OperatorRateResource addOperationRate -> response code : " + responseCode);
		log.debug("OperatorRateResource addOperationRate -> response body : " + responseString);

		return Response.status(responseCode).entity(responseString).build();
	}
}

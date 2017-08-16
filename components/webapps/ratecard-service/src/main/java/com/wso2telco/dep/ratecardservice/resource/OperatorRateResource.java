package com.wso2telco.dep.ratecardservice.resource;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
import com.wso2telco.dep.ratecardservice.service.OperationRateService;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OperatorRateResource {

	private final Log log = LogFactory.getLog(OperationRateResource.class);
	private OperationRateService operationRateService = new OperationRateService();
	
	@GET
	public Response getOperationRatesByAPIName(@PathParam("operatorName") String operatorName, @QueryParam("api") String apiName) {

		List<OperationRateDTO> operationRates = null;
		Status responseCode = null;
		Object responseString = null;

		try {

			operationRates = operationRateService.getOperationRates(apiName, operatorName.toUpperCase());

			if (!operationRates.isEmpty()) {

				responseString = operationRates;
				responseCode = Response.Status.OK;
			} else {

				log.error(
						"Error in OperationRateResource getOperationRatesByAPIName : operation rates are not found in database ");
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

		log.debug("OperationRateResource getOperationRatesByAPIName -> response code : " + responseCode);
		log.debug("OperationRateResource getOperationRatesByAPIName -> response body : " + responseString);

		return Response.status(responseCode).entity(responseString).build();
	}
}

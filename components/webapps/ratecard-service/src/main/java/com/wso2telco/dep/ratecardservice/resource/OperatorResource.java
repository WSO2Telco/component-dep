package com.wso2telco.dep.ratecardservice.resource;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.ratecardservice.dao.model.OperatorDTO;
import com.wso2telco.dep.ratecardservice.dao.model.ErrorDTO;
import com.wso2telco.dep.ratecardservice.service.OperatorService;

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

		log.debug("OperatorResource getOperators -> response code : " + responseCode);
		log.debug("OperatorResource getOperators -> response body : " + responseString);

		return Response.status(responseCode).entity(responseString).build();
	}
	
	@Path("/{operatorName}/operatorrates")
	public OperatorRateResource getOperatorRateResource() {

		return new OperatorRateResource();
	}
}

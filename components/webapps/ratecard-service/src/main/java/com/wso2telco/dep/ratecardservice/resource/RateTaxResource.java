package com.wso2telco.dep.ratecardservice.resource;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import com.wso2telco.dep.ratecardservice.dao.model.ErrorDTO;
import com.wso2telco.dep.ratecardservice.dao.model.RateTaxDTO;
import com.wso2telco.dep.ratecardservice.service.RateTaxService;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RateTaxResource {

	private final Log log = LogFactory.getLog(RateTaxResource.class);
	private RateTaxService rateTaxService = new RateTaxService();

	@POST
	public Response addRateTax(@PathParam("taxId") int taxId, RateTaxDTO rateTax) {

		RateTaxDTO newRateTaxDTO = null;
		Status responseCode = null;
		Object responseString = null;

		try {

			newRateTaxDTO = rateTaxService.addRateTax(rateTax);

			if (newRateTaxDTO != null) {

				responseString = newRateTaxDTO;
				responseCode = Response.Status.CREATED;
			} else {

				log.error("Error in RateTaxResource addRateTax : rate tax can not insert to database ");
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

		log.debug("RateTaxResource addRateTax -> response code : " + responseCode);
		log.debug("RateTaxResource addRateTax -> response body : " + responseString);

		return Response.status(responseCode).entity(responseString).build();
	}

	@GET
	public Response getRateTaxes(@PathParam("taxId") int taxId, @QueryParam("schema") String schema) {

		List<RateTaxDTO> rateTaxes = null;
		Status responseCode = null;
		Object responseString = null;

		try {

			rateTaxes = rateTaxService.getRateTaxes(taxId, schema);

			if (!rateTaxes.isEmpty()) {

				responseString = rateTaxes;
				responseCode = Response.Status.OK;
			} else {

				log.error("Error in RateTaxResource getRateTaxes : rate taxes are not found in database ");
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

		log.debug("RateTaxResource getRateTaxes -> response code : " + responseCode);
		log.debug("RateTaxResource getRateTaxes -> response body : " + responseString);

		return Response.status(responseCode).entity(responseString).build();
	}

	@DELETE
	@Path("/{rateTaxId}")
	public Response deleteRateTax(@PathParam("taxId") int taxId, @PathParam("rateTaxId") int rateTaxId) {

		boolean status = false;
		Status responseCode = null;
		Object responseString = null;

		try {

			status = rateTaxService.deleteRateTax(rateTaxId);

			if (status != false) {

				responseCode = Response.Status.NO_CONTENT;
			} else {

				log.error("Error in RateTaxResource deleteRateTax : rate tax is not found in database ");
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

		log.debug("RateTaxResource deleteRateTax -> response code : " + responseCode);
		log.debug("RateTaxResource deleteRateTax -> response body : " + responseString);

		return Response.status(responseCode).build();
	}
}

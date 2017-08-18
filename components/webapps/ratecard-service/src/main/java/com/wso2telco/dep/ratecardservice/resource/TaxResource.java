package com.wso2telco.dep.ratecardservice.resource;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
import com.wso2telco.dep.ratecardservice.dao.model.TaxDTO;
import com.wso2telco.dep.ratecardservice.service.TaxService;

@Path("/taxes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TaxResource {

	private final Log log = LogFactory.getLog(TaxResource.class);
	private TaxService taxService = new TaxService();

	@GET
	public Response getTaxes() {

		List<TaxDTO> taxes = null;
		Status responseCode = null;
		Object responseString = null;

		try {

			taxes = taxService.getTaxes();

			if (!taxes.isEmpty()) {

				responseString = taxes;
				responseCode = Response.Status.OK;
			} else {

				log.error("Error in TaxResource getTaxes : taxes are not found in database ");
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

		log.debug("TaxResource getTaxes -> response code : " + responseCode);
		log.debug("TaxResource getTaxes -> response body : " + responseString);

		return Response.status(responseCode).entity(responseString).build();
	}

	@POST
	public Response addTax(TaxDTO tax) {

		TaxDTO newtax = null;
		Status responseCode = null;
		Object responseString = null;

		try {

			newtax = taxService.addTax(tax);

			if (newtax != null) {

				responseString = newtax;
				responseCode = Response.Status.CREATED;
			} else {

				log.error("Error in TaxResource addTax : tax can not insert to database ");
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

		log.debug("TaxResource addTax -> response code : " + responseCode);
		log.debug("TaxResource addTax -> response body : " + responseString);

		return Response.status(responseCode).entity(responseString).build();
	}

	@GET
	@Path("/{taxId}")
	public Response getTax(@PathParam("taxId") int taxId) {

		TaxDTO tax = null;
		Status responseCode = null;
		Object responseString = null;

		try {

			tax = taxService.getTax(taxId);

			if (tax != null) {

				responseString = tax;
				responseCode = Response.Status.OK;
			} else {

				log.error("Error in TaxResource getTax : tax is not found in database ");
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

		log.debug("TaxResource getTax -> response code : " + responseCode);
		log.debug("TaxResource getTax -> response body : " + responseString);

		return Response.status(responseCode).entity(responseString).build();
	}

	@Path("/{taxId}/ratetaxes")
	public RateTaxResource getRateTaxResource() {

		return new RateTaxResource();
	}
}

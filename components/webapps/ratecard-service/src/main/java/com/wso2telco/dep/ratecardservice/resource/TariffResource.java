package com.wso2telco.dep.ratecardservice.resource;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.ratecardservice.dao.model.ErrorDTO;
import com.wso2telco.dep.ratecardservice.dao.model.TariffDTO;
import com.wso2telco.dep.ratecardservice.service.TariffService;

@Path("/tariffs")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TariffResource {

	private final Log log = LogFactory.getLog(TariffResource.class);
	private TariffService tariffService = new TariffService();
	
	@GET
	public Response getTariffs() {
		
		List<TariffDTO> tariffs = null;
		Status responseCode = null;
		Object responseString = null;
		
		try {

			tariffs = tariffService.getTariffs();
			
			if (!tariffs.isEmpty()) {

				responseString = tariffs;
				responseCode = Response.Status.OK;
			} else {

				log.error("Error in TariffResource getTariffs : tariffs are not found in database ");
				new BusinessException(ServiceError.NO_RESOURCES);
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

		log.debug("TariffResource getTariffs -> response code : " + responseCode);
		log.debug("TariffResource getTariffs -> response body : " + responseString);

		return Response.status(responseCode).entity(responseString).build();
	}
	
	@POST
	public Response addTariff(TariffDTO tariff) {

		TariffDTO newTariff = null;
		Status responseCode = null;
		Object responseString = null;

		try {

			newTariff = tariffService.addTariff(tariff);

			if (newTariff != null) {

				responseString = newTariff;
				responseCode = Response.Status.CREATED;
			} else {

				log.error("Error in TariffResource addTariff : tariff can not insert to database ");
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

		log.debug("TariffResource addTariff -> response code : " + responseCode);
		log.debug("TariffResource addTariff -> response body : " + responseString);

		return Response.status(responseCode).entity(responseString).build();
	}
}

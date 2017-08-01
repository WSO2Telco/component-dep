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
import com.wso2telco.dep.ratecardservice.service.CurrencyService;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.dep.ratecardservice.dao.model.CurrencyDTO;
import com.wso2telco.dep.ratecardservice.dao.model.ErrorDTO;

@Path("/currencies")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CurrencyResource {

	private final Log log = LogFactory.getLog(CurrencyResource.class);
	private CurrencyService currencyService = new CurrencyService();

	@GET
	public Response getCurrencies() {

		List<CurrencyDTO> currencies = null;
		Status responseCode = null;
		Object responseString = null;

		try {

			currencies = currencyService.getCurrencies();

			if (!currencies.isEmpty()) {

				responseString = currencies;
				responseCode = Response.Status.OK;
			} else {

				log.error("Error in CurrencyResource getCurrencies : currencies are not found in database ");
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

		log.debug("CurrencyResource getCurrencies -> response code : " + responseCode);
		log.debug("CurrencyResource getCurrencies -> response body : " + responseString);

		return Response.status(responseCode).entity(responseString).build();
	}

	@POST
	public Response addCurrency(CurrencyDTO currency) {

		CurrencyDTO newCurrency = null;
		Status responseCode = null;
		Object responseString = null;

		try {

			newCurrency = currencyService.addCurrency(currency);

			if (newCurrency != null) {

				responseString = newCurrency;
				responseCode = Response.Status.CREATED;
			} else {

				log.error("Error in CurrencyResource addCurrency : currency can not insert to database ");
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

		log.debug("CurrencyResource addCurrency -> response code : " + responseCode);
		log.debug("CurrencyResource addCurrency -> response body : " + responseString);

		return Response.status(responseCode).entity(responseString).build();
	}
}

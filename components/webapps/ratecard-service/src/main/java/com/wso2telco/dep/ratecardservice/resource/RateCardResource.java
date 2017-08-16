package com.wso2telco.dep.ratecardservice.resource;

import javax.ws.rs.Consumes;
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
import com.wso2telco.dep.ratecardservice.dao.model.RateCardDTO;
import com.wso2telco.dep.ratecardservice.service.RateCardService;

@Path("/ratecards")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RateCardResource {

	private final Log log = LogFactory.getLog(RateCardResource.class);
	private RateCardService rateCardService = new RateCardService();
	
	@POST
	public Response addRateCard(RateCardDTO rateCard) {

		RateCardDTO newRateCard = null;
		Status responseCode = null;
		Object responseString = null;

		try {

			newRateCard = rateCardService.addRateCard(rateCard);

			if (newRateCard != null) {

				responseString = newRateCard;
				responseCode = Response.Status.CREATED;
			} else {

				log.error(
						"Error in RateCardResource addRateCard : rate card can not insert to database ");
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

		log.debug("RateCardResource addRateCard -> response code : " + responseCode);
		log.debug("RateCardResource addRateCard -> response body : " + responseString);

		return Response.status(responseCode).entity(responseString).build();
	}
}

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
import com.wso2telco.dep.ratecardservice.dao.model.ApplicationSubcriptionsDTO;
import com.wso2telco.dep.ratecardservice.dao.model.ErrorDTO;
import com.wso2telco.dep.ratecardservice.dao.model.OperationRateDTO;
import com.wso2telco.dep.ratecardservice.service.ApplicationSubcriptionsService;


@Path("/applications")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ApplicationSubcriptionsResource {
	
	private final Log log = LogFactory.getLog(ApplicationSubcriptionsResource.class);
	private ApplicationSubcriptionsService service = new ApplicationSubcriptionsService();

	
	@GET
	@Path("/{appId}/operators/{operatorId}/apis/{apiName}/apiversion/{version}/SBsubscriptions")
	public Response getSBOperationsDetails(@PathParam("appId") String appId,@PathParam("operatorId") String operatorId,@PathParam("apiName") String apiName,@PathParam("version") String version) {

		List<ApplicationSubcriptionsDTO> applicationSubcriptions = null;
		Status responseCode = null;
		Object responseString = null;
		

		try {
			applicationSubcriptions = service.getSBSubscriptionRateInfo(appId, operatorId, apiName,version);
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

		responseString = applicationSubcriptions;
		responseCode = Response.Status.OK;

		log.debug("APIResource getAPIs -> response code : " + responseCode);
		log.debug("APIResource getAPIs -> response body : " + responseString);

		return Response.status(responseCode).entity(responseString).build();
	}
	
	@GET
	@Path("/{appId}/apis/{apiName}/apiversion/{version}/NBsubscriptions")
	public Response getNBOperationsDetails(@PathParam("appId") String appId,@PathParam("apiName") String apiName,@PathParam("version") String version) {

		List<ApplicationSubcriptionsDTO> applicationSubcriptions = null;
		Status responseCode = null;
		Object responseString = null;

		
		try {
			applicationSubcriptions = service.getNBSubscriptionRateInfo(appId,apiName,version );
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

		responseString = applicationSubcriptions;
		responseCode = Response.Status.OK;

		log.debug("APIResource getAPIs -> response code : " + responseCode);
		log.debug("APIResource getAPIs -> response body : " + responseString);

		return Response.status(responseCode).entity(responseString).build();
	}
	
	@POST
	@Path("/{appId}/apis/{apiName}/apiversion/{version}/NBsubscriptions")
	public Response updateNBOperationsDetails(@PathParam("appId") String appId,@PathParam("apiName") String apiName,@PathParam("version") String version,List<ApplicationSubcriptionsDTO> applicationSubcriptionsRateList) {

		List<ApplicationSubcriptionsDTO> applicationSubcriptions = null;
		Status responseCode = null;
		Object responseString = null;

		try {
			applicationSubcriptions = service.updateNBSubscriptionRateInfo(appId, apiName,version,applicationSubcriptionsRateList);
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

		responseString = applicationSubcriptions;
		responseCode = Response.Status.OK;

		log.debug("APIResource getAPIs -> response code : " + responseCode);
		log.debug("APIResource getAPIs -> response body : " + responseString);

		return Response.status(responseCode).entity(responseString).build();
	}
	
	@POST
	@Path("/{appId}/operators/{operatorId}/apis/{apiName}/apiversion/{version}/SBsubscriptions")
	public Response updateSBOperationsDetails(@PathParam("appId") String appId,@PathParam("operatorId") String operatorId,@PathParam("apiName") String apiName,@PathParam("version") String version,List<ApplicationSubcriptionsDTO> applicationSubcriptionsRateList) {

		List<ApplicationSubcriptionsDTO> applicationSubcriptions = null;
		Status responseCode = null;
		Object responseString = null;

		try {
			applicationSubcriptions = service.updateSBSubscriptionRateInfo(appId, operatorId, apiName,version,applicationSubcriptionsRateList);
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

		responseString = applicationSubcriptions;
		responseCode = Response.Status.OK;

		log.debug("APIResource getAPIs -> response code : " + responseCode);
		log.debug("APIResource getAPIs -> response body : " + responseString);

		return Response.status(responseCode).entity(responseString).build();
	}

	
}

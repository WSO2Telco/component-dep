package com.wso2telco.dep.ratecardservice.service;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.dep.ratecardservice.dao.ApplicationSubcriptionsDAO;
import com.wso2telco.dep.ratecardservice.dao.model.ApplicationSubcriptionsDTO;
import com.wso2telco.dep.ratecardservice.resource.APIResource;

public class ApplicationSubcriptionsService {
	
	private final Log log = LogFactory.getLog(ApplicationSubcriptionsService.class);
	
	public List<ApplicationSubcriptionsDTO> getSBSubscriptionRateInfo(String appId, String operatorId,String apiName,String version) throws BusinessException{
		 
		ApplicationSubcriptionsDAO applicationSubcriptionsDAO=new ApplicationSubcriptionsDAO();
		List<ApplicationSubcriptionsDTO> applicationSubcriptions=applicationSubcriptionsDAO.getSBRates(appId, operatorId, apiName,version);
		return applicationSubcriptions;
	}
	
	
	public List<ApplicationSubcriptionsDTO> getNBSubscriptionRateInfo(String appId,String apiName,String version) throws BusinessException{
		 
		ApplicationSubcriptionsDAO applicationSubcriptionsDAO=new ApplicationSubcriptionsDAO();
		List<ApplicationSubcriptionsDTO> applicationSubcriptions=applicationSubcriptionsDAO.getNBRates(appId, apiName,version);
		return applicationSubcriptions;
	}
	
	public List<ApplicationSubcriptionsDTO> updateSBSubscriptionRateInfo(String appId, String operatorId,String apiName, String version,List<ApplicationSubcriptionsDTO> applicationSubcriptionsRateList) throws BusinessException{
		 
		ApplicationSubcriptionsDAO applicationSubcriptionsDAO=new ApplicationSubcriptionsDAO();
		List<ApplicationSubcriptionsDTO> applicationSubcriptionsDTOs = null;
		List<ApplicationSubcriptionsDTO> updatedList = new ArrayList<ApplicationSubcriptionsDTO>();;
		applicationSubcriptionsDTOs=applicationSubcriptionsDAO.getExistingSBRates(appId, operatorId, apiName,version);
				
		for (ApplicationSubcriptionsDTO applicationSubcriptionsDTO:applicationSubcriptionsRateList) {
			boolean isUpdate=false;
			ApplicationSubcriptionsDTO updatedDto=null;
			for ( ApplicationSubcriptionsDTO applicationSubcriptionsDTODB:applicationSubcriptionsDTOs) {



				if (applicationSubcriptionsDTO.getApiOperationId().equals(applicationSubcriptionsDTODB.getApiOperationId()) &&
					applicationSubcriptionsDTO.getApplicationId().equals(applicationSubcriptionsDTODB.getApplicationId()) &&
					applicationSubcriptionsDTO.getOperatorId().equals(applicationSubcriptionsDTODB.getOperatorId()) ) 
				{	
					isUpdate=true;
					updatedDto=applicationSubcriptionsDTODB;
					applicationSubcriptionsDTO.setApiOperationId(applicationSubcriptionsDTODB.getApiOperationId());
					break;
				} 
			}
			
			
			if (isUpdate) {
				log.debug("SB before update");
				ApplicationSubcriptionsDTO ubdatedDTO=applicationSubcriptionsDAO.updateSBRates(applicationSubcriptionsDTO);
				log.debug("update success");
				updatedDto.setComment(applicationSubcriptionsDTO.getComment());
				applicationSubcriptionsDAO.insertUpdatedSBRates(updatedDto);
				log.debug("insert update success");
				updatedList.add(ubdatedDTO);
			} else {
				log.debug("SB insert");
				ApplicationSubcriptionsDTO ubdatedDTO=applicationSubcriptionsDAO.insertSBRates(applicationSubcriptionsDTO);
				log.debug("SB insert  success");
				updatedList.add(ubdatedDTO);
			}

		}
		return updatedList;
	}
	
	
	public List<ApplicationSubcriptionsDTO> updateNBSubscriptionRateInfo(String appId,String apiName, String version, List<ApplicationSubcriptionsDTO> applicationSubcriptionsRateList) throws BusinessException{
		 
		ApplicationSubcriptionsDAO applicationSubcriptionsDAO=new ApplicationSubcriptionsDAO();
		List<ApplicationSubcriptionsDTO> applicationSubcriptionsDTOs = null;
		List<ApplicationSubcriptionsDTO> updatedList = new ArrayList<ApplicationSubcriptionsDTO>();;
		applicationSubcriptionsDTOs=applicationSubcriptionsDAO.getExistingNBRates(appId, apiName, version);
		
		for (ApplicationSubcriptionsDTO applicationSubcriptionsDTO:applicationSubcriptionsRateList) {
			
			boolean isUpdate=false;
			ApplicationSubcriptionsDTO updatedDto=null;
			for (ApplicationSubcriptionsDTO applicationSubcriptionsDTODB:applicationSubcriptionsDTOs) {


				if (applicationSubcriptionsDTO.getApiOperationId().equals(applicationSubcriptionsDTODB.getApiOperationId()) &&
					applicationSubcriptionsDTO.getApplicationId().equals(applicationSubcriptionsDTODB.getApplicationId()) ) 
				{				
					isUpdate=true;
					updatedDto=applicationSubcriptionsDTODB;
					applicationSubcriptionsDTO.setApiOperationId(applicationSubcriptionsDTODB.getApiOperationId());
					break;
				} 
			}
			
			if (isUpdate) {
				log.debug("Nb before update");
				ApplicationSubcriptionsDTO ubdatedDTO=applicationSubcriptionsDAO.updateNBRates(applicationSubcriptionsDTO);
				log.debug("update success");
				updatedList.add(ubdatedDTO);
				updatedDto.setComment(applicationSubcriptionsDTO.getComment());
				applicationSubcriptionsDAO.insertUpdatedNBRates(updatedDto);
				log.debug("insert update success");
			} else {

				log.debug("NB insert  success");
				ApplicationSubcriptionsDTO ubdatedDTO=applicationSubcriptionsDAO.insertNBRates(applicationSubcriptionsDTO);
				log.debug("NB insert  success");
				updatedList.add(ubdatedDTO);
			}

		}
		return updatedList;
	}
}

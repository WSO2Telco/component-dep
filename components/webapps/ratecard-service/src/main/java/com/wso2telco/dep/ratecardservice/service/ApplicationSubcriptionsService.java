package com.wso2telco.dep.ratecardservice.service;


import java.util.ArrayList;
import java.util.List;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.dep.ratecardservice.dao.ApplicationSubcriptionsDAO;
import com.wso2telco.dep.ratecardservice.dao.model.ApplicationSubcriptionsDTO;

public class ApplicationSubcriptionsService {
	
	public List<ApplicationSubcriptionsDTO> getSBSubscriptionRateInfo(String appId, String operatorId,String apiId) throws BusinessException{
		 
		ApplicationSubcriptionsDAO applicationSubcriptionsDAO=new ApplicationSubcriptionsDAO();
		List<ApplicationSubcriptionsDTO> applicationSubcriptions=applicationSubcriptionsDAO.getSBRates(appId, operatorId, apiId);
		return applicationSubcriptions;
	}
	
	
	public List<ApplicationSubcriptionsDTO> getNBSubscriptionRateInfo(String appId,String apiId) throws BusinessException{
		 
		ApplicationSubcriptionsDAO applicationSubcriptionsDAO=new ApplicationSubcriptionsDAO();
		List<ApplicationSubcriptionsDTO> applicationSubcriptions=applicationSubcriptionsDAO.getNBRates(appId, apiId);
		return applicationSubcriptions;
	}
	
	public List<ApplicationSubcriptionsDTO> updateSBSubscriptionRateInfo(String appId, String operatorId,String apiId,List<ApplicationSubcriptionsDTO> applicationSubcriptionsRateList) throws BusinessException{
		 
		ApplicationSubcriptionsDAO applicationSubcriptionsDAO=new ApplicationSubcriptionsDAO();
		List<ApplicationSubcriptionsDTO> applicationSubcriptionsDTOs = null;
		List<ApplicationSubcriptionsDTO> updatedList = new ArrayList<ApplicationSubcriptionsDTO>();;
		applicationSubcriptionsDTOs=applicationSubcriptionsDAO.getSBRates(appId, operatorId, apiId);
				
		for (ApplicationSubcriptionsDTO applicationSubcriptionsDTODB:applicationSubcriptionsDTOs) {
			for (ApplicationSubcriptionsDTO applicationSubcriptionsDTO:applicationSubcriptionsRateList) {


				if (applicationSubcriptionsDTO.getApiOperation().equals(applicationSubcriptionsDTODB.getApiOperation()) &&
					applicationSubcriptionsDTO.getApplicationId().equals(applicationSubcriptionsDTODB.getApplicationId()) &&
					applicationSubcriptionsDTO.getOperatorId().equals(applicationSubcriptionsDTODB.getOperatorId()) ) 
				{	
					ApplicationSubcriptionsDTO ubdatedDTO=applicationSubcriptionsDAO.updateSBRates(applicationSubcriptionsDTO);
					applicationSubcriptionsDAO.insertUpdatedSBRates(applicationSubcriptionsDTODB);
					updatedList.add(ubdatedDTO);
				} else {
					ApplicationSubcriptionsDTO ubdatedDTO=applicationSubcriptionsDAO.insertSBRates(applicationSubcriptionsDTO);
					updatedList.add(ubdatedDTO);
				}
			}

		}
		return updatedList;
	}
	
	
	public List<ApplicationSubcriptionsDTO> updateNBSubscriptionRateInfo(String appId,String apiId,List<ApplicationSubcriptionsDTO> applicationSubcriptionsRateList) throws BusinessException{
		 
		ApplicationSubcriptionsDAO applicationSubcriptionsDAO=new ApplicationSubcriptionsDAO();
		List<ApplicationSubcriptionsDTO> applicationSubcriptionsDTOs = null;
		List<ApplicationSubcriptionsDTO> updatedList = new ArrayList<ApplicationSubcriptionsDTO>();;
		applicationSubcriptionsDTOs=applicationSubcriptionsDAO.getNBRates(appId, apiId);
		
		for (ApplicationSubcriptionsDTO applicationSubcriptionsDTODB:applicationSubcriptionsDTOs) {
			for (ApplicationSubcriptionsDTO applicationSubcriptionsDTO:applicationSubcriptionsRateList) {


				if (applicationSubcriptionsDTO.getApiOperation().equals(applicationSubcriptionsDTODB.getApiOperation()) &&
					applicationSubcriptionsDTO.getApplicationId().equals(applicationSubcriptionsDTODB.getApplicationId()) ) 
				{	
					ApplicationSubcriptionsDTO ubdatedDTO=applicationSubcriptionsDAO.updateNBRates(applicationSubcriptionsDTO);
					updatedList.add(ubdatedDTO);
					applicationSubcriptionsDAO.insertUpdatedNBRates(applicationSubcriptionsDTODB);
				} else {
					ApplicationSubcriptionsDTO ubdatedDTO=applicationSubcriptionsDAO.insertNBRates(applicationSubcriptionsDTO);
					updatedList.add(ubdatedDTO);
				}
			}

		}
		return updatedList;
	}
}

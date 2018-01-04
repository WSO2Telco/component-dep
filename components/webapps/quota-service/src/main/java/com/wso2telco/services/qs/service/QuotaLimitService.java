package com.wso2telco.services.qs.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.usage.client.exception.APIMgtUsageQueryServiceClientException;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.GenaralError;
import com.wso2telco.core.userprofile.cache.CacheFactory;
import com.wso2telco.core.userprofile.cache.UserProfileCachable;
import com.wso2telco.core.userprofile.dto.UserProfileDTO;
import com.wso2telco.core.userprofile.util.CacheType;
import com.wso2telco.dep.qs.util.QuotaLimitException;
import com.wso2telco.services.qs.dao.QuotaLimitDao;
import com.wso2telco.services.qs.entity.QuotaBean;

import edu.emory.mathcs.backport.java.util.Arrays;

public class QuotaLimitService {

	Log LOG = LogFactory.getLog(QuotaLimitService.class);
	private QuotaLimitDao dao;

	{
		dao = new QuotaLimitDao();
	}

	@SuppressWarnings("unchecked")
	public void addQuotaLimit(QuotaBean quotaBean, String sessionId) throws BusinessException {
		
		try {
			
			UserProfileCachable cachable = CacheFactory.getInstance(CacheType.LOCAL).getService();
			UserProfileDTO userProfileDTO = cachable.get(sessionId);
			String[] roles = userProfileDTO.getUserRoles();
			List <String> roleList = new ArrayList<>(Arrays.asList(roles));
			
			if(roleList.contains("operator/admin")){
				
				quotaBean.setOperator(userProfileDTO.getOrganization());
			}
			
			dao.addQuotaLimit(quotaBean);
		} catch (Exception exception) {
			
			LOG.error("error in addQuotaLimit", exception);
			throw new QuotaLimitException(GenaralError.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings("unchecked")
	public List<QuotaBean> getQuotaLimitInfo(String byFlag, String info, String operator, String sessionId) throws QuotaLimitException{
		
		List<QuotaBean> returnObjList=new ArrayList<>();
		
		try {
			
			UserProfileCachable cachable = CacheFactory.getInstance(CacheType.LOCAL).getService();
			UserProfileDTO userProfileDTO = cachable.get(sessionId);
			String[] roles = userProfileDTO.getUserRoles();
			List <String> roleList = new ArrayList<>(Arrays.asList(roles));
			
			if(roleList.contains("operator/admin")){
				
				operator = userProfileDTO.getOrganization();
			}
			
			switch (byFlag) {
			case "byServiceProvider":
				returnObjList= dao.getQuotaLimitInfoByServiceProvider(info, operator);
				break;
			case "byApplication":
				returnObjList= dao.getQuotaLimitInfoByApplication(info, operator);
				break;
			case "byApi":
				returnObjList= dao.getQuotaLimitInfoByApi(info, operator);
				break;
			default:
				break;
			}
			
			return returnObjList;
		} catch (Exception exception) {
			
			LOG.error("error in addQuotaLimit", exception);
			throw new QuotaLimitException(GenaralError.INTERNAL_SERVER_ERROR);
		}
	}
	
	@SuppressWarnings("unchecked")
	public Boolean checkIfDatesOverlap(String byFlag, String info,String fromDate, String toDate, String operator, String sessionId) throws QuotaLimitException{
		
		Boolean checkIfDatesOverlap=false;
		
		try {
			
			UserProfileCachable cachable = CacheFactory.getInstance(CacheType.LOCAL).getService();
			UserProfileDTO userProfileDTO = cachable.get(sessionId);
			String[] roles = userProfileDTO.getUserRoles();
			List <String> roleList = new ArrayList<>(Arrays.asList(roles));
			
			if(roleList.contains("operator/admin")){
				
				operator = userProfileDTO.getOrganization();
			}
			
			switch (byFlag) {
			case "byServiceProvider":
				checkIfDatesOverlap= dao.checkQuotaLimitInfoByServiceProviderWithDateRange(info,fromDate,toDate,operator);
				break;
			case "byApplication":
				checkIfDatesOverlap= dao.checkQuotaLimitInfoByApplicationWithDateRange(info,fromDate,toDate,operator);
				break;
			case "byApi":
				checkIfDatesOverlap= dao.checkQuotaLimitInfoByApiWithDateRange(info,fromDate,toDate,operator);
				break;
			default:
				break;
			}
			
		} catch (Exception exception) {
			
			LOG.error("error in addQuotaLimit", exception);
			throw new QuotaLimitException(GenaralError.INTERNAL_SERVER_ERROR);
		}
		
		return checkIfDatesOverlap;
	}

	public  static List<String> getOperatorNamesByApplication(int applicationId) throws Exception, SQLException {
		return QuotaLimitDao.getOperatorNamesByApplication(applicationId);
	}

	public  static List<Integer> getApplicationsByOperator(String operatorName) throws Exception, SQLException {
		return QuotaLimitDao.getApplicationsByOperator(operatorName);
	}

	public static List<String> getAllSubscribers() {
		List<String> subscriptions = QuotaLimitDao.getAllSubscribers();
		Collections.sort(subscriptions, String.CASE_INSENSITIVE_ORDER);
		return subscriptions;

	}




}

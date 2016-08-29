package com.wso2telco.dep.publisheventsdata.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.dep.publisheventsdata.dao.SpendLimitDAO;
import com.wso2telco.dep.publisheventsdata.util.ErrorType;
import com.wso2telco.utils.exception.BusinessException;
import com.wso2telco.utils.exception.GenaralError;

public class SpendLimitService {

		/** The Constant log. */
		private final Log log = LogFactory.getLog(SpendLimitService.class);

		SpendLimitDAO spendlimitDAO;

		{
			spendlimitDAO = new SpendLimitDAO();
		}
		
		
		public Double getGroupTotalMonthAmount(String groupName, String operator, String msisdn) throws Exception {
		
			if (groupName == null || groupName.trim().length() <= 0) {

				throw new BusinessException(ErrorType.INVALID_GROUP_NAME);
			}
			if (operator == null || operator.trim().length() <= 0) {

				throw new BusinessException(ErrorType.INVALID_OPERATOR);
			}
			if (msisdn == null || msisdn.trim().length() <= 0) {

				throw new BusinessException(ErrorType.INVALID_MSISDN);
			}
			
	        Double groupTotalMonthAmount = 0.0;

			try {

				groupTotalMonthAmount = spendlimitDAO.getGroupTotalMonthAmount(groupName, operator, msisdn);
			} catch (Exception e) {

				throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
			}

			return groupTotalMonthAmount;
			
		}
		
		public Double getGroupTotalDayAmount(String groupName, String operator, String msisdn) throws Exception {

			if (groupName == null || groupName.trim().length() <= 0) {

				throw new BusinessException(ErrorType.INVALID_GROUP_NAME);
			}
			if (operator == null || operator.trim().length() <= 0) {

				throw new BusinessException(ErrorType.INVALID_OPERATOR);
			}
			if (msisdn == null || msisdn.trim().length() <= 0) {

				throw new BusinessException(ErrorType.INVALID_MSISDN);
			}
			
	        Double groupTotalDayAmount = 0.0;

			try {

				groupTotalDayAmount = spendlimitDAO.getGroupTotalDayAmount(groupName, operator, msisdn);
			} catch (Exception e) {

				throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
			}

			return groupTotalDayAmount;
			
		}
}

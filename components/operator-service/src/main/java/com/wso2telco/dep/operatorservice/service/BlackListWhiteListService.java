package com.wso2telco.dep.operatorservice.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.GenaralError;
import com.wso2telco.core.msisdnvalidator.InvalidMSISDNException;
import com.wso2telco.core.msisdnvalidator.MSISDN;
import com.wso2telco.core.msisdnvalidator.MSISDNUtil;
import com.wso2telco.dep.operatorservice.dao.BlackListWhiteListDAO;
import com.wso2telco.dep.operatorservice.exception.NumberBlackListException;
import com.wso2telco.dep.operatorservice.exception.SubscriptionWhiteListException;
import com.wso2telco.dep.operatorservice.exception.SubscriptionWhiteListException.SubscriptionWhiteListErrorType;
import com.wso2telco.dep.operatorservice.model.BlackListDTO;
import com.wso2telco.dep.operatorservice.model.MSISDNSearchDTO;
import com.wso2telco.dep.operatorservice.model.WhiteListDTO;
import com.wso2telco.dep.operatorservice.model.WhiteListMSISDNSearchDTO;

public class BlackListWhiteListService {
	Log LOG = LogFactory.getLog(BlackListWhiteListService.class);
	private BlackListWhiteListDAO dao;

	private MSISDNUtil phoneNumberValidationUtil_;
	{
		dao = new BlackListWhiteListDAO();
		phoneNumberValidationUtil_ = new MSISDNUtil();
	}

	/**
	 * validate the MSISDN s and black list . The MSISDN need to provided with +
	 * sign if not all the process fails. The entire process run as a single
	 * transaction .
	 */
	public void blacklist(BlackListDTO dto) throws BusinessException {

		String[] msisdns = dto.getUserMSISDN();
		List<MSISDN> numberA = new ArrayList<MSISDN>();
		MSISDNSearchDTO mSISDNSearchDTO = new MSISDNSearchDTO();
		try {
		for (String msisdn : msisdns) {

			MSISDN msisdnDTO = phoneNumberValidationUtil_.parse(msisdn);
			numberA.add(msisdnDTO);
			mSISDNSearchDTO.addMSISDN2Search(msisdnDTO);
		}

		final String apiID_ = dto.hasValidApiID() ? dto.getApiID().trim() : null;
		final String apiName_ = dto.getApiName();
		final String userId_ = dto.getUserID();

		// load already black listed numbers
		mSISDNSearchDTO.setApiID(apiID_);
		List<MSISDN> alreadyBlacklisted;
	
			alreadyBlacklisted = dao.loadAlreadyBlacklisted(mSISDNSearchDTO);

			// Remove already black listed from the list
			for (MSISDN msisdn : alreadyBlacklisted) {
				numberA.remove(msisdn);
			}

			if (numberA.isEmpty()) {
				LOG.debug(" All the numbers already black listed");
				return;
			}
			dao.blacklist(numberA, apiID_, apiName_, userId_);
		} catch (Exception e) {
			LOG.error("blacklist ", e);
			throw new BusinessException(GenaralError.INTERNAL_SERVER_ERROR);
		}
	}

	public String[] loadBlacklisted(MSISDNSearchDTO searchDTO) throws BusinessException {
		try {
			return dao.getBlacklisted(searchDTO);
		} catch (Exception e) {
			LOG.error("loadBlacklisted", e);
			throw new NumberBlackListException(GenaralError.INTERNAL_SERVER_ERROR);
		}
	}

	public void removeBlacklist(final String apiName, final String userMSISDN) throws BusinessException {
		try {
			dao.removeBlacklist(apiName, userMSISDN);
		} catch (Exception e) {
			LOG.error("removeBlacklist", e);
			throw new NumberBlackListException(GenaralError.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * remove already white listed msisdns from the list and white list the
	 * subscription
	 * 
	 * @param whiteListDTO
	 * @throws BusinessException
	 */
	public void whiteListSubscription(WhiteListDTO whiteListDTO) throws InvalidMSISDNException,BusinessException {
		String[] msisdns = whiteListDTO.getUserMSISDN();
		List<MSISDN> numberA = new ArrayList<MSISDN>();
		WhiteListMSISDNSearchDTO mSISDNSearchDTO = new WhiteListMSISDNSearchDTO();

		for (String msisdn : msisdns) {

			MSISDN msisdnDTO;
				msisdnDTO = phoneNumberValidationUtil_.parse(msisdn);
			numberA.add(msisdnDTO);
			mSISDNSearchDTO.addMSISDN2Search(msisdnDTO);
		}

		final String apiID = whiteListDTO.hasValidApiID() ? whiteListDTO.getApiID() : null;
		String subscriptionID = whiteListDTO.hasValidSubscriptionID() ? whiteListDTO.getSubscriptionID() : null;
		final String applicationID = whiteListDTO.hasValidApplicationID() ? whiteListDTO.getApplicationID() : null;

		// if no subscription provided
		// subscription id derived using applicationid and using api id
		try {
			if (whiteListDTO.hasValidSubscriptionID()) {
				subscriptionID = dao.findSubscriptionId(applicationID, apiID);
			}
			if (subscriptionID == null || subscriptionID.trim().length() <= 0) {
				throw new SubscriptionWhiteListException(SubscriptionWhiteListErrorType.NULL_SUBSCRIPTION);
			}
		} catch (Exception e1) {
			throw new SubscriptionWhiteListException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		// check the input stream for already white listed numbers
		mSISDNSearchDTO.setApiID(apiID);
		List<MSISDN> alreadyWhilteListed;
		try {
			alreadyWhilteListed = dao.loadSubScriptionWhiteListed(mSISDNSearchDTO);
			for (MSISDN msisdn : alreadyWhilteListed) {
				numberA.remove(msisdn);
			}
		} catch (SQLException e) {
			LOG.error("whiteListSubscription, calling dao.loadSubScriptionWhiteListed(", e);
			throw new SubscriptionWhiteListException(GenaralError.INTERNAL_SERVER_ERROR);
		}

		// All numbers already white listed then throw exception
		if (numberA.isEmpty()) {
			LOG.debug(" All the numbers already black listed");
			throw new SubscriptionWhiteListException(SubscriptionWhiteListErrorType.SUBSCRIPTION_ALREADY_WHITELISTED);
		}
		// persistence goes hare
		try {
			dao.whitelist(numberA, subscriptionID, apiID, applicationID);

		} catch (Exception e) {
			LOG.error("whiteListSubscription. caling dao.whitelist", e);
			throw new SubscriptionWhiteListException(GenaralError.INTERNAL_SERVER_ERROR);
		}

	}

	public void removeWhitelistNumber(String userMSISDN) throws BusinessException {

		try {
			dao.removeWhitelistNumber(userMSISDN);
		} catch (Exception e) {
			LOG.error("removeWhitelistNumber", e);
			throw new SubscriptionWhiteListException(GenaralError.INTERNAL_SERVER_ERROR);
		}
	}

	public String[] getWhiteListNumbers() throws BusinessException {
		try {
			List<String> result = dao.getWhiteListNumbers();
			if (result == null || result.isEmpty()) {
				return null;
			}
			return result.toArray(new String[result.size()]);
		} catch (Exception e) {
			LOG.error("getWhiteListNumbers", e);
			throw new SubscriptionWhiteListException(GenaralError.INTERNAL_SERVER_ERROR);
		}
	}
}

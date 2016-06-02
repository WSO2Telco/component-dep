package com.wso2telco.dep.operatorservice.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wso2telco.dbutils.AxataDBUtilException;
import com.wso2telco.dep.operatorservice.dao.OperatorDAO;
import com.wso2telco.dep.operatorservice.model.Operator;
import com.wso2telco.dep.operatorservice.model.OperatorSearchDTO;
import com.wso2telco.dep.operatorservice.model.ProvisionReq;
import com.wso2telco.dep.operatorservice.util.OparatorError;
import com.wso2telco.utils.exception.BusinessException;
import com.wso2telco.dep.operatorservice.model.OperatorEndPointDTO;
import com.wso2telco.dep.operatorservice.model.OperatorApplicationDTO;

public class OparatorService {
	Log LOG = LogFactory.getLog(OparatorService.class);
	OperatorDAO dao;

	{
		dao = new OperatorDAO();
	}

	/**
	 * load all operators according to OperatorSearchDTO filters
	 * 
	 * @param searchDTO
	 * @return
	 * @throws Exception
	 */
	public List<Operator> loadOperators(OperatorSearchDTO searchDTO) throws Exception {
		LOG.debug(" Got request to loadOperators  searchDTO :" + searchDTO);
		dao.seachOparators(searchDTO);
		return dao.seachOparators(searchDTO);
	}

	public void blacklistAggregator(ProvisionReq provisionreq) throws BusinessException {

		Integer applicationid = (provisionreq.getProvisioninfo().getApplicationid() != null)
				? Integer.parseInt(provisionreq.getProvisioninfo().getApplicationid()) : null;

		String subscriber = provisionreq.getProvisioninfo().getSubscriber();
		String operator = provisionreq.getProvisioninfo().getOperatorcode();
		String[] merchants = (String[]) (provisionreq.getProvisioninfo().getMerchantcode())
				.toArray(new String[(provisionreq.getProvisioninfo().getMerchantcode()).size()]);

		LOG.debug(" applicationid :" + applicationid + " subscriber:" + subscriber + " operator:" + operator
				+ " merchants :" + StringUtils.join(merchants));
		/**
		 * response created
		 */
		try {
			/**
			 * check for macing operator for given operator name
			 */
			OperatorSearchDTO searchDTO = new OperatorSearchDTO();
			searchDTO.setName(operator);
			List<Operator> oparators = dao.seachOparators(searchDTO);

			/**
			 * No operators found :return
			 */
			if (oparators == null || oparators.isEmpty()) {
				throw new BusinessException(OparatorError.INVALID_OPARATOR_NAME);
			}

			/**
			 * if operators available first one will taken out
			 */
			Operator oparator = oparators.get(0);

			dao.insertBlacklistAggregatoRows(applicationid, subscriber, oparator.getOperatorId(), merchants);
		} catch (Exception e) {
			LOG.error("blacklistAggregator", e);
			throw new BusinessException(OparatorError.INVALID_OPARATOR_NAME);

		}

	}

	public List<OperatorEndPointDTO> getOperatorEndpoints() throws Exception {

		return dao.getOperatorEndpoints();
	}

	public List<OperatorApplicationDTO> getApplicationOperators(Integer applicationId) throws Exception {

		return dao.getApplicationOperators(applicationId);
	}

	public List<Integer> getActiveApplicationOperators(Integer appId, String apiType)
			throws SQLException, AxataDBUtilException {

		return dao.getActiveApplicationOperators(appId, apiType);
	}

	public Integer updateOperatorToken(int id, String refreshToken, long tokenValidity, long tokenTime, String token)
			throws Exception {

		return dao.updateOperatorToken(id, refreshToken, tokenValidity, tokenTime, token);
	}
}

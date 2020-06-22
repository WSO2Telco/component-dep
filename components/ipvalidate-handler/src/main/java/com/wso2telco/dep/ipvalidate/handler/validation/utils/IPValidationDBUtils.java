package com.wso2telco.dep.ipvalidate.handler.validation.utils;

import java.util.ArrayList;
import java.util.List;

import com.wso2telco.dep.ipvalidate.handler.validation.dao.IPValidationDao;
import com.wso2telco.dep.ipvalidate.handler.validation.dto.ClientIPSummary;
import com.wso2telco.dep.ipvalidate.handler.validation.dto.ClientKeyIPData;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IPValidationDBUtils {

	private static final Log log = LogFactory.getLog(IPValidationDBUtils.class);

	private IPValidationDao ipValidationdao;

	public IPValidationDBUtils() {
		ipValidationdao = new IPValidationDao();
	}

	public ArrayList<String> getClientKeyList() throws Exception {
		log.debug("Get client key list");
		ArrayList<String> clientKeyList = new ArrayList<String>();
		try {
			clientKeyList = ipValidationdao.getClientIdList();
		} catch (Exception e) {
			log.error("error while getting client key list " + e);
			throw e;
		}
		return clientKeyList;
	}

	public List<ClientKeyIPData> getValidIPListForClient(String clientId) throws Exception {
		log.debug("Get valid IP list for client : " + clientId);
		log.info("Get valid IP list for client : " + clientId);
		List<ClientKeyIPData> clientIPDataList = new ArrayList<ClientKeyIPData>();
		try {
			List<ClientIPSummary> summaryIdList = ipValidationdao.getSumaryListForClient(clientId);
			
			for (ClientIPSummary clientIPSummary : summaryIdList) {				
				ClientKeyIPData clientKeyIPData = new ClientKeyIPData();

				clientKeyIPData.setClientId(clientId);
				clientKeyIPData.setSummaryId(clientIPSummary.getSummaryId());
				clientKeyIPData.setValidationEnabled(clientIPSummary.isValidationEnabled());
				clientKeyIPData.setClientToken(clientIPSummary.getClientToken());
				clientKeyIPData.setPoolIpList(ipValidationdao.getPoolIPListBySummaryId(clientIPSummary.getSummaryId()));
				clientKeyIPData
						.setRangeIpList(ipValidationdao.getRangeIPListBySummaryId(clientIPSummary.getSummaryId()));
								
				clientIPDataList.add(clientKeyIPData);
			}
		} catch (Exception e) {
			log.error("error while valid IP list for client " + e);
			throw e;
		}
		
		return clientIPDataList;
	}

}

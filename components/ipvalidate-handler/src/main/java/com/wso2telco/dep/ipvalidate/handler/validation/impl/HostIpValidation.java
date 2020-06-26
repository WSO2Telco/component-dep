package com.wso2telco.dep.ipvalidate.handler.validation.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityConstants;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.wso2telco.dep.ipvalidate.handler.validation.CustomValidator;
import com.wso2telco.dep.ipvalidate.handler.validation.configuration.IPValidationProperties;
import com.wso2telco.dep.ipvalidate.handler.validation.dto.ClientIPPool;
import com.wso2telco.dep.ipvalidate.handler.validation.dto.ClientIPRange;
import com.wso2telco.dep.ipvalidate.handler.validation.dto.ClientKeyIPData;
import com.wso2telco.dep.ipvalidate.handler.validation.dto.RequestData;
import com.wso2telco.dep.ipvalidate.handler.validation.service.ValidationCacheService;

public class HostIpValidation extends CustomValidator {

	private static final Log log = LogFactory.getLog(HostIpValidation.class);

	@Override
	public boolean doValidation(RequestData requestData) throws APISecurityException {
		log.debug("Host IP validation : " + requestData);
		boolean status = false;

		try {
			List<ClientKeyIPData> clientIpSummaryList = ValidationCacheService.getCache()
					.get(requestData.getClientkey());

			if (clientIpSummaryList == null) {
				ValidationCacheService.loadCache();
				clientIpSummaryList = ValidationCacheService.getCache().get(requestData.getClientkey());
			}

			for (ClientKeyIPData clientIpSummary : clientIpSummaryList) {
				ClientIPPool clientIPPool = clientIpSummary.getPoolIpList();

				List<ClientIPRange> clientIPRangeList = clientIpSummary.getRangeIpList();
				if (isValidPoolIP(clientIPPool.getIp(), requestData.getHostip())) {
					status = true;
					break;
				} else if (isIPinValidRange(clientIPRangeList, requestData.getHostip())) {
					status = true;
					break;
				}
			}

			if (status && nextValidator != null) {
				nextValidator.doValidation(requestData);
			}

		} catch (APISecurityException e) {
			throw e;
		} catch (Exception e) {
			log.error(e);
			throw new APISecurityException(IPValidationProperties.getInvalidHostErrCode(),
					IPValidationProperties.getInvalidHostErrMsg());
		}
		return status;
	}

	public boolean isValidPoolIP(ArrayList<String> strings, String searchString) {
		log.debug("Check is valid pool IP :" + strings + " ; " + searchString);
		log.info("Check is valid pool IP :" + strings + " ; " + searchString);
		boolean isIPValid = false;
		if (strings.contains(searchString)) {
			isIPValid = true;
		}
		return isIPValid;
	}

	public boolean isIPinValidRange(List<ClientIPRange> clientIPRangeList, String ip) {
		log.debug("Check is isIPinValidRange :" + clientIPRangeList + " , " + ip + " ; ");
		log.info("Check isIPinValidRange :" + clientIPRangeList + " , " + ip + " ; ");
		boolean isIPValid = false;
		for (ClientIPRange clientIpRange : clientIPRangeList) {
			log.info("Check clientIpRange :" + clientIpRange);
			if (isValidRangeIP(clientIpRange.getStartIP(), clientIpRange.getEndIP(), ip)) {
				isIPValid = true;
				break;
			}
		}
		return isIPValid;
	}

	public static boolean isValidRangeIP(String ipStart, String ipEnd, String ipToCheck) {

		log.debug("Check is valid range IP :" + ipStart + " , " + ipEnd + " ; " + ipToCheck);
		log.info("Check is valid range IP :" + ipStart + " , " + ipEnd + " ; " + ipToCheck);
		try {
			long ipLo = ipToLong(InetAddress.getByName(ipStart));
			long ipHi = ipToLong(InetAddress.getByName(ipEnd));
			long ipToTest = ipToLong(InetAddress.getByName(ipToCheck));
			return (ipToTest >= ipLo && ipToTest <= ipHi);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static long ipToLong(InetAddress ip) {
		byte[] octets = ip.getAddress();
		long result = 0;
		for (byte octet : octets) {
			result <<= 8;
			result |= octet & 0xff;
		}
		return result;
	}

}

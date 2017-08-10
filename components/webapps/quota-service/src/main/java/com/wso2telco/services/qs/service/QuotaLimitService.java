package com.wso2telco.services.qs.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.regexp.recompile;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.GenaralError;
import com.wso2telco.dep.qs.util.QuotaLimitException;
import com.wso2telco.services.qs.dao.QuotaLimitDao;
import com.wso2telco.services.qs.entity.QuotaBean;

public class QuotaLimitService {

	Log LOG = LogFactory.getLog(QuotaLimitService.class);
	private QuotaLimitDao dao;

	{
		dao = new QuotaLimitDao();
	}

	public void addQuotaLimit(QuotaBean quotaBean) throws BusinessException {
		try {
			dao.addQuotaLimit(quotaBean);
		} catch (Exception exception) {
			LOG.error("error in addQuotaLimit", exception);
			throw new QuotaLimitException(GenaralError.INTERNAL_SERVER_ERROR);
		}
	}

	public List<QuotaBean> getQuotaLimitInfo(String byFlag, String info, Date fromDate, Date toDate) throws QuotaLimitException{
		List<QuotaBean> returnObjList=new ArrayList<QuotaBean>();
		try {
			switch (byFlag) {
			case "byServiceProvider":
				returnObjList= dao.getQuotaLimitInfoByServiceProvider(info);
				break;
			case "byApplication":
				returnObjList= dao.getQuotaLimitInfoByApplication(info);
				break;
			case "byApi":
				returnObjList= dao.getQuotaLimitInfoByApi(info);
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

}

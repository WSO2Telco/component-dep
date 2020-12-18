package com.wso2telco.workflow.service.admin.factory;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.workflow.dao.ApplicationDAO;
import com.wso2telco.workflow.model.*;
import com.wso2telco.workflow.utils.Constants;

import java.sql.SQLException;

public class TierUpdtReqestFactory<T extends BaseTierUpdtReq,K extends BaseEditDTO> {

    ApplicationDAO applicationDAO;

    {
        applicationDAO = new ApplicationDAO();
    }

    public T getTierUpdateRequest(K k) throws SQLException, BusinessException{

        T t = null;

        if(k instanceof ApplicationEditDTO){
            AppTierUpdtReq tierUpdtReq = new AppTierUpdtReq();
            populateApplicationTierUptd(tierUpdtReq,(ApplicationEditDTO)k);
            t = (T)tierUpdtReq;
        } else if(k instanceof SubscriptionEditDTO) {
            //TODO: Implement Subscription flow
            SubTierUpdtReq tierUpdtReq = new SubTierUpdtReq();
        }

        return t;
    }

    private void populateApplicationTierUptd(AppTierUpdtReq updtReq,ApplicationEditDTO applicationEditDTO) throws SQLException, BusinessException {
        String UUID = applicationDAO.getUUID(applicationEditDTO.getApplicationId());
        updtReq.setApplicationId(UUID);
        updtReq.setName(applicationEditDTO.getApplicationName());
        if(applicationEditDTO.getTokenType() == null || applicationEditDTO.getTokenType().equals("")){
            updtReq.setTokenType(Constants.TOKEN_TYPE_JWT);
        }else{
            updtReq.setTokenType(applicationEditDTO.getTokenType());
        }

        updtReq.setThrottlingPolicy(applicationEditDTO.getApplicationTier());
    }

}

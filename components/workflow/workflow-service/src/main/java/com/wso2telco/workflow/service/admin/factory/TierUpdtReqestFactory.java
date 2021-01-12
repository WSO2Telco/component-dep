package com.wso2telco.workflow.service.admin.factory;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.workflow.dao.ApplicationDAO;
import com.wso2telco.workflow.dao.SubscriptionDAO;
import com.wso2telco.workflow.model.*;
import com.wso2telco.workflow.utils.Constants;

import org.wso2.carbon.apimgt.api.APIConsumer;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.model.API;
import org.wso2.carbon.apimgt.api.model.APIProduct;
import org.wso2.carbon.apimgt.api.model.APIProductIdentifier;
import org.wso2.carbon.apimgt.api.model.SubscribedAPI;
import org.wso2.carbon.apimgt.rest.api.util.RestApiConstants;
import org.wso2.carbon.apimgt.rest.api.util.utils.RestAPIStoreUtils;
import org.wso2.carbon.apimgt.rest.api.util.utils.RestApiUtil;
import org.wso2.carbon.apimgt.api.model.APIIdentifier;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.utils.multitenancy.MultitenantConstants;

import java.sql.SQLException;

public class TierUpdtReqestFactory<T extends BaseTierUpdtReq,K extends BaseEditDTO> {

    private static final Log log = LogFactory.getLog(TierUpdtReqestFactory.class);

    ApplicationDAO applicationDAO;
    SubscriptionDAO subscriptionDAO;

    {
        applicationDAO = new ApplicationDAO();
        subscriptionDAO = new SubscriptionDAO();

    }

    public T getTierUpdateRequest(K k) throws SQLException, BusinessException, APIManagementException{

        T t = null;

        if(k instanceof ApplicationEditDTO){
            AppTierUpdtReq tierUpdtReq = new AppTierUpdtReq();
            populateApplicationTierUptd(tierUpdtReq,(ApplicationEditDTO)k);
            t = (T)tierUpdtReq;
        } else if(k instanceof SubscriptionEditDTO) {
            SubTierUpdtReq tierUpdtReq = populateSubscriptionTierUptd((SubscriptionEditDTO)k);
            t = (T)tierUpdtReq;
        }

        return t;
    }

    private void populateApplicationTierUptd(AppTierUpdtReq updtReq, ApplicationEditDTO applicationEditDTO) throws SQLException, BusinessException {
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

    private SubTierUpdtReq populateSubscriptionTierUptd(SubscriptionEditDTO subscriptionEditDTO) throws SQLException, BusinessException, APIManagementException {

        APIConsumer apiConsumer;
        SubTierUpdtReq subTierUpdtReq;

        try {
            String username = subscriptionEditDTO.getServiceProvider();
            PrivilegedCarbonContext.startTenantFlow();
            PrivilegedCarbonContext privilegedCarbonContext = PrivilegedCarbonContext.getThreadLocalCarbonContext();
            privilegedCarbonContext.setTenantId(MultitenantConstants.SUPER_TENANT_ID);
            privilegedCarbonContext.setTenantDomain(MultitenantConstants.SUPER_TENANT_DOMAIN_NAME);
            privilegedCarbonContext.setUsername(username);

            String subscriptionUUID = subscriptionDAO.getUUID(subscriptionEditDTO.getApplicationId(), subscriptionEditDTO.getApiID());

            apiConsumer = RestApiUtil.getConsumer(username);
            SubscribedAPI subscribedAPI = getSubscritionDetails(subscriptionUUID, apiConsumer);
            subTierUpdtReq = fromSubscriptionToDTO(subscribedAPI, subscriptionEditDTO);
        } finally {
            PrivilegedCarbonContext.endTenantFlow();
        }

        return subTierUpdtReq;

    }

    private SubscribedAPI getSubscritionDetails (String subscriptionId, APIConsumer apiConsumer) throws APIManagementException {
        SubscribedAPI subscribedAPI = apiConsumer.getSubscriptionByUUID(subscriptionId);
        if (subscribedAPI == null) {
            RestApiUtil.handleResourceNotFoundError(RestApiConstants.RESOURCE_SUBSCRIPTION, subscriptionId, log);
        }
        if (!RestAPIStoreUtils.isUserAccessAllowedForSubscription(subscribedAPI)) {
            RestApiUtil.handleAuthorizationFailure(RestApiConstants.RESOURCE_SUBSCRIPTION, subscriptionId, log);
        }
        return subscribedAPI;
    }

    private SubTierUpdtReq fromSubscriptionToDTO(SubscribedAPI subscription, SubscriptionEditDTO subscriptionEditDTO)
            throws APIManagementException{
        SubTierUpdtReq subTierUpdtReq = new SubTierUpdtReq();
        getApiInfo(subscription, subTierUpdtReq);

        subTierUpdtReq.setSubscriptionId(subscription.getUUID());
        subTierUpdtReq.setApplicationId(subscription.getApplication().getUUID());
        subTierUpdtReq.setStatus(
                SubTierUpdtReq.SubscriptionStatusEnum.valueOf(subscription.getSubStatus()));
        subTierUpdtReq.setThrottlingPolicy(subscription.getTier().getName());
        subTierUpdtReq.setRequestedThrottlingPolicy(subscriptionEditDTO.getSubscriptionTier());

        return subTierUpdtReq;
    }

    private void getApiInfo(SubscribedAPI subscription, SubTierUpdtReq subTierUpdtReq)
            throws APIManagementException {
        APIConsumer apiConsumer = RestApiUtil.getLoggedInUserConsumer();
        //SubTierUpdtReq subTierUpdtReq = new SubTierUpdtReq();
        subTierUpdtReq.setSubscriptionId(subscription.getUUID());
        APIIdentifier apiId = subscription.getApiId();
        APIProductIdentifier apiProdId = subscription.getProductId();
        if (apiId != null) {
            API api = apiConsumer.getLightweightAPI(apiId);
            subTierUpdtReq.setApiId(api.getUUID());
        }
        if (apiProdId != null) {
            APIProduct apiProduct = apiConsumer.getAPIProduct(apiProdId);
            subTierUpdtReq.setApiId(apiProduct.getUuid());
        }
    }
}

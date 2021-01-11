package com.wso2telco.workflow.service.admin.factory;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.workflow.model.*;
import com.wso2telco.workflow.utils.Constants;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.APIManagerConfiguration;
import org.wso2.carbon.apimgt.impl.internal.ServiceReferenceHolder;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.sql.SQLException;
import java.util.Base64;

public class TierUpdtConnFactory<T extends TierUpdtConnDTO,K extends BaseTierUpdtReq>  {

    private String serverEP;
    private String userName;
    private String password;

    {
        readSystemProperties();
    }

    public T getTierUpdateRequest(K k) throws SQLException, BusinessException {

        T t = null;

        if(k instanceof AppTierUpdtReq){
            AppTierUpdtReq tierUpdtReq = (AppTierUpdtReq)k;
            TierUpdtConnDTO tierUpdtConnDTO = new TierUpdtConnDTO();
            tierUpdtConnDTO.setServerEP(getServerEP());
            tierUpdtConnDTO.setPath(Constants.REMOTE_ADMIN_APP_TIER_UPDT_PATH+tierUpdtReq.getApplicationId());
            tierUpdtConnDTO.setHeaders(getSecurityHeaders());
            tierUpdtConnDTO.setMediaType(MediaType.APPLICATION_JSON_TYPE);
            t = (T) tierUpdtConnDTO;
        } else if(k instanceof SubTierUpdtReq) {
            SubTierUpdtReq subTierUpdtReq = (SubTierUpdtReq) k;
            TierUpdtConnDTO tierUpdtConnDTO = new TierUpdtConnDTO();
            tierUpdtConnDTO.setServerEP(getServerEP());
            tierUpdtConnDTO.setPath(Constants.REMOTE_ADMIN_SUB_TIER_UPDATE_PATH + subTierUpdtReq.getSubscriptionId());
            tierUpdtConnDTO.setHeaders(getSecurityHeaders());
            tierUpdtConnDTO.setMediaType(MediaType.APPLICATION_JSON_TYPE);
            t = (T)tierUpdtConnDTO;
        }

        return t;
    }

    private MultivaluedMap getSecurityHeaders() {
        MultivaluedMap<String,String> headers = new MultivaluedStringMap();
        headers.add(Constants.SECURITY_AUTHORIZATION_HEADER,Constants.SECURITY_AUTHORIZATION_BASIC+" "+getBase64ENUserPass());
        return headers;
    }

    private String getBase64ENUserPass(){
        StringBuilder buildUser = new StringBuilder();
        return Base64.getEncoder().encodeToString(buildUser.append(this.userName).append(":").append(this.password).toString().getBytes());
    }

    private String getUserName(){
        return userName;
    }

    private String getPassword(){
        return password;
    }

    private String getServerEP() {
        return serverEP;
    }

    private void readSystemProperties() {

        APIManagerConfiguration config = ServiceReferenceHolder.getInstance()
                .getAPIManagerConfigurationService()
                .getAPIManagerConfiguration();

        this.serverEP = config.getFirstProperty(APIConstants.AUTH_MANAGER_URL);
        this.serverEP = this.serverEP.replace("/services/","");
        this.userName = config.getFirstProperty(APIConstants.AUTH_MANAGER_USERNAME);
        this.password = config.getFirstProperty(APIConstants.AUTH_MANAGER_PASSWORD);

    }

}

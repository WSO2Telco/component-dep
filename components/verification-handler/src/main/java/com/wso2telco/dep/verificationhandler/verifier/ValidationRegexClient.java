package com.wso2telco.dep.verificationhandler.verifier;

import com.google.gson.Gson;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.ServiceError;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.wso2.carbon.apimgt.hostobjects.internal.HostObjectComponent;
import org.wso2.carbon.apimgt.impl.APIManagerConfiguration;
import java.io.IOException;

public class ValidationRegexClient {

    private ValidationRegexClient() {}

    private static final Log log = LogFactory.getLog(ValidationRegexClient.class);

    static APIManagerConfiguration config = HostObjectComponent.getAPIManagerConfiguration();
    static String serviceEndpoint = config.getFirstProperty("ValidationRegexURL");

    public static ValidationRegexDTO getValidationRegex() throws BusinessException {

        CloseableHttpClient client = null;
        client = HttpClientBuilder.create().build();
        Gson gson = new Gson();
        HttpResponse response;
        ValidationRegexDTO dto = null;

        HttpGet get = new HttpGet(serviceEndpoint);

        try {
            response = client.execute(get);
            dto = gson.fromJson(EntityUtils.toString(response.getEntity()), ValidationRegexDTO.class);
            client.close();
        } catch (IOException e) {
            log.error(e);
            throw  new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
        }
        return dto;
    }
}

class ValidationRegexDTO {

    private String validationRegex;
    private String prefixGroup;
    private String digitsGroup;

    public String getValidationRegex() {
        return validationRegex;
    }

    public String getPrefixGroup() {
        return prefixGroup;
    }

    public String getDigitsGroup() {
        return digitsGroup;
    }
}

package com.wso2telco.dep.authorize.token.handler.util;


import com.google.gson.Gson;
import com.wso2telco.dep.authorize.token.handler.dto.AddNewSpDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Created by sheshan on 8/4/17.
 */
public class TokenPoolUtil {

    private static final Log log = LogFactory.getLog(TokenPoolUtil.class);
    private static final String APPLICATION_JSON = "application/json";
    private static final String TOKEN_POOL_URL="token_pool_url";



    public static void callTokenPoolToAddSpToken(AddNewSpDto spDto){
        try {
            Gson gson = new Gson();
            String jsonInString = gson.toJson(spDto);
            Map<String , String> propertyMap = ReadPropertyFile.getPropertyFile();

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(propertyMap.get(TOKEN_POOL_URL));
            StringEntity input = new StringEntity(jsonInString);
            input.setContentType(APPLICATION_JSON);
            postRequest.setEntity(input);

            HttpResponse response = httpClient.execute(postRequest);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }
            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));
            httpClient.getConnectionManager().shutdown();

        } catch (ClientProtocolException e) {
            log.error("Error occurred while calling Token Pool" + e.getMessage());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

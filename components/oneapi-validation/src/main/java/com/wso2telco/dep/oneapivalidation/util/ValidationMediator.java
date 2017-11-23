/*******************************************************************************
 * Copyright  (c) 2016-2017, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *
 * WSO2.Telco Inc. licences this file to you under  the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.dep.oneapivalidation.util;

import com.google.gson.*;
import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.wso2telco.dep.oneapivalidation.util.Validation.getValidationRegex;

public class ValidationMediator extends AbstractMediator {

    @Override
    public boolean mediate(MessageContext mc) {

        String validationRegex = getValidationRegex();
        List<MsisdnDTO> tempValidMsisdnList = new ArrayList<MsisdnDTO>();
        List<String> validMsisdnList = new ArrayList<String>();
        List<String> invalidMsisdnList = new ArrayList<String>();
        Pattern pattern = Pattern.compile(validationRegex);
        Matcher matcher;
        Gson gson = new Gson();

        try {

            JsonArray msisdns = new JsonParser().parse(mc.getProperty("MSISDN").toString()).getAsJsonArray();

            for (int i=0;i<msisdns.size();i++) {
                matcher = pattern.matcher(msisdns.get(i).getAsString());

                if (matcher.matches()) {
                    MsisdnDTO msisdn = new MsisdnDTO(matcher.group(2), matcher.group(9));

                    if (!tempValidMsisdnList.contains(msisdn)) {
                        tempValidMsisdnList.add(msisdn);
                    }
                } else {
                    invalidMsisdnList.add(msisdns.get(i).getAsString());
                }
            }

            for(MsisdnDTO msisdn : tempValidMsisdnList){
                validMsisdnList.add(msisdn.toString());
            }

            mc.setProperty("validMsisdns", gson.toJson(validMsisdnList));
            mc.setProperty("invalidMsisdns", gson.toJson(invalidMsisdnList));
            mc.setProperty("validationRegex", validationRegex);

        } catch (Exception e) {
            log.error("Error Validating MSISDN", e);
            setErrorInContext(mc,"SVC0001",e.getMessage(),"","400","SERVICE_EXCEPTION");
            mc.setProperty("INTERNAL_ERROR","true");
        }

        return true;
    }

    private void setErrorInContext(MessageContext synContext, String messageId,
                                   String errorText, String errorVariable, String httpStatusCode,
                                   String exceptionType) {

        synContext.setProperty("messageId", messageId);
        synContext.setProperty("errorText", errorText);
        synContext.setProperty("errorVariable", errorVariable);
        synContext.setProperty("httpStatusCode", httpStatusCode);
        synContext.setProperty("exceptionType", exceptionType);
    }
}
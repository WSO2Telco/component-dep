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

import com.google.gson.Gson;
import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import static com.wso2telco.dep.oneapivalidation.util.Validation.getValidationRegex;

public class ValidationMediator extends AbstractMediator {

    @Override
    public boolean mediate(MessageContext mc) {

        List<String> validMsisdnList = new ArrayList<String>();
        List<String> invalidMsisdnList = new ArrayList<String>();
        Gson gson = new Gson();

        try {
            String msisdns = mc.getProperty("MSISDN").toString();
            String msisdnList[] = msisdns.split(",");

                for (String msisdn : msisdnList) {
                    if (Validation.isCorrectlyFormattedNumber(msisdn)) {
                            validMsisdnList.add(msisdn);
                    }else {
                        invalidMsisdnList.add(msisdn);
                    }
            }
                mc.setProperty("validMsisdns", gson.toJson(validMsisdnList));
                mc.setProperty("invalidMsisdns", gson.toJson(invalidMsisdnList));
                mc.setProperty("validationRegex", getValidationRegex());

        } catch (Exception e) {
            log.error("Error Validating MSISDN", e);
        }

        return true;
    }

}

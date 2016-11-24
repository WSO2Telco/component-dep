/**
 * Copyright (c) 2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wso2telco.dep.mediator.util;

import com.wso2telco.dep.mediator.dao.RequestDAO;
import com.wso2telco.dep.mediator.model.MessageDTO;
import com.wso2telco.dep.mediator.model.SpendChargeDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessagePersistor {

    private Log log = LogFactory.getLog(MessagePersistor.class);
    private ExecutorService executorService;
    private static MessagePersistor instance;
    private RequestDAO dbservice;

    private MessagePersistor() {

        executorService = Executors.newFixedThreadPool(Integer.parseInt(Util.getApplicationProperty("numberOfThreads")));
        dbservice = new RequestDAO();

    }

    public synchronized static MessagePersistor getInstance() {
        if (instance == null) {
            instance = new MessagePersistor();
        }
        return instance;
    }

    public void publishMessage(final MessageDTO messageDTO) throws Exception {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    dbservice.publishMessage(messageDTO);
                } catch (Exception e) {
                    log.debug("error while inserting data into database");
                }
            }
        });

    }

    public void persistSpendDate(final SpendChargeDTO spendChargeDTO) throws Exception{

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    dbservice.persistSpendCharge(spendChargeDTO);
                } catch (Exception e) {
                    log.debug("error while inserting data into database");
                }

            }
        });

    }


}

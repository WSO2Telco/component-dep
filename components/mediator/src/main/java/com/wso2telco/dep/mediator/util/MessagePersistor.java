package com.wso2telco.dep.mediator.util;

import com.wso2telco.dep.mediator.dao.RequestDAO;
import com.wso2telco.dep.mediator.model.MessageDTO;
import com.wso2telco.dep.mediator.model.SpendChargeDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sasikala on 11/24/16.
 */
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

package com.wso2telco.dep.publisheventsextensions.window;

import com.wso2telco.dep.publisheventsextensions.PublishEventsExtensionsConstants;

import org.apache.log4j.Logger;
import org.quartz.*;
import org.wso2.siddhi.core.event.in.InEvent;

public class EventRemoverJob implements Job {

    static final Logger log = Logger.getLogger(EventRemoverJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        log.info("Running Event Remover Job");

//            CronTimeWindowProcessor window = (CronTimeWindowProcessor) schedulerContext.get("windowProcessor");
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        CronTimeWindowProcessor window = (CronTimeWindowProcessor) dataMap.get("windowProcessor");

        for (String eventKey : window.getNewEventKeyList()) {
            String [] y = eventKey.split(",");
            InEvent resetEvent = new InEvent(PublishEventsExtensionsConstants.SPEND_LIMIT_SUMMING_STREAM, System
                    .currentTimeMillis(), new Object[]{y[0].trim(),"",y[1].trim(),y[2].trim(), 0.0, true,"","",""});
            window.processEvent(resetEvent);

        }
        window.getNewEventKeyList().clear();

    }
}

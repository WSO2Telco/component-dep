package com.wso2telco.hub.workflow.extensions.impl;

import com.wso2telco.dep.operatorservice.model.Operator;
import com.wso2telco.dep.operatorservice.service.OparatorService;
import com.wso2telco.hub.workflow.extensions.interfaces.OperatorApi;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;


public class OperatorImpl implements OperatorApi {

    private static final Log log = LogFactory.getLog(OperatorImpl.class);

    @Override
    public String getOperators(){
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
        String operators="";
=======
        String operators="operator";
>>>>>>> b52bad2... workflow improvement
=======
        String operators="operator";
>>>>>>> b52bad2... workflow improvement
=======
        String operators="operator";
>>>>>>> b52bad2... workflow improvement
=======
        String operators="operator";
>>>>>>> b52bad2... workflow improvement
        try {
            OparatorService operatorService = new OparatorService();
            List<Operator> operatorList = operatorService.retrieveOperatorList();
            for (Operator operator : operatorList) {
                operators = operators + operator.getOperatorName().toLowerCase() + ",";
            }
        }catch(Exception ex){
            log.error("Operator service Exception "+ex);
        }
        return operators;
    }
}

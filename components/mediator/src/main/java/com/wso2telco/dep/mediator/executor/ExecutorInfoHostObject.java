package com.wso2telco.dep.mediator.executor;


import com.wso2telco.dep.mediator.dao.ExecutorDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.wso2.carbon.apimgt.api.APIManagementException;

import java.util.*;

public class ExecutorInfoHostObject extends ScriptableObject {

    private static final Log log = LogFactory.getLog(ExecutorInfoHostObject.class);

    private final String hostObjectName = "Executor";

    //private ExecutorDAO executorDAO;
    private HashMap<String,String> executors = new HashMap <String,String>();


    @Override
    public String getClassName() {
        return hostObjectName;
    }

    public ExecutorInfoHostObject () {
        log.info("initializing Executor host object");
        bindExecutors();
        //executorDAO = new ExecutorDAO();
    }

    public static HashSet<String> jsFunction_getAllExecutors (Context cx, Scriptable thisObj, Object[] args,
                                                           Function funObj) {
        //TODO:load operators from search dto class as in store host object
        return getExecutorsList(thisObj);

    }

    public static String jsFunction_executorDescription (Context cx, Scriptable thisObj, Object[] args,
                                                         Function funObj) {
        String executor = (String) args[0];

        return "this is a sample test";

    }

    //TODO:use osgi service to bind data.
    private void bindExecutors () {
        executors.put("payment Executor", "this is payment executor");
        executors.put("location Executor", "thisis location executor");
        executors.put("wallet Executor", "this is wallet executor");
    }


    public static HashSet<String> getExecutorsList (Scriptable thisObj) {
        return ((ExecutorInfoHostObject)thisObj).getExecutorsList();
    }

    public static String getExecutorDescript (Scriptable thisObj, String executor) {
        return ((ExecutorInfoHostObject)thisObj).getExecutorDescript(executor);
    }

    private HashSet<String> getExecutorsList () {
        HashSet<String> executorList = (HashSet<String>) executors.keySet();
        return executorList;

    }

    private String getExecutorDescript (String executor) {
        if (executors.containsKey(executor)) {
            executor.
        }
    }

    //exception handling
    private static void handleExeception (String msg) throws APIManagementException {
        log.error(msg);
        throw new APIManagementException(msg);
    }

    private static void handleException (String msg, Throwable t) throws APIManagementException {
        log.error(msg, t);
        throw new APIManagementException(msg,t);
    }

}














































































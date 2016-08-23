package com.wso2telco.dep.mediator.executor;


import com.wso2telco.dep.mediator.dao.ExecutorDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.*;
import org.wso2.carbon.apimgt.api.APIManagementException;

import java.util.*;

public class ExecutorInfoHostObject extends ScriptableObject {


    private static final Log log = LogFactory.getLog(ExecutorInfoHostObject.class);

    private final String hostObjectName = "Executor";

    //private ExecutorDAO executorDAO;
    private HashMap<String,ExecutorObj> executors = new HashMap <String,ExecutorObj>();

    @Override
    public String getClassName() {
        return hostObjectName;
    }

    public ExecutorInfoHostObject () {
        log.info("initializing Executor host object");
        bindExecutors();
        //executorDAO = new ExecutorDAO();
    }

    public static NativeArray jsFunction_getAllExecutors (Context cx, Scriptable thisObj, Object[] args,
                                                           Function funObj) {
        //TODO:load operators from search dto class as in store host object
        return getExecutorsList(thisObj);

    }

    public static NativeObject jsFunction_getExecutorwithDesc (Context cx, Scriptable thisObj, Object[] args,
                                                             Function funObj) {

        return getExecutorwithDesc(thisObj);
    }

    /**
     * Js function_get each executor's description from osgi.
     *
     * @param cx the cx
     * @param thisObj the this obj
     * @param args the args
     * @param funObj the fun obj
     * @return executor description
     * @throws Exception
     */
    public static String jsFunction_executorDescription (Context cx, Scriptable thisObj, Object[] args,
                                                         Function funObj) throws APIManagementException{
        String description = null;

        if (args.length == 0) {
            description = "No description";
        } else {
            String executorName = (String) args[0];
            description = getExecutorDescript(thisObj,executorName);
        }
        return description;
    }

    //TODO:use osgi service to bind data.
    private void bindExecutors () {

        executors.put("payment_executor", new ExecutorObj("payment_executor","Payment Executor", "Payment Executor Description"));
        executors.put("location_executor", new ExecutorObj("location_executor","Location Executor", "Location Executor Description"));
        executors.put("wallet_executor", new ExecutorObj("wallet_executor","Wallet Executor", "Wallet Executor Description"));
    }


    public static NativeArray getExecutorsList (Scriptable thisObj) {
        return ((ExecutorInfoHostObject)thisObj).getExecutorsList();
    }

    public static NativeObject getExecutorwithDesc (Scriptable thisObj) {
        return ((ExecutorInfoHostObject)thisObj).getExecutorwithDesc();
    }

    public static String getExecutorDescript (Scriptable thisObj, String executor) {
        return ((ExecutorInfoHostObject)thisObj).getExecutorDescript(executor);
    }

    private NativeArray getExecutorsList () {

        Object[] keys = executors.keySet().toArray();
        NativeArray array = new NativeArray(keys);

        //Object[] keys = executors.keySet().toArray();
        //String [] executorsList =  Arrays.copyOf(keys, keys.length,String[].class);

        return array;
    }

    private NativeObject getExecutorwithDesc () {

        NativeObject executor = new NativeObject();

        for (ExecutorObj excObj : executors.values()) {
            executor.put(excObj.getExecutorName(), executor, excObj.getExecutorDescription());
        }

        return executor ;
    }


    private String getExecutorDescript (String executor) {
        
        String executorDes = null;

        if (executors.containsKey(executor)) {
            executorDes = executors.get(executor).getExecutorDescription();
        }

        return executorDes;
    }

    //exception handling
    /**
     * Handle exception.
     *
     * @param msg
     *            the msg
     * @throws APIManagementException
     *             the API management exception
     */
    private static void handleException(String msg) throws APIManagementException {
        log.error(msg);
        throw new APIManagementException(msg);
    }

    /**
     * Handle exception.
     *
     * @param msg
     *            the msg
     * @param t
     *            the t
     * @throws APIManagementException
     *             the API management exception
     */
    private static void handleException(String msg, Throwable t) throws APIManagementException {
        log.error(msg, t);
        throw new APIManagementException(msg, t);
    }

}














































































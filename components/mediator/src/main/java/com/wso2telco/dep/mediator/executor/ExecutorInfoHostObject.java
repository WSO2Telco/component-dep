package com.wso2telco.dep.mediator.executor;


import com.wso2telco.dep.mediator.dao.ExecutorDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.mozilla.javascript.*;
import org.wso2.carbon.apimgt.api.APIManagementException;

import java.util.*;

public class ExecutorInfoHostObject extends ScriptableObject {
//TODO:write method to get fullqulified name

    private static final Log log = LogFactory.getLog(ExecutorInfoHostObject.class);

    private final String hostObjectName = "Executor";

    //private ExecutorDAO executorDAO;
    private HashMap<String,ExecutorObj> executors = new HashMap <String,ExecutorObj>();
    private HashMap<String, HandlerObj> handlers = new HashMap<String, HandlerObj>();

    @Override
    public String getClassName() {
        return hostObjectName;
    }

    public ExecutorInfoHostObject () {
        log.info("initializing Executor host object");
        bindExecutors();
        bindHandlers();
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
    public static String jsFunction_getExecutorDescription (Context cx, Scriptable thisObj, Object[] args,
                                                         Function funObj) throws APIManagementException{
        String description = null;

        if (args.length == 0) {
            description = "No description";
        } else {
            String executorName = (String) args[0];
            description = getExecutorDescription(thisObj,executorName);
        }
        return description;
    }

    public static String jsFunction_getExecutorFQN (Context cx, Scriptable thisObj, Object[] args,
                                                    Function funObj) throws APIManagementException {

        String executorFQN = null;

        if (args.length == 0) {
            executorFQN = "Executor Name Not Received";
        } else {
            String executorName = (String)args[0];
            executorFQN = getExecutorFQN(thisObj, executorFQN);

        }
        return executorFQN;
    }

    public static NativeArray jsFunction_getHandlers (Context cx, Scriptable thisObj, Object[] args,
                                          Function funObj) throws APIManagementException {
        return getHandlerList(thisObj);
    }

    public static String jsFunction_getHandlerDescription (Context cx, Scriptable thisObj, Object[] args,
                                                           Function funObj) throws APIManagementException {
        String handlerDescription = null;

        if (args.length == 0) {
            handlerDescription = "Handler Name Not Received";
        } else {
            String handlerName = (String)args[0];
            handlerDescription = getHandlerDescription(thisObj, handlerName);
        }
        return handlerDescription;
    }

    public static String jsFunction_getHandlerFQN (Context cx, Scriptable thisObj, Object[] args,
                                                   Function funObj) throws APIManagementException {
        String handlerFQN = null;

        if (args.length == 0) {
            handlerFQN = "Handler Name Not Received";
        } else {
            String handlerName = (String)args[0];
            handlerFQN = getHandlerFQN(thisObj, handlerName);
        }
        return handlerFQN;
    }

    public static NativeArray jsFunction_getHandlerObjects (Context cx, Scriptable thisObj, Object[] args,
                                                            Function funObj) throws APIManagementException {
        return getHandlerObjects(thisObj);
    }

    //TODO:use osgi service to bind data.
    private void bindExecutors () {

        executors.put("payment_executor", new ExecutorObj("payment_executor","com.wso2telco.dep.mediator.impl.payment.PaymentExecutor", "Payment Executor Description"));
        executors.put("location_executor", new ExecutorObj("location_executor","com.wso2telco.dep.mediator.impl.LocationExecutor", "Location Executor Description"));
        //executors.put("wallet_executor", new ExecutorObj("wallet_executor","Wallet Executor", "Wallet Executor Description"));
    }

    private void bindHandlers () {

        handlers.put("DialogAPIRequestHandler", new HandlerObj("DialogAPIRequestHandler", "com.wso2telco.verificationhandler.verifier.DialogAPIRequestHandler"));
        handlers.put("DialogBlacklistHandler", new HandlerObj("DialogBlacklistHandler", "com.wso2telco.verificationhandler.verifier.DialogBlacklistHandler"));
    }

    public static NativeArray getHandlerObjects (Scriptable thisObj) {
        return ((ExecutorInfoHostObject)thisObj).getHandlerObjects();
    }

    public static NativeArray getHandlerList (Scriptable thisObj) {
        return ((ExecutorInfoHostObject)thisObj).getHandlerList();
    }

    public static String getHandlerFQN (Scriptable thisObj, String handlerName) {
        return ((ExecutorInfoHostObject)thisObj).getHandlerFQN(handlerName);
    }


    public static NativeArray getExecutorsList (Scriptable thisObj) {
        return ((ExecutorInfoHostObject)thisObj).getExecutorsList();
    }

    public static NativeObject getExecutorwithDesc (Scriptable thisObj) {
        return ((ExecutorInfoHostObject)thisObj).getExecutorwithDesc();
    }

    public static String getExecutorDescription (Scriptable thisObj, String executor) {
        return ((ExecutorInfoHostObject)thisObj).getExecutorDescription(executor);
    }

    public static String getHandlerDescription (Scriptable thisObj, String handler) {
        return ((ExecutorInfoHostObject)thisObj).getHandlerDescription(handler);
    }

    public static String getExecutorFQN (Scriptable thisObj, String executor) {
        return ((ExecutorInfoHostObject)thisObj).getExecutorFQN(executor);
    }

    private NativeArray getHandlerList () {

        Object[] keys = handlers.keySet().toArray();
        NativeArray array = new NativeArray(keys);

        return array;
    }

    private NativeArray getExecutorsList () {

        Object[] keys = executors.keySet().toArray();
        NativeArray array = new NativeArray(keys);

        //Object[] keys = executors.keySet().toArray();
        //String [] executorsList =  Arrays.copyOf(keys, keys.length,String[].class);

        return array;
    }

    private NativeArray getHandlerObjects () {

        Object[] values = handlers.values().toArray();
        NativeArray nativeArray = new NativeArray(values.length);
        JSONArray jsonArray = new JSONArray();

        for (Object obj : values) {

            HandlerObj object = (HandlerObj) obj;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("handlerName", object.getHandlerName());
            jsonObject.put("handlerFQN", object.getHandlerFullQulifiedName());

            jsonArray.add(jsonObject);

            //NativeObject nativeObject = new NativeObject();
            //nativeObject.put(object.getHandlerName(), nativeObject, object.getHandlerFullQulifiedName());
        }

        nativeArray.put(0, nativeArray, jsonArray.toJSONString());






        return nativeArray;
    }

    private NativeObject getExecutorwithDesc () {

        NativeObject executor = new NativeObject();

        for (ExecutorObj excObj : executors.values()) {
            executor.put(excObj.getExecutorName(), executor, excObj.getExecutorDescription());
        }

        return executor ;
    }

    private String getExecutorDescription (String executor) {
        
        String executorDes = null;

        if (executors.containsKey(executor)) {
            executorDes = executors.get(executor).getExecutorDescription();
        }

        return executorDes;
    }

    private String getHandlerDescription (String handler) {

        String handlerDescription = null;

        if (handlers.containsKey(handler)) {
            handlerDescription = handlers.get(handler).getHandlerDescription();
        }

        return handlerDescription;
    }

    private String getExecutorFQN (String executor) {

        String executorFQN = null;

        if (executors.containsKey(executor)) {
            executorFQN = executors.get(executor).getFullQulifiedName();
        } else {

        }

        return executorFQN;
    }

    private String getHandlerFQN (String handler) {

        String handlerFQN = null;

        if (handlers.containsKey(handler)) {
            handlerFQN = handlers.get(handler).getHandlerFullQulifiedName();
        } else {

        }
        return handlerFQN;
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














































































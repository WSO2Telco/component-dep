package com.wso2telco.operatorservice;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.api.APIManagementException;
import com.wso2telco.operatorservice.dao.AxiataDAO;
import com.wso2telco.operatorservice.model.Operator;

import org.mozilla.javascript.*;

/**
 * Host object to hanled Axiata specific store related tasks. 
 */
public class AxiataStoreHostObject extends ScriptableObject {

	private static final Log log = LogFactory.getLog(AxiataStoreHostObject.class);
	private String hostobjectName = "AxiataStore";

	@Override
	public String getClassName() {
		return hostobjectName;
	}

	public AxiataStoreHostObject() {
		log.info("::: Initialized HostObject ");
	}

	/**
	 * This method retrieves the list of available operators.
	 * @param cx
	 * @param thisObj
	 * @param args
	 * @param funObj
	 * @return
	 * @throws APIManagementException
	 */
	public static List<Operator> jsFunction_retrieveOperatorList(Context cx,
													Scriptable thisObj, Object[] args, Function funObj)
													throws APIManagementException {
		
		List<Operator> operatorList = null;
		
		try {
			AxiataDAO axiataDAO = new AxiataDAO();
			operatorList = axiataDAO.retrieveOperatorList();
			
		} catch(Exception e) {
			handleException("Error occured while retrieving operator list. ", e);
		}
		
		return operatorList;
	}
	
	/**
	 * This method persists the selected list of operators for a given subscription.
	 * @param cx
	 * @param thisObj
	 * @param args
	 * @param funObj
	 * @return
	 * @throws APIManagementException
	 */
	public static boolean jsFunction_persistSubOperatorList(Context cx,
													Scriptable thisObj, Object[] args, Function funObj)
													throws APIManagementException {
		
		boolean status = false;
		
		String apiName = (String)args[0];
		String apiVersion = (String)args[1];
		String apiProvider = (String)args[2];;
		int appId = ((Double)args[3]).intValue();
		String operatorList = (String)args[4];
		
		try {
			AxiataDAO axiataDAO = new AxiataDAO();
			axiataDAO.persistOperators(apiName, apiVersion, apiProvider, appId, operatorList);
			
		} catch(Exception e) {
			handleException("Error occured while retrieving operator list. ", e);
		}
		
		return status;
	}
	
	/**
	 * Handle expection.
	 * @param msg
	 * @throws APIManagementException
	 */
	private static void handleException(String msg) throws APIManagementException {
		log.error(msg);
		throw new APIManagementException(msg);
	}

	/**
	 * Handle expection.
	 * @param msg
	 * @param t
	 * @throws APIManagementException
	 */
	private static void handleException(String msg, Throwable t) throws APIManagementException {
		log.error(msg, t);
		throw new APIManagementException(msg, t);
	}
}

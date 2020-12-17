package com.wso2telco.workflow.dao;


import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.workflow.model.APISubscriptionDTO;
import com.wso2telco.workflow.model.APISubscriptionStatusDTO;
import com.wso2telco.workflow.model.ApplicationStatusDTO;
import com.wso2telco.workflow.utils.WorkflowServiceException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * DAO layer for handling all db queries related to WorkflowHistoryService
 */
public class WorkflowHistoryDAO {

	private static final Log log = LogFactory.getLog(WorkflowHistoryDAO.class);
	//Constants of table column names
	private static final String OPERATOR_NAME = "operator_name";
	private static final String OPERATOR_APPROVAL = "operator_approval";
	private static final String API_NAME = "api_name";
	private static final String API_ID = "api_id";

	public ApplicationStatusDTO getApplicationStatus(int appID) throws WorkflowServiceException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ApplicationStatusDTO app = new ApplicationStatusDTO();

		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);

			if (conn == null) {
				throw new WorkflowServiceException("Unable to get DB Connection!");
			}

			String depDB = DbUtils.getDbNames().get(DataSourceNames.WSO2TELCO_DEP_DB);
			String apimgtDB = DbUtils.getDbNames().get(DataSourceNames.WSO2AM_DB);

			String sql = "SELECT app.NAME AS 'app_name'," +
					"       o.operatorname AS 'operator_name'," +
					"       oa.isactive AS 'operator_approval'," +
					"       app.application_status AS 'admin_approval' " +
					"FROM " + depDB + ".operators o," +
					"     " + depDB + ".operatorapps oa," +
					"     " + apimgtDB + ".am_application app " +
					"WHERE oa.applicationid = app.application_id" +
					"  AND oa.applicationid = ?" +
					"  AND oa.operatorid = o.id";

			ps = conn.prepareStatement(sql);
			ps.setInt(1, appID);
			rs = ps.executeQuery();

			while (rs.next()) {
				if (app.getName() == null) {
					app.setName(rs.getString("app_name"));
				}
				if (app.getStatus() == null) {
					app.setStatus(rs.getString("admin_approval"));
				}

				app.addOperator(rs.getString(OPERATOR_NAME), rs.getString(OPERATOR_APPROVAL));
			}

		} catch (Exception e) {
			throw new WorkflowServiceException(e);
		} finally {
			DbUtils.closeAllConnections(ps, conn, rs);
		}

		return app;
	}


	public ApplicationStatusDTO getSubscribedAPIs(int appID, ApplicationStatusDTO app) throws WorkflowServiceException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		APISubscriptionStatusDTO subscription = null;
		String lastUsedAPI = "";

		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);

			if (conn == null) {
				throw new WorkflowServiceException("Unable to get DB Connection!");
			}

			String depDB = DbUtils.getDbNames().get(DataSourceNames.WSO2TELCO_DEP_DB);
			String apimgtDB = DbUtils.getDbNames().get(DataSourceNames.WSO2AM_DB);

			String sql = "SELECT api.api_name, " +
					"       api.api_version, " +
					"       api.api_id, " +
					"       sub.tier_id, " +
					"       sub.sub_status AS 'admin_approval', " +
					"       o.operatorname AS 'operator_name', " +
					"       epa.isactive   AS 'operator_approval', " +
					"       sub.updated_time " +
					"FROM   " + depDB + ".endpointapps epa, " +
					" " + depDB + ".operatorendpoints oep, " +
					" " + depDB + ".operators o, " +
					" " + apimgtDB + ".am_api api, " +
					" " + apimgtDB + ".am_subscription sub " +
					"WHERE  epa.applicationid = ? " +
					"       AND epa.endpointid = oep.id " +
					"       AND o.id = oep.operatorid " +
					"       AND oep.api = api.api_name " +
					"       AND sub.api_id = api.api_id" +
					"       AND sub.application_id = epa.applicationid " +
					"ORDER BY api_name";

			ps = conn.prepareStatement(sql);
			ps.setInt(1, appID);
			rs = ps.executeQuery();

			while (rs.next()) {
				if (!lastUsedAPI.equals(rs.getString(API_NAME))) {
					subscription = new APISubscriptionStatusDTO();
					subscription.setName(rs.getString(API_NAME));
					subscription.setId(rs.getString(API_ID));
					subscription.setVersion(rs.getString("api_version"));
					subscription.setTier(rs.getString("tier_id"));
					subscription.setAdminApprovalStatus(rs.getString("admin_approval"));
					subscription.addOperator(rs.getString(OPERATOR_NAME), rs.getString(OPERATOR_APPROVAL));
					subscription.setLastUpdated(rs.getDate("updated_time").toString() + " " + rs.getTime("updated_time"));
					lastUsedAPI = rs.getString(API_NAME);
					app.addSubscription(subscription);
				} else {
					if (subscription != null) {
						subscription.addOperator(rs.getString(OPERATOR_NAME), rs.getString(OPERATOR_APPROVAL));
					}
				}
			}

		} catch (Exception e) {
			throw new WorkflowServiceException(e);
		} finally {
			DbUtils.closeAllConnections(ps, conn, rs);
		}
		return app;
	}

	public List<APISubscriptionDTO> getSubscribedAPIsWithOperators(int appID,int opId,String apiid ,int start, int size) throws WorkflowServiceException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<APISubscriptionDTO> subscriptions=new ArrayList<APISubscriptionDTO>();

		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);

			if (conn == null) {
				throw new WorkflowServiceException("Unable to get DB Connection!");
			}

			String depDB = DbUtils.getDbNames().get(DataSourceNames.WSO2TELCO_DEP_DB);
			String apimgtDB = DbUtils.getDbNames().get(DataSourceNames.WSO2AM_DB);

			String sqlPredecessor ="SELECT api.api_name, " +
					"       api.api_version, " +
					"       api.api_provider," +
					"       api.api_id, " +
					"       sub.tier_id, " +
					"       o.operatorname AS 'operator_name', " +
					"       epa.isactive   AS 'operator_approval', " +
					"       sub.updated_time " +
					"FROM   " + depDB + ".endpointapps epa, " +
					" " + depDB + ".operatorendpoints oep, " +
					" " + depDB + ".operators o, " +
					" " + apimgtDB + ".am_api api, " +
					" " + apimgtDB + ".am_subscription sub " +
					"WHERE  epa.applicationid = ? " +
					"       AND epa.endpointid = oep.id " +
					"       AND o.id = oep.operatorid " +
					"       AND oep.api = api.api_name " +
					"       AND sub.api_id = api.api_id" +
					"       AND sub.application_id = epa.applicationid " +
					"       AND o.id = ? ";  


			if(!apiid.equals("_ALL")){
				sqlPredecessor= sqlPredecessor.concat("   AND api.api_id= ? ");
			}
			sqlPredecessor= sqlPredecessor.concat("       AND oep.api = api.api_name " +
					"       AND sub.api_id = api.api_id" +
					"       AND sub.application_id = epa.applicationid " +
					"ORDER BY api_name limit ?,?");
			ps = conn.prepareStatement(sqlPredecessor);
			ps.setInt(1, appID);
			ps.setInt(2, opId);


			if(!apiid.equals("_ALL")){
				ps.setString(3, apiid);
				ps.setInt(4, start);
				ps.setInt(5, size);
			}else{
				ps.setInt(3, start);
				ps.setInt(4, size);
			}

			rs = ps.executeQuery();

			while (rs.next()) {
				boolean isNew=true;
				for(APISubscriptionDTO subscriptionDB:subscriptions){
					if(subscriptionDB.getName().equals(rs.getString(API_NAME)) && subscriptionDB.getVersion().equals(rs.getString("api_version"))){
						isNew=false;
					}
				}
				if(isNew){
					APISubscriptionDTO subscription  = new APISubscriptionDTO();
					subscription.setName(rs.getString(API_NAME));
					subscription.setId(rs.getString(API_ID));
					subscription.setVersion(rs.getString("api_version"));
					subscription.setProvider(rs.getString("api_provider"));
					subscription.setTier(rs.getString("tier_id"));
					subscription.setOperatoApprovalStatus(rs.getString(OPERATOR_NAME), rs.getString(OPERATOR_APPROVAL));
					subscription.setLastUpdated(rs.getDate("updated_time").toString() + " " + rs.getTime("updated_time"));
					subscriptions.add(subscription);
				}
			}


		} catch (Exception e) {
			throw new WorkflowServiceException(e);
		} finally {
			DbUtils.closeAllConnections(ps, conn, rs);
		}
		return subscriptions;
	}
	public List<APISubscriptionDTO> getSubscribedAPIsWithoutOperators(int appID, String apiid ,int start, int size) throws WorkflowServiceException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<APISubscriptionDTO> subscriptions=new ArrayList<APISubscriptionDTO>();
		try {
			conn = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_DEP_DB);

			if (conn == null) {
				throw new WorkflowServiceException("Unable to get DB Connection!");
			}

			String apimgtDB = DbUtils.getDbNames().get(DataSourceNames.WSO2AM_DB);

			String sql="SELECT api.api_name, " +
					"       api.api_version, " +
					"       api.api_provider," +
					"       api.api_id, " +
					"       sub.tier_id, " +
					"       sub.sub_status AS 'admin_approval', " +
					"       sub.updated_time " +
					"FROM   "+ apimgtDB + ".am_api api, " +
					" " + apimgtDB + ".am_subscription sub " +
					"WHERE  sub.application_id = ?" +
					"       AND api.api_id = sub.api_id";
			
			if(!apiid.equals("_ALL")){

				sql= sql.concat(" AND api.api_id = ? limit ?, ?"); 

				ps = conn.prepareStatement(sql);
				ps.setInt(1, appID);
				ps.setInt(2, Integer.parseInt(apiid));
				ps.setInt(3, start);
				ps.setInt(4, size);

			}else{
				sql=  sql.concat(" limit ?, ?"); 
				ps = conn.prepareStatement(sql);
				ps.setInt(1, appID);
				ps.setInt(2, start);
				ps.setInt(3, size);
			}


			rs = ps.executeQuery();

			log.debug(ps.toString());
			while (rs.next()) {
				APISubscriptionDTO subscription = new APISubscriptionDTO();
				log.debug(rs.getString(API_NAME));
				subscription.setName(rs.getString(API_NAME));
				subscription.setId(rs.getString(API_ID));
				subscription.setVersion(rs.getString("api_version"));
				subscription.setProvider(rs.getString("api_provider"));
				subscription.setTier(rs.getString("tier_id"));
				subscription.setAdminApprovalStatus(rs.getString("admin_approval"));
				subscription.setLastUpdated(rs.getDate("updated_time").toString() + " " + rs.getTime("updated_time"));
				subscriptions.add(subscription);
			} 

		} catch (Exception e) {
			throw new WorkflowServiceException(e);
		} finally {
			DbUtils.closeAllConnections(ps, conn, rs);
		}
		return subscriptions;
	}

}

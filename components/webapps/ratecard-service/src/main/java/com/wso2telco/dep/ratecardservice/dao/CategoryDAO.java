package com.wso2telco.dep.ratecardservice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.wso2telco.core.dbutils.DbUtils;
import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.core.dbutils.exception.ServiceError;
import com.wso2telco.core.dbutils.util.DataSourceNames;
import com.wso2telco.dep.ratecardservice.dao.model.CategoryDTO;
import com.wso2telco.dep.ratecardservice.util.DatabaseTables;

public class CategoryDAO {

	private final Log log = LogFactory.getLog(CategoryDAO.class);

	public List<CategoryDTO> getCategories() throws Exception {

		List<CategoryDTO> categories = new ArrayList<CategoryDTO>();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder query = new StringBuilder(
					"select categoryid, categoryname, categorycode, categorydesc, createdby from ");
			query.append(DatabaseTables.CATEGORY.getTObject());

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getCategories : " + ps);

			rs = ps.executeQuery();

			while (rs.next()) {

				CategoryDTO category = new CategoryDTO();

				category.setCategoryId(rs.getInt("categoryid"));
				category.setCategoryName(rs.getString("categoryname"));
				category.setCategoryCode(rs.getString("categorycode"));
				category.setCategoryDescription(rs.getString("categorydesc"));
				category.setCreatedBy(rs.getString("createdby"));

				categories.add(category);
			}
		} catch (SQLException e) {

			log.error("database operation error in getCategories : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in getCategories : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return categories;
	}

	public CategoryDTO addCategory(CategoryDTO category) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer categoryId = 0;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder query = new StringBuilder("insert into ");
			query.append(DatabaseTables.CATEGORY.getTObject());
			query.append(" (categoryname, categorycode, categorydesc, createdby)");
			query.append(" values");
			query.append(" (?, ?, ?, ?)");

			ps = con.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);

			log.debug("sql query in addCategory : " + ps);

			ps.setString(1, category.getCategoryName());
			ps.setString(2, category.getCategoryCode());
			ps.setString(3, category.getCategoryDescription());
			ps.setString(4, category.getCreatedBy());

			ps.executeUpdate();

			rs = ps.getGeneratedKeys();

			while (rs.next()) {

				categoryId = rs.getInt(1);
			}

			category.setCategoryId(categoryId);
		} catch (SQLException e) {

			log.error("database operation error in addCategory : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in addCategory : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return category;
	}

	public CategoryDTO getCategory(int categoryId) throws Exception {

		CategoryDTO category = null;

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder query = new StringBuilder(
					"select categoryid, categoryname, categorycode, categorydesc, createdby from ");
			query.append(DatabaseTables.CATEGORY.getTObject());
			query.append(" where categoryid = ?");

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getCategory : " + ps);

			ps.setInt(1, categoryId);

			rs = ps.executeQuery();

			while (rs.next()) {

				category = new CategoryDTO();

				category.setCategoryId(rs.getInt("categoryid"));
				category.setCategoryName(rs.getString("categoryname"));
				category.setCategoryCode(rs.getString("categorycode"));
				category.setCategoryDescription(rs.getString("categorydesc"));
				category.setCreatedBy(rs.getString("createdby"));
			}
		} catch (SQLException e) {

			log.error("database operation error in getCategory : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in getCategory : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return category;
	}
}

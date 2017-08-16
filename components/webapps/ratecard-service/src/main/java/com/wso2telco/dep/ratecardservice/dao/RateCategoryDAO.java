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
import com.wso2telco.dep.ratecardservice.dao.model.RateCategoryDTO;
import com.wso2telco.dep.ratecardservice.dao.model.RateDefinitionDTO;
import com.wso2telco.dep.ratecardservice.dao.model.TariffDTO;
import com.wso2telco.dep.ratecardservice.util.DatabaseTables;

public class RateCategoryDAO {

	private final Log log = LogFactory.getLog(RateCategoryDAO.class);

	public RateCategoryDTO addRateCategory(RateCategoryDTO rateCategory) throws Exception {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer rateCategoryId = 0;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder query = new StringBuilder("insert into ");
			query.append(DatabaseTables.RATE_CATEGORY.getTObject());
			query.append(" (rate_defid, parentcategoryid, childcategoryid, tariffid, createdby)");
			query.append(" values");
			query.append(" (?, ?, ?, ?, ?)");

			ps = con.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);

			log.debug("sql query in addRateCategory : " + ps);

			ps.setInt(1, rateCategory.getRateDefinition().getRateDefId());
			ps.setInt(2, rateCategory.getCategory().getCategoryId());
			ps.setInt(3, rateCategory.getSubCategory().getCategoryId());
			ps.setInt(4, rateCategory.getTariff().getTariffId());
			ps.setString(5, rateCategory.getCreatedBy());

			ps.executeUpdate();

			rs = ps.getGeneratedKeys();

			while (rs.next()) {

				rateCategoryId = rs.getInt(1);
			}

			rateCategory.setRateCategoryId(rateCategoryId);
		} catch (SQLException e) {

			log.error("database operation error in addRateCategory : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in addRateCategory : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return rateCategory;
	}

	public List<RateCategoryDTO> getRateCategories(int rateDefId) throws Exception {

		List<RateCategoryDTO> rateCategories = new ArrayList<RateCategoryDTO>();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder query = new StringBuilder("select * from ");
			query.append(DatabaseTables.RATE_CATEGORY.getTObject());
			query.append(" where rate_defid = ?");

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getRateCategories : " + ps);
			
			ps.setInt(1, rateDefId);

			rs = ps.executeQuery();

			while (rs.next()) {

				RateCategoryDTO rateCategory = new RateCategoryDTO();

				rateCategory.setRateCategoryId(rs.getInt("rate_category_id"));
				
				RateDefinitionDTO rateDefinition = new RateDefinitionDTO();
				rateDefinition.setRateDefId(rs.getInt("rate_defid"));
				rateCategory.setRateDefinition(rateDefinition);
				
				CategoryDTO category = new CategoryDTO();
				category.setCategoryId(rs.getInt("parentcategoryid"));
				rateCategory.setCategory(category);
				
				CategoryDTO subCategory = new CategoryDTO();
				subCategory.setCategoryId(rs.getInt("childcategoryid"));
				rateCategory.setSubCategory(subCategory);
				
				TariffDTO tariff = new TariffDTO();
				tariff.setTariffId(rs.getInt("tariffid"));
				rateCategory.setTariff(tariff);
				
				rateCategory.setCreatedBy(rs.getString("createdby"));
				rateCategory.setCreatedDate(rs.getTimestamp("createddate").toString());
				rateCategory.setUpdatedBy(rs.getString("updatedby"));
				rateCategory.setUpdatedDate(rs.getTimestamp("updateddate").toString());

				rateCategories.add(rateCategory);
			}
		} catch (SQLException e) {

			log.error("database operation error in getRateCategories : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in getRateCategories : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return rateCategories;
	}
	
	public RateCategoryDTO getRateCategory(int rateCategoryId) throws Exception {

		RateCategoryDTO rateCategory = null;

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = DbUtils.getDbConnection(DataSourceNames.WSO2TELCO_RATE_DB);
			if (con == null) {

				throw new Exception("Connection not found");
			}

			StringBuilder query = new StringBuilder("select * from ");
			query.append(DatabaseTables.RATE_CATEGORY.getTObject());
			query.append(" where rate_category_id = ?");

			ps = con.prepareStatement(query.toString());

			log.debug("sql query in getRateCategory : " + ps);
			
			ps.setInt(1, rateCategoryId);

			rs = ps.executeQuery();

			while (rs.next()) {

				rateCategory = new RateCategoryDTO();

				rateCategory.setRateCategoryId(rs.getInt("rate_category_id"));
				
				RateDefinitionDTO rateDefinition = new RateDefinitionDTO();
				rateDefinition.setRateDefId(rs.getInt("rate_defid"));
				rateCategory.setRateDefinition(rateDefinition);
				
				CategoryDTO category = new CategoryDTO();
				category.setCategoryId(rs.getInt("parentcategoryid"));
				rateCategory.setCategory(category);
				
				CategoryDTO subCategory = new CategoryDTO();
				subCategory.setCategoryId(rs.getInt("childcategoryid"));
				rateCategory.setSubCategory(subCategory);
				
				TariffDTO tariff = new TariffDTO();
				tariff.setTariffId(rs.getInt("tariffid"));
				rateCategory.setTariff(tariff);
				
				rateCategory.setCreatedBy(rs.getString("createdby"));
				rateCategory.setCreatedDate(rs.getTimestamp("createddate").toString());
				rateCategory.setUpdatedBy(rs.getString("updatedby"));
				rateCategory.setUpdatedDate(rs.getTimestamp("updateddate").toString());
			}
		} catch (SQLException e) {

			log.error("database operation error in getRateCategory : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} catch (Exception e) {

			log.error("error in getRateCategory : ", e);
			throw new BusinessException(ServiceError.SERVICE_ERROR_OCCURED);
		} finally {

			DbUtils.closeAllConnections(ps, con, rs);
		}

		return rateCategory;
	}
}

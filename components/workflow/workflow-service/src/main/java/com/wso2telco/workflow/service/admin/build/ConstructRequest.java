package com.wso2telco.workflow.service.admin.build;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.workflow.model.BaseEditDTO;
import com.wso2telco.workflow.model.TierUpdtConnDTO;

import java.sql.SQLException;

public interface ConstructRequest<T extends TierUpdtConnDTO,K extends BaseEditDTO> {
    public T constructTierUpdtRequsst(K k) throws SQLException, BusinessException;
}

package com.wso2telco.workflow.service.admin.build;

import com.wso2telco.core.dbutils.exception.BusinessException;
import com.wso2telco.workflow.model.BaseEditDTO;
import com.wso2telco.workflow.model.BaseTierUpdtReq;
import com.wso2telco.workflow.model.TierUpdtConnDTO;
import com.wso2telco.workflow.service.admin.factory.TierUpdtConnFactory;
import com.wso2telco.workflow.service.admin.factory.TierUpdtReqestFactory;

import java.sql.SQLException;

public class TierRequst<T extends TierUpdtConnDTO> implements ConstructRequest{

    private TierUpdtReqestFactory factory;
    private TierUpdtConnFactory connFactory;
    private BaseTierUpdtReq tierUpdtReq;
    private TierUpdtConnDTO tierUpdtConnDTO;

    @Override
    public T constructTierUpdtRequsst(BaseEditDTO baseEditDTO)  throws SQLException, BusinessException {

        T t = null;
        factory = new TierUpdtReqestFactory();
        connFactory = new TierUpdtConnFactory();

        tierUpdtReq = factory.getTierUpdateRequest(baseEditDTO);

        tierUpdtConnDTO = connFactory.getTierUpdateRequest(tierUpdtReq);
        tierUpdtConnDTO.setTierUpdtReq(tierUpdtReq);

        t = (T) tierUpdtConnDTO;
        return t;

    }
}

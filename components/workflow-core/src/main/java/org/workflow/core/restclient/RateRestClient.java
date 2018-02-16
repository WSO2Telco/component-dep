package org.workflow.core.restclient;

import feign.Param;
import feign.RequestLine;
import org.workflow.core.execption.WorkflowExtensionException;
import org.workflow.core.model.OperationRateResponse;

public interface RateRestClient {

    @RequestLine("GET apis/{apiName}/operations/operationrates")
    OperationRateResponse getAdminOperationRates(@Param("apiName") String apiName) throws WorkflowExtensionException;

    @RequestLine("GET operators/{operator}/apis/{apiName}/operatorrates")
    OperationRateResponse getOperatorOperationRates(@Param("apiName") String apiName, @Param("operator") String operator) throws WorkflowExtensionException;

}

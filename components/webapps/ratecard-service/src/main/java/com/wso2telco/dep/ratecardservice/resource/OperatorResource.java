package com.wso2telco.dep.ratecardservice.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/operators")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OperatorResource {

	@Path("/{operatorName}/operatorrates")
	public OperatorRateResource getOperatorRateResource() {

		return new OperatorRateResource();
	}
}

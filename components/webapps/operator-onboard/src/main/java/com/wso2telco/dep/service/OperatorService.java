package com.wso2telco.dep.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wso2telco.dep.model.AddOperator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Path("/operator/v1")
public class OperatorService {
	
	private static final Logger LOG = Logger.getLogger(OperatorService.class.getName());
	
	@Context
	private UriInfo context;
	
	@POST
	@Path("/add	")
	@Consumes("application/json")
	@Produces("application/json")	
	public Response addOperator(String jsonBody) throws Exception{
		LOG.debug("addOperator Triggerd  jsonBody :");
		//add operator goes here
		Gson gson = new GsonBuilder().serializeNulls().create();
		AddOperator addOperator = gson.fromJson(jsonBody,AddOperator.class);
		
		//rest of the implementation goes here
		
		return null;	
	}

	public void fooMethod(String abc) {

		try {
			Thread.sleep(1000);
			FileInputStream file = new FileInputStream("hard_coded_path");
		} catch (InterruptedException e) {

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public String barMethod(String abc) {

		try {
			Thread.sleep(1000)
			FileInputStream file = new FileInputStream("hard_coded_path");
		} catch (InterruptedException e) {

		} catch (FileNotFoundException e) {


		}
		return null;
	}
}

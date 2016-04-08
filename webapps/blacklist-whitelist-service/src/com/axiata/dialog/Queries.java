/*
 * Queries.java
 * Apr 2, 2013  11:20:38 AM
 * Dialog
 *
 * Copyright (C) Dialog Axiata PLC. All Rights Reserved.
 */
package com.axiata.dialog;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import com.axiata.dialog.entity.BlackList;
import com.axiata.dialog.entity.BlackListBulk;
import com.axiata.dialog.entity.Id;
import com.axiata.dialog.entity.RemoveRequest;
import com.axiata.dialog.entity.WhiteList;
import com.axiata.dialog.entity.WhiteListBulk;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/**
 * REST Web Service
 *
 * @version $Id: Queries.java,v 1.00.000
 */
@Path("/queries")
public class Queries {

    private static final Logger LOG = Logger.getLogger(Queries.class.getName());
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of QueriesResource
     */
    public Queries() {
    }

    /**
     * GET method for creating an instance of QueriesResource
     *
     * @param address,requestedAccuracy representation for the new resource
     * @return an HTTP response with content of the created resource
     * @throws Exception 
     */
    @POST
    @Path("/Blacklist/{MSISDN}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response location(@PathParam("MSISDN") String msisdn, String jsonBody) throws Exception {

        String jsonreturn = null; 
        Gson gson = new GsonBuilder().serializeNulls().create();
        
        BlackList blackListReq = gson.fromJson(jsonBody, BlackList.class);
        
        String apiID = blackListReq.getAPIID();
        String apiName = blackListReq.getAPIName();
        String userID = blackListReq.getUserID();
        
        if (apiID != null && apiName != null && userID != null) {

            try {
                DatabaseUtils.WriteBlacklistNumbers(msisdn,apiID,apiName,userID);

                jsonreturn = "{" + "\"Success\":" + "{"
                   + "\"messageId\":\"" + "Blacklist Number" + "\","
                   + "\"text\":\""+  "Blacklist Number Successfully Added to the system "+ "\","
                   + "\"variables\":\"" + msisdn + "\""
                   + "}}"; 

                return Response.status(200).entity(jsonreturn).build();
            } catch (NamingException ex) {
                java.util.logging.Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);

                jsonreturn = "{" + "\"Failed\":" + "{"
                   + "\"messageId\":\"" + "Blacklist Number" + "\","
                   + "\"text\":\""+  "Blacklist Number could not be added to the system "+ "\","
                   + "\"variables\":\"" + msisdn + "\""
                   + "}}"; 
                return Response.status(400).entity(jsonreturn).build();
            }  catch (SQLException e) {
                jsonreturn = "{" + "\"Failed\":" + "{"
                   + "\"messageId\":\"" + "Blacklist Number" + "\","
                   + "\"text\":\""+  "Blacklist Number could not be added to the system "+ "\","
                   + "\"variables\":\"" + msisdn + "\""
                   + "}}"; 
                return Response.status(400).entity(jsonreturn).build();
            }   
        }
        else{
            jsonreturn = "{" + "\"Failed\":" + "{"
                   + "\"messageId\":\"" + "Blacklist Number" + "\","
                   + "\"text\":\""+  "Blacklist Number could not be added to the system "+ "\","
                   + "\"variables\":\"" + msisdn + "\""
                   + "}}"; 
                return Response.status(400).entity(jsonreturn).build();
        }
        
    }

    /**
     * Adds a list of subscribers to blacklist
     *
     * {
     * "apiID":"1", "apiName":"USSD", "userID":"admin"
     * "msisdnList":["94777000001","94777000002","94777000003"] 
     * }
     * @throws Exception 
     */
    @POST
    @Path("/Blacklist")
    @Consumes("application/json")
    @Produces("text/plain")
    public Response location(String jsonBody){

        String jsonreturn = null;
        Gson gson = new GsonBuilder().serializeNulls().create();

        BlackListBulk blackListReq = gson.fromJson(jsonBody, BlackListBulk.class);

        String apiID = blackListReq.getAPIID();
        String apiName = blackListReq.getAPIName();
        String userID = blackListReq.getUserID();
        String[] msisdnList = blackListReq.getMsisdnList();

        gson = new Gson();

        if (apiID != null && apiName != null && userID != null && msisdnList != null) {

            msisdnList = removeNullMsisdnValues(msisdnList);

            try {
                DatabaseUtils.writeBlacklistNumbersBulk(msisdnList, apiID, apiName, userID);

                jsonreturn = "{" + "\"Success\":" + "{"
                        + "\"messageId\":\"" + "Blacklist Numbers" + "\","
                        + "\"text\":\"" + "Blacklist Numbers Successfully Added to the system " + "\","
                        + "\"variables\":" + gson.toJson(msisdnList)
                        + "}}";

                return Response.status(200).entity(jsonreturn).build();
            } catch (NamingException ex) {
                java.util.logging.Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);

                jsonreturn = "{" + "\"Failed\":" + "{"
                        + "\"messageId\":\"" + "Blacklist Numbers" + "\","
                        + "\"text\":\"" + "Blacklist Numbers could not be added to the system " + "\","
                        + "\"variables\":" + gson.toJson(msisdnList)
                        + "}}";
                return Response.status(400).entity(jsonreturn).build();
            } catch (SQLException e) {
                jsonreturn = "{" + "\"Failed\":" + "{"
                        + "\"messageId\":\"" + "Blacklist Numbers" + "\","
                        + "\"text\":\"" + "Blacklist Numbers could not be added to the system " + "\","
                        + "\"variables\":" + gson.toJson(msisdnList)
                        + "}}";
                return Response.status(400).entity(jsonreturn).build();
            }catch(Exception ex){
            	  jsonreturn = "{" + "\"Failed\":" + "{"
                          + "\"messageId\":\"" + "Blacklist Numbers" + "\","
                          + "\"text\":\"" + "Blacklist Numbers could not be added to the system " + "\","
                          + "\"variables\":" + gson.toJson(msisdnList)
                          + "}}";
                  return Response.status(400).entity(jsonreturn).build();
           }
            
            
        } else {
            jsonreturn = "{" + "\"Failed\":" + "{"
                    + "\"messageId\":\"" + "Blacklist Numbers" + "\","
                    + "\"text\":\"" + "Blacklist Numbers could not be added to the system " + "\","
                    + "\"variables\":" + gson.toJson(msisdnList)
                    + "}}";
            return Response.status(400).entity(jsonreturn).build();
        }

    }

    private String[] removeNullMsisdnValues(String[] msisdnList) {
        List<String> list = new ArrayList<String>(Arrays.asList(msisdnList));
        list.removeAll(Collections.singleton(null));
        return list.toArray(new String[list.size()]);
    }

    /**
     * Returns the blacklist per API
     *
     * {empty request body}
     */
    @POST
    @Path("/GetBlacklistPerApi/{APINAME}")
    @Consumes("application/json")
    @Produces("text/plain")
    public Response getBlacklistPerApi(@PathParam("APINAME") String apiName) throws SQLException {

        String jsonreturn = null;
        Gson gson = new Gson();

        if (apiName != null) {

            try {
                String[] blacklist = DatabaseUtils.getBlacklistNumbers(apiName);

                jsonreturn = "{" + "\"Success\":" + "{"
                        + "\"messageId\":\"" + "Blacklist result" + "\","
                        + "\"text\":\"" + "Blacklist numbers were retrieved from the system" + "\","
                        + "\"variables\":" + gson.toJson(blacklist)
                        + "}}";

                return Response.status(200).entity(jsonreturn).build();
            } catch (NamingException ex) {
                java.util.logging.Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);

                jsonreturn = "{" + "\"Failed\":" + "{"
                        + "\"messageId\":\"" + "Blacklist result" + "\","
                        + "\"text\":\"" + "Blacklist numbers could not be retrieved" + "\""
                        + "}}";
                return Response.status(400).entity(jsonreturn).build();
            } catch (SQLException e) {
                jsonreturn = "{" + "\"Failed\":" + "{"
                        + "\"messageId\":\"" + "Blacklist result" + "\","
                        + "\"text\":\"" + "Blacklist numbers could not be retrieved" + "\""
                        + "}}";
                return Response.status(400).entity(jsonreturn).build();
            }
        } else {
            jsonreturn = "{" + "\"Failed\":" + "{"
                    + "\"messageId\":\"" + "Blacklist result" + "\","
                    + "\"text\":\"" + "Blacklist numbers could not be retrieved" + "\""
                    + "}}";
            return Response.status(400).entity(jsonreturn).build();
        }
    }

    /**
     * Removes from the blacklist
     *
     * {
     * "apiName":"USSD"
     * }
     */
    @POST
    @Path("/RemoveFromBlacklist/{MSISDN}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response removeFromBlacklist(@PathParam("MSISDN") String msisdn, String jsonBody) throws SQLException {

        String jsonreturn = null; 
        Gson gson = new GsonBuilder().serializeNulls().create();
        
        RemoveRequest removeReq = gson.fromJson(jsonBody, RemoveRequest.class);
        
        String apiName = removeReq.getAPIName();
        
        if (apiName != null) {

            try {
                DatabaseUtils.removeBlacklistNumbers(apiName, msisdn);

                jsonreturn = "{" + "\"Success\":" + "{"
                   + "\"messageId\":\"" + "Remove Number" + "\","
                   + "\"text\":\""+  "Blacklist number successfully removed "+ "\","
                   + "\"variables\":\"" + msisdn + "\""
                   + "}}"; 

                return Response.status(200).entity(jsonreturn).build();
            } catch (NamingException ex) {
                java.util.logging.Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);

                jsonreturn = "{" + "\"Failed\":" + "{"
                   + "\"messageId\":\"" + "Remove Number" + "\","
                   + "\"text\":\""+  "Blacklist number could not be removed "+ "\","
                   + "\"variables\":\"" + msisdn + "\""
                   + "}}"; 
                return Response.status(400).entity(jsonreturn).build();
            }  catch (SQLException e) {
                jsonreturn = "{" + "\"Failed\":" + "{"
                   + "\"messageId\":\"" + "Remove Number" + "\","
                   + "\"text\":\""+  "Blacklist number could not be removed "+ "\","
                   + "\"variables\":\"" + msisdn + "\""
                   + "}}"; 
                return Response.status(400).entity(jsonreturn).build();
            }   
        }
        else{
            jsonreturn = "{" + "\"Failed\":" + "{"
                   + "\"messageId\":\"" + "Remove Number" + "\","
                   + "\"text\":\""+  "Blacklist number could not be removed "+ "\","
                   + "\"variables\":\"" + msisdn + "\""
                   + "}}"; 
                return Response.status(400).entity(jsonreturn).build();
        }
        
    }

     /**
     * GET method for creating an instance of QueriesResource
     *
     * @param address,requestedAccuracy representation for the new resource
     * @return an HTTP response with content of the created resource
     */
    @POST
    @Path("/whitelist/{MSISDN}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response provisionWhiteListedNumber(@PathParam("MSISDN") String msisdn, String jsonBody) throws SQLException {

        String jsonreturn = null; 
        Gson gson = new GsonBuilder().serializeNulls().create();
        
        WhiteList whiteListReq = gson.fromJson(jsonBody, WhiteList.class);
        
        String subscriptionID = whiteListReq.getSubscriptionID();
        String apiID = whiteListReq.getAPIID();
        String applicationID = whiteListReq.getApplicationID();
        
        if (subscriptionID != null && apiID != null && applicationID != null) {
                
            try {
                DatabaseUtils.WriteWhitelistNumbers(msisdn,subscriptionID,apiID,applicationID);

                 jsonreturn = "{" + "\"Success\":" + "{"
                   + "\"messageId\":\"" + "WhiteListed Number" + "\","
                   + "\"text\":\""+  "WhiteListed Number Successfully Added to the system "+ "\","
                   + "\"variables\":\"" + msisdn + "\""
                   + "}}";


                return Response.status(200).entity(jsonreturn).build();
            } catch (NamingException ex) {
                java.util.logging.Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);

                jsonreturn = "{" + "\"Failed\":" + "{"
                   + "\"messageId\":\"" + "Whitelisted Number" + "\","
                   + "\"text\":\""+  "Whitelisted Number could not be added to the system "+ "\","
                   + "\"variables\":\"" + msisdn + "\""
                   + "}}"; 
                return Response.status(400).entity(jsonreturn).build();
            }  catch (SQLException e) {
                jsonreturn = "{" + "\"Failed\":" + "{"
                   + "\"messageId\":\"" + "Whitelisted Number" + "\","
                   + "\"text\":\""+  "Whitelisted Number could not be added to the system "+ "\","
                   + "\"variables\":\"" + msisdn + "\""
                   + "}}"; 
                return Response.status(400).entity(jsonreturn).build();
            }      


        }
    
    
        else{
                jsonreturn = "{" + "\"Failed\":" + "{"
                       + "\"messageId\":\"" + "Whitelisted Number" + "\","
                       + "\"text\":\""+  "Whitelisted Number could not be added to the system "+ "\","
                       + "\"variables\":\"" + msisdn + "\""
                       + "}}"; 
                    return Response.status(400).entity(jsonreturn).build();
        }
    
    }
    
    //------------------------------------------------------PRIYANKA_06608--------------------------------------
    /**
     * Adds a list of subscribers to whitelist
     *
     * {
     * "apiID":"1", "apiName":"USSD", "userID":"admin"
     * "msisdnList":["94777000001","94777000002","94777000003"] 
     * }
     */
    @POST
    @Path("/Whitelist")
    @Consumes("application/json")
    @Produces("application/json")
    public Response bulkWhitelist(String jsonBody) throws SQLException {

        String jsonreturn = null;
        Gson gson = new GsonBuilder().serializeNulls().create();
        System.out.println("mmmmmmmmmmm>" + jsonBody);
        WhiteListBulk whiteListReq = gson.fromJson(jsonBody, WhiteListBulk.class);

        String appId = whiteListReq.getAppId();
        String apiId = whiteListReq.getApiId();
//        String apiName = whiteListReq.getAPIName();
        String userID = whiteListReq.getUserID();
        String[] msisdnList = whiteListReq.getMsisdnList();

        gson = new Gson();

        //if (apiID != null && apiName != null && userID != null && msisdnList != null) {
        if (userID != null && msisdnList != null) {
            msisdnList = removeNullMsisdnValues(msisdnList);

            try {
                DatabaseUtils.writeWhitelistNumbersBulk(msisdnList, apiId, userID, appId);

                jsonreturn = "{" + "\"Success\":" + "{"
                        + "\"messageId\":\"" + "Whitelist Numbers" + "\","
                        + "\"text\":\"" + "Whitelist Numbers Successfully Added to the system " + "\","
                        + "\"variables\":" + gson.toJson(msisdnList)
                        + "}}";

                return Response.status(200).entity(jsonreturn).build();
            } catch (NamingException ex) {
                java.util.logging.Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);

                jsonreturn = "{" + "\"Failed\":" + "{"
                        + "\"messageId\":\"" + "Whitelist Numbers" + "\","
                        + "\"text\":\"" + "Whitelist Numbers could not be added to the system " + "\","
                        + "\"variables\":" + gson.toJson(msisdnList)
                        + "}}";
                return Response.status(400).entity(jsonreturn).build();
            } catch (SQLException e) {
                jsonreturn = "{" + "\"Failed\":" + "{"
                        + "\"messageId\":\"" + "Whitelist Numbers" + "\","
                        + "\"text\":\"" + "Whitelist Numbers could not be added to the system " + "\","
                        + "\"variables\":" + gson.toJson(msisdnList)
                        + "}}";
                return Response.status(400).entity(jsonreturn).build();
            } catch (Exception e) {
                jsonreturn = "{" + "\"Failed\":" + "{"
                        + "\"messageId\":\"" + "Whitelist Numbers" + "\","
                        + "\"text\":\"" + e.getMessage() + "\","
                        + "\"variables\":" + gson.toJson(msisdnList)
                        + "}}";
                return Response.status(400).entity(jsonreturn).build();
            }
        } else {
            jsonreturn = "{" + "\"Failed\":" + "{"
                    + "\"messageId\":\"" + "Whitelist Numbers" + "\","
                    + "\"text\":\"" + "Whitelist Numbers could not be added to the system " + "\","
                    + "\"variables\":" + gson.toJson(msisdnList)
                    + "}}";
            return Response.status(400).entity(jsonreturn).build();
        }

    }

    /**
     * get all subscribers
     */
    @POST
    @Path("/getSubscribers")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getSubscribers(String jsonBody) {
		try {
			String subscribersJson = DatabaseUtils.getSubscribers();
			return Response.status(200).entity(subscribersJson).build();
		} catch (Exception ex) {
			java.util.logging.Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
			return Response.status(400).entity("{\"error\":\"true\"}").build();
		}
    }
    
    /**
     * get all apps of subscriber
     */
    @POST
    @Path("/getApps")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getApps(String jsonBody) {
    	try {
    		Gson gson = new GsonBuilder().serializeNulls().create();
    		Id whiteListReq = gson.fromJson(jsonBody, Id.class);

            String subscriberId = whiteListReq.getId();
    		String json = DatabaseUtils.getApps(subscriberId);
    		return Response.status(200).entity(json).build();
    	} catch (Exception ex) {
    		java.util.logging.Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
    		return Response.status(400).entity("{\"error\":\"true\"}").build();
    	}
    }
    
    /**
     * get apis of app
     */
    @POST
    @Path("/getApis")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getApis(String jsonBody) {
    	try {
    		Gson gson = new GsonBuilder().serializeNulls().create();
    		Id whiteListReq = gson.fromJson(jsonBody, Id.class);

            String appId = whiteListReq.getId();
    		String json = DatabaseUtils.getApis(appId);
    		return Response.status(200).entity(json).build();
    	} catch (Exception ex) {
    		java.util.logging.Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
    		return Response.status(400).entity("{\"error\":\"true\"}").build();
    	}
    }


    @POST
    @Path("/GetWhiteList")
    @Consumes("application/json")
    @Produces("text/plain")
    public Response getWhiteListNumbers() throws SQLException {

        String jsonreturn = null;
        Gson gson = new Gson();

            try {
                String[] whiteListNumbers = DatabaseUtils.getWhiteListNumbers();

                    jsonreturn = "{" + "\"Success\":" + "{"
                            + "\"messageId\":\"" + "Whitelist result" + "\","
                            + "\"text\":\"" + "WhiteList numbers were retrieved from the system" + "\","
                            + "\"variables\":" + gson.toJson(whiteListNumbers)
                            + "}}";

                    return Response.status(200).entity(jsonreturn).build();

            } catch (NamingException ex) {

                java.util.logging.Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
                jsonreturn = "{" + "\"Failed\":" + "{"
                        + "\"messageId\":\"" + "Whitelist result" + "\","
                        + "\"text\":\"" + "WhiteList numbers could not be retrieved" + "\""
                        + "}}";
                return Response.status(400).entity(jsonreturn).build();

            } catch (SQLException e) {

                java.util.logging.Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, e);
                jsonreturn = "{" + "\"Failed\":" + "{"
                        + "\"messageId\":\"" + "Whitelist result" + "\","
                        + "\"text\":\"" + "WhiteList numbers could not be retrieved" + "\""
                        + "}}";
                return Response.status(400).entity(jsonreturn).build();
            }

    }

     @POST
     @Path("/RemoveFromWhiteList/{MSISDN}")
     @Consumes("application/json")
     @Produces("application/json")
     public Response removeFromWhiteListNumbers(@PathParam("MSISDN") String msisdn) throws SQLException {

     String jsonreturn = null;
     Gson gson = new GsonBuilder().serializeNulls().create();

     try {
        DatabaseUtils.removeWhitelistNumber(msisdn);

             jsonreturn = "{" + "\"Success\":" + "{"
                 + "\"messageId\":\"" + "Remove Number" + "\","
                 + "\"text\":\""+  "Whitelist number successfully removed "+ "\","
                 + "\"variables\":\"" + msisdn + "\""
                 + "}}";
         return Response.status(200).entity(jsonreturn).build();

     } catch (NamingException ex) {
        java.util.logging.Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);

            jsonreturn = "{" + "\"Failed\":" + "{"
                 + "\"messageId\":\"" + "Remove Number" + "\","
                 + "\"text\":\""+  "Blacklist number could not be removed "+ "\","
                 + "\"variables\":\"" + msisdn + "\""
                 + "}}";
             return Response.status(400).entity(jsonreturn).build();

     }  catch (SQLException e) {
         jsonreturn = "{" + "\"Failed\":" + "{"
         + "\"messageId\":\"" + "Remove Number" + "\","
         + "\"text\":\""+  "Blacklist number could not be removed "+ "\","
         + "\"variables\":\"" + msisdn + "\""
         + "}}";
         return Response.status(400).entity(jsonreturn).build();

     }

     }

}

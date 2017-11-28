package com.wso2telco.workflow.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.codehaus.jackson.annotate.JsonProperty;
import org.json.JSONObject;

import com.google.gson.Gson;

public class JesonConvertor {

	public static void main(String[] args) {
		//
		Map<String, String> yourmap = new HashMap<String, String>();
		yourmap.put("Nuwan", "1");
		yourmap.put("Nalin", "11");
		yourmap.put("Senarath", "111");
		yourmap.put("Bandara", "1111");
		yourmap.put("Disath", "111111");
		JSONObject obj = new JSONObject(yourmap);

		List<ResponseItem> items = new ArrayList<ResponseItem>();
		items.add(new ResponseItem() {
			public String getAttribute() {
				return "Nuwan";
			}

			public String getAttributeVal() {
				return "1";
			}
		});

		items.add(new ResponseItem() {
			public String getAttribute() {
				return "Nalin";
			}

			public String getAttributeVal() {
				return "12";
			}
		});
		


		Response res = Response.status(Response.Status.OK).entity(items).build();

//		System.out.println(gson.toJson(items));
		System.out.println(res);
	}

}

 

interface ResponseItem {

	@JsonProperty("attribute")
	public String getAttribute();

	@JsonProperty("attributeVal")
	public String getAttributeVal() ;
}
/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licences this file to you under  the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.dep.mediator.internal;

import org.json.JSONObject;

 
// TODO: Auto-generated Javadoc
/**
 * The Class APICall.
 */
public class APICall {
	
	/** The uri. */
	private String uri = "";
	
	/** The body. */
	private JSONObject body;

	 
	/**
	 * Instantiates a new API call.
	 */
	public APICall() {
	}
	
	 
	/**
	 * Instantiates a new API call.
	 *
	 * @param uri the uri
	 * @param body the body
	 */
	public APICall(String uri, JSONObject body) {
		super();
		this.uri = uri;
		this.body = body;
	}

	/**
	 * Gets the uri.
	 *
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * Sets the uri.
	 *
	 * @param uri the new uri
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * Gets the body.
	 *
	 * @return the body
	 */
	public JSONObject getBody() {
		return body;
	}

	/**
	 * Sets the body.
	 *
	 * @param body the new body
	 */
	public void setBody(JSONObject body) {
		this.body = body;
	}
}

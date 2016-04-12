/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *  
 *  WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
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
package com.wso2telco.custom.hostobjects;

// TODO: Auto-generated Javadoc
/**
 * The Class SPObject.
 */
public class SPObject {
	
	/** The app id. */
	private Integer appId;
	
	/** The sp name. */
	private String spName;
	
	/** The user name. */
	private String userName;
	
	/** The token. */
	private String token;
	
	/** The key. */
	private String key;
	
	/** The secret. */
	private String secret;

	/**
	 * Gets the token.
	 *
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Sets the token.
	 *
	 * @param token the new token
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * Gets the key.
	 *
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Sets the key.
	 *
	 * @param key the new key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Gets the secret.
	 *
	 * @return the secret
	 */
	public String getSecret() {
		return secret;
	}

	/**
	 * Sets the secret.
	 *
	 * @param secret the new secret
	 */
	public void setSecret(String secret) {
		this.secret = secret;
	}

	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the user name.
	 *
	 * @param userName the new user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Gets the app id.
	 *
	 * @return the app id
	 */
	public Integer getAppId() {
		return appId;
	}

	/**
	 * Sets the app id.
	 *
	 * @param appId the new app id
	 */
	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	/**
	 * Gets the sp name.
	 *
	 * @return the sp name
	 */
	public String getSpName() {
		return spName;
	}

	/**
	 * Sets the sp name.
	 *
	 * @param spName the new sp name
	 */
	public void setSpName(String spName) {
		this.spName = spName;
	}

}

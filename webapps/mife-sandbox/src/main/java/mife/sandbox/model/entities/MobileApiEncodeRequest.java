/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mife.sandbox.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * This class represent a request for encoding a consumer-key: consumer secret pair
 */
@Entity
@Table(name = "mobileidapiencoderequest")
public class MobileApiEncodeRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mobIdApiId")
	private int mobIdApiId;

	@Column(name = "consumerkey")
	private String key;

	@Column(name = "consumersecret")
	private String secret;

	@Column(name = "authcode")
	private String authCode;

	@Column(name = "granttype")
	private String granttype;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "scope")
	private String scope;

	@Column(name = "user")
	private String user;

	@Column(name = "refreshToken")
	private String refreshToken;
	
	@Column(name = "accessToken")
	private String accessToken;
	
	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getGranttype() {
		return granttype;
	}

	public void setGranttype(String granttype) {
		this.granttype = granttype;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * @return the mobIdApiId
	 */
	public int getMobIdApiId() {
		return mobIdApiId;
	}

	/**
	 * @param mobIdApiId
	 *            the mobIdApiId to set
	 */
	public void setMobIdApiId(int mobIdApiId) {
		this.mobIdApiId = mobIdApiId;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the secret
	 */
	public String getSecret() {
		return secret;
	}

	/**
	 * @param secret
	 *            the secret to set
	 */
	public void setSecret(String secret) {
		this.secret = secret;
	}

	/**
	 * @return the authCode
	 */
	public String getAuthCode() {
		return authCode;
	}

	/**
	 * @param authCode
	 *            the authCode to set
	 */
	public void setAuthCode(String authCode) {
		this.authCode = authCode;

        }
}
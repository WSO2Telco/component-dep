package com.wso2telco.dep.operator.onboarding.dao.model.domain;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "operators")
public class Operator {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;
	
	@Column(name = "operatorname", unique = true)
	private String operatorname;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "created")
	private String created;
	
	@Column(name = "created_date")
	private Date created_date;
	
	@Column(name = "lastupdated")
	private String lastupdated;
	
	@Column(name = "lastupdated_date")
	private Date lastupdated_date;
	
	@Column(name = "refreshtoken")
	private String refreshtoken;
	
	@Column(name = "tokenvalidity")
	private double tokenvalidity;
	
	@Column(name = "tokentime")
	private double tokentime;
	
	@Column(name = "token")
	private String token;
	
	@Column(name = "tokenurl")
	private String tokenurl;
	
	@Column(name = "tokenauth")
	private String tokenauth;
	
	@Column(name = "mnc")
	private String mnc;
	
	@Column(name = "mcc")
	private String mcc;
	
	@Column(name = "status")
	private int status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOperatorname() {
		return operatorname;
	}

	public void setOperatorname(String operatorname) {
		this.operatorname = operatorname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public String getLastupdated() {
		return lastupdated;
	}

	public void setLastupdated(String lastupdated) {
		this.lastupdated = lastupdated;
	}

	public Date getLastupdated_date() {
		return lastupdated_date;
	}

	public void setLastupdated_date(Date lastupdated_date) {
		this.lastupdated_date = lastupdated_date;
	}

	public String getRefreshtoken() {
		return refreshtoken;
	}

	public void setRefreshtoken(String refreshtoken) {
		this.refreshtoken = refreshtoken;
	}

	public double getTokenvalidity() {
		return tokenvalidity;
	}

	public void setTokenvalidity(double tokenvalidity) {
		this.tokenvalidity = tokenvalidity;
	}

	public double getTokentime() {
		return tokentime;
	}

	public void setTokentime(double tokentime) {
		this.tokentime = tokentime;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTokenurl() {
		return tokenurl;
	}

	public void setTokenurl(String tokenurl) {
		this.tokenurl = tokenurl;
	}

	public String getTokenauth() {
		return tokenauth;
	}

	public void setTokenauth(String tokenauth) {
		this.tokenauth = tokenauth;
	}
	
	public String getMnc() {
		return mnc;
	}

	public void setMnc(String mnc) {
		this.mnc = mnc;
	}

	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}



}

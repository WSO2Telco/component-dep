package com.wso2telco.dep.operatorservice.model;


// TODO: Auto-generated Javadoc
/**
 * The Class Operator.
 */
public class OperatorApplicationDTO {

    /** The isactive. */
    int id,applicationid, operatorid,isactive;

    /** The operatorname. */
    String operatorname;
    String apiName;
    public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	/** The note. */
    String note;

    /** The created. */
    String created;

    /** The created_date. */
    String created_date;

    /** The lastupdated. */
    String lastupdated;

    /** The lastupdated_date. */
    String lastupdated_date;

    /** The refreshtoken. */
    String refreshtoken;

    /** The tokenvalidity. */
    long tokenvalidity;

    /** The tokentime. */
    long tokentime;

    /** The token. */
    String token;

    /** The tokenurl. */
    String tokenurl;

    /** The tokenauth. */
    String tokenauth;

    /**
     * Gets the id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the applicationid.
     *
     * @return the applicationid
     */
    public int getApplicationid() {
        return applicationid;
    }

    /**
     * Gets the operatorname.
     *
     * @return the operatorname
     */
    public String getOperatorname() {
        return operatorname;
    }

    /**
     * Sets the operatorname.
     *
     * @param operatorname the new operatorname
     */
    public void setOperatorname(String operatorname) {
        this.operatorname = operatorname;
    }

    /**
     * Sets the applicationid.
     *
     * @param applicationid the new applicationid
     */
    public void setApplicationid(int applicationid) {
        this.applicationid = applicationid;
    }

    /**
     * Gets the operatorid.
     *
     * @return the operatorid
     */
    public int getOperatorid() {
        return operatorid;
    }

    /**
     * Sets the operatorid.
     *
     * @param operatorid the new operatorid
     */
    public void setOperatorid(int operatorid) {
        this.operatorid = operatorid;
    }

    /**
     * Gets the isactive.
     *
     * @return the isactive
     */
    public int getIsactive() {
        return isactive;
    }

    /**
     * Sets the isactive.
     *
     * @param isactive the new isactive
     */
    public void setIsactive(int isactive) {
        this.isactive = isactive;
    }

    /**
     * Gets the note.
     *
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * Sets the note.
     *
     * @param note the new note
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * Gets the created.
     *
     * @return the created
     */
    public String getCreated() {
        return created;
    }

    /**
     * Sets the created.
     *
     * @param created the new created
     */
    public void setCreated(String created) {
        this.created = created;
    }

    /**
     * Gets the created_date.
     *
     * @return the created_date
     */
    public String getCreated_date() {
        return created_date;
    }

    /**
     * Sets the created_date.
     *
     * @param created_date the new created_date
     */
    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    /**
     * Gets the lastupdated.
     *
     * @return the lastupdated
     */
    public String getLastupdated() {
        return lastupdated;
    }

    /**
     * Sets the lastupdated.
     *
     * @param lastupdated the new lastupdated
     */
    public void setLastupdated(String lastupdated) {
        this.lastupdated = lastupdated;
    }

    /**
     * Gets the lastupdated_date.
     *
     * @return the lastupdated_date
     */
    public String getLastupdated_date() {
        return lastupdated_date;
    }

    /**
     * Sets the lastupdated_date.
     *
     * @param lastupdated_date the new lastupdated_date
     */
    public void setLastupdated_date(String lastupdated_date) {
        this.lastupdated_date = lastupdated_date;
    }

    /**
     * Gets the refreshtoken.
     *
     * @return the refreshtoken
     */
    public String getRefreshtoken() {
        return refreshtoken;
    }

    /**
     * Sets the refreshtoken.
     *
     * @param refreshtoken the new refreshtoken
     */
    public void setRefreshtoken(String refreshtoken) {
        this.refreshtoken = refreshtoken;
    }

    /**
     * Gets the tokenvalidity.
     *
     * @return the tokenvalidity
     */
    public double getTokenvalidity() {
        return tokenvalidity;
    }

    /**
     * Sets the tokenvalidity.
     *
     * @param tokenvalidity the new tokenvalidity
     */
    public void setTokenvalidity(long tokenvalidity) {
        this.tokenvalidity = tokenvalidity;
    }

    /**
     * Gets the tokentime.
     *
     * @return the tokentime
     */
    public double getTokentime() {
        return tokentime;
    }

    /**
     * Sets the tokentime.
     *
     * @param tokentime the new tokentime
     */
    public void setTokentime(long tokentime) {
        this.tokentime = tokentime;
    }

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
     * Gets the tokenurl.
     *
     * @return the tokenurl
     */
    public String getTokenurl() {
        return tokenurl;
    }

    /**
     * Sets the tokenurl.
     *
     * @param tokenurl the new tokenurl
     */
    public void setTokenurl(String tokenurl) {
        this.tokenurl = tokenurl;
    }

    /**
     * Gets the tokenauth.
     *
     * @return the tokenauth
     */
    public String getTokenauth() {
        return tokenauth;
    }

    /**
     * Sets the tokenauth.
     *
     * @param tokenauth the new tokenauth
     */
    public void setTokenauth(String tokenauth) {
        this.tokenauth = tokenauth;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apiName == null) ? 0 : apiName.hashCode());
		result = prime * result + applicationid;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OperatorApplicationDTO other = (OperatorApplicationDTO) obj;
		if (apiName == null) {
			if (other.apiName != null)
				return false;
		} else if (!apiName.equals(other.apiName))
			return false;
		if (applicationid != other.applicationid)
			return false;
		return true;
	}

}

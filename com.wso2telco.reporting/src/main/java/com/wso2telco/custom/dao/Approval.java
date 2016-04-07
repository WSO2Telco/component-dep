
package com.wso2telco.custom.dao;


public class Approval {

    String application_id;
    String type;
    String name;
    int operatorid;
    String isactive;
    String tier_id;
    String api_name;
    String api_version;
    String created;
    String last_updated;

    public Approval(String application_id, String type, String name, int operatorid, String isactive, String tier_id,
            String api_name, String api_version, String created,String lastupdated) {
        this.application_id = application_id;
        this.type = type;
        this.name = name;
        this.operatorid = operatorid;
        this.isactive = isactive;
        this.tier_id = tier_id;
        this.api_name = api_name;
        this.api_version = api_version;
        this.created = created;
        this.last_updated = lastupdated;
    }
    
    public String getApplication_id() {
        return application_id;
    }

    public void setApplication_id(String application_id) {
        this.application_id = application_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOperatorid() {
        return operatorid;
    }

    public void setOperatorid(int operatorid) {
        this.operatorid = operatorid;
    }

    public String getIsactive() {
        return isactive;
    }

    public void setIsactive(String isactive) {
        this.isactive = isactive;
    }

    public String getTier_id() {
        return tier_id;
    }

    public void setTier_id(String tier_id) {
        this.tier_id = tier_id;
    }

    public String getApi_name() {
        return api_name;
    }

    public void setApi_name(String api_name) {
        this.api_name = api_name;
    }

    public String getApi_version() {
        return api_version;
    }

    public void setApi_version(String api_version) {
        this.api_version = api_version;
    }

    public String getCreated() {
        return (created == null)? "" : created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLast_updated() {
        return (last_updated == null)? "" : last_updated;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }  
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mife.sandbox.model.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Dialog
 */

@Entity
@Table(name="mobileidapirequest")
public class MobileIdApiRequest {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int mobIdApiId;
    
    @Column(name="sub")
    private String sub;
    
    @Column(name="email")
    private String email;
    
    @Column(name="name")
    private String name;
    
    @Column(name="family_name")
    private String family_name;
    
    @Column(name="preferred_username")
    private String preferred_username;
    
    @Column(name="given_name")
    private String given_name;
    
    @Column(name="tran_oper_status")
    private String transactionstatus;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name="effect_date")
    private Date date;


    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName="id")
    private User user;

    public int getMobIdApiId() {
        return mobIdApiId;
    }

    public void setMobIdApiId(int mobIdApiId) {
        this.mobIdApiId = mobIdApiId;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily_name() {
        return family_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    public String getPreferred_username() {
        return preferred_username;
    }

    public void setPreferred_username(String preferred_username) {
        this.preferred_username = preferred_username;
    }

    public String getGiven_name() {
        return given_name;
    }

    public void setGiven_name(String given_name) {
        this.given_name = given_name;
    }

    public String getTransactionstatus() {
        return transactionstatus;
    }

    public void setTransactionstatus(String transactionstatus) {
        this.transactionstatus = transactionstatus;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    
    
}

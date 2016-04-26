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

/**
 *
 * @author Dialog
 */

@Entity
@Table(name="locationtransactionlog")
public class LocationRequestLog {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int locId;
    
    private String address;
    
    @Column(name="requested_accuracy")
    private Double requestedAccuracy;
    
    @Column(name="tran_oper_status")
    private String transactionstatus;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name="effect_date")
    private Date date;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName="id")
    private User user;

    /**
     * @return the locId
     */
    public int getLocId() {
        return locId;
    }

    /**
     * @param locId the locId to set
     */
    public void setLocId(int locId) {
        this.locId = locId;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the requestedAccuracy
     */
    public Double getRequestedAccuracy() {
        return requestedAccuracy;
    }

    /**
     * @param requestedAccuracy the requestedAccuracy to set
     */
    public void setRequestedAccuracy(Double requestedAccuracy) {
        this.requestedAccuracy = requestedAccuracy;
    }

    /**
     * @return the transactionstatus
     */
    public String getTransactionstatus() {
        return transactionstatus;
    }

    /**
     * @param transactionstatus the transactionstatus to set
     */
    public void setTransactionstatus(String transactionstatus) {
        this.transactionstatus = transactionstatus;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }
}

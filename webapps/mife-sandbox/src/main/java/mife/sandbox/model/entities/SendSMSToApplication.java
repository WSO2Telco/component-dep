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
 * @author User
 */

@Entity
@Table(name="send_sms_to_application")
public class SendSMSToApplication {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="sms_id")
    private int smsId;
    
    @Column(name="sender_address")
    private String senderAddress;
    
    @Column(name="destination_address")
    private String destinationAddress;
    
    private String message;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name="effect_date")
    private Date date;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName="id")
    private User user;

    /**
     * @return the smsId
     */
    public int getSmsId() {
        return smsId;
    }

    /**
     * @param smsId the smsId to set
     */
    public void setSmsId(int smsId) {
        this.smsId = smsId;
    }

    /**
     * @return the senderAddress
     */
    public String getSenderAddress() {
        return senderAddress;
    }

    /**
     * @param senderAddress the senderAddress to set
     */
    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    /**
     * @return the destinationAddress
     */
    public String getDestinationAddress() {
        return destinationAddress;
    }

    /**
     * @param destinationAddress the destinationAddress to set
     */
    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
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

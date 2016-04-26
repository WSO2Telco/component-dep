/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mife.sandbox.model.entities;

import java.io.Serializable;
import java.sql.Time;
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
@Table(name="subscribe_sms_request")
public class SubscribeSMSRequest implements Serializable {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="subscribe_id")
    private int subscribeId;
    
    @Column(name="destination_address")
    private String destinationAddress;
    
    @Column(name="notify_url")
    private String notifyURL;
    
    @Column(name="callback_data")
    private String callbackData;
    
    private String criteria;
    
    @Column(name="notification_format")
    private String notificationFormat;
    
    @Column(name="client_correlator")
    private String clientCorrelator;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name="effect_date")
    private Date Date;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName="id")
    private User user;
    /**
     * @return the subscribeId
     */
    public int getSubscribeId() {
        return subscribeId;
    }

    /**
     * @param subscribeId the subscribeId to set
     */
    public void setSubscribeId(int subscribeId) {
        this.subscribeId = subscribeId;
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
     * @return the notifyURL
     */
    public String getNotifyURL() {
        return notifyURL;
    }

    /**
     * @param notifyURL the notifyURL to set
     */
    public void setNotifyURL(String notifyURL) {
        this.notifyURL = notifyURL;
    }

    /**
     * @return the callbackData
     */
    public String getCallbackData() {
        return callbackData;
    }

    /**
     * @param callbackData the callbackData to set
     */
    public void setCallbackData(String callbackData) {
        this.callbackData = callbackData;
    }

    /**
     * @return the criteria
     */
    public String getCriteria() {
        return criteria;
    }

    /**
     * @param criteria the criteria to set
     */
    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    /**
     * @return the notificationFormat
     */
    public String getNotificationFormat() {
        return notificationFormat;
    }

    /**
     * @param notificationFormat the notificationFormat to set
     */
    public void setNotificationFormat(String notificationFormat) {
        this.notificationFormat = notificationFormat;
    }

    /**
     * @return the clientCorrelator
     */
    public String getClientCorrelator() {
        return clientCorrelator;
    }

    /**
     * @param clientCorrelator the clientCorrelator to set
     */
    public void setClientCorrelator(String clientCorrelator) {
        this.clientCorrelator = clientCorrelator;
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

    /**
     * @return the Date
     */
    public Date getDate() {
        return Date;
    }

    /**
     * @param Date the Date to set
     */
    public void setDate(Date Date) {
        this.Date = Date;
    }

}

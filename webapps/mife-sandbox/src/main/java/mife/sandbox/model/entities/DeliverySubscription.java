/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mife.sandbox.model.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="sms_delivery_subscription")
public class DeliverySubscription implements Serializable{
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    
    @Column(name="sender_address")
    private String senderAddress;
    
    @Column(name="notify_url")
    private String notifyUrl;
    
    @Column(name="filter")
    private String filterCriteria;
    
    @Column(name="callbackdata")
    private String callbackData;
    
    @Column(name="clientcorrelator")
    private String clientCorrelator;
    
    @Column(name="request")
    private String requestData;
    
    @Column(name="sub_status")
    private int subStatus;
    
    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName="id")
    private User user;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
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
     * @return the notifyUrl
     */
    public String getNotifyUrl() {
        return notifyUrl;
    }

    /**
     * @param notifyUrl the notifyUrl to set
     */
    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    /**
     * @return the filterCriteria
     */
    public String getFilterCriteria() {
        return filterCriteria;
    }

    /**
     * @param filterCriteria the filterCriteria to set
     */
    public void setFilterCriteria(String filterCriteria) {
        this.filterCriteria = filterCriteria;
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
     * @return the requestData
     */
    public String getRequestData() {
        return requestData;
    }

    /**
     * @param requestData the requestData to set
     */
    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    /**
     * @return the subStatus
     */
    public int getSubStatus() {
        return subStatus;
    }

    /**
     * @param subStatus the subStatus to set
     */
    public void setSubStatus(int subStatus) {
        this.subStatus = subStatus;
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

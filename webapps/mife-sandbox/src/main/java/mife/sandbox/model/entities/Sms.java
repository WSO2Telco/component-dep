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

/**
 *
 * @author User
 */
@Entity
@Table(name="sms")
public class Sms implements Serializable {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    
    @Column(name="notificationDelay")
    private String notificationDelay;
    
    @Column(name="maxNotifications")
    private String maxNotifications;
    
    @Column(name="deliveryStatus")
    private String deliveryStatus;
    
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
     * @return the notificationDelay
     */
    public String getNotificationDelay() {
        return notificationDelay;
    }

    /**
     * @param notificationDelay the notificationDelay to set
     */
    public void setNotificationDelay(String notificationDelay) {
        this.notificationDelay = notificationDelay;
    }

    /**
     * @return the maxNotifications
     */
    public String getMaxNotifications() {
        return maxNotifications;
    }

    /**
     * @param maxNotifications the maxNotifications to set
     */
    public void setMaxNotifications(String maxNotifications) {
        this.maxNotifications = maxNotifications;
    }

    /**
     * @return the deliveryStatus
     */
    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    /**
     * @param deliveryStatus the deliveryStatus to set
     */
    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
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

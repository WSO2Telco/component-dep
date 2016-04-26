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
@Table(name="payment_gen")
public class Payment implements Serializable {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    
    @Column(name="delivery_status")
    private String deliveryStatus;
    
    @Column(name="max_tx_perday")
    private String maxTxPerDay;
    
    @Column(name="max_pay_amount")
    private String maxPayAmount;
    
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
     * @return the maxTxPerDay
     */
    public String getMaxTxPerDay() {
        return maxTxPerDay;
    }

    /**
     * @param maxTxPerDay the maxTxPerDay to set
     */
    public void setMaxTxPerDay(String maxTxPerDay) {
        this.maxTxPerDay = maxTxPerDay;
    }

    /**
     * @return the maxPayAmount
     */
    public String getMaxPayAmount() {
        return maxPayAmount;
    }

    /**
     * @param maxPayAmount the maxPayAmount to set
     */
    public void setMaxPayAmount(String maxPayAmount) {
        this.maxPayAmount = maxPayAmount;
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

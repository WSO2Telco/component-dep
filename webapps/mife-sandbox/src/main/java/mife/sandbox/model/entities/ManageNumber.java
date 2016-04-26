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
@Table(name = "numbers")
public class ManageNumber implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name="number")
    private String Number;
    
    @Column(name="num_description")
    private String description;
    
    @Column(name="num_balance")
    private double balance;
    
    @Column(name="reserved_amount", columnDefinition = "double default true")
    private double reserved_amount;
    
    @Column(name="num_status")
    private int status;
    
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
     * @return the ManageNumber
     */
    public String getNumber() {
        return Number;
    }

    /**
     * @param ManageNumber the ManageNumber to set
     */
    public void setNumber(String Number) {
        this.Number = Number;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * @param balance the balance to set
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the reserved_amount
     */
    public double getReserved_amount() {
        return reserved_amount;
    }

    /**
     * @param reserved_amount the reserved_amount to set
     */
    public void setReserved_amount(double reserved_amount) {
        this.reserved_amount = reserved_amount;
    }
}

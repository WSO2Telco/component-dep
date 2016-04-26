/*
 * Smsparam.java
 * May 27, 2014  4:30:42 PM
 * Roshan.Saputhanthri
 *
 * Copyright (C) Dialog Axiata PLC. All Rights Reserved.
 */

package mife.sandbox.model.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * <TO-DO> <code>Smsparam</code>
 * @version $Id: Smsparam.java,v 1.00.000
 */
@Entity
@Table(name = "smsparam")
@NamedQueries({
    @NamedQuery(name = "Smsparam.findAll", query = "SELECT s FROM Smsparam s")})
public class Smsparam implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "deliveryStatus")
    private String deliveryStatus;
    @Column(name = "maxNotifications")
    private String maxNotifications;
    @Column(name = "notificationDelay")
    private String notificationDelay;
    @Column(name = "userid")
    private Integer userid;
    @Column(name = "created")
    private String created;
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "lastupdated")
    private String lastupdated;
    @Column(name = "lastupdated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastupdatedDate;

    public Smsparam() {
    }

    public Smsparam(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getMaxNotifications() {
        return maxNotifications;
    }

    public void setMaxNotifications(String maxNotifications) {
        this.maxNotifications = maxNotifications;
    }

    public String getNotificationDelay() {
        return notificationDelay;
    }

    public void setNotificationDelay(String notificationDelay) {
        this.notificationDelay = notificationDelay;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastupdated() {
        return lastupdated;
    }

    public void setLastupdated(String lastupdated) {
        this.lastupdated = lastupdated;
    }

    public Date getLastupdatedDate() {
        return lastupdatedDate;
    }

    public void setLastupdatedDate(Date lastupdatedDate) {
        this.lastupdatedDate = lastupdatedDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Smsparam)) {
            return false;
        }
        Smsparam other = (Smsparam) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lk.dialog.cg.payment.Smsparam[ id=" + id + " ]";
    }

}

/*
 * Paymentparam.java
 * May 22, 2014  11:04:18 AM
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
 * <TO-DO> <code>Paymentparam</code>
 * @version $Id: Paymentparam.java,v 1.00.000
 */
@Entity
@Table(name = "paymentparam")
@NamedQueries({
    @NamedQuery(name = "Paymentparam.findAll", query = "SELECT p FROM Paymentparam p")})
public class Paymentparam implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "userid")
    private Integer userid;
    @Column(name = "paystatus")
    private String paystatus;    
    @Column(name = "maxtrn")
    private Integer maxtrn;
    @Column(name = "maxamt")
    private Double maxamt;
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

    public Paymentparam() {
    }

    public Paymentparam(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getPaystatus() {
        return paystatus;
    }

    public void setPaystatus(String paystatus) {
        this.paystatus = paystatus;
    }

    public String getMaxtrn() {
        return String.valueOf(maxtrn);
    }

    public void setMaxtrn(Integer maxtrn) {
        this.maxtrn = maxtrn;
    }

    public String getMaxamt() {
        return String.valueOf(maxamt);
    }

    public void setMaxamt(Double maxamt) {
        this.maxamt = maxamt;
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
        if (!(object instanceof Paymentparam)) {
            return false;
        }
        Paymentparam other = (Paymentparam) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.dialog.psi.api.entity.Paymentparam[ id=" + id + " ]";
    }

}

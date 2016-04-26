/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author 
 */
@Entity
@Table(name = "locationparam")
@NamedQueries({
    @NamedQuery(name = "Locationparam.findAll", query = "SELECT p FROM Locationparam p")})
public class Locationparam implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name="altitude")
    private String altitude;
    
    @Column(name="latitude")
    private String latitude;
    
    @Column(name="longitude")
    private String longitude;
    
    @Column(name="loc_ret_status")
    private String locationRetrieveStatus;
    
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
     * @return the altitude
     */
    public String getAltitude() {
        return altitude;
    }

    /**
     * @param altitude the altitude to set
     */
    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    /**
     * @return the latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the locationRetrieveStatus
     */
    public String getLocationRetrieveStatus() {
        return locationRetrieveStatus;
    }

    /**
     * @param locationRetrieveStatus the locationRetrieveStatus to set
     */
    public void setLocationRetrieveStatus(String locationRetrieveStatus) {
        this.locationRetrieveStatus = locationRetrieveStatus;
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

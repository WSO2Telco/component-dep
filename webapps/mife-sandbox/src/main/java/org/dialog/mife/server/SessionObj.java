/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dialog.mife.server;

import java.io.Serializable;

/**
 *
 * @author User
 */
public class SessionObj implements Serializable{
    
    private Double outstandingAmount = 1000.00;
    private boolean SMSSubscribed;

    /**
     * @return the outstandingAmount
     */
    public Double getOutstandingAmount() {
        return outstandingAmount;
    }

    /**
     * @param outstandingAmount the outstandingAmount to set
     */
    public void setOutstandingAmount(Double outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    /**
     * @return the SMSSubscribed
     */
    public boolean isSMSSubscribed() {
        return SMSSubscribed;
    }

    /**
     * @param SMSSubscribed the SMSSubscribed to set
     */
    public void setSMSSubscribed(boolean SMSSubscribed) {
        this.SMSSubscribed = SMSSubscribed;
    }
}

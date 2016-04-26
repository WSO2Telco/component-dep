/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mife.sandbox.model;

import java.util.Date;
import mife.sandbox.model.entities.LocationRequestLog;
import mife.sandbox.model.entities.Locationparam;
import mife.sandbox.model.entities.ManageNumber;
import mife.sandbox.model.entities.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Dialog
 */
public class LocationImpl {

    public User getUser(String username) {

        Session sess = HibernateUtil.getSession();
        User usr = null;
        try {
            usr = (User) sess.createQuery("from User where userName = ?").setString(0, username).uniqueResult();
            if (usr == null) {
                throw new Exception("User Not Found");
            }

        } catch (Exception e) {
            //LOG.error("[ApplicationManager], RemveInbound + " + "Error " + e.getMessage());
            e.printStackTrace();
        } finally {
            sess.close();
        }

        return usr;
    }

    public ManageNumber getWhitelisted(int userid, String num) {

        Session sess = HibernateUtil.getSession();
        ManageNumber whitelisted = null;
        try {
            whitelisted = (ManageNumber) sess.createQuery("from ManageNumber where user.id = ? and number = ?").setInteger(0, userid).setString(1, num).uniqueResult();

        } catch (Exception e) {
            System.out.println("getUserWhitelist: " + e);
        } finally {
            sess.close();
        }
        return whitelisted;
    }

    public boolean saveTransaction(String address, Double requestedAccuracy, String tranStatus, User user) {

        Session sess = null;
        Transaction tx = null;

        try {

            sess = HibernateUtil.getSession();
            tx = sess.beginTransaction();

            LocationRequestLog locationRequestLog = new LocationRequestLog();
            locationRequestLog.setAddress(address);
            locationRequestLog.setRequestedAccuracy(requestedAccuracy);
            locationRequestLog.setTransactionstatus(tranStatus);
            locationRequestLog.setDate(new Date());
            locationRequestLog.setUser(user);
            sess.save(locationRequestLog);

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            //LOG.error("[ApplicationManager], RemveInbound + " + "Error " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {

            sess.close();
        }
        
        return true;
    }
    
    public Locationparam queryLocationParam(Integer userId) {

        Session sess = null;
        Transaction tx = null;
        Locationparam locparam = null;

        try {

            sess = HibernateUtil.getSession();
            locparam = (Locationparam) sess.createQuery("from Locationparam where user = ?").setInteger(0, userId).uniqueResult();
        } catch (Exception e) {
            tx.rollback();
            //LOG.error("[ApplicationManager], RemveInbound + " + "Error " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            sess.close();
        }
        return locparam;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mife.sandbox.controller;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import mife.sandbox.model.HibernateUtil;
import mife.sandbox.model.entities.UssdSubscriptions;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author User
 */
public class UssdCtrlFunctions {
    
    
    public static List getUssdSubscriptions(int userId) {

        List ussdSubscriptions = null;
        Session s = HibernateUtil.getSession();
        try {
            
            String listSubString = "FROM UssdSubscriptions AS us WHERE us.user.id = :userId AND us.subStatus = 1";
            Query query = s.createQuery(listSubString);
            query.setInteger("userId", userId);
            ussdSubscriptions = query.list();
            
            
        } catch (Exception e) {
            System.out.println("getUssdSubscriptions: " + e);
        } finally {
            s.close();
        }

        return ussdSubscriptions;
    }
    
    public static UssdSubscriptions getUssdObjectBySubId(String subId){
        
        UssdSubscriptions sub = null;
        Session s = HibernateUtil.getSession();
        
        try {
            String queryString = "FROM UssdSubscriptions AS sub WHERE sub.subscriptionId = :subId";
            Query query = s.createQuery(queryString);
            query.setString("subId", subId);

            List resultedList = query.list();

            Iterator iter = resultedList.iterator();
            while (iter.hasNext()) {
                sub = (UssdSubscriptions) iter.next();
            }
        } catch (Exception e) {
            System.out.println("getActiveNumbers: " + e);
        } finally {
            s.close();
        }
        
        return sub;
    }
    
    public static boolean isSubscriptionExists(String subId){
        boolean isAvailable = false;
        Session s = HibernateUtil.getSession();
        
        try {
            String queryString = "FROM UssdSubscriptions AS sub WHERE sub.id = :subId";
            Query query = s.createQuery(queryString);
            query.setString("subId", subId);

            List resultedList = query.list();
            if(!resultedList.isEmpty()){
                isAvailable = true;
            }
        } catch (Exception e) {
            System.out.println("getActiveNumbers: " + e);
        } finally {
            s.close();
        }
        
        return isAvailable;
    }
    
    public static boolean isClientCorrelatorExist(int userId, String clientCorrelator) {
        //List paymentRequestList = null;
        //ChargeAmountRequest chargeAmountRequest = null;
        boolean isExist = false;
        Session sess = HibernateUtil.getSession();
        try {
            String getterString = "FROM UssdTransaction AS ut WHERE ut.clientCorrelator = :clientCorrelator AND ut.user.id = :userid";
            Query subQuery = sess.createQuery(getterString);
            subQuery.setString("clientCorrelator", clientCorrelator);
            subQuery.setInteger("userid", userId);

            if(subQuery.list().size() > 0){
                isExist = true;
            }
        } catch (Exception e) {
            System.out.println("Error in isClientCorrelatorExist : " + e);
        } finally {
            sess.close();
        }
        return isExist;
    }
}

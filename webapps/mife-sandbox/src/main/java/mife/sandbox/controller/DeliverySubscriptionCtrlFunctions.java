/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mife.sandbox.controller;

import java.util.Iterator;
import java.util.List;
import mife.sandbox.model.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author User
 */
public class DeliverySubscriptionCtrlFunctions {
    
    public static void getDeliverySubscriptionObj(){
        
    }
    public static void getSubscriptionStatus(){}
    
    public static void getDefaultSubsctiptionMsg(){}
    
    public static List getActiveSubsctiptions(int userid){
        List activeSubscriptions = null;
        Session s = HibernateUtil.getSession();
        try {
            String activeNumbersString = "FROM DeliverySubscription AS ds WHERE ds.subStatus = 1 AND ds.user.id = :userId";
            Query activeNumbersQuery = s.createQuery(activeNumbersString);
            activeNumbersQuery.setInteger("userId", userid);

            activeSubscriptions = activeNumbersQuery.list();
        } catch (Exception e) {
            System.out.println("getActiveSubsctiptions: " + e);
        } finally {
            s.close();
        }

        return activeSubscriptions;
    }
    
    public static boolean isSubscriptionExists(String subId){
        boolean isExists = false;
        Session s = HibernateUtil.getSession();
        
        try {
            String queryString = "FROM DeliverySubscription AS ds WHERE ds.subscriptionId = :subscriptionId";
            Query query = s.createQuery(queryString);
            query.setString("subscriptionId", subId);

            List resultedList = query.list();
            if(resultedList.size() > 0){
                isExists = true;
            }
        } catch (Exception e) {
            System.out.println("isSubscriptionExists: " + e);
        } finally {
            s.close();
        }
        return isExists;
    }
    
    public static boolean isSubscriptionExists(String filterCriteria, String notifyUrl, String callbackData, String clientCorrelator){
        boolean isExists = false;
        Session s = HibernateUtil.getSession();
        
        try {
            String queryString = "FROM DeliverySubscription AS ds WHERE ds.filterCriteria = :filter AND ds.notifyUrl = :notify AND ds.callbackData = :callback AND ds.clientCorrelator = :client";
            Query query = s.createQuery(queryString);
            query.setString("filter", filterCriteria);
            query.setString("notify", notifyUrl);
            query.setString("callback", callbackData);
            query.setString("client", clientCorrelator);

            List resultedList = query.list();
            if(resultedList.size() > 0){
                isExists = true;
            }
        } catch (Exception e) {
            System.out.println("isSubscriptionExists(2): " + e);
        } finally {
            s.close();
        }
        return isExists;
    }
}

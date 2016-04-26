/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mife.sandbox.model.functions;

import java.util.Date;
import mife.sandbox.controller.UssdCtrlFunctions;
import mife.sandbox.model.HibernateUtil;
import mife.sandbox.model.entities.UssdSubscriptions;
import mife.sandbox.model.entities.UssdTransaction;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author User
 */
public class UssdFunctions {

    private static final int ACTION_SUCCESS = 201;
    private static final int ACTION_FAILED = 500;

    public static int saveUssdTransaction(UssdTransaction ussd) {

        int actionState = ACTION_FAILED;
        int addedId = 0;
        Session s = HibernateUtil.getSession();
        Transaction tx;

        try {
            tx = s.beginTransaction();
            s.save(ussd);
            tx.commit();
            actionState = ACTION_SUCCESS;
        } catch (Exception e) {
            System.out.println("Exception in saveUssdTransaction: " + e);
            try {
                s.getTransaction().rollback();
                System.out.println("Transaction rollbacked!");
            } catch (RuntimeException ex) {
                System.out.println("Transaction rollback failed! " + ex);
            }
        }
        s.close();

        return actionState;
    }

    public static String saveUssdSubscription(UssdSubscriptions sub) {

        String addedSubscriptionId = "";
        Session s = HibernateUtil.getSession();
        Transaction tx;

        try {
            tx = s.beginTransaction();
            s.save(sub);
            //tx.commit();

            int addedId = sub.getId();
            addedSubscriptionId = "sub" + addedId;

            sub.setSubscriptionId(addedSubscriptionId);
            s.update(sub);
            tx.commit();

        } catch (Exception e) {
            System.out.println("Exception in saveUssdSubscription: " + e);
            try {
                s.getTransaction().rollback();
                System.out.println("Transaction rollbacked!");
            } catch (RuntimeException ex) {
                System.out.println("Transaction rollback failed! " + ex);
            }
        }
        s.close();

        return addedSubscriptionId;

    }

    public static boolean unsubscribeUssdSub(String subId) {
        
        boolean isOk = false;
        Session s = HibernateUtil.getSession();
        Transaction tx;
        
        
        UssdSubscriptions sub = UssdCtrlFunctions.getUssdObjectBySubId(subId);
        sub.setSubStatus(0);
        sub.setEffectedDate(new Date());

        try {
        
            tx = s.beginTransaction();
            s.update(sub);
            tx.commit();
            isOk = true;
            
        } catch (Exception e) {
            System.out.println("Exception in unsubscribeUssdSub: " + e);
            try {
                s.getTransaction().rollback();
                System.out.println("Transaction rollbacked!");
            } catch (RuntimeException ex) {
                System.out.println("Transaction rollback failed! " + ex);
            }
        }
        s.close();
        
        return isOk;
    }
}

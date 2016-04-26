/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mife.sandbox.model.functions;

import java.util.List;
import mife.sandbox.controller.UserControlFunctions;
import mife.sandbox.model.Getters;
import mife.sandbox.model.HibernateUtil;
import mife.sandbox.model.entities.DeliverySubscription;
import mife.sandbox.model.entities.Paymentparam;
import mife.sandbox.model.entities.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author User
 */
public class DeliverySubscriptionFunctions {
    
    private static final int ACTION_SUCCESS = 201;
    private static final int ACTION_FAILED = 500;
    
    public static final String MESSAGE_WAITING = "MessageWaiting";
    
    public static int saveDeliverySubscription(DeliverySubscription ds){
        
        int actionState = ACTION_FAILED;
        Session s = HibernateUtil.getSession();
        Transaction tx;

        try {
            tx = s.beginTransaction();
            s.save(ds);
            tx.commit();
            actionState = ACTION_SUCCESS;
        } catch (Exception e) {
            System.out.println("Exception in saveDeliverySubscription: " + e);
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
    
    public static void updateDeliverySubscription(DeliverySubscription ds){
        
    }
    
    public static boolean deleteDeliverySubscription(int id){
        boolean actionState = false;
        Session s = HibernateUtil.getSession();
        Transaction tx;
        
        try {
            DeliverySubscription sub = (DeliverySubscription) s.get(DeliverySubscription.class, id);
            
            tx = s.beginTransaction();
            s.delete(sub);
            tx.commit();
            actionState = true;
        } catch (Exception e) {
            System.out.println("Exception in deleteDeliverySubscription: " + e);
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
    
}

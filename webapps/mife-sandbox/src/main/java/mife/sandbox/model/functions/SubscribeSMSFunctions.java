/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mife.sandbox.model.functions;

import mife.sandbox.controller.SubscriberSMSCtrlFunctions;
import mife.sandbox.model.HibernateUtil;
import mife.sandbox.model.entities.SubscribeSMSRequest;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author User
 */
public class SubscribeSMSFunctions {
    public static boolean editSubscribeSMSRequest(int id, String callbackData, String criteria, String notifyUrl) {
        boolean actionState = false;
        Session s = HibernateUtil.getSession();
        Transaction tx;
        
        try {
            SubscribeSMSRequest req = SubscriberSMSCtrlFunctions.getSubscribeSMSObj(id);
            req.setCallbackData(callbackData);
            req.setCriteria(criteria);
            req.setNotifyURL(notifyUrl);
            
            tx = s.beginTransaction();
            s.update(req);
            tx.commit();
            actionState = true;
            
        } catch (Exception e) {
            System.out.println("Exception in editSubscribeSMSRequest: " + e);
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
    
    public static boolean deleteSubscribeSMSRequest(int id) {
        boolean actionState = false;
        Session s = HibernateUtil.getSession();
        Transaction tx;
        
        try {
            SubscribeSMSRequest req = SubscriberSMSCtrlFunctions.getSubscribeSMSObj(id);
            
            tx = s.beginTransaction();
            s.delete(req);
            tx.commit();
            actionState = true;
            
        } catch (Exception e) {
            System.out.println("Exception in deleteSubscribeSMSRequest: " + e);
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

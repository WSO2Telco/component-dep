/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mife.sandbox.model.functions;

import mife.sandbox.model.HibernateUtil;
import mife.sandbox.model.entities.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author User
 */
public class UserFunctions {
    public static boolean createUser(User u){
        boolean actionState = false;
        Session s = HibernateUtil.getSession();
        Transaction tx;
        
        try {
            tx = s.beginTransaction();
            s.save(u);
            tx.commit();
            actionState = true;
        } catch (Exception e) {
            System.out.println("Exception in createUser: " + e);
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

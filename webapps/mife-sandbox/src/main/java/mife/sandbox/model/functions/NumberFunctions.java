/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mife.sandbox.model.functions;

import mife.sandbox.model.HibernateUtil;
import mife.sandbox.model.entities.ManageNumber;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author User
 */
public class NumberFunctions {
    
    private static final int ACTION_SUCCESS = 201;
    private static final int ACTION_FAILED = 500;

    public static int[] saveNumber(ManageNumber number) {

        int actionState = ACTION_FAILED;
        int addedId = 0;
        Session s = HibernateUtil.getSession();
        Transaction tx;

        try {
            tx = s.beginTransaction();
            s.save(number);
            tx.commit();
            actionState = ACTION_SUCCESS;
            addedId = number.getId();
        } catch (Exception e) {
            System.out.println("Exception in saveNumber: " + e);
            try {
                s.getTransaction().rollback();
                System.out.println("Transaction rollbacked!");
            } catch (RuntimeException ex) {
                System.out.println("Transaction rollback failed! " + ex);
            }
        }
        s.close();
        
        int[] outputParams = {actionState, addedId};
        
        return outputParams;
    }

    public static boolean editNumber(int id, String num, String description, double balance) {
        boolean actionState = false;
        Session s = HibernateUtil.getSession();
        Transaction tx;
        
        try {
            ManageNumber number = (ManageNumber) s.get(ManageNumber.class, id);
            number.setNumber(num);
            number.setDescription(description);
            number.setBalance(balance);
            
            tx = s.beginTransaction();
            s.update(number);
            tx.commit();
            actionState = true;
            
        } catch (Exception e) {
            System.out.println("Exception in editNumber: " + e);
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
    
    public static boolean deleteNumber(int id) {
        boolean actionState = false;
        Session s = HibernateUtil.getSession();
        Transaction tx;
        
        try {
            ManageNumber number = (ManageNumber) s.get(ManageNumber.class, id);
            
            tx = s.beginTransaction();
            s.delete(number);
            tx.commit();
            actionState = true;
        } catch (Exception e) {
            System.out.println("Exception in deleteNumber: " + e);
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

    public static boolean updateBalance(int id, double bal) {
        boolean actionState = false;
        Session s = HibernateUtil.getSession();
        Transaction tx;

        try {
            ManageNumber number = (ManageNumber) s.get(ManageNumber.class, id);
            number.setBalance(bal);
            
            tx = s.beginTransaction();
            s.update(number);
            tx.commit();
            actionState = true;
            
        } catch (Exception e) {
            System.out.println("Exception in updateBalance: " + e);
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

    public static boolean editNumberStatus(int id, int status) {
        boolean actionState = false;
        Session s = HibernateUtil.getSession();
        Transaction tx;

        try {
            ManageNumber number = (ManageNumber) s.get(ManageNumber.class, id);
            number.setStatus(status);
            
            tx = s.beginTransaction();
            s.update(number);
            tx.commit();
            actionState = true;
            
        } catch (Exception e) {
            System.out.println("Exception in editNumberStatus: " + e);
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
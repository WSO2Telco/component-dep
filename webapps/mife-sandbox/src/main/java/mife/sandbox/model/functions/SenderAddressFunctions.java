/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mife.sandbox.model.functions;

import java.util.ArrayList;
import java.util.List;
import mife.sandbox.model.HibernateUtil;
import mife.sandbox.model.entities.SenderAddress;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author User
 */
public class SenderAddressFunctions {
    
    private static final int ACTION_SUCCESS = 201;
    private static final int ACTION_FAILED = 500;
    
    public static int[] saveSenderAddress(SenderAddress senderAddress) {

        int actionState = ACTION_FAILED;
        int addedId = 0;
        Session s = HibernateUtil.getSession();
        Transaction tx;

        try {
            tx = s.beginTransaction();
            s.save(senderAddress);
            tx.commit();
            actionState = ACTION_SUCCESS;
            addedId = senderAddress.getId();
        } catch (Exception e) {
            System.out.println("Exception in saveSenderAddress: " + e);
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
    
    public static boolean editSenderAddress(int id, String code, String description) {
        boolean actionState = false;
        Session s = HibernateUtil.getSession();
        Transaction tx;
        
        try {
            SenderAddress senderAddress = (SenderAddress) s.get(SenderAddress.class, id);
            senderAddress.setShortCode(code);
            senderAddress.setDescription(description);
            
            tx = s.beginTransaction();
            s.update(senderAddress);
            tx.commit();
            actionState = true;
            
        } catch (Exception e) {
            System.out.println("Exception in editSenderAddress: " + e);
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
    
    public static boolean deleteSenderAddress(int id) {
        boolean actionState = false;
        Session s = HibernateUtil.getSession();
        Transaction tx;
        
        try {
            SenderAddress senderAddress = (SenderAddress) s.get(SenderAddress.class, id);
            
            tx = s.beginTransaction();
            s.delete(senderAddress);
            tx.commit();
            actionState = true;
        } catch (Exception e) {
            System.out.println("Exception in deleteSenderAddress: " + e);
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
    
    public static List getFilterdAddressData(int userId, String code, String description){
        List filteredSA = new ArrayList();
        Session s = HibernateUtil.getSession();
        try {
            String filterQueryString = "FROM SenderAddress AS s WHERE s.user.id = :userId";
            if (code.length() > 0) {
                filterQueryString = filterQueryString + " AND (s.shortCode LIKE :shortcode)";
            }
            if (description.length() > 0) {
                filterQueryString = filterQueryString + " AND (s.description LIKE :description)";
            }

            Query query = s.createQuery(filterQueryString);
            query.setInteger("userId", userId);

            if (code.length() > 0) {
                query.setParameter("shortcode", "%" + code + "%");
            }
            if (description.length() > 0) {
                query.setParameter("description", "%" + description + "%");
            }

            filteredSA = query.list();
        } catch (Exception e) {
            System.out.println("getFilterdAddressData: " + e);
        }

        return filteredSA;
    }
    
}

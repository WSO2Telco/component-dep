/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mife.sandbox.model.functions;

import mife.sandbox.model.HibernateUtil;
import mife.sandbox.model.entities.Locationparam;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author User
 */
public class LocationFunctions {
    
    private static final int ACTION_SUCCESS = 201;
    private static final int ACTION_FAILED = 500;
    
    public static int saveLocation(Locationparam loc){
        
        int actionState = ACTION_FAILED;
        Session s = HibernateUtil.getSession();
        Transaction tx;

        try {
            tx = s.beginTransaction();
            s.save(loc);
            tx.commit();
            actionState = ACTION_SUCCESS;
        } catch (Exception e) {
            System.out.println("Exception in saveLocation: " + e);
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
    
    public static boolean editLocation(int userId, Locationparam locRec){
        boolean actionState = false;
        Session s = HibernateUtil.getSession();
        Transaction tx;
        
        try {
            
//            Locationparam loc = (Locationparam) s.get(Locationparam.class, id);
//            loc.setAltitude(locRec.getAltitude());
//            loc.setLongitude(locRec.getLongitude());
//            loc.setLatitude(locRec.getLatitude());
//            loc.setLocationRetrieveStatus(locRec.getLocationRetrieveStatus());
            
            tx = s.beginTransaction();
            s.update(locRec);
            tx.commit();
            actionState = true;
            
        } catch (Exception e) {
            System.out.println("Exception in editLocation: " + e);
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mife.sandbox.controller;

import java.util.Iterator;
import java.util.List;
import mife.sandbox.model.HibernateUtil;
import mife.sandbox.model.entities.SenderAddress;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author User
 */
public class SenderAddressCtrlFunctions {
    
    public static List getActiveSenderAddressList(int userId) {
        List activeSA = null;
        Session s = HibernateUtil.getSession();
        try {
            String getterString = "FROM SenderAddress AS s WHERE s.user.id = :userId";
            Query activeNumbersQuery = s.createQuery(getterString);
            activeNumbersQuery.setInteger("userId", userId);

            activeSA = activeNumbersQuery.list();
        } catch (Exception e) {
            System.out.println("getActiveSenderAddressList: " + e);
        } finally {
            s.close();
        }
        return activeSA;
    }
    
    public static SenderAddress getSenderAddressData(int id){
        SenderAddress sa = null;
        
        Session s = HibernateUtil.getSession();
        
        try {
            String queryString = "FROM SenderAddress AS s WHERE s.id = :id";
            Query query = s.createQuery(queryString);
            query.setInteger("id", id);

            List resultedList = query.list();

            Iterator iter = resultedList.iterator();
            while (iter.hasNext()) {
                sa = (SenderAddress) iter.next();
            }
        } catch (Exception e) {
            System.out.println("getActiveNumbers: " + e);
        } finally {
            s.close();
        }
        return sa;
    }
    
}

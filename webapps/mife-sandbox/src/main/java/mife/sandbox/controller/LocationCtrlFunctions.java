/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mife.sandbox.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import mife.sandbox.model.HibernateUtil;
import mife.sandbox.model.entities.Locationparam;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author User
 */
public class LocationCtrlFunctions {

    public static Locationparam getUserLocationSettings(int userid) {
        Locationparam l = null;
        //int user_id = ((User)LocationImpl.getUser(userid)).getId();//Integer.parseInt(userid);
        Session s = HibernateUtil.getSession();
        
        try {
            String queryString = "FROM Locationparam AS loc WHERE loc.user.id = :userId";
            Query query = s.createQuery(queryString);
            query.setInteger("userId", userid);

            List resultedList = query.list();

            Iterator iter = resultedList.iterator();
            while (iter.hasNext()) {
                l = (Locationparam) iter.next();
            }
        } catch (Exception e) {
            System.out.println("getUserLocationSettings: " + e);
        } finally {
            s.close();
        }
        
        return l;
    }
    
    public static boolean isLocationSettingsExists(int userid) {
        boolean isExist = false;
        List filteredNumbers = new ArrayList();
        Session s = HibernateUtil.getSession();
        try {
            String filterQueryString = "FROM Locationparam AS l WHERE l.user.id = :userId";
            
            Query query = s.createQuery(filterQueryString);
            query.setInteger("userId", userid);
            filteredNumbers = query.list();
            if (filteredNumbers.size() > 0) {
                isExist = true;
            }
        } catch (Exception e) {
            System.out.println("isLocationSettingsExists: " + e);
        } finally {
            s.close();
        }
        return isExist;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mife.sandbox.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import mife.sandbox.model.HibernateUtil;
import mife.sandbox.model.entities.ManageNumber;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author User
 */
public class ManageNumberFunctions {

    public static List getActiveNumbers(int userId) {

        List activeNumbers = null;
        Session s = HibernateUtil.getSession();
        try {
            String activeNumbersString = "FROM ManageNumber AS manageNumber WHERE manageNumber.status = 1 AND manageNumber.user.id = :userId";
            Query activeNumbersQuery = s.createQuery(activeNumbersString);
            activeNumbersQuery.setInteger("userId", userId);

            activeNumbers = activeNumbersQuery.list();
        } catch (Exception e) {
            System.out.println("getActiveNumbers: " + e);
        } finally {
            s.close();
        }

        return activeNumbers;
    }
    
    public static boolean isNumberExistforUser(int userId, String number){
        boolean isExist = false;
        Session s = HibernateUtil.getSession();
        try {
            String activeNumbersString = "FROM ManageNumber AS manageNumber WHERE manageNumber.Number = :num AND manageNumber.user.id = :userId";
            Query activeNumbersQuery = s.createQuery(activeNumbersString);
            activeNumbersQuery.setString("num", number);
            activeNumbersQuery.setInteger("userId", userId);
            
            if(activeNumbersQuery.list().size() > 0){
                isExist = true;
            }

        } catch (Exception e) {
            System.out.println("getActiveNumbers: " + e);
        } finally {
            s.close();
        }
        return isExist;
    }
    
    public static ManageNumber searchNumber(int id){
        ManageNumber num = null;
        
        Session s = HibernateUtil.getSession();
        
        try {
            String queryString = "FROM ManageNumber AS manageNumber WHERE manageNumber.status = 1 AND manageNumber.id = :id";
            Query query = s.createQuery(queryString);
            query.setInteger("id", id);

            List resultedList = query.list();

            Iterator iter = resultedList.iterator();
            while (iter.hasNext()) {
                num = (ManageNumber) iter.next();
            }
        } catch (Exception e) {
            System.out.println("getActiveNumbers: " + e);
        } finally {
            s.close();
        }
        return num;
    }

    public static List getFilteredNumbers(int userId, String number, String description) {
        List filteredNumbers = new ArrayList();
        Session s = HibernateUtil.getSession();
        try {
            String filterQueryString = "FROM ManageNumber AS a WHERE a.status = 1 AND a.user.id = :userId";
            if (number.length() > 0) {
                filterQueryString = filterQueryString + " AND (a.Number LIKE :number)";
            }
            if (description.length() > 0) {
                filterQueryString = filterQueryString + " AND (a.description LIKE :description)";
            }

            Query query = s.createQuery(filterQueryString);
            query.setInteger("userId", userId);

            if (number.length() > 0) {
                query.setParameter("number", "%" + number + "%");
            }
            if (description.length() > 0) {
                query.setParameter("description", "%" + description + "%");
            }

            filteredNumbers = query.list();
        } catch (Exception e) {
            System.out.println("getFilteredNumbers: " + e);
        } finally {
            s.close();
        }

        return filteredNumbers;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mife.sandbox.controller;

import mife.sandbox.model.HibernateUtil;
import mife.sandbox.model.entities.User;
import org.hibernate.Session;

/**
 *
 * @author User
 */
public class UserControlFunctions {
    public static User getUserObj(int userId){
        Session s = HibernateUtil.getSession();
        User u = null;
        try {
            u = (User) s.get(User.class, userId);
        } catch (Exception e) {
            System.out.println("getUserObj: " + e);
        } finally {
            s.close();
        }
        return u;
    }
}

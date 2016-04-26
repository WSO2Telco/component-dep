/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mife.sandbox.model;

import mife.sandbox.model.entities.ManageNumber;
import mife.sandbox.model.entities.User;
import mife.sandbox.model.entities.UssdTransaction;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author User
 */
public class UssdImpl {
    
    
    static Logger logger = Logger.getLogger(UssdImpl.class);
    
    public User getUser(String username) {
        
        Session sess = HibernateUtil.getSession();
        User usr = null;
        try {
            usr = (User) sess.createQuery("from User where userName = ?").setString(0, username).uniqueResult();
            if (usr == null) {
                throw new Exception("User Not Found");
            }

        } catch (Exception e) {
            //LOG.error("[ApplicationManager], RemveInbound + " + "Error " + e.getMessage());
            e.printStackTrace();
        } finally {
            sess.close();
        }

        return usr;
    }
    
    public static ManageNumber getWhitelisted(int userid, String num) {

        Session sess = null;
        ManageNumber whitelisted = null;
        try {

            sess = HibernateUtil.getSession();
            whitelisted = (ManageNumber) sess.createQuery("from ManageNumber where user.id = ? and number = ?").setInteger(0, userid).setString(1, num).uniqueResult();

        } catch (Exception e) {
            logger.error("Error in getWhitelisted : " + e);
        } finally {
            sess.close();
        }
        return whitelisted;
    }
    
    public String getLastMobileNumber(String str) {
        return str.substring(Math.max(0, str.length() - 11));
    }
    
}

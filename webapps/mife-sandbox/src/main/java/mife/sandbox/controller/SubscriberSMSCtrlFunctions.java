/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mife.sandbox.controller;

import mife.sandbox.model.HibernateUtil;
import mife.sandbox.model.entities.SubscribeSMSRequest;
import org.hibernate.Session;

/**
 *
 * @author User
 */
public class SubscriberSMSCtrlFunctions {
    public static SubscribeSMSRequest getSubscribeSMSObj(int id){
        Session s = HibernateUtil.getSession();
        SubscribeSMSRequest subscribeObj = null;
        try {
            subscribeObj = (SubscribeSMSRequest) s.get(SubscribeSMSRequest.class, id);
        } catch (Exception e) {
            System.out.println("getUserObj: " + e);
        } finally {
            s.close();
        }
        return subscribeObj;
    }
}

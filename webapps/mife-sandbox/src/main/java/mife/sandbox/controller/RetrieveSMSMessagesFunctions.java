/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mife.sandbox.controller;


import java.util.List;
import mife.sandbox.model.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author User
 */
public class RetrieveSMSMessagesFunctions {
    
    public static List getSendSMSToApplication(int userId, String destinationAddress, int batchSize) {
        
        List smsMessages = null;
        Session s = HibernateUtil.getSession();
        try {
            /*System.out.println("from date in data access layer: " + fromDate);
            System.out.println("to date in data access layer: " + toDate);*/
            String smsMessagesString = "FROM SendSMSToApplication AS retMessage WHERE retMessage.user.id = :userId AND retMessage.destinationAddress = :destinationAddress";
            Query smsMessagesQuery = s.createQuery(smsMessagesString).setMaxResults(batchSize);
            smsMessagesQuery.setInteger("userId", userId);
            smsMessagesQuery.setString("destinationAddress", destinationAddress);
            //smsMessagesQuery.setInteger("toDate", batchSize);
            smsMessages = smsMessagesQuery.list();
        } catch (Exception e) {
            System.out.println("getSendSMSToApplication: " + e);
        } finally {
            s.close();
        }

        return smsMessages;
    }
}

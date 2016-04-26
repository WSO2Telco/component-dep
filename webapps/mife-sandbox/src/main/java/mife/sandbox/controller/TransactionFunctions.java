/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mife.sandbox.controller;

import java.util.Date;
import java.util.List;
import mife.sandbox.model.HibernateUtil;
import mife.sandbox.model.entities.ChargeAmountRequest;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Dialog
 */
public class TransactionFunctions {

    public static List getSMSLogTransactions(int userId, Date fromDate, Date toDate, int txType) {

        List smsTransactions = null;
        Session s = HibernateUtil.getSession();
        try {
            /*System.out.println("from date in data access layer: " + fromDate);
            System.out.println("to date in data access layer: " + toDate);*/
            String sendSMSRequestString = "FROM SMSRequestLog AS smsLog WHERE smsLog.user.id = :userId AND smsLog.date BETWEEN :fromDate AND :toDate AND smsLog.txntype = :txType";
            Query sendSMSRequestQuery = s.createQuery(sendSMSRequestString);
            sendSMSRequestQuery.setInteger("userId", userId);
            sendSMSRequestQuery.setDate("fromDate", fromDate);
            sendSMSRequestQuery.setDate("toDate", toDate);
            sendSMSRequestQuery.setInteger("txType", txType);
            smsTransactions = sendSMSRequestQuery.list();
        } catch (Exception e) {
            System.out.println("getSMSLogTransactions: " + e);
        } finally {
            s.close();
        }

        return smsTransactions;
    }

    public static List getPaymentTransactions(int userId, int txType, Date fromDate, Date toDate) {

        List paymentTransactions = null;
        Session s = HibernateUtil.getSession();
        try {
            String chargeAmountRequestString = "FROM ChargeAmountRequest AS charheRequest WHERE charheRequest.user.id = :userId AND charheRequest.paymentTranscationType = :txType AND charheRequest.date BETWEEN :fromDate AND :toDate";
            Query chargeAmountRequestQuery = s.createQuery(chargeAmountRequestString);
            chargeAmountRequestQuery.setInteger("userId", userId);
            chargeAmountRequestQuery.setInteger("txType", txType);
            chargeAmountRequestQuery.setDate("fromDate", fromDate);
            chargeAmountRequestQuery.setDate("toDate", toDate);
            
            paymentTransactions = chargeAmountRequestQuery.list();
        } catch (Exception e) {
            System.out.println("getPaymentTransactions: " + e);
        } finally {
            s.close();
        }

        return paymentTransactions;
    }
    
    public static List getUssdTransactions(int userId, int txType, Date fromDate, Date toDate) {
        List ussdTx = null;
        Session s = HibernateUtil.getSession();
        try {
            String UssdTxRequestString = "FROM UssdTransaction WHERE user.id = :userId AND actionStatus = :txType AND date BETWEEN :fromDate AND :toDate";
            Query chargeAmountRequestQuery = s.createQuery(UssdTxRequestString);
            chargeAmountRequestQuery.setInteger("userId", userId);
            chargeAmountRequestQuery.setInteger("txType", txType);
            chargeAmountRequestQuery.setDate("fromDate", fromDate);
            chargeAmountRequestQuery.setDate("toDate", toDate);
            
            ussdTx = chargeAmountRequestQuery.list();
        } catch (Exception e) {
            System.out.println("getUssdTransactions: " + e);
        } finally {
            s.close();
        }

        return ussdTx;
    }
    
    public static List getUssdSubscriptions(int userId, Date fromDate, Date toDate) {
        List ussdSub = null;
        Session s = HibernateUtil.getSession();
        try {
            String UssdTxRequestString = "FROM UssdSubscriptions WHERE user.id = :userId AND createdDate BETWEEN :fromDate AND :toDate";
            Query chargeAmountRequestQuery = s.createQuery(UssdTxRequestString);
            chargeAmountRequestQuery.setInteger("userId", userId);
            chargeAmountRequestQuery.setDate("fromDate", fromDate);
            chargeAmountRequestQuery.setDate("toDate", toDate);
            
            ussdSub = chargeAmountRequestQuery.list();
        } catch (Exception e) {
            System.out.println("getUssdSubscriptions: " + e);
        } finally {
            s.close();
        }

        return ussdSub;
    }
    
    public static List getLocationTransactions(int userId, Date fromDate, Date toDate){
        
        List locationTransactions = null;
        Session s = HibernateUtil.getSession();
        try {
            String locationRequestString = "FROM LocationRequestLog AS locationRequestLog WHERE locationRequestLog.user.id = :userId AND locationRequestLog.date BETWEEN :fromDate AND :toDate";
            Query locationRequestQuery = s.createQuery(locationRequestString);
            locationRequestQuery.setInteger("userId", userId);
            locationRequestQuery.setDate("fromDate", fromDate);
            locationRequestQuery.setDate("toDate", toDate);
            
            locationTransactions = locationRequestQuery.list();
        } catch (Exception e) {
             System.out.println("getLocationTransactions: " + e);
        } finally {
            s.close();
        }
        
        return locationTransactions;
    }
    
    public static List<ChargeAmountRequest> getListTransactionsData(int userId, String endUserId) {

        List<ChargeAmountRequest> paymentTransactions = null;
        Session s = HibernateUtil.getSession();
        try {
            String listTxQueryString = "FROM ChargeAmountRequest AS cr WHERE cr.user.id = :userId AND cr.endUserId LIKE :enduserId";
            Query listTxQuery = s.createQuery(listTxQueryString);
            listTxQuery.setInteger("userId", userId);
            listTxQuery.setString("enduserId", "%"+endUserId);
            
            paymentTransactions = listTxQuery.list();
//            paymentTransactions = s.createQuery("FROM ChargeAmountRequest AS cr WHERE cr.user.id = ? AND cr.endUserId LIKE ?").setInteger(0, userId).setString(1, "%"+endUserId).list();
        } catch (Exception e) {
            System.out.println("getListTransactionsData: " + e);
        } finally {
            s.close();
        }

        return paymentTransactions;
    }
}

/*
 * SmsImpl.java
 * May 27, 2014  2:25:40 PM
 * Roshan.Saputhanthri
 *
 * Copyright (C) Dialog Axiata PLC. All Rights Reserved.
 */
package mife.sandbox.model;

import java.util.Date;
import java.util.List;
import mife.sandbox.model.entities.DeliverySubscription;
import mife.sandbox.model.entities.ManageNumber;
import mife.sandbox.model.entities.Paymentparam;
import mife.sandbox.model.entities.SMSDeliveryStatus;
import mife.sandbox.model.entities.SMSRequestLog;
import mife.sandbox.model.entities.SendSMSToApplication;
import mife.sandbox.model.entities.SenderAddress;
import mife.sandbox.model.entities.Smsparam;
import mife.sandbox.model.entities.SubscribeSMSRequest;
import mife.sandbox.model.entities.User;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * <TO-DO>
 * <code>SmsImpl</code>
 *
 * @version $Id: SmsImpl.java,v 1.00.000
 */
public class SmsImpl {
    
    static Logger logger = Logger.getLogger(SmsImpl.class);
    
    public boolean saveParam(String user, String delstatus, String notifydelay, String maxallowed) {
        
        Session sess = null;
        Transaction tx = null;
        Smsparam sms = null;
        try {
            
            sess = HibernateUtil.getSession();
            tx = sess.beginTransaction();
            
            User usr = getUser(user);
            if (usr == null) {
                throw new Exception("User Not Found");
            }
            
            sms = querySmsParam(user);
            if (sms != null) {
                sms.setDeliveryStatus(delstatus);
                sms.setNotificationDelay(notifydelay);
                sms.setMaxNotifications(maxallowed);
                sess.update(sms);
            } else {
                sms = new Smsparam();
                sms.setUserid(usr.getId());
                sms.setDeliveryStatus(delstatus);
                sms.setMaxNotifications(maxallowed);
                sms.setNotificationDelay(notifydelay);
                sms.setCreatedDate(new Date());
                sess.save(sms);
            }
            
            tx.commit();
            
        } catch (Exception e) {
            tx.rollback();
            //LOG.error("[ApplicationManager], RemveInbound + " + "Error " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            
            sess.close();
        }
        
        return true;
        
    }
    
    public Smsparam querySmsParam(String userId) {
        
        Session sess = null;
        Transaction tx = null;
        Smsparam sms = null;
        
        try {
            User user = getUser(userId);
            if (user == null) {
                throw new Exception("User Not Found");
            }
            sess = HibernateUtil.getSession();
            
            String hql = "from Smsparam where userid = ?";
            Query query = sess.createQuery(hql);
            query.setInteger(0, user.getId());
            sms = (Smsparam) query.uniqueResult();
            
        } catch (Exception e) {
            //tx.rollback();
            //LOG.error("[ApplicationManager], RemveInbound + " + "Error " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            sess.close();
        }
        return sms;
    }
    
    public User getUser(String username) {
        
        Session sess = HibernateUtil.getSession();
        User usr = null;
        try {
            usr = (User) sess.createQuery("from User where userName = :userName").setString("userName", username).uniqueResult();
            
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
    
    public List getSubscriberSMSTableData(String username) {
        List sub = null;
        Session s = HibernateUtil.getSession();
        User u = getUser(username);
        try {
            String getterString = "FROM SubscribeSMSRequest AS s WHERE s.user.id = :userid";
            Query subQuery = s.createQuery(getterString);
            subQuery.setInteger("userid", u.getId());
            
            sub = subQuery.list();
        } catch (Exception e) {
            System.out.println("getSubTableData: " + e);
        } finally {
            s.close();
        }
        return sub;
    }
    
    public Integer saveSubsNotification(String user, String callback_data, String client_correlator,
            String criteria, String destination_address, String notify_url) {
        
        Session sess = null;
        Transaction tx = null;
        SubscribeSMSRequest smsnotify = null;
        Integer subsid = null;
        
        try {
            
            sess = HibernateUtil.getSession();
            tx = sess.beginTransaction();
            
            User usr = getUser(user);
            if (usr == null) {
                throw new Exception("User Not Found");
            }
            smsnotify = new SubscribeSMSRequest();
            smsnotify.setCallbackData(callback_data);
            smsnotify.setClientCorrelator(client_correlator);
            smsnotify.setCriteria(criteria);
            smsnotify.setDestinationAddress(destination_address);
            smsnotify.setNotifyURL(notify_url);
            smsnotify.setUser(usr);
            smsnotify.setDate(new Date());
            smsnotify.setNotificationFormat("JSON");
            sess.save(smsnotify);
            subsid = smsnotify.getSubscribeId();
            
            tx.commit();
            
        } catch (Exception e) {
            tx.rollback();
            //LOG.error("[ApplicationManager], RemveInbound + " + "Error " + e.getMessage());
            e.printStackTrace();
        } finally {
            sess.close();
        }
        
        return subsid;
        
    }
    
    public boolean saveDeliverySub(String user, String body, String notify, String clientCr, String callback, String filter) {
        boolean isOk = false;
        Session sess = null;
        Transaction tx = null;
        DeliverySubscription ds = null;
        
        try {
            
            sess = HibernateUtil.getSession();
            tx = sess.beginTransaction();
            
            User usr = getUser(user);
            if (usr == null) {
                throw new Exception("User Not Found");
            }
            
            ds = new DeliverySubscription();
            ds.setCallbackData(callback);
            ds.setClientCorrelator(clientCr);
            ds.setNotifyUrl(notify);
            ds.setFilterCriteria(filter);
            ds.setSubStatus(1);
            ds.setRequestData(body);
            ds.setUser(usr);
            
            sess.save(ds);
            tx.commit();
            
        } catch (Exception e) {
            tx.rollback();
            //LOG.error("[ApplicationManager], RemveInbound + " + "Error " + e.getMessage());
            e.printStackTrace();
        } finally {
            sess.close();
        }
        
        return isOk;
    }
    
    public boolean getSubsNotification(String destAddr, String keyword) {
        Session sess = null;
        boolean isfound = false;
        try {
            sess = HibernateUtil.getSession();
            SubscribeSMSRequest receiptsub = (SubscribeSMSRequest) sess.createQuery("from SubscribeSMSRequest where destinationAddress = ? and criteria = ?").setString(0, destAddr).setString(1, keyword).uniqueResult();
            if (receiptsub != null) {
                isfound = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            
        } finally {
            sess.close();
        }
        return isfound;
    }
    
    public String checkWhitelisted(int userid, String[] nums) {
        
        Session sess = null;
        ManageNumber whitelisted = null;
        try {
            
            for (String num : nums) {
                sess = HibernateUtil.getSession();
                whitelisted = (ManageNumber) sess.createQuery("from ManageNumber where user.id = ? and number = ?").setInteger(0, userid).setString(1, getLastMobileNumber(num)).uniqueResult();
                if (whitelisted == null) {
                    return num;
                }
            }
            
        } catch (Exception e) {
            System.out.println("getUserWhitelist: " + e);
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }
    
    public boolean isSenderAddress(int userid, String num) {
        
        Session sess = null;
        
        try {
            sess = HibernateUtil.getSession();
            SenderAddress whitelisted = (SenderAddress) sess.createQuery("from SenderAddress where user.id = ? and shortCode = ?").setInteger(0, userid).setString(1, num).uniqueResult();
            if (whitelisted == null) {
                return false;
            }
        } catch (Exception e) {
            System.out.println("getUserWhitelist: " + e);
            throw new RuntimeException(e.getMessage());
        } finally {
            sess.close();
        }
        return true;
    }
    
    public boolean deleteSubscription(int userid, String subStr) {
        
        Session sess = null;
        Transaction tx = null;
        SubscribeSMSRequest app = null;
        
        try {
            
            if ((subStr == null) || (subStr.isEmpty())) {
                return false;
            }
            
            Integer subid = Integer.parseInt(subStr.replaceFirst("sub", ""));
            
            sess = HibernateUtil.getSession();
            tx = sess.beginTransaction();
            // Query q = (SmppReceiptSubs) sess.createQuery("delete from SmppReceiptSubs where id = ?").setString(0, appID).uniqueResult();
            Query q = sess.createQuery("delete from SubscribeSMSRequest where subscribeId = :subId");
            q.setInteger("subId", subid);
            q.executeUpdate();
            tx.commit();
            
        } catch (Exception e) {
            tx.rollback();
            //e.printStackTrace();
            //LOG.error("[ApplicationManager], unsubsNotify + " + "Error " + e.getMessage());
            return false;
        } finally {
            sess.close();
        }
        
        return true;
    }
    
    public String getLastMobileNumber(String str) {
        return str.substring(Math.max(0, str.length() - 11));
    }
    
    public List<SendSMSToApplication> getMessageInbound(String regid, Integer userid) {
        Session sess = null;
        SendSMSToApplication smsinbound = null;
        List<SendSMSToApplication> listmsg = null;
        
        try {
            sess = HibernateUtil.getSession();
            listmsg = sess.createQuery("from SendSMSToApplication where destinationAddress = ? and user.id = ?").setString(0, regid).setInteger(1, userid).list();
        } catch (Exception e) {
            //LOG.error("[ApplicationManager], getMessageInbound + " + "Error " + e.getMessage());
            e.printStackTrace();
        } finally {
            sess.close();
        }
        
        return listmsg;
    }
    
    public boolean saveTransaction(String senderAddress, String addresses, String message, String clientCorrelator, String senderName,
            String notifyURL, String callbackData, Integer batchsize, String status, Integer txntype, String criteria, String notificationFormat, User user, String requestId) {
        
        Session sess = null;
        Transaction tx = null;
        
        try {
            sess = HibernateUtil.getSession();
            tx = sess.beginTransaction();
            
            SMSRequestLog smsRequest = new SMSRequestLog();
            smsRequest.setSenderAddress(senderAddress);
            smsRequest.setAddresses((String) addresses);
            smsRequest.setMessage(message);
            smsRequest.setClientCorrelator(clientCorrelator);
            smsRequest.setSenderName(senderName);
            smsRequest.setNotifyURL(notifyURL);
            smsRequest.setCallbackData(callbackData);
            smsRequest.setUser(user);
            smsRequest.setDate(new Date());
            smsRequest.setBatchsize(batchsize);
            smsRequest.setTransactionstatus(status);
            smsRequest.setTxntype(txntype);
            smsRequest.setCriteria(criteria);
            smsRequest.setNotificationFormat(notificationFormat);
            smsRequest.setRequestId(requestId);
            sess.save(smsRequest);
            
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            logger.error("Error in saveTransaction : " + e);
            return false;
        } finally {
            sess.close();
        }
        
        return true;
    }
    
    public String saveSendSMSTransaction(String senderAddress, String addresses, String message, String clientCorrelator, String senderName,
            String notifyURL, String callbackData, Integer batchsize, String status, Integer txntype, String criteria, String notificationFormat, User user, String deliveryStatus) {
        
        Session sess = null;
        Transaction tx = null;
        String transactionId = null;
        
        try {
            sess = HibernateUtil.getSession();
            tx = sess.beginTransaction();
            
            SMSRequestLog smsRequest = new SMSRequestLog();
            smsRequest.setSenderAddress(senderAddress);
            smsRequest.setAddresses((String) addresses);
            smsRequest.setMessage(message);
            smsRequest.setClientCorrelator(clientCorrelator);
            smsRequest.setSenderName(senderName);
            smsRequest.setNotifyURL(notifyURL);
            smsRequest.setCallbackData(callbackData);
            smsRequest.setUser(user);
            smsRequest.setDate(new Date());
            smsRequest.setBatchsize(batchsize);
            smsRequest.setTransactionstatus(status);
            smsRequest.setTxntype(txntype);
            smsRequest.setCriteria(criteria);
            smsRequest.setNotificationFormat(notificationFormat);
            sess.save(smsRequest);
            
            int smsId = smsRequest.getSmsId();
            SMSRequestLog lastSendSMSRequest = (SMSRequestLog) sess.get(SMSRequestLog.class, smsId);
            if (lastSendSMSRequest == null) {
                logger.debug("Last transaction not found");
                throw new Exception("Last transaction not found");
            }
            
            transactionId = "smstran-" + smsId;
            lastSendSMSRequest.setTransactionId(transactionId);
            sess.update(lastSendSMSRequest);
            
            SMSDeliveryStatus smsDeliveryStatus = new SMSDeliveryStatus();
            smsDeliveryStatus.setTransactionId(transactionId);
            smsDeliveryStatus.setSenderAddress(senderAddress);
            smsDeliveryStatus.setDeliveryStatus(deliveryStatus);
            smsDeliveryStatus.setDate(new Date());
            smsDeliveryStatus.setUser(user);
            sess.save(smsDeliveryStatus);
            
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            logger.error("Error in saveSendSMSTransaction : " + e);
            return null;
        } finally {
            sess.close();
        }
        logger.debug("SMS transaction id : " + transactionId);
        return transactionId;
    }
    
    public static void saveSendSMSToApplication(SendSMSToApplication smsmsg) {
        
        Session s = null;
        Transaction tx;
        try {
            s = HibernateUtil.getSession();
            tx = s.beginTransaction();
            s.save(smsmsg);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Exception in SendSMSToApplicationFunctions: " + e);
            try {
                s.getTransaction().rollback();
                System.out.println("Transaction rollbacked!");
            } catch (RuntimeException ex) {
                System.out.println("Transaction rollback failed! " + ex);
            }
        } finally {
            s.close();
        }
    }
    
    public SubscribeSMSRequest getAppByKeywordAndapp(String appid, String criteria) {
        Session sess = null;
        
        SubscribeSMSRequest app = null;
        try {
            sess = HibernateUtil.getSession();
            
            app = (SubscribeSMSRequest) sess.createQuery("from SubscribeSMSRequest where destinationAddress = ? and criteria = ?").setString(0, appid).setString(1, criteria).uniqueResult();
            if (app == null) {
                app = (SubscribeSMSRequest) sess.createQuery("from SubscribeSMSRequest where destinationAddress = ? and criteria = ?").setString(0, appid).setString(1, "").uniqueResult();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            //LOG.error("[ApplicationManager], Error + " + "getApplicationByKeywordAndSender " + e.getMessage());
        } finally {
            sess.close();
        }
        
        return app;
    }
    
    public boolean saveUser(String user) {
        
        Session sess = null;
        Transaction tx = null;
        try {
            
            sess = HibernateUtil.getSession();
            tx = sess.beginTransaction();
            
            User usr = getUser(user);
            if (usr == null) {
                usr = new User();
                usr.setUserName(user);
                usr.setUserStatus(1);
                sess.save(usr);
                //same default parameters
                Smsparam sms = new Smsparam();
                sms.setUserid(usr.getId());
                sms.setDeliveryStatus("DeliveredToTerminal");
                sms.setMaxNotifications("1000");
                sms.setNotificationDelay("10");
                //sms.setCreatedDate(new Date());
                sess.save(sms);
                
                Paymentparam paymentparam = new Paymentparam();
                paymentparam.setUserid(usr.getId());
                paymentparam.setPaystatus("Processing");
                paymentparam.setMaxtrn(Integer.valueOf("1000"));
                paymentparam.setMaxamt(Double.valueOf("15000"));
                sess.save(paymentparam);
            }
            
            tx.commit();
            
        } catch (Exception e) {
            tx.rollback();
            //LOG.error("[ApplicationManager], RemveInbound + " + "Error " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            
            sess.close();
        }
        
        return true;
        
    }
    
    public SMSDeliveryStatus getPreviousSMSDeliveryDetailsByTransactionId(String transactionId) {
        SMSDeliveryStatus smsDeliveryDetails = null;
        Session sess = null;
        
        try {
            logger.debug("Transaction id : " + transactionId);
            sess = HibernateUtil.getSession();
            
            smsDeliveryDetails = (SMSDeliveryStatus) sess.get(SMSDeliveryStatus.class, transactionId);
            
        } catch (Exception e) {
            logger.error("Error in getPreviousSMSDeliveryDetailsByTransactionId : " + e);
            return null;
        } finally {
            sess.close();
        }
        return smsDeliveryDetails;
    }
    
    public SMSRequestLog getPreviousChargeAmountRequestByTransactionId(int transactionId) {
        SMSRequestLog smsRequestLog = null;
        Session sess = null;
        
        try {
            logger.debug("SMS request log id : " + transactionId);
            sess = HibernateUtil.getSession();
            
            smsRequestLog = (SMSRequestLog) sess.get(SMSRequestLog.class, transactionId);
            
        } catch (Exception e) {
            logger.error("Error in getPreviousChargeAmountRequestByTransactionId : " + e);
            return null;
        } finally {
            sess.close();
        }
        return smsRequestLog;
    }
}

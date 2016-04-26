/*
 * PaymentImpl.java
 * May 22, 2014  10:43:35 AM
 * Roshan.Saputhanthri
 *
 * Copyright (C) Dialog Axiata PLC. All Rights Reserved.
 */
package mife.sandbox.model;

import java.sql.ResultSet;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import mife.sandbox.model.entities.*;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * <TO-DO>
 * <code>PaymentImpl</code>
 *
 * @version $Id: PaymentImpl.java,v 1.00.000
 */
public class PaymentImpl {

    static Logger logger = Logger.getLogger(PaymentImpl.class);

    public boolean saveParam(String user, String paystatus, String maxtrn, String maxamt) {

        Session sess = null;
        Transaction tx = null;
        Paymentparam paymentparam = null;
        try {

            sess = HibernateUtil.getSession();
            tx = sess.beginTransaction();

            User usr = getUser(user);
            if (usr == null) {
                throw new Exception("User Not Found");
            }

            paymentparam = queryPaymentParam(usr.getId());
            if (paymentparam != null) {
                paymentparam.setPaystatus(paystatus);
                paymentparam.setMaxtrn(Integer.valueOf(maxtrn));
                paymentparam.setMaxamt(Double.parseDouble(maxamt));
                sess.update(paymentparam);
            } else {
                paymentparam = new Paymentparam();
                paymentparam.setUserid(usr.getId());
                paymentparam.setPaystatus(paystatus);
                paymentparam.setMaxtrn(Integer.valueOf(maxtrn));
                paymentparam.setMaxamt(Double.valueOf(maxamt));
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

    public Paymentparam queryPaymentParam(Integer userId) {

        Session sess = null;
        Transaction tx = null;
        Paymentparam payparam = null;

        try {

            sess = HibernateUtil.getSession();
            payparam = (Paymentparam) sess.createQuery("from Paymentparam where userid = ?").setInteger(0, userId).uniqueResult();

        } catch (Exception e) {
            tx.rollback();
            //LOG.error("[ApplicationManager], RemveInbound + " + "Error " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            sess.close();
        }
        return payparam;
    }

    public Paymentparam queryPaymentParam(String userId) {

        Session sess = null;
        Transaction tx = null;
        Paymentparam payparam = null;

        try {
            User user = getUser(userId);
            if (user == null) {
                throw new Exception("User Not Found");
            }
            sess = HibernateUtil.getSession();
            payparam = (Paymentparam) sess.createQuery("from Paymentparam where userid = ?").setInteger(0, user.getId()).uniqueResult();

        } catch (Exception e) {
            tx.rollback();
            //LOG.error("[ApplicationManager], RemveInbound + " + "Error " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            sess.close();
        }
        return payparam;
    }

    public User getUser(String username) {

        Session sess = null;
        User usr = null;
        try {

            sess = HibernateUtil.getSession();

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

    public ManageNumber getWhitelisted(int userid, String num) {

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

    public PaymentTransaction getPreviousTransactionDetailsByTransactionId(String transactionId) {
        PaymentTransaction transactionDetails = null;
        Session sess = null;

        try {
            logger.debug("Transaction id : " + transactionId);
            sess = HibernateUtil.getSession();

            transactionDetails = (PaymentTransaction) sess.get(PaymentTransaction.class, transactionId);

        } catch (Exception e) {
            logger.error("Error in getPreviousTransactionDetailsByTransactionId : " + e);
            return null;
        } finally {
            sess.close();
        }
        return transactionDetails;
    }

    public ChargeAmountRequest getPreviousChargeAmountRequestByTransactionId(int transactionId) {
        ChargeAmountRequest chargeAmountRequest = null;
        Session sess = null;

        try {
            logger.debug("Charge amount request id : " + transactionId);
            sess = HibernateUtil.getSession();

            chargeAmountRequest = (ChargeAmountRequest) sess.get(ChargeAmountRequest.class, transactionId);

        } catch (Exception e) {
            logger.error("Error in getPreviousChargeAmountRequestByTransactionId : " + e);
            return null;
        } finally {
            sess.close();
        }
        return chargeAmountRequest;
    }

    public boolean updateWhitelisted(Integer numberid, double balance) {

        Session sess = null;
        Transaction tx = null;

        try {

            sess = HibernateUtil.getSession();
            tx = sess.beginTransaction();

            ManageNumber whitelisted = (ManageNumber) sess.get(ManageNumber.class, numberid);
            if (whitelisted == null) {
                logger.debug("Number not found");
                throw new Exception("Number not found");
            }
            whitelisted.setBalance(balance);
            sess.update(whitelisted);

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            logger.error("Error in updateWhitelisted : " + e);
            return false;
        } finally {
            sess.close();
        }
        return true;
    }

    public boolean updateWhitelisted(Integer numberid, double balance, double reservedAmount) {

        Session sess = null;
        Transaction tx = null;

        try {
            logger.debug("Id : " + numberid);
            logger.debug("Balance : " + balance);
            logger.debug("Reserved amount : " + reservedAmount);

            sess = HibernateUtil.getSession();
            tx = sess.beginTransaction();

            ManageNumber whitelisted = (ManageNumber) sess.get(ManageNumber.class, numberid);
            if (whitelisted == null) {
                logger.debug("Number not found");
                throw new Exception("Number not found");
            }

            whitelisted.setBalance(balance);
            whitelisted.setReserved_amount(reservedAmount);
            sess.update(whitelisted);

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            logger.error("Error in updateWhitelisted : " + e);
            return false;
        } finally {
            sess.close();
        }
        return true;
    }

    public String saveChargeTransaction(User user, String clientCoorelator, String endUserId, double amount, String currency, String description,
            String onBehalfOf, String purchastCatCode, String channel, double taxAmount, String refCode, String transOpStatus, String callbackData, String notifyURL, String mandateId, String notificationFormat, String productId, int referenceSequence, String serviceId, String code, Double totalAmountCharged, Double amountReserved, int paymentTranscationType) {

        Session sess = null;
        Transaction tx = null;
        String serverReferenceCode = null;
        String transactionId = null;

        try {

            sess = HibernateUtil.getSession();
            tx = sess.beginTransaction();
            ChargeAmountRequest chargerequest = new ChargeAmountRequest();
            chargerequest.setAmount(amount);
            chargerequest.setChannel(channel);
            chargerequest.setClientCorrelator(clientCoorelator);
            chargerequest.setCurrency(currency);
            chargerequest.setDate(new Date());
            chargerequest.setDescription(description);
            chargerequest.setEndUserId(endUserId);
            chargerequest.setOnBehalfOf(onBehalfOf);
            chargerequest.setPurchaseCategoryCode(purchastCatCode);
            chargerequest.setReferenceCode(refCode);
            chargerequest.setTaxAmount(taxAmount);
            chargerequest.setTransactionOperationStatus(transOpStatus);
            chargerequest.setUser(user);
            chargerequest.setCallbackData(callbackData);
            chargerequest.setNotifyURL(notifyURL);
            chargerequest.setMandateId(mandateId);
            chargerequest.setNotificationFormat(notificationFormat);
            chargerequest.setProductId(productId);
            chargerequest.setReferenceSequence(referenceSequence);
            chargerequest.setServiceId(serviceId);
            chargerequest.setCode(code);
            chargerequest.setTotalAmountCharged(totalAmountCharged);
            chargerequest.setAmountReserved(amountReserved);
            chargerequest.setPaymentTranscationType(paymentTranscationType);

            sess.save(chargerequest);

            int chargeId = chargerequest.getChargeId();
            ChargeAmountRequest lastChargeAmountRequest = (ChargeAmountRequest) sess.get(ChargeAmountRequest.class, chargeId);
            if (lastChargeAmountRequest == null) {
                logger.debug("Last transaction not found");
                throw new Exception("Last transaction not found");
            }

            transactionId = "paytran-" + chargeId;
            lastChargeAmountRequest.setTransactionId(transactionId);
            sess.update(lastChargeAmountRequest);

            serverReferenceCode = "src-" + chargeId;

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            logger.error("Error in saveTransaction : " + e);
            return null;
        } finally {
            sess.close();
        }
        logger.debug("Server reference code : " + serverReferenceCode);
        logger.debug("Payment transaction id : " + transactionId);

        return serverReferenceCode;
    }

    public String saveRefundTransaction(User user, String clientCoorelator, String endUserId, double amount, String currency, String description,
            String onBehalfOf, String purchastCatCode, String channel, double taxAmount, String refCode, String originalServerReferenceCode, String transOpStatus, String callbackData, String notifyURL, String mandateId, String notificationFormat, String productId, int referenceSequence, String serviceId, String code, Double totalAmountCharged, Double amountReserved, int paymentTranscationType) {

        Session sess = null;
        Transaction tx = null;
        String transactionId = null;

        try {

            sess = HibernateUtil.getSession();
            tx = sess.beginTransaction();
            ChargeAmountRequest chargerequest = new ChargeAmountRequest();
            chargerequest.setAmount(amount);
            chargerequest.setChannel(channel);
            chargerequest.setClientCorrelator(clientCoorelator);
            chargerequest.setCurrency(currency);
            chargerequest.setDate(new Date());
            chargerequest.setDescription(description);
            chargerequest.setEndUserId(endUserId);
            chargerequest.setOnBehalfOf(onBehalfOf);
            chargerequest.setPurchaseCategoryCode(purchastCatCode);
            chargerequest.setReferenceCode(refCode);
            chargerequest.setOriginalServerReferenceCode(originalServerReferenceCode);
            chargerequest.setTaxAmount(taxAmount);
            chargerequest.setTransactionOperationStatus(transOpStatus);
            chargerequest.setUser(user);
            chargerequest.setCallbackData(callbackData);
            chargerequest.setNotifyURL(notifyURL);
            chargerequest.setMandateId(mandateId);
            chargerequest.setNotificationFormat(notificationFormat);
            chargerequest.setProductId(productId);
            chargerequest.setReferenceSequence(referenceSequence);
            chargerequest.setServiceId(serviceId);
            chargerequest.setCode(code);
            chargerequest.setTotalAmountCharged(totalAmountCharged);
            chargerequest.setAmountReserved(amountReserved);
            chargerequest.setPaymentTranscationType(paymentTranscationType);

            sess.save(chargerequest);

            int chargeId = chargerequest.getChargeId();
            ChargeAmountRequest lastChargeAmountRequest = (ChargeAmountRequest) sess.get(ChargeAmountRequest.class, chargeId);
            if (lastChargeAmountRequest == null) {
                logger.debug("Last transaction not found");
                throw new Exception("Last transaction not found");
            }

            transactionId = "paytran-" + chargeId;
            lastChargeAmountRequest.setTransactionId(transactionId);
            sess.update(lastChargeAmountRequest);

            String originalServerReferenceCodeParts[] = originalServerReferenceCode.split("-");
            ChargeAmountRequest previousChargeAmountRequest = (ChargeAmountRequest) sess.get(ChargeAmountRequest.class, Integer.parseInt(originalServerReferenceCodeParts[1]));
            if (lastChargeAmountRequest == null) {
                logger.debug("Last transaction not found");
                throw new Exception("Last transaction not found");
            }
            previousChargeAmountRequest.setRefundStatus(1);
            sess.update(previousChargeAmountRequest);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            logger.error("Error in saveTransaction : " + e);
            return null;
        } finally {
            sess.close();
        }
        logger.debug("Payment transaction id : " + transactionId);

        return transactionId;
    }

    public String saveReservationTransaction(User user, String clientCoorelator, String endUserId, double amount, String currency, String description,
            String onBehalfOf, String purchastCatCode, String channel, double taxAmount, String refCode, String transOpStatus, String callbackData, String notifyURL, String mandateId, String notificationFormat, String productId, int referenceSequence, String serviceId, String code, Double totalAmountCharged, Double amountReserved, int paymentTranscationType) {

        Session sess = null;
        Transaction tx = null;
        String transactionId = null;

        try {

            sess = HibernateUtil.getSession();
            tx = sess.beginTransaction();
            ChargeAmountRequest chargerequest = new ChargeAmountRequest();
            chargerequest.setAmount(amount);
            chargerequest.setChannel(channel);
            chargerequest.setClientCorrelator(clientCoorelator);
            chargerequest.setCurrency(currency);
            chargerequest.setDate(new Date());
            chargerequest.setDescription(description);
            chargerequest.setEndUserId(endUserId);
            chargerequest.setOnBehalfOf(onBehalfOf);
            chargerequest.setPurchaseCategoryCode(purchastCatCode);
            chargerequest.setReferenceCode(refCode);
            chargerequest.setTaxAmount(taxAmount);
            chargerequest.setTransactionOperationStatus(transOpStatus);
            chargerequest.setUser(user);
            chargerequest.setCallbackData(callbackData);
            chargerequest.setNotifyURL(notifyURL);
            chargerequest.setMandateId(mandateId);
            chargerequest.setNotificationFormat(notificationFormat);
            chargerequest.setProductId(productId);
            chargerequest.setReferenceSequence(referenceSequence);
            chargerequest.setServiceId(serviceId);
            chargerequest.setCode(code);
            chargerequest.setTotalAmountCharged(totalAmountCharged);
            chargerequest.setAmountReserved(amountReserved);
            chargerequest.setPaymentTranscationType(paymentTranscationType);

            sess.save(chargerequest);

            int chargeId = chargerequest.getChargeId();
            ChargeAmountRequest lastChargeAmountRequest = (ChargeAmountRequest) sess.get(ChargeAmountRequest.class, chargeId);
            if (lastChargeAmountRequest == null) {
                logger.debug("Last transaction not found");
                throw new Exception("Last transaction not found");
            }

            transactionId = "paytran-" + chargeId;
            lastChargeAmountRequest.setTransactionId(transactionId);
            sess.update(lastChargeAmountRequest);

            PaymentTransaction paymentTransaction = new PaymentTransaction();
            paymentTransaction.setTransactionId(transactionId);
            paymentTransaction.setAmount(amount);
            paymentTransaction.setCurrency(currency);
            paymentTransaction.setDate(new Date());
            paymentTransaction.setEndUserId(endUserId);
            paymentTransaction.setUser(user);

            sess.save(paymentTransaction);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            logger.error("Error in saveReservationTransaction : " + e);
            return null;
        } finally {
            sess.close();
        }
        logger.debug("Payment transaction id : " + transactionId);
        return transactionId;
    }

    public String saveAdditionalReservationTransaction(User user, String clientCoorelator, String endUserId, double amount, String currency, String description,
            String onBehalfOf, String purchastCatCode, String channel, double taxAmount, String refCode, String transOpStatus, String callbackData, String notifyURL, String mandateId, String notificationFormat, String productId, int referenceSequence, String serviceId, String code, Double totalAmountCharged, Double amountReserved, int paymentTranscationType, String previousTransactionId) {

        Session sess = null;
        Transaction tx = null;

        try {

            sess = HibernateUtil.getSession();
            tx = sess.beginTransaction();
            ChargeAmountRequest chargerequest = new ChargeAmountRequest();
            chargerequest.setAmount(amount);
            chargerequest.setChannel(channel);
            chargerequest.setClientCorrelator(clientCoorelator);
            chargerequest.setCurrency(currency);
            chargerequest.setDate(new Date());
            chargerequest.setDescription(description);
            chargerequest.setEndUserId(endUserId);
            chargerequest.setOnBehalfOf(onBehalfOf);
            chargerequest.setPurchaseCategoryCode(purchastCatCode);
            chargerequest.setReferenceCode(refCode);
            chargerequest.setTaxAmount(taxAmount);
            chargerequest.setTransactionOperationStatus(transOpStatus);
            chargerequest.setUser(user);
            chargerequest.setCallbackData(callbackData);
            chargerequest.setNotifyURL(notifyURL);
            chargerequest.setMandateId(mandateId);
            chargerequest.setNotificationFormat(notificationFormat);
            chargerequest.setProductId(productId);
            chargerequest.setReferenceSequence(referenceSequence);
            chargerequest.setServiceId(serviceId);
            chargerequest.setCode(code);
            chargerequest.setTotalAmountCharged(totalAmountCharged);
            chargerequest.setAmountReserved(amountReserved);
            chargerequest.setTransactionId(previousTransactionId);
            chargerequest.setPaymentTranscationType(paymentTranscationType);

            sess.save(chargerequest);

            //Update the payment transaction table
            PaymentTransaction paymentTransaction = (PaymentTransaction) sess.get(PaymentTransaction.class, previousTransactionId);
            if (paymentTransaction == null) {
                logger.debug("Transaction not found");
                throw new Exception("Transaction not found");
            }

            double previousAmount = paymentTransaction.getAmount();
            double totalReservedAmount = previousAmount + amount;
            paymentTransaction.setAmount(totalReservedAmount);
            sess.update(paymentTransaction);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            logger.error("Error in saveAdditionalReservationTransaction : " + e);
            return null;
        } finally {
            sess.close();
        }
        return previousTransactionId;
    }

    public String saveChargeReservationTransaction(User user, String clientCoorelator, String endUserId, double amount, String currency, String description,
            String onBehalfOf, String purchastCatCode, String channel, double taxAmount, String refCode, String transOpStatus, String callbackData, String notifyURL, String mandateId, String notificationFormat, String productId, int referenceSequence, String serviceId, String code, Double totalAmountCharged, Double amountReserved, int paymentTranscationType, String previousTransactionId) {

        Session sess = null;
        Transaction tx = null;
        String serverReferenceCode = null;

        try {

            sess = HibernateUtil.getSession();
            tx = sess.beginTransaction();
            ChargeAmountRequest chargerequest = new ChargeAmountRequest();
            chargerequest.setAmount(amount);
            chargerequest.setChannel(channel);
            chargerequest.setClientCorrelator(clientCoorelator);
            chargerequest.setCurrency(currency);
            chargerequest.setDate(new Date());
            chargerequest.setDescription(description);
            chargerequest.setEndUserId(endUserId);
            chargerequest.setOnBehalfOf(onBehalfOf);
            chargerequest.setPurchaseCategoryCode(purchastCatCode);
            chargerequest.setReferenceCode(refCode);
            chargerequest.setTaxAmount(taxAmount);
            chargerequest.setTransactionOperationStatus(transOpStatus);
            chargerequest.setUser(user);
            chargerequest.setCallbackData(callbackData);
            chargerequest.setNotifyURL(notifyURL);
            chargerequest.setMandateId(mandateId);
            chargerequest.setNotificationFormat(notificationFormat);
            chargerequest.setProductId(productId);
            chargerequest.setReferenceSequence(referenceSequence);
            chargerequest.setServiceId(serviceId);
            chargerequest.setCode(code);
            chargerequest.setTotalAmountCharged(totalAmountCharged);
            chargerequest.setAmountReserved(amountReserved);
            chargerequest.setTransactionId(previousTransactionId);
            chargerequest.setPaymentTranscationType(paymentTranscationType);

            sess.save(chargerequest);

            int chargeId = chargerequest.getChargeId();
            
            //Update the payment transaction table
            PaymentTransaction paymentTransaction = (PaymentTransaction) sess.get(PaymentTransaction.class, previousTransactionId);
            if (paymentTransaction == null) {
                logger.debug("Transaction not found");
                throw new Exception("Transaction not found");
            }

            double previousAmount = paymentTransaction.getAmount();
            double remainingReservedAmount = previousAmount - totalAmountCharged;
            paymentTransaction.setAmount(remainingReservedAmount);
            sess.update(paymentTransaction);
            
            serverReferenceCode = "src-" + chargeId;

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            logger.error("Error in saveChargeReservationTransaction : " + e);
            return null;
        } finally {
            sess.close();
        }
        
        logger.debug("Server reference code : " + serverReferenceCode);
        logger.debug("Previous payment transaction id : " + previousTransactionId);
        
        return serverReferenceCode;
    }

    public String saveReleasedReservationTransaction(User user, String clientCoorelator, String endUserId, double amount, String currency, String description,
            String onBehalfOf, String purchastCatCode, String channel, double taxAmount, String refCode, String transOpStatus, String callbackData, String notifyURL, String mandateId, String notificationFormat, String productId, int referenceSequence, String serviceId, String code, Double totalAmountCharged, Double amountReserved, int paymentTranscationType, String previousTransactionId) {

        Session sess = null;
        Transaction tx = null;

        try {

            sess = HibernateUtil.getSession();
            tx = sess.beginTransaction();
            ChargeAmountRequest chargerequest = new ChargeAmountRequest();
            chargerequest.setAmount(amount);
            chargerequest.setChannel(channel);
            chargerequest.setClientCorrelator(clientCoorelator);
            chargerequest.setCurrency(currency);
            chargerequest.setDate(new Date());
            chargerequest.setDescription(description);
            chargerequest.setEndUserId(endUserId);
            chargerequest.setOnBehalfOf(onBehalfOf);
            chargerequest.setPurchaseCategoryCode(purchastCatCode);
            chargerequest.setReferenceCode(refCode);
            chargerequest.setTaxAmount(taxAmount);
            chargerequest.setTransactionOperationStatus(transOpStatus);
            chargerequest.setUser(user);
            chargerequest.setCallbackData(callbackData);
            chargerequest.setNotifyURL(notifyURL);
            chargerequest.setMandateId(mandateId);
            chargerequest.setNotificationFormat(notificationFormat);
            chargerequest.setProductId(productId);
            chargerequest.setReferenceSequence(referenceSequence);
            chargerequest.setServiceId(serviceId);
            chargerequest.setCode(code);
            chargerequest.setTotalAmountCharged(totalAmountCharged);
            chargerequest.setAmountReserved(amountReserved);
            chargerequest.setTransactionId(previousTransactionId);
            chargerequest.setPaymentTranscationType(paymentTranscationType);

            sess.save(chargerequest);

            //Update the payment transaction table
            PaymentTransaction paymentTransaction = (PaymentTransaction) sess.get(PaymentTransaction.class, previousTransactionId);
            if (paymentTransaction == null) {
                logger.debug("Transaction not found");
                throw new Exception("Transaction not found");
            }

            paymentTransaction.setAmount(0.0);
            sess.update(paymentTransaction);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            logger.error("Error in saveReleasedReservationTransaction : " + e);
            return null;
        } finally {
            sess.close();
        }
        return previousTransactionId;
    }

    public int getTransactionCount(int userId) {
        List countList = null;
        int count = 0;
        Session sess = HibernateUtil.getSession();
        try {
            String getterString = "SELECT COUNT(*) FROM ChargeAmountRequest AS chargeAmountRequest WHERE chargeAmountRequest.date = :tranDate AND chargeAmountRequest.user.id = :userid";
            Query subQuery = sess.createQuery(getterString);
            subQuery.setDate("tranDate", new Date());
            subQuery.setInteger("userid", userId);

            countList = subQuery.list();
            Iterator iter = countList.iterator();
            while (iter.hasNext()) {
                count = Integer.parseInt(iter.next().toString());
            }
        } catch (Exception e) {
            logger.error("Error in getTransactionCount : " + e);
        } finally {
            sess.close();
        }
        return count;
    }
    
    public ChargeAmountRequest checkClientCorrelator(int userId, String clientCorrelator) {
        List paymentRequestList = null;
        ChargeAmountRequest chargeAmountRequest = null;
        Session sess = HibernateUtil.getSession();
        try {
            String getterString = "FROM ChargeAmountRequest AS chargeAmountRequest WHERE chargeAmountRequest.clientCorrelator = :clientCorrelator AND chargeAmountRequest.user.id = :userid";
            Query subQuery = sess.createQuery(getterString);
            subQuery.setString("clientCorrelator", clientCorrelator);
            subQuery.setInteger("userid", userId);

            paymentRequestList = subQuery.list();
            Iterator iter = paymentRequestList.iterator();
            while (iter.hasNext()) {
                chargeAmountRequest = (ChargeAmountRequest) iter.next();
            }
        } catch (Exception e) {
            logger.error("Error in checkClientCorrelator : " + e);
        } finally {
            sess.close();
        }
        return chargeAmountRequest;
    }
}

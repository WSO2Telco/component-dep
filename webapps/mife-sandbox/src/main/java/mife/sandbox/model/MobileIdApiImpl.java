/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mife.sandbox.model;

import java.util.Date;

import java.util.UUID;

import java.util.List;


import mife.sandbox.model.entities.LocationRequestLog;
import mife.sandbox.model.entities.Locationparam;
import mife.sandbox.model.entities.ManageNumber;
import mife.sandbox.model.entities.MobileApiEncodeRequest;
import mife.sandbox.model.entities.MobileIdApiRequest;
import mife.sandbox.model.entities.User;

import org.apache.commons.codec.binary.Base64;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Dialog
 */
public class MobileIdApiImpl {

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

    public ManageNumber getWhitelisted(int userid, String num) {

        Session sess = HibernateUtil.getSession();
        ManageNumber whitelisted = null;
        try {
            whitelisted = (ManageNumber) sess.createQuery("from ManageNumber where user.id = ? and number = ?").setInteger(0, userid).setString(1, num).uniqueResult();

        } catch (Exception e) {
            System.out.println("getUserWhitelist: " + e);
        } finally {
            sess.close();
        }
        return whitelisted;
    }

    public MobileIdApiRequest saveTransaction(String sub, String email, String name, String family_name, String preferred_username, String given_name, User user) {

        Session sess = null;
        Transaction tx = null;
        MobileIdApiRequest mobileIdApiRequest = new MobileIdApiRequest();
        try {

            sess = HibernateUtil.getSession();
            tx = sess.beginTransaction();

            mobileIdApiRequest.setSub(sub);
            mobileIdApiRequest.setEmail(email);
            mobileIdApiRequest.setName(name);
            mobileIdApiRequest.setFamily_name(family_name);
            mobileIdApiRequest.setPreferred_username(preferred_username);
            mobileIdApiRequest.setGiven_name(given_name);

            mobileIdApiRequest.setTransactionstatus("ACTIVE");
            mobileIdApiRequest.setDate(new Date());
            mobileIdApiRequest.setUser(user);
            sess.save(mobileIdApiRequest);

            tx.commit();
            return mobileIdApiRequest;

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
            return null;
        } finally {

            sess.close();
        }
    }

    public MobileApiEncodeRequest saveClientAuthCode(String consumerKey, String consumerSecret) {
        MobileApiEncodeRequest mobileApiEncodeRequest = new MobileApiEncodeRequest();
        Session sess = null;
        Transaction tx = null;

        try {
            sess = HibernateUtil.getSession();
            tx = sess.beginTransaction();
            byte[] encodedBytes = Base64.encodeBase64((consumerKey + ":" + consumerSecret).getBytes());
            String oauthCode = new String(encodedBytes);
            mobileApiEncodeRequest.setKey(consumerKey);
            mobileApiEncodeRequest.setSecret(consumerSecret);
            mobileApiEncodeRequest.setAuthCode(oauthCode);

            sess.save(mobileApiEncodeRequest);
            tx.commit();
            return mobileApiEncodeRequest;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
            return null;
        } finally {

            sess.close();
        }

    }

    public Locationparam queryLocationParam(Integer userId) {

        Session sess = null;
        Transaction tx = null;
        Locationparam locparam = null;

        try {

            sess = HibernateUtil.getSession();
            locparam = (Locationparam) sess.createQuery("from Locationparam where user = ?").setInteger(0, userId).uniqueResult();
        } catch (Exception e) {
            tx.rollback();
            //LOG.error("[ApplicationManager], RemveInbound + " + "Error " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            sess.close();
        }
        return locparam;
    }

	public MobileIdApiRequest getFromTable(String authorization, User user) {
		
		
		 Session sess = null;
	        Transaction tx = null;
	        MobileIdApiRequest mobileIdApiRequest = new MobileIdApiRequest();
	        try {

	            sess = HibernateUtil.getSession();
	            tx = sess.beginTransaction();

	    		
	    		Criteria criteria = sess.createCriteria(MobileIdApiRequest.class);
	    		//criteria.add(Restrictions.eq("fieldVariable", anyValue));
	    		criteria.add(Restrictions.sqlRestriction("1=1 order by rand()"));
	    		criteria.setMaxResults(1);
	    		mobileIdApiRequest=(MobileIdApiRequest) criteria.uniqueResult();
	    		
	            tx.commit();
	            return mobileIdApiRequest;

	        } catch (Exception e) {
	            tx.rollback();
	            e.printStackTrace();
	            return null;
	        } finally {

	            sess.close();
	        }		
	}

	public MobileApiEncodeRequest saveTokenData(String granttype, String username,String password, String scope, User user,String authcode) {
        MobileApiEncodeRequest mobileApiEncodeRequest = new MobileApiEncodeRequest();
        Session sess = null;
        Transaction tx = null;

        try {
            sess = HibernateUtil.getSession();
            tx = sess.beginTransaction();
            
            Query query=sess.createQuery("from MobileApiEncodeRequest where authcode=:authcode");
            query.setString("authcode", authcode);
            query.setMaxResults(1);
            mobileApiEncodeRequest=(MobileApiEncodeRequest) query.uniqueResult();           
            
/*            Criteria criteria = sess.createCriteria(MobileApiEncodeRequest.class);
    		criteria.add(Restrictions.eq("token", token));
    		criteria.setMaxResults(1);
    		mobileApiEncodeRequest=(MobileApiEncodeRequest) criteria.uniqueResult();*/
            
            mobileApiEncodeRequest.setGranttype(granttype);
            mobileApiEncodeRequest.setUsername(username);
            mobileApiEncodeRequest.setPassword(password);
            mobileApiEncodeRequest.setScope(scope);
            mobileApiEncodeRequest.setUser(String.valueOf(user.getId()));

            sess.update(mobileApiEncodeRequest);
            tx.commit();
            return mobileApiEncodeRequest;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
            return null;
        } finally {

            sess.close();
        }

    }

	public MobileApiEncodeRequest getTokenData(String granttype,String username, String password, String scope) {
        MobileApiEncodeRequest mobileApiEncodeRequest = new MobileApiEncodeRequest();
        Session sess = null;
        Transaction tx = null;

        try {
            sess = HibernateUtil.getSession();
            tx = sess.beginTransaction();
            
            Query query=sess.createQuery("from MobileApiEncodeRequest where granttype=:granttype and username=:username and password=:password and scope=:scope");
            query.setString("granttype", granttype);
            query.setString("username", username);
            query.setString("password", password);
            query.setString("scope", scope);
            query.setMaxResults(1);
            mobileApiEncodeRequest=(MobileApiEncodeRequest) query.uniqueResult();           

            String refreshToken = UUID.randomUUID().toString().replace("-", "");
            mobileApiEncodeRequest.setRefreshToken(refreshToken);

            String accessToken = UUID.randomUUID().toString().replace("-", "");
            mobileApiEncodeRequest.setAccessToken(accessToken);

            sess.update(mobileApiEncodeRequest);

            tx.commit();
            return mobileApiEncodeRequest;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
            return null;
        } finally {

            sess.close();
        }
    }
	
	public MobileApiEncodeRequest getAcccessToken(String grantType, String refresh_token, String scope, String authCode) {
		MobileApiEncodeRequest mobileApiEncodeRequest = new MobileApiEncodeRequest();
        Session sess = null;
        Transaction tx = null;

        try {
            sess = HibernateUtil.getSession();
            tx = sess.beginTransaction();
            
            Query query=sess.createQuery("from MobileApiEncodeRequest where authcode=:authCode and refreshToken=:refresh_token");
            query.setString("authCode", authCode);
            query.setString("refresh_token", refresh_token);
            query.setMaxResults(1);
            mobileApiEncodeRequest=(MobileApiEncodeRequest) query.uniqueResult();           

            String accessToken = UUID.randomUUID().toString().replace("-", "");
            mobileApiEncodeRequest.setAccessToken(accessToken);
            mobileApiEncodeRequest.setGranttype(grantType);
            mobileApiEncodeRequest.setRefreshToken(refresh_token);
            mobileApiEncodeRequest.setScope(scope);

            sess.update(mobileApiEncodeRequest);

            tx.commit();
            return mobileApiEncodeRequest;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
            return null;
        } finally {

            sess.close();
        }
	}
}

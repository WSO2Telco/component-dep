/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mife.sandbox.model;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 *
 * @author User
 */
public class HibernateUtil_old_bak {
     private static SessionFactory sessionFactory;
////    static {
////        try {
////            sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
////        } catch (Throwable ex) {
////            throw new ExceptionInInitializerError(ex);
////        }
////    }
    /**
     * 
     */
    public HibernateUtil_old_bak() {
        
    }
    /**
     * 
     * @return
     * @throws HibernateException
     */
    public static Session getSession() throws HibernateException {
        try {
            sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
        return sessionFactory.openSession();
    }
}

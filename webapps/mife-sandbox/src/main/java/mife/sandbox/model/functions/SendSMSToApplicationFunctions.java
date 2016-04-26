package mife.sandbox.model.functions;

import mife.sandbox.model.HibernateUtil;
import mife.sandbox.model.entities.SendSMSToApplication;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author User
 */
public class SendSMSToApplicationFunctions {

    public static void saveSendSMSToApplication(SendSMSToApplication smsmsg) {

        Session s = HibernateUtil.getSession();
        Transaction tx;
        try {
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
        }
        s.close();
    }
}

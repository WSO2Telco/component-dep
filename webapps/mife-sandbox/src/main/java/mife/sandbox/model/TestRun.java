/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mife.sandbox.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import mife.sandbox.controller.UserControlFunctions;
import mife.sandbox.model.entities.SMSRequestLog;
import mife.sandbox.model.entities.SenderAddress;
import mife.sandbox.model.entities.User;
import mife.sandbox.model.functions.SenderAddressFunctions;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

/**
 *
 * @author User
 */
public class TestRun {
    public static void main(String arg[]){
//        AnnotationConfiguration config = new AnnotationConfiguration();
//        config.addAnnotatedClass(User.class);
//        config.configure("hibernate.cfg.xml");
        
//        new SchemaExport(config).create(true, true);
        
//        User u = new User();
//        u.setUserName("Test User");
//        u.setUserStatus(1);
//        
//        if(UserFunctions.createUser(u)){
//            System.out.println("ACTION: User "+u.getUserName()+" created successfully");
//        }
//        ManageNumber n = ManageNumberFunctions.searchNumber(21);
//        System.out.println("NUM: "+n.getNumber());
//        System.out.println("DESC: "+n.getDescription());
//        System.out.println("BAL: "+n.getBalance());
//        
//        String return_val;
//        
//        SenderAddress sa = new SenderAddress();
//            sa.setShortCode("7788");
//            sa.setDescription("Second code to test");
//            sa.setUser(UserControlFunctions.getUserObj(1));
//            
//            int[] receivedData = SenderAddressFunctions.saveSenderAddress(sa);
//            if (receivedData[0] == 201) {
//                return_val = "true," + receivedData[1];
//            } else {
//                return_val = "false";
//            }
//            System.out.println(return_val);
        
    }
}

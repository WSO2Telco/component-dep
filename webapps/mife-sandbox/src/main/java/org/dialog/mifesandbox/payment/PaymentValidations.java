/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dialog.mifesandbox.payment;

import java.sql.ResultSet;

/**
 *
 * @author User
 */
public class PaymentValidations {

    private static final String[] telFormats = {
        "tel\\:\\+[0-9]+", "tel\\:[0-9]+"
    };

    public static boolean isCorrectlyFormattedNumber(String tel) {

        boolean matched = false;
        if (tel != null) {
            for (int i = 0; i < telFormats.length && !matched; i++) {
                if (tel.matches(telFormats[i])) {
                    matched = true;
                }
                System.out.println("Number=" + tel + " matches regex=" + telFormats[i] + " = " + matched);
            }
        }
        return matched;
    }
}

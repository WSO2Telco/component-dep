/**
 * 
 */
package com.wso2telco.dep.custom.jwt;

import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class JWTUnitTest extends TestCase {

	/**
	 * @param name
	 */
	public JWTUnitTest(String name) {
		super(name);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(JWTUnitTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		assertTrue(true);
	}

	public void testFormatUsrRoles() {
		
		String[] expectedResult = new String[] { "abcd1234", "DFG12" };
		String[] result = CommonUtils.formatStringsToAlphaNum(new String[] { "abcd!@#1234", "D$F%G^12" });
		assertTrue(Arrays.equals(expectedResult, result));

	}

}

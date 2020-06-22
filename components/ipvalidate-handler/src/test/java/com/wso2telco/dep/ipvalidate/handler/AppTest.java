package com.wso2telco.dep.ipvalidate.handler;

import com.wso2telco.dep.ipvalidate.handler.validation.utils.IPValidationDBUtils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
    
//    public void test()
//    {
//    	IPValidationDBUtils ipvalidationDBUtils = new IPValidationDBUtils();
//    	try {
//			ipvalidationDBUtils.getClientKeyList();
//			assertTrue( false );
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			assertTrue( false );
//		}
//        
//    }
}

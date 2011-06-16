/*
 * (c) Copyright IBM Corp 2006 
 */

package com.ibm.wsdl;

import javax.xml.namespace.QNameSerializationTest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.ibm.wsdl.extensions.soap12.ReadSOAP12Test;
import com.ibm.wsdl.factory.WSDLFactoryPropertyTest;
import com.ibm.wsdl.xml.ImportWSDLTest;
import com.ibm.wsdl.xml.SetFactoryNameTest;
import com.ibm.wsdl.xml.WSDLExceptionTest;

public class WSDL4JTestSuite extends TestCase
{
  public static Test suite()
  {
    TestSuite testSuite = new TestSuite();

    testSuite.addTestSuite(ImportWSDLTest.class);
    testSuite.addTestSuite(SetFactoryNameTest.class);
    testSuite.addTestSuite(WSDLExceptionTest.class);
    testSuite.addTestSuite(ExtendedWSDLCreator.class);
    testSuite.addTestSuite(ReadSOAP12Test.class);
    testSuite.addTestSuite(WSDLFactoryPropertyTest.class);
    testSuite.addTestSuite(WSDLLocatorTest.class);
    testSuite.addTestSuite(RecursiveGetTest.class);
    testSuite.addTestSuite(QNameSerializationTest.class);

    return testSuite;
  }
}
/*
 * (c) Copyright IBM Corp 2006 
 */

package com.ibm.wsdl.xml;

import javax.wsdl.Definition;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;

import com.ibm.wsdl.factory.WSDLFactoryImpl3;

import junit.framework.TestCase;


public class SetFactoryNameTest extends TestCase
{
  public SetFactoryNameTest()
  {
    super("WSDLPrinter");
  }
  
  /**
   * Test that if the FactoryImplName is set to the same string
   * as it is already, then the FactoryImpl cache is not cleared.
   * 
   * @throws Exception
   */
  public void testSetFactoryName() throws Exception
  {
    WSDLFactory factory = WSDLFactory.newInstance();
    WSDLReader reader = factory.newWSDLReader();

    reader.setFeature("javax.wsdl.verbose", true);
    reader.setFeature("javax.wsdl.importDocuments", true);
    
    String wsdlURI = "test/resources/MyWSDL.wsdl";
    
    //set the factory name once
    reader.setFactoryImplName("com.ibm.wsdl.factory.WSDLFactoryImpl3");
    Definition def = reader.readWSDL(null, wsdlURI);
    
    //set the factory name again to the same string
    reader.setFactoryImplName("com.ibm.wsdl.factory.WSDLFactoryImpl3");
    def = reader.readWSDL(null, wsdlURI);
    
    //if the cache had been (incorrectly) cleared then the ctor
    //would have been called more than once
    if(WSDLFactoryImpl3.ctorCount > 1)
    {
      fail("WSDLFactoryImpl3 constructor called too many times");
    }
  }  
}

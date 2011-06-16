/*
 * (c) Copyright IBM Corp 2006 
 */

package com.ibm.wsdl.xml;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLLocator;
import javax.wsdl.xml.WSDLReader;

import org.xml.sax.InputSource;

import com.ibm.wsdl.factory.WSDLFactoryImpl3;

import junit.framework.TestCase;


public class WSDLExceptionTest extends TestCase
{
  class MyWSDLLocator implements WSDLLocator
  {  
    public MyWSDLLocator()
    {      
    }

    public InputSource getBaseInputSource()
    {
      throw new RuntimeException("Test Runtime Exception");
    }

    public InputSource getImportInputSource(String parentLocation, String importLocation)
    {
      throw new RuntimeException("Test Runtime Exception");
    }

    public String getBaseURI()
    {
      throw new RuntimeException("Test Runtime Exception");
    }

    public String getLatestImportURI()
    {
      throw new RuntimeException("Test Runtime Exception");
    }

    public void close()
    {
      // TODO Auto-generated method stub
      
    }
  }

  public WSDLExceptionTest()
  {
    super("WSDLException");
  }
  
  /**
   * Test that WSDLException.toString returns the correct string.
   * 
   * @throws Exception
   */
  public void testToString() throws Exception
  {
    WSDLFactory factory = WSDLFactory.newInstance();
    WSDLReader reader = factory.newWSDLReader();

    reader.setFeature("javax.wsdl.verbose", true);
    reader.setFeature("javax.wsdl.importDocuments", true);
    
    String wsdlURI = "rubbishURI";
    String toString = null;
    try
    {
      Definition def = reader.readWSDL(null, wsdlURI);
    }
    catch(WSDLException e)
    {
      toString = e.toString();
    }
    
    //System.out.println(toString);
    
    assertTrue("String is not correct",toString.startsWith("javax.wsdl.WSDLException: WSDLException: faultCode=OTHER_ERROR: Unable to resolve imported document at 'rubbishURI'.: java.io.FileNotFoundException: This file was not found: file:"));
    
    Exception e = new IllegalArgumentException("Test IllegalArgument");
    
    WSDLException wsdlException = new WSDLException(WSDLException.CONFIGURATION_ERROR,"Test WSDL Exception",e);
    
    toString = wsdlException.toString();
    
    assertEquals("String is not correct","javax.wsdl.WSDLException: WSDLException: faultCode=CONFIGURATION_ERROR: Test WSDL Exception: java.lang.IllegalArgumentException: Test IllegalArgument",toString);       
  }

  /**
   * Test that RuntimeExceptions are thrown all the way up.
   * 
   * @throws Exception
   */
  public void testRuntimeException() throws Exception
  {
    WSDLFactory factory = WSDLFactory.newInstance();
    WSDLReader reader = factory.newWSDLReader();
  
    reader.setFeature("javax.wsdl.verbose", true);
    reader.setFeature("javax.wsdl.importDocuments", true);
    
    WSDLLocator myWSDLLocator = new MyWSDLLocator();
    try
    {
      Definition def = reader.readWSDL(myWSDLLocator);
    }
    catch(WSDLException e)
    {
      fail("Should not have caught a WSDLException");
    }    
    catch(RuntimeException e)
    {
      //System.out.println("RuntimeException correctly thrown");
      //this is what we wanted
    }
  }  
}

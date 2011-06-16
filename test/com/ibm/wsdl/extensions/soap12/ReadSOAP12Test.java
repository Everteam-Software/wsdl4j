/*
 * (c) Copyright IBM Corp 2006 
 */

package com.ibm.wsdl.extensions.soap12;

import javax.wsdl.Definition;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.wsdl.xml.WSDLWriter;

import junit.framework.TestCase;

public class ReadSOAP12Test extends TestCase
{
  public ReadSOAP12Test()
  {
    super("ReadSOAP12Test");
  }    

  public void testReadSOAP12() throws Exception
  {
    String wsdlURI = "test/resources/SOAP12Test.wsdl";
    
    WSDLFactory factory = WSDLFactory.newInstance();
    WSDLReader reader = factory.newWSDLReader();
    reader.setExtensionRegistry(factory.newPopulatedExtensionRegistry());

    reader.setFeature("javax.wsdl.verbose", true);
    reader.setFeature("javax.wsdl.importDocuments", true);

    Definition def = reader.readWSDL(null, wsdlURI);
    
    WSDLWriter writer = factory.newWSDLWriter();
    
    writer.writeWSDL(def,System.out);
  }
}

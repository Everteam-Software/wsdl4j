/*
 * (c) Copyright IBM Corp 2006 
 */
package com.ibm.wsdl.factory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.wsdl.Definition;
import javax.wsdl.Input;
import javax.wsdl.Operation;
import javax.wsdl.Output;
import javax.wsdl.PortType;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;

import junit.framework.TestCase;

public class WSDLFactoryPropertyTest extends TestCase
{
  private static final String META_INF_SERVICES_PROPERTY_FILE_NAME =
    "javax.wsdl.factory.WSDLFactory";
  private static final String DEFAULT_FACTORY_IMPL_NAME =
    "com.ibm.wsdl.factory.WSDLFactoryImpl";
  private static final String ALT_FACTORY_IMPL_NAME =
    "com.ibm.wsdl.factory.WSDLFactoryImpl2";
  
  public WSDLFactoryPropertyTest()
  {
    super("WSDLPrinter");
  }
  
  public void testMETAINFServicesConfig() throws Exception
  {
    setMETAINFServicesData(DEFAULT_FACTORY_IMPL_NAME);
    try
    {
      checkWSDLFactory(null);
    }     
    finally
    {
      setMETAINFServicesData(null);
    }
  }
  
  public void testMETAINFServicesConfigAlternate() throws Exception
  {
    setMETAINFServicesData(ALT_FACTORY_IMPL_NAME);
    try
    {
      WSDLFactoryImpl2.ctor = 0;
      checkWSDLFactory(null);
      assertEquals("Wrong WSDLFactoryImpl used",2,WSDLFactoryImpl2.ctor);
    }
    finally
    {
      setMETAINFServicesData(null);
    }
  }
  
  public void testMETAINFServicesConfigFailure() throws Exception
  {
    setMETAINFServicesData("some rubbish that should fail");
    try
    {
      checkWSDLFactory(null);
      fail("This shouldn't have worked");      
    }
    catch (WSDLException e)
    {
      if(e.getFaultCode() == WSDLException.CONFIGURATION_ERROR)
      {
        if(!(e.getTargetException() instanceof ClassNotFoundException))
        {
          fail(e.getTargetException().getMessage());
        }
      }
      else
      {
        fail(e.getFaultCode());
      }
    }
    finally
    {
      setMETAINFServicesData(null);
    }
  }
  
  public void testMETAINFServicesConfigAltClassLoader() throws Exception
  {
    setMETAINFServicesData(ALT_FACTORY_IMPL_NAME);
    try
    {
      WSDLFactoryImpl2.ctor = 0;
      MyClassLoader cl = new MyClassLoader();
      checkWSDLFactory(cl);
      assertEquals("Wrong WSDLFactoryImpl used",2,WSDLFactoryImpl2.ctor);
      assertEquals("Wrong ClassLoader used",ALT_FACTORY_IMPL_NAME,cl.last);
    }     
    finally
    {
      setMETAINFServicesData(null);
    }    
  }
  
  private void setMETAINFServicesData(String data)
  {
    String metaInfServicesDir = "META-INF/services/";
    
    try
    {
      URL url = this.getClass().getResource("/");
      File rootFile = new File(URI.create(url.toString()));
      File servicesFile = new File(rootFile,metaInfServicesDir);
      //System.out.println(servicesFile);
      servicesFile.mkdirs();
      
      File propFile = new File(servicesFile,META_INF_SERVICES_PROPERTY_FILE_NAME);
      
      //System.out.println(propFile);
      if(propFile.exists())
      {
        propFile.delete();        
      }
      
      if(data!=null)
      {
        propFile.createNewFile();
        FileWriter fw = new FileWriter(propFile);
        BufferedWriter bw = new BufferedWriter(fw);
        
        bw.write(data);
        
        bw.close();
        fw.close();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }
  
  private void checkWSDLFactory(ClassLoader loader) throws Exception
  {
    String wsdlURI = "test/resources/MyWSDL.wsdl";
    
    WSDLFactory factory = null;
    if(loader == null)
    {
      factory = WSDLFactory.newInstance();
    }
    else
    {
      factory = WSDLFactory.newInstance(ALT_FACTORY_IMPL_NAME, loader);
    }
    WSDLReader reader = factory.newWSDLReader();

    reader.setFeature("javax.wsdl.verbose", true);
    reader.setFeature("javax.wsdl.importDocuments", true);    

    Definition def = reader.readWSDL(null, wsdlURI);
  }
  
  private void printMap(Map map)
  {
    Iterator itr = map.keySet().iterator();
    while(itr.hasNext())
    {
      Object key = itr.next();
      System.out.print(key);
      System.out.print(" = ");
      Object data = map.get(key);
      if(data instanceof Map)
      {
        printMap((Map) data);
      }
      else if(data instanceof PortType)
      {
        printPortType((PortType)data);
      }
      else if(data instanceof String)
      {
        System.out.println("(S)"+data);
      }
      else
      {
        System.out.println("("+data.getClass()+")"+data);
      }
      System.out.println("--------********--------");
    }
  }
  
  private void printPortType(PortType portType)
  {
    String indent = "--> ";
    QName name = portType.getQName();
    System.out.println(indent+"Name = "+name.getLocalPart());
    System.out.println(indent+"TS = "+name.getNamespaceURI());
    List operations = portType.getOperations();
    if(operations.size() > 0)
    {
      System.out.println(indent+"Ops = ");
      Iterator itr = operations.iterator();
      while(itr.hasNext())
      {
        Operation op = (Operation) itr.next();
        System.out.println(indent+indent+" Name = "+op.getName());
        Input input = op.getInput();
        System.out.println(indent+indent+" Input = "+input.getName());
        System.out.println(indent+indent+indent+" Message = "+input.getMessage());
        Output output = op.getOutput();
        System.out.println(indent+indent+" Output = "+output.getName());
        System.out.println(indent+indent+indent+" Message = "+output.getMessage());
      }
    }
  }
}

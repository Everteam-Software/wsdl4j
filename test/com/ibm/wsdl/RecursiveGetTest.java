/*
 * (c) Copyright IBM Corp 2006 
 */
package com.ibm.wsdl;

import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;

import junit.framework.TestCase;

public class RecursiveGetTest extends TestCase
{
  public RecursiveGetTest()
  {
    super("WSDLPrinter");
  }
  
  public void testGetAllXXXText() throws Exception
  {
    String wsdlURI = "test/resources/testGetXXXA.wsdl";
    
    WSDLFactory factory = WSDLFactory.newInstance();
    WSDLReader reader = factory.newWSDLReader();

    reader.setFeature("javax.wsdl.verbose", true);
    reader.setFeature("javax.wsdl.importDocuments", true);
    reader.setFactoryImplName("com.ibm.wsdl.factory.WSDLFactoryImpl");

    Definition def = reader.readWSDL(null, wsdlURI);
    
    Map portTypes = def.getAllPortTypes();
    assertEquals("getAllportTypes() did not return the expected number of port types from the composite WSDL document.",
		         3, portTypes.size());

    Map bindings = def.getAllBindings();
    assertEquals("getAllBindings() did not return the expected number of bindings from the composite WSDL document.",
    		     3, bindings.size());
    
    Map services = def.getAllServices();
    assertEquals("getAllServices() did not return the expected number of services from the composite WSDL document.",
		         3, services.size());

    
    /* The following print statements can be used for diagnostics, 
     * but are not needed when running the WSDL4J junit test suite 
     */
    
    /*
    Iterator itr = bindings.keySet().iterator();
    while(itr.hasNext())
    {
      System.out.println(itr.next());
    }
    
    itr = portTypes.keySet().iterator();
    while(itr.hasNext())
    {
      System.out.println(itr.next());
    }
    
    itr = services.keySet().iterator();
    while(itr.hasNext())
    {
      System.out.println(itr.next());
    }
    */
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
      else if(data instanceof Vector)
      {
        printVector((Vector) data);
      }
      else if(data instanceof String)
      {
        System.out.println("(S)"+data);
      }
      else if(data instanceof Binding)
      {
        printBinding((Binding) data);
      }
      else if(data instanceof PortType)
      {
        printPortType((PortType) data);
      }
      else if(data instanceof Service)
      {
        printService((Service) data);
      }
      else
      {
        System.out.println("("+data.getClass()+")"+data);
      }
      System.out.println("--------********--------");
    }
  }
  
  private void printBinding(Binding binding)
  {
    System.out.println("Binding : "+binding.getQName());
  }
  
  private void printPortType(PortType portType)
  {
    System.out.println("PortType : "+portType.getQName());
  }
  
  private void printService(Service service)
  {
    System.out.println("Service : "+service.getQName());
  }
  
  private void printVector(Vector vector)
  {
    Iterator itr = vector.iterator();
    while(itr.hasNext())
    {
      Object data = itr.next();
      if(data != null)
      {
        if(data instanceof Map)
        {
          printMap((Map) data);
        }
        else if(data instanceof Vector)
        {
          printVector((Vector) data);
        }
        else if(data instanceof String)
        {
          System.out.println("(S)"+data);
        }
        else
        {
          System.out.println("("+data.getClass()+")"+data);
        }
      }
      System.out.println("--------********--------");
    }
  }
}

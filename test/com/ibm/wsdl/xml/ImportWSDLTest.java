/*
 * (c) Copyright IBM Corp 2006 
 */
package com.ibm.wsdl.xml;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLLocator;
import javax.wsdl.xml.WSDLReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.ibm.wsdl.util.StringUtils;

public class ImportWSDLTest extends TestCase
{
  public ImportWSDLTest()
  {
    super("WSDLPrinter");
  }
  
  /**
   * This test loads MyImportA which imports B, C and the B again.
   * C also imports B. MyWSDLLocator uses a single MyImportSource
   * for each wsdl ... if WSDL4J tries to import any one more than
   * once, the test will fail.
   * 
   * @throws Exception
   */
  public void testImportWSDL() throws Exception
  {
    String baseURI = "test/resources/imports/";
    String wsdlURIA = "MyImportA.wsdl";
    
    WSDLFactory factory = WSDLFactory.newInstance();
    WSDLReader reader = factory.newWSDLReader();

    reader.setFeature("javax.wsdl.verbose", true);
    reader.setFeature("javax.wsdl.importDocuments", true);
    reader.setFactoryImplName("com.ibm.wsdl.factory.WSDLFactoryImpl");    
    
    MyWSDLLocator loc = new MyWSDLLocator(baseURI, wsdlURIA);
    
    Definition defA = reader.readWSDL(loc);
    
//    Map importsA = defA.getImports();
//    System.out.println("======Imports A=======");
//    printMap(importsA);
//    System.out.println("=====================");
  }
  
  public void testImportWithNullBaseURI() throws Exception
  {
	    String wsdlLoc = "test/resources/imports/TestImportDefs.wsdl";
	    
	    try {
	        WSDLFactory factory = WSDLFactory.newInstance();
	        WSDLReader reader = factory.newWSDLReader();

	        reader.setFeature("javax.wsdl.verbose", true);
	        reader.setFeature("javax.wsdl.importDocuments", true);
	        reader.setFactoryImplName("com.ibm.wsdl.factory.WSDLFactoryImpl");

	        URL url = StringUtils.getURL(null, wsdlLoc);
	        InputStream inputStream = StringUtils.getContentAsInputStream(url);
	        InputSource inputSource = new InputSource(inputStream);
	        inputSource.setSystemId(url.toString());
	        
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        dbf.setNamespaceAware(true);
	        dbf.setValidating(false);
	        Document doc = null;
	        DocumentBuilder builder = dbf.newDocumentBuilder();
	        doc = builder.parse(inputSource);

	        inputStream.close();

	        Definition def = reader.readWSDL((String)null, doc);
	        
	    } catch (Exception e) {
        	throw new WSDLException(WSDLException.PARSER_ERROR,
        			"Problem parsing '" + wsdlLoc + "'.",
        			e);
	    }
  }
  
  private void printMap(Map map)
  {
    Iterator itr = map.keySet().iterator();
    while(itr.hasNext())
    {
      Object key = itr.next();
      System.out.print(key);
      System.out.println(" = ");
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
      else
      {
        System.out.println("("+data.getClass()+")"+data);
      }
      System.out.println("--------********--------");
    }
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
  
  class MyWSDLLocator implements WSDLLocator
  {
    private String wsdlURI;
  
    private InputSource baseInputSource;
    private Map importInputSources = new HashMap();
    private boolean closed = false;
    private String baseURI;
    private String lastImportURI = null;
  
    public MyWSDLLocator(String baseURI, String wsdlURI)
    {
      this.baseURI = baseURI;
      this.wsdlURI = wsdlURI;
    }
  
    public InputSource getBaseInputSource()
    {
      if (baseInputSource == null)
      {
        lastImportURI = baseURI+wsdlURI;
        baseInputSource = new MyInputSource(lastImportURI);
      }
      return baseInputSource;
    }
  
    public InputSource getImportInputSource(String parentLocation,
        String importLocation)
    {
      InputSource inSource = (InputSource) importInputSources.get(importLocation);
      if(inSource == null)
      {
        lastImportURI = baseURI+importLocation;
        inSource = new MyInputSource(lastImportURI);
        importInputSources.put(importLocation,inSource);
      }
      return inSource;
    }
  
    public String getBaseURI()
    {
      return baseURI+wsdlURI;
    }
  
    public String getLatestImportURI()
    {
      return lastImportURI;
    }
  
    public void close()
    {
      closed = true;
    }
  
    public boolean isClosed()
    {
      return closed;
    }
  }
  
  public class MyInputSource extends InputSource
  {
    private boolean runOnce = false;
    
    public MyInputSource(String uri)
    {
      super(uri);
    }
    
    public InputStream getByteStream()
    {
      if(runOnce)
      {
        fail("InputSource used already");
      }
      else
      {
        runOnce = true;
      }
      return super.getByteStream();
    }
  }
}

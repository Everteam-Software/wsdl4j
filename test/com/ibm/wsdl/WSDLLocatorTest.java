/*
 * (c) Copyright IBM Corp 2006 
 */
package com.ibm.wsdl;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

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

public class WSDLLocatorTest extends TestCase
{
  public WSDLLocatorTest()
  {
    super("WSDLPrinter");
  }

  public void testClose() throws Exception
  {
    String wsdlURI = "test/resources/MyExtendedWSDL.wsdl";

    MyWSDLLocator loc = new MyWSDLLocator(wsdlURI);

    WSDLFactory factory = WSDLFactory.newInstance();
    WSDLReader reader = factory.newWSDLReader();

    Definition def = reader.readWSDL(loc);

    assertTrue(loc.isClosed());
  }

  public void testIgnoreImportSource() throws Exception
  {
    String wsdlURI = "test/resources/MyExtendedWSDL.wsdl";

    URL contextURL = (wsdlURI != null) ? StringUtils.getURL(null, wsdlURI)
        : null;

    URL url = StringUtils.getURL(contextURL, wsdlURI);
    InputStream inputStream = new FileInputStream(wsdlURI); 
    InputSource inputSource = new InputSource(inputStream);
    inputSource.setSystemId(url.toString());
    Document doc = getDocument(inputSource, url.toString());

    inputStream.close();

    MyWSDLLocator loc = new MyWSDLLocator(wsdlURI);

    WSDLFactory factory = WSDLFactory.newInstance();
    WSDLReader reader = factory.newWSDLReader();

    Definition def1 = reader.readWSDL(wsdlURI);
    Definition def2 = reader.readWSDL(loc, doc.getDocumentElement());    

    assertTrue(loc.isClosed());
  }

  private static Document getDocument(InputSource inputSource, String desc)
      throws WSDLException
  {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    factory.setNamespaceAware(true);
    factory.setValidating(false);

    try
    {
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.parse(inputSource);

      return doc;
    }
    catch (RuntimeException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new WSDLException(WSDLException.PARSER_ERROR, "Problem parsing '"
          + desc + "'.", e);
    }
  }

  class MyWSDLLocator implements WSDLLocator
  {
    private String wsdlURI;

    private InputSource inputSource;

    private boolean closed = false;

    public MyWSDLLocator(String wsdlURI)
    {
      this.wsdlURI = wsdlURI;
    }

    public InputSource getBaseInputSource()
    {
      if (inputSource == null)
      {
        inputSource = new InputSource(wsdlURI);
      }
      return inputSource;
    }

    public InputSource getImportInputSource(String parentLocation,
        String importLocation)
    {
      return getBaseInputSource();
    }

    public String getBaseURI()
    {
      return wsdlURI;
    }

    public String getLatestImportURI()
    {
      return wsdlURI;
    }

    public void close()
    {
      if (inputSource != null)
      {
        inputSource = null;
      }
      closed = true;
    }

    public boolean isClosed()
    {
      return closed;
    }
  }
}

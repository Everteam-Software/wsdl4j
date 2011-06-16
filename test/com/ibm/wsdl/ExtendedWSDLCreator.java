/*
 * (c) Copyright IBM Corp 2006 
 */
package com.ibm.wsdl;

import java.io.File;
import java.io.FileOutputStream;

import javax.wsdl.Binding;
import javax.wsdl.BindingFault;
import javax.wsdl.BindingInput;
import javax.wsdl.BindingOperation;
import javax.wsdl.BindingOutput;
import javax.wsdl.Definition;
import javax.wsdl.Import;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.Types;
import javax.wsdl.WSDLElement;
import javax.wsdl.extensions.AttributeExtensible;
import javax.wsdl.extensions.ElementExtensible;
import javax.wsdl.extensions.UnknownExtensibilityElement;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.wsdl.xml.WSDLWriter;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class ExtendedWSDLCreator extends TestCase
{
  private Document doc;

  File file = new File("test/resources/test.wsdl");
  
  public ExtendedWSDLCreator()
  {
    super("WSDLPrinter");
    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder;
    try
    {
      docBuilder = docFactory.newDocumentBuilder();
      doc = docBuilder.newDocument();
    }
    catch (ParserConfigurationException e)
    {
      e.printStackTrace();
    }
  }
  
  public void test01CreateWSDL() throws Exception
  {
    WSDLFactory factory = WSDLFactory.newInstance();
    
    Definition def = factory.newDefinition();
    QName qName = new QName("TestDefinitionNS", "TestDefinitionName");
    def.setQName(qName);
    def.setTargetNamespace("TestDefinitionNS");
    addCommon(def, "TestDefinition",def);
    
    addImport(def);
    addTypes(def);
    addMessage(def);
    def.addPortType(createPortType(def));
    def.addBinding(createBinding(def));
    addService(def);
        
    WSDLWriter writer = factory.newWSDLWriter();
    file.createNewFile();
    FileOutputStream fos = new FileOutputStream(file);
    writer.writeWSDL(def, fos);
    writer.writeWSDL(def, System.out);
  }
  
  public void test02ParseWSDL() throws Exception
  {
    WSDLFactory factory = WSDLFactory.newInstance();
    
    WSDLReader reader = factory.newWSDLReader();
    Definition def = reader.readWSDL(file.toString());
  }
  
  void addService(Definition def)
  {
    Service service = def.createService();
    addCommon(service,"TestService",def);
    addPort(service,def);
    QName qName = new QName("TestServiceName");
    service.setQName(qName);
    def.addService(service);
  }
  
  void addPort(Service service, Definition def)
  {
    Port port = def.createPort();
    addCommon(port,"TestPort",def);
    port.setBinding(createBinding(def));
    port.setName("TestPortName");
    service.addPort(port);
  }
  
  void addMessage(Definition def)
  {
    Message message = def.createMessage();
    addCommon(message,"TestMessage",def);
    addPart(message, def);
    QName qName = new QName("TestMessageName");
    message.setQName(qName);
    message.setUndefined(false);
    def.addMessage(message);
  }
  
  void addPart(Message message, Definition def)
  {
    Part part = def.createPart();
    addCommon(part,"TestPart",def);
    QName eleName = new QName("TestPartElementName");
    part.setElementName(eleName);
    part.setName("TestPartName");
    QName typeName = new QName("TestPartTypeName");
    part.setTypeName(typeName);
    message.addPart(part);
  }
  
  void addTypes(Definition def)
  {
    Types types = def.createTypes();
    addCommon(types,"TestTypes",def);
    def.setTypes(types);
  }
  
  void addImport(Definition def)
  {
    Import importDef = def.createImport();
    addCommon(importDef,"TestImport", def);
    importDef.setLocationURI("imports/MyImportA.wsdl");
    importDef.setNamespaceURI("testImportNSURI");
    def.addImport(importDef);
  }
  
  Binding createBinding(Definition def)
  {
    Binding binding = def.createBinding();
    QName qName = new QName("TestBindingName");
    binding.setQName(qName);
    addCommon(binding, "TestBinding",def);
    addBindingOp(binding,def);
    binding.setPortType(createPortType(def));
    binding.setUndefined(false);
    return binding;
  }
  
  void addBindingOp(Binding binding, Definition def)
  {
    BindingOperation bindingOperation = def.createBindingOperation();
    bindingOperation.setName("TestBindingOperation");
    
    addCommon(bindingOperation, "TestBindingOperation",def);
    addBindingFault(bindingOperation, def);
    addBindingInput(bindingOperation, def);
    addBindingOutput(bindingOperation, def);
    bindingOperation.setOperation(createOperation(def));
        
    binding.addBindingOperation(bindingOperation);
  }
  
  Operation createOperation(Definition def)
  {
    Operation op = def.createOperation();
    op.setName("TestOperation");
    addCommon(op, "TestOperation",def);
    op.setUndefined(false);
    return op;
  }
  
  PortType createPortType(Definition def)
  {
    PortType portType = def.createPortType();
    QName qName = new QName("TestPortTypeName");
    portType.setQName(qName);
    addCommon(portType, "TestPortType",def);
    portType.setUndefined(false);
    return portType;
  }
  
  void addBindingInput(BindingOperation bindingOperation, Definition def)
  {
    BindingInput bindingInput = def.createBindingInput();
    bindingInput.setName("TestBindingInput");
    addCommon(bindingInput, "TestBindingInput",def);
    bindingOperation.setBindingInput(bindingInput);
  }
  
  void addBindingOutput(BindingOperation bindingOperation, Definition def)
  {
    BindingOutput bindingOutput = def.createBindingOutput();
    bindingOutput.setName("TestBindingOutput");
    addCommon(bindingOutput, "TestBindingOutput",def);
    bindingOperation.setBindingOutput(bindingOutput);
  }
  
  void addBindingFault(BindingOperation bindingOp, Definition def)
  {
    BindingFault bindingFault = def.createBindingFault();
    bindingFault.setName("TestBindingFault");
    addCommon(bindingFault, "TestBindingFault",def);
    bindingOp.addBindingFault(bindingFault);    
  }
  
  void addCommon(WSDLElement element, String prefix, Definition def)
  {
    setDocElement(element, prefix);
    addExtElement(element, prefix, def);
    setExtAttr(element, prefix, def);
  }
  
  void setDocElement(WSDLElement element, String prefix)
  {
    Element docEle = doc.createElementNS(Constants.NS_URI_WSDL, "documentation");
    docEle.setPrefix("wsdl");
    Text textNode = doc.createTextNode(prefix+" Documentation");
    docEle.appendChild(textNode);
    element.setDocumentationElement(docEle);
  }
  
  void addExtElement(ElementExtensible element, String prefix, Definition def)
  {
    Element ele = doc.createElementNS(prefix,"ExtElement");
    ele.setPrefix(prefix);
    Text textNode = doc.createTextNode(prefix+" Extensibility Element");
    ele.appendChild(textNode);
    
    def.addNamespace(prefix,prefix);
    
    UnknownExtensibilityElement extEle = new UnknownExtensibilityElement();
    QName qName = new QName("ExtEleNS", "EleName");
    extEle.setElementType(qName);
    extEle.setRequired(Boolean.FALSE);
    extEle.setElement(ele);
    element.addExtensibilityElement(extEle);
  }
  
  void setExtAttr(AttributeExtensible element, String prefix, Definition def)
  {
    QName qName = new QName("ExtAttrNS", "AttrName");
    def.addNamespace(prefix,"ExtAttrNS");
    element.setExtensionAttribute(qName,prefix+"Value");
  }
}

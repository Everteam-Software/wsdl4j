package com.ibm.wsdl.xml;

import java.util.HashMap;
import java.util.Map;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensionRegistry;
import javax.wsdl.xml.WSDLLocator;
import javax.wsdl.xml.WSDLReader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

public class WSDLCachedReader implements WSDLReader {
	private WSDLReader proxy;

	public WSDLCachedReader(WSDLReader proxy) {
		super();
		this.proxy = proxy;
	}

	public ExtensionRegistry getExtensionRegistry() {
		return proxy.getExtensionRegistry();
	}

	public String getFactoryImplName() {
		return proxy.getFactoryImplName();
	}

	public boolean getFeature(String name) throws IllegalArgumentException {
		return proxy.getFeature(name);
	}
	
	public Definition readWSDL(String documentBaseURI, Document wsdlDocument)
			throws WSDLException {
		return tryCachedWSDL(proxy.readWSDL(documentBaseURI, wsdlDocument));
	}

	public Definition readWSDL(String documentBaseURI,
			Element definitionsElement) throws WSDLException {
		return tryCachedWSDL(proxy.readWSDL(documentBaseURI, definitionsElement));
	}

	public Definition readWSDL(String documentBaseURI, InputSource inputSource)
			throws WSDLException {
		return tryCachedWSDL(proxy.readWSDL(documentBaseURI, inputSource));
	}

	public Definition readWSDL(String contextURI, String wsdlURI)
			throws WSDLException {
		return tryCachedWSDL(proxy.readWSDL(contextURI, wsdlURI));
	}

	public Definition readWSDL(String wsdlURI) throws WSDLException {
		return tryCachedWSDL(proxy.readWSDL(wsdlURI));
	}

	public Definition readWSDL(WSDLLocator locator, Element definitionsElement)
			throws WSDLException {
		return tryCachedWSDL(proxy.readWSDL(locator, definitionsElement));
	}

	public Definition readWSDL(WSDLLocator locator) throws WSDLException {
		return tryCachedWSDL(proxy.readWSDL(locator));
	}

	public void setExtensionRegistry(ExtensionRegistry extReg) {
		proxy.setExtensionRegistry(extReg);
	}

	public void setFactoryImplName(String factoryImplName)
			throws UnsupportedOperationException {
		proxy.setFactoryImplName(factoryImplName);
	}

	public void setFeature(String name, boolean value)
			throws IllegalArgumentException {
		proxy.setFeature(name, value);
	}

	
	
	public static Map<String, Definition> cachedWSDLs = new HashMap<String, Definition>();
	
	public Definition tryCachedWSDL(Definition def) {
		synchronized (cachedWSDLs) {
			String uri = def.getDocumentBaseURI();
			if (cachedWSDLs.containsKey(uri)) {
				return cachedWSDLs.get(uri);
			} else {
				cachedWSDLs.put(uri, def);
				return def;
			}
		}
	}
}

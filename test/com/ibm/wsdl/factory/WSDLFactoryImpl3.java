/*
 * (c) Copyright IBM Corp 2006 
 */
package com.ibm.wsdl.factory;

public class WSDLFactoryImpl3 extends WSDLFactoryImpl
{
  public static int ctorCount = 0;
  
  public WSDLFactoryImpl3()
  {
    super();
    ctorCount++;
  }
}
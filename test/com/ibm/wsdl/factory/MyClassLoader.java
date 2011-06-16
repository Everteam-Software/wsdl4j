/*
 * (c) Copyright IBM Corp 2006 
 */
package com.ibm.wsdl.factory;

public class MyClassLoader extends ClassLoader
{
  public String last = null;

  protected synchronized Class loadClass(String className, boolean resolveClass) throws ClassNotFoundException
  {
    last  = className;
    return super.loadClass(className, resolveClass);
  }
}

/*
 * (c) Copyright IBM Corp 2006 
 */
package javax.xml.namespace;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URL;

import junit.framework.TestCase;

/**
 * Test serializing and deserializing the QName class. Ensure that
 * old QName objects without a 'prefix' field can be deserialized
 * by the current QName class which does have a prefix.
 * Based on a subset of the Apache Woden QNameTest class.
 * 
 * @author John Kaputin
 */
public class QNameSerializationTest extends TestCase {

    private static final String emptyString = "";
    private QName qname;
    
    
    //public static Test suite()
    //{
    //    return new TestSuite(QNameSerializationTest.class);
    //}
    
    public QNameSerializationTest()
    {
      super("QNameSerializationTest");
    }

    public void testGoodSerializeThenDeserialize() throws IOException,
                                                          ClassNotFoundException
    {
        //Serialize a QName then deserialize it to test it has been initialized 
        //correctly by the readObject() method.
        
        qname = new QName("myNamespace", "myLocalPart", "myPrefix");
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(qname);
        oos.close();

        QName qnameCopy = null;
        
        ObjectInputStream ois =
            new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        Object o = ois.readObject();
        qnameCopy = (QName)o;
        ois.close();

        assertTrue("QName was not initialized correctly after deserialization.",
                (qnameCopy.getNamespaceURI().equals("myNamespace") &&
                 qnameCopy.getLocalPart().equals("myLocalPart") &&
                 qnameCopy.getPrefix().equals("myPrefix")));
        
    }
    
    public void testGoodDeserializeQNameWithoutPrefix() throws IOException, 
                                                               ClassNotFoundException, Exception
    {
        /* 
         * Test that 'prefix' is initialized correctly when deserializing a v1.0 QName.
         * The v1.0 QName does not have a 'prefix' field, so when it's deserialized using
         * the v1.1 QName, its 'prefix' field should be set to the empty string "", not 
         * just initialized as null.
         * 
         * The input to this testcase is a file containing a v1.0 QName serialized 
         * from the WSDL4J version of QName.
         */
        
        URL url = getClass().getClassLoader().getResource("javax/xml/namespace/serialized_QName_no_prefix");

        File f = new File(new URI(url.getFile()).getPath());
        
        ObjectInputStream ois =
            new ObjectInputStream(new FileInputStream(f));
        
        Object o = ois.readObject();
        qname = (QName)o;
        
        assertTrue("Expected a null 'prefix' to be initialized to the empty string \"\".",
                qname.getPrefix() != null && qname.getPrefix().equals(emptyString));
            
    }
    

}

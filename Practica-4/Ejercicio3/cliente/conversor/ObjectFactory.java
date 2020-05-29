
package cliente.conversor;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cliente.conversor package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ExcepcionConversion_QNAME = new QName("http://webService/", "ExcepcionConversion");
    private final static QName _ParseIntegerResponse_QNAME = new QName("http://webService/", "parseIntegerResponse");
    private final static QName _ParseInteger_QNAME = new QName("http://webService/", "parseInteger");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cliente.conversor
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ParseIntegerResponse }
     * 
     */
    public ParseIntegerResponse createParseIntegerResponse() {
        return new ParseIntegerResponse();
    }

    /**
     * Create an instance of {@link ParseInteger }
     * 
     */
    public ParseInteger createParseInteger() {
        return new ParseInteger();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webService/", name = "ExcepcionConversion")
    public JAXBElement<String> createExcepcionConversion(String value) {
        return new JAXBElement<String>(_ExcepcionConversion_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParseIntegerResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webService/", name = "parseIntegerResponse")
    public JAXBElement<ParseIntegerResponse> createParseIntegerResponse(ParseIntegerResponse value) {
        return new JAXBElement<ParseIntegerResponse>(_ParseIntegerResponse_QNAME, ParseIntegerResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParseInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webService/", name = "parseInteger")
    public JAXBElement<ParseInteger> createParseInteger(ParseInteger value) {
        return new JAXBElement<ParseInteger>(_ParseInteger_QNAME, ParseInteger.class, null, value);
    }

}

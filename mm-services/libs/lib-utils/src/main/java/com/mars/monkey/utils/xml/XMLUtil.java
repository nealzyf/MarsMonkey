package com.mars.monkey.utils.xml;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Abstract class with static XML utility methods.
 */
public abstract class XMLUtil {
    private static final String CODE = "ISO-8859-1";

    private static final String YES = "yes";

    public static final int PRETTY_PRINT = 1;

    public static final int OMIT_HEADER = 1 << 1;

    /**
     * commons logger for this class.
     */
    private static final Logger logger = LoggerFactory.getLogger(XMLUtil.class);

    /**
     * The XML transformer attached to each thread it is used from.
     */
    private static ThreadLocal<Transformer> transformer = new ThreadLocal<Transformer>();

    /**
     * The XML document builder attached to each thread it is used from.
     */
    private static ThreadLocal<DocumentBuilder> documentBuilder = new ThreadLocal<DocumentBuilder>();

    /**
     * Type list maps to node types in org.w3c.Node
     */
    private static Map<Short, String> typeList = new HashMap<Short, String>();

    /*
     * Type map for debug information. Taken from the class org.w3c.Node
     */
    static {
        typeList.put((short) 1, "ELEMENT_NODE");
        typeList.put((short) 2, "ATTRIBUTE_NODE");
        typeList.put((short) 3, "TEXT_NODE");
        typeList.put((short) 4, "CDATA_SECTION_NODE");
        typeList.put((short) 5, "ENTITY_REFERENCE_NODE");
        typeList.put((short) 6, "ENTITY_NODE");
        typeList.put((short) 7, "PROCESSING_INSTRUCTION_NODE");
        typeList.put((short) 8, "COMMENT_NODE");
        typeList.put((short) 9, "DOCUMENT_NODE");
        typeList.put((short) 10, "DOCUMENT_TYPE_NODE");
        typeList.put((short) 11, "DOCUMENT_FRAGMENT_NODE");
        typeList.put((short) 12, "NOTATION_NODE");
    }

    /**
     * Performs a check that the provided Nodeobject is not null.
     *
     * @param node
     *            The node to check
     * @throws IllegalArgumentException
     *             In case the node is null
     */

    /**
     * private constructor.
     */
    private XMLUtil() {
        throw new AssertionError();
    }

    private static void assertNotNull(Node node) {
        if (node == null) {
            throw new IllegalArgumentException("The input node cannot be null");
        }
    }

    /**
     * Get an instance of the DocumentBuilder. This method guarantees that each thread will have it's own unique instance.
     *
     * @return
     */
    private static DocumentBuilder getDocumentBuilder() {
        /*
         * TODO: 28 okt 2009:epknerg - Do a similar pool construction as in the XSLTTransformator. That should save some memory instead of as there will be
         * fewer factory instances compared to the current solution using ThreadLocal
         */

        DocumentBuilder documentBuilderInstance = documentBuilder.get();
        if (documentBuilderInstance == null) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setNamespaceAware(true);
                factory.setIgnoringElementContentWhitespace(true);
                documentBuilderInstance = factory.newDocumentBuilder();
                documentBuilderInstance.setEntityResolver(new DefaultEntityResolverImpl());
                documentBuilderInstance.setErrorHandler(new DefaultErrorHandlerImpl());
                documentBuilder.set(documentBuilderInstance);
            } catch (Exception ex) {
                logger.error("Could not configure XML document builder", ex);
            }
        }
        return documentBuilderInstance;
    }

    protected static Transformer getTransformer(int options) throws TransformerConfigurationException {
        Transformer instance = transformer.get();
        if (instance == null) {
            try {
                instance = TransformerFactory.newInstance().newTransformer();
            } catch (TransformerConfigurationException ex) {
                logger.error("Could not configure XML transformer", ex);
                throw ex;
            }
        }

        instance.setOutputProperties(new Properties());
        instance.setOutputProperty(OutputKeys.ENCODING, CODE);
        instance.setOutputProperty(OutputKeys.STANDALONE, YES);
        instance.setOutputProperty(OutputKeys.INDENT, YES);

        if ((options & PRETTY_PRINT) == PRETTY_PRINT) {
            instance.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        }
        if ((options & OMIT_HEADER) == OMIT_HEADER) {
            instance.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, YES);
        }

        return instance;
    }

    /**
     * Add an attribute to an Element, without namespace
     *
     * @param element
     *         element to add the attribute to
     * @param qualifieName
     *         the qualified name
     * @param nodeValue
     *         the value of the attribute
     * @return
     */
    public static void addAttribute(Element element, String qualifiedName, String nodeValue) {
        Document document = element.getOwnerDocument();
        Attr attribute = document.createAttribute(qualifiedName);
        attribute.setNodeValue(nodeValue);
        element.setAttributeNode(attribute);
    }

    /**
     * Add an attribute to an Element by using attribute namespace
     *
     * @param element
     *         element to add the attribute to
     * @param namespaceURI
     *         the namespace
     * @param qualifiedName
     *         the qualified name
     * @param nodeValue
     *         the value of the attribute
     * @return
     */
    public static void addAttributeNS(Element element, String namespaceURI, String qualifiedName, String nodeValue) {
        Document document = element.getOwnerDocument();
        Attr attribute = document.createAttributeNS(namespaceURI, qualifiedName);
        attribute.setNodeValue(nodeValue);
        element.setAttributeNode(attribute);
    }

    /**
     * Add a comment to a Node in the XML Document
     *
     * @param text
     *         the text in the comment
     * @param node
     *         the node where the comment must be added to (must be Document or have an ownerdocument)
     */
    public static void addComment(Node node, String text) {
        Comment comment;
        if (node instanceof Document) {
            comment = ((Document) node).createComment(text);
        } else {
            comment = node.getOwnerDocument().createComment(text);
        }
        node.appendChild(comment);
    }

    /**
     * Append a new child element to the provided element.
     *
     * @param parent
     *         The parent element
     * @param namespaceURI
     *         The ns-URI that the new element shall belong to
     * @param qualifiedName
     *         The name of the new element
     * @return
     */
    public static Element appendChildElementNS(Element parent, String namespaceURI, String qualifiedName) {
        Element newElement = parent.getOwnerDocument().createElementNS(namespaceURI, qualifiedName);
        parent.appendChild(newElement);
        return newElement;
    }

    /**
     * Copies all the contents of the provided document to a new document. <br>
     * All elements and attributes in the new document will belong to the provided namespaceURI regardless of what URI's that were defined in the original
     * document.
     *
     * @param document
     * @param namespaceURI
     * @return
     */
    public static Document copyDocument(Document document, String namespaceURI) {
        Document newDocument = getDocument();
        String name = document.getDocumentElement().getNodeName();
        Element newElement = newDocument.createElementNS(namespaceURI, name);
        newDocument.appendChild(newElement);

        copyElement(document.getDocumentElement(), newElement, namespaceURI);

        return newDocument;
    }

    /**
     * Recursively copy all child elements from the original to the copy. <br>
     * Any attributes and value of the element will also be copied
     *
     * @param originalElement
     * @param copyElement
     * @param namespaceURI
     */
    public static void copyElement(Element originalElement, Element copyElement, String namespaceURI) {
        Collection<Element> children = getChildElements(originalElement);
        String value = getElementValue(originalElement);
        if (value != null && value.trim().length() > 0) {
            setElementValue(copyElement, value);
        }

        for (Element child : children) {
            Element newElement = appendChildElementNS(copyElement, namespaceURI, child.getNodeName());
            copyElement(child, newElement, namespaceURI);
        }

        /*
         * Copy all the attributes
         */
        NamedNodeMap attributeList = originalElement.getAttributes();
        Node attribute;
        for (int i = 0; i < attributeList.getLength(); i++) {
            attribute = attributeList.item(i);
            // don't copy any of the xmlns declarations
            if (!attribute.getNodeName().startsWith("xmlns")) {
                addAttributeNS(copyElement, namespaceURI, attribute.getNodeName(), attribute.getNodeValue());
            }
        }

    }

    /**
     * Get all child elements that are direct children the provided element.
     *
     * @param parent
     *         The parent element
     * @return The list with child elements, empty list if no children
     */
    public static Collection<Element> getChildElements(Element parent) {
        assertNotNull(parent);
        Collection<Element> childList = new ArrayList<Element>();

        NodeList children = parent.getChildNodes();
        Node node;
        for (int i = 0; i < children.getLength(); i++) {
            node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                childList.add((Element) node);
            }
        }

        return childList;
    }

    /**
     * Get all child elements with the provided local name that are direct children the provided element.
     *
     * @param parent
     *         The parent element
     * @param name
     *         The local name of the child elements to find
     * @return The list with child elements, empty list if no matching children
     */
    public static Collection<Element> getChildElementsByLocalName(Element parent, String name) {
        assertNotNull(parent);
        Collection<Element> childList = new ArrayList<Element>();

        NodeList children = parent.getChildNodes();
        Node node;
        for (int i = 0; i < children.getLength(); i++) {
            node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE && node.getLocalName().equals(name)) {
                childList.add((Element) node);
            }
        }

        return childList;
    }

    /**
     * Get all child elements with the provided name that are direct children the provided element.
     *
     * @param parent
     *         The parent element
     * @param name
     *         The name of the child elements to find
     * @return The list with child elements, empty list if no matching children
     */
    public static Collection<Element> getChildElementsByName(Element parent, String name) {
        assertNotNull(parent);
        Collection<Element> childList = new ArrayList<Element>();

        NodeList children = parent.getChildNodes();
        Node node;
        for (int i = 0; i < children.getLength(); i++) {
            node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals(name)) {
                childList.add((Element) node);
            }
        }

        return childList;
    }

    /**
     * Creates a new empty XML document. <br>
     * This method is thread safe since it uses a static DocumentBuilder initiated as ThreadLocal.
     *
     * @return The XML document
     */
    public static Document getDocument() {
        return getDocumentBuilder().newDocument();
    }

    /**
     * Get an XML document from the provided InputStream. <br>
     * This method is thread safe since it uses a static DocumentBuilder initiated as ThreadLocal.
     *
     * @param inputStream
     *         The stream with XML content
     * @return The XML document
     * @throws IOException
     * @throws SAXException
     */
    public static Document getDocument(InputStream inputStream) throws IOException, SAXException {
        DocumentBuilder docBuilder = getDocumentBuilder();
        return docBuilder.parse(inputStream);
    }

    /**
     * Get an XML document from the provided Node. <br>
     * This method is thread safe since it uses a static DocumentBuilder initiated as ThreadLocal.
     *
     * @param node
     *         The XML Node
     * @return The XML document
     */
    public static Document getDocument(Node node) {
        Document document = getDocumentBuilder().newDocument();
        document.appendChild(document.adoptNode(node));
        return document;
    }

    /**
     * Get an XML document from the provided XML formatted string. <br>
     * This method is thread safe since it uses a static DocumentBuilder initiated as ThreadLocal.
     *
     * @param xmlString
     *         The XML formatted string
     * @return The XML document
     * @throws IOException
     * @throws SAXException
     */
    public static Document getDocument(String xmlString) throws IOException, SAXException {
        DocumentBuilder docBuilder = getDocumentBuilder();
        return docBuilder.parse(new InputSource(new StringReader(xmlString)));
    }

    /**
     * Get the node value of the provided element. The method will list and concat the values of all children of the type <code>Node.TEXT_NODE</code> .
     *
     * @param element
     * @return
     */
    public static String getElementValue(Element element) {
        assertNotNull(element);
        StringBuilder sb = new StringBuilder();
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.TEXT_NODE) {
                sb.append(children.item(i).getNodeValue());
            }
        }

        return sb.toString();
    }

    public static Node getNodeByXPath(Element element, String xpath) throws XPathExpressionException {
        return (Node) XPathFactory.newInstance().newXPath().compile(xpath).evaluate(element, XPathConstants.NODE);
    }

    /**
     * Get the first found child element with the provided local name that is a direct child the provided element.
     *
     * @param parent
     *         The parent element
     * @param localName
     *         The name of the child element to find
     * @return The first found child element, null if no matching children
     */
    public static Element getFirstChildElementByLocalName(Element parent, String localName) {
        assertNotNull(parent);
        NodeList children = parent.getChildNodes();
        Node node;
        for (int i = 0; i < children.getLength(); i++) {
            node = children.item(i);
            boolean nodeNameEqual = node.getNodeName() != null && node.getNodeName().equals(localName);
            boolean localNameEqual = node.getLocalName() != null && node.getLocalName().equals(localName);
            if (node.getNodeType() == Node.ELEMENT_NODE && (nodeNameEqual || localNameEqual)) {
                return (Element) node;
            }
        }

        return null;
    }

    /**
     * Get the first found child element with the provided name that is a direct child the provided element.
     *
     * @param parent
     *         The parent element
     * @param name
     *         The name of the child element to find
     * @return The first found child element, null if no matching children
     */
    public static Element getFirstChildElementByName(Element parent, String name) {
        assertNotNull(parent);
        NodeList children = parent.getChildNodes();
        Node node;
        for (int i = 0; i < children.getLength(); i++) {
            node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals(name)) {
                return (Element) node;
            }
        }

        return null;
    }

    /**
     * Get the first element of a specific XML node
     */
    public static String getFirstElementValue(Node node, String localName) {
        NodeList list;
        String result = null;
        list = node.getChildNodes();

        Node currentNode;
        Node valueNode;
        for (int i = 0; i < list.getLength(); i++) {
            currentNode = list.item(i);
            if (currentNode.getNodeName().equals(localName)) {
                valueNode = currentNode.getFirstChild();
                if (valueNode != null) {
                    result = valueNode.getNodeValue();
                }
                break;
            }
        }
        return result;
    }

    /**
     * Get the value of first element in the document specificied with a specific tag name. <br>
     * Does not always work for documents that use namespaces
     * <p>
     * The method will search through the whole hierarchy of the document
     *
     * @param parent
     *         to start searching
     * @param name
     *         name of the tag
     * @return
     */
    public static String getFirstValueByName(Element parent, String name) {
        NodeList list = parent.getElementsByTagName(name);
        String result;
        if (list.getLength() == 0) {
            result = null;
        } else {
            Node valueNode = list.item(0).getFirstChild();
            if (valueNode != null) {
                result = valueNode.getNodeValue();
            } else {
                result = null;
            }
        }
        return result;
    }

    /**
     * Get the value of first element in the document specificied with a specific tag namespace and localName.
     * <p>
     * The method will search through the whole hierarchy of the document
     *
     * @param namespaceURI
     *         The namespace URI of the elements to match on. The special value "*" matches all namespaces.
     * @param localName
     *         The local name of the elements to match on. The special value "*" matches all local names.
     * @return
     * @parent parent the element to start to search in
     */
    public static String getFirstValueByNameNS(Element parent, String namespaceURI, String localName) {
        NodeList list = parent.getElementsByTagNameNS(namespaceURI, localName);
        String result;
        if (list.getLength() == 0) {
            result = null;
        } else {
            Node valueNode = list.item(0).getFirstChild();
            if (valueNode != null) {
                result = valueNode.getNodeValue();
            } else {
                result = null;
            }
        }
        return result;
    }

    /**
     * Get a new instance of Document with only a root element, given a specific QName. <br>
     * The root element of the document is part of the same namespaceURI as the Document itself <br>
     * This method is thread safe since it uses a static DocumentBuilder initiated as ThreadLocal.
     *
     * @param qname
     *         the namespace and qualified name. Namespace will be used by Document and element.
     * @return
     */
    public static Document getSingleElementDocument(QName qname) {
        Document document = getDocument();
        document.setDocumentURI(qname.getNamespaceURI());
        Element root = document.createElementNS(qname.getNamespaceURI(), qname.getLocalPart());
        document.appendChild(root);
        return document;
    }

    /**
     * Checks if a node value is blank (contains only white spaces).
     *
     * @param value
     *         The value to check
     * @return false if the value is null or a non white space character is found
     */
    public static boolean isBlank(String value) {
        if (value == null) {
            return false;
        }

        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (!Character.isWhitespace(c)) {
                return false;
            }
        }

        return true;
    }

    /**
     * check if the element has neither elements nor attributes, except the namespace attribute
     *
     * @param element
     *         The element to check
     * @return true if it is empty
     */
    public static boolean isEmptyElement(Element element) {
        NamedNodeMap attributes = element.getAttributes();
        boolean hasRealAttributes = false;
        if (attributes != null) {
            for (int i = 0; i < attributes.getLength(); ++i) {
                Node node = attributes.item(i);
                if (!node.getNodeName().trim().startsWith("xmlns")) {
                    hasRealAttributes = true;
                    break;
                }
            }
        }

        return "".equals(element.getTextContent().trim()) && XMLUtil.getChildElements(element).isEmpty() && !hasRealAttributes;
    }

    /**
     * Recursively removes all empty child text nodes starting from the provided node. <br>
     * The method will also trim the non empty text nodes, i.e. any white spaces in the beginning and the end of the text node will be removed.
     *
     * @param node
     *         The node to normalize
     */
    public static void normalizeDocument(Node node) {
        assertNotNull(node);
        if (!node.hasChildNodes()) {
            return;
        }

        NodeList nodeList = node.getChildNodes();
        String nodeValue;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node n = nodeList.item(i);

            if (n.getNodeType() == Node.TEXT_NODE) {
                nodeValue = n.getNodeValue().trim();

                // It the node is empty remove it
                if (XMLUtil.isBlank(nodeValue)) {
                    node.removeChild(n);
                    i--;
                }
                // Overwrite the node with the trimmed text
                else {
                    n.setNodeValue(nodeValue);
                }
            } else {
                normalizeDocument(n);
            }
        }
    }

    /**
     * remove all sub elements if the element does not have any attribute or content
     *
     * @param element
     */
    public static void removeEmptyElements(Element element) {
        assertNotNull(element);

        if (!element.hasChildNodes()) {
            return;
        }

        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node n = nodeList.item(i);

            if (n.getNodeType() == Node.ELEMENT_NODE) {
                Element sub = (Element) n;

                removeEmptyElements(sub);
                if (isEmptyElement(sub)) {
                    element.removeChild(sub);
                    i--;
                }
            } else if (n.getNodeType() == Node.TEXT_NODE) {
                String nodeValue = n.getNodeValue().trim();

                // It the node is empty remove it
                if (XMLUtil.isBlank(nodeValue)) {
                    element.removeChild(n);
                    i--;
                }
                // Overwrite the node with the trimmed text
                else {
                    n.setNodeValue(nodeValue);
                }
            } else {
                element.removeChild(n);
                i--;
            }
        }

    }

    /**
     * Appends a new node of the type <code>Node.TEXT_NODE</code> with the provided value to the provided element.
     *
     * @param element
     * @param value
     */
    public static void setElementValue(Element element, String value) {
        element.appendChild(element.getOwnerDocument().createTextNode(value));
    }

    /**
     * Writes the document to a supplied outputstream. <br>
     * As well as writing the output to a any type of OutputStream (e.g. sockets, files), this method is also suitable to use for debugging. The output could be
     * either of the <code>System.out</code> or the <code>System.err</code> streams. <br>
     * This method is thread safe since it uses a static Transformer initiated as ThreadLocal.
     *
     * @param document
     *         The document to write
     * @param ostream
     *         The target stream
     * @param closeStream
     *         If the outputstream is to be closed after write
     * @throws IOException
     * @throws TransformerException
     */
    public static void toStream(Document document, OutputStream ostream, boolean closeStream) throws IOException, TransformerException {
        toStream(document, ostream, closeStream, false);
    }

    /**
     * Writes the document to a supplied outputstream. <br>
     * As well as writing the output to a any type of OutputStream (e.g. sockets, files), this method is also suitable to use for debugging. The output could be
     * either of the <code>System.out</code> or the <code>System.err</code> streams. <br>
     * This method is thread safe since it uses a static Transformer initiated as ThreadLocal.
     *
     * @param document
     *         The document to write
     * @param ostream
     *         The target stream
     * @param closeStream
     *         If the outputstream is to be closed after write
     * @param prettyPrint
     *         If the output shall be formatted
     * @throws IOException
     * @throws TransformerException
     */
    public static void toStream(Document document, OutputStream ostream, boolean closeStream, boolean prettyPrint) throws IOException, TransformerException {
        assertNotNull(document);
        if (ostream == null) {
            throw new IllegalArgumentException("The OutputStream cannot be null");
        }

        Transformer instance = getTransformer(prettyPrint ? PRETTY_PRINT : 0);
        instance.transform(new DOMSource(document), new StreamResult(new BufferedOutputStream(ostream)));
        ostream.flush();

        if (closeStream) {
            ostream.close();
        }
    }

    /**
     * Write the XML document to a string. This method is thread safe since it uses a static Transformer initiated as ThreadLocal.
     *
     * @param document
     *         The document to write
     * @return The string, null if failed
     */
    public static String toString(Node document) {
        return toString(document, false);
    }

    /**
     * Write the XML document to a string. This method is thread safe since it uses a static Transformer initiated as ThreadLocal.
     *
     * @param document
     *         The document to write
     * @param prettyPrint
     *         Pretty print (format) the output
     * @return The string, null if failed
     * @since version 1.0, increment 3
     */
    public static String toString(Node document, boolean prettyPrint) {
        assertNotNull(document);
        try {
            StringWriter sw = new StringWriter();
            toWriter(document, sw, prettyPrint);
            return sw.toString().trim();
        } catch (Exception ex) {
            logger.warn("Failed to write XML to string", ex);
            return null;
        }
    }

    public static String toString(Node document, int outputOptions) {
        assertNotNull(document);
        try {
            StringWriter sw = new StringWriter();
            toWriter(document, sw, outputOptions);
            return sw.toString().trim();
        } catch (Exception ex) {
            logger.warn("Failed to write XML to string", ex);
            return null;
        }
    }

    /**
     * Writes the document to a supplied writer. This method is thread safe since it uses a static Transformer initiated as ThreadLocal.
     *
     * @param document
     *         The document to write
     * @param writer
     *         The target writer
     * @throws IOException
     * @throws TransformerException
     */
    public static void toWriter(Document document, Writer writer) throws IOException, TransformerException {
        toWriter(document, writer, false);
    }

    /**
     * Writes the document to a supplied writer. This method is thread safe since it uses a static Transformer initiated as ThreadLocal.
     *
     * @param node
     *         The document to write
     * @param writer
     *         The target writer
     * @param prettyPrint
     *         Pretty print (format) the output
     * @throws IOException
     * @throws TransformerException
     * @since version 1.0, increment 3
     */
    public static void toWriter(Node node, Writer writer, boolean prettyPrint) throws IOException, TransformerException {
        toWriter(node, writer, prettyPrint ? PRETTY_PRINT : 0);
    }

    /**
     * Writes the document to a supplied writer. This method is thread safe since it uses a static Transformer initiated as ThreadLocal.
     *
     * @param document
     *         The document to write
     * @param writer
     *         The target writer
     * @param outputOptions
     *         Pretty print (format) the output
     * @throws IOException
     * @throws TransformerException
     * @since version 1.0, increment 3
     */
    private static void toWriter(Node document, Writer writer, int outputOptions) throws TransformerException, IOException {
        assertNotNull(document);
        if (writer == null) {
            throw new IllegalArgumentException("The Writer cannot be null");
        }

        Transformer instance = getTransformer(outputOptions);
        instance.transform(new DOMSource(document), new StreamResult(new BufferedWriter(writer)));

        writer.flush();
    }

    /**
     * Gets debug information to make clear what a Document Object exactly contains. <br>
     * Converts a Document to a String for pretty printing, for debug information such as system logs, necessary for troubleshooting
     * <p>
     * For each node the localname, namespace, type and textcontents is displayed
     *
     * @param document
     * @return
     */
    public static String toDebugString(Document document) {
        StringBuilder builder = new StringBuilder();
        prettyPrintNodeForDebug(builder, document.getDocumentElement(), 0);
        return builder.toString();
    }

    /**
     * Gets debug information to make clear what a Node or Element Object exactly contains. <br>
     * Converts the Node or Element to String for pretty printing, necessary for trouble shooting
     * <p>
     * For each node the localname, namespace, type and textcontents is displayed
     *
     * @param node
     * @return
     */
    public static String toDebugString(Node node) {
        StringBuilder builder = new StringBuilder();
        prettyPrintNodeForDebug(builder, node, 0);
        return builder.toString();
    }

    /**
     * Pretty print one Node in a Document and all its children
     *
     * @param builder
     *         builder to add to
     * @param node
     *         the Node to print
     * @param level
     *         the level for pretty printing
     */
    private static void prettyPrintNodeForDebug(StringBuilder builder, Node node, int level) {
        if (node == null) {
            return;
        }
        for (int i = 0; i < level * 2; i++) {
            builder.append(" ");
        }
        String namespaceURI = node.getNamespaceURI();
        String localName = node.getLocalName();
        String type = typeList.get(node.getNodeType());
        String textContents = node.getTextContent();

        builder
                .append("localName: '")
                .append(localName)
                .append("', ns: '")
                .append(namespaceURI)
                .append("', type: '")
                .append(type)
                .append("', textContents: '")
                .append(filterForPrinting(textContents))
                .append("'");

        NamedNodeMap attributeMap = node.getAttributes();
        if (attributeMap != null) {
            builder.append(" attributes: [");
            for (int index = 0; index < attributeMap.getLength(); index++) {
                Node attribute = attributeMap.item(index);
                if (index > 0) {
                    builder.append(", ");
                }
                builder
                        .append("[name: '")
                        .append(attribute.getLocalName())
                        .append("', namespace: '")
                        .append(attribute.getNamespaceURI())
                        .append("', value: '")
                        .append(attribute.getNodeValue())
                        .append("']");

            }
            builder.append("]");
        }

        builder.append(System.getProperty("line.separator"));

        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            prettyPrintNodeForDebug(builder, child, level + 1);
        }
    }

    /**
     * Remove all formatting and newlines from a line, and trim the String
     *
     * @param line
     * @return
     */
    private static String filterForPrinting(String line) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c > 31) {
                builder.append(c);
            }
        }
        return builder.toString().trim();
    }

}

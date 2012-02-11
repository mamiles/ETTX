package com.cisco.ettx.admin.common.util;

import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.*; 

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException; 
/*
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.apache.xml.serialize.LineSeparator;
*/
import java.io.FileWriter;
import java.io.StringBufferInputStream;
import java.util.Vector;

import org.apache.log4j.Logger;

public class XMLUtil
{
	private static Logger logger = Logger.getLogger(XMLUtil.class);
    public static synchronized Document createDocument() 
	throws Exception
    {
	    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	    Document document = docBuilder.newDocument();
		return document;
	}

    public static synchronized Document loadXmlFile(String xmlFile)
	throws Exception
    {
	    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	    Document document = docBuilder.parse(new File(xmlFile));
	    document.getDocumentElement().normalize();
		return document;
    }

    public static synchronized Document loadXml(String xmlDoc)
	throws Exception
    {
	    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	    StringBufferInputStream stream = new StringBufferInputStream(xmlDoc);
	    Document document = docBuilder.parse(stream);
	    document.getDocumentElement().normalize();
		return document;
    }

	public static synchronized void writeXmlFile(String xmlFile, 
			Document document) throws Exception {
	    	// transform the document to the file
	    	TransformerFactory xformFactory = TransformerFactory.newInstance();
	    	Transformer idTransformer = xformFactory.newTransformer();
		idTransformer.setOutputProperty("indent","yes");
	    	Source input = new DOMSource(document);
	    	Result output = new StreamResult(new File(xmlFile));
	    	idTransformer.transform(input, output);
/*
		OutputFormat format = new OutputFormat();
		format.setLineSeparator(LineSeparator.Unix);
		format.setIndenting(true);
		format.setLineWidth(0);
		format.setPreserveSpace(true);
		XMLSerializer ser = new XMLSerializer(new FileWriter(xmlFile),
			format);
		ser.asDOMSerializer();
		ser.serialize(document);
*/
		return;
    	}

	public static Vector findChildren(Node node,String elTag) {
		NodeList children = node.getChildNodes();
		Vector items = new Vector();
		for (int i = 0; i < children.getLength(); i++) {
			Node item = children.item(i);
			if (item.getNodeName().equals(elTag)) {
				items.add(item);
			}
		}
		return items;
	}

	public static Node getChildNode(Node node,String elTag) 
		throws Exception {
		NodeList children = node.getChildNodes();
		Vector items = new Vector();
		for (int i = 0; i < children.getLength(); i++) {
			Node item = children.item(i);
			if (item.getNodeName().equals(elTag)) {
				return item;
			}
		}
		throw new XMLUtil.XMLUtilException(XMLUtil.XMLUtilException.NO_SUCH_CHILD);
	}

	public static String getChildValue(Node node,String elTag) 
		throws Exception {
		Node item = getChildNode(node,elTag);
		if (item == null) {
			logger.warn("No child " + elTag + " in node " + node);
			throw new XMLUtil.XMLUtilException(XMLUtil.XMLUtilException.NO_SUCH_CHILD);
		}
		return getNodeValue(item);
	}

	
	public static String getNodeValue(Node node) throws XMLUtilException {
		Node value = node.getFirstChild();
		if (value == null) {
			logger.warn("No value for object " + node);
			throw new XMLUtilException(XMLUtilException.NO_VALUE);
		}
		return value.getNodeValue();
	}

	public static String getAttrValue(Node node,String attrName) throws XMLUtilException {
		NamedNodeMap attrs = node.getAttributes();
		Node attr = attrs.getNamedItem(attrName);
		if (attr == null) return null;
		return attr.getNodeValue();
	}

	public static Node createTextNode(Document document, String elTag,
		String value) {
		Node elem = document.createElement(elTag);
		Node text = document.createTextNode(value);
		elem.appendChild(text);
		return elem;
	}

	public static void addTextNode(Document document, Node parent,
		String elTag, String value) {
		Node node = createTextNode(document,elTag,value);
		parent.appendChild(node);
	}

	public static void addAttribute(Document document,
		Node parent,String attrName, String value) {
		Attr attr = document.createAttribute(attrName);
		attr.setValue(value);
		NamedNodeMap map = parent.getAttributes();
		map.setNamedItem(attr);
	}

	public static class XMLUtilException extends java.lang.Exception {
		public static final String NO_SUCH_CHILD = "Expected Node in the children";
		public static final String NO_VALUE = "No value for the node";

		public XMLUtilException(String msg) {
			super(msg);
		}
	}

}

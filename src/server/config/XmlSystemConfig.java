package com.cisco.ettx.admin.config.xml;

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

import org.apache.log4j.Logger;

import com.cisco.ettx.admin.common.SystemConfiguration;
import com.cisco.ettx.admin.authentication.CryptoHelper;

public class XmlSystemConfig
{
    private Logger logger = Logger.getLogger(XmlSystemConfig.class);
    private static XmlSystemConfig _instance = null;

    private String xmlFile;

    private static String USER_ID = "USER_ID";
    private static String PASSWORD = "PASSWORD";
    private static String EVENT_PURGE_DURATION = "EVENT_PURGE_DURATION";
    private static String EVENT_EXPORT_DIR = "EVENT_EXPORT_DIR";
    private static String LEASE_HIST_EXPORT_SCHEDULE = "LEASE_HIST_EXPORT_SCHEDULE";
    private static String LEASE_HIST_EXPORT_DAY = "LEASE_HIST_EXPORT_DAY";
    private static String LEASE_HIST_EXPORT_TIME = "LEASE_HIST_EXPORT_TIME";
    private static String LEASE_HIST_EXPORT_DIR = "LEASE_HIST_EXPORT_DIR";

    Document document;

    public static XmlSystemConfig getInstance(String ettxDir)
	throws Exception
    {
	if (_instance == null) 
	    _instance = new XmlSystemConfig(ettxDir);
	return _instance;
    }

    protected XmlSystemConfig(String ettxDir)
	throws Exception
    {
	xmlFile = ettxDir + "/config/SystemConfig.xml";
	loadXmlFile();
    }

    public synchronized void loadXmlFile()
	throws Exception
    {
	logger.debug("Loading XmlFile: " + xmlFile);
	try {
	    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	    document = docBuilder.parse(new File(xmlFile));
	    document.getDocumentElement().normalize();
	} catch (javax.xml.parsers.ParserConfigurationException e) {
	    logger.error("Exception ParserConfigurationException: " + e.getMessage());
	    throw e;
	} catch (org.xml.sax.SAXException e) {
	    logger.error("SAXException: " + e.getMessage());
	    throw e;
	} catch (java.io.IOException e) {
	    logger.error("IOException: " + e.getMessage());
	    throw e;
	} catch (Exception e) {
	    logger.error("Unexpected Exception: " + e);
	    throw e;
	}
    }

    public SystemConfiguration processSystemConfig()
	throws Exception 
    {
	    
	logger.debug("Process SystemConfiguration...");
	SystemConfiguration systemconfig = new SystemConfiguration();

	NodeList nodelist = document.getElementsByTagName("SYSTEM_CONFIG");
	// There should be only one
	Node node = nodelist.item(0);
	NodeList childNodes = node.getChildNodes();
	for (int i=0; i<childNodes.getLength(); i++)
	{
	    Node childNode = childNodes.item(i);

	    if (childNode.getNodeType() == Node.ELEMENT_NODE) {
		Node value = childNode.getFirstChild();
		String tagname = childNode.getNodeName(); 
		if (tagname.equals(USER_ID))
		    systemconfig.setSpeUserID(value.getNodeValue());
		else if (tagname.equals(PASSWORD)) { 
		    String decryptedPassword = CryptoHelper.getInstance().decryption(value.getNodeValue());
		    systemconfig.setSpePassword(decryptedPassword);
		}
		else if (tagname.equals(EVENT_PURGE_DURATION)) {
		    String eventPurgeDuration = value.getNodeValue();
		    systemconfig.setPurgeDur(Integer.parseInt(eventPurgeDuration));
		}
		else if (tagname.equals(EVENT_EXPORT_DIR))
		    systemconfig.setExportDir(value.getNodeValue());
		else if (tagname.equals(LEASE_HIST_EXPORT_SCHEDULE)) {
		    String leaseHistoryExportSchedule = value.getNodeValue();
		    Boolean bool = Boolean.valueOf(leaseHistoryExportSchedule);
		    systemconfig.setLeaseHistorySchOn(bool.booleanValue());
	 	}
		else if (tagname.equals(LEASE_HIST_EXPORT_DIR))
		    systemconfig.setLeaseHistoryExportDir(value.getNodeValue());
		else if (tagname.equals(LEASE_HIST_EXPORT_DAY)) {
		    String leaseHistoryExportDay = value.getNodeValue();
		    systemconfig.setLeaseHistoryExportDay(Integer.parseInt(leaseHistoryExportDay));
		}
		else if (tagname.equals(LEASE_HIST_EXPORT_TIME))
		    systemconfig.setLeaseHistoryExportTime(value.getNodeValue());
	    }
	}
	return systemconfig;
    }

    public synchronized void writeSystemConfiguration(SystemConfiguration newconfig)
	throws Exception
    {
	String encryptedPassword = null;

	NodeList nodelist = document.getElementsByTagName("SYSTEM_CONFIG");
	// There should be only one
	Node node = nodelist.item(0);
	NodeList childNodes = node.getChildNodes();
	for (int i=0; i<childNodes.getLength(); i++)
	{
	    Node childNode = childNodes.item(i);
	    if (childNode.getNodeType() == Node.ELEMENT_NODE) {
		Node newvalue;
		Node oldvalue = childNode.getFirstChild();
		String tagname = childNode.getNodeName(); 
		if (tagname.equals(USER_ID)) {
		    newvalue = document.createTextNode(newconfig.getSpeUserID());
		    childNode.replaceChild(newvalue, oldvalue);
		} 
		else if (tagname.equals(PASSWORD)) {
		    encryptedPassword = CryptoHelper.getInstance().encryption(newconfig.getSpePassword());
		    newvalue = document.createTextNode(encryptedPassword);
		    childNode.replaceChild(newvalue, oldvalue);
		}
		else if (tagname.equals(EVENT_PURGE_DURATION)) {
		    int eventPurgeDuration = newconfig.getPurgeDur();
		    newvalue = document.createTextNode(String.valueOf(eventPurgeDuration));
		    childNode.replaceChild(newvalue, oldvalue);
		}
		else if (tagname.equals(EVENT_EXPORT_DIR)) {
		    newvalue = document.createTextNode(newconfig.getExportDir());
		    childNode.replaceChild(newvalue, oldvalue);
		}
		else if (tagname.equals(LEASE_HIST_EXPORT_SCHEDULE)) {
		    boolean leaseHistoryExportSchedule = newconfig.getLeaseHistorySchOn(); 
		    newvalue = document.createTextNode(String.valueOf(leaseHistoryExportSchedule));
		    childNode.replaceChild(newvalue, oldvalue);
		}
		else if (tagname.equals(LEASE_HIST_EXPORT_DIR)) {
		    newvalue = document.createTextNode(newconfig.getLeaseHistoryExportDir());
		    childNode.replaceChild(newvalue, oldvalue);
		}
		else if (tagname.equals(LEASE_HIST_EXPORT_DAY)) {
		    int leaseHistoryExportDay = newconfig.getLeaseHistoryExportDay();
		    newvalue = document.createTextNode(String.valueOf(leaseHistoryExportDay));
		    childNode.replaceChild(newvalue, oldvalue);
		}
		else if (tagname.equals(LEASE_HIST_EXPORT_TIME)) {
		    newvalue = document.createTextNode(newconfig.getLeaseHistoryExportTime());
		    childNode.replaceChild(newvalue, oldvalue);
		}
	    }
	}

	try {
	    // transform the document to the file
	    TransformerFactory xformFactory = TransformerFactory.newInstance();
	    Transformer idTransformer = xformFactory.newTransformer();
	    Source input = new DOMSource(document);
	    Result output = new StreamResult(new File(xmlFile));
	    idTransformer.transform(input, output);
	} catch (javax.xml.transform.TransformerConfigurationException e) {
	    logger.error("TransformerConfigurationException: " + e.getMessage());
	    throw e;
	} catch (javax.xml.transform.TransformerException e) {
	    logger.error("TransformerException: " + e.getMessage());
	    throw e;
	} catch (Exception e) {
	    logger.error("Unexpected Exception: " + e);
	    throw e;
	}

	return;
	
    }
     
}

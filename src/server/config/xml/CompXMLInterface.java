package com.cisco.ettx.admin.config.xml;
import java.io.File;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import org.w3c.dom.*; 

import org.apache.log4j.Logger;

import com.cisco.ettx.admin.common.SMSComponent;
import com.cisco.ettx.admin.common.ComponentHostData;
import com.cisco.ettx.admin.authentication.CryptoHelper;
import com.cisco.ettx.admin.config.ConfigurationException;
import com.cisco.ettx.admin.common.util.XMLUtil;

public class CompXMLInterface
{
    private static Logger logger = Logger.getLogger(CompXMLInterface.class);

    private static String COMP_CONFIG = "COMP_CONFIG";
    private static String COMPONENT = "COMPONENT";
    private static String COMPONENT_LABEL = "COMPONENT_LABEL";
    private static String HOST = "HOST";
    private static String HOST_NAME = "NAME";
    private static String COMP_NAME = "NAME";
    private static String APPL_LOGIN_ID = "APPL_LOGIN_ID";
    private static String APPL_PASSWORD = "APPL_PASSWORD";
    private static String HOST_LOGIN_ID = "HOST_LOGIN_ID";
    private static String HOST_PASSWORD = "HOST_PASSWORD";
    private static String HOST_PROMPT = "HOST_PROMPT";
    private static String LOG_DIRS = "LOG_DIRS";
    private static String USE_SECURE_SHELL = "USE_SECURE_SHELL";
    private static String PATH = "PATH";

	public static Hashtable readCompConfig(String fileName) 
		throws ConfigurationException {
		Hashtable elems = new Hashtable();
		try {
			Document document = XMLUtil.loadXmlFile(fileName);
			NodeList nodelist = document.getElementsByTagName(COMP_CONFIG);
			Node compList = nodelist.item(0);
			Vector comps = XMLUtil.findChildren(compList,COMPONENT);
			Enumeration iter = comps.elements();
			while (iter.hasMoreElements()) {
				Node comp = (Node)iter.nextElement();
				SMSComponent elem = createComponent(comp);
				logger.debug("Loading component " + elem.getName());
				elems.put(elem.getName(),elem);
			}
			return elems;
		}
		catch (Exception ex) {
			logger.error("Error Reading XML File " + fileName + "Exception " + ex.toString(),ex);
			throw new ConfigurationException(ConfigurationException.ERROR_READING_XML_FILE);
		}
	}

	private static SMSComponent createComponent(Node compNode) throws Exception {
		String compName = XMLUtil.getAttrValue(compNode,COMP_NAME);
		SMSComponent comp = new SMSComponent(compName);
		comp.setLabel(XMLUtil.getChildValue(compNode,COMPONENT_LABEL));
		Vector hosts = XMLUtil.findChildren(compNode,HOST);
		Enumeration iter = hosts.elements();
		Vector chosts = new Vector();
		while (iter.hasMoreElements()) {
			Node host = (Node)iter.nextElement();
			ComponentHostData chost = createHost(host);
			chosts.add(chost);
		}
		comp.setComponentHostData(chosts);
		return comp;
	}

	public static ComponentHostData createHost(Node host) throws Exception {
		ComponentHostData cHost = new ComponentHostData();
		String hostName = XMLUtil.getAttrValue(host,HOST_NAME);
		cHost.setHostName(hostName);
/*

		try {
			cHost.setLoginID(XMLUtil.getChildValue(host,APPL_LOGIN_ID));
		}
		catch (XMLUtil.XMLUtilException ex) {
			cHost.setLoginID(new String());
		}
		try {
			cHost.setPassword(CryptoHelper.getInstance().decryption(
				XMLUtil.getChildValue(host,APPL_PASSWORD)));
		}
		catch (XMLUtil.XMLUtilException ex) {
			cHost.setPassword(new String());
		}
*/
		try {
			cHost.setUnixLoginID(XMLUtil.getChildValue(host,HOST_LOGIN_ID));
		}
		catch (XMLUtil.XMLUtilException ex) {
			cHost.setUnixLoginID(null);
		}
		try {
			cHost.setUnixPassword(CryptoHelper.getInstance().decryption(
				XMLUtil.getChildValue(host,HOST_PASSWORD)));
		}
		catch (XMLUtil.XMLUtilException ex) {
			cHost.setUnixPassword(null);
		}
		try {
			cHost.setUnixPrompt(XMLUtil.getChildValue(host,HOST_PROMPT));
		}
		catch (XMLUtil.XMLUtilException ex) {
			cHost.setUnixPrompt(null);
		}
		try {
			Boolean bool = new Boolean(XMLUtil.getChildValue(host,USE_SECURE_SHELL));
			cHost.setUseSecureShell(bool.booleanValue());
		}
		catch (XMLUtil.XMLUtilException ex) {
		}
		Node logDirsNode = XMLUtil.getChildNode(host,LOG_DIRS);
		Vector logPaths = XMLUtil.findChildren(logDirsNode,PATH);
		Enumeration iter = logPaths.elements();
		Vector logDirs = new Vector();
		while (iter.hasMoreElements()) {
			Node path = (Node)iter.nextElement();
			String pathValue = XMLUtil.getNodeValue(path);
			logDirs.add(pathValue);
		}
		cHost.setLogDirs(logDirs);
		return cHost;
	}

	public static void writeCompConfig(Hashtable comps,
		String fileName)
		throws ConfigurationException {
		try {
			Document document = XMLUtil.createDocument();
			//REVISIT - add doc type
			Element compConfig = document.createElement(COMP_CONFIG);
			document.appendChild(compConfig);
			Enumeration iter = comps.elements();
			while (iter.hasMoreElements()) {
				SMSComponent elem = (SMSComponent)iter.nextElement();
				Node node = createComponentNode(document,elem);
				compConfig.appendChild(node);
			}
			XMLUtil.writeXmlFile(fileName,document);
		}
		catch (Exception ex) {
			logger.error("Error Writing XML File " + fileName + "Exception " + ex.toString(),ex);
			throw new ConfigurationException(ConfigurationException.ERROR_WRITING_XML_FILE);
		}
	}

	public static Node createComponentNode(Document document,
		SMSComponent comp) throws Exception {
		Element elem = document.createElement(COMPONENT);
		XMLUtil.addAttribute(document,elem,COMP_NAME,comp.getName());

		XMLUtil.addTextNode(document,elem,COMPONENT_LABEL, comp.getLabel());
		Vector hosts = comp.getComponentHostData();
		for (int i = 0; i < hosts.size(); i++) {
			ComponentHostData host = (ComponentHostData)hosts.elementAt(i);
			Node hostNode = createCompHostNode(document,host);
			elem.appendChild(hostNode);
		}
		return elem;
	}

	public static Node createCompHostNode(Document document,
		ComponentHostData host) throws Exception {
		Element elem = document.createElement(HOST);
		XMLUtil.addAttribute(document,elem,HOST_NAME,host.getHostName());
/*
		XMLUtil.addTextNode(document,elem,APPL_LOGIN_ID, host.getLoginID());
		XMLUtil.addTextNode(document,elem,APPL_PASSWORD, 
			CryptoHelper.getInstance().encryption(host.getPassword()));
*/
		if (host.getUnixLoginID() != null) {
			XMLUtil.addTextNode(document,elem,HOST_LOGIN_ID, host.getUnixLoginID());
		}
		if (host.getUnixPassword() != null) {
			XMLUtil.addTextNode(document,elem,HOST_PASSWORD, 
				CryptoHelper.getInstance().encryption(
				host.getUnixPassword()));
		}
		if (host.getUnixPrompt() != null) {
			XMLUtil.addTextNode(document,elem,HOST_PROMPT, host.getUnixPrompt());
		}
		Boolean bool = new Boolean(host.getUseSecureShell());
		XMLUtil.addTextNode(document,elem,USE_SECURE_SHELL, bool.toString());

		Vector logdirs = host.getLogDirs();
		Node logdirsNode = document.createElement(LOG_DIRS);
		elem.appendChild(logdirsNode);
		for (int i = 0; i < logdirs.size(); i++) {
			String path = (String)logdirs.elementAt(i);
			XMLUtil.addTextNode(document,logdirsNode,PATH, path);
		}
		return elem;
	}
}

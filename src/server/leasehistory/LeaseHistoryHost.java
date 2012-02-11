//**************************************************
// Copyright (c) 2001, 2002 Cisco Systems, Inc.
// All rights reserved.
//**************************************************
// Author: Marvin Miles


package com.cisco.ettx.admin.server.leasehistory;
import java.util.Vector;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Date;
import java.util.Enumeration;
import java.text.SimpleDateFormat;
import com.cisco.ettx.admin.common.net.TelnetWrapper;
import java.io.*;
import org.apache.log4j.Logger;
import org.w3c.dom.*;
import com.cisco.ettx.admin.common.util.XMLUtil;

public class LeaseHistoryHost {

	private static Logger logger = Logger.getLogger("LeaseHistoryHost");
	private final static int TELNET_PORT = 23;
	private final static String LEASEHISTORYXML = "/config/leasehistory.xml";
	private final static String LEASEHISTORY = "LEASEHISTORY";
	private final static String DEFAULTS = "DEFAULTS";
	private final static String NAMESPACE = "NAMESPACE";
	private final static String IPHIST_COMMAND = "IPHIST_COMMAND";
	private final static String IPFORMAT = "IPFORMAT";
	private final static String OUTPUTFILE = "/tmp/iphistoryOutput";
	
	private String hostname;
	private String userid;
	private String password;
	private String prompt;
	private String namespace;
	private String iphistCommand;
	private String ipFormat;
	
	
	public LeaseHistoryHost(String hostname, String userid, String password, String prompt, String ettxRoot) 
		throws LeaseHistoryException {
		
		String leaseHistoryXmlFile = ettxRoot + LEASEHISTORYXML;
		
		setHostname(hostname);
		setUserid(userid);
		setPassword(password);
		setPrompt(prompt);
		
		try {
			logger.debug("Reading " + leaseHistoryXmlFile);
			Document document = XMLUtil.loadXmlFile(leaseHistoryXmlFile);
			NodeList nodelist = document.getElementsByTagName(LEASEHISTORY);
			logger.debug("Received LeaseHistory Elements " + nodelist.getLength());
			Node leasehist = nodelist.item(0);
			Vector defaults = XMLUtil.findChildren(leasehist,DEFAULTS);
			logger.debug("Loaded " + defaults.size() + " defaults from file");
			Enumeration iter = defaults.elements();
			while (iter.hasMoreElements()) {
				Node def = (Node)iter.nextElement();
				setNamespace(XMLUtil.getChildValue(def,NAMESPACE));
				setIphistCommand(XMLUtil.getChildValue(def,IPHIST_COMMAND));
				setIpFormat(XMLUtil.getChildValue(def,IPFORMAT));
				logger.debug("Namespace: " + getNamespace() + " iphist_command: " + getIphistCommand() + " ipFormat: " + getIpFormat());
			}
		}
		catch (Exception ex) {
			logger.error("Error Reading XML File " + leaseHistoryXmlFile + " Exception " + ex.toString(),ex);
			throw new LeaseHistoryException(LeaseHistoryException.ERROR_READING_XML_FILE);
		}		
		logger.info("LeaseHistoryHost initialized successfully...");
	}	

	public void leaseHistoryExtract(String adminLoginID, String adminPassword, Date startDate, Date endDate)
		throws LeaseHistoryException {

		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy@HH:mm:ss");
		String command = getIphistCommand() + " -N " + adminLoginID + " -P " + adminPassword + " -n " + getNamespace() + " -o " + OUTPUTFILE + " -f " + getIpFormat() + " all " + formatter.format(startDate) + " " + formatter.format(endDate);
		logger.debug("Executing command to archive IP Lease History");
		logger.debug(command);
		
		TelnetWrapper telnet = new TelnetWrapper();
		try {
			telnet.connect(getHostname(), TELNET_PORT);
			telnet.login(getUserid(), getPassword());
			telnet.setPrompt(this.prompt);
			logger.debug(telnet.send(""));
			logger.debug(telnet.send(""));
			logger.info("About to execute command on " + getHostname());
			logger.info(telnet.send(command));
			logger.debug("Commnad Complete");
			logger.debug(telnet.send(""));
			logger.debug(telnet.send("exit"));
			logger.debug("Leaveing telnet");
		} catch(java.io.IOException ex) {
			logger.error("Unable to execute command at host " + getHostname() + " Exception : " + ex.toString());
			throw new LeaseHistoryException(LeaseHistoryException.UNABLE_TO_EXECUTE_COMMAND);
		}
	}
		
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public String getHostname() {
		return hostname;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUserid() {
		return userid;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}
	public String getPrompt() {
		return prompt;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setIphistCommand(String iphistCommand) {
		this.iphistCommand = iphistCommand;
	}
	public String getIphistCommand() {
		return iphistCommand;
	}
	public void setIpFormat(String ipFormat) {
		this.ipFormat = ipFormat;
	}
	public String getIpFormat() {
		return ipFormat;
	}
}
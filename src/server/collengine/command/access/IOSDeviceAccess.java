

package com.cisco.ettx.admin.collengine.command;

import com.cisco.ettx.admin.collengine.*;
import com.cisco.ettx.admin.common.util.XMLUtil;
import java.util.Hashtable;
import de.mud.telnet.TelnetWrapper;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;

//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : IOSDeviceAccess.java
// Desc : Interface class to perform data collection via executing a command remotely
//**************************************************

public class  IOSDeviceAccess implements HostAccess {
	protected AttrMap hostNameAttr = null;
	protected AttrMap userNameAttr = null;
	protected AttrMap passwordAttr = null;
	protected AttrMap useSecureShell = null;
	protected AttrMap commandPromptAttr = null;
	private final static String DEFAULT_PROMPT=">";
	private final static String MORE_PROMPT="--More-- ";
	private final static String MORE_PROMPT_RESP=" ";
	private final static int TELNET_PORT = 23;
	private final static int MAX_ATTEMPTS = 5;
	private final static String HOST_NAME = "HOST_NAME";
	private final static String USER_NAME = "USER_NAME";
	private final static String PASSWORD = "PASSWORD";
	private final static String SECURE_SHELL = "SECURE_SHELL";
	private final static String COMMAND_PROMPT = "COMMAND_PROMPT";
	private final static Logger logger = Logger.getLogger(IOSDeviceAccess.class);

	private final static String[] loginPrompts = {"Password: ", "Username: "}; //REVISIT
	private final static String[] passwordPrompts = {"Password: ", DEFAULT_PROMPT};

	public IOSDeviceAccess() {
	}

	public void parseConfig(Node node) throws DataCollectionException {
		try {
			Node child = XMLUtil.getChildNode(node,HOST_NAME);
			hostNameAttr = AttrMap.getAttrMap(child);
			child = XMLUtil.getChildNode(node,USER_NAME);
			userNameAttr = AttrMap.getAttrMap(child);
			child = XMLUtil.getChildNode(node,PASSWORD);
			passwordAttr = AttrMap.getAttrMap(child);
		}
		catch (Exception ex) {
			logger.error("Unable to get the attributes for host", ex);
			throw new DataCollectionException(DataCollectionException.PARSE_ERROR);
		}
		try {
			Node child = XMLUtil.getChildNode(node,COMMAND_PROMPT);
			if (child != null) {
				commandPromptAttr = AttrMap.getAttrMap(child);
			}
		}
		catch (XMLUtil.XMLUtilException ex) {
			commandPromptAttr = new AttrMap();
			commandPromptAttr.setValue(DEFAULT_PROMPT);
		}
		catch (Exception ex) {
			logger.error("Unable to get the attributes for host", ex);
			throw new DataCollectionException(DataCollectionException.PARSE_ERROR);
		}
		try {
			Node child = XMLUtil.getChildNode(node,SECURE_SHELL);
			if (child != null) {
				useSecureShell = AttrMap.getAttrMap(child);
			}
		}
		catch (XMLUtil.XMLUtilException ex) {
			//Do nothing
		}
		catch (Exception ex) {
			logger.error("Unable to get the attributes for host", ex);
			throw new DataCollectionException(DataCollectionException.PARSE_ERROR);
		}
	}

	public String performAction (CollectionHolder data,
			String command) throws 
		DataCollectionException {
		//REVISIT for SSH
		//Get the host name, password and user name
		//from the attributes
		String hostName = (String)hostNameAttr.getAttrValue(data);
		if (hostName == null) {
			logger.error("Unable to get host name from attribute " + hostNameAttr.getMapAttr());
			return null;
		}
		String userName = "";
		try {
			userName = (String)userNameAttr.getAttrValue(data);
		}
		catch (Exception ex) {
			//REVISIT - Username need not be a value present
			//but there may be other issues
		}
		String password = (String)passwordAttr.getAttrValue(data);
		if (password == null) {
			logger.error("Unable to get password attribute " + passwordAttr.getMapAttr());
			return null;
		}
		String prompt = (String)commandPromptAttr.getAttrValue(data);
		if (prompt == null) {
			logger.error("Unable to get password attribute " + commandPromptAttr.getMapAttr());
			return null;
		}
		try {
			TelnetWrapper telnet = new TelnetWrapper();
			telnet.connect(hostName,TELNET_PORT);
			String ret = telnet.waitfor(loginPrompts);
			if (ret.endsWith("Username: ")) {
				//Needs user name and password
				telnet.send(userName);
				ret = telnet.waitfor(loginPrompts);
			}
			if (ret.endsWith("Password: ")) {	//REVISIT - is this okay
				telnet.send(password);
				//Set the prompt value so that we either get the
				// Password back or the prompt.If password comes
				//that means the user was not validated
				passwordPrompts[1] = prompt;	
				ret = telnet.waitfor(passwordPrompts);
				if (ret.endsWith("Password: ")) {
					logger.error("Unable to login to the device " + hostName + ". Invalid UserName/Password");
					throw new DataCollectionException(DataCollectionException.UNABLE_TO_EXECUTE_COMMAND);
				}
			}
			else {
				logger.error("Unable to login to the device. Unexpected prompt " + ret);
				throw new DataCollectionException(DataCollectionException.UNABLE_TO_EXECUTE_COMMAND);
			}
			//telnet.setPrompt(prompt);
			String[] prompts = new String[] {prompt,MORE_PROMPT};
			
			StringBuffer buf = new StringBuffer();
			telnet.send(command);
			int i = 0; 
			while (i < MAX_ATTEMPTS) {
				String output = telnet.waitfor(prompts);
				if (output.endsWith(MORE_PROMPT)) {
					//Remove More 
					String tmp =output.substring(0,output.length()-MORE_PROMPT.length()-1);
					buf.append(tmp.trim());
					telnet.send(MORE_PROMPT_RESP);
				}
				if (output.endsWith(prompt)) {
					buf.append(output.trim());
					break;
				}
				else  {
					//Dont know what to do here
					logger.error("Came to unexpected place. Got output " + output);
				}
			}
			return buf.toString();
		}
		catch (java.net.ConnectException ex) {
			logger.error("Unable to connect to device " ,ex);
			return DataCollectionException.UNABLE_TO_CONNECT_TO_DEVICE;
		}
		catch (IOException ex) {
			logger.error("Unable to execute command at device " + hostName,ex);
			return DataCollectionException.UNABLE_TO_CONNECT_TO_DEVICE;
		}
	}
}

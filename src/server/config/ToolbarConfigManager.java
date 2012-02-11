package com.cisco.ettx.admin.config;

//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : ToolbarConfigManager.java
// Desc : Clss which maintains component information
//**************************************************
import org.apache.log4j.Logger;
import com.cisco.ettx.admin.common.ToolbarElement;
import com.cisco.ettx.admin.config.xml.ToolbarXMLInterface;

public class ToolbarConfigManager {
	private ToolbarElement[] elems = null;
	private String filePath = null;

	private static ToolbarConfigManager instance = null;
	private static Logger logger = Logger.getLogger(
		"ToolbarConfigManager");
	private static final String FILE_NAME="/config/toolbar.xml";

	public static ToolbarConfigManager getInstance()  {
		return instance;
	}

	public ToolbarConfigManager(String dir) 
		throws ConfigurationException {
		if (instance != null) {
			//singleton object
			logger.error("Creating more than one instance");
			throw new ConfigurationException(ConfigurationException.SINGLETON_VIOLATION);
		}
		filePath = new String(dir + FILE_NAME);
		elems = ToolbarXMLInterface.readToolbarConfig(filePath);
		//Configuration initialized
		logger.info("Toolbar configuration initialized successfully...");
		instance = this;
	}

	public ToolbarElement[] getToolbarElements()  {
		return elems;
	}

	public synchronized void setToolbarElements(ToolbarElement[] lelems)
		throws ConfigurationException {
		ToolbarXMLInterface.writeToolbarConfig(lelems,filePath);
		logger.info("Toolbar configuration updated successfully...");
		elems = lelems;
	}

	public synchronized void addToolbarElement(String desc, String url) 
		throws ConfigurationException {
		ToolbarElement[] newElems = new ToolbarElement[elems.length+1];
		int i = 0;
		for (i = 0; i < elems.length;i++) {	
			newElems[i] = elems[i];
		}
		ToolbarElement elem = new ToolbarElement(desc,url);
		newElems[i] = elem;
		setToolbarElements(newElems);
	}

	public synchronized void changeToolbarElement(String oldLink,String desc, String url) 
		throws ConfigurationException {
		ToolbarElement[] newElems = new ToolbarElement[elems.length];
		int i = 0;
		for (i = 0; i < elems.length;i++) {	
			if (elems[i].getUrlLink().equals(oldLink)) {
				newElems[i] = new ToolbarElement(desc,url);
			}
			else {
				newElems[i] = elems[i];
			}
		}
		setToolbarElements(newElems);
	}

	public synchronized void deleteToolbarElement(String urlLink) 
		throws ConfigurationException {
		int i =0;
		for (i = 0; i < elems.length;i++) {	
			if (elems[i].getUrlLink().equals(urlLink)) {
				break;
			}
		}
		if (i != elems.length) {
			ToolbarElement[] newElems = new ToolbarElement[elems.length-1];
			int j = 0;
			for (j = 0;j < i;j++) {
				newElems[j] = elems[j];
			}
			for (j=i+1;j < elems.length;j++) {
				newElems[j-1] = elems[j];
			}
			setToolbarElements(newElems);
		}
	}

}

package com.cisco.ettx.admin.config;

//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : ComponentConfigManager.java
// Desc : Clss which maintains component information
//**************************************************
import org.apache.log4j.Logger;
import com.cisco.ettx.admin.common.*;
import com.cisco.ettx.admin.config.xml.CompXMLInterface;
import java.util.Hashtable;
import java.util.Collection;
import java.util.Vector;
import java.io.File;

public class ComponentConfigManager extends ChangeNotification implements ChangeNotification.NotifyTarget {
	private Hashtable components = null;
	private String filePath = null;

	private static ComponentConfigManager instance = null;
	private static Logger logger = Logger.getLogger(
		"ComponentConfigManager");
	private static final String FILE_NAME="/config/smscomponents.xml";

	public static final String SMS="SMS";
	public static final String ADMIN_APP="AdminApplication";

	public static ComponentConfigManager getInstance()  {
		return instance;
	}

	public ComponentConfigManager(String dir) 
		throws ConfigurationException {
		if (instance != null) {
			//singleton object
			logger.error("Creating more than one instance");
			throw new ConfigurationException(ConfigurationException.SINGLETON_VIOLATION);
		}
		filePath = new String(dir + FILE_NAME);
		components = CompXMLInterface.readCompConfig(filePath);
		instance = this;
		SystemConfigManager.getInstance().addTarget(this);
		logger.info("Component configuration initialized successfully...");
	}

	public SMSComponent[] getAllComponents()  {
		SMSComponent[] array = SMSComponent.toArray(components);
		return (array);
	}

	public SMSComponent getComponent(String compName) 
		throws ConfigurationException {

		logger.debug("Getting component value for " + compName);
		SMSComponent comp = (SMSComponent)components.get(compName);
		if (comp == null) {
			logger.error("Unable to find data for component " + compName);
			throw new ConfigurationException(ConfigurationException.NO_COMPONENT);
		}
		return comp;
	}

	public synchronized void setSMSComponent(SMSComponent comp) 
		throws ConfigurationException {
		logger.debug("Setting component value for " + comp.getName());
		SMSComponent oldComp =  (SMSComponent)components.get(comp.getName());
		Hashtable newTable = new Hashtable(components);
		newTable.put(comp.getName(),comp);
		CompXMLInterface.writeCompConfig(newTable,filePath);
		components = newTable;
		notifyChange(oldComp,comp);
	}

	public synchronized void changeHost(String compName,String hostName,
		ComponentHostData host) 
		throws ConfigurationException {
		SMSComponent oldComp = (SMSComponent)components.get(compName);
		if (oldComp == null) {
			logger.error("Unable to find a component matching name " + compName);
			throw new ConfigurationException(ConfigurationException.NO_COMPONENT);
		}
		SMSComponent newComp = new SMSComponent(compName);
		newComp.setLabel(oldComp.getLabel());
		newComp.setComponentHostData(new Vector(oldComp.getComponentHostData()));
		//delete the host if already there
		if (hostName != null)  {
			newComp.deleteHost(hostName);
		}
		newComp.addHost(host);
		Hashtable newTable = new Hashtable(components);
		newTable.put(compName,newComp);
		CompXMLInterface.writeCompConfig(newTable,filePath);
		components = newTable;
		notifyChange(oldComp,newComp);
	}

	public synchronized void deleteHost(String compName,String hostName) throws ConfigurationException {
		SMSComponent oldComp = (SMSComponent)components.get(compName);
		if (oldComp == null) {
			logger.error("Unable to find a component matching name " + compName);
			throw new ConfigurationException(ConfigurationException.NO_COMPONENT);
		}
		SMSComponent newComp = new SMSComponent(compName);
		newComp.setLabel(oldComp.getLabel());
		newComp.setComponentHostData(new Vector(oldComp.getComponentHostData()));
		//delete the host if already there
		newComp.deleteHost(hostName);
		Hashtable newTable = new Hashtable(components);
		newTable.put(compName,newComp);
		CompXMLInterface.writeCompConfig(newTable,filePath);
		components = newTable;
		notifyChange(oldComp,newComp);
	}

	public void configChanged(Object from,Object to) {
		if (from instanceof SystemConfiguration) {
			boolean saveConfig = false;;
			SystemConfiguration oldConfig = (SystemConfiguration)from;
			SystemConfiguration newConfig = (SystemConfiguration)to;
			SMSComponent comp = (SMSComponent)components.get(ADMIN_APP);
			ComponentHostData host = null;
			if (comp == null) {
				logger.warn("SMSComponent AdminApplication does not exist. Creating one..");
				comp = new SMSComponent(ADMIN_APP);
				comp.setLabel("Administration Management");
				host = new ComponentHostData();
				host.setHostName("localhost");
				comp.addHost(host);
				saveConfig = true;
			}
			else {
				host = (ComponentHostData)comp.getComponentHostData().elementAt(0);
			}
			Vector logDirs = host.getLogDirs();
			if (!newConfig.getLeaseHistoryExportDir().equals(oldConfig.getLeaseHistoryExportDir())) {
				logger.debug("Changing AdminApp component directory for lease history");
				//Changed value
				logDirs.removeElement(oldConfig.getLeaseHistoryExportDir());
				logDirs.add(newConfig.getLeaseHistoryExportDir());
				saveConfig = true;
			}
			if (!newConfig.getExportDir().equals(oldConfig.getExportDir())) {
				//Changed value
				logger.debug("Changing AdminApp component directory for export directory");
				logDirs.removeElement(oldConfig.getExportDir());
				logDirs.add(newConfig.getExportDir());
				saveConfig = true;
			}
			if (saveConfig) {
				try {
					setSMSComponent(comp);
				}
				catch (ConfigurationException ex) {
					logger.error("Unable to save configuration for AdminApplication component",ex);
				}
			}
		}
	}
}

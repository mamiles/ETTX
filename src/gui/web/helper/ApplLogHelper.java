
package com.cisco.ettx.admin.gui.web.helper;

import com.cisco.ettx.admin.common.*;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Iterator;
import java.util.Enumeration;
import org.apache.log4j.Logger;
import com.cisco.ettx.admin.client.axis.*;
import java.rmi.RemoteException;


public class ApplLogHelper {
	public static final String UNABLE_TO_EXECUTE_SERVICE = "Unable to execute service";
	private static Logger logger = Logger.getLogger(ApplLogHelper.class);
	private static Configuration configStub = null;
	private static LogDisplay dispStub = null;

	private static Configuration getConfigStub() throws javax.xml.rpc.ServiceException,AdminServicesException {
		if (configStub != null) return configStub;
		try  {
			AdminServicesLocator loc = new AdminServicesLocator();
			loc.setEngine(AxisAdmin.getEngine());
			configStub = loc.getConfiguration(AxisAdmin.getURL());
			((ConfigurationBindingStub)configStub).setTimeout(AxisAdmin.getTimeout());
		}
		catch (org.apache.axis.ConfigurationException ex) {
			logger.error("Unable to create axis client ",ex);
			throw new AdminServicesException(UNABLE_TO_EXECUTE_SERVICE);
		}
		return configStub;
	}

	private static LogDisplay getDisplayStub() throws javax.xml.rpc.ServiceException,AdminServicesException {
		if (dispStub != null) return dispStub;
		try  {
			AdminServicesLocator loc = new AdminServicesLocator();
			loc.setEngine(AxisAdmin.getEngine());
			dispStub = loc.getLogDisplay(AxisAdmin.getURL());
			((LogDisplayBindingStub)dispStub).setTimeout(AxisAdmin.getTimeout());
		}
		catch (org.apache.axis.ConfigurationException ex) {
			logger.error("Unable to create axis client ",ex);
			throw new AdminServicesException(UNABLE_TO_EXECUTE_SERVICE);
		}
		return dispStub;
	}

	public static Hashtable getAllCompWithoutLogDir(String sessionID)
		throws AdminServicesException {
		Hashtable elems = new Hashtable();
		Vector componentHostData;
		try {
			Configuration stub = getConfigStub();
			SMSComponent[] comps = stub.getAllComponents(sessionID);
			for (int i = 0; i < comps.length; i++) {
				componentHostData = comps[i].getComponentHostData();
				for (int j = 0; j < componentHostData.size(); j++) {
					ComponentHostData host = (ComponentHostData)componentHostData.elementAt(j);
					logger.debug("Removing LogDirs from " + comps[i].getName() + " Host: " + host.getHostName());
					host.setLogDirs(new Vector());
				}
				elems.put(comps[i].getLabel(),comps[i]);
			}
			return elems;
		}
		catch (AdminServicesException ex) {
			logger.error("Service failed at server ",ex);
			throw ex;
		}
		catch (javax.xml.rpc.ServiceException ex) {
			logger.error("Unable to get service executed",ex);
			throw new AdminServicesException(UNABLE_TO_EXECUTE_SERVICE);
		}
		catch (java.rmi.RemoteException ex) {
			logger.error("Unable to get service executed",ex);
			throw new AdminServicesException(UNABLE_TO_EXECUTE_SERVICE);
		}

	}

	public static Hashtable removeEmptyComp(String sessionID,
		Hashtable components) {
		Vector componentHostData;
		Enumeration iter = components.elements();
		while (iter.hasMoreElements()) {
			SMSComponent comps = (SMSComponent)iter.nextElement();
			componentHostData = comps.getComponentHostData();
			logger.debug("Number of hosts: " + componentHostData.size() + " for component: " + comps.getName());
			Iterator it = componentHostData.iterator();
			while (it.hasNext()) {
				ComponentHostData host = (ComponentHostData)it.next();
				logger.debug("Checking for empty component: " + comps.getName() + " Host: " + host.getHostName() + " LogDirs: " + host.getLogDirs().toString());
				if ( host.getLogDirs().isEmpty() ) {
					logger.debug("Delete Host: " + host.getHostName());
					it.remove();
				}
			}
			if (comps.getComponentHostData().isEmpty()) {
				logger.debug("Deleteing component: " + comps.getName());
				components.remove(comps.getLabel());
			}
		}
		return components;
	}

	public static Hashtable getLabelNames(String sessionID)
		throws AdminServicesException {
		Hashtable elems = new Hashtable();
		try {
			Configuration stub = getConfigStub();
			SMSComponent[] comps = stub.getAllComponents(sessionID);
			for (int i = 0; i < comps.length; i++) {
				elems.put(comps[i].getName(), comps[i].getLabel());
			}
			return elems;
		}
		catch (AdminServicesException ex) {
			logger.error("Service failed at server ",ex);
			throw ex;
		}
		catch (javax.xml.rpc.ServiceException ex) {
			logger.error("Unable to get service executed",ex);
			throw new AdminServicesException(UNABLE_TO_EXECUTE_SERVICE);
		}
		catch (java.rmi.RemoteException ex) {
			logger.error("Unable to get service executed",ex);
			throw new AdminServicesException(UNABLE_TO_EXECUTE_SERVICE);
		}

	}


	public static ApplLog[] getLogFilenames(String sessionID,Hashtable components, String searchValue)
		throws AdminServicesException {

		try {
			LogDisplay stub2 = getDisplayStub();
			logger.debug("Getting Log Filenames from server");
			ApplLog[] logs = stub2.getLogFilenames(sessionID,SMSComponent.toArray(components), searchValue);
			return logs;
		}
		catch (AdminServicesException ex) {
			logger.error("Service failed at server ",ex);
			throw ex;
		}
		catch (javax.xml.rpc.ServiceException ex) {
			logger.error("Unable to get service executed",ex);
			throw new AdminServicesException(UNABLE_TO_EXECUTE_SERVICE);
		}
		catch (java.rmi.RemoteException ex) {
			logger.error("Unable to get service executed",ex);
			throw new AdminServicesException(UNABLE_TO_EXECUTE_SERVICE);
		}
	}

	public static String getLogFile(String sessionID,ApplLog log)
		throws AdminServicesException {

		AdminServicesLocator loc = new AdminServicesLocator();
		try {
			LogDisplay stub2 = getDisplayStub();
			String logUrl = stub2.getLogFile(sessionID,log);
			return logUrl;
		}
		catch (AdminServicesException ex) {
			logger.error("Service failed at server ",ex);
			throw ex;
		}
		catch (javax.xml.rpc.ServiceException ex) {
			logger.error("Unable to get service executed",ex);
			throw new AdminServicesException(UNABLE_TO_EXECUTE_SERVICE);
		}
		catch (java.rmi.RemoteException ex) {
			logger.error("Unable to get service executed",ex);
			throw new AdminServicesException(UNABLE_TO_EXECUTE_SERVICE);
		}
	}
}

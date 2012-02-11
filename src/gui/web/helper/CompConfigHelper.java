
package com.cisco.ettx.admin.gui.web.helper;

import com.cisco.ettx.admin.common.*;
import com.cisco.ettx.admin.gui.web.beans.CompConfigurationFormBean;
import java.util.Hashtable;
import org.apache.log4j.Logger;
import com.cisco.ettx.admin.client.axis.*;
import java.rmi.RemoteException;


public class CompConfigHelper {
	public static final String UNABLE_TO_EXECUTE_SERVICE = "Unable to execute service";
	public static final String UNABLE_TO_CONNECT_TO_ADMIN_SERVER = "Unable to connect to server";
	private static Logger logger = Logger.getLogger(CompConfigHelper.class);
	private static Configuration stub = null;

	private static Configuration getStub() throws javax.xml.rpc.ServiceException,AdminServicesException {
		if (stub != null) return stub;
		try  {
			AdminServicesLocator loc = new AdminServicesLocator();
			loc.setEngine(AxisAdmin.getEngine());
			stub = loc.getConfiguration(AxisAdmin.getURL());
			((ConfigurationBindingStub)stub).setTimeout(AxisAdmin.getTimeout());
		}
		catch (org.apache.axis.ConfigurationException ex) {
			logger.error("Unable to create axis client ",ex);
			throw new AdminServicesException(UNABLE_TO_EXECUTE_SERVICE);
		}
		return stub;
	}
	
	public static SMSComponent loadComponent(String sessionID,CompConfigurationFormBean sysBean,
		String compName) throws
		AdminServicesException {
		AdminServicesLocator loc = new AdminServicesLocator();
		try {
			Configuration stub = getStub();
			SMSComponent comp = stub.getCompConfig(sessionID,compName);
			//Add to sysBean
			Hashtable comps = sysBean.getComponents();
			comps.put(compName,comp);
			return comp;
		}
		catch (AdminServicesException ex) {
			logger.error("Service failed at server ",ex);
			throw ex;
		}		
		catch (org.apache.axis.AxisFault ex) {
			logger.error("Unable to connect to server ",ex);
			throw new AdminServicesException(UNABLE_TO_CONNECT_TO_ADMIN_SERVER);
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

	public static void saveComponent(String sessionID,CompConfigurationFormBean sysBean,
		SMSComponent comp) throws
		AdminServicesException {
		try {
			AdminServicesLocator loc = new AdminServicesLocator();
			Configuration stub = getStub();
			stub.setCompConfig(sessionID,comp);
			Hashtable comps = sysBean.getComponents();
			comps.put(comp.getName(),comp);
		}
		catch (AdminServicesException ex) {
			logger.error("Service failed at server ",ex);
			throw ex;
		}		
		catch (org.apache.axis.AxisFault ex) {
			logger.error("Unable to connect to server ",ex);
			throw new AdminServicesException(UNABLE_TO_CONNECT_TO_ADMIN_SERVER);
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
	
	public static SMSComponent[] getAllComponents(String sessionID) throws
		AdminServicesException {
		AdminServicesLocator loc = new AdminServicesLocator();
		try {
			Configuration stub = getStub();
			return stub.getAllComponents(sessionID);
		}
		catch (AdminServicesException ex) {
			logger.error("Service failed at server ",ex);
			throw ex;
		}		
		catch (org.apache.axis.AxisFault ex) {
			logger.error("Unable to connect to server ",ex);
			throw new AdminServicesException(UNABLE_TO_CONNECT_TO_ADMIN_SERVER);
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

	public static void changeHost(String sessionID,String compName,String hostName,ComponentHostData host) throws AdminServicesException {
		try {
			AdminServicesLocator loc = new AdminServicesLocator();
			Configuration stub = getStub();
			stub.changeCompHost(sessionID,compName,hostName,host);
		}
		catch (AdminServicesException ex) {
			logger.error("Service failed at server ",ex);
			throw ex;
		}		
		catch (org.apache.axis.AxisFault ex) {
			logger.error("Unable to connect to server ",ex);
			throw new AdminServicesException(UNABLE_TO_CONNECT_TO_ADMIN_SERVER);
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

	public static void deleteHost(String sessionID,String compName,String hostName) throws AdminServicesException {
		try {
			AdminServicesLocator loc = new AdminServicesLocator();
			Configuration stub = getStub();
			stub.deleteCompHost(sessionID,compName,hostName);
		}
		catch (AdminServicesException ex) {
			logger.error("Service failed at server ",ex);
			throw ex;
		}		
		catch (org.apache.axis.AxisFault ex) {
			logger.error("Unable to connect to server ",ex);
			throw new AdminServicesException(UNABLE_TO_CONNECT_TO_ADMIN_SERVER);
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

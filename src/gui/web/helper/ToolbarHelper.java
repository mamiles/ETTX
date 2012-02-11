
package com.cisco.ettx.admin.gui.web.helper;

import com.cisco.ettx.admin.common.*;
import com.cisco.ettx.admin.gui.web.beans.ToolbarFormBean;
import java.util.Vector;
import org.apache.log4j.Logger;
import com.cisco.ettx.admin.client.axis.*;
import java.rmi.RemoteException;


public class ToolbarHelper {
	public static final String UNABLE_TO_EXECUTE_SERVICE = "Unable to execute service";
	public static final String UNABLE_TO_CONNECT_TO_ADMIN_SERVER = "Unable to connect to server";
	private static Logger logger = Logger.getLogger(ToolbarHelper.class);
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
	
	public static Vector loadElements(String sessionID,ToolbarFormBean sysBean) 
		throws AdminServicesException {
		try {
			Configuration stub = getStub();
			ToolbarElement[] comp = stub.getToolbarElements(sessionID);
			Vector elems = new Vector();
			if (comp == null) {
				//No elements
				logger.debug("ToolbarHelper: No elements returned from server");
				return elems;
			}
			logger.debug("ToolbarHelper: " + comp.length + " elements returned from server");
			for (int i = 0; i < comp.length; i++) {
				elems.add(comp[i]);
			}
			return elems;
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

	public static void saveElements(String sessionID,ToolbarFormBean sysBean,
		Vector elems) throws
		AdminServicesException {
		try {
			Configuration stub = getStub();
			logger.debug("Saving " + elems.size() + " elements in server");
			stub.setToolbarElements(sessionID,ToolbarElement.toArray(elems));
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

	public static void addElement(String sessionID,String name,String url)
		throws AdminServicesException {
		try {
			Configuration stub = getStub();
			stub.addToolbarElement(sessionID,name,url);
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

	public static void changeElement(String sessionID,String oldUrl,
		String name,String url) throws
		AdminServicesException {
		try {
			Configuration stub = getStub();
			stub.changeToolbarElement(sessionID,oldUrl,name,url);
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

	public static void deleteElement(String sessionID,String url)
		throws AdminServicesException {
		try {
			Configuration stub = getStub();
			stub.deleteToolbarElement(sessionID,url);
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


package com.cisco.ettx.admin.gui.web.helper;

import com.cisco.ettx.admin.common.*;
import java.util.Hashtable;
import org.apache.log4j.Logger;
import com.cisco.ettx.admin.client.axis.*;
import java.rmi.RemoteException;


public class SystemConfigHelper {
	public static final String UNABLE_TO_EXECUTE_SERVICE = "Unable to execute service";
	public static final String UNABLE_TO_CONNECT_TO_ADMIN_SERVER = "Unable to connect to server";
	private static Logger logger = Logger.getLogger(SystemConfigHelper.class);
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
	
	public static SystemConfiguration getSystemConfig(String sessionID) throws
		AdminServicesException {
		AdminServicesLocator loc = new AdminServicesLocator();
		try {
			Configuration stub = getStub();
			return stub.getSystemConfig(sessionID);
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

	public static void setSystemConfig(String sessionID,SystemConfiguration config) throws
		AdminServicesException {
		try {
			AdminServicesLocator loc = new AdminServicesLocator();
			Configuration stub = getStub();
			stub.setSystemConfig(sessionID,config);
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
			logger.error("It came here");
			logger.error("Unable to get service executed",ex);
			throw new AdminServicesException(UNABLE_TO_EXECUTE_SERVICE);
		}
		catch (java.rmi.RemoteException ex) {
			logger.error("Unable to get service executed",ex);
			throw new AdminServicesException(UNABLE_TO_EXECUTE_SERVICE);
		}		
	}
	
}

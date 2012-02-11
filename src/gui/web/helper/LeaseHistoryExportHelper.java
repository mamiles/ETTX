package com.cisco.ettx.admin.gui.web.helper;

import com.cisco.ettx.admin.common.*;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.log4j.Logger;
import com.cisco.ettx.admin.client.axis.*;
import java.rmi.RemoteException;

public class LeaseHistoryExportHelper {
	public static final String UNABLE_TO_EXECUTE_SERVICE = "Unable to execute service";
	public static final String UNABLE_TO_CONNECT_TO_ADMIN_SERVER = "Unable to connect to server";
	private static Logger logger = Logger.getLogger(LeaseHistoryExportHelper.class);
	private static LeaseHistoryService stub = null;
	private static Configuration configStub = null;

	private static Configuration getConfigStub() throws javax.xml.rpc.ServiceException, AdminServicesException {
		if (configStub != null) {
			return configStub;
		}
		try {
			AdminServicesLocator loc = new AdminServicesLocator();
			loc.setEngine(AxisAdmin.getEngine());
			configStub = loc.getConfiguration(AxisAdmin.getURL());
			( (ConfigurationBindingStub) configStub).setTimeout(AxisAdmin.getTimeout());
		}
		catch (org.apache.axis.ConfigurationException ex) {
			logger.error("Unable to create axis client ", ex);
			throw new AdminServicesException(UNABLE_TO_EXECUTE_SERVICE);
		}
		return configStub;
	}

	private static LeaseHistoryService getStub() throws javax.xml.rpc.ServiceException, AdminServicesException {
		if (stub != null) {
			return stub;
		}
		try {
			AdminServicesLocator loc = new AdminServicesLocator();
			loc.setEngine(AxisAdmin.getEngine());
			stub = loc.getLeaseHistoryService(AxisAdmin.getURL());
			( (LeaseHistoryServiceBindingStub) stub).setTimeout(AxisAdmin.getTimeout());
		}
		catch (org.apache.axis.ConfigurationException ex) {
			logger.error("Unable to create axis client ", ex);
			throw new AdminServicesException(UNABLE_TO_EXECUTE_SERVICE);
		}
		return stub;
	}

	public static String runLeaseHistoryArchive(String sessionID, String cnrHost, Date startDate, Date endDate, String archiveFile) throws
		AdminServicesException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd@HH:mm:ss");
		try {
			logger.debug("runLeaseHistoryArvhive with CNR Host: " + cnrHost + " Start Date: " + formatter.format(startDate) + " End Date: " +
						 formatter.format(endDate) + " Archive File: " + archiveFile);
			LeaseHistoryService stub = getStub();
			return stub.runLeaseHistoryArchive(sessionID, cnrHost, startDate, endDate, archiveFile);
		}
		catch (AdminServicesException ex) {
			logger.error("Service failed at server ", ex);
			throw ex;
		}
		catch (org.apache.axis.AxisFault ex) {
			logger.error("Unable to connect to server ", ex);
			throw new AdminServicesException(UNABLE_TO_CONNECT_TO_ADMIN_SERVER);
		}
		catch (javax.xml.rpc.ServiceException ex) {
			logger.error("Unable to get service executed", ex);
			throw new AdminServicesException(UNABLE_TO_EXECUTE_SERVICE);
		}
		catch (java.rmi.RemoteException ex) {
			logger.error("Unable to get service executed", ex);
			throw new AdminServicesException(UNABLE_TO_EXECUTE_SERVICE);
		}
	}

	public static Vector getCNRHosts(String sessionID) throws AdminServicesException {
		try {
			Vector cnrHostList = new Vector();
			logger.debug("Get Configuration Service...");
			Configuration configStub = getConfigStub();
			logger.debug("Get SMSComponent for CNR");
			SMSComponent comp = configStub.getCompConfig(sessionID, "CNR");
			Vector componentHostData = comp.getComponentHostData();
			for (int j = 0; j < componentHostData.size(); j++) {
				ComponentHostData host = (ComponentHostData) componentHostData.elementAt(j);
				cnrHostList.add(host.getHostName());
			}
			return cnrHostList;
		}
		catch (AdminServicesException ex) {
			logger.error("Service failed at server ", ex);
			throw ex;
		}
		catch (org.apache.axis.AxisFault ex) {
			logger.error("Unable to connect to server ", ex);
			throw new AdminServicesException(UNABLE_TO_CONNECT_TO_ADMIN_SERVER);
		}
		catch (javax.xml.rpc.ServiceException ex) {
			logger.error("Unable to get service executed", ex);
			throw new AdminServicesException(UNABLE_TO_EXECUTE_SERVICE);
		}
		catch (java.rmi.RemoteException ex) {
			logger.error("Unable to get service executed", ex);
			throw new AdminServicesException(UNABLE_TO_EXECUTE_SERVICE);
		}
	}
}
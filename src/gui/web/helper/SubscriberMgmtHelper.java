
package com.cisco.ettx.admin.gui.web.helper;

import com.cisco.ettx.admin.common.*;
import org.apache.log4j.Logger;
import com.cisco.ettx.admin.gui.web.datatypes.*;
import com.cisco.ettx.admin.common.SubsDeviceInfo;
import java.util.*;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import com.cisco.ettx.admin.common.util.AttrNameConstants;
import com.cisco.ettx.admin.client.axis.*;
import com.cisco.ettx.admin.gui.web.beans.QuerySubscribersFormBean;
import java.rmi.RemoteException;


public class SubscriberMgmtHelper extends AttrNameConstants {
	public static final String UNABLE_TO_EXECUTE_SERVICE = "Unable to execute service";
	public static final String UNABLE_TO_CONNECT_TO_ADMIN_SERVER = "Unable to connect to server";
	private static Logger logger = Logger.getLogger(SubscriberMgmtHelper.class);

	//Task names
	private static String MODIFY_SUBSCRIBER_PROFILE = "MODIFY_SUBSCRIBER_PROFILE";
	private static String DELETE_SUBSCRIBER = "DELETE_SUBSCRIBER";
	private static String MODIFY_SERVICE_PARAMS = "MODIFY_SERVICE_PARAMETERS";
	private static String ADD_SUBS_DEVICE = "ADD_SUBSCRIBER_DEVICE";
	private static String MODIFY_SUBS_DEVICE = "MODIFY_SUBSCRIBER_DEVICE";
	private static String DELETE_SUBS_DEVICE = "DELETE_SUBSCRIBER_DEVICE";

	private static DataCollection stub = null;


	private static int NUM_RECORDS = 500;

	private static DataCollection getStub() throws javax.xml.rpc.ServiceException,AdminServicesException {
		if (stub != null) return stub;
		try  {
			AdminServicesLocator loc = new AdminServicesLocator();
			loc.setEngine(AxisAdmin.getEngine());
			stub = loc.getDataCollection(AxisAdmin.getURL());
			((DataCollectionBindingStub)stub).setTimeout(AxisAdmin.getTimeout());
		}
		catch (org.apache.axis.ConfigurationException ex) {
			logger.error("Unable to create axis client ",ex);
			throw new AdminServicesException(UNABLE_TO_EXECUTE_SERVICE);
		}
		return stub;
	}

	public static  AttrResult iterateRecords(String sessionID,String token,int numRecords) throws
		AdminServicesException {
		try {
			DataCollection stub = getStub();
			return stub.iterateRecords(sessionID,token,numRecords);
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


	public static void modifySubscriberProfile(String sessionID, SubscriberRecord record)
		throws AdminServicesException {
		try {

			DataCollection stub = getStub();
			HashMap input = new HashMap();

			input.put(SUBSCRIBER_ID,record.getSubscriberID());
			input.put(SUBSCRIBER_NAME,record.getSubscriberName());
			input.put(SUBSCRIBER_FIRST_NAME,record.getSubscriberFirstName());
			input.put(SUBSCRIBER_LAST_NAME,record.getSubscriberLastName());
			input.put(LOGIN_ID,record.getLoginID());
			input.put(PASSWORD,record.getPassword());
			input.put(HOME_NUMBER,record.getHomeNumber());
			input.put(MOBILE_NUMBER,record.getMobileNumber());
			input.put(POSTAL_ADDRESS,record.getPostalAddress());
			input.put(POSTAL_CITY,record.getPostalCity());
			input.put(POSTAL_STATE,record.getPostalState());
			input.put(POSTAL_CODE,record.getPostalCode());
			input.put(COUNTRY_CODE,record.getCountryCode());
			HashMap output = stub.executeTask(sessionID,MODIFY_SUBSCRIBER_PROFILE,input,NUM_RECORDS);
			return;
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

	public static void deleteSubscriber(String sessionID, String subsId)
		throws AdminServicesException {
		try {

			DataCollection stub = getStub();
			HashMap input = new HashMap();
			input.put(SUBSCRIBER_ID,subsId);
/*
			Vector services = record.getSubscribedServices();
			if (services.size() > 0) {
				input.put(SERVICE_NAME,services.elementAt(0));
			}
*/
			HashMap output = stub.executeTask(sessionID,DELETE_SUBSCRIBER,input,NUM_RECORDS);
			return;
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

	public static void modifyServiceParameters(String sessionID, 
		String subscriberID, Vector serviceList)
		throws AdminServicesException {
		try {
			DataCollection stub = getStub();
			HashMap input = new HashMap();

			Vector serviceFeatures = new Vector();
			Enumeration iter = serviceList.elements();
			while (iter.hasMoreElements()) {
				HashMap serviceMap = new HashMap();
				ServiceFeatureList service = (ServiceFeatureList)iter.nextElement();
				serviceMap.put(SERVICE_NAME,service.getServiceName());

				serviceMap.put(SERVICE_FEATURES,createServiceFeatureInfo(service.getFeatureList()));
				serviceFeatures.add(serviceMap);
			}
			input.put(SERVICE_PARAMETERS,serviceFeatures);
			input.put(SUBSCRIBER_ID,subscriberID);
			logger.debug("Modifying service parameters for " + subscriberID);
			HashMap output = stub.executeTask(sessionID,MODIFY_SERVICE_PARAMS,input,NUM_RECORDS);
			return;
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

	private static Vector createServiceFeatureInfo(Vector featureList) {
		Vector serviceFeatures = new Vector();
		Enumeration iter = featureList.elements();
		while (iter.hasMoreElements()) {
			HashMap featureMap = new HashMap();
			ServiceFeature feature = (ServiceFeature)iter.nextElement();
			featureMap.put(FEATURE_NAME,feature.getFeatureName());
			featureMap.put(FEATURE_VALUE,feature.getFeatureValue());
			featureMap.put(DEFAULT_VALUE,feature.getDefaultValue());
			featureMap.put(ALLOWED_VALUES,feature.getAllowedValues());
			serviceFeatures.add(featureMap);
		}
		return serviceFeatures;
	}

	public static void addSubsDevice(String sessionID, String subscriberID, 
		SubsDeviceInfo subsDevice) 
		throws AdminServicesException {
		try {
			DataCollection stub = getStub();
			HashMap input = new HashMap();
			input.put(SUBSCRIBER_ID,subscriberID);
			input.put(MAC_ADDRESS,subsDevice.getMacAddress());
			input.put(DISPLAY_NAME,subsDevice.getDisplayName());
			logger.debug("Modifying service parameters for " + subscriberID);
			HashMap output = stub.executeTask(sessionID,ADD_SUBS_DEVICE,input,NUM_RECORDS);
			return;
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

/*
	public static void modifySubsDevice(String sessionID, String subscriberID, 
		SubsDeviceInfo subsDevice,boolean prevStatus) 
		throws AdminServicesException {
		try {
			DataCollection stub = getStub();
			HashMap input = new HashMap();
			input.put(SUBSCRIBER_ID,subscriberID);
			input.put(MAC_ADDRESS,subsDevice.getMacAddress());
			input.put(ACTIVE_STATUS,new Boolean(subsDevice.getActiveStatus()));
			input.put(PREV_ACTIVE_STATUS,new Boolean(prevStatus));
			input.put(DISPLAY_NAME,subsDevice.getDisplayName());
			logger.debug("Modifying service parameters for " + subscriberID);
			HashMap output = stub.executeTask(sessionID,MODIFY_SUBS_DEVICE,input,NUM_RECORDS);
			return;
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
*/

	public static void deleteSubsDevice(String sessionID, String subscriberID, 
		SubsDeviceInfo subsDevice) 
		throws AdminServicesException {
		try {
			DataCollection stub = getStub();
			HashMap input = new HashMap();
			input.put(SUBSCRIBER_ID,subscriberID);
			input.put(MAC_ADDRESS,subsDevice.getMacAddress());
			input.put(DISPLAY_NAME,subsDevice.getDisplayName());
			logger.debug("Modifying service parameters for " + subscriberID);
			HashMap output = stub.executeTask(sessionID,DELETE_SUBS_DEVICE,input,NUM_RECORDS);
			return;
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


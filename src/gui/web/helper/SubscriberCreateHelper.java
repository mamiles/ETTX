
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
import com.cisco.ettx.admin.gui.web.beans.SubscriberCreateFormBean;
import java.rmi.RemoteException;

public class SubscriberCreateHelper extends AttrNameConstants {
	public static final String UNABLE_TO_EXECUTE_SERVICE = "Unable to execute service";
	public static final String MORE_THAN_ONE = "Postal Address Map query returned more than one subscriber record";
	public static final String UNABLE_TO_CONNECT_TO_ADMIN_SERVER = "Unable to connect to server";
	private static Logger logger = Logger.getLogger(SubscriberCreateHelper.class);

	//Task names
	private static String CREATE_SUBSCRIBER_PROFILE = "CREATE_SUBSCRIBER_PROFILE";
	private static String QUERY_POSTAL_ADDRESS_MAP = "QUERY_POSTAL_ADDRESS_MAP";

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

	public static void createSubscriberProfile(String sessionID, SubscriberRecord record,
											   PostalAddressMap addrMap, Vector serviceList)
		throws AdminServicesException {

		try {
			DataCollection stub = getStub();
			HashMap input = new HashMap();

			input.put(SUBSCRIBER_ID,record.getSubscriberID());
			input.put(SUBSCRIBER_NAME,record.getSubscriberName());
			input.put(SUBSCRIBER_FIRST_NAME,record.getSubscriberFirstName());
			input.put(SUBSCRIBER_LAST_NAME,record.getSubscriberLastName());
			input.put(LOGIN_ID,record.getLoginID());
			input.put(ACCT_NUMBER,record.getLoginID());
			input.put(PASSWORD,record.getPassword());
			input.put(HOME_NUMBER,record.getHomeNumber());
			input.put(MOBILE_NUMBER,record.getMobileNumber());
			input.put(POSTAL_ADDRESS,record.getPostalAddress());
			input.put(POSTAL_CITY,record.getPostalCity());
			input.put(POSTAL_STATE,record.getPostalState());
			input.put(POSTAL_CODE,record.getPostalCode());
			input.put(COUNTRY_CODE,record.getCountryCode());

			// setup subscriber DeviceInfo
			Vector subDeviceInfo = new Vector();
			Enumeration eachDevice = record.getSubsDevices().elements();
			while (eachDevice.hasMoreElements()) {
				HashMap deviceInfoMap = new HashMap();
				SubsDeviceInfo deviceInfo = (SubsDeviceInfo)eachDevice.nextElement();
				deviceInfoMap.put(MAC_ADDRESS,deviceInfo.getMacAddress());
				deviceInfoMap.put(DISPLAY_NAME,deviceInfo.getDisplayName());
/*
				deviceInfoMap.put(ACTIVE_STATUS,Boolean.valueOf(deviceInfo.getActiveStatus()));
*/
				subDeviceInfo.add(deviceInfoMap);
			}
			input.put(SUBS_DEVICE_INFO,subDeviceInfo);

            // I don't like these names because they are not correct
			// should be "switchId" and "portInterface"
			input.put(SWITCH_IP_ADDRESS, addrMap.getSwitchIPAddress());
			input.put(SWITCH_ID, addrMap.getSwitchID());
			input.put(SUBS_PORT_INFO, addrMap.getSwitchPortName());

			// Setup service to send
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

			HashMap output = stub.executeTask(sessionID,CREATE_SUBSCRIBER_PROFILE,input,NUM_RECORDS);
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

	public static PostalAddressMap queryPostalAddressMap(String sessionID, SubscriberRecord record)
		throws AdminServicesException, AdminServicesNotification {
		try {

			DataCollection stub = getStub();
			HashMap input = new HashMap();

			input.put(POSTAL_ADDRESS,record.getPostalAddress());
			input.put(POSTAL_CITY,record.getPostalCity());
			input.put(POSTAL_STATE,record.getPostalState());
			input.put(POSTAL_CODE,record.getPostalCode());
			input.put(PORT_IDENTIFIER,record.getPortIdentifier());

			HashMap output = stub.executeTask(sessionID,QUERY_POSTAL_ADDRESS_MAP,input,0);
			if (output == null || output.size() == 0) {
				//We did not get enough data
				logger.error("Unable to execute service. Returned no data");
				throw new AdminServicesException(UNABLE_TO_EXECUTE_SERVICE);
			}
			AttrResult  result = (AttrResult)output.get(POSTAL_ADDRESS_MAP);
			if (result == null) {
				logger.error("Unable to execute service. Did not return subscriberRecords");
				throw new AdminServicesException(UNABLE_TO_EXECUTE_SERVICE);
			}
			Vector list = new Vector();
			boolean pendingRecords =  true;
			while (pendingRecords) {
				Vector recs = (Vector)result.getOutput();
				Enumeration iter = recs.elements();
				while (iter.hasMoreElements()) {
					HashMap rec = (HashMap)iter.nextElement();
					list.add(CollEngineHelper.createObject(rec,PostalAddressMap.class));
				}
				if (result.getPending() > 0) {
					result = CollEngineHelper.iterateRecords(sessionID,result.getIterator(),NUM_RECORDS);
				}
				else {
					pendingRecords = false;
				}
			}
			while (result.getPending() > 0);

			if (list.size() != 1) {
				logger.error("Number of PostalAddressMap objects " + list.size());
				logger.error(
					"Postal Address Map query returned more than one subscriber records or none");
	            throw new AdminServicesException(MORE_THAN_ONE);
			}
			return (PostalAddressMap)list.firstElement();
		}
		catch (AdminServicesException ex) {
			logger.error("Service failed at server ",ex);
			throw ex;
		}
		catch (AdminServicesNotification ex) {
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

	/** Given a Forname and Surname of a Subscriber, generates a selection of
	 * usernames that could be used by this user.
	 * <p>
	 * This implementation will only ever return a single selection i.e. a one
	 * entry array, and it will not confirm that the suggestion is usable.
	 * (Doesn't check if the username is already taken.)
	 * <p>
	 * For this implementation, if there are no forename or surname values then
	 * a single suggestion of empty String will be returned.
	 */
	public static String suggestUsernames(String forename, String surname) {

	   if ((surname == null) || (surname.length() == 0)) {
		   return new String();
	   }

	   if ((forename == null)  || (forename.length() == 0)) {
			return new String(surname);
	   }

	   char forenameInitialChar = forename.charAt(0);
	   String forenameInitial = new String(new char[] { forenameInitialChar });

	   // finally ensure that we don't return a username longer than 20 chars
	   StringBuffer buf = new StringBuffer(forenameInitial);
	   buf.append(surname);
	   String returnValue = null;
	   if (buf.length() > 20) {
			returnValue = buf.substring(0,20);
	   }
	   else {
		   returnValue = buf.toString();
	   }

	   return new String(returnValue.toLowerCase());
	}
	/**
	 * Only generates a single option in this implementation.
	 */
	public static String suggestScreennames(String forename, String surname, String city) {

	   StringBuffer buf = new StringBuffer();

	   if ((forename != null) && (forename.length() != 0)) {
			buf.append(forename);
			buf.append("_");
	   }

	   if ((surname != null) && (surname.length() != 0)) {
			buf.append(surname);
			buf.append("_");
	   }

	   if ((city != null) && (city.length() != 0)) {
			buf.append(city);
	   }

	   // if String ends in '_' then remove final underscore
		if (buf.length() != 0) {
			char finalChar = buf.charAt(buf.length()-1);
			if (finalChar == '_') {
				buf.deleteCharAt(buf.length()-1);
			}
		}

		return new String(buf.toString());
	}


}

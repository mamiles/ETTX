
package com.cisco.ettx.admin.gui.web.helper;

import com.cisco.ettx.admin.common.*;
import org.apache.log4j.Logger;
import com.cisco.ettx.admin.gui.web.datatypes.*;
import java.util.*;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import com.cisco.ettx.admin.common.util.AttrNameConstants;
import com.cisco.ettx.admin.client.axis.*;
import com.cisco.ettx.admin.gui.web.beans.QuerySubscribersFormBean;
import com.cisco.ettx.admin.gui.web.beans.PostalAddressMapFormBean;
import java.rmi.RemoteException;


public class CollEngineHelper extends AttrNameConstants {
	public static final String UNABLE_TO_EXECUTE_SERVICE = "Unable to execute service";
	public static final String UNABLE_TO_CONNECT_TO_ADMIN_SERVER = "Unable to connect to server";
	private static Logger logger = Logger.getLogger(CollEngineHelper.class);

	//Task names
	private static String TROUBLESHOOT_DATA_SERVICE = "TROUBLESHOOT_DATA_SERVICE";
	private static String QUERY_SUBSCRIBERS_SERVICE = "QUERY_SUBSCRIBERS_SERVICE";
	private static String GET_SERVICES_SERVICE = "GET_SERVICES";
	private static String GET_SUBSCRIBER_DETAIL_SERVICE = "GET_SUBSCRIBER_DETAIL_SERVICE";
	private static String QUERY_LEASE_HISTORY_SERVICE = "QUERY_LEASE_HISTORY_SERVICE";
	private static String GET_SUBSCRIBER_INFO_SERVICE = "GET_SUBSCRIBER_INFO_SERVICE";
	private static String GET_SERVICE_PARAMETERS = "GET_SERVICE_PARAMETERS";
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


	public static TroubleshootRecord getTroubleshootDetails(String sessionID,
		String subsID)
		throws AdminServicesException {
		try {
			DataCollection stub = getStub();
			HashMap input = new HashMap();
			input.put(SUBSCRIBER_ID,subsID);
			HashMap output = stub.executeTask(sessionID,TROUBLESHOOT_DATA_SERVICE,
				input,0);
			if (output == null || output.size() == 0) {
				//We did not get enough data
				logger.error("Unable to execute service. Returned no data");
				throw new AdminServicesException(UNABLE_TO_EXECUTE_SERVICE);
			}
			TroubleshootRecord rec = (TroubleshootRecord)createObject(output,TroubleshootRecord.class);
			return rec;
		}
		catch (AdminServicesException ex) {
			logger.error("Service failed at server ",ex);
			throw ex;
		}
		catch (AdminServicesNotification ex) {
			logger.error("Service failed at server ",ex);
			throw new AdminServicesException(ex.getNotification());
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

	public static SubscriberRecord getSubscriberDetails(String sessionID,
		String subsID)
		throws AdminServicesException {
		try {
			DataCollection stub = getStub();
			HashMap input = new HashMap();
			input.put(SUBSCRIBER_ID,subsID);
			HashMap output = stub.executeTask(sessionID,GET_SUBSCRIBER_DETAIL_SERVICE,
				input,0);
			if (output == null || output.size() == 0) {
				//We did not get enough data
				logger.error("Unable to execute service. Returned no data");
				throw new AdminServicesException(UNABLE_TO_EXECUTE_SERVICE);
			}
			SubscriberRecord rec = (SubscriberRecord)createObject(output,SubscriberRecord.class);
			return rec;

		}
		catch (AdminServicesException ex) {
			logger.error("Service failed at server ",ex);
			throw ex;
		}
		catch (AdminServicesNotification ex) {
			logger.error("Service failed at server ",ex);
			throw new AdminServicesException(ex.getNotification());
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


	public static Vector getServiceNames(String sessionID)
		throws AdminServicesException {
		try {
			DataCollection stub = getStub();
			HashMap input = new HashMap();
			HashMap output = stub.executeTask(sessionID,GET_SERVICES_SERVICE,
				input,0);
			Vector list = formatServiceFeatureList(output);
			Vector serviceNames = new Vector();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					ServiceFeatureList element = (ServiceFeatureList)list.elementAt(i);
					serviceNames.add(element.getServiceName());
				}
			}
			return serviceNames;
		}
		catch (AdminServicesException ex) {
			logger.error("Service failed at server ",ex);
			throw ex;
		}
		catch (AdminServicesNotification ex) {
			logger.error("Service failed at server ",ex);
			throw new AdminServicesException(ex.getNotification());
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

	public static Vector getServiceRecords(String sessionID)
		throws AdminServicesException {
		try {
			DataCollection stub = getStub();
			HashMap input = new HashMap();
			HashMap output = stub.executeTask(sessionID,GET_SERVICES_SERVICE,
				input,0);
			return formatServiceFeatureList(output);
		}
		catch (AdminServicesException ex) {
			logger.error("Service failed at server ",ex);
			throw ex;
		}
		catch (AdminServicesNotification ex) {
			logger.error("Service failed at server ",ex);
			throw new AdminServicesException(ex.getNotification());
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

	public static Vector getSubscriberRecords(String sessionID,
		QuerySubscribersFormBean sysBean)
		throws AdminServicesException,AdminServicesNotification {
		try {

			DataCollection stub = getStub();
			HashMap input = new HashMap();
			String layer = sysBean.getCurrentLayer();
			if (layer.equals(sysBean.SUBSCRIBER)) {
				   System.out.println ("Performing the query.");
				String value = sysBean.getFamilyName();
				if (value != null) {
					input.put(SUBS_LAST_NAME_FILTER,value);
				}
				value = sysBean.getGivenName();
				if (value != null) {
					input.put(SUBS_FIRST_NAME_FILTER,value);
				}
				value = sysBean.getHomeNumber();
				if (value != null) {
					input.put(HOME_NUM_FILTER_ATTR_NAME,value);
				}
				value = sysBean.getMobileNumber();
				if (value != null) {
					input.put(MOBILE_NUM_FILTER_ATTR_NAME,value);
				}
				value = sysBean.getPostalAddress();
				if (value != null) {
					input.put(POSTAL_ADDR_FILTER_ATTR_NAME,value);
				}
				value = sysBean.getPostalState();
				if (value != null) {
					input.put(POSTAL_STATE_FILTER_ATTR_NAME,value);
				}
				value = sysBean.getAccountNumber();
				if (value != null) {
					input.put(ACCT_FILTER_ATTR_NAME,value);
				}
				value = sysBean.getLoginID();
				if (value != null) {
					input.put(LOGIN_ID_FILTER_ATTR_NAME,value);
				}
			}
			else if (layer.equals(sysBean.MAC_ADDRESS)) {
				input.put(MAC_ADDRESS_FILTER_ATTR_NAME,sysBean.getMacAddress());
				input.put(MAC_ADDRESS_ALIAS_FILTER_ATTR_NAME,sysBean.getMacAddressAlias());
			}
			else if (layer.equals(sysBean.SERVICE)) {
				input.put(SERVICE_FILTER_ATTR_NAME,sysBean.getService());
			}
			else if (layer.equals(sysBean.SWITCH)) {
				input.put(SWITCH_FILTER_ATTR_NAME,sysBean.getSwitchDetails());
			}
			HashMap output = stub.executeTask(sessionID,QUERY_SUBSCRIBERS_SERVICE,
				input,NUM_RECORDS);
			if (output == null || output.size() == 0) {
				//We did not get enough data
				logger.error("Unable to execute service. Returned no data");
				throw new AdminServicesException(UNABLE_TO_EXECUTE_SERVICE);
			}
			AttrResult  result = (AttrResult)output.get(SUBS_RECORDS_ATTR_NAME);
			if (result == null) {
				logger.error("Unable to execute service. Did not return subscriberRecords");
				throw new AdminServicesException(UNABLE_TO_EXECUTE_SERVICE);
			}
			Vector subs = new Vector();
			boolean pendingRecords =  true;
			while (pendingRecords) {
				Vector records = (Vector)result.getOutput();
				Enumeration iter = records.elements();
				while (iter.hasMoreElements()) {
					HashMap record = (HashMap)iter.nextElement();
					subs.add(createObject(record,SubscriberRecord.class));
				}
				if (result.getPending() > 0) {
					result = iterateRecords(sessionID,result.getIterator(),NUM_RECORDS);
				}
				else {
					pendingRecords = false;
				}
			}
			return subs;
		}
		catch (AdminServicesException ex) {
			logger.error("Service failed at server ",ex);
			throw ex;
		}
		catch (AdminServicesNotification ex) {
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

	public static Vector getLeaseHistory(String sessionID,String subsName,String ipAddr, Calendar startDate, Calendar endDate)
		throws AdminServicesException,AdminServicesNotification {
		try {
			DataCollection stub = getStub();
			HashMap input = new HashMap();
			if (subsName != null && subsName.length() > 0) {
				input.put(SUBS_ATTR_NAME,subsName);
			}
			if (ipAddr != null && ipAddr.length() > 0) {
				input.put(IP_ADDR_ATTR_NAME,ipAddr);
			}
			input.put(START_DATE_ATTR_NAME,startDate);
			input.put(END_DATE_ATTR_NAME,endDate);
			HashMap output = stub.executeTask(sessionID,QUERY_LEASE_HISTORY_SERVICE,
				input,NUM_RECORDS);
			if (output == null || output.size() == 0) {
				//We did not get enough data
				logger.error("Unable to execute service. Returned no data");
				throw new AdminServicesException(UNABLE_TO_EXECUTE_SERVICE);
			}
			AttrResult  result = (AttrResult)output.get(LEASE_INF_ATTR_NAME);
			if (result == null) {
				logger.error("Unable to execute service. Did not return subscriberRecords");
				throw new AdminServicesException(UNABLE_TO_EXECUTE_SERVICE);
			}
			Vector list = new Vector();
			boolean pendingRecords =  true;
			while (pendingRecords) {
				Vector records = (Vector)result.getOutput();
				Enumeration iter = records.elements();
				while (iter.hasMoreElements()) {
					HashMap record = (HashMap)iter.nextElement();
					list.add(createObject(record,LeaseHistoryRecord.class));
				}
				if (result.getPending() > 0) {
					result = iterateRecords(sessionID,result.getIterator(),NUM_RECORDS);
				}
				else {
					pendingRecords = false;
				}
			}
			while (result.getPending() > 0);
			return list;
		}
		catch (AdminServicesException ex) {
			logger.error("Service failed at server ",ex);
			throw ex;
		}
		catch (AdminServicesNotification ex) {
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


	public static Object createObject(HashMap output,Class classObj) throws AdminServicesException {
		try {
			Object rec = classObj.newInstance();
			Iterator keys = output.keySet().iterator();
			while (keys.hasNext()) {
				String attrName = (String)keys.next();
				Object value = output.get(attrName);
				try {
					logger.debug("Adding value " + value + " to property " + attrName + " in Object " + classObj.toString());
					PropertyDescriptor prop = new PropertyDescriptor(
						attrName,classObj);
					Method setMethod = prop.getWriteMethod();
					setMethod.invoke(rec,new Object[] {value});
				}
				catch (Exception ex) {
					logger.error("Unable to set value " + value + " for attribute " + attrName,ex);
				}
			}
			return rec;
		}
		catch (Exception ex) {
			logger.error("Unable to create object of class " + classObj,ex);
			throw new AdminServicesException(UNABLE_TO_EXECUTE_SERVICE);
		}
	}

	public static SubscriberRecord getSubscriberInfo(String sessionID,
		String subsID)
		throws AdminServicesException {
		try {
			DataCollection stub = getStub();
			HashMap input = new HashMap();
			input.put(SUBSCRIBER_ID,subsID);
			HashMap output = stub.executeTask(sessionID,GET_SUBSCRIBER_INFO_SERVICE, input,0);
			if (output == null || output.size() == 0) {
				//We did not get enough data
				logger.error("Unable to execute service. Returned no data");
				throw new AdminServicesException(UNABLE_TO_EXECUTE_SERVICE);
			}
			SubscriberRecord rec = (SubscriberRecord)createObject(output,SubscriberRecord.class);
			return rec;

		}
		catch (AdminServicesException ex) {
			logger.error("Service failed at server ",ex);
			throw ex;
		}
		catch (AdminServicesNotification ex) {
			logger.error("Service failed at server ",ex);
			throw new AdminServicesException(ex.getNotification());
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

	public static Vector getServiceFeatureList(String sessionID, String subsID)
		throws AdminServicesException {
		try {
			DataCollection stub = getStub();
			HashMap input = new HashMap();
			input.put(SUBSCRIBER_ID,subsID);
			logger.debug("Getting service feature list for " + subsID);
			HashMap output = stub.executeTask(sessionID,GET_SERVICE_PARAMETERS, input,0);

			return formatServiceFeatureList(output);
		}
		catch (AdminServicesException ex) {
			logger.error("Service failed at server ",ex);
			throw ex;
		}
		catch (AdminServicesNotification ex) {
			logger.error("Service failed at server ",ex);
			throw new AdminServicesException(ex.getNotification());
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

	public static Vector queryPostalAddressMap(String sessionID, PostalAddressMapFormBean sysBean)
		throws AdminServicesException,AdminServicesNotification {
		try {
			DataCollection stub = getStub();
			HashMap input = new HashMap();
			if (sysBean.getPostalAddress() != null || sysBean.getPostalAddress().length() != 0) {
				input.put(POSTAL_ADDRESS,sysBean.getPostalAddress());
			}
			if (sysBean.getPostalCity() != null || sysBean.getPostalCity().length() != 0) {
				input.put(POSTAL_CITY,sysBean.getPostalCity());
			}
			if (sysBean.getPostalCode() != null || sysBean.getPostalCode().length() != 0) {
				input.put(POSTAL_CODE,sysBean.getPostalCode());
			}
			if (sysBean.getPostalState() != null || sysBean.getPostalState().length() != 0) {
				input.put(POSTAL_STATE,sysBean.getPostalState());
			}
			if (sysBean.getPortIdentifier() != null || sysBean.getPortIdentifier().length() != 0) {
				input.put(PORT_IDENTIFIER,sysBean.getPortIdentifier());
			}

			logger.debug("Getting postal address map");
			HashMap output = stub.executeTask(sessionID,QUERY_POSTAL_ADDRESS_MAP, input,0);
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
				Vector records = (Vector)result.getOutput();
				Enumeration iter = records.elements();
				while (iter.hasMoreElements()) {
					HashMap record = (HashMap)iter.nextElement();
					list.add(createObject(record,PostalAddressMap.class));
				}
				if (result.getPending() > 0) {
					result = iterateRecords(sessionID,result.getIterator(),NUM_RECORDS);
				}
				else {
					pendingRecords = false;
				}
			}
			while (result.getPending() > 0);
			return list;
		}
		catch (AdminServicesException ex) {
			logger.error("Service failed at server ",ex);
			throw ex;
		}
		catch (AdminServicesNotification ex) {
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

	private static Vector formatServiceFeatureList(HashMap output) throws AdminServicesException {

		if (output == null || output.size() == 0) {
			//We did not get enough data
			logger.error("Unable to execute service. Returned no data");
			throw new AdminServicesException(UNABLE_TO_EXECUTE_SERVICE);
		}
		Vector  records = (Vector)output.get(SERVICE_PARAMETERS);
		if (records == null) {
			logger.error("Unable to execute service. Returned no data");
			throw new AdminServicesException(UNABLE_TO_EXECUTE_SERVICE);
		}
		Vector serviceList = new Vector();
		Enumeration iter = records.elements();
		while (iter.hasMoreElements()) {
			HashMap record = (HashMap)iter.nextElement();
			ServiceFeatureList service = new ServiceFeatureList();
			service.setServiceName((String)record.get(SERVICE_NAME));
			logger.debug("Decoding service " + service.getServiceName());
			Vector features = (Vector)record.get(SERVICE_FEATURES);
			Vector serviceFeatures = new Vector();
			Enumeration featureItr = features.elements();
			while (featureItr.hasMoreElements()) {
				HashMap featureRecord = (HashMap)featureItr.nextElement();
				serviceFeatures.add(createObject(featureRecord,ServiceFeature.class));
			}
			service.setFeatureList(serviceFeatures);
			serviceList.add(service);
		}
		return serviceList;
	}

}

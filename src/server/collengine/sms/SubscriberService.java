
package com.cisco.ettx.admin.collengine.sms;

import java.util.Hashtable;
import java.util.HashMap;
import java.util.Vector;
import java.util.List;
import com.cisco.ettx.admin.collengine.CollectionHolder;
import com.cisco.ettx.admin.collengine.DataCollectionException;
import com.cisco.ettx.admin.common.util.AttrNameConstants;
import com.cisco.ettx.admin.common.SubsDeviceInfo;
import org.apache.log4j.Logger;
import java.util.Arrays;
import com.cisco.ettx.admin.server.axis.sms.SubscriberHelper;
import com.cisco.ettx.admin.server.axis.sms.SubscriptionsHelper;
import com.cisco.ettx.admin.server.axis.sms.SMSAPIException;
import com.cisco.sesm.sms.types.*;

//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : SubscriberServices.java
// Desc : Class which performs SMS queries
//**************************************************


public class SubscriberService extends AttrNameConstants implements SMSService {
	private static Logger logger = Logger.getLogger(SubscriberService.class);
	SMSTaskInfo taskInfo = null;
	CollectionHolder holder = null;

	private static String GET_ALL_DETAILS = "GetAllDetailsForSubscriber";
	private static String GET_DETAILS = "GetDetailsForSubscriber";
	private static String QUERY_SUBS = "QuerySubscribers";
	private static String GET_INFO = "GetSubscriberInfo";
	private static String MODIFY_PROFILE = "ModifySubscriberProfile";
	private static String CREATE_SUBS = "CreateSubscriberProfile";
	private static String DELETE_SUBS = "DeleteSubscriber";
	private static String GET_SERVICE_PARAMS = "GetServiceParameters";
	private static String MODIFY_SERVICE_PARAMS = "ModifyServiceParameters";
	private static String ADD_SUBSCRIBER_SERVICE = "AddSubscriberDevice";
	private static String MODIFY_SUBSCRIBER_SERVICE = "ModifySubscriberDevice";
	private static String DELETE_SUBSCRIBER_SERVICE = "DeleteSubscriberDevice";

	//Rollback levels
	private static int ACTIVATE = 1;
	private static int DEACTIVATE = 2;
	private static int UNSUBSCRIBE = 3;
	private static int SUBSCRIBE = 4;

	public SubscriberService() {
	}

	public SubscriberService(SMSTaskInfo ltaskInfo,
		CollectionHolder lholder) {
		taskInfo = ltaskInfo;
		holder = lholder;
	}

	public void performCollection(
		SMSTaskInfo ltaskInfo,
		CollectionHolder lholder)
		throws DataCollectionException {
		taskInfo = ltaskInfo;
		holder = lholder;

		if (taskInfo.getTaskName().equals(QUERY_SUBS)) {
			querySubscribers();
			return;
		}
		else if (taskInfo.getTaskName().equals(GET_DETAILS)) {
			getDetailsForSubscriber();
			return;
		}
		else if (taskInfo.getTaskName().equals(GET_ALL_DETAILS)) {
			getAllDetailsForSubscriber();
			return;
		}
		else if (taskInfo.getTaskName().equals(GET_INFO)) {
			getSubscriberInfo();
			return;
		}
		else if (taskInfo.getTaskName().equals(MODIFY_PROFILE)) {
			modifySubscriberProfile();
			return;
		}
		else if (taskInfo.getTaskName().equals(CREATE_SUBS)) {
			createSubscriberProfile();
			return;
		}
		else if (taskInfo.getTaskName().equals(DELETE_SUBS)) {
			deleteSubscriber();
			return;
		}
		else if (taskInfo.getTaskName().equals(GET_SERVICE_PARAMS)) {
			getServiceParameters();
			return;
		}
		else if (taskInfo.getTaskName().equals(MODIFY_SERVICE_PARAMS)) {
			modifyServiceParameters();
			return;
		}
		else if (taskInfo.getTaskName().equals(ADD_SUBSCRIBER_SERVICE)) {
			addSubsDevice();
			return;
		}
/*
		else if (taskInfo.getTaskName().equals(MODIFY_SUBSCRIBER_SERVICE)) {
			modifySubsDevice();
			return;
		}
*/
		else if (taskInfo.getTaskName().equals(DELETE_SUBSCRIBER_SERVICE)) {
			deleteSubsDevice();
			return;
		}
		logger.error("Unable to map task " + taskInfo.getTaskName() + " to a service for SubscriberService");
		throw new DataCollectionException(DataCollectionException.UNABLE_TO_EXECUTE_TASK);
	}

	public void querySubscribers() throws DataCollectionException {
		try {
			String service = (String)taskInfo.getAttrMapValue(holder,SERVICE_FILTER_ATTR_NAME);
			String switchInfo = (String)taskInfo.getAttrMapValue(holder,SWITCH_FILTER_ATTR_NAME);
			String macAddress = (String)taskInfo.getAttrMapValue(holder,MAC_ADDRESS_FILTER_ATTR_NAME);
			String macAddressAlias = (String)taskInfo.getAttrMapValue(holder,MAC_ADDRESS_ALIAS_FILTER_ATTR_NAME);
			SubscriberInfo[] subs = null;
			if (service != null) {
				logger.debug("Finding subscribers for service " + service);
				subs = SubscriberHelper.findSubsByService(holder.getSessionID(),service);
			}
			else if (switchInfo != null) {
				logger.debug("Finding subscribers for switch " + switchInfo);
				subs = SubscriberHelper.findSubsBySwitch(holder.getSessionID(),switchInfo);
			}
			else if (macAddress != null) {
				logger.debug("Finding subscribers for mac address " + macAddress);
				DeviceInfo device = new DeviceInfo();
				device.setMacAddress(macAddress);
				device.setAlias(macAddressAlias);
				subs = SubscriberHelper.querySubscribersByDevice(holder.getSessionID(),device);
			}
			else {
/*
				String filter = SubscriberHelper.createFilter(subsName,phoneNo,address);
				logger.debug("Finding subscribers for filter " + filter);
				subs = SubscriberHelper.querySubscribers(holder.getSessionID(),filter);
*/
				SubscriberInfo subsInfo = new SubscriberInfo();
				subsInfo.setGivenName((String)taskInfo.getAttrMapValue(holder,SUBS_FIRST_NAME_FILTER));
				subsInfo.setFamilyName((String)taskInfo.getAttrMapValue(holder,SUBS_LAST_NAME_FILTER));
				subsInfo.setHomeNumber((String)taskInfo.getAttrMapValue(holder,HOME_NUM_FILTER_ATTR_NAME));
				subsInfo.setMobileNumber((String)taskInfo.getAttrMapValue(holder,MOBILE_NUM_FILTER_ATTR_NAME));
				subsInfo.setSubscriberId((String)taskInfo.getAttrMapValue(holder,LOGIN_ID_FILTER_ATTR_NAME));
				subsInfo.setAccountId((String)taskInfo.getAttrMapValue(holder,ACCT_FILTER_ATTR_NAME));
				subsInfo.setStreetAddress((String)taskInfo.getAttrMapValue(holder,POSTAL_ADDR_FILTER_ATTR_NAME));
				subsInfo.setPostalState((String)taskInfo.getAttrMapValue(holder,POSTAL_STATE_FILTER_ATTR_NAME));

				subs = SubscriberHelper.querySubscribers(
					holder.getSessionID(),subsInfo);
			}
			Vector subsRecords = createSubsRecords(subs);
			taskInfo.setAttrMapValue(holder,SUBS_RECORDS_ATTR_NAME,subsRecords);
		}
		catch (DataCollectionException ex) {
			throw ex;
		}
		catch (SMSAPIException ex) {
			logger.error("Unable to execute subscriber query ",ex);
			throw new DataCollectionException(ex.getMessage());
		}
		catch (Exception ex) {
			logger.error("Unable to perform get subscriber records matching the query",ex);
			throw new DataCollectionException(DataCollectionException.UNABLE_TO_EXECUTE_TASK);
		}
		return;
	}


	Vector createSubsRecords(SubscriberInfo[] subs) throws DataCollectionException {
		Vector vector = new Vector();
		if (subs == null || subs.length == 0) {
			logger.error("No records received for subscriber query..");
			throw new DataCollectionException(DataCollectionException.NO_SUCH_RECORD,DataCollectionException.INFO);
		}
		logger.debug("Received " + subs.length + " subscribers");
		for (int i = 0; i < subs.length; i++) {
			SubscriberInfo info = subs[i];
			Hashtable record = new Hashtable();
			if (info == null) {
				logger.error("Got a null info object when retrieving  subscribers");
				throw new DataCollectionException(DataCollectionException.UNABLE_TO_EXECUTE_TASK);
			}

			logger.debug("Converting subscriber record " + info.getSubscriberId() + "," + info.getSubscriberId());
			record.put(SUBSCRIBER_ID,info.getSubscriberId());
			if (info.getGivenName() != null)
				record.put(SUBSCRIBER_FIRST_NAME,info.getGivenName());
			if (info.getGivenName() != null)
				record.put(SUBSCRIBER_LAST_NAME,info.getFamilyName());
			if (info.getStreetAddress() != null) {
				record.put(POSTAL_ADDRESS,info.getStreetAddress());
			}
			if (info.getCity() != null) {
				record.put(POSTAL_CITY,info.getCity());
			}
			if (info.getPostalState() != null) {
				record.put(POSTAL_STATE,info.getPostalState());
			}
			if (info.getHomeNumber() != null) {
				record.put(HOME_NUMBER,info.getHomeNumber());
			}
			if (info.getMobileNumber() != null) {
				record.put(MOBILE_NUMBER,info.getMobileNumber());
			}
			record.put(LOGIN_ID,info.getSubscriberId());
			record.put(SERVICES,formatServiceList(info));
			if (info.getAccountId() != null)
				record.put(ACCT_NUMBER,info.getAccountId());
			if (info.getAccountStatus() != null)
				record.put(ACCT_STATUS,info.getAccountStatus());
			vector.add(record);
		}
		return vector;
	}

	public void getAllDetailsForSubscriber() throws DataCollectionException {
		getSubscriberInfo();
		LeaseService leaseService = new LeaseService(taskInfo,holder);
		InventoryService inventoryService = new InventoryService(taskInfo,holder);
		inventoryService.getInventoryForSubscriber();
		leaseService.getLeaseForSubscriber();
		inventoryService.getCPEIPAddress();
		inventoryService.getEdgeRouterForSwitch();
	}

	public void getDetailsForSubscriber() throws DataCollectionException {
		getSubscriberInfo();
		LeaseService leaseService = new LeaseService(taskInfo,holder);
		InventoryService inventoryService = new InventoryService(taskInfo,holder);
		inventoryService.getInventoryForSubscriber();
		leaseService.getLeaseForSubscriber();
	}

	public void getSubscriberInfo() throws DataCollectionException {
		try {
			String subsId = (String)taskInfo.getAttrMapValue(holder,SUBS_ATTR_NAME);
			logger.debug("getSubscriberInfo " + subsId);
			if (subsId == null) {
				logger.error("Need Subscriber ID to retrieve all details");
				throw new DataCollectionException(DataCollectionException.INSUFFICIENT_DATA_FOR_TASK);
			}
			SubscriberInfo info = SubscriberHelper.getSubscriberInfo(holder.getSessionID(),subsId);
			if (info == null) {
				logger.error("unable to to retrieve all details for subscriber " + subsId);
				throw new DataCollectionException(DataCollectionException.UNABLE_TO_EXECUTE_TASK);
			}
			logger.debug("Received Subscriber Details " + subsId);
			taskInfo.setAttrMapValue(holder,SUBSCRIBER_ID,info.getSubscriberId());
			taskInfo.setAttrMapValue(holder,SUBSCRIBER_NAME,info.getDisplayName());
			taskInfo.setAttrMapValue(holder,SUBSCRIBER_FIRST_NAME,info.getGivenName());
			taskInfo.setAttrMapValue(holder,SUBSCRIBER_LAST_NAME,info.getFamilyName());
			taskInfo.setAttrMapValue(holder,POSTAL_ADDRESS,info.getStreetAddress());
			taskInfo.setAttrMapValue(holder,POSTAL_CITY,info.getCity());
			taskInfo.setAttrMapValue(holder,POSTAL_STATE,info.getPostalState());
			taskInfo.setAttrMapValue(holder,POSTAL_CODE,info.getPostalCode());
			taskInfo.setAttrMapValue(holder,COUNTRY_CODE,info.getCountryCode());
			taskInfo.setAttrMapValue(holder,LOGIN_ID,info.getSubscriberId());
			taskInfo.setAttrMapValue(holder,SERVICES,formatServiceList(info));

			taskInfo.setAttrMapValue(holder,ACCT_NUMBER,info.getAccountId());
			taskInfo.setAttrMapValue(holder,ACCT_STATUS,info.getAccountStatus());
			taskInfo.setAttrMapValue(holder,HOME_NUMBER,info.getHomeNumber());
			taskInfo.setAttrMapValue(holder,MOBILE_NUMBER,info.getMobileNumber());
			taskInfo.setAttrMapValue(holder,PASSWORD,info.getPassword());
			taskInfo.setExportAttrMapValue(holder,SUBS_OBJECT,info);
			Vector devices = formatSubsDevices(info);
			taskInfo.setAttrMapValue(holder,SUBS_DEVICES,devices);
		}
		catch (DataCollectionException ex) {
			throw ex;
		}
		catch (SMSAPIException ex) {
			logger.error("Unable to execute subscriber query ",ex);
			throw new DataCollectionException(ex.getMessage());
		}
		catch (Exception ex) {
			logger.error("Unable to perform task getAllDetailsForSubscribers",ex);
			throw new DataCollectionException(DataCollectionException.UNABLE_TO_EXECUTE_TASK);
		}
	}

	private Vector formatServiceList(SubscriberInfo info) {

		ServiceFeaturesListInfo[] serviceFeatures = info.getServiceFeatures();
		if (serviceFeatures != null) {
			Vector serviceList = new Vector();
			for (int i = 0; i < serviceFeatures.length; i++) {
				StringBuffer service = new StringBuffer(serviceFeatures[i].getServiceName());
				//Get service feature info
				ServiceFeatureInfo[] features = serviceFeatures[i].getServiceFeatures();
				/*
				Updating the service attribute for the
				subscriber so it gets reflected in the screen
				Using only the first feature value which
				contains the selected speed. Need to update
				with better mechanism to display multiple
				feature info post EFT
				*/
				if (features != null && features.length > 0) {
					service.append(" - ");
					for (int j = 0; j < features.length; j++) {
						if (j != 0) service.append(", ");
						if (features[j].getSelectedValue() != null) {
							service.append(features[j].getSelectedValue());
						}
						else service.append(features[j].getDefaultValue());
					}
				}
				serviceList.add(service.toString());
			}
			return serviceList;
		}
		List list = Arrays.asList(info.getSubscribedServices());
		return new Vector(list);
	}

	public void modifySubscriberProfile() throws DataCollectionException {
		//get the subscriber information
		try {
			//set whatever values have been changed
			String subsId = (String)taskInfo.getAttrMapValue(holder,SUBSCRIBER_ID);
			if (subsId == null) {
				logger.error("Need Subscriber ID to retrieve all details");
				throw new DataCollectionException(DataCollectionException.INSUFFICIENT_DATA_FOR_TASK);
			}
			SubscriberInfo info = SubscriberHelper.getSubscriberInfo(holder.getSessionID(),subsId);
			String value = (String)taskInfo.getAttrMapValue(holder,SUBSCRIBER_NAME);
			if (value != null) info.setDisplayName(value);
			value = (String)taskInfo.getAttrMapValue(holder,SUBSCRIBER_FIRST_NAME);
			if (value != null) info.setGivenName(value);
			value = (String)taskInfo.getAttrMapValue(holder,SUBSCRIBER_LAST_NAME);
			if (value != null) info.setFamilyName(value);
			value = (String)taskInfo.getAttrMapValue(holder,POSTAL_ADDRESS);
			if (value != null) info.setStreetAddress(value);
			value = (String)taskInfo.getAttrMapValue(holder,POSTAL_CITY);
			if (value != null) info.setCity(value);
			value = (String)taskInfo.getAttrMapValue(holder,POSTAL_STATE);
			if (value != null) info.setPostalState(value);
			value = (String)taskInfo.getAttrMapValue(holder,POSTAL_CODE);
			if (value != null) info.setPostalCode(value);
			value = (String)taskInfo.getAttrMapValue(holder,COUNTRY_CODE);
			if (value != null) info.setCountryCode(value);
			value = (String)taskInfo.getAttrMapValue(holder,LOGIN_ID);
			if (value != null) info.setSubscriberId(value);
			value = (String)taskInfo.getAttrMapValue(holder,PASSWORD);
			if (value != null) info.setPassword(value);
			value = (String)taskInfo.getAttrMapValue(holder,HOME_NUMBER);
			if (value != null) info.setHomeNumber(value);
			value = (String)taskInfo.getAttrMapValue(holder,MOBILE_NUMBER);
			if (value != null) info.setMobileNumber(value);
			info = SubscriberHelper.modifySubscriberInfo(
				holder.getSessionID(),info);
		}
		catch (SMSAPIException ex) {
			logger.error("Unable to execute subscriber query ",ex);
			throw new DataCollectionException(ex.getMessage());
		}
		catch (Exception ex) {
			logger.error("Unable to perform task modifySubscriberProfile",ex);
			throw new DataCollectionException(DataCollectionException.UNABLE_TO_EXECUTE_TASK);
		}
	}

	public void createSubscriberProfile() throws DataCollectionException {
        //  Create Subscriber
		String subsId = (String)taskInfo.getAttrMapValue(holder,SUBSCRIBER_ID);
		try {
			SubscriberInfo info = new SubscriberInfo();
			String value = (String)taskInfo.getAttrMapValue(holder,SUBSCRIBER_ID);
			if (value != null) info.setSubscriberId(value);
			value = (String)taskInfo.getAttrMapValue(holder,SUBSCRIBER_NAME);
			if (value != null) info.setDisplayName(value);
			value = (String)taskInfo.getAttrMapValue(holder,SUBSCRIBER_FIRST_NAME);
			if (value != null) info.setGivenName(value);
			value = (String)taskInfo.getAttrMapValue(holder,SUBSCRIBER_LAST_NAME);
			if (value != null) info.setFamilyName(value);
			value = (String)taskInfo.getAttrMapValue(holder,POSTAL_ADDRESS);
			if (value != null) info.setStreetAddress(value);
			value = (String)taskInfo.getAttrMapValue(holder,POSTAL_CITY);
			if (value != null) info.setCity(value);
			value = (String)taskInfo.getAttrMapValue(holder,POSTAL_STATE);
			if (value != null) info.setPostalState(value);
			value = (String)taskInfo.getAttrMapValue(holder,POSTAL_CODE);
			if (value != null) info.setPostalCode(value);
			value = (String)taskInfo.getAttrMapValue(holder,COUNTRY_CODE);
			if (value != null) info.setCountryCode(value);
			value = (String)taskInfo.getAttrMapValue(holder,LOGIN_ID);
			if (value != null) info.setSubscriberId(value);
			value = (String)taskInfo.getAttrMapValue(holder,ACCT_NUMBER);
			if (value != null) info.setAccountId(value);
			value = (String)taskInfo.getAttrMapValue(holder,PASSWORD);
			if (value != null) info.setPassword(value);
			value = (String)taskInfo.getAttrMapValue(holder,HOME_NUMBER);
			if (value != null) info.setHomeNumber(value);
			value = (String)taskInfo.getAttrMapValue(holder,MOBILE_NUMBER);
			if (value != null) info.setMobileNumber(value);
			value = (String)taskInfo.getAttrMapValue(holder,SWITCH_ID);
			if (value != null) info.setSwitchId(value);
			value = (String)taskInfo.getAttrMapValue(holder,SUBS_PORT_INFO);
			if (value != null) info.setPortInterface(value);

			info = SubscriberHelper.createSubscriberInfo(
                         holder.getSessionID(),info);
		}
		catch (SMSAPIException ex) {
			logger.error("Unable to create subscriber ",ex);
			throw new DataCollectionException(ex.getMessage());
		}
		catch (Exception ex) {
			logger.error("Unable to perform task createSubscriberProfile",ex);
			throw new DataCollectionException(DataCollectionException.UNABLE_TO_EXECUTE_TASK);
		}

		// Subscribe to service
		ServiceFeaturesListInfo[] featureList;
		try {
			featureList = createServiceFeatures();
			SubscriptionsHelper.subscribeService(holder.getSessionID(),subsId,featureList[0]);
		}
		catch (Exception ex) {
			logger.error("Unable to subscribe service for subscriber " + subsId,ex);
			//Delete the subscirber if we cant subscribe to service
			try {
				SubscriberHelper.deleteSubscriber(holder.getSessionID(),
												  subsId);
			}
			catch (SMSAPIException exx) {
				logger.error("Unable to delete subscriber ", exx);
				throw new DataCollectionException(exx.getMessage());
			}
			catch (Exception exy) {
				logger.error("Unable to delete subscriber ", exy);
				throw new DataCollectionException(DataCollectionException.UNABLE_TO_EXECUTE_TASK);
			}
			throw new DataCollectionException(ex.getMessage());
		}

		// Activate Service
		try {
			SubscriptionsHelper.activateService(holder.getSessionID(),subsId,
												featureList[0].getServiceName());
		}
		catch (Exception ex) {
			logger.error("Unable to activate service for subscriber " + subsId,ex);
			//Unsubscribe Service and delete the subscriber
			try {

				SubscriptionsHelper.unsubscribeService(
								holder.getSessionID(),
								subsId, featureList[0].getServiceName());

				SubscriberHelper.deleteSubscriber(holder.getSessionID(),
												  subsId);
			}
			catch (SMSAPIException exq) {
				logger.error("Unable to delete subscriber ", exq);
				throw new DataCollectionException(exq.getMessage());
			}
			catch (Exception exa) {
				logger.error("Unable to delete subscriber ", exa);
				throw new DataCollectionException(DataCollectionException.UNABLE_TO_EXECUTE_TASK);
			}

			throw new DataCollectionException(ex.getMessage());
		}

		// Register DeviceInfo for Subscriber
		DeviceInfo[] deviceInfoList;
		try {
			deviceInfoList = createDeviceInfo();
			if (deviceInfoList.length == 1) {
				if (! (deviceInfoList[0].getMacAddress() == null || deviceInfoList[0].getMacAddress().equals(""))) {
					SubscriberHelper.registerSubscriberDevice(holder.getSessionID(),
						(String) taskInfo.getAttrMapValue(holder, SUBSCRIBER_ID),
						deviceInfoList[0]);
				}
			}

		}
		catch (Exception ex) {
			logger.error("Unable to perform task createSubscriberProfile (Register DeviceInfo)",ex);
			try {

				SubscriptionsHelper.unsubscribeService(
								holder.getSessionID(),
								subsId, featureList[0].getServiceName());

				SubscriberHelper.deleteSubscriber(holder.getSessionID(),
												  subsId);
			}
			catch (SMSAPIException exq) {
				logger.error("Unable to delete subscriber ", exq);
				throw new DataCollectionException(exq.getMessage());
			}
			catch (Exception exa) {
				logger.error("Unable to delete subscriber ", exa);
				throw new DataCollectionException(DataCollectionException.UNABLE_TO_EXECUTE_TASK);
			}

			throw new DataCollectionException(ex.getMessage());
		}

	}


	public void deleteSubscriber() throws DataCollectionException {
		//get the subscriber information
		try {
			//set whatever values have been changed
			String subsId = (String)taskInfo.getAttrMapValue(holder,SUBSCRIBER_ID);
			if (subsId == null) {
				logger.error("Need Subscriber ID to retrieve all details");
				throw new DataCollectionException(DataCollectionException.INSUFFICIENT_DATA_FOR_TASK);
			}
			SubscriberInfo info = SubscriberHelper.getSubscriberInfo(holder.getSessionID(),subsId);
			//Deactivate the subscriber from the service
			if (info == null) {
				logger.error("There was no subscriber record for subscriber id " + subsId);
				throw new DataCollectionException(DataCollectionException.UNKNOWN_SUBSCRIBER);
			}
			String[] services = info.getSubscribedServices();
			if (services != null && services.length > 0) {
				//Assuming only one service here
				String serviceName = services[0];
/*
			String serviceName = (String)taskInfo.getAttrMapValue(holder,SERVICE_NAME);
			if (serviceName != null) {
*/
				logger.debug("Deactivating and unsubscribing subscriber " + subsId + " from service " + serviceName);
				SubscriptionsHelper.deactivateService(
					holder.getSessionID(),
					subsId, serviceName);
				SubscriptionsHelper.unsubscribeService(
					holder.getSessionID(),
					subsId, serviceName);
			}
			SubscriberHelper.deleteSubscriber(holder.getSessionID(),
				subsId);
		}
		catch (SMSAPIException ex) {
			logger.error("Unable to delete subscriber ", ex);
			throw new DataCollectionException(ex.getMessage());
		}
		catch (Exception ex) {
			logger.error("Unable to delete subscriber ", ex);
			throw new DataCollectionException(DataCollectionException.UNABLE_TO_EXECUTE_TASK);
		}
	}

	public void getServiceParameters() throws DataCollectionException {
		try {
			String subsId = (String)taskInfo.getAttrMapValue(holder,SUBSCRIBER_ID);
			if (subsId == null) {
				logger.error("Need Subscriber ID to retrieve all details");
				throw new DataCollectionException(DataCollectionException.INSUFFICIENT_DATA_FOR_TASK);
			}
			SubscriberInfo info = SubscriberHelper.getSubscriberInfo(holder.getSessionID(),subsId);
			if (info == null) {
				logger.error("unable to to retrieve all details for subscriber " + subsId);
				throw new DataCollectionException(DataCollectionException.UNABLE_TO_EXECUTE_TASK);
			}
			//For the service
			String[] services = info.getSubscribedServices();
			if (services == null || services.length <= 0) {
				logger.info("Subscriber is not subscribed to any services " + subsId);
				throw new DataCollectionException(DataCollectionException.NO_SERVICE_FOR_SUBSCRIBER);
			}
			//For the service get the service parameters
			ServiceInfo[] serviceInfos = SubscriptionsHelper.querySubscribedServices(holder.getSessionID(),subsId);
			if (serviceInfos == null || serviceInfos.length <= 0) {
				logger.error("Unable to get service information for the subscriber " + subsId);
				throw new DataCollectionException(DataCollectionException.UNABLE_TO_EXECUTE_TASK);
			}
			Vector records = new Vector();
			for (int i = 0; i < serviceInfos.length; i++) {
				ServiceInfo serviceInfo = serviceInfos[i];
				HashMap record = new HashMap();
				record.put(SERVICE_NAME,serviceInfo.getServiceName());
				Vector serviceFeatures = new Vector();
				ServiceFeatureInfo[] sServiceFeatures =  serviceInfo.getServiceFeatures();
				ServiceFeaturesListInfo subsServiceFeatures = findSubscriberServiceInfo(info.getServiceFeatures(),
					serviceInfo.getServiceName());
				for (int j = 0; j < sServiceFeatures.length; j++) {
					ServiceFeatureInfo serviceFeature = sServiceFeatures[j];
					HashMap featureRecord = new HashMap();
					//Find the associated feature record from subscriber
					ServiceFeatureInfo subsFeature = findServiceFeatureInfo(subsServiceFeatures,
						serviceFeature.getServiceFeatureName());
					featureRecord.put(FEATURE_NAME,serviceFeature.getServiceFeatureName());
					if (serviceFeature.getAllowedValues() != null) {
						List list = Arrays.asList(serviceFeature.getAllowedValues());
						featureRecord.put(ALLOWED_VALUES,new Vector(list));
					}

					if (subsFeature != null)  {
						if (subsFeature.getSelectedValue() == null) {
							featureRecord.put(FEATURE_VALUE,subsFeature.getDefaultValue());
						}
						else {
							featureRecord.put(FEATURE_VALUE,subsFeature.getSelectedValue());
						}

					}
					else {
						if (serviceFeature.getSelectedValue() == null) {
							featureRecord.put(FEATURE_VALUE,serviceFeature.getDefaultValue());
						}
						else {
							featureRecord.put(FEATURE_VALUE,serviceFeature.getSelectedValue());
						}
					}
					featureRecord.put(DEFAULT_VALUE,serviceFeature.getDefaultValue());
					serviceFeatures.add(featureRecord);
				}
				record.put(SERVICE_FEATURES,serviceFeatures);
				records.add(record);
			}
			//Set the record in attributes
			taskInfo.setAttrMapValue(holder,SERVICE_PARAMETERS,records);
		}
		catch (SMSAPIException ex) {
			logger.error("Unable to get subscriber service parameters ",ex);
			throw new DataCollectionException(ex.getMessage());
		}
		catch (Exception ex) {
			logger.error("Unable to get subscriber service parameters ",ex);
			throw new DataCollectionException(DataCollectionException.UNABLE_TO_EXECUTE_TASK);
		}
	}

	private ServiceFeaturesListInfo findSubscriberServiceInfo(ServiceFeaturesListInfo[] serviceList,String serviceName)
	{
		if (serviceList == null) return null;
		for (int i = 0; i < serviceList.length; i++) {
			if (serviceList[i].getServiceName().equals(serviceName)) return serviceList[i];
		}
		return null;
	}

	private ServiceFeatureInfo findServiceFeatureInfo(ServiceFeaturesListInfo featureList,String featureName)
	{
		if (featureList == null) return null;
		ServiceFeatureInfo[] features = featureList.getServiceFeatures();
		for (int i = 0; i < features.length; i++) {
			if (features[i].getServiceFeatureName().equals(featureName)) return features[i];
		}
		return null;
	}

	public void modifyServiceParameters() throws DataCollectionException {
		try {
			String subsId = (String)taskInfo.getAttrMapValue(holder,SUBSCRIBER_ID);
			if (subsId == null) {
				logger.error("Need Subscriber ID to retrieve all details");
				throw new DataCollectionException(DataCollectionException.INSUFFICIENT_DATA_FOR_TASK);
			}
			logger.debug("Changing service parameters for subscriber " + subsId);
			SubscriberInfo info = SubscriberHelper.getSubscriberInfo(holder.getSessionID(),subsId);
			if (info == null) {
				logger.error("unable to to retrieve all details for subscriber " + subsId);
				throw new DataCollectionException(DataCollectionException.UNABLE_TO_EXECUTE_TASK);
			}
			ServiceFeaturesListInfo[] currentFeatures = info.getServiceFeatures(); //Save the old information

			ServiceFeaturesListInfo[] featureList = createServiceFeatures();

			//First deactivate the subscriber
			if (currentFeatures != null && currentFeatures.length > 0) {
				try {
					SubscriptionsHelper.deactivateService(holder.getSessionID(),subsId,
						currentFeatures[0].getServiceName()); //REVISIT
				}
				catch (Exception ex) {
					logger.error("Unable to deactivate subscriber " + subsId,ex);
					throw new DataCollectionException(ex.getMessage());
				}
				try {
					SubscriptionsHelper.unsubscribeService(holder.getSessionID(),subsId,
						currentFeatures[0].getServiceName());
				}
				catch (Exception ex) {
					logger.error("Unable to unsubscribe service for subscriber " + subsId,ex);
					//Rollback the modification
					rollback(subsId,currentFeatures,ACTIVATE);
					throw new DataCollectionException(ex.getMessage());
				}
			}
			try {
				SubscriptionsHelper.subscribeService(holder.getSessionID(),subsId,featureList[0]);
			}
			catch (Exception ex) {
				logger.error("Unable to subscribe service for subscriber " + subsId,ex);
				//Rollback the modification
				rollback(subsId,currentFeatures,SUBSCRIBE);
				throw new DataCollectionException(ex.getMessage());
			}
			try {
				SubscriptionsHelper.activateService(holder.getSessionID(),subsId,
					featureList[0].getServiceName()); //REVISIT
			}
			catch (Exception ex) {
				logger.error("Unable to subscribe service for subscriber " + subsId,ex);
				//Rollback the modification
				rollback(subsId,currentFeatures,SUBSCRIBE);
				throw new DataCollectionException(ex.getMessage());
			}
		}
		catch (Exception ex) {
			logger.error("Unable to subscribe service for subscriber ",ex);
			throw new DataCollectionException(ex.getMessage());
		}
	}

	private ServiceFeaturesListInfo[] createServiceFeatures() throws Exception
	{
		Vector paramRecords = (Vector)taskInfo.getAttrMapValue(holder,SERVICE_PARAMETERS);
		ServiceFeaturesListInfo[] serviceFeatures = new ServiceFeaturesListInfo[paramRecords.size()];
		for (int i = 0; i < paramRecords.size();i++) {
			ServiceFeaturesListInfo serviceInfo = new ServiceFeaturesListInfo();

			HashMap paramRecord = (HashMap)paramRecords.elementAt(i);
			serviceFeatures[i] = serviceInfo;
			serviceInfo.setServiceName((String)paramRecord.get(SERVICE_NAME));
			Vector featureRecords = (Vector)paramRecord.get(SERVICE_FEATURES);
			ServiceFeatureInfo[] features = new ServiceFeatureInfo[featureRecords.size()];
			serviceInfo.setServiceFeatures(features);
			for (int j = 0; j < featureRecords.size(); j++) {
				HashMap featureInfo = (HashMap)featureRecords.elementAt(j);
				ServiceFeatureInfo feature = new ServiceFeatureInfo();
				feature.setServiceFeatureName((String)featureInfo.get(FEATURE_NAME));
				feature.setDefaultValue((String)featureInfo.get(DEFAULT_VALUE));
				feature.setSelectedValue((String)featureInfo.get(FEATURE_VALUE));
				features[j] = feature;
			}
		}
		return serviceFeatures;
	}

	private DeviceInfo[] createDeviceInfo() throws Exception
	{
		Vector deviceInfoRecords = (Vector)taskInfo.getAttrMapValue(holder,SUBS_DEVICE_INFO);
		DeviceInfo[] deviceInfoList = new DeviceInfo[deviceInfoRecords.size()];
		for (int i = 0; i < deviceInfoRecords.size(); i++) {
			DeviceInfo deviceInfo = new DeviceInfo();

			HashMap deviceInfoRecord = (HashMap)deviceInfoRecords.elementAt(i);
			deviceInfoList[i] = deviceInfo;
			deviceInfo.setMacAddress((String)deviceInfoRecord.get(MAC_ADDRESS));
			deviceInfo.setAlias((String)deviceInfoRecord.get(DISPLAY_NAME));
			deviceInfo.setSubscriberId((String)taskInfo.getAttrMapValue(holder,SUBSCRIBER_ID));
		}
		return deviceInfoList;
	}

	private void rollback(String subsId,ServiceFeaturesListInfo[] serviceFeatures,int level) {
		if (serviceFeatures == null || serviceFeatures.length == 0)
			return;
		if (level >= SUBSCRIBE) {
			try {
				SubscriptionsHelper.subscribeService(
					holder.getSessionID(), subsId,serviceFeatures[0]);
			}
			catch (Exception ex) {
				logger.error("Failed  to subscribe subscriber " + subsId + " during rollback",ex);
			}
		}
		if (level >= ACTIVATE) { //Levels above will do this as well
			try {
				SubscriptionsHelper.activateService(
					holder.getSessionID(),subsId,serviceFeatures[0].getServiceName());
			}
			catch (Exception ex) {
				logger.error("Failed  to activate subscriber " + subsId + " during rollback",ex);
			}
		}
	}

	public void addSubsDevice() throws DataCollectionException
	{
		try {
			String subsId = (String)taskInfo.getAttrMapValue(holder,SUBSCRIBER_ID);
			DeviceInfo deviceInfo = new DeviceInfo();
			deviceInfo.setMacAddress((String)taskInfo.getAttrMapValue(holder,MAC_ADDRESS));
			deviceInfo.setAlias((String)taskInfo.getAttrMapValue(holder,DISPLAY_NAME));
			deviceInfo.setSubscriberId(subsId);
			SubscriberHelper.registerSubscriberDevice(
				holder.getSessionID(), subsId,deviceInfo);
		}
		catch (DataCollectionException ex) {
			throw ex;
		}
		catch (SMSAPIException ex) {
			throw new DataCollectionException(ex.getMessage());
		}
		catch (Exception ex) {
			logger.error("Received error while adding subscriber device");
			throw new DataCollectionException(ex.getMessage());
		}
	}

/*
	public void modifySubsDevice() throws DataCollectionException
	{
		try {
			String subsId = (String)taskInfo.getAttrMapValue(holder,SUBSCRIBER_ID);
			DeviceInfo deviceInfo = new DeviceInfo();
			deviceInfo.setMacAddress((String)taskInfo.getAttrMapValue(holder,MAC_ADDRESS));
			deviceInfo.setAlias((String)taskInfo.getAttrMapValue(holder,DISPLAY_NAME));
			deviceInfo.setSubscriberId(subsId);
			if (prevStatus.booleanValue() != deviceInfo.getActive().booleanValue())  {
				if (deviceInfo.getActive().booleanValue() == true) {
					SubscriberHelper.activateSubscriberDevice(
						holder.getSessionID(),
						subsId,deviceInfo);
				}
				else {
					SubscriberHelper.deactivateSubscriberDevice(
						holder.getSessionID(),
						subsId,deviceInfo);
				}
			}
			else {
				SubscriberHelper.modifySubscriberDevice(
					holder.getSessionID(),
					subsId,deviceInfo);
			}
		}
		catch (DataCollectionException ex) {
			throw ex;
		}
		catch (SMSAPIException ex) {
			throw new DataCollectionException(ex.getMessage());
		}
		catch (Exception ex) {
			logger.error("Received error while adding subscriber device");
			throw new DataCollectionException(ex.getMessage());
		}
	}
*/

	public void deleteSubsDevice() throws DataCollectionException
	{
		try {
			String subsId = (String)taskInfo.getAttrMapValue(holder,SUBSCRIBER_ID);
			DeviceInfo deviceInfo = new DeviceInfo();
			deviceInfo.setMacAddress((String)taskInfo.getAttrMapValue(holder,MAC_ADDRESS));
			deviceInfo.setAlias((String)taskInfo.getAttrMapValue(holder,DISPLAY_NAME));
			deviceInfo.setSubscriberId(subsId);
			SubscriberHelper.unregisterSubscriberDevice(
				holder.getSessionID(),
				subsId,deviceInfo);
		}
		catch (DataCollectionException ex) {
			throw ex;
		}
		catch (SMSAPIException ex) {
			throw new DataCollectionException(ex.getMessage());
		}
		catch (Exception ex) {
			logger.error("Received error while deleting subscriber device");
			throw new DataCollectionException(ex.getMessage());
		}
	}

	private Vector formatSubsDevices(SubscriberInfo info) {
		Vector devices = new Vector();
		DeviceInfo[] smcDevices = info.getDevices();
		if (smcDevices == null || smcDevices.length == 0) {

			return devices;
		}
		for (int i = 0; i < smcDevices.length; i++) {
			SubsDeviceInfo device = new SubsDeviceInfo();
			device.setMacAddress(smcDevices[i].getMacAddress());
			device.setDisplayName(smcDevices[i].getAlias());
			devices.add(device);
		}
		return devices;

	}

}


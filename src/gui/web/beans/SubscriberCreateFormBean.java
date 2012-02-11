package com.cisco.ettx.admin.gui.web.beans;

import com.cisco.ettx.admin.gui.web.datatypes.*;
import com.cisco.ettx.admin.common.SubsDeviceInfo;
import java.util.Vector;
import org.apache.struts.action.*;
import java.io.*;
import java.util.*;

/**
 * <p>Title: ETTX Admin Application</p>
 * <p>Description: ETTX Admin App</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Cisco Systems</p>
 * @author Marvin Miles
 * @version 1.0
 */

public class SubscriberCreateFormBean
	extends ActionForm {

	private SubscriberRecord subscriberRecord = null;
	private SubsDeviceInfo subsDeviceInfo = null;
	private ServiceFeatureList service = null;
	private Vector availableServices = null;
	private String selectedServiceName = null;
	private Vector availableServicesNames = null;
	private PostalAddressMap addrMap = null;

	public SubscriberCreateFormBean() {
	}

	public SubsDeviceInfo getSubsDeviceInfo() {
		return subsDeviceInfo;
	}

	public void setSubsDeviceInfo(SubsDeviceInfo rec) {
		subsDeviceInfo = rec;
	}

	public PostalAddressMap getAddrMap() {
		return addrMap;
	}

	public void setAddrMap(PostalAddressMap rec) {
		addrMap = rec;
	}

	public ServiceFeatureList getService() {
		return service;
	}

	public void setService(ServiceFeatureList rec) {
		service = rec;
	}

	public SubscriberRecord getSubscriberRecord() {
		return subscriberRecord;
	}

	public void setSubscriberRecord(SubscriberRecord rec) {
		subscriberRecord = rec;
	}

	public Vector getAvailableServices() {
		return availableServices;
	}
	public void setAvailableServices(Vector rec) {
		availableServices = rec;
		// Populate the services names
		Vector availName = new Vector();
		Enumeration iter = availableServices.elements();
		while (iter.hasMoreElements()) {
			ServiceFeatureList list = (ServiceFeatureList)iter.nextElement();
			availName.add(list.getServiceName());
		}
		setAvailableServicesNames(availName);
		ServiceFeatureList myList = (ServiceFeatureList)availableServices.firstElement();
		setSelectedServiceName(myList.getServiceName());
	}

	public ServiceFeatureList[] getServiceArray() {
		ServiceFeatureList[] serviceArray = new ServiceFeatureList[availableServices.size()];
		for (int i = 0; i < serviceArray.length; i++) {
			serviceArray[i] = (ServiceFeatureList)availableServices.elementAt(i);
		}
		return serviceArray;
	}
	public String getSelectedServiceName() {
		return selectedServiceName;
	}
	public void setSelectedServiceName(String servName) {
		selectedServiceName = servName;
		// populate the features for the selected service name
		Enumeration iter = availableServices.elements();
		while (iter.hasMoreElements()) {
			ServiceFeatureList list = (ServiceFeatureList)iter.nextElement();
			if (list.getServiceName().equals(servName)) {
				setService(list);
				break;
			}
		}
	}

	public Vector getAvailableServicesNames() {
		return availableServicesNames;
	}
	public void setAvailableServicesNames(Vector availNames) {
		availableServicesNames = availNames;
	}
}

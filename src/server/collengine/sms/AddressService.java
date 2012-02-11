
package com.cisco.ettx.admin.collengine.sms;

import java.util.Hashtable;
import java.util.HashMap;
import java.util.Vector;
import java.util.List;
import com.cisco.ettx.admin.collengine.CollectionHolder;
import com.cisco.ettx.admin.collengine.DataCollectionException;
import com.cisco.ettx.admin.common.util.AttrNameConstants;
import org.apache.log4j.Logger;
import java.util.Arrays;
import com.cisco.ettx.admin.server.axis.sms.AddressHelper;
import com.cisco.ettx.admin.server.axis.sms.InventoryHelper;
import com.cisco.ettx.admin.server.axis.sms.SubscriptionsHelper;
import com.cisco.ettx.admin.server.axis.sms.SMSAPIException;
import com.cisco.sesm.sms.types.*;

//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : AddressServices.java
// Desc : Class which performs SMS queries
//**************************************************


public class AddressService extends AttrNameConstants implements SMSService {
	private static Logger logger = Logger.getLogger(AddressService.class);
	SMSTaskInfo taskInfo = null;
	CollectionHolder holder = null;

	private static String QUERY_POSTAL_ADDRESS_MAP = "QueryPostalAddressMap";


	public AddressService() {
	}

	public AddressService(SMSTaskInfo ltaskInfo,
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

		if (taskInfo.getTaskName().equals(QUERY_POSTAL_ADDRESS_MAP)) {
			queryPostalAddressMap();
			return;
		}
		logger.error("Unable to map task " + taskInfo.getTaskName() + " to a service for AddressService");
		throw new DataCollectionException(DataCollectionException.UNABLE_TO_EXECUTE_TASK);
	}

	public void queryPostalAddressMap() throws DataCollectionException {
		try {
			AddressInfo info = new AddressInfo();
			String value = (String)taskInfo.getAttrMapValue(holder,POSTAL_ADDRESS);
			if (value != null && value.length() != 0) {
				info.setPostalAddress(value);
			}
			value = (String)taskInfo.getAttrMapValue(holder,POSTAL_STATE);
			if (value != null && value.length() != 0) {
				info.setPostalRegion(value);
			}
			value = (String)taskInfo.getAttrMapValue(holder,POSTAL_CITY);
			if (value != null && value.length() != 0) {
				info.setPostalLocale(value);
			}
			value = (String)taskInfo.getAttrMapValue(holder,POSTAL_CODE);
			if (value != null && value.length() != 0) {
				info.setPostalCode(value);
			}
			value = (String)taskInfo.getAttrMapValue(holder,PORT_IDENTIFIER);
			if (value != null && value.length() != 0) {
				info.setId(value);
			}

			AddressInfo[] records = AddressHelper.queryPostalAddressMap(
					holder.getSessionID(),info);
			if (records == null || records.length == 0) {
				logger.error("No records received for address query..");
				throw new DataCollectionException(DataCollectionException.NO_SUCH_RECORD,DataCollectionException.INFO);
			}
			//Convert the switch Ids to switch IP address
			SwitchInfo[] switches = InventoryHelper.querySwitchesForAddr(holder.getSessionID(),records);
			if (switches == null || switches.length != records.length) {
				logger.error("No records received for address query..");
				throw new DataCollectionException(DataCollectionException.NO_SUCH_RECORD);
			}
			Vector output = createRecords(records,switches);
/*
			Vector output = createRecords(records);
*/
			taskInfo.setAttrMapValue(holder,POSTAL_ADDRESS_MAP,output);
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


	Vector createRecords(AddressInfo[] addrs,SwitchInfo[] switches) throws DataCollectionException {
		Vector vector = new Vector();

		for (int i = 0; i < addrs.length; i++) {
			AddressInfo info = addrs[i];
			Hashtable record = new Hashtable();
			if (info == null) {
				logger.error("Got a null info object when retrieving  addresss");
				throw new DataCollectionException(DataCollectionException.UNABLE_TO_EXECUTE_TASK);
			}

			if (info.getPostalAddress() != null) {
				record.put(POSTAL_ADDRESS,info.getPostalAddress());
			}
			if (info.getPostalLocale() != null) {
				record.put(POSTAL_CITY,info.getPostalLocale());
			}
			if (info.getPostalRegion() != null) {
				record.put(POSTAL_STATE,info.getPostalRegion());
			}
			if (info.getPostalCode() != null) {
				record.put(POSTAL_CODE,info.getPostalCode());
			}
			if (info.getId() != null) {
				record.put(PORT_IDENTIFIER,info.getId());
			}
			if (info.getSwitchId() != null) {
				record.put(SWITCH_ID,info.getSwitchId());
			}
//REVISIT
			if (switches[i] != null && switches[i].getIpAddress() != null) {
				record.put(SWITCH_IP_ADDRESS,switches[i].getIpAddress());
			}
			if (switches[i] != null & switches[i].getModel() != null) {
				record.put(MODEL,switches[i].getModel());
			}
			if (info.getPortInterface() != null) {
				record.put(SUBS_PORT_INFO,info.getPortInterface());
			}
			vector.add(record);
		}
		return vector;
	}

/*
	Vector createRecords(AddressInfo[] addrs) throws DataCollectionException {
		Vector vector = new Vector();

		for (int i = 0; i < addrs.length; i++) {
			AddressInfo info = addrs[i];
			Hashtable record = new Hashtable();
			if (info == null) {
				logger.error("Got a null info object when retrieving  addresss");
				throw new DataCollectionException(DataCollectionException.UNABLE_TO_EXECUTE_TASK);
			}

			if (info.getPostalAddress() != null) {
				record.put(POSTAL_ADDRESS,info.getPostalAddress());
			}
			if (info.getPostalLocale() != null) {
				record.put(POSTAL_CITY,info.getPostalLocale());
			}
			if (info.getPostalRegion() != null) {
				record.put(POSTAL_STATE,info.getPostalRegion());
			}
			if (info.getPostalCode() != null) {
				record.put(POSTAL_CODE,info.getPostalCode());
			}
			if (info.getSwitchId() != null) {
				record.put(SWITCH_IP_ADDRESS,info.getSwitchId());
			}
			if (info.getPortInterface() != null) {
				record.put(SUBS_PORT_INFO,info.getPortInterface());
			}
			vector.add(record);
		}
		return vector;
	}
*/
}

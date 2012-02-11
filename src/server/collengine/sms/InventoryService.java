
package com.cisco.ettx.admin.collengine.sms;

import java.util.Hashtable;
import java.util.Vector;
import com.cisco.ettx.admin.collengine.CollectionHolder;
import com.cisco.ettx.admin.collengine.DataCollectionException;
import com.cisco.ettx.admin.common.util.AttrNameConstants;
import com.cisco.ettx.admin.server.axis.sms.InventoryHelper;
import com.cisco.ettx.admin.server.axis.sms.SMSAPIException;
import org.apache.log4j.Logger;

//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : InventoryServices.java
// Desc : Class which performs SMS queries
//**************************************************


public class InventoryService extends AttrNameConstants implements SMSService {
	private static Logger logger = Logger.getLogger(InventoryService.class);
	SMSTaskInfo taskInfo = null;
	CollectionHolder holder = null;

	private static final String GET_INVENTORY = "GetInventoryForSubscriber";

	public InventoryService() {
	}

	public InventoryService(SMSTaskInfo ltaskInfo,
		CollectionHolder lholder) {
		taskInfo = ltaskInfo;
		holder = lholder;
	}

	public void performCollection(SMSTaskInfo ltaskInfo,
		CollectionHolder lholder) 
		throws DataCollectionException {
		taskInfo = ltaskInfo;
		holder = lholder;
		if (taskInfo.getTaskName().equals(GET_INVENTORY)) {
			getInventoryForSubscriber();
			return;
		}
	}

	public void  getInventoryForSubscriber() 
		throws DataCollectionException {
		try {
			String acctNo = (String)taskInfo.getAttrMapValue(holder,ACCT_NUMBER);
			if (acctNo == null) {
				logger.error("Subscriber does not have account number");
				return ;
			}
			InventoryHelper.PortDetails info = InventoryHelper.getInventoryForSubscriber(holder.getSessionID(),acctNo);
			if (info == null) {
				logger.error("Unable to get port information for account " + acctNo);
				return ;
				//throw new DataCollectionException(DataCollectionException.UNABLE_TO_EXECUTE_TASK);
			}
			taskInfo.setAttrMapValue(holder,SUBS_PORT_INFO,info.portNumber);
			taskInfo.setAttrMapValue(holder,SWITCH_IP_ADDRESS,info.switchIPAddress);
			taskInfo.setAttrMapValue(holder,SWITCH_OPTION82,new Boolean(info.switchOption82));
			taskInfo.setExportAttrMapValue(holder,SUBS_PORT_ID,info.portId);
			taskInfo.setExportAttrMapValue(holder,SWITCH_USER_NAME,info.userName);
			taskInfo.setExportAttrMapValue(holder,SWITCH_PASSWORD,info.password);
			taskInfo.setExportAttrMapValue(holder,DEVICE_ID,info.switchId);
		}
		catch (SMSAPIException ex) {
			logger.error("Unable to perform task getInventoryForSubscriber for account " ,ex);
			throw new DataCollectionException(ex.getMessage());
		}
		catch (Exception ex) {
			logger.error("Unable to perform task getInventoryForSubscriber for account " ,ex);
			throw new DataCollectionException(DataCollectionException.UNABLE_TO_EXECUTE_TASK);
		}
	}

	public void  getEdgeRouterForSwitch() 
		throws DataCollectionException {
		try {
			String switchIPAddress = (String)taskInfo.getAttrMapValue(holder,SWITCH_IP_ADDRESS);
			String deviceId = (String)taskInfo.getAttrMapValue(holder,DEVICE_ID);
			if (switchIPAddress == null || switchIPAddress.length() == 0)  {
				logger.error("There is no switch IP Address to map the edge router ID");
				//throw new DataCollectionException(DataCollectionException.INSUFFICIENT_DATA_FOR_TASK);
				return ;
			}
			String edgeRouterID = InventoryHelper.getEdgeRouterForSwitch(holder.getSessionID(),deviceId,switchIPAddress);
			if (edgeRouterID == null) {
				logger.error("Unable to get edge router for switch " + switchIPAddress);
				//throw new DataCollectionException(DataCollectionException.INVALID_EDGE_ROUTER_ID);
				return ;
			}
			taskInfo.setAttrMapValue(holder,EDGE_ROUTER_ID,edgeRouterID);
		}
		catch (SMSAPIException ex) {
			logger.error("Unable to perform task getInventoryForSubscriber for account " ,ex);
			throw new DataCollectionException(ex.getMessage());
		}
		catch (Exception ex) {
			logger.error("Unable to get edge router for the switch");
			throw new DataCollectionException(DataCollectionException.UNABLE_TO_EXECUTE_TASK);
		}
	}

	public void getCPEIPAddress() throws DataCollectionException {
		try {
			String acctNo = (String)taskInfo.getAttrMapValue(holder,ACCT_NUMBER);
			if (acctNo == null) {
				logger.error("Subscriber does not have account number to retrieve CPE IP Address");
				return ;
			}
			String ipAddr = InventoryHelper.getCPEIPAddress(holder.getSessionID(),acctNo);
			if (ipAddr == null) {
				logger.error("Unable to get CPE IPAddress for subscriber " + acctNo);
				return ;
				//throw new DataCollectionException(DataCollectionException.UNABLE_TO_EXECUTE_TASK);
			}
			taskInfo.setExportAttrMapValue(holder,CPE_IP_ADDRESS,ipAddr);
				
		}
		catch (SMSAPIException ex) {
			logger.error("Unable to perform task getInventoryForSubscriber for account " ,ex);

			throw new DataCollectionException(ex.getMessage());
		}
		catch (Exception ex) {
			logger.error("Unable to perform task getCPEIPAddr for account ",ex);
			throw new DataCollectionException(DataCollectionException.UNABLE_TO_EXECUTE_TASK);
		}
	}
}

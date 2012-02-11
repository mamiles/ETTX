
package com.cisco.ettx.admin.server.axis.sms;

import com.cisco.sesm.sms.types.*;
import com.cisco.sesm.sms.exceptions.api.*;
import com.cisco.sesm.sms.api.*;
import com.cisco.cns.security.soap.common.*;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.apache.axis.AxisFault;

//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : InventoryServices.java
// Desc : Class which performs SMS queries
//**************************************************


public class InventoryHelper extends SMSAPI {
	private static Logger logger = Logger.getLogger(InventoryHelper.class);
	private static Inventory inventoryStub = null;
	private static Device deviceStub = null;

	public static final String INVENTORY_SERVICE = "Inventory";
	public static final String DEVICE_SERVICE = "Device";

	public static class PortDetails {
		public String switchIPAddress = null;
		public String password = null;
		public String userName = null;
		public String portNumber = null;
		public String portId = null;
		public String switchId = null;
		public boolean switchOption82 = false;

		public PortDetails() {
		}
	}

	private static Inventory getInventoryStub() throws javax.xml.rpc.ServiceException,SMSAPIException {
		if (inventoryStub != null) return inventoryStub;
		try {
			InventoryServiceLocator loc = new InventoryServiceLocator();
			loc.setEngine(getEngine());
			inventoryStub = loc.getInventory(getURL(INVENTORY_SERVICE));
			((InventorySoapBindingStub)inventoryStub).setTimeout(getTimeout());
		}
		catch (org.apache.axis.ConfigurationException ex) {
			logger.error("Unable to get the engine configuration for Axis",ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
		return inventoryStub;
	}

	private static Device getDeviceStub() throws javax.xml.rpc.ServiceException ,SMSAPIException{
		if (deviceStub != null) return deviceStub;
		DeviceServiceLocator loc = new DeviceServiceLocator();
		deviceStub = loc.getDevice(getURL(DEVICE_SERVICE));
		((DeviceSoapBindingStub)deviceStub).setTimeout(getTimeout());
		return deviceStub;
	}

	public static PortDetails  getInventoryForSubscriber(String sessionID,String acctNo) 
		throws SMSAPIException {
		try {
			Inventory stub = getInventoryStub();
			PortInfo info = stub.queryPortFromAccount(getSecurityToken(sessionID),acctNo);
			if (info == null) {
				logger.error("Unable to get port information for account " + acctNo);
				return null;
				//throw new SMSAPIException(SMSAPIException.NO_PORT_INFO);
			}
			SwitchInfo tmp = new SwitchInfo();
			tmp.setId(info.getSwitchId());
			SwitchInfo[] switchInfo = stub.query(getSecurityToken(sessionID),tmp);
			if (switchInfo == null || switchInfo.length != 1) {
				logger.error("Unable to get device information for device id " + info.getSwitchId());
				throw new SMSAPIException(SMSAPIException.NO_PORT_INFO);
			}
			logger.debug("Retrieve connection details for the switch ");
			ConnectInfo connInfo = new ConnectInfo();
			connInfo.setId(switchInfo[0].getConnectionId());
			ConnectInfo[] retConns = stub.query(getSecurityToken(sessionID),connInfo);
			if (retConns == null || retConns.length != 1) {
				logger.error("Unable to get connection  information for connection id " + switchInfo[0].getConnectionId());
				throw new SMSAPIException(SMSAPIException.NO_PORT_INFO);
			}

			PortDetails result = new PortDetails();
			result.portNumber = info.getPortInterface();
			result.portId = info.getId();
			result.switchId = info.getSwitchId();
			result.switchIPAddress = switchInfo[0].getIpAddress();
			if (switchInfo[0].getModel().equals("3500XL")) {
				result.switchOption82 =  false;
			}
			else {
				logger.debug("Switch Model is " + switchInfo[0].getModel());
				result.switchOption82 = true;
			}
			result.userName = retConns[0].getUserName();
			result.password = retConns[0].getPassword();
			return result;
		}
		catch (IllegalArgumentAPIException ex) {
			logger.error("Unable to execute getInventoryForSubscriber ",ex);
			throw new SMSAPIException(SMSAPIException.INSUFFICIENT_DATA_FOR_SERVICE);
		}
		catch (AuthenticationAPIException ex) {
			logger.error("Unable to execute getInventoryForSubscriber ",ex);
			throw new SMSAPIException(SMSAPIException.INVALID_AUTH);
		}
		catch (InventoryAPIException ex) {
			logger.error("Unable to execute getInventoryForSubscriber ",ex);
			throw new SMSAPIException(ex.getMessage());
		}
		catch (AxisFault ex) {
			Throwable e = ex.getCause();
			if (e != null) {
				logger.error("Unable to execute getInventoryForSubscriber ",e);
				throw new SMSAPIException(e.getMessage());
			}
			else {
				logger.error("Unable to execute getInventoryForSubscriber " + ex.getFaultString());
				throw new SMSAPIException(ex.getFaultString());
			}
		}
		catch (Exception ex) {
			logger.error("Unable to execute getInventoryForSubscriber ",ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
	}

	public static String  getEdgeRouterForSwitch(String sessionID, String switchId,String switchIPAddress)
		throws SMSAPIException {
		try {
			Inventory stub = getInventoryStub();
			SwitchInfo info = new SwitchInfo();
			info.setId(switchId);
			info.setIpAddress(switchIPAddress);

			EdgeDeviceInfo edgeInfo = stub.getEdgeDevice(
				getSecurityToken(sessionID),info);
			if (edgeInfo == null) {
				logger.error("Unable to get edge router for switch " + switchIPAddress);
				return null;
			}
			return edgeInfo.getIpAddress();
		}
		catch (IllegalArgumentAPIException ex) {
			logger.error("Unable to get edge router ",ex);
			throw new SMSAPIException(SMSAPIException.INSUFFICIENT_DATA_FOR_SERVICE);
		}
		catch (AuthenticationAPIException ex) {
			logger.error("Unable to get edge router ",ex);
			throw new SMSAPIException(SMSAPIException.INVALID_AUTH);
		}
		catch (InventoryAPIException ex) {
			logger.error("Unable to get edge router.Received InventoryAPIException ",ex);
			throw new SMSAPIException(ex.getMessage());
		}
		catch (AxisFault ex) {
			Throwable e = ex.getCause();
			if (e != null) {
				logger.error("Unable to get edge router ",e);
				throw new SMSAPIException(e.getMessage());
			}
			else {
				logger.error("Unable to get edge router "+ ex.getFaultString());
				throw new SMSAPIException(ex.getFaultString());
			}
		}
		catch (Exception ex) {
			logger.error("Unable to get edge router ",ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
	}

	public static String getCPEIPAddress(String sessionID,String acctNo) throws SMSAPIException {
		try {
			Inventory stub = getInventoryStub();
			return stub.getCPEAddressForSubscriber(
				getSecurityToken(sessionID),acctNo);
		}
		catch (IllegalArgumentAPIException ex) {
			logger.error("Unable to get cpe address ",ex);
			throw new SMSAPIException(SMSAPIException.INSUFFICIENT_DATA_FOR_SERVICE);
		}
		catch (AuthenticationAPIException ex) {
			logger.error("Unable to get cpe address ",ex);
			throw new SMSAPIException(SMSAPIException.INVALID_AUTH);
		}
		catch (InventoryAPIException ex) {
			logger.error("Unable to get cpe address.Received InventoryAPIException ",ex);
			throw new SMSAPIException(ex.getMessage());
		}
		catch (AxisFault ex) {
			Throwable e = ex.getCause();
			if (e != null) {
				logger.error("Unable to get cpe address ",e);
				throw new SMSAPIException(e.getMessage());
			}
			else {
				logger.error("Unable to get cpe address "+ ex.getFaultString());
				throw new SMSAPIException(ex.getFaultString());
			}
		}
		catch (Exception ex) {
			logger.error("Unable to get cpe address ",ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
	}

	public static SwitchInfo[] querySwitchesForAddr(String sessionID,AddressInfo[] addr) throws SMSAPIException {
		try {
			String[] ids = new String[addr.length];
			for (int i = 0; i < addr.length; i++) {
				ids[i] = addr[i].getSwitchId();
			}
			logger.debug("querySwithcesForAddr " + ids);
			SwitchInfo[] output = getInventoryStub().querySwitchByIds(getSecurityToken(sessionID),ids);
			return output;
		}
		catch (IllegalArgumentAPIException ex) {
			logger.error("Unable to query switch for address ",ex);
			throw new SMSAPIException(SMSAPIException.INSUFFICIENT_DATA_FOR_SERVICE);
		}
		catch (AuthenticationAPIException ex) {
			logger.error("Unable to query switch for address ",ex);
			throw new SMSAPIException(SMSAPIException.INVALID_AUTH);
		}
		catch (InventoryAPIException ex) {
			logger.error("Unable to query switch for address ",ex);
			throw new SMSAPIException(ex.getMessage());
		}
		catch (AxisFault ex) {
			Throwable e = ex.getCause();
			if (e != null) {
				logger.error("Unable to query switch for address ",e);
				throw new SMSAPIException(e.getMessage());
			}
			else {
				logger.error("Unable to query switch for address ",ex);
				throw new SMSAPIException(ex.getFaultString());
			}
		}
		catch (Exception ex) {
			logger.error("Unable to query switch for address ",ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
	}
}

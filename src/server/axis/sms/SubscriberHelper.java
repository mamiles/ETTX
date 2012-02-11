
package com.cisco.ettx.admin.server.axis.sms;

import java.util.Hashtable;
import java.util.Vector;
import java.util.List;
import org.apache.log4j.Logger;
import java.util.Arrays;
import com.cisco.sesm.sms.types.*;
import com.cisco.sesm.sms.exceptions.api.*;
import com.cisco.sesm.sms.api.*;
import org.apache.axis.AxisFault;

//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : SubscriberHelper.java
// Desc : Class which performs SMS queries
//**************************************************


public class SubscriberHelper extends SMSAPI {
	private static Logger logger = Logger.getLogger(SubscriberHelper.class);
	private static Subscriber subsStub = null;
	public static final String SUBSCRIBER_SERVICE = "Subscriber";

	private static Subscriber getStub()
		throws javax.xml.rpc.ServiceException,SMSAPIException {
		if (subsStub != null) return subsStub;
		try {
			SubscriberServiceLocator loc = new SubscriberServiceLocator();
			loc.setEngine(getEngine());
			subsStub = loc.getSubscriber(getURL(SUBSCRIBER_SERVICE));
			((SubscriberSoapBindingStub)subsStub).setTimeout(getTimeout());
		}
		catch (org.apache.axis.ConfigurationException ex) {
			logger.error("Unable to get the engine configuration for Axis",ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
		return subsStub;
	}


	public static SubscriberInfo[] querySubscribers(String sessionID, String filter)
		throws SMSAPIException {
		try {
			Subscriber stub = getStub();
			return stub.query(getSecurityToken(sessionID),filter);
		}
		catch (IllegalArgumentAPIException ex) {
			logger.error("Unable to query subscribers ",ex);
			throw new SMSAPIException(SMSAPIException.INSUFFICIENT_DATA_FOR_SERVICE);
		}
		catch (AuthenticationAPIException ex) {
			logger.error("Unable to query subscribers ",ex);
			throw new SMSAPIException(SMSAPIException.INVALID_AUTH);
		}
		catch (SubscriberAPIException ex) {
			logger.error("Unable to query subscribers ",ex);
			throw new SMSAPIException(ex.getMessage());
		}
		catch (AxisFault ex) {
			Throwable e = ex.getCause();
			if (e != null) {
				logger.error("Unable to query subscribers ",e);
				throw new SMSAPIException(e.getMessage());
			}
			else {
				logger.error("Unable to query subscribers "+ ex.getFaultString());
				throw new SMSAPIException(ex.getFaultString());
			}
		}
		catch (Exception ex) {
			logger.error("Unable to query subscribers ",ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
	}

	public static SubscriberInfo[] querySubscribers(String sessionID,
		SubscriberInfo info)
		throws SMSAPIException {
		try {
			Subscriber stub = getStub();
			return stub.query(getSecurityToken(sessionID),info);
		}
		catch (IllegalArgumentAPIException ex) {
			logger.error("Unable to query subscribers ",ex);
			throw new SMSAPIException(SMSAPIException.INSUFFICIENT_DATA_FOR_SERVICE);
		}
		catch (AuthenticationAPIException ex) {
			logger.error("Unable to query subscribers ",ex);
			throw new SMSAPIException(SMSAPIException.INVALID_AUTH);
		}
		catch (SubscriberAPIException ex) {
			logger.error("Unable to query subscribers ",ex);
			throw new SMSAPIException(ex.getMessage());
		}
		catch (AxisFault ex) {
			Throwable e = ex.getCause();
			if (e != null) {
				logger.error("Unable to query subscribers ",e);
				throw new SMSAPIException(e.getMessage());
			}
			else {
				logger.error("Unable to query subscribers "+ ex.getFaultString());
				throw new SMSAPIException(ex.getFaultString());
			}
		}
		catch (Exception ex) {
			logger.error("Unable to query subscribers ",ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
	}

	public static SubscriberInfo[] findSubsByService(String sessionID, String service)
		throws SMSAPIException {
		try {
			Subscriber stub = getStub();
			return stub.findSubscribersByService(getSecurityToken(sessionID),service);
		}
		catch (IllegalArgumentAPIException ex) {
			logger.error("Unable to find subscribers for service ",ex);
			throw new SMSAPIException(SMSAPIException.INSUFFICIENT_DATA_FOR_SERVICE);
		}
		catch (AuthenticationAPIException ex) {
			logger.error("Unable to find subscribers for service ",ex);
			throw new SMSAPIException(SMSAPIException.INVALID_AUTH);
		}
		catch (SubscriberAPIException ex) {
			logger.error("Unable to find subscribers for service ",ex);
			throw new SMSAPIException(ex.getMessage());
		}
		catch (AxisFault ex) {
			Throwable e = ex.getCause();
			if (e != null) {
				logger.error("Unable to find subscribers for service ",e);
				throw new SMSAPIException(e.getMessage());
			}
			else {
				logger.error("Unable to find subscribers for service " + ex.getFaultString());
				throw new SMSAPIException(ex.getFaultString());
			}
		}
		catch (Exception ex) {
			logger.error("Unable to find subscribers for service ",ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
	}

	public static SubscriberInfo[] findSubsBySwitch(String sessionID, String hostName)
		throws SMSAPIException {
		try {
			Subscriber stub = getStub();
			SwitchInfo info = new SwitchInfo();
			info.setIpAddress(hostName);
			return stub.findSubscribersBySwitch(getSecurityToken(sessionID),info);
		}
		catch (IllegalArgumentAPIException ex) {
			logger.error("Unable to find subscribers for switch ",ex);
			throw new SMSAPIException(SMSAPIException.INSUFFICIENT_DATA_FOR_SERVICE);
		}
		catch (AuthenticationAPIException ex) {
			logger.error("Unable to find subscribers for switch ",ex);
			throw new SMSAPIException(SMSAPIException.INVALID_AUTH);
		}
		catch (SubscriberAPIException ex) {
			logger.error("Unable to find subscribers for switch ",ex);
			throw new SMSAPIException(ex.getMessage());
		}
		catch (AxisFault ex) {
			Throwable e = ex.getCause();
			if (e != null) {
				logger.error("Unable to find subscribers for switch ",e);
				throw new SMSAPIException(e.getMessage());
			}
			else {
				logger.error("Unable to find subscribers for switch "+ ex.getFaultString());
				throw new SMSAPIException(ex.getFaultString());
			}
		}
		catch (Exception ex) {
			logger.error("Unable to find subscribers for switch ",ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
	}


	public static SubscriberInfo getSubscriberInfo(String sessionID, String subsId) throws SMSAPIException {
		try {
			Subscriber stub = getStub();
			return stub.getSubscriberInfo(getSecurityToken(sessionID),subsId);
		}
		catch (IllegalArgumentAPIException ex) {
			logger.error("Unable to get subscriber info ",ex);
			throw new SMSAPIException(SMSAPIException.INSUFFICIENT_DATA_FOR_SERVICE);
		}
		catch (AuthenticationAPIException ex) {
			logger.error("Unable to get subscriber info ",ex);
			throw new SMSAPIException(SMSAPIException.INVALID_AUTH);
		}
		catch (SubscriberAPIException ex) {
			logger.error("Unable to get subscriber info ",ex);
			throw new SMSAPIException(ex.getMessage());
		}
		catch (AxisFault ex) {
			Throwable e = ex.getCause();
			if (e != null) {
				logger.error("Unable to get subscriber info ",e);
				throw new SMSAPIException(e.getMessage());
			}
			else {
				logger.error("Unable to get subscriber info "+ ex.getFaultString());
				throw new SMSAPIException(ex.getFaultString());
			}
		}
		catch (Exception ex) {
			logger.error("Unable to get subscriber info ",ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
	}

	public static SubscriberInfo modifySubscriberInfo(String sessionID, SubscriberInfo subsInfo) throws SMSAPIException {
		try {
			Subscriber stub = getStub();
			return stub.modify(getSecurityToken(sessionID),subsInfo);
		}
		catch (IllegalArgumentAPIException ex) {
			logger.error("Unable to modify subscriber info ",ex);
			throw new SMSAPIException(SMSAPIException.INSUFFICIENT_DATA_FOR_SERVICE);
		}
		catch (AuthenticationAPIException ex) {
			logger.error("Unable to modify subscriber info ",ex);
			throw new SMSAPIException(SMSAPIException.INVALID_AUTH);
		}
		catch (SubscriberAPIException ex) {
			logger.error("Unable to modify subscriber info ",ex);
			throw new SMSAPIException(ex.getMessage());
		}
		catch (AxisFault ex) {
			Throwable e = ex.getCause();
			if (e != null) {
				logger.error("Unable to modify subscriber info ",e);
				throw new SMSAPIException(e.getMessage());
			}
			else {
				logger.error("Unable to modify subscriber info "+ ex.getFaultString());
				throw new SMSAPIException(ex.getFaultString());
			}
		}
		catch (Exception ex) {
			logger.error("Unable to modify subscriber info ",ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
	}

	public static SubscriberInfo createSubscriberInfo(String sessionID, SubscriberInfo subsInfo) throws SMSAPIException {
		try {
			Subscriber stub = getStub();
			return stub.create(getSecurityToken(sessionID),subsInfo);
		}
		catch (IllegalArgumentAPIException ex) {
			logger.error("Unable to create subscriber info ",ex);
			throw new SMSAPIException(SMSAPIException.INSUFFICIENT_DATA_FOR_SERVICE);
		}
		catch (AuthenticationAPIException ex) {
			logger.error("Unable to create subscriber info ",ex);
			throw new SMSAPIException(SMSAPIException.INVALID_AUTH);
		}
		catch (SubscriberAPIException ex) {
			logger.error("Unable to create subscriber info ",ex);
			throw new SMSAPIException(ex.getMessage());
		}
		catch (AxisFault ex) {
			Throwable e = ex.getCause();
			if (e != null) {
				logger.error("Unable to create subscriber info ",e);
				throw new SMSAPIException(e.getMessage());
			}
			else {
				logger.error("Unable to create subscriber info "+ ex.getFaultString());
				throw new SMSAPIException(ex.getFaultString());
			}
		}
		catch (Exception ex) {
			logger.error("Unable to create subscriber info ",ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
	}

	public static void deleteSubscriber(String sessionID, String subsId)
		throws SMSAPIException {
		try {
			Subscriber stub = getStub();
			stub.delete(getSecurityToken(sessionID),subsId);
		}
		catch (IllegalArgumentAPIException ex) {
			logger.error("Unable to delete subscriber " + subsId,ex);
			throw new SMSAPIException(SMSAPIException.INSUFFICIENT_DATA_FOR_SERVICE);
		}
		catch (AuthenticationAPIException ex) {
			logger.error("Unable to delete subscriber " + subsId,ex);
			throw new SMSAPIException(SMSAPIException.INVALID_AUTH);
		}
		catch (SubscriberAPIException ex) {
			logger.error("Unable to delete subscriber " + subsId,ex);
			throw new SMSAPIException(ex.getMessage());
		}
		catch (AxisFault ex) {
			Throwable e = ex.getCause();
			if (e != null) {
				logger.error("Unable to delete subscriber " + subsId,e);
				throw new SMSAPIException(e.getMessage());
			}
			else {
				logger.error("Unable to delete subscriber " + subsId + " " + ex.getFaultString());
				throw new SMSAPIException(ex.getFaultString());
			}
		}
		catch (Exception ex) {
			logger.error("Unable to delete subscriber " + subsId,ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
	}

	public static DeviceInfo registerSubscriberDevice(String sessionID,
	                                           String subscriberId,
											   DeviceInfo devInfo)
											   throws SMSAPIException {
		try {
			Subscriber stub = getStub();
			return stub.registerSubscriberDevice(getSecurityToken(sessionID),subscriberId,devInfo);
		}
		catch (IllegalArgumentAPIException ex) {
			logger.error("Unable to register DeviceInfo for subscriber ",ex);
			throw new SMSAPIException(SMSAPIException.INSUFFICIENT_DATA_FOR_SERVICE);
		}
		catch (AuthenticationAPIException ex) {
			logger.error("Unable to register DeviceInfo for subscriber ",ex);
			throw new SMSAPIException(SMSAPIException.INVALID_AUTH);
		}
		catch (SubscriberAPIException ex) {
			logger.error("Unable to register DeviceInfo for subscriber ",ex);
			throw new SMSAPIException(ex.getMessage());
		}
		catch (AxisFault ex) {
			Throwable e = ex.getCause();
			if (e != null) {
				logger.error("Unable to register DeviceInfo for subscriber ",e);
				throw new SMSAPIException(e.getMessage());
			}
			else {
				logger.error("Unable to register DeviceInfo for subscriber "+ ex.getFaultString());
				throw new SMSAPIException(ex.getFaultString());
			}
		}
		catch (Exception ex) {
			logger.error("Unable to register DeviceInfo for subscriber ",ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
	}

	public static void unregisterSubscriberDevice(String sessionID,
	                                           String subscriberId,
											   DeviceInfo devInfo)
											   throws SMSAPIException {
		try {
			Subscriber stub = getStub();
			stub.unregisterSubscriberDevice(getSecurityToken(sessionID),subscriberId,devInfo);
		}
		catch (IllegalArgumentAPIException ex) {
			logger.error("Unable to unregister DeviceInfo for subscriber ",ex);
			throw new SMSAPIException(SMSAPIException.INSUFFICIENT_DATA_FOR_SERVICE);
		}
		catch (AuthenticationAPIException ex) {
			logger.error("Unable to unregister DeviceInfo for subscriber ",ex);
			throw new SMSAPIException(SMSAPIException.INVALID_AUTH);
		}
		catch (SubscriberAPIException ex) {
			logger.error("Unable to unregister DeviceInfo for subscriber ",ex);
			throw new SMSAPIException(ex.getMessage());
		}
		catch (AxisFault ex) {
			Throwable e = ex.getCause();
			if (e != null) {
				logger.error("Unable to unregister DeviceInfo for subscriber ",e);
				throw new SMSAPIException(e.getMessage());
			}
			else {
				logger.error("Unable to unregister DeviceInfo for subscriber "+ ex.getFaultString());
				throw new SMSAPIException(ex.getFaultString());
			}
		}
		catch (Exception ex) {
			logger.error("Unable to unregister DeviceInfo for subscriber ",ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
	}

	public static void modifySubscriberDevice(String sessionID,
	              String subscriberId,DeviceInfo devInfo)
			throws SMSAPIException {
		try {
			Subscriber stub = getStub();
			stub.modifySubscriberDevice(getSecurityToken(sessionID),subscriberId,devInfo);
		}
		catch (IllegalArgumentAPIException ex) {
			logger.error("Unable to unregister DeviceInfo for subscriber ",ex);
			throw new SMSAPIException(SMSAPIException.INSUFFICIENT_DATA_FOR_SERVICE);
		}
		catch (AuthenticationAPIException ex) {
			logger.error("Unable to unregister DeviceInfo for subscriber ",ex);
			throw new SMSAPIException(SMSAPIException.INVALID_AUTH);
		}
		catch (SubscriberAPIException ex) {
			logger.error("Unable to unregister DeviceInfo for subscriber ",ex);
			throw new SMSAPIException(ex.getMessage());
		}
		catch (AxisFault ex) {
			Throwable e = ex.getCause();
			if (e != null) {
				logger.error("Unable to unregister DeviceInfo for subscriber ",e);
				throw new SMSAPIException(e.getMessage());
			}
			else {
				logger.error("Unable to unregister DeviceInfo for subscriber "+ ex.getFaultString());
				throw new SMSAPIException(ex.getFaultString());
			}
		}
		catch (Exception ex) {
			logger.error("Unable to unregister DeviceInfo for subscriber ",ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
	}

/*
	public static void activateSubscriberDevice(String sessionID,
	              String subscriberId, DeviceInfo devInfo) throws SMSAPIException {
		try {
			Subscriber stub = getStub();
			stub.activateSubscriberDevice(getSecurityToken(sessionID),subscriberId,devInfo);
		}
		catch (IllegalArgumentAPIException ex) {
			logger.error("Unable to unregister DeviceInfo for subscriber ",ex);
			throw new SMSAPIException(SMSAPIException.INSUFFICIENT_DATA_FOR_SERVICE);
		}
		catch (AuthenticationAPIException ex) {
			logger.error("Unable to unregister DeviceInfo for subscriber ",ex);
			throw new SMSAPIException(SMSAPIException.INVALID_AUTH);
		}
		catch (SubscriberAPIException ex) {
			logger.error("Unable to unregister DeviceInfo for subscriber ",ex);
			throw new SMSAPIException(ex.getMessage());
		}
		catch (AxisFault ex) {
			Throwable e = ex.getCause();
			if (e != null) {
				logger.error("Unable to unregister DeviceInfo for subscriber ",e);
				throw new SMSAPIException(e.getMessage());
			}
			else {
				logger.error("Unable to unregister DeviceInfo for subscriber "+ ex.getFaultString());
				throw new SMSAPIException(ex.getFaultString());
			}
		}
		catch (Exception ex) {
			logger.error("Unable to unregister DeviceInfo for subscriber ",ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
	}

	public static void deactivateSubscriberDevice(String sessionID,
	              String subscriberId, DeviceInfo devInfo) throws SMSAPIException {
		try {
			Subscriber stub = getStub();
			stub.deactivateSubscriberDevice(getSecurityToken(sessionID),subscriberId,devInfo);
		}
		catch (IllegalArgumentAPIException ex) {
			logger.error("Unable to unregister DeviceInfo for subscriber ",ex);
			throw new SMSAPIException(SMSAPIException.INSUFFICIENT_DATA_FOR_SERVICE);
		}
		catch (AuthenticationAPIException ex) {
			logger.error("Unable to unregister DeviceInfo for subscriber ",ex);
			throw new SMSAPIException(SMSAPIException.INVALID_AUTH);
		}
		catch (SubscriberAPIException ex) {
			logger.error("Unable to unregister DeviceInfo for subscriber ",ex);
			throw new SMSAPIException(ex.getMessage());
		}
		catch (AxisFault ex) {
			Throwable e = ex.getCause();
			if (e != null) {
				logger.error("Unable to unregister DeviceInfo for subscriber ",e);
				throw new SMSAPIException(e.getMessage());
			}
			else {
				logger.error("Unable to unregister DeviceInfo for subscriber "+ ex.getFaultString());
				throw new SMSAPIException(ex.getFaultString());
			}
		}
		catch (Exception ex) {
			logger.error("Unable to unregister DeviceInfo for subscriber ",ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
	}
*/

	public static SubscriberInfo[] querySubscribersByDevice(
			String sessionID,
	              DeviceInfo devInfo) throws SMSAPIException {
		try {
			Subscriber stub = getStub();
			return stub.querySubscriberByDevice(getSecurityToken(sessionID),devInfo);
		}
		catch (IllegalArgumentAPIException ex) {
			logger.error("Unable to query subscribers for DeviceInfo ",ex);
			throw new SMSAPIException(SMSAPIException.INSUFFICIENT_DATA_FOR_SERVICE);
		}
		catch (AuthenticationAPIException ex) {
			logger.error("Unable to query subscribers for DeviceInfo ",ex);
			throw new SMSAPIException(SMSAPIException.INVALID_AUTH);
		}
		catch (SubscriberAPIException ex) {
			logger.error("Unable to query subscribers for DeviceInfo ",ex);
			throw new SMSAPIException(ex.getMessage());
		}
		catch (AxisFault ex) {
			Throwable e = ex.getCause();
			if (e != null) {
				logger.error("Unable to query subscribers for DeviceInfo ",ex);
				throw new SMSAPIException(e.getMessage());
			}
			else {
				logger.error("Unable to query subscribers for DeviceInfo " + ex.getFaultString());
				throw new SMSAPIException(ex.getFaultString());
			}
		}
		catch (Exception ex) {
			logger.error("Unable to query subscribers for DeviceInfo " , ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
	}

}





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
// File : SubscriptionsHelper.java
// Desc : Class which performs SMS queries
//**************************************************


public class SubscriptionsHelper extends SMSAPI {
	private static Logger logger = Logger.getLogger(SubscriptionsService.class);
	private static Subscriptions subsStub = null;
	private static Provisioning provStub = null;
	public static final String SUBSCRIPTIONS_SERVICE = "Subscriptions";
	public static final String PROVISIONING_SERVICE = "Provisioning";

	private static Subscriptions getSubscriptionsStub() 
		throws javax.xml.rpc.ServiceException,SMSAPIException {
		if (subsStub != null) return subsStub;
		try {
			SubscriptionsServiceLocator loc = new SubscriptionsServiceLocator();
			loc.setEngine(getEngine());
			subsStub = loc.getSubscriptions(getURL(SUBSCRIPTIONS_SERVICE));
			((SubscriptionsSoapBindingStub)subsStub).setTimeout(getTimeout());
		}
		catch (org.apache.axis.ConfigurationException ex) {
			logger.error("Unable to get the engine configuration for Axis",ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
		return subsStub;
	}

	private static Provisioning getProvisioningStub() 
		throws javax.xml.rpc.ServiceException,SMSAPIException {
		if (provStub != null) return provStub;
		try {
			ProvisioningServiceLocator loc = new ProvisioningServiceLocator();
			loc.setEngine(getEngine());
			provStub = loc.getProvisioning(getURL(PROVISIONING_SERVICE));
			((ProvisioningSoapBindingStub)provStub).setTimeout(getTimeout());
		}
		catch (org.apache.axis.ConfigurationException ex) {
			logger.error("Unable to get the engine configuration for Axis",ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
		return provStub;
	}


	public static void activateService(String sessionID, String subsId, String serviceName)
		throws SMSAPIException {
		try {
			Provisioning stub = getProvisioningStub();
			stub.activateService(getSecurityToken(sessionID),subsId,new String[] {serviceName});
		}
		catch (IllegalArgumentAPIException ex) {
			logger.error("Unable to activate service " + subsId,ex);
			throw new SMSAPIException(SMSAPIException.INSUFFICIENT_DATA_FOR_SERVICE);
		}
		catch (AuthenticationAPIException ex) {
			logger.error("Unable to activate service " + subsId,ex);
			throw new SMSAPIException(SMSAPIException.INVALID_AUTH);
		}
		catch (ProvisioningAPIException ex) {
			logger.error("Unable to activate service " + subsId,ex);
			throw new SMSAPIException(ex.getMessage());
		}
		catch (AxisFault ex) {
			Throwable e = ex.getCause();
			if (e != null) {
				logger.error("Unable to activate service " + subsId,e);
				throw new SMSAPIException(e.getMessage());
			}
			else {
				logger.error("Unable to activate service " + subsId + " " + ex.getFaultString());
				throw new SMSAPIException(ex.getFaultString());
			}
		}
		catch (Exception ex) {
			logger.error("Unable to activate service " + subsId,ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
	}

	public static void deactivateService(String sessionID, String subsId, String serviceName)
		throws SMSAPIException {
		try {
			Provisioning stub = getProvisioningStub();
			stub.deactivateService(getSecurityToken(sessionID),subsId,new String[] {serviceName});
		}
		catch (IllegalArgumentAPIException ex) {
			logger.error("Unable to deactivate service " + subsId,ex);
			throw new SMSAPIException(SMSAPIException.INSUFFICIENT_DATA_FOR_SERVICE);
		}
		catch (AuthenticationAPIException ex) {
			logger.error("Unable to deactivate service " + subsId,ex);
			throw new SMSAPIException(SMSAPIException.INVALID_AUTH);
		}
		catch (ProvisioningAPIException ex) {
			logger.error("Unable to deactivate service " + subsId,ex);
			throw new SMSAPIException(ex.getMessage());
		}
		catch (AxisFault ex) {
			Throwable e = ex.getCause();
			if (e != null) {
				logger.error("Unable to deactivate service " + subsId,e);
				throw new SMSAPIException(e.getMessage());
			}
			else {
				logger.error("Unable to deactivate service " + subsId + " " + ex.getFaultString());
				throw new SMSAPIException(ex.getFaultString());
			}
		}
		catch (Exception ex) {
			logger.error("Unable to deactivate service " + subsId,ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
	}

	public static void subscribeService(String sessionID, String subsId, ServiceFeaturesListInfo serviceInfo) 
		throws SMSAPIException {
		try {
			Subscriptions stub = getSubscriptionsStub();
			ServiceFeaturesListInfo[] slfo = new ServiceFeaturesListInfo[1];
			slfo[0] = serviceInfo;
			stub.subscribe(getSecurityToken(sessionID),subsId,slfo);
		}
		catch (IllegalArgumentAPIException ex) {
			logger.error("Unable to subscribe subscriber " + subsId,ex);
			throw new SMSAPIException(SMSAPIException.INSUFFICIENT_DATA_FOR_SERVICE);
		}
		catch (AuthenticationAPIException ex) {
			logger.error("Unable to subscribe subscriber " + subsId,ex);
			throw new SMSAPIException(SMSAPIException.INVALID_AUTH);
		}
		catch (SubscriptionsAPIException ex) {
			logger.error("Unable to subscribe subscriber " + subsId,ex);
			throw new SMSAPIException(ex.getMessage());
		}
		catch (AxisFault ex) {
			Throwable e = ex.getCause();
			if (e != null) {
				logger.error("Unable to subscribe subscriber " + subsId,e);
				throw new SMSAPIException(e.getMessage());
			}
			else {
				logger.error("Unable to subscribe subscriber " + subsId + " " + ex.getFaultString());
				throw new SMSAPIException(ex.getFaultString());
			}
		}
		catch (Exception ex) {
			logger.error("Unable to subscribe subscriber " + subsId,ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
	}

	public static void unsubscribeService(String sessionID, String subsId, String serviceName)
		throws SMSAPIException {
		try {
			Subscriptions stub = getSubscriptionsStub();
			stub.unsubscribe(getSecurityToken(sessionID),subsId,new String[]{serviceName});
		}
		catch (IllegalArgumentAPIException ex) {
			logger.error("Unable to unsubscribe subscriber " + subsId,ex);
			throw new SMSAPIException(SMSAPIException.INSUFFICIENT_DATA_FOR_SERVICE);
		}
		catch (AuthenticationAPIException ex) {
			logger.error("Unable to unsubscribe subscriber " + subsId,ex);
			throw new SMSAPIException(SMSAPIException.INVALID_AUTH);
		}
		catch (SubscriptionsAPIException ex) {
			logger.error("Unable to unsubscribe subscriber " + subsId,ex);
			throw new SMSAPIException(ex.getMessage());
		}
		catch (AxisFault ex) {
			Throwable e = ex.getCause();
			if (e != null) {
				logger.error("Unable to unsubscribe subscriber " + subsId,e);
				throw new SMSAPIException(e.getMessage());
			}
			else {
				logger.error("Unable to unsubscribe subscriber " + subsId + " " + ex.getFaultString());
				throw new SMSAPIException(ex.getFaultString());
			}
		}
		catch (Exception ex) {
			logger.error("Unable to unsubscribe subscriber " + subsId,ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
	}

	public static ServiceInfo[] querySubscribedServices(String sessionID, String subsId)
		throws SMSAPIException {
		try {
			Subscriptions stub = getSubscriptionsStub();
			return stub.querySubscribedServices(getSecurityToken(sessionID),subsId);
		}
		catch (IllegalArgumentAPIException ex) {
			logger.error("Unable to unsubscribe subscriber " + subsId,ex);
			throw new SMSAPIException(SMSAPIException.INSUFFICIENT_DATA_FOR_SERVICE);
		}
		catch (AuthenticationAPIException ex) {
			logger.error("Unable to unsubscribe subscriber " + subsId,ex);
			throw new SMSAPIException(SMSAPIException.INVALID_AUTH);
		}
		catch (SubscriptionsAPIException ex) {
			logger.error("Unable to unsubscribe subscriber " + subsId,ex);
			throw new SMSAPIException(ex.getMessage());
		}
		catch (AxisFault ex) {
			Throwable e = ex.getCause();
			if (e != null) {
				logger.error("Unable to unsubscribe subscriber " + subsId,e);
				throw new SMSAPIException(e.getMessage());
			}
			else {
				logger.error("Unable to unsubscribe subscriber " + subsId + " " + ex.getFaultString());
				throw new SMSAPIException(ex.getFaultString());
			}
		}
		catch (Exception ex) {
			logger.error("Unable to unsubscribe subscriber " + subsId,ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
	}
}

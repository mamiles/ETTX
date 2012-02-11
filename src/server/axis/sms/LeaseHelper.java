
package com.cisco.ettx.admin.server.axis.sms;

import java.util.HashMap;
import java.util.Vector;
import java.util.Calendar;
import org.apache.log4j.Logger;
import com.cisco.sesm.sms.types.*;
import com.cisco.sesm.sms.exceptions.api.*;
import com.cisco.sesm.sms.api.*;
import org.apache.axis.AxisFault;

//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : LeaseHelper.java
// Desc : Class which performs SMS queries
//**************************************************


public class LeaseHelper extends SMSAPI {
	private static Logger logger = Logger.getLogger(LeaseHelper.class);
	private static Lease leaseStub = null;
	public static final String LEASE_SERVICE = "Lease";

	private static Lease getLeaseStub() 
		throws javax.xml.rpc.ServiceException ,SMSAPIException{
		if (leaseStub != null) return leaseStub;
		try {
			LeaseServiceLocator loc = new LeaseServiceLocator();
			loc.setEngine(getEngine());
			leaseStub = loc.getLease(getURL(LEASE_SERVICE));
			((LeaseSoapBindingStub)leaseStub).setTimeout(getTimeout());
		}
		catch (org.apache.axis.ConfigurationException ex) {
			logger.error("Unable to get the engine configuration for Axis",ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
		return leaseStub;
	}


	public static LeaseInfo[] queryLeasesForSubs(String sessionID, SubscriberInfo subs,Calendar startTime,
		Calendar endTime) throws SMSAPIException {
		try {
			Lease leaseService = getLeaseStub();
			LeaseInfo[] leases = leaseService.queryLeaseFromSubscriber(getSecurityToken(sessionID),subs,startTime,endTime);
			return leases;
		}
		catch (IllegalArgumentAPIException ex) {
			logger.error("Unable to execute lease query ",ex);
			throw new SMSAPIException(SMSAPIException.INSUFFICIENT_DATA_FOR_SERVICE);
		}
		catch (AuthenticationAPIException ex) {
			logger.error("Unable to execute lease query ",ex);
			throw new SMSAPIException(SMSAPIException.INVALID_AUTH);
		}
		catch (LeaseAPIException ex) {
			logger.error("Unable to execute lease query ",ex);
			throw new SMSAPIException(ex.getMessage());
		}
		catch (AxisFault ex) {
			Throwable e = ex.getCause();
			if (e != null) {
				logger.error("Unable to execute lease query ",e);
				throw new SMSAPIException(e.getMessage());
			}
			else {
				logger.error("Unable to execute lease query ",ex);
				throw new SMSAPIException(ex.getFaultString());
			}
		}
		catch (Exception ex) {
			logger.error("Unable to execute lease query ",ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
	}

	public static LeaseInfo[] queryLeasesForIPAddr(String sessionID,String ipAddr, Calendar startTime,
		Calendar endTime) throws SMSAPIException {
		try {
			//perform query for subscriber
			Lease leaseService = getLeaseStub();
			LeaseInfo[] leases = leaseService.queryLeaseFromIpAddress(getSecurityToken(sessionID),ipAddr,startTime,endTime);
			return leases;
		}
		catch (IllegalArgumentAPIException ex) {
			logger.error("Unable to execute lease query ",ex);
			throw new SMSAPIException(SMSAPIException.INSUFFICIENT_DATA_FOR_SERVICE);
		}
		catch (AuthenticationAPIException ex) {
			logger.error("Unable to execute lease query ",ex);
			throw new SMSAPIException(SMSAPIException.INVALID_AUTH);
		}
		catch (LeaseAPIException ex) {
			logger.error("Unable to execute lease query ",ex);
			throw new SMSAPIException(ex.getMessage());
		}
		catch (AxisFault ex) {
			Throwable e = ex.getCause();
			if (e != null) {
				logger.error("Unable to execute lease query ",e);
				throw new SMSAPIException(e.getMessage());
			}
			else {
				logger.error("Unable to execute lease query ", ex);
				throw new SMSAPIException(ex.getFaultString());
			}
		}
		catch (Exception ex) {
			logger.error("Unable to execute lease query ",ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
	}

	public static SubscriberInfo[] querySubsForLeases(String sessionID,LeaseInfo[] leases) 
		throws SMSAPIException {
		try {
			//perform query for subscriber
			Lease leaseService = getLeaseStub();
			SubscriberInfo[] subs = leaseService.querySubscribersFromLeases(getSecurityToken(sessionID),leases);
			return subs;
		}
		catch (IllegalArgumentAPIException ex) {
			logger.error("Unable to execute lease query ",ex);
			throw new SMSAPIException(SMSAPIException.INSUFFICIENT_DATA_FOR_SERVICE);
		}
		catch (AuthenticationAPIException ex) {
			logger.error("Unable to execute lease query ",ex);
			throw new SMSAPIException(SMSAPIException.INVALID_AUTH);
		}
		catch (LeaseAPIException ex) {
			logger.error("Unable to execute lease query ",ex);
			throw new SMSAPIException(ex.getMessage());
		}
		catch (AxisFault ex) {
			Throwable e = ex.getCause();
			if (e != null) {
				logger.error("Unable to execute lease query ",e);
				throw new SMSAPIException(e.getMessage());
			}
			else {
				logger.error("Unable to execute lease query ", ex);
				throw new SMSAPIException(ex.getFaultString());
			}
		}
		catch (Exception ex) {
			logger.error("Unable to execute lease query ",ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
	}

	public static LeaseInfo[] getLeaseForSubscriber(String sessionID, SubscriberInfo subsInfo) throws SMSAPIException {
		try {
			Lease stub = getLeaseStub();
			LeaseInfo[] leases = stub.queryLeaseFromSubscriber(getSecurityToken(sessionID),subsInfo);
			return leases;
		}
		catch (IllegalArgumentAPIException ex) {
			logger.error("Unable to execute lease query ",ex);
			throw new SMSAPIException(SMSAPIException.INSUFFICIENT_DATA_FOR_SERVICE);
		}
		catch (AuthenticationAPIException ex) {
			logger.error("Unable to execute lease query ",ex);
			throw new SMSAPIException(SMSAPIException.INVALID_AUTH);
		}
		catch (LeaseAPIException ex) {
			logger.error("Unable to execute lease query ",ex);
			throw new SMSAPIException(ex.getMessage());
		}
		catch (AxisFault ex) {
			Throwable e = ex.getCause();
			if (e != null) {
				logger.error("Unable to execute lease query ",e);
				throw new SMSAPIException(e.getMessage());
			}
			else {
				logger.error("Unable to execute lease query ", ex);
				throw new SMSAPIException(ex.getFaultString());
			}
		}
		catch (Exception ex) {
			logger.error("Unable to execute lease query ",ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
	}

	public static String archiveIPHistory(String sessionID, String cnrAddress, Calendar startTime, Calendar endTime, String format, String outputfileName) throws SMSAPIException {
		try {
			DHCPServerInfo info = new DHCPServerInfo();
			info.setIpAddress(cnrAddress);
			Lease stub = getLeaseStub();
			return stub.archiveIPHistory(getSecurityToken(sessionID),info,startTime,endTime,format,outputfileName);
		}
		catch (IllegalArgumentAPIException ex) {
			logger.error("Unable to execute lease query ",ex);
			throw new SMSAPIException(SMSAPIException.INSUFFICIENT_DATA_FOR_SERVICE);
		}
		catch (AuthenticationAPIException ex) {
			logger.error("Unable to execute lease query ",ex);
			throw new SMSAPIException(SMSAPIException.INVALID_AUTH);
		}
		catch (LeaseAPIException ex) {
			logger.error("Unable to execute lease query ",ex);
			throw new SMSAPIException(ex.getMessage());
		}
		catch (AxisFault ex) {
			Throwable e = ex.getCause();
			if (e != null) {
				logger.error("Unable to execute lease query ",e);
				throw new SMSAPIException(e.getMessage());
			}
			else {
				logger.error("Unable to execute lease query ", ex);
				throw new SMSAPIException(ex.getFaultString());
			}
		}
		catch (Exception ex) {
			logger.error("Unable to execute lease query ",ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
	}
}

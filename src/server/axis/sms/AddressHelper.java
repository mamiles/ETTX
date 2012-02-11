
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
// File : AddressHelper.java
// Desc : Class which performs SMS queries
//**************************************************


public class AddressHelper extends SMSAPI {
	private static Logger logger = Logger.getLogger(AddressService.class);
	private static Address stub = null;
	public static final String ADDRESS_SERVICE = "Address";

	private static Address getStub() 
		throws javax.xml.rpc.ServiceException,SMSAPIException {
		if (stub != null) return stub;
		try {
			AddressServiceLocator loc = new AddressServiceLocator();
			loc.setEngine(getEngine());
			stub = loc.getAddress(getURL(ADDRESS_SERVICE));
			((AddressSoapBindingStub)stub).setTimeout(getTimeout());
		}
		catch (org.apache.axis.ConfigurationException ex) {
			logger.error("Unable to get the engine configuration for Axis",ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
		return stub;
	}


	public static AddressInfo[] queryPostalAddressMap(String sessionID, 
		AddressInfo info)
		throws SMSAPIException {
		try {
			Address stub = getStub();
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
		catch (AddressAPIException ex) {
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
}

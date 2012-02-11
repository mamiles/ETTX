

package com.cisco.ettx.admin.server.axis.sms;

import org.apache.log4j.Logger;
import com.cisco.sesm.sms.types.*;
import com.cisco.sesm.sms.exceptions.api.*;
import java.util.Vector;
import org.apache.axis.AxisFault;
import com.cisco.sesm.sms.api.*;

//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : ServiceHelper.java
// Desc : Class which performs SMS queries
//**************************************************


public class ServiceHelper extends SMSAPI {
	private static Logger logger = Logger.getLogger(ServiceHelper.class);
	private static Service serviceStub = null;

	public static final String SERVICE_SERVICE = "Service";

	private static Service getServiceStub() 
		throws javax.xml.rpc.ServiceException,SMSAPIException {
		if (serviceStub != null) return serviceStub;
		try {
			ServiceServiceLocator loc = new ServiceServiceLocator();
			loc.setEngine(getEngine());
			serviceStub = loc.getService(getURL(SERVICE_SERVICE));
			((ServiceSoapBindingStub)serviceStub).setTimeout(getTimeout());
		}
		catch (org.apache.axis.ConfigurationException ex) {
			logger.error("Unable to get the engine configuration for Axis",ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
		return serviceStub;
	}


	public static ServiceInfo[] queryAllServices(String sessionID) throws SMSAPIException {
		try {
			Service stub = getServiceStub();
			return stub.queryAllServices(getSecurityToken(sessionID));
		}
		catch (IllegalArgumentAPIException ex) {
			logger.error("Unable to query services ",ex);
			throw new SMSAPIException(SMSAPIException.INSUFFICIENT_DATA_FOR_SERVICE);
		}
		catch (AuthenticationAPIException ex) {
			logger.error("Unable to query services ",ex);
			throw new SMSAPIException(SMSAPIException.INVALID_AUTH);
		}
		catch (ServiceAPIException ex) {
			logger.error("Unable to query services ",ex);
			throw new SMSAPIException(ex.getMessage());
		}
		catch (AxisFault ex) {
			Throwable e = ex.getCause();
			if (e != null) {
				logger.error("Unable to query services ",e);
				throw new SMSAPIException(e.getMessage());
			}
			else {
				logger.error("Unable to query services "+ ex.getFaultString());
				throw new SMSAPIException(ex.getFaultString());
			}
		}
		catch (Exception ex) {
			logger.error("Unable to query services ",ex);
			throw new SMSAPIException(SMSAPIException.UNABLE_TO_EXECUTE_SERVICE);
		}
	}
}

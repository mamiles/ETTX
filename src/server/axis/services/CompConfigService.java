

/*
 * CompConfigService.java
 *
 * Copyright (c) 2002 by Cisco Systems, Inc.,
 * 170 West Tasman Drive, San Jose, California, 95134, U.S.A.
 * All rights reserved.
 *
 */

package com.cisco.ettx.admin.server.axis.services;

import org.apache.log4j.Logger;
import com.cisco.ettx.admin.common.SMSComponent;
import com.cisco.ettx.admin.common.ComponentHostData;
import com.cisco.ettx.admin.config.ComponentConfigManager;
import com.cisco.ettx.admin.config.ConfigurationException;
import com.cisco.ettx.admin.server.axis.spe.AuthenticateHelper;
import com.cisco.ettx.admin.server.axis.spe.AuthenticationException;
import com.cisco.ettx.admin.common.AdminServicesException; 
import java.rmi.RemoteException;


public class CompConfigService {

	private static Logger logger = Logger. getLogger(CompConfigService.class);
	public SMSComponent getCompConfig(String sessionID, String compName)
		throws AdminServicesException {
		//If successful, the next handler to be invoked
		//will be the session handler for creating a session
		//if failure an exception will be thrown

		logger.debug("Getting Configuration for comp " + compName);
		try {
			AuthenticateHelper.validateSession(sessionID);
			ComponentConfigManager mgr = ComponentConfigManager.getInstance();
			return mgr.getComponent(compName);
		}
		catch (AuthenticationException ex) {
			throw new AdminServicesException(ex.getMessage());
		}
		catch (ConfigurationException ex) {
			logger.error("Received error when getting toolbar configuration " + ex.toString());
			throw new AdminServicesException(ex.getMessage());
		}
	}

	public void setCompConfig(String sessionID, SMSComponent comp)
		throws AdminServicesException {
		//Update the configuration
		logger.debug("Setting Configuration for comp " + comp.getName());
		try {
			AuthenticateHelper.validateSession(sessionID);
			ComponentConfigManager mgr = ComponentConfigManager.getInstance();
			mgr.setSMSComponent(comp);
		}
		catch (AuthenticationException ex) {
			throw new AdminServicesException(ex.getMessage());
		}
		catch (ConfigurationException ex) {
			logger.error("Received error when getting toolbar configuration " + ex.toString());
			throw new AdminServicesException(ex.getMessage());
		}
		return;
	}

	public SMSComponent[] getAllComponents(String sessionID) 
		throws AdminServicesException {
		logger.debug("Getting Configuration for all components");
		try {
			AuthenticateHelper.validateSession(sessionID);
			ComponentConfigManager mgr = ComponentConfigManager.getInstance();
			return mgr.getAllComponents();
		}
		catch (AuthenticationException ex) {
			throw new AdminServicesException(ex.getMessage());
		}
	}

	public void changeCompHost(String sessionID,
		String compName, String oldHostName,ComponentHostData host) 
		throws AdminServicesException {
		logger.debug("Changing host configuration for component " + compName + " host " + host.getHostName());
		try {
			AuthenticateHelper.validateSession(sessionID);
			ComponentConfigManager mgr = ComponentConfigManager.getInstance();
			mgr.changeHost(compName,oldHostName,host);
		}
		catch (AuthenticationException ex) {
			throw new AdminServicesException(ex.getMessage());
		}
		catch (ConfigurationException ex) {
			logger.error("Received error when getting toolbar configuration " + ex.toString());
			throw new AdminServicesException(ex.getMessage());
		}
	}

	public void deleteCompHost(String sessionID, String compName, 
		String hostName) throws AdminServicesException {
		logger.debug("Deleting host configuration for component " + compName + " host " + hostName);
		try {
			AuthenticateHelper.validateSession(sessionID);
			ComponentConfigManager mgr = ComponentConfigManager.getInstance();
			mgr.deleteHost(compName,hostName);
		}
		catch (AuthenticationException ex) {
			throw new AdminServicesException(ex.getMessage());
		}
		catch (ConfigurationException ex) {
			logger.error("Received error when getting toolbar configuration " + ex.toString());
			throw new AdminServicesException(ex.getMessage());
		}
	}
}



/*
 * ToolbarConfigService.java
 *
 * Copyright (c) 2002 by Cisco Systems, Inc.,
 * 170 West Tasman Drive, San Jose, California, 95134, U.S.A.
 * All rights reserved.
 *
 */

package com.cisco.ettx.admin.server.axis.services;

import org.apache.log4j.Logger;
import com.cisco.ettx.admin.common.ToolbarElement;
import com.cisco.ettx.admin.config.ToolbarConfigManager;
import com.cisco.ettx.admin.config.ConfigurationException;
import java.rmi.RemoteException;
import com.cisco.ettx.admin.common.AdminServicesException; 
import com.cisco.ettx.admin.server.axis.spe.AuthenticateHelper;
import com.cisco.ettx.admin.server.axis.spe.AuthenticationException;

public class ToolbarConfigService {
	private static Logger logger = Logger.getLogger("ToolbarConfiguration");

	public ToolbarElement[] getToolbarElements(String sessionID)
		throws AdminServicesException {
		logger.debug("Getting Toolbar Configuration");
		try {
			AuthenticateHelper.validateSession(sessionID);
			ToolbarConfigManager instance = ToolbarConfigManager.getInstance();
			return instance.getToolbarElements();
		}
		catch (AuthenticationException ex) {
			throw new AdminServicesException(ex.getMessage());
		}

	}

	public void setToolbarElements(String sessionID, ToolbarElement[] elements) throws 
		AdminServicesException {
		//Update the configuration
		logger.debug("Setting Toolbar Configuration");
		try {
			AuthenticateHelper.validateSession(sessionID);
			ToolbarConfigManager instance = ToolbarConfigManager.getInstance();
			instance.setToolbarElements(elements);
		}
		catch (AuthenticationException ex) {
			throw new AdminServicesException(ex.getMessage());
		}
		catch (ConfigurationException ex) {
			logger.error("Received error when getting toolbar configuration " + ex.toString());
			throw new AdminServicesException(ex.getMessage());
		}
	}

	public void addToolbarElement(String sessionID, String name,String url)
		throws AdminServicesException {
		//Update the configuration
		logger.debug("Adding Toolbar Configuration " + url);
		try {
			AuthenticateHelper.validateSession(sessionID);
			ToolbarConfigManager instance = ToolbarConfigManager.getInstance();
			instance.addToolbarElement(name,url);
		}
		catch (AuthenticationException ex) {
			throw new AdminServicesException(ex.getMessage());
		}
		catch (ConfigurationException ex) {
			logger.error("Received error when getting toolbar configuration " + ex.toString());
			throw new AdminServicesException(ex.getMessage());
		}
	}

	public void changeToolbarElement(String sessionID, String oldUrl,
		String name,String url)
		throws AdminServicesException {
		//Update the configuration
		logger.debug("Changing Toolbar Configuration " + oldUrl);
		try {
			AuthenticateHelper.validateSession(sessionID);
			ToolbarConfigManager instance = ToolbarConfigManager.getInstance();
			instance.changeToolbarElement(oldUrl,name,url);
		}
		catch (AuthenticationException ex) {
			throw new AdminServicesException(ex.getMessage());
		}
		catch (ConfigurationException ex) {
			logger.error("Received error when getting toolbar configuration " + ex.toString());
			throw new AdminServicesException(ex.getMessage());
		}
	}

	public void deleteToolbarElement(String sessionID, String url)
		throws AdminServicesException {
		//Update the configuration
		logger.debug("DeletingToolbar Configuration" + url);
		try {
			AuthenticateHelper.validateSession(sessionID);
			ToolbarConfigManager instance = ToolbarConfigManager.getInstance();
			instance.deleteToolbarElement(url);
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

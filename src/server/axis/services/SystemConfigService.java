/*
 * SystemConfigService.java
 *
 * Copyright (c) 2002 by Cisco Systems, Inc.,
 * 170 West Tasman Drive, San Jose, California, 95134, U.S.A.
 * All rights reserved.
 *
 */

package com.cisco.ettx.admin.server.axis.services;

import org.apache.log4j.Logger;
import com.cisco.ettx.admin.common.SystemConfiguration;
import com.cisco.ettx.admin.config.SystemConfigManager;
import com.cisco.ettx.admin.config.ConfigurationException;
import com.cisco.ettx.admin.server.axis.spe.AuthenticateHelper;
import com.cisco.ettx.admin.common.AdminServicesException; 
import com.cisco.ettx.admin.server.axis.spe.AuthenticationException;

import java.rmi.RemoteException;


public class SystemConfigService {

    private Logger logger = Logger.getLogger(SystemConfigService.class);

    public SystemConfiguration getSystemConfig(String sessionID)
	throws AdminServicesException 
   {

	logger.debug("Getting System Configuration ");
	try {
		AuthenticateHelper.validateSession(sessionID);

	    // If successful, the next handler to be invoked
	    // will be the session handler for creating a session
	    // if failure an exception will be thrown
	    SystemConfigManager mgr = SystemConfigManager.getInstance();
	    return mgr.getSystemConfiguration();
	}
		catch (AuthenticationException ex) {
			throw new AdminServicesException(ex.getMessage());
		}

    }
	
    public void setSystemConfig(String sessionID, SystemConfiguration systemconfig) 
	throws AdminServicesException 
    {
	logger.debug("Setting System Configuration");

	try {
		AuthenticateHelper.validateSession(sessionID);
	    SystemConfigManager mgr = SystemConfigManager.getInstance();
	    mgr.setSystemConfiguration(systemconfig);
	}
		catch (AuthenticationException ex) {
			throw new AdminServicesException(ex.getMessage());
		}
	catch (ConfigurationException e) {
		logger.error("Received exception while setting configuration " + e.getMessage(), e);
	    throw new AdminServicesException(e.getMessage());
	}

	return;
    }
}

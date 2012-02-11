/*
 * LogDisplayService.java
 *
 * Copyright (c) 2002 by Cisco Systems, Inc.,
 * 170 West Tasman Drive, San Jose, California, 95134, U.S.A.
 * All rights reserved.
 *
 */

package com.cisco.ettx.admin.server.axis.services;

import org.apache.log4j.Logger;
import com.cisco.ettx.admin.server.logdisplay.LogDisplayServer;
import com.cisco.ettx.admin.common.ApplLog;
import com.cisco.ettx.admin.common.SMSComponent;
import java.rmi.RemoteException;
import com.cisco.ettx.admin.server.axis.spe.AuthenticateHelper;
import com.cisco.ettx.admin.common.AdminServicesException; 
import com.cisco.ettx.admin.server.axis.spe.AuthenticationException;

public class LogDisplayService {

	private static Logger logger = Logger.getLogger(LogDisplayService.class);

	public ApplLog[] getLogFilenames(String sessionID, SMSComponent[] comps, String filterString) throws AdminServicesException {

		try {
			AuthenticateHelper.validateSession(sessionID);
			logger.debug("Getting Log file names");
			LogDisplayServer mgr = LogDisplayServer.getInstance();
			return mgr.getLogFilenames(comps, filterString);
		}
		catch (Exception ex) {
			logger.error("Received exception getting log file names", ex);
			throw new AdminServicesException(ex.getMessage());
		}
	}

	public String getLogFile(String sessionID, ApplLog logFileDesc) throws AdminServicesException {
		try {
			AuthenticateHelper.validateSession(sessionID);
			logger.debug("Getting log file for desc " + logFileDesc.toString());
			LogDisplayServer mgr = LogDisplayServer.getInstance();
			return mgr.getLogFile(logFileDesc);
		}
		catch (Exception ex) {
			logger.error("Received exception getting log file", ex);
			throw new AdminServicesException(ex.getMessage());
		}

	}
}

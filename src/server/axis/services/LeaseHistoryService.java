

/*
 * LeaseHistoryService.java
 *
 * Copyright (c) 2002 by Cisco Systems, Inc.,
 * 170 West Tasman Drive, San Jose, California, 95134, U.S.A.
 * All rights reserved.
 *
 */

package com.cisco.ettx.admin.server.axis.services;

import org.apache.log4j.Logger;
import java.util.Date;
import java.rmi.RemoteException;
import com.cisco.ettx.admin.common.AdminServicesException;
import com.cisco.ettx.admin.server.leasehistory.*;
import com.cisco.ettx.admin.server.axis.spe.AuthenticateHelper;
import com.cisco.ettx.admin.server.axis.spe.AuthenticationException;

public class LeaseHistoryService {
	private static Logger logger = Logger.getLogger(LeaseHistoryService.class);

	public String runLeaseHistoryArchive(String sessionID, String cnrHost, Date startDate, Date endDate, String archiveFile) 
		throws AdminServicesException {
		try {
			AuthenticateHelper.validateSession(sessionID);
			logger.debug("Calling runLeaseHistoryArchive for host: " + cnrHost);
			com.cisco.ettx.admin.server.leasehistory.LeaseHistoryService lhs = com.cisco.ettx.admin.server.leasehistory.LeaseHistoryService.getInstance();
			return lhs.runLeaseHistoryArchive(sessionID, cnrHost, startDate, endDate, archiveFile);
		}
		catch (AuthenticationException ex) {
			throw new AdminServicesException(ex.getMessage());
		}
		catch (LeaseHistoryException ex) {
			logger.error("Received exception while archiving lease history", ex);
			throw new AdminServicesException(ex.getMessage());
		}
	}
}

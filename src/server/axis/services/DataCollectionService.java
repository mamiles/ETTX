

/*
 * DataCollectionService.java
 *
 * Copyright (c) 2002 by Cisco Systems, Inc.,
 * 170 West Tasman Drive, San Jose, California, 95134, U.S.A.
 * All rights reserved.
 *
 */

package com.cisco.ettx.admin.server.axis.services;

import org.apache.log4j.Logger;
import com.cisco.ettx.admin.collengine.DataCollectionHandler;
import com.cisco.ettx.admin.collengine.OutputManager;
import com.cisco.ettx.admin.collengine.DataCollectionException;
import com.cisco.ettx.admin.common.AttrResult;
import com.cisco.ettx.admin.common.AdminServicesException;
import com.cisco.ettx.admin.common.AdminServicesNotification;
import com.cisco.ettx.admin.server.axis.spe.AuthenticationException;
import java.util.HashMap;
import com.cisco.ettx.admin.server.axis.spe.AuthenticateHelper;

public class DataCollectionService {
	private static Logger logger = Logger.getLogger(DataCollectionService.class);

	public HashMap executeTask(String sessionID, String taskName,HashMap attrs, int numRecords)
		throws AdminServicesException,AdminServicesNotification {
		logger.debug("Executing task " + taskName + " with attributes " + attrs);
		DataCollectionHandler instance = DataCollectionHandler.getInstance();
		try {
			AuthenticateHelper.validateSession(sessionID);
			return DataCollectionHandler.getInstance().performCollection(sessionID, taskName,attrs,numRecords);
		}
		catch (AuthenticationException ex) {
			throw new AdminServicesException(ex.getMessage());
		}
		catch (DataCollectionException ex) {
			logger.error("Received exception while executing task", ex);
			if (ex.getType() == DataCollectionException.INFO) {
				throw new AdminServicesNotification(ex.getMessage());
			} else {
				throw new AdminServicesException(ex.getMessage());
			}
		}
	}

	public AttrResult iterateRecords(String sessionID, String ltoken,int numRecords) throws AdminServicesException {
		try {
			AuthenticateHelper.validateSession(sessionID);
			AttrResult result = new AttrResult(ltoken);
			result.setOutput(OutputManager.getInstance().iterateRecords(ltoken,numRecords));
			result.setPending(OutputManager.getInstance().getPendingCount(ltoken));
			return result;
		}
		catch (AuthenticationException ex) {
			throw new AdminServicesException(ex.getMessage());
		}
		catch (DataCollectionException ex) {
			logger.error("Received exception while executing task", ex);
			throw new AdminServicesException(ex.getMessage());
		}
	}
}

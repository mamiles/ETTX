/*
 * AuthenticateService.java
 *
 * Copyright (c) 2002 by Cisco Systems, Inc.,
 * 170 West Tasman Drive, San Jose, California, 95134, U.S.A.
 * All rights reserved.
 *
 */

package com.cisco.ettx.admin.server.axis.services;

import org.apache.log4j.Logger;
import java.rmi.RemoteException;

import com.cisco.ettx.admin.config.*;
import com.cisco.ettx.admin.common.*; 
import com.cisco.ettx.admin.server.axis.spe.AuthenticateHelper;
import com.cisco.ettx.admin.server.axis.spe.AuthenticationException;

public class AuthenticateService  
{

    private Logger logger = Logger.getLogger(AuthenticateService.class);

    public String login(String userid, String password) 
	throws AdminServicesException 
    {

	logger.debug("Authenticate user: " + userid);

	    // If successful, the next handler to be invoked
	    // will be the session handler for creating a session
	    // if failure an exception will be thrown

		try {
			String sessionID = AuthenticateHelper.login(userid,password);
			logger.debug("Successfully authenticated userId: " + userid);
			return sessionID;
		}
		catch (AuthenticationException ex) {
			throw new AdminServicesException(ex.getMessage());
		}
	

    }

    public void logout(String sessionID) throws AdminServicesException
    {
	// Do we need to do any clean-up here?
		try {
			logger.debug("User is logged out");
			AuthenticateHelper.logout(sessionID);
		}
		catch (AuthenticationException ex) {
			throw new AdminServicesException(ex.getMessage());
		}
	return;
    }

    public boolean checkPermission(String sessionID,
		String[] tokens) throws AdminServicesException
    {
		try {
			return AuthenticateHelper.checkPermission(sessionID,tokens);
		}
		catch (AuthenticationException ex) {
			throw new AdminServicesException(ex.getMessage());
		}
    }
}

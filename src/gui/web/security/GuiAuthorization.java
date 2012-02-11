/*
 * File: GuiAuthorization.java
 * 
 * Description
 *    helper for both authentication and authorization.
 *
 * Revision: $Revision$
 * Created: 
 * Author:  hvo  
 *
 * Copyright (c) 2000 company Cisco Systems. All rights reserved.
 *
 * The copyright to the computer program(s) herein is the property
 * of Cisco Systems. The program(s) may be used and/or copied only with 
 * the written permission of company Cisco Systems or in accordance
 * with the terms and condition stipulated in the agreement/contract
 * under which the program(s) has been supplied
 *
 * $Header$
 *
 * $Log$
 */



package com.cisco.ettx.admin.gui.web.security;


import com.cisco.nm.uii.security.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import org.apache.log4j.Logger;
import com.cisco.ettx.admin.gui.web.helper.AuthenticateHelper;
import com.cisco.ettx.admin.common.AdminServicesException;
import com.cisco.ettx.admin.gui.web.util.ETTXUtil;

public class GuiAuthorization implements UIIAuthorize {

   // method that check whether currently login user has authorization for the
   // task id list
	public static final Logger logger = Logger.getLogger(GuiAuthorization.class);

   public boolean isAuthorized(HttpServletRequest request,
                               List taskIdList) {
	logger.debug("Checking permission for " + taskIdList);
	if (taskIdList == null || taskIdList.size() == 0) return true;
	try {
		String sessionID = ETTXUtil.getSessionID(request);
		return AuthenticateHelper.checkPermission(sessionID,(String[])taskIdList.toArray(new String[taskIdList.size()]));
	}
	catch (AdminServicesException ex) {
		logger.error("Unable to get permission for user ",ex);
		return false;
	}
   }
 
}	// END of class GuiAuthorization

/**
 * Class: ProvisioningAPI
 *
 * Copyright (c) 2003 by Cisco Systems, Inc. All rights reserved.
 *
 * Author: djf
 *
 */

package com.cisco.sesm.sms.extensions;
import com.cisco.cns.security.soap.common.*;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.io.*;
import java.util.HashMap;
import java.util.Vector;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import com.cisco.sesm.sms.exceptions.api.*;
import com.cisco.sesm.sms.types.*;

/** 
 * ProvisioningAPI
 *
 * @author David Fletcher
 */
public class ProvisioningAPI 
{

    /**
     * Return all the subscribers that match the supplied filter.
     *
     * @param token the SMS security token previously retrieved
     * @param filter the filter to be used to find matching subscribers
     * @return an array of <code>ProvisioningInfo</code> objects specifying each
     * of the subscribers selected by the supplied filter. Should there be no
     * matches, a zero length array will be returned.
     * @throws AuthenticationAPIException if the security token is invalid
     * @throws MissingFieldsAPIException should required fields be missing from the
     * <code>FilterInfo</code> parameter
     * @throws ProvisioningAPIException if a failure occurs within the 
     * Provisioning service
     * @see com.cisco.sesm.sms.types.FilterInfo
     */
	private static Logger logger = Logger.getLogger(ProvisioningAPI.class);

    public void activateService(SecurityToken token,String subsId,
		String[] serviceNames) 
	throws AuthenticationAPIException , IllegalArgumentAPIException, ProvisioningAPIException
   {

		if (token == null) throw new AuthenticationAPIException("Authen Failure");
		logger.info("Activating " + subsId + " to services " + serviceNames);
    }

    public void deactivateService(SecurityToken token,String subsId,
			   String[] serviceNames)
	throws AuthenticationAPIException , IllegalArgumentAPIException, ProvisioningAPIException
   {

		if (token == null) throw new AuthenticationAPIException("Authen Failure");
		logger.info("Deactivating " + subsId + " from services " + serviceNames);
    }
}

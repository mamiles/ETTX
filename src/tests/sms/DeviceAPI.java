/**
 * Class: DeviceAPI
 *
 * Copyright (c) 2003 by Cisco Systems, Inc. All rights reserved.
 *
 * Author: djf
 *
 */

package com.cisco.sesm.sms.extensions;
import com.cisco.sesm.sms.exceptions.api.*;
import com.cisco.sesm.sms.types.*;
import com.cisco.cns.security.soap.common.*;

/** 
 * DeviceAPI
 *
 * @author Chandrika R
 */
public class DeviceAPI 
{

    public EdgeDeviceInfo getEdgeDeviceInfo(
		SecurityToken token, String acct) 
	throws AuthenticationAPIException , IllegalArgumentAPIException
   {

		if (token == null) throw new AuthenticationAPIException("Authen Failure");
            if (acct == null ||
		acct.length() == 0) {
			throw new IllegalArgumentAPIException("Illegal Arguments");
	    }
		EdgeDeviceInfo info = new EdgeDeviceInfo();
		info.setIpAddress("64.101.181.63");
		return info;
    }
}

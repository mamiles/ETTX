
package com.cisco.ettx.admin.server.axis.sms;

import org.apache.log4j.Logger;
import com.cisco.ettx.admin.server.axis.spe.SPEAPI;
import com.cisco.cns.security.soap.common.SecurityToken;
import org.apache.axis.client.*;
import java.net.URL;
import java.rmi.RemoteException;

public class SMSAPI {
	private static int timeout = 0;

	private static String SMS_SERVICES_URL = "SMS_SERVICES_URL";
	private static String ETTX_CLIENT_TIMEOUT = "ETTX_CLIENT_TIMEOUT";
	private static int DEFAULT_TIMEOUT = 30000;
	public static Logger myLogger  = Logger.getLogger(SMSAPI.class);

	protected SMSAPI() {
	}

	public static URL getURL(String service) throws SMSAPIException {
			//get the URL
			try {
				String urlName =new String(System.getProperty(SMS_SERVICES_URL) + service);;
				myLogger.debug("URL is " + urlName);
				URL url = new URL(urlName);
				if (url == null) {
					throw new SMSAPIException(SMSAPIException.NO_URL_FOR_SMS);
				}
				return url;
			}
			catch (Exception ex) {
				throw new SMSAPIException(SMSAPIException.NO_URL_FOR_SMS);
			}
	}

	public static int getTimeout() {
		if (timeout == 0) {
			String timeoutStr = System.getProperty(ETTX_CLIENT_TIMEOUT);
			if (timeoutStr != null) {
				timeout = Integer.valueOf(timeoutStr).intValue();
			}
			if (timeout == 0) {
				timeout = DEFAULT_TIMEOUT;
			}
		}
		return timeout;
	}
			

	public static SecurityToken getSecurityToken(String sessionID) {
		return SPEAPI.getSecurityToken(sessionID);
	}

	public static AxisClient getEngine() throws org.apache.axis.ConfigurationException {
		return SPEAPI.getEngine();
	}
}

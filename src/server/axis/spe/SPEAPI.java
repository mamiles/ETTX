
package com.cisco.ettx.admin.server.axis.spe;

import org.apache.log4j.Logger;
import org.apache.axis.client.*;
import org.apache.axis.configuration.*;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.utils.SessionUtils;
import java.net.URL;
import com.cisco.cns.security.soap.common.SecurityToken;
import java.util.Hashtable;

public class SPEAPI {
	private static int timeout = 0;

	private static String SPE_SERVICES_URL = "SPE_SERVICES_URL";
	private static String ETTX_CLIENT_TIMEOUT = "ETTX_CLIENT_TIMEOUT";
	private static String ETTX_ROOT = "ETTX_ROOT";
	private static String CONFIG_FILE = "client-config.wsdd";
	private static int DEFAULT_TIMEOUT = 30000;
	protected static AxisClient engine = null;
	protected static Hashtable tokens = new Hashtable();

	protected SPEAPI() {
	}

	public static URL getURL(String serviceName) throws AuthenticationException {
			//get the URL
			try {
				String urlName = new String(System.getProperty(SPE_SERVICES_URL) + serviceName);
				URL url = new URL(urlName);
				if (url == null) {
					throw new AuthenticationException(AuthenticationException.NO_URL_FOR_SPE);
				}
				return url;
			}
			catch (Exception ex) {
				throw new AuthenticationException(AuthenticationException.NO_URL_FOR_SPE);
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
			

	public static AxisClient getEngine() throws org.apache.axis.ConfigurationException {
		if (engine == null) {
			engine = new AxisClient(new FileProvider(System.getProperty(ETTX_ROOT),
				CONFIG_FILE));
		}
		return engine;
	}

    public static synchronized SecurityToken getSecurityToken(String sessionID)
    {

	return (SecurityToken)tokens.get(sessionID);
    }

    public static synchronized String createSecurityToken(SecurityToken token)
    {
	String sessionID = SessionUtils.generateSessionId();
	tokens.put(sessionID,token);
	return sessionID;
    }

    public static synchronized void removeSecurityToken(String sessionID)
    {
	tokens.remove(sessionID);
    }

}

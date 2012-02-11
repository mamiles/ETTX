
package com.cisco.ettx.admin.client.axis;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.client.AxisClient;
import org.apache.axis.configuration.FileProvider;
import org.apache.axis.AxisProperties;
import java.net.URL;
import org.apache.log4j.Logger;

//Call AxisAdmin.initialize to initialize the TIB transport

public class AxisAdmin {
	private static AxisClient axisClient = null;
	private static URL url = null;
	private static int timeout = 30000;

	private static String SERVICE_URL_PROP = "ETTX_SERVICES_URL";
	private static String TIMEOUT = "ETTX_CLIENT_TIMEOUT";
	private static Logger logger = Logger.getLogger(AxisAdmin.class);
	protected static AxisClient engine = null;
	private static String ETTX_ADMIN_GUI_HOME = "ETTX_ADMIN_GUI_HOME";
	private static String CONFIG_FILE = "WEB-INF/client-config.wsdd";
	
	public static void initialize() throws Exception {
		try {
			axisClient = new AxisClient();
			url = new java.net.URL(System.getProperty(SERVICE_URL_PROP));
			String timeoutStr = System.getProperty(TIMEOUT);
			if (timeoutStr != null) {
				Integer i = Integer.valueOf(timeoutStr);
				timeout = i.intValue();
			}
		}
		catch (Exception ex) {
			logger.error("Unable to create Axis client or the URL " + System.getProperty(SERVICE_URL_PROP),ex);
			url =null;
			throw ex;
		}
	}

	public static URL getURL() throws javax.xml.rpc.ServiceException {
		if (url == null) {
			throw new javax.xml.rpc.ServiceException("No URL for server");
		}
		return url;
	}

	public static int getTimeout() {
		return timeout;
	}
	
	public static AxisClient getEngine() throws org.apache.axis.ConfigurationException {
		if (engine == null) {
			engine = new AxisClient(new FileProvider(System.getProperty(ETTX_ADMIN_GUI_HOME), CONFIG_FILE));
		}
		return engine;
	}
}


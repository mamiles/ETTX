package com.cisco.ettx.admin.tests.logdisp;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;

import com.cisco.ettx.admin.common.*;
import com.cisco.ettx.admin.client.axis.*;
import java.util.Vector;

/**
 * Tests the simple File transport.  To run:
 *      java org.apache.axis.utils.Admin client client_deploy.xml
 *      java org.apache.axis.utils.Admin server deploy.xml
 *      java samples.transport.FileTest IBM
 *      java samples.transport.FileTest XXX
 */
public class LogDispTest
{
    public static void main(String [] args)
	throws Exception
    {
	try
	{
		AxisAdmin.initialize();
	    AdminServicesLocator loc = new AdminServicesLocator();
	    Authenticate stub = loc.getAuthenticate(new java.net.URL(System.getProperty("ETTX_SERVICES_URL")));
		String sessionID = stub.login("mamiles","21igma!");
	    	System.out.println("User mamiles Logged in");
	    Configuration stub1 = loc.getConfiguration(new java.net.URL(System.getProperty("ETTX_SERVICES_URL")));
		SMSComponent[] comps = stub1.getAllComponents(sessionID);
		System.out.println(comps);

	LogDisplay stub2 = loc.getLogDisplay();
	ApplLog[] logs = stub2.getLogFilenames(sessionID,comps,"");
	System.out.println(logs.length);
	for (int i = 0; i < logs.length; i++) {
		System.out.println(logs[i].toString());
		System.out.println(stub2.getLogFile(sessionID,logs[i]));
	}
//	String value = stub2.getLogFile(logs[0]);
//	System.out.println(value);
	

		stub.logout(sessionID);
	    	System.out.println("User admin Logged out");
	}
	catch (AxisFault af)
	{
	    System.err.println(af.dumpToString());
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }
}

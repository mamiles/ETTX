package com.cisco.ettx.admin.tests.authenticate;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;

import com.cisco.ettx.admin.common.*;
import com.cisco.ettx.admin.client.axis.*;
import java.net.URL;

/**
 * Tests the simple File transport.  To run:
 *      java org.apache.axis.utils.Admin client client_deploy.xml
 *      java org.apache.axis.utils.Admin server deploy.xml
 *      java samples.transport.FileTest IBM
 *      java samples.transport.FileTest XXX
 */
public class AuthenticateTest
{
    public static void main(String [] args)
	throws Exception
    {
	try
	{
		AxisAdmin.initialize();
	    AdminServicesLocator loc = new AdminServicesLocator();
	    Authenticate stub = loc.getAuthenticate(new java.net.URL(System.getProperty("ETTX_SERVICES_URL")));
		String sessionID = stub.login("admin","admin");
	    	System.out.println("User admin Logged in");
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

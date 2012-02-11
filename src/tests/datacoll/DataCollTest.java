package com.cisco.ettx.admin.tests.DataCollTests;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;

import com.cisco.ettx.admin.common.*;
import com.cisco.ettx.admin.client.axis.*;
import java.util.Vector;
import java.util.HashMap;
import org.apache.log4j.*;
import java.net.URL;

/**
 * Tests the simple File transport.  To run:
 *      java org.apache.axis.utils.Admin client client_deploy.xml
 *      java org.apache.axis.utils.Admin server deploy.xml
 *      java samples.transport.FileTest IBM
 *      java samples.transport.FileTest XXX
 */
public class DataCollTest
{
    public static void main(String [] args)
	throws Exception
    {
	try
	{
		PropertyConfigurator.configure("/tmp/log.properties");
		AxisAdmin.initialize();
	    AdminServicesLocator loc = new AdminServicesLocator();
	    Authenticate stub = loc.getAuthenticate(new java.net.URL(System.getProperty("ETTX_SERVICES_URL")));
		String sessionID = stub.login("admin","admin");
	    	System.out.println("User admin Logged in");
	    	DataCollection stub1 = loc.getDataCollection(new java.net.URL(System.getProperty("ETTX_SERVICES_URL")));
		HashMap queryAttrs = new HashMap();
		HashMap output = stub1.executeTask(sessionID,"QUERY_SUBSCRIBERS_SERVICE",queryAttrs,10);
		System.out.println("Output is " + output);
		AttrResult result = (AttrResult)output.get("subscriberRecords");
		System.out.println("Partial Result is " + result.getOutput());
		while(result.getPending() > 0) {
			result = stub1.iterateRecords(sessionID,result.getIterator(),10);
			System.out.println("Partial Result is " + result.getOutput());
		}
		HashMap attrs = new HashMap();
		attrs.put("subscriberID","Subs1");
		output = stub1.executeTask(sessionID,"TROUBLESHOOT_DATA_SERVICE",attrs,0);
		System.out.println("Output is " + output);
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

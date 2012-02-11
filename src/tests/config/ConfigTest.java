package com.cisco.ettx.admin.tests.config;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;

import com.cisco.ettx.admin.common.*;
import com.cisco.ettx.admin.client.axis.*;
import java.util.Vector;
import java.net.URL;

/**
 * Tests the simple File transport.  To run:
 *      java org.apache.axis.utils.Admin client client_deploy.xml
 *      java org.apache.axis.utils.Admin server deploy.xml
 *      java samples.transport.FileTest IBM
 *      java samples.transport.FileTest XXX
 */
public class ConfigTest
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
	    Configuration stub1 = loc.getConfiguration(new java.net.URL(System.getProperty("ETTX_SERVICES_URL")));
	    SystemConfiguration config = stub1.getSystemConfig(sessionID);
	    System.out.println(config.toString());
	    config.setSpeUserID("admin");
	    config.setSpePassword("admin123");
	    config.setPurgeDur(10);
	    stub1.setSystemConfig(sessionID,config);

	    ToolbarElement[] elems = stub1.getToolbarElements(sessionID);
	    System.out.println(elems);
	    ToolbarElement elem = new ToolbarElement("Cisco","www.cisco.com");
	    elems = new ToolbarElement[] {elem};
	    stub1.setToolbarElements(sessionID,elems);

	    SMSComponent comp = stub1.getCompConfig(sessionID,"SMS");
	    System.out.println(comp);
	    ComponentHostData host = new ComponentHostData();
		host.setHostName("dummy");
	    Vector dirs = new Vector();
	    dirs.add(new String("hello"));
	    dirs.add(new String("helloAgain"));
	    host.setLogDirs(dirs);
		Vector vec = new Vector();
		vec.add(host);
		comp.setComponentHostData(vec);
	    stub1.setCompConfig(sessionID,comp);

		SMSComponent[] comps = stub1.getAllComponents(sessionID);
		System.out.println(comps);

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

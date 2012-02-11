package com.cisco.ettx.admin.server;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Logger;

//import com.cisco.ettx.admin.server.axis.AdminTibServer;
import com.cisco.ettx.admin.config.*;
import com.cisco.ettx.admin.authentication.CryptoHelper;
import com.cisco.ettx.admin.auditlog.AuditLogger;
import com.cisco.ettx.admin.auditlog.FilePurgeAppender;
import com.cisco.ettx.admin.server.logdisplay.LogDisplayServer;
import com.cisco.ettx.admin.server.leasehistory.LeaseHistoryService;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Enumeration;
import org.apache.axis.AxisProperties;

public class EttxAdminServer extends HttpServlet
{
    	private Logger logger = Logger.getLogger(EttxAdminServer.class);
	private String ettxDir = null;
	private String catalinaHome = null;
	private String tomcatPort = null;
	//private AdminTibServer tibServer = null;
	private AuditLogger auditLogger = null;
	boolean viaTIB = false;

    public EttxAdminServer()
	throws Exception
    {
    }

    public void init(ServletConfig config) 
	throws ServletException  {
	super.init(config);


	Enumeration e = config.getInitParameterNames();
	String param = null, value = null;
	ServletContext servletCtx = config.getServletContext();
	while (e.hasMoreElements()) 
	{
	    param = (String) e.nextElement();
	    value = config.getInitParameter(param);
	    System.setProperty(param,value);
	}

	ettxDir = System.getProperty("ETTX_ROOT");
	if (ettxDir == null) {
		throw new ServletException("ETTX_ROOT is not set");
	}
	catalinaHome = System.getProperty("CATALINA_HOME");
	if (catalinaHome == null) {
		throw new ServletException("CATALINA_HOME is not set");
	}
	tomcatPort = System.getProperty("TOMCAT_PORT");
	if (tomcatPort == null) {
		throw new ServletException("TOMCAT_PORT is not set");
	}
	String configFile = System.getProperty("axis.ClientConfigFile");
	if (configFile != null) {
		AxisProperties.setProperty("axis.ClientConfigFile",configFile);
	}
	configFile = System.getProperty("axis.ServerConfigFile");
	if (configFile != null) {
		AxisProperties.setProperty("axis.ServerConfigFile",configFile);
	}

	initLog4j();
        logger = Logger.getLogger(EttxAdminServer.class);
	logger.debug("Initializing Server and Configurations...");
	try {
		new CryptoHelper(ettxDir);
		new SystemConfigManager(ettxDir);
		new ComponentConfigManager(ettxDir);
		new ToolbarConfigManager(ettxDir);
		new LogDisplayServer(ettxDir, catalinaHome, tomcatPort);
		new LeaseHistoryService(ettxDir);
		auditLogger = new AuditLogger(ettxDir);
		FilePurgeAppender.getInstance().init(); //Not sure how else to do it for now
		new com.cisco.ettx.admin.collengine.command.CommandExecutor(ettxDir);
		new com.cisco.ettx.admin.collengine.sms.SMSDataCollector(ettxDir);
			//REVISIT - The individual data collectors must be done another way
			//Create individual collectors before initializing data collectors
		new com.cisco.ettx.admin.collengine.DataCollectionHandler(ettxDir);
	
	/*
		logger.info("Initializing TIB Server...");
		tibServer = new AdminTibServer();
	*/

	//Start all the threads
		auditLogger.start();
	}
	catch (Exception ex) {
		logger.error("Unable to initialize ETTX Admin Server",ex);
		throw new ServletException(ex);
	}

	logger.info("ETTX Admin Server initialized");
    }

    private void initLog4j()
    {
	String log4jConfigFile = ettxDir + "/config/server-log4j.prop";
	PropertyConfigurator.configure(log4jConfigFile);
    }

	/*private void run() throws Exception {
	    tibServer.run();
    	}

    public static void main(String [] args)
    {
	try {
	    EttxAdminServer ettx_server = new EttxAdminServer();
	    ettx_server.run();
	} catch (Exception e) {
	    System.err.println("Error starting EttxAdmin Server");
	    System.err.println("Unexpected Exception:\n");
		e.printStackTrace();
	    return;
	}
	
    }
*/
	
}
 

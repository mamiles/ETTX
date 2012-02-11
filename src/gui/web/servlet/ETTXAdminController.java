/*
 * File: ETTXAdminController.java
 * 
 * Description
 *    helper for both authentication and authorization.
 *
 * Revision: $Revision$
 * Created: 
 * Author:  Chandrika  
 *
 * Copyright (c) 2000 company Cisco Systems. All rights reserved.
 *
 * The copyright to the computer program(s) herein is the property
 * of Cisco Systems. The program(s) may be used and/or copied only with 
 * the written permission of company Cisco Systems or in accordance
 * with the terms and condition stipulated in the agreement/contract
 * under which the program(s) has been supplied
 *
 * $Header$
 *
 * $Log$
 */



package com.cisco.ettx.admin.gui.web.servlet;


import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import com.cisco.nm.uii.*;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import com.cisco.ettx.admin.common.*;
import com.cisco.ettx.admin.client.axis.*;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

// create our own controller so we can load in our own init params
public class ETTXAdminController extends UIIController {

    public static final String ETTX_ADMIN_GUI_HOME = "ETTX_ADMIN_GUI_HOME";

    // the only method we are interested in
    public void init(ServletConfig config) 
	throws ServletException 
    {
	super.init(config);

	Enumeration e = config.getInitParameterNames();
	String param = null, value = null;
	ServletContext servletCtx = config.getServletContext();
	while (e.hasMoreElements()) 
	{
	    param = (String) e.nextElement();
	    value = config.getInitParameter(param);
	    System.setProperty(param, value);
	    servletCtx.log("ETTX: setting system property: " + param + " == " + value);
	}

	// initialize Log4j
	initLog4j();

	// initialize Axis 
	servletCtx.log("Initializing Axis");
	try {
		AxisAdmin.initialize();
	}
	catch (Exception ex) {
		servletCtx.log("Error in Axis initialization");	
		ex.printStackTrace();
		throw new ServletException("Error in Axis Initialization");
	}

      
   }
 
 
    // need to log out when the servlet is taken out of service
    public void destroy() 
    {
	super.destroy();
    }
   
   
    public void initLog4j() 
    {
	String GUI_HOME = System.getProperty(ETTX_ADMIN_GUI_HOME);
	
	String log4jConfigFile = GUI_HOME + "/WEB-INF/config/log4j.config";
	System.out.println("+++++++++++++ Log$j configure file: "+ log4jConfigFile);
	PropertyConfigurator.configure( log4jConfigFile );
    }
   
}

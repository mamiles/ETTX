/*
 * File: LoginServlet.java
 * 
 * Description
 *    helper for both authentication and authorization.
 *
 * Revision: $Revision$
 * Created: 
 * Author: 
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


import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import com.cisco.nm.uii.*;
import org.apache.log4j.Logger;
import com.cisco.ettx.admin.gui.web.util.ETTXUtil;
import com.cisco.ettx.admin.common.AdminServicesException;

import com.cisco.ettx.admin.gui.web.helper.AuthenticateHelper;
import java.rmi.RemoteException;

public class LoginServlet extends HttpServlet {

    private Logger logger = Logger.getLogger(LoginServlet.class);
   
    public void doGet(HttpServletRequest request,
	HttpServletResponse response)
	throws ServletException, java.io.IOException {


	// get the classpath
	String classpath = System.getProperty("java.class.path");
	logger.debug("CLASSPATH=" + classpath);

	String userId   = request.getParameter( "userId" );
	String password = request.getParameter( "userPasswd" );

	logger.debug("Trying to authenticate user id: " + userId);
    
	// if from previous login page
	String path = (String) request.getAttribute("originalPath");
	// if from controller
	if (path == null || path.equals( "" ))
	    path = ( String ) request.getParameter("originalPath");

	logger.debug("The original path is: " + path);

	if (userId == null || userId.equals("") || 
	    password == null || password.equals( "" )) {
		generateLoginPage(request, response);
		return;
	} else {
	    // call this to remove any residual attributes
	    // com.cisco.dslnms.gui.web.actions.LogoutAction.doLogOut(request);
	    HttpSession session = request.getSession();
	    try {
		String sessionID = AuthenticateHelper.login(userId,password);
	    	//Set authentication service info 
		ETTXUtil.setSessionID(request,sessionID);
	    } catch (AdminServicesException e) {
		logger.error("Failed to authenticate user: " + userId + " Server Exception: " + e );
		session.setAttribute(ETTXUtil.AUTHENTICATION_ERROR, ETTXUtil.localizeMessage(e));
		generateLoginPage(request, response);
		return;
	    }
            catch (Exception e) {
		logger.error("Failed to authenticate user: " + userId + " Unexpected Exception: " + e );
		session.setAttribute(ETTXUtil.AUTHENTICATION_ERROR, ETTXUtil.localizeMessage("loginFailure"));
		generateLoginPage(request, response);
		return;
	    }
	
	     session.setAttribute(ETTXUtil.AUTHENTICATION_ERROR,null);
	    logger.debug("User id: " + userId + " successfully authenticated");
	    ServletContext sc = this.getServletContext();


            String forwardPath = request.getContextPath() + "/" + com.cisco.ettx.admin.gui.web.actions.RepQuerySubscribersAction.DEFAULT_FORWARD + ".do";

	    logger.debug("LoginServlet browser redirecting to: " + forwardPath);
      
	    response.sendRedirect(forwardPath);
	}
    }

    public void doPost(HttpServletRequest request,
	HttpServletResponse response )
	throws ServletException, java.io.IOException 
    {
	doGet( request, response );
    }  

    private void generateLoginPage(HttpServletRequest request,
	HttpServletResponse response) 
	throws ServletException, IOException 
    {
	response.setContentType("text/html");
	RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/screens/login.jsp");
	dispatcher.forward(request, response);
    }
  
}

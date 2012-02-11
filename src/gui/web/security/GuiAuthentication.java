/*
 * File: GuiAuthentication.java
 * 
 * Description
 *    helper for both authentication and authorization.
 *
 * Revision: $Revision$
 * Created: 
 * Author:  hvo  
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


package com.cisco.ettx.admin.gui.web.security;

import com.cisco.nm.uii.*;
import com.cisco.nm.uii.security.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.*;
import com.cisco.ettx.admin.gui.web.util.ETTXUtil;


public class GuiAuthentication implements UIIAuthenticate {

   public boolean isAuthenticated(HttpServletRequest request,
                                  HttpServletResponse response,
                                  ServletConfig servletConfig) {

	HttpSession session = request.getSession();
 
      // get servlet context for logging and forwarding
      ServletContext sc = servletConfig.getServletContext();
	String isAuthenticated = ETTXUtil.getSessionID(request);
	if (isAuthenticated != null) {
		return true;
	}
      try {

            String path = UIIController.computePath(request);
            path = path.substring(1);
            // Set incomeing path so LoginServlet can send the user back to
            // 'this' path after they successfully log in.
            request.setAttribute("originalPath", path);

            RequestDispatcher rd = sc.getRequestDispatcher( 
               "/servlet/com.cisco.ettx.admin.gui.web.servlet.LoginServlet");
            rd.forward(request, response);
            
            return false;
      } catch (Exception e) {
           sc.log("Authenticate could not forward to LoginServlet", e); 
           return false;
      }
   }
 
}	// END of class GuiAuthentication

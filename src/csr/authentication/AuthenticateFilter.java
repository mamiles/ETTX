/*
 * AuthenticateFilter.java
 *
 * Created on 21 April 2003, 23:34
 */

package com.cisco.ettx.provisioning.authentication;

import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import javax.servlet.*;
import javax.servlet.http.*;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.cisco.ettx.provisioning.authentication.User;

/**
 * This filter will check for the presence of an attribute within the 
 * user's session that indicates that the user is authenticated. The name of
 * this attribute is specified from an initialization parameter.
 * <p>
 * If the attribute indicating authorized is not found then the request will
 * be forwarded to the login page action which is also configurable.
 * <p>
 * <B>Note:</B>Often for debug purposes you may wish not to be required to
 * authenticate. To disable this filter add an init-parameter called <i>
 * disabled</i> with the value <i>true</i> to the web.xml. To enable filtering 
 * again remove this parameter or set it's value to something other than
 * <i>true</i>
 *
 * @author  pcurren
 * @version
 */

public class AuthenticateFilter implements Filter {
    
    public static String INDICATOR_NAME = "USER";
    
    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured.
    private FilterConfig filterConfig = null;
    
    public AuthenticateFilter() {
    }
    
    /**
     *
     * @param request The servlet request we are processing
     * @param result The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
    FilterChain chain)
    throws IOException, ServletException {
    
        // get the name of the attribute being looked for.
        // the presence of this attribute indicates authenticated.
        boolean runFilter = true;
        String forwardName = getFilterConfig().getInitParameter("loginPage");
        
        if (forwardName == null) {
            log("AuthenticateFilter: No loginPage parameter supplied. No filtering performed.");
            runFilter = false;
        }
        
        HttpServletRequest httpReq = null;
        if (!(request instanceof HttpServletRequest)) {
            log("AuthenticateFilter: This filter only applies to HTTP requests.");
            runFilter = false;
        }
        else {
            httpReq = (HttpServletRequest)request;
        }
        
        // check if authentication filtering is to be used. This is useful for
        // debug
        Boolean disabledFlag = Boolean.valueOf(getFilterConfig().getInitParameter("disabled"));
        if (disabledFlag == Boolean.TRUE) {
            log("AuthenticateFilter: Filter configured to be disabled. Inserting dummy User into HttpSession.");
            runFilter = false;
            // insert made up details into the session. Run filter is set to false
            // for debug purposes normally, so we'll need a USER to continue to run
            if (httpReq != null) {
                User dummyUser = new User();
                dummyUser.setUsername("DummyUser");
                dummyUser.setPassword("DummyPassword");
                httpReq.getSession().setAttribute(AuthenticateFilter.INDICATOR_NAME,dummyUser);
            }
        }
        
        if (runFilter) {
            Object obj = httpReq.getSession().getAttribute(INDICATOR_NAME);
            
            if (obj == null) {
                // authentication required
                RequestDispatcher disp = httpReq.getRequestDispatcher(forwardName);
                disp.forward(request,response);
            }
            else if (!(obj instanceof User)) {
                log("AuthenticateFilter: The stored authenticated indicator is not of class com.cisco.ettx.provisioning.authentication.User");
                log("AuthenticateFilter: Wrong class, so authentication is required");
                httpReq.getSession().removeAttribute(INDICATOR_NAME);
                RequestDispatcher disp = httpReq.getRequestDispatcher(forwardName);
                disp.forward(request,response);
            }
            else {
                // authenticated - do the rest of the filter chain
                chain.doFilter(request, response);
            }
        }
        else {
            // go onto next filter in chain
            chain.doFilter(request, response);
        }
    
    }
    
    
    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }
    
    
    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        
        this.filterConfig = filterConfig;
    }
    
    /**
     * Destroy method for this filter
     *
     */
    public void destroy() {
    }
    
    
    /**
     * Init method for this filter
     *
     */
    public void init(FilterConfig filterConfig) {
        
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {
                log("AuthenticateFilter:Initializing filter");
            }
        }
    }
    
    /**
     * Return a String representation of this object.
     */
    public String toString() {
        
        if (filterConfig == null) return ("AuthenticateFilter()");
        StringBuffer sb = new StringBuffer("AuthenticateFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
        
    }
        
    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }
    
    private static final boolean debug = true;
}

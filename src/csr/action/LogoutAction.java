/*
 * LogoutAction.java
 *
 * Created on 18 April 2003, 16:45
 */

package com.cisco.ettx.provisioning.action;

import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.ModuleException;
import org.apache.struts.util.MessageResources;

import com.cisco.cns.security.soap.common.SecurityToken;
import com.cisco.ettx.provisioning.Audit;
import com.cisco.ettx.provisioning.authentication.AuthenticateFilter;
import com.cisco.ettx.provisioning.authentication.SPEAuthenticate;
import com.cisco.ettx.provisioning.authentication.SecurityTokenCache;
import com.cisco.ettx.provisioning.authentication.User;

/**
 *
 * @author  pcurren
 */
public class LogoutAction extends Action {
    
    /**
     * The <code>Log</code> instance for this application.
     */
    private Log log =
        LogFactory.getLog("com.cisco.ettx.provisioning.action.LogoutAction");
    
    
    /**
     * Logout a supplied user from SPE, and from the web application. A logout
     * involves simply removing the USER attribute from the session, and 
     * ensuring the SPE token is logged out.
     */
    public ActionForward execute(ActionMapping mapping, 
                                 ActionForm actionForm, 
                                 HttpServletRequest request, 
                                 HttpServletResponse response) 
        throws Exception {
            
        // get the USER attribute from the session
        User user = (User) request.getSession().getAttribute(AuthenticateFilter.INDICATOR_NAME);
        
        if (user == null) {
            // user was not logged in
            log.info("Attempt made to logout a user who is not logged in.");
            log.debug("No " + AuthenticateFilter.INDICATOR_NAME 
                      + " attribute was found in the HttpSession.");
        }
        else {
            SPEAuthenticate.logout(user.getUsername());
            request.getSession().removeAttribute(AuthenticateFilter.INDICATOR_NAME);
            String client = request.getRemoteAddr();
            Audit.record(user.getUsername(), client, "Logged out");
        }

        return mapping.findForward(Forwards.LOCAL_SUCCESS_FORWARD);
    }
}

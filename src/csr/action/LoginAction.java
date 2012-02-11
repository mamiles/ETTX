/*
 * LoginAction.java
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
import com.cisco.ettx.provisioning.authentication.User;

/**
 *
 * @author  pcurren
 */
public class LoginAction extends Action {
    
    /**
     * The <code>Log</code> instance for this application.
     */
    private Log log =
        LogFactory.getLog("com.cisco.ettx.provisioning.action.LoginAction");
    
    
    /**
     * Login the user against SPE. If login is successful, then forward to
     * the application's home page, otherwise return to the login page
     * with the appropriate message inserted in the form bean
     */
    public ActionForward execute(ActionMapping mapping, 
                                 ActionForm actionForm, 
                                 HttpServletRequest request, 
                                 HttpServletResponse response) 
        throws Exception {
            
        DynaActionForm form = (DynaActionForm)actionForm;
        String username = (String)form.get("username");
        String password = (String)form.get("password");
        
        // confirm that username and password have been supplied
        ActionErrors errors = new ActionErrors();
        if (username == null) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("login.errors.missingUser"));
        }
        
        if (password == null) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("login.errors.missingPass"));
        }
    
        // no errors so far so try and authenticate
        if (errors.size() == 0) {
            SecurityToken token = SPEAuthenticate.authenticate(username,password);
            if (token == null) {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("login.errors.failedlogin"));
            }
            else {
                // create a User object and store in the Session
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                
                request.getSession().setAttribute(AuthenticateFilter.INDICATOR_NAME,user);
            }
        }

        // for audit logging purposes
        String client = request.getRemoteHost();
        String message;
        
        ActionForward fwrd = null;
        if (errors.size() > 0) {
            saveErrors(request,errors);
            fwrd = mapping.findForward(Forwards.FAILURE_FORWARD);
            message = "Failed login attempt";
        }
        else {
            fwrd = mapping.findForward(Forwards.LOCAL_SUCCESS_FORWARD);
            message = "Logged in";
        }
        
        Audit.record(username, client, message);
        return fwrd;
    }
    
}

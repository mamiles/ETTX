/*
 * PrepareRegistrationWizardAction.java
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

import com.cisco.ettx.provisioning.application.SubscriberSearch;
import com.cisco.ettx.provisioning.Audit;
import com.cisco.ettx.provisioning.authentication.AuthenticateFilter;
import com.cisco.ettx.provisioning.authentication.User;

import com.cisco.sesm.sms.types.SubscriberInfo;

/** 
 * Find all the subscribers within the system and forward to a page to display
 * the results.
 * <p>
 * Noddy! No time to do this stuff right at the moment.
 *
 * @author  pcurren
 */
public class SubscriberSearchAction extends Action {
    
    /**
     * The <code>Log</code> instance for this application.
     */
    private Log log =
        LogFactory.getLog("com.cisco.ettx.provisioning.action.SubscriberSearchAction");
    
    /**
     *
     */
    public ActionForward execute(ActionMapping mapping, 
                                 ActionForm actionForm, 
                                 HttpServletRequest request, 
                                 HttpServletResponse response) 
        throws Exception {
        ActionErrors errors = new ActionErrors();
        DynaActionForm form = (DynaActionForm)actionForm;
        
        // for auditing purposes, get the operator's username
        User operator = (User)request.getSession().getAttribute(AuthenticateFilter.INDICATOR_NAME);
        String operatorName = operator.getUsername();
        String clientIp = request.getRemoteHost();
        log.trace("About to perform subscriber search for " + operatorName);
        
        // get available services
        findSubscribers(operator,form,errors);

        String forward = null;
        if (errors.size() > 0) {
            log.trace("SubscriberSearchAction failed for operator " + operatorName);
            saveErrors(request,errors);
            forward = Forwards.FAILURE_FORWARD;
        }
        else {
            forward = Forwards.SEARCH_RESULT;
        }
        
        return mapping.findForward(forward);
    }
    
    private void findSubscribers(User user, DynaActionForm form, ActionErrors errors) {
        SubscriberInfo[] subscribers = SubscriberSearch.findAll(user.getUsername(),
                                                                user.getPassword());
        
        if ((subscribers == null) || (subscribers.length == 0)) {
            log.warn("No subscribers found.");
            errors.add(ActionErrors.GLOBAL_ERROR,
                       new ActionError("subscriberSearch.noResults", null));
            return;
        }
        
        // Successfully retrieved subscribers, insert into form for display
        ETTXSubscriberBean[] beans = new ETTXSubscriberBean[subscribers.length];
        
        for (int i=0; i < subscribers.length; i++) {
            beans[i] = new ETTXSubscriberBean();
            beans[i].setForename(subscribers[i].getGivenName());
            beans[i].setSurname(subscribers[i].getFamilyName());
            beans[i].setSubscriberId(subscribers[i].getSubscriberId());
            
            String[] subscribedServices = subscribers[i].getSubscribedServices();
            if ((subscribedServices != null) && (subscribedServices.length > 0)) {
                beans[i].setServiceName(subscribedServices[0]);
            }
            else {
                if (log.isDebugEnabled()) {
                    log.debug("Subscriber " + subscribers[i].getSubscriberId() + 
                              " has no subscribed services.");
                }
            }
        }

        // add subscriber bean to the form
        form.set("searchResult",beans);
        if (log.isDebugEnabled()) {
            log.debug("Successfully inserted " + String.valueOf(beans.length) 
                      + " search results into the form.");
        }
    }
}

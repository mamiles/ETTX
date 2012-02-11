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

import com.cisco.ettx.provisioning.application.SMCCache;
import com.cisco.ettx.provisioning.Audit;
import com.cisco.ettx.provisioning.authentication.AuthenticateFilter;
import com.cisco.ettx.provisioning.authentication.User;
import com.cisco.sesm.sms.types.ServiceInfo;
import com.cisco.sesm.sms.types.SwitchInfo;

/** 
 * This action should be called prior to presenting the registration wizard.
 * This will prepopulate the form (which should be set with session scope)
 * with any details known such as current country, available services,
 * available switches, etc.
 * <p>
 * This action will then forward to the first page of the wizard, which is
 * specified by the parameter attribute on the action in the Struts config
 * file.
 *
 * @author  pcurren
 */
public class PrepareRegistrationWizardAction extends Action {
    
    /**
     * The <code>Log</code> instance for this application.
     */
    private Log log =
        LogFactory.getLog("com.cisco.ettx.provisioning.action.PrepareRegistrationWizardAction");
    
    
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
        
        // if no parameter configured for the action in Struts then we don't
        // know the first page of the wizard to forward to.
        String forwardParam = mapping.getParameter();
        if ((forwardParam == null) || (forwardParam.length() == 0)) {
            log.error("No parameter configured in the Struts config indicating the first page of the wizard.");
            errors.add(ActionErrors.GLOBAL_ERROR, 
                       new ActionError("registration.errors.missingFirstPage"));
        }

        // for auditing purposes, get the operator's username
        User operator = (User)request.getSession().getAttribute(AuthenticateFilter.INDICATOR_NAME);
        String operatorName = operator.getUsername();
        String clientIp = request.getRemoteHost();
        log.trace("Preparing RegistrationWizard first page for operator " + operatorName);
        
        // ensure the form is cleared of any existing data
        form.initialize(mapping);
        
        // Set current locale, so that country is suggested on form
        Locale locale = getLocale(request);
        form.set("country",locale.getCountry());
        
        // get available services
        populateServices(operator,form,errors);

        // get available switches
        populateSwitches(operator,form,errors);
        
        String forward = null;
        if (errors.size() > 0) {
            log.trace("Preparation of RegistrationWizard failed for operator " + operatorName);
            saveErrors(request,errors);
            forward = Forwards.FAILURE_FORWARD;
        }
        else {
            forward = Utils.getForwardForTitle(forwardParam);
        }
        
        return mapping.findForward(forward);
    }
    
    
    /**
     * Find the available services from the SMCCache and insert the data into
     * the form.
     * <p>
     * If there is an error getting this information then insert an error into
     * the supplied ActionErrors parameter
     */
    private void populateServices(User user, DynaActionForm form, ActionErrors errors) {
        log.trace("Populating available services for operator " + user.getUsername());
        ServiceInfo[] services = SMCCache.getInstance().getAvailableServices(user.getUsername(), user.getPassword());
        if (services == null) {
            errors.add(ActionErrors.GLOBAL_ERROR, 
                       new ActionError("registration.errors.failedServicesRetrieval", 
                                       new String[] { user.getUsername() }));
            return;
        }

        // successfully retrieved services, now translate them into a bean
        // suitable for display in the jsp
        ETTXServiceBean[] serviceBeans = new ETTXServiceBean[services.length];
        for (int i=0; i < serviceBeans.length; i++) {
            serviceBeans[i] = Utils.convertToETTXServiceBean(services[i]);
        }
        form.set("availableServices",serviceBeans);
        if (log.isDebugEnabled()) {
            log.debug("Successfully inserted " + String.valueOf(serviceBeans.length) 
                      + " services into the form for user " + user.getUsername());
        }
        
        // preselect first service in array
        if (serviceBeans.length > 0) {
            form.set("selectedService", serviceBeans[0].getServiceName());
            log.debug("Preselected service " + serviceBeans[0].getServiceName() +
                      " for user " + user.getUsername());
        }
    }

    /**
     * Find the available switches from the SMCCache and insert the data into
     * the form.
     * <p>
     * If there is an error getting this information then insert an error into
     * the supplied ActionErrors parameter
     */
    private void populateSwitches(User user, DynaActionForm form, ActionErrors errors) {
        log.trace("Populating available services for operator " + user.getUsername());
        SwitchInfo[] switches = SMCCache.getInstance().getAvailableSwitches(user.getUsername(), user.getPassword());
        if (switches == null) {
            errors.add(ActionErrors.GLOBAL_ERROR, 
                       new ActionError("registration.errors.failedSwitchRetrieval", 
                                       new String[] { user.getUsername() }));
            return;
        }

        // successfully retrieved services, now translate them into a bean
        // suitable for display in the jsp
        ETTXSwitchBean[] switchBeans = new ETTXSwitchBean[switches.length];
        for (int i=0; i < switchBeans.length; i++) {
            switchBeans[i] = Utils.convertToETTXSwitchBean(switches[i]);
        }
        form.set("availableSwitches",switchBeans);
        if (log.isDebugEnabled()) {
            log.debug("Successfully inserted " + String.valueOf(switchBeans.length) 
                      + " switchess into the form for user " + user.getUsername());
        }
    }
}

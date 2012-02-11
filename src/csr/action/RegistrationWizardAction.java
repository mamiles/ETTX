/*
 * RegistrationAction.java
 *
 * Created on 26 April 2003, 16:45
 */

package com.cisco.ettx.provisioning.action;

import java.lang.StringBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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

import com.cisco.ettx.provisioning.application.RegistrationApplication;
import com.cisco.ettx.provisioning.application.ProvisioningCheck;
import com.cisco.ettx.provisioning.application.ProvisioningException;
import com.cisco.ettx.provisioning.application.SecurityException;
import com.cisco.ettx.provisioning.application.SubscriberDefaultSuggestor;
import com.cisco.ettx.provisioning.Audit;
import com.cisco.ettx.provisioning.authentication.AuthenticateFilter;
import com.cisco.ettx.provisioning.authentication.User;
import com.cisco.ettx.provisioning.progress.TaskProgress;
import com.cisco.ettx.provisioning.progress.TaskProgressStore;
import com.cisco.ettx.provisioning.progress.TaskState;
import com.cisco.ettx.provisioning.progress.StrutsTaskProgress;
import com.cisco.sesm.sms.types.ServiceFeatureInfo;
import com.cisco.sesm.sms.types.ServiceFeaturesListInfo;
import com.cisco.sesm.sms.types.SubscriberInfo;

/**
 * This action manages the information gathering for subscriber registration
 * before finally kicking off the registration process.
 * <p>
 * Each page of the registration process will submit to this action, where if
 * required for that step, action will be taken and the approriate response
 * given. e.g. after credit card gathering step, a credit check may want to be
 * performed against the billing service.
 * <p>
 * This action makes use of two DynaBeans - one that will be updated with 
 * subscriber details, and another that will be updated with service selection
 * and available services.
 *
 * @author  pcurren
 */
public class RegistrationWizardAction extends Action {
    
    private static String TITLE_DETAILS = "registration.details.title";
    private static String TITLE_PAYMENT = "registration.payment.title";
    private static String TITLE_LOGIN   = "registration.login.title";
    private static String TITLE_SERVICE = "registration.service.title";
    private static String TITLE_SUMMARY = "registration.summary.title";

    /**
     * The <code>Log</code> instance for this application.
     */
    private Log log =
        LogFactory.getLog("com.cisco.ettx.provisioning.action.RegistrationAction");

    /**
     * Decide on the stage of registration we are at, and call out to the 
     * appropriate helper method. Registration stage is figured out from a
     * property in the actionForm called <i>pageTitle</i>.
     * <p>
     * The first thing that is done before any other work is to ensure that
     * the user hasn't chosen to cancel the process. If cancel is selected
     * (indicated by the <i>submit</i> property of the ActionForm) then any 
     * details stored in the form are cleared before forwarding back to the
     * main page of the application.
     */
    public ActionForward execute(ActionMapping mapping, 
                                 ActionForm actionForm, 
                                 HttpServletRequest request, 
                                 HttpServletResponse response) 
        throws Exception {
            
        DynaActionForm form = (DynaActionForm)actionForm;
        
        // get the application resource for registration.button.cancel
        MessageResources messages = getResources(request);
        ActionErrors errors = new ActionErrors();
        String forward = null;
        
        // for auditing purposes, get the operator's username
        User operator = (User)request.getSession().getAttribute(AuthenticateFilter.INDICATOR_NAME);
        String operatorName = operator.getUsername();
        String clientIp = request.getRemoteHost();
        // decide what to do based on the title of the page submitted
        String pageTitle = (String)form.get("pageTitle");

        if (isCancelled(request)) {
            forward = processCancelRegistration(operatorName, clientIp, form, mapping, errors);
        }
        else {
            // submit button was pressed - not cancel
            if (TITLE_DETAILS.equals(pageTitle)) {
                // no business logic type validation to be done
                // just move onto credit card details
                forward = processSubscriberDetails(operatorName,clientIp,form,errors);
                if (errors.size() == 0) {
                }
            }
            else if (TITLE_PAYMENT.equals(pageTitle)) {
                // Check with billing system on validity of credit card details
                // etc
                forward = processPaymentDetails(form,errors);
            }
            else if (TITLE_LOGIN.equals(pageTitle)) {
                forward = processLoginDetails(form,errors);
            }
            else if (TITLE_SERVICE.equals(pageTitle)) {
                forward = processServiceDetails(operator,form,errors);
            }
            else if (TITLE_SUMMARY.equals(pageTitle)) {
                forward = performRegistration(operator, request, form, errors);
                // probably also need to supply request so progress checking
                // can be performed.
                // on successful instigation the returned forward will be the
                // progress checking page
                // otherwise it will be a return to the wizard
             }
            else {
                // a page has been submitted with no page title - can't process it
                errors.add(ActionErrors.GLOBAL_ERROR, 
                           new ActionError("registration.errors.unknownPage"));
            }
        }
        
        ActionForward fwrd = null;
        if (errors.size() > 0) {
            saveErrors(request,errors);
            // on error go back to the page that was just submitted
            String failureFwrd = Utils.getForwardForTitle(pageTitle);
            fwrd = mapping.findForward(failureFwrd);
        }
        else {
            fwrd = mapping.findForward(forward);
        }
        
        return fwrd;
    }
    
    
    /**
     * Process the cancellation of a registration wizard
     */
    private String processCancelRegistration(String operator, String client,
                                             DynaActionForm form,
                                             ActionMapping mapping,
                                             ActionErrors errors) {

        String subscriberName = (String)form.get("username");
        // ToDo: the audit string should be looked up from the resource bundle
        StringBuffer message = new StringBuffer("Registration wizard cancelled for subscriber '");
        message.append(Utils.getUserFullName(form));
        message.append("'");
        Audit.record(operator,client,message.toString());
        form.initialize(mapping);
        return Forwards.HOME_FORWARD;
    }
    
    /**
     * Process a submitted subscriber details form.
     */
    private String processSubscriberDetails(String operator, String client, 
                                            DynaActionForm form, 
                                            ActionErrors errors) {
        // ToDo: the audit string should be looked up from the resource bundle
        StringBuffer message = new StringBuffer("Running registration wizard for subscriber '");
        message.append(Utils.getUserFullName(form));
        message.append("'");
        Audit.record(operator,client,message.toString());
        
        // we now have enough details to suggest a username and screenname for
        // display later in the wizard
        SubscriberInfo info = Utils.populateSubscriberInfoFromForm(null,form,true);
        String[] usernames = SubscriberDefaultSuggestor.suggestUsernames(info);
        String[] screennames = SubscriberDefaultSuggestor.suggestScreennames(info);
        form.set("username",usernames[0]);
        form.set("screenName",screennames[0]);
        
        String nextTitle = (String)form.get("nextTitle");
        return Utils.getForwardForTitle(nextTitle);
    }
    
    /**
     * Process a submitted payment details form. This processing should not include
     * instigation of billing. That does not happen until registration is completed.
     *
     * @param form the submitted form, which will contain the payment details
     * @param errors the ActionErrors object to be updated with any errors that
     * occur
     * @return the forward appropriate for the result of the processing. It should
     * be noted, that the forward will not necessarily be used. If the errors
     * object contains errors then the failure forward will be used in preference.
     */
    private String processPaymentDetails(DynaActionForm form, ActionErrors errors) {
        // no billing system in place so can't do anything with checking of 
        // credit card number.
       String nextTitle = (String)form.get("nextTitle");        
       return Utils.getForwardForTitle(nextTitle);
    }

    /**
     * Process the login details selection form. This method will ensure that the
     * selected username has not already been taken, and do any checking of the
     * entered password that could not be accomplished by the validator.
     */
    private String processLoginDetails(DynaActionForm form, ActionErrors errors) {
        // PAC: Hard code failure
        String username = (String)form.get("username");
        /*
        errors.add(ActionErrors.GLOBAL_ERROR, 
                   new ActionError("registration.errors.usernameTaken", 
                                   new String[] { username }));
        return Forwards.FAILURE_FORWARD;
         */
       String nextTitle = (String)form.get("nextTitle");        
       return Utils.getForwardForTitle(nextTitle);        
    }

    /**
     * Process the service selection form.
     * <p>
     * At the moment this simply performs a check that the port entered is available
     * for use. Should it not be possible to discover this then the fallback will be
     * to state that the port is available, allowing the chance that registration
     * could continue successfully.
     */
    private String processServiceDetails(User operator, DynaActionForm form, ActionErrors errors) {
        // get switch and port information from form
        String switchId = (String)form.get("selectedSwitch");
        String port = (String)form.get("selectedPort");
        
        boolean assigned = true;
        
        try {
            assigned = ProvisioningCheck.isPortAssigned(operator.getUsername(), operator.getPassword(),
                                                         switchId, port);
        }
        catch (SecurityException ex) {
            assigned = true;
        }
        catch (ProvisioningException ex) {
            assigned = true;
        }
        
        if (assigned) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                       new ActionError("registration.errors.portInUse",
                                       new String[] { port }));
            return Forwards.FAILURE_FORWARD;
        }
        
       String nextTitle = (String)form.get("nextTitle");        
       return Utils.getForwardForTitle(nextTitle);        
    }
    
    /**
     * Kick off the thread that will perform registration and redirect to the
     * progress reporting forward. Any errors instigating registration will be
     * put into the supplied ActionErrors.
     * <p>
     * This method is currently a hole for error reporting - it doesn't, 
     * whereas everything around it does. Fix this when you have time donut
     * head.
     */
    private String performRegistration(User user, HttpServletRequest request, 
                                       DynaActionForm form, ActionErrors errors) {
        // first convert the DynaActionForm to a SubscriberInfo object
        SubscriberInfo subscriber = new SubscriberInfo();
        Utils.populateSubscriberInfoFromForm(subscriber,form,true);
        
        // now build the ServiceFeatureInfos from the service selection
        ETTXServiceBean[] services = (ETTXServiceBean[])form.get("availableServices");
        String selectedService = (String)form.get("selectedService");
        
        ServiceFeatureInfo[] features = null;
        for (int i=0; i < services.length; i++) {
            if (selectedService.equals(services[i].getServiceName())) {
                // now have the selected service, how many features does it have?
                ETTXServiceFeatureBean[] enteredFeatures = services[i].getFeatures();
                if (enteredFeatures == null)
                    break;
                    
                features = new ServiceFeatureInfo[enteredFeatures.length];
                
                // go through each feature getting it's name, and it's value
                for (int j=0; j < enteredFeatures.length; j++) {
                    features[j] = new ServiceFeatureInfo();
                    features[j].setServiceFeatureName(enteredFeatures[j].getFeatureName());
                    features[j].setSelectedValue(enteredFeatures[j].getSelectedValue());
                }
            }
        }
        
        if (features == null) {
            log.error("Problems parsing selected service from form bean.");
            log.error("There is no error handling for this condition since the validator should have ensured this never occurred. Fix the validator bozo.");
            // no good error handling - validation should make sure this doesn't
            // happen
        }
        
        ServiceFeaturesListInfo sflo = new ServiceFeaturesListInfo();
        sflo.setServiceName(selectedService);
        sflo.setServiceFeatures(features);
                                           
        StrutsTaskProgress prog = TaskProgressStore.getInstance().getNewTaskProgress();
        // look up task name from Resource bundle.
        MessageResources messages = getResources(request);
        String taskName = messages.getMessage("registration.task.name");
        
        prog.setName(taskName);
        prog.setTimeout(300);  // how long until the task is considered jammed in seconds
        prog.setPercentageComplete(0);

        // the forwards to be used for each state the task could be in
        Map actions = new HashMap();
        // just use default forward for RUNNING state
        //actions.put(TaskState.RUNNING,"task/forwards/pactestRunning");
        actions.put(TaskState.COMPLETE,Forwards.REGISTRATION_COMPLETE);
        actions.put(TaskState.FAILED,Forwards.REGISTRATION_FAILED);
        prog.setActions(actions);
        
        RegistrationApplication application = 
            new RegistrationApplication(RegistrationApplication.Operation.REGISTER,
                                        user.getUsername(), user.getPassword(),
                                        request.getRemoteHost(), subscriber, 
                                        sflo, prog);
        application.start();
        
        // finally, encode the taskProgressId in the request and forward to
        // the progressCheck forward
        request.setAttribute(ProgressCheckAction.TASK_ID, new Long(prog.getId()).toString());
        return Forwards.PROGRESS_CHECK_FORWARD;
    }
    
    
}

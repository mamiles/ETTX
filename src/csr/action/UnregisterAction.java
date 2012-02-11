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
public class UnregisterAction extends Action {
    
    /**
     * The <code>Log</code> instance for this application.
     */
    private Log log =
        LogFactory.getLog("com.cisco.ettx.provisioning.action.UnregisterAction");

    public ActionForward execute(ActionMapping mapping, 
                                 ActionForm actionForm, 
                                 HttpServletRequest request, 
                                 HttpServletResponse response) 
        throws Exception {
            
        DynaActionForm form = (DynaActionForm)actionForm;
        
        ActionErrors errors = new ActionErrors();
        String forward = null;
        
        // for auditing purposes, get the operator's username
        User operator = (User)request.getSession().getAttribute(AuthenticateFilter.INDICATOR_NAME);
        String operatorName = operator.getUsername();
        String clientIp = request.getRemoteHost();

        // get subscriber details from form
        String subscriberId = (String)form.get("subscriber");
        String serviceName = null;
        
        // work out the subscriber service by finding this subscriberId in the form's
        // array
        ETTXSubscriberBean[] subscribers = (ETTXSubscriberBean[])form.get("searchResult");
        for (int i=0; i < subscribers.length; i++) {
            String sId = subscribers[i].getSubscriberId();
            if (sId.equals(subscriberId)) {
                serviceName = subscribers[i].getServiceName();
                break;
            }
        }
        
        if ((serviceName == null) || (serviceName.length() == 0)) {
            log.warn("No service found for subscriber " + subscriberId);
            errors.add(ActionErrors.GLOBAL_ERROR, 
                       new ActionError("unregistration.task.noService", new String[] { subscriberId }));
        }
        else {
            // begin unregistration task
            Audit.record(operatorName,clientIp,"Unregistering subscriber " + subscriberId);

            StrutsTaskProgress prog = TaskProgressStore.getInstance().getNewTaskProgress();
            // look up task name from Resource bundle.
            MessageResources messages = getResources(request);
            String taskName = messages.getMessage("unregistration.task.name");
        
            prog.setName(taskName);
            prog.setTimeout(300);  // how long until the task is considered jammed in seconds
            prog.setPercentageComplete(0);

            // the forwards to be used for each state the task could be in
            Map actions = new HashMap();
            // just use default forward for RUNNING state
            //actions.put(TaskState.RUNNING,"task/forwards/pactestRunning");
            actions.put(TaskState.COMPLETE,Forwards.UNREGISTRATION_COMPLETE);
            actions.put(TaskState.FAILED,Forwards.UNREGISTRATION_FAILED);
            prog.setActions(actions);
        
            ServiceFeaturesListInfo sflo = new ServiceFeaturesListInfo();
            sflo.setServiceName(serviceName);
            SubscriberInfo subInfo = new SubscriberInfo();
            subInfo.setSubscriberId(subscriberId);
            
            RegistrationApplication application = 
                new RegistrationApplication(RegistrationApplication.Operation.UNREGISTER,
                                            operator.getUsername(), operator.getPassword(),
                                            request.getRemoteHost(), subInfo, 
                                            sflo, prog);
            application.start();
        
            // finally, encode the taskProgressId in the request and forward to
            // the progressCheck forward
            request.setAttribute(ProgressCheckAction.TASK_ID, new Long(prog.getId()).toString());
            forward = Forwards.PROGRESS_CHECK_FORWARD;
        }
        
        
        ActionForward fwrd = null;
        if (errors.size() > 0) {
            saveErrors(request,errors);
            // on error go back to the page that was just submitted
            fwrd = mapping.findForward(Forwards.FAILURE_FORWARD);
        }
        else {
            fwrd = mapping.findForward(forward);
        }
        
        return fwrd;
    }
}

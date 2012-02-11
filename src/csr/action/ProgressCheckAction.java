/*
 * ProgressCheckAction.java
 *
 * Created on 18 April 2003, 16:45
 */

package com.cisco.ettx.provisioning.action;

import java.util.Iterator;
import java.util.Locale;
import java.util.List;
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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.ModuleException;
import org.apache.struts.util.MessageResources;

import com.cisco.ettx.provisioning.Config;
import com.cisco.ettx.provisioning.progress.ProgressMessage;
import com.cisco.ettx.provisioning.progress.TaskProgressStore;
import com.cisco.ettx.provisioning.progress.TaskState;
import com.cisco.ettx.provisioning.progress.StrutsTaskProgress;

/**
 * Will check the progress on an identified <code>TaskProgressStore</code>.
 * The <code>TaskProgressStore</code> is identified by the <code>TASK_ID</code>
 * attribute value within the request.
 *
 * @author  pcurren
 */
public class ProgressCheckAction extends Action {
    
    public static String TASK_ID             = "TASKID";
//    public static String PROGRESS_MESSAGES   = "MESSAGES";
    public static String TASK_NAME           = "TASKNAME";
    public static String PERCENTAGE_COMPLETE = "PERCENTAGE";
    public static String FINISH_CODE         = "FINISHCODE";
    
    /**
     * The <code>Log</code> instance for this application.
     */
    private Log log =
        LogFactory.getLog("com.cisco.ettx.provisioning.action.ProgressCheckAction");
    
    
    /**
     * Retrieve a <code>TaskProgress</code> from the store as identified in the
     * request, and forward as specified depending on the state.
     * <p>
     * Various attributes of the <code>TaskProgress</code> will be placed in
     * the request for use by the JSP that is called by the forward.
     */
    public ActionForward execute(ActionMapping mapping, 
                                 ActionForm actionForm, 
                                 HttpServletRequest request, 
                                 HttpServletResponse response) 
        throws Exception {
            
        // try and get task id from the form if it exists
        DynaActionForm form = (DynaActionForm)actionForm;        
        String str = (String)form.get("taskId");

        ActionErrors errors = new ActionErrors();                                
        if ((str == null) || (str.length() == 0)) {
            log.debug("Task Progress Id could not be retrieved from the form. Checking request instead.");
            // try to get the task id from the request
            str = (String)request.getAttribute(TASK_ID);
            
            if ((str == null) || (str.length() == 0))  {
                log.error("Could not retrieve TaskProgress id from request. No progress check can be performed.");
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("task.progress.checkerror.id"));
            } 
        }
        
        if (errors.size() == 0) {
            // ok so far
            StrutsTaskProgress prog = null;
            try {
                long taskId = (Long.valueOf(str)).longValue();
                prog = (StrutsTaskProgress)TaskProgressStore.getInstance().getTaskProgress(taskId);
            }
            catch (NumberFormatException nfex) {
                log.error("The taskid was not a String representing a valid long.");
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("task.progress.checkerror.id"));
            }
        
            if (prog == null) {
                log.error("The identified TaskProgress was not found within the store.");
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("task.progress.checkerror.noTask"));
            }
            else {
                // store some relevant information from the TaskProgress to the request
                // for use by the page we forward to
                request.setAttribute(TASK_NAME, prog.getName());
                if (prog.getPercentageComplete() != null) {
                    request.setAttribute(PERCENTAGE_COMPLETE, prog.getPercentageComplete());
                }

                List allMessages = prog.getAllProgressMessages();
                if (allMessages != null) {
                    // convert all the ProgressMessages to Struts ActionMessages
                    ActionMessages messages = new ActionMessages();
                    synchronized(allMessages) {
                        Iterator it = allMessages.iterator();
                        while (it.hasNext()){
                            ProgressMessage msg = (ProgressMessage)it.next();
                            messages.add(ActionMessages.GLOBAL_MESSAGE, 
                                         new ActionMessage(msg.getKey(), msg.getParams()));
                        }
                    }
                    saveMessages(request,messages);
                }
                if (prog.getFinishedCode() != null) {
                    request.setAttribute(FINISH_CODE, prog.getFinishedCode());
                }   
        
                // now have TaskProgress - find the forward appropriate for the task's current
                // state
                TaskState state = prog.getState();
                
                // note: if task is in COMPLETE, RUNNING or TIMEDOUT state then
                // remove it from the store
                if ((state == TaskState.COMPLETE) || (state == TaskState.FAILED) ||
                    (state == TaskState.TIMEDOUT)) {
                    TaskProgressStore.getInstance().removeTaskProgress(prog.getId());
                }
                
                String forward = prog.getAction(state);
        
                if (forward == null) {
                    log.error("There was no forward for the task in state " + state.toString() + " stored either in the task or in configuration.");
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("task.progress.checkerror.noForward", state));
                }
                else {
                    ActionForward fwrd = mapping.findForward(forward);
        
                    if (fwrd == null) {
                        log.error("There was no forward configured in struts for " + forward + " as specified by the task or configuration.");
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("task.progress.checkerror.noForwardConfigured", forward));
                    }
                    else {
                        // Successful path of execution
                        return fwrd;
                    }
                }
            }
        }

        saveErrors(request,errors);
        return mapping.findForward(Forwards.FAILURE_FORWARD);
    }
}

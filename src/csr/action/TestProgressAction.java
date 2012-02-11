/*
 * Test action
 *
 * Created on 18 April 2003, 16:45
 */

package com.cisco.ettx.provisioning.action;

import java.lang.Runnable;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;
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
import com.cisco.ettx.provisioning.progress.TaskProgress;
import com.cisco.ettx.provisioning.progress.TaskProgressStore;
import com.cisco.ettx.provisioning.progress.TaskState;
import com.cisco.ettx.provisioning.progress.StrutsTaskProgress;

/**
 * Just a stupid test action to test how task progress reporting works.
 * @author  pcurren
 */
public class TestProgressAction extends Action {
    
    /**
     * The <code>Log</code> instance for this application.
     */
    private Log log =
        LogFactory.getLog("com.cisco.ettx.provisioning.action.TestProgressAction");
    

    class KidOnApplicationThread extends Thread {
        private TaskProgress m_progress;
        
        public KidOnApplicationThread(String name, TaskProgress progress) {
            super(name);
            m_progress = progress;
        }
        
        public void run() {

            try {
            // update state to say that execution has started
            m_progress.setState(TaskState.RUNNING);
            String[] params = new String[] { m_progress.getName() };
            m_progress.setProgressMessageKey("task.progress.started",params);
            m_progress.setPercentageComplete(0);
            
            sleep(3000);
            
            m_progress.setState(TaskState.RUNNING);
            params = new String[] {"Some work 1"};
            m_progress.setProgressMessageKey("task.progress.PACtesting",params);
            m_progress.setPercentageComplete(20);
            
            sleep(10000);
            
            m_progress.setState(TaskState.RUNNING);
            params = new String[] {"Some work 2"};
            m_progress.setProgressMessageKey("task.progress.PACtesting",params);
            m_progress.setPercentageComplete(50);            
            
            sleep(12000);
            
            m_progress.setState(TaskState.RUNNING);
            params = new String[] {"Some work 3"};
            m_progress.setProgressMessageKey("task.progress.PACtesting",params);
            m_progress.setPercentageComplete(80);            
            
            sleep(8000);
            m_progress.setState(TaskState.COMPLETE);
            params = new String[] {m_progress.getName()};
            m_progress.setProgressMessageKey("task.progress.success",params);
            m_progress.setPercentageComplete(100);
            m_progress.setFinishedCode(new Integer(999));
            }
            catch (InterruptedException ex) {
                log.error("KidOnApplicationThread interupted.");
            }
        }
    }
    
    
    
    /**
     */
    public ActionForward execute(ActionMapping mapping, 
                                 ActionForm actionForm, 
                                 HttpServletRequest request, 
                                 HttpServletResponse response) 
        throws Exception {

        // emulate the kicking off of some work, such as subscriber registration
        StrutsTaskProgress prog = TaskProgressStore.getInstance().getNewTaskProgress();
        
        prog.setName("PAC_TEST_TASK");
        prog.setTimeout(600);
        prog.setPercentageComplete(0);

        Map actions = new HashMap();
        //actions.put(TaskState.RUNNING,"task/forwards/pactestRunning");
        actions.put(TaskState.COMPLETE,"task/forwards/pactestComplete");
        actions.put(TaskState.FAILED,"task/forwards/pactestFailed");
        prog.setActions(actions);
        
        KidOnApplicationThread application = new KidOnApplicationThread("KidOnApplicationThread",prog);
        application.start();
        
        // finally, encode the taskProgressId in the request and forward to
        // the progressCheck forward
        request.setAttribute(ProgressCheckAction.TASK_ID, new Long(prog.getId()).toString());
        return mapping.findForward(Forwards.PROGRESS_CHECK_FORWARD);
    }
}

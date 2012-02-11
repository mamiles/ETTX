/*
 * StrutsTaskProgress.java
 *
 * Created on 25 April 2003, 18:44
 */

package com.cisco.ettx.provisioning.progress;

import com.cisco.ettx.provisioning.Config;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * This class extends the standard <code>TaskProgress</code> adding properties
 * indicating the Struts action to perform for various possible states
 *
 * @author  pcurren
 */
public class StrutsTaskProgress extends TaskProgress {
    
    private Map m_actions;
    
    /** Creates a new instance of StrutsTaskProgress */
    protected StrutsTaskProgress() {
        super();
    }
    
    /** Set a Map of Strings representing the Struts action to be performed for
     * each possible task state. Any states unspecified will use the default
     * action from configuration for the relevant state.
     * <p>
     * Any existing mappings are removed.
     *
     * @param actions a Map of actions as Strings, keyed on the TaskState it
     * should be associated with
     */
    public void setActions(Map actions) {
        m_actions = actions;
        m_lastAccess = new GregorianCalendar();
    }
    
    /** 
     * Get the Struts action to be used for the specified TaskState. If the
     * specified state does not have an action then the default value from
     * configuration will be used. If there is no default value configured
     * then null will be returned.
     */
    public String getAction(TaskState state) {
        if (state == null) {
            m_lastAccess = new GregorianCalendar();
            return null;
        }
        
        String action = null;
        synchronized(m_actions) {
            if (!m_actions.containsKey(state)) {
                // get default from config
                action = Config.getInstance().getTaskStateForward(state.toString());
            }
            else {
                action = (String)m_actions.get(state);
            }
        }
            
        m_lastAccess = new GregorianCalendar();
        return action;
    }
    
    /** 
     * Get the action for the current state of the TaskProgress. As with
     * <code>getAction(TaskState)</code>, if none is configured the value from config is
     * used. If it also doesn't exist then null is returned.
     */
    public String getAction() {
        return getAction(m_state);
    }
}

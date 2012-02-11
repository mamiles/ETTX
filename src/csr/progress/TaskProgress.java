/*
 * TaskProgress.java
 *
 * Created on 24 April 2003, 01:00
 */

package com.cisco.ettx.provisioning.progress;

import com.cisco.ettx.provisioning.Config;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class that models the progress of a task 
 *
 * @author  pcurren
 */
public class TaskProgress {
    
    /** The default timeout in seconds given to a new task progress. This can be 
     * overridden by the user of the TaskProgress. The timeout is how long a
     * task must go without being accessed (a setter called) before it is 
     * considered timedout.
     */
    private static int DEFAULT_TIMEOUT = 120;
    
    private long m_id;
    private String m_name = null;
    protected TaskState m_state = null;
    private Integer m_finalCode = null;
    private Integer m_percentageComplete = null;
    private List m_progressMessages = new ArrayList();
    private int m_timeout = DEFAULT_TIMEOUT;
    protected Calendar m_creationTime = null;
    protected Calendar m_lastAccess = null;
    protected Timer m_timer = null;
    
   private class TimeoutTimerTask extends TimerTask {
        private TaskProgress m_taskProgress = null;
        public TimeoutTimerTask(TaskProgress tp) {
            m_taskProgress = tp;
        }
        
        // if the task is not complete or failed, then move it to timedout
        public void run() {
            synchronized (m_taskProgress) {
                if ((m_taskProgress.getState() != TaskState.COMPLETE) &&
                    (m_taskProgress.getState() != TaskState.RUNNING) &&
                    (m_taskProgress.getState() != TaskState.NEW)) {
                    m_taskProgress.setState(TaskState.TIMEDOUT);
                }
            }
        }
    }
    
    /** Creates a new instance of TaskProgress. The state of this task will
     * initially be NEW
     */
    protected TaskProgress() {
        m_creationTime = new GregorianCalendar();
        m_state = TaskState.NEW;
        
        // call TaskProgressStore to get id
        m_id = TaskProgressStore.getTaskId();
        m_lastAccess = new GregorianCalendar();
    }
    
    /** 
     * Update the current state of the task
     */
    public synchronized void setState(TaskState state) {
        if ((state == TaskState.COMPLETE) || (state == TaskState.FAILED) ||
            (state == TaskState.TIMEDOUT)) {
            // stop the timer
                if (m_timer != null) {
                    m_timer.cancel();
                    m_timer = null;
                }
            
                if (m_percentageComplete != null) {
                    m_percentageComplete = new Integer(100);
                }
        }

        // all other states, cause the timer to be restarted
        if (m_timer != null) {
            m_timer.cancel();
        }
        
        m_timer = new Timer(true);
        m_timer.schedule(new TaskProgress.TimeoutTimerTask(this), m_timeout * 1000);
        
        m_state = state;
        m_lastAccess = new GregorianCalendar();        
    }
    
    /**
     * Get the current state of the task
     */
    public synchronized TaskState getState() {
        m_lastAccess = new GregorianCalendar();        
        return m_state;
    }
    
    /**
     * Get the name of the task
     */
    public String getName() {
        m_lastAccess = new GregorianCalendar();        
        return m_name;
    }
    
    /**
     * Set the name of the task
     */
    public void setName(String name) {
        m_lastAccess = new GregorianCalendar();
        m_name = name;
    }
    
    /**
     * Get the finished code returned by the task. This value should have 
     * meaning to the application. If the task is not yet finished, then
     * null will be returned. Likewise, the application may not give a finished
     * code.
     */
    public synchronized Integer getFinishedCode() {
        m_lastAccess = new GregorianCalendar();        
        return m_finalCode;
    }
    
    /**
     * Set the FinishedCode for the task
     */
    public synchronized void setFinishedCode(Integer code) {
        m_finalCode = code;
        if (m_timer != null) {
            m_timer.cancel();
        }
        
        m_timer = new Timer(true);
        m_timer.schedule(new TaskProgress.TimeoutTimerTask(this), m_timeout * 1000);
        m_lastAccess = new GregorianCalendar();        
    }
    
    /** Add a progress message resource key, along with any associated 
     * parameters. If the key does not take parameters then null can be
     * supplied or an empty list
     */
    public void setProgressMessageKey(String key, Object[] params) {
        if ((key == null) || (key.length() == 0)) {
            return;
        }
        
        ProgressMessage progMess = new ProgressMessage(key, params);
        
        synchronized(m_progressMessages) {
            m_progressMessages.add(progMess);
        }
        
        if (m_timer != null) {
            m_timer.cancel();
        }
        
        m_timer = new Timer(true);
        m_timer.schedule(new TaskProgress.TimeoutTimerTask(this), m_timeout * 1000);
        m_lastAccess = new GregorianCalendar();                
    }
    
    /**
     * Get the ProgressMessage for the last message on the list. If there is none
     * return null.
     */
    public ProgressMessage getLastProgressMessage() {
        synchronized(m_progressMessages) {
            m_lastAccess = new GregorianCalendar();                    

            if (m_progressMessages.size() == 0) {
                return null;
            }
            return (ProgressMessage)m_progressMessages.get(m_progressMessages.size());
        }
    }
    
    /**
     * Return a list containing all the Progress Messages so far. If there are none
     * then an empty list will be returned
     */
    public List getAllProgressMessages() {
        m_lastAccess = new GregorianCalendar();                    
        return m_progressMessages;
    }
    
    
    /**
     * Get a value indicating the percentage complete for the task.
     * If the task is not using this field, then null will be returned.
     * <p>
     * The returned Integer will never have a value less than 0 or greater
     * than 100
     */
    public synchronized Integer getPercentageComplete() {
        m_lastAccess = new GregorianCalendar();                    
        return m_percentageComplete;
    }
    
    /** 
     * Set the percentage complete for a task. If the supplied value is
     * less than 0 it will be set to 0, and if greater than 100 it will be
     * set to 100
     */
    public synchronized void setPercentageComplete(int percentage) {
        if (percentage < 0) {
            m_percentageComplete = new Integer(0);
        }
        else if (percentage > 100) {
            m_percentageComplete = new Integer(100);
        }
        else {
            m_percentageComplete = new Integer(percentage);
        }
        
        if (m_timer != null) {
            m_timer.cancel();
        }
        
        m_timer = new Timer(true);
        m_timer.schedule(new TaskProgress.TimeoutTimerTask(this), m_timeout * 1000);
        m_lastAccess = new GregorianCalendar();                    
    }
        
    /** Setting a new timeout value resets the timeout timer.
     */
    public synchronized void setTimeout(int seconds) {
        m_timeout = seconds;
        
        if (m_timer != null) {
            m_timer.cancel();
            m_timer = new Timer(true);
            m_timer.schedule(new TaskProgress.TimeoutTimerTask(this), m_timeout * 1000);
        }
        m_lastAccess = new GregorianCalendar();                    
    }
    
    public synchronized int getTimeout() {
        m_lastAccess = new GregorianCalendar();                    
        return m_timeout;
    }

    public long getId() {
        m_lastAccess = new GregorianCalendar();
        return m_id;
    }
    
    
    /**
     * Return when this TaskProgress was last accessed. This call does not 
     * actually count as an access to the object, so will not modify the time
     * unlike all other calls.
     */
    public Calendar getLastAccess() {
       return m_lastAccess; 
    }
    
}



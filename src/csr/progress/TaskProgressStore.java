/*
 * TaskProgressStore.java
 *
 * Created on 25 April 2003, 06:34
 */

package com.cisco.ettx.provisioning.progress;

import com.cisco.ettx.provisioning.Config;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Storage singleton that holds TaskProgress objects keyed by a unique id that
 * has also been generated from this class. This singleton class also has a
 * factory method for creating TaskProgress objects.
 * @author  pcurren
 */
public class TaskProgressStore {
    

    /**
     * The <code>Log</code> instance for this class.
     */
    private Log log 
        = LogFactory.getLog("com.cisco.ettx.provisioning.progress.TaskProgressStore");
    
    // Scavenger class - will remove TaskProgress objects that have been 
    // accidentally left in the cache and not accessed
    private class TaskScavenger extends Thread {

        public TaskScavenger(String name) {
            super(name);
        }
        
        /**
         * Thread run method for scavenging unused TaskProgress objects from the
         * Store.
         */
        public void run() {
            log.debug("Starting TaskProgress scavenging thread");
            
            try {
                while(true) {
                    int checkInt = Config.getInstance().getTaskScavengerSleep();
                    log.debug("TaskProgress scavenging sleeping for " 
                              + String.valueOf(checkInt) + " seconds.");
                    sleep((long)checkInt*1000);

                    int expiryInt = Config.getInstance().getTaskForgottenInterval();
                    log.debug("Ageing out TaskProgress objects older than "
                              + String.valueOf(expiryInt) + " seconds.");
                    ScavengeTaskProgresses(expiryInt);
                }
            }
            catch(InterruptedException iex)
            {
                log.warn("TaskProgress scavenging interrupted:" + iex.getMessage());
                log.debug(iex);
            }
        }

        /**
         * Removes TaskProgress objects from the store that haven't been used 
         * for a period of time.
         *
         * @param agedSeconds an <code>int</code> value
         */
        void ScavengeTaskProgresses(int agedSeconds) {
            log.debug("Scavenging for unused TaskProgress objects.");

            Calendar threshold = new GregorianCalendar();
            threshold.add(Calendar.SECOND, -agedSeconds);
            synchronized(TaskProgressStore.this.m_store) {
                Iterator taskIt = TaskProgressStore.this.m_store.values().iterator();
                while(taskIt.hasNext()) {
                    TaskProgress tp = (TaskProgress)(taskIt.next());

                    // if task has not been accessed recently
                    if (tp.getLastAccess().before(threshold)) {
                        log.debug("Removing TaskProgress " + String.valueOf(tp.getId()) + ":" + tp.getName());
                        taskIt.remove();
                    }
                    else {
                        log.debug("Leaving TaskProgress " + String.valueOf(tp.getId()) + ":" + tp.getName());
                    }
                }
            }
        }
    }
    
    
    TaskScavenger m_scavenger = null;    
    
    private static TaskProgressStore m_instance = new TaskProgressStore();
    private static Random m_randomGenerator = new Random();
    private Map m_store = new HashMap();
    
    /** Creates a new instance of TaskProgressStore */
    private TaskProgressStore() {
    }
    
    public static TaskProgressStore getInstance() {
        return m_instance;
    }
    
    /**
     * Return a task Id, generated from the current time to the nearest 
     * millisecond with a randomly generated number subtracted.
     */
    public static long getTaskId() {
       Date now = new Date();
       long randnum = m_randomGenerator.nextInt();
       
       return now.getTime() - randnum;
    }
    
    /**
     * Get a new TaskProgress. The returned TaskProgress will have already been
     * added to the store and you will be returned a reference to it.
     */
    public StrutsTaskProgress getNewTaskProgress() {
        StrutsTaskProgress tp = new StrutsTaskProgress();
        
        synchronized(m_store) {
            m_store.put(new Long(tp.getId()), tp);
        }
        
        // start the scavenger if it hasn't already been started
        if(m_scavenger == null) {
            m_scavenger = new TaskScavenger("TaskProgressScavenger");
            m_scavenger.start();
        }      
        
        return tp;
    }
    
    /** 
     * Return an existing TaskProgress with the supplied id. If the supplied id
     * cannot be found then return null
     */
    public TaskProgress getTaskProgress(long id) {
        Long lid = new Long(id);
        
        synchronized(m_store) {
            if (m_store.containsKey(lid)) {
                return (TaskProgress)m_store.get(lid);
            }
            else {
                return null;
            }
        }
    }
    
    /** Remove the TaskProgress from the store, we are done with it.
     */
    public void removeTaskProgress(long id) {
        Long lid = new Long(id);
        
        synchronized(m_store) {
            if (m_store.containsKey(lid)) {
                m_store.remove(lid);
            }
        }
    }
}

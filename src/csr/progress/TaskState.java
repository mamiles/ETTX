/*
 * TaskState.java
 *
 * Created on 24 April 2003, 00:49
 */

package com.cisco.ettx.provisioning.progress;

/**
 * An String enum class that represents the state of a task
 *
 * @author  pcurren
 */
public class TaskState {
    
    /** Task has finished successfully */
    public static TaskState COMPLETE = new TaskState("complete");

    /** Task has finished with a failure */
    public static TaskState FAILED = new TaskState("failed");
    
    /** Task is not started yet */
    public static TaskState NEW = new TaskState("new");

    /** Task is paused temporarily - will resume on it's own */
    public static TaskState PAUSED = new TaskState("paused");
    
    /** Task is currently active */
    public static TaskState RUNNING = new TaskState("running");
    
    /** Task was timed-out by the monitor - the task should be considered
     * complete but failed.
     */
    public static TaskState TIMEDOUT = new TaskState("timedout");
    
    /** Task is paused waiting for user input  - <i>unused because i've given no
     * thought as to how this could be implemented</i> 
     */
    public static TaskState WAITING = new TaskState("waiting");
    
    String m_name;
    
    /** Creates a new instance of TaskState */
    private TaskState(String name) {
        m_name = name;
    }
    
    public String toString() {
        return m_name;
    }
    
    public int hashCode() {
        return m_name.hashCode();
    }
    
}

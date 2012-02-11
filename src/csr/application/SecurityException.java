/*
 * SecurityException.java
 *
 * Created on 29 May 2003, 10:58
 */

package com.cisco.ettx.provisioning.application;

/**
 *
 * @author  pcurren
 */
public class SecurityException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>SecurityException</code> without detail message.
     */
    public SecurityException() {
    }
    
    
    /**
     * Constructs an instance of <code>SecurityException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public SecurityException(String msg) {
        super(msg);
    }
}

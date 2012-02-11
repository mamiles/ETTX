/*
 * ProvisioningException.java
 *
 * Created on 29 May 2003, 11:05
 */

package com.cisco.ettx.provisioning.application;

/**
 *
 * @author  pcurren
 */
public class ProvisioningException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>ProvisioningException</code> without detail message.
     */
    public ProvisioningException() {
    }
    
    
    /**
     * Constructs an instance of <code>ProvisioningException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ProvisioningException(String msg) {
        super(msg);
    }
}

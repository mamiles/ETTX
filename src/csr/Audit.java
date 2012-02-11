/*
 * Audit.java
 *
 * Created on 25 April 2003, 19:30
 */

package com.cisco.ettx.provisioning;

import java.lang.StringBuffer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author  pcurren
 */
public class Audit {
    /**
     * The <code>Log</code> instance for this class.
     */
    private static Log log =
        LogFactory.getLog("com.cisco.ettx.provisioning.Audit");
        
    
    /** Creates a new instance of Audit */
    private Audit() {
    }
    
    /**
     * Output to the audit log the specified message.
     */
    public static void record(String user, String client, String message) {
        // may want to append date, etc unless canbe configured to do that
        // automagically through logging
        StringBuffer buf = new StringBuffer("USER ");
        
        if ((user == null) || (user.length() == 0)) {
            buf.append("none : ");
        }
        else {
            buf.append(user);
            buf.append(" : ");
        }
        
        buf.append("CLIENT ");
        if ((client == null) || (client.length() == 0)) {
            buf.append("none : ");
        }   
        else {
            buf.append(client);
            buf.append(" : ");
        }
        
        buf.append(message);
        log.info(buf.toString());
    }
    
}

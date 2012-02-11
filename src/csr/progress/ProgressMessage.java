/*
 * ProgressMessage.java
 *
 * Created on 24 April 2003, 01:58
 */

package com.cisco.ettx.provisioning.progress;

import java.lang.IllegalArgumentException;

/**
 * This class encapsulates a Message key and it's associated parameters.
 * Because this class is intended for use in JSPs in the <code>
 * %lt;bean:message%gt;</code> tag, it will automatically pad any parameters
 * set to contain five entries, with any unspecified entries containing null.
 * This is so the jsp using these <code>ProgressMessages</code> can use all
 * five <code>arg</code> parameters on the <code>%lt;bean:message%gt;</code> tag
 *
 * @author  pcurren
 */
public class ProgressMessage {
    
    private String m_key;
    private Object[] m_params;
    
    /** Creates a new instance of ProgressMessage.
     * If the message mapped to this key does not take parameters then the 
     * params parameter can be either null or empty
     */
    public ProgressMessage(String key, Object[] params) {
        if (key == null) {
            throw new IllegalArgumentException("Message key must not be null.");
        }
        
        m_key = key;
        m_params = params;
    }
    
    public String getKey() {
        return m_key;
    }
    
    public Object[] getParams() {
        return m_params;
    }
    
    public void setParams(Object[] params) {
        m_params = params;
    }
    
    /** Pad the supplied list to the specified size, using nulls for padding.
     */
    /*
    private static List padList(int num, List lst) {
        if (lst == null) {
            lst = new ArrayList(num);
        }

        int padding = num - lst.size();
        for (int i=0; i < padding; i++) {
            lst.add(null);
        }   
        
        return lst;
    }
    */
}

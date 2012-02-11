/*
 * ETTXService.java
 *
 * Created on 29 April 2003, 03:41
 */

package com.cisco.ettx.provisioning.action;

import java.beans.*;

/**
 *
 * @author  pcurren
 */
public class ETTXSubscriberBean extends Object implements java.io.Serializable {
    
    private String m_subscriberId;  
    private String m_forename;
    private String m_surname;
    private String m_serviceName;

    public String getSubscriberId() {
        return this.m_subscriberId;
    }
    
    public void setSubscriberId(String id) {
        this.m_subscriberId = id;
    }
    
    public String getForename() {
        return this.m_forename;
    }
    
    public void setForename(String name) {
        this.m_forename = name;
    }
    
    public String getSurname() {
        return this.m_surname;
    }
    
    public void setSurname(String name) {
        this.m_surname = name;
    }
        
    /** Getter for property serviceName.
     * @return Value of property serviceName.
     */
    public String getServiceName() {
        return this.m_serviceName;
    }
    
    /** Setter for property serviceName.
     * @param serviceName New value of property serviceName.
     */
    public void setServiceName(String serviceName) {
        this.m_serviceName = serviceName;
    }
}

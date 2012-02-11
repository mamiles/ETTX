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
public class ETTXServiceBean extends Object implements java.io.Serializable {
    
    /** Holds value of property serviceName. */
    private String m_serviceName;  
    private String m_serviceDescription;
    private String m_serviceProfileName;
    private ETTXServiceFeatureBean[] m_features;
    private String m_selectedFeatureName;
    
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
    
    public String getServiceDescription() {
        return this.m_serviceDescription;
    }
    
    public void setServiceDescription(String desc) {
        this.m_serviceDescription = desc;
    }
    
    public String getServiceProfileName() {
        return this.m_serviceProfileName;
    }

    public void setServiceProfileName(String prof) {
        this.m_serviceProfileName = prof;
    }
    
    public ETTXServiceFeatureBean[] getFeatures() {
        return this.m_features;
    }
    
    public void setFeatures(ETTXServiceFeatureBean[] features) {
        this.m_features = features;
    }
    
    public String getSelectedFeatureValue() {
        return this.m_selectedFeatureName;
    }
    
    public void setSelectedFeatureValue(String name) {
        this.m_selectedFeatureName = name;
    }
}

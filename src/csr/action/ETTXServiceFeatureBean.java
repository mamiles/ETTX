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
public class ETTXServiceFeatureBean extends Object implements java.io.Serializable {
    
    /** Holds value of property serviceName. */
    private String m_featureName;    
    private String m_selectedValue;
    private String m_defaultValue;
    private String[] m_possibleValues;
    
    /** Getter for property serviceName.
     * @return Value of property serviceName.
     */
    public String getFeatureName() {
        return this.m_featureName;
    }
    
    /** Setter for property serviceName.
     * @param serviceName New value of property serviceName.
     */
    public void setFeatureName(String featureName) {
        this.m_featureName = featureName;
    }
    
    public String getSelectedValue() {
        return this.m_selectedValue;
    }
    
    public void setSelectedValue(String value) {
        this.m_selectedValue = value;
    }
    
    public String getDefaultValue() {
        return this.m_defaultValue;
    }
    
    public void setDefaultValue(String value) {
        this.m_defaultValue = value;
    }
    
    public String[] getPossibleValues() {
        return this.m_possibleValues;
    }
    
    public void setPossibleValues(String[] values) {
        this.m_possibleValues = values;
    }
}

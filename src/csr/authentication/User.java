/*
 * User.java
 *
 * Created on 20 April 2003, 14:55
 */

package com.cisco.ettx.provisioning.authentication;

import com.cisco.cns.security.soap.common.SecurityToken;

/**
 *
 * @author  pcurren
 */
public class User extends Object implements java.io.Serializable {
    
    private java.beans.PropertyChangeSupport propertySupport;
    
    private String m_username;
    private String m_password;
    
    
    /** Creates new User */
    public User() {
        propertySupport = new java.beans.PropertyChangeSupport( this );
    }
    
    /** Getter for property username.
     * @return Value of property username.
     */
    public String getUsername() {
        return m_username;
    }
    
    /** Setter for property username.
     * @param username New value of property username.
     */
    public void setUsername(String username) {
        m_username = username;
    }
    
    /** Getter for property password.
     * @return Value of property password.
     */
    public String getPassword() {
        return m_password;
    }
    
    /** Setter for property password.
     * @param password New value of property password.
     */
    public void setPassword(String password) {
        m_password = password;
    }    
}

/*
 * SPEAuthenticate.java
 *
 * Created on 22 April 2003, 19:23
 */

package com.cisco.ettx.provisioning.authentication;

import com.cisco.cns.security.soap.common.SecurityToken;

/**
 *
 * @author  pcurren
 */
public class SPEAuthenticate {
    
    /** Static class - no constructions necessary */
    private SPEAuthenticate() {
    }
    
    /** 
     * Authenticate the supplied user against the configured SPE
     * authentication manager.
     *
     * @return the SecurityToken for the supplied credentials or null if not
     * authenticated.
     */
    public static SecurityToken authenticate(String username, String password) {
        if ((username == null) || (username.length() == 0) ||
            (password == null) || (password.length() == 0)) {
            return null;
        }
        
        return SecurityTokenCache.getInstance().login(username,password);
    }
    
    /**
     * Logout a SecurityToken which is stored for the supplied username.
     */
    public static void logout(String username) {
        if ((username == null) || (username.length() == 0)) {
            return;
        }        
        
        SecurityTokenCache.getInstance().logout(username);
    }
    
}

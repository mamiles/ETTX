/*
 * ProvisionedCheck.java
 *
 * Created on 05 May 2003, 15:29
 */

package com.cisco.ettx.provisioning.application;

import com.cisco.cns.security.soap.common.SecurityToken;
import com.cisco.ettx.provisioning.authentication.SecurityTokenCache;
import com.cisco.ettx.provisioning.Config;
import com.cisco.sesm.sms.api.Provisioning;
import com.cisco.sesm.sms.exceptions.api.AuthenticationAPIException;
import com.cisco.sesm.sms.exceptions.api.IllegalArgumentAPIException;
import com.cisco.sesm.sms.exceptions.api.ProvisioningAPIException;

import java.rmi.RemoteException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility class relating to checking the provisioned status of ports, etc.
 *
 * @author  pcurren
 */
public class ProvisioningCheck {

    /**
     * The <code>Log</code> instance for this class.
     */
    private static Log log 
        = LogFactory.getLog("com.cisco.ettx.provisioning.application.ProvisioningCheck");    
    
    private ProvisioningCheck() {
    }
  
    /** Check to discover whether a specified port on a switch is assigned to a
     * subscriber already.
     *
     * @throws ProvisioningException if an error occurs from the call to the
     * ProvisioningAPI
     * @throws SecurityException if the supplied credentials cannot be used to 
     * retrieve a SecurityToken, or if the SecurityToken is incorrect.
     */
    public static boolean isPortAssigned(String username, String password,
                                         String switchId, String port)
        throws ProvisioningException, SecurityException {
        if ((username == null) || (username.length() == 0)) {
            String msg = "Could not check isPortAssigned - no username supplied.";
            log.error(msg);
            throw new SecurityException(msg);
        }
        
        if ((password == null) || (password.length() == 0)) {
            String msg = "Could not check isPortAssigned - no password supplied.";
            log.error(msg);
            throw new SecurityException(msg);
        }
        
        SecurityToken token = SecurityTokenCache.getInstance().getSecurityToken(username,password);
        if (token == null) {
            String msg = "Could not check isPortAssigned - no security token found for admin user " + username;
            log.error(msg);
            throw new SecurityException(msg);
        }
        
        // now get Subscriber SOAP stub
        Provisioning provisioningStub = Config.getInstance().getProvisioningStub();
        if (provisioningStub == null) {
            String msg = "Could not check isPortAssigned - no Provisioning Stub endpoint found in configuration.";
            log.error(msg);
            throw new ProvisioningException(msg);
        }                                             
         
        log.trace("Checking assigned status for port " + port + " on switch " + switchId);
        try {
            boolean result = provisioningStub.isPortAssigned(token,switchId,port);
            log.debug("isPortAssigned returned " + String.valueOf(result) + " for port " + port + " on switch " + switchId);
            return result;
        }
        catch (IllegalArgumentException ex) {
            String msg = "isPortAssigned check returned IllegalArgumentAPIException.";
            log.warn(msg);
            log.debug(ex.getMessage());
            throw new ProvisioningException(msg);
        }
        catch (AuthenticationAPIException ex) {
            String msg = "isPortAssigned check returned AuthenticationAPIException.";
            log.warn(msg);
            log.debug(ex.getMessage());
            throw new ProvisioningException(msg);            
        }
        catch (ProvisioningAPIException ex) {
            String msg = "isPortAssigned check returned ProvisioningAPIException - " + ex.getMessage();
            log.warn(msg);
            throw new ProvisioningException(msg);
        }
        catch (RemoteException ex) {
            String msg = "isPortAssigned check returned RemoteException.";
            log.warn(msg);
            log.debug(ex.getMessage());
            throw new ProvisioningException(msg);
        }
    }    
}

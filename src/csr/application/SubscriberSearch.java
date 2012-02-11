/*
 * SubscriberSearch.java
 *
 * Created on 05 May 2003, 15:29
 */

package com.cisco.ettx.provisioning.application;

import com.cisco.cns.security.soap.common.SecurityToken;
import com.cisco.ettx.provisioning.authentication.SecurityTokenCache;
import com.cisco.ettx.provisioning.Config;
import com.cisco.sesm.sms.api.Subscriber;
import com.cisco.sesm.sms.types.SubscriberInfo;
import com.cisco.sesm.sms.exceptions.api.AuthenticationAPIException;
import com.cisco.sesm.sms.exceptions.api.IllegalArgumentAPIException;
import com.cisco.sesm.sms.exceptions.api.MissingFieldsAPIException;
import com.cisco.sesm.sms.exceptions.api.ProvisioningAPIException;
import com.cisco.sesm.sms.exceptions.api.SubscriberAPIException;

import java.rmi.RemoteException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is a very very noddy implementation of a search for a subscriber.
 * Currently you can do nothing to limit the search and instead all 
 * subscribers found are returned.
 * <p>
 * This implementation is also not multithreaded but should be. The current
 * crappy implementation is here because I needed this functionality in a 
 * real hurry.
 *
 * @author  pcurren
 */
public class SubscriberSearch {

    /**
     * The <code>Log</code> instance for this class.
     */
    private static Log log 
        = LogFactory.getLog("com.cisco.ettx.provisioning.application.SubscriberSearch");    
    
    /** Creates a new instance of SubscriberSearch */
    private SubscriberSearch() {
    }
  
    /**
     * Find all the subscribers in the system. If none are found then an empty
     * array is returned (not null)
     * @param username the admin user's username
     * @param password the admin user's password
     */
    public static SubscriberInfo[] findAll(String username, String password) {
        if ((username == null) || (username.length() == 0)) {
            log.error("Could not perform subscriber search - no username supplied.");
            return new SubscriberInfo[0];
        }
        
        if ((password == null) || (password.length() == 0)) {
            log.error("Could not perform subscriber search - no password supplied.");
            return new SubscriberInfo[0];
        }
        
        SecurityToken token = SecurityTokenCache.getInstance().getSecurityToken(username,password);
        if (token == null) {
            log.error("Could not perform subscriber search - no security token found for admin user " + username);
            return new SubscriberInfo[0];
        }
     
        // now get Subscriber SOAP stub
        Subscriber subscriberStub = Config.getInstance().getSubscriberStub();
        if (subscriberStub == null) {
            log.error("Could not perform subscriber search - no Subscriber Stub endpoint found in configuration.");
            return new SubscriberInfo[0];
        }
        
        // finally, perform seach
        SubscriberInfo[] result = null;
        try {
            result = subscriberStub.query(token,new SubscriberInfo());
        }
        catch(AuthenticationAPIException ex) {
            log.warn("No authorization to search for subscribers for user " + username);
            log.warn("AuthenticationAPIException Message: " + ex.getMessage());
            log.debug(ex);
        }
        catch (IllegalArgumentAPIException ex) {
            log.warn("Illegal arguments supplied to the subscriber query call");
            log.warn("IllegalArgumentAPIException Message: " + ex.getMessage());
            log.debug(ex);            
        }
        catch (MissingFieldsAPIException ex) {
            log.warn("Required fields were missing from the SubscriberInfo template used to query subscribers");
            log.warn("MissingFieldsAPIException Message: " + ex.getMessage());
            log.debug(ex);
        }
        catch (SubscriberAPIException ex) {
            log.warn("Subscriber API reports a problem that prevents query of the subscribers");
            log.warn("SubscriberAPIException Message: " + ex.getMessage());
            log.debug(ex);        
        }
        catch (RemoteException ex) {
            log.warn("RemoteException occurred querying subscribers");
            log.warn("RemoteException Message: " + ex.getMessage());
            log.debug(ex);
        }        
        
        if (result == null) {
            return new SubscriberInfo[0];
        }
        else {
            return result;
        }
    }
    
}

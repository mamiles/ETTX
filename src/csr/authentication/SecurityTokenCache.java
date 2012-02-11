/**
 * Class: SecurityTokenCache
 *
 * Version: SESM 3.9+
 *
 * Created: 10 March 2003
 *
 * Copyright (c) 2003 by Cisco Systems, Inc. All rights reserved.
 *
 * @author Paul Curren
 *
 **/
package com.cisco.ettx.provisioning.authentication;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;

//import com.cisco.cns.security.soap.common.CNSException;
import com.cisco.cns.security.soap.common.EResponseStatus;
//import com.cisco.cns.security.soap.common.InvalidUserOrCredentialException;
import com.cisco.cns.security.soap.common.SecurityToken;
import com.cisco.cns.security.soap.common.SResponse;
import com.cisco.cns.security.soap.authentication.AuthenticationManager;
import com.cisco.ettx.provisioning.Config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class handles the creation and caching of SPE distributed Security 
 * Tokens that have been authorized. This class will be used by all impls that 
 * make use of SPE, such as dess mode, and SMS/SMC mode.
 * <p>
 * Should a token be requested that is not available, then it can be created as
 * required.
 * <p>
 * It should be noted that this implementation of a token cache has the 
 * additional overhead of converting all retrieved tokens into SOAP usable
 * SPE SecurityTokens. If you only require to work with SPE <code>Token</code>
 * objects (i.e. you never require to interact with a remote SPE) then you #
 * should use the cache implemented at
 * {@link com.cisco.sesm.spis.dess.AuthorizedTokenCache AuthorizedTokenCache}
 * <p>
 * Because this implementation deals with SecurityTokens we have no way of 
 * checking for when tokens are expired. Therefore this cache is only suitable
 * for use in SMS deployments where the tokens do not expire.
 * <p>
 * It should also be noted that the way the SPE distributed token cache works
 * means that all users logging in with the same username/password will get the
 * same SecurityToken.
 * <p>
 * Before using the SecurityTokenCache a client should ensure they have 
 * called the <code>login</code> method. This is important as it will increment
 * the number of users on the SecurityToken you are interested in, ensure
 * the token isn't removed from the cache prematurely on a removeToken or 
 * logout call.
 *
 * @author <a href="mailto:pcurren@cisco.com">Paul Curren</a>
 */
public class SecurityTokenCache {

    /**
     * The <code>Log</code> instance for this class.
     */
    private Log log =
        LogFactory.getLog("com.cisco.ettx.provisioning.authentication.SecurityTokenCache");
    
    // Scavenger class
    private class ExpiredTokenScavenger extends Thread {

        /**
         * Thread run method for scavenging auth tokens from the cache.
         * Tokens will be removed from the cache if they have not been accessed
         * within the configured time period.
         * <p>
         * Because this implementation deals with SecurityTokens we have no
         * way of checking for when tokens are expired.
         */
        public void run() {
            log.debug("Starting token scavenging thread");
            
            try {
                while(true) {
                    int checkInt = Config.getInstance().getTokenScavengerSleep();
                    log.debug("Token scavenging sleeping for " 
                              + String.valueOf(checkInt) + " seconds.");
                    sleep((long)checkInt * 1000);

                    int expiryInt = Config.getInstance().getTokenTimeout();
                    log.debug("Ageing out tokens older than "
                              + String.valueOf(expiryInt) + " seconds.");
                    ScavengeTokens(expiryInt);
                }
            }
            catch(InterruptedException iex)
            {
                log.warn("Token scavenging interrupted:" + iex.getMessage());
                log.debug(iex);
            }
        }

        /**
         * Removes tokens from the cache that haven't been used for a period of time,
         * or that have expired
         *
         * @param agedSeconds an <code>int</code> value
         */
        void ScavengeTokens(int agedSeconds) {
            log.debug("Scavenging Authorized tokens");

            Calendar threshold = new GregorianCalendar();
            threshold.add(Calendar.SECOND, -agedSeconds);
            java.util.List tokensRemoved = new java.util.ArrayList();
            synchronized(SecurityTokenCache.this.m_tokens) {
                Iterator tokenIt = SecurityTokenCache.this.m_tokens.values().iterator();
                while(tokenIt.hasNext()) {
                    TokenStore store = (TokenStore)(tokenIt.next());

                    // if token is expired or has not been accessed recently
                    if (store.lastAccess.before(threshold)) {
                        log.debug("Removing Authorized token for " + store.userName);
                        tokensRemoved.add(store.securityToken);
                        tokenIt.remove();
                    }
                    else {
                        log.debug("Leaving Authorized token for " + store.userName);
                    }
                }
            }
            // log out the removed tokens
            Iterator i = tokensRemoved.iterator();
            while (i.hasNext()) {
                SecurityTokenCache.this.logoutToken((SecurityToken)i.next());
            }
        }
    }
    
    
    ExpiredTokenScavenger m_scavenger = null;
    HashMap m_tokens = new HashMap();

    /**
       Holds an Auth token and a timestamp of when it was last used.
       This is so that the objects can be scavenged.
     **/
    private static class TokenStore {
        String userName;
        public Calendar lastAccess;
        public SecurityToken securityToken;
        public int users;
    }

    private static final SecurityTokenCache m_instance = new SecurityTokenCache();
    
    private SecurityTokenCache() {
    }
    
    /** Get an instance of the SecurityTokenCache to use
     */
    public static SecurityTokenCache getInstance() {
        return m_instance;
    }
    
    /**
     * Return a <code>TokenStore</code> for the supplied username and 
     * credential. This token will be retrieved from the internal cache. Should 
     * the token not exist in the cache or if it does exist but is expired, then
     * a new token will be retrieved using the supplied credentials.
     *
     * @return SPE <code>TokenStore</code> or null if no token was found or 
     * could be created.
     */
    private TokenStore getTokenStore(String username, String credential) {
        if (!m_tokens.containsKey(username)) {
            
            // get the SPE Authentication end point
            AuthenticationManager authMan = Config.getInstance().getAuthenticationStub();
            
            if (authMan == null) {
                log.error("Could not get the SOAP Endpoint for the SPE AuthenticationManager. No token can be retrieved for user " + username);
                return null;
            }
            
            SecurityToken token = null;
            try {
                SResponse sResponse = authMan.login(username, credential);
                if (sResponse.getStatus() != EResponseStatus.SUCCESS) {
                    log.warn("Unsuccessful attempt to login over SOAP with the user " + username);
                }
                else {
                    token = sResponse.getToken();
                }
            }
            catch (RemoteException rEx) {
                log.warn("RemoteException logging in user " + username);
                log.debug(rEx);
            }
            
            if (token == null) {
                return null;
            }
            
            log.info("Successfully retrieved token for username " + username);
            
            // now store retrieved token in the cache
            TokenStore theToken = new TokenStore();
            theToken.userName = username;
            theToken.lastAccess = new GregorianCalendar();
            theToken.securityToken = token;
            theToken.users = 0; // if the token is inactive it can be removed
                                // - it has no users.

            synchronized (m_tokens) {
                m_tokens.put(username,theToken);    
            }

            // start the scavenger if it hasn't already been started
            if(m_scavenger == null) {
                m_scavenger = new ExpiredTokenScavenger();
                m_scavenger.start();
            }                    
        }

        TokenStore theToken = null;
        synchronized (m_tokens) {
             theToken = (TokenStore)m_tokens.get(username);
             // update the last access time before returning it.
            if (theToken != null) {
                theToken.lastAccess = new GregorianCalendar();
                return theToken;
            }
            else {
                log.warn("The token for " + username + " has been removed from the cache unexpectedly.");
                return null;
            }
        }
    }
    
    /**
     * Return a <code>SecurityToken</code> for the supplied username and 
     * credential. This token will be retrieved from the internal cache. Should 
     * the token not exist in the cache or if it does exist but is expired, then 
     * a new token will be retrieved using the supplied credentials.
     * <p>
     * It should be ensured before using this method, that the DESS System has
     * previously been initialized e.g. DESSManager.init has been called, for
     * instance by the SESM <code>com.cisco.sesm.dessauth.DirectoryMBean</code>
     *
     * @param username this is the key used to lookup the token.
     * @param credential this parameter is only used if no token can be found
     * for the specified username, and is used to get a new token
     *
     * @return SPE <code>SecurityToken</code> or null if no token was found or 
     * could not be created.
     */
    public SecurityToken getSecurityToken(String username, String credential) {
        TokenStore tstore = getTokenStore(username,credential);
        
        if (tstore == null) {
            log.info("Could not get a Token for the user " + username + " with the supplied credential.");
            return null;
        }
        
        return tstore.securityToken;
    } 
    
    /**
     * Perform a login to SPE and retrieve a SecurityToken matching the supplied
     * credentials. If a token is already in the cache for these credentials
     * then simply return this token with no explicit request to SPE, but
     * increment the number of users of this token.
     *
     * @param username usernames are unique within SPE
     * @param credential the credential is only used if the token does not
     * exist within the cache. Checks against the cache are made only using the
     * username
     * @return the SecurityToken matching the supplied credentials or null if
     * one could not be retrieved.
     */
    public SecurityToken login(String username, String credential) {
        TokenStore tokStore = getTokenStore(username, credential);
        
        if (tokStore == null) {
            return null;
        }
        
        tokStore.users++;
        return tokStore.securityToken;
    }

    /**
     * When finished with a SecurityToken i.e. the user has logged out, then
     * you should call logout. This will decrement the user count of the 
     * token, and if it has no users then the token will be removed from the
     * cache.
     */
    public void logout(String username) {
        if (hasToken(username)) {
            TokenStore tokStore = getTokenStore(username, null);
            
            tokStore.users--;
            
            if (tokStore.users <= 0) {
                removeToken(username);
            }
        }
    }
    
    /**
     * Remove a specific token from the token store.
     *
     * @param userName Key for token to be removed.
     */
    private void removeToken(String username) {
        SecurityToken token = null;
        synchronized(m_tokens) {
            if(m_tokens.containsKey(username)) {
                token = ((TokenStore)m_tokens.get(username)).securityToken;
                log.debug("Removing token for user: " + username);
                m_tokens.remove(username);
            }
        }
        if (token != null) {
            // We removed a token from our cache.  Need to make logout call 
            // for it.
            log.debug("Logging out token from SPE for user: " + username);
            logoutToken(token);
        }
    }
    
    /**
     * Check if the SecurityTokenCache contains a token for the supplied 
     * credentials.
     */
    public boolean hasToken(String username) {
        return m_tokens.containsKey(username);
    }
    

    /** Make a logout call to SPE AuthenticationManager for a token.
     */
    private void logoutToken(SecurityToken token) {
       
        // get the SPE Authentication end point
        AuthenticationManager authMan = Config.getInstance().getAuthenticationStub();
        
        if (authMan == null) {
            log.error("Could not get the SOAP Endpoint for the SPE AuthenticationManager. Can't log out token " + token);
            return;
        }
        try {
            authMan.logout(token);
        }
        catch (RemoteException ex) {
            log.warn("RemoteException logging out token " + token);
            log.debug(ex);
        }
    }
}

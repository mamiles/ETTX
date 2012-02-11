/*
 * SMCCache.java
 *
 * Created on 29 April 2003, 01:55
 */

package com.cisco.ettx.provisioning.application;

import com.cisco.cns.security.soap.common.SecurityToken;
import com.cisco.ettx.provisioning.authentication.SecurityTokenCache;
import com.cisco.ettx.provisioning.Config;
import com.cisco.sesm.sms.api.Inventory;
import com.cisco.sesm.sms.api.Service;
import com.cisco.sesm.sms.exceptions.api.AuthenticationAPIException;
import com.cisco.sesm.sms.exceptions.api.IllegalArgumentAPIException;
import com.cisco.sesm.sms.exceptions.api.InventoryAPIException;
import com.cisco.sesm.sms.exceptions.api.MissingFieldsAPIException;
import com.cisco.sesm.sms.exceptions.api.ServiceAPIException; 
import com.cisco.sesm.sms.types.ServiceInfo;
import com.cisco.sesm.sms.types.SwitchInfo;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Singleton class that will cache frequently used, but infrequently
 * changing data retrieved from the SMC API's. For example
 * <ul>
 *  <li>All available services (and features>
 *  <li>All available switches and their ports
 * </ul>
 * Data is cache against given username's, allowing for partitioning of admin
 * users. e.g. one user could have access only to certain switches, etc.
 *
 * @author  pcurren
 */
public class SMCCache {
    /**
     * The <code>Log</code> instance for this class.
     */
    private Log log =
        LogFactory.getLog("com.cisco.ettx.provisioning.application.SMCCache");
    
    protected static SMCCache m_instance = new SMCCache();
    protected Map m_services = new HashMap();
    protected Map m_switches = new HashMap();
    
    /** Creates a new instance of SMCCache */
    private SMCCache() {}
    
    public static SMCCache getInstance() {
        return m_instance;
    }
    
    /** Get the available services for the SecurityToken identified by the
     * supplied username. 
     *
     * @param password only required should there be no SecurityToken in the
     * cache for the required user
     *
     * @return null if there is a problem retrieving services for the 
     * supplied username
     */
    public ServiceInfo[] getAvailableServices(String username, String password) {
        synchronized(m_services) {
            if (m_services.containsKey(username)) {
                return (ServiceInfo[])m_services.get(username);
            }
        }   
        
        // wasn't in the cache, so retrieve
        if (!populateServices(username,password)) {
            return null;
        }
        
        ServiceInfo[] services = null;
        synchronized(m_services) {
            services = (ServiceInfo[])m_services.get(username);
        }

        if (services == null) {
            log.warn("Despite successfully retrieving services for user " 
                     + username 
                     + " they are no longer in the cache. It appears another thread must have removed these services from the cache.");
        }
        else {
            if (log.isDebugEnabled()) {
                // for debug purposes display the service names to the log
                StringBuffer serviceNames = new StringBuffer("For user ");
                serviceNames.append(username);
                serviceNames.append(" retrieved the following named services - ");
                for (int i=0; i < services.length; i++) {
                    serviceNames.append(services[i].getServiceName());
                    serviceNames.append(",");
                }
                
                // remove final ","
                serviceNames.deleteCharAt(serviceNames.length()-1);
                log.debug(serviceNames.toString());
            }
        }
        
        return services;
    }
    
    /** 
     * Get and store services against a supplied username
     */
    private boolean populateServices(String username, String password) {
        log.trace("Populating available services for user " + username);
        // first, get SecurityToken to use
        SecurityToken tok = SecurityTokenCache.getInstance().getSecurityToken(username,password);
        if (tok == null) {
            log.error("Could not get a SecurityToken for the user " + username);
            return false;
        }
        
        log.debug("Got SecurityToken for service population for user " + username);
        
        // now get Service stub
        Service serviceStub = Config.getInstance().getServiceStub();
        if (serviceStub == null) {
            log.error("No Service API point was configured. Cannot retrieve available services for user " + username);
            return false;
        }
        
        // now get the services using this token
        ServiceInfo[] services = null;
        try {
            services = serviceStub.queryAllServices(tok);
        }
        catch (AuthenticationAPIException ex) {
            log.warn("No authorization to retrieve services for user " + username);
        }
        catch (IllegalArgumentAPIException ex) {
            log.warn("Illegal arguments supplied to the Service.queryAllServices SOAP API");
            log.warn("IllegalArgumentAPIException Message: " + ex.getMessage());
            log.debug(ex);
        }
        catch (ServiceAPIException ex) {
            log.warn("Internal problem with Service API prevented retrieval of services for user " + username);
            log.warn("ServiceAPIException Message: " + ex.getMessage());
            log.debug(ex);
        }
        catch (RemoteException ex) {
            log.warn("RemoteException occurred retrieving available services for user " + username);
            log.warn("RemoteException Message: " + ex.getMessage());
            log.info("Ensure the correct URL for the Service endpoint is configured.");
            log.debug(ex);
        }
        
        if (services == null) {
            log.info("Failed to retrieve services for user " + username);
            return false;
        }

        // put retrieved services into cache
        synchronized (m_services) {
            m_services.put(username,services);
        }

        log.debug("Successfully retrieved " + services.length + " services for user " + username);
        return true;
    }
        
    /** Get the available switches for the SecurityToken identified by the
     * supplied username. 
     *
     * @param password only required should there be no SecurityToken in the
     * cache for the required user
     *
     * @return null if there is a problem retrieving switches for the 
     * supplied username
     */
    public SwitchInfo[] getAvailableSwitches(String username, String password) {
        synchronized(m_switches) {
            if (m_switches.containsKey(username)) {
                return (SwitchInfo[])m_switches.get(username);
            }
        }   
        
        // wasn't in the cache, so retrieve
        if (!populateSwitches(username,password)) {
            return null;
        }
        
        SwitchInfo[] switches = null;
        synchronized(m_switches) {
            switches = (SwitchInfo[])m_switches.get(username);
        }

        if (switches == null) {
            log.warn("Despite successfully retrieving switches for user " 
                     + username 
                     + " they are no longer in the cache. It appears another thread must have removed these switches from the cache.");
        }
        else {
            if (log.isDebugEnabled()) {
                // for debug purposes display the service names to the log
                StringBuffer switchDetails = new StringBuffer("For user ");
                switchDetails.append(username);
                switchDetails.append(" retrieved the following switches - ");
                for (int i=0; i < switches.length; i++) {
                    switchDetails.append("[");
                    switchDetails.append(switches[i].getHostName());
                    switchDetails.append(" ");
                    switchDetails.append(switches[i].getIpAddress());
                    switchDetails.append(" ");
                    switchDetails.append(switches[i].getModel());
                    switchDetails.append("] ");
                }
                
                // remove final ","
                log.debug(switchDetails.toString());
            }
        }
        
        return switches;
    }    
    
    /** 
     * Get and store switches against a supplied username
     */
    private boolean populateSwitches(String username, String password) {
        log.trace("Populating available switches for user " + username);
        // first, get SecurityToken to use
        SecurityToken tok = SecurityTokenCache.getInstance().getSecurityToken(username,password);
        if (tok == null) {
            log.error("Could not get a SecurityToken for the user " + username);
            return false;
        }
        
        log.debug("Got SecurityToken for switch population for user " + username);
        
        // now get Service stub
        Inventory inventoryStub = Config.getInstance().getInventoryStub();
        if (inventoryStub == null) {
            log.error("No Inventory API point was configured. Cannot retrieve available switches for user " + username);
            return false;
        }
        
        // now get the services using this token
        SwitchInfo[] switches = null;
        try {
            switches = inventoryStub.query(tok, new SwitchInfo());
        }
        catch (AuthenticationAPIException ex) {
            log.warn("No authorization to retrieve switches for user " + username);
        }
        catch (InventoryAPIException ex) {
            log.warn("Internal problem with Inventory API prevented retrieval of switches for user " + username);
            log.warn("InventoryAPIException Message: " + ex.getMessage());
            log.debug(ex);
        }
        catch (MissingFieldsAPIException ex) {
            log.warn("Required fields were missing from the SwitchInfo template supplied to Inventory.query. No switches retrieved for user " + username);
            log.warn("MissingFieldsAPIException Message: " + ex.getMessage());
            log.debug(ex);
        }
        catch (RemoteException ex) {
            log.warn("RemoteException occurred retrieving available switches for user " + username);
            log.warn("RemoteException Message: " + ex.getMessage());
            log.info("Ensure the correct URL for the Inventory endpoint is configured.");
            log.debug(ex);
        }
        
        if (switches == null) {
            log.info("Failed to retrieve switches for user " + username);
            return false;
        }

        // put retrieved services into cache
        synchronized (m_switches) {
            m_switches.put(username,switches);
        }

        log.debug("Successfully retrieved " + switches.length + " switches for user " + username);
        return true;
    }    
    
}

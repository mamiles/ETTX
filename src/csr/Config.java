/*
 * Config.java
 *
 * Created on 20 April 2003, 15:20
 */

package com.cisco.ettx.provisioning;

import java.io.InputStream;
import java.io.IOException;
import java.lang.StringBuffer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.xml.rpc.ServiceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cisco.cns.security.soap.authentication.AuthenticationManager;
import com.cisco.cns.security.soap.authentication.AuthenticationManagerServiceLocator;
import com.cisco.sesm.sms.api.Billing;
import com.cisco.sesm.sms.api.BillingServiceLocator;
import com.cisco.sesm.sms.api.Device;
import com.cisco.sesm.sms.api.DeviceServiceLocator;
import com.cisco.sesm.sms.api.Inventory;
import com.cisco.sesm.sms.api.InventoryServiceLocator;
import com.cisco.sesm.sms.api.Lease;
import com.cisco.sesm.sms.api.LeaseServiceLocator;
import com.cisco.sesm.sms.api.Provisioning;
import com.cisco.sesm.sms.api.ProvisioningServiceLocator;
import com.cisco.sesm.sms.api.Service;
import com.cisco.sesm.sms.api.ServiceServiceLocator;
import com.cisco.sesm.sms.api.Subscriber;
import com.cisco.sesm.sms.api.SubscriberServiceLocator;
import com.cisco.sesm.sms.api.Subscriptions;
import com.cisco.sesm.sms.api.SubscriptionsServiceLocator;

/** Holds the SOAP endpoints for all of the SMC API services, plus other
 * relevant configuration items
 *
 * @author  pcurren
 */
public class Config {
    /**
     * The <code>Log</code> instance for this class.
     */
    private Log log =
        LogFactory.getLog("com.cisco.ettx.provisioning.Config");
    
    private static String DEFAULT_APP_CONFIG = "config.application";
    
    public static String AUTHENTICATION_ENDPOINT_PROP = "config.api.authentication";
    public static String BILLING_ENDPOINT_PROP        = "config.api.billing";
    public static String DEVICE_ENDPOINT_PROP         = "config.api.device";
    public static String INVENTORY_ENDPOINT_PROP      = "config.api.inventory";
    public static String LEASE_ENDPOINT_PROP          = "config.api.lease";
    public static String PROVISIONING_ENDPOINT_PROP   = "config.api.provisioning";
    public static String SERVICE_ENDPOINT_PROP        = "config.api.service";
    public static String SUBSCRIBER_ENDPOINT_PROP     = "config.api.subscriber";
    public static String SUBSCRIPTIONS_ENDPOINT_PROP  = "config.api.subscriptions";

    public static String TASK_SCAVENGER_SLEEP_PROP    = "config.tasks.scavengerSleep";
    public static String TASK_FORGOTTEN_PROP          = "config.tasks.forgottenInterval";
    
    public static String LOG4J_CONFIG_PROP            = "config.logging.configFile";
    
    public static String TASK_TIMEOUT_PREFIX          = "config.timeout.";
    public static String TASK_FORWARD_PREFIX          = "config.task.forward.";
    
    public static String SCAVENGER_SLEEP_PROP         = "config.token.scavengerSleep";
    public static String TOKEN_TIMEOUT_PROP           = "config.token.timeoutInterval";
    
    public static int DEFAULT_SCAVENGER_SLEEP         = 600;
    public static int DEFAULT_TOKEN_EXPIRY            = 600;
    
    public static int DEFAULT_TASK_SCAVENGER_SLEEP    = 3600;
    public static int DEFAULT_TASK_FORGOTTEN_INTERVAL = 180;
    public static int DEFAULT_TASK_TIMEOUT            = 180;
    
    private static Config m_instance = null;
    
    private String m_propertiesFile = null;
    private ResourceBundle m_resources = null;
    
    private AuthenticationManager m_authenticationStub = null;
    private Billing m_billingStub = null;
    private Device m_deviceStub = null;
    private Inventory m_inventoryStub = null;
    private Lease m_leaseStub = null;
    private Provisioning m_provisioningStub = null;
    private Service m_serviceStub = null;
    private Subscriber m_subscriberStub = null;
    private Subscriptions m_subscriptionsStub = null;   
   
    /**
     * Get an instance of the Config singleton
     */
    public static synchronized Config getInstance() {
        if (m_instance == null) {
            m_instance = new Config(DEFAULT_APP_CONFIG);
        }
        
        return m_instance;
    }
    
    /** Creates a new instance of Config 
     * @param props The fully qualified name of the resource file to be used
     * to get the configuration from
     */
    private Config(String props) {
        newConfig(props);
    }
    
    /**
     * Create new configuration from the supplied fully qualified resource
     * name. The properties file will be accessed from this classes
     * class loader.
     */
    public boolean newConfig(String propsFile) {
        if ((propsFile == null) || (propsFile.length() == 0)) {
            log.warn("No properties file was specified. No modification of configuration made.");
            return false;
        }
       
        ResourceBundle myResources = null;
        try {
            myResources = ResourceBundle.getBundle(propsFile);
        }
        catch (MissingResourceException ex) {
            log.warn("Could not find a configuration file for the default locale. No modification of configuration made.");
            return false;
        }

        // now create end points for configured URLs
        int stubs = createStubs(myResources);
        if (log.isDebugEnabled()) {
            log.debug("Created " + String.valueOf(stubs) + " SOAP stubs from supplied Properties object.");
        }

        m_resources = myResources;
        
        return true;
    }
    
    
    
    /**
     * Create SOAP end points from the parsed configuration. The m_properties
     * property is taken to contain the full configuration
     *
     * @return the number of endpoints successfully created
     */
    private int createStubs(ResourceBundle bundle) {
        log.trace("Config.createEndpoints: Creating new SOAP stubs.");
        int counter = 0;
        
        // create each stub in turn, if it is configured. If there is
        // no configuration for a particular endpoint then set it null.
        String urlStr = null;
        try {
            urlStr = bundle.getString(AUTHENTICATION_ENDPOINT_PROP);
            URL url = new URL(urlStr);
            AuthenticationManagerServiceLocator locator = new AuthenticationManagerServiceLocator();
            m_authenticationStub = locator.getAuthentication(url);
            counter++;
        }
        catch (MissingResourceException ex) {
            m_authenticationStub = null;
            log.info("No Authentication endpoint configured.");
        }
        catch (MalformedURLException ex) {
            log.warn("Configured URL for Authentication endpoint is malformed.");
        }
        catch (ServiceException ex) {
            log.warn("ServiceException creating Authentication stub from configured URL " + urlStr);
            log.debug(ex);
        }

        try {
            urlStr = bundle.getString(BILLING_ENDPOINT_PROP);
            URL url = new URL(urlStr);
            BillingServiceLocator locator = new BillingServiceLocator();
            m_billingStub = locator.getBilling(url);
            counter++;
        }
        catch (MalformedURLException ex) {
            log.warn("Configured URL for Billing endpoint is malformed.");
        }
        catch (ServiceException ex) {
            log.warn("ServiceException creating Billing stub from configured URL " + urlStr);
            log.debug(ex);
        }
        catch (MissingResourceException ex) {
            m_billingStub = null;
            log.info("No Billing endpoint configured.");
        }      
        
        try {
            urlStr = bundle.getString(DEVICE_ENDPOINT_PROP);
            URL url = new URL(urlStr);
            DeviceServiceLocator locator = new DeviceServiceLocator();
            m_deviceStub = locator.getDevice(url);
            counter++;
        }
        catch (MalformedURLException ex) {
            log.warn("Configured URL for Device endpoint is malformed.");
        }
        catch (ServiceException ex) {
            log.warn("ServiceException creating Device stub from configured URL " + urlStr);
            log.debug(ex);
        }
        catch (MissingResourceException ex) {        
            m_deviceStub = null;
            log.info("No Device endpoint configured.");
        }              
        
        try {
            urlStr = bundle.getString(INVENTORY_ENDPOINT_PROP);
            URL url = new URL(urlStr);
            InventoryServiceLocator locator = new InventoryServiceLocator();
            m_inventoryStub = locator.getInventory(url);
            counter++;
        }
        catch (MalformedURLException ex) {
            log.warn("Configured URL for Inventory endpoint is malformed.");
        }
        catch (ServiceException ex) {
            log.warn("ServiceException creating Inventory stub from configured URL " + urlStr);
            log.debug(ex);
        }
        catch (MissingResourceException ex) {
            m_inventoryStub = null;
            log.info("No Inventory endpoint configured.");
        }                

        try {
            urlStr = bundle.getString(LEASE_ENDPOINT_PROP);
            URL url = new URL(urlStr);
            LeaseServiceLocator locator = new LeaseServiceLocator();
            m_leaseStub = locator.getLease(url);
            counter++;
        }
        catch (MalformedURLException ex) {
            log.warn("Configured URL for Lease endpoint is malformed.");
        }
        catch (ServiceException ex) {
            log.warn("ServiceException creating Lease stub from configured URL " + urlStr);
            log.debug(ex);
        }
        catch (MissingResourceException ex) {        
            m_leaseStub = null;
            log.info("No Lease endpoint configured.");
        }              
        
        try {
            urlStr = bundle.getString(PROVISIONING_ENDPOINT_PROP);
            URL url = new URL(urlStr);
            ProvisioningServiceLocator locator = new ProvisioningServiceLocator();
            m_provisioningStub = locator.getProvisioning(url);
            counter++;
        }
        catch (MalformedURLException ex) {
            log.warn("Configured URL for Provisioning endpoint is malformed.");
        }
        catch (ServiceException ex) {
            log.warn("ServiceException creating Provisioning stub from configured URL " + urlStr);
            log.debug(ex);
        }
        catch (MissingResourceException ex) {        
            m_provisioningStub = null;
            log.info("No Provisioning endpoint configured.");
        }                
        
        try {
            urlStr = bundle.getString(SERVICE_ENDPOINT_PROP);
            URL url = new URL(urlStr);
            ServiceServiceLocator locator = new ServiceServiceLocator();
            m_serviceStub = locator.getService(url);
            counter++;
        }
        catch (MalformedURLException ex) {
            log.warn("Configured URL for Service endpoint is malformed.");
        }
        catch (ServiceException ex) {
            log.warn("ServiceException creating Service stub from configured URL " + urlStr);
            log.debug(ex);
        }
        catch (MissingResourceException ex) {        
            m_serviceStub = null;
            log.info("No Service endpoint configured.");
        }                
        
        try {
            urlStr = bundle.getString(SUBSCRIBER_ENDPOINT_PROP);
            URL url = new URL(urlStr);
            SubscriberServiceLocator locator = new SubscriberServiceLocator();
            m_subscriberStub = locator.getSubscriber(url);
            counter++;
        }
        catch (MalformedURLException ex) {
            log.warn("Configured URL for Subscriber endpoint is malformed.");
        }
        catch (ServiceException ex) {
            log.warn("ServiceException creating Subscriber stub from configured URL " + urlStr);
            log.debug(ex);
        }
        catch (MissingResourceException ex) {
            m_subscriberStub = null;
            log.info("No Subscriber endpoint configured.");
        }       
        
        try {
            urlStr = bundle.getString(SUBSCRIPTIONS_ENDPOINT_PROP);
            URL url = new URL(urlStr);
            SubscriptionsServiceLocator locator = new SubscriptionsServiceLocator();
            m_subscriptionsStub = locator.getSubscriptions(url);
            counter++;
        }
        catch (MalformedURLException ex) {
            log.warn("Configured URL for Subscriptions endpoint is malformed.");
        }
        catch (ServiceException ex) {
            log.warn("ServiceException creating Subscriptions stub from configured URL " + urlStr);
            log.debug(ex);
        }
        catch (MissingResourceException ex) {
            m_subscriptionsStub = null;
            log.info("No Subscriptions endpoint configured.");
        }        
        
        return counter;
    }

    /**
     * Return the Authentication stub, or null if there is none configured.
     */
    public AuthenticationManager getAuthenticationStub() {
       return m_authenticationStub; 
    }    
    
    /**
     * Return the BillingStub, or null if there is none configured.
     */
    public Billing getBillingStub() {
       return m_billingStub; 
    }
    
    /**
     * Return the DeviceStub, or null if there is none configured.
     */
    public Device getDeviceStub() {
       return m_deviceStub; 
    }

    /**
     * Return the InventoryStub, or null if there is none configured.
     */
    public Inventory getInventoryStub() {
       return m_inventoryStub; 
    }    
    
    /**
     * Return the LeaseStub, or null if there is none configured.
     */
    public Lease getLeaseStub() {
       return m_leaseStub; 
    }    
    
    /**
     * Return the ProvisioningStub, or null if there is none configured.
     */
    public Provisioning getProvisioningStub() {
       return m_provisioningStub; 
    }    
    
    /**
     * Return the ServiceStub, or null if there is none configured.
     */
    public Service getServiceStub() {
       return m_serviceStub; 
    }    
    
    /**
     * Return the SubscriberStub, or null if there is none configured.
     */
    public Subscriber getSubscriberStub() {
       return m_subscriberStub; 
    }    
    
    /**
     * Return the SubscriptionsStub, or null if there is none configured.
     */
    public Subscriptions getSubscriptionsStub() {
       return m_subscriptionsStub; 
    }        
    
    /**
     * Return the token scavenger sleep interval
     */
    public int getTokenScavengerSleep() {
        try {
            String sleepInt = m_resources.getString(SCAVENGER_SLEEP_PROP);
            Integer sleepIntInt = Integer.valueOf(sleepInt);
            return sleepIntInt.intValue();
        }
        catch (MissingResourceException ex) {
            log.warn("No " + SCAVENGER_SLEEP_PROP + " property set in the configuration file. Returning default value of " + DEFAULT_SCAVENGER_SLEEP);
        }
        catch (NumberFormatException ex) {
            log.warn("The " + SCAVENGER_SLEEP_PROP + " property set is not an integer. Returning default value of " + DEFAULT_SCAVENGER_SLEEP);
        }
        
        return DEFAULT_SCAVENGER_SLEEP;
    }
    
    /**
     * Return the token timeout interval
     */
    public int getTokenTimeout() {
        try {
            String timeoutStr = m_resources.getString(TOKEN_TIMEOUT_PROP);
            Integer timeout = Integer.valueOf(timeoutStr);
            return timeout.intValue();
        }
        catch (MissingResourceException ex) {
            log.warn("No " + TOKEN_TIMEOUT_PROP + " property set in the configuration file. Returning default value of " + DEFAULT_TOKEN_EXPIRY);
        }
        catch (NumberFormatException ex) {
            log.warn("The " + TOKEN_TIMEOUT_PROP + " property set is not an integer. Returning default value of " + DEFAULT_TOKEN_EXPIRY);
        }
        
        return DEFAULT_TOKEN_EXPIRY;
    }    
    
    /**
     * Return the task record scavenger sleep interval. This interval should
     * generally be quite long (e.g. 1 hour) since it's not too important to
     * scavenge task records - the clients should be removing them manually.
     */
    public int getTaskScavengerSleep() {
        try {
            String sleepInt = m_resources.getString(TASK_SCAVENGER_SLEEP_PROP);
            Integer sleepIntInt = Integer.valueOf(sleepInt);
            return sleepIntInt.intValue();
        }
        catch (MissingResourceException ex) {
            log.warn("No " + TASK_SCAVENGER_SLEEP_PROP + " property set in the configuration file. Returning default value of " + DEFAULT_SCAVENGER_SLEEP);
        }
        catch (NumberFormatException ex) {
            log.warn("The " + TASK_SCAVENGER_SLEEP_PROP + " property set is not an integer. Returning default value of " + DEFAULT_SCAVENGER_SLEEP);
        }
        
        return DEFAULT_TASK_SCAVENGER_SLEEP;
    }
    
    /**
     * Return the interval that a Task record (<code>TaskProgressStore</code>)
     * should be unused before it is removed.
     */
    public int getTaskForgottenInterval() {
        try {
            String timeoutStr = m_resources.getString(TASK_FORGOTTEN_PROP);
            Integer timeout = Integer.valueOf(timeoutStr);
            return timeout.intValue();
        }
        catch (MissingResourceException ex) {
            log.warn("No " + TASK_FORGOTTEN_PROP + " property set in the configuration file. Returning default value of " + DEFAULT_TOKEN_EXPIRY);
        }
        catch (NumberFormatException ex) {
            log.warn("The " + TASK_FORGOTTEN_PROP + " property set is not an integer. Returning default value of " + DEFAULT_TOKEN_EXPIRY);
        }
        
        return DEFAULT_TASK_FORGOTTEN_INTERVAL;
    }    
    
    /**
     * Get the timeout value from configuration for the named task.
     * If not found in the config file, then the default timeout is returned.
     *
     * @param taskName the supplied task name is appended to the <code>
     * Config.TASK_TIMEOUT_PREFIX</code> and read from the configuration file.
     * @return value in seconds representing the task timeout
     */
    public int getTaskTimeout(String taskName) {
        if ((taskName == null) || (taskName.length() ==0)) {
            log.warn("No taskName supplied to getTaskTimeout()");
            return DEFAULT_TASK_TIMEOUT;
        }
        
        StringBuffer buf = new StringBuffer(TASK_TIMEOUT_PREFIX);
        buf.append(taskName);
        
        try {
            String timeoutStr = m_resources.getString(buf.toString());
            Integer timeout = Integer.valueOf(timeoutStr);
            return timeout.intValue();
        }
        catch (MissingResourceException ex) {
            log.warn("No " + buf.toString() + " property set in the configuration file. Returning default value of " + DEFAULT_TASK_TIMEOUT);
        }
        catch (NumberFormatException ex) {
            log.warn("The " + buf.toString() + " property set is not an integer. Returning default value of " + DEFAULT_TASK_TIMEOUT);
        }

        return DEFAULT_TASK_TIMEOUT;
    }    
    
    
    /**
     * Get the default task action for a given TaskState as a String. The 
     * supplied String is appended to the <code>TASK_FORWARD_PREFIX</code>
     * before being retrieved as a String from the config.
     * <p>
     * If there is no action in configuration for the requested String then
     * null will be returned;
     */
    public String getTaskStateForward(String state) {
        if ((state == null) || (state.length() ==0)) {
            log.warn("No state supplied to getTaskStateForward()");
            return null;
        }
        
        StringBuffer buf = new StringBuffer(TASK_FORWARD_PREFIX);
        buf.append(state);
        
        try {
            String action = m_resources.getString(buf.toString());
            return action;
        }
        catch (MissingResourceException ex) {
            log.warn("No " + buf.toString() + " property set in the configuration file.");
            return null;
        }
    }
    
    public String getLog4JConfig() {
        try {
            return m_resources.getString(LOG4J_CONFIG_PROP);
        }
        catch (MissingResourceException ex) {
            log.warn("No " + LOG4J_CONFIG_PROP + " property set in the configuration file. No specific logging configuration applied");
        }
        
        return null;
    }    
    
}

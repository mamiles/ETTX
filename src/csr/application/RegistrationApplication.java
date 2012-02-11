/*
 * RegistrationApplication.java
 *
 * Created on 29 April 2003, 22:03
 */

package com.cisco.ettx.provisioning.application;

import java.lang.Runnable;
import java.lang.Thread;
import java.rmi.RemoteException;

import com.cisco.cns.security.soap.common.SecurityToken;
import com.cisco.ettx.provisioning.authentication.SecurityTokenCache;
import com.cisco.ettx.provisioning.authentication.User;
import com.cisco.ettx.provisioning.Audit;
import com.cisco.ettx.provisioning.Config;
import com.cisco.ettx.provisioning.progress.TaskProgress;
import com.cisco.ettx.provisioning.progress.TaskState;
import com.cisco.sesm.sms.api.Subscriber;
import com.cisco.sesm.sms.api.Subscriptions;
import com.cisco.sesm.sms.api.Provisioning;
import com.cisco.sesm.sms.exceptions.api.AuthenticationAPIException;
import com.cisco.sesm.sms.exceptions.api.IllegalArgumentAPIException;
import com.cisco.sesm.sms.exceptions.api.MissingFieldsAPIException;
import com.cisco.sesm.sms.exceptions.api.ProvisioningAPIException;
import com.cisco.sesm.sms.exceptions.api.SubscriberAPIException;
import com.cisco.sesm.sms.exceptions.api.SubscriptionsAPIException;
import com.cisco.sesm.sms.types.ServiceFeaturesListInfo;
import com.cisco.sesm.sms.types.ServiceFeatureInfo;
import com.cisco.sesm.sms.types.SubscriberInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Application that will take information about a subscriber and services and
 * register the subscriber, activating the appropriate services for them.
 * <p>
 * Ultimately, the functions of this class should be broken up to allow it to
 * more easily reusable for other tasks, such as unregistration etc.
 * <p>
 * This application will take a TaskProgress object into which it updates its
 * progress. This TaskProgress object is accessible by external entities who
 * may want to check on the progress of registration.
 *
 * @author  pcurren
 */
public class RegistrationApplication extends Thread {
    
    // Codes that this application may insert in the task progress.
    public static int SUCCESS = 0;
    public static int INIT_MISSING_USERNAME = 110;
    public static int INIT_MISSING_PASSWORD = 111;
    public static int INIT_MISSING_SERVICENAME = 112;
    public static int INIT_MISSING_SERVICEFEATURES = 113;
    public static int INIT_MISSING_SUBSCRIBERINFO = 114;
    public static int INIT_NO_SECURITY_TOKEN = 150;
    public static int INIT_NO_SMC_STUBS = 160;    
    public static int SUBSCRIBER_MISSING_FIELDS = 210;
    public static int SUBSCRIBER_ILLEGAL_ARGUMENT = 212;
    public static int SUBSCRIBER_AUTHENTICATION_FAILURE = 220;
    public static int SUBSCRIBER_REMOTE_EXCEPTION = 230;
    public static int SUBSCRIBER_API_FAIL = 240; // API reported a failure
    public static int SUBSCRIBER_DELETE_FAIL = 300;
    public static int SUBSCRIPTION_CREATE_FAIL = 400;
    public static int SUBSCRIPTION_DELETE_FAIL = 410;
    public static int SERVICE_ACTIVATE_FAIL = 500;
    public static int SERVICE_DEACTIVATE_FAIL = 510;

    // Enumerated type that indicates the task the application is to perform
    public static class Operation {
        public static Operation REGISTER = new Operation("register");
        public static Operation UNREGISTER = new Operation("unregister");
        
        private String m_opType = null;
        private Operation(String opType) {
            m_opType = opType;
        }
        
        public String toString() {
            return m_opType;
        }
    }    
    
    /**
     * The <code>Log</code> instance for this class.
     */
    private Log log 
        = LogFactory.getLog("com.cisco.ettx.provisioning.application.RegistrationApplication");    
    
    /** The operation that a run of this application will perform. */
    private Operation m_operation = null;
    
    /** The client IP address of the operator running this application. This is used for
     * audit logging.
     */
    private String m_clientIp = null;
    
    /** The username this application will run with */
    private String m_username = null;

    /** The password this application will run with */
    private String m_password = null;
    
    /** The SecurityToken that matches the supplied credentials */
    private SecurityToken m_token = null;
    
    /** The ServiceFeature's and Service name to be registered */
    private ServiceFeaturesListInfo m_serviceFeatures = null;
    
    /** The TaskProgress object to be updated with ongoing task status */
    private TaskProgress m_progress = null;
    
    /** The Subscriber to create */
    private SubscriberInfo m_subscriber = null;
    
    /** The subscriber stub that will be used to create the Subscriber */
    private Subscriber m_subscriberStub = null;
    
    /** The Subscriptions stub that will be used to subscribe to a service */
    private Subscriptions m_subscriptionsStub = null;
    
    /** The Provisioning stub that will be used to activate a service */
    private Provisioning m_provisioningStub = null;
    
    /** Creates a new instance of RegistrationApplication. No checking of the
     * supplied parameters is done at this stage except to confirm that a 
     * TaskProgress has been supplied. However, once the task is
     * started, parameter checking is performed, and if there are problems
     * the TaskProgress will be moved to the failed state.
     *
     * @param username the username this application will use for SMC API access
     * @param password the password this application will use for SMC API access
     * @param subscriber Information about the subscriber to be created, including
     * switch and port identifiers
     * @param features an array of the features a subscriber requests on their
     * named service, and also includes the serviceName
     * @param tp to be updated with the ongoing progress of registration
     * @throws IllegalArgumentException if the supplied TaskProgress parameter
     * is null.
     */
    public RegistrationApplication(Operation op, String username, String password, 
                                   String clientIp, SubscriberInfo subscriber, 
                                   ServiceFeaturesListInfo features, TaskProgress tp)
        throws IllegalArgumentException {
        super();
        if (tp == null) {
            throw new IllegalArgumentException("You must supply a TaskProgress parameter.");
        }
        
        if (log.isTraceEnabled()) {
            log.trace("Creating RegistrationApplication for operator " + username);
        }
    
        m_username = username;
        m_password = password;
        m_clientIp = clientIp;
        m_subscriber = subscriber;
        m_progress = tp;
        m_operation = op;
    
        // build ServiceFeaturesListInfo[] from supplied features
        m_serviceFeatures = features;
        
        if (log.isDebugEnabled()) {
            if (m_operation == null) {
                log.debug("No Operation type supplied to RegistrationApplication. Task will not run.");
            }
            
            if (subscriber == null) {
                log.debug("No SubscriberInfo supplied to RegistrationApplication for operator " + username);
                return;
            }
            
            if (features == null) {
                log.debug("No ServiceFeatureInfo[] supplied to RegistrationApplication for operator " + username);
                return;
            }
            
            StringBuffer buf = new StringBuffer("Created RegistrationApplication for operator ");
            buf.append(username);
            buf.append(" [subscriberId=");
            buf.append(subscriber.getSubscriberId());
            buf.append(" service=");
            buf.append(m_serviceFeatures.getServiceName());
            buf.append(" with operation type " + m_operation);
            log.debug(buf.toString());
            
        }
    }

    /** this method starts the same work as <code>run</code> does however it will
     * run in the same thread that executes it, unlike run which will start a new
     * thread. For short lived tasks you should probably make use of this call.
     * For longer lived tasks, you may prefer to use <code>run</code>.
     * <p>
     * As with <code>run</code> this method is void and you should instead check
     * the status by referring to the TaskProgress object you supplied to the
     * constructor of the application
     */
    public void runInThread() {
        if (m_operation == Operation.REGISTER) {
            if (!runRegisterWorkflow()) {
                Audit.record(m_username, m_clientIp, "Subscriber registration process failed for " + m_subscriber.getSubscriberId());
                Audit.record(m_username, m_clientIp, "Task returned error code " + m_progress.getFinishedCode());
            }
            else {
                Audit.record(m_username, m_clientIp, "Subscriber " + m_subscriber.getSubscriberId() + " successfully registered.");
            }
        }
        else if (m_operation == Operation.UNREGISTER) {
            if (!runUnregisterWorkflow()) {
                Audit.record(m_username, m_clientIp, "Subscriber unregistration process failed for " + m_subscriber.getSubscriberId());
                Audit.record(m_username, m_clientIp, "Task returned error code " + m_progress.getFinishedCode());
            }
            else {
                Audit.record(m_username, m_clientIp, "Subscriber " + m_subscriber.getSubscriberId() + " successfully unregistered.");
            }
        }
    }
    
    
    /** Start the RegistrationApplication thread causing it to attempt a 
     * complete subscriber registration.
     */
    public void run() {
        if (m_operation == Operation.REGISTER) {
            if (!runRegisterWorkflow()) {
                Audit.record(m_username, m_clientIp, "Subscriber registration process failed for " + m_subscriber.getSubscriberId());
                Audit.record(m_username, m_clientIp, "Task returned error code " + m_progress.getFinishedCode());
            }
            else {
                Audit.record(m_username, m_clientIp, "Subscriber " + m_subscriber.getSubscriberId() + " successfully registered.");
            }
        }
        else if (m_operation == Operation.UNREGISTER) {
            if (!runUnregisterWorkflow()) {
                Audit.record(m_username, m_clientIp, "Subscriber unregistration process failed for " + m_subscriber.getSubscriberId());
                Audit.record(m_username, m_clientIp, "Task returned error code " + m_progress.getFinishedCode());
            }
            else {
                Audit.record(m_username, m_clientIp, "Subscriber " + m_subscriber.getSubscriberId() + " successfully unregistered.");
            }
        }
    }
    
    /** Starts the Registration workflow. Calls the appropriate initialize first and then
     * commences if initialization is successful
     *
     * @return true if successfully completed, or false if a failure occurred at any stage.
     */
    private boolean runRegisterWorkflow() {
        log.trace("Beginning Subscriber Registration workflow.");
        m_progress.setProgressMessageKey("task.initializing.message", new String[] { m_progress.getName() });
        
        if (!initializeRegistration()) {
            m_progress.setProgressMessageKey("registration.task.failed.initialize", null);
            return false;
        }

        Audit.record(m_username, m_clientIp, "Beginning Subscriber Registration operation for subscriber " + m_subscriber.getSubscriberId());
        m_progress.setProgressMessageKey("task.initialized.message", new String[] { m_progress.getName() });
        // mark task progress as running
        m_progress.setState(TaskState.RUNNING);

        // successfully initialized. Now create the subscriber
        m_progress.setProgressMessageKey("registration.task.creating", new String[] { m_subscriber.getSubscriberId() });
        
        if (!createSubscriber()) {
            m_progress.setProgressMessageKey("registration.task.failed.create", new String[] { m_subscriber.getSubscriberId() });
            m_progress.setState(TaskState.FAILED);
            log.trace("Finishing RegistrationApplication run with a failure. Failed at Subscriber Creation for subscriber " + m_subscriber.getSubscriberId());
            return false;
        }
        
        m_progress.setPercentageComplete(20);
        log.debug("Successfully created subscriber " + m_subscriber.getSubscriberId());        

        // now subscribe to services
        m_progress.setProgressMessageKey("registration.task.subscribing", new String[] { m_subscriber.getSubscriberId(), m_serviceFeatures.getServiceName() });
        
        if (!subscribeService()) {
            // remove subscriber from SPE via SMC API
            m_progress.setProgressMessageKey("registration.task.failure.rollback", new String[] { m_subscriber.getSubscriberId() });
            m_progress.setPercentageComplete(70);            
            if (!deleteSubscriber()) {
                log.error("Deletion of subscriber failed for subscriber id " + m_subscriber.getSubscriberId());
            }
            m_progress.setState(TaskState.FAILED);
            log.trace("Finishing RegistrationApplication run with a failure. Failed at service subscription for subscriber " + m_subscriber.getSubscriberId());
            return false;
        }
        
        m_progress.setPercentageComplete(60);
        log.debug("Successfully subscribed " + m_subscriber.getSubscriberId() + " to service " + m_serviceFeatures.getServiceName());
        
        // final step in subscription process is activation of service
        m_progress.setProgressMessageKey("registration.task.activating", new String[] { m_serviceFeatures.getServiceName(), m_subscriber.getSubscriberId() });
        if (!activateService()) {
            // unsubscribe the subscriber from service previously subscribed and then remove
            // the subscriber
            m_progress.setProgressMessageKey("registration.task.failure.rollback", new String[] { m_subscriber.getSubscriberId() });            
            m_progress.setPercentageComplete(50);
            
            if (!unsubscribeService()) {
                log.error("Unsubscription failed for subscriber id " + m_subscriber.getSubscriberId() + " for service " + m_serviceFeatures.getServiceName());
            }

            m_progress.setPercentageComplete(75);
            if (!deleteSubscriber()) {
                log.error("Deletion of subscriber failed for subscriber id " + m_subscriber.getSubscriberId());
            }
            m_progress.setState(TaskState.FAILED);
            log.trace("Finishing RegistrationApplication run with a failure. Failed at service activation for subscriber " + m_subscriber.getSubscriberId());
            return false;
        }

        m_progress.setProgressMessageKey("registration.task.complete.success", new String[] { m_subscriber.getSubscriberId() });
        m_progress.setState(TaskState.COMPLETE);      
        log.trace("Successfully completed registration of subscriber " + m_subscriber.getSubscriberId());        
        return true;
    }
    

    private boolean initialize() {
        if (m_operation == Operation.REGISTER) {
            return initializeRegistration();
        }
        else if (m_operation == Operation.UNREGISTER) {
            return initializeUnregistration();
        }
        else {
            return false;
        }
    }
    
    
    /** Called prior to the thread being run to ensure that all the necessary data has
     * been supplied in the constructor and to perform any other necessary initialization.
     * If this reports false then the thread should not run.
     * @return false if unsuccessful
     */
    private boolean initializeRegistration() {
        // first, ensure username and password supplied
        log.trace("Initializing RegistrationApplication for operator " + m_username);
        if ((m_username == null) || (m_username.length() == 0)) {
            m_progress.setFinishedCode(new Integer(INIT_MISSING_USERNAME));
            m_progress.setProgressMessageKey("registration.task.missing.username", null);            
            m_progress.setState(TaskState.FAILED);
            return false;
        }
        
        if ((m_password == null) || (m_password.length() == 0)) {
            m_progress.setFinishedCode(new Integer(INIT_MISSING_PASSWORD));
            m_progress.setProgressMessageKey("registration.task.missing.password", null);
            m_progress.setState(TaskState.FAILED);
            return false;
        }
        
        if ((m_serviceFeatures.getServiceName() == null) || (m_serviceFeatures.getServiceName().length() == 0)) {
            m_progress.setFinishedCode(new Integer(INIT_MISSING_SERVICENAME));
            m_progress.setProgressMessageKey("registration.task.missing.serviceName", null);
            m_progress.setState(TaskState.FAILED);
            return false;
        }
        
        if ((m_serviceFeatures.getServiceFeatures() == null) || (m_serviceFeatures.getServiceFeatures().length == 0)) {
            m_progress.setFinishedCode(new Integer(INIT_MISSING_SERVICEFEATURES));
            m_progress.setProgressMessageKey("registration.task.missing.serviceFeatures", null);
            m_progress.setState(TaskState.FAILED);
            return false;
        }
        
        if ((m_subscriber == null) || (m_subscriber.getSubscriberId() == null) || (m_subscriber.getSubscriberId().length() == 0)) {
            m_progress.setFinishedCode(new Integer(INIT_MISSING_SUBSCRIBERINFO));
            m_progress.setProgressMessageKey("registration.task.missing.subscriber", null);
            m_progress.setState(TaskState.FAILED);
            return false;
        }
        
        // Get the SecurityToken
        m_token = SecurityTokenCache.getInstance().getSecurityToken(m_username,m_password);
        if (m_token == null) {
            m_progress.setFinishedCode(new Integer(INIT_NO_SECURITY_TOKEN));
            m_progress.setProgressMessageKey("registration.task.noSecurityToken", new String[] { m_username });
            m_progress.setState(TaskState.FAILED);
            return false;
        }
        
        // Get the stubs that will be used to communicate with the SMC APIs
        m_subscriberStub = Config.getInstance().getSubscriberStub();
        m_subscriptionsStub = Config.getInstance().getSubscriptionsStub();
        m_provisioningStub = Config.getInstance().getProvisioningStub();
        
        if ((m_subscriberStub == null) || (m_subscriptionsStub == null) ||
            (m_provisioningStub == null)) {
            m_progress.setFinishedCode(new Integer(INIT_NO_SMC_STUBS));
            m_progress.setProgressMessageKey("registration.task.noSMCStubs", null);
            m_progress.setState(TaskState.FAILED);
            return false;
        }

        log.debug("RegistrationApplication initialization succeeded");
        return true;
    }
    
    
    private boolean initializeUnregistration() {
        // first, ensure username and password supplied
        log.trace("Initializing UnregistrationApplication for operator " + m_username);
        if ((m_username == null) || (m_username.length() == 0)) {
            m_progress.setFinishedCode(new Integer(INIT_MISSING_USERNAME));
            m_progress.setProgressMessageKey("registration.task.missing.username", null);            
            m_progress.setState(TaskState.FAILED);
            return false;
        }    
        
        if ((m_password == null) || (m_password.length() == 0)) {
            m_progress.setFinishedCode(new Integer(INIT_MISSING_PASSWORD));
            m_progress.setProgressMessageKey("registration.task.missing.password", null);
            m_progress.setState(TaskState.FAILED);
            return false;
        }
        
        if ((m_serviceFeatures.getServiceName() == null) || (m_serviceFeatures.getServiceName().length() == 0)) {
            m_progress.setFinishedCode(new Integer(INIT_MISSING_SERVICENAME));
            m_progress.setProgressMessageKey("registration.task.missing.serviceName", null);
            m_progress.setState(TaskState.FAILED);
            return false;
        }
        
        /** Service features not required for unregistration workflow
        if ((m_serviceFeatures.getServiceFeatures() == null) || (m_serviceFeatures.getServiceFeatures().length == 0)) {
            m_progress.setFinishedCode(new Integer(INIT_MISSING_SERVICEFEATURES));
            m_progress.setProgressMessageKey("registration.task.missing.serviceFeatures", null);
            m_progress.setState(TaskState.FAILED);
            return false;
        }
        */
        
        if ((m_subscriber == null) || (m_subscriber.getSubscriberId() == null) || (m_subscriber.getSubscriberId().length() == 0)) {
            m_progress.setFinishedCode(new Integer(INIT_MISSING_SUBSCRIBERINFO));
            m_progress.setProgressMessageKey("registration.task.missing.subscriber", null);
            m_progress.setState(TaskState.FAILED);
            return false;
        }
        
        // Get the SecurityToken
        m_token = SecurityTokenCache.getInstance().getSecurityToken(m_username,m_password);
        if (m_token == null) {
            m_progress.setFinishedCode(new Integer(INIT_NO_SECURITY_TOKEN));
            m_progress.setProgressMessageKey("registration.task.noSecurityToken", new String[] { m_username });
            m_progress.setState(TaskState.FAILED);
            return false;
        }
        
        // Get the stubs that will be used to communicate with the SMC APIs
        m_subscriberStub = Config.getInstance().getSubscriberStub();
        m_subscriptionsStub = Config.getInstance().getSubscriptionsStub();
        m_provisioningStub = Config.getInstance().getProvisioningStub();
        
        if ((m_subscriberStub == null) || (m_subscriptionsStub == null) ||
            (m_provisioningStub == null)) {
            m_progress.setFinishedCode(new Integer(INIT_NO_SMC_STUBS));
            m_progress.setProgressMessageKey("registration.task.noSMCStubs", null);
            m_progress.setState(TaskState.FAILED);
            return false;
        }

        log.debug("UnregistrationApplication initialization succeeded");
        return true;
    }
    
    /** Should only be called after a successful initialization call. Will attempt
     * to create the subscriber specified by the SubscriberInfo member variable.
     * <p>
     * Registration progress will be updated on the store TaskProgress. If registration
     * fails a message and final code will be inserted into the TaskProgress.
     *
     * @return true if successfully created, otherwise false. 
     */
    private boolean createSubscriber() {
        SubscriberInfo createdSubscriber = null;
        try {
            createdSubscriber = m_subscriberStub.create(m_token,m_subscriber);
        }
        catch(AuthenticationAPIException ex) {
            log.warn("No authorization to create a subscriber for user " + m_username);
            log.warn("AuthenticationAPIException Message: " + ex.getMessage());
            log.debug(ex);
            m_progress.setFinishedCode(new Integer(SUBSCRIBER_AUTHENTICATION_FAILURE));
            m_progress.setProgressMessageKey("smc.exception.authenticationFailed", new String[] { m_username });
        }
        catch (IllegalArgumentAPIException ex) {
            log.warn("Illegal arguments supplied to the create subscriber call");
            log.warn("IllegalArgumentAPIException Message: " + ex.getMessage());
            log.debug(ex);            
            m_progress.setFinishedCode(new Integer(SUBSCRIBER_ILLEGAL_ARGUMENT));
            m_progress.setProgressMessageKey("smc.exception.illegalArgument", new String[] { "Subscriber" });
        }
        catch (MissingFieldsAPIException ex) {
            log.warn("Required fields were missing from the SubscriberInfo template used to create the subscriber " + m_subscriber.getSubscriberId());
            log.warn("MissingFieldsAPIException Message: " + ex.getMessage());
            log.debug(ex);
            m_progress.setFinishedCode(new Integer(SUBSCRIBER_MISSING_FIELDS));
            m_progress.setProgressMessageKey("smc.exception.missingFields", new String[] { "SubscriberInfo","Subscriber" });
        }
        catch (SubscriberAPIException ex) {
            log.warn("Subscriber API reports a problem that prevents creation of the subscriber " + m_subscriber.getSubscriberId());
            log.warn("SubscriberAPIException Message: " + ex.getMessage());
            log.debug(ex);        
            m_progress.setFinishedCode(new Integer(SUBSCRIBER_API_FAIL));
            m_progress.setProgressMessageKey("smc.exception.api", new String[] { "Subscriber",ex.getMessage() });
        }
        catch (RemoteException ex) {
            log.warn("RemoteException occurred creating the subscriber " + m_subscriber.getSubscriberId() + " by operator " + m_username);
            log.warn("RemoteException Message: " + ex.getMessage());
            log.info("Ensure the correct URL for the Subscriber endpoint is configured.");
            log.debug(ex);
            
            // debug code to get the cause of the remote exception
            Throwable thr = ex.getCause();
            if (thr == null) {
                log.warn("No cause within this exception.");
            }
            else {
                log.warn("Cause is of class "  + thr.getClass().getName());
            }
            
            m_progress.setFinishedCode(new Integer(SUBSCRIBER_REMOTE_EXCEPTION));
            m_progress.setProgressMessageKey("smc.exception.remote", new String[] { "Subscriber",ex.getMessage() });
        }
        
        if (createdSubscriber == null) {
            return false;
        }
        else {
            m_subscriber = createdSubscriber;
        
            // update TaskProgress
            m_progress.setPercentageComplete(20);
            m_progress.setProgressMessageKey("registration.task.createdSubscriber", new String[] { m_subscriber.getSubscriberId() });
            return true;
        }
    }
    
    /**
     * Remove a subscriber identified by the id stored within the SubscriberInfo
     * member variable. This current implementation does not provide very fine
     * granularity of error codes in the TaskProgress because to do so is time 
     * consuming for an operation that will not fail particularly often.
     */
    private boolean deleteSubscriber() {
        boolean success = false;
        String subscriberId = m_subscriber.getSubscriberId();
        log.trace("Deleting subscriber " + subscriberId);
        try {
            m_subscriberStub.delete(m_token,subscriberId);
            success = true;
        }
        catch(AuthenticationAPIException ex) {
            log.warn("No authorization to delete subscriber " + subscriberId + " for user " + m_username);
            log.warn("AuthenticationAPIException Message: " + ex.getMessage());
            log.debug(ex);
        }
        catch (IllegalArgumentAPIException ex) {
            log.warn("Illegal arguments supplied to the delete subscriber call");
            log.warn("IllegalArgumentAPIException Message: " + ex.getMessage());
            log.debug(ex);            
        }
        catch (SubscriberAPIException ex) {
            log.warn("Subscriber API reports a problem that prevents deletion of the subscriber " + subscriberId);
            log.warn("SubscriberAPIException Message: " + ex.getMessage());
            log.debug(ex);        
        }
        catch (RemoteException ex) {
            log.warn("RemoteException occurred deleting the subscriber " + subscriberId + " by operator " + m_username);
            log.warn("RemoteException Message: " + ex.getMessage());
            log.info("Ensure the correct URL for the Subscriber endpoint is configured.");
            log.debug(ex);
        }
        
        if (!success) {
            m_progress.setFinishedCode(new Integer(SUBSCRIBER_DELETE_FAIL));
            m_progress.setProgressMessageKey("registration.task.failed.delete", new String[] { subscriberId, m_username });
            log.debug("Failed to delete subscriber " + subscriberId);
            return false;
        }
        else {
            m_progress.setProgressMessageKey("registration.task.deletedSubscriber", new String[] { subscriberId });
            log.debug("Successfully deleted subscriber " + subscriberId);
            return true;
        }
    }
    
    /** Called by the registration process if subscriber creation is successful.
     * This will subscribe the newly created subscriber to the indicated
     * service
     */
    private boolean subscribeService() {
        boolean success = false;
        String subscriberId = m_subscriber.getSubscriberId();
        log.trace("Subscribing " + subscriberId + " to the service " + m_serviceFeatures.getServiceName());
        
        // create array of service feature lists as required by API
        ServiceFeaturesListInfo[] sflo = new ServiceFeaturesListInfo[1];
        sflo[0] = m_serviceFeatures;
        try {
            m_subscriptionsStub.subscribe(m_token,subscriberId,sflo);
            success=true;
        }
        catch(AuthenticationAPIException ex) {
            log.warn("No authorization to subscriber subscriber " + subscriberId + " to service " + m_serviceFeatures.getServiceName());
            log.warn("AuthenticationAPIException Message: " + ex.getMessage());
            log.debug(ex);
        }
        catch (IllegalArgumentAPIException ex) {
            log.warn("Illegal arguments supplied to the subscriber call");
            log.warn("IllegalArgumentAPIException Message: " + ex.getMessage());
            log.debug(ex);            
        }
        catch (SubscriptionsAPIException ex) {
            log.warn("Subscriptions API reports a problem that prevents subscription of the subscriber " + subscriberId + " to the service " + m_serviceFeatures.getServiceName());
            log.warn("SubscriberAPIException Message: " + ex.getMessage());
            log.debug(ex);        
        }
        catch (RemoteException ex) {
            log.warn("RemoteException occurred subscribing the subscriber " + subscriberId + " by operator " + m_username);
            log.warn("RemoteException Message: " + ex.getMessage());
            log.debug(ex);
        }
        
        if (!success) {
            m_progress.setFinishedCode(new Integer(SUBSCRIPTION_CREATE_FAIL));
            m_progress.setProgressMessageKey("registration.task.failed.subscribe", new String[] { subscriberId, m_serviceFeatures.getServiceName()});
            log.debug("Failed to subscribe "  + subscriberId + " to service " + m_serviceFeatures.getServiceName());
            return false;
        }
        else {
            m_progress.setProgressMessageKey("registration.task.subscribed", new String[] { subscriberId, m_serviceFeatures.getServiceName() });
            log.debug("Successfully subscribed " + subscriberId + " to the service " + m_serviceFeatures.getServiceName());
            return true;
        }
    }
    
    /** 
     * Called should service activation fail. This method will reverse the action done
     * when creating a service subscription. This should be done prior to removing a
     * subscriber.
     */
    private boolean unsubscribeService() {
        boolean success = false;
        String subscriberId = m_subscriber.getSubscriberId();
        log.trace("Unsubscribing " + subscriberId + " from the service " + m_serviceFeatures.getServiceName());
        
        // create array of service names as required by API
        String[] serviceNames = new String[1];
        serviceNames[0] = m_serviceFeatures.getServiceName();
        try {
            m_subscriptionsStub.unsubscribe(m_token,subscriberId,serviceNames);
            success=true;
        }
        catch(AuthenticationAPIException ex) {
            log.warn("No authorization to unsubscribe subscriber " + subscriberId + " from service " + serviceNames[0]);
            log.warn("AuthenticationAPIException Message: " + ex.getMessage());
            log.debug(ex);
        }
        catch (IllegalArgumentAPIException ex) {
            log.warn("Illegal arguments supplied to the subscriber call");
            log.warn("IllegalArgumentAPIException Message: " + ex.getMessage());
            log.debug(ex);            
        }
        catch (SubscriptionsAPIException ex) {
            log.warn("Subscriptions API reports a problem that prevents unsubscription of the subscriber " + subscriberId + " from the service " + serviceNames[0]);
            log.warn("SubscriberAPIException Message: " + ex.getMessage());
            log.debug(ex);        
        }
        catch (RemoteException ex) {
            log.warn("RemoteException occurred unsubscribing the subscriber " + subscriberId + " by operator " + m_username);
            log.warn("RemoteException Message: " + ex.getMessage());
            log.debug(ex);
        }
        
        if (!success) {
            m_progress.setFinishedCode(new Integer(SUBSCRIPTION_DELETE_FAIL));
            m_progress.setProgressMessageKey("registration.task.failed.unsubscribe", new String[] { subscriberId, serviceNames[0]});
            log.debug("Failed to unsubscribe "  + subscriberId + " from the service " + serviceNames[0]);
            return false;
        }
        else {
            m_progress.setProgressMessageKey("registration.task.unsubscribed", new String[] { subscriberId, serviceNames[0] });
            log.debug("Successfully unsubscribed " + subscriberId + " from the service " + serviceNames[0]);
            return true;
        }        
    }
    
    
    /** Activate the specified service for the user
     *
     * Called by the registration process if subscriber creation is successful.
     * This will subscribe the newly created subscriber to the indicated
     * service
     */
    private boolean activateService() {
        boolean success = false;
        String subscriberId = m_subscriber.getSubscriberId();
        String serviceName = m_serviceFeatures.getServiceName();
        log.trace("Activatingthe service " + serviceName + "for subscriber " + subscriberId);
        
        try {
            m_provisioningStub.activateService(m_token,subscriberId, new String[] { serviceName });
            success=true;
        }
        catch(AuthenticationAPIException ex) {
            log.warn("No authorization to activate service " + serviceName + " for subscriber " + subscriberId);
            log.warn("AuthenticationAPIException Message: " + ex.getMessage());
            log.debug(ex);
        }
        catch (IllegalArgumentAPIException ex) {
            log.warn("Illegal arguments supplied to the service activation call");
            log.warn("IllegalArgumentAPIException Message: " + ex.getMessage());
            log.debug(ex);            
        }
        catch (ProvisioningAPIException ex) {
            log.warn("Provisioning API reports a problem that prevents activation of the service " + serviceName + " for the subscriber " + subscriberId);
            log.warn("ProvisioningAPIException Message: " + ex.getMessage());
            log.debug(ex);        
        }
        catch (RemoteException ex) {
            log.warn("RemoteException occurred activating service " + serviceName + " for the subscriber " + subscriberId + " by operator " + m_username);
            log.warn("RemoteException Message: " + ex.getMessage());
            log.debug(ex);
        }
        
        if (!success) {
            m_progress.setFinishedCode(new Integer(SERVICE_ACTIVATE_FAIL));
            m_progress.setProgressMessageKey("registration.task.failed.activate", new String[] { serviceName, subscriberId });
            log.debug("Failed to activate the service " + serviceName + " for subscriber " + subscriberId);
            return false;
        }
        else {
            m_progress.setProgressMessageKey("registration.task.activated", new String[] { serviceName, subscriberId });
            log.debug("Successfully activated service " + serviceName + " for subscriber " + subscriberId);
            return true;
        }
    }    
    
    /** Deactivate the specified service for the user
     *
     */
    private boolean deactivateService() {
        boolean success = false;
        String subscriberId = m_subscriber.getSubscriberId();
        String serviceName = m_serviceFeatures.getServiceName();
        log.trace("Deactivating the service " + serviceName + "for subscriber " + subscriberId);
        
        try {
            m_provisioningStub.deactivateService(m_token,subscriberId, new String[] { serviceName });
            success=true;
        }
        catch(AuthenticationAPIException ex) {
            log.warn("No authorization to deactivate service " + serviceName + " for subscriber " + subscriberId);
            log.warn("AuthenticationAPIException Message: " + ex.getMessage());
            log.debug(ex);
        }
        catch (IllegalArgumentAPIException ex) {
            log.warn("Illegal arguments supplied to the service deactivation call");
            log.warn("IllegalArgumentAPIException Message: " + ex.getMessage());
            log.debug(ex);            
        }
        catch (ProvisioningAPIException ex) {
            log.warn("Provisioning API reports a problem that prevents deactivation of the service " + serviceName + " for the subscriber " + subscriberId);
            log.warn("ProvisioningAPIException Message: " + ex.getMessage());
            log.debug(ex);        
        }
        catch (RemoteException ex) {
            log.warn("RemoteException occurred deactivating service " + serviceName + " for the subscriber " + subscriberId + " by operator " + m_username);
            log.warn("RemoteException Message: " + ex.getMessage());
            log.debug(ex);
        }
        
        if (!success) {
            m_progress.setFinishedCode(new Integer(SERVICE_DEACTIVATE_FAIL));
            m_progress.setProgressMessageKey("registration.task.failed.deactivate", new String[] { serviceName, subscriberId });
            log.debug("Failed to deactivate the service " + serviceName + " for subscriber " + subscriberId);
            return false;
        }
        else {
            m_progress.setProgressMessageKey("registration.task.deactivated", new String[] { serviceName, subscriberId });
            log.debug("Successfully deactivated service " + serviceName + " for subscriber " + subscriberId);
            return true;
        }
    }    
    
    
    /** Starts the Unregistration workflow. Calls the appropriate initialize first and then
     * commences if initialization is successful
     *
     * @return true if successfully completed, or false if a failure occurred at any stage.
     */
    private boolean runUnregisterWorkflow() {
        log.trace("Beginning Subscriber Unregistration workflow.");
        m_progress.setProgressMessageKey("task.initializing.message", new String[] { m_progress.getName() });
        
        if (!initializeUnregistration()) {
            m_progress.setProgressMessageKey("registration.task.failed.initialize", null);
            return false;
        }

        Audit.record(m_username, m_clientIp, "Beginning Subscriber unregistration operation for subscriber " + m_subscriber.getSubscriberId());
        m_progress.setProgressMessageKey("task.initialized.message", new String[] { m_progress.getName() });
        // mark task progress as running
        m_progress.setState(TaskState.RUNNING);

        String serviceName = m_serviceFeatures.getServiceName();
        String subscriberId = m_subscriber.getSubscriberId();        
        // final step in subscription process is activation of service
        m_progress.setProgressMessageKey("registration.task.deactivating", new String[] { serviceName, subscriberId });
        if (!deactivateService()) {
            // no point going any further with the process
            m_progress.setProgressMessageKey("unregistration.task.complete.fail", new String[] { subscriberId });            
            m_progress.setState(TaskState.FAILED);
            log.trace("Finishing RegistrationApplication run with a failure. Failed to deactivate service for subscriber " + subscriberId);
            return false;
        }

        m_progress.setProgressMessageKey("registration.task.unsubscribing", new String[] { subscriberId, serviceName });
        m_progress.setPercentageComplete(50);
        
        // now unsubscribe from service
        if (!unsubscribeService()) {
            log.debug("Unsubscription failed for subscriber id " + m_subscriber.getSubscriberId() + " for service " + m_serviceFeatures.getServiceName());            
            log.trace("Finishing RegistrationApplication run with a failure. Failed to deactivate service for subscriber " + subscriberId);            
            m_progress.setProgressMessageKey("unregistration.task.unsubscribe.fail", new String[] { subscriberId, serviceName });
            m_progress.setState(TaskState.FAILED);
            return false;
        }
         // finally, remove the subscriber
        m_progress.setPercentageComplete(75);            
        m_progress.setProgressMessageKey("registration.task.deleting", new String[] { subscriberId });
        if (!deleteSubscriber()) {
            log.debug("Deletion of subscriber failed for subscriber id " + subscriberId);
            log.trace("Finishing RegistrationApplication run with a failure. Failed to remove subscriber from system.");
            m_progress.setProgressMessageKey("unregistration.task.delete.fail", new String[] { subscriberId, serviceName });
            m_progress.setState(TaskState.FAILED);
            return false;
        }
        
        m_progress.setProgressMessageKey("unregistration.task.complete.success", new String[] { subscriberId });
        m_progress.setState(TaskState.COMPLETE);      
        log.trace("Successfully completed unregistration of subscriber " + subscriberId);        
        return true;
    }
    
}

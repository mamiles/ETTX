/*
 * Forwards.java
 *
 * Created on 23 April 2003, 00:40
 */

package com.cisco.ettx.provisioning.action;

/**
 * Forwards used by the actions. This class provides a single location where
 * forwards may be updated for the application
 *
 * @author  pcurren
 */
final class Forwards {
    
    public static String FAILURE_FORWARD        = "failure";
    public static String HOME_FORWARD           = "home";
    public static String LOGIN_FORWARD          = "login";
    public static String LOCAL_SUCCESS_FORWARD  = "success";
    public static String PROGRESS_CHECK_FORWARD = "task/progress/forward/check";
    public static String REGISTRATION_DETAILS   = "registration/details";
    public static String REGISTRATION_PAYMENT   = "registration/payment";
    public static String REGISTRATION_LOGIN     = "registration/login";
    public static String REGISTRATION_SERVICE   = "registration/service";
    public static String REGISTRATION_SUMMARY   = "registration/summary";
    public static String REGISTRATION_COMPLETE  = "task/forwards/registrationSuccess";
    public static String REGISTRATION_FAILED    = "task/forwards/registrationFailed";
    public static String SEARCH_RESULT          = "search/result";
    public static String UNREGISTRATION_COMPLETE= "task/forwards/unregistrationSuccess";
    public static String UNREGISTRATION_FAILED  = "task/forwards/unregistrationFailed";
    
    
    private Forwards() {
    }
    
}

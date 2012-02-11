/*
 * SubscriberDefaultSuggestor.java
 *
 * Created on 28 April 2003, 05:59
 */

package com.cisco.ettx.provisioning.application;

import com.cisco.sesm.sms.types.SubscriberInfo;

/**
 * Given information about a Subscriber in the form of a SubscriberInfo,
 * this static utility class would generate valid suggestions for a username,
 * or other fields for the new (not created yet) subscriber.
 * <p>
 * This task should also involve checking that the generated suggestion(s) are
 * not already taken. However, the implementation at this stage is much simpler
 * than that and only generates a username consisting of the first character
 * of the forename appended to the surname, and within 20 characters.
 * <p>
 * This should only be considered a demo implementation at this stage.
 *
 * @author  pcurren
 */
public class SubscriberDefaultSuggestor {
    
    /** Creates a new instance of LoginNameSuggestor */
    private SubscriberDefaultSuggestor() {
    }
    
    /** Given a SubscriberInfo describing a Subscriber, generates a selection of
     * usernames that could be used by this user.
     * <p>
     * This implementation will only ever return a single selection i.e. a one
     * entry array, and it will not confirm that the suggestion is usable. 
     * (Doesn't check if the username is already taken.)
     * <p>
     * For this implementation, if there are no forename or surname values in
     * the subscriber info then a single suggestion of empty String 
     * will be returned.
     */
    public static String[] suggestUsernames(SubscriberInfo info) {
       String forename = info.getGivenName();
       String surname = info.getFamilyName();
       
       if ((surname == null) || (surname.length() == 0)) {
           return new String[] { "" };
       }
       
       if ((forename == null)  || (forename.length() == 0)) {
            return new String[] { surname };
       }

       char forenameInitialChar = forename.charAt(0);
       String forenameInitial = new String(new char[] { forenameInitialChar });
       
       // finally ensure that we don't return a username longer than 20 chars
       StringBuffer buf = new StringBuffer(forenameInitial);
       buf.append(surname);
       String returnValue = null;
       if (buf.length() > 20) {
            returnValue = buf.substring(0,20);
       }
       else {
           returnValue = buf.toString();
       }
       
       return new String[] { returnValue.toLowerCase() };
    }
 
    /**
     * Only generates a single option in this implementation.
     */
    public static String[] suggestScreennames(SubscriberInfo info) {
       String forename = info.getGivenName();
       String surname = info.getFamilyName();
       String city = info.getCity();
       
       StringBuffer buf = new StringBuffer();
       
       if ((forename != null) && (forename.length() != 0)) {
            buf.append(forename);
            buf.append("_");
       }
       
       if ((surname != null) && (surname.length() != 0)) {
            buf.append(surname);
            buf.append("_");
       }
       
       if ((city != null) && (city.length() != 0)) {
            buf.append(city);
       }
       
       // if String ends in '_' then remove final underscore
        if (buf.length() != 0) {
            char finalChar = buf.charAt(buf.length()-1);
            if (finalChar == '_') {
                buf.deleteCharAt(buf.length()-1);
            }
        }
        
        return new String[] { buf.toString() };
    }
    
}

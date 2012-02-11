/*
 * Utils.java
 *
 * Created on 28 April 2003, 06:03
 */

package com.cisco.ettx.provisioning.action;

import com.cisco.sesm.sms.types.ServiceFeatureInfo;
import com.cisco.sesm.sms.types.ServiceInfo;
import com.cisco.sesm.sms.types.SubscriberInfo;
import com.cisco.sesm.sms.types.SwitchInfo;
import java.util.Properties;
import org.apache.struts.action.DynaActionForm;

/**
 * Helpful utilities primarily intended for helping interface the action
 * classes with the application logic
 *
 * @author  pcurren
 */
public class Utils {

    // initialized once to supply a mapping between page titles and their forwards
    private static Properties titleMap = null;
    
    /** Creates a new instance of Utils */
    private Utils() {
    }
    
    /**
     * Take the appropriate properties from the supplied DynaActionForm and
     * set them on the matching properties in the supplied SubscriberInfo.
     * If any of the properties already exist in the SubscriberInfo they will be
     * overwritten.
     * <p>
     * Should no info parameter be supplied then a new one is created and 
     * returned, otherwise the supplied SubscriberInfo is returned.
     * <p>
     * If there are no properties in the supplied form or the form is null
     * then an empty SubscriberInfo (or an unchanged one) will be returned
     * 
     * @param copyblanks with this flag set true then any properties which are
     * blank or null within the form parameter will be set on the SubscriberInfo.
     * Otherwise any null or empty String values will not be set onto the
     * SubscriberInfo.
     */
     static SubscriberInfo populateSubscriberInfoFromForm(SubscriberInfo info,
                                                          DynaActionForm form,
                                                          boolean copyblanks) {
        if (info == null) {
            info = new SubscriberInfo();
        }
        
        if (form == null) {
            return info;
        }
        
        String formValue = (String)form.get("city");
        if (copyblanks) {
            info.setCity(formValue);
        }
        else if ((formValue != null) && (formValue.length() != 0)) {
            info.setCity(formValue);
        }
        
        formValue = (String)form.get("country");
        if (copyblanks) {
            info.setCountryCode(formValue);
        }
        else if ((formValue != null) && (formValue.length() != 0)) {
            info.setCountryCode(formValue);
        }

        formValue = (String)form.get("dob");
        if (copyblanks) {
            info.setAgeGroup(formValue);
        }
        else if ((formValue != null) && (formValue.length() != 0)) {
            info.setAgeGroup(formValue);
        }
        
        formValue = (String)form.get("forename");
        if (copyblanks) {
            info.setGivenName(formValue);
        }
        else if ((formValue != null) && (formValue.length() != 0)) {
            info.setGivenName(formValue);
        }
        
        formValue = (String)form.get("homeNumber");
        if (copyblanks) {
            info.setHomeNumber(formValue);
        }
        else if ((formValue != null) && (formValue.length() != 0)) {
            info.setHomeNumber(formValue);
        }
        
        formValue = (String)form.get("initials");
        if (copyblanks) {
            info.setInitials(formValue);
        }
        else if ((formValue != null) && (formValue.length() != 0)) {
            info.setInitials(formValue);
        }
        
        formValue = (String)form.get("mobileNumber");
        if (copyblanks) {
            info.setMobileNumber(formValue);
        }
        else if ((formValue != null) && (formValue.length() != 0)) {
            info.setMobileNumber(formValue);
        }

        formValue = (String)form.get("password");
        if (copyblanks) {
            info.setPassword(formValue);
        }
        else if ((formValue != null) && (formValue.length() != 0)) {
            info.setPassword(formValue);
        }
        
        formValue = (String)form.get("postcode");
        if (copyblanks) {
            info.setPostalCode(formValue);
        }
        else if ((formValue != null) && (formValue.length() != 0)) {
            info.setPostalCode(formValue);
        }
        
        formValue = (String)form.get("screenName");
        if (copyblanks) {
            info.setDisplayName(formValue);
        }
        else if ((formValue != null) && (formValue.length() != 0)) {
            info.setDisplayName(formValue);
        }

        formValue = (String)form.get("state");
        if (copyblanks) {
            info.setPostalState(formValue);
        }
        else if ((formValue != null) && (formValue.length() != 0)) {
            info.setPostalState(formValue);
        }
        
        formValue = (String)form.get("selectedPort");
        if (copyblanks) {
            info.setPortInterface(formValue);
        }
        else if ((formValue != null) && (formValue.length() != 0)) {
            info.setPortInterface(formValue);
        }

        formValue = (String)form.get("selectedSwitch");
        if (copyblanks) {
            info.setSwitchId(formValue);
        }
        else if ((formValue != null) && (formValue.length() != 0)) {
            info.setSwitchId(formValue);
        }
        
        formValue = (String)form.get("street");
        if (copyblanks) {
            info.setStreetAddress(formValue);
        }
        else if ((formValue != null) && (formValue.length() != 0)) {
            info.setStreetAddress(formValue);
        }
        
        formValue = (String)form.get("surname");
        if (copyblanks) {
            info.setFamilyName(formValue);
        }
        else if ((formValue != null) && (formValue.length() != 0)) {
            info.setFamilyName(formValue);
        }
        
        formValue = (String)form.get("username");
        if (copyblanks) {
            // account Id should be set to login id in current implementation            
            info.setSubscriberId(formValue);
            info.setAccountId(formValue);
        }
        else if ((formValue != null) && (formValue.length() != 0)) {
            info.setSubscriberId(formValue);
        }

        return info;
    }
    
    /** Utility method used for auditing. Return the full name for the 
     * subscriber from the submitted form. If there is no user details
     * then simply return the String <none>
     */
    static String getUserFullName(DynaActionForm form) {
        
        String forename = (String)form.get("forename");
        String initials = (String)form.get("initials");
        String surname = (String)form.get("surname");
        
        if (((forename == null) || (forename.length() == 0)) && 
            ((initials == null) || (initials.length() == 0)) &&
            ((surname == null) || (surname.length() == 0))) {
                return "none";
        }
                
        StringBuffer fullname = new StringBuffer();
        fullname.append(forename);
        fullname.append(" ");
        fullname.append(initials);
        fullname.append(" ");
        fullname.append(surname);
        return fullname.toString();
    }    


    /**
     * Utility method that converts the supplied nextTitle property supplied by
     * the form bean to a forward.
     */
    static String getForwardForTitle(String title) {
        if (titleMap == null) {
            buildForwardMapping();
        }
        
        return titleMap.getProperty(title);
    }
    
    /** Should only be called a single time to build a mapping of page title
     * Strings to Struts forwards
     */
    private static void buildForwardMapping() {
        titleMap = new Properties();
        titleMap.setProperty("registration.details.title",Forwards.REGISTRATION_DETAILS);
        titleMap.setProperty("registration.payment.title",Forwards.REGISTRATION_PAYMENT);
        titleMap.setProperty("registration.login.title",Forwards.REGISTRATION_LOGIN);
        titleMap.setProperty("registration.service.title",Forwards.REGISTRATION_SERVICE);
        titleMap.setProperty("registration.summary.title",Forwards.REGISTRATION_SUMMARY);
    }
    
    /**
     * Utility method to convert the application logic's ServiceInfo structure
     * into the ETTXServiceBean used by the view component (the jsp's)
     *
     * @return an ETTXServiceBean containing the information in the supplied
     * ServiceInfo. If the supplied service info is null, then null will be
     * returned. Likewise, if it contains no fields then neither will the
     * returned ETTXServiceBean
     */
    public static ETTXServiceBean convertToETTXServiceBean(ServiceInfo info) {
        if (info == null) {
            return null;
        }
        
        ETTXServiceBean bean = new ETTXServiceBean();
        bean.setServiceName(info.getServiceName());
        bean.setServiceDescription(info.getServiceDescription());
        bean.setServiceProfileName(info.getServiceProfileName());
        
        ServiceFeatureInfo[] features = info.getServiceFeatures();
        if (features != null) {
            ETTXServiceFeatureBean[] featureBeans 
                = new ETTXServiceFeatureBean[features.length];
            for (int i=0; i < features.length; i++) {
                featureBeans[i] = new ETTXServiceFeatureBean();
                featureBeans[i].setDefaultValue(features[i].getDefaultValue());
                featureBeans[i].setFeatureName(features[i].getServiceFeatureName());
                featureBeans[i].setSelectedValue(features[i].getSelectedValue());
                featureBeans[i].setPossibleValues(features[i].getAllowedValues());
            }
            bean.setFeatures(featureBeans);
        }
        else {
            bean.setFeatures(null);
        }
        
        return bean;
    }
    
    /**
     * Utility method to convert the application logic's SwitchInfo structure
     * into the ETTXSwitchBean used by the view component (the jsp's)
     *
     * @return an ETTXSwitchBean containing the information in the supplied
     * SwitchInfo. If the supplied SwitchInfo is null, then null will be
     * returned. Likewise, if it contains no fields then neither will the
     * returned ETTXSwitchBean
     */
    public static ETTXSwitchBean convertToETTXSwitchBean(SwitchInfo info) {
        if (info == null) {
            return null;
        }
    
        ETTXSwitchBean bean = new ETTXSwitchBean();
        bean.setId(info.getId());
        bean.setCnoteServer(info.getCnoteServer());
        bean.setConfigDownloadMethod(info.getConfigDownloadMethod());
        bean.setConfigRegistrar(info.getConfigRegistrar());
        bean.setConnectionId(info.getConnectionId());
        bean.setDhcpServer(info.getDhcpServer());
        bean.setEdgeDeviceId(info.getEdgeDeviceId());
        bean.setFqdn(info.getFqdn());
        bean.setHostName(info.getHostName());
        bean.setInterfaceTypes(info.getInterfaceTypes());
        bean.setIpAddress(info.getIpAddress());
        bean.setIpAutoAllocate(info.getIpAutoAllocate());
        bean.setLocationName(info.getLocationName());
        bean.setMacAddress(info.getMacAddress());
        bean.setManagementVlanId(info.getManagementVlanId());
        bean.setModel(info.getModel());
        bean.setSubType(info.getSubType());
        bean.setSubnetMask(info.getSubnetMask());
        bean.setTechnology(info.getTechnology());
        bean.setTelnetGateway(info.getTelnetGateway());
        bean.setTerminalServer(info.getTerminalServer());
        bean.setTerminalServerPortNumber(info.getTerminalServerPortNumber());
        bean.setType(info.getType());
        bean.setUseSsh(info.getUseSsh());
        bean.setVendorName(info.getVendorName());
        
        return bean;
    }
    
}

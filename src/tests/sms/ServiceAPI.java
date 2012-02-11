/**
 * Class: ServiceAPI
 *
 * Copyright (c) 2003 by Cisco Systems, Inc. All rights reserved.
 *
 * Author: djf
 *
 */

package com.cisco.sesm.sms.extensions;
import com.cisco.sesm.sms.exceptions.api.*;
import com.cisco.sesm.sms.types.*;
import com.cisco.cns.security.soap.common.*;

/**
 * ServiceAPI
 *
 * @author David Fletcher
 */
public class ServiceAPI
{

    /**
     * Return the list of all services potentially available within the SMS
     * system without reference to those available for particular users. <p> The
     * services in the returned list won't necessary be available to all users -
     * {@link #queryAvailableServices(SecurityToken,String) queryAvailableServices}
     * should be used for this purpose.
     *
     * @param token the SMS security token previously retrieved.
     * @return array containing all the services available within SMS
     * @throws AuthenticationAPIException if the security token is invalid
     * @throws ServiceAPIException if a failure occurs internal to the Service
     * interface
     */
    public ServiceInfo[] queryAllServices(SecurityToken token)
	throws AuthenticationAPIException , IllegalArgumentAPIException
   {

		if (token == null) throw new AuthenticationAPIException("Authen Failure");
		ServiceInfo[] services = new ServiceInfo[2];
		services[0] = new ServiceInfo();
		services[0].setServiceName("Gold Service");
		ServiceFeatureInfo[] features = new ServiceFeatureInfo[3];
		features[0] = new ServiceFeatureInfo();
		features[1] = new ServiceFeatureInfo();
		features[2] = new ServiceFeatureInfo();
		features[0].setServiceFeatureName("Gold Service");
		features[0].setAllowedValues(new String[]{"10 MB", "250 KB", "1 MB"});
		features[0].setDefaultValue("10 MB");
		features[1].setServiceFeatureName("IP Address Pool");
		features[1].setAllowedValues(new String[]{"1","2","3","4"});
		features[1].setDefaultValue("3");
		features[2].setServiceFeatureName("IP Address");
		//features[2].setAllowedValues(new String[]{""});
		features[2].setAllowedValues(null);
		features[2].setDefaultValue("");
		services[0].setServiceFeatures(features);

		services[1] = new ServiceInfo();
		services[1].setServiceName("Silver Service");
		ServiceFeatureInfo[] features1 = new ServiceFeatureInfo[2];
		features1[0] = new ServiceFeatureInfo();
		features1[1] = new ServiceFeatureInfo();
		features1[0].setServiceFeatureName("Silver Service");
		features1[0].setAllowedValues(new String[]{"250 KB", "1 MB"});
		features1[0].setDefaultValue("250 KB");
		features1[1].setServiceFeatureName("IP Address Pool");
		features1[1].setAllowedValues(new String[]{"1","2","3"});
		features1[1].setDefaultValue("1");
		services[1].setServiceFeatures(features1);
		return services;
    }
}

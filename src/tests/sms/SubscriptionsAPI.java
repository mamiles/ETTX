/**
 * Class: SubscriptionsAPI
 *
 * Copyright (c) 2003 by Cisco Systems, Inc. All rights reserved.
 *
 * Author: djf
 *
 */

package com.cisco.sesm.sms.extensions;
import com.cisco.cns.security.soap.common.*;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.io.*;
import java.util.HashMap;
import java.util.Vector;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import com.cisco.sesm.sms.exceptions.api.*;
import com.cisco.sesm.sms.types.*;

/** 
 * SubscriptionsAPI
 *
 * @author David Fletcher
 */
public class SubscriptionsAPI 
{

    /**
     * Return all the subscribers that match the supplied filter.
     *
     * @param token the SMS security token previously retrieved
     * @param filter the filter to be used to find matching subscribers
     * @return an array of <code>SubscriptionsInfo</code> objects specifying each
     * of the subscribers selected by the supplied filter. Should there be no
     * matches, a zero length array will be returned.
     * @throws AuthenticationAPIException if the security token is invalid
     * @throws MissingFieldsAPIException should required fields be missing from the
     * <code>FilterInfo</code> parameter
     * @throws SubscriptionsAPIException if a failure occurs within the 
     * Subscriptions service
     * @see com.cisco.sesm.sms.types.FilterInfo
     */
	private static Logger logger = Logger.getLogger(SubscriptionsAPI.class);

    public void subscribe(SecurityToken token,String subsId,
			   ServiceFeaturesListInfo[] services)
	throws AuthenticationAPIException , IllegalArgumentAPIException, SubscriptionsAPIException
   {

		if (token == null) throw new AuthenticationAPIException("Authen Failure");
		logger.info("Subscribing " + subsId + " to services " + services);
		for (int i = 0; i < services.length; i++) {
			logger.info("Service " + services[i].getServiceName());
			ServiceFeatureInfo[] features = services[i].getServiceFeatures();
			if (features != null) {
			for (int j = 0; j < features.length; j++) {
				logger.info("feature  " + features[j].getServiceFeatureName() + " " + features[j].getSelectedValue());
			}
			}
		}
    }

    public void unsubscribe(SecurityToken token,String subsId,
			   String[] serviceNames)
	throws AuthenticationAPIException , IllegalArgumentAPIException, SubscriptionsAPIException
   {

		if (token == null) throw new AuthenticationAPIException("Authen Failure");
		logger.info("Unsubscribing " + subsId + " from services " + serviceNames);
    }

	public ServiceInfo[] querySubscribedServices(SecurityToken token, String subsId) 
	throws AuthenticationAPIException , IllegalArgumentAPIException, SubscriptionsAPIException
   {

	if (token == null) throw new AuthenticationAPIException("Authen Failure");
	logger.info("querying subscribed services " + subsId );
	ServiceInfo[] services = new ServiceInfo[1]; services[0] = new ServiceInfo();
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
	features[2].setAllowedValues(new String[]{});
	features[1].setSelectedValue("1.2.3.4");

	services[0].setServiceFeatures(features);
	return services;
    }
}

/**
 * Class: SubscriberAPI
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
 * SubscriberAPI
 *
 * @author David Fletcher
 */
public class SubscriberAPI
{

    /**
     * Return all the subscribers that match the supplied filter.
     *
     * @param token the SMS security token previously retrieved
     * @param filter the filter to be used to find matching subscribers
     * @return an array of <code>SubscriberInfo</code> objects specifying each
     * of the subscribers selected by the supplied filter. Should there be no
     * matches, a zero length array will be returned.
     * @throws AuthenticationAPIException if the security token is invalid
     * @throws MissingFieldsAPIException should required fields be missing from the
     * <code>FilterInfo</code> parameter
     * @throws SubscriberAPIException if a failure occurs within the
     * Subscriber service
     * @see com.cisco.sesm.sms.types.FilterInfo
     */
	private static String SUBS_FILE = "subsRecords.csv";
	private static String[] attrs = new String[] {
	"id",
    	"subscriberId",
        "displayName",
	"accountId",
    	"accountStatus",
    	"homeNumber",
    	"mobileNumber",
	"streetAddress",
    	"city",
    	"postalState",
    	"countryCode",
    	"postalCode",
    	"subscribedServices"};
	private static Logger logger = Logger.getLogger(SubscriberAPI.class);

	private static String[] attrTypes = new String[] {
	"String",
    	"String",
        "String",
	"String",
    	"String",
    	"String",
    	"String",
	"String",
    	"String",
	"String",
    	"String",
    	"String",
    	"String[]"};

    public SubscriberInfo[] query(SecurityToken token,
					    String filter)
	throws AuthenticationAPIException , IllegalArgumentAPIException, SubscriberAPIException
   {

		if (token == null) throw new AuthenticationAPIException("Authen Failure");
		HashMap subss = new HashMap();
		readSubsFromFile(SUBS_FILE,subss);
		if (filter.startsWith("(displayName=")) {
			Object[] list = subss.values().toArray();
			String subs = filter.substring(13,filter.length()-1);
			logger.info("Finding subscriber " + subs);
			for (int i = 0; i < list.length; i++) {
				SubscriberInfo info = (SubscriberInfo)list[i];
				if (subs.equals(info.getDisplayName())) {
					logger.info("Found Subscriber " + info.getId());

					SubscriberInfo[] newList = new SubscriberInfo[1];
					newList[0] = info;
					return newList;
				}
			}
			return null;
		}
		return convertToList(subss.values().toArray());
    }

    public SubscriberInfo[] query(SecurityToken token,
					    SubscriberInfo subsInfo)
	throws AuthenticationAPIException , IllegalArgumentAPIException, SubscriberAPIException
   {

		if (token == null) throw new AuthenticationAPIException("Authen Failure");
		HashMap subss = new HashMap();
		readSubsFromFile(SUBS_FILE,subss);
		if (subsInfo.getSubscriberId() != null && subsInfo.getSubscriberId().length() != 0) {
			Object[] list = subss.values().toArray();
			String subs = subsInfo.getSubscriberId();
			logger.info("Finding subscriber " + subs);
			for (int i = 0; i < list.length; i++) {
				SubscriberInfo info = (SubscriberInfo)list[i];
				if (subs.equals(info.getSubscriberId())) {
					logger.info("Found Subscriber " + info.getId());

					SubscriberInfo[] newList = new SubscriberInfo[1];
					newList[0] = info;
					return newList;
				}
			}
			return null;
		}
		return convertToList(subss.values().toArray());

    }

	private SubscriberInfo[] convertToList(Object[] list) {
		SubscriberInfo[] newList = new SubscriberInfo[list.length];
		for (int i = 0; i < list.length; i++) {
			newList[i] = (SubscriberInfo)list[i];
		}
		return newList;
	}

    public SubscriberInfo create(SecurityToken token,SubscriberInfo info)

	throws AuthenticationAPIException , IllegalArgumentAPIException, SubscriberAPIException
   {
		if (token == null) throw new AuthenticationAPIException("Authen Failure");
		logger.info("Creating subscriber "+ info.getSubscriberId() +
			" " + info.getDisplayName()  +
			" " + info.getGivenName()  +
			" " + info.getFamilyName()  +
			" " + info.getPassword()  +
			" " + info.getHomeNumber()  +
			" " + info.getMobileNumber()  +
			" " + info.getStreetAddress()  +
			" " + info.getCity()  +
			" " + info.getPostalState()  +
			" " + info.getCountryCode()  +
			" " + info.getPostalCode());
		HashMap subss = new HashMap();
		readSubsFromFile(SUBS_FILE,subss);
		//check if subscriber is already there
		if (subss.get(info.getSubscriberId()) != null) {
			throw new SubscriberAPIException("Subscriber already exists");
		}
		return info;
    }

	/**
	 * Registers a device to a subscriber based on the DeviceInfo template
	 * passed. If the device template specifies that the device should be
	 * active then a call to activateSubscriberDevice is made.
	 * @param token the security token to authorize this method call
	 * @param subscriberId the id of the subscriber on which to operate
	 * @param template the template on which to base the new device
	 * @return the new DeviceInfo instance
	 * @throws AuthenticationAPIException if the security token is invalid
	 * @throws SubscriberAPIException if a failure occurs within the
	 *         Subscriber service
	 */
	public DeviceInfo registerSubscriberDevice(SecurityToken token,
											   String subscriberId,
											   DeviceInfo template)
		throws IllegalArgumentAPIException, AuthenticationAPIException,
		SubscriberAPIException
	{
		if (token == null) throw new AuthenticationAPIException("Authen Failure");
		if (subscriberId == null) throw new IllegalArgumentAPIException("Subscriber ID required");
		logger.info("Register MAC Address: " + template.getMacAddress() + " " + template.getAlias() + " to subscriber: " + subscriberId);
		return template;
	}


    public SubscriberInfo modify(SecurityToken token,SubscriberInfo info)

	throws AuthenticationAPIException , IllegalArgumentAPIException, SubscriberAPIException
   {
		if (token == null) throw new AuthenticationAPIException("Authen Failure");
		logger.info("Modifying subscriber "+ info.getSubscriberId() +
			" " + info.getDisplayName()  +
			" " + info.getGivenName()  +
			" " + info.getFamilyName()  +
			" " + info.getPassword()  +
			" " + info.getHomeNumber()  +
			" " + info.getMobileNumber()  +
			" " + info.getStreetAddress()  +
			" " + info.getCity()  +
			" " + info.getPostalState()  +
			" " + info.getCountryCode()  +
			" " + info.getPostalCode());
		return info;
    }

    public void delete(SecurityToken token,String subsId)

	throws AuthenticationAPIException , IllegalArgumentAPIException, SubscriberAPIException
   {
		if (token == null) throw new AuthenticationAPIException("Authen Failure");
		logger.info("Deleting subscriber "+ subsId);
    }

    public SubscriberInfo[] findSubscribersBySwitch(SecurityToken token,
                                                    SwitchInfo switchPort)
	throws AuthenticationAPIException , IllegalArgumentAPIException, SubscriberAPIException
   {
		if (token == null) throw new AuthenticationAPIException("Authen Failure");
		HashMap subss = new HashMap();
		readSubsFromFile(SUBS_FILE,subss);
		return convertToList(subss.values().toArray());
    }


    public SubscriberInfo[] findSubscribersByService(SecurityToken token,
                                                     String serviceName)
	throws AuthenticationAPIException , IllegalArgumentAPIException, SubscriberAPIException
   {
		if (token == null) throw new AuthenticationAPIException("Authen Failure");
		HashMap subss = new HashMap();
		readSubsFromFile(SUBS_FILE,subss);
		return convertToList(subss.values().toArray());
    }

    public SubscriberInfo getSubscriberInfo(SecurityToken token,
                                            String subscriberId)
	throws AuthenticationAPIException , IllegalArgumentAPIException, SubscriberAPIException
   {
		if (token == null) throw new AuthenticationAPIException("Authen Failure");
		HashMap subss = new HashMap();
		readSubsFromFile(SUBS_FILE,subss);
		SubscriberInfo info = (SubscriberInfo)subss.get(subscriberId);
		if (info != null) {
			DeviceInfo[] devices = new DeviceInfo[2];
			devices[0] = new DeviceInfo();
			devices[1] = new DeviceInfo();
			devices[0].setMacAddress("101010101010");
			devices[0].setAlias("Addr 1");
			devices[0].setActive(new Boolean(true));
			devices[1].setMacAddress("101010102100");
			devices[1].setActive(new Boolean(false));
			devices[1].setAlias("Addr 2");
			info.setDevices(devices);
		}
		return info;
			
    }

	private void readSubsFromFile(String fileName,HashMap subs)
		throws SubscriberAPIException {
		try {
		FileInputStream str = new FileInputStream(fileName);
		StreamTokenizer t = new StreamTokenizer(str);
		t.whitespaceChars(',',',');
		t.ordinaryChar(' ');
		t.wordChars(' ',' ');
		t.ordinaryChars('0','9');
		t.wordChars('0','9');
		t.eolIsSignificant(true);
		int ttype = StreamTokenizer.TT_EOF;
		while (true) {
			SubscriberInfo info = new SubscriberInfo();
			for (int i = 0; i < attrs.length;i++) {
				Object value = null;
				String attrName = attrs[i];
				ttype = t.nextToken();
				logger.debug("Got token " + t.toString());
				logger.debug("Attr is " + attrs[i] + " Type is " + attrTypes[i]);
				if (ttype == StreamTokenizer.TT_EOL) {
					//Thats all the fields we have
					break;
				}
				if (ttype == StreamTokenizer.TT_EOF) {
					break;
				}
				else {
					if (attrTypes[i].equals("String[]")) {
						value = convertToVector(t.sval);
					}
					else {
						value = t.sval;
					}
				}
				logger.debug("Adding value " + value + " to property " + attrName + " in SubscriberInfo");
				PropertyDescriptor prop = new PropertyDescriptor(
						attrName,SubscriberInfo.class);
				Method setMethod = prop.getWriteMethod();
				setMethod.invoke(info,new Object[] {value});
			}
			// add to
			ttype = t.nextToken();
			if (ttype == StreamTokenizer.TT_EOF) {
				return;
			}
			if (ttype != StreamTokenizer.TT_EOL) {
				logger.error("Getting unexpected token from the file " + t.toString());
				throw new SubscriberAPIException("Unable to read file");
			}
			ServiceFeatureInfo[] features = new ServiceFeatureInfo[1];
			features[0] = new ServiceFeatureInfo();
			features[0].setServiceFeatureName("Gold Service");
			features[0].setAllowedValues(new String[]{"10 MB", "250 KB", "1 MB"});
			features[0].setDefaultValue("10 MB");
			features[0].setSelectedValue("10 MB");

			ServiceFeaturesListInfo[] services = new ServiceFeaturesListInfo[1]; services[0] = new ServiceFeaturesListInfo();
			services[0].setServiceName("Gold Service");
			services[0].setServiceFeatures(features);

			info.setServiceFeatures(services);
			subs.put(info.getId(),info);

		}
		} catch (Exception ex) {
			logger.error("Unable to create objects of class SubscriberInfo",ex);
			throw new SubscriberAPIException("Unable to read file");
		}
	}

	private String[] convertToVector(String value) {
		StringTokenizer t = new StringTokenizer(value,",");
		String[] v = new String[t.countTokens()];
		int i = 0;
		while (t.hasMoreElements()) {
			v[i] = (String)t.nextElement();
			i++;
		}
		return v;
	}

	/**
	 * Registers a device to a subscriber based on the DeviceInfo template
	 * passed. If the device template specifies that the device should be
	 * active then a call to activateSubscriberDevice is made.
	 * @param token the security token to authorize this method call
	 * @param subscriberId the id of the subscriber on which to operate
	 * @param template the template on which to base the new device
	 * @return the new DeviceInfo instance
	 * @throws AuthenticationAPIException if the security token is invalid
	 * @throws SubscriberAPIException if a failure occurs within the
	 *         Subscriber service
	 */
	public DeviceInfo unregisterSubscriberDevice(SecurityToken token,
											   String subscriberId,
											   DeviceInfo template)
		throws IllegalArgumentAPIException, AuthenticationAPIException,
		SubscriberAPIException
	{
		if (token == null) throw new AuthenticationAPIException("Authen Failure");
		if (subscriberId == null) throw new IllegalArgumentAPIException("Subscriber ID required");
		logger.info("Unregister MAC Address: " + template.getMacAddress() + " to subscriber: " + subscriberId);
		return template;
	}

	/**
	 * Registers a device to a subscriber based on the DeviceInfo template
	 * passed. If the device template specifies that the device should be
	 * active then a call to activateSubscriberDevice is made.
	 * @param token the security token to authorize this method call
	 * @param subscriberId the id of the subscriber on which to operate
	 * @param template the template on which to base the new device
	 * @return the new DeviceInfo instance
	 * @throws AuthenticationAPIException if the security token is invalid
	 * @throws SubscriberAPIException if a failure occurs within the
	 *         Subscriber service
	 */
	public DeviceInfo modifySubscriberDevice(SecurityToken token,
											   String subscriberId,
											   DeviceInfo template)
		throws IllegalArgumentAPIException, AuthenticationAPIException,
		SubscriberAPIException
	{
		if (token == null) throw new AuthenticationAPIException("Authen Failure");
		if (subscriberId == null) throw new IllegalArgumentAPIException("Subscriber ID required");
		logger.info("Modify MAC Address: " + template.getMacAddress() + " to subscriber: " + subscriberId);
		return template;
	}

    public SubscriberInfo[] querySubscriberByDevice(SecurityToken token,
					    DeviceInfo deviceInfo)
	throws AuthenticationAPIException , IllegalArgumentAPIException, SubscriberAPIException
   {

		if (token == null) throw new AuthenticationAPIException("Authen Failure");
		logger.info("Query MAC Address: " + deviceInfo.getMacAddress());
		if (!deviceInfo.getMacAddress().equals("101010101010")) {
			return new SubscriberInfo[0];
		}
		HashMap subss = new HashMap();
		readSubsFromFile(SUBS_FILE,subss);
		SubscriberInfo[] info = new SubscriberInfo[1];
		info[0] = (SubscriberInfo)subss.get("Subs 1");
		return info;
    }

}

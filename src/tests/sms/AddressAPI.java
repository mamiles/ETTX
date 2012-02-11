/**
 * Class: AddressAPI
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
 * AddressAPI
 *
 */
public class AddressAPI
{

    /**
     * Return all the subscribers that match the supplied filter.
     *
     * @param token the SMS security token previously retrieved
     * @param filter the filter to be used to find matching subscribers
     * @return an array of <code>AddressInfo</code> objects specifying each
     * of the subscribers selected by the supplied filter. Should there be no
     * matches, a zero length array will be returned.
     * @throws AuthenticationAPIException if the security token is invalid
     * @throws MissingFieldsAPIException should required fields be missing from the
     * <code>FilterInfo</code> parameter
     * @throws AddressAPIException if a failure occurs within the
     * Address service
     * @see com.cisco.sesm.sms.types.FilterInfo
     */
	private static String ADDR_FILE = "addrRecords.csv";
	private static String[] attrs = new String[] {
	"id",
    	"postalAddress",
	"postalLocale",
    	"postalRegion",
        "postalCode",
    	"switchId",
    	"portInterface"};
	private static Logger logger = Logger.getLogger(AddressAPI.class);

	private static String[] attrTypes = new String[] {
	"String",
    	"String",
        "String",
    	"String",
	"String",
    	"String",
    	"String"};

    public AddressInfo[] query(SecurityToken token,
					    AddressInfo filter)
	throws AuthenticationAPIException , IllegalArgumentAPIException, AddressAPIException
   {

		if (token == null) throw new AuthenticationAPIException("Authen Failure");
		HashMap addrs = new HashMap();
		readAddrFromFile(ADDR_FILE,addrs);
		Object[] list = addrs.values().toArray();
		Vector newList = new Vector();
		logger.debug("Filter values " + filter.getPostalAddress() +
			" "  + filter.getPostalLocale() + " " + filter.getPostalRegion() + " " + filter.getPostalCode());
		for (int i = 0; i < list.length; i++) {
			AddressInfo info = (AddressInfo)list[i];
			if (filter.getId() != null && filter.getPostalAddress().length() != 0) {
				if (filter.getId().equals(info.getId())) {
					logger.info("Found Address by Port Identifier " + info.getId());
					newList.add(info);
					break;
				}
			}
			if (filter.getPostalAddress() != null && filter.getPostalAddress().length() != 0) {
				if (!filter.getPostalAddress().equals(info.getPostalAddress())) {
					//Dont match - continue with next object
					continue;
				}
			}
			if (filter.getPostalLocale() != null && filter.getPostalLocale().length() != 0) {
				if (!filter.getPostalLocale().equals(info.getPostalLocale())) {
					//Dont match - continue with next object
					continue;
				}
			}
			if (filter.getPostalCode() != null && filter.getPostalCode().length() != 0) {
				if (!filter.getPostalCode().equals(info.getPostalCode())) {
					//Dont match - continue with next object
					continue;
				}
			}
			if (filter.getPostalRegion() != null && filter.getPostalRegion().length() != 0) {
				if (!filter.getPostalRegion().equals(info.getPostalRegion())) {
					//Dont match - continue with next object
					continue;
				}
			}
			if (filter.getId() != null && filter.getId().length() != 0) {
				if (!filter.getId().equals(info.getId())) {
					//Dont match - continue with next object
					continue;
				}
			}

			logger.info("Found Address " + info.getId());
			newList.add(info);
		}
		return (AddressInfo[])newList.toArray(new AddressInfo[newList.size()]);

    }

	private void readAddrFromFile(String fileName,HashMap subs)
		throws AddressAPIException {
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
		logger.debug("Attrs are " + attrs);
		while (true) {
			AddressInfo info = new AddressInfo();
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
				logger.debug("Adding value " + value + " to property " + attrName + " in AddressInfo");
				PropertyDescriptor prop = new PropertyDescriptor(
						attrName,AddressInfo.class);
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
				throw new AddressAPIException("Unable to read file");
			}
			subs.put(info.getId(),info);

		}
		} catch (Exception ex) {
			logger.error("Unable to create objects of class AddressInfo",ex);
			throw new AddressAPIException("Unable to read file");
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

}

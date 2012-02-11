/**
 * Class: LeaseAPI
 *
 * Copyright (c) 2003 by Cisco Systems, Inc. All rights reserved.
 *
 * Author: djf
 *
 */

package com.cisco.sesm.sms.extensions;

import java.util.GregorianCalendar;
import java.util.Calendar;
import com.cisco.sesm.sms.exceptions.api.*;
import com.cisco.sesm.sms.types.*;
import com.cisco.cns.security.soap.common.*;
import org.apache.log4j.Logger;
import java.io.File;
import java.util.StringTokenizer;

/**
 * LeaseAPI
 *
 * @author Chandrika R
 */
public class LeaseAPI {

	/**
	 * Return IP Address lease information for a specified subscriber. The
	 * information returned will relate to the subscribers current lease.
	 *
	 * @param token the SMS security token previously retrieved
	 * @param subscriber information about the required subscriber
	 * @return details on the subscriber's current lease
	 * @throws AuthenticationException if the security token is invalid
	 * @throws LeaseException if a specific Lease interface error occurs.
	 * @throws MissingFieldsException should required fields be missing from the
	 * <code>SubscriberInfo</code> parameter
	 */
	private static Logger logger = Logger.getLogger(LeaseAPI.class);
	public LeaseInfo[] queryLeaseFromSubscriber(SecurityToken token, SubscriberInfo subscriber) throws AuthenticationAPIException,
		IllegalArgumentAPIException {
		logger.info("QueryLeaseFromSubscriber " + subscriber);
		if (token == null) {
			throw new AuthenticationAPIException("Authen Failure");
		}
		if (subscriber == null || subscriber.getId() == null) {
			throw new IllegalArgumentAPIException("Missing fields");
		}
		LeaseInfo[] lease = new LeaseInfo[4];
		for (int i = 0; i < 4; i++) {
			lease[i] = new LeaseInfo();
		}
		lease[0].setIpAddress(new String("10.10.10.10"));
		lease[1].setIpAddress(new String("10.10.10.11"));
		lease[2].setIpAddress(new String("10.10.10.12"));
		lease[3].setIpAddress(new String("10.10.10.13"));
		lease[0].setMacAddress(new String("EB 10 DF D3 1F 00"));
		lease[1].setMacAddress(new String("EB 10 DF D3 1F 01"));
		lease[2].setMacAddress(new String("EB 10 DF D3 1F 02"));
		lease[3].setMacAddress(new String("EB 10 DF D3 1F 03"));
		return lease;
	}

	/**
	 * Return IP Address lease information for a specified subscriber across a
	 * specified time span.
	 * <p>
	 * Multiple pieces of lease information can be returned if the time span
	 * specified it long enough.
	 *
	 * @param token the SMS security token previously retrieved
	 * @param subscriber information identifying a subscriber
	 * @param beginTime lease information starting from this time.
	 * @param endTime lease information ending at this time.
	 * @return an array of <code>LeaseInfo</code>
	 * @throws AuthenticationAPIException if the security token is invalid
	 * @throws LeaseAPIException if a specific Lease interface error occurs.
	 * @throws MissingFieldAPIException should required fields be missing from the
	 * <code>SubscriberInfo</code> parameter
	 */
	public LeaseInfo[] queryLeaseFromSubscriber(SecurityToken token, SubscriberInfo subscriber, Calendar beginTime, Calendar endTime) throws
		AuthenticationAPIException, IllegalArgumentAPIException {
		logger.info("QueryLeaseFromSubscriber(timed) " + subscriber);
		if (token == null) {
			throw new AuthenticationAPIException("Authen Failure");
		}
		if (subscriber == null || subscriber.getId() == null) {
			throw new IllegalArgumentAPIException("Missing fields");
		}
		LeaseInfo[] leases = new LeaseInfo[10];
		for (int i = 0; i < 10; i++) {
			LeaseInfo info = new LeaseInfo();
			info.setIpAddress("10.10.10.1" + i);
			info.setMacAddress("CD BC 40 E3 45 1" + i);
			Calendar cal = GregorianCalendar.getInstance();
			cal.set(Calendar.MONTH, i);
			info.setLeaseRenewalTime(cal);
			info.setExpiration(cal);
			info.setStartTimeOfState(cal);
			info.setState("Inactive");
			info.setClientHostName("cramanau-sb1");
			info.setClientId(subscriber.getId());
			String[] subs = new String[] {
				subscriber.getId()};
			leases[i] = info;
		}
		return leases;
	}

	/**
	 * Retrive a <code>LeaseInfo</code> object for a specified IP address
	 * within the specified time range.
	 *
	 * @param token the SMS security token previously retrieved
	 * @param ip the ip address you wish to retrieve a lease for.
	 * @param beginTime start time for your query
	 * @param endTime end time for your query
	 * @return an array of <code>LeaseInfo</code> objects representing the list
	 * of LeasesInfo's to which an IP address belonged over the specified time.
	 * @throws AuthenticationAPIException if the security token is invalid
	 * @throws LeaseAPIException if a specific Lease interface error occurs.
	 */
	public LeaseInfo[] queryLeaseFromIpAddress(SecurityToken token, String ip, Calendar beginTime, Calendar endTime) throws
		AuthenticationAPIException, IllegalArgumentAPIException {
		logger.info("queryLeaseFromIpAddress(timed) " + ip);
		if (token == null) {
			throw new AuthenticationAPIException("Authen Failure");
		}
		if (ip == null || ip.length() == 0) {
			throw new IllegalArgumentAPIException("Missing fields");
		}
		LeaseInfo[] leases = new LeaseInfo[10];
		for (int i = 0; i < 10; i++) {
			LeaseInfo info = new LeaseInfo();
			info.setIpAddress("10.10.10.1" + i);
			info.setMacAddress("CD BC 40 E3 45 1" + i);
			Calendar cal = GregorianCalendar.getInstance();
			cal.set(Calendar.MONTH, i);
			info.setLeaseRenewalTime(cal);
			info.setExpiration(cal);
			info.setStartTimeOfState(cal);
			info.setState("Inactive");
			info.setClientHostName("cramanau-sb1");
			leases[i] = info;
		}
		return leases;
	}

	/**
	 * Return information about the subscribers that have held the supplied
	 * leases. There is no way of mapping the returned Subscribers to the
	 * supplied Leases.
	 *
	 * @param token the SMS security token previously retrieved
	 * @param leases array of lease information you wish to find the subscribers
	 * for.
	 * @return an array of <code>SubscriberInfo</code> representing the list of
	 * subscribers who have held the supplied Leases
	 * @throws AuthenticationAPIException if the security token is invalid
	 * @throws LeaseAPIException if a specific Lease interface error occurs.
	 */
	public SubscriberInfo[] querySubscribersFromLeases(SecurityToken token, LeaseInfo[] leases) throws AuthenticationAPIException,
		IllegalArgumentAPIException {
		logger.info("querySubscribersFromLeases");
		if (token == null) {
			throw new AuthenticationAPIException("Authen Failure");
		}
		if (leases == null || leases.length == 0) {
			throw new IllegalArgumentAPIException("Missing fields");
		}
		SubscriberInfo[] recs = new SubscriberInfo[leases.length];
		for (int i = 0; i < leases.length; i++) {
			SubscriberInfo info = new SubscriberInfo();
			info.setId("Subs " + i);
			info.setDisplayName("Subscriber " + i);
			int j = i % 3;
			if (j == 0) {
				recs[i] = null;
			}
			else {
				recs[i] = info;
			}
		}
		return recs;
	}

	public java.lang.String archiveIPHistory(SecurityToken token, DHCPServerInfo server, Calendar startTime, Calendar endTime, String format,
											 String outFileName) throws AuthenticationAPIException, IllegalArgumentAPIException, LeaseAPIException {
		String outFile = "";
		logger.info("archiveIPHistory " + server + " " + startTime + " " + endTime);
		if (token == null) {
			throw new AuthenticationAPIException("Authen Failure");
		}
		if (server == null || startTime == null || endTime == null) {
			throw new IllegalArgumentAPIException("Illegal Arguments");
		}
		try {
			Runtime runtime = Runtime.getRuntime();
			if (outFileName.charAt(0) == '/') {
				outFile = "/tmp" + outFileName;
			}
			else {
				outFile = "/tmp/" + outFileName;
			}
			System.out.println("output file: " + outFile);
			File dirsFH = new File(getDir(outFile));
			dirsFH.mkdirs();
			File archFH = new File(outFile);
			archFH.createNewFile();
			System.out.println("File created successfully");
			String command = "cp leaseHistoryExport.csv " + outFile;
			logger.info("Executing command " + command);
			Process proc = runtime.exec(command);
			proc.waitFor();
		}
		catch (Exception ex) {
			logger.error(ex);
			throw new LeaseAPIException("Unable to copy command");
		}
		return outFileName;

	}

	public String getDir(String fileNamePath) {
		System.out.println("Get parent Directory: " + fileNamePath);
		StringTokenizer st = new StringTokenizer(fileNamePath, "/");
		int numOfTokens = st.countTokens() -1;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < numOfTokens; i++) {
			sb.append("/" + st.nextToken());
		}
		System.out.println("Parent Directory: " + sb.toString());
		return sb.toString();
	}
}
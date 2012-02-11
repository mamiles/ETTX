
package com.cisco.ettx.admin.collengine.sms;

import java.util.HashMap;
import java.util.Vector;
import java.util.Calendar;
import org.apache.log4j.Logger;
import com.cisco.ettx.admin.collengine.CollectionHolder;
import com.cisco.ettx.admin.collengine.DataCollectionException;
import com.cisco.ettx.admin.common.util.AttrNameConstants;
import com.cisco.ettx.admin.server.axis.sms.*;
import com.cisco.sesm.sms.types.*;

//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : LeaseService.java
// Desc : Class which performs SMS queries
//**************************************************


public class LeaseService extends AttrNameConstants implements SMSService {
	SMSTaskInfo taskInfo = null;
	CollectionHolder holder = null;
	private static Logger logger = Logger.getLogger(LeaseService.class);

	private static String GET_LEASE_HISTORY = "GetLeaseHistory";

	public LeaseService() {
	}

	public LeaseService(SMSTaskInfo ltaskInfo,
		CollectionHolder lholder) {
		taskInfo = ltaskInfo;
		holder = lholder;
	}

	public void performCollection(SMSTaskInfo ltaskInfo,
		CollectionHolder lholder) 
		throws DataCollectionException {
		taskInfo = ltaskInfo;
		holder = lholder;
		if (taskInfo.getTaskName().equals(GET_LEASE_HISTORY)) {
			getLeaseHistory();
			return;
		}
		logger.error("No such method supported in service " + taskInfo.getTaskName() + " for LeaseService");
		throw new DataCollectionException(DataCollectionException.UNABLE_TO_EXECUTE_TASK);
	}

	public void getLeaseHistory() throws DataCollectionException {
		try {
			String ipAddr = (String)taskInfo.getAttrMapValue(holder,IP_ADDR_ATTR_NAME);
			String subsId = (String)taskInfo.getAttrMapValue(holder,SUBS_ATTR_NAME);
			Calendar startTime = (Calendar)taskInfo.getAttrMapValue(holder,START_DATE_ATTR_NAME);
			Calendar endTime = (Calendar)taskInfo.getAttrMapValue(holder,END_DATE_ATTR_NAME);
			if (startTime == null) {
				//Can this be?
				//Not all information is available
				logger.error("Need start date to perform lease history query task " + taskInfo.getTaskName());
				throw new DataCollectionException(DataCollectionException.INSUFFICIENT_DATA_FOR_TASK);
			}
			if (endTime == null) {
				//Can this be?
				//Not all information is available
				logger.error("Need end date to perform lease history query task " + taskInfo.getTaskName());
				throw new DataCollectionException(DataCollectionException.INSUFFICIENT_DATA_FOR_TASK);
			}
			if (ipAddr == null || ipAddr.length() == 0) {
				if (subsId == null || subsId.length() == 0) {
					//Not all information is available
					logger.error("Need IP Address or Subscriber name to perform lease history query task " + taskInfo.getTaskName());
					throw new DataCollectionException(DataCollectionException.INSUFFICIENT_DATA_FOR_TASK);
				}
				queryLeasesForSubs(subsId,startTime,endTime);
			}
			else {
				queryLeasesForIPAddr(ipAddr,startTime,endTime);
			}
		}
		catch (DataCollectionException ex) {
			logger.error("Resending data collection exception",ex);
			throw ex;
		}
		catch (Exception ex) {
			//Unable to perform the query
			logger.error("Unable to execute lease query ",ex);
			throw new DataCollectionException(DataCollectionException.INSUFFICIENT_DATA_FOR_TASK);
		}
	}

	private void queryLeasesForSubs(String subsId,Calendar startTime,
		Calendar endTime) throws DataCollectionException {
		try {
			//perform query for subscriber
			SubscriberInfo filter = new SubscriberInfo();
			filter.setDisplayName(subsId);
			SubscriberInfo subs = SubscriberHelper.getSubscriberInfo(holder.getSessionID(),subsId);
			if (subs == null) {
				logger.error("Unable to match a single subscriber for subscriber name " + subsId);
				throw new DataCollectionException(DataCollectionException.NO_SUCH_RECORD,DataCollectionException.INFO);
			}
			LeaseInfo[] leases = LeaseHelper.queryLeasesForSubs(holder.getSessionID(),subs,startTime,endTime);
/*
			SubscriberInfo[] subs = SubscriberHelper.querySubscribers(holder.getSessionID(),filter);
			if (subs == null || subs.length != 1) {
				logger.error("Unable to match a single subscriber for subscriber name");
				throw new DataCollectionException(DataCollectionException.NO_SUCH_RECORD);
			}
			LeaseInfo[] leases = LeaseHelper.queryLeasesForSubs(holder.getSessionID(),subs[0],startTime,endTime);
*/
			if (leases == null || leases.length == 0) {
				logger.debug("No leases were found for subscriber " + subsId);
				throw new DataCollectionException(DataCollectionException.NO_SUCH_RECORD);
			}
			Vector records = new Vector();
			for (int i = 0; i < leases.length; i++) {
/*
				HashMap leaseRecord = createLeaseRecord(leases[i], subs[0]);
*/
				HashMap leaseRecord = createLeaseRecord(leases[i], subs);
				records.add(leaseRecord);
			}
			taskInfo.setAttrMapValue(holder,LEASE_INF_ATTR_NAME,records);
		}
		catch (DataCollectionException ex) {
			throw ex;
		}
		catch (SMSAPIException ex) {
			logger.error("Unable to execute lease query ",ex);
			throw new DataCollectionException(ex.getMessage());
		}
		catch (Exception ex) {
			logger.error("Unable to execute lease query ",ex);
			throw new DataCollectionException(DataCollectionException.INSUFFICIENT_DATA_FOR_TASK);
		}
	}

	private void queryLeasesForIPAddr(String ipAddr, Calendar startTime,
		Calendar endTime) throws DataCollectionException {
		try {
			//perform query for subscriber
			LeaseInfo[] leases = LeaseHelper.queryLeasesForIPAddr(holder.getSessionID(),ipAddr,startTime,endTime);
			if (leases.length == 0) {
				logger.debug("No leases were found for ip address" + ipAddr);
				throw new DataCollectionException(DataCollectionException.NO_SUCH_RECORD);
			}
			logger.debug("Number of leases received " + leases.length);
			//query subscriber info for lease
			SubscriberInfo[] subs = LeaseHelper.querySubsForLeases(holder.getSessionID(),leases);
			logger.debug("Number of subscribers received " + subs.length);
			if (subs == null || subs.length != leases.length) {
				logger.error("Unable to get subscriber information for leases " + subs);
				throw new DataCollectionException(DataCollectionException.NO_SUCH_RECORD);
			}
			//collate all the info
			Vector records = new Vector();
			for (int i = 0; i < leases.length; i++) {
				HashMap leaseRecord = createLeaseRecord(leases[i], subs[i]);
				records.add(leaseRecord);
			}
			taskInfo.setAttrMapValue(holder,LEASE_INF_ATTR_NAME,records);
		}
		catch (DataCollectionException ex) {
			throw ex;
		}
		catch (SMSAPIException ex) {
			logger.error("Unable to execute lease query ",ex);
			throw new DataCollectionException(ex.getMessage());
		}
		catch (Exception ex) {
				logger.error("Unable to execute lease query ",ex);
				throw new DataCollectionException(DataCollectionException.INSUFFICIENT_DATA_FOR_TASK);
		}
	}

	private HashMap createLeaseRecord(LeaseInfo lease,SubscriberInfo subs) {
		HashMap record = new HashMap();
		record.put(LEASE_START_DATE,lease.getStartTimeOfState());
		record.put(LEASE_END_DATE,lease.getExpiration());
		record.put(IP_ADDRESS,lease.getIpAddress());
		record.put(MAC_ADDRESS,lease.getMacAddress());
		record.put(LEASE_STATUS,lease.getState());
		if (subs != null) {
			record.put(SUBSCRIBER_NAME,subs.getDisplayName());
			record.put(LOGIN_ID,subs.getSubscriberId());
		}
		return record;
	}

	public void getLeaseForSubscriber() throws DataCollectionException {
		try {
			SubscriberInfo info = (SubscriberInfo)taskInfo.getAttrMapValue(holder,SUBS_OBJECT);
			if (info == null) {
				logger.error("Unable to execute lease query for subscriber. No Subscriber object present");
				throw new DataCollectionException(DataCollectionException.INSUFFICIENT_DATA_FOR_TASK);
			}
			LeaseInfo[] leases = LeaseHelper.getLeaseForSubscriber(holder.getSessionID(),info);
			if (leases == null) {
				logger.error("Unable to get leases for account " + info.getAccountId());
				throw new DataCollectionException(DataCollectionException.UNABLE_TO_EXECUTE_TASK);
			}
			Vector ipAddr = new Vector();
			Vector macAddr = new Vector();
			for (int i = 0; i < leases.length; i++) {
				ipAddr.add(leases[i].getIpAddress());
				macAddr.add(leases[i].getMacAddress());
			}
			taskInfo.setAttrMapValue(holder,SUBS_IP_ADDRESS,ipAddr);
			taskInfo.setAttrMapValue(holder,SUBS_MAC_ADDRESS,macAddr);
		}
		catch (SMSAPIException ex) {
			logger.error("Unable to execute lease query ",ex);
			throw new DataCollectionException(ex.getMessage());
		}
		catch (Exception ex) {
			logger.error("Unable to perform task getLeaseForSubscriber for account ",ex);
			throw new DataCollectionException(DataCollectionException.UNABLE_TO_EXECUTE_TASK);
		}
	}
}

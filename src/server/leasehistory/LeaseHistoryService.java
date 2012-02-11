//**************************************************
 // Copyright (c) 2001, 2002 Cisco Systems, Inc.
 // All rights reserved.
 //**************************************************
  // Author: Marvin Miles

  package com.cisco.ettx.admin.server.leasehistory;

import org.apache.log4j.Logger;
import com.cisco.ettx.admin.common.SMSComponent;
import com.cisco.ettx.admin.common.ComponentHostData;
import com.cisco.ettx.admin.config.ComponentConfigManager;
import com.cisco.ettx.admin.config.ConfigurationException;
import com.cisco.ettx.admin.config.SystemConfigManager;
import com.cisco.ettx.admin.common.SystemConfiguration;
import com.cisco.ettx.admin.common.AdminServicesException;
import com.cisco.ettx.admin.config.ChangeNotification;
import com.cisco.ettx.admin.server.logdisplay.FtpLog;
import com.cisco.ettx.admin.server.logdisplay.SshLog;
import com.cisco.ettx.admin.server.logdisplay.LogDisplayException;
import com.cisco.ettx.admin.server.axis.sms.LeaseHelper;
import com.cisco.ettx.admin.server.axis.spe.AuthenticateHelper;
import com.cisco.ettx.admin.server.axis.spe.AuthenticationException;
import com.cisco.ettx.admin.server.axis.sms.SMSAPIException;
import com.cisco.ettx.admin.common.util.XMLUtil;
import java.net.InetAddress;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.io.*;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.log4j.Logger;
import org.w3c.dom.*;

public class LeaseHistoryService
	implements ChangeNotification.NotifyTarget {

	private static LeaseHistoryService instance = null;
	private static Logger logger = Logger.getLogger("LeaseHistoryService");
	private final static String CNR_COMPONENT = "CNR";
	private final static String MANUAL_OUTPUTFILE = "/tmp/manual-iphistoryOutput";
	private final static String AUTO_OUTPUTFILE = "/tmp/auto-iphistoryOutput";
	private final static String LEASEHISTORYXML = "/config/leasehistory.xml";
	private final static String LEASEHISTORY = "LEASEHISTORY";
	private final static String DEFAULTS = "DEFAULTS";
	private final static String IPFORMAT = "IPFORMAT";
	private final static String LOCALHOST = "localhost";
	private String ETTXROOT = "";
	private ScheduleLeaseHistoryArchive schedule = null;
	private String ipFormat;

	public static LeaseHistoryService getInstance() {
		return instance;
	}

	public LeaseHistoryService(String ettxRoot) throws LeaseHistoryException {
		if (instance != null) {
			//singleton object
			logger.error("Creating more than one instance");
			throw new LeaseHistoryException(LeaseHistoryException.SINGLETON_VIOLATION);
		}
		ETTXROOT = ettxRoot;
		instance = this;

		String leaseHistoryXmlFile = ettxRoot + LEASEHISTORYXML;
		try {
			logger.debug("Reading " + leaseHistoryXmlFile);
			Document document = XMLUtil.loadXmlFile(leaseHistoryXmlFile);
			NodeList nodelist = document.getElementsByTagName(LEASEHISTORY);
			logger.debug("Received LeaseHistory Elements " + nodelist.getLength());
			Node leasehist = nodelist.item(0);
			Vector defaults = XMLUtil.findChildren(leasehist, DEFAULTS);
			logger.debug("Loaded " + defaults.size() + " defaults from file");
			Enumeration iter = defaults.elements();
			while (iter.hasMoreElements()) {
				Node def = (Node) iter.nextElement();
				setIpFormat(XMLUtil.getChildValue(def, IPFORMAT));
				logger.debug("ipFormat: " + getIpFormat());
			}
		}
		catch (Exception ex) {
			logger.error("Error Reading XML File " + leaseHistoryXmlFile + " Exception " + ex.toString(), ex);
			throw new LeaseHistoryException(LeaseHistoryException.ERROR_READING_XML_FILE);
		}

		SystemConfigManager sysMgr = SystemConfigManager.getInstance();
		sysMgr.addTarget(this);
		SystemConfiguration sysconfig = sysMgr.getSystemConfiguration();
		if (sysconfig.getLeaseHistorySchOn()) {
			schedule = new ScheduleLeaseHistoryArchive();
		}
		logger.info("LeaseHistoryService initialized successfully...");
	}

	public void configChanged(Object from, Object to) {
		if (to instanceof SystemConfiguration) {
			logger.info("Changed System configuration. Updating schedule to Lease History Archive");
			if (schedule != null) {
				schedule.cancleCurrentSchedule();
			}
			SystemConfigManager sysMgr = SystemConfigManager.getInstance();
			SystemConfiguration sysconfig = sysMgr.getSystemConfiguration();
			if (sysconfig.getLeaseHistorySchOn()) {
				schedule = new ScheduleLeaseHistoryArchive();
			}
		}
	}

	public synchronized String runLeaseHistoryArchive(String sessionID, String cnrHost, Date startDate, Date endDate, String archiveFile) throws
		LeaseHistoryException {

		logger.info("Runing Lease History Archive - Host: " + cnrHost + " Start: " + startDate + " End: " + endDate + " Arhive File: " + archiveFile);
		ComponentHostData host = null;
		FtpLog ftp = null;
		SshLog ssh = null;

		File archFH = new File(archiveFile);
		try {
			if (archFH.createNewFile()) {
				logger.debug("Successfully able to create lease history archive file");
				archFH.delete();
			}
			else {
				logger.error("Lease History File Name already exists: " + archiveFile);
				throw new LeaseHistoryException(LeaseHistoryException.FILE_NAME_EXISTS);
			}
		}
		catch (IOException ex1) {
			logger.error("I/O Error trying to create Lease History Archive file: " + archiveFile);
			logger.error(ex1);
			throw new LeaseHistoryException(LeaseHistoryException.INVALID_FILE_NAME);
		}

		ComponentConfigManager mgr = ComponentConfigManager.getInstance();
		try {
			SMSComponent comp = mgr.getComponent(CNR_COMPONENT);
			host = comp.getHost(cnrHost);
		}
		catch (ConfigurationException ce) {
			logger.error(ce);
			throw new LeaseHistoryException(LeaseHistoryException.COMPONENT_READ_ERROR);
		}
		try {
			String cnrIpAddr = getIPAddress(host.getHostName());
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(startDate);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endDate);
			String fileName = LeaseHelper.archiveIPHistory(sessionID, cnrIpAddr, startCal, endCal, getIpFormat(), MANUAL_OUTPUTFILE);
		}
		catch (SMSAPIException ex) {
			logger.error(ex);
			throw new LeaseHistoryException(LeaseHistoryException.LEASE_HISTORY_EXTRACT_ERROR);
		}
		try {
			if (cnrHost.equalsIgnoreCase("localhost")) {
				copyFile("/tmp" + MANUAL_OUTPUTFILE, archiveFile);
			}
			else {
				if (host.getUseSecureShell()) {
					ssh = new SshLog(ETTXROOT, host.getHostName(), host.getUnixLoginID(), host.getUnixPassword(), host.getUnixPrompt());
					ssh.getRemoteFileAndDelete("/tmp" + MANUAL_OUTPUTFILE, archiveFile);
				}
				else {
					ftp = new FtpLog(host.getHostName(), host.getUnixLoginID(), host.getUnixPassword());
					ftp.getRemoteFileAndDelete("/tmp" + MANUAL_OUTPUTFILE, archiveFile);
				}
			}
		}
		catch (LogDisplayException ex) {
			logger.error("Error with SFTP or FTP of the Lease History file");
			logger.error(ex);
			throw new LeaseHistoryException(ex.getMessage());
		}
		return "success";
	}

	public void copyFile(String fromFile, String toFile) throws LeaseHistoryException {
		try {
			logger.debug("Coping lease history file from: " + fromFile + " to: " + toFile);
			FileInputStream fis = new FileInputStream(fromFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			FileOutputStream fos = new FileOutputStream(toFile);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			int b;
			while ( (b = bis.read()) != -1) {
				bos.write(b);
			}
			bis.close();
			bos.close();
		}
		catch (IOException e) {
			logger.error("Error with coping lease history file from: " + fromFile + " to: " + toFile);
			logger.error(e);
			throw new LeaseHistoryException(e.getMessage());
		}
	}

	public synchronized void runAutoArchive() throws LeaseHistoryException {

		ComponentHostData host = null;
		FtpLog ftp = null;
		SshLog ssh = null;
		Vector componentHostData;
		logger.info("Running Scheduled Lease History Archive");

		// Find out the first and last dates of last month
		Calendar startCal = Calendar.getInstance();
		logger.debug("Current Date: " + startCal.getTime());
		startCal.set(Calendar.MONTH, startCal.get(Calendar.MONTH) - 1);
		startCal.set(Calendar.DAY_OF_MONTH, startCal.getActualMinimum(Calendar.DAY_OF_MONTH));
		startCal.set(Calendar.HOUR_OF_DAY, 0);
		startCal.set(Calendar.MINUTE, 0);
		startCal.set(Calendar.SECOND, 0);
		Date startDate = startCal.getTime();

		Calendar endCal = Calendar.getInstance();
		endCal.set(Calendar.MONTH, endCal.get(Calendar.MONTH) - 1);
		endCal.set(Calendar.DAY_OF_MONTH, endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
		endCal.set(Calendar.HOUR_OF_DAY, 23);
		endCal.set(Calendar.MINUTE, 59);
		endCal.set(Calendar.SECOND, 59);
		Date endDate = endCal.getTime();

		logger.debug("Beginning Date: " + startDate);
		logger.debug("Ending Date: " + endDate);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		SystemConfigManager sysMgr = SystemConfigManager.getInstance();
		SystemConfiguration sysconfig = sysMgr.getSystemConfiguration();

		ComponentConfigManager mgr = ComponentConfigManager.getInstance();
		try {

			//Login to SPE
			String sessionID = AuthenticateHelper.login(sysconfig.getSpeUserID(), sysconfig.getSpePassword());
			SMSComponent comp = mgr.getComponent(CNR_COMPONENT);
			componentHostData = comp.getComponentHostData();
			for (int j = 0; j < componentHostData.size(); j++) {
				host = (ComponentHostData) componentHostData.elementAt(j);
				String archiveFile = sysconfig.getLeaseHistoryExportDir() + "/IPHIST-" + host.getHostName() + "-" + formatter.format(startCal.getTime()) +
					"-T0-" + formatter.format(endCal.getTime());
				logger.info("Runing Lease History Archive - Host: " + host.getHostName() + " Start: " + startDate + " End: " + endDate + " Arhive File: " + archiveFile);
				File archFH = new File(archiveFile);
				try {
					if (archFH.createNewFile()) {
						logger.debug("Successfully able to create lease history archive file");
						archFH.delete();
					}
					else {
						logger.error("Lease History File Name already exists: " + archiveFile);
						throw new LeaseHistoryException(LeaseHistoryException.FILE_NAME_EXISTS);
					}
				}
				catch (IOException ex1) {
					logger.error("I/O Error trying to create Lease History Archive file: " + archiveFile);
					logger.error(ex1);
					throw new LeaseHistoryException(LeaseHistoryException.INVALID_FILE_NAME);
				}

				try {
					String cnrIpAddr = getIPAddress(host.getHostName());
					String fileName = LeaseHelper.archiveIPHistory(sessionID, cnrIpAddr, startCal, endCal, getIpFormat(), AUTO_OUTPUTFILE);
				}
				catch (SMSAPIException ex) {
					logger.error(ex);
					throw new LeaseHistoryException(LeaseHistoryException.LEASE_HISTORY_EXTRACT_ERROR);
				}
				try {
					if (host.getHostName().equalsIgnoreCase("localhost")) {
						copyFile("/tmp" + AUTO_OUTPUTFILE, archiveFile);
					}
					else {
						if (host.getUseSecureShell()) {
							ssh = new SshLog(ETTXROOT, host.getHostName(), host.getUnixLoginID(), host.getUnixPassword(), host.getUnixPrompt());
							ssh.getRemoteFileAndDelete("/tmp" + AUTO_OUTPUTFILE, archiveFile);
						}
						else {
							ftp = new FtpLog(host.getHostName(), host.getUnixLoginID(), host.getUnixPassword());
							ftp.getRemoteFileAndDelete("/tmp" + AUTO_OUTPUTFILE, archiveFile);
						}
					}
				}
				catch (LogDisplayException ex) {
					logger.error("Error with SFTP or FTP of the Lease History file");
					logger.error(ex);
					throw new LeaseHistoryException(ex.getMessage());
				}

				logger.info("Created IP History Archive File: " + archiveFile);
			}
			if (sysconfig.getLeaseHistorySchOn()) {
				schedule = new ScheduleLeaseHistoryArchive();
			}
		}
		catch (ConfigurationException ce) {
			logger.error(ce);
			throw new LeaseHistoryException(LeaseHistoryException.COMPONENT_READ_ERROR);
		}
		catch (AuthenticationException ex) {
			logger.error(ex);
			throw new LeaseHistoryException(ex.getMessage());
		}

	}

	private String getIPAddress(String hostName) throws LeaseHistoryException {
		try {
			if (hostName.equals(LOCALHOST)) {
				InetAddress addr = InetAddress.getLocalHost();
				return addr.getHostAddress();
			}
			InetAddress addr = InetAddress.getByName(hostName);
			return addr.getHostAddress();
		}
		catch (Exception ex) {
			logger.error("Unable to map host name " + hostName + " to IP Address ");
			throw new LeaseHistoryException(LeaseHistoryException.LEASE_HISTORY_EXTRACT_ERROR);
		}
	}

	public void setIpFormat(String ipFormat) {
		this.ipFormat = ipFormat;
	}

	public String getIpFormat() {
		return ipFormat;
	}

}
package com.cisco.ettx.admin.auditlog;

//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : FilePurgeAppender.java
// Desc : Class which purges the file directory based on the event purge time.
//**************************************************
import org.apache.log4j.*;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.helpers.LogLog;
import com.cisco.ettx.admin.common.SystemConfiguration;
import com.cisco.ettx.admin.config.SystemConfigManager;
import com.cisco.ettx.admin.config.ChangeNotification;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Date;
import java.io.File;
import java.io.FilenameFilter;

public class FilePurgeAppender extends DailyRollingFileAppender implements ChangeNotification.NotifyTarget, FilenameFilter {

	private int eventPurgeDur = 0;
	private Calendar nextPurgeTime = null;
	private String fileName = null;
	private String dirName = null;
	private static FilePurgeAppender instance = null;

	public static FilePurgeAppender getInstance() {
		return instance;
	}
	
	public FilePurgeAppender() throws Exception {
		super();
		if (instance != null) {
			throw new Exception("More than one instance of FilePurgeAppender created");
		}
		instance = this;
	}

	public void setFileName(String name) {
		LogLog.debug("Setting Filename in FilePurgeAppender " + name);
		fileName = name;
	}

	public void init() {
		//Get information from systemConfiguration
		SystemConfigManager mgr = SystemConfigManager.getInstance();
		mgr.addTarget(this);

		SystemConfiguration config = mgr.getSystemConfiguration();
		initFromSystemConfig(config);
	}

	private void initFromSystemConfig(SystemConfiguration config)  {
		LogLog.debug("Initializng from systemconfig");
		eventPurgeDur = config.getPurgeDur();
		nextPurgeTime = new GregorianCalendar(); //Set to current time to do purging initially
		dirName  = config.getExportDir();
		File f = new File(dirName,fileName);
		LogLog.debug("Seting File to " + f.getPath() + " and purge duration to " + eventPurgeDur);
		setFile(f.getPath());
		activateOptions();
	}

	public void configChanged(Object from,Object to) {
		if (to instanceof SystemConfiguration) {
			LogLog.debug("Changed System configuration. Updating event purge log");
			initFromSystemConfig((SystemConfiguration)to);
			SystemConfiguration oldConfig = (SystemConfiguration)from;
			SystemConfiguration newConfig = (SystemConfiguration)to;
			if (!oldConfig.getExportDir().equals(newConfig.getExportDir())) {
				LogLog.debug("Moving files from " + oldConfig.getExportDir() + " to " + newConfig.getExportDir());
				File oldDir = new File(oldConfig.getExportDir(),fileName);
				File newDir = new File(newConfig.getExportDir(),fileName);
				File[] files = oldDir.listFiles(this);
				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					file.renameTo(newDir);
				}
			}
		}
	}

	protected synchronized void subAppend(LoggingEvent event) {
		super.subAppend(event);
		LogLog.debug("Logging event");

		//Remove all files
		Calendar d = new GregorianCalendar(); //Current time
		if (d.after(nextPurgeTime)) { //need to purge the directory
			purgeLogFiles();
			nextPurgeTime.add(Calendar.DATE,eventPurgeDur);
			LogLog.debug("Next event purging will be done at " + nextPurgeTime.getTime().toString());
		}
		return ;
	}

	private synchronized void purgeLogFiles() {
		try {
			Calendar purgeLine = (Calendar)nextPurgeTime.clone();
			purgeLine.add(Calendar.DATE, 0-eventPurgeDur);
			File dirF = new File(dirName);
			File[] files = dirF.listFiles(this);
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				long t = file.lastModified();
				Calendar modifiedTime = new GregorianCalendar();
				modifiedTime.setTimeInMillis(t);
				if (purgeLine.after(modifiedTime)) {
					//Last modified time is worse than current time
					LogLog.debug("Removing file " + file.getPath());
					if (!file.delete()) {
						LogLog.warn("Unable to purge file " + file.getPath());
					}
				}
			}
		}
		catch (Exception ex) {
			LogLog.error("Unable to purge files ",ex);
		}
	}
	

	public boolean accept(File path, String lfileName) {
		if (lfileName.startsWith(fileName)) {
			return true;
		}
		return false;
	}
}

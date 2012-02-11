//**************************************************
 // Copyright (c) 2001, 2002 Cisco Systems, Inc.
 // All rights reserved.
 //**************************************************
  // Author: Marvin Miles

  package com.cisco.ettx.admin.server.logdisplay;

  import org.apache.log4j.Logger;
import com.cisco.ettx.admin.common.SMSComponent;
import com.cisco.ettx.admin.common.ComponentHostData;
import com.cisco.ettx.admin.config.ComponentConfigManager;
import com.cisco.ettx.admin.config.ConfigurationException;
import com.cisco.ettx.admin.config.SystemConfigManager;
import com.cisco.ettx.admin.common.SystemConfiguration;
import com.cisco.ettx.admin.server.logdisplay.FtpLog;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.io.*;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class SchedulePurgeServerCache {

	private static Logger logger = Logger.getLogger("SchedulePurgeServerCache");
	private Timer purgeServerTimer;

	public SchedulePurgeServerCache() {

		Date schedDate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, +1);
		schedDate = cal.getTime();
		SimpleDateFormat debugFormat = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss zzz");
		logger.info("Scheduling Purge Server Log Cache for " + debugFormat.format(schedDate));
		purgeServerTimer = new Timer();
		purgeServerTimer.schedule(new PurgeServerCacheTask(), schedDate);
	}

	public void cancleCurrentSchedule() {
		purgeServerTimer.cancel();
	}

	class PurgeServerCacheTask
		extends TimerTask {

		public void run() {

			try {
				PurgeServerCache psc = PurgeServerCache.getInstance();
				psc.runPurgeServerCache();
				purgeServerTimer.cancel();
			}
			catch (Exception ex) {
				logger.error("Error running Purge Server Log Cache Scheduled Process");
				logger.error(ex);
			}
		}
	}

}
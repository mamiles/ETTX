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

public class ScheduleLeaseHistoryArchive {

	private static Logger logger = Logger.getLogger("ScheduleLeaseHistoryArchive");
	private Timer leaseHistoryTimer;
	
	public ScheduleLeaseHistoryArchive() {
		
		SystemConfigManager sysMgr = SystemConfigManager.getInstance();
		SystemConfiguration sysconfig = sysMgr.getSystemConfiguration();
		StringTokenizer st = new StringTokenizer(sysconfig.getLeaseHistoryExportTime(), ":");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, sysconfig.getLeaseHistoryExportDay());
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(st.nextToken()));
		cal.set(Calendar.MINUTE, Integer.parseInt(st.nextToken()));
		cal.set(Calendar.SECOND, Integer.parseInt(st.nextToken()));
		Date schedDate = cal.getTime();
		Date currentDate = new Date();
		if ( schedDate.before(currentDate) ) {
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
			schedDate = cal.getTime();
		}
		SimpleDateFormat debugFormat = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss zzz");
		logger.info("Scheduling Lease History Archive for " + debugFormat.format(schedDate));
        	leaseHistoryTimer = new Timer();
        	leaseHistoryTimer.schedule(new LeaseHistoryTask(), schedDate);
    	}
    	
    	public void cancleCurrentSchedule() {
    		leaseHistoryTimer.cancel();
    	}
    	
    	class LeaseHistoryTask extends TimerTask {
        	
        	public void run() {
        	
        		try {
        			LeaseHistoryService lhs = LeaseHistoryService.getInstance();
            			lhs.runAutoArchive();
            			leaseHistoryTimer.cancel();
            		}
            		catch (LeaseHistoryException ex) {
            			logger.error("Error running Lease History Archive Scheduled Process");
				logger.error(ex);
			}
        	}
    	}
    	
}
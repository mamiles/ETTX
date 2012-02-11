

package com.cisco.ettx.admin.collengine.sms;

import com.cisco.ettx.admin.collengine.*;
import java.util.Hashtable;
import java.util.Vector;
import java.io.File;
import org.apache.log4j.Logger;

//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : SMSDataCollector.java
// Desc : Interface class to perform data collector
//**************************************************

public class SMSDataCollector extends DataCollector {
	Hashtable tasks = new Hashtable();
	File configDir;
	private static String CONFIG_DIR = "config/sms";

	private static Logger logger = Logger.getLogger("SMSDataCollector");

	public SMSDataCollector(String rootDir) throws DataCollectionException {
		super();

		//Read the tasks from the list
		configDir = new File(rootDir,CONFIG_DIR);
		if (configDir == null) {
			logger.error("Unable to get to config directory " + rootDir + "/" + CONFIG_DIR);
			throw new DataCollectionException(DataCollectionException.MISSING_CONFIG);
		}
		File[] files = configDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = (File)files[i];
			SMSTaskInfo info = SMSTaskInfo.readTaskConfig(f.getPath());
			logger.debug("Adding task to list " + info.getTaskName());
			tasks.put(info.getTaskName(),info);
		}
	}
		
	public void performCollection(String subtaskName,
		CollectionHolder holder) 
		throws DataCollectionException {

		SMSTaskInfo taskInfo = (SMSTaskInfo)tasks.get(subtaskName);
		if (taskInfo == null) {
			logger.error("Unable to get task details for task " + subtaskName);
			throw new DataCollectionException(DataCollectionException.UNKNOWN_TASK);
		}
		SMSService service = mapToService(taskInfo);
		logger.debug("Performing task " + subtaskName);
		service.performCollection(taskInfo,holder);
	}

	private SMSService mapToService(SMSTaskInfo taskInfo) throws DataCollectionException {
		String serviceName = taskInfo.getServiceName();
		try {
			Class cls = Class.forName(serviceName);
			SMSService service =(SMSService)cls.newInstance();
			return service;
		}
		catch (Exception ex) {
			logger.error("Unable to map service " + serviceName + " to a service",ex);
			throw new DataCollectionException(DataCollectionException.NO_SUCH_SERVICE);
		}
	}

}

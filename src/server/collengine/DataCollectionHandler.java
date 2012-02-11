
package com.cisco.ettx.admin.collengine;

//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : DataCollectionHandler.java
// Desc : Interface class to perform data collector
//**************************************************
import java.util.*;
import java.io.File;
import org.apache.log4j.Logger;
import java.io.File;
import com.cisco.ettx.admin.common.util.XMLUtil;
import com.cisco.ettx.admin.common.AttrResult;
import org.w3c.dom.*; 

public class DataCollectionHandler {
	Hashtable tasks = new Hashtable();
	File configDir;

	private static DataCollectionHandler instance = null;
	private static Logger logger = Logger.getLogger(DataCollectionHandler.class);
	private static String CONFIG_DIR = "config/task";

	public static DataCollectionHandler getInstance() {
		return instance;

	}

	public DataCollectionHandler(String rootDir) throws DataCollectionException {
		instance = this;
		//Read the tasks from the list
		configDir = new File(rootDir,CONFIG_DIR);
		if (configDir == null) {
			logger.error("Unable to get to config directory " + rootDir + "/" + CONFIG_DIR);
			throw new DataCollectionException(DataCollectionException.MISSING_CONFIG);
		}
		File[] files = configDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = (File)files[i];
			TaskInfo info = TaskInfo.readTaskConfig(f.getPath());
			logger.debug("Adding task to list " + info.taskName);
			tasks.put(info.taskName,info);
		}
	}
		
	public HashMap performCollection(String sessionID, String taskName,HashMap attrs , int numRecords) 
		throws DataCollectionException {
		TaskInfo detail = (TaskInfo)tasks.get(taskName);
		if (detail == null) {
			logger.error("Unable to get task details for task " + taskName);
			throw new DataCollectionException(DataCollectionException.UNKNOWN_TASK);
		}
		//Start the task information
		TaskExecHandler handler = new TaskExecHandler(detail,sessionID);
		HashMap outputAttrs = handler.executeTask(attrs);
		//If the task has been executed, refer to the output value
		Vector outputAttrsInfo = detail.getOutputAttrsInfo();
		if (outputAttrsInfo == null) {
			//Send all details
			logger.debug("Sending output " + outputAttrs + " for task " + taskName);
			return outputAttrs;
		}
		//logger.debug("Collecting output " + outputAttrsInfo + " for task " + taskName);
		//Create a list of all the out that needs to be sent
		HashMap tobeSent = new HashMap();
		Enumeration iter = outputAttrsInfo.elements();
		while (iter.hasMoreElements()) {
			TaskInfo.AttrDefinition def = (TaskInfo.AttrDefinition)iter.nextElement();
			String name = def.getAttrName();
			Object value = outputAttrs.get(name);
			if (name == null) {
				logger.error("Unable to get the attribute name for the list");
				throw new DataCollectionException(DataCollectionException.UNABLE_TO_EXECUTE_TASK);
			}
			if (def.isCollection()) {
				Collection coll = (Collection)value;
				OutputManager instance =  OutputManager.getInstance();
				String token = instance.createIterator(coll);
				AttrResult result = new AttrResult(token);
				result.setOutput(instance.iterateRecords(token,numRecords));
				result.setPending(instance.getPendingCount(token));
				tobeSent.put(name,result);
			}
			else {
				logger.debug("Adding value " + value + " for name " + name);
				tobeSent.put(name,value);
			}
		}
		return tobeSent;
	}
}

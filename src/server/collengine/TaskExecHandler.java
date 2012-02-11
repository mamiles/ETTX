
package com.cisco.ettx.admin.collengine;

//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : TaskExecHandler.java
// Desc : Maintains information about the task
//**************************************************

import java.util.Vector;
import java.util.HashMap;
import java.util.Enumeration;
import org.apache.log4j.Logger;

public class TaskExecHandler {
	TaskInfo task;
	CollectionHolder dataHolder = null;

	private static Logger logger = Logger.getLogger(TaskExecHandler.class);
	public TaskExecHandler(TaskInfo ltask,String sessionID) {
		task = ltask;
		dataHolder = new CollectionHolder(sessionID);
	}


	public CollectionHolder getDataObject() {
		return dataHolder;
	}

	public HashMap executeTask(HashMap inputAttrs) 
		throws DataCollectionException {
		logger.debug("Executing task " + task.taskName + " with Attributes " + inputAttrs);
		dataHolder.setSharedAttrs(inputAttrs); //REVISIT
		dataHolder.setOutputAttrs(new HashMap());
		TaskInfo.Collection coll = null;
		try {
			TaskInfo.Collection[] collList = task.getCollectionList();

			for (int i = 0; i < collList.length; i++) {
				coll = collList[i];
				logger.debug("Executing component task " + coll.getSubTaskName());
				coll.getCollector().performCollection(
					coll.getSubTaskName(),
					dataHolder);
			}
		}
		catch (DataCollectionException ex) {
			logger.error("Errored in Task " + task.getTaskName() +
					" in Collection " + coll);
			throw ex;
		}
		catch (Exception ex) {
			logger.error("Unknown exception in Task " + task.getTaskName() +
				" in Collection " + coll,ex);
			throw new DataCollectionException(DataCollectionException.UNKNOWN_ERROR);
		}
		return dataHolder.getOutputAttrs();
	}
}

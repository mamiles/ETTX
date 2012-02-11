
package com.cisco.ettx.admin.collengine;

//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : DataCollector.java
// Desc : Interface class to perform data collector
//**************************************************
import java.util.Hashtable;
import org.apache.log4j.Logger;

public abstract class DataCollector {
	static Hashtable collectors = new Hashtable();
	private static Logger myLogger =  Logger.getLogger("DataCollector");

	public abstract void performCollection(String subtaskName,
		CollectionHolder holderObject) 
		throws DataCollectionException;

	public DataCollector() {
		addInstance(this);
	}

	public static synchronized void addInstance(DataCollector coll) {
		myLogger.debug("Addng instance " + coll.getClass().getName());
		collectors.put(coll.getClass().getName(),coll);
	}

	public static DataCollector getInstance(String className) {
		myLogger.debug("Getting instance " + className);
		return (DataCollector)collectors.get(className);
	}
}

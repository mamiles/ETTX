package com.cisco.ettx.admin.collengine;

//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : OutputManager.java
// Desc : Interface class to perform iteration of data collection
//**************************************************
import java.util.Hashtable;
import java.util.Vector;
import java.util.Collection;
import java.util.Iterator;
import java.util.Date;
import java.io.File;
import org.apache.log4j.Logger;
import java.io.File;
import com.cisco.ettx.admin.common.util.XMLUtil;
import org.w3c.dom.*; 

public class OutputManager {

	private Hashtable iterators = new Hashtable();
	private int nextToken = 1;
	private static Logger logger = Logger.getLogger(OutputManager.class);
	private static OutputManager instance = null;

	public static OutputManager getInstance() {
		if (instance == null) {
			instance = new OutputManager();
		}
		return instance;
	}

//REVISIT to purge iterators
	private OutputManager() {
	}

	public synchronized String createIterator(Collection lrecords) {
		String token = "DataIterator" + nextToken;
		iterators.put(token,new DataIterator(token,lrecords));
		return token;
	}

	public Vector iterateRecords(String token,int numRecords) 
		throws DataCollectionException {
		DataIterator iter = (DataIterator)iterators.get(token);
		if (iter == null) {
			throw new DataCollectionException(DataCollectionException.NO_SUCH_RECORD);
		}
		Vector recs = iter.getRecords(numRecords);
		if (iter.isEmpty()) {
			//all records complete
			synchronized(iterators) {
				iterators.remove(token);
			}
		}
		return recs;
	}

	public int getPendingCount(String token) {
		DataIterator iter = (DataIterator)iterators.get(token);
		if (iter == null) {
			return 0;
		}
		return iter.getPendingCount();
	}

	private class DataIterator {
		private String token = null;
		private Iterator iter = null;
		private int pending = 0;
		private Date lastAccessTime = new Date();

		private DataIterator(String ltoken, Collection lrecords) {
			token = ltoken;
			iter = lrecords.iterator();
			pending = lrecords.size();
		}

		public Vector getRecords(int numRecords) throws DataCollectionException {
			lastAccessTime = new Date(); //Reset access time
			Vector temp = new Vector();
			int i = 0; 
			if (iter == null) {
				//reached the end of list
				throw new DataCollectionException(DataCollectionException.END_OF_RECORDS);
			}
			while (iter.hasNext() && i < numRecords) {
				temp.add(iter.next());
				pending--;
			}
			if (!iter.hasNext()) {
				//Finished all records
				iter = null;
			}
			return temp;
		}
	
		public boolean isEmpty() {
			if (iter != null) return false;
			return true;
		}

		public int getPendingCount() {
			return pending;
		}
	}

}

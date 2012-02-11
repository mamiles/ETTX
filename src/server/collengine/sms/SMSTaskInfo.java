
package com.cisco.ettx.admin.collengine.sms;

//import com.cisco.insmbu.sms.*;
import java.util.HashMap;
import java.util.Vector;
import java.util.Enumeration;
import com.cisco.ettx.admin.collengine.CollectionHolder;
import com.cisco.ettx.admin.collengine.AttrMap;
import com.cisco.ettx.admin.collengine.DataCollectionException;
import org.w3c.dom.*; 
import org.apache.log4j.Logger;
import com.cisco.ettx.admin.common.util.XMLUtil;

//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : SubscriberServices.java
// Desc : Class which performs SMS queries
//**************************************************


public class SMSTaskInfo {
	private String serviceName;
	private String taskName;
	private HashMap	attrMaps;
	private static Logger logger = Logger.getLogger("SMSDataCollector");

	public SMSTaskInfo(String ltaskName) {
		taskName = ltaskName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String lserviceName) {
		serviceName = lserviceName;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String lname) {
		taskName = lname;
	}

	public void setAttrMaps(HashMap lattrMaps) {
		attrMaps = lattrMaps;
	}

	public Object getAttrMapValue(CollectionHolder holder,String attrName)
		throws DataCollectionException
	{
		AttrMap attrMap = (AttrMap)attrMaps.get(attrName);
		if (attrMap == null) {
			return holder.findAttrValue(attrName);
		}
		return attrMap.getAttrValue(holder);
	}


	public void setAttrMapValue(CollectionHolder holder, String attrName,
		Object value) throws DataCollectionException {
		AttrMap attrMap = (AttrMap)attrMaps.get(attrName);
		String aliasName =  attrName;
		if (attrMap != null) {
			aliasName = attrMap.getMapAttr();
			if (aliasName == null) {
				//throw exception
				throw new DataCollectionException(DataCollectionException.UNABLE_TO_SET_VALUE);
			}
		}
		//logger.debug("Adding attribute " + aliasName + " with value " + value);
		holder.addOutputAttr(aliasName,value);
	}

	public void setExportAttrMapValue(CollectionHolder holder, String attrName,
		Object value) throws DataCollectionException {
		AttrMap attrMap = (AttrMap)attrMaps.get(attrName);
		String aliasName =  attrName;
		if (attrMap != null) {
			aliasName = attrMap.getMapAttr();
			if (aliasName == null) {
				//throw exception
				throw new DataCollectionException(DataCollectionException.UNABLE_TO_SET_VALUE);
			}
		}
		holder.addSharedAttr(aliasName,value);
	}

	private static String SMS_TASK= "SMS_TASK";
	private static String TASK_NAME = "NAME";
	private static String SMS_SERVICE = "SERVICE";
	private static String ATTRIBUTE= "ATTRIBUTE";
	private static String ATTR_NAME= "NAME";

	public static SMSTaskInfo readTaskConfig(String fileName) throws DataCollectionException {
		logger.debug("Reading task configuration from file " + fileName);

		try {
			Document doc = XMLUtil.loadXmlFile(fileName);
			NodeList nodelist = doc.getElementsByTagName(SMS_TASK);
			Node node = nodelist.item(0);
			String taskname = XMLUtil.getAttrValue(node,TASK_NAME);
			SMSTaskInfo taskInfo = new SMSTaskInfo(taskname);
			String serviceName = XMLUtil.getAttrValue(node,SMS_SERVICE);
			taskInfo.setServiceName(serviceName);
			HashMap attrs = new HashMap();
			Vector colls = XMLUtil.findChildren(node,ATTRIBUTE);
			Enumeration iter = colls.elements();
			while (iter.hasMoreElements()) {
				Node n = (Node)iter.nextElement();
				String attrName = XMLUtil.getAttrValue(n,ATTR_NAME);
				AttrMap attrMap = AttrMap.getAttrMap(n);
				attrs.put(attrName,attrMap);
			}
			taskInfo.setAttrMaps(attrs);
			return taskInfo;
		}
		catch (DataCollectionException ex) {
			throw ex;
		}
		catch (Exception ex) {
			logger.error("Unable to parse the XML file for task " + fileName,ex);
			throw new DataCollectionException(DataCollectionException.PARSE_ERROR);
		}
	}
}

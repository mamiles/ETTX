
package com.cisco.ettx.admin.collengine;

//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : TaskInfo.java
// Desc : Maintains information about the task
//**************************************************
import java.util.Vector;
import java.util.Enumeration;
import com.cisco.ettx.admin.common.util.XMLUtil;
import org.w3c.dom.*; 
import org.apache.log4j.Logger;
import java.lang.reflect.Method;

public class TaskInfo {
	String taskName;
	Collection[] collectionList = null;
	Vector outputAttrs = null;

	public static final Logger logger = Logger.getLogger("TaskInfo"); 

	public TaskInfo(String ltaskname) {
		taskName = ltaskname;
	}

	public String getTaskName() {
		return taskName;
	}

	public Collection[] getCollectionList() {
		return collectionList;
	}

	public void setCollectionList(Collection[] list) {
		collectionList = list;
	}

	public Vector getOutputAttrsInfo() {
		return outputAttrs;
	}

	public void setOutputAttrsInfo(Vector linfo) {
		outputAttrs = linfo;
	}

	public static class Collection {
		String subTaskName = null;
		DataCollector collector = null;


		public Collection(String lcompTaskName,DataCollector lcoll) {
			subTaskName = lcompTaskName;
			collector = lcoll;
		}

		public Collection() {
		}

		public String getSubTaskName() {
			return subTaskName;
		}
		public void setSubTaskname(String lsubTaskName) {
			subTaskName = lsubTaskName;
		}

		public DataCollector getCollector() 
			throws DataCollectionException {
			return collector;
		}

		public void setDataCollector(DataCollector lcollector) {
			collector = lcollector;
		}

		public static DataCollector mapHandlerToCollector(String handlerName)  
			throws DataCollectionException {
			if (handlerName == null) {
				TaskInfo.logger.error("Unable to map handler name " + handlerName + "to a class. No handler name  present");
				throw new DataCollectionException(DataCollectionException.HANDLER_CLASS_EXCEPTION);
			}
			try {
				return DataCollector.getInstance(handlerName);
			}
			catch (Exception ex) {
				TaskInfo.logger.error("Unable to map handler name " + handlerName + "to a class. Exception : " , ex);
				throw new DataCollectionException(DataCollectionException.HANDLER_CLASS_EXCEPTION);
			}
		}
	}

	public static class AttrDefinition {
		public String attrName;
		public boolean collection;

		public AttrDefinition(String lattrName,boolean lisCollection) {
			attrName = lattrName;
			collection = lisCollection;
		}

		public String getAttrName() {
			return attrName;
		}

		public boolean isCollection() {
			return collection;
		}
	}

	private static String TASK_NAME = "NAME";
	private static String TASK= "TASK";
	private static String COLLECTION= "COLLECTION";
	private static String ORDER= "ORDER";
	private static String HANDLER= "HANDLER";
	private static String COMPONENT_TASK= "COMPONENT_TASK";
	private static String OUTPUT_DEFINITION= "OUTPUT_DEFINITION";
	private static String ATTRIBUTE= "ATTRIBUTE";
	private static String ATTR_NAME= "NAME";

	public static TaskInfo readTaskConfig(String fileName) throws DataCollectionException {
		logger.debug("Reading task configuration from file " + fileName);

		try {
			Document doc = XMLUtil.loadXmlFile(fileName);
			NodeList nodelist = doc.getElementsByTagName(TASK);
			Node node = nodelist.item(0);
			String taskname = XMLUtil.getAttrValue(node,TASK_NAME);
			TaskInfo taskInfo = new TaskInfo(taskname);
			Vector colls = XMLUtil.findChildren(node,COLLECTION);
			Collection[] collList = new Collection[colls.size()];
			Enumeration iter = (Enumeration)colls.elements();
			while (iter.hasMoreElements()) {
				Node child = (Node)iter.nextElement();
				String orderS = XMLUtil.getAttrValue(child,ORDER);
				String handlerName = XMLUtil.getChildValue(child,HANDLER);
				String subtaskName = XMLUtil.getChildValue(child,COMPONENT_TASK);
				Integer order = Integer.valueOf(orderS);
				if (order.intValue() == 0 || order.intValue() > colls.size()) {
					logger.error("Invalid order no " + order + " for task " + taskname);
				}
				if (collList[order.intValue()-1] !=  null) {
					logger.error("Duplicate ordering " + order + " for task " + taskname);
					throw new DataCollectionException(DataCollectionException.PARSE_ERROR);
				}
				DataCollector collector = Collection.mapHandlerToCollector(handlerName);
				Collection coll = new Collection(subtaskName,collector);
				collList[order.intValue()-1] = coll;
			}
			taskInfo.setCollectionList(collList);

			Node outputDef = XMLUtil.getChildNode(node,OUTPUT_DEFINITION);
			if (outputDef == null) {
				logger.error("Unable to parse the XML file for task " + fileName);
				throw new DataCollectionException(DataCollectionException.PARSE_ERROR);
			}
			Vector children = XMLUtil.findChildren(outputDef,ATTRIBUTE);
			Vector outputAttrs = new Vector();
			iter = (Enumeration)children.elements();
			while (iter.hasMoreElements()) {
				Node child = (Node)iter.nextElement();
				String name = XMLUtil.getAttrValue(child,ATTR_NAME);
				String coll = XMLUtil.getAttrValue(child,COLLECTION);
				boolean isColl = false;
				if (coll != null) {
					Boolean bool = Boolean.valueOf(coll);
					isColl = bool.booleanValue();
				}
				outputAttrs.add(new AttrDefinition(name,isColl));
			}
			taskInfo.setOutputAttrsInfo(outputAttrs);
			return taskInfo;
		}
		catch (DataCollectionException ex) {
			throw ex;
		}
		catch (Exception ex) {
			logger.error("Unable to parse the XML file for task " + fileName);
			throw new DataCollectionException(DataCollectionException.PARSE_ERROR);
		}
	}

}

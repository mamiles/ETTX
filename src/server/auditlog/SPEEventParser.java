package com.cisco.ettx.admin.auditlog;

//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : EventParser.java
// Desc : Class which formats an event to be displayed in event log
//**************************************************
import com.tibco.tibrv.TibrvMsg;
import com.cisco.ettx.admin.common.util.XMLUtil;
import org.w3c.dom.*; 
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Hashtable;
import java.util.StringTokenizer;


public class SPEEventParser implements EventParser {
	String field = "CNS.SPE.EVENT";
	private static String CNS_EVENT="CNS_EVENT";
	private static String EVENT_MESSAGE="EVENT_MESSAGE";
	private static String EventId="EventId";
	private static String EventCategory="EventCategory";
	private static String EventName="EventName";
	private static String RequestId="RequestId";
	private static String Time="Time";
	private static String Error="Error";
	private static String ObjectName="ObjectName";
	private static String ClassName="ClassName";
	private static String UserName="UserName";
	private static String ApplicationName="ApplicationName";
	private static String Message="Message";
	private static String[] cnsEventAttrs = {EventCategory,EventId,
				EventName,RequestId};
	private static String[] msgAttrs = {Time,Error,ObjectName,ClassName,
				UserName,ApplicationName,Message};

	private String[] logAttrs = {EventId, EventName, RequestId, 
				Time, ObjectName, ClassName, UserName,
				ApplicationName, Message};
	private String msgSeparator=" ";
	private static Logger myLogger = Logger.getLogger(SPEEventParser.class);


	public SPEEventParser() {
		//Nothing being done
	}

	public void logMessage(Logger logger, TibrvMsg msg) {
		try {
			String eventInfo = (String)msg.get(field);
			logXMLEvent(logger,eventInfo);
		}
		catch (Exception ex) {
		}
	}

	public void doConfigure(String listener,Properties prop) {
		String seq = prop.getProperty("auditlog."+listener+".attributes");
		if (seq != null) {
			StringTokenizer t = new StringTokenizer(seq,",");
			logAttrs = new String[t.countTokens()];
			int i = 0;
			while (t.hasMoreElements()) {
				logAttrs[i] = (String)t.nextElement();
				i++;
			}
		}
		String msgS = prop.getProperty("auditlog."+listener+".separator");
		if (msgS != null) {
			msgSeparator = msgS;
		}
	}

	private void logXMLEvent(Logger logger,String eventInfo) {
		try {
			Hashtable attrValues = new Hashtable();
			Document doc = XMLUtil.loadXml(eventInfo);
			NodeList nodelist = doc.getElementsByTagName(CNS_EVENT);
			Node cnsEvent = nodelist.item(0);
			getAttrs(cnsEvent,cnsEventAttrs,attrValues);
			Vector events  = XMLUtil.findChildren(cnsEvent,EVENT_MESSAGE);
			Enumeration iter = events.elements();
			while (iter.hasMoreElements()) {
				Hashtable eventAttrs = (Hashtable)attrValues.clone();
				Node eventMsg = (Node)iter.nextElement();
				getAttrs(eventMsg,msgAttrs,eventAttrs);
				logEventMessage(logger,eventAttrs);
			}
		}
		catch (Exception ex) {
			logger.error("Unable to map event " + eventInfo,ex);
		}
	}

	private synchronized void logEventMessage(Logger logger,
		Hashtable attrValues) {
		String category = (String)attrValues.get(EventCategory);
		Level  level = Level.INFO;
		if (category != null) {
			level = mapCategoryToLog4jLevel(category);
		}
		StringBuffer msg = new StringBuffer();
		for (int i = 0; i < logAttrs.length; i++) {
			String value = (String)attrValues.get(logAttrs[i]);
			if (value != null) {
				//Note all messages may not appear
				if (msg.length() > 0) {
					msg.append(msgSeparator);
				}
				msg.append(value);
			}
		}
		logger.log(level,msg.toString());
	}

	private Level mapCategoryToLog4jLevel(String value) {
		if (value.equals("ERR")) {
			return Level.ERROR;
		}
		return Level.INFO;	//REVISIT
	}

	private void getAttrs(Node node, String[] attrList, 
		Hashtable attrValues) throws Exception {
		for (int i = 0; i < attrList.length; i++) {
			String value = XMLUtil.getAttrValue(node,attrList[i]);
			if (value != null) {
				attrValues.put(attrList[i],value);
			}
		}
	}

}

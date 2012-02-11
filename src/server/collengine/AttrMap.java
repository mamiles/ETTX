
//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : OutputParser.java
// Desc : Interface class to format a set of data
//**************************************************
package com.cisco.ettx.admin.collengine;

import java.util.Vector;
import java.util.Enumeration;
import org.w3c.dom.*; 
import com.cisco.ettx.admin.common.util.XMLUtil;
import org.apache.log4j.Logger;

public class AttrMap {
	private String value = null;
	private String attrMap = null;
	private static String VALUE = "VALUE";
	private static String ATTR_MAP = "ATTR_MAP";
	private static Logger logger = Logger.getLogger(AttrMap.class);

	public AttrMap() {
	}

	public String getValue() {
		return value;
	}

	public void setValue(String lvalue) {
		value = lvalue;
	}

	public String getMapAttr() {
		return attrMap;
	}

	public void getMapAttr(String lattr) {
		attrMap = lattr;
	}

	public static AttrMap getAttrMap(Node n) throws DataCollectionException {
		AttrMap attr = new AttrMap();
		try {
			attr.value = XMLUtil.getAttrValue(n,VALUE);
			if (attr.value == null) {
				attr.attrMap = XMLUtil.getAttrValue(n,ATTR_MAP);
			}
			if (attr.value == null && attr.attrMap == null) {
				logger.error("Unable to parse the command node. Expected VALUE or ATTR_MAP for Node " + n.getNodeName());
				throw new DataCollectionException(DataCollectionException.PARSE_ERROR);
			}
		}
		catch (Exception ex) {
			logger.error("Unable to parse XML File ", ex);
			throw new DataCollectionException(DataCollectionException.PARSE_ERROR);
		}
		return attr;
	}

	public Object getAttrValue(CollectionHolder holder) throws DataCollectionException {
		Object v = null;
		if (attrMap != null) {
			v = holder.findAttrValue(attrMap);
/*
			if (v == null) {
				logger.error("Unable to find the value for attr ibute " + attrMap);
				throw new DataCollectionException(DataCollectionException.NO_VALUE_FOR_ATTR);
			}
*/
		}
		else {
			v = value;
		}
		return v;
	}
}

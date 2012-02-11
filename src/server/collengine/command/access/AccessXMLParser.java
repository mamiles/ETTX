
package com.cisco.ettx.admin.collengine.command;

import com.cisco.ettx.admin.collengine.*;
import com.cisco.ettx.admin.common.util.XMLUtil;
import java.util.Hashtable;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;

//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : AccessXMLParser.java
// Desc : Parses the XML for HOST and creates a HostAccess object


public class AccessXMLParser {
	public static Logger logger = Logger.getLogger(AccessXMLParser.class);

	private static String ACCESS_CLASS = "ACCESS_CLASS";

	public static HostAccess parseConfig(Node accessNode) 
		throws DataCollectionException {
		try {
			String accessClass = XMLUtil.getAttrValue(accessNode,ACCESS_CLASS);
			if (accessClass == null) {
				logger.error("Expected ACCESS_CLASS for Attribute of ACCESS node");
				throw new DataCollectionException(DataCollectionException.PARSE_ERROR);
			}
			Class cls = Class.forName(accessClass);
			HostAccess access =(HostAccess)cls.newInstance();
			access.parseConfig(accessNode);
			return access;
		}
		catch (Exception ex) {
			logger.error("Unable to parse XML File ", ex);
			throw new DataCollectionException(DataCollectionException.PARSE_ERROR);
		}
	}
}


//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : OutputParser.java
// Desc : Interface class to format a set of data
//**************************************************
package com.cisco.ettx.admin.collengine.command;

import com.cisco.ettx.admin.collengine.*;
import java.util.Vector;
import java.util.Enumeration;
import org.w3c.dom.*; 
import com.cisco.ettx.admin.common.util.XMLUtil;
import org.apache.log4j.Logger;

public class Command {
	public Vector commandParts;

	private static String PART = "PART";
	private static Logger logger = Logger.getLogger(Command.class);

	public Command() {
		commandParts = new Vector();
	}

	public void parseCommandParts(Node commandNode)
		throws DataCollectionException {
		try {
			Vector nodes = XMLUtil.findChildren(commandNode,PART);
			if (nodes.size() == 0) {
				logger.error("Unable to parse the node for commands.Expected PART attributes");
				throw new DataCollectionException(DataCollectionException.PARSE_ERROR);
			}
			Enumeration iter = nodes.elements();
			while (iter.hasMoreElements()) {
				Node n = (Node)iter.nextElement();
				AttrMap part = AttrMap.getAttrMap(n);
				commandParts.add(part);
			}
		}
		catch (Exception ex) {
			logger.error("Unable to parse XML File ", ex);
			throw new DataCollectionException(DataCollectionException.PARSE_ERROR);
		}
	}

	public String getCommand(CollectionHolder holder) 
			throws DataCollectionException {
		Enumeration iter = commandParts.elements();
		StringBuffer buf = new StringBuffer();
		//Assuming command parts are in order
		while (iter.hasMoreElements()) {
			AttrMap commandPart = (AttrMap)iter.nextElement();
			Object value = commandPart.getAttrValue(holder);
			if (value == null) {
				logger.error("Unable to get value for " + commandPart.getMapAttr());
				//throw new DataCollectionException(DataCollectionException.INSUFFICIENT_DATA_FOR_TASK);
				return null;
			}
			buf.append(value.toString());
			//Note that space is not being added by us
			//the command should have the appr. delimiters
		}
		return buf.toString();
			
	}

}

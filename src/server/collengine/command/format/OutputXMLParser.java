//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : OutputXMLParser.java
// Desc : Class which parses XML commands
//**************************************************

package com.cisco.ettx.admin.collengine.command;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import com.cisco.ettx.admin.collengine.DataCollectionException;
import com.cisco.ettx.admin.common.util.XMLUtil;

public class OutputXMLParser {
	public static Logger logger = Logger.getLogger(OutputXMLParser.class);

	private static String PARSER_CLASS = "PARSER_CLASS";

	public static OutputParser parseConfig(Node parserNode) 
		throws DataCollectionException {
		try {
			String parserClass = XMLUtil.getAttrValue(parserNode,PARSER_CLASS);
			if (parserClass == null) {
				logger.error("Expected PARSER_CLASS for Attribute of PARSER node");
				throw new DataCollectionException(DataCollectionException.PARSE_ERROR);
			}
			Class cls = Class.forName(parserClass);
			OutputParser parser =(OutputParser)cls.newInstance();
			parser.parseConfig(parserNode);
			return parser;
		}
		catch (Exception ex) {
			logger.error("Unable to parse XML File ",ex);
			throw new DataCollectionException(DataCollectionException.PARSE_ERROR);
		}
	}
}

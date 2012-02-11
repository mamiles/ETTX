
//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : OutputParser.java
// Desc : Interface class to format a set of data
//**************************************************
package com.cisco.ettx.admin.collengine.command;

import com.cisco.ettx.admin.collengine.*;
import org.w3c.dom.Node;

public interface OutputParser {

	public void formatOutput(CollectionHolder holder,String output) 
			throws DataCollectionException ;

	public void parseConfig(Node parserNode) throws DataCollectionException ;

}

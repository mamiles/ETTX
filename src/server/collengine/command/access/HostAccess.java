


package com.cisco.ettx.admin.collengine.command;

import com.cisco.ettx.admin.collengine.*;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;

//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : HostAccess.java
// Desc : Interface class to perform data collection 
//	locally or remotely. All access must implement this inferface
//**************************************************

public interface HostAccess {
	public String performAction(CollectionHolder holder,
		String command) throws
		DataCollectionException;

	public void parseConfig(Node n) throws DataCollectionException;
	
}

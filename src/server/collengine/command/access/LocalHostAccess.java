

package com.cisco.ettx.admin.collengine.command;

import com.cisco.ettx.admin.collengine.*;
import java.util.Hashtable;
import de.mud.telnet.TelnetWrapper;
import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;

//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : LocalHostAccess.java
// Desc : Class which performs data collection by locally executing a command
//**************************************************

public class  LocalHostAccess implements HostAccess {
	private final static Logger logger = Logger.getLogger(LocalHostAccess.class);


	public LocalHostAccess() {
	}

	public String performAction (CollectionHolder data,
			String command) throws 
		DataCollectionException {
		try {
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec(command);
			int exitCode = proc.waitFor();
			InputStream stream = proc.getInputStream();
			String buf = readFromStream(stream);
			return buf;
		}
		catch (Exception ex) {
			logger.error("Unable to execute command " + command + " Exception : " + ex.toString());
			throw new DataCollectionException(DataCollectionException.UNABLE_TO_EXECUTE_COMMAND);
		}
	}

	public void parseConfig(Node n) throws DataCollectionException {
		//Nothing to be done here
		return;
	}


	private String readFromStream(InputStream stream) 
		throws IOException {
		int avail = stream.available();
		byte[] tmp = new byte[avail];
		stream.read(tmp);
		return new String(tmp);
	}

}

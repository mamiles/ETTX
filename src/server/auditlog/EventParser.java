package com.cisco.ettx.admin.auditlog;

//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : EventParser.java
// Desc : Class which formats an event to be displayed in event log
//**************************************************
import com.tibco.tibrv.*;
import org.apache.log4j.Logger;
import java.util.Properties;


public interface EventParser {
	public void doConfigure(String listenerName, Properties prop);
	public void logMessage(Logger logger, TibrvMsg msg) ;
}

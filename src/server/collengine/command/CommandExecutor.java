

package com.cisco.ettx.admin.collengine.command;

import com.cisco.ettx.admin.collengine.*;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.log4j.Logger;
import java.io.File;
import com.cisco.ettx.admin.common.util.XMLUtil;
import org.w3c.dom.*; 

//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : CommandExecutor.java
// Desc : Interface class to perform data collection via executing a command
//	locally or remotely
//**************************************************

public class CommandExecutor extends DataCollector {
	Hashtable tasks = new Hashtable();
	private File configDir;

	private static Logger logger =  Logger.getLogger("CommandExecutor");
	private static final String CONFIG_DIR = "config/command";

	private static String COMMAND_HANDLER = "COMMAND_HANDLER";
	private static String ACCESS = "ACCESS";
	private static String COMMAND = "COMMAND";
	private static String OUTPUT_PARSER = "OUTPUT_PARSER";
	private static String TASK_NAME = "TASK_NAME";

	public CommandExecutor(String rootDir) throws DataCollectionException {
		super();
		configDir = new File(rootDir,CONFIG_DIR);
		File[] files = configDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = (File)files[i];
			CommandTaskInfo info = readConfigFile(f);
			tasks.put(info.commandTaskName,info);
		}
			
	}

	public void performCollection(String subtaskName,
		CollectionHolder holder) 
		throws DataCollectionException {
		CommandTaskInfo taskInfo = (CommandTaskInfo)tasks.get(subtaskName);
		if (taskInfo == null) {
			//Read information about the taskInfo
			//REVISIT
			//taskInfo = getTaskDetail(subtaskName);
			//tasks.put(subtaskName,taskInfo);
			logger.error("Undefined task " + subtaskName + " in directory " + configDir.getPath());
			throw new DataCollectionException(DataCollectionException.UNABLE_TO_EXECUTE_COMMAND);
		}

		//Create the command to be executed
		String command = taskInfo.getCommand().getCommand(holder);
		if (command == null) {
			logger.error("Unable to form command to be executed");
			return;
		}
		logger.debug("Executing command " + command);
		String output = 
			taskInfo.getHostAccess().performAction(holder,command);
		taskInfo.getOutputParser().formatOutput(holder,output);
		//Action is completed
	}

/*
	public CommandTaskInfo getTaskDetail(String taskName) {
		//REVISIT
		CommandTaskInfo info = new CommandTaskInfo(taskName);
		return info;
	}
*/

	public CommandTaskInfo readConfigFile(File f) throws DataCollectionException {
		logger.debug("Initializing Command Task Information from file " + f.getPath());
		try {
			Document doc = XMLUtil.loadXmlFile(f.getPath());
			NodeList nodelist = doc.getElementsByTagName(COMMAND_HANDLER);
			Node node = nodelist.item(0);
			String taskName = XMLUtil.getAttrValue(node,TASK_NAME);
			CommandTaskInfo info = new CommandTaskInfo(taskName);
			Node commandNode = XMLUtil.getChildNode(node,COMMAND);
			Command command = new Command();
			command.parseCommandParts(commandNode);
			info.setCommand(command);
			Node accessNode = XMLUtil.getChildNode(node,ACCESS);
			info.setHostAccess(AccessXMLParser.parseConfig(accessNode));
			Node parserNode = XMLUtil.getChildNode(node,OUTPUT_PARSER);
			info.setOutputParser(OutputXMLParser.parseConfig(parserNode));
			return info;
		}
		catch (DataCollectionException ex) {
			throw ex;
		}
		catch (Exception ex) {
			logger.error("Unable to parse the XML file " + f.getPath(), ex);
			throw new DataCollectionException(DataCollectionException.PARSE_ERROR);
		}
	}

	private class CommandTaskInfo {
		public String commandTaskName;
		public Command command;
		public HostAccess hostAccess;
		public OutputParser parserInfo;
		
		public CommandTaskInfo(String ltaskName) {
			commandTaskName = ltaskName;
			command = null;
			hostAccess = null;
			parserInfo = null;
		}

		public String getCommandTaskName() {
			return commandTaskName;
		}

		public void setCommand(Command lcommand) {
			command = lcommand;
		}

		public Command getCommand() {
			return command;
		}

		public HostAccess getHostAccess() {
			return hostAccess;
		}

		public void setHostAccess(HostAccess lhost) {
			hostAccess = lhost;
		}

		public OutputParser getOutputParser() {
			return parserInfo;
		}

		public void setOutputParser(OutputParser parser) {
			parserInfo = parser;
		}
	}
}

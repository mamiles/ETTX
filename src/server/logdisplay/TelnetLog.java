//**************************************************
// Copyright (c) 2001, 2002 Cisco Systems, Inc.
// All rights reserved.
//**************************************************
// Author: Marvin Miles

package com.cisco.ettx.admin.server.logdisplay;

import java.util.Vector;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.io.*;
import com.cisco.ettx.admin.common.net.TelnetWrapper;
import com.cisco.ettx.admin.common.net.ScriptHandler;
import org.apache.log4j.Logger;

public class TelnetLog
    extends ProcessLog {

	private static Logger logger = Logger.getLogger("TelnetLog");
	private String hostname;
	private String userid;
	private String password;
	private String prompt;

	public TelnetLog(String hostname, String userid, String password) {
		setHostname(hostname);
		setUserid(userid);
		setPassword(password);
		setPrompt("# ");
	}

	public TelnetLog(String hostname, String userid, String password, String prompt) {
		setHostname(hostname);
		setUserid(userid);
		setPassword(password);
		setPrompt(prompt);
	}

	public Vector getFileList(Vector pathVector) throws LogDisplayException {
		Iterator it = pathVector.iterator();
		String path = null;
		setFileNameList(new Vector());

		TelnetWrapper telnet = new TelnetWrapper();
		try {
			telnet.connect(getHostname(), 23);
			telnet.login(getUserid(), getPassword());
			telnet.setPrompt(this.prompt);
			logger.debug(telnet.send(""));
			if (telnet.send("") == null) {
				logger.error("Password or prompt invalid for telnet");
				throw new LogDisplayException(LogDisplayException.TELNET_ERROR);
			}
			logger.debug(telnet.send(""));
			telnet.setMaxTime(5000);
			while (it.hasNext()) {
				path = it.next().toString();
				StringBuffer fileList = new StringBuffer();
				fileList.append(telnet.send("/usr/bin/ls -l " + path));
				logger.debug(fileList);
				if (fileList == null) {
					logger.error("Timed out waiting for /usr/bin/ls -l " + path);
					throw new LogDisplayException(LogDisplayException.TELNET_ERROR);
				}
				ScriptHandler handler = new ScriptHandler();
				handler.setup("/usr/bin/ls");
				if (handler.match(fileList.toString().getBytes(), fileList.length())) {
					logger.debug("ls command executed ok");
					processFileList(fileList, path);
				}
				else {
					logger.info("ls command may not have worked.  Trying again");
					fileList = new StringBuffer();
					fileList.append(telnet.send("/usr/bin/ls -l " + path));
					logger.debug(fileList);
					processFileList(fileList, path);
				}
			}
			telnet.disconnect();
		}
		catch (java.io.IOException e) {
			logger.error(e);
			throw new LogDisplayException(LogDisplayException.TELNET_IOERROR);
		}
		return (super.fileNameList);
	}

	public Vector getFileList(String path) throws LogDisplayException {
		setFileNameList(new Vector());

		TelnetWrapper telnet = new TelnetWrapper();
		try {
			telnet.connect(getHostname(), 23);
			telnet.login(getUserid(), getPassword());
			telnet.setPrompt(this.prompt);
			telnet.send("");
			if (telnet.send("") == null) {
				logger.error("Password or prompt invalid for telnet");
				throw new LogDisplayException(LogDisplayException.TELNET_ERROR);
			}
			telnet.setMaxTime(5000);
			StringBuffer fileList = new StringBuffer();
			fileList.append(telnet.send("/usr/bin/ls -l " + path));
			if (fileList == null) {
				logger.error("Timed out waiting for /usr/bin/ls -l " + path);
				throw new LogDisplayException(LogDisplayException.TELNET_ERROR);
			}
			ScriptHandler handler = new ScriptHandler();
			handler.setup("/usr/bin/ls");
			if (handler.match(fileList.toString().getBytes(), fileList.length())) {
				logger.debug("ls command executed ok");
				processFileList(fileList, path);
			}
			else {
				logger.info("ls command may not have worked.  Trying again");
				fileList = new StringBuffer();
				fileList.append(telnet.send("/usr/bin/ls -l " + path));
				logger.debug(fileList);
				processFileList(fileList, path);
			}

			telnet.disconnect();
		}
		catch (java.io.IOException e) {
			logger.error(e);
			throw new LogDisplayException(LogDisplayException.TELNET_IOERROR);
		}
		return (super.fileNameList);

	}

	public Vector getFileListWithString(Vector pathVector, String searchValue) throws LogDisplayException {
		Iterator it = pathVector.iterator();
		String path = null;
		int i = 0;
		String pathSearch = "";
		setFileNameList(new Vector());

		TelnetWrapper telnet = new TelnetWrapper();
		try {
			telnet.connect(getHostname(), 23);
			telnet.login(getUserid(), getPassword());
			telnet.setPrompt(this.prompt);
			telnet.send("");
			if (telnet.send("") == null) {
				logger.error("Password or prompt invalid for telnet");
				throw new LogDisplayException(LogDisplayException.TELNET_ERROR);
			}
			telnet.setMaxTime(9000);
			while (it.hasNext()) {
				StringBuffer fileList = new StringBuffer();
				path = it.next().toString();

				boolean foundAst = false;
				for (i = 0; i < path.length(); i++) {
					if (path.charAt(i) == '*') {
						foundAst = true;
						break;
					}
				}
				if (foundAst) {
					pathSearch = path;
				}
				else {
					pathSearch = path + "/*";
				}
				logger.debug("/usr/bin/ls -l `/usr/bin/egrep -i -l '" + searchValue + "' " + pathSearch + " || echo null`");
				fileList.append(telnet.send("/usr/bin/ls -l `/usr/bin/egrep -i -l '" + searchValue + "' " + pathSearch +
				    " || echo null`"));
				logger.debug(fileList);

				if (fileList == null) {
					logger.error("Timed out waiting for /usr/bin/ls -l `/usr/bin/egrep -i -l '" + searchValue + "' " + pathSearch +
					    " || echo null`");
					throw new LogDisplayException(LogDisplayException.TELNET_ERROR);
				}
				ScriptHandler handler = new ScriptHandler();
				handler.setup("/usr/bin/egrep");
				if (handler.match(fileList.toString().getBytes(), fileList.length())) {
					logger.debug("egrep command executed ok");
					processFileList(fileList, pathSearch);
				}
				else {
					logger.debug("egrep command did not seem to execute.  Trying it again.");
					logger.debug("/usr/bin/ls -l `/usr/bin/egrep -i -l '" + searchValue + "' " + pathSearch + " || echo null`");
					fileList = new StringBuffer();
					fileList.append(telnet.send("/usr/bin/ls -l `/usr/bin/egrep -i -l '" + searchValue + "' " + pathSearch +
					    " || echo null`"));
					logger.debug(fileList);
					processFileList(fileList, pathSearch);
				}
			}
			telnet.disconnect();
		}
		catch (java.io.IOException e) {
			logger.error(e);
			throw new LogDisplayException(LogDisplayException.TELNET_IOERROR);
		}
		return (super.fileNameList);
	}

	public Vector getFileListWithString(String path, String searchValue) throws LogDisplayException {

		int i = 0;
		String pathSearch = "";
		setFileNameList(new Vector());

		TelnetWrapper telnet = new TelnetWrapper();
		try {
			telnet.connect(getHostname(), 23);
			telnet.login(getUserid(), getPassword());
			telnet.setPrompt(this.prompt);
			telnet.send("");
			if (telnet.send("") == null) {
				logger.error("Password or prompt invalid for telnet");
				throw new LogDisplayException(LogDisplayException.TELNET_ERROR);
			}
			telnet.setMaxTime(9000);
			StringBuffer fileList = new StringBuffer();
			boolean foundAst = false;
			for (i = 0; i < path.length(); i++) {
				if (path.charAt(i) == '*') {
					foundAst = true;
					break;
				}
			}
			if (foundAst) {
				pathSearch = path;
			}
			else {
				pathSearch = path + "/*";
			}
			fileList.append(telnet.send("/usr/bin/ls -l `/usr/bin/egrep -i -l '" + searchValue + "' " + pathSearch + " || echo null`"));
			if (fileList == null) {
				logger.error("Timed out waiting for /usr/bin/ls -l `/usr/bin/egrep -i -l '" + searchValue + "' " + pathSearch +
					     " || echo null`");
				throw new LogDisplayException(LogDisplayException.TELNET_ERROR);
			}

			ScriptHandler handler = new ScriptHandler();
			handler.setup("/usr/bin/egrep");
			if (handler.match(fileList.toString().getBytes(), fileList.length())) {
				logger.debug("egrep command executed ok");
				processFileList(fileList, pathSearch);
			}
			else {
				logger.debug("egrep command did not seem to execute.  Trying it again.");
				logger.debug("/usr/bin/ls -l `/usr/bin/egrep -i -l '" + searchValue + "' " + pathSearch + " || echo null`");
				fileList = new StringBuffer();
				fileList.append(telnet.send("/usr/bin/ls -l `/usr/bin/egrep -i -l '" + searchValue + "' " + pathSearch +
				    " || echo null`"));
				logger.debug(fileList);
				processFileList(fileList, pathSearch);
			}

			telnet.disconnect();
		}
		catch (java.io.IOException e) {
			logger.error(e);
			throw new LogDisplayException(LogDisplayException.TELNET_IOERROR);
		}
		return (super.fileNameList);
	}

	public long[] getCheckSumAndSize(String fileName) throws LogDisplayException {
		String line = "";
		String ckSum = "";
		String size = "";
		String name = "";
		long[] longArray = {
		    0, 0};

		TelnetWrapper telnet = new TelnetWrapper();
		try {
			telnet.connect(getHostname(), 23);
			telnet.login(getUserid(), getPassword());
			telnet.setPrompt(this.prompt);
			telnet.send("");
			if (telnet.send("") == null) {
				logger.error("Password or prompt invalid for telnet");
				throw new LogDisplayException(LogDisplayException.TELNET_ERROR);
			}
			StringBuffer cmd = new StringBuffer();
			StringBuffer sb = new StringBuffer();
			cmd.append(telnet.send("/usr/bin/cksum " + fileName));
			logger.debug(cmd.toString());
			ScriptHandler handler = new ScriptHandler();
			handler.setup("/usr/bin/cksum");
			if (handler.match(cmd.toString().getBytes(), cmd.length())) {
				logger.debug("cksum command executed ok");
			}
			else {
				logger.debug("cksum command did not seem to execute.  Trying again");
				cmd = new StringBuffer();
				cmd.append(telnet.send("/usr/bin/cksum " + fileName));
				logger.debug(cmd.toString());
			}

			for (int i = 0; i < cmd.length(); i++) {
				if (cmd.charAt(i) != '\n') {
					sb.append(cmd.charAt(i));
				}
				else {
					line = sb.toString();
					if (line.length() != 0) {
						StringTokenizer st = new StringTokenizer(line);
						ckSum = st.nextToken();
						try {
							longArray[0] = Long.parseLong(ckSum);
							size = st.nextToken();
							longArray[1] = Long.parseLong(size);
							name = st.nextToken();
							break;
						}
						catch (NumberFormatException exc) {
							sb = new StringBuffer();
						}
					}
				}
			}
			if (fileName.compareTo(name) != 0) {
				line = cmd.toString();
				logger.error(line);
				logger.error("Error: Can't get Checksum for file " + fileName);
				throw new LogDisplayException(LogDisplayException.CKSUM_ERROR);
			}
			telnet.disconnect();
		}
		catch (java.io.IOException e) {
			logger.error(e);
			throw new LogDisplayException(LogDisplayException.TELNET_IOERROR);
		}
		return longArray;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getHostname() {
		return hostname;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserid() {
		return userid;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public String getPrompt() {
		return prompt;
	}

}
//**************************************************
 // Copyright (c) 2001, 2002 Cisco Systems, Inc.
 // All rights reserved.
 //**************************************************
  // Author: Marvin Miles

  //  NOTE:  Make sure and setup Shell scripts to get from eveionment varialbe

  package com.cisco.ettx.admin.server.logdisplay;

import org.apache.log4j.Logger;
import java.util.Vector;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.io.*;

public class LocalLog
    extends ProcessLog {

	private String ettxRoot = null;
	private static Logger logger = Logger.getLogger("LocalLog");

	public LocalLog(String dir) {
		logger.debug("ettxRoot: " + dir);
		ettxRoot = dir;
		logger.info("LocalLog initialized successfully...");
	}

	public Vector getFileList(Vector pathVector) {
		Iterator it = pathVector.iterator();
		String path = null;

		Runtime r = Runtime.getRuntime();
		Process p; // Process tracks one external native process
		BufferedReader is; // reader for output of process
		BufferedReader es;
		String line;
		setFileNameList(new Vector());

		while (it.hasNext()) {
			path = it.next().toString();
			logger.debug("Processing path: " + path);
			StringBuffer fileList = new StringBuffer();
			try {

				p = r.exec("/usr/bin/ksh " + ettxRoot + "/scripts/list.ksh " + path);
				is = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ( (line = is.readLine()) != null) {
					fileList.append(line + "\n");
				}

				try {
					p.waitFor(); // wait for process to complete
				}
				catch (InterruptedException e) {
					logger.error(e); // "Can'tHappen"
					return (super.fileNameList);
				}
				if (p.exitValue() != 0) {
					es = new BufferedReader(new InputStreamReader(p.getErrorStream()));
					logger.error(es.readLine());
					logger.error("Process done, exit status: " + p.exitValue());
				}
			}
			catch (IOException io) {
				io.printStackTrace();
			}
			logger.debug("Redy to process");
			processFileList(fileList, path);
		}

		return (super.fileNameList);
	}

	public Vector getFileList(String path) {
		Runtime r = Runtime.getRuntime();
		Process p; // Process tracks one external native process
		BufferedReader is; // reader for output of process
		BufferedReader es;
		String line;
		StringBuffer fileList = new StringBuffer();
		setFileNameList(new Vector());

		try {
			p = r.exec("/usr/bin/ksh " + ettxRoot + "/scripts/list.ksh " + path);
			is = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ( (line = is.readLine()) != null) {
				fileList.append(line + "\n");
				logger.debug(line);
			}
			try {
				p.waitFor(); // wait for process to complete
			}
			catch (InterruptedException e) {
				logger.error(e); // "Can'tHappen"
				return (super.fileNameList);
			}
			if (p.exitValue() != 0) {
				es = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				logger.error(es.readLine());
				logger.error("Process done, exit status: " + p.exitValue());
			}

		}
		catch (IOException io) {
			io.printStackTrace();
		}
		processFileList(fileList, path);
		return (super.fileNameList);
	}

	public Vector getFileListWithString(Vector pathVector, String searchValue) {
		Iterator it = pathVector.iterator();
		String path = null;
		int i = 0;
		String pathSearch = "";

		Runtime r = Runtime.getRuntime();
		Process p; // Process tracks one external native process
		BufferedReader is; // reader for output of process
		BufferedReader es;
		String line;
		setFileNameList(new Vector());

		while (it.hasNext()) {
			path = it.next().toString();
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
			try {
				String cmd = "/usr/bin/ksh " + ettxRoot + "/scripts/egrep.ksh " + pathSearch + " " + searchValue;
				logger.debug(cmd);
				p = r.exec(cmd);
				is = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ( (line = is.readLine()) != null) {
					fileList.append(line + "\n");
					logger.debug(line);
				}
				try {
					p.waitFor(); // wait for process to complete
				}
				catch (InterruptedException e) {
					logger.error(e); // "Can'tHappen"
					return (super.fileNameList);
				}
				if (p.exitValue() != 0) {
					es = new BufferedReader(new InputStreamReader(p.getErrorStream()));
					logger.error(es.readLine());
					logger.error("Process done, exit status: " + p.exitValue());
				}

			}
			catch (IOException io) {
				io.printStackTrace();
			}
			processFileList(fileList, pathSearch);
		}

		return (super.fileNameList);
	}

	public Vector getFileListWithString(String path, String searchValue) {
		int i = 0;
		String pathSearch = "";

		Runtime r = Runtime.getRuntime();
		Process p; // Process tracks one external native process
		BufferedReader is; // reader for output of process
		BufferedReader es;
		String line;
		setFileNameList(new Vector());

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
		try {
			String cmd = "/usr/bin/ksh " + ettxRoot + "/scripts/egrep.ksh " + pathSearch + " " + searchValue;
			logger.debug(cmd);
			p = r.exec(cmd);
			is = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ( (line = is.readLine()) != null) {
				fileList.append(line + "\n");
				logger.debug(line);
			}
			try {
				p.waitFor(); // wait for process to complete
			}
			catch (InterruptedException e) {
				logger.error(e); // "Can'tHappen"
				return (super.fileNameList);
			}
			if (p.exitValue() != 0) {
				es = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				logger.error(es.readLine());
				logger.error("Process done, exit status: " + p.exitValue());
			}

		}
		catch (IOException io) {
			io.printStackTrace();
		}
		processFileList(fileList, pathSearch);

		return (super.fileNameList);

	}

	public long[] getCheckSumAndSize(String fileName) {
		Runtime r = Runtime.getRuntime();
		Process p; // Process tracks one external native process
		BufferedReader is; // reader for output of process
		BufferedReader es;
		String line;
		String ckSum = "";
		String size = "";
		String name = "";
		long[] longArray = {
		    0, 0};
		try {
			p = r.exec("/usr/bin/cksum " + fileName);
			is = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ( (line = is.readLine()) != null) {
				logger.debug(line);
				StringTokenizer st = new StringTokenizer(line);
				ckSum = st.nextToken();
				size = st.nextToken();
				name = st.nextToken();
				if (fileName.compareTo(name) == 0) {
					break;
				}
			}
			try {
				p.waitFor(); // wait for process to complete
			}
			catch (InterruptedException e) {
				logger.error(e); // "Can'tHappen"
				return longArray;
			}
			if (p.exitValue() != 0) {
				es = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				logger.error(es.readLine());
				logger.error("Process done, exit status: " + p.exitValue());
			}

		}
		catch (IOException io) {
			io.printStackTrace();
		}
		longArray[0] = Long.parseLong(ckSum);
		longArray[1] = Long.parseLong(size);
		return longArray;
	}

	public String getHostname() {
		Runtime r = Runtime.getRuntime();
		Process p; // Process tracks one external native process
		BufferedReader is; // reader for output of process
		BufferedReader es;
		String line;
		String hostName = "";

		try {
			p = r.exec("/usr/bin/hostname");
			is = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ( (line = is.readLine()) != null) {
				logger.debug(line);
				StringTokenizer st = new StringTokenizer(line);
				hostName = st.nextToken();
			}
			try {
				p.waitFor(); // wait for process to complete
			}
			catch (InterruptedException e) {
				logger.error(e); // "Can'tHappen"
				return hostName;
			}
			if (p.exitValue() != 0) {
				es = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				logger.error(es.readLine());
				logger.error("Process done, exit status: " + p.exitValue());
			}

		}
		catch (IOException io) {
			io.printStackTrace();
		}
		return hostName;
	}

	public void createLink(String sourceName, String targetName) {
		Runtime r = Runtime.getRuntime();
		Process p; // Process tracks one external native process
		BufferedReader is; // reader for output of process
		BufferedReader es;
		String line;

		try {
			p = r.exec("/usr/bin/ln -s " + sourceName + " " + targetName);
			is = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ( (line = is.readLine()) != null) {
				logger.debug(line);
			}
			try {
				p.waitFor(); // wait for process to complete
			}
			catch (InterruptedException e) {
				logger.error(e); // "Can'tHappen"
				return;
			}
			if (p.exitValue() != 0) {
				es = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				logger.error(es.readLine());
				logger.error("Process done, exit status: " + p.exitValue());
			}

		}
		catch (IOException io) {
			io.printStackTrace();
		}
		return;
	}
}
//**************************************************
 // Copyright (c) 2001, 2002 Cisco Systems, Inc.
 // All rights reserved.
 //**************************************************
  // Author: Marvin Miles

package com.cisco.ettx.admin.server.logdisplay;

import org.apache.log4j.Logger;
import com.cisco.ettx.admin.common.ApplLog;
import com.cisco.ettx.admin.common.SMSComponent;
import com.cisco.ettx.admin.common.ComponentHostData;
import com.cisco.ettx.admin.config.ComponentConfigManager;
import com.cisco.ettx.admin.config.ConfigurationException;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.io.*;
import java.io.File;

/**
 * This class will ftp a remote log file or provide a listing of log files.
 * @author Marvin Miles
 * @version 1
 **/
public class LogDisplayServer {

	private static LogDisplayServer instance = null;
	private static Logger logger = Logger.getLogger("LogDisplayServer");
	private String logCache = null;
	private String logUrl = null;
	private String hostName = null;
	private LocalLog localLog;
	private String ettxRoot = null;

	public static LogDisplayServer getInstance() {
		return instance;
	}

	public LogDisplayServer(String dir, String catalinaHome, String tomcatPort) throws LogDisplayException {
		if (instance != null) {
			//singleton object
			logger.error("Creating more than one instance");
			throw new LogDisplayException(LogDisplayException.SINGLETON_VIOLATION);
		}
		logCache = new String(catalinaHome + "/webapps/ettx_admin_server/logDisplay");
		localLog = new LocalLog(dir);
		ettxRoot = dir;
		hostName = localLog.getHostname();
		logUrl = "http://" + hostName + ":" + tomcatPort + "/ettx_admin_server/logDisplay";
		// logUrl = "/logDisplay";
		instance = this;
		try {
			new PurgeServerCache(catalinaHome);
		}
		catch (LogDisplayException ex) {
			logger.error(ex);
		}
		logger.info("Component configuration initialized successfully...");
	}

	public ApplLog[] getLogFilenames(SMSComponent[] comps, String filterString) throws LogDisplayException {
		Vector componentHostData;
		String componentName;
		ArrayList applLog = new ArrayList();
		Vector fileNameAttr = new Vector();

		for (int i = 0; i < comps.length; i++) {
			componentHostData = comps[i].getComponentHostData();
			componentName = comps[i].getName();
			logger.debug("getLogFilenames for " + i + " Component: " + componentName + " Number of Components: " + comps.length);
			for (int j = 0; j < componentHostData.size(); j++) {
				ComponentHostData host = (ComponentHostData) componentHostData.elementAt(j);

				// Determine if local or remote

				logger.debug("Processing " + j + " host: " + host.getHostName() + " Unix UID: " + host.getUnixLoginID() + " PWD: " + host.getUnixPassword() + " Prompt: " + host.getUnixPrompt());
				Iterator debugIt = host.getLogDirs().iterator();
				logger.debug("Number of Log Directories: " + host.getLogDirs().size());
				while (debugIt.hasNext()) {
					logger.debug("Processing LogPathDir: " + debugIt.next().toString());
				}
				logger.debug("Completed list of LogPathDir");
				logger.debug("Local hostname: " + localLog.getHostname() + " Processing hostname: " + host.getHostName());
				if (host.getHostName().equals("localhost") || host.getHostName().equals(localLog.getHostname())) {
					logger.debug("Log Files are Local");
					if (filterString.equals("")) {
						logger.debug("Calling localLog.getFileList(logDirs)");
						fileNameAttr = localLog.getFileList(host.getLogDirs());
					}
					else {
						logger.debug("Calling localLog.getFileListWithString(logDirs, filterString)");
						fileNameAttr = localLog.getFileListWithString(host.getLogDirs(), filterString);
					}
					Iterator it = fileNameAttr.iterator();
					while (it.hasNext()) {
						FileNameAttr fna = (FileNameAttr) it.next();
						applLog.add(new ApplLog(componentName, host.getHostName(), fna.getFileNamePath(), fna.getShortFileName(), fna.getSizeString(), fna.getChangeDate()));
					}
				}
				else {
					logger.debug("Log Files are Remote");
					if (host.getUseSecureShell()) {
						logger.info("Secure Shell being used");
						SshLog ssh = new SshLog(ettxRoot, host.getHostName(), host.getUnixLoginID(), host.getUnixPassword(), host.getUnixPrompt());
						if (filterString.equals("")) {
							logger.debug("Calling ssh.getFileList(logDirs)");
							fileNameAttr = ssh.getFileList(host.getLogDirs());
						}
						else {
							logger.debug("Calling ssh.getFileListWithString(logDirs, filterString)");
							fileNameAttr = ssh.getFileListWithString(host.getLogDirs(), filterString);
						}
						Iterator itsh = fileNameAttr.iterator();
						while (itsh.hasNext()) {
							FileNameAttr fnaa = (FileNameAttr) itsh.next();
							applLog.add(new ApplLog(componentName, host.getHostName(), fnaa.getFileNamePath(), fnaa.getShortFileName(), fnaa.getSizeString(), fnaa.getChangeDate()));
						}

					}
					else {
						TelnetLog telnet = new TelnetLog(host.getHostName(), host.getUnixLoginID(), host.getUnixPassword(), host.getUnixPrompt());
						if (filterString.equals("")) {
							logger.debug("Calling telnet.getFileList(logDirs)");
							fileNameAttr = telnet.getFileList(host.getLogDirs());
						}
						else {
							logger.debug("Calling telnet.getFileListWithString(logDirs, filterString)");
							fileNameAttr = telnet.getFileListWithString(host.getLogDirs(), filterString);
						}
						Iterator itt = fileNameAttr.iterator();
						while (itt.hasNext()) {
							FileNameAttr fnaa = (FileNameAttr) itt.next();
							applLog.add(new ApplLog(componentName, host.getHostName(), fnaa.getFileNamePath(), fnaa.getShortFileName(), fnaa.getSizeString(), fnaa.getChangeDate()));
						}
					}
				}
			}
		}
		logger.debug("Ready to return array of ApplLog objects");
		ApplLog[] returnList = (ApplLog[]) applLog.toArray(new ApplLog[applLog.size()]);
		return returnList;
	}

	public String getLogFile(ApplLog logFileDesc) throws LogDisplayException {
		long[] remoteCksumAndSize = {
		    0, 0};
		long[] localCksumAndSize = {
		    0, 0};
		String filePathString = null;
		String fileSplitDir = null;
		ComponentHostData host = null;
		File cacheDir;

		ComponentConfigManager mgr = ComponentConfigManager.getInstance();
		try {
			SMSComponent comp = mgr.getComponent(logFileDesc.getApplName());
			host = comp.getHost(logFileDesc.getHostName());
		}
		catch (ConfigurationException ce) {
			logger.error(ce);
		}
		if (host.getHostName().equals("localhost") || host.getHostName().equals(localLog.getHostname())) {
			filePathString = logFileDesc.getLogPath() + "/" + logFileDesc.getLogName();
			fileSplitDir = logCache + "/" + logFileDesc.getApplName() + "/" + logFileDesc.getHostName() + logFileDesc.getLogPath() + "/" + logFileDesc.getLogName();
			File localFile = new File(filePathString);
			if (localFile.length() > 2097152) {
				fileSplitWithDir(filePathString, fileSplitDir, logFileDesc.getLogName());
				return logUrl + "/" + logFileDesc.getApplName() + "/" + logFileDesc.getHostName() + logFileDesc.getLogPath() + "/" + logFileDesc.getLogName();
			}
			cacheDir = new File(logCache + "/" + logFileDesc.getApplName() + "/" + logFileDesc.getHostName() + logFileDesc.getLogPath());
			if (!cacheDir.exists()) {
				cacheDir.mkdirs();
			}
			File link = new File(fileSplitDir + ".txt");
			if (link.exists()) {
				link.delete();
			}
			localLog.createLink(filePathString, fileSplitDir + ".txt");
			return logUrl + "/" + logFileDesc.getApplName() + "/" + logFileDesc.getHostName() + logFileDesc.getLogPath() + "/" + logFileDesc.getLogName() + ".txt";
		}
		else {
			if (host.getUseSecureShell()) {
				logger.info("Secure Shell being used.");
				filePathString = logCache + "/" + logFileDesc.getApplName() + "/" + logFileDesc.getHostName() + logFileDesc.getLogPath() + "/" + logFileDesc.getLogName();
				File cacheFile = new File(filePathString + ".txt");
				SshLog ssh = new SshLog(ettxRoot, host.getHostName(), host.getUnixLoginID(), host.getUnixPassword(), host.getUnixPrompt());
				if (cacheFile.exists() && cacheFile.isFile()) {
					remoteCksumAndSize = ssh.getCheckSumAndSize(logFileDesc.getLogPath() + "/" + logFileDesc.getLogName());
					localCksumAndSize = localLog.getCheckSumAndSize(filePathString + ".txt");
					if (remoteCksumAndSize[0] != localCksumAndSize[0] || remoteCksumAndSize[1] != localCksumAndSize[1]) {
						cacheFile.delete();
						ssh.getRemoteFile(logFileDesc.getLogPath() + "/" + logFileDesc.getLogName(), filePathString + ".txt");
					}
				}
				else {
					cacheDir = new File(logCache + "/" + logFileDesc.getApplName() + "/" + logFileDesc.getHostName() + logFileDesc.getLogPath());
					cacheDir.mkdirs();
					ssh.getRemoteFile(logFileDesc.getLogPath() + "/" + logFileDesc.getLogName(), filePathString + ".txt");
				}
				if (cacheFile.length() > 2097152) {
					fileSplit(filePathString, logFileDesc.getLogName());
					cacheFile.delete();
					return logUrl + "/" + logFileDesc.getApplName() + "/" + logFileDesc.getHostName() + logFileDesc.getLogPath() + "/" + logFileDesc.getLogName();
				}
				else {
					return logUrl + "/" + logFileDesc.getApplName() + "/" + logFileDesc.getHostName() + logFileDesc.getLogPath() + "/" + logFileDesc.getLogName() + ".txt";
				}
			}
			else {
				filePathString = logCache + "/" + logFileDesc.getApplName() + "/" + logFileDesc.getHostName() + logFileDesc.getLogPath() + "/" + logFileDesc.getLogName();
				File cacheFile = new File(filePathString + ".txt");
				FtpLog ftpLog = new FtpLog(host.getHostName(), host.getUnixLoginID(), host.getUnixPassword());
				if (cacheFile.exists() && cacheFile.isFile()) {
					TelnetLog telnet = new TelnetLog(host.getHostName(), host.getUnixLoginID(), host.getUnixPassword(), host.getUnixPrompt());
					remoteCksumAndSize = telnet.getCheckSumAndSize(logFileDesc.getLogPath() + "/" + logFileDesc.getLogName());
					localCksumAndSize = localLog.getCheckSumAndSize(filePathString + ".txt");
					if (remoteCksumAndSize[0] != localCksumAndSize[0] || remoteCksumAndSize[1] != localCksumAndSize[1]) {
						cacheFile.delete();
						ftpLog.getRemoteFile(logFileDesc.getLogPath() + "/" + logFileDesc.getLogName(), filePathString + ".txt");
					}
				}
				else {
					cacheDir = new File(logCache + "/" + logFileDesc.getApplName() + "/" + logFileDesc.getHostName() + logFileDesc.getLogPath());
					cacheDir.mkdirs();
					ftpLog.getRemoteFile(logFileDesc.getLogPath() + "/" + logFileDesc.getLogName(), filePathString + ".txt");
				}
				if (cacheFile.length() > 2097152) {
					fileSplit(filePathString, logFileDesc.getLogName());
					cacheFile.delete();
					return logUrl + "/" + logFileDesc.getApplName() + "/" + logFileDesc.getHostName() + logFileDesc.getLogPath() + "/" + logFileDesc.getLogName();
				}
				else {
					return logUrl + "/" + logFileDesc.getApplName() + "/" + logFileDesc.getHostName() + logFileDesc.getLogPath() + "/" + logFileDesc.getLogName() + ".txt";
				}
			}
		}
	}

	private void fileSplit(String filePathString, String fileNameString) {
		// Split file "filePathString + ".txt"" into directory filePathString
		FileOutputStream fos;
		BufferedOutputStream bos;

		try {
			File dirFile = new File(filePathString);
			if (dirFile.exists() && dirFile.isDirectory()) {
				File[] dirList = dirFile.listFiles();
				for (int i = 0; i < dirList.length; i++) {
					String delFileName = dirList[i].getAbsolutePath();
					if (dirList[i].delete()) {
						logger.debug("Deleted file: " + delFileName);
					}
					else {
						logger.debug("Failed to delete file: " + delFileName);
					}
				}
			}
			else {
				logger.debug("Making Directory: " + filePathString);
				dirFile.mkdirs();
			}

			FileInputStream fis = new FileInputStream(filePathString + ".txt");
			BufferedInputStream bis = new BufferedInputStream(fis);
			int splitNo = 1;
			fos = new FileOutputStream(filePathString + "/" + fileNameString + ".split" + splitNo + ".txt");
			bos = new BufferedOutputStream(fos);
			splitNo++;
			int byteCount = 1;
			int b;
			while ( (b = bis.read()) != -1) {
				if (byteCount++ > 1048576) {
					bos.flush();
					bos.close();
					fos = new FileOutputStream(filePathString + "/" + fileNameString + ".split" + splitNo + ".txt");
					bos = new BufferedOutputStream(fos);
					splitNo++;
					byteCount = 1;
				}
				bos.write(b);
			}
			bis.close();
			bos.close();
		}
		catch (IOException e) {
			logger.error(e);
		}

	}

	private void fileSplitWithDir(String filePathString, String fileSplitDir, String fileNameString) {
		// Split file "filePathString" into directory fileSplitDir - File to split fileNameString
		FileOutputStream fos;
		BufferedOutputStream bos;

		try {
			File dirFile = new File(fileSplitDir);
			if (dirFile.exists() && dirFile.isDirectory()) {
				File[] dirList = dirFile.listFiles();
				for (int i = 0; i < dirList.length; i++) {
					String delFileName = dirList[i].getAbsolutePath();
					if (dirList[i].delete()) {
						logger.debug("Deleted file: " + delFileName);
					}
					else {
						logger.debug("Failed to delete file: " + delFileName);
					}
				}
			}
			else {
				logger.debug("Making Directory: " + fileSplitDir);
				dirFile.mkdirs();
			}

			FileInputStream fis = new FileInputStream(filePathString);
			BufferedInputStream bis = new BufferedInputStream(fis);
			int splitNo = 1;
			fos = new FileOutputStream(fileSplitDir + "/" + fileNameString + ".split" + splitNo + ".txt");
			bos = new BufferedOutputStream(fos);
			splitNo++;
			int byteCount = 1;
			int b;
			while ( (b = bis.read()) != -1) {
				if (byteCount++ > 1048576) {
					bos.flush();
					bos.close();
					fos = new FileOutputStream(fileSplitDir + "/" + fileNameString + ".split" + splitNo + ".txt");
					bos = new BufferedOutputStream(fos);
					splitNo++;
					byteCount = 1;
				}
				bos.write(b);
			}
			bis.close();
			bos.close();
		}
		catch (IOException e) {
			logger.error(e);
		}

	}
}

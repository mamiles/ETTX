//**************************************************
 // Copyright (c) 2001, 2002 Cisco Systems, Inc.
 // All rights reserved.
 //**************************************************
  // Author: Marvin Miles

  // Add the following method:
  //		public Vector getFileList(Vector pathVector)
  //  Only if I have time because this same method is avaialbel through telnet.
  //
  //  Fix problem with getFileList(String path) so it excludes files starting with .  (hidden files)  (funcionality allready handled in telnet).

  package com.cisco.ettx.admin.server.logdisplay;

import java.util.Vector;
import java.util.StringTokenizer;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.*;
import com.cisco.ettx.admin.common.net.FtpClient;
import com.cisco.ettx.admin.common.net.FtpReader;
import com.cisco.ettx.admin.common.net.FtpInputStream;
import org.apache.log4j.Logger;

/**
 * This class will ftp a remote log file or provide a listing of log files.
 * @author Marvin Miles
 * @version 1
 **/
public class FtpLog
    extends ProcessLog {

	private static Logger logger = Logger.getLogger("FtpLog");
	private String hostname;
	private String userid;
	private String password;
	Vector fileNameList = new Vector();
	public static final char IMAGE_TYPE = 'I';
	public static final char ASCII_TYPE = 'A';

	public FtpLog(String hostname, String userid, String password) {
		setHostname(hostname);
		setUserid(userid);
		setPassword(password);
		logger.info("FTP to host: " + hostname + " using userid: " + userid + " initialized successfully");
	}

	public Vector getFileList(String path) throws LogDisplayException {

		setFileNameList(new Vector());

		logger.info("FTP get file list for: " + path);
		try {
			FtpClient ftp = new FtpClient();
			ftp.setDebugger(new OutputStreamWriter(System.out));
			ftp.connect(getHostname());
			if (!ftp.getResponse().isPositiveCompletion()) {
				logger.error("Failed to connect to " + getHostname() + ".");
				throw new LogDisplayException(LogDisplayException.FAILED_TO_CONNECT);
			}
			ftp.userName(getUserid());
			if (!ftp.getResponse().isPositiveIntermediary()) {
				logger.error("Userid " + getUserid() + " not accepted.");
				throw new LogDisplayException(LogDisplayException.USERID_ERROR);
			}
			ftp.password(getPassword());
			if (!ftp.getResponse().isPositiveCompletion()) {
				logger.error("Password not accepted.");
				throw new LogDisplayException(LogDisplayException.PASSWORD_ERROR);
			}
			ftp.representationType(ASCII_TYPE);
			if (!ftp.getResponse().isPositiveCompletion()) {
				logger.error("TYPE IMAGE failed.");
				throw new LogDisplayException(LogDisplayException.TYPE_IMAGE_FAILED);
			}
			ftp.dataPort();
			if (!ftp.getResponse().isPositiveCompletion()) {
				logger.error("PORT failed.");
				throw new LogDisplayException(LogDisplayException.PORT_FAILED);
			}

			FtpReader ftpin = ftp.list(path);
			if (!ftp.getResponse().isPositivePreliminary()) {
				logger.error("LIST failed.");
				throw new LogDisplayException(LogDisplayException.LIST_FAILED);
			}

			processFileList(ftpin, path);

			ftpin.close();

			if (!ftp.getResponse().isPositiveCompletion()) {
				logger.error("List processing failed.");
				throw new LogDisplayException(LogDisplayException.LIST_PROCESSING_FAILED);
			}
			ftp.logout();
			if (!ftp.getResponse().isPositiveCompletion()) {
				logger.error("Logout failed.");
				throw new LogDisplayException(LogDisplayException.LOGOUT_FAILED);
			}
			ftp.disconnect();
		}
		catch (Exception exc) {
			logger.error("FTP failed");
			throw new LogDisplayException(exc.getMessage());
		}

		return (this.fileNameList);
	}

	public void getRemoteFile(String remotePath, String localPath) throws LogDisplayException {
		String listline = null;
		StringBuffer sb = new StringBuffer();
		int b;
		String subPath = null;
		setFileNameList(new Vector());
		logger.info("FTP remote file: " + remotePath + " to " + localPath);
		try {
			FtpClient ftp = new FtpClient();
			ftp.setDebugger(new OutputStreamWriter(System.out));
			ftp.connect(getHostname());
			if (!ftp.getResponse().isPositiveCompletion()) {
				logger.error("Failed to connect to " + getHostname() + ".");
				throw new LogDisplayException(LogDisplayException.FAILED_TO_CONNECT);
			}
			ftp.userName(getUserid());
			if (!ftp.getResponse().isPositiveIntermediary()) {
				logger.error("Userid " + getUserid() + " not accepted.");
				throw new LogDisplayException(LogDisplayException.USERID_ERROR);
			}
			ftp.password(getPassword());
			if (!ftp.getResponse().isPositiveCompletion()) {
				logger.error("Password not accepted.");
				throw new LogDisplayException(LogDisplayException.PASSWORD_ERROR);
			}
			ftp.representationType(IMAGE_TYPE);
			if (!ftp.getResponse().isPositiveCompletion()) {
				logger.error("TYPE IMAGE failed.");
				throw new LogDisplayException(LogDisplayException.TYPE_IMAGE_FAILED);
			}
			ftp.dataPort();
			if (!ftp.getResponse().isPositiveCompletion()) {
				logger.error("PORT failed.");
				throw new LogDisplayException(LogDisplayException.PORT_FAILED);
			}

			FtpInputStream ftpstr = ftp.retrieveStream(remotePath);
			if (!ftp.getResponse().isPositivePreliminary()) {
				logger.error("RETR retrieveStream failed.");
				throw new LogDisplayException(LogDisplayException.RETR_FAILED);
			}
			BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(localPath));
			while (true) {
				int ch = ftpstr.read();
				if (ch == -1) {
					ftpstr.close();
					os.close();
					break;
				}
				os.write(ch);
				// System.out.print((char)ch);
			}
			// System.logger.debug();

			if (!ftp.getResponse().isPositiveCompletion()) {
				logger.error("RETR retrieveStream processing failed.");
				throw new LogDisplayException(LogDisplayException.RETR_PROCESSING_FAILED);
			}
			ftp.logout();
			if (!ftp.getResponse().isPositiveCompletion()) {
				logger.error("Logout failed.");
				throw new LogDisplayException(LogDisplayException.LOGOUT_FAILED);
			}
			ftp.disconnect();
		}
		catch (Exception exc) {
			logger.error("FTP failed");
			throw new LogDisplayException(exc.getMessage());
		}

		return;
	}

	public void getRemoteFileAndDelete(String remotePath, String localPath) throws LogDisplayException {
		String listline = null;
		StringBuffer sb = new StringBuffer();
		int b;
		String subPath = null;
		setFileNameList(new Vector());
		logger.info("FTP and delete remote file: " + remotePath + " to " + localPath);
		try {
			FtpClient ftp = new FtpClient();
			ftp.setDebugger(new OutputStreamWriter(System.out));
			ftp.connect(getHostname());
			if (!ftp.getResponse().isPositiveCompletion()) {
				logger.error("Failed to connect to " + getHostname() + ".");
				throw new LogDisplayException(LogDisplayException.FAILED_TO_CONNECT);
			}
			ftp.userName(getUserid());
			if (!ftp.getResponse().isPositiveIntermediary()) {
				logger.error("Userid " + getUserid() + " not accepted.");
				throw new LogDisplayException(LogDisplayException.USERID_ERROR);
			}
			ftp.password(getPassword());
			if (!ftp.getResponse().isPositiveCompletion()) {
				logger.error("Password not accepted.");
				throw new LogDisplayException(LogDisplayException.PASSWORD_ERROR);
			}
			ftp.representationType(IMAGE_TYPE);
			if (!ftp.getResponse().isPositiveCompletion()) {
				logger.error("TYPE IMAGE failed.");
				throw new LogDisplayException(LogDisplayException.TYPE_IMAGE_FAILED);
			}
			ftp.dataPort();
			if (!ftp.getResponse().isPositiveCompletion()) {
				logger.error("PORT failed.");
				throw new LogDisplayException(LogDisplayException.PORT_FAILED);
			}

			FtpInputStream ftpstr = ftp.retrieveStream(remotePath);
			if (!ftp.getResponse().isPositivePreliminary()) {
				logger.error("RETR retrieveStream failed.");
				throw new LogDisplayException(LogDisplayException.RETR_FAILED);
			}
			BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(localPath));
			while (true) {
				int ch = ftpstr.read();
				if (ch == -1) {
					ftpstr.close();
					os.close();
					break;
				}
				os.write(ch);
			}
			if (!ftp.getResponse().isPositiveCompletion()) {
				logger.error("RETR retrieveStream processing failed.");
				throw new LogDisplayException(LogDisplayException.RETR_PROCESSING_FAILED);
			}
			ftp.delete(remotePath);
			if (!ftp.getResponse().isPositiveCompletion()) {
				logger.error("DELE failed.");
				throw new LogDisplayException(LogDisplayException.DELETE_FAILED);
			}

			ftp.logout();
			if (!ftp.getResponse().isPositiveCompletion()) {
				logger.error("Logout failed.");
				throw new LogDisplayException(LogDisplayException.LOGOUT_FAILED);
			}
			ftp.disconnect();
		}
		catch (Exception exc) {
			logger.error("FTP failed");
			throw new LogDisplayException(exc.getMessage());
		}

		return;
	}

	private void processFileList(FtpReader ftpin, String path) throws LogDisplayException {
		String listline = null;
		StringBuffer sb = new StringBuffer();
		int b;
		int i;
		String subPath = null;
		// Determine if there is a * in the path name

		boolean foundAst = false;
		for (i = 0; i < path.length(); i++) {
			if (path.charAt(i) == '*') {
				foundAst = true;
				break;
			}
		}
		if (foundAst) {
			subPath = null;
		}
		else {
			subPath = path;
		}

		try {
			while (ftpin.ready()) {
				b = ftpin.read();
				if (b != '\n') {
					sb.append( (char) b);
				}
				else {
					listline = sb.toString();
					if (listline.charAt(0) == '-') {
						StringTokenizer st = new StringTokenizer(listline);
						FileNameAttr fileName = new FileNameAttr();
						fileName.setAttributes(st.nextToken());
						fileName.setNumInodes(st.nextToken());
						fileName.setOwnerName(st.nextToken());
						fileName.setGroupName(st.nextToken());
						fileName.setSize(st.nextToken());
						fileName.setChangeDate(st.nextToken() + " " + st.nextToken() + " " + st.nextToken());
						if (subPath == null) {
							fileName.setFileName(st.nextToken());
						}
						else {
							fileName.setFileName(subPath + "/" + st.nextToken());
						}
						while (st.hasMoreTokens()) {
							fileName.setFileName(fileName.getFileName() + " " + st.nextToken());
						}
						if (fileName.getFileName().charAt(0) != '.') {
							this.fileNameList.addElement(fileName);
							logger.debug(fileName.toString());
						}
					}
					else {
						if (listline.charAt(0) == '/') {
							StringTokenizer sPath = new StringTokenizer(listline, ":");
							subPath = sPath.nextToken();
							// System.out.println(listline);

						}
					}
					sb = new StringBuffer();
				}
			}
		}
		catch (Exception exc) {
			logger.error("Error Processsing File List in FTP");
			throw new LogDisplayException(exc.getMessage());
		}

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

	public void setFileNameList(Vector fileNameList) {
		this.fileNameList = fileNameList;
	}

	public Vector getFileNameList() {
		return fileNameList;
	}

}

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
import org.apache.log4j.Logger;

public class ProcessLog {

	private static Logger logger = Logger.getLogger("ProcessLog");
	public Vector fileNameList = new Vector();


	public void processFileList(StringBuffer fileList, String path) {
		String listline = null;
		String subPath = null;
		int i = 0;

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
		logger.debug("subPath: " + subPath);
		StringBuffer sb = new StringBuffer();
		for (i = 0; i < fileList.length(); i++) {
			if ( fileList.charAt(i) != '\n' ) {
				sb.append(fileList.charAt(i));
			}
			else {
				listline = sb.toString();
				//logger.debug(listline);
				if (listline.length() != 0) {
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
							//logger.debug(fileName.toString());
						}
					}
					else {
						if (listline.charAt(0) == '/' && listline.endsWith(":")) {
							StringTokenizer sPath = new StringTokenizer(listline, ":");
							subPath = sPath.nextToken();
//                     					System.out.println(listline);
						}
					}
				}
				sb = new StringBuffer();
			}
		}
	}

	public void setFileNameList(Vector fileNameList) {
		this.fileNameList = fileNameList;
	}
	public Vector getFileNameList() {
		return fileNameList;
	}

}
//**************************************************
// Copyright (c) 2001, 2002 Cisco Systems, Inc.
// All rights reserved.
//**************************************************
// Author: Marvin Miles
//
// Create method to get getShortFileName and getPath

package com.cisco.ettx.admin.server.logdisplay;
import java.util.StringTokenizer;
import java.lang.Integer;

/**
* This class holds all the attributes from a UNIX file (INODE) from a ls -l command.
* @author Marvin Miles
* @version 1
**/
public class FileNameAttr {
	
	private String attributes;
	private int numInodes;
	private String ownerName;
	private String groupName;
	private int size;
	private String changeDate;
	private String fileName;


	/**
	*  Constructor that creates a default FileNameAttr object
	**/
	public FileNameAttr() {
		this("", 0, "", "", 0, "", "");
	}

	/**
	*  Constructor to enter all the values FileNameAttr object
	**/	
	public FileNameAttr(String attributes, int numInodes, String ownerName, String groupName, int size, String changeDate, String fileName) {
		setAttributes(attributes);
		setNumInodes(numInodes);
		setOwnerName(ownerName);
		setGroupName(groupName);
		setSize(size);
		setChangeDate(changeDate);
		setFileName(fileName);
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}
	public String getAttributes() {
		return attributes;
	}
	public void setNumInodes(String numInodes) {
		this.numInodes = Integer.parseInt(numInodes);
	}
	public void setNumInodes(int numInodes) {
		this.numInodes = numInodes;
	}
	public int getNumInodes() {
		return numInodes;
	}	
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getOwnerName() {
		return ownerName;
	}	
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setSize(String size) {
		this.size = Integer.parseInt(size);
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getSize() {
		return size;
	}
	public String getSizeString() {
		Integer fileSize = new Integer(size);
		return fileSize.toString();
		
	}
	public void setChangeDate(String changeDate) {
		this.changeDate = changeDate;
	}
	public String getChangeDate() {
		return changeDate;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileName() {
		return fileName;
	}
	public String getFileNamePath() {
		StringTokenizer st = new StringTokenizer(fileName, "/");
		String pathName = "";
		int j = st.countTokens() - 1;
		for (int i = 0; i < j; i++) {
			pathName = pathName + "/" + st.nextToken();
		}
		return pathName;	
	}
	public String getShortFileName() {
		StringTokenizer st = new StringTokenizer(fileName, "/");
		String pathName = "";
		int j = st.countTokens() - 1;
		for (int i = 0; i < j; i++) {
			pathName = pathName + "/" + st.nextToken();
		}
		return st.nextToken();			
	}
	public String toString() {
		return getAttributes() + " size: " + getSize() + " Date: " + getChangeDate() + " Filename: " + getFileName();
		// return getAttributes() + " " + getNumInodes() + " " + getOwnerName() + " " + getGroupName() + " " + getSize() + " " + getChangeDate() + " " + getFileName();
	}
}

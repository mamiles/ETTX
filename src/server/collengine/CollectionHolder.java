
package com.cisco.ettx.admin.collengine;


//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : CollectionHolder.java
// Desc : Maintains information about the task
//**************************************************
import java.util.HashMap;

public class CollectionHolder {
	HashMap sharedAttrs;
	HashMap outputAttrs;
	String sessionID = null;

	public CollectionHolder(String lsessionID) {
		sharedAttrs = null;
		outputAttrs = null;
		sessionID = lsessionID;
	}

	public HashMap getOutputAttrs() {
		return outputAttrs;
	}

	public void setOutputAttrs(HashMap list) {
		outputAttrs =list;
	}

	public HashMap getSharedAttrs() {
		return sharedAttrs;
	}

	public void setSharedAttrs(HashMap list) {
		sharedAttrs =list;
	}

	public Object findAttrValue(String attrName) {
		return sharedAttrs.get(attrName);
	}

	public String getAttrStringValue(String attrName) {
		return (String)findAttrValue(attrName);
	}

	public void addOutputAttr(String attrName,Object value) {
		outputAttrs.put(attrName,value);
		//Also added to shared attributes
		sharedAttrs.put(attrName,value);
	}

	public void addSharedAttr(String attrName, Object value) {
		sharedAttrs.put(attrName,value);
	}

	public String getSessionID() {
		return sessionID;
	}
}

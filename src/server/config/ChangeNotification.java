package com.cisco.ettx.admin.config;

//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : ChangeNotification.java
// Desc : Clss which maintains component information
//**************************************************
import org.apache.log4j.Logger;
import java.util.Vector;
import java.util.Enumeration;

public class ChangeNotification {

	public interface NotifyTarget {
		public void configChanged(Object from,Object to);
	}

	private Vector targets = new Vector();

	//Called only by the upper class
	protected ChangeNotification() {
	}

	public void addTarget(NotifyTarget target) {
		targets.add(target);
	}

	protected void notifyChange(Object from, Object to) {
		try {
			Enumeration iter = targets.elements();
			while (iter.hasMoreElements()) {
				NotifyTarget target = (NotifyTarget)iter.nextElement();
				target.configChanged(from,to);
			}
		}
		catch (Exception ex) {
			//REVISIT What do we do with exceptions
		}
	}
}

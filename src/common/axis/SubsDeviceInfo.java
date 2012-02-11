
package com.cisco.ettx.admin.common;

import java.util.Vector;
import java.beans.Beans;
import java.io.Serializable;

// The class should implement Serializable to prevent tomcat
// from casting an exception on startup.
public class SubsDeviceInfo extends Beans implements Serializable {

	public String macAddress = null;
	public String displayName = null;
	public boolean activeStatus = true;

	public SubsDeviceInfo() {
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String lid) {
		macAddress = lid;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String lname) {
		displayName = lname;
	}


}


package com.cisco.ettx.admin.common;

import java.util.Vector;
import java.util.StringTokenizer;

public class ComponentHostData {
	String hostName = null;
	String unixLoginID = null;
	String unixPassword = null;
	String unixPrompt = null;
	boolean useSecureShell = false;
	Vector logDirs = new Vector();

	public ComponentHostData() {
	}

	public ComponentHostData(ComponentHostData in) {
		hostName = in.hostName;
		unixLoginID = in.unixLoginID;
		unixPassword = in.unixPassword;
		useSecureShell = in.useSecureShell;
		logDirs = in.logDirs;
		unixPrompt = in.unixPrompt;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String lhostName) {
		hostName=lhostName;
	}

	public String getUnixLoginID() {
		return unixLoginID;
	}

	public void setUnixLoginID(String lunixLoginID) {
		unixLoginID=lunixLoginID;
	}

	public String getUnixPassword() {
		return unixPassword;
	}

	public void setUnixPassword(String lunixPassword) {
		unixPassword=lunixPassword;
	}

	public String getUnixPrompt() {
		return unixPrompt;
	}

	public void setUnixPrompt(String lunixPrompt) {
		unixPrompt=lunixPrompt;
	}

	public Vector getLogDirs() {
		return logDirs;
	}

	public void setLogDirs(Vector ldirs) {
		logDirs = ldirs;
	}

	public boolean getUseSecureShell() {
		return useSecureShell;
	}

	public void setUseSecureShell(boolean value) {
		useSecureShell = value;
	}

}

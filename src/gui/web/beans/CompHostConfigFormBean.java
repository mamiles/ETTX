
package com.cisco.ettx.admin.gui.web.beans;

import java.util.Vector;
import java.util.StringTokenizer;
import com.cisco.ettx.admin.common.ComponentHostData;

public class CompHostConfigFormBean {
	ComponentHostData host;
	String currentLayer = null;
	String selectedLogDir = null;

	private static final String LOCALHOST = "localhost";
	private static final String REMOTEHOST = "remotehost";

	public CompHostConfigFormBean(ComponentHostData lhost) {
		host = lhost;
		initCurrentLayer();
	}

	private void initCurrentLayer() {
		String hostName = getHostName();
		if (hostName != null && hostName.equals(LOCALHOST)) {
			currentLayer = LOCALHOST;
		}
		else {
			currentLayer = REMOTEHOST;
		}
	}

	public void setCurrentLayer(String llayer) {
		currentLayer = llayer;
		if (currentLayer.equals(LOCALHOST)) {
			setHostName(LOCALHOST);
		}
		else {
			String hostName = getHostName();
			if (hostName != null && hostName.equals(LOCALHOST)) {
				setHostName("");
			}
		}
	}

	public String getCurrentLayer() {
		return currentLayer;
	}

	public void setSelectedLogDir(String ldir) {
		selectedLogDir =ldir;
	}

	public String getSelectedLogDir() {
		return selectedLogDir;
	}

	public ComponentHostData getHost() {
		return host;
	}

	public String getHostName() {
		return host.getHostName();
	}

	public void setHostName(String lhostName) {
		host.setHostName(lhostName);
	}

/*
	public String getLoginID() {
		return host.getLoginID();
	}

	public void setLoginID(String lloginID) {
		host.setLoginID(lloginID);
	}

	public String getPassword() {
		return host.getPassword();
	}

	public void setPassword(String lpassword) {
		host.setPassword(lpassword);
	}
*/

	public String getUnixLoginID() {
		return host.getUnixLoginID();
	}

	public void setUnixLoginID(String lunixLoginID) {
			//Need this for scroll table issue
			//value cannot be "" string
		if (lunixLoginID != null && lunixLoginID.length() == 0)
			lunixLoginID = null;
		host.setUnixLoginID(lunixLoginID);
	}

	public String getUnixPassword() {
		return host.getUnixPassword();
	}

	public void setUnixPassword(String lunixPassword) {
			//Need this for scroll table issue
		if (lunixPassword != null && lunixPassword.length() == 0)
			lunixPassword = null;
		host.setUnixPassword(lunixPassword);
	}

	public String getUnixPrompt() {
		return host.getUnixPrompt();
	}

	public void setUnixPrompt(String lunixPrompt) {
		host.setUnixPrompt(lunixPrompt);
	}

	public String getLogDirectory() {
		StringBuffer logDir = new StringBuffer();
		Vector logDirs = host.getLogDirs();
		for (int i = 0; i < logDirs.size(); i++) {
			String log = (String)logDirs.elementAt(i);
			logDir.append(log);
			logDir.append("\n");
		}
		return logDir.toString();
	}

	public void setLogDirectory(String logDir) {
		StringTokenizer tokenizer = new StringTokenizer(logDir,"\r\n");
		Vector logDirs = new Vector();
		while (tokenizer.hasMoreElements()) {
			logDirs.add(tokenizer.nextElement());
		}
		host.setLogDirs(logDirs);
		return;
	}

	public String getUseSecureShell() {
		Boolean bool = new Boolean(host.getUseSecureShell());
		return bool.toString();
	}

	public void setUseSecureShell(String value) {
		Boolean bool = new Boolean(value);
		host.setUseSecureShell(bool.booleanValue());
	}
}

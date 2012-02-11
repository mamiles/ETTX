package com.cisco.ettx.admin.gui.web.beans;

import java.util.Hashtable;
import java.util.StringTokenizer;
import org.apache.struts.action.*;
import com.cisco.ettx.admin.common.SMSComponent;
import com.cisco.ettx.admin.common.ComponentHostData;

public final class CompConfigurationFormBean  extends ActionForm {
	Hashtable components = new Hashtable();
	SMSComponent currentComponent = null;
	CompHostConfigFormBean currentCompHost = null;
	String selectedHostName = null;

	public CompConfigurationFormBean() {
	}

	public Hashtable getComponents() {
		return components;
	}

	public void setSelectedHostName(String lname) {
		selectedHostName = lname;
	}

	public String getSelectedHostName() {
		return selectedHostName;
	}

	public void setCurrentCompHost(ComponentHostData lhost) {
		currentCompHost = new CompHostConfigFormBean(lhost);
	}

	public ComponentHostData getCurrentCompHost() {
		return currentCompHost.getHost();
	}

	public void setCurrentComponent(SMSComponent lcomp) {
		currentComponent = lcomp;
	}

	public CompHostConfigFormBean getCurrentHostBean() {
		return currentCompHost;
	}

	public SMSComponent getCurrentComponent() {
		return currentComponent;
	}

}

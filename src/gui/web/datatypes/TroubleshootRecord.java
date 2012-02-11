package com.cisco.ettx.admin.gui.web.datatypes;

import java.util.Vector;
import java.beans.Beans;
import java.io.Serializable;

// The class should implement Serializable to prevent tomcat
// from casting an exception on startup.
public class TroubleshootRecord extends SubscriberRecord
{
	public String traceOutput = new String();
	public String pingOutput = new String();
	public String showPortOutput = new String();

	public TroubleshootRecord() {
	}


	public String getTraceOutput() {
		return traceOutput;
	}

	public void setTraceOutput(String ltraceOutput) {
		traceOutput = ltraceOutput;
	}

	public String getPingOutput() {
		return pingOutput;
	}

	public void setPingOutput(String loutput) {
		pingOutput = loutput;
	}
	
	public String getShowPortOutput() {
		return showPortOutput;
	}
	public void setShowPortOutput(String lportOutput) {
		showPortOutput = lportOutput;
	}

}

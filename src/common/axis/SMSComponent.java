package com.cisco.ettx.admin.common;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

public class SMSComponent {
	String name = new String();
	String label = new String();

	Vector componentHostData = new Vector();

	public SMSComponent() {
	}

	public SMSComponent(String lname) {
		name = lname;
	}

	public String getName() {
		return name;
	}
	public void setName(String lname) {
		name = lname;
	}

	public String getLabel() {
		return label;
	}
	public void setLabel(String llabel) {
		label = llabel;
	}

	public Vector getComponentHostData() {
		return componentHostData;
	}

	public void setComponentHostData(Vector vector) {
		componentHostData = vector;
	}

	public void addHost(ComponentHostData hostData) {
		componentHostData.add(hostData);
	}

	public void deleteHost(ComponentHostData hostData) {
		componentHostData.remove(hostData);
	}


	public void deleteHost(String hostName) {
		for (int i = 0; i < componentHostData.size(); i++) {
			ComponentHostData host = (ComponentHostData)componentHostData.elementAt(i);
			if (host.getHostName().equals(hostName)) {
				componentHostData.remove(host);
			}
		}
	}

	public ComponentHostData getHost(String hostName) {
		for (int i = 0; i < componentHostData.size(); i++) {
			ComponentHostData host = (ComponentHostData)componentHostData.elementAt(i);
			if (host.getHostName().equals(hostName)) {
				return host;
			}
		}
		return null;
	}

	public static SMSComponent[] toArray(Hashtable elems) {
		Enumeration iter = elems.elements();
		SMSComponent[] array = new SMSComponent[elems.size()];
		int i = 0;
		while (iter.hasMoreElements()) {
			array[i++] = (SMSComponent)iter.nextElement();
		}
		return array;
	}
}

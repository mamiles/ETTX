package com.cisco.ettx.admin.gui.web.datatypes;

import java.util.Vector;
import java.beans.Beans;
import java.io.Serializable;

// The class should implement Serializable to prevent tomcat
// from casting an exception on startup.
public class PostalAddressMap extends Beans implements Serializable {

	public String postalAddress = null;
	public String postalCity = null;
	public String postalState = null;
	public String postalCode = null;
	public String portIdentifier = null;
	public String switchIPAddress = null;
	public String switchPortName = null;
	public String switchID = null;
    public String model = null;

	public PostalAddressMap() {
	}

	public void setPostalAddress(String laddr) {
		postalAddress = laddr;
	}

	public String getPostalAddress() {
		return postalAddress;
	}

	public void setPostalCity(String laddr) {
		postalCity = laddr;
	}

	public String getPostalCity() {
		return postalCity;
	}

	public void setPostalState(String lstate) {
		postalState = lstate;
	}

	public String getPostalState() {
		return postalState;
	}

	public void setSwitchPortName(String details) {
		switchPortName = details;
	}

	public String getSwitchPortName() {
		return switchPortName;
	}

	public String getSwitchIPAddress() {
		return switchIPAddress;
	}
	public void setSwitchIPAddress(String addr) {
		switchIPAddress = addr;
	}

	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String code) {
		postalCode = code;
	}
    public String getModel() {
	return model;
    }
    public void setModel(String model) {
	this.model = model;
    }

    public String getPortIdentifier() {
	return portIdentifier;
    }
    public void setPortIdentifier(String portIdentifier) {
	this.portIdentifier = portIdentifier;
    }

    public String getSwitchID() {
	return switchID;
    }
    public void setSwitchID(String id) {
	this.switchID = id;
    }


}

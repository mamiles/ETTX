package com.cisco.ettx.admin.gui.web.datatypes;

import java.util.Vector;
import java.beans.Beans;
import java.io.Serializable;
import com.cisco.ettx.admin.common.SubsDeviceInfo;

// The class should implement Serializable to prevent tomcat
// from casting an exception on startup.
public class SubscriberRecord extends Beans implements Serializable {

	public String subscriberID = null;
	public String subscriberName = null;
	public String subscriberFirstName = null;
	public String subscriberLastName = null;
	public String loginID = null;
	public String postalAddress = null;
	public String postalCity = null;
	public String postalState = null;
	public String countryCode = null;
	public Vector subscribedServices = new Vector();
	public String homeNumber = null;
	public String mobileNumber = null;
	public String accountNumber = null;
	public String accountStatus = null;
	public String postalCode = null;
	public String password = null;
	public String switchIPAddress = new String();
	public String switchPortName = null;
	public boolean switchOption82 = false;
	public Vector macAddresses = new Vector();
	public Vector ipAddresses = new Vector();
	public Vector subsDevices = new Vector();
	public String portIdentifier = null;
    public String switchId = null;
    public String portInterface = null;

	public SubscriberRecord(String fdn) {
		subscriberID = fdn;
	}

	public SubscriberRecord() {
	}

	public String getSubscriberID() {
		return subscriberID;
	}

	public void setSubscriberID(String lid) {
		subscriberID = lid;
	}

	public String getSubscriberName() {
		return subscriberName;
	}

	public void setSubscriberName(String lname) {
		subscriberName = lname;
	}

	public String getSubscriberFirstName() {
		return subscriberFirstName;
	}

	public void setSubscriberFirstName(String lname) {
		subscriberFirstName = lname;
	}

	public String getSubscriberLastName() {
		return subscriberLastName;
	}

	public void setSubscriberLastName(String lname) {
		subscriberLastName = lname;
	}

	public String getSubscriberFullName() {
		if (subscriberLastName != null && subscriberFirstName != null) {
			StringBuffer buf = new StringBuffer();
			buf.append(subscriberLastName);
			buf.append(", ");
			buf.append(subscriberFirstName);
			return buf.toString();
		}
		if (subscriberLastName != null) return subscriberLastName;
		if (subscriberFirstName != null) return subscriberFirstName;
		return " ";
	}


	public String getSubscriberNameLink() {
		StringBuffer subscriberNameLink = new StringBuffer();
		subscriberNameLink.append(this.getSubscriberName());
		subscriberNameLink.append(" <javascript:invokeTroubleshoot('");
		subscriberNameLink.append(this.getSubscriberID());
		subscriberNameLink.append("')>");
		return subscriberNameLink.toString();
	}

	public String getLoginIDLink() {
		StringBuffer loginIDLink = new StringBuffer();
		loginIDLink.append(this.getLoginID());
		loginIDLink.append(" <javascript:invokeTroubleshoot('");
		loginIDLink.append(this.getSubscriberID());
		loginIDLink.append("')>");
		return loginIDLink.toString();
	}

	public String getLoginID() {
		return loginID;
	}
	public void setLoginID(String llid) {
		loginID = llid;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String lacctNumber) {
		accountNumber = lacctNumber;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String lacctStatus) {
		accountStatus = lacctStatus;
	}

	public void setSubscribedServices(Vector services) {
		subscribedServices = services;
	}

	public Vector getSubscribedServices() {
		return subscribedServices;
	}

	public String getSubscriberServicesText() {
		return convertToTextData(subscribedServices);
	}

	public String getSubscriberServicesCell() {
		return convertToCellData(subscribedServices);
	}

	private String convertToTextData(Vector vector) {
		StringBuffer string = new StringBuffer();
		for (int i = 0; i < vector.size() ; i++) {
			String element = (String)vector.elementAt(i);
			string.append(element);
			string.append("\n");
		}
		return string.toString();
	}

	public String convertToCellData(Vector vector) {
		int size = 30; //REVISIT
		StringBuffer string = new StringBuffer();
		for (int i = 0; i < vector.size() ; i++) {
			String element = (String)vector.elementAt(i);
			string.append(element);
			for (int k = element.length(); k < size;k++) string.append(" ");
		}
		return string.toString();
	}

	public void setHomeNumber(String inhomeNumber) {
		homeNumber = inhomeNumber;
	}

	public String getHomeNumber() {
		return homeNumber;
	}

	public void setMobileNumber(String inmobileNumber) {
		mobileNumber = inmobileNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
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

	public void setCountryCode(String lcode) {
		countryCode = lcode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public String getIpAddressText() {
		return convertToTextData(ipAddresses);
	}

	public void setIpAddresses(Vector addr) {
		ipAddresses = addr;
	}

	public Vector getIpAddresses() {
		return ipAddresses;
	}

	public String getMacAddressText() {
		return convertToTextData(macAddresses);
	}

	public void setMacAddresses(Vector addr) {
		macAddresses = addr;
	}
	public Vector getMacAddresses() {
		return macAddresses;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String lpwd) {
		password = lpwd;
	}
	public String getPortIdentifier() {
		return portIdentifier;
	}
	public void setPortIdentifier(String portIdent) {
		portIdentifier = portIdent;
	}
    public String getSwitchId() {
		return switchId;
    }
    public void setSwitchId(String switchId) {
		this.switchId = switchId;
    }
    public String getPortInterface() {
		return portInterface;
    }
    public void setPortInterface(String portInterface) {
		this.portInterface = portInterface;
    }

	public Vector getSubsDevices() {
		return subsDevices;
	}

	public void setSubsDevices(Vector ldevices) {
		subsDevices = ldevices;
	}

	public boolean getSwitchOption82() {
		return switchOption82;
	}

	public void setSwitchOption82(boolean loption82) {
		switchOption82 = loption82;
	}

	public String getSubsDevicesText() {
		StringBuffer string = new StringBuffer();
		for (int i = 0; i < subsDevices.size() ; i++) {
			SubsDeviceInfo element = (SubsDeviceInfo)subsDevices.elementAt(i);
			string.append(element.getMacAddress());
			string.append("\n");
		}
		return string.toString();
	}


}

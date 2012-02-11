package com.cisco.ettx.admin.gui.web.datatypes;

import java.util.*;
import java.beans.Beans;
import java.io.Serializable;

// The class should implement Serializable to prevent tomcat
// from casting an exception on startup.
public class LeaseHistoryRecord extends Beans implements Serializable {

    public String subscriberName = new String();
    public String loginID = new String();
    public String devicePort = new String();
    public String ipAddress = new String();
    public String macAddress = new String();
    public String leaseStatus = new String();
    public Calendar   startDate = null;
    public Calendar endDate = null;

    public String getSubscriberName() {
	return subscriberName;
    }

    public void setSubscriberName(String lname) {
	subscriberName = lname;
    }

    public String getSubscriberNameLink() {
	StringBuffer subsNameLink = new StringBuffer();
	subsNameLink.append(this.getSubscriberName());
	subsNameLink.append(" <javascript:invokeTroubleshoot('");
	subsNameLink.append(this.getSubscriberName());
	subsNameLink.append("')>");
	return subsNameLink.toString();
    }

    public String getDevicePort() {
	return devicePort;
    }
    public void setDevicePort(String devicePort) {
	this.devicePort = devicePort;
    }

    public String getIpAddress() {
	return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
	this.ipAddress = ipAddress;
    }

    public Date getStartDateAsDate() {
		return startDate.getTime();
	}

    public Calendar getStartDate() {
	return startDate;
    }

    public void setStartDate(Calendar startDate) {
	this.startDate = startDate;
    }

    public Date getEndDateAsDate() {
		return endDate.getTime();
	}

    public Calendar getEndDate() {
	return endDate;
    }

    public void setEndDate(Calendar endDate) {
	this.endDate = endDate;
    }

    public String getMacAddress() {
	return macAddress;
    }

    public void setMacAddress(String addr) {
	this.macAddress = addr;
    }

    public String getLeaseStatus() {
	return leaseStatus;
    }

    public void setLeaseStatus(String status) {
	this.leaseStatus = status;
    }

    public String getLoginID() {
	return loginID;
    }

    public void setLoginID(String id) {
	this.loginID = id;
    }

}

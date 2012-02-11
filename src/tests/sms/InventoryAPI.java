/**
 * Class: InventoryAPI
 *
 * Copyright (c) 2003 by Cisco Systems, Inc. All rights reserved.
 *
 * Author: djf
 *
 */

package com.cisco.sesm.sms.extensions;

import com.cisco.sesm.sms.exceptions.api.*;
import com.cisco.sesm.sms.types.*;
import com.cisco.cns.security.soap.common.*;
import org.apache.log4j.Logger;

/** 
 * InventoryAPI
 *
 * @author Chandrika R
 */
public class InventoryAPI 
{

	private static Logger logger = Logger.getLogger(InventoryAPI.class);
    public PortInfo queryPortFromAccount(SecurityToken token, String subscriberAccountNo)
	throws AuthenticationAPIException , IllegalArgumentAPIException
   {
		logger.info("queryPortFromAccount(acctNo) " + subscriberAccountNo);
		if (token == null) throw new AuthenticationAPIException("Authen Failure");
            if (subscriberAccountNo == null || 
		subscriberAccountNo.length() == 0) {
			throw new IllegalArgumentAPIException("Illegal Arguments");
	    }
	    PortInfo portInfo = new PortInfo();
	    portInfo.setId("10.89.158.62 atm0/1");
	    portInfo.setPortInterface("atm0/1");
	    portInfo.setSwitchId("10.89.158.62");

	    return portInfo;
    }

	public SwitchInfo[] query(SecurityToken token, SwitchInfo switchInfo)
	throws AuthenticationAPIException , IllegalArgumentAPIException
   	{
		logger.info("query(SwitchInfo) " + switchInfo);
		if (token == null) throw new AuthenticationAPIException("Authen Failure");
		if (switchInfo == null || switchInfo.getId() == null || !switchInfo.getId().equals("10.89.158.62")) {
			throw new IllegalArgumentAPIException("Invalid Switch ID");
		}
	
		SwitchInfo[] info = new SwitchInfo[1];
		info[0] = new SwitchInfo();
	    	info[0].setId("10.89.158.62");
	    	info[0].setIpAddress("10.89.158.62");
	    	info[0].setModel("3500XL");
		info[0].setConnectionId("10.89.158.62");
		return  info;
	}

    public String getCPEAddressForSubscriber(SecurityToken token,
                                      String subscriberAccountNo)
	throws AuthenticationAPIException , IllegalArgumentAPIException {
		logger.info("getCPEAddressForSubscriber(acctNo) " + subscriberAccountNo);
		if (token == null) throw new AuthenticationAPIException("Authen Failure");
            if (subscriberAccountNo == null || 
		subscriberAccountNo.length() == 0) {
			throw new IllegalArgumentAPIException("Missing fields");
	    }
		return "128.107.138.145";
    }

    public EdgeDeviceInfo getEdgeDevice(
		SecurityToken token, SwitchInfo switchInfo) 
	throws AuthenticationAPIException , IllegalArgumentAPIException
   {

		logger.info("getEdgeDevice(switchInfo) " + switchInfo);
		if (token == null) throw new AuthenticationAPIException("Authen Failure");
            if (switchInfo == null ||
		switchInfo.getId() == null) {
			throw new IllegalArgumentAPIException("Illegal Arguments");
	    }
		EdgeDeviceInfo info = new EdgeDeviceInfo();
		info.setIpAddress("64.101.181.63");
		return info;
    }

	public ConnectInfo[] query(SecurityToken token, ConnectInfo info) 
	throws AuthenticationAPIException , IllegalArgumentAPIException {
		logger.info("query(ConnectInfo) " + info);
		if (token == null) throw new AuthenticationAPIException("Authen Failure");
            if (info == null ||
		info.getId() == null) {
			throw new IllegalArgumentAPIException("Illegal Arguments");
	    }
		ConnectInfo[] outList = new ConnectInfo[1];
		outList[0] = new ConnectInfo();
		outList[0].setId(info.getId());
	    	outList[0].setUserName("");
	    	outList[0].setPassword("adsl");
	    	outList[0].setEnablePassword("adsl");
		return outList;
	}

	public SwitchInfo[] querySwitchByIds(SecurityToken token, String[] ids)
	throws AuthenticationAPIException , IllegalArgumentAPIException {
		if (token == null) throw new AuthenticationAPIException("Authen Failure");
            if (ids == null || ids.length == 0) {
			throw new IllegalArgumentAPIException("Illegal Arguments");
	    }
		SwitchInfo[] info = new SwitchInfo[ids.length];
		for (int  i = 0; i < ids.length;i++) {
			info[i] = new SwitchInfo();
			info[i].setId(ids[i]);
			info[i].setIpAddress(ids[i]);
			if (i%2 != 0) {
	    			info[0].setModel("3500XL");
			}
			else {
	    			info[0].setModel("4000");
			}
		}
		return info;
	}
}

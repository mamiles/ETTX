package com.cisco.ettx.admin.gui.web.beans;

import java.util.Vector;
import org.apache.struts.action.*;
import com.cisco.ettx.admin.gui.web.datatypes.SubscriberRecord;

public final class SubscriberListFormBean  extends QuerySubscribersFormBean {
	Vector subscriberList = new Vector();
	String selectedSubsFDN = new String();

	public SubscriberListFormBean()  {
		//createSubscribers();
	}

	public Vector getSubscriberList() {
		return subscriberList;
	}

	public void setSubscriberList(Vector list) {
		subscriberList = list;
	}

	public void setSelectedSubsFDN(String fdn) {
		selectedSubsFDN = fdn;
	}

	public String getSelectedSubsFDN() {
		return selectedSubsFDN;
	}

	public void save(QuerySubscribersFormBean lbean) {
    		givenName = lbean.givenName;
    		familyName = lbean.familyName;
    		loginID = lbean.loginID;
    		homeNumber = lbean.homeNumber;
    		mobileNumber = lbean.mobileNumber;
    		postalAddress = lbean.postalAddress;
    		postalState = lbean.postalState;
    		accountNumber = lbean.accountNumber;
    		service = lbean.service;
    		switchDetails = lbean.switchDetails;
    		macAddress = lbean.macAddress;
    		macAddressAlias = lbean.macAddressAlias;
		currentLayer = lbean.currentLayer;
	}

/*
	private void createSubscribers() {
		for (int i = 0; i < 10; i++) {
			System.out.println("Create Subscriber: Subs" + i);	
			SubscriberRecord rec = new SubscriberRecord("Subs" + i);
			//Make subs name linkable to troubleshoot Send the FDN as the parameter
			// rec.setSubsName("Subscriber " + i + "<javascript:invokeTroubleshoot('" + "Subs" + i + "')>");
			rec.setSubsName("Subscriber " + i);
			rec.setLoginID("Subs" + i);
			Vector ipaddresses = new Vector();
			ipaddresses.add(new String("192.168.1.100"));
			ipaddresses.add(new String("192.168.1.101"));
			rec.setIpAddressList(ipaddresses);
			if (i == 5)
			    rec.setAddress("12515, Research Blvd, Bld. 4, Austin TX 78759");
			else 
			    rec.setAddress("12515, Research Blvd, Austin TX 78759");
			rec.setAccountNumber("" + i);
			rec.setAccountStatus("Enabled");
			Vector macaddresses = new Vector();
			macaddresses.add(new String("00:40:96:34:4B:67"));
			macaddresses.add(new String("00:40:96:34:4B:68"));
			rec.setMacAddressList(macaddresses);
			Vector numbers = new Vector();
			numbers.add(new String("111 111 11" + i + "1"));
			numbers.add(new String("111 111 11" + i + "2"));
			numbers.add(new String("111 111 11" + i + "3"));
			numbers.add(new String("111 111 11" + i + "4"));
			rec.setPhoneNumbers(numbers);
			Vector services = new Vector();
			services.add(new String("Gold"));
			services.add(new String("Voice 64K"));
			services.add(new String("Data 10M"));
			rec.setSubscriberServices(services);
			rec.setSubscriptionLevel("3");
			subscriberList.add(rec);
		}
	}
*/

}

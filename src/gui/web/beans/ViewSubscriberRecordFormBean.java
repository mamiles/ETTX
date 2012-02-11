package com.cisco.ettx.admin.gui.web.beans;

import org.apache.struts.action.*;
import com.cisco.ettx.admin.gui.web.datatypes.SubscriberRecord;

public final class ViewSubscriberRecordFormBean  extends ActionForm {

    private SubscriberRecord subscriberRecord = null;

    public ViewSubscriberRecordFormBean()  {
    }
    
/*
    public String getSubscriberName() {
	return subscriberRecord.getSubscriberName();
    }

    public String getLoginID() {
	return subscriberRecord.getLoginID();
    }

    public String getAccountNumber() {
	return subscriberRecord.getAccountNumber();
    }

    public String getAccountStatus() {
	return subscriberRecord.getAccountStatus();
    }

    public String getAddress() {
	return subscriberRecord.getAddress();
    }

    public String getPhoneNumberText() {
	return subscriberRecord.getPhoneNumberText();
    }

    public String getSubscriptionLevel() {
	return subscriberRecord.getSubscriptionLevel();
    }

    public String getSubscriberServicesText() {
	return subscriberRecord.getSubscriberServicesText();
    }

    public String getMacAddressText() {
	return subscriberRecord.getMacAddressText();
    }

    public String getIpAddressText() {
	return subscriberRecord.getIpAddressText();
    }
*/

    public SubscriberRecord getSubscriberRecord() {
	return subscriberRecord;
    }

    public void setSubscriberRecord(SubscriberRecord rec) {
	subscriberRecord =  rec;
    }

}

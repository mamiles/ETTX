package com.cisco.ettx.admin.gui.web.beans;

import java.util.*;
import org.apache.struts.action.*;
import com.cisco.ettx.admin.gui.web.datatypes.LeaseHistoryRecord;

public final class LeaseHistoryListFormBean  extends ActionForm {
    Collection leaseHistoryList = new LinkedList();

    public LeaseHistoryListFormBean()  {
	//createLeaseHistoryRecords();
    }

    public Collection getLeaseHistoryList() {
	return leaseHistoryList;
    }

    public void setLeaseHistoryList(Collection list) {
	leaseHistoryList = list;
    }

/*
    private void createLeaseHistoryRecords() {
	for (int i = 0; i < 10; i++) {
	    Date date;
	    String subscriberId = new String ("Subscriber" + (i%3));
	    System.out.println("Create Lease History Record: " + subscriberId);
	    LeaseHistoryRecord rec = new LeaseHistoryRecord(subscriberId);
	    rec.setSubscriberName(subscriberId);
	    rec.setDevicePort (Integer.toString(i));
	    rec.setIpAddress ("172.21.177." + i);
	    date = new Date();
	    date.setMonth (i);
	    rec.setStartDate (date);
	    date = new Date();
	    date.setMonth (i + 2);
	    rec.setEndDate (date);

	    leaseHistoryList.add (rec);
	}
    }
*/

}

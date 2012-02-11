package com.cisco.ettx.admin.gui.web.beans;

import org.apache.struts.action.*;
import com.cisco.ettx.admin.gui.web.datatypes.SubscriberRecord;

public final class SubscriberInfoFormBean  extends ActionForm {

    private SubscriberRecord subscriberRecord = null;

    public SubscriberInfoFormBean()  {
    }
    
    public SubscriberRecord getSubscriberRecord() {
	return subscriberRecord;
    }

    public void setSubscriberRecord(SubscriberRecord rec) {
	subscriberRecord =  rec;
    }
}

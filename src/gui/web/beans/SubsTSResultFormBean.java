
package com.cisco.ettx.admin.gui.web.beans;

import java.util.Vector;
import org.apache.struts.action.*;
import com.cisco.ettx.admin.gui.web.datatypes.TroubleshootRecord;

public final class SubsTSResultFormBean  extends ActionForm {
	public TroubleshootRecord subsRecord = null;

	public SubsTSResultFormBean()  {
	}

	public TroubleshootRecord getSubscriberRecord() {
		return subsRecord;
	}

	public void setSubscriberRecord(TroubleshootRecord lrec) {
		subsRecord = lrec;
	}

	public String getSwitchIPAddress() {
		return subsRecord.getSwitchIPAddress(); //Need for telnet window
	}

}

package com.cisco.ettx.admin.gui.web.beans;

import org.apache.struts.action.*;
import com.cisco.ettx.admin.common.SubsDeviceInfo;
import java.util.Vector;

public final class SubsDevicesFormBean  extends ActionForm {

	public String subscriberID = null;
	public String subscriberName = null;
    private Vector subsDevices = null;
    private SubsDeviceInfo curntDevice = null;
    private String selectedSubsDevice = null;
	boolean modify = false;
/*
	boolean prevStatus = false;
*/

    public SubsDevicesFormBean()  {
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

	public void setSubscriberName(String lid) {
		subscriberName = lid;
	}
   
    
    public Vector getSubsDevices() {
	return subsDevices;
    }

    public void setSubsDevices(Vector list) {
	subsDevices =  list;
    }

	public void setSelectedSubsDevice(String ldevice) {
		selectedSubsDevice = ldevice;
		curntDevice = null;
		for (int i = 0; i < subsDevices.size(); i++) {
			SubsDeviceInfo temp = (SubsDeviceInfo)subsDevices.elementAt(i);
			if (temp.getMacAddress().equals(ldevice)) {
				setCurntDevice(temp);
				break;
			}
		}
	}

	public String getSelectedSubsDevice() {
		return selectedSubsDevice;
	}

	public void setCurntDevice(SubsDeviceInfo ldevice) {
		curntDevice = ldevice;
		//prevStatus = ldevice.getActiveStatus();
	}

	public SubsDeviceInfo getCurntDevice() {
		return curntDevice;
	}

	public boolean modify() {
		return modify;
	}

	public void setModify(boolean modify) {
		this.modify = modify;
	}


}

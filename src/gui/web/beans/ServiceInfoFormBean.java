package com.cisco.ettx.admin.gui.web.beans;

import org.apache.struts.action.*;
import com.cisco.ettx.admin.gui.web.datatypes.ServiceFeatureList;
import com.cisco.ettx.admin.gui.web.datatypes.ServiceFeature;

public final class ServiceInfoFormBean  extends ActionForm {

    private ServiceFeatureList service  = null;
    private String subscriberID  = null;

    public ServiceInfoFormBean()  {
    }
    
    public ServiceFeatureList getService() {
		return service;
    }

    public void setService(ServiceFeatureList rec) {
	service =  rec;
    }
    
    public String getSubscriberID() {
		return subscriberID;
    }

    public void setSubscriberID(String id) {
	subscriberID =  id;
    }

}

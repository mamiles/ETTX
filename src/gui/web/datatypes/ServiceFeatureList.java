
package com.cisco.ettx.admin.gui.web.datatypes;

import java.util.Vector;
import java.beans.Beans;
import java.io.Serializable;

// The class should implement Serializable to prevent tomcat
// from casting an exception on startup.
public class ServiceFeatureList extends Beans implements Serializable {

	public String serviceName = null;
	public Vector featureList = new Vector();
	
	public ServiceFeatureList() {
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String lid) {
		serviceName = lid;
	}

	public Vector getFeatureList() {
		return featureList;
	}

	public void setFeatureList(Vector lvalue) {
		featureList = lvalue;
	}

	public ServiceFeature[] getFeatureArray() {
		ServiceFeature[] featureArray = new ServiceFeature[featureList.size()];
		for (int i = 0; i < featureArray.length; i++) {
			featureArray[i] = (ServiceFeature)featureList.elementAt(i);
		}
		return featureArray;
	}
}

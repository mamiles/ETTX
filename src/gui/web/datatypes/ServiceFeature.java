
package com.cisco.ettx.admin.gui.web.datatypes;

import java.util.Vector;
import java.beans.Beans;
import java.io.Serializable;

// The class should implement Serializable to prevent tomcat
// from casting an exception on startup.
public class ServiceFeature extends Beans implements Serializable {

	public String featureName = null;
	public String featureValue = null;
	public String defaultValue = null;
	public Vector allowedValues = new Vector();
	
	public ServiceFeature() {
	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String lid) {
		featureName = lid;
	}

	public String getFeatureValue() {
		return featureValue;
	}

	public void setFeatureValue(String lvalue) {
		featureValue = lvalue;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String lvalue) {
		defaultValue = lvalue;
	}

	public Vector getAllowedValues() {
		return allowedValues;
	}

	public void setAllowedValues(Vector lvalue) {
		allowedValues = lvalue;
	}
}

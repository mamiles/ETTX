package com.cisco.ettx.admin.common;

import java.util.Vector;
import java.beans.Beans;

public class ToolbarElement extends Beans{
	public String displayName = new String();
	public String urlLink = new String();


	public ToolbarElement() {
	}

	public ToolbarElement(String lname,String llink) {
		displayName = lname;
		urlLink = llink;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String lname) {
		displayName = lname;
	}

	public String getUrlLink() {
		return urlLink;
	}

	public void setUrlLink(String llink) {
		urlLink = llink;
	}

	public static ToolbarElement[] toArray(Vector elems) {
		if (elems.size() == 0) {
			return new ToolbarElement[] {};
		}
		ToolbarElement[] array = new ToolbarElement[elems.size()];
		for (int i = 0; i < elems.size(); i++) {
			array[i] = (ToolbarElement)elems.elementAt(i);
		}
		return array;
	}
}

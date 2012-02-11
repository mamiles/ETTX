package com.cisco.ettx.admin.gui.web.beans;

import java.util.Vector;
import java.util.StringTokenizer;
import org.apache.struts.action.*;
import com.cisco.ettx.admin.common.ToolbarElement;

public final class ToolbarFormBean  extends ActionForm {
	Vector toolbarList = new Vector();
	ToolbarElement selectedURL = null;
	String selectedURLLink = null;
	String displayName = null;
	String urlLink = null;

	public ToolbarFormBean() {
	}

	public void setToolbarList(Vector list) {
		toolbarList = list;
	}

	public Vector getToolbarList() {
		return toolbarList;
	}

	public void setDisplayName(String lname) {
		displayName = lname;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setUrlLink(String llink) {
		urlLink  = llink;
	}

	public String getUrlLink() {
		return urlLink;
	}

	public void setSelectedURLLink(String llink) {
		selectedURLLink = llink;
	}

	public String getSelectedURLLink() {
		return selectedURLLink;
	}

	public void setSelectedURL(ToolbarElement elem) {
		selectedURL = elem;
	}

	public ToolbarElement getSelectedURL() {
		return selectedURL;
	}

	public ToolbarElement getToolbarElement(String urlLink) {
		for (int i = 0; i < toolbarList.size(); i++) {
			ToolbarElement elem = (ToolbarElement)toolbarList.elementAt(i);
			if (elem.getUrlLink().equals(urlLink)) {
				return elem;
			}
		}
		return null;
	}


}

package com.cisco.ettx.admin.gui.web.beans;
//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
//**************************************************

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import java.util.TreeMap;

import java.util.ArrayList;
import com.cisco.ettx.admin.common.*;

/**
 *	A Simple Action Form with no other methods
 *	implemented.
 **/

public class ApplLogListActionFormBean extends org.apache.struts.action.ActionForm
{

 /* private String applNameRtr;
  private String hostNameRtr;
  private String logPathRtr;
  private String logNameRtr;

  public String getRtrApplName() {
    return applNameRtr;
  }
  public void setRtrApplName(String strApplName) {
    applNameRtr = strApplName;
  }
  public String getRtrHostName() {
    return hostNameRtr;
  }
  public void setRtrHostName(String strHostName) {
    hostNameRtr = strHostName;
  }
  public String getRtrLogPath() {
    return logPathRtr;
  }
  public void setRtrLogPath(String strLogPath) {
    logPathRtr = strLogPath;
  }
  public String getRtrLogName() {
    return logNameRtr;
  }
  public void setRtrLogName(String strLogName) {
    logNameRtr = strLogName;
  }
*/
  private String _strApplLogSelection = "";
  public void setApplLog(String strSelection)
  {
    _strApplLogSelection = strSelection;
  }
  
  private TreeMap applLogListValue;
  public void setApplLogList(TreeMap applLogList) {
  	applLogListValue = applLogList;
  }
  public TreeMap getApplLogList() {
  	return applLogListValue;
  }
  
  public String getApplLog()
  {
    return _strApplLogSelection;
  }

  private String _strSortSelection = "";
  public void setSort(String strSelection)
  {
    _strSortSelection = strSelection;
  }//end method setSort
  
  public String getSort()
  {
    return _strSortSelection;
  }//end method getSort
  
}

//**************************************************
// Copyright (c) 2001, 2002 Cisco Systems, Inc.
// All rights reserved.
//**************************************************
// Author: Marvin Miles

package com.cisco.ettx.admin.common;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

public final class ApplLog
{

  /**********************************
   * Data Members
   **********************************/
  private String applName;
  private String labelName;
  private String hostName;
  private String logPath;
  private String logName;
  private String logSize;
  private String dateTime;


  /**********************************
   * Constructors
   **********************************/
  public ApplLog(String strApplName, String strHostName, String strLogPath, String strLogName, String strLogSize, String strDateTime)
  {
    applName = strApplName;
    hostName = strHostName;
    logPath = strLogPath;
    logName = strLogName;
    logSize = strLogSize;
    dateTime = strDateTime;

   // System.out.println("Constructor ApplLog "+ applName+ hostName+ logPath+ logName+ logSize+ dateTime);

  }//end constructor

  public ApplLog() {
		applName = null;
		hostName = null;
		logPath = null;
		logName = null;
		logSize = null;
		dateTime = null;
  }

  public String toString()
  {
    return (applName+ hostName+ logPath+ logName+ logSize+ dateTime);
  }

  /**********************************
   * Public Methods
   **********************************/
  // Appl Name
  public String getApplName()
  {
    return applName;
  }

  public void setApplName(String strApplName)
  {
    applName = strApplName;
  }

  public String getLabelName()
  {
	return labelName;
  }

  public void setLabelName(String strLabelName)
  {
	labelName = strLabelName;
  }


  // Host Name
  public String getHostName()
  {
    return hostName;
  }

  public void setHostName(String strHostName)
  {
    hostName = strHostName;
  }

  // Log Path
  public String getLogPath()
  {
    return logPath;
  }

  public void setLogPath(String strLogPath)
  {
    logPath = strLogPath;
  }

  // Log Name
  public String getLogName()
  {
    return logName;
  }

  public void setLogName(String strLogName)
  {
    logName = strLogName;
  }

  // Log Size
  public String getLogSize()
  {
    return logSize;
  }

  public void setLogSize(String strLogSize)
  {
    logSize = strLogSize;
  }

  // Date Time
  public String getDateTime()
  {
    return dateTime;
  }

  public void setDateTime(String strDateTime)
  {
    dateTime = strDateTime;
  }
}// enc class ApplLog

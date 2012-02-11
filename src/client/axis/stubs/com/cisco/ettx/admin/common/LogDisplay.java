/**
 * LogDisplay.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.cisco.ettx.admin.common;

public interface LogDisplay extends java.rmi.Remote {
    public com.cisco.ettx.admin.common.ApplLog[] getLogFilenames(java.lang.String sessionID, com.cisco.ettx.admin.common.SMSComponent[] comps, java.lang.String filter) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesException;
    public java.lang.String getLogFile(java.lang.String sessionID, com.cisco.ettx.admin.common.ApplLog logDesc) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesException;
}

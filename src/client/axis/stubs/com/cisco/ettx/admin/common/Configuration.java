/**
 * Configuration.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.cisco.ettx.admin.common;

public interface Configuration extends java.rmi.Remote {
    public com.cisco.ettx.admin.common.SystemConfiguration getSystemConfig(java.lang.String sessionID) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesException;
    public void setSystemConfig(java.lang.String sessionID, com.cisco.ettx.admin.common.SystemConfiguration config) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesException;
    public com.cisco.ettx.admin.common.ToolbarElement[] getToolbarElements(java.lang.String sessionID) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesException;
    public void setToolbarElements(java.lang.String sessionID, com.cisco.ettx.admin.common.ToolbarElement[] elements) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesException;
    public void addToolbarElement(java.lang.String sessionID, java.lang.String name, java.lang.String url) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesException;
    public void changeToolbarElement(java.lang.String sessionID, java.lang.String oldUrl, java.lang.String name, java.lang.String url) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesException;
    public void deleteToolbarElement(java.lang.String sessionID, java.lang.String url) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesException;
    public com.cisco.ettx.admin.common.SMSComponent getCompConfig(java.lang.String sessionID, java.lang.String compname) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesException;
    public void setCompConfig(java.lang.String sessionID, com.cisco.ettx.admin.common.SMSComponent comp) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesException;
    public com.cisco.ettx.admin.common.SMSComponent[] getAllComponents(java.lang.String sessionID) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesException;
    public void changeCompHost(java.lang.String sessionID, java.lang.String compName, java.lang.String oldHostName, com.cisco.ettx.admin.common.ComponentHostData compHost) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesException;
    public void deleteCompHost(java.lang.String sessionID, java.lang.String compName, java.lang.String hostName) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesException;
}

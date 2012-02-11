/**
 * Authenticate.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.cisco.ettx.admin.common;

public interface Authenticate extends java.rmi.Remote {
    public java.lang.String login(java.lang.String userID, java.lang.String password) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesException;
    public void logout(java.lang.String sessionID) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesException;
    public boolean checkPermission(java.lang.String sessionID, java.lang.String[] taskIDList) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesException;
}

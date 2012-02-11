/**
 * DataCollection.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.cisco.ettx.admin.common;

public interface DataCollection extends java.rmi.Remote {
    public java.util.HashMap executeTask(java.lang.String sessionID, java.lang.String taskname, java.util.HashMap attrs, int numrecords) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesNotification;
    public com.cisco.ettx.admin.common.AttrResult iterateRecords(java.lang.String sessionID, java.lang.String token, int numrecords) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesException;
}

/**
 * LeaseHistoryService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.cisco.ettx.admin.common;

public interface LeaseHistoryService extends java.rmi.Remote {
    public java.lang.String runLeaseHistoryArchive(java.lang.String sessionID, java.lang.String cnrHost, java.util.Date startDate, java.util.Date endDate, java.lang.String archiveFile) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesException;
}

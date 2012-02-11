/**
 * AdminServices.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.cisco.ettx.admin.common;

public interface AdminServices extends javax.xml.rpc.Service {
    public java.lang.String getDataCollectionAddress();

    public com.cisco.ettx.admin.common.DataCollection getDataCollection() throws javax.xml.rpc.ServiceException;

    public com.cisco.ettx.admin.common.DataCollection getDataCollection(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
    public java.lang.String getConfigurationAddress();

    public com.cisco.ettx.admin.common.Configuration getConfiguration() throws javax.xml.rpc.ServiceException;

    public com.cisco.ettx.admin.common.Configuration getConfiguration(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
    public java.lang.String getLeaseHistoryServiceAddress();

    public com.cisco.ettx.admin.common.LeaseHistoryService getLeaseHistoryService() throws javax.xml.rpc.ServiceException;

    public com.cisco.ettx.admin.common.LeaseHistoryService getLeaseHistoryService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
    public java.lang.String getLogDisplayAddress();

    public com.cisco.ettx.admin.common.LogDisplay getLogDisplay() throws javax.xml.rpc.ServiceException;

    public com.cisco.ettx.admin.common.LogDisplay getLogDisplay(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
    public java.lang.String getAuthenticateAddress();

    public com.cisco.ettx.admin.common.Authenticate getAuthenticate() throws javax.xml.rpc.ServiceException;

    public com.cisco.ettx.admin.common.Authenticate getAuthenticate(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}

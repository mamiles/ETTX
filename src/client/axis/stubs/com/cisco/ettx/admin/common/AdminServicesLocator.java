/**
 * AdminServicesLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.cisco.ettx.admin.common;

public class AdminServicesLocator extends org.apache.axis.client.Service implements com.cisco.ettx.admin.common.AdminServices {

    // Use to get a proxy class for DataCollection
    private final java.lang.String DataCollection_address = "http://www.cisco.com/ettx_admin_server/services/adminService";

    public java.lang.String getDataCollectionAddress() {
        return DataCollection_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String DataCollectionWSDDServiceName = "DataCollection";

    public java.lang.String getDataCollectionWSDDServiceName() {
        return DataCollectionWSDDServiceName;
    }

    public void setDataCollectionWSDDServiceName(java.lang.String name) {
        DataCollectionWSDDServiceName = name;
    }

    public com.cisco.ettx.admin.common.DataCollection getDataCollection() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(DataCollection_address);
        }
        catch (java.net.MalformedURLException e) {
            return null; // unlikely as URL was validated in WSDL2Java
        }
        return getDataCollection(endpoint);
    }

    public com.cisco.ettx.admin.common.DataCollection getDataCollection(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.cisco.ettx.admin.common.DataCollectionBindingStub _stub = new com.cisco.ettx.admin.common.DataCollectionBindingStub(portAddress, this);
            _stub.setPortName(getDataCollectionWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }


    // Use to get a proxy class for Configuration
    private final java.lang.String Configuration_address = "http://www.cisco.com/ettx_admin_server/services/adminService";

    public java.lang.String getConfigurationAddress() {
        return Configuration_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ConfigurationWSDDServiceName = "Configuration";

    public java.lang.String getConfigurationWSDDServiceName() {
        return ConfigurationWSDDServiceName;
    }

    public void setConfigurationWSDDServiceName(java.lang.String name) {
        ConfigurationWSDDServiceName = name;
    }

    public com.cisco.ettx.admin.common.Configuration getConfiguration() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(Configuration_address);
        }
        catch (java.net.MalformedURLException e) {
            return null; // unlikely as URL was validated in WSDL2Java
        }
        return getConfiguration(endpoint);
    }

    public com.cisco.ettx.admin.common.Configuration getConfiguration(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.cisco.ettx.admin.common.ConfigurationBindingStub _stub = new com.cisco.ettx.admin.common.ConfigurationBindingStub(portAddress, this);
            _stub.setPortName(getConfigurationWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }


    // Use to get a proxy class for LeaseHistoryService
    private final java.lang.String LeaseHistoryService_address = "http://www.cisco.com/ettx_admin_server/services/adminService";

    public java.lang.String getLeaseHistoryServiceAddress() {
        return LeaseHistoryService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String LeaseHistoryServiceWSDDServiceName = "LeaseHistoryService";

    public java.lang.String getLeaseHistoryServiceWSDDServiceName() {
        return LeaseHistoryServiceWSDDServiceName;
    }

    public void setLeaseHistoryServiceWSDDServiceName(java.lang.String name) {
        LeaseHistoryServiceWSDDServiceName = name;
    }

    public com.cisco.ettx.admin.common.LeaseHistoryService getLeaseHistoryService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(LeaseHistoryService_address);
        }
        catch (java.net.MalformedURLException e) {
            return null; // unlikely as URL was validated in WSDL2Java
        }
        return getLeaseHistoryService(endpoint);
    }

    public com.cisco.ettx.admin.common.LeaseHistoryService getLeaseHistoryService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.cisco.ettx.admin.common.LeaseHistoryServiceBindingStub _stub = new com.cisco.ettx.admin.common.LeaseHistoryServiceBindingStub(portAddress, this);
            _stub.setPortName(getLeaseHistoryServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }


    // Use to get a proxy class for LogDisplay
    private final java.lang.String LogDisplay_address = "http://www.cisco.com/ettx_admin_server/services/adminService";

    public java.lang.String getLogDisplayAddress() {
        return LogDisplay_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String LogDisplayWSDDServiceName = "LogDisplay";

    public java.lang.String getLogDisplayWSDDServiceName() {
        return LogDisplayWSDDServiceName;
    }

    public void setLogDisplayWSDDServiceName(java.lang.String name) {
        LogDisplayWSDDServiceName = name;
    }

    public com.cisco.ettx.admin.common.LogDisplay getLogDisplay() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(LogDisplay_address);
        }
        catch (java.net.MalformedURLException e) {
            return null; // unlikely as URL was validated in WSDL2Java
        }
        return getLogDisplay(endpoint);
    }

    public com.cisco.ettx.admin.common.LogDisplay getLogDisplay(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.cisco.ettx.admin.common.LogDisplayBindingStub _stub = new com.cisco.ettx.admin.common.LogDisplayBindingStub(portAddress, this);
            _stub.setPortName(getLogDisplayWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }


    // Use to get a proxy class for Authenticate
    private final java.lang.String Authenticate_address = "http://www.cisco.com/ettx_admin_server/services/adminService";

    public java.lang.String getAuthenticateAddress() {
        return Authenticate_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String AuthenticateWSDDServiceName = "Authenticate";

    public java.lang.String getAuthenticateWSDDServiceName() {
        return AuthenticateWSDDServiceName;
    }

    public void setAuthenticateWSDDServiceName(java.lang.String name) {
        AuthenticateWSDDServiceName = name;
    }

    public com.cisco.ettx.admin.common.Authenticate getAuthenticate() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(Authenticate_address);
        }
        catch (java.net.MalformedURLException e) {
            return null; // unlikely as URL was validated in WSDL2Java
        }
        return getAuthenticate(endpoint);
    }

    public com.cisco.ettx.admin.common.Authenticate getAuthenticate(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.cisco.ettx.admin.common.AuthenticateBindingStub _stub = new com.cisco.ettx.admin.common.AuthenticateBindingStub(portAddress, this);
            _stub.setPortName(getAuthenticateWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.cisco.ettx.admin.common.DataCollection.class.isAssignableFrom(serviceEndpointInterface)) {
                com.cisco.ettx.admin.common.DataCollectionBindingStub _stub = new com.cisco.ettx.admin.common.DataCollectionBindingStub(new java.net.URL(DataCollection_address), this);
                _stub.setPortName(getDataCollectionWSDDServiceName());
                return _stub;
            }
            if (com.cisco.ettx.admin.common.Configuration.class.isAssignableFrom(serviceEndpointInterface)) {
                com.cisco.ettx.admin.common.ConfigurationBindingStub _stub = new com.cisco.ettx.admin.common.ConfigurationBindingStub(new java.net.URL(Configuration_address), this);
                _stub.setPortName(getConfigurationWSDDServiceName());
                return _stub;
            }
            if (com.cisco.ettx.admin.common.LeaseHistoryService.class.isAssignableFrom(serviceEndpointInterface)) {
                com.cisco.ettx.admin.common.LeaseHistoryServiceBindingStub _stub = new com.cisco.ettx.admin.common.LeaseHistoryServiceBindingStub(new java.net.URL(LeaseHistoryService_address), this);
                _stub.setPortName(getLeaseHistoryServiceWSDDServiceName());
                return _stub;
            }
            if (com.cisco.ettx.admin.common.LogDisplay.class.isAssignableFrom(serviceEndpointInterface)) {
                com.cisco.ettx.admin.common.LogDisplayBindingStub _stub = new com.cisco.ettx.admin.common.LogDisplayBindingStub(new java.net.URL(LogDisplay_address), this);
                _stub.setPortName(getLogDisplayWSDDServiceName());
                return _stub;
            }
            if (com.cisco.ettx.admin.common.Authenticate.class.isAssignableFrom(serviceEndpointInterface)) {
                com.cisco.ettx.admin.common.AuthenticateBindingStub _stub = new com.cisco.ettx.admin.common.AuthenticateBindingStub(new java.net.URL(Authenticate_address), this);
                _stub.setPortName(getAuthenticateWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        java.rmi.Remote _stub = getPort(serviceEndpointInterface);
        ((org.apache.axis.client.Stub) _stub).setPortName(portName);
        return _stub;
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:adminServices", "AdminServices");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("DataCollection"));
            ports.add(new javax.xml.namespace.QName("Configuration"));
            ports.add(new javax.xml.namespace.QName("LeaseHistoryService"));
            ports.add(new javax.xml.namespace.QName("LogDisplay"));
            ports.add(new javax.xml.namespace.QName("Authenticate"));
        }
        return ports.iterator();
    }

}

/*
 * SwitchBean.java
 *
 * Created on 29 April 2003, 18:48
 */

package com.cisco.ettx.provisioning.action;

import java.beans.*;

/**
 * Information for the view about a Switch within the system. This is populated
 * from a com.cisco.sesm.sms.types.SwitchInfo so closely resembles it in 
 * structure
 *
 * @author  pcurren
 */
public class ETTXSwitchBean extends Object implements java.io.Serializable {
    private String ipAddress = null;
    private String hostName = null;
    private String fqdn = null;
    private String locationName = null;
    private Boolean useSsh = null;
    private Boolean ipAutoAllocate = null;
    private String subnetMask = null;
    private String technology = null;
    private String vendorName = null;
    private String model = null;
    private String type = null;
    private String subType = null;
    private String connectionId = null;
    private String dhcpServer = null;
    private String telnetGateway = null;
    private String configRegistrar = null;
    private String cnoteServer = null;
    private String terminalServer = null;
    private Integer terminalServerPortNumber = null;
    private String configDownloadMethod = null;
    private String edgeDeviceId = null;
    private String macAddress = null;
    private String[] interfaceTypes = null;
    private String managementVlanId = null;
    private String id = null;
    
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Returns the edgeDeviceId.
     * @return String
     */
    public String getEdgeDeviceId() {
        return edgeDeviceId;
    }

    /**
     * Returns the macAddress.
     * @return String
     */
    public String getMacAddress() {
        return macAddress;
    }

    /**
     * Sets the edgeDeviceId.
     * @param edgeDeviceId The edgeDeviceId to set
     */
    public void setEdgeDeviceId(String edgeDeviceId) {
        this.edgeDeviceId = edgeDeviceId;
    }

    /**
     * Sets the macAddress.
     * @param macAddress The macAddress to set
     */
    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String[] getInterfaceTypes() {
	return this.interfaceTypes;
    }

    public void setInterfaceTypes(String[] interfaceTypes) {
	this.interfaceTypes = interfaceTypes;
    }

    public String getManagementVlanId() {
	return this.managementVlanId;
    }

    public void setManagementVlanId(String managementVlanId) {
	this.managementVlanId = managementVlanId;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getConnectionId() {
        return this.connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    /**
     * Returns the fqdn.
     * @return String
     */
    public String getFqdn() {
        return fqdn;
    }

    /**
     * Returns the hostName.
     * @return String
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * Returns the locationName.
     * @return String
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * Sets the fqdn.
     * @param fqdn The fqdn to set
     */
    public void setFqdn(String fqdn) {
        this.fqdn = fqdn;
    }

    /**
     * Sets the hostName.
     * @param hostName The hostName to set
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * Sets the locationName.
     * @param locationName The locationName to set
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    /**
     * Returns the ipAutoAllocate.
     * @return Boolean
     */
    public Boolean getIpAutoAllocate() {
        return ipAutoAllocate;
    }

    /**
     * Returns the model.
     * @return String
     */
    public String getModel() {
        return model;
    }

    /**
     * Returns the subnetMask.
     * @return String
     */
    public String getSubnetMask() {
        return subnetMask;
    }

    /**
     * Returns the subType.
     * @return String
     */
    public String getSubType() {
        return subType;
    }

    /**
     * Returns the technology.
     * @return String
     */
    public String getTechnology() {
        return technology;
    }

    /**
     * Returns the type.
     * @return String
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the useSsh.
     * @return Boolean
     */
    public Boolean getUseSsh() {
        return useSsh;
    }

    /**
     * Returns the vendorName.
     * @return String
     */
    public String getVendorName() {
        return vendorName;
    }

    /**
     * Sets the ipAutoAllocate.
     * @param ipAutoAllocate The ipAutoAllocate to set
     */
    public void setIpAutoAllocate(Boolean ipAutoAllocate) {
        this.ipAutoAllocate = ipAutoAllocate;
    }

    /**
     * Sets the model.
     * @param model The model to set
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Sets the subnetMask.
     * @param subnetMask The subnetMask to set
     */
    public void setSubnetMask(String subnetMask) {
        this.subnetMask = subnetMask;
    }

    /**
     * Sets the subType.
     * @param subType The subType to set
     */
    public void setSubType(String subType) {
        this.subType = subType;
    }

    /**
     * Sets the technology.
     * @param technology The technology to set
     */
    public void setTechnology(String technology) {
        this.technology = technology;
    }

    /**
     * Sets the type.
     * @param type The type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Sets the useSsh.
     * @param useSsh The useSsh to set
     */
    public void setUseSsh(Boolean useSsh) {
        this.useSsh = useSsh;
    }

    /**
     * Sets the vendorName.
     * @param vendorName The vendorName to set
     */
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getDhcpServer() {
	return this.dhcpServer;
    }

    public void setDhcpServer(String dhcpServer) {
	this.dhcpServer = dhcpServer;
    }

    public String getTelnetGateway() {
	return this.telnetGateway;
    }

    public void setTelnetGateway(String telnetGateway) {
	this.telnetGateway = telnetGateway;
    }

    public String getConfigRegistrar() {
	return this.configRegistrar;
    }

    public void setConfigRegistrar(String configRegistrar) {
	this.configRegistrar = configRegistrar;
    }

    public String getCnoteServer() {
	return this.cnoteServer;
    }

    public void setCnoteServer(String cnoteServer) {
	this.cnoteServer = cnoteServer;
    }

    public String getTerminalServer() {
	return this.terminalServer;
    }

    public void setTerminalServer(String terminalServer) {
	this.terminalServer = terminalServer;
    }

    public Integer getTerminalServerPortNumber() {
	return this.terminalServerPortNumber;
    }

    public void setTerminalServerPortNumber(Integer terminalServerPortNumber) {
	this.terminalServerPortNumber = terminalServerPortNumber;
    }

    /**
     * Returns the configDownloadMethod attribute
     * @return the value as a String
     */
    public String getConfigDownloadMethod() {
	return this.configDownloadMethod;
    }

    /**
     * Sets the configDownloadMethod attribute
     * @param configDownloadMethod the new value
     */
    public void setConfigDownloadMethod(String configDownloadMethod) {
	this.configDownloadMethod = configDownloadMethod;
    }
        
}

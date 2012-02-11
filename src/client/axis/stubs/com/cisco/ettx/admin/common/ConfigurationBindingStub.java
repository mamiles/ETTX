/**
 * ConfigurationBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.cisco.ettx.admin.common;

public class ConfigurationBindingStub extends org.apache.axis.client.Stub implements com.cisco.ettx.admin.common.Configuration {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    public ConfigurationBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public ConfigurationBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public ConfigurationBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("urn:BeanService", "ToolbarElement");
            cachedSerQNames.add(qName);
            cls = com.cisco.ettx.admin.common.ToolbarElement.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:adminServices", "ArrayOfSMSComponent");
            cachedSerQNames.add(qName);
            cls = com.cisco.ettx.admin.common.SMSComponent[].class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(arraysf);
            cachedDeserFactories.add(arraydf);

            qName = new javax.xml.namespace.QName("urn:BeanService", "AdminServicesException");
            cachedSerQNames.add(qName);
            cls = com.cisco.ettx.admin.common.AdminServicesException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:BeanService", "ComponentHostData");
            cachedSerQNames.add(qName);
            cls = com.cisco.ettx.admin.common.ComponentHostData.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:BeanService", "SMSComponent");
            cachedSerQNames.add(qName);
            cls = com.cisco.ettx.admin.common.SMSComponent.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:adminServices", "ArrayOfToolbarElement");
            cachedSerQNames.add(qName);
            cls = com.cisco.ettx.admin.common.ToolbarElement[].class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(arraysf);
            cachedDeserFactories.add(arraydf);

            qName = new javax.xml.namespace.QName("urn:BeanService", "SystemConfiguration");
            cachedSerQNames.add(qName);
            cls = com.cisco.ettx.admin.common.SystemConfiguration.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    private org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call =
                    (org.apache.axis.client.Call) super.service.createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                if(_call.isPropertySupported(key))
                    _call.setProperty(key, super.cachedProperties.get(key));
                else
                    _call.setScopedProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(org.apache.axis.Constants.URI_SOAP11_ENC);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                        java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                        _call.registerTypeMapping(cls, qName, sf, df, false);
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", t);
        }
    }

    public com.cisco.ettx.admin.common.SystemConfiguration getSystemConfig(java.lang.String sessionID) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.addParameter(new javax.xml.namespace.QName("", "sessionID"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.setReturnType(new javax.xml.namespace.QName("urn:BeanService", "SystemConfiguration"), com.cisco.ettx.admin.common.SystemConfiguration.class);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("getSystemConfig");
        _call.setOperationStyle("rpc");
        _call.setOperationName(new javax.xml.namespace.QName("urn:systemConfig", "getSystemConfig"));

        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionID});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            try {
                return (com.cisco.ettx.admin.common.SystemConfiguration) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.cisco.ettx.admin.common.SystemConfiguration) org.apache.axis.utils.JavaUtils.convert(_resp, com.cisco.ettx.admin.common.SystemConfiguration.class);
            }
        }
    }

    public void setSystemConfig(java.lang.String sessionID, com.cisco.ettx.admin.common.SystemConfiguration config) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.addParameter(new javax.xml.namespace.QName("", "sessionID"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.addParameter(new javax.xml.namespace.QName("", "config"), new javax.xml.namespace.QName("urn:BeanService", "SystemConfiguration"), com.cisco.ettx.admin.common.SystemConfiguration.class, javax.xml.rpc.ParameterMode.IN);
        _call.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("setSystemConfig");
        _call.setOperationStyle("rpc");
        _call.setOperationName(new javax.xml.namespace.QName("urn:systemConfig", "setSystemConfig"));

        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionID, config});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
    }

    public com.cisco.ettx.admin.common.ToolbarElement[] getToolbarElements(java.lang.String sessionID) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.addParameter(new javax.xml.namespace.QName("", "sessionID"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.setReturnType(new javax.xml.namespace.QName("urn:adminServices", "ArrayOfToolbarElement"), com.cisco.ettx.admin.common.ToolbarElement[].class);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("getToolbarElements");
        _call.setOperationStyle("rpc");
        _call.setOperationName(new javax.xml.namespace.QName("urn:toolbarConfig", "getToolbarElements"));

        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionID});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            try {
                return (com.cisco.ettx.admin.common.ToolbarElement[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.cisco.ettx.admin.common.ToolbarElement[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.cisco.ettx.admin.common.ToolbarElement[].class);
            }
        }
    }

    public void setToolbarElements(java.lang.String sessionID, com.cisco.ettx.admin.common.ToolbarElement[] elements) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.addParameter(new javax.xml.namespace.QName("", "sessionID"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.addParameter(new javax.xml.namespace.QName("", "elements"), new javax.xml.namespace.QName("urn:adminServices", "ArrayOfToolbarElement"), com.cisco.ettx.admin.common.ToolbarElement[].class, javax.xml.rpc.ParameterMode.IN);
        _call.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("setToolbarElements");
        _call.setOperationStyle("rpc");
        _call.setOperationName(new javax.xml.namespace.QName("urn:toolbarConfig", "setToolbarElements"));

        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionID, elements});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
    }

    public void addToolbarElement(java.lang.String sessionID, java.lang.String name, java.lang.String url) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.addParameter(new javax.xml.namespace.QName("", "sessionID"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.addParameter(new javax.xml.namespace.QName("", "name"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.addParameter(new javax.xml.namespace.QName("", "url"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("addToolbarElement");
        _call.setOperationStyle("rpc");
        _call.setOperationName(new javax.xml.namespace.QName("urn:toolbarConfig", "addToolbarElement"));

        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionID, name, url});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
    }

    public void changeToolbarElement(java.lang.String sessionID, java.lang.String oldUrl, java.lang.String name, java.lang.String url) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.addParameter(new javax.xml.namespace.QName("", "sessionID"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.addParameter(new javax.xml.namespace.QName("", "oldUrl"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.addParameter(new javax.xml.namespace.QName("", "name"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.addParameter(new javax.xml.namespace.QName("", "url"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("changeToolbarElement");
        _call.setOperationStyle("rpc");
        _call.setOperationName(new javax.xml.namespace.QName("urn:toolbarConfig", "changeToolbarElement"));

        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionID, oldUrl, name, url});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
    }

    public void deleteToolbarElement(java.lang.String sessionID, java.lang.String url) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.addParameter(new javax.xml.namespace.QName("", "sessionID"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.addParameter(new javax.xml.namespace.QName("", "url"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("deleteToolbarElement");
        _call.setOperationStyle("rpc");
        _call.setOperationName(new javax.xml.namespace.QName("urn:toolbarConfig", "deleteToolbarElement"));

        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionID, url});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
    }

    public com.cisco.ettx.admin.common.SMSComponent getCompConfig(java.lang.String sessionID, java.lang.String compname) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.addParameter(new javax.xml.namespace.QName("", "sessionID"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.addParameter(new javax.xml.namespace.QName("", "compname"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.setReturnType(new javax.xml.namespace.QName("urn:BeanService", "SMSComponent"), com.cisco.ettx.admin.common.SMSComponent.class);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("getCompConfig");
        _call.setOperationStyle("rpc");
        _call.setOperationName(new javax.xml.namespace.QName("urn:compConfig", "getCompConfig"));

        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionID, compname});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            try {
                return (com.cisco.ettx.admin.common.SMSComponent) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.cisco.ettx.admin.common.SMSComponent) org.apache.axis.utils.JavaUtils.convert(_resp, com.cisco.ettx.admin.common.SMSComponent.class);
            }
        }
    }

    public void setCompConfig(java.lang.String sessionID, com.cisco.ettx.admin.common.SMSComponent comp) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.addParameter(new javax.xml.namespace.QName("", "sessionID"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.addParameter(new javax.xml.namespace.QName("", "comp"), new javax.xml.namespace.QName("urn:BeanService", "SMSComponent"), com.cisco.ettx.admin.common.SMSComponent.class, javax.xml.rpc.ParameterMode.IN);
        _call.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("setCompConfig");
        _call.setOperationStyle("rpc");
        _call.setOperationName(new javax.xml.namespace.QName("urn:compConfig", "setCompConfig"));

        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionID, comp});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
    }

    public com.cisco.ettx.admin.common.SMSComponent[] getAllComponents(java.lang.String sessionID) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.addParameter(new javax.xml.namespace.QName("", "sessionID"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.setReturnType(new javax.xml.namespace.QName("urn:adminServices", "ArrayOfSMSComponent"), com.cisco.ettx.admin.common.SMSComponent[].class);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("getAllComponents");
        _call.setOperationStyle("rpc");
        _call.setOperationName(new javax.xml.namespace.QName("urn:compConfig", "getAllComponents"));

        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionID});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            try {
                return (com.cisco.ettx.admin.common.SMSComponent[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.cisco.ettx.admin.common.SMSComponent[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.cisco.ettx.admin.common.SMSComponent[].class);
            }
        }
    }

    public void changeCompHost(java.lang.String sessionID, java.lang.String compName, java.lang.String oldHostName, com.cisco.ettx.admin.common.ComponentHostData compHost) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.addParameter(new javax.xml.namespace.QName("", "sessionID"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.addParameter(new javax.xml.namespace.QName("", "compName"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.addParameter(new javax.xml.namespace.QName("", "oldHostName"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.addParameter(new javax.xml.namespace.QName("", "compHost"), new javax.xml.namespace.QName("urn:BeanService", "ComponentHostData"), com.cisco.ettx.admin.common.ComponentHostData.class, javax.xml.rpc.ParameterMode.IN);
        _call.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("changeCompHost");
        _call.setOperationStyle("rpc");
        _call.setOperationName(new javax.xml.namespace.QName("urn:compConfig", "changeCompHost"));

        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionID, compName, oldHostName, compHost});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
    }

    public void deleteCompHost(java.lang.String sessionID, java.lang.String compName, java.lang.String hostName) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.addParameter(new javax.xml.namespace.QName("", "sessionID"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.addParameter(new javax.xml.namespace.QName("", "compName"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.addParameter(new javax.xml.namespace.QName("", "hostName"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("deleteCompHost");
        _call.setOperationStyle("rpc");
        _call.setOperationName(new javax.xml.namespace.QName("urn:compConfig", "deleteCompHost"));

        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionID, compName, hostName});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
    }

}

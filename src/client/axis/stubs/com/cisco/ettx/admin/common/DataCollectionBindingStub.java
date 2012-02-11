/**
 * DataCollectionBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.cisco.ettx.admin.common;

public class DataCollectionBindingStub extends org.apache.axis.client.Stub implements com.cisco.ettx.admin.common.DataCollection {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    public DataCollectionBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public DataCollectionBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public DataCollectionBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
            qName = new javax.xml.namespace.QName("urn:BeanService", "AdminServicesNotification");
            cachedSerQNames.add(qName);
            cls = com.cisco.ettx.admin.common.AdminServicesNotification.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:BeanService", "AdminServicesException");
            cachedSerQNames.add(qName);
            cls = com.cisco.ettx.admin.common.AdminServicesException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:BeanService", "AttrResult");
            cachedSerQNames.add(qName);
            cls = com.cisco.ettx.admin.common.AttrResult.class;
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

    public java.util.HashMap executeTask(java.lang.String sessionID, java.lang.String taskname, java.util.HashMap attrs, int numrecords) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesNotification {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.addParameter(new javax.xml.namespace.QName("", "sessionID"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.addParameter(new javax.xml.namespace.QName("", "taskname"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.addParameter(new javax.xml.namespace.QName("", "attrs"), new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Map"), java.util.HashMap.class, javax.xml.rpc.ParameterMode.IN);
        _call.addParameter(new javax.xml.namespace.QName("", "numrecords"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, javax.xml.rpc.ParameterMode.IN);
        _call.setReturnType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Map"), java.util.HashMap.class);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("executeTask");
        _call.setOperationStyle("rpc");
        _call.setOperationName(new javax.xml.namespace.QName("urn:dataCollection", "executeTask"));

        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionID, taskname, attrs, new java.lang.Integer(numrecords)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            try {
                return (java.util.HashMap) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.util.HashMap) org.apache.axis.utils.JavaUtils.convert(_resp, java.util.HashMap.class);
            }
        }
    }

    public com.cisco.ettx.admin.common.AttrResult iterateRecords(java.lang.String sessionID, java.lang.String token, int numrecords) throws java.rmi.RemoteException, com.cisco.ettx.admin.common.AdminServicesException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.addParameter(new javax.xml.namespace.QName("", "sessionID"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.addParameter(new javax.xml.namespace.QName("", "token"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, javax.xml.rpc.ParameterMode.IN);
        _call.addParameter(new javax.xml.namespace.QName("", "numrecords"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, javax.xml.rpc.ParameterMode.IN);
        _call.setReturnType(new javax.xml.namespace.QName("urn:BeanService", "AttrResult"), com.cisco.ettx.admin.common.AttrResult.class);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("iterateRecords");
        _call.setOperationStyle("rpc");
        _call.setOperationName(new javax.xml.namespace.QName("urn:dataCollection", "iterateRecords"));

        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionID, token, new java.lang.Integer(numrecords)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            try {
                return (com.cisco.ettx.admin.common.AttrResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.cisco.ettx.admin.common.AttrResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.cisco.ettx.admin.common.AttrResult.class);
            }
        }
    }

}

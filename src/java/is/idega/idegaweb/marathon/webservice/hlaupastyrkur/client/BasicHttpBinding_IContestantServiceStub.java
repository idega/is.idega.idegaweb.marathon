/**
 * BasicHttpBinding_IContestantServiceStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client;

public class BasicHttpBinding_IContestantServiceStub extends org.apache.axis.client.Stub implements is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.IContestantService {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[4];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("RegisterContestant");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "ContestantRequest"), is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ContestantRequest.class, false, false);
        param.setOmittable(true);
        param.setNillable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tmsoftware.is/Service/Faults/", "ServiceFault"),
                      "is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ServiceFault",
                      new javax.xml.namespace.QName("http://tmsoftware.is/Service/Faults/", "ServiceFault"), 
                      true
                     ));
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("UpdateContestant");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "UpdateContestantRequest"), is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.UpdateContestantRequest.class, false, false);
        param.setOmittable(true);
        param.setNillable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tmsoftware.is/Service/Faults/", "ServiceFault"),
                      "is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ServiceFault",
                      new javax.xml.namespace.QName("http://tmsoftware.is/Service/Faults/", "ServiceFault"), 
                      true
                     ));
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("RegisterTeam");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "TeamRequest"), is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.TeamRequest.class, false, false);
        param.setOmittable(true);
        param.setNillable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tmsoftware.is/Service/Faults/", "ServiceFault"),
                      "is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ServiceFault",
                      new javax.xml.namespace.QName("http://tmsoftware.is/Service/Faults/", "ServiceFault"), 
                      true
                     ));
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("UpdateTeam");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "UpdateTeamRequest"), is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.UpdateTeamRequest.class, false, false);
        param.setOmittable(true);
        param.setNillable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://tmsoftware.is/Service/Faults/", "ServiceFault"),
                      "is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ServiceFault",
                      new javax.xml.namespace.QName("http://tmsoftware.is/Service/Faults/", "ServiceFault"), 
                      true
                     ));
        _operations[3] = oper;

    }

    public BasicHttpBinding_IContestantServiceStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public BasicHttpBinding_IContestantServiceStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public BasicHttpBinding_IContestantServiceStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "ArrayOfTeamMember");
            cachedSerQNames.add(qName);
            cls = is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.TeamMember[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "TeamMember");
            qName2 = new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "TeamMember");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "BaseRequest");
            cachedSerQNames.add(qName);
            cls = is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.BaseRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "ContestantRequest");
            cachedSerQNames.add(qName);
            cls = is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ContestantRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "Login");
            cachedSerQNames.add(qName);
            cls = is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.Login.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "RegisterRequest");
            cachedSerQNames.add(qName);
            cls = is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.RegisterRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "TeamMember");
            cachedSerQNames.add(qName);
            cls = is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.TeamMember.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "TeamRequest");
            cachedSerQNames.add(qName);
            cls = is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.TeamRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "UpdateContestantRequest");
            cachedSerQNames.add(qName);
            cls = is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.UpdateContestantRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "UpdateRequest");
            cachedSerQNames.add(qName);
            cls = is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.UpdateRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "UpdateTeamRequest");
            cachedSerQNames.add(qName);
            cls = is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.UpdateTeamRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://tmsoftware.is/Service/Faults/", "ServiceException");
            cachedSerQNames.add(qName);
            cls = is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ServiceException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://tmsoftware.is/Service/Faults/", "ServiceFault");
            cachedSerQNames.add(qName);
            cls = is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ServiceFault.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
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
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public void registerContestant(is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ContestantRequest request) throws java.rmi.RemoteException, is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ServiceFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://tmsoftware.is/charities/services/IContestantService/RegisterContestant");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "RegisterContestant"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ServiceFault) {
              throw (is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ServiceFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void updateContestant(is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.UpdateContestantRequest request) throws java.rmi.RemoteException, is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ServiceFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://tmsoftware.is/charities/services/IContestantService/UpdateContestant");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "UpdateContestant"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ServiceFault) {
              throw (is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ServiceFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void registerTeam(is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.TeamRequest request) throws java.rmi.RemoteException, is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ServiceFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://tmsoftware.is/charities/services/IContestantService/RegisterTeam");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "RegisterTeam"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ServiceFault) {
              throw (is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ServiceFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void updateTeam(is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.UpdateTeamRequest request) throws java.rmi.RemoteException, is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ServiceFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://tmsoftware.is/charities/services/IContestantService/UpdateTeam");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "UpdateTeam"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ServiceFault) {
              throw (is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ServiceFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

}

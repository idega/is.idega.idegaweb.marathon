/**
 * ISBServiceSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.marathon.webservice.isb.server;

public class ISBServiceSoapBindingSkeleton implements is.idega.idegaweb.marathon.webservice.isb.server.ISBService_PortType, org.apache.axis.wsdl.Skeleton {
    private is.idega.idegaweb.marathon.webservice.isb.server.ISBService_PortType impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://illuminati.is", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://illuminati.is", "Login"), is.idega.idegaweb.marathon.webservice.isb.server.Login.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("authenticateUser", _params, new javax.xml.namespace.QName("http://illuminati.is", "authenticateUserReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://illuminati.is", "Session"));
        _oper.setElementQName(new javax.xml.namespace.QName("", "authenticateUser"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("authenticateUser") == null) {
            _myOperations.put("authenticateUser", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("authenticateUser")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://illuminati.is", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://illuminati.is", "RunnerInfo"), is.idega.idegaweb.marathon.webservice.isb.server.RunnerInfo.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("registerRunner", _params, new javax.xml.namespace.QName("http://illuminati.is", "registerRunnerReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        _oper.setElementQName(new javax.xml.namespace.QName("", "registerRunner"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("registerRunner") == null) {
            _myOperations.put("registerRunner", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("registerRunner")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("SessionTimedOutException");
        _fault.setQName(new javax.xml.namespace.QName("http://illuminati.is", "fault"));
        _fault.setClassName("is.idega.idegaweb.marathon.webservice.isb.server.SessionTimedOutException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://illuminati.is", "SessionTimedOutException"));
        _oper.addFault(_fault);
    }

    public ISBServiceSoapBindingSkeleton() {
        this.impl = new is.idega.idegaweb.marathon.webservice.isb.server.ISBServiceSoapBindingImpl();
    }

    public ISBServiceSoapBindingSkeleton(is.idega.idegaweb.marathon.webservice.isb.server.ISBService_PortType impl) {
        this.impl = impl;
    }
    public is.idega.idegaweb.marathon.webservice.isb.server.Session authenticateUser(is.idega.idegaweb.marathon.webservice.isb.server.Login in0) throws java.rmi.RemoteException
    {
        is.idega.idegaweb.marathon.webservice.isb.server.Session ret = impl.authenticateUser(in0);
        return ret;
    }

    public boolean registerRunner(is.idega.idegaweb.marathon.webservice.isb.server.RunnerInfo in0) throws java.rmi.RemoteException, is.idega.idegaweb.marathon.webservice.isb.server.SessionTimedOutException
    {
        boolean ret = impl.registerRunner(in0);
        return ret;
    }

}

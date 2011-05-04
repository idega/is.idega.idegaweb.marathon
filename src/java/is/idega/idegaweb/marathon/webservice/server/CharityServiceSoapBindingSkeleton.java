/**
 * CharityServiceSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.marathon.webservice.server;

public class CharityServiceSoapBindingSkeleton implements is.idega.idegaweb.marathon.webservice.server.CharityService_PortType, org.apache.axis.wsdl.Skeleton {
    private is.idega.idegaweb.marathon.webservice.server.CharityService_PortType impl;
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
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "personalID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getCharityInformation", _params, new javax.xml.namespace.QName("", "getCharityInformationReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://illuminati.is", "CharityInformation"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://illuminati.is", "getCharityInformation"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getCharityInformation") == null) {
            _myOperations.put("getCharityInformation", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getCharityInformation")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "loginName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "password"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("authenticateUser", _params, new javax.xml.namespace.QName("", "authenticateUserReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://illuminati.is", "Session"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://illuminati.is", "authenticateUser"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("authenticateUser") == null) {
            _myOperations.put("authenticateUser", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("authenticateUser")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "session"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://illuminati.is", "Session"), is.idega.idegaweb.marathon.webservice.server.Session.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "personalID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "password"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("updateUserPassword", _params, new javax.xml.namespace.QName("", "updateUserPasswordReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://illuminati.is", "updateUserPassword"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("updateUserPassword") == null) {
            _myOperations.put("updateUserPassword", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("updateUserPassword")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("SessionTimedOutException");
        _fault.setQName(new javax.xml.namespace.QName("http://illuminati.is", "fault"));
        _fault.setClassName("is.idega.idegaweb.marathon.webservice.server.SessionTimedOutException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://illuminati.is", "SessionTimedOutException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "session"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://illuminati.is", "Session"), is.idega.idegaweb.marathon.webservice.server.Session.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "personalID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "charityPersonalID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("updateUserCharity", _params, new javax.xml.namespace.QName("", "updateUserCharityReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://illuminati.is", "updateUserCharity"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("updateUserCharity") == null) {
            _myOperations.put("updateUserCharity", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("updateUserCharity")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("SessionTimedOutException");
        _fault.setQName(new javax.xml.namespace.QName("http://illuminati.is", "fault"));
        _fault.setClassName("is.idega.idegaweb.marathon.webservice.server.SessionTimedOutException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://illuminati.is", "SessionTimedOutException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "session"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://illuminati.is", "Session"), is.idega.idegaweb.marathon.webservice.server.Session.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "personalID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "email"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("updateUserEmail", _params, new javax.xml.namespace.QName("", "updateUserEmailReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://illuminati.is", "updateUserEmail"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("updateUserEmail") == null) {
            _myOperations.put("updateUserEmail", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("updateUserEmail")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("SessionTimedOutException");
        _fault.setQName(new javax.xml.namespace.QName("http://illuminati.is", "fault"));
        _fault.setClassName("is.idega.idegaweb.marathon.webservice.server.SessionTimedOutException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://illuminati.is", "SessionTimedOutException"));
        _oper.addFault(_fault);
    }

    public CharityServiceSoapBindingSkeleton() {
        this.impl = new is.idega.idegaweb.marathon.webservice.server.CharityServiceSoapBindingImpl();
    }

    public CharityServiceSoapBindingSkeleton(is.idega.idegaweb.marathon.webservice.server.CharityService_PortType impl) {
        this.impl = impl;
    }
    public is.idega.idegaweb.marathon.webservice.server.CharityInformation getCharityInformation(java.lang.String personalID) throws java.rmi.RemoteException
    {
        is.idega.idegaweb.marathon.webservice.server.CharityInformation ret = impl.getCharityInformation(personalID);
        return ret;
    }

    public is.idega.idegaweb.marathon.webservice.server.Session authenticateUser(java.lang.String loginName, java.lang.String password) throws java.rmi.RemoteException
    {
        is.idega.idegaweb.marathon.webservice.server.Session ret = impl.authenticateUser(loginName, password);
        return ret;
    }

    public boolean updateUserPassword(is.idega.idegaweb.marathon.webservice.server.Session session, java.lang.String personalID, java.lang.String password) throws java.rmi.RemoteException, is.idega.idegaweb.marathon.webservice.server.SessionTimedOutException
    {
        boolean ret = impl.updateUserPassword(session, personalID, password);
        return ret;
    }

    public boolean updateUserCharity(is.idega.idegaweb.marathon.webservice.server.Session session, java.lang.String personalID, java.lang.String charityPersonalID) throws java.rmi.RemoteException, is.idega.idegaweb.marathon.webservice.server.SessionTimedOutException
    {
        boolean ret = impl.updateUserCharity(session, personalID, charityPersonalID);
        return ret;
    }

    public boolean updateUserEmail(is.idega.idegaweb.marathon.webservice.server.Session session, java.lang.String personalID, java.lang.String email) throws java.rmi.RemoteException, is.idega.idegaweb.marathon.webservice.server.SessionTimedOutException
    {
        boolean ret = impl.updateUserEmail(session, personalID, email);
        return ret;
    }

}

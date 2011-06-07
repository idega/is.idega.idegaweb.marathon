/**
 * ContestantServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client;

public class ContestantServiceLocator extends org.apache.axis.client.Service implements is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ContestantService {

    public ContestantServiceLocator() {
    }


    public ContestantServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ContestantServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for BasicHttpBinding_IContestantService
    private java.lang.String BasicHttpBinding_IContestantService_address = "http://www.hlaupastyrkur.is/services/ContestantService.svc";

    public java.lang.String getBasicHttpBinding_IContestantServiceAddress() {
        return BasicHttpBinding_IContestantService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String BasicHttpBinding_IContestantServiceWSDDServiceName = "BasicHttpBinding_IContestantService";

    public java.lang.String getBasicHttpBinding_IContestantServiceWSDDServiceName() {
        return BasicHttpBinding_IContestantServiceWSDDServiceName;
    }

    public void setBasicHttpBinding_IContestantServiceWSDDServiceName(java.lang.String name) {
        BasicHttpBinding_IContestantServiceWSDDServiceName = name;
    }

    public is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.IContestantService getBasicHttpBinding_IContestantService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(BasicHttpBinding_IContestantService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getBasicHttpBinding_IContestantService(endpoint);
    }

    public is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.IContestantService getBasicHttpBinding_IContestantService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.BasicHttpBinding_IContestantServiceStub _stub = new is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.BasicHttpBinding_IContestantServiceStub(portAddress, this);
            _stub.setPortName(getBasicHttpBinding_IContestantServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setBasicHttpBinding_IContestantServiceEndpointAddress(java.lang.String address) {
        BasicHttpBinding_IContestantService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.IContestantService.class.isAssignableFrom(serviceEndpointInterface)) {
                is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.BasicHttpBinding_IContestantServiceStub _stub = new is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.BasicHttpBinding_IContestantServiceStub(new java.net.URL(BasicHttpBinding_IContestantService_address), this);
                _stub.setPortName(getBasicHttpBinding_IContestantServiceWSDDServiceName());
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
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("BasicHttpBinding_IContestantService".equals(inputPortName)) {
            return getBasicHttpBinding_IContestantService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "ContestantService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "BasicHttpBinding_IContestantService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("BasicHttpBinding_IContestantService".equals(portName)) {
            setBasicHttpBinding_IContestantServiceEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}

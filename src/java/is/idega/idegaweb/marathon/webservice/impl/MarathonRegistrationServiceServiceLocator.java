/**
 * MarathonRegistrationServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.marathon.webservice.impl;

public class MarathonRegistrationServiceServiceLocator extends org.apache.axis.client.Service implements is.idega.idegaweb.marathon.webservice.impl.MarathonRegistrationServiceService {

    public MarathonRegistrationServiceServiceLocator() {
    }


    public MarathonRegistrationServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public MarathonRegistrationServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for RegistrationService
    private java.lang.String RegistrationService_address = "http://www.marathon.is/services/RegistrationService";

    public java.lang.String getRegistrationServiceAddress() {
        return RegistrationService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String RegistrationServiceWSDDServiceName = "RegistrationService";

    public java.lang.String getRegistrationServiceWSDDServiceName() {
        return RegistrationServiceWSDDServiceName;
    }

    public void setRegistrationServiceWSDDServiceName(java.lang.String name) {
        RegistrationServiceWSDDServiceName = name;
    }

    public is.idega.idegaweb.marathon.webservice.impl.MarathonRegistrationService getRegistrationService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(RegistrationService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getRegistrationService(endpoint);
    }

    public is.idega.idegaweb.marathon.webservice.impl.MarathonRegistrationService getRegistrationService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            is.idega.idegaweb.marathon.webservice.impl.RegistrationServiceSoapBindingStub _stub = new is.idega.idegaweb.marathon.webservice.impl.RegistrationServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getRegistrationServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setRegistrationServiceEndpointAddress(java.lang.String address) {
        RegistrationService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (is.idega.idegaweb.marathon.webservice.impl.MarathonRegistrationService.class.isAssignableFrom(serviceEndpointInterface)) {
                is.idega.idegaweb.marathon.webservice.impl.RegistrationServiceSoapBindingStub _stub = new is.idega.idegaweb.marathon.webservice.impl.RegistrationServiceSoapBindingStub(new java.net.URL(RegistrationService_address), this);
                _stub.setPortName(getRegistrationServiceWSDDServiceName());
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
        if ("RegistrationService".equals(inputPortName)) {
            return getRegistrationService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:MarathonRegistrationService", "MarathonRegistrationServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:MarathonRegistrationService", "RegistrationService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("RegistrationService".equals(portName)) {
            setRegistrationServiceEndpointAddress(address);
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

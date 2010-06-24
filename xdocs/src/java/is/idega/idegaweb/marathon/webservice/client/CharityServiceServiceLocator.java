/**
 * CharityServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.marathon.webservice.client;

public class CharityServiceServiceLocator extends org.apache.axis.client.Service implements is.idega.idegaweb.marathon.webservice.client.CharityServiceService {

    public CharityServiceServiceLocator() {
    }


    public CharityServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CharityServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CharityService
    private java.lang.String CharityService_address = "http://skraning.marathon.is/services/CharityService";

    public java.lang.String getCharityServiceAddress() {
        return CharityService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CharityServiceWSDDServiceName = "CharityService";

    public java.lang.String getCharityServiceWSDDServiceName() {
        return CharityServiceWSDDServiceName;
    }

    public void setCharityServiceWSDDServiceName(java.lang.String name) {
        CharityServiceWSDDServiceName = name;
    }

    public is.idega.idegaweb.marathon.webservice.client.CharityService_PortType getCharityService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CharityService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCharityService(endpoint);
    }

    public is.idega.idegaweb.marathon.webservice.client.CharityService_PortType getCharityService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            is.idega.idegaweb.marathon.webservice.client.CharityServiceSoapBindingStub _stub = new is.idega.idegaweb.marathon.webservice.client.CharityServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getCharityServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCharityServiceEndpointAddress(java.lang.String address) {
        CharityService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (is.idega.idegaweb.marathon.webservice.client.CharityService_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                is.idega.idegaweb.marathon.webservice.client.CharityServiceSoapBindingStub _stub = new is.idega.idegaweb.marathon.webservice.client.CharityServiceSoapBindingStub(new java.net.URL(CharityService_address), this);
                _stub.setPortName(getCharityServiceWSDDServiceName());
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
        if ("CharityService".equals(inputPortName)) {
            return getCharityService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://illuminati.is", "CharityServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://illuminati.is", "CharityService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("CharityService".equals(portName)) {
            setCharityServiceEndpointAddress(address);
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

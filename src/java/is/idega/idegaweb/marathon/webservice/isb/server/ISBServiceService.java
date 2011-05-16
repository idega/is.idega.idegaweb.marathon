/**
 * ISBServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.marathon.webservice.isb.server;

public interface ISBServiceService extends javax.xml.rpc.Service {
    public java.lang.String getISBServiceAddress();

    public is.idega.idegaweb.marathon.webservice.isb.server.ISBService_PortType getISBService() throws javax.xml.rpc.ServiceException;

    public is.idega.idegaweb.marathon.webservice.isb.server.ISBService_PortType getISBService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}

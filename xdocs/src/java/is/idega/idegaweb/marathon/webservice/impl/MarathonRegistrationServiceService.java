/**
 * MarathonRegistrationServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.marathon.webservice.impl;

public interface MarathonRegistrationServiceService extends javax.xml.rpc.Service {
    public java.lang.String getRegistrationServiceAddress();

    public is.idega.idegaweb.marathon.webservice.impl.MarathonRegistrationService getRegistrationService() throws javax.xml.rpc.ServiceException;

    public is.idega.idegaweb.marathon.webservice.impl.MarathonRegistrationService getRegistrationService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}

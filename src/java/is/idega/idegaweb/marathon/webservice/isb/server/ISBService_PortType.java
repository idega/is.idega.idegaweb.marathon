/**
 * ISBService_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.marathon.webservice.isb.server;

public interface ISBService_PortType extends java.rmi.Remote {
    public is.idega.idegaweb.marathon.webservice.isb.server.Session authenticateUser(java.lang.String loginName, java.lang.String password) throws java.rmi.RemoteException;
    public boolean registerRunner(is.idega.idegaweb.marathon.webservice.isb.server.Session session, java.lang.String personalID, java.lang.String distance, java.lang.String shirtSize, java.lang.String email, java.lang.String phone, java.lang.String mobile, java.lang.String leg, is.idega.idegaweb.marathon.webservice.isb.server.RelayPartnerInfo[] partners, java.lang.String registeredBy) throws java.rmi.RemoteException, is.idega.idegaweb.marathon.webservice.isb.server.SessionTimedOutException;
}

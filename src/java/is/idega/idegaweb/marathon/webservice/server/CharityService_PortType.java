/**
 * CharityService_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.marathon.webservice.server;

public interface CharityService_PortType extends java.rmi.Remote {
    public is.idega.idegaweb.marathon.webservice.server.CharityInformation getCharityInformation(java.lang.String personalID) throws java.rmi.RemoteException;
    public is.idega.idegaweb.marathon.webservice.server.Session authenticateUser(java.lang.String loginName, java.lang.String password) throws java.rmi.RemoteException;
    public boolean updateUserPassword(is.idega.idegaweb.marathon.webservice.server.Session session, java.lang.String personalID, java.lang.String password) throws java.rmi.RemoteException, is.idega.idegaweb.marathon.webservice.server.SessionTimedOutException;
    public boolean updateUserCharity(is.idega.idegaweb.marathon.webservice.server.Session session, java.lang.String personalID, java.lang.String charityPersonalID) throws java.rmi.RemoteException, is.idega.idegaweb.marathon.webservice.server.SessionTimedOutException;
    public boolean updateUserEmail(is.idega.idegaweb.marathon.webservice.server.Session session, java.lang.String personalID, java.lang.String email) throws java.rmi.RemoteException, is.idega.idegaweb.marathon.webservice.server.SessionTimedOutException;
    public is.idega.idegaweb.marathon.webservice.server.Charity[] getCharities() throws java.rmi.RemoteException;
}

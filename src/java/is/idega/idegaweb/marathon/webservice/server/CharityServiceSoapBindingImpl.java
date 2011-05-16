/**
 * CharityServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.marathon.webservice.server;

import is.idega.idegaweb.marathon.webservice.business.WebServiceBusiness;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWMainApplication;

public class CharityServiceSoapBindingImpl implements
		is.idega.idegaweb.marathon.webservice.server.CharityService_PortType {
	public is.idega.idegaweb.marathon.webservice.server.CharityInformation getCharityInformation(
			java.lang.String personalID) throws java.rmi.RemoteException {
		return getBusiness().getCharityInformation(personalID);
	}
	public is.idega.idegaweb.marathon.webservice.server.Session authenticateUser(
			java.lang.String loginName, java.lang.String password)
			throws java.rmi.RemoteException {
		return getBusiness().authenticateUser(loginName, password);
	}

	public boolean updateUserPassword(
			is.idega.idegaweb.marathon.webservice.server.Session session,
			java.lang.String personalID, java.lang.String password)
			throws java.rmi.RemoteException,
			is.idega.idegaweb.marathon.webservice.server.SessionTimedOutException {
		return getBusiness().updateUserPassword(session, personalID, password);
	}

	public boolean updateUserCharity(
			is.idega.idegaweb.marathon.webservice.server.Session session,
			java.lang.String personalID, java.lang.String charityPersonalID)
			throws java.rmi.RemoteException,
			is.idega.idegaweb.marathon.webservice.server.SessionTimedOutException {
		return getBusiness().updateUserCharity(session, personalID, charityPersonalID);
	}

	public boolean updateUserEmail(
			is.idega.idegaweb.marathon.webservice.server.Session session,
			java.lang.String personalID, java.lang.String email)
			throws java.rmi.RemoteException,
			is.idega.idegaweb.marathon.webservice.server.SessionTimedOutException {
		return getBusiness().updateUserEmail(session, personalID, email);
	}

	private WebServiceBusiness getBusiness() throws IBOLookupException {
		return (WebServiceBusiness) IBOLookup.getServiceInstance(
				IWMainApplication.getDefaultIWApplicationContext(),
				WebServiceBusiness.class);
	}
}

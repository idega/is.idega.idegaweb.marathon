/**
 * ISBServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.marathon.webservice.isb.server;

import is.idega.idegaweb.marathon.webservice.business.WebServiceBusiness;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWMainApplication;

public class ISBServiceSoapBindingImpl implements is.idega.idegaweb.marathon.webservice.isb.server.ISBService_PortType{
    public is.idega.idegaweb.marathon.webservice.isb.server.Session authenticateUser(is.idega.idegaweb.marathon.webservice.isb.server.Login in0) throws java.rmi.RemoteException {
    	is.idega.idegaweb.marathon.webservice.server.Session session = getBusiness().authenticateUser(in0.getLoginName(), in0.getPassword());
    	
        return new is.idega.idegaweb.marathon.webservice.isb.server.Session(session.getSessionID());
    }

    public boolean registerRunner(is.idega.idegaweb.marathon.webservice.isb.server.RunnerInfo in0) throws java.rmi.RemoteException, is.idega.idegaweb.marathon.webservice.isb.server.SessionTimedOutException {
    	return getBusiness().registerRunner(in0.getSession(), in0.getPersonalID(), in0.getDistance(), in0.getShirtSize(), in0.getShirtSizeGender(), in0.getEmail(), in0.getPhone(), in0.getMobile(), in0.getLeg(), in0.getPartners(), in0.getRegisteredBy());
    }

    private WebServiceBusiness getBusiness() throws IBOLookupException {
		return (WebServiceBusiness) IBOLookup.getServiceInstance(
				IWMainApplication.getDefaultIWApplicationContext(),
				WebServiceBusiness.class);
	}
}
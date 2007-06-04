/**
 * RegistrationServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.marathon.webservice.impl;

import is.idega.idegaweb.marathon.data.Participant;
import is.idega.idegaweb.marathon.data.ParticipantBMPBean;
import is.idega.idegaweb.marathon.data.ParticipantHome;
import is.idega.idegaweb.marathon.presentation.RunRegistration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;

public class RegistrationServiceSoapBindingImpl implements is.idega.idegaweb.marathon.webservice.impl.MarathonRegistrationService{
    public is.idega.idegaweb.marathon.webservice.impl.Registrations getRegistrations(java.lang.String runId, java.lang.String year) throws java.rmi.RemoteException {
        //return null;
        
        Registrations regs = new Registrations();
        
        List registrationList = new ArrayList();
        
        int iRunId = Integer.parseInt(runId);
        int iYear = Integer.parseInt(year);
        
        ParticipantHome partHome = (ParticipantHome) IDOLookup.getHome(Participant.class);
        try {
			Collection participants = partHome.findAllByRunGroupIdAndYear(iRunId,iYear);
			for (Iterator iter = participants.iterator(); iter.hasNext();) {
				Participant participant = (Participant) iter.next();
				
				String personalId=participant.getUser().getPersonalID();
				int distance=participant.getRunDistanceGroup().getDistanceInKms();
				String charityId=participant.getCharityId();
				
				Registration reg = new Registration(charityId,distance,personalId);
				registrationList.add(reg);
			}
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Registration[] registrations = (Registration[]) registrationList.toArray(new Registration[0]);
        regs.setRegistrations(registrations);
        
        return regs;
    }

}

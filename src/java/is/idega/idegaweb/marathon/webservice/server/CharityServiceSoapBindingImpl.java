/**
 * CharityServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.marathon.webservice.server;

import is.idega.idegaweb.marathon.business.CharityBusiness;
import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.data.Charity;
import is.idega.idegaweb.marathon.data.Participant;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.business.AddressBusiness;
import com.idega.core.location.data.Address;
import com.idega.idegaweb.IWMainApplication;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;

public class CharityServiceSoapBindingImpl implements
		is.idega.idegaweb.marathon.webservice.server.CharityService_PortType {
	public is.idega.idegaweb.marathon.webservice.server.CharityInformation getCharityInformation(
			java.lang.String personalID) throws java.rmi.RemoteException {
		
		User user = null;
		Group run = null;
		Group year = null;
		Participant runner = null;
		
		try {
			run = getRunBusiness().getRunGroupByGroupId(new Integer(4));
			year = getRunBusiness().getRunGroupByGroupId(new Integer(416257));
			user = getUserBusiness().getUser(personalID);

			runner = getRunBusiness().getParticipantByRunAndYear(
					user, run, year);

			Charity charity = null;
			if (runner.getCharityId() != null
					&& !runner.getCharityId().equals("")) {
				charity = getCharityBusiness().getCharityByOrganisationalID(runner.getCharityId());
			}

			CharityInformation info = new CharityInformation();
			if (charity != null) {
				info.setCharityID(runner.getCharityId());
				info.setCharityName(charity.getName());
			}
			info.setName(user.getName());
			info.setPersonalID(user.getPersonalID());
			Address address = null;
			try {
				address = getUserBusiness().getUsersMainAddress(user);
			} catch (Exception e) {
			}
			Email email = null;
			try {
				email = getUserBusiness().getUserMail(user);
			} catch (Exception e) {
			}
			Phone mobile = null;
			try {
				mobile = getUserBusiness().getUsersMobilePhone(user);
			} catch (Exception e) {
			}
			Phone phone = null;
			try {
				phone = getUserBusiness().getUsersHomePhone(user);
			} catch (Exception e) {
			}
			
			if (address != null) {
				if (address.getStreetAddress() != null) {
					info.setAddress(address.getStreetAddress());
				}
				if (address.getCity() != null) {
					info.setCity(address.getCity());
				}
				if (address.getCountry() != null) {
					info.setCountry(address.getCountry().getName());
				}
				if (address.getPostalCode() != null) {
					info.setPostalCode(address.getPostalCode().getPostalCode());
				}
			}
			info.setDistance(runner.getRunDistanceGroup().getName());
			info.setGender(user.getGender().getName());
			info.setNationality(runner.getUserNationality());
			if (phone != null && phone.getNumber() != null) {
				info.setPhone(phone.getNumber());
			}
			if (mobile != null && mobile.getNumber() != null) {
				info.setMobile(mobile.getNumber());
			}
			if (email != null && email.getEmailAddress() != null) {
				info.setEmail(email.getEmailAddress());
			}

			return info;

		} catch (FinderException e) {

			if (run == null || year == null) {
				return null;
			}
			
			boolean partner1 = false;
			boolean partner2 = false;
			boolean partner3 = false;
			
			try {
				runner = getRunBusiness().getParticipantPartnerByRunAndYear(personalID, run, year, 1);
				partner1 = true;

			} catch (FinderException e1) {
				try {
					runner = getRunBusiness().getParticipantPartnerByRunAndYear(personalID, run, year, 2);
					partner2 = true;
				} catch (FinderException e2) {
					try {
						runner = getRunBusiness().getParticipantPartnerByRunAndYear(personalID, run, year, 3);
						partner3 = true;
					} catch (FinderException e3) {
						return null;
					}
				}
			}
			
			CharityInformation info = new CharityInformation();
			info.setDistance(runner.getRunDistanceGroup().getName());
			
			if (partner1) {
				info.setName(runner.getRelayPartner1Name());
				info.setPersonalID(runner.getRelayPartner1SSN());
				info.setEmail(runner.getRelayPartner1Email());
			} else if (partner2) {
				info.setName(runner.getRelayPartner2Name());
				info.setPersonalID(runner.getRelayPartner2SSN());
				info.setEmail(runner.getRelayPartner2Email());
			} else if (partner3) {
				info.setName(runner.getRelayPartner3Name());
				info.setPersonalID(runner.getRelayPartner3SSN());
				info.setEmail(runner.getRelayPartner3Email());				
			}

			Charity charity = null;
			if (runner.getCharityId() != null
					&& !runner.getCharityId().equals("")) {
				charity = getCharityBusiness().getCharityByOrganisationalID(runner.getCharityId());
			}
			if (charity != null) {
				info.setCharityID(runner.getCharityId());
				info.setCharityName(charity.getName());
			}
			
			return info;			
		}
	}

	private RunBusiness getRunBusiness() throws IBOLookupException {
		return (RunBusiness) IBOLookup.getServiceInstance(IWMainApplication
				.getDefaultIWApplicationContext(), RunBusiness.class);
	}

	private UserBusiness getUserBusiness() throws IBOLookupException {
		return (UserBusiness) IBOLookup.getServiceInstance(IWMainApplication
				.getDefaultIWApplicationContext(), UserBusiness.class);
	}

	private AddressBusiness getAddressBusiness() throws IBOLookupException {
		return (AddressBusiness) IBOLookup.getServiceInstance(IWMainApplication
				.getDefaultIWApplicationContext(), AddressBusiness.class);
	}

	private CharityBusiness getCharityBusiness() throws IBOLookupException {
		return (CharityBusiness) IBOLookup.getServiceInstance(IWMainApplication
				.getDefaultIWApplicationContext(), CharityBusiness.class);
	}
}

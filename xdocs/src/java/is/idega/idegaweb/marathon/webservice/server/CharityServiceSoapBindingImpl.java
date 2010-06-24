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
		try {
			User user = getUserBusiness().getUser(personalID);
			Group run = getRunBusiness().getRunGroupByGroupId(new Integer(4));
			Group year = getRunBusiness().getRunGroupByGroupId(new Integer(416257));

			Participant runner = getRunBusiness().getParticipantByRunAndYear(
					user, run, year);

			if (runner.getCharityId() == null
					|| runner.getCharityId().equals("")) {
				return null;
			}

			Charity charity = getCharityBusiness()
					.getCharityByOrganisationalID(runner.getCharityId());
			CharityInformation info = new CharityInformation();
			info.setCharityID(runner.getCharityId());
			info.setCharityName(charity.getName());
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
		}

		return null;
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

package is.idega.idegaweb.marathon.webservice.business;

import is.idega.idegaweb.marathon.business.CharityBusiness;
import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.data.Charity;
import is.idega.idegaweb.marathon.data.Participant;
import is.idega.idegaweb.marathon.webservice.data.WebServiceLoginSession;
import is.idega.idegaweb.marathon.webservice.data.WebServiceLoginSessionHome;
import is.idega.idegaweb.marathon.webservice.server.CharityInformation;
import is.idega.idegaweb.marathon.webservice.server.Session;
import is.idega.idegaweb.marathon.webservice.server.SessionTimedOutException;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.core.accesscontrol.business.AccessController;
import com.idega.core.accesscontrol.business.LoginBusinessBean;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.accesscontrol.data.LoginTableHome;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.business.AddressBusiness;
import com.idega.core.location.data.Address;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.IWContext;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

public class WebServiceBusinessBean extends IBOServiceBean implements
		WebServiceBusiness {

	public CharityInformation getCharityInformation(String personalID)
			throws java.rmi.RemoteException {
		User user = null;
		Group run = null;
		Group year = null;
		Participant runner = null;

		try {
			run = getRunBusiness().getRunGroupByGroupId(new Integer(4));
			year = getRunBusiness().getRunGroupByGroupId(new Integer(426626)); // TODO
																				// fix
			user = getUserBusiness().getUser(personalID);

			runner = getRunBusiness().getParticipantByRunAndYear(user, run,
					year);

			Charity charity = null;
			if (runner.getCharityId() != null
					&& !runner.getCharityId().equals("")) {
				charity = getCharityBusiness().getCharityByOrganisationalID(
						runner.getCharityId());
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
				runner = getRunBusiness().getParticipantPartnerByRunAndYear(
						personalID, run, year, 1);
				partner1 = true;

			} catch (FinderException e1) {
				try {
					runner = getRunBusiness()
							.getParticipantPartnerByRunAndYear(personalID, run,
									year, 2);
					partner2 = true;
				} catch (FinderException e2) {
					try {
						runner = getRunBusiness()
								.getParticipantPartnerByRunAndYear(personalID,
										run, year, 3);
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
				charity = getCharityBusiness().getCharityByOrganisationalID(
						runner.getCharityId());
			}
			if (charity != null) {
				info.setCharityID(runner.getCharityId());
				info.setCharityName(charity.getName());
			}

			return info;
		}
	}

	public is.idega.idegaweb.marathon.webservice.server.Session authenticateUser(
			String userName, String password) {
		try {

			LoginBusinessBean loginBean = LoginBusinessBean
					.getDefaultLoginBusinessBean();
			LoginTable loginTable = getLoginTableHome().findByLogin(userName);
			if (loginTable != null) {
				// check if user is already verified
				Collection sessions = getWebServiceLoginSessionHome()
						.findAllByUser(loginTable.getUser());
				if (sessions != null && !sessions.isEmpty()) {
					IWTimestamp now = IWTimestamp.RightNow();
					Iterator it = sessions.iterator();
					while (it.hasNext()) {
						WebServiceLoginSession loginSession = (WebServiceLoginSession) it
								.next();
						IWTimestamp lastAccess = new IWTimestamp(
								loginSession.getLastAccess());
						if (IWTimestamp.getMilliSecondsBetween(lastAccess, now) <= getSessionTimeout()) {
							loginSession.setLastAccess(now.getTimestamp());
							loginSession.store();

							return new is.idega.idegaweb.marathon.webservice.server.Session(
									loginSession.getUniqueId());
						} else {
							loginSession.setIsClosed(true);
							loginSession.store();
						}
					}
				}

				// verify password and create new ws session
				if (loginBean.verifyPassword(loginTable, password)) {
					
					//check if user has the correct role to use the web services
					IWContext iwc = IWContext.getInstance();
					AccessController acc = iwc.getAccessController();
					try {
					if (!acc.hasPermissionForGroup("CharityService", loginTable.getUser().getPrimaryGroup(), iwc)) {
						return new is.idega.idegaweb.marathon.webservice.server.Session("-1");
					}
					} catch(Exception e) {
						return new is.idega.idegaweb.marathon.webservice.server.Session("-1");
					}
					
					WebServiceLoginSession loginSession = getWebServiceLoginSessionHome()
							.create();
					IWTimestamp now = IWTimestamp.RightNow();
					loginSession.setCreated(now.getTimestamp());
					loginSession.setLastAccess(now.getTimestamp());
					loginSession.setIsClosed(false);
					loginSession.setUser(loginTable.getUser());
					loginSession.store();

					return new is.idega.idegaweb.marathon.webservice.server.Session(
							loginSession.getUniqueId());
				}
			}
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		}

		return new is.idega.idegaweb.marathon.webservice.server.Session("-1");
	}
	
	private LoginTableHome getLoginTableHome() {
		try {
			return (LoginTableHome) IDOLookup.getHome(LoginTable.class);
		} catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private int getSessionTimeout() {
		String timeout = this.getIWMainApplication().getSettings()
				.getProperty("WEBSERVICE_TIMEOUT", "900000");

		return Integer.parseInt(timeout);
	}

	private WebServiceLoginSession validateAndUpdateLoginSession(
			is.idega.idegaweb.marathon.webservice.server.Session session) {
		if (session == null) {
			return null;
		}

		if (session.getSessionID() == null
				|| "-1".equals(session.getSessionID())) {
			return null;
		}

		try {
			WebServiceLoginSession loginSession = getWebServiceLoginSessionHome()
					.findByUniqueID(session.getSessionID());
			if (loginSession != null && !loginSession.getIsClosed()) {
				IWTimestamp now = IWTimestamp.RightNow();
				IWTimestamp lastAccess = new IWTimestamp(
						loginSession.getLastAccess());
				if (IWTimestamp.getMilliSecondsBetween(lastAccess, now) <= getSessionTimeout()) {
					loginSession.setLastAccess(now.getTimestamp());
					loginSession.store();

					return loginSession;
				} else {
					loginSession.setIsClosed(true);
					loginSession.store();
				}
			}
		} catch (FinderException e) {
		}

		return null;
	}

	private WebServiceLoginSessionHome getWebServiceLoginSessionHome() {
		try {
			return (WebServiceLoginSessionHome) IDOLookup
					.getHome(WebServiceLoginSession.class);
		} catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private RunBusiness getRunBusiness() throws IBOLookupException {
		return (RunBusiness) IBOLookup.getServiceInstance(
				IWMainApplication.getDefaultIWApplicationContext(),
				RunBusiness.class);
	}

	private UserBusiness getUserBusiness() throws IBOLookupException {
		return (UserBusiness) IBOLookup.getServiceInstance(
				IWMainApplication.getDefaultIWApplicationContext(),
				UserBusiness.class);
	}

	private AddressBusiness getAddressBusiness() throws IBOLookupException {
		return (AddressBusiness) IBOLookup.getServiceInstance(
				IWMainApplication.getDefaultIWApplicationContext(),
				AddressBusiness.class);
	}

	private CharityBusiness getCharityBusiness() throws IBOLookupException {
		return (CharityBusiness) IBOLookup.getServiceInstance(
				IWMainApplication.getDefaultIWApplicationContext(),
				CharityBusiness.class);
	}

	public boolean updateUserPassword(Session session, String personalID,
			String password) throws RemoteException, SessionTimedOutException {

		WebServiceLoginSession loginSession = validateAndUpdateLoginSession(session);
		if (loginSession != null) {
		} else {
			throw new SessionTimedOutException();
		}
		
		try {
			User user = getUserBusiness().getUser(personalID);
			if (getUserBusiness().hasUserLogin(user)) {
				LoginTable login = LoginDBHandler.getUserLogin(user);
				LoginDBHandler.changePassword(login, password);
			}
		} catch (Exception e) {
		}
		
		return false;
	}

	public boolean updateUserCharity(Session session, String personalID,
			String charityPersonalID) throws RemoteException,
			SessionTimedOutException {
		WebServiceLoginSession loginSession = validateAndUpdateLoginSession(session);
		if (loginSession != null) {
		} else {
			throw new SessionTimedOutException();
		}

		try {
			User user = getUserBusiness().getUser(personalID);
			Charity charity = getCharityBusiness().getCharityByOrganisationalID(charityPersonalID);
			Group run = getRunBusiness().getRunGroupByGroupId(new Integer(4));
			Group year = getRunBusiness().getRunGroupByGroupId(new Integer(426626)); // TODO			
			Participant participant = getRunBusiness().getParticipantByRunAndYear(user, run, year);
			participant.setCharityId(charityPersonalID);
			participant.store();
		} catch (FinderException e) {
		}

		return false;
	}

	public boolean updateUserEmail(Session session, String personalID,
			String email) throws RemoteException, SessionTimedOutException {
		WebServiceLoginSession loginSession = validateAndUpdateLoginSession(session);
		if (loginSession != null) {
		} else {
			throw new SessionTimedOutException();
		}

		try {
			User user = getUserBusiness().getUser(personalID);
			getUserBusiness().updateUserMail(user, email);
			
			return true;
		} catch (Exception e) {
		}

		return false;
	}
}
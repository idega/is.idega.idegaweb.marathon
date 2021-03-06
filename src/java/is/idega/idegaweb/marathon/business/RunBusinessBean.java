/*
 * Created on Jun 30, 2004
 */
package is.idega.idegaweb.marathon.business;

import is.idega.block.family.business.FamilyLogic;
import is.idega.block.family.business.NoParentFound;
import is.idega.idegaweb.marathon.data.Charity;
import is.idega.idegaweb.marathon.data.Distance;
import is.idega.idegaweb.marathon.data.Participant;
import is.idega.idegaweb.marathon.data.ParticipantHome;
import is.idega.idegaweb.marathon.data.Run;
import is.idega.idegaweb.marathon.data.Year;
import is.idega.idegaweb.marathon.data.YearBMPBean;
import is.idega.idegaweb.marathon.presentation.CreateYearForm;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;
import is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ContestantRequest;
import is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ContestantServiceLocator;
import is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.IContestantService;
import is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.Login;
import is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.TeamMember;
import is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.TeamRequest;

import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Date;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.transaction.UserTransaction;

import com.idega.block.creditcard.business.CreditCardAuthorizationException;
import com.idega.block.creditcard.business.CreditCardBusiness;
import com.idega.block.creditcard.business.CreditCardClient;
import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.block.creditcard.data.TPosMerchant;
import com.idega.block.creditcard.data.TPosMerchantHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.contact.data.Email;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.AddressHome;
import com.idega.core.location.data.Country;
import com.idega.core.location.data.CountryHome;
import com.idega.core.location.data.PostalCode;
import com.idega.core.location.data.PostalCodeHome;
import com.idega.core.messaging.MessagingSettings;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOCreateException;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.NoEmailFoundException;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Gender;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;
import com.idega.util.Age;
import com.idega.util.IWTimestamp;
import com.idega.util.LocaleUtil;
import com.idega.util.text.Name;

/**
 * Description: Business bean (service) for run... <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * 
 * @author birna
 */
public class RunBusinessBean extends IBOServiceBean implements RunBusiness {

	private static final String RUN_MIDNIGHT_RUN = "Midnight Run";
	private static final String RUN_LAUGAVEGUR = "Laugavegur";
	private static final String RUN_LAUGAVEGUR_PREREGISTRATION = "Laugavegur - Forskraning";
	private static final String RUN_RVK_MARATHON = "Reykjavik Marathon";
	private static final String RUN_LAZY_TOWN_RUN = "Lazy Town Run";

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3105168986587179336L;

	private final static String IW_BUNDLE_IDENTIFIER = IWMarathonConstants.IW_BUNDLE_IDENTIFIER;

	private static String DEFAULT_SMTP_MAILSERVER = "mail.idega.is";
	private static String DEFAULT_MESSAGEBOX_FROM_ADDRESS = "marathon@marathon.is";
	private static String DEFAULT_CC_ADDRESS = "marathon@marathon.is";

	private AddressHome addressHome;
	private ParticipantHome participantHome;

	/**
	 * saves information on the user - creates a new user if he doesn't exsist..
	 */
	public User saveUser(String name, String ssn, IWTimestamp dateOfBirth,
			Gender gender, String address, String postal, String city,
			Country country) {
		User user = null;
		try {
			if (dateOfBirth == null) {
				dateOfBirth = getBirthDateFromSSN(ssn);
			}
			Name fullName = new Name(name);
			user = getUserBiz().createUser(fullName.getFirstName(),
					fullName.getMiddleName(), fullName.getLastName(),
					fullName.getName(), ssn, gender, dateOfBirth);
			user.store();

			if (address != null && !address.equals("")) {
				AddressHome addressHome = (AddressHome) getIDOHome(Address.class);
				Address a = addressHome.create();
				a.setStreetName(address);
				a.setCity(city);
				a.setCountry(country);
				a.setAddressType(getAddressHome().getAddressType2());
				a.store();

				PostalCodeHome postalHome = (PostalCodeHome) getIDOHome(PostalCode.class);
				Integer countryID = (Integer) country.getPrimaryKey();
				PostalCode p = null;
				try {
					p = postalHome.findByPostalCodeAndCountryId(postal,
							countryID.intValue());
				} catch (FinderException fe) {
					p = postalHome.create();
					p.setCountry(country);
					p.setPostalCode(postal);
					p.setName(city);
					p.store();
				}
				if (p != null) {
					a.setPostalCode(p);
				}
				a.store();
				try {
					user.addAddress(a);
				} catch (IDOAddRelationshipException idoEx) {
				}
			}
			user.store();
		} catch (RemoteException rme) {
		} catch (CreateException cre) {
		}
		return user;
	}

	public boolean isRegisteredInRun(int runID, int userID) {
		try {
			User user = getUserBiz().getUserHome().findByPrimaryKey(
					new Integer(userID));

			return getUserBiz().isMemberOfGroup(runID, user);
		} catch (RemoteException re) {
			log(re);
		} catch (FinderException fe) {
			// User does not exist in database...
		}
		return false;
	}

	public boolean isRegisteredInRun(String year, Group run, User user) {
		try {
			Group runYear = null;
			String[] types = { IWMarathonConstants.GROUP_TYPE_RUN_YEAR };
			Collection years = getGroupBiz().getChildGroups(run, types, true);
			Iterator iter = years.iterator();
			while (iter.hasNext()) {
				Group yearGroup = (Group) iter.next();
				if (yearGroup.getName().equals(year)) {
					runYear = yearGroup;
					break;
				}
			}

			if (runYear == null) {
				return false;
			}

			((ParticipantHome) IDOLookup.getHome(Participant.class))
					.findByUserAndRun(user, run, runYear, false);
			return true;
		} catch (FinderException fe) {
			return false;
		} catch (RemoteException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	public boolean isRegisteredInRunPreviousYear(Group run, User user) {
		try {
			String year = "";
			Group runYear = null;
			String[] types = { IWMarathonConstants.GROUP_TYPE_RUN_YEAR };
			Collection years = getGroupBiz().getChildGroups(run, types, true);
			Iterator iter = years.iterator();
			while (iter.hasNext()) {
				Group yearGroup = (Group) iter.next();
				if (yearGroup.getName().equals(year)) {
					runYear = yearGroup;
					break;
				}
			}

			if (runYear == null) {
				return false;
			}

			((ParticipantHome) IDOLookup.getHome(Participant.class))
					.findByUserAndRun(user, run, runYear, false);
			return true;
		} catch (FinderException fe) {
			return false;
		} catch (RemoteException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	public boolean doesGroupExist(Object distancePK, String groupName) {
		try {
			return ((ParticipantHome) IDOLookup.getHome(Participant.class))
					.getCountByDistanceAndGroupName(distancePK, groupName) > 0;
		} catch (IDOException ie) {
			ie.printStackTrace();
			return false;
		} catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	public boolean isRegisteredInRun(int runID, String personalID) {
		try {
			User user = getUserBiz().getUserHome().findByPersonalID(personalID);

			return getUserBiz().isMemberOfGroup(runID, user);
		} catch (RemoteException re) {
			log(re);
		} catch (FinderException fe) {
			// User does not exist in database...
		}
		return false;
	}

	/**
	 * 
	 * @param pin
	 *            - a social security number - format ddmmyyxxxx or ddmmyyyy
	 * @return IWTimstamp - the date of birth from the pin..
	 */
	private IWTimestamp getBirthDateFromSSN(String pin) {
		// pin format = 14011973
		if (pin.length() == 8) {
			int edd = Integer.parseInt(pin.substring(0, 2));
			int emm = Integer.parseInt(pin.substring(2, 4));
			int eyyyy = Integer.parseInt(pin.substring(4, 8));
			IWTimestamp dob = new IWTimestamp(edd, emm, eyyyy);
			return dob;
		}
		// pin format = 140173xxxx ddmmyyxxxx
		else if (pin.length() == 10) {
			int dd = Integer.parseInt(pin.substring(0, 2));
			int mm = Integer.parseInt(pin.substring(2, 4));
			int yy = Integer.parseInt(pin.substring(4, 6));
			int century = Integer.parseInt(pin.substring(9, 10));
			int yyyy = 0;
			if (century == 9) {
				yyyy = yy + 1900;
			} else if (century == 0) {
				yyyy = yy + 2000;
			}
			IWTimestamp dob = new IWTimestamp(dd, mm, yyyy);
			return dob;
		} else {
			return null;
		}
	}

	public int getAgeFromPersonalID(String personalID) {
		if (personalID != null) {
			IWTimestamp dateOfBirth = getBirthDateFromSSN(personalID);
			if (dateOfBirth != null) {
				Age age = new Age(dateOfBirth.getDate());
				return age.getYears();
			}
		}
		return -1;
	}

	public Participant importParticipant(User user, Group run, Group year,
			Group distance) throws CreateException {
		try {
			Group ageGenderGroup = getAgeGroup(user, run, distance);
			ageGenderGroup.addGroup(user);

			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			Participant participant = runHome.create();
			participant.setUser(user);
			participant.setRunTypeGroup(run);
			participant.setRunDistanceGroup(distance);
			participant.setRunYearGroup(year);
			participant.setRunGroupGroup(ageGenderGroup);

			participant.store();

			return participant;
		} catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public Participant storeParticipantRegistration(Runner runner,
			Locale locale, String runPrefix) throws IDOCreateException {
		Participant participant = null;

		UserTransaction trans = getSessionContext().getUserTransaction();
		try {
			trans.begin();
			User user = runner.getUser();
			String personalId = null;
			if (user == null) {
				personalId = runner.getPersonalID();
				if (personalId == null || "".equals(personalId.trim())) {
					IWTimestamp dob = new IWTimestamp(runner.getDateOfBirth());
					personalId = dob.getDateString("yyyy-MM-dd");
				}
				user = saveUser(runner.getName(), personalId, new IWTimestamp(
						runner.getDateOfBirth()), runner.getGender(),
						runner.getAddress(), runner.getPostalCode(),
						runner.getCity(), runner.getCountry());
			} else {
				personalId = user.getPersonalID();
			}

			Group ageGenderGroup = getAgeGroup(user, runner.getRun(),
					runner.getDistance());
			ageGenderGroup.addGroup(user);
			Group yearGroup = (Group) runner.getDistance().getParentNode();
			Group run = runner.getRun();
			Group distance = runner.getDistance();
			Run selectedRun = null;
			try {
				selectedRun = ConverterUtility.getInstance().convertGroupToRun(
						run);
			} catch (FinderException e) {
				// Run not found
			}

			try {
				ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
				participant = runHome.create();
				participant.setUser(user);
				participant.setRunTypeGroup(run);
				participant.setRunDistanceGroup(distance);
				participant.setRunYearGroup(yearGroup);
				participant.setRunGroupGroup(ageGenderGroup);

				// participant.setShirtSize(runner.getShirtSize());
				participant.setUserNationality(runner.getNationality()
						.getName());
				if (runner.getDistance() != null) {
					participant
							.setParticipantNumber(getNextAvailableParticipantNumber(runner
									.getDistance()));
				}
				// participant.setAllowsEmails(runner.getAllowsEmails());
				participant.setQuestion1Hour(runner.getQuestion1Hour());
				participant.setQuestion1Minute(runner.getQuestion1Minute());
				participant.setQuestion1Year(runner.getQuestion1Year());
				participant.setQuestion1NeverRan(runner.getQuestion1NeverRan());
				// participant.setQuestion2Hour(runner.getQuestion2Hour());
				// participant.setQuestion2Minute(runner.getQuestion2Minute());
				participant.setQuestion3Hour(runner.getQuestion3Hour());
				participant.setQuestion3Minute(runner.getQuestion3Minute());
				participant.setQuestion3Year(runner.getQuestion3Year());
				participant.setQuestion3NeverRan(runner.getQuestion3NeverRan());

				participant.store();

				getUserBiz().updateUserHomePhone(user, runner.getHomePhone());
				getUserBiz().updateUserMobilePhone(user,
						runner.getMobilePhone());
				getUserBiz().updateUserMail(user, runner.getEmail());

				if (runner.getEmail() != null) {
					IWResourceBundle iwrb = getIWApplicationContext()
							.getIWMainApplication()
							.getBundle(IWMarathonConstants.IW_BUNDLE_IDENTIFIER)
							.getResourceBundle(locale);
					String distanceString = iwrb.getLocalizedString(
							distance.getName(), distance.getName());
					Object[] args = {
							user.getName(),
							iwrb.getLocalizedString(run.getName(),
									run.getName())
									+ " " + yearGroup.getName() };
					String subject = iwrb.getLocalizedString(runPrefix
							+ "registration_received_subject_mail",
							"Your registration has been received.");
					String body = MessageFormat.format(iwrb.getLocalizedString(
							runPrefix + "registration_received_body_mail",
							"Your registration has been received."), args);
					sendMessage(runner.getEmail(), subject, body);
				}
			} catch (CreateException ce) {
				ce.printStackTrace();
			} catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}

			trans.commit();
		} catch (Exception ex) {
			try {
				trans.rollback();
			} catch (javax.transaction.SystemException e) {
				throw new IDOCreateException(e.getMessage());
			}
			ex.printStackTrace();
			throw new IDOCreateException(ex);
		}

		return participant;
	}

	public void storeParticipantConfirmationPayment(Collection participans,
			String email, String hiddenCardNumber, double amount,
			IWTimestamp date, Locale locale,
			boolean disableSendPaymentConfirmation, String runPrefix)
			throws IDOCreateException {
		Collection participants = new ArrayList();

		UserTransaction trans = getSessionContext().getUserTransaction();
		try {
			trans.begin();
			Iterator iter = participans.iterator();
			while (iter.hasNext()) {
				Participant participant = (Participant) iter.next();
				User user = participant.getUser();

				participant.setHasPayedConfirmation(true);
				participant.store();

				Email participantEmail = null;
				try {
					participantEmail = this.getUserBiz()
							.getUsersMainEmail(user);
				} catch (NoEmailFoundException nefe) {
				} catch (RemoteException e) {
				}

				if (participantEmail != null) {
					IWResourceBundle iwrb = getIWApplicationContext()
							.getIWMainApplication()
							.getBundle(IWMarathonConstants.IW_BUNDLE_IDENTIFIER)
							.getResourceBundle(locale);
					Object[] args = {
							user.getName(),
							iwrb.getLocalizedString(participant
									.getRunTypeGroup().getName(), participant
									.getRunTypeGroup().getName())
									+ " "
									+ participant.getRunYearGroup().getName(),
							iwrb.getLocalizedString(
									"shirt_size." + participant.getShirtSize(),
									participant.getShirtSize()) };
					String subject = iwrb.getLocalizedString(runPrefix
							+ "confirmation_payment_subject_mail",
							"Your registration has been received.");
					String body = MessageFormat.format(iwrb.getLocalizedString(
							runPrefix + "confirmation_payment_body_mail",
							"Your registration has been received."), args);
					sendMessage(participantEmail.getEmailAddress(), subject,
							body);
				}

			}
			if (email != null && !disableSendPaymentConfirmation && amount > 0) {
				IWResourceBundle iwrb = getIWApplicationContext()
						.getIWMainApplication()
						.getBundle(IWMarathonConstants.IW_BUNDLE_IDENTIFIER)
						.getResourceBundle(locale);

				Object[] args = {
						hiddenCardNumber,
						String.valueOf(amount),
						date.getLocaleDateAndTime(locale, IWTimestamp.SHORT,
								IWTimestamp.SHORT) };
				String subject = iwrb.getLocalizedString(runPrefix
						+ "receipt_subject_mail",
						"Your receipt for registration");
				String body = MessageFormat.format(iwrb.getLocalizedString(
						runPrefix + "receipt_body_mail",
						"Your registration has been received."), args);
				sendMessage(email, subject, body);
			}
			trans.commit();
		} catch (Exception ex) {
			try {
				trans.rollback();
			} catch (javax.transaction.SystemException e) {
				throw new IDOCreateException(e.getMessage());
			}
			ex.printStackTrace();
			throw new IDOCreateException(ex);
		}
	}

	public void markParticipantAsPaid(Collection participans, String email,
			String hiddenCardNumber, double amount, IWTimestamp date,
			Locale locale, boolean disableSendPaymentConfirmation,
			String runPrefix) throws IDOCreateException {
		Collection participants = new ArrayList();

		UserTransaction trans = getSessionContext().getUserTransaction();
		try {
			trans.begin();
			Iterator iter = participans.iterator();
			while (iter.hasNext()) {
				Participant participant = (Participant) iter.next();
				User user = participant.getUser();

				participant.setHasPayedFee(true);
				participant.store();

				Email participantEmail = null;
				try {
					participantEmail = this.getUserBiz()
							.getUsersMainEmail(user);
				} catch (NoEmailFoundException nefe) {
				} catch (RemoteException e) {
				}

				if (participantEmail != null) {
					IWResourceBundle iwrb = getIWApplicationContext()
							.getIWMainApplication()
							.getBundle(IWMarathonConstants.IW_BUNDLE_IDENTIFIER)
							.getResourceBundle(locale);
					Object[] args = {
							user.getName(),
							iwrb.getLocalizedString(participant
									.getRunTypeGroup().getName(), participant
									.getRunTypeGroup().getName())
									+ " "
									+ participant.getRunYearGroup().getName(),
							iwrb.getLocalizedString(
									"shirt_size." + participant.getShirtSize(),
									participant.getShirtSize()) };
					String subject = iwrb.getLocalizedString(runPrefix
							+ "confirmation_payment_subject_mail",
							"Your registration has been received.");
					String body = MessageFormat.format(iwrb.getLocalizedString(
							runPrefix + "confirmation_payment_body_mail",
							"Your registration has been received."), args);
					sendMessage(participantEmail.getEmailAddress(), subject,
							body);
				}

			}
			if (email != null && !disableSendPaymentConfirmation && amount > 0) {
				IWResourceBundle iwrb = getIWApplicationContext()
						.getIWMainApplication()
						.getBundle(IWMarathonConstants.IW_BUNDLE_IDENTIFIER)
						.getResourceBundle(locale);

				Object[] args = {
						hiddenCardNumber,
						String.valueOf(amount),
						date.getLocaleDateAndTime(locale, IWTimestamp.SHORT,
								IWTimestamp.SHORT) };
				String subject = iwrb.getLocalizedString(runPrefix
						+ "receipt_subject_mail",
						"Your receipt for registration");
				String body = MessageFormat.format(iwrb.getLocalizedString(
						runPrefix + "receipt_body_mail",
						"Your registration has been received."), args);
				sendMessage(email, subject, body);
			}
			trans.commit();
		} catch (Exception ex) {
			try {
				trans.rollback();
			} catch (javax.transaction.SystemException e) {
				throw new IDOCreateException(e.getMessage());
			}
			ex.printStackTrace();
			throw new IDOCreateException(ex);
		}
	}

	public Collection saveParticipants(Collection runners, String email,
			String hiddenCardNumber, double amount, IWTimestamp date,
			Locale locale, boolean disableSendPaymentConfirmation,
			String runPrefix, boolean sendCharityRegistration,
			String authorizationNumber) throws IDOCreateException {
		Collection participants = new ArrayList();

		List hlaupastyrkur = new ArrayList();

		UserTransaction trans = getSessionContext().getUserTransaction();
		try {
			trans.begin();
			Iterator iter = runners.iterator();
			Group run = null;
			Group distance = null;
			Run selectedRun = null;
			Year selectedYear = null;
			while (iter.hasNext()) {
				Runner runner = (Runner) iter.next();
				User user = runner.getUser();
				String personalId = null;
				if (user == null) {
					personalId = runner.getPersonalID();
					user = saveUser(runner.getName(), personalId,
							new IWTimestamp(runner.getDateOfBirth()),
							runner.getGender(), runner.getAddress(),
							runner.getPostalCode(), runner.getCity(),
							runner.getCountry());
				} else {
					personalId = user.getPersonalID();
				}
				String userNameString = "";
				String passwordString = "";

				if (getUserBiz().hasUserLogin(user)) {
					try {
						LoginTable login = LoginDBHandler.getUserLogin(user);
						userNameString = login.getUserLogin();
						passwordString = LoginDBHandler
								.getGeneratedPasswordForUser();
						LoginDBHandler.changePassword(login, passwordString);
					} catch (Exception e) {
						System.out
								.println("Error re-generating password for user: "
										+ user.getName());
						e.printStackTrace();
					}
				} else {
					try {
						LoginTable login = getUserBiz().generateUserLogin(user);
						userNameString = login.getUserLogin();
						passwordString = login.getUnencryptedUserPassword();
					} catch (Exception e) {
						System.out.println("Error creating login for user: "
								+ user.getName());
						e.printStackTrace();
					}
				}

				Group ageGenderGroup = getAgeGroup(user, runner.getRun(),
						runner.getDistance());
				ageGenderGroup.addGroup(user);
				Group yearGroup = (Group) runner.getDistance().getParentNode();
				run = runner.getRun();
				distance = runner.getDistance();
				selectedRun = null;
				try {
					selectedRun = ConverterUtility.getInstance()
							.convertGroupToRun(run);
					selectedYear = ConverterUtility.getInstance()
							.convertGroupToYear(yearGroup);
				} catch (FinderException e) {
					// Run not found
				}

				if (sendCharityRegistration) {
					if (!selectedYear.isCharityEnabled()) {
						sendCharityRegistration = false;
						if (runPrefix.equals("rm_reg.")) {
							runPrefix = "lt_reg.";
						}
					}
				}

				try {
					HlaupastyrkurHolder holder = null;
					ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
					Participant participant = runHome.create();
					participant.setUser(user);
					participant.setRunTypeGroup(run);
					participant.setRunDistanceGroup(distance);
					participant.setRunYearGroup(yearGroup);
					participant.setRunGroupGroup(ageGenderGroup);
					if (runner.isParticipateInCharity()) {
						Charity charity = runner.getCharity();
						if (charity != null) {
							participant.setCharityId(charity
									.getOrganizationalID());
						}

						if (sendCharityRegistration && charity != null) {
							holder = new HlaupastyrkurHolder();
							holder.setRunner(runner);
							holder.setLogin(userNameString);
							holder.setPassword(passwordString);
						}
					}
					if (runner.getCategory() != null) {
						participant.setCategoryId(((Integer) runner
								.getCategory().getPrimaryKey()).intValue());
					}
					if (runner.getAmount() > 0) {
						participant.setPayedAmount(String.valueOf(runner
								.getAmount()));
					}

					participant.setShirtSize(runner.getShirtSize());
					participant.setUserNationality(runner.getNationality()
							.getName());
					if (runner.getDistance() != null) {
						participant
								.setParticipantNumber(getNextAvailableParticipantNumber(runner
										.getDistance()));
					}

					if (runner.getPaymentGroup() != null) {
						participant.setPaymentGroup(runner.getPaymentGroup());
					}

					participant.setQuestion1Hour(runner.getQuestion1Hour());
					participant.setQuestion1Minute(runner.getQuestion1Minute());
					participant.setQuestion1Year(runner.getQuestion1Year());
					participant.setQuestion1NeverRan(runner
							.getQuestion1NeverRan());
					participant.setQuestion2Hour(runner.getQuestion2Hour());
					participant.setQuestion2Minute(runner.getQuestion2Minute());
					participant.setQuestion3Hour(runner.getQuestion3Hour());
					participant.setQuestion3Minute(runner.getQuestion3Minute());
					participant.setQuestion3Year(runner.getQuestion3Year());
					participant.setQuestion3NeverRan(runner
							.getQuestion3NeverRan());

					if (runner.getRelayLeg() != null
							& !"".equals(runner.getRelayLeg())) {
						participant.setRelayLeg(runner.getRelayLeg());

						if (runner.getPartner1SSN() != null
								& !"".equals(runner.getPartner1SSN())) {
							participant.setRelayPartner1SSN(runner
									.getPartner1SSN());
							participant.setRelayPartner1Name(runner
									.getPartner1Name());
							participant.setRelayPartner1Email(runner
									.getPartner1Email());
							participant.setRelayPartner1ShirtSize(runner
									.getPartner1ShirtSize());
							participant.setRelayPartner1Leg(runner
									.getPartner1Leg());

							if (holder != null) {
								TeamMember member = new TeamMember(
										runner.getPartner1SSN(), Boolean.TRUE);
								holder.addToTeam(member);
							}

							if (runner.getPartner2SSN() != null
									& !"".equals(runner.getPartner2SSN())) {
								participant.setRelayPartner2SSN(runner
										.getPartner2SSN());
								participant.setRelayPartner2Name(runner
										.getPartner2Name());
								participant.setRelayPartner2Email(runner
										.getPartner2Email());
								participant.setRelayPartner2ShirtSize(runner
										.getPartner2ShirtSize());
								participant.setRelayPartner2Leg(runner
										.getPartner2Leg());

								if (holder != null) {
									TeamMember member = new TeamMember(
											runner.getPartner2SSN(),
											Boolean.TRUE);
									holder.addToTeam(member);
								}
								if (runner.getPartner3SSN() != null
										& !"".equals(runner.getPartner3SSN())) {
									participant.setRelayPartner3SSN(runner
											.getPartner3SSN());
									participant.setRelayPartner3Name(runner
											.getPartner3Name());
									participant.setRelayPartner3Email(runner
											.getPartner3Email());
									participant
											.setRelayPartner3ShirtSize(runner
													.getPartner3ShirtSize());
									participant.setRelayPartner3Leg(runner
											.getPartner3Leg());

									if (holder != null) {
										TeamMember member = new TeamMember(
												runner.getPartner3SSN(),
												Boolean.TRUE);
										holder.addToTeam(member);
									}
								}
							}
						}
					}

					if (holder != null) {
						hlaupastyrkur.add(holder);
					}
					participant.store();

					participants.add(participant);

					getUserBiz().updateUserHomePhone(user,
							runner.getHomePhone());
					getUserBiz().updateUserMobilePhone(user,
							runner.getMobilePhone());
					getUserBiz().updateUserMail(user, runner.getEmail());

					if (runner.getEmail() != null) {
						IWResourceBundle iwrb = getIWApplicationContext()
								.getIWMainApplication()
								.getBundle(
										IWMarathonConstants.IW_BUNDLE_IDENTIFIER)
								.getResourceBundle(locale);
						String distanceString = iwrb.getLocalizedString(
								distance.getName(), distance.getName());
						String informationPageString = "";
						String greeting = "";
						String runHomePageString = "";
						String receiptInfo = "";
						if (selectedRun != null) {
							runHomePageString = selectedRun.getRunHomePage();
							if (locale.equals(LocaleUtil.getIcelandicLocale())) {
								informationPageString = selectedRun
										.getRunInformationPage();
								greeting = selectedRun
										.getRunRegistrationReceiptGreeting();
								receiptInfo = selectedRun
										.getRunRegistrationReceiptInfo();
							} else {
								informationPageString = selectedRun
										.getEnglishRunInformationPage();
								greeting = selectedRun
										.getRunRegistrationReceiptGreetingEnglish();
								receiptInfo = selectedRun
										.getRunRegistrationReceiptInfoEnglish();
							}

							if (informationPageString == null) {
								informationPageString = " ";
							}
							if (greeting == null) {
								greeting = " ";
							}
							if (receiptInfo == null) {
								receiptInfo = " ";
							}
						}
						Object[] args = {
								user.getName(),
								iwrb.getLocalizedString(run.getName(),
										run.getName())
										+ " " + yearGroup.getName(),
								distanceString,
								iwrb.getLocalizedString(
										"shirt_size." + runner.getShirtSize(),
										runner.getShirtSize()),
								String.valueOf(participant
										.getParticipantNumber()),
								runHomePageString, informationPageString,
								userNameString, passwordString, greeting,
								receiptInfo };
						String subject = iwrb.getLocalizedString(runPrefix
								+ "registration_received_subject_mail",
								"Your registration has been received.");
						String body = MessageFormat
								.format(iwrb.getLocalizedString(runPrefix
										+ "registration_received_body_mail",
										"Your registration has been received."),
										args);
						sendMessage(runner.getEmail(), subject, body);
					}

					// sendSponsorEmail(runner,locale);
				} catch (CreateException ce) {
					ce.printStackTrace();
				} catch (RemoteException re) {
					throw new IBORuntimeException(re);
				}
			}
			if (email != null && !disableSendPaymentConfirmation && amount > 0) {
				IWResourceBundle iwrb = getIWApplicationContext()
						.getIWMainApplication()
						.getBundle(IWMarathonConstants.IW_BUNDLE_IDENTIFIER)
						.getResourceBundle(locale);
				String runName = "";
				String runHomePage = "";
				if (selectedRun != null) {
					runName = iwrb.getLocalizedString(selectedRun.getName(),
							selectedRun.getName());
					runHomePage = selectedRun.getRunHomePage();
				}

				Object[] args = {
						hiddenCardNumber,
						String.valueOf(amount),
						date.getLocaleDateAndTime(locale, IWTimestamp.SHORT,
								IWTimestamp.SHORT), runName, runHomePage };
				String subject = iwrb.getLocalizedString(runPrefix
						+ "receipt_subject_mail",
						"Your receipt for registration");
				String body = MessageFormat.format(iwrb.getLocalizedString(
						runPrefix + "receipt_body_mail",
						"Your registration has been received."), args);
				sendMessage(email, subject, body);
			}
			trans.commit();

			if (!hlaupastyrkur.isEmpty()) {
				String passwd = getIWApplicationContext()
						.getIWMainApplication().getSettings()
						.getProperty("hlaupastyrkur_passwd", "");
				String userID = getIWApplicationContext()
						.getIWMainApplication().getSettings()
						.getProperty("hlaupastyrkur_userid", "");
				ContestantServiceLocator locator = new ContestantServiceLocator();
				IContestantService port = locator
						.getBasicHttpBinding_IContestantService(new URL(
								"http://www.hlaupastyrkur.is/services/contestantservice.svc"));
				Iterator it = hlaupastyrkur.iterator();
				while (it.hasNext()) {
					HlaupastyrkurHolder holder = (HlaupastyrkurHolder) it
							.next();
					Runner runner = holder.getRunner();
					String userNameString = holder.getLogin();
					String passwordString = holder.getPassword();
					Charity charity = runner.getCharity();
					try {
						String name = null;
						if (runner.getName() != null) {
							name = runner.getName();
						} else if (runner.getUser() != null) {
							name = runner.getUser().getName();
						}
						if (holder.isTeam()) {
							TeamRequest request2 = new TeamRequest(runner
									.getDistance().getName(), new Login(passwd,
									userID), charity.getOrganizationalID(),
									name, passwordString, userNameString,
									holder.getMembers(), name);
							port.registerTeam(request2);
						} else {
							ContestantRequest request = new ContestantRequest(
									runner.getDistance().getName(), new Login(
											passwd, userID),
									charity.getOrganizationalID(), name,
									passwordString, userNameString,
									runner.getPersonalID(), Boolean.TRUE);
							port.registerContestant(request);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception ex) {
			try {
				trans.rollback();
			} catch (javax.transaction.SystemException e) {
				throw new IDOCreateException(e.getMessage());
			}
			ex.printStackTrace();
			throw new IDOCreateException(ex);
		}

		return participants;
	}

	/*
	 * private void sendSponsorEmail(Runner runner,Locale locale) {
	 * 
	 * if(runner.isMaySponsorContactRunner()){ try{ User user =
	 * runner.getUser(); if(user!=null){ String name =
	 * runner.getUser().getName(); String personalId = runner.getPersonalID();
	 * String sAddress = ""; Collection addresses =
	 * runner.getUser().getAddresses(); if(addresses.size()>0){ for (Iterator
	 * iter = addresses.iterator(); iter.hasNext();) { Address address =
	 * (Address) iter.next();
	 * sAddress=address.getName()+" "+address.getPostalAddress(); }
	 * 
	 * } String phone = runner.getHomePhone(); String mobilePhone =
	 * runner.getMobilePhone(); String runnerEmail = runner.getEmail(); String
	 * sponsorEmail =
	 * getIWApplicationContext().getIWMainApplication().getSettings
	 * ().getProperty("MARATHON_SPONSOR_EMAIL");
	 * 
	 * IWResourceBundle iwrb =
	 * getIWApplicationContext().getIWMainApplication().getBundle
	 * (IWMarathonConstants.IW_BUNDLE_IDENTIFIER).getResourceBundle(locale);
	 * Object[] args = { name, personalId, sAddress, phone,
	 * mobilePhone,runnerEmail }; String subject =
	 * iwrb.getLocalizedString("sponsor_mail_subject",
	 * "Request for invitation to be a customer from marathon.is"); String body
	 * = MessageFormat.format(iwrb.getLocalizedString("sponsor_mail_body",
	 * "Name: {0}\nPersonal ID: {1}\nAddress: {2}\nPhone: {3}\nMobile Phone: {4}\nE-mail: {5}"
	 * ), args); sendMessage(sponsorEmail, subject, body); } else{
	 * logWarning("User is null in sendSponsorEmail for runner: "
	 * +runner.getName()+", "+runner.getEmail()); } } catch(Exception e){
	 * e.printStackTrace(); } }
	 * 
	 * }
	 */

	public synchronized int getNextAvailableParticipantNumber(Distance distance) {
		int number = distance.getNextAvailableParticipantNumber();
		int minNumber = distance.getMinimumParticipantNumberForDistance();
		int maxNumber = distance.getMaximumParticipantNumberForDistance();
		if (minNumber == -1) {
			minNumber = 1;
		}
		if (number == -1) {
			number = minNumber;
		}
		if (maxNumber != -1 && number > maxNumber) {
			return minNumber;
		}

		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			while (number <= (maxNumber == -1 ? 999999 : maxNumber)) {
				if (runHome.getCountByDistanceAndNumber(
						distance.getPrimaryKey(), number) == 0) {
					distance.setNextAvailableParticipantNumber(number + 1);
					distance.store();
					return number;
				}

				number++;
			}
		} catch (RemoteException re) {
			throw new IBORuntimeException(re);
		} catch (IDOException ie) {
			ie.printStackTrace();
		}
		return minNumber;
	}

	public void addParticipantsToGroup(String[] participants, String groupName) {
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);

			for (int i = 0; i < participants.length; i++) {
				try {
					Participant participant = runHome
							.findByPrimaryKey(new Integer(participants[i]));
					participant.setRunGroupName(groupName);
					participant.store();
				} catch (FinderException fe) {
					fe.printStackTrace();
				}
			}
		} catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public boolean isCrewLabelAlreadyExistsForRun(int runId, int yearId,
			String crewLabel) {
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			return runHome.findCrewLabelAlreadyExistsForRun(runId, yearId,
					crewLabel);
		} catch (RemoteException re) {
			throw new IBORuntimeException(re);
		} catch (FinderException re) {
			throw new IBORuntimeException(re);
		}
	}

	private Group getAgeGroup(User user, Run run, Distance distance) {
		return getAgeGroup(user, (Group) run, distance);
	}

	public Group getAgeGroup(User user, Group run, Group distance) {
		Year year = null;
		try {
			year = ConverterUtility.getInstance().convertGroupToYear(
					(Group) distance.getParentNode());
		} catch (FinderException fe) {
			fe.printStackTrace();
		}

		IWTimestamp runDate = new IWTimestamp();
		runDate.setYear(Integer.parseInt(year.getName()));
		if (year != null && year.getRunDate() != null) {
			runDate = new IWTimestamp(year.getRunDate());
		}
		IWTimestamp dateOfBirth = new IWTimestamp(user.getDateOfBirth());
		dateOfBirth.setDay(1);
		dateOfBirth.setMonth(1);
		Age age = new Age(dateOfBirth.getDate());

		String[] groupType = { IWMarathonConstants.GROUP_TYPE_RUN_GROUP };
		try {
			Collection groups = getGroupBiz().getChildGroupsRecursive(distance,
					groupType, true);

			Iterator groupsIter = groups.iterator();
			while (groupsIter.hasNext()) {
				Group group = (Group) groupsIter.next();
				if (group.getName().equals(
						getGroupName(age.getYears(runDate.getDate()), run,
								user.getGenderID(), distance))) {
					return group;
				}
			}
		} catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
		return null;
	}

	public void finishPayment(String properties, String currency)
			throws CreditCardAuthorizationException {
		try {
			CreditCardClient client = getCreditCardBusiness()
					.getCreditCardClient(getCreditCardMerchant(currency));
			client.finishTransaction(properties);
		} catch (CreditCardAuthorizationException ccae) {
			throw ccae;
		} catch (Exception e) {
			e.printStackTrace(System.err);
			throw new CreditCardAuthorizationException(
					"Online payment failed. Unknown error.");
		}
	}

	/**
	 * @deprecated use authorizePayment with expiresDate parameter
	 */
	public String authorizePayment(String nameOnCard, String cardNumber,
			String monthExpires, String yearExpires, String ccVerifyNumber,
			double amount, String currency, String referenceNumber)
			throws CreditCardAuthorizationException {
		try {
			CreditCardClient client = getCreditCardBusiness()
					.getCreditCardClient(getCreditCardMerchant(currency));
			return client.creditcardAuthorization(nameOnCard, cardNumber,
					monthExpires, yearExpires, ccVerifyNumber, amount,
					currency, referenceNumber);
		} catch (CreditCardAuthorizationException ccae) {
			throw ccae;
		} catch (Exception e) {
			e.printStackTrace(System.err);
			throw new CreditCardAuthorizationException(
					"Online payment failed. Unknown error.");
		}
	}

	public String getAuthorizationNumberFromProperties(
			String creditCardProperties, String currency) {
		try {
			CreditCardClient client = getCreditCardBusiness()
					.getCreditCardClient(getCreditCardMerchant(currency));
			return client.getAuthorizationNumber(creditCardProperties);
		} catch (Exception e) {
		}

		return null;
	}

	/**
	 * 
	 * @param nameOnCard
	 * @param cardNumber
	 * @param expiresDate
	 *            - only year and month are relevant
	 * @param ccVerifyNumber
	 * @param amount
	 * @param currency
	 * @param referenceNumber
	 * @return
	 * @throws CreditCardAuthorizationException
	 */
	public String authorizePayment(String nameOnCard, String cardNumber,
			java.util.Date expiresDate, String ccVerifyNumber, double amount,
			String currency, String referenceNumber)
			throws CreditCardAuthorizationException {

		IWTimestamp expirationDate = new IWTimestamp(expiresDate);
		String yearPostfix = String.valueOf(expirationDate.getYear() % 100);
		String monthPostfix = String.valueOf(expirationDate.getMonth());

		if (yearPostfix.length() != 2)
			yearPostfix = "0" + yearPostfix;

		if (monthPostfix.length() != 2)
			monthPostfix = "0" + monthPostfix;

		try {
			CreditCardClient client = getCreditCardBusiness()
					.getCreditCardClient(getCreditCardMerchant(currency));
			return client.creditcardAuthorization(nameOnCard, cardNumber,
					monthPostfix, yearPostfix, ccVerifyNumber, amount,
					currency, referenceNumber);
		} catch (CreditCardAuthorizationException ccae) {
			throw ccae;
		} catch (Exception e) {
			e.printStackTrace(System.err);
			throw new CreditCardAuthorizationException(
					"Online payment failed. Unknown error.");
		}
	}

	public float getPriceForRunner(Runner runner, Locale locale) {
		Age age = null;
		if (runner.getUser() != null) {
			age = new Age(runner.getUser().getDateOfBirth());
		} else {
			age = new Age(runner.getDateOfBirth());
		}
		boolean isChild = age.getYears() <= 16;

		float runnerPrice = isChild ? runner.getDistance().getChildrenPrice(
				locale) : runner.getDistance().getPrice(locale);

		return runnerPrice;
	}

	public int getNumberOfSiblings(Collection children) throws RemoteException {
		int numberOfSiblings = 0;

		Iterator iter = children.iterator();
		HashMap childCounter = new HashMap();
		int counter = 0;
		// Making sure I check all the children, since not all may be siblings
		while (iter.hasNext()) {
			Runner child = (Runner) iter.next();
			try {
				User user = child.getUser();
				if (user != null) {
					Collection uPats = getFamilyLogic().getParentsFor(user);
					Iterator piter = uPats.iterator();
					while (piter.hasNext()) {
						User pat = (User) piter.next();
						if (!childCounter.containsKey(pat)) {
							childCounter.put(pat, new Integer(1));
						} else {
							childCounter.put(
									pat,
									new Integer(((Integer) childCounter
											.get(pat)).intValue() + 1));
						}
					}
				}
			} catch (NoParentFound e) {
			}
			++counter;
		}

		Collection values = childCounter.values();
		if (values != null && !values.isEmpty()) {
			Iterator viter = values.iterator();
			Integer val = (Integer) viter.next();
			numberOfSiblings = val.intValue();
			while (viter.hasNext()) {
				val = (Integer) viter.next();
				numberOfSiblings = Math.max(numberOfSiblings, val.intValue());
			}
		}

		return numberOfSiblings;
	}

	public int getNumberOfChildren(Collection runners) {
		int numberOfChildren = 0;

		Iterator iter = runners.iterator();
		while (iter.hasNext()) {
			Runner runner = (Runner) iter.next();
			Age age = null;
			if (runner.getUser() != null) {
				age = new Age(runner.getUser().getDateOfBirth());
			} else {
				age = new Age(runner.getDateOfBirth());
			}

			if (age.getYears() <= 16) {
				if (runner.getDistance().isFamilyDiscount()) {
					numberOfChildren++;
				}
			}
		}

		return numberOfChildren;
	}

	private FamilyLogic getFamilyLogic() {
		try {
			return (FamilyLogic) getServiceInstance(FamilyLogic.class);
		} catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	public CharityBusiness getCharityBusiness() {
		try {
			return (CharityBusiness) getServiceInstance(CharityBusiness.class);
		} catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	public Collection getCreditCardImages(String currency) {
		try {
			return getCreditCardBusiness().getCreditCardTypeImages(
					getCreditCardBusiness().getCreditCardClient(
							getCreditCardMerchant(currency)));
		} catch (FinderException fe) {
			fe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}

	private CreditCardMerchant getCreditCardMerchant(String currency)
			throws FinderException {
		String merchantPK = getIWApplicationContext()
				.getIWMainApplication()
				.getBundle(IWMarathonConstants.IW_BUNDLE_IDENTIFIER)
				.getProperty(
						IWMarathonConstants.PROPERTY_MERCHANT_PK + "_"
								+ currency);
		if (merchantPK != null) {
			try {
				// return ((KortathjonustanMerchantHome) IDOLookup
				// .getHome(KortathjonustanMerchant.class))
				// .findByPrimaryKey(new Integer(merchantPK));
				return ((TPosMerchantHome) IDOLookup
						.getHome(TPosMerchant.class))
						.findByPrimaryKey(new Integer(merchantPK));
			} catch (IDOLookupException ile) {
				throw new IBORuntimeException(ile);
			}
		}
		return null;
	}

	public void savePayment(int userID, int distanceID, String payMethod,
			String amount) {
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			Participant run = runHome.findByUserIDandDistanceID(userID,
					distanceID);
			if (run != null) {
				run.setPayMethod(payMethod);
				run.setPayedAmount(amount);
				run.store();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public void updateRunForParticipant(Participant participant, int bibNumber,
			String runTime, String chipTime, String splitTime1,
			String splitTime2) {
		if (runTime != null) {
			runTime = runTime.trim();
			if (!runTime.equals("")) {
				participant.setRunTime(convertTimeToInt(runTime));
			}
		}
		if (chipTime != null) {
			chipTime = chipTime.trim();
			if (!chipTime.equals("")) {
				participant.setChipTime(convertTimeToInt(chipTime));
			}
		}
		if (splitTime1 != null) {
			splitTime1 = splitTime1.trim();
			if (!splitTime1.equals("")) {
				participant.setSplitTime1(convertTimeToInt(splitTime1));
			}
		}
		if (splitTime2 != null) {
			splitTime2 = splitTime2.trim();
			if (!splitTime2.equals("")) {
				participant.setSplitTime2(convertTimeToInt(splitTime2));
			}
		}
		participant.setParticipantNumber(bibNumber);
		participant.store();
	}

	private int convertTimeToInt(String time) {
		if (time == null || time.equals("-1")) {
			return -1;
		}
		int index = time.lastIndexOf(":");
		if (index == -1) {
			return Integer.parseInt(time);
		}
		int hours = 0;
		int minutes = 0;
		int seconds = Integer.parseInt(time.substring(index + 1));
		time = time.substring(0, index);
		if (time.indexOf(":") != -1) {
			hours = Integer.parseInt(time.substring(0, time.indexOf(":")));
			minutes = Integer.parseInt(time.substring(time.indexOf(":") + 1));
		} else {
			minutes = Integer.parseInt(time);
		}
		seconds += hours * 60 * 60;
		seconds += minutes * 60;

		return seconds;
	}

	public Participant getRunObjByUserIDandYearID(int userID, int yearID) {
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			return runHome.findByUserIDandYearID(userID, yearID);
		} catch (RemoteException e) {
			log(e);
		} catch (FinderException e) {
			log(e);
		}

		return null;
	}

	public Participant getRunObjByUserIDandDistanceID(int userID, int distanceID) {
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			return runHome.findByUserIDandDistanceID(userID, distanceID);
		} catch (RemoteException e) {
			log(e);
		} catch (FinderException e) {
			log(e);
		}

		return null;
	}

	public Participant getParticipantByDistanceAndParticipantNumber(
			Object distancePK, int participantNumber) throws FinderException {
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			return runHome.findByDistanceAndParticipantNumber(distancePK,
					participantNumber);
		} catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}

	public Collection getParticipantsByYearAndTeamName(Object yearPK,
			String teamName) throws FinderException {
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			return runHome.findByYearAndTeamName(yearPK, teamName);
		} catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}

	public Collection getParticipantsByYearAndCrewInOrCrewInvitationParticipantId(
			Object yearPK, Integer crewParticipantId) throws FinderException {
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			return runHome.findByYearAndCrewInOrCrewInvitationParticipantId(
					yearPK, crewParticipantId);
		} catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}

	public Collection getParticipantsByUser(User user) throws FinderException {
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			return runHome.findByUserID(((Integer) user.getPrimaryKey())
					.intValue());
		} catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}

	/**
	 * 
	 * @param yearPK
	 * @param searchQuery
	 * @return - participants, where search query matches either participant
	 *         user full name, or personal id, or participant number
	 * @throws FinderException
	 */
	public Collection getParticipantsBySearchQuery(Object yearPK,
			String searchQuery) throws FinderException {
		try {
			ParticipantHome participantHome = (ParticipantHome) getIDOHome(Participant.class);
			return participantHome
					.findByYearAndFullNameOrPersonalIdOrParticipantNumberOrParentGroup(
							yearPK, searchQuery);
		} catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}

	public Participant getParticipantByRunAndYear(User user, Group run,
			Group year, boolean showDeleted) throws FinderException {
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			return runHome.findByUserAndRun(user, run, year, showDeleted);
		} catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}

	public Participant getParticipantPartnerByRunAndYear(String personalID,
			Group run, Group year, int partnerNumber) throws FinderException {
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			return runHome.findByPartnerAndRun(personalID, run, year,
					partnerNumber);
		} catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}

	public Group getRunGroupByGroupId(Integer groupId) {
		try {
			GroupHome groupHome = (GroupHome) getIDOHome(Group.class);
			return groupHome.findByPrimaryKey(groupId);
		} catch (RemoteException e) {
			log(e);
		} catch (FinderException e) {
			log(e);
		}
		return null;
	}

	public Participant getRunObjByUserAndGroup(int userID, int groupID) {
		Participant run = null;
		int yearGroupID = -1;
		int distanceGroupID = -1;
		int runGroupID = -1;
		Group group = null;
		Collection parentGroups = null;
		try {
			group = getGroupBiz().getGroupByGroupID(groupID);
			if (group != null) {
				parentGroups = getGroupBiz().getParentGroupsRecursive(group);
			}
		} catch (IBOLookupException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		if (parentGroups != null && !parentGroups.isEmpty()) {
			Iterator groupIter = parentGroups.iterator();
			while (groupIter.hasNext()) {
				Group parentGroup = (Group) groupIter.next();
				if (parentGroup.getGroupType().equals(
						IWMarathonConstants.GROUP_TYPE_RUN)) {
					runGroupID = Integer.parseInt(parentGroup.getPrimaryKey()
							.toString());
				} else if (parentGroup.getGroupType().equals(
						IWMarathonConstants.GROUP_TYPE_RUN_YEAR)) {
					yearGroupID = Integer.parseInt(parentGroup.getPrimaryKey()
							.toString());
				} else if (parentGroup.getGroupType().equals(
						IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE)) {
					distanceGroupID = Integer.parseInt(parentGroup
							.getPrimaryKey().toString());
				}
			}
		}
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			if (runGroupID != -1 && yearGroupID != -1 && distanceGroupID != -1) {
				Collection runObjs = runHome.findByUserAndParentGroup(userID,
						runGroupID, 2004, distanceGroupID);
				if (runObjs != null && !runObjs.isEmpty()) {
					Iterator runIt = runObjs.iterator();
					while (runIt.hasNext()) {
						Participant runObj = (Participant) runIt.next();
						run = runObj;
					}
				}
			}
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (FinderException e1) {
			e1.printStackTrace();
		}
		return run;
	}

	public Collection getRunnersByDistance(Group distance, Group runGroup) {
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			return runHome.findAllByDistanceAndGroup(distance, runGroup);
		} catch (RemoteException e) {
			log(e);
		} catch (FinderException e) {
			log(e);
		}

		return new ArrayList();
	}

	public void setParticipantNumber(Participant participant, String run) {
		participant
				.setParticipantNumber(getNextAvailableParticipantNumber(participant
						.getRunDistanceGroup()));
		participant.store();
	}

	public void sendMessage(String email, String subject, String body) {
		boolean sendEmail = true;
		String sSendEmail = this.getIWMainApplication()
				.getBundle(IW_BUNDLE_IDENTIFIER)
				.getProperty(IWMarathonConstants.PROPERTY_SEND_EMAILS);
		if ("no".equalsIgnoreCase(sSendEmail)) {
			sendEmail = false;
		}

		if (sendEmail) {
			String mailServer = DEFAULT_SMTP_MAILSERVER;
			String fromAddress = DEFAULT_MESSAGEBOX_FROM_ADDRESS;
			String cc = DEFAULT_CC_ADDRESS;
			try {
				MessagingSettings messagingSetting = getIWApplicationContext()
						.getIWMainApplication().getMessagingSettings();
				mailServer = messagingSetting.getSMTPMailServer();
				fromAddress = messagingSetting.getFromMailAddress();
				cc = getIWApplicationContext().getIWMainApplication()
						.getSettings()
						.getProperty("messagebox_cc_receiver_address", "");
			} catch (Exception e) {
				System.err
						.println("MessageBusinessBean: Error getting mail property from bundle");
				e.printStackTrace();
			}

			try {
				com.idega.util.SendMail.send(fromAddress, email.trim(), cc, "",
						mailServer, subject, body);
			} catch (javax.mail.MessagingException me) {
				System.err
						.println("MessagingException when sending mail to address: "
								+ email + " Message was: " + me.getMessage());
			} catch (Exception e) {
				System.err.println("Exception when sending mail to address: "
						+ email + " Message was: " + e.getMessage());
			}
		}
	}

	/**
	 * @param age
	 * @param nameOfGroup
	 * @return
	 */
	private String getGroupName(int age, Group group, int genderID,
			Group distance) {
		String runName = group.getName();
		String nameOfGroup = "";
		if (runName.equals(RUN_RVK_MARATHON) || runName.equals("Test hlaup")
		/* || runName.equals(RUN_ROLLER_SKATE) */) {
			if (distance.getName().equals(IWMarathonConstants.DISTANCE_1_5)) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_11;
				} else {
					nameOfGroup = IWMarathonConstants.MALE_11;
				}
			} else {
				if (age <= 14) {
					if (genderID == 2) {
						nameOfGroup = IWMarathonConstants.FEMALE_14;
					} else {
						nameOfGroup = IWMarathonConstants.MALE_14;
					}
				} else if (age > 14 && age <= 17) {
					if (genderID == 2) {
						nameOfGroup = IWMarathonConstants.FEMALE_15_17;
					} else {
						nameOfGroup = IWMarathonConstants.MALE_15_17;
					}
				} else if (age > 17 && age <= 39) {
					if (genderID == 2) {
						nameOfGroup = IWMarathonConstants.FEMALE_18_39;
					} else {
						nameOfGroup = IWMarathonConstants.MALE_18_39;
					}
				} else if (age > 39 && age <= 49) {
					if (genderID == 2) {
						nameOfGroup = IWMarathonConstants.FEMALE_40_49;
					} else {
						nameOfGroup = IWMarathonConstants.MALE_40_49;
					}
				} else if (age > 49 && age <= 59) {
					if (genderID == 2) {
						nameOfGroup = IWMarathonConstants.FEMALE_50_59;
					} else {
						nameOfGroup = IWMarathonConstants.MALE_50_59;
					}
				} else if (age > 59) {
					if (genderID == 2) {
						nameOfGroup = IWMarathonConstants.FEMALE_60;
					} else {
						nameOfGroup = IWMarathonConstants.MALE_60;
					}
				}
			}
		}
		if (runName.equals(RUN_LAZY_TOWN_RUN)) {
			if (distance.getName().equals(IWMarathonConstants.DISTANCE_1)) {
				if (age >= 0 && age <= 1) {
					nameOfGroup = IWMarathonConstants.AGE_1;
				} else if (age > 1 && age <= 2) {
					nameOfGroup = IWMarathonConstants.AGE_2;
				} else if (age > 2 && age <= 3) {
					nameOfGroup = IWMarathonConstants.AGE_3;
				} else if (age > 3 && age <= 4) {
					nameOfGroup = IWMarathonConstants.AGE_4;
				} else if (age > 4 && age <= 5) {
					nameOfGroup = IWMarathonConstants.AGE_5;
				} else if (age > 5 && age <= 6) {
					nameOfGroup = IWMarathonConstants.AGE_6;
				} else if (age > 6 && age <= 7) {
					nameOfGroup = IWMarathonConstants.AGE_7;
				} else if (age > 7 && age <= 8) {
					nameOfGroup = IWMarathonConstants.AGE_8;
				} else if (age > 8 && age <= 9) {
					nameOfGroup = IWMarathonConstants.AGE_9;
				} else if (age > 9 && age <= 10) {
					nameOfGroup = IWMarathonConstants.AGE_10;
				}
			}
		} else if (runName.equals(RUN_MIDNIGHT_RUN)) {
			if (age <= 18) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_18;
				} else {
					nameOfGroup = IWMarathonConstants.MALE_18;
				}
			} else if (age > 18 && age <= 39) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_19_39;
				} else {
					nameOfGroup = IWMarathonConstants.MALE_19_39;
				}
			} else if (age > 39 && age <= 49) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_40_49;
				} else {
					nameOfGroup = IWMarathonConstants.MALE_40_49;
				}
			} else if (age > 49) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_50;
				} else {
					nameOfGroup = IWMarathonConstants.MALE_50;
				}
			}
		} else if (runName.equals(RUN_LAUGAVEGUR)
				|| runName.equals(RUN_LAUGAVEGUR_PREREGISTRATION)) {
			if (age > 17 && age <= 29) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_18_29;
				} else {
					nameOfGroup = IWMarathonConstants.MALE_18_29;
				}
			} else if (age > 29 && age <= 39) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_30_39;
				} else {
					nameOfGroup = IWMarathonConstants.MALE_30_39;
				}
			} else if (age > 39 && age <= 49) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_40_49;
				} else {
					nameOfGroup = IWMarathonConstants.MALE_40_49;
				}
			} else if (age > 49 && age <= 59) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_50_59;
				} else {
					nameOfGroup = IWMarathonConstants.MALE_50_59;
				}
			} else if (age > 59) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_60;
				} else {
					nameOfGroup = IWMarathonConstants.MALE_60;
				}
			}
		}
		return nameOfGroup;
	}

	public String[] getDistancesForRun(Group run) {
		String runName = run.getName();
		String[] disForMarathon = { IWMarathonConstants.DISTANCE_42,
				IWMarathonConstants.DISTANCE_21,
				IWMarathonConstants.DISTANCE_10,
				IWMarathonConstants.DISTANCE_3,
				IWMarathonConstants.DISTANCE_CHARITY_42 };
		/*
		 * String[] disForOsloMarathon = { IWMarathonConstants.DISTANCE_42,
		 * IWMarathonConstants.DISTANCE_21, IWMarathonConstants.DISTANCE_3 };
		 */
		String[] disForLaugavegur = { IWMarathonConstants.DISTANCE_55 };
		String[] disForMidnight = { IWMarathonConstants.DISTANCE_10,
				IWMarathonConstants.DISTANCE_5, IWMarathonConstants.DISTANCE_3 };
		/*
		 * String[] disForRollerSkate = { IWMarathonConstants.DISTANCE_10,
		 * IWMarathonConstants.DISTANCE_5 };
		 */
		String[] disForLazyTown = { IWMarathonConstants.DISTANCE_1 };
		/* String[] disForLazyTownOslo = { IWMarathonConstants.DISTANCE_0_5 }; */

		if (runName.equals(RUN_RVK_MARATHON)) {
			return disForMarathon;
		} /*
		 * else if (runName.equals(RUN_OSLO_MARATHON)) { return
		 * disForOsloMarathon; }
		 */else if (runName.equals(RUN_LAZY_TOWN_RUN)) {
			return disForLazyTown;
		} /*
		 * else if (runName.equals(RUN_LAZY_TOWN_MINIMARATON_OSLO)) { return
		 * disForLazyTownOslo; }
		 */else if (runName.equals(RUN_MIDNIGHT_RUN)) {
			return disForMidnight;
		} else if (runName.equals(RUN_LAUGAVEGUR)
				|| runName.equals(RUN_LAUGAVEGUR_PREREGISTRATION)) {
			return disForLaugavegur;
		} /*
		 * else if (runName.equals(RUN_ROLLER_SKATE)) { return
		 * disForRollerSkate; }
		 */

		return null;
	}

	public void createNewGroupYear(IWContext iwc, String runID) {
		String year = iwc.getParameter("year");
		String[] priceISK = iwc.getParameterValues("price_isk");
		String[] priceEUR = iwc.getParameterValues("price_eur");
		String[] useChips = iwc.getParameterValues("use_chip");
		String[] childrenPriceISK = iwc
				.getParameterValues("price_children_isk");
		String[] childrenPriceEUR = iwc
				.getParameterValues("price_children_eur");
		String[] familyDiscount = iwc.getParameterValues("family_discount");
		String[] allowsGroups = iwc.getParameterValues("allows_groups");
		String[] numberOfSplits = iwc.getParameterValues("number_of_splits");
		String[] offersTransport = iwc.getParameterValues("offers_transport");
		String sCharityEnabled = iwc
				.getParameter(CreateYearForm.PARAMETER_CHARITY_ENABLED);
		boolean charityEnabled = false;
		if (sCharityEnabled != null) {
			if (sCharityEnabled.equalsIgnoreCase("Y")) {
				charityEnabled = true;
			}
		}
		String sPledgedBySponsor = iwc
				.getParameter(CreateYearForm.PARAMETER_PLEDGED_BY_SPONSOR);
		int pledgedBySponsor = -1;
		try {
			pledgedBySponsor = Integer.parseInt(sPledgedBySponsor);
		} catch (Exception e) {
		}

		String sPledgedBySponsorGroup = iwc
				.getParameter(CreateYearForm.PARAMETER_PLEDGED_BY_SPONSOR_GROUP);
		int pledgedBySponsorGroup = -1;
		try {
			pledgedBySponsorGroup = Integer.parseInt(sPledgedBySponsorGroup);
		} catch (Exception e) {
		}

		Group run = null;
		try {
			if (runID != null && !runID.equals("")) {
				int id = Integer.parseInt(runID);
				run = getGroupBiz().getGroupByGroupID(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String runName = run.getName();
		Group group = null;
		try {
			group = getGroupBiz().createGroupUnder(year, null, run);
			group.setGroupType(IWMarathonConstants.GROUP_TYPE_RUN_YEAR);
			group.setMetaData(YearBMPBean.METADATA_ENABLE_CHARITY, new Boolean(
					charityEnabled).toString());
			if (pledgedBySponsor != -1) {
				group.setMetaData(
						YearBMPBean.METADATA_PLEDGED_PER_KILOMETER_BY_SPONSOR,
						sPledgedBySponsor);
			}
			if (pledgedBySponsorGroup != -1) {
				group.setMetaData(
						YearBMPBean.METADATA_PLEDGED_PER_KILOMETER_BY_SPONSOR_GROUP,
						sPledgedBySponsorGroup);
			}
			group.store();
		} catch (IBOLookupException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		}
		String[] grForMarathon = { IWMarathonConstants.FEMALE_14,
				IWMarathonConstants.FEMALE_15_17,
				IWMarathonConstants.FEMALE_18_39,
				IWMarathonConstants.FEMALE_40_49,
				IWMarathonConstants.FEMALE_50_59,
				IWMarathonConstants.FEMALE_60, IWMarathonConstants.MALE_14,
				IWMarathonConstants.MALE_15_17, IWMarathonConstants.MALE_18_39,
				IWMarathonConstants.MALE_40_49, IWMarathonConstants.MALE_50_59,
				IWMarathonConstants.MALE_60 };

		String[] grForLazyTown = { IWMarathonConstants.AGE_1,
				IWMarathonConstants.AGE_2, IWMarathonConstants.AGE_3,
				IWMarathonConstants.AGE_4, IWMarathonConstants.AGE_5,
				IWMarathonConstants.AGE_6, IWMarathonConstants.AGE_6,
				IWMarathonConstants.AGE_8, IWMarathonConstants.AGE_9,
				IWMarathonConstants.AGE_10 };

		String[] grForLaugavegur = { IWMarathonConstants.FEMALE_18_29,
				IWMarathonConstants.FEMALE_30_39,
				IWMarathonConstants.FEMALE_40_49,
				IWMarathonConstants.FEMALE_50_59,
				IWMarathonConstants.FEMALE_60, IWMarathonConstants.MALE_18_29,
				IWMarathonConstants.MALE_30_39, IWMarathonConstants.MALE_40_49,
				IWMarathonConstants.MALE_50_59, IWMarathonConstants.MALE_60 };
		String[] grForMidnight = { IWMarathonConstants.FEMALE_18,
				IWMarathonConstants.FEMALE_19_39,
				IWMarathonConstants.FEMALE_40_49,
				IWMarathonConstants.FEMALE_50, IWMarathonConstants.MALE_18,
				IWMarathonConstants.MALE_19_39, IWMarathonConstants.MALE_40_49,
				IWMarathonConstants.MALE_50 };
		// TODO: remove this hack - set metadata on the groups containing the
		// specific run...
		if (runName.equals(RUN_RVK_MARATHON)) {
			generateSubGroups(iwc, group, getDistancesForRun(run),
					grForMarathon, priceISK, priceEUR, useChips,
					childrenPriceISK, childrenPriceEUR, familyDiscount,
					allowsGroups, numberOfSplits, offersTransport);
		} else if (runName.equals(RUN_LAZY_TOWN_RUN)) {
			generateSubGroups(iwc, group, getDistancesForRun(run),
					grForLazyTown, priceISK, priceEUR, useChips,
					childrenPriceISK, childrenPriceEUR, familyDiscount,
					allowsGroups, numberOfSplits, offersTransport);
		} else if (runName.equals(RUN_MIDNIGHT_RUN)) {
			generateSubGroups(iwc, group, getDistancesForRun(run),
					grForMidnight, priceISK, priceEUR, useChips,
					childrenPriceISK, childrenPriceEUR, familyDiscount,
					allowsGroups, numberOfSplits, offersTransport);
		} else if (runName.equals(RUN_LAUGAVEGUR)
				|| runName.equals(RUN_LAUGAVEGUR_PREREGISTRATION)) {
			generateSubGroups(iwc, group, getDistancesForRun(run),
					grForLaugavegur, priceISK, priceEUR, useChips,
					childrenPriceISK, childrenPriceEUR, familyDiscount,
					allowsGroups, numberOfSplits, offersTransport);
		}
	}

	/**
	 * @param iwc
	 * @param group
	 * @param disForMarathon
	 * @param grForMarathon
	 */
	private void generateSubGroups(IWContext iwc, Group group, String[] dis,
			String[] gr, String[] priceISK, String[] priceEUR,
			String[] useChips, String[] childrenPriceISK,
			String[] childrenPriceEUR, String[] familyDiscount,
			String[] allowsGroups, String[] numberOfSplits,
			String[] offersTransport) {
		for (int i = 0; i < dis.length; i++) {
			Group distance = null;
			try {
				distance = getGroupBiz().createGroupUnder(dis[i], null, group);
				distance.setGroupType(IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE);
				distance.store();
				try {
					Distance distanceGroup = ConverterUtility.getInstance()
							.convertGroupToDistance(distance);
					distanceGroup.setUseChip("Y".equals(useChips[i]));
					if (priceISK[i] != null && priceISK[i].length() > 0) {
						distanceGroup.setPriceInISK(Float
								.parseFloat(priceISK[i]));
					} else {
						distanceGroup.setPriceInISK(0);
					}
					if (priceEUR[i] != null && priceEUR[i].length() > 0) {
						distanceGroup.setPriceInEUR(Float
								.parseFloat(priceEUR[i]));
					} else {
						distanceGroup.setPriceInEUR(0);
					}
					if (childrenPriceISK[i] != null
							&& childrenPriceISK[i].length() > 0) {
						distanceGroup.setChildrenPriceInISK(Float
								.parseFloat(childrenPriceISK[i]));
					} else {
						distanceGroup.setChildrenPriceInISK(0);
					}
					if (childrenPriceEUR[i] != null
							&& childrenPriceEUR[i].length() > 0) {
						distanceGroup.setChildrenPriceInEUR(Float
								.parseFloat(childrenPriceEUR[i]));
					} else {
						distanceGroup.setChildrenPriceInEUR(0);
					}

					distanceGroup.setFamilyDiscount("Y"
							.equals(familyDiscount[i]));
					distanceGroup.setAllowsGroups("Y".equals(allowsGroups[i]));
					distanceGroup.setNumberOfSplits(new Integer(
							numberOfSplits[i]).intValue());
					// distanceGroup.setTransportOffered("Y".equals(offersTransport[i]));
					distanceGroup.store();
				} catch (FinderException fe) {
					fe.printStackTrace();
				}
				for (int j = 0; j < gr.length; j++) {
					Group g = null;
					try {
						g = getGroupBiz().createGroupUnder(gr[j], null,
								distance);
						g.setGroupType(IWMarathonConstants.GROUP_TYPE_RUN_GROUP);
						g.store();
					} catch (Exception e) {
					}
				}
			} catch (IBOLookupException e1) {
				e1.printStackTrace();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			} catch (CreateException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * gets all the run type groups of the type
	 * "iwma_run_marathon","iwma_run_laugavegur" and "iwma_run_midnight"
	 * 
	 * @return a Collection of the run types
	 */
	public Collection getRuns() {
		Collection runs = null;
		String[] type = { IWMarathonConstants.GROUP_TYPE_RUN };
		try {
			runs = getGroupBiz().getGroups(type, true);
		} catch (Exception e) {
		}
		return runs;
	}

	public Collection getRunsForUser(User user) {
		Collection groups = null;
		Collection runs = new ArrayList();
		String[] typeRun = { IWMarathonConstants.GROUP_TYPE_RUN };
		String[] typeGroup = { IWMarathonConstants.GROUP_TYPE_RUN_GROUP };
		try {
			groups = getUserBiz().getUserGroups(user, typeGroup, true);
		} catch (IBOLookupException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		if (groups != null) {
			Iterator groupsIter = groups.iterator();
			while (groupsIter.hasNext()) {
				Group group = (Group) groupsIter.next();
				Collection r = null;
				try {
					r = getGroupBiz().getParentGroupsRecursive(group, typeRun,
							true);
				} catch (IBOLookupException e1) {
					e1.printStackTrace();
				} catch (EJBException e1) {
					e1.printStackTrace();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				if (r != null) {
					Iterator rIter = r.iterator();
					while (rIter.hasNext()) {
						Group run = (Group) rIter.next();
						if (run != null) {
							runs.add(run);
						}
					}
				}
			}
		}
		return runs;

	}

	public Collection getRunGroupOfTypeForUser(User user, String type) {
		Collection typeGroups = null;
		Collection runs = new ArrayList();
		String[] typeRun = { type };
		String[] typeGroup = { IWMarathonConstants.GROUP_TYPE_RUN_GROUP };
		try {
			typeGroups = getUserBiz().getUserGroups(user, typeGroup, true);
		} catch (IBOLookupException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		if (typeGroups != null) {
			Iterator groupsIter = typeGroups.iterator();
			while (groupsIter.hasNext()) {
				Group group = (Group) groupsIter.next();
				Collection r = null;
				try {
					r = getGroupBiz().getParentGroupsRecursive(group, typeRun,
							true);
				} catch (IBOLookupException e1) {
					e1.printStackTrace();
				} catch (EJBException e1) {
					e1.printStackTrace();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				if (r != null) {
					Iterator rIter = r.iterator();
					while (rIter.hasNext()) {
						Group run = (Group) rIter.next();
						if (run != null) {
							runs.add(run);
						}
					}
				}
			}
		}
		return runs;

	}

	public Group getRunGroupOfTypeForGroup(Group group, String type) {

		String[] types = { type };
		Collection r = null;
		Group run = null;

		try {
			r = getGroupBiz().getParentGroupsRecursive(group, types, true);
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		if (r != null) {
			Iterator rIter = r.iterator();
			if (rIter.hasNext()) {
				run = (Group) rIter.next();
			}
		}
		return run;
	}

	/**
	 * gets all the "gender/age" groups for the user e.g. "female_14",
	 * "male_14", "female_14_17", "male_14_17", "female_18_39", "male_18_39" ...
	 * 
	 * @return a Collection of the "iwma_run_group" types
	 */
	public Collection getRunGroupsForUser(User user) {
		Collection groups = null;
		String[] typeGroup = { IWMarathonConstants.GROUP_TYPE_RUN_GROUP };
		try {
			groups = getUserBiz().getUserGroups(user, typeGroup, true);
		} catch (IBOLookupException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return groups;
	}

	/**
	 * Gets all the years that exist for a specific run. The years are groups
	 * with the group type "iwma_year"
	 * 
	 * @param run
	 *            - the supergroup of the years
	 * @return Collection of the years for the specific run
	 */
	public Collection getYears(Group run) {
		return getYears(run, null);
	}

	public Collection getYears(Group run, String yearFilter) {
		Collection years = null;
		Collection type = new ArrayList();
		type.add(IWMarathonConstants.GROUP_TYPE_RUN_YEAR);
		try {
			years = getGroupBiz().getChildGroupsResultFiltered(run, yearFilter,
					type, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return years;
	}

	/**
	 * Gets all the years that exist for a specific run. The years are groups
	 * with the group type "iwma_year". This method returns a Map instead of a
	 * Collection. This is for example used to display the years in a secondary
	 * dropdown menu when displaying two connected dropdowns.
	 * 
	 * @param Group
	 *            run - the supergroup of the years
	 * @return Map of the years for the specific run
	 */
	public Map getYearsMap(Group run) {
		return getYearsMap(run, null);
	}

	public Map getYearsMap(Group run, String groupNameFilter) {
		Map yearsMap = new LinkedHashMap();
		Iterator yearsIter = run.getChildrenIterator();
		while (yearsIter.hasNext()) {
			Group year = (Group) yearsIter.next();
			try {
				yearsMap.put(year.getName(), ConverterUtility.getInstance()
						.convertGroupToYear(year));
			} catch (FinderException fe) {
				fe.printStackTrace();
			}
		}
		return yearsMap;
	}

	/**
	 * Gets a Collection of distances for a specific run and year. Distances are
	 * groups with the group type "iwma_distance".
	 * 
	 * @param Group
	 *            run - the supersupergroup of the specific run
	 * @param year
	 *            - the year of the run
	 * @return Collection of all distances for a specific run on a specific year
	 */
	public List getDistancesMap(Group run, String year) {
		List distances = null;
		Collection type = new ArrayList();
		type.add(IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE);
		if (run != null) {
			Collection years = getYears(run, year);
			if (years != null) {
				Iterator yearsIter = years.iterator();
				while (yearsIter.hasNext()) {
					Group y = (Group) yearsIter.next();
					if (y.getName().equals(year)) {
						try {
							distances = new ArrayList(getGroupBiz()
									.getChildGroupsRecursiveResultFiltered(y,
											type, true));
						} catch (Exception e) {
							distances = null;
						}
					}
				}
				if (distances != null && distances.size() > 1) {
					Collections.sort(distances, new RunDistanceComparator());
				}
			}
		}

		return distances;
	}

	public Group getDistanceByUserID(int userID) {
		Group dis = null;
		Collection groups = null;
		String[] type = { IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE };
		try {
			groups = getUserBiz().getUserGroups(getUserBiz().getUser(userID),
					type, true);
		} catch (IBOLookupException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		if (groups != null) {
			Iterator i = groups.iterator();
			while (i.hasNext()) {
				Group g = (Group) i.next();
				dis = g;/*
						 * Group g = (Group) i.next(); Collection parentGr =
						 * null; try { parentGr =
						 * getGroupBiz().getParentGroups(g); } catch
						 * (IBOLookupException e1) { e1.printStackTrace(); }
						 * catch (RemoteException e1) { e1.printStackTrace(); }
						 * if(parentGr != null) { Iterator j =
						 * parentGr.iterator(); while(j.hasNext()) { Group pg =
						 * (Group) j.next();
						 * if(pg.getName().equals(RUN_RVK_MARATHON)) { dis = g;
						 * } } }
						 */
			}
		}
		return dis;

	}

	public Distance getDistanceByID(int ID) {
		Group dis = null;
		try {
			dis = getGroupBiz().getGroupByGroupID(ID);
			return ConverterUtility.getInstance().convertGroupToDistance(dis);
		} catch (IBOLookupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 
	 * @param user
	 * @return an int representing the age of the user
	 */
	public int getUserAge(User user) {
		Date dob = user.getDateOfBirth();
		IWTimestamp t = new IWTimestamp(dob);
		int birthYear = t.getYear();
		IWTimestamp time = IWTimestamp.RightNow();
		int year = time.getYear();
		return year - birthYear;
	}

	/**
	 * Gets all countries. This method is for example used when displaying a
	 * dropdown menu of all countries
	 * 
	 * @return Colleciton of all countries
	 */
	public Collection getCountries() {
		return getCountries(null);
	}

	/**
	 * Gets all countries. This method is for example used when displaying a
	 * dropdown menu of all countries
	 * 
	 * @return Colleciton of all countries
	 */
	public Collection getCountries(String[] presetCountries) {
		List countries = null;
		try {
			CountryHome countryHome = (CountryHome) getIDOHome(Country.class);
			countries = new ArrayList(countryHome.findAll());

			if (presetCountries != null) {
				// iterate reverse through the array to get the correct order:
				for (int i = presetCountries.length - 1; i > -1; i--) {
					String presetCountry = presetCountries[i];
					List tempList = new ArrayList(countries);
					for (Iterator iter = tempList.iterator(); iter.hasNext();) {
						Country country = (Country) iter.next();
						String countryIsoAbbr = country.getIsoAbbreviation();
						if (countryIsoAbbr != null
								&& countryIsoAbbr
										.equalsIgnoreCase(presetCountry)) {
							countries.remove(country);
							countries.add(0, country);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return countries;
	}

	private GroupBusiness getGroupBiz() throws IBOLookupException {
		GroupBusiness business = (GroupBusiness) IBOLookup.getServiceInstance(
				getIWApplicationContext(), GroupBusiness.class);
		return business;
	}

	private CreditCardBusiness getCreditCardBusiness() {
		try {
			return (CreditCardBusiness) IBOLookup.getServiceInstance(
					getIWApplicationContext(), CreditCardBusiness.class);
		} catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	public UserBusiness getUserBiz() throws IBOLookupException {
		UserBusiness business = (UserBusiness) IBOLookup.getServiceInstance(
				getIWApplicationContext(), UserBusiness.class);
		return business;
	}

	public Country getCountryByNationality(Object nationality) {
		Country country = null;
		try {
			CountryHome home = (CountryHome) getIDOHome(Country.class);
			try {
				int countryPK = Integer.parseInt(nationality.toString());
				country = home.findByPrimaryKey(new Integer(countryPK));
			} catch (NumberFormatException nfe) {
				country = home.findByIsoAbbreviation(nationality.toString());
			}
		} catch (FinderException fe) {
			// log(fe);
		} catch (RemoteException re) {
			// log(re);
		}
		return country;
	}

	public AddressHome getAddressHome() {
		if (this.addressHome == null) {
			try {
				this.addressHome = (AddressHome) IDOLookup
						.getHome(Address.class);
			} catch (RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return this.addressHome;
	}

	public Participant getParticipantByPrimaryKey(int participantID) {
		Participant participant = null;
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			participant = runHome.findByPrimaryKey(new Integer(participantID));
		} catch (RemoteException e1) {
		} catch (FinderException e1) {
		}
		return participant;
	}

	private String localizeForRun(String key, String value, Runner runner,
			IWResourceBundle iwrb) {
		if (runner.getRun() != null) {
			String runKey = key + "_runid_" + runner.getRun().getId();
			String localizedString = iwrb.getLocalizedString(runKey);
			if (localizedString == null) {
				localizedString = iwrb.getLocalizedString(key, value);
			}
			return localizedString;
		} else {
			return iwrb.getLocalizedString(key, value);
		}
	}

	public ParticipantHome getParticipantHome() {
		if (this.participantHome == null) {
			try {
				this.participantHome = (ParticipantHome) IDOLookup
						.getHome(Participant.class);
			} catch (RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return this.participantHome;
	}

	public List getDisallowedDistancesPKs(User user, List distances) {

		int age = new Age(user.getDateOfBirth()).getYears();
		List disallowed = new ArrayList();

		for (Iterator iterator = distances.iterator(); iterator.hasNext();) {
			Distance distance = (Distance) iterator.next();

			if ((distance.getMinimumAgeForDistance() > 0 && age < distance
					.getMinimumAgeForDistance())
					|| (distance.getMaximumAgeForDistance() > 0 && age > distance
							.getMaximumAgeForDistance()))
				disallowed.add(distance.getPrimaryKey().toString());
		}

		return disallowed;
	}

	public Collection getConfirmedParticipants() {
		try {
			return getParticipantHome().findAllPaidConfirmation();
		} catch (FinderException e) {
		}

		return null;
	}
}
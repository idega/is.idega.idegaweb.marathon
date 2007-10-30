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
import is.idega.idegaweb.marathon.glitnirws.MarathonWS2Client;
import is.idega.idegaweb.marathon.presentation.CreateYearForm;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;

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
import com.idega.block.creditcard.data.KortathjonustanMerchant;
import com.idega.block.creditcard.data.KortathjonustanMerchantHome;
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
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.UnavailableIWContext;
import com.idega.presentation.IWContext;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Gender;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;
import com.idega.util.Age;
import com.idega.util.IWTimestamp;
import com.idega.util.LocaleUtil;
import com.idega.util.Timer;
import com.idega.util.text.Name;

/**
 * Description: Business bean (service) for run... <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * 
 * @author birna
 */
public class RunBusinessBean extends IBOServiceBean implements RunBusiness {

	private static final String RUN_ROLLER_SKATE = "Roller Skate";
	private static final String RUN_MIDNIGHT_RUN = "Midnight Run";
	private static final String RUN_LAUGAVEGUR = "Laugavegur";
	private static final String RUN_RVK_MARATHON = "Reykjavik Marathon";
	private static final String RUN_OSLO_MARATHON = "Oslo Marathon";
	private static final String RUN_LAZY_TOWN_RUN = "Lazy Town Run";
	private static final String RUN_LAZY_TOWN_MINIMARATON_OSLO = "Lazy Town Minimaraton Oslo";

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
	public User saveUser(String name, String ssn, IWTimestamp dateOfBirth, Gender gender, String address, String postal, String city, Country country) {
		User user = null;
		try {
			if (dateOfBirth == null) {
				dateOfBirth = getBirthDateFromSSN(ssn);
			}
			Name fullName = new Name(name);
			user = getUserBiz().createUser(fullName.getFirstName(), fullName.getMiddleName(), fullName.getLastName(), fullName.getName(), ssn, gender, dateOfBirth);
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
					p = postalHome.findByPostalCodeAndCountryId(postal, countryID.intValue());
				}
				catch (FinderException fe) {
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
				}
				catch (IDOAddRelationshipException idoEx) {
				}
			}
			user.store();
		}
		catch (RemoteException rme) {
		}
		catch (CreateException cre) {
		}
		return user;
	}
	
	public boolean isRegisteredInRun(int runID, int userID) {
		try {
			User user = getUserBiz().getUserHome().findByPrimaryKey(new Integer(userID));
			
			return getUserBiz().isMemberOfGroup(runID, user);
		}
		catch (RemoteException re) {
			log(re);
		}
		catch (FinderException fe) {
			//User does not exist in database...
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
			
			((ParticipantHome) IDOLookup.getHome(Participant.class)).findByUserAndRun(user, run, runYear);
			return true;
		}
		catch (FinderException fe) {
			return false;
		}
		catch (RemoteException ile) {
			throw new IBORuntimeException(ile);
		}
	}
	
	public boolean doesGroupExist(Object distancePK, String groupName) {
		try {
			return ((ParticipantHome) IDOLookup.getHome(Participant.class)).getCountByDistanceAndGroupName(distancePK, groupName) > 0;
		}
		catch (IDOException ie) {
			ie.printStackTrace();
			return false;
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
	
	public boolean isRegisteredInRun(int runID, String personalID) {
		try {
			User user = getUserBiz().getUserHome().findByPersonalID(personalID);
			
			return getUserBiz().isMemberOfGroup(runID, user);
		}
		catch (RemoteException re) {
			log(re);
		}
		catch (FinderException fe) {
			//User does not exist in database...
		}
		return false;
	}

	/**
	 * 
	 * @param pin -
	 *            a social security number - format ddmmyyxxxx or ddmmyyyy
	 * @return IWTimstamp - the date of birth from the pin..
	 */
	private IWTimestamp getBirthDateFromSSN(String pin) {
		//pin format = 14011973
		if (pin.length() == 8) {
			int edd = Integer.parseInt(pin.substring(0, 2));
			int emm = Integer.parseInt(pin.substring(2, 4));
			int eyyyy = Integer.parseInt(pin.substring(4, 8));
			IWTimestamp dob = new IWTimestamp(edd, emm, eyyyy);
			return dob;
		}
		//  pin format = 140173xxxx ddmmyyxxxx
		else if (pin.length() == 10) {
			int dd = Integer.parseInt(pin.substring(0, 2));
			int mm = Integer.parseInt(pin.substring(2, 4));
			int yy = Integer.parseInt(pin.substring(4, 6));
			int century = Integer.parseInt(pin.substring(9, 10));
			int yyyy = 0;
			if (century == 9) {
				yyyy = yy + 1900;
			}
			else if (century == 0) {
				yyyy = yy + 2000;
			}
			IWTimestamp dob = new IWTimestamp(dd, mm, yyyy);
			return dob;
		}
		else {
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

	/**
	 * saves information on the run for the specific user puts user in the right
	 * group
	 */
	public void saveRun(int userID, String run, String distance, String year, String nationality, String tshirt, String chipOwnershipStatus, String chipNumber, String groupName, String bestTime, String goalTime, Locale locale) {
		Group groupRun = null;
		Group disGroup = null;
		int ageGenderGroupID = -1;
		User user = null;
		try {
			groupRun = getGroupBiz().getGroupByGroupID(Integer.parseInt(run));
		}
		catch (IBOLookupException e) {
			e.printStackTrace();
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (UnavailableIWContext e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		String distanceType = null;
		try {
			user = getUserBiz().getUser(userID);
			int age = getUserAge(user);
			if (distance != null && !distance.equals("")) {
				int disGroupID = Integer.parseInt(distance);
				
				try {
					disGroup = getGroupBiz().getGroupByGroupID(disGroupID);
					distanceType = disGroup.getName();
				}
				catch (UnavailableIWContext e1) {
					e1.printStackTrace();
				}
				catch (FinderException e1) {
					e1.printStackTrace();
				}
				String[] groupType = { IWMarathonConstants.GROUP_TYPE_RUN_GROUP };
				Collection groups = getGroupBiz().getChildGroupsRecursive(disGroup, groupType, true);
				Iterator groupsIter = groups.iterator();
				while (groupsIter.hasNext()) {
					Group group = (Group) groupsIter.next();
					if (group.getName().equals(getGroupName(age, groupRun, user.getGenderID(), disGroup))) {
						ageGenderGroupID = Integer.parseInt(group.getPrimaryKey().toString());
						group.addGroup(user);
						group.store();
					}
				}
			}
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			Participant r = runHome.create();
			r.setUserID(userID);
			r.setRunTypeGroupID(Integer.parseInt(run));
			r.setRunDistanceGroupID(Integer.parseInt(distance));
			r.setRunYearGroupID(Integer.parseInt(year));
			if(ageGenderGroupID != -1) {
				r.setRunGroupGroupID(ageGenderGroupID);
			}
			r.setShirtSize(tshirt);
			r.setChipOwnershipStatus(chipOwnershipStatus);
			r.setChipNumber(chipNumber);
			r.setRunGroupName(groupName);
			r.setUserNationality(nationality);
			if (bestTime != null && !bestTime.equals("")) {
				r.setBestTime(bestTime);
			}
			if (goalTime != null && !goalTime.equals("")) {
				r.setGoalTime(goalTime);
			}
			if (distanceType != null) {
				try {
					int participantNumber = runHome.getNextAvailableParticipantNumber(distance, getMinParticipantNumber(distanceType, groupRun.getName()), getMaxParticipantNumber(distanceType, groupRun.getName()));
					if (participantNumber == 0) {
						participantNumber = getMinParticipantNumber(distanceType, groupRun.getName());
					}
					else {
						participantNumber++;
					}
					r.setParticipantNumber(participantNumber);
				}
				catch (IDOException ie) {
					ie.printStackTrace();
				}
			}
			r.store();

			Email email = getUserBiz().getUserMail(user);
			if (groupRun != null && user != null && email != null && email.getEmailAddress() != null) {
				IWResourceBundle iwrb = getIWApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).getResourceBundle(locale);
				Object[] args = { user.getName(), iwrb.getLocalizedString(groupRun.getName(),groupRun.getName()), iwrb.getLocalizedString(disGroup.getName(),disGroup.getName()), iwrb.getLocalizedString(tshirt, tshirt) };
				String subject = iwrb.getLocalizedString("registration_received_subject_mail", "Your registration has been received.");
				String body = MessageFormat.format(iwrb.getLocalizedString("registration_received_body_mail", "Your registration has been received."), args);
				sendMessage(email.getEmailAddress(), subject, body);
			}
		}
		catch (RemoteException rme) {
		}
		catch (CreateException cre) {
		}
	}
	
	public Participant importParticipant(User user, Group run, Group year, Group distance) throws CreateException {
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
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public Collection saveParticipants(Collection runners, String email, String hiddenCardNumber, double amount, IWTimestamp date, Locale locale, boolean disableSendPaymentConfirmation) throws IDOCreateException {
		Collection participants = new ArrayList();

		UserTransaction trans = getSessionContext().getUserTransaction();
		try {
			trans.begin();
			Iterator iter = runners.iterator();
			Group run = null;
			Group distance = null;
			Run selectedRun = null;
			while (iter.hasNext()) {
				Runner runner = (Runner) iter.next();
				User user = runner.getUser();
				String personalId = null;
				if (user == null) {
					personalId = runner.getPersonalID();
					user = saveUser(runner.getName(), personalId, new IWTimestamp(runner.getDateOfBirth()), runner.getGender(), runner.getAddress(), runner.getPostalCode(), runner.getCity(), runner.getCountry());
				} else {
					personalId = user.getPersonalID();
				}
				String userNameString = "";
				String passwordString = "";

				if (getUserBiz().hasUserLogin(user)) {
					try {
						LoginTable login = LoginDBHandler.getUserLogin(user);
						userNameString = login.getUserLogin();
						passwordString = LoginDBHandler.getGeneratedPasswordForUser();
						LoginDBHandler.changePassword(login, passwordString);
					} catch (Exception e) {
						System.out.println("Error re-generating password for user: " + user.getName());
						e.printStackTrace();
					}
				} else {
					try {
						LoginTable login = getUserBiz().generateUserLogin(user);
						userNameString = login.getUserLogin();
						passwordString = login.getUnencryptedUserPassword();
					} catch (Exception e) {
						System.out.println("Error creating login for user: " + user.getName());
						e.printStackTrace();
					}
				}
				
				Group ageGenderGroup = getAgeGroup(user, runner.getRun(), runner.getDistance());
				ageGenderGroup.addGroup(user);
				Group yearGroup = (Group) runner.getDistance().getParentNode();
				run = runner.getRun();
				distance = runner.getDistance();
				selectedRun = null;
				try {
					selectedRun = ConverterUtility.getInstance().convertGroupToRun(run);
				} catch (FinderException e) {
					//Run not found
				}
				
				try {
					ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
					Participant participant = runHome.create();
					participant.setUser(user);
					participant.setRunTypeGroup(run);
					participant.setRunDistanceGroup(distance);
					participant.setRunYearGroup(yearGroup);
					participant.setRunGroupGroup(ageGenderGroup);
					participant.setMaySponsorContact(runner.isMaySponsorContactRunner());
					if(runner.isParticipateInCharity()){
						Charity charity = runner.getCharity();
						if(charity!=null){
							participant.setCharityId(charity.getOrganizationalID());
						}
					}
					if (runner.getCategory()!= null) {
						participant.setCategoryId(((Integer)runner.getCategory().getPrimaryKey()).intValue());
					}
					if (runner.getAmount() > 0) {
						participant.setPayedAmount(String.valueOf(runner.getAmount()));
					}
					
					participant.setShirtSize(runner.getShirtSize());
					if (runner.isOwnChip()) {
						participant.setChipOwnershipStatus(IWMarathonConstants.CHIP_OWN);
					}
					else if (runner.isRentChip()) {
						participant.setChipOwnershipStatus(IWMarathonConstants.CHIP_RENT);
					}
					else if (runner.isBuyChip()) {
						participant.setChipOwnershipStatus(IWMarathonConstants.CHIP_BUY);
					}
					participant.setChipNumber(runner.getChipNumber());
					participant.setUserNationality(runner.getNationality().getName());
					if (runner.getDistance() != null) {
						participant.setParticipantNumber(getNextAvailableParticipantNumber(runner.getRun(), runner.getDistance()));
					}
					participant.setTransportOrdered(String.valueOf(runner.isTransportOrdered()));
					participant.setApplyForDomesticTravelSupport(runner.isApplyForDomesticTravelSupport());
					participant.setApplyForInternationalTravelSupport(runner.isApplyForInternationalTravelSupport());
					participant.setSponsoredRunner(runner.isSponsoredRunner());
					//check customer:
					boolean enableCustomerWebservice = "true".equalsIgnoreCase(getIWMainApplication().getSettings().getProperty("MARATHON_ENABLE_CUSTOMER_WS","false"));
					if (personalId != null && enableCustomerWebservice) {
						Timer wsTimer = new Timer();
						wsTimer.start();
						try{
							MarathonWS2Client wsClient = new MarathonWS2Client(getIWMainApplication());
							if (getUserBiz().hasValidIcelandicSSN(user)) {
								
								if(wsClient.erIVidskiptumVidGlitni(user.getPersonalID())){
									participant.setCustomer(true);
								}
								else{
									participant.setCustomer(false);
								}
							} else {
								participant.setCustomer(false);
							}
						}
						catch(Exception e){
							System.out.println("Lookup to the GlitnirCustomerWebService failed: " + e.getMessage());
							//e.printStackTrace();
						}
						wsTimer.stop();
						System.out.println("Time to execute GlitnirCustomerWebService was: " + wsTimer.getTimeString());
					}
					participant.store();
					participants.add(participant);
					
					getUserBiz().updateUserHomePhone(user, runner.getHomePhone());
					getUserBiz().updateUserMobilePhone(user, runner.getMobilePhone());
					getUserBiz().updateUserMail(user, runner.getEmail());

					if (runner.getEmail() != null) {
						IWResourceBundle iwrb = getIWApplicationContext().getIWMainApplication().getBundle(IWMarathonConstants.IW_BUNDLE_IDENTIFIER).getResourceBundle(locale);
						String distanceString = iwrb.getLocalizedString(distance.getName(),distance.getName());
						if (runner.isTransportOrdered()) {
							distanceString = distanceString + " (" + iwrb.getLocalizedString("run_reg.with_bus_trip","with bus trip") + ")";
						}
						String informationPageString = "";
						String runHomePageString = "";
						if (selectedRun != null) {
							runHomePageString = selectedRun.getRunHomePage();
							if (locale.equals(LocaleUtil.getIcelandicLocale())) {
								informationPageString = selectedRun.getRunInformationPage();
							} else {
								informationPageString = selectedRun.getEnglishRunInformationPage();
							}
						}
						Object[] args = { user.getName(), iwrb.getLocalizedString(run.getName(),run.getName()), distanceString, iwrb.getLocalizedString("shirt_size." + runner.getShirtSize(), runner.getShirtSize()), String.valueOf(participant.getParticipantNumber()), runHomePageString, informationPageString, userNameString, passwordString };
						String subject = iwrb.getLocalizedString("registration_received_subject_mail", "Your registration has been received.");
						String body = MessageFormat.format(localizeForRun("registration_received_body_mail", "Your registration has been received.", runner, iwrb), args);
						sendMessage(runner.getEmail(), subject, body);
					}
					

					
					sendSponsorEmail(runner,locale);
				}
				catch (CreateException ce) {
					ce.printStackTrace();
				}
				catch (RemoteException re) {
					throw new IBORuntimeException(re);
				}
			}
			if (email != null && !disableSendPaymentConfirmation && amount > 0) {
				IWResourceBundle iwrb = getIWApplicationContext().getIWMainApplication().getBundle(IWMarathonConstants.IW_BUNDLE_IDENTIFIER).getResourceBundle(locale);
				String runName = "";
				String runHomePage = "";
				if (selectedRun != null) {
					runName = iwrb.getLocalizedString(selectedRun.getName(), selectedRun.getName());
					runHomePage = selectedRun.getRunHomePage();
				}
				
				Object[] args = { hiddenCardNumber, String.valueOf(amount), date.getLocaleDateAndTime(locale, IWTimestamp.SHORT, IWTimestamp.SHORT), runName, runHomePage };
				String subject = iwrb.getLocalizedString("receipt_subject_mail", "Your receipt for registration");
				String body = MessageFormat.format(iwrb.getLocalizedString("receipt_body_mail", "Your registration has been received."), args);
				sendMessage(email, subject, body);
			}
			trans.commit();
		}
		catch (Exception ex) {
			try {
				trans.rollback();
			}
			catch (javax.transaction.SystemException e) {
				throw new IDOCreateException(e.getMessage());
			}
			ex.printStackTrace();
			throw new IDOCreateException(ex);
		}
		
		return participants;
	}
	
	private void sendSponsorEmail(Runner runner,Locale locale) {
		
		if(runner.isMaySponsorContactRunner()){
			try{
				User user = runner.getUser();
				if(user!=null){
					String name = runner.getUser().getName();
					String personalId = runner.getPersonalID();
					String sAddress = "";
					Collection addresses = runner.getUser().getAddresses();
					if(addresses.size()>0){
						for (Iterator iter = addresses.iterator(); iter.hasNext();) {
							Address address = (Address) iter.next();
							sAddress=address.getName()+" "+address.getPostalAddress();
						}
						
					}
					String phone = runner.getHomePhone();
					String mobilePhone = runner.getMobilePhone();
					String runnerEmail = runner.getEmail();
					String sponsorEmail = getIWApplicationContext().getIWMainApplication().getSettings().getProperty("MARATHON_SPONSOR_EMAIL");
					
					IWResourceBundle iwrb = getIWApplicationContext().getIWMainApplication().getBundle(IWMarathonConstants.IW_BUNDLE_IDENTIFIER).getResourceBundle(locale);
					Object[] args = { name, personalId, sAddress, phone, mobilePhone,runnerEmail };
					String subject = iwrb.getLocalizedString("sponsor_mail_subject", "Request for invitation to be a customer from marathon.is");
					String body = MessageFormat.format(iwrb.getLocalizedString("sponsor_mail_body", "Name: {0}\nPersonal ID: {1}\nAddress: {2}\nPhone: {3}\nMobile Phone: {4}\nE-mail: {5}"), args);
					sendMessage(sponsorEmail, subject, body);
				}
				else{
					logWarning("User is null in sendSponsorEmail for runner: "+runner.getName()+", "+runner.getEmail());
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}

	public int getNextAvailableParticipantNumber(Group run, Distance distance) {
		int number = distance.getNextAvailableParticipantNumber();
		int minNumber = getMinParticipantNumber(distance.getName(), run.getName());
		int maxNumber = getMaxParticipantNumber(distance.getName(), run.getName());
		if (number == -1) {
			number = minNumber;
		}
		if (number > maxNumber) {
			return minNumber;
		}
		
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			while (number <= maxNumber) {
				if (runHome.getCountByDistanceAndNumber(distance.getPrimaryKey(), number) == 0) {
					distance.setNextAvailableParticipantNumber(number + 1);
					distance.store();
					return number;
				}
				else {
					number++;
				}
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
		catch (IDOException ie) {
			ie.printStackTrace();
		}
		return minNumber;
	}
	
	public void addParticipantsToGroup(String[] participants, String groupName) {
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			
			for (int i = 0; i < participants.length; i++) {
				try {
					Participant participant = runHome.findByPrimaryKey(new Integer(participants[i]));
					participant.setRunGroupName(groupName);
					participant.store();
				}
				catch (FinderException fe) {
					fe.printStackTrace();
				}
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	private Group getAgeGroup(User user, Run run, Distance distance) {
		return getAgeGroup(user, (Group) run, distance);
	}
	
	public Group getAgeGroup(User user, Group run, Group distance) {
		Year year = null;
		try {
			year = ConverterUtility.getInstance().convertGroupToYear((Group) distance.getParentNode());
		}
		catch (FinderException fe) {
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
			Collection groups = getGroupBiz().getChildGroupsRecursive(distance, groupType, true);
			
			Iterator groupsIter = groups.iterator();
			while (groupsIter.hasNext()) {
				Group group = (Group) groupsIter.next();
				if (group.getName().equals(getGroupName(age.getYears(runDate.getDate()), run, user.getGenderID(), distance))) {
					return group;
				}
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
		return null;
	}
	
	public void finishPayment(String properties) throws CreditCardAuthorizationException {
		try {
			CreditCardClient client = getCreditCardBusiness().getCreditCardClient(getCreditCardMerchant());
			client.finishTransaction(properties);
		}
		catch (CreditCardAuthorizationException ccae) {
			throw ccae;
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			throw new CreditCardAuthorizationException("Online payment failed. Unknown error.");
		}
	}
	
	public String authorizePayment(String nameOnCard, String cardNumber, String monthExpires, String yearExpires, String ccVerifyNumber, double amount, String currency, String referenceNumber) throws CreditCardAuthorizationException {
		try {
			CreditCardClient client = getCreditCardBusiness().getCreditCardClient(getCreditCardMerchant());
			return client.creditcardAuthorization(nameOnCard, cardNumber, monthExpires, yearExpires, ccVerifyNumber, amount, currency, referenceNumber);
		}
		catch (CreditCardAuthorizationException ccae) {
			throw ccae;
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			throw new CreditCardAuthorizationException("Online payment failed. Unknown error.");
		}
	}
	
	public float getPriceForRunner(Runner runner, Locale locale, float chipDiscount, float chipPrice) {
		Age age = null;
		if (runner.getUser() != null) {
			int groupID = Integer.parseInt(getIWApplicationContext().getApplicationSettings().getProperty(IWMarathonConstants.PROPERTY_SPONSOR_GROUP_ID, "-1"));
			if (groupID != -1 && runner.getYear().isSponsoredRun()) {
				try {
					if (getUserBiz().isMemberOfGroup(groupID, runner.getUser())) {
						runner.setSponsoredRunner(true);
						return 0;
					}
				}
				catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			
			age = new Age(runner.getUser().getDateOfBirth());
		}
		else {
			age = new Age(runner.getDateOfBirth());
		}
		boolean isChild = age.getYears() <= 12;
		
		float runnerPrice = isChild ? runner.getDistance().getChildrenPrice(locale) : runner.getDistance().getPrice(locale);
		if (runner.isOwnChip() || runner.isBuyChip()) {
			runnerPrice = runnerPrice - chipDiscount;
			if (runner.isBuyChip()) {
				runnerPrice += chipPrice;
			}
		}
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
							childCounter.put(pat, new Integer(((Integer) childCounter.get(pat)).intValue()+1));
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
		Collection tempRunners = new ArrayList(runners);

		try {
			Runner parent1 = null;
			Runner parent2 = null;

			boolean hasParents = false;
			Iterator iter = tempRunners.iterator();
			while (iter.hasNext()) {
				Runner runner = (Runner) iter.next();
				Iterator iterator = tempRunners.iterator();
				while (iterator.hasNext()) {
					Runner otherRunner = (Runner) iterator.next();
					if (runner.getUser() != null && otherRunner.getUser() != null && !runner.getUser().equals(otherRunner.getUser()) && runner.getDistance().isFamilyDiscount()) {
						if (getFamilyLogic().isSpouseOf(runner.getUser(), otherRunner.getUser()) && otherRunner.getDistance().isFamilyDiscount()) {
							hasParents = true;
							parent1 = runner;
							parent2 = otherRunner;
							break;
						}
					}
				}
				if (hasParents) {
					break;
				}
			}
			
			if (hasParents) {
				tempRunners.remove(parent1);
				tempRunners.remove(parent2);
				
				Iterator iterator = tempRunners.iterator();
				while (iterator.hasNext()) {
					Runner runner = (Runner) iterator.next();
					if (getFamilyLogic().isParentOf(parent1.getUser(), runner.getUser()) || getFamilyLogic().isParentOf(parent2.getUser(), runner.getUser())) {
						if (runner.getDistance().isFamilyDiscount()) {
							numberOfChildren++;
						}
					}
				}
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
		
		return numberOfChildren;
	}
	
	private FamilyLogic getFamilyLogic() {
		try {
			return (FamilyLogic) getServiceInstance(FamilyLogic.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
	
	public Collection getCreditCardImages() {
		try {
			return getCreditCardBusiness().getCreditCardTypeImages(getCreditCardBusiness().getCreditCardClient(getCreditCardMerchant()));
		}
		catch (FinderException fe) {
			fe.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}
	
	private CreditCardMerchant getCreditCardMerchant() throws FinderException {
		String merchantPK = getIWApplicationContext().getIWMainApplication().getBundle(IWMarathonConstants.IW_BUNDLE_IDENTIFIER).getProperty(IWMarathonConstants.PROPERTY_MERCHANT_PK);
		if (merchantPK != null) {
			try {
				return ((KortathjonustanMerchantHome) IDOLookup.getHome(KortathjonustanMerchant.class)).findByPrimaryKey(new Integer(merchantPK));
			}
			catch (IDOLookupException ile) {
				throw new IBORuntimeException(ile);
			}
		}
		return null;
	}
	
	public void savePayment(int userID, int distanceID, String payMethod, String amount) {
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			Participant run = runHome.findByUserIDandDistanceID(userID,distanceID);
			if(run != null) {
				run.setPayMethod(payMethod);
				run.setPayedAmount(amount);
				run.store();
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public void updateRunForParticipant(Participant participant, int bibNumber, String runTime, String chipTime, String splitTime1, String splitTime2) {
		if(runTime != null) {
			runTime = runTime.trim();
			if (!runTime.equals("")) {
				participant.setRunTime(convertTimeToInt(runTime));
			}
		}
		if(chipTime != null) {
			chipTime = chipTime.trim();
			if(!chipTime.equals("")) {
				participant.setChipTime(convertTimeToInt(chipTime));
			}
		}
		if(splitTime1 != null) {
			splitTime1 = splitTime1.trim();
			if(!splitTime1.equals("")) {
				participant.setSplitTime1(convertTimeToInt(splitTime1));
			}
		}
		if(splitTime2 != null) {
			splitTime2 = splitTime2.trim();
			if(!splitTime2.equals("")) {
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
		}
		else {
			minutes = Integer.parseInt(time);
		}
		seconds += hours * 60 * 60;
		seconds += minutes * 60;
		
		return seconds;
	}
	
	public Participant getRunObjByUserIDandYearID(int userID, int yearID) {
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			return runHome.findByUserIDandYearID(userID,yearID);
		}
		catch (RemoteException e) {
			log(e);
		}
		catch (FinderException e) {
			log(e);
		}
		 
		return null;
	}
	
	public Participant getRunObjByUserIDandDistanceID(int userID, int distanceID) {
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			return runHome.findByUserIDandDistanceID(userID,distanceID);
		}
		catch (RemoteException e) {
			log(e);
		}
		catch (FinderException e) {
			log(e);
		}
		 
		return null;
	}
	
	public Participant getParticipantByDistanceAndParticipantNumber(Object distancePK, int participantNumber) throws FinderException {
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			return runHome.findByDistanceAndParticipantNumber(distancePK, participantNumber);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}
	
	public Collection getParticipantsByYearAndTeamName(Object yearPK, String teamName) throws FinderException {
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			return runHome.findByYearAndTeamName(yearPK, teamName);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}
	
	public Collection getParticipantsByUser(User user) throws FinderException {
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			return runHome.findByUserID(((Integer)user.getPrimaryKey()).intValue());
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}

	public Participant getParticipantByRunAndYear(User user, Group run, Group year) throws FinderException {
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			return runHome.findByUserAndRun(user, run, year);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}
	
	public Group getRunGroupByGroupId(Integer groupId) {
		try {
			GroupHome groupHome = (GroupHome) getIDOHome(Group.class);
			return groupHome.findByPrimaryKey(groupId);
		}
		catch (RemoteException e) {
			log(e);
		}
		catch (FinderException e) {
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
			if(group != null) {
				parentGroups = getGroupBiz().getParentGroupsRecursive(group);
			}
		}
		catch (IBOLookupException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		if(parentGroups != null && !parentGroups.isEmpty()) {
			Iterator groupIter = parentGroups.iterator();
			while(groupIter.hasNext()) {
				Group parentGroup = (Group) groupIter.next();
				if(parentGroup.getGroupType().equals(IWMarathonConstants.GROUP_TYPE_RUN)) {
					runGroupID = Integer.parseInt(parentGroup.getPrimaryKey().toString());
				}
				else if(parentGroup.getGroupType().equals(IWMarathonConstants.GROUP_TYPE_RUN_YEAR)) {
					yearGroupID = Integer.parseInt(parentGroup.getPrimaryKey().toString());
				}
				else if(parentGroup.getGroupType().equals(IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE)) {
					distanceGroupID = Integer.parseInt(parentGroup.getPrimaryKey().toString());
				}
			}
		}
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			if(runGroupID != -1 && yearGroupID != -1 && distanceGroupID != -1) {
				Collection runObjs = runHome.findByUserAndParentGroup(userID,runGroupID,2004,distanceGroupID);
				if(runObjs != null && !runObjs.isEmpty()) {
					Iterator runIt = runObjs.iterator();
					while(runIt.hasNext()) {
						Participant runObj = (Participant) runIt.next();
						run = runObj;
					}
				}
			}
		}
		catch (RemoteException e1) {
			e1.printStackTrace();
		}
		catch (FinderException e1) {
			e1.printStackTrace();
		}
		return run;
	}
	
	public Collection getRunnersByDistance(Group distance, Group runGroup) {
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);
			return runHome.findAllByDistanceAndGroup(distance, runGroup);
		}
		catch (RemoteException e) {
			log(e);
		}
		catch (FinderException e) {
			log(e);
		}
		 
		return new ArrayList();
	}
	
	public void setParticipantNumber(Participant participant, String run) {
		try {
			ParticipantHome runHome = (ParticipantHome) getIDOHome(Participant.class);

			int groupID = participant.getRunDistanceGroupID();
			Group group = null;
			try {
				group = getGroupBiz().getGroupByGroupID(groupID);
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
			if(group!=null) {
				int participantNumber = runHome.getNextAvailableParticipantNumber(group.getPrimaryKey(), getMinParticipantNumber(group.getName(), run), getMaxParticipantNumber(group.getName(), run));
				if (participantNumber == 0) {
					participantNumber = getMinParticipantNumber(group.getName(), run);
				}
				else {
					participantNumber++;
				}
				participant.setParticipantNumber(participantNumber);
				participant.store();

			}
		}
		catch (IDOException ie) {
			log(ie);
		}
		catch (RemoteException re) {
			log(re);
		}
	}

	public void sendMessage(String email, String subject, String body) {

		boolean sendEmail = true;
		String sSendEmail = this.getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).getProperty(IWMarathonConstants.PROPERTY_SEND_EMAILS);
		if ("no".equalsIgnoreCase(sSendEmail)) {
			sendEmail = false;
		}
		
		if (sendEmail) {
			String mailServer = DEFAULT_SMTP_MAILSERVER;
			String fromAddress = DEFAULT_MESSAGEBOX_FROM_ADDRESS;
			String cc = DEFAULT_CC_ADDRESS;
			try {
				MessagingSettings messagingSetting = getIWApplicationContext().getIWMainApplication().getMessagingSettings();
				mailServer = messagingSetting.getSMTPMailServer();
				fromAddress = messagingSetting.getFromMailAddress();
				cc = getIWApplicationContext().getIWMainApplication().getSettings().getProperty("messagebox_cc_receiver_address","");
			}
			catch (Exception e) {
				System.err.println("MessageBusinessBean: Error getting mail property from bundle");
				e.printStackTrace();
			}
			
			try {
				com.idega.util.SendMail.send(fromAddress, email.trim(), cc, "", mailServer, subject, body);
			}
			catch (javax.mail.MessagingException me) {
				System.err.println("MessagingException when sending mail to address: " + email + " Message was: " + me.getMessage());
			}
			catch (Exception e) {
				System.err.println("Exception when sending mail to address: " + email + " Message was: " + e.getMessage());
			}
		}
	}

	/**
	 * @param age
	 * @param nameOfGroup
	 * @return
	 */
	private String getGroupName(int age, Group group, int genderID, Group distance) {
		String runName = group.getName();
		String nameOfGroup = "";
		if (runName.equals(RUN_RVK_MARATHON) || runName.equals(RUN_ROLLER_SKATE)) {
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
					}
					else {
						nameOfGroup = IWMarathonConstants.MALE_14;
					}
				}
				else if (age > 14 && age <= 17) {
					if (genderID == 2) {
						nameOfGroup = IWMarathonConstants.FEMALE_15_17;
					}
					else {
						nameOfGroup = IWMarathonConstants.MALE_15_17;
					}
				}
				else if (age > 17 && age <= 39) {
					if (genderID == 2) {
						nameOfGroup = IWMarathonConstants.FEMALE_18_39;
					}
					else {
						nameOfGroup = IWMarathonConstants.MALE_18_39;
					}
				}
				else if (age > 39 && age <= 49) {
					if (genderID == 2) {
						nameOfGroup = IWMarathonConstants.FEMALE_40_49;
					}
					else {
						nameOfGroup = IWMarathonConstants.MALE_40_49;
					}
				}
				else if (age > 49 && age <= 59) {
					if (genderID == 2) {
						nameOfGroup = IWMarathonConstants.FEMALE_50_59;
					}
					else {
						nameOfGroup = IWMarathonConstants.MALE_50_59;
					}
				}
				else if (age > 59) {
					if (genderID == 2) {
						nameOfGroup = IWMarathonConstants.FEMALE_60;
					}
					else {
						nameOfGroup = IWMarathonConstants.MALE_60;
					}
				}
			}
		}
		if (runName.equals(RUN_LAZY_TOWN_RUN)) {
			if (distance.getName().equals(IWMarathonConstants.DISTANCE_1)) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_9;
				} else {
					nameOfGroup = IWMarathonConstants.MALE_9;
				}
			}
		}
		else if (runName.equals(RUN_LAZY_TOWN_MINIMARATON_OSLO)) {
			if (distance.getName().equals(IWMarathonConstants.DISTANCE_0_5)) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_9;
				} else {
					nameOfGroup = IWMarathonConstants.MALE_9;
				}
			}
		}
		else if (runName.equals(RUN_OSLO_MARATHON)) {
			if (age >= 18 && age <= 22) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_18_22;
				}
				else {
					nameOfGroup = IWMarathonConstants.MALE_18_22;
				}
			}
			else if (age > 22 && age <= 34) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_23_34;
				}
				else {
					nameOfGroup = IWMarathonConstants.MALE_23_34;
				}
			}
			else if (age > 34 && age <= 39) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_35_39;
				}
				else {
					nameOfGroup = IWMarathonConstants.MALE_35_39;
				}
			}
			else if (age > 39 && age <= 44) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_40_44;
				}
				else {
					nameOfGroup = IWMarathonConstants.MALE_40_44;
				}
			}
			else if (age > 44 && age <= 49) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_45_49;
				}
				else {
					nameOfGroup = IWMarathonConstants.MALE_45_49;
				}
			}
			else if (age > 49 && age <= 54) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_50_54;
				}
				else {
					nameOfGroup = IWMarathonConstants.MALE_50_54;
				}
			}
			else if (age > 54 && age <= 59) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_55_59;
				}
				else {
					nameOfGroup = IWMarathonConstants.MALE_55_59;
				}
			}
			else if (age > 59 && age <= 64) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_60_64;
				}
				else {
					nameOfGroup = IWMarathonConstants.MALE_60_64;
				}
			}
			else if (age > 64 && age <= 69) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_65_69;
				}
				else {
					nameOfGroup = IWMarathonConstants.MALE_65_69;
				}
			}
			else if (age > 69 && age <= 74) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_70_74;
				}
				else {
					nameOfGroup = IWMarathonConstants.MALE_70_74;
				}
			}
			else if (age > 74 && age <= 79) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_75_79;
				}
				else {
					nameOfGroup = IWMarathonConstants.MALE_75_79;
				}
			}
			else if (age > 79 && age <= 84) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_80_84;
				}
				else {
					nameOfGroup = IWMarathonConstants.MALE_80_84;
				}
			}
			else if (age > 84 && age <= 99) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_85_99;
				}
				else {
					nameOfGroup = IWMarathonConstants.MALE_85_99;
				}
			}
		}
		else if (runName.equals(RUN_MIDNIGHT_RUN)) {
			if (age <= 18) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_18;
				}
				else {
					nameOfGroup = IWMarathonConstants.MALE_18;
				}
			}
			else if (age > 18 && age <= 39) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_19_39;
				}
				else {
					nameOfGroup = IWMarathonConstants.MALE_19_39;
				}
			}
			else if (age > 39 && age <= 49) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_40_49;
				}
				else {
					nameOfGroup = IWMarathonConstants.MALE_40_49;
				}
			}
			else if (age > 49) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_50;
				}
				else {
					nameOfGroup = IWMarathonConstants.MALE_50;
				}
			}
		}
		else if (runName.equals(RUN_LAUGAVEGUR)) {
			if (age > 17 && age <= 29) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_18_29;
				}
				else {
					nameOfGroup = IWMarathonConstants.MALE_18_29;
				}
			}
			else if (age > 29 && age <= 39) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_30_39;
				}
				else {
					nameOfGroup = IWMarathonConstants.MALE_30_39;
				}
			}
			else if (age > 39 && age <= 49) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_40_49;
				}
				else {
					nameOfGroup = IWMarathonConstants.MALE_40_49;
				}
			}
			else if (age > 49 && age <= 59) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_50;
				}
				else {
					nameOfGroup = IWMarathonConstants.MALE_50_59;
				}
			}
			else if (age > 59) {
				if (genderID == 2) {
					nameOfGroup = IWMarathonConstants.FEMALE_50;
				}
				else {
					nameOfGroup = IWMarathonConstants.MALE_60;
				}
			}
		}
		return nameOfGroup;
	}
	
	public String[] getDistancesForRun(Group run) {
		String runName = run.getName();
		String[] disForMarathon = { IWMarathonConstants.DISTANCE_42, IWMarathonConstants.DISTANCE_21, IWMarathonConstants.DISTANCE_10, IWMarathonConstants.DISTANCE_3, IWMarathonConstants.DISTANCE_CHARITY_42 };
		String[] disForOsloMarathon = { IWMarathonConstants.DISTANCE_42, IWMarathonConstants.DISTANCE_21, IWMarathonConstants.DISTANCE_3 };
		String[] disForLaugavegur = { IWMarathonConstants.DISTANCE_55 };
		String[] disForMidnight = { IWMarathonConstants.DISTANCE_10, IWMarathonConstants.DISTANCE_5, IWMarathonConstants.DISTANCE_3 };
		String[] disForRollerSkate = { IWMarathonConstants.DISTANCE_10, IWMarathonConstants.DISTANCE_5 };
		String[] disForLazyTown = { IWMarathonConstants.DISTANCE_1 };
		String[] disForLazyTownOslo = { IWMarathonConstants.DISTANCE_0_5 };

		if (runName.equals(RUN_RVK_MARATHON)) {
			return disForMarathon;
		}
		else if (runName.equals(RUN_OSLO_MARATHON)) {
			return disForOsloMarathon;
		}
		else if (runName.equals(RUN_LAZY_TOWN_RUN)) {
			return disForLazyTown;
		}
		else if (runName.equals(RUN_LAZY_TOWN_MINIMARATON_OSLO)) {
			return disForLazyTownOslo;
		}
		else if (runName.equals(RUN_MIDNIGHT_RUN)) {
			return disForMidnight;
		}
		else if (runName.equals(RUN_LAUGAVEGUR)) {
			return disForLaugavegur;
		}
		else if (runName.equals(RUN_ROLLER_SKATE)) {
			return disForRollerSkate;
		}
		return null;
	}

	public void createNewGroupYear(IWContext iwc, String runID) {
		String year = iwc.getParameter("year");
		String[] priceISK = iwc.getParameterValues("price_isk");
		String[] priceEUR = iwc.getParameterValues("price_eur");
		String[] useChips = iwc.getParameterValues("use_chip");
		String[] childrenPriceISK = iwc.getParameterValues("price_children_isk");
		String[] childrenPriceEUR = iwc.getParameterValues("price_children_eur");
		String[] familyDiscount = iwc.getParameterValues("family_discount");
		String[] allowsGroups = iwc.getParameterValues("allows_groups");
		String[] numberOfSplits = iwc.getParameterValues("number_of_splits");
		String[] offersTransport = iwc.getParameterValues("offers_transport");
		String sCharityEnabled = iwc.getParameter(CreateYearForm.PARAMETER_CHARITY_ENABLED);
		boolean charityEnabled = false;
		if(sCharityEnabled!=null){
			if(sCharityEnabled.equalsIgnoreCase("Y")){
				charityEnabled=true;
			}
		}
		String sPledgedBySponsor = iwc.getParameter(CreateYearForm.PARAMETER_PLEDGED_BY_SPONSOR);
		int pledgedBySponsor = -1;
		try{
			pledgedBySponsor = Integer.parseInt(sPledgedBySponsor);
		}
		catch(Exception e){
		}
		
		String sPledgedBySponsorGroup = iwc.getParameter(CreateYearForm.PARAMETER_PLEDGED_BY_SPONSOR_GROUP);
		int pledgedBySponsorGroup = -1;
		try{
			pledgedBySponsorGroup = Integer.parseInt(sPledgedBySponsorGroup);
		}
		catch(Exception e){
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
			group.setMetaData(YearBMPBean.METADATA_ENABLE_CHARITY,new Boolean(charityEnabled).toString());
			if(pledgedBySponsor!=-1){
				group.setMetaData(YearBMPBean.METADATA_PLEDGED_PER_KILOMETER_BY_SPONSOR, sPledgedBySponsor);
			}
			if(pledgedBySponsorGroup!=-1){
				group.setMetaData(YearBMPBean.METADATA_PLEDGED_PER_KILOMETER_BY_SPONSOR_GROUP, sPledgedBySponsorGroup);
			}
			group.store();
		}
		catch (IBOLookupException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (CreateException e) {
			e.printStackTrace();
		}
		String[] grForMarathon = { IWMarathonConstants.FEMALE_14, IWMarathonConstants.FEMALE_15_17, IWMarathonConstants.FEMALE_18_39, IWMarathonConstants.FEMALE_40_49, IWMarathonConstants.FEMALE_50_59, IWMarathonConstants.FEMALE_60, IWMarathonConstants.MALE_14, IWMarathonConstants.MALE_15_17, IWMarathonConstants.MALE_18_39, IWMarathonConstants.MALE_40_49, IWMarathonConstants.MALE_50_59, IWMarathonConstants.MALE_60 };
		String[] grForOsloMarathon = { IWMarathonConstants.FEMALE_18_22, IWMarathonConstants.FEMALE_23_34, IWMarathonConstants.FEMALE_35_39, IWMarathonConstants.FEMALE_40_44, IWMarathonConstants.FEMALE_45_49, IWMarathonConstants.FEMALE_50_54, IWMarathonConstants.FEMALE_55_59, IWMarathonConstants.FEMALE_60_64, IWMarathonConstants.FEMALE_65_69, IWMarathonConstants.FEMALE_70_74, IWMarathonConstants.FEMALE_75_79, IWMarathonConstants.FEMALE_80_84, IWMarathonConstants.FEMALE_85_99, IWMarathonConstants.MALE_18_22, IWMarathonConstants.MALE_23_34, IWMarathonConstants.MALE_35_39, IWMarathonConstants.MALE_40_44, IWMarathonConstants.MALE_45_49, IWMarathonConstants.MALE_50_54, IWMarathonConstants.MALE_55_59, IWMarathonConstants.MALE_60_64, IWMarathonConstants.MALE_65_69, IWMarathonConstants.MALE_70_74, IWMarathonConstants.MALE_75_79, IWMarathonConstants.MALE_80_84, IWMarathonConstants.MALE_85_99 };
		String[] grForLazyTown = { IWMarathonConstants.FEMALE_9, IWMarathonConstants.MALE_9 };
		String[] grForLazyTownOslo = { IWMarathonConstants.FEMALE_9, IWMarathonConstants.MALE_9 };
		String[] grForLaugavegur = { IWMarathonConstants.FEMALE_18_29, IWMarathonConstants.FEMALE_30_39, IWMarathonConstants.FEMALE_40_49, IWMarathonConstants.FEMALE_50, IWMarathonConstants.MALE_18_29, IWMarathonConstants.MALE_30_39, IWMarathonConstants.MALE_40_49, IWMarathonConstants.MALE_50_59, IWMarathonConstants.MALE_60 };
		String[] grForMidnight = { IWMarathonConstants.FEMALE_18, IWMarathonConstants.FEMALE_19_39, IWMarathonConstants.FEMALE_40_49, IWMarathonConstants.FEMALE_50, IWMarathonConstants.MALE_18, IWMarathonConstants.MALE_19_39, IWMarathonConstants.MALE_40_49, IWMarathonConstants.MALE_50 };
		//TODO: remove this hack - set metadata on the groups containing the
		// specific run...
		if (runName.equals(RUN_RVK_MARATHON)) {
			generateSubGroups(iwc, group, getDistancesForRun(run), grForMarathon, priceISK, priceEUR, useChips, childrenPriceISK, childrenPriceEUR, familyDiscount, allowsGroups, numberOfSplits, offersTransport);
		}
		else if (runName.equals(RUN_OSLO_MARATHON)) {
			generateSubGroups(iwc, group, getDistancesForRun(run), grForOsloMarathon, priceISK, priceEUR, useChips, childrenPriceISK, childrenPriceEUR, familyDiscount, allowsGroups, numberOfSplits, offersTransport);
		}
		else if (runName.equals(RUN_LAZY_TOWN_RUN)) {
			generateSubGroups(iwc, group, getDistancesForRun(run), grForLazyTown, priceISK, priceEUR, useChips, childrenPriceISK, childrenPriceEUR, familyDiscount, allowsGroups, numberOfSplits, offersTransport);
		}
		else if (runName.equals(RUN_LAZY_TOWN_MINIMARATON_OSLO)) {
			generateSubGroups(iwc, group, getDistancesForRun(run), grForLazyTownOslo, priceISK, priceEUR, useChips, childrenPriceISK, childrenPriceEUR, familyDiscount, allowsGroups, numberOfSplits, offersTransport);
		}
		else if (runName.equals(RUN_MIDNIGHT_RUN)) {
			generateSubGroups(iwc, group, getDistancesForRun(run), grForMidnight, priceISK, priceEUR, useChips, childrenPriceISK, childrenPriceEUR, familyDiscount, allowsGroups, numberOfSplits, offersTransport);
		}
		else if (runName.equals(RUN_LAUGAVEGUR)) {
			generateSubGroups(iwc, group, getDistancesForRun(run), grForLaugavegur, priceISK, priceEUR, useChips, childrenPriceISK, childrenPriceEUR, familyDiscount, allowsGroups, numberOfSplits, offersTransport);
		}
		else if (runName.equals(RUN_ROLLER_SKATE)) {
			generateSubGroups(iwc, group, getDistancesForRun(run), grForMarathon, priceISK, priceEUR, useChips, childrenPriceISK, childrenPriceEUR, familyDiscount, allowsGroups, numberOfSplits, offersTransport);
		}
	}

	/**
	 * @param iwc
	 * @param group
	 * @param disForMarathon
	 * @param grForMarathon
	 */
	private void generateSubGroups(IWContext iwc, Group group, String[] dis, String[] gr, String[] priceISK, String[] priceEUR, String[] useChips, String[] childrenPriceISK, String[] childrenPriceEUR, String[] familyDiscount, String[] allowsGroups, String[] numberOfSplits, String[] offersTransport) {
		for (int i = 0; i < dis.length; i++) {
			Group distance = null;
			try {
				distance = getGroupBiz().createGroupUnder(dis[i], null, group);
				distance.setGroupType(IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE);
				distance.store();
				try {
					Distance distanceGroup = ConverterUtility.getInstance().convertGroupToDistance(distance);
					distanceGroup.setUseChip("Y".equals(useChips[i]));
					if (priceISK[i] != null  && priceISK[i].length() > 0) {
						distanceGroup.setPriceInISK(Float.parseFloat(priceISK[i]));
					} else {
						distanceGroup.setPriceInISK(0);
					}
					if (priceEUR[i] != null  && priceEUR[i].length() > 0) {
						distanceGroup.setPriceInEUR(Float.parseFloat(priceEUR[i]));
					} else {
						distanceGroup.setPriceInEUR(0);
					}
					if (childrenPriceISK[i] != null && childrenPriceISK[i].length() > 0) {
						distanceGroup.setChildrenPriceInISK(Float.parseFloat(childrenPriceISK[i]));
					} else {
						distanceGroup.setChildrenPriceInISK(0);
					}
					if (childrenPriceEUR[i] != null && childrenPriceEUR[i].length() > 0) {
						distanceGroup.setChildrenPriceInEUR(Float.parseFloat(childrenPriceEUR[i]));
					} else {
						distanceGroup.setChildrenPriceInEUR(0);
					}
						
					distanceGroup.setFamilyDiscount("Y".equals(familyDiscount[i]));
					distanceGroup.setAllowsGroups("Y".equals(allowsGroups[i]));
					distanceGroup.setNumberOfSplits(new Integer(numberOfSplits[i]).intValue());
					distanceGroup.setTransportOffered("Y".equals(offersTransport[i]));
					distanceGroup.store();
				}
				catch (FinderException fe) {
					fe.printStackTrace();
				}
				for (int j = 0; j < gr.length; j++) {
					Group g = null;
					try {
						g = getGroupBiz().createGroupUnder(gr[j], null, distance);
						g.setGroupType(IWMarathonConstants.GROUP_TYPE_RUN_GROUP);
						g.store();
					}
					catch (Exception e) {
					}
				}
			}
			catch (IBOLookupException e1) {
				e1.printStackTrace();
			}
			catch (RemoteException e1) {
				e1.printStackTrace();
			}
			catch (CreateException e1) {
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
		}
		catch (Exception e) {
		}
		return runs;
	}
	public Collection getRunsForUser(User user) {
		Collection groups = null;
		Collection runs = new ArrayList();
		String[] typeRun = { IWMarathonConstants.GROUP_TYPE_RUN };
		String[] typeGroup = { IWMarathonConstants.GROUP_TYPE_RUN_GROUP };
		try {
			groups = getUserBiz().getUserGroups(user,typeGroup,true);
		}
		catch (IBOLookupException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		if(groups != null) {
			Iterator groupsIter = groups.iterator();
			while(groupsIter.hasNext()) {
				Group group = (Group) groupsIter.next();
				Collection r = null;
				try {
					r = getGroupBiz().getParentGroupsRecursive(group,typeRun,true);
				}
				catch (IBOLookupException e1) {
					e1.printStackTrace();
				}
				catch (EJBException e1) {
					e1.printStackTrace();
				}
				catch (RemoteException e1) {
					e1.printStackTrace();
				}
				if(r != null) {
					Iterator rIter = r.iterator();
					while(rIter.hasNext()) {
						Group run = (Group) rIter.next();
						if(run != null) {
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
			typeGroups = getUserBiz().getUserGroups(user,typeGroup,true);
		}
		catch (IBOLookupException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		if(typeGroups != null) {
			Iterator groupsIter = typeGroups.iterator();
			while(groupsIter.hasNext()) {
				Group group = (Group) groupsIter.next();
				Collection r = null;
				try {
					r = getGroupBiz().getParentGroupsRecursive(group,typeRun,true);
				}
				catch (IBOLookupException e1) {
					e1.printStackTrace();
				}
				catch (EJBException e1) {
					e1.printStackTrace();
				}
				catch (RemoteException e1) {
					e1.printStackTrace();
				}
				if(r != null) {
					Iterator rIter = r.iterator();
					while(rIter.hasNext()) {
						Group run = (Group) rIter.next();
						if(run != null) {
							runs.add(run);
						}
					}
				}
			}
		}
		return runs;
		
	}

	public Group getRunGroupOfTypeForGroup(Group group, String type) {
		
		String[] types = {type};
		Collection r = null;
		Group run = null;

		try {
			r = getGroupBiz().getParentGroupsRecursive(group,types,true);
		}
		catch (RemoteException e1) {
			e1.printStackTrace();
		}
		if(r != null) {
			Iterator rIter = r.iterator();
			if(rIter.hasNext()) {
				 run = (Group) rIter.next();
			}
		}
		return run;
	}

	/**
	 * gets all the "gender/age" groups for the user
	 * e.g. "female_14", "male_14", "female_14_17", "male_14_17", "female_18_39", "male_18_39" ...
	 * 
	 * @return a Collection of the "iwma_run_group" types
	 */
	public Collection getRunGroupsForUser(User user) {
		Collection groups = null;
		String[] typeGroup = { IWMarathonConstants.GROUP_TYPE_RUN_GROUP };
		try {
			groups = getUserBiz().getUserGroups(user, typeGroup, true);
		}
		catch (IBOLookupException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		return groups;
	}

	/**
	 * Gets all the years that exist for a specific run. The years are groups
	 * with the group type "iwma_year"
	 * 
	 * @param run -
	 *            the supergroup of the years
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
			years = getGroupBiz().getChildGroupsResultFiltered(run, yearFilter, type, true);
		}
		catch (Exception e) {
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
				yearsMap.put(year.getName(), ConverterUtility.getInstance().convertGroupToYear(year));
			}
			catch (FinderException fe) {
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
	 * @param year -
	 *            the year of the run
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
							distances = new ArrayList(getGroupBiz().getChildGroupsRecursiveResultFiltered(y, type, true));
						}
						catch (Exception e) {
							distances = null;
						}
					}
				}
				if(distances != null) {
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
			groups = getUserBiz().getUserGroups(getUserBiz().getUser(userID),type,true);
		}
		catch (IBOLookupException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		if(groups != null) {
			Iterator i = groups.iterator();
			while(i.hasNext()) {
				Group g = (Group) i.next();
				dis = g;/*
				Group g = (Group) i.next();
				Collection parentGr = null;
				try {
					parentGr = getGroupBiz().getParentGroups(g);
				}
				catch (IBOLookupException e1) {
					e1.printStackTrace();
				}
				catch (RemoteException e1) {
					e1.printStackTrace();
				}
				if(parentGr != null) {
					Iterator j = parentGr.iterator();
					while(j.hasNext()) {
						Group pg = (Group) j.next();
						if(pg.getName().equals(RUN_RVK_MARATHON)) {
							dis = g;
						}
					}
				}
			*/}
		}
		return dis;
		
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
			
			if(presetCountries!=null){
				//iterate reverse through the array to get the correct order:
				for (int i = presetCountries.length-1; i > -1; i--) {
					String presetCountry = presetCountries[i];
					List tempList = new ArrayList(countries);
					for (Iterator iter = tempList.iterator(); iter.hasNext();) {
						Country country = (Country) iter.next();
						String countryIsoAbbr = country.getIsoAbbreviation();
						if(countryIsoAbbr!=null && countryIsoAbbr.equalsIgnoreCase(presetCountry)){
							countries.remove(country);
							countries.add(0, country);	
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return countries;
	}

	private GroupBusiness getGroupBiz() throws IBOLookupException {
		GroupBusiness business = (GroupBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), GroupBusiness.class);
		return business;
	}

	private CreditCardBusiness getCreditCardBusiness() {
		try {
			return (CreditCardBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), CreditCardBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	public UserBusiness getUserBiz() throws IBOLookupException {
		UserBusiness business = (UserBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), UserBusiness.class);
		return business;
	}

	private int getMaxParticipantNumber(String distanceType, String run) {
		if (distanceType.equals(IWMarathonConstants.DISTANCE_55) || distanceType.equals(IWMarathonConstants.DISTANCE_55_WITH_BUS)) {
			return IWMarathonConstants.MAX_NUMBER_DISTANCE_55;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_42)) {
			return IWMarathonConstants.MAX_NUMBER_DISTANCE_42;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_21)) {
			return IWMarathonConstants.MAX_NUMBER_DISTANCE_21;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_CHARITY_42)) {
			return IWMarathonConstants.MAX_NUMBER_DISTANCE_CHARITY_42;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_10)) {
			if (run.equals(RUN_MIDNIGHT_RUN)) {
				return IWMarathonConstants.MAX_NUMBER_DISTANCE_MIDNIGHT_10;
			}
			else {
				return IWMarathonConstants.MAX_NUMBER_DISTANCE_10;
			}
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_5)) {
			return IWMarathonConstants.MAX_NUMBER_DISTANCE_MIDNIGHT_5;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_3)) {
			if (run.equals(RUN_MIDNIGHT_RUN)) {
				return IWMarathonConstants.MAX_NUMBER_DISTANCE_MIDNIGHT_3;
			}
			else {
				return IWMarathonConstants.MAX_NUMBER_DISTANCE_3;
			}
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_1_5)) {
			return IWMarathonConstants.MAX_NUMBER_DISTANCE_1_5;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_1)) {
			return IWMarathonConstants.MAX_NUMBER_DISTANCE_1;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_0_5)) {
			return IWMarathonConstants.MAX_NUMBER_DISTANCE_0_5;
		}
		return 0;
	}

	private int getMinParticipantNumber(String distanceType, String run) {
		if (distanceType.equals(IWMarathonConstants.DISTANCE_55) || distanceType.equals(IWMarathonConstants.DISTANCE_55_WITH_BUS)) {
			return IWMarathonConstants.MIN_NUMBER_DISTANCE_55;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_42)) {
			return IWMarathonConstants.MIN_NUMBER_DISTANCE_42;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_21)) {
			return IWMarathonConstants.MIN_NUMBER_DISTANCE_21;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_CHARITY_42)) {
			return IWMarathonConstants.MIN_NUMBER_DISTANCE_CHARITY_42;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_10)) {
			if (run.equals(RUN_MIDNIGHT_RUN)) {
				return IWMarathonConstants.MIN_NUMBER_DISTANCE_MIDNIGHT_10;
			}
			else {
				return IWMarathonConstants.MIN_NUMBER_DISTANCE_10;
			}
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_5)) {
			return IWMarathonConstants.MIN_NUMBER_DISTANCE_MIDNIGHT_5;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_3)) {
			if (run.equals(RUN_MIDNIGHT_RUN)) {
				return IWMarathonConstants.MIN_NUMBER_DISTANCE_MIDNIGHT_3;
			}
			else {
				return IWMarathonConstants.MIN_NUMBER_DISTANCE_3;
			}
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_1_5)) {
			return IWMarathonConstants.MIN_NUMBER_DISTANCE_1_5;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_1)) {
			return IWMarathonConstants.MIN_NUMBER_DISTANCE_1;
		}
		else if (distanceType.equals(IWMarathonConstants.DISTANCE_0_5)) {
			return IWMarathonConstants.MIN_NUMBER_DISTANCE_0_5;
		}
		return 0;
	}
	
	public Country getCountryByNationality(Object nationality) {
		Country country = null;
		try {
			CountryHome home = (CountryHome) getIDOHome(Country.class);
			try {
				int countryPK = Integer.parseInt(nationality.toString());
				country = home.findByPrimaryKey(new Integer(countryPK));
			}
			catch (NumberFormatException nfe) {
				country = home.findByIsoAbbreviation(nationality.toString());
			}
		}
		catch (FinderException fe) {
			//log(fe);
		}
		catch (RemoteException re) {
			//log(re);
		}
		return country;
	}

	public AddressHome getAddressHome() {
		if (this.addressHome == null) {
			try {
				this.addressHome = (AddressHome) IDOLookup.getHome(Address.class);
			}
			catch (RemoteException rme) {
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
		}
		catch (RemoteException e1) {
			e1.printStackTrace();
		}
		catch (FinderException e1) {
			e1.printStackTrace();
		}
		return participant;
	}

	private String localizeForRun(String key, String value, Runner runner, IWResourceBundle iwrb) {
		if (runner.getRun() != null) {
			String runKey = key+"_runid_"+runner.getRun().getId();
			String localizedString = iwrb.getLocalizedString(runKey);
			if(localizedString == null){
				localizedString = iwrb.getLocalizedString(key, value);
			}
			return localizedString;
		} 
		else {
			return iwrb.getLocalizedString(key, value);
		}
	}

	public ParticipantHome getParticipantHome() {
		if (this.participantHome == null) {
			try {
				this.participantHome = (ParticipantHome) IDOLookup.getHome(Participant.class);
			}
			catch (RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return this.participantHome;
	}
}
/*
 * $Id: RunBusiness.java,v 1.46 2008/01/11 19:29:40 civilis Exp $
 * Created on Aug 16, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.business;

import is.idega.idegaweb.marathon.data.Distance;
import is.idega.idegaweb.marathon.data.Participant;
import is.idega.idegaweb.marathon.data.ParticipantHome;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.block.creditcard.business.CreditCardAuthorizationException;
import com.idega.business.IBOLookupException;
import com.idega.business.IBOService;
import com.idega.core.location.data.Country;
import com.idega.data.IDOCreateException;
import com.idega.presentation.IWContext;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Gender;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;


/**
 * Last modified: $Date: 2008/01/11 19:29:40 $ by $Author: civilis $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.46 $
 */
public interface RunBusiness extends IBOService {

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#isRegisteredInRun
	 */
	public boolean isRegisteredInRun(int runID, int userID) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#isRegisteredInRun
	 */
	public boolean isRegisteredInRun(String year, Group run, User user) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#doesGroupExist
	 */
	public boolean doesGroupExist(Object distancePK, String groupName) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#isRegisteredInRun
	 */
	public boolean isRegisteredInRun(int runID, String personalID) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getAgeFromPersonalID
	 */
	public int getAgeFromPersonalID(String personalID) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#saveRun
	 */
	public void saveRun(int userID, String run, String distance, String year, String nationality, String tshirt,
			String chipOwnershipStatus, String chipNumber, String groupName, String bestTime, String goalTime, Locale locale)
			throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#importParticipant
	 */
	public Participant importParticipant(User user, Group run, Group year, Group distance)
			throws CreateException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#saveUser
	 */
	public User saveUser(String name, String ssn, IWTimestamp dateOfBirth, Gender gender, String address, String postal, String city, Country country) throws java.rmi.RemoteException;
	
	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#saveParticipants
	 */
	public Collection saveParticipants(Collection runners, String email, String hiddenCardNumber, double amount,
			IWTimestamp date, Locale locale, boolean disableSendPaymentConfirmation) throws IDOCreateException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#addParticipantsToGroup
	 */
	public void addParticipantsToGroup(String[] participants, String groupName) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#finishPayment
	 */
	public void finishPayment(String properties) throws CreditCardAuthorizationException, java.rmi.RemoteException;

	/**
	 * 
	 * @deprecated use authorizePayment with expiresDate parameter
	 *
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#authorizePayment
	 */
	public String authorizePayment(String nameOnCard, String cardNumber, String monthExpires, String yearExpires,
			String ccVerifyNumber, double amount, String currency, String referenceNumber)
			throws CreditCardAuthorizationException, java.rmi.RemoteException;
	
	/**
	 * 
	 * @param nameOnCard
	 * @param cardNumber
	 * @param expiresDate - only year and month are relevant
	 * @param ccVerifyNumber
	 * @param amount
	 * @param currency
	 * @param referenceNumber
	 * @return
	 * @throws CreditCardAuthorizationException
	 */
	public String authorizePayment(String nameOnCard, String cardNumber, java.util.Date expiresDate, String ccVerifyNumber, double amount, String currency, String referenceNumber) throws CreditCardAuthorizationException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getPriceForRunner
	 */
	public float getPriceForRunner(Runner runner, Locale locale, float chipDiscount, float chipPrice)
			throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getNumberOfChildren
	 */
	public int getNumberOfChildren(Collection runners) throws java.rmi.RemoteException;
	public int getNumberOfSiblings(Collection children) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getCreditCardImages
	 */
	public Collection getCreditCardImages() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#savePayment
	 */
	public void savePayment(int userID, int distanceID, String payMethod, String amount) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#updateRunForParticipant
	 */
	public void updateRunForParticipant(Participant participant, int bibNumber, String runTime, String chipTime,
			String splitTime1, String splitTime2) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getRunObjByUserIDandYearID
	 */
	public Participant getRunObjByUserIDandYearID(int userID, int yearID) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getRunObjByUserIDandDistanceID
	 */
	public Participant getRunObjByUserIDandDistanceID(int userID, int distanceID) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getParticipantByDistanceAndParticipantNumber
	 */
	public Participant getParticipantByDistanceAndParticipantNumber(Object distancePK, int participantNumber)
			throws FinderException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getParticipantsByUser
	 */
	public Collection getParticipantsByUser(User user) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getParticipantsByYearAndTeamName
	 */
	public Collection getParticipantsByYearAndTeamName(Object yearPrimaryKey, String teamName) throws FinderException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getParticipantByRunAndYear
	 */
	public Participant getParticipantByRunAndYear(User user, Group run, Group year) throws FinderException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getRunGroupByGroupId
	 */
	public Group getRunGroupByGroupId(Integer groupId) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getRunObjByUserAndGroup
	 */
	public Participant getRunObjByUserAndGroup(int userID, int groupID) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getRunnersByDistance
	 */
	public Collection getRunnersByDistance(Group distance, Group runGroup) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#setParticipantNumber
	 */
	public void setParticipantNumber(Participant participant, String run) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#sendMessage
	 */
	public void sendMessage(String email, String subject, String body) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getDistancesForRun
	 */
	public String[] getDistancesForRun(Group run) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#createNewGroupYear
	 */
	public void createNewGroupYear(IWContext iwc, String runID) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getRuns
	 */
	public Collection getRuns() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getRunsForUser
	 */
	public Collection getRunsForUser(User user) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getRunGroupsForUser
	 */
	public Collection getRunGroupsForUser(User user) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getRunGroupsForUser
	 */
	public Collection getRunGroupOfTypeForUser(User user, String type) throws java.rmi.RemoteException;
	
	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getRunGroupOfTypeForGroup
	 */
	public Group getRunGroupOfTypeForGroup(Group group, String type) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getYears
	 */
	public Collection getYears(Group run) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getYears
	 */
	public Collection getYears(Group run, String yearFilter) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getYearsMap
	 */
	public Map getYearsMap(Group run) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getYearsMap
	 */
	public Map getYearsMap(Group run, String groupNameFilter) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getDistancesMap
	 */
	public List getDistancesMap(Group run, String year) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getDistanceByUserID
	 */
	public Group getDistanceByUserID(int userID) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getUserAge
	 */
	public int getUserAge(User user) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getCountries
	 */
	public Collection getCountries() throws java.rmi.RemoteException;
	
	public Collection getCountries(String[] presetCountries) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getUserBiz
	 */
	public UserBusiness getUserBiz() throws IBOLookupException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getCountryByNationality
	 */
	public Country getCountryByNationality(Object nationality) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getParticipantByPrimaryKey
	 */
	public Participant getParticipantByPrimaryKey(int participantID) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getNextAvailableParticipantNumber
	 */
	public int getNextAvailableParticipantNumber(Group run, Distance distance) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.user.business.UserBusinessBean#getParticipantHome
	 */
	public ParticipantHome getParticipantHome() throws java.rmi.RemoteException;
	
	/**
	 * @see com.idega.user.business.UserBusinessBean#getAgeGroup
	 */
	public Group getAgeGroup(User user, Group run, Group distance) throws java.rmi.RemoteException;
	
	public List getDisallowedDistancesPKs(User user, List distances);
	
	public boolean isCrewLabelAlreadyExistsForRun(int runId, int yearId, String crewLabel);
	
	/**
	 * 
	 * @param yearPK
	 * @param searchQuery
	 * @return - participants, where search query matches either participant user full name, or personal id, or participant number
	 * @throws FinderException
	 */
	public Collection getParticipantsBySearchQuery(Object yearPK, String searchQuery) throws FinderException;
	
	public Collection getParticipantsByYearAndCrewNameOrInvitationParticipantId(Object yearPK, String crewName, Integer participantId) throws FinderException;
}
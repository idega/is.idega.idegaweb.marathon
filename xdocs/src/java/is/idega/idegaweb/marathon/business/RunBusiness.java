package is.idega.idegaweb.marathon.business;


import is.idega.idegaweb.marathon.data.ParticipantHome;
import com.idega.core.location.data.AddressHome;
import java.util.Map;
import is.idega.idegaweb.marathon.data.Participant;
import java.rmi.RemoteException;
import java.util.List;
import com.idega.data.IDOCreateException;
import is.idega.idegaweb.marathon.data.Distance;
import com.idega.block.creditcard.business.CreditCardAuthorizationException;
import com.idega.business.IBOService;
import com.idega.business.IBOLookupException;
import com.idega.presentation.IWContext;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.Gender;
import javax.ejb.CreateException;
import com.idega.util.IWTimestamp;
import com.idega.user.data.User;
import java.util.Locale;
import javax.ejb.FinderException;
import com.idega.core.location.data.Country;
import java.util.Collection;

public interface RunBusiness extends IBOService {
	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#saveUser
	 */
	public User saveUser(String name, String ssn, IWTimestamp dateOfBirth,
			Gender gender, String address, String postal, String city,
			Country country) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#isRegisteredInRun
	 */
	public boolean isRegisteredInRun(int runID, int userID)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#isRegisteredInRun
	 */
	public boolean isRegisteredInRun(String year, Group run, User user)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#doesGroupExist
	 */
	public boolean doesGroupExist(Object distancePK, String groupName)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#isRegisteredInRun
	 */
	public boolean isRegisteredInRun(int runID, String personalID)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getAgeFromPersonalID
	 */
	public int getAgeFromPersonalID(String personalID) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#saveRun
	 */
/*	public void saveRun(int userID, String run, String distance, String year,
			String nationality, String tshirt, String chipOwnershipStatus,
			String chipNumber, String groupName, String bestTime,
			String goalTime, Locale locale) throws RemoteException;*/

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#importParticipant
	 */
	public Participant importParticipant(User user, Group run, Group year,
			Group distance) throws CreateException, RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#saveParticipants
	 */
	public Collection saveParticipants(Collection runners, String email,
			String hiddenCardNumber, double amount, IWTimestamp date,
			Locale locale, boolean disableSendPaymentConfirmation)
			throws IDOCreateException, RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getNextAvailableParticipantNumber
	 */
	public int getNextAvailableParticipantNumber(Distance distance)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#addParticipantsToGroup
	 */
	public void addParticipantsToGroup(String[] participants, String groupName)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#isCrewLabelAlreadyExistsForRun
	 */
	public boolean isCrewLabelAlreadyExistsForRun(int runId, int yearId,
			String crewLabel) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getAgeGroup
	 */
	public Group getAgeGroup(User user, Group run, Group distance)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#finishPayment
	 */
	public void finishPayment(String properties)
			throws CreditCardAuthorizationException, RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#authorizePayment
	 */
	public String authorizePayment(String nameOnCard, String cardNumber,
			String monthExpires, String yearExpires, String ccVerifyNumber,
			double amount, String currency, String referenceNumber)
			throws CreditCardAuthorizationException, RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#authorizePayment
	 */
	public String authorizePayment(String nameOnCard, String cardNumber,
			java.util.Date expiresDate, String ccVerifyNumber, double amount,
			String currency, String referenceNumber)
			throws CreditCardAuthorizationException, RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getPriceForRunner
	 */
	public float getPriceForRunner(Runner runner, Locale locale,
			float chipDiscount, float chipPrice) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getNumberOfSiblings
	 */
	public int getNumberOfSiblings(Collection children) throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getNumberOfChildren
	 */
	public int getNumberOfChildren(Collection runners) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getCharityBusiness
	 */
	public CharityBusiness getCharityBusiness() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getCreditCardImages
	 */
	public Collection getCreditCardImages() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#savePayment
	 */
	public void savePayment(int userID, int distanceID, String payMethod,
			String amount) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#updateRunForParticipant
	 */
	public void updateRunForParticipant(Participant participant, int bibNumber,
			String runTime, String chipTime, String splitTime1,
			String splitTime2) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getRunObjByUserIDandYearID
	 */
	public Participant getRunObjByUserIDandYearID(int userID, int yearID)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getRunObjByUserIDandDistanceID
	 */
	public Participant getRunObjByUserIDandDistanceID(int userID, int distanceID)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getParticipantByDistanceAndParticipantNumber
	 */
	public Participant getParticipantByDistanceAndParticipantNumber(
			Object distancePK, int participantNumber) throws FinderException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getParticipantsByYearAndTeamName
	 */
	public Collection getParticipantsByYearAndTeamName(Object yearPK,
			String teamName) throws FinderException, RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getParticipantsByYearAndCrewInOrCrewInvitationParticipantId
	 */
	public Collection getParticipantsByYearAndCrewInOrCrewInvitationParticipantId(
			Object yearPK, Integer crewParticipantId) throws FinderException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getParticipantsByUser
	 */
	public Collection getParticipantsByUser(User user) throws FinderException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getParticipantsBySearchQuery
	 */
	public Collection getParticipantsBySearchQuery(Object yearPK,
			String searchQuery) throws FinderException, RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getParticipantByRunAndYear
	 */
	public Participant getParticipantByRunAndYear(User user, Group run,
			Group year) throws FinderException, RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getRunGroupByGroupId
	 */
	public Group getRunGroupByGroupId(Integer groupId) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getRunObjByUserAndGroup
	 */
	public Participant getRunObjByUserAndGroup(int userID, int groupID)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getRunnersByDistance
	 */
	public Collection getRunnersByDistance(Group distance, Group runGroup)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#setParticipantNumber
	 */
	public void setParticipantNumber(Participant participant, String run)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#sendMessage
	 */
	public void sendMessage(String email, String subject, String body)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getDistancesForRun
	 */
	public String[] getDistancesForRun(Group run) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#createNewGroupYear
	 */
	public void createNewGroupYear(IWContext iwc, String runID)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getRuns
	 */
	public Collection getRuns() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getRunsForUser
	 */
	public Collection getRunsForUser(User user) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getRunGroupOfTypeForUser
	 */
	public Collection getRunGroupOfTypeForUser(User user, String type)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getRunGroupOfTypeForGroup
	 */
	public Group getRunGroupOfTypeForGroup(Group group, String type)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getRunGroupsForUser
	 */
	public Collection getRunGroupsForUser(User user) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getYears
	 */
	public Collection getYears(Group run) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getYears
	 */
	public Collection getYears(Group run, String yearFilter)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getYearsMap
	 */
	public Map getYearsMap(Group run) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getYearsMap
	 */
	public Map getYearsMap(Group run, String groupNameFilter)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getDistancesMap
	 */
	public List getDistancesMap(Group run, String year) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getDistanceByUserID
	 */
	public Group getDistanceByUserID(int userID) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getDistanceByID
	 */
	public Distance getDistanceByID(int ID) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getUserAge
	 */
	public int getUserAge(User user) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getCountries
	 */
	public Collection getCountries() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getCountries
	 */
	public Collection getCountries(String[] presetCountries)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getUserBiz
	 */
	public UserBusiness getUserBiz() throws IBOLookupException, RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getCountryByNationality
	 */
	public Country getCountryByNationality(Object nationality)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getAddressHome
	 */
	public AddressHome getAddressHome() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getParticipantByPrimaryKey
	 */
	public Participant getParticipantByPrimaryKey(int participantID)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getParticipantHome
	 */
	public ParticipantHome getParticipantHome() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunBusinessBean#getDisallowedDistancesPKs
	 */
	public List getDisallowedDistancesPKs(User user, List distances)
			throws RemoteException;
}
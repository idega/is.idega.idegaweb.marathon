package is.idega.idegaweb.marathon.data;


import com.idega.data.IDOEntity;
import com.idega.user.data.User;
import java.util.Collection;
import com.idega.user.data.Group;

public interface Participant extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRunID
	 */
	public int getRunID();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRunTypeGroupID
	 */
	public int getRunTypeGroupID();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRunTypeGroup
	 */
	public Group getRunTypeGroup();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRunYearGroupID
	 */
	public int getRunYearGroupID();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRunYearGroup
	 */
	public Group getRunYearGroup();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRunDistanceGroupID
	 */
	public int getRunDistanceGroupID();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRunDistanceGroup
	 */
	public Distance getRunDistanceGroup();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getPaymentGroupsForYear
	 */
	public Collection getPaymentGroupsForYear();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRunGroupGroupID
	 */
	public int getRunGroupGroupID();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRunGroupGroup
	 */
	public Group getRunGroupGroup();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRunTime
	 */
	public int getRunTime();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getChipTime
	 */
	public int getChipTime();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getSplitTime1
	 */
	public int getSplitTime1();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getSplitTime2
	 */
	public int getSplitTime2();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getChipOwnershipStatus
	 */
	public String getChipOwnershipStatus();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getUser
	 */
	public User getUser();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getUserID
	 */
	public int getUserID();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getChipNumber
	 */
	public String getChipNumber();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getChipBunchNumber
	 */
	public String getChipBunchNumber();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getUserNationality
	 */
	public String getUserNationality();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getShirtSize
	 */
	public String getShirtSize();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRelayPartner1SSN
	 */
	public String getRelayPartner1SSN();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRelayPartner1Name
	 */
	public String getRelayPartner1Name();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRelayPartner1Email
	 */
	public String getRelayPartner1Email();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRelayPartner1ShirtSize
	 */
	public String getRelayPartner1ShirtSize();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRelayPartner1Leg
	 */
	public String getRelayPartner1Leg();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRelayPartner2SSN
	 */
	public String getRelayPartner2SSN();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRelayPartner2Name
	 */
	public String getRelayPartner2Name();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRelayPartner2Email
	 */
	public String getRelayPartner2Email();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRelayPartner2ShirtSize
	 */
	public String getRelayPartner2ShirtSize();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRelayPartner2Leg
	 */
	public String getRelayPartner2Leg();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRelayPartner3SSN
	 */
	public String getRelayPartner3SSN();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRelayPartner3Name
	 */
	public String getRelayPartner3Name();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRelayPartner3Email
	 */
	public String getRelayPartner3Email();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRelayPartner3ShirtSize
	 */
	public String getRelayPartner3ShirtSize();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRelayPartner3Leg
	 */
	public String getRelayPartner3Leg();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRelayLeg
	 */
	public String getRelayLeg();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRunGroupName
	 */
	public String getRunGroupName();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getCrewInvitedParticipantId
	 */
	public Integer getCrewInvitedParticipantId();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getCrewInParticipantId
	 */
	public Integer getCrewInParticipantId();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getBestTime
	 */
	public String getBestTime();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getGoalTime
	 */
	public String getGoalTime();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getParticipantNumber
	 */
	public int getParticipantNumber();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getPayMethod
	 */
	public String getPayMethod();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getPayedAmount
	 */
	public String getPayedAmount();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getTransportOrdered
	 */
	public String getTransportOrdered();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getIsDeleted
	 */
	public boolean getIsDeleted();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRunTypeGroupID
	 */
	public void setRunTypeGroupID(int runTypeGroupID);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRunTypeGroup
	 */
	public void setRunTypeGroup(Group runTypeGroup);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRunYearGroupID
	 */
	public void setRunYearGroupID(int runYearGroupID);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRunYearGroup
	 */
	public void setRunYearGroup(Group runYearGroup);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRunDistanceGroupID
	 */
	public void setRunDistanceGroupID(int runDisGroupID);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRunDistanceGroup
	 */
	public void setRunDistanceGroup(Group runDisGroup);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRunGroupGroupID
	 */
	public void setRunGroupGroupID(int runGroupGroupID);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRunGroupGroup
	 */
	public void setRunGroupGroup(Group runGroupGroup);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRunTime
	 */
	public void setRunTime(int runTime);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setChipTime
	 */
	public void setChipTime(int chipTime);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setSplitTime1
	 */
	public void setSplitTime1(int splitTime);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setSplitTime2
	 */
	public void setSplitTime2(int splitTime);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setChipOwnershipStatus
	 */
	public void setChipOwnershipStatus(String ownershipStatus);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setUserID
	 */
	public void setUserID(int userID);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setUser
	 */
	public void setUser(User user);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setChipNumber
	 */
	public void setChipNumber(String chipNumber);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setChipBunchNumber
	 */
	public void setChipBunchNumber(String chipBunchNumber);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setUserNationality
	 */
	public void setUserNationality(String nationality);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setShirtSize
	 */
	public void setShirtSize(String tShirtSize);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRelayPartner1SSN
	 */
	public void setRelayPartner1SSN(String ssn);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRelayPartner1Name
	 */
	public void setRelayPartner1Name(String name);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRelayPartner1Email
	 */
	public void setRelayPartner1Email(String email);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRelayPartner1ShirtSize
	 */
	public void setRelayPartner1ShirtSize(String shirtSize);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRelayPartner1Leg
	 */
	public void setRelayPartner1Leg(String leg);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRelayPartner2SSN
	 */
	public void setRelayPartner2SSN(String ssn);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRelayPartner2Name
	 */
	public void setRelayPartner2Name(String name);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRelayPartner2Email
	 */
	public void setRelayPartner2Email(String email);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRelayPartner2ShirtSize
	 */
	public void setRelayPartner2ShirtSize(String shirtSize);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRelayPartner2Leg
	 */
	public void setRelayPartner2Leg(String leg);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRelayPartner3SSN
	 */
	public void setRelayPartner3SSN(String ssn);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRelayPartner3Name
	 */
	public void setRelayPartner3Name(String name);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRelayPartner3Email
	 */
	public void setRelayPartner3Email(String email);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRelayPartner3ShirtSize
	 */
	public void setRelayPartner3ShirtSize(String shirtSize);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRelayPartner3Leg
	 */
	public void setRelayPartner3Leg(String leg);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRelayLeg
	 */
	public void setRelayLeg(String leg);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRunGroupName
	 */
	public void setRunGroupName(String runGrName);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setCrewInvitedParticipantId
	 */
	public void setCrewInvitedParticipantId(Integer crewInvitedParticipantId);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setCrewInParticipantId
	 */
	public void setCrewInParticipantId(Integer crewInParticipantId);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setBestTime
	 */
	public void setBestTime(String bestTime);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setGoalTime
	 */
	public void setGoalTime(String goalTime);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setParticipantNumber
	 */
	public void setParticipantNumber(int participantNumber);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setPayMethod
	 */
	public void setPayMethod(String payMethod);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setPayedAmount
	 */
	public void setPayedAmount(String amount);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setTransportOrdered
	 */
	public void setTransportOrdered(String transportOrdered);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setIsDeleted
	 */
	public void setIsDeleted(boolean deleted);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setCharityId
	 */
	public void setCharityId(String charityId);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getCharityId
	 */
	public String getCharityId();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getParticipatesInCharity
	 */
	public boolean getParticipatesInCharity();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setMaySponsorContact
	 */
	public void setMaySponsorContact(boolean mayContact);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getMaySponsorContact
	 */
	public boolean getMaySponsorContact();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setCategoryId
	 */
	public void setCategoryId(int categoryId);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getCategoryId
	 */
	public int getCategoryId();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setAllowsEmails
	 */
	public void setAllowsEmails(boolean allowsEmails);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getAllowsEmails
	 */
	public boolean getAllowsEmails();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setQuestion1Hour
	 */
	public void setQuestion1Hour(String hour);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getQuestion1Hour
	 */
	public String getQuestion1Hour();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setQuestion1Minute
	 */
	public void setQuestion1Minute(String minute);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getQuestion1Minute
	 */
	public String getQuestion1Minute();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setQuestion1Year
	 */
	public void setQuestion1Year(String year);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getQuestion1NeverRan
	 */
	public boolean getQuestion1NeverRan();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setQuestion1NeverRan
	 */
	public void setQuestion1NeverRan(boolean neverRan);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getQuestion1Year
	 */
	public String getQuestion1Year();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setQuestion2Hour
	 */
	public void setQuestion2Hour(String hour);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getQuestion2Hour
	 */
	public String getQuestion2Hour();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setQuestion2Minute
	 */
	public void setQuestion2Minute(String minute);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getQuestion2Minute
	 */
	public String getQuestion2Minute();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setQuestion3Hour
	 */
	public void setQuestion3Hour(String hour);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getQuestion3Hour
	 */
	public String getQuestion3Hour();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setQuestion3Minute
	 */
	public void setQuestion3Minute(String minute);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getQuestion3Minute
	 */
	public String getQuestion3Minute();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setQuestion3Year
	 */
	public void setQuestion3Year(String year);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getQuestion3Year
	 */
	public String getQuestion3Year();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getQuestion3NeverRan
	 */
	public boolean getQuestion3NeverRan();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setQuestion3NeverRan
	 */
	public void setQuestion3NeverRan(boolean neverRan);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#isApplyForDomesticTravelSupport
	 */
	public boolean isApplyForDomesticTravelSupport();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setApplyForDomesticTravelSupport
	 */
	public void setApplyForDomesticTravelSupport(
			boolean applyForDomesticTravelSupport);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#isApplyForInternationalTravelSupport
	 */
	public boolean isApplyForInternationalTravelSupport();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setApplyForInternationalTravelSupport
	 */
	public void setApplyForInternationalTravelSupport(
			boolean applyForInternationalTravelSupport);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#isSponsoredRunner
	 */
	public boolean isSponsoredRunner();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setSponsoredRunner
	 */
	public void setSponsoredRunner(boolean sponsoredRunner);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#isCustomer
	 */
	public boolean isCustomer();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setCustomer
	 */
	public void setCustomer(boolean isCustomer);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getPaymentGroup
	 */
	public String getPaymentGroup();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setPaymentGroup
	 */
	public void setPaymentGroup(String paymentGroup);
}
package is.idega.idegaweb.marathon.data;


import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.data.IDOEntity;

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
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#getRunGroupName
	 */
	public String getRunGroupName();

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
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setRunGroupName
	 */
	public void setRunGroupName(String runGrName);

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
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#isApplyForDomesticTravelSupport
	 */
	public boolean isApplyForDomesticTravelSupport();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setApplyForDomesticTravelSupport
	 */
	public void setApplyForDomesticTravelSupport(boolean applyForDomesticTravelSupport);

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#isApplyForInternationalTravelSupport
	 */
	public boolean isApplyForInternationalTravelSupport();

	/**
	 * @see is.idega.idegaweb.marathon.data.ParticipantBMPBean#setApplyForInternationalTravelSupport
	 */
	public void setApplyForInternationalTravelSupport(boolean applyForInternationalTravelSupport);
}
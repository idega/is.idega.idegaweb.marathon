/*
 * Created on Jul 6, 2004
 */
package is.idega.idegaweb.marathon.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOException;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOQuery;
import com.idega.data.query.Column;
import com.idega.data.query.CountColumn;
import com.idega.data.query.Criteria;
import com.idega.data.query.JoinCriteria;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.MaxColumn;
import com.idega.data.query.OR;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;

/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * 
 * @author birna
 */
public class ParticipantBMPBean extends GenericEntity implements Participant {

	private static final String COLUMN_ALLOWS_EMAIL_FROM_RM = "allows_emails";
	
	private static final String COLUMN_QUESTION1_HOUR = "q1_hour";
	private static final String COLUMN_QUESTION1_MINUTE = "q1_minute";
	private static final String COLUMN_QUESTION1_YEAR = "q1_year";
	private static final String COLUMN_QUESTION1_NEVER_RAN = "q1_never_ran";
	private static final String COLUMN_QUESTION2_HOUR = "q2_hour";
	private static final String COLUMN_QUESTION2_MINUTE = "q2_minute";
	private static final String COLUMN_QUESTION3_HOUR = "q3_hour";
	private static final String COLUMN_QUESTION3_MINUTE = "q3_minute";
	private static final String COLUMN_QUESTION3_YEAR = "q3_year";
	private static final String COLUMN_QUESTION3_NEVER_RAN = "q3_never_ran";
	
	private static final String COLUMN_RELAY_LEG = "relay_leg";
	
	private static final String COLUMN_RELAY1_PERSONAL_ID = "rel1_ssn";
	private static final String COLUMN_RELAY1_NAME = "rel1_name";
	private static final String COLUMN_RELAY1_EMAIL = "rel1_email";
	private static final String COLUMN_RELAY1_SHIRT_SIZE = "rel1_shirt_size";
	private static final String COLUMN_RELAY1_LEG = "rel1_leg";

	private static final String COLUMN_RELAY2_PERSONAL_ID = "rel2_ssn";
	private static final String COLUMN_RELAY2_NAME = "rel2_name";
	private static final String COLUMN_RELAY2_EMAIL = "rel2_email";
	private static final String COLUMN_RELAY2_SHIRT_SIZE = "rel2_shirt_size";
	private static final String COLUMN_RELAY2_LEG = "rel2_leg";

	private static final String COLUMN_RELAY3_PERSONAL_ID = "rel3_ssn";
	private static final String COLUMN_RELAY3_NAME = "rel3_name";
	private static final String COLUMN_RELAY3_EMAIL = "rel3_email";
	private static final String COLUMN_RELAY3_SHIRT_SIZE = "rel3_shirt_size";
	private static final String COLUMN_RELAY3_LEG = "rel3_leg";
	
	private static final String COLUMN_DELETED = "deleted";

	
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -3580829766912725507L;


	public ParticipantBMPBean() {
		super();
	}

	public ParticipantBMPBean(int id) throws SQLException {
		super(id);
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());

		addManyToOneRelationship(getColumnNameRunTypeGroupID(), "Run Type", Group.class);
		addManyToOneRelationship(getColumnNameRunYearGroupID(), "Run Year", Group.class);
		addManyToOneRelationship(getColumnNameRunDistanceGroupID(), "Run Distance", Group.class);
		addManyToOneRelationship(getColumnNameRunGroupGroupID(), "Run Group", Group.class);
		addManyToOneRelationship(getColumnNameUserID(), "User ID", User.class);
		
		addAttribute(getColumnNameRunTime(), "Run Time", true, true, Integer.class);
		addAttribute(getColumnNameChipTime(), "Chip Time", true, true, Integer.class);
		addAttribute(getColumnNameSplitTime1(), "Split Time 1", true, true, Integer.class);
		addAttribute(getColumnNameSplitTime2(), "Split Time 2", true, true, Integer.class);
		addAttribute(getColumnNameChipNumber(), "Chip Number", true, true, String.class);
		addAttribute(getColumnNameChipBunchNumber(), "Chip bunch Number", true, true, String.class);
		addAttribute(getColumnNameChipOwnershipStatus(), "Chip ownership status", true, true, String.class);
		addAttribute(getColumnNameUserNationality(), "User Nationality", true, true, String.class);
		addAttribute(getColumnNameTShirtSize(), "TShirt Size", true, true, String.class);
		addAttribute(getColumnNameRunGroupName(), "Run Group Name", true, true, String.class);
		addAttribute(getColumnNameCrewInvitedParticipantId(), "Crew invited participant id", true, true, Integer.class);
		addAttribute(getColumnNameCrewInParticipantId(), "Crew this participant is member of participant id", true, true, Integer.class);
		
		addAttribute(getColumnNameBestTime(), "Best Time", true, true, String.class);
		addAttribute(getColumnNameGoalTime(), "Goal Time", true, true, String.class);
		addAttribute(getColumnNameParticipantNumber(), "Participant number", true, true, Integer.class);
		addAttribute(getColumnNamePayMethod(), "Pay method", true, true, String.class);
		addAttribute(getColumnNameAmountPayed(), "Amount payed", true, true, String.class);
		addAttribute(getColumnNameTransportOrdered(), "Transport ordered", true, true, String.class);
		
		addAttribute(getColumnNameCharityId(), "CharityId", true, true, String.class);
		addAttribute(getColumnNameMaySponsorContact(), "May Sponsor Contact", true, true, Boolean.class);
		addAttribute(getColumnNameCategoryId(), "CategoryId", true, true, Integer.class);
		
		addAttribute(getColumnApplyForDomesticTravelSupport(), "Domestic travel support", true, true, Boolean.class);
		addAttribute(getColumnApplyForInternationalTravelSupport(), "International Travel support", true, true, Boolean.class);
		addAttribute(getColumnSponsoredRunner(), "Sponsored runner", true, true, Boolean.class);
		addAttribute(getColumnIsCustomer(), "Is Customer", true, true, Boolean.class);
		addAttribute(getColumnPaymentGroup(), "PaymentGroup", true, true, String.class);	
		
		addAttribute(COLUMN_ALLOWS_EMAIL_FROM_RM, "Allows emails", Boolean.class);
		
		addAttribute(COLUMN_QUESTION1_HOUR, "", String.class);
		addAttribute(COLUMN_QUESTION1_MINUTE, "", String.class);
		addAttribute(COLUMN_QUESTION1_YEAR, "", String.class);
		addAttribute(COLUMN_QUESTION1_NEVER_RAN, "", Boolean.class);
		addAttribute(COLUMN_QUESTION2_HOUR, "", String.class);
		addAttribute(COLUMN_QUESTION2_MINUTE, "", String.class);
		addAttribute(COLUMN_QUESTION3_HOUR, "", String.class);
		addAttribute(COLUMN_QUESTION3_MINUTE, "", String.class);
		addAttribute(COLUMN_QUESTION3_YEAR, "", String.class);
		addAttribute(COLUMN_QUESTION3_NEVER_RAN, "", Boolean.class);
		
		addAttribute(COLUMN_RELAY_LEG, "", String.class);
		
		addAttribute(COLUMN_RELAY1_PERSONAL_ID, "", String.class);
		addAttribute(COLUMN_RELAY1_NAME, "", String.class);
		addAttribute(COLUMN_RELAY1_EMAIL, "", String.class);
		addAttribute(COLUMN_RELAY1_SHIRT_SIZE, "", String.class);
		addAttribute(COLUMN_RELAY1_LEG, "", String.class);

		addAttribute(COLUMN_RELAY2_PERSONAL_ID, "", String.class);
		addAttribute(COLUMN_RELAY2_NAME, "", String.class);
		addAttribute(COLUMN_RELAY2_EMAIL, "", String.class);
		addAttribute(COLUMN_RELAY2_SHIRT_SIZE, "", String.class);
		addAttribute(COLUMN_RELAY2_LEG, "", String.class);

		addAttribute(COLUMN_RELAY3_PERSONAL_ID, "", String.class);
		addAttribute(COLUMN_RELAY3_NAME, "", String.class);
		addAttribute(COLUMN_RELAY3_EMAIL, "", String.class);
		addAttribute(COLUMN_RELAY3_SHIRT_SIZE, "", String.class);
		addAttribute(COLUMN_RELAY3_LEG, "", String.class);

		addAttribute(COLUMN_DELETED, "Deleted", Boolean.class);
	}

	public static String getEntityTableName() {
		return "run";
	}

	public static String getColumnNameRunID() {
		return "run_id";
	}

	public static String getColumnNameRunTypeGroupID() {
		return com.idega.user.data.GroupBMPBean.getColumnNameGroupID() + "_run";
	}

	public static String getColumnNameRunYearGroupID() {
		return com.idega.user.data.GroupBMPBean.getColumnNameGroupID() + "_year";
	}

	public static String getColumnNameRunDistanceGroupID() {
		return com.idega.user.data.GroupBMPBean.getColumnNameGroupID() + "_distance";
	}

	public static String getColumnNameRunGroupGroupID() {
		return com.idega.user.data.GroupBMPBean.getColumnNameGroupID() + "_group";
	}

	public static String getColumnNameRunTime() {
		return "run_time";
	}

	public static String getColumnNameChipTime() {
		return "chip_time";
	}

	public static String getColumnNameSplitTime1() {
		return "split_time_1";
	}

	public static String getColumnNameSplitTime2() {
		return "split_time_2";
	}

	public static String getColumnNameChipNumber() {
		return "run_chip_number";
	}
	
	public static String getColumnNameChipBunchNumber() {
		return "run_chip_bunch_number";
	}
	
	public static String getColumnNameChipOwnershipStatus() {
		return "run_chip_ownership_status";
	}

	public static String getColumnNameUserNationality() {
		return "user_nationality";
	}

	public static String getColumnNameTShirtSize() {
		return "run_tShirt_size";
	}

//	this is actually crew name
	public static String getColumnNameRunGroupName() {
		return "run_group_name";
	}
	
	public static String getColumnNameCrewInvitedParticipantId() {
		return "crew_invited_pid";
	}
	
	public static String getColumnNameCrewInParticipantId() {
		return "crew_in_pid";
	}

	public static String getColumnNameBestTime() {
		return "run_best_time";
	}

	public static String getColumnNameGoalTime() {
		return "run_goal_time";
	}

	public static String getColumnNameUserID() {
		return com.idega.user.data.UserBMPBean.getColumnNameUserID();
	}

	public static String getColumnNameParticipantNumber() {
		return "participant_number";
	}
	
	public static String getColumnNamePayMethod() {
		return "pay_method";
	}
	
	public static String getColumnNameAmountPayed() {
		return "payed_amount";
	}

	public static String getColumnNameTransportOrdered() {
		return "transport_ordered";
	}

	public static String getColumnNameCharityId() {
		return "charity_organizational_id";
	}

	public static String getColumnNameCategoryId() {
		return "run_category_id";
	}

	public static String getColumnNameMaySponsorContact() {
		return "may_sponsor_contact";
	}
	
	public static String getColumnApplyForDomesticTravelSupport() {
		return "domestic_travel_support";
	}
	
	public static String getColumnApplyForInternationalTravelSupport() {
		return "international_travel_support";
	}

	public static String getColumnSponsoredRunner() {
		return "sponsored_runner";
	}

	public static String getColumnIsCustomer(){
		return "is_customer";
	}
	
	public static String getColumnPaymentGroup(){
		return "payment_group";
	}
	
	public String getIDColumnName() {
		return getColumnNameRunID();
	}

	public String getEntityName() {
		return getEntityTableName();
	}

	public int getRunID() {
		return getIntColumnValue(getColumnNameRunID());
	}

	//GET
	public int getRunTypeGroupID() {
		return getIntColumnValue(getColumnNameRunTypeGroupID());
	}
	
	public Group getRunTypeGroup() {
		return (Group) getColumnValue(getColumnNameRunTypeGroupID());
	}

	public int getRunYearGroupID() {
		return getIntColumnValue(getColumnNameRunYearGroupID());
	}

	public Group getRunYearGroup() {
		return (Group) getColumnValue(getColumnNameRunYearGroupID());
	}

	public int getRunDistanceGroupID() {
		return getIntColumnValue(getColumnNameRunDistanceGroupID());
	}

	public Distance getRunDistanceGroup() {
		int distanceGroupId = getIntColumnValue(getColumnNameRunDistanceGroupID());
		DistanceHome dHome;
		try {
			dHome = (DistanceHome) getIDOHome(Distance.class);
			return (Distance) dHome.findByPrimaryKey(new Integer(distanceGroupId));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Collection getPaymentGroupsForYear() {
		Connection conn = null;
		Statement Stmt = null;
		ResultSet RS = null;
		Collection paymentGroups = new ArrayList();
		try {
			conn = getConnection(getDatasource());
			Stmt = conn.createStatement();
			String sql = "select distinct " + getColumnPaymentGroup() + " from " + getEntityName() + " where " + getColumnNameRunYearGroupID() + " = " + getRunYearGroupID() +" and " +getColumnPaymentGroup() + " is not null and " + getColumnPaymentGroup() + " != ''";
			RS = Stmt.executeQuery(sql);
			while (RS.next()) {
				String paymentGroupName = RS.getString(getColumnPaymentGroup());
				paymentGroups.add(paymentGroupName);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		finally {
			try {
				if (RS != null) {
					RS.close();
				}
				if (Stmt != null) {
					Stmt.close();
				}
				if (conn != null) {
					freeConnection(getDatasource(), conn);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return paymentGroups;
	}

	public int getRunGroupGroupID() {
		return getIntColumnValue(getColumnNameRunGroupGroupID());
	}

	public Group getRunGroupGroup() {
		return (Group) getColumnValue(getColumnNameRunGroupGroupID());
	}

	public int getRunTime() {
		return getIntColumnValue(getColumnNameRunTime());
	}

	public int getChipTime() {
		return getIntColumnValue(getColumnNameChipTime());
	}
	
	public int getSplitTime1() {
		return getIntColumnValue(getColumnNameSplitTime1());
	}
	
	public int getSplitTime2() {
		return getIntColumnValue(getColumnNameSplitTime2());
	}
	
	public String getChipOwnershipStatus() {
		return getStringColumnValue(getColumnNameChipOwnershipStatus());
	}

	public User getUser() {
		return (User) getColumnValue(getColumnNameUserID());
	}

	public int getUserID() {
		return getIntColumnValue(getColumnNameUserID());
	}

	public String getChipNumber() {
		return getStringColumnValue(getColumnNameChipNumber());
	}
	
	public String getChipBunchNumber() {
		return getStringColumnValue(getColumnNameChipBunchNumber());
	}

	public String getUserNationality() {
		return getStringColumnValue(getColumnNameUserNationality());
	}

	public String getShirtSize() {
		return getStringColumnValue(getColumnNameTShirtSize());
	}

	public String getRelayPartner1SSN() {
		return getStringColumnValue(COLUMN_RELAY1_PERSONAL_ID);
	}
	
	public String getRelayPartner1Name() {
		return getStringColumnValue(COLUMN_RELAY1_NAME);
	}
	
	public String getRelayPartner1Email() {
		return getStringColumnValue(COLUMN_RELAY1_EMAIL);
	}
	
	public String getRelayPartner1ShirtSize() {
		return getStringColumnValue(COLUMN_RELAY1_SHIRT_SIZE);
	}
	
	public String getRelayPartner1Leg() {
		return getStringColumnValue(COLUMN_RELAY1_LEG);
	}

	public String getRelayPartner2SSN() {
		return getStringColumnValue(COLUMN_RELAY2_PERSONAL_ID);
	}
	
	public String getRelayPartner2Name() {
		return getStringColumnValue(COLUMN_RELAY2_NAME);
	}
	
	public String getRelayPartner2Email() {
		return getStringColumnValue(COLUMN_RELAY2_EMAIL);
	}
	
	public String getRelayPartner2ShirtSize() {
		return getStringColumnValue(COLUMN_RELAY2_SHIRT_SIZE);
	}
	
	public String getRelayPartner2Leg() {
		return getStringColumnValue(COLUMN_RELAY2_LEG);
	}

	public String getRelayPartner3SSN() {
		return getStringColumnValue(COLUMN_RELAY3_PERSONAL_ID);
	}
	
	public String getRelayPartner3Name() {
		return getStringColumnValue(COLUMN_RELAY3_NAME);
	}
	
	public String getRelayPartner3Email() {
		return getStringColumnValue(COLUMN_RELAY3_EMAIL);
	}
	
	public String getRelayPartner3ShirtSize() {
		return getStringColumnValue(COLUMN_RELAY3_SHIRT_SIZE);
	}
	
	public String getRelayPartner3Leg() {
		return getStringColumnValue(COLUMN_RELAY3_LEG);
	}
	
	public String getRelayLeg() {
		return getStringColumnValue(COLUMN_RELAY_LEG);
	}

	/**
	 * this is actually crew name
	 */
	public String getRunGroupName() {
		return getStringColumnValue(getColumnNameRunGroupName());
	}
	
	public Integer getCrewInvitedParticipantId() {
		return getIntegerColumnValue(getColumnNameCrewInvitedParticipantId());
	}
	
	public Integer getCrewInParticipantId() {
		return getIntegerColumnValue(getColumnNameCrewInParticipantId());
	}

	public String getBestTime() {
		return getStringColumnValue(getColumnNameBestTime());
	}

	public String getGoalTime() {
		return getStringColumnValue(getColumnNameGoalTime());
	}

	public int getParticipantNumber() {
		return getIntColumnValue(getColumnNameParticipantNumber());
	}
	
	public String getPayMethod() {
		return getStringColumnValue(getColumnNamePayMethod());
	}
	
	public String getPayedAmount() {
		return getStringColumnValue(getColumnNameAmountPayed());
	}

	public String getTransportOrdered() {
		return getStringColumnValue(getColumnNameTransportOrdered());
	}

	public boolean getIsDeleted() {
		return getBooleanColumnValue(COLUMN_DELETED, false);
	}
	
	//SET
	public void setRunTypeGroupID(int runTypeGroupID) {
		setColumn(getColumnNameRunTypeGroupID(), runTypeGroupID);
	}

	public void setRunTypeGroup(Group runTypeGroup) {
		setColumn(getColumnNameRunTypeGroupID(), runTypeGroup);
	}

	public void setRunYearGroupID(int runYearGroupID) {
		setColumn(getColumnNameRunYearGroupID(), runYearGroupID);
	}

	public void setRunYearGroup(Group runYearGroup) {
		setColumn(getColumnNameRunYearGroupID(), runYearGroup);
	}

	public void setRunDistanceGroupID(int runDisGroupID) {
		setColumn(getColumnNameRunDistanceGroupID(), runDisGroupID);
	}

	public void setRunDistanceGroup(Group runDisGroup) {
		setColumn(getColumnNameRunDistanceGroupID(), runDisGroup);
	}

	public void setRunGroupGroupID(int runGroupGroupID) {
		setColumn(getColumnNameRunGroupGroupID(), runGroupGroupID);
	}

	public void setRunGroupGroup(Group runGroupGroup) {
		setColumn(getColumnNameRunGroupGroupID(), runGroupGroup);
	}

	public void setRunTime(int runTime) {
		setColumn(getColumnNameRunTime(), runTime);
	}

	public void setChipTime(int chipTime) {
		setColumn(getColumnNameChipTime(), chipTime);
	}
	
	public void setSplitTime1(int splitTime) {
		setColumn(getColumnNameSplitTime1(), splitTime);
	}
	
	public void setSplitTime2(int splitTime) {
		setColumn(getColumnNameSplitTime2(), splitTime);
	}
	
	public void setChipOwnershipStatus(String ownershipStatus) {
		setColumn(getColumnNameChipOwnershipStatus(), ownershipStatus);
	}

	public void setUserID(int userID) {
		setColumn(getColumnNameUserID(), userID);
	}

	public void setUser(User user) {
		setColumn(getColumnNameUserID(), user);
	}

	public void setChipNumber(String chipNumber) {
		setColumn(getColumnNameChipNumber(), chipNumber);
	}
	
	public void setChipBunchNumber(String chipBunchNumber) {
		setColumn(getColumnNameChipBunchNumber(), chipBunchNumber);
	}

	public void setUserNationality(String nationality) {
		setColumn(getColumnNameUserNationality(), nationality);
	}

	public void setShirtSize(String tShirtSize) {
		setColumn(getColumnNameTShirtSize(), tShirtSize);
	}

	public void setRelayPartner1SSN(String ssn) {
		setColumn(COLUMN_RELAY1_PERSONAL_ID, ssn);
	}
	
	public void setRelayPartner1Name(String name) {
		setColumn(COLUMN_RELAY1_NAME, name);
	}
	
	public void setRelayPartner1Email(String email) {
		setColumn(COLUMN_RELAY1_EMAIL, email);
	}
	
	public void setRelayPartner1ShirtSize(String shirtSize) {
		setColumn(COLUMN_RELAY1_SHIRT_SIZE, shirtSize);
	}
	
	public void setRelayPartner1Leg(String leg) {
		setColumn(COLUMN_RELAY1_LEG, leg);
	}

	public void setRelayPartner2SSN(String ssn) {
		setColumn(COLUMN_RELAY2_PERSONAL_ID, ssn);
	}
	
	public void setRelayPartner2Name(String name) {
		setColumn(COLUMN_RELAY2_NAME, name);
	}
	
	public void setRelayPartner2Email(String email) {
		setColumn(COLUMN_RELAY2_EMAIL, email);
	}
	
	public void setRelayPartner2ShirtSize(String shirtSize) {
		setColumn(COLUMN_RELAY2_SHIRT_SIZE, shirtSize);
	}
	
	public void setRelayPartner2Leg(String leg) {
		setColumn(COLUMN_RELAY2_LEG, leg);
	}

	public void setRelayPartner3SSN(String ssn) {
		setColumn(COLUMN_RELAY3_PERSONAL_ID, ssn);
	}
	
	public void setRelayPartner3Name(String name) {
		setColumn(COLUMN_RELAY3_NAME, name);
	}
	
	public void setRelayPartner3Email(String email) {
		setColumn(COLUMN_RELAY3_EMAIL, email);
	}
	
	public void setRelayPartner3ShirtSize(String shirtSize) {
		setColumn(COLUMN_RELAY3_SHIRT_SIZE, shirtSize);
	}
	
	public void setRelayPartner3Leg(String leg) {
		setColumn(COLUMN_RELAY3_LEG, leg);
	}

	public void setRelayLeg(String leg) {
		setColumn(COLUMN_RELAY_LEG, leg);
	}
	
	/**
	 * this is actually crew name
	 */
	public void setRunGroupName(String runGrName) {
		setColumn(getColumnNameRunGroupName(), runGrName);
	}
	
	public void setCrewInvitedParticipantId(Integer crewInvitedParticipantId) {
		setColumn(getColumnNameCrewInvitedParticipantId(), crewInvitedParticipantId);
	}
	
	public void setCrewInParticipantId(Integer crewInParticipantId) {
		setColumn(getColumnNameCrewInParticipantId(), crewInParticipantId);
	}

	public void setBestTime(String bestTime) {
		setColumn(getColumnNameBestTime(), bestTime);
	}

	public void setGoalTime(String goalTime) {
		setColumn(getColumnNameGoalTime(), goalTime);
	}
	
	public void setParticipantNumber(int participantNumber) {
		setColumn(getColumnNameParticipantNumber(), participantNumber);
	}
	
	public void setPayMethod(String payMethod) {
		setColumn(getColumnNamePayMethod(), payMethod);
	}
	
	public void setPayedAmount(String amount) {
		setColumn(getColumnNameAmountPayed(),amount);
	}
	
	public void setTransportOrdered(String transportOrdered) {
		setColumn(getColumnNameTransportOrdered(),transportOrdered);
	}

	public void setIsDeleted(boolean deleted) {
		setColumn(COLUMN_DELETED, deleted);
	}
	
	public void setCharityId(String charityId) {
		setColumn(getColumnNameCharityId(),charityId);
	}
	
	public String getCharityId() {
		return getStringColumnValue(getColumnNameCharityId());
	}
	
	public boolean getParticipatesInCharity(){
		String charityId = getCharityId();
		if(charityId!=null){
			return true;
		}
		return false;
	}
	
	public void setMaySponsorContact(boolean mayContact) {
		setColumn(getColumnNameMaySponsorContact(),mayContact);
	}
	
	public boolean getMaySponsorContact() {
		return getBooleanColumnValue(getColumnNameMaySponsorContact());
	}
	
	public void setCategoryId(int categoryId) {
		setColumn(getColumnNameCategoryId(),categoryId);
	}
	
	public int getCategoryId() {
		return getIntColumnValue(getColumnNameCategoryId());
	}
	
	public void setAllowsEmails(boolean allowsEmails) {
		setColumn(COLUMN_ALLOWS_EMAIL_FROM_RM, allowsEmails);
	}
	
	public boolean getAllowsEmails() {
		return getBooleanColumnValue(COLUMN_ALLOWS_EMAIL_FROM_RM, false);
	}

	public void setQuestion1Hour(String hour) {
		setColumn(COLUMN_QUESTION1_HOUR, hour);
	}
	
	public String getQuestion1Hour() {
		return getStringColumnValue(COLUMN_QUESTION1_HOUR);
	}

	public void setQuestion1Minute(String minute) {
		setColumn(COLUMN_QUESTION1_MINUTE, minute);
	}
	
	public String getQuestion1Minute() {
		return getStringColumnValue(COLUMN_QUESTION1_MINUTE);
	}
	
	public void setQuestion1Year(String year) {
		setColumn(COLUMN_QUESTION1_YEAR, year);
	}

	public boolean getQuestion1NeverRan() {
		return getBooleanColumnValue(COLUMN_QUESTION1_NEVER_RAN);
	}
	
	public void setQuestion1NeverRan(boolean neverRan) {
		setColumn(COLUMN_QUESTION1_NEVER_RAN, neverRan);
	}

	
	public String getQuestion1Year() {
		return getStringColumnValue(COLUMN_QUESTION1_YEAR);
	}

	public void setQuestion2Hour(String hour) {
		setColumn(COLUMN_QUESTION2_HOUR, hour);
	}
	
	public String getQuestion2Hour() {
		return getStringColumnValue(COLUMN_QUESTION2_HOUR);
	}

	public void setQuestion2Minute(String minute) {
		setColumn(COLUMN_QUESTION2_MINUTE, minute);
	}
	
	public String getQuestion2Minute() {
		return getStringColumnValue(COLUMN_QUESTION2_MINUTE);
	}

	public void setQuestion3Hour(String hour) {
		setColumn(COLUMN_QUESTION3_HOUR, hour);
	}
	
	public String getQuestion3Hour() {
		return getStringColumnValue(COLUMN_QUESTION3_HOUR);
	}

	public void setQuestion3Minute(String minute) {
		setColumn(COLUMN_QUESTION3_MINUTE, minute);
	}
	
	public String getQuestion3Minute() {
		return getStringColumnValue(COLUMN_QUESTION3_MINUTE);
	}
	
	public void setQuestion3Year(String year) {
		setColumn(COLUMN_QUESTION3_YEAR, year);
	}
	
	public String getQuestion3Year() {
		return getStringColumnValue(COLUMN_QUESTION3_YEAR);
	}

	public boolean getQuestion3NeverRan() {
		return getBooleanColumnValue(COLUMN_QUESTION3_NEVER_RAN);
	}
	
	public void setQuestion3NeverRan(boolean neverRan) {
		setColumn(COLUMN_QUESTION3_NEVER_RAN, neverRan);
	}

	public Collection ejbFindAll() throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn());
		
		return idoFindPKsBySQL(query.toString());
	}
	
	public int ejbHomeGetNextAvailableParticipantNumber(Object distancePK, int min, int max) throws IDOException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new MaxColumn(getColumnNameParticipantNumber()));
		query.addCriteria(new MatchCriteria(table, getColumnNameRunDistanceGroupID(), MatchCriteria.EQUALS, distancePK));
		query.addCriteria(new MatchCriteria(table, getColumnNameParticipantNumber(), MatchCriteria.GREATEREQUAL, min));
		query.addCriteria(new MatchCriteria(table, getColumnNameParticipantNumber(), MatchCriteria.LESSEQUAL, max));
		
		return idoGetNumberOfRecords(query.toString());
	}
	
	public int ejbHomeGetNumberOfParticipantsByDistance(Object distancePK, int min, int max) throws IDOException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new CountColumn(getColumnNameParticipantNumber()));
		query.addCriteria(new MatchCriteria(table, getColumnNameRunDistanceGroupID(), MatchCriteria.EQUALS, distancePK));
		query.addCriteria(new MatchCriteria(table, getColumnNameParticipantNumber(), MatchCriteria.GREATEREQUAL, min));
		query.addCriteria(new MatchCriteria(table, getColumnNameParticipantNumber(), MatchCriteria.LESSEQUAL, max));
		
		return idoGetNumberOfRecords(query.toString());
	}
	
	public int ejbHomeGetCountByDistanceAndNumber(Object distancePK, int number) throws IDOException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new CountColumn(getColumnNameParticipantNumber()));
		query.addCriteria(new MatchCriteria(table, getColumnNameRunDistanceGroupID(), MatchCriteria.EQUALS, distancePK));
		query.addCriteria(new MatchCriteria(table, getColumnNameParticipantNumber(), MatchCriteria.EQUALS, number));
		
		return idoGetNumberOfRecords(query.toString());
	}
	
	public int ejbHomeGetCountByDistanceAndGroupName(Object distancePK, String groupName) throws IDOException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		query.addCriteria(new MatchCriteria(table, getColumnNameRunDistanceGroupID(), MatchCriteria.EQUALS, distancePK));
		query.addCriteria(new MatchCriteria(table, getColumnNameRunGroupName(), MatchCriteria.EQUALS, groupName));
		
		return idoGetNumberOfRecords(query);
	}
	
	public Collection ejbFindAllByDistanceAndGroup(Group distance, Group runGroup) throws FinderException {
		Table table = new Table(this);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(getIDColumnName()));
		query.addCriteria(new MatchCriteria(table, getColumnNameRunDistanceGroupID(), MatchCriteria.EQUALS, distance));
		if (runGroup != null) {
			query.addCriteria(new MatchCriteria(table, getColumnNameRunGroupGroupID(), MatchCriteria.EQUALS, runGroup));
		}
		query.addOrder(table, getColumnNameRunTime(), true);
		
		return idoFindPKsBySQL(query.toString());
	}
	
	public Integer ejbFindByUserIDandYearID(int userID, int yearID) throws FinderException{
		IDOQuery query = idoQueryGetSelect();
		query.appendWhereEquals(getColumnNameUserID(),userID);
		query.appendAndEquals(getColumnNameRunYearGroupID(),yearID);
		return (Integer) super.idoFindOnePKByQuery(query);
	}

	public Integer ejbFindByUserIDandDistanceID(int userID, int distanceID) throws FinderException{
		IDOQuery query = idoQueryGetSelect();
		query.appendWhereEquals(getColumnNameUserID(),userID);
		query.appendAndEquals(getColumnNameRunDistanceGroupID(),distanceID);
		return (Integer) super.idoFindOnePKByQuery(query);
	}
	
	public Integer ejbFindByYearAndParticipantNumberAndName(Object yearPK, int participantNumber, String fullName) throws FinderException{
		Table table = new Table(this);
		Table userTable = new Table(User.class);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(getIDColumnName()));
		query.addCriteria(new MatchCriteria(table, getColumnNameRunYearGroupID(), MatchCriteria.EQUALS, yearPK));
		query.addCriteria(new MatchCriteria(table, getColumnNameParticipantNumber(), MatchCriteria.EQUALS, participantNumber));
		query.addCriteria(new JoinCriteria(new Column(table, getColumnNameUserID()), new Column(userTable, User.FIELD_USER_ID)));
		query.addCriteria(new MatchCriteria(userTable, User.FIELD_DISPLAY_NAME, MatchCriteria.EQUALS, fullName));

		return (Integer) super.idoFindOnePKByQuery(query);
	}
	
	public Collection ejbFindByYearAndFullNameOrPersonalIdOrParticipantNumberOrParentGroup(Object yearPK, String searchQuery) throws FinderException{
		
		Table table = new Table(this);
		Table userTable = new Table(User.class);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(getIDColumnName()));
		
		query.addCriteria(new MatchCriteria(table, getColumnNameRunYearGroupID(), MatchCriteria.EQUALS, yearPK));
		query.addCriteria(new JoinCriteria(new Column(table, getColumnNameUserID()), new Column(userTable, User.FIELD_USER_ID)));
		
		OR orCriteria = new OR(new MatchCriteria(userTable, User.FIELD_PERSONAL_ID, MatchCriteria.EQUALS, searchQuery),
				new MatchCriteria(userTable, User.FIELD_DISPLAY_NAME, MatchCriteria.EQUALS, searchQuery));
		try {
			int couldBeParticipantNr = Integer.parseInt(searchQuery);
			orCriteria = new OR(orCriteria, new MatchCriteria(table, getColumnNameParticipantNumber(), MatchCriteria.EQUALS, couldBeParticipantNr));
			
		} catch (NumberFormatException e) { }
		
		query.addCriteria(orCriteria);
		
		return super.idoFindPKsByQuery(query);
	}
	
	public Collection ejbFindByYearAndCrewInOrCrewInvitationParticipantId(Object yearPK, Integer crewParticipantId) throws FinderException{
		Table table = new Table(this);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(getIDColumnName()));
		
		query.addCriteria(new MatchCriteria(table, getColumnNameRunYearGroupID(), MatchCriteria.EQUALS, yearPK));
		
		
		Criteria crewInCriteria = new MatchCriteria(table, getColumnNameCrewInParticipantId(), MatchCriteria.EQUALS, crewParticipantId);
		Criteria crewInvitedCriteria = new MatchCriteria(table, getColumnNameCrewInvitedParticipantId(), MatchCriteria.EQUALS, crewParticipantId);
		query.addCriteria(new OR(crewInCriteria, crewInvitedCriteria));
		
		return super.idoFindPKsByQuery(query);
	}

	public Integer ejbFindByDistanceAndParticipantNumber(Object distancePK, int participantNumber) throws FinderException{
		IDOQuery query = idoQueryGetSelect();
		query.appendWhereEquals(getColumnNameRunDistanceGroupID(), distancePK);
		query.appendAndEquals(getColumnNameParticipantNumber(), participantNumber);
		return (Integer) super.idoFindOnePKByQuery(query);
	}

	public Collection ejbFindByYearAndTeamName(Object yearPK, String teamName) throws FinderException{
		IDOQuery query = idoQueryGetSelect();
		query.appendWhereEquals(getColumnNameRunYearGroupID(), yearPK);
		query.appendAndEqualsQuoted(getColumnNameRunGroupName(), teamName);
		return super.idoFindPKsByQuery(query);
	}

	public Integer ejbFindByUserAndRun(User user, Group run, Group year) throws FinderException{
		IDOQuery query = idoQuery();
		query.appendSelect();
		query.append(getIDColumnName());
		query.appendFrom();
		query.append(this.getEntityName());
		query.appendWhereEquals(getColumnNameUserID(),user);
		query.appendAndEquals(getColumnNameRunTypeGroupID(),run);
		query.appendAndEquals(getColumnNameRunYearGroupID(),year);
		query.appendAnd();
		query.appendLeftParenthesis();
		query.append(COLUMN_DELETED);
		query.appendIsNull();
		query.appendOrEquals(COLUMN_DELETED, false);
		query.appendRightParenthesis();

		return (Integer) super.idoFindOnePKByQuery(query);
	}

	public Integer ejbFindByPartnerAndRun(String partnerPersonalID, Group run, Group year, int partnerNumber) throws FinderException{
		IDOQuery query = idoQuery();
		query.appendSelect();
		query.append(getIDColumnName());
		query.appendFrom();
		query.append(this.getEntityName());
		if (partnerNumber == 1) {
			query.appendWhereEqualsQuoted(COLUMN_RELAY1_PERSONAL_ID, partnerPersonalID);			
		} else if (partnerNumber == 2) {
			query.appendWhereEqualsQuoted(COLUMN_RELAY2_PERSONAL_ID, partnerPersonalID);			
		} else if (partnerNumber == 3) {
			query.appendWhereEqualsQuoted(COLUMN_RELAY3_PERSONAL_ID, partnerPersonalID);			
		} else {
			return null;
		}
		
		query.appendAndEquals(getColumnNameRunTypeGroupID(),run);
		query.appendAndEquals(getColumnNameRunYearGroupID(),year);
				
		return (Integer) super.idoFindOnePKByQuery(query);
	}

	
	public Collection ejbFindByUserAndParentGroup(int userID, int runGroupID, int yearGroupID, int distanceGroupID) throws FinderException{
		IDOQuery query = idoQueryGetSelect();
		query.appendWhereEquals(getColumnNameUserID(),userID);
		query.appendAndEquals(getColumnNameRunTypeGroupID(),runGroupID);
//		query.appendAndEquals(getColumnNameRunYearGroupID(),yearGroupID);
		query.appendAndEquals(getColumnNameRunDistanceGroupID(),distanceGroupID);
		return super.idoFindPKsByQuery(query);
	
	}
	public Collection ejbFindByUserID(int userID) throws FinderException{
		IDOQuery query = idoQueryGetSelect();
		query.appendWhereEquals(getColumnNameUserID(),userID);
		return super.idoFindPKsByQuery(query);
	}
	
	public Collection ejbFindAllWithoutChipNumber(int distanceIDtoIgnore) throws FinderException {
		IDOQuery query = idoQueryGetSelect();
		query.appendWhere().append("("+getColumnNameChipNumber()).appendIsNull().appendOr().append(getColumnNameChipNumber()).append("= '')");
		if (distanceIDtoIgnore != -1) {
			query.appendAnd().append(getColumnNameRunDistanceGroupID()).appendNOTEqual().append(distanceIDtoIgnore);
		}
		return super.idoFindPKsByQuery(query);		
	}
	
	public Collection ejbFindAllByRunGroupIdAndYearGroupId(int runId, int yearId) throws FinderException{
		IDOQuery query = idoQueryGetSelect();
		//query.appendWhereEquals(getColumnNameUserID(),user);
		query.appendWhereEquals(getColumnNameRunTypeGroupID(),runId);
		query.appendAndEquals(getColumnNameRunYearGroupID(),yearId);
		return super.idoFindPKsByQuery(query);
	}
	
	public boolean ejbFindCrewLabelAlreadyExistsForRun(int runId, int yearId, String crewLabel) throws FinderException {
		IDOQuery query = idoQueryGetSelect();
		query.appendWhereEquals(getColumnNameRunTypeGroupID(), runId);
		query.appendAndEquals(getColumnNameRunYearGroupID(), yearId);
		
		Collection existing = super.idoFindPKsByQuery(query);
		return existing != null && !existing.isEmpty();
	}
	
	public Collection ejbFindAllByRunGroupIdAndYear(int runId, int year) throws FinderException{
		
		GroupHome groupHome;
		try {
			groupHome = (GroupHome) getIDOHome(Group.class);
		} catch (IDOLookupException e) {
			throw new RuntimeException(e);
		}
		Group runGroup = groupHome.findByPrimaryKey(new Integer(runId));
		Collection childrenGroups = runGroup.getChildren();
		Integer yearId = new Integer(-1);
		for (Iterator iter = childrenGroups.iterator(); iter.hasNext();) {
			Group childGroup = (Group) iter.next();
			if(childGroup.getName().equals(Integer.toString(year))){
				yearId = (Integer) childGroup.getPrimaryKey();
			}
		}
		
		return ejbFindAllByRunGroupIdAndYearGroupId(runId,yearId.intValue());
	}

	
	public boolean isApplyForDomesticTravelSupport() {
		return getBooleanColumnValue(getColumnApplyForDomesticTravelSupport());
	}

	
	public void setApplyForDomesticTravelSupport(boolean applyForDomesticTravelSupport) {
		setColumn(getColumnApplyForDomesticTravelSupport(), applyForDomesticTravelSupport);
	}

	
	public boolean isApplyForInternationalTravelSupport() {
		return getBooleanColumnValue(getColumnApplyForInternationalTravelSupport());
	}

	public void setApplyForInternationalTravelSupport(boolean applyForInternationalTravelSupport) {
		setColumn(getColumnApplyForInternationalTravelSupport(), applyForInternationalTravelSupport);
	}

	public boolean isSponsoredRunner() {
		return getBooleanColumnValue(getColumnSponsoredRunner());
	}

	public void setSponsoredRunner(boolean sponsoredRunner) {
		setColumn(getColumnSponsoredRunner(), sponsoredRunner);
	}

	public boolean isCustomer() {
		return getBooleanColumnValue(getColumnIsCustomer());
	}

	public void setCustomer(boolean isCustomer) {
		setColumn(getColumnIsCustomer(), isCustomer);
	}
	
	public String getPaymentGroup() {
		return getStringColumnValue(getColumnPaymentGroup());
	}

	public void setPaymentGroup(String paymentGroup) {
		setColumn(getColumnPaymentGroup(), paymentGroup);
	}
}
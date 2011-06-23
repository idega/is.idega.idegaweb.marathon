package is.idega.idegaweb.marathon.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import com.idega.user.data.User;
import javax.ejb.FinderException;
import com.idega.data.IDOException;
import java.util.Collection;
import com.idega.user.data.Group;

public interface ParticipantHome extends IDOHome {
	public Participant create() throws CreateException;

	public Participant findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAll() throws FinderException;

	public int getNextAvailableParticipantNumber(Object distancePK, int min,
			int max) throws IDOException;

	public int getNumberOfParticipantsByDistance(Object distancePK, int min,
			int max) throws IDOException;

	public int getCountByDistanceAndNumber(Object distancePK, int number)
			throws IDOException;

	public int getCountByDistanceAndGroupName(Object distancePK,
			String groupName) throws IDOException;

	public Collection findAllByDistanceAndGroup(Group distance, Group runGroup)
			throws FinderException;

	public Participant findByUserIDandYearID(int userID, int yearID)
			throws FinderException;

	public Participant findByUserIDandDistanceID(int userID, int distanceID)
			throws FinderException;

	public Participant findByYearAndParticipantNumberAndName(Object yearPK,
			int participantNumber, String fullName) throws FinderException;

	public Collection findByYearAndFullNameOrPersonalIdOrParticipantNumberOrParentGroup(
			Object yearPK, String searchQuery) throws FinderException;

	public Collection findByYearAndCrewInOrCrewInvitationParticipantId(
			Object yearPK, Integer crewParticipantId) throws FinderException;

	public Participant findByDistanceAndParticipantNumber(Object distancePK,
			int participantNumber) throws FinderException;

	public Collection findByYearAndTeamName(Object yearPK, String teamName)
			throws FinderException;

	public Participant findByUserAndRun(User user, Group run, Group year,
			boolean alsoDeleted) throws FinderException;

	public Participant findByPartnerAndRun(String partnerPersonalID, Group run,
			Group year, int partnerNumber) throws FinderException;

	public Collection findByUserAndParentGroup(int userID, int runGroupID,
			int yearGroupID, int distanceGroupID) throws FinderException;

	public Collection findByUserID(int userID) throws FinderException;

	public Collection findAllWithoutChipNumber(int distanceIDtoIgnore)
			throws FinderException;

	public Collection findAllByRunGroupIdAndYearGroupId(int runId, int yearId)
			throws FinderException;

	public boolean findCrewLabelAlreadyExistsForRun(int runId, int yearId,
			String crewLabel) throws FinderException;

	public Collection findAllAllowedToRun() throws FinderException;

	public Collection findAllPaidConfirmation() throws FinderException;

	public Collection findAllByRunGroupIdAndYear(int runId, int year)
			throws FinderException;
}